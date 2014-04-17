package com.pillowdrift.drillergame.entities;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.pillowdrift.drillergame.framework.Sprite;
import com.pillowdrift.drillergame.framework.Utils;
import com.pillowdrift.drillergame.other.SpawnableManager;
import com.pillowdrift.drillergame.scenes.GameScene;

/**
 * 
 * @author strawberries
 *
 */
public class InpenetrableRock extends RunningItem {
	
	private float size = 0.0f;

	public InpenetrableRock(GameScene parent, SpawnableManager manager) {
		super(parent, manager);
		//Set origin to half texture resolution
		_originX = 16.0f;
		_originY = 16.0f;
		//Set up the texture
		_sprites.addSprite("rock", new Sprite(_parent.getResourceManager().getAtlasRegion("atlas01", "rock"), 32, 0.0f));
		_sprites.setCurrentSpriteName("rock");
		//Set the scale
		
		//Get a random number between 1 and 3
		float scale = MathUtils.random(1, 3);
		_scaleX = scale;
		_scaleY = scale;
		
		//set collision radius
		size = scale * 15.0f;
	}

	//Distance from the player at which we count a collision
	@Override
	public float collisionDistance()
	{
		return size;
	}
		
	//Function called upon collision with player
	@Override
	public void touchPlayer(Player player)
	{
		Vector2 direction = player.getVelocity().nor();
		Vector2 currentPos = player.getPosV2();
		player.setPosX(currentPos.x - (direction.x * 10.0f) - 5);
		player.setPosY(currentPos.y - (direction.y * 10.0f) - 5);
	}
	
	@Override
	public void activate()
	{
		super.activate();
		_x = _parent.getTargetWidth() + 64.0f;
		_y = Utils.randomF(GameScene.WORLD_BEDROCK_HEIGHT + 20.0f, GameScene.WORLD_SURFACE_HEIGHT - 20.0f);
	}
}
