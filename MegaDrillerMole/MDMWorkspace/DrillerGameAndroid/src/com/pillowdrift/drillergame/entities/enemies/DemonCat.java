package com.pillowdrift.drillergame.entities.enemies;

import com.badlogic.gdx.math.Vector2;
import com.pillowdrift.drillergame.entities.DemonCatBreath;
import com.pillowdrift.drillergame.entities.Player;
import com.pillowdrift.drillergame.framework.Sprite;
import com.pillowdrift.drillergame.framework.Utils;
import com.pillowdrift.drillergame.other.DemonCatSpawner;
import com.pillowdrift.drillergame.other.SpawnableManager;
import com.pillowdrift.drillergame.scenes.GameScene;

public class DemonCat extends Enemy
{
	//Constants
	//Spawn
	public static final float CAT_SPAWNTIME = 0.77f;
	public static final float CAT_DEATHTIME = 0.77f;
	
	//Attack
	public static final float CAT_ATTACKDELAY_MIN = 2.7f;
	public static final float CAT_ATTACKDELAY_MAX = 8.0f;
	public static final float CAT_ATTACKTIME_SPIT = 1.22f;
	public static final float CAT_ATTACKTIME_STOP = 1.88f;
	public static final float CAT_SHOOTFROM_X = -5.0f;
	public static final float CAT_SHOOTFROM_Y = 68.0f;
	//Walkspeed
	public static final float CAT_WALKSPEED_MIN = 35.0f;
	public static final float CAT_WALKSPEED_MAX = 76.0f;
	//Bounds
	private static final float BOUND_DISTANCE = 20.0f;
	private final float BOUND_RIGHT;
	private final float BOUND_LEFT;
	private final float BOUND_TOP;
	private final float BOUND_BOTTOM;
	//Walk direction enumeration
	public static final int CAT_WALKDIR_LEFT = 0;
	public static final int CAT_WALKDIR_RIGHT = 1;
	//State enumeration
	public static final int CAT_STATE_SPAWN = 0;
	public static final int CAT_STATE_WALK = 1;
	public static final int CAT_STATE_ATTACK = 2;
	public static final int CAT_STATE_DESPAWN = 3;
	
	//Data
	int _state = 0;
	int _walkDir = 0;
	float _walkSpeed = 0.0f;
	float _spawnAccum = 0.0f;
	float _attackDelayTime = 0.0f;
	float _attackDelayAccum = 0.0f;
	float _attackAccum = 0.0f;
	boolean _canSpit = false;
	float _despawnAccum = 0.0f;
	float _despawnHeight = 0.0f;
	
	//Player reference - should be renewed every now and again
	Player _player;
	
	//Construction
	public DemonCat(GameScene parent, SpawnableManager manager)
	{
		super(parent, manager);
		
		//Set boundaries
		BOUND_RIGHT = _parent.getTargetWidth() - BOUND_DISTANCE;
		BOUND_LEFT = 0 + BOUND_DISTANCE;
		BOUND_TOP = GameScene.WORLD_BEDROCK_VISIBILITY + 80.0f;
		BOUND_BOTTOM = GameScene.WORLD_BEDROCK_VISIBILITY + 6.0f;
		
		//Load sprites
		_sprites.addSprite("spawn", new Sprite(_parent.getResourceManager().getAtlasRegion("atlas01", "demonCatSpawn"), 64, 9.0f));
		_sprites.addSprite("despawn", new Sprite(_parent.getResourceManager().getAtlasRegion("atlas01", "demonCatDeath"), 64, 9.0f));
		_sprites.addSprite("idle", new Sprite(_parent.getResourceManager().getAtlasRegion("atlas01", "demonCatIdle"), 64, 9.0f));
		_sprites.addSprite("attack", new Sprite(_parent.getResourceManager().getAtlasRegion("atlas01", "demonCatAttack"), 64, 2.5f));
		_sprites.setCurrentSpriteName("idle");
		
		//Set origin
		_originX = 32.0f;
		_originY = 0.0f;
		
		//Set depth
		_depth = -10;
		
		//Calculate despawn height
		_despawnHeight = GameScene.WORLD_BEDROCK_HEIGHT + (DemonCatSpawner.SPAWN_HEIGHT_OFFSET*2.0f);
	}
	
	//Operation
	@Override
	public void update(float dt)
	{
		//Suppress super - it does stupid things at the moment
		//super.update(dt);
		
		//Renew player reference
		//Get a reference to the player
		_player = (Player)_parent.getEntityFirst("Player");
		
		//Advance sprite manually - NEEDS TO BE REMOVED IF SUPER IS FIXED AND UNCOMMENTED
		_sprites.update(dt);
		
		//Face the direction you're going
		if(_walkDir == CAT_WALKDIR_LEFT)
		{
			_scaleX = 1.0f;
		}
		else if (_walkDir == CAT_WALKDIR_RIGHT)
		{
			_scaleX = -1.0f;
		}
		
		//Select state
		switch (_state)
		{
		case CAT_STATE_SPAWN:
			updateSpawning(dt);
			break;
		case CAT_STATE_DESPAWN:
			updateDespawning(dt);
			break;
		case CAT_STATE_WALK:
			updateWalking(dt);
			break;
		case CAT_STATE_ATTACK:
			updateAttacking(dt);
			break;
		default:
			//Do nothing
		}
	}
	
	//State functions
	public void updateSpawning(float dt)
	{
		//Animate
		_sprites.setCurrentSpriteName("spawn");
		if((_spawnAccum += dt) >= CAT_SPAWNTIME)
		{
			_sprites.setCurrentSpriteName("idle");
			_state = CAT_STATE_WALK;
		}
	}
	
	public void updateDespawning(float dt)
	{
		//Animate
		_sprites.setCurrentSpriteName("despawn");
		if((_despawnAccum += dt) >= CAT_DEATHTIME)
		{
			remove();
		}
	}
	
	public void updateWalking(float dt)
	{
		//Animate
		_sprites.setCurrentSpriteName("idle");
		//Act based on walking direction
		if(_walkDir == CAT_WALKDIR_LEFT)
		{
			//Walk
			_x -= _walkSpeed*dt;
			//Check bounds and reverse direction
			if(_x < BOUND_LEFT)
			{
				_x = BOUND_LEFT;
				_walkDir = CAT_WALKDIR_RIGHT;
			}
		}
		else if(_walkDir == CAT_WALKDIR_RIGHT)
		{
			//Walk
			_x += _walkSpeed*dt;
			//Check bounds and reverse direction
			if(_x > BOUND_RIGHT)
			{
				_x = BOUND_RIGHT;
				_walkDir = CAT_WALKDIR_LEFT;
			}
		}
		
		//Shall we attack?
		if((_attackDelayAccum += dt) > _attackDelayTime)
		{
			//Select the next delay time
			_attackDelayAccum = 0.0f;
			_canSpit = true;
			_attackDelayTime = Utils.randomF(CAT_ATTACKDELAY_MIN, CAT_ATTACKDELAY_MAX);
			
			//Randomise walk speed
			_walkSpeed = Utils.randomF(CAT_WALKSPEED_MIN, CAT_WALKSPEED_MAX);
			
			//Set to attack state
			_attackAccum = 0.0f;
			_state = CAT_STATE_ATTACK;
		}
	}
	
	public void updateAttacking(float dt)
	{
		//Animate
		_sprites.setCurrentSpriteName("attack");
		
		if(_player != null)
		{
			//Get player position
			Vector2 playerPos = _player.getPosV2();
			
			//Face the player
			if(playerPos.x >= _x)
			{
				_walkDir = CAT_WALKDIR_RIGHT;
			}
			else
			{
				_walkDir = CAT_WALKDIR_LEFT;
			}
			
			//Accumulate attack time
			_attackAccum += dt;
			if(_attackAccum >= CAT_ATTACKTIME_SPIT)
			{
				//Attack if not done yet
				if(_canSpit)
				{
					//Mark as having spit already
					_canSpit = false;
					//Shoot a fireball at the player
					_parent.addEntity("enemyBullet", new DemonCatBreath(_parentGameScene,
																		_x + ((_walkDir == CAT_WALKDIR_LEFT)? CAT_SHOOTFROM_X : -CAT_SHOOTFROM_X),
																		_y + CAT_SHOOTFROM_Y));
					_parent.getResourceManager().getSound("fireball").play();
				}
			}
			if(_attackAccum >= CAT_ATTACKTIME_STOP)
			{
				if(playerPos.y > _despawnHeight)
				{
					kill();
				}
				else
				{
					//Go back to walking
					_state = CAT_STATE_WALK;
				}
			}
		}
		else
		{
			//Go back to walking
			_state = CAT_STATE_WALK;
		}
	}

	//Function
	@Override
	public void activate()
	{
		super.activate();
		
		//Set to spawning state
		_state = CAT_STATE_SPAWN;
		//Set to spawning animation
		_sprites.setCurrentSpriteName("spawn");
		
		//Zero accumulated spawn time
		_spawnAccum = 0.0f;
		
		//Set first attack delay
		_attackDelayAccum = 0.0f;
		_attackDelayTime = Utils.randomF(CAT_ATTACKDELAY_MIN, CAT_ATTACKDELAY_MAX);
		
		//Set walking direction
		_walkDir = Utils.randomI(CAT_WALKDIR_LEFT, CAT_WALKDIR_RIGHT);
		//Set walking speed
		_walkSpeed = Utils.randomF(CAT_WALKSPEED_MIN, CAT_WALKSPEED_MAX);
		
		//Set random position
		_x = Utils.randomF(BOUND_LEFT, BOUND_RIGHT);
		_y = Utils.randomF(BOUND_BOTTOM, BOUND_TOP);
		
		// Play the entry sound
		_parent.getResourceManager().getSound("demoncatentry").play();
	}
	
	void kill()
	{
		//Die
		_state = CAT_STATE_DESPAWN;
		_despawnAccum = 0.0f;
	}
}
