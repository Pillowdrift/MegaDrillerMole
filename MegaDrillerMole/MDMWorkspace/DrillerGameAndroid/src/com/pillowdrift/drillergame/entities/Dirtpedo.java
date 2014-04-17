package com.pillowdrift.drillergame.entities;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.pillowdrift.drillergame.emitters.PlayerExplosionEmitter;
import com.pillowdrift.drillergame.framework.GameEntity;
import com.pillowdrift.drillergame.framework.Sprite;
import com.pillowdrift.drillergame.scenes.GameScene;

public class Dirtpedo
	extends GameEntity
{
	public static final float TERMINAL_VELOCITY = 250.0f;
	public static final float MIN_LIFE_SPAN = 2.0f;
	public static final float MAX_LIFE_SPAN = 4.0f;
	
	private float _life;

	private Rectangle _boundingBox;
	
	// Emitters
	private PlayerExplosionEmitter _explosion;
	
	public Dirtpedo(GameScene parent, float x, float y)
	{
		super(parent);
		
		// Create the emitter
		_explosion = new PlayerExplosionEmitter(parent.getParticlePool01(), parent, 3);
		
		// Add texture
		_sprites.addSprite("dirtpedo", new Sprite(_parent.getResourceManager().getAtlasRegion("atlas01", "dirtpedo"), 64, 1.0f));
		_sprites.setCurrentSpriteName("dirtpedo");
		
		_scaleX = _scaleY = 0.75f;
		
		// Set origin to centre of texture
		_originX = _sprites.getCurrentSprite().getWidth() / 2.0f;
		_originY = _sprites.getCurrentSprite().getHeight() / 2.0f;
		
		// Set depth to be on top
		_depth = -12;
		
		// Set initial position
		_x = x;
		_y = y;
		
		// Construct bounding rectangle
		_boundingBox = new Rectangle();
		_boundingBox.set(_x - _originX, _y - _originY, _sprites.getCurrentSprite().getWidth()*0.45f, _sprites.getCurrentSprite().getHeight()*0.45f);
		
		_life = MathUtils.random() * (MAX_LIFE_SPAN - MIN_LIFE_SPAN) + MIN_LIFE_SPAN;
	}

	@Override
	public void update(float dt)
	{
		super.update(dt);
		
		//Advance with ground
		_x += _parentGameScene.ALL_VELOCITY_X * dt;		
		_rotation = _velocity.angle();
		
		// Update bounding rectangle
		_boundingBox.x = _x - _originX;
		_boundingBox.y = _y - _originY;
		
		// Check player collision (rectangular)
		Player player = (Player)_parent.getEntityFirst("Player");
		if (player != null)
		{
			// If player has collided with this
			Rectangle boundingRect = player.getBoundingbox();
			if (boundingRect.overlaps(_boundingBox))
			{
				// Blow up.
				_explosion.emit(_x, _y);
				_parent.getResourceManager().getSound("dirtpedo").play();
				
				// Hurt player and destroy this, ending this update
				player.hurt();
				remove();
				return;
			}
		}
		
		if (_life > 0)
		{
			_life -= dt;
		
			if (_life <= 0)
			{
				remove();
				
				// Blow up
				_explosion.emit(_x, _y);
				_parent.getResourceManager().getSound("dirtpedo").play();
			}
		}
		
		// Accelerate due to gravity
		_velocity.y += GameScene.WORLD_GRAVITY_POWER * dt;
		
		if (_velocity.y < -TERMINAL_VELOCITY)
			_velocity.y = -TERMINAL_VELOCITY;
		
		// Update position
		_x += _velocity.x * dt;
		_y += _velocity.y * dt;
	}
	
	@Override
	public void onGameReset() {
		remove();
	}
}
