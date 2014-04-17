package com.pillowdrift.drillergame.entities;

import com.badlogic.gdx.math.Vector2;
import com.pillowdrift.drillergame.emitters.DemonCatBreathTrailEmitter;
import com.pillowdrift.drillergame.emitters.PlayerFireEmitter;
import com.pillowdrift.drillergame.framework.GameEntity;
import com.pillowdrift.drillergame.framework.Sprite;
import com.pillowdrift.drillergame.framework.Utils;
import com.pillowdrift.drillergame.other.SpawnableManager;
import com.pillowdrift.drillergame.scenes.GameScene;

public class DemonCatBreath extends GameEntity
{
	//Constants
	public static final float ROTATE_SPEED_MIN = -2.8f;
	public static final float ROTATE_SPEED_MAX = 2.8f;
	public static final float MOVE_SPEED = 2.7f;
	public static final float COLLISIONDISTANCE = 15.0f;
	public static final float LIFESPAN = 3.4f;
	
	//Data
	DemonCatBreathTrailEmitter _trailEmitter;
	float _rotateSpeed;
	float _dx = 0.0f;
	float _dy = 0.0f;
	float _lifeAccum = 0.0f;
	
	//Constant
	public DemonCatBreath(GameScene parent, float x, float y)
	{
		super(parent);
		
		//Add texture
		_sprites.addSprite("idle", new Sprite(_parent.getResourceManager().getAtlasRegion("atlas01", "demonCatBreath"), 32, 0.0f));
		_sprites.setCurrentSpriteName("idle");
		
		//Set origin
		_originX = 16.0f;
		_originY = 16.0f;
		
		//Set scale
		_scaleX = 1.0f;
		_scaleY = 1.0f;
		
		//Set depth
		_depth = -11;
		
		//Set rotation
		_rotation = Utils.randomF(0.0f, 360.0f);
		_rotateSpeed = Utils.randomF(ROTATE_SPEED_MIN, ROTATE_SPEED_MAX);
		
		//Set position
		_x = x;
		_y = y;
		
		//Get player
		Player player = (Player)_parent.getEntityFirst("Player");
		//Set velocity
		Vector2 vel = player.getPosV2();
		vel.sub(getPosV2());
		vel.nor();
		vel.mul(MOVE_SPEED);
		_dx = vel.x;
		_dy = vel.y;
		
		//Set up particle emitters
		_trailEmitter = new DemonCatBreathTrailEmitter(parent.getParticlePool01(), parent);
	}

	//Function
	@Override
	public void update(float dt)
	{
		super.update(dt);
		
		//Time to remove yet?
		if((_lifeAccum += dt) > LIFESPAN)
		{
			remove();
			return;
		}
		
		//Update rotation
		_rotation += _rotateSpeed;
		
		//Update position
		_x += _dx;
		_y += _dy;
		
		//Update emitter
		_trailEmitter.update(dt, _x, _y);
		
		//Check range with player for collision
		Player player = (Player)_parent.getEntityFirst("Player");
		if(player != null)
		{
			Vector2 diffToPlayer = new Vector2(player.getPosX() - _x, player.getPosY() - _y);
			if(diffToPlayer.len() < COLLISIONDISTANCE)
			{
				//Nuke multiplier and carried points
				player.hurt();
				//Remove self
				remove();
			}
		}
	}
	
	@Override
	public void onGameReset()
	{
		super.onGameReset();
		
		//Remove self
		remove();
	}
}
