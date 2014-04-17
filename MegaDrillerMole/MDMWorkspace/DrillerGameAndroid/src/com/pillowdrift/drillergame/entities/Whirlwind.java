package com.pillowdrift.drillergame.entities;

import com.pillowdrift.drillergame.framework.Sprite;
import com.pillowdrift.drillergame.framework.Utils;
import com.pillowdrift.drillergame.other.SpawnableManager;
import com.pillowdrift.drillergame.scenes.GameScene;

/**
 * A bomb that destroys all items on the screen
 * @author strawberries
 *
 */
public class Whirlwind extends RunningItem
{
	private static final float SURFACE_CLEARANCE = 100;

	public Whirlwind(GameScene parent, SpawnableManager manager)
	{
		super(parent, manager);
		
		//Set origin to half texture resolution
		_originX = 16.0f;
		_originY = 16.0f;
		
		//Set up the texture
		_sprites.addSprite("Worldwind", new Sprite(_parent.getResourceManager().getAtlasRegion("atlas01", "worldwind"), 32, 0.0f));
		_sprites.setCurrentSpriteName("Worldwind");
	}

	//Override player collision
	@Override
	public void touchPlayer(Player player)
	{		
		_parentGameScene.bomb(_x, _y);
		_parent.getResourceManager().getSound("whirlwind").play();
		
		//Call default
		super.touchPlayer(player);
	}

	@Override
	public void activate()
	{
		super.activate();
		
		_x = _parent.getTargetWidth() + 64.0f;
		_y = Utils.randomF(GameScene.WORLD_BEDROCK_HEIGHT + 20.0f, GameScene.WORLD_SURFACE_HEIGHT - 20.0f);
		
		if (_y < GameScene.WORLD_SURFACE_HEIGHT - SURFACE_CLEARANCE)
		{
			_y = GameScene.WORLD_SURFACE_HEIGHT - SURFACE_CLEARANCE;
		}
	}
	
	@Override
	public void update(float dt)
	{
		super.update(dt);
		_rotation += 300.0f * dt;
	}
}
