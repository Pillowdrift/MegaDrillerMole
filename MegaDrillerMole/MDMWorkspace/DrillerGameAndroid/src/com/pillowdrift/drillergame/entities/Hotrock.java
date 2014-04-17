package com.pillowdrift.drillergame.entities;

import com.pillowdrift.drillergame.framework.Sprite;
import com.pillowdrift.drillergame.framework.Utils;
import com.pillowdrift.drillergame.other.SpawnableManager;
import com.pillowdrift.drillergame.scenes.GameScene;

/**
 * Entity which provides the player with a certain amount of boost fuel when collected.
 * @author cake_cruncher_7
 *
 */
public class Hotrock extends RunningItem
{

	protected float _fuelValue = 50.0f;
	
	public Hotrock(GameScene parent, SpawnableManager manager)
	{
		super(parent, manager);

		//Set origin to half texture resolution
		_originX = 16.0f;
		_originY = 16.0f;
		//Set up the texture
		_sprites.addSprite("idle", new Sprite(_parent.getResourceManager().getAtlasRegion("atlas01", "hotrock"), 32, 3.0f));
		_sprites.setCurrentSpriteName("idle");
	}

	//Override player collision
	@Override
	public void touchPlayer(Player player)
	{
		//Add this rock to the player's fuel resources
		player.giveFuel(_fuelValue);
		
		//Call default
		super.touchPlayer(player);
	}

	//Override positioning
	@Override
	public void activate()
	{
		super.activate();
		_x = _parent.getTargetWidth() + 64.0f;
		_y = Utils.randomF(GameScene.WORLD_BEDROCK_HEIGHT + 20.0f, GameScene.WORLD_BEDROCK_HEIGHT + 60.0f);
	}
}
