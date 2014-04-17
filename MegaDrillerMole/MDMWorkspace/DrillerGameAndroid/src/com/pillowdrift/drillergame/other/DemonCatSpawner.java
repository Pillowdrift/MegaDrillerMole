package com.pillowdrift.drillergame.other;

import com.pillowdrift.drillergame.entities.ManagableSpawnable;
import com.pillowdrift.drillergame.entities.Player;
import com.pillowdrift.drillergame.scenes.GameScene;

public class DemonCatSpawner extends SpawnableManager
{
	//Constants
	public static final float SPAWN_HEIGHT_OFFSET = 5.0f;
	
	//Data
	float _spawnHeight = 0.0f;		//The height beneath which demon cats will spawn
	
	//Construction
	public DemonCatSpawner(Class<? extends ManagableSpawnable> spawnableClass,
						   GameScene parentGameScene, int numToGenerate,
						   float minMaxTimeToSpawn, float maxMaxTimeToSpawn,
						   float minMinTimeToSpawn, float maxMinTimeToSpawn, float maxTime)
	{
		//Super constructor
		super(spawnableClass, parentGameScene, numToGenerate, minMaxTimeToSpawn,
				maxMaxTimeToSpawn, minMinTimeToSpawn, maxMinTimeToSpawn, maxTime);
		
		//Get spawn height
		_spawnHeight = GameScene.WORLD_BEDROCK_HEIGHT + SPAWN_HEIGHT_OFFSET;
	}
	
	//Function
	@Override
	public void update(float dt)
	{
		//Get player
		Player player = (Player)_parent.getEntityFirst("Player");
		
		if(player != null)
		{
			if(player.getPosY() < _spawnHeight)
			super.update(dt);
		}
	}

}
