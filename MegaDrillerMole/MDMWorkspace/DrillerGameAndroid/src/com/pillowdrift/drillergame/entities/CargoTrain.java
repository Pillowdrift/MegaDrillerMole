package com.pillowdrift.drillergame.entities;

import com.badlogic.gdx.math.Vector2;
import com.pillowdrift.drillergame.framework.GameEntity;
import com.pillowdrift.drillergame.framework.Sprite;
import com.pillowdrift.drillergame.scenes.GameScene;

/**
 * The train to which the player must take his load to score points.
 * @author cake_cruncher_7
 *
 */
public class CargoTrain extends GameEntity
{
	//CONSTANTS
	public static final float POINT_COLLECTION_RANGE_X_MIN = -300.0f;
	public static final float POINT_COLLECTION_RANGE_X_MAX = 300.0f;
	public static final float POINT_COLLECTION_RANGE_Y_MIN = 100.0f;
	//DATA
	private boolean playerInRange = false;
	
	//CONSTRUCTION
	public CargoTrain(GameScene parent) {
		super(parent);
		
		//Get graphics
		_sprites.addSprite("drive", new Sprite(parent.getResourceManager().getAtlasRegion("atlas01", "trainDrive"), 128, 10.0f));
		_sprites.setCurrentSpriteName("drive");
		
		//Set origin
		_originX = _sprites.getCurrentSprite().getWidth()/2.0f;
		_originY = 0.0f;
		
		//Set position
		_x = parent.getTargetWidth()/7.0f*3.0f;
		_y = GameScene.WORLD_SURFACE_HEIGHT;
		_depth = 40.0f;
	}
	
	//FUNCTION
	@Override
	public void update(float dt)
	{
		super.update(dt);
		
		// Get the player.
		Player player = (Player)_parent.getEntityFirst("Player");	
		
		if(player != null)
		{
			// Get the position and velocity of the player.
			Vector2 playerPos = player.getPosV2();
			Vector2 vel = player.getVelocity();	
			
			//Collect points from the player when they're in the right position
			if(_parentGameScene.getPlayer01Load() > 0.0f)
			{
				
				// If we are falling (eg as high as possible)
				if(playerPos.y > _y + POINT_COLLECTION_RANGE_Y_MIN)
				{
					if (vel.y < 0 && playerInRange || (playerInRange && !((playerPos.x < _x + POINT_COLLECTION_RANGE_X_MAX) &&
							   		  									 (playerPos.x > _x + POINT_COLLECTION_RANGE_X_MIN)))) 
					{
						//Player is within the required range!
						//Calculate height bonus:
						//How many hundreds high are we? //Lets pretend every 70.0f is 100, to make the scores more interesting
						int heightBonus = (int)(Math.floor((playerPos.y - _y)/70.0f)*100.0f);
						//Transfer points:
						player.transferLoad(heightBonus);
					}
				}
			}
			
			// Get whether or not the player is in range.
			playerInRange = (playerPos.x < _x + POINT_COLLECTION_RANGE_X_MAX) &&
	 						(playerPos.x > _x + POINT_COLLECTION_RANGE_X_MIN);
			
		}
	}
}
