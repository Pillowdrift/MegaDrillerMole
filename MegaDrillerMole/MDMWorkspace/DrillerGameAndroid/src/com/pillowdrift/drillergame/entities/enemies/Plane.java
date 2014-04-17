package com.pillowdrift.drillergame.entities.enemies;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.pillowdrift.drillergame.emitters.PlaneFireEmitter;
import com.pillowdrift.drillergame.emitters.PlaneSmokeEmitter;
import com.pillowdrift.drillergame.emitters.PlayerExplosionEmitter;
import com.pillowdrift.drillergame.entities.Dirtpedo;
import com.pillowdrift.drillergame.entities.Player;
import com.pillowdrift.drillergame.framework.Sprite;
import com.pillowdrift.drillergame.framework.Utils;
import com.pillowdrift.drillergame.other.SpawnableManager;
import com.pillowdrift.drillergame.scenes.GameScene;

public class Plane
	extends Enemy 
{
	private final static float MIN_HEIGHT = 150.0f;
	private final static float MAX_HEIGHT = 500.0f;
	private final static float MAX_SPEED = -100.0f;
	private final static float DIRTPEDO_DELAY = 2.0f;
	private final static int MAX_DIRTPEDO = 2;
	
	private float _nextDirtpedo = DIRTPEDO_DELAY;
	private int _ammo = MAX_DIRTPEDO;
	
	private boolean _crashing = false;
	
	private Vector2 _velocity;

	private Rectangle _boundingBox;
	
	private PlaneFireEmitter _fireEmitter;
	private PlaneSmokeEmitter _smokeEmitter;
	private PlayerExplosionEmitter _explosion;
	
	
	/**
	 * Create a new instance of a cloud
	 * @param parent
	 * @param manager
	 */
	public Plane(GameScene parent, SpawnableManager manager)
	{
		super(parent, manager);
		
		// Create the emitter.
		_fireEmitter = new PlaneFireEmitter(parent.getParticlePool01(), parent);
		_smokeEmitter = new PlaneSmokeEmitter(parent.getParticlePool01(), parent);
		_explosion = new PlayerExplosionEmitter(parent.getParticlePool01(), parent, 5);
		
		// Set sprites
		_sprites.addSprite("Plane", new Sprite(parent.getResourceManager().getAtlasRegion("atlas01", "plane"), 128, 0));
		_sprites.setCurrentSpriteName("Plane");
		
		// Set initial Velocity
		_velocity = new Vector2(MathUtils.random() * MAX_SPEED, 0);
		
		// Set offset to centre of sprite
		_originX = _sprites.getCurrentSprite().getWidth() / 2.0f;
		_originY = _sprites.getCurrentSprite().getHeight() / 2.0f;
		
		// Tint red
		//_color = Color.RED;
		
		// Construct bounding rectangle
		_boundingBox = new Rectangle();
		_boundingBox.set(_x - _originX, _y - _originY, _sprites.getCurrentSprite().getWidth()*0.9f, _sprites.getCurrentSprite().getHeight()*0.9f);
	}
	
	@Override
	public float collisionDistance() 
	{
		return 0;
	}
	
	@Override
	public void update(float dt) 
	{
		super.update(dt);
		
		// Update bounding rectangle
		_boundingBox.x = _x - _originX;
		_boundingBox.y = _y - _originY;
		
		if (!_crashing)
		{
			// Check player collision (rectangular)
			Player player = (Player)_parent.getEntityFirst("Player");
			if (player != null)
			{
				Rectangle boundingRect = player.getBoundingbox();
				if (boundingRect.overlaps(_boundingBox))
				{
					// If player isn't hitting from above
					if (player.getVelocity().y > 0)
					{
						// Hurt them
						player.hurt();
					}
					else
					{
						// Boost them
						player.setVelocity(player.getVelocity().add(0, 400));
					}
					
					// Fall either way					
					_crashing = true;
					return;
				}
			}
			
			// Count down to next dirtpedo launch
			if (_nextDirtpedo > 0.0f)
			{
				_nextDirtpedo -= dt;
			
				if (_nextDirtpedo <= 0.0f)
				{
					// Reset timer and reduce ammo
					_nextDirtpedo = DIRTPEDO_DELAY;
					_ammo--;
					
					// Launch dirtpedo if ammo remains
					if (_ammo >= 0)
					{
						_parent.addEntity("dirtpedo", new Dirtpedo(_parentGameScene, _x, _y));
					}
				}
			}
		}
		else
		{
			// We're falling
			// Emit fire particles.
			_fireEmitter.update(dt, _x, _y);
			_fireEmitter.emit();
			_smokeEmitter.update(dt, _x, _y);
			_smokeEmitter.emit();
			
			_velocity.y += dt * GameScene.WORLD_GRAVITY_POWER;
			
			if (_y < GameScene.WORLD_SURFACE_HEIGHT)
			{
				// Exploderize!
				_explosion.emit(_x, _y);
				_parent.getResourceManager().getSound("explosion").play();
				
				remove();
			}
		}
		
		// Update position
		_x += _velocity.x * dt;
		_y += _velocity.y * dt;
	}
	
	@Override
	public void activate()
	{
		super.activate();
		
		_x = _parent.getTargetWidth() + 64.0f;
		
		// We're not falling
		_crashing = false;
		_velocity = new Vector2(MathUtils.random() * MAX_SPEED, 0);
		
		// Reset ammo
		_ammo = MAX_DIRTPEDO;
		
		// Generate a random position.
		_y = Utils.randomF(GameScene.WORLD_SURFACE_HEIGHT + MIN_HEIGHT,
						   GameScene.WORLD_SURFACE_HEIGHT + MAX_HEIGHT);
	}
	
	@Override
	public void touchPlayer(Player player)
	{

	}
	
	@Override
	public void onGameReset()
	{
		_nextDirtpedo = DIRTPEDO_DELAY;
		_ammo = MAX_DIRTPEDO;
	}
}
