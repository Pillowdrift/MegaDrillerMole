package com.pillowdrift.drillergame.entities.enemies;

import com.pillowdrift.drillergame.entities.RunningItem;
import com.pillowdrift.drillergame.other.SpawnableManager;
import com.pillowdrift.drillergame.scenes.GameScene;

public class Enemy extends RunningItem
{
	/**
	 * Create a new enemy.
	 * @param parent
	 * @param manager
	 */
	public Enemy(GameScene parent, SpawnableManager manager)
	{
		super(parent, manager);
	}
}
