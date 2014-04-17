package com.pillowdrift.drillergame.entities;

import com.badlogic.gdx.math.Vector2;
import com.pillowdrift.drillergame.other.SpawnableManager;
import com.pillowdrift.drillergame.scenes.GameScene;

/**
 * A class which expands ManagableSpawnable to form the basis of many collectables.
 * @author cake_cruncher_7
 *
 */
public class RunningItem
	extends ManagableSpawnable
{

	public RunningItem(GameScene parent, SpawnableManager manager)
	{
		super(parent, manager);
		
		_depth = 10;
	}
	
	@Override
	public void update(float dt)
	{
		super.update(dt);
		
		//Advance with ground
		_x += _parentGameScene.ALL_VELOCITY_X * dt;
		
		//Check left bound
		if(_x < -64.0f)
		{
			remove();
		}
		
		//Check collision with player
		Player player = (Player)_parent.getEntityFirst("Player");
		if(player != null)
		{
			Vector2 diffToPlayer = new Vector2(player.getPosX() - _x, player.getPosY() - _y);
			if(diffToPlayer.len() < collisionDistance())
			{
				touchPlayer(player);
			}
		}
	}
	
	//Distance from the player at which we count a collision
	public float collisionDistance()
	{
		return 20.0f;
	}
	
	//Function called upon collision with player
	public void touchPlayer(Player player)
	{
		//By default just remove self.
		remove();
	}
}
