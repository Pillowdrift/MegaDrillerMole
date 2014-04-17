package com.pillowdrift.drillergame.entities;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.pillowdrift.drillergame.entities.enemies.Cloud;
import com.pillowdrift.drillergame.entities.enemies.DemonCat;
import com.pillowdrift.drillergame.entities.enemies.Plane;
import com.pillowdrift.drillergame.entities.enemies.Worm;
import com.pillowdrift.drillergame.framework.GameEntity;
import com.pillowdrift.drillergame.other.DemonCatSpawner;
import com.pillowdrift.drillergame.other.SpawnableManager;
import com.pillowdrift.drillergame.scenes.GameScene;

public class MasterSpawner
	extends GameEntity
{
	/*
	 * Constants
	 */
	
	// Minerals
	public static final float TIME_MINERAL_START_MAX = 3.0f;
	public static final float TIME_MINERAL_START_MIN = 2.0f; 
	public static final float TIME_MINERAL_END_MAX = 2.0f;
	public static final float TIME_MINERAL_END_MIN = 1.0f;
	public static final float TIME_MINERAL_MAX_TIME = 6 * 60;
	
	// Mines
	public static final float TIME_MINE_START_MAX = 5.0f;
	public static final float TIME_MINE_START_MIN = 1.0f;
	public static final float TIME_MINE_END_MAX = 1.8f;
	public static final float TIME_MINE_END_MIN = 0.7f;
	public static final float TIME_MINE_MAX_TIME = 4 * 60;
	
	// Hotrocks
	public static final float TIME_HOTROCK_START_MAX = 20.0f;
	public static final float TIME_HOTROCK_START_MIN = 4.6f;
	public static final float TIME_HOTROCK_END_MAX = 10.0f;
	public static final float TIME_HOTROCK_END_MIN = 3.0f;
	public static final float TIME_HOTROCK_MAX_TIME = 10 * 60;
	
	// Clouds
	public static final float TIME_BIRD_START_MAX = 3.0f;
	public static final float TIME_BIRD_START_MIN = 2.0f;
	public static final float TIME_BIRD_END_MAX = 3.0f;
	public static final float TIME_BIRD_END_MIN = 1.5f;
	public static final float TIME_BIRD_MAX_TIME = 10 * 60;
	
	// Rock
	public static final float TIME_ROCK_START_MAX = 10.0f;
	public static final float TIME_ROCK_START_MIN = 8.7f; 
	public static final float TIME_ROCK_END_MAX = 0.5f;
	public static final float TIME_ROCK_END_MIN = 0.2f;
	public static final float TIME_ROCK_MAX_TIME = 10 * 60;
	
	// Worm
	public static final float TIME_WORM_START_MAX = 80.0f;
	public static final float TIME_WORM_START_MIN = 50.7f; 
	public static final float TIME_WORM_END_MAX = 10.5f;
	public static final float TIME_WORM_END_MIN = 10.2f;
	public static final float TIME_WORM_MAX_TIME = 5 * 60;
	
	// Bomb
	public static final float TIME_BOMB_START_MAX = 30.0f;
	public static final float TIME_BOMB_START_MIN = 26.7f; 
	public static final float TIME_BOMB_END_MAX = 25.0f;
	public static final float TIME_BOMB_END_MIN = 20.0f;
	public static final float TIME_BOMB_MAX_TIME = 10 * 60;
	
	// Cats
	public static final float TIME_CAT_START_MAX = 0.4f;
	public static final float TIME_CAT_START_MIN = 0.3f;
	public static final float TIME_CAT_END_MAX = 0.4f;
	public static final float TIME_CAT_END_MIN = 0.3f;
	public static final float TIME_CAT_MAX_TIME = 10 * 60;
	
	// Plane
	public static final float TIME_PLANE_START_MAX = 300.0f;
	public static final float TIME_PLANE_START_MIN = 280.7f; 
	public static final float TIME_PLANE_END_MAX = 20.5f;
	public static final float TIME_PLANE_END_MIN = 10.2f;
	public static final float TIME_PLANE_MAX_TIME = 10 * 60;
	
	// ROCKVEIN
	public static final float TIME_VEIN_START_MAX = 60.0f;
	public static final float TIME_VEIN_START_MIN = 50.7f; 
	public static final float TIME_VEIN_END_MAX = 3.5f;
	public static final float TIME_VEIN_END_MIN = 3.2f;
	public static final float TIME_VEIN_MAX_TIME = 10 * 60;	
	
	// Create a new master spawner.
	public MasterSpawner(GameScene parent)
	{
		super(parent);
		
		// Create other spawners.
		_parent.addEntity("MineralSpawner", new SpawnableManager(Mineral.class, parent, 28,
																TIME_MINERAL_START_MIN, TIME_MINERAL_START_MAX,
																TIME_MINERAL_END_MIN, TIME_MINERAL_END_MIN,
																TIME_MINERAL_MAX_TIME));
		
		_parent.addEntity("MineSpawner", new SpawnableManager(Mine.class, parent, 10,
																TIME_MINE_START_MIN, TIME_MINE_START_MAX,
																TIME_MINE_END_MIN, TIME_MINE_END_MIN,
																TIME_MINE_MAX_TIME));
		
		_parent.addEntity("HotRockSpawner", new SpawnableManager(Hotrock.class, parent, 8,
																TIME_HOTROCK_START_MIN, TIME_HOTROCK_START_MAX,
																TIME_HOTROCK_END_MIN, TIME_HOTROCK_END_MIN,
																TIME_HOTROCK_MAX_TIME));
		
		_parent.addEntity("CloudSpawner", new SpawnableManager(Cloud.class, parent, 28,
																TIME_BIRD_START_MIN, TIME_BIRD_START_MAX,
																TIME_BIRD_END_MIN, TIME_BIRD_END_MIN,
																TIME_BIRD_MAX_TIME));	
		_parent.addEntity("WormSpawner", new SpawnableManager(Worm.class, parent, 2,
																TIME_WORM_START_MIN, TIME_WORM_START_MAX,
																TIME_WORM_END_MIN, TIME_WORM_END_MIN,
																TIME_WORM_MAX_TIME));
		_parent.addEntity("BombSpawner", new SpawnableManager(Whirlwind.class, parent, 1,
																TIME_BOMB_START_MIN, TIME_BOMB_START_MAX,
																TIME_BOMB_END_MIN, TIME_BOMB_END_MIN,
																TIME_BOMB_MAX_TIME));
		
		_parent.addEntity("PlaneSpawner", new SpawnableManager(Plane.class, parent, 1,
																TIME_PLANE_START_MIN, TIME_PLANE_START_MAX,
																TIME_PLANE_END_MIN, TIME_PLANE_END_MIN,
																TIME_PLANE_MAX_TIME));
		
		_parent.addEntity("VeinSpawner", new SpawnableManager(RockVein.class, parent, 3,
																TIME_VEIN_START_MIN, TIME_VEIN_START_MAX,
																TIME_VEIN_END_MIN, TIME_VEIN_END_MIN,
																TIME_VEIN_MAX_TIME));	
		
		_parent.addEntity("CatSpawner", new DemonCatSpawner(DemonCat.class, parent, 5,
				TIME_CAT_START_MIN, TIME_CAT_START_MAX,
				TIME_CAT_END_MIN, TIME_CAT_END_MIN,
				TIME_CAT_MAX_TIME));
	}
	
	@Override
	public void draw(SpriteBatch spriteBatch)
	{
		// Suppress drawing...
	}
}
