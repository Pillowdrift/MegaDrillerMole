package com.pillowdrift.drillergame.entities;

import com.pillowdrift.drillergame.framework.GameEntity;
import com.pillowdrift.drillergame.other.SpawnableManager;
import com.pillowdrift.drillergame.scenes.GameScene;

/**
 * Base class for entities which can be managed by a SpawnableManager for
 * efficient and controllable spawning during the game's execution.
 * @author cake_cruncher_7
 *
 */
public class ManagableSpawnable extends GameEntity
{
	//DATA
	SpawnableManager _spawnableManager;

	//CONSTRUCTION
	public ManagableSpawnable(GameScene parent, SpawnableManager manager)
	{
		super(parent);
		
		_spawnableManager = manager;
	}
	
	//Function called whenever the instance is handed out by it's manager
	public void activate()
	{
		_removeFlag = false;
	}

	//Override onRemove to return the spawnable to it's manager
	public void onRemove()
	{
		_spawnableManager.giveSpawnable(this);
	} 
	
	@Override
	public void onGameReset() {
		super.onGameReset();
		
		// Kill ourselves =(
		_removeFlag = true;
	}
}
