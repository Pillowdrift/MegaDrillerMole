package com.pillowdrift.drillergame.entities;

import com.pillowdrift.drillergame.framework.Sprite;
import com.pillowdrift.drillergame.framework.Utils;
import com.pillowdrift.drillergame.other.SpawnableManager;
import com.pillowdrift.drillergame.scenes.GameScene;

/**
 * ManagableSpawnable representing a mine which harms the player and destroys his load and combo
 * @author cake_cruncher_7
 *
 */
public class Mine extends RunningItem
{
	private static final float SURFACE_CLEARANCE = 100;

	public Mine(GameScene parentGameScene, SpawnableManager manager)
	{
		super(parentGameScene, manager);

		//Set origin to half texture resolution
		_originX = 16.0f;
		_originY = 16.0f;
		//Set up the texture
		_sprites.addSprite("idle", new Sprite(_parent.getResourceManager().getAtlasRegion("atlas01", "mine01"), 32, 0.0f));
		_sprites.setCurrentSpriteName("idle");
	}
	
	//Distance from the player at which we count a collision
	@Override
	public float collisionDistance()
	{
		return 29.0f;
	}
	
	//Override player collision
	public void touchPlayer(Player player)
	{
		//Slow the player down
		player.setVelocity(player.getVelocity().mul(0.5f));
		
		//Nuke multiplier and carried points
		player.hurt();
		
		//Call default
		super.touchPlayer(player);
	}
	
	
	@Override
	public void activate()
	{
		super.activate();
		
		_x = _parent.getTargetWidth() + 64.0f;
		_y = Utils.randomF(GameScene.WORLD_BEDROCK_HEIGHT + 20.0f, GameScene.WORLD_SURFACE_HEIGHT - SURFACE_CLEARANCE);
	}
}
