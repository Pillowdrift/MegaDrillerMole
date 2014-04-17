package com.pillowdrift.drillergame.entities.enemies;

import com.badlogic.gdx.math.Vector2;
import com.pillowdrift.drillergame.entities.Player;
import com.pillowdrift.drillergame.framework.Sprite;
import com.pillowdrift.drillergame.framework.Utils;
import com.pillowdrift.drillergame.other.SpawnableManager;
import com.pillowdrift.drillergame.scenes.GameScene;

public class Cloud extends Enemy 
{
	// Min and Max heights for the clouds to spawn.
	private final static float MIN_HEIGHT = 100.0f;
	private final static float MAX_HEIGHT = 2000.0f;
	private final static float PLAYER_ACC = 200.0f;
	
	/**
	 * Create a new instance of a cloud
	 * @param parent
	 * @param manager
	 */
	public Cloud(GameScene parent, SpawnableManager manager)
	{
		super(parent, manager);
		
		_sprites.addSprite("Cloud", new Sprite(parent.getResourceManager().getAtlasRegion("atlas01", "cloud2"), 64, 0));
		_sprites.setCurrentSpriteName("Cloud");
		
		_originX = 32;
		_originY = 32;
	}
	
	@Override
	public float collisionDistance() 
	{
		// TODO Auto-generated method stub
		return 32;//_sprites.getCurrentSprite().getWidth();
	}
	
	@Override
	public void update(float dt) 
	{
		// TODO Auto-generated method stub
		super.update(dt);
	}
	
	@Override
	public void activate()
	{
		super.activate();
		
		_x = _parent.getTargetWidth() + 64.0f;
		
		// Generate a random position.
		_y = Utils.randomF(GameScene.WORLD_SURFACE_HEIGHT + MIN_HEIGHT,
						   GameScene.WORLD_SURFACE_HEIGHT + MAX_HEIGHT);
	}
	
	@Override
	public void touchPlayer(Player player)
	{
		super.touchPlayer(player);
		
		// Increase the players velocity upwards
		Vector2 vel = player.getVelocity();
		vel.y += PLAYER_ACC;
		player.setVelocity(vel);
	}
}
