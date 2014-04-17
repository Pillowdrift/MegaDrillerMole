package com.pillowdrift.drillergame.entities;

import com.pillowdrift.drillergame.emitters.WorldEmberEmitter;
import com.pillowdrift.drillergame.framework.GameEntity;
import com.pillowdrift.drillergame.scenes.GameScene;

/**
 * Entity which will control the flow of gameplay by dynamically spawning and despawning other entities.
 * @author cake_cruncher_7
 *
 */
public class GameplayController extends GameEntity
{
	//World effects
	WorldEmberEmitter _emberEmitter;		//Emitter to spawn embers near the depths of hell
	
	public GameplayController(GameScene parent)
	{
		super(parent);
		
		_depth = 150.0f;

		// Initialize effects
		_emberEmitter = new WorldEmberEmitter(parent.getParticlePool01(), parent);
		
		// Create the master spawner.
		_parent.addEntity("MasterSpawner", new MasterSpawner(parent));
	}

	@Override
	public void update(float dt)
	{
		//Update effects
		_emberEmitter.update(dt);
	}
}
