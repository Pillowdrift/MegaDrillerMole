package com.pillowdrift.drillergame.entities.enemies;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.pillowdrift.drillergame.emitters.WormExplosionEmitter;
import com.pillowdrift.drillergame.entities.DigTrail;
import com.pillowdrift.drillergame.entities.Player;
import com.pillowdrift.drillergame.framework.Sprite;
import com.pillowdrift.drillergame.framework.Utils;
import com.pillowdrift.drillergame.other.SpawnableManager;
import com.pillowdrift.drillergame.scenes.GameScene;

public class Worm extends Enemy 
{
	// Constants
	private static final int WORM_LENGTH = 5;
	private static final float WORM_SPEED = 200;
	private static final float WORM_HEAD_DELAY = 5;
	private static final float WORM_PART_DISTANCE = 25.0f;
	private static final float WORM_DIRECTIONAL_DELAY = 2;
	private static final float WORM_RANDOM_MOVEMENT_DISTANCE = 1000.0f;
	
	// This worm's velocity
	protected Vector2 _targetVelocity = new Vector2();
	
	// The worm's movement target
	protected Vector2 _target = new Vector2();
	
	// The spawn manager for this type
	private SpawnableManager _manager;
	
	// The parent and child parts (a worm is basically a linked list)
	private Worm _parentWorm = null;
	private Worm _childWorm = null;
	
	// Whether this worm is a head
	private boolean _isHead = false;
	private boolean _isOriginalHead = false;
	
	// The time until this worm becomes a head
	private float _timeUntilHead = getHeadDelay();
	private float _timeUntilDirectionChange = getMovementDelay();
	
	// How long it takes the worm to explode.
	boolean _dead = false;
	private float _explosionTime = 0.4f;
	
	// A particle emitter for the explosion.
	WormExplosionEmitter _emitter;
	
	// Not used.
	//DigTrail _trail;
	
	/**
	 * Create a new worm
	 * @param parent
	 * @param manager
	 */
	public Worm(GameScene parent, SpawnableManager manager)
	{
		super(parent, manager);
		
		this._manager = manager;
		
		// Create the trail for the head
		//_trail = new DigTrail(parent, this);
		//_parent.addEntity("DigTrail", _trail);

		this._originX = 16;
		this._originY = 16;
		
		// Create the emitter
		_emitter = new WormExplosionEmitter(parent.getParticlePool01(), parent);
		
		// This is an original head 
		_isOriginalHead = true;
	}
	
	/**
	 * Create a new part of a worm
	 * @param parent
	 * @param manager
	 * @param parentWorm
	 */
	public Worm(GameScene parent, SpawnableManager manager, Worm parentWorm)
	{
		super(parent, manager);
		
		this._manager = null;
		this._parentWorm = parentWorm;

		init();
		
		// Create the trail for the body
		//_trail = new DigTrail(parent, this);
		//_parent.addEntity("DigTrail", _trail);

		this._originX = 16;
		this._originY = 16;
		
		// Create the emitter
		_emitter = new WormExplosionEmitter(parent.getParticlePool01(), parent);	
	}
	
	/**
	 * Make this worm segment exploderize.
	 */
	public void blowUp() {
		_dead = true;
		if (_childWorm != null) {
			_childWorm._parentWorm = null;
			_childWorm = null;
		}
		if (_parentWorm != null) {
			_parentWorm._childWorm = null;
			_parentWorm = null;
		}
	}
	
	@Override
	public void onRemove() {
		if (_parentWorm != null)
			_parentWorm._childWorm = null;
		if (_childWorm != null)
			_childWorm._parentWorm = null;
		
		if (_isOriginalHead)
			super.onRemove();
	}
	
	private void init()
	{
		_sprites.addSprite("head", new Sprite(_parent.getResourceManager().getAtlasRegion("atlas01", "wormhead"), 40, 0.0f));
		_sprites.addSprite("body", new Sprite(_parent.getResourceManager().getAtlasRegion("atlas01", "wormsegment"), 32, 3.0f));
		_sprites.setCurrentSpriteName("body");
		this._depth = -10;
	}
	
	public void create(int parts)
	{
		// Exclude the head
		if (parts > 1)
		{
			_childWorm = new Worm((GameScene)_parent, _manager, this);
			_childWorm.create(parts-1);
			_childWorm.setPos(_x, _y);

			_parent.addEntity("wormchild", _childWorm);
		}
	}
	
	@Override
	public float collisionDistance() 
	{
		return _sprites.getCurrentSprite().getWidth();
	}
	
	@Override
	public void update(float dt) 
	{
		super.update(dt);
		
		// Remove any dead parents or children.
		if (_parentWorm != null)
			if (_parentWorm._dead)
				_parentWorm = null;
		if (_childWorm != null)
			if (_childWorm._dead)
				_childWorm = null;
		
		// If  this section is dead, then update the emitter.
		if (_dead) {
			_emitter.update(dt, _x, _y);
			_emitter.emit();
			
			// Reduce the timer
			_explosionTime -= dt;
			if (_explosionTime < 0) {
				_removeFlag = true;
				_explosionTime = 0.4f;
			}
		}
		
		if (!_isHead && _parentWorm == null)
		{
			_timeUntilHead -= dt;
			
			// If there's no parent, turn this into a head after WORM_HEAD_DELAY
			if (_timeUntilHead <= 0)
			{
				_timeUntilHead = getHeadDelay();
				setHead();
			}
		}
		else if (_isHead)
		{
			// Change direction if enough time has elapsed
			_timeUntilDirectionChange -= dt;
			if (_timeUntilDirectionChange <= 0)
			{
				updateTarget();
				
				_timeUntilDirectionChange = getMovementDelay();
			}
		}
		
		// If this head only has one child, then kill it
		if (_parentWorm == null && _childWorm == null)
			blowUp();
		else if (_parentWorm == null && _childWorm != null && _childWorm._childWorm == null) {
			_childWorm.blowUp();
			blowUp();
		}	
		
		// If this is a head, lead the worm to a player (or a random position if player is dead)
		if (_isHead)
		{			
			float targetDistanceSquared;
			
			// Update velocity
			_targetVelocity.set(_target.x - _x, _target.y - _y);
			targetDistanceSquared = _targetVelocity.len2();
			
			_targetVelocity.nor();
			_targetVelocity.mul(WORM_SPEED);
				
			// Interpolate towards target velocity for smooth movement
			_velocity.lerp(_targetVelocity, dt);
			
			// If distance to target is less than the velocity, update movement target
			if (targetDistanceSquared < _velocity.len2())
			{
				updateTarget();
				
				_timeUntilDirectionChange = getMovementDelay();
			}

			// Move in direction of velocity
			_x += _velocity.x * dt;
			_y += _velocity.y * dt;
		}
		else if (_parentWorm != null)
		{
			// Follow behind parent worm
			_velocity.set(_parentWorm._x - _x, _parentWorm._y - _y);
			_velocity.nor();
			_velocity.mul(WORM_PART_DISTANCE);
			
			_x = _parentWorm._x - _velocity.x;
			_y = _parentWorm._y - _velocity.y;
		}
		
		// Rotate the section.
		_rotation = _velocity.angle();
	}
	
	@Override
	public void activate()
	{
		super.activate();
		
		_x = _parent.getTargetWidth() + 64.0f;
		_y = Utils.randomF(GameScene.WORLD_BEDROCK_HEIGHT + 20.0f, GameScene.WORLD_SURFACE_HEIGHT - 20.0f);

		_removeFlag = false;
		_dead = false;
		
		init();
		setHead();
		create(getLength());
	}	
	
	@Override
	public void touchPlayer(Player player)
	{
		// Slow the player.
		player.setVelocity(player.getVelocity().nor().mul(player.getVelocity().len() * 0.1f));
		
		// If we aren't already dead
		if (_dead)
			return;
		
		// If this is a child
		if (!_isHead)
		{
			// Detatch from parent
			if (_parentWorm != null)
				_parentWorm._childWorm = null;
		}
		else if (_childWorm != null && !player.isBoosting())
		{
			// Otherwise, if this has at least two parts, kill the player
			player.hurt();
		}
		
		// If this is a parent
		if (_childWorm != null)
		{
			// Detatch from child
			_childWorm._parentWorm = null;
		}
		
		_parentWorm = null;
		_childWorm = null;
		
		// Kill trail
		//_trail.remove();
		
		// Set ourselves to be dead.
		_dead = true;
		
		// Play a sound..
		_parent.getResourceManager().getSound("wormexplosion").play();
	}
	
	@Override
	public void onGameReset()
	{
		super.onGameReset();
	}
	
	/**
	 * The delay until a headless worm body becomes a head
	 * Override to change this behaviour
	 * 
	 * @return delay in seconds
	 */
	public float getHeadDelay()
	{
		return WORM_HEAD_DELAY;
	}
	
	/**
	 * Called when this worm body part becomes a head
	 */
	public void setHead()
	{
		_isHead = true;
		_sprites.setCurrentSpriteName("head");
	}
	
	/**
	 * Length of the worm
	 * Override to change the length of the worm in a derived class
	 * 
	 * @return worm length (number of bodyparts)
	 */
	public int getLength()
	{
		return WORM_LENGTH;
	}
	
	/**
	 * Delay between updating the movement target of the worm
	 * Override to change the movement behaviour of this worm
	 * 
	 * @return delay in seconds
	 */
	public float getMovementDelay()
	{
		return WORM_DIRECTIONAL_DELAY;
	}
	
	/**
	 * Updates the movement target of this worm
	 * Override to change the movement behaviour
	 */
	public void updateTarget()
	{
		Player player = (Player)_parent.getEntityFirst("Player");
		
		// If the player is alive, move to its position
		if (player != null && player.isAlive() && player.getPosY() < GameScene.WORLD_SURFACE_HEIGHT)
		{
			_target.x = player.getPosX();
			_target.y = player.getPosY();
		}
		else
		{
			// Move in a random direction
			_target.x = _x + (MathUtils.random() - 0.5f) * WORM_RANDOM_MOVEMENT_DISTANCE;
			_target.y = _y + (MathUtils.random() - 0.5f) * WORM_RANDOM_MOVEMENT_DISTANCE;
			
			if (_target.y > GameScene.WORLD_SURFACE_HEIGHT)
				_target.y = GameScene.WORLD_SURFACE_HEIGHT;
		}
	}
}
