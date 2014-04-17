package com.pillowdrift.drillergame.entities;

import android.view.VelocityTracker;

import com.badlogic.gdx.math.Matrix3;
import com.badlogic.gdx.math.Vector2;
import com.pillowdrift.drillergame.framework.Sprite;
import com.pillowdrift.drillergame.framework.Utils;
import com.pillowdrift.drillergame.other.SpawnableManager;
import com.pillowdrift.drillergame.scenes.GameScene;

/**
 * Managable spawnable which the player must collect to gain load and combo.
 * @author cake_cruncher_7
 *
 */
public class Mineral extends RunningItem
{
	//Type class
	public class MineralType
	{
		public int _pointsValue;
		public String _textureName;
		public int _chance;
		public int _accumChance;
		public float _maxHeight;
		public float _minHeight;
	}
	
	private static final int NUM_MINERALS = 6;
	
	private static final float DAMPING = 0.95f;	
	private static final float GRAVITY = 50.0f;
	
	private float COLLECT_RADIUS = 10.0f;
	private static final float MIN_GRAVITY = 10.0f;
	private static final float TIMER_RATE = 1.5f;

	
	//Static vars
	protected static MineralType[] _mineralTypes;
	protected static boolean _mineralTypesInitialised = false;
	protected static int _totalChance;
	
	private float pickupTimer = 0;
	
	//Vars
	protected int _pointsValue = 100;
	private Player _collectedByPlayer = null;
	
	public Mineral(GameScene parentGameScene, SpawnableManager manager)
	{
		super(parentGameScene, manager);

		//Initialise types
		if(!_mineralTypesInitialised) initialiseTypes();
		
		//Set origin to half texture resolution
		_originX = 16.0f;
		_originY = 16.0f;
		//Set up the texture
		_sprites.addSprite("mineralCopper", new Sprite(_parent.getResourceManager().getAtlasRegion("atlas01", "mineralCopper"), 32, 0.0f));
		_sprites.addSprite("mineralSilver", new Sprite(_parent.getResourceManager().getAtlasRegion("atlas01", "mineralSilver"), 32, 0.0f));
		_sprites.addSprite("mineralGold", new Sprite(_parent.getResourceManager().getAtlasRegion("atlas01", "mineralGold"), 32, 0.0f));
		_sprites.addSprite("mineralDiamond", new Sprite(_parent.getResourceManager().getAtlasRegion("atlas01", "mineralDiamond"), 32, 0.0f));
		_sprites.addSprite("mineralAmethyst", new Sprite(_parent.getResourceManager().getAtlasRegion("atlas01", "mineralAmethyst"), 32, 0.0f));
		_sprites.addSprite("mineralCrystalline", new Sprite(_parent.getResourceManager().getAtlasRegion("atlas01", "mineralCrystalline"), 32, 0.0f));
		_sprites.setCurrentSpriteName("mineralCopper");
	}
	
	//Override player collision
	public void touchPlayer(Player player)
	{	
		if(_collectedByPlayer != null)
			return;
		
		_collectedByPlayer = player;	
		
		//Load points to the player
		_collectedByPlayer.givePoints(_pointsValue);
		
		//Play a sound
		_parent.getResourceManager().getSound("gem").play();
	}
	
	@Override
	public float collisionDistance() 
	{
		// Get the player
		Player player = ((Player)_parent.getEntityFirst("Player"));
		if (player == null)
			return 25.0f;
		
		return 25.0f + player.getActiveDrill().drillSize();
	};
	
	
	@Override
	public void update(float dt) 
	{
		if(_collectedByPlayer != null)
		{	
			
			Vector2 playerDist = new Vector2(_collectedByPlayer.getPosX() - _x, _collectedByPlayer.getPosY() - _y);
			float dist = playerDist.len();			
			if((dist < COLLECT_RADIUS) || (_collectedByPlayer.isInvincible()))
			{		
				super.touchPlayer(_collectedByPlayer);
			}
			else
			{	
				// add the velocity of the player to the position of the mineral
				// this effectively attaches the mineral to the player
				// this is interpolated in using pickupTimer, which linearly increases from 0 to 1
				_x += _collectedByPlayer.getVelocity().x * pickupTimer * dt;
				_y += _collectedByPlayer.getVelocity().y * pickupTimer * dt;
				
				// apply some damping to simulate friction
				_velocity.x *= DAMPING;
				_velocity.y *= DAMPING;		
				
//				playerDist.nor();
//				
//				dist = Math.max(dist, MIN_GRAVITY);
//				
//				//apply a force towards the player, scaled by the distance (becuase playerDist is not normalised)			
//				_velocity.x += playerDist.x * GRAVITY * dist * dt;
//				_velocity.y += playerDist.y * GRAVITY * dist * dt;	
				
//				//apply a force towards the player, scaled by the distance (becuase playerDist is not normalised)					
				_velocity.x += playerDist.x * GRAVITY * dt;
				_velocity.y += playerDist.y * GRAVITY * dt;					
				
				//increase the pickup timer up to a maximum of 1
				pickupTimer = Math.min(1, pickupTimer + TIMER_RATE * dt);
			}			
		}
		
		// Update position
		_x += _velocity.x * dt;
		_y += _velocity.y * dt;
		
		super.update(dt);

	};
	
	@Override
	public void activate()
	{
		super.activate();
		
		//Select random mineral
		int mineralType = getRandomMineral();
		
		_pointsValue = _mineralTypes[mineralType]._pointsValue;
		_sprites.setCurrentSpriteName(_mineralTypes[mineralType]._textureName);
		_x = _parent.getTargetWidth() + 64.0f;
		_y = Utils.randomF(_mineralTypes[mineralType]._minHeight, _mineralTypes[mineralType]._maxHeight);
		_collectedByPlayer = null;
		_velocity.set(0,0);
		pickupTimer = 0;
	}
	
	//Function to initialise different types of mineral
	public void initialiseTypes()
	{
		//Mark types initialised
		_mineralTypesInitialised = true;
		
		float normMineralTop = GameScene.WORLD_SURFACE_HEIGHT - 20.0f;
		float normMineralBottom = GameScene.WORLD_BEDROCK_HEIGHT + 20.0f;
		float normMineralRange = normMineralTop - normMineralBottom;
		
		//Initialise types
		_mineralTypes = new MineralType[NUM_MINERALS];
		//Copper
		_mineralTypes[0] = new MineralType();
		_mineralTypes[0]._pointsValue = 50;
		_mineralTypes[0]._chance = 200;
		_mineralTypes[0]._textureName = "mineralCopper";
		_mineralTypes[0]._maxHeight = normMineralTop;
		_mineralTypes[0]._minHeight = normMineralTop - (normMineralRange*0.5f);
		//Silver
		_mineralTypes[1] = new MineralType();
		_mineralTypes[1]._pointsValue = 100;
		_mineralTypes[1]._chance = 183;
		_mineralTypes[1]._textureName = "mineralSilver";
		_mineralTypes[1]._maxHeight = normMineralTop - (normMineralRange*0.36f);
		_mineralTypes[1]._minHeight = normMineralTop - (normMineralRange*0.75f);
		//Gold
		_mineralTypes[2] = new MineralType();
		_mineralTypes[2]._pointsValue = 150;
		_mineralTypes[2]._chance = 175;
		_mineralTypes[2]._textureName = "mineralGold";
		_mineralTypes[2]._maxHeight = normMineralTop - (normMineralRange*0.685f);
		_mineralTypes[2]._minHeight = normMineralBottom;
		//Diamond
		_mineralTypes[3] = new MineralType();
		_mineralTypes[3]._pointsValue = 500;
		_mineralTypes[3]._chance = 70;
		_mineralTypes[3]._textureName = "mineralDiamond";
		_mineralTypes[3]._maxHeight = GameScene.WORLD_BEDROCK_HEIGHT - 68.0f;
		_mineralTypes[3]._minHeight = GameScene.WORLD_BEDROCK_HEIGHT - 100.0f;
		//Amethyst
		_mineralTypes[4] = new MineralType();
		_mineralTypes[4]._pointsValue = 500;
		_mineralTypes[4]._chance = 5;
		_mineralTypes[4]._textureName = "mineralAmethyst";
		_mineralTypes[4]._maxHeight = normMineralTop;
		_mineralTypes[4]._minHeight = normMineralBottom;
		//Crystalline
		_mineralTypes[5] = new MineralType();
		_mineralTypes[5]._pointsValue = 1500;
		_mineralTypes[5]._chance = 1;
		_mineralTypes[5]._textureName = "mineralCrystalline";
		_mineralTypes[5]._maxHeight = GameScene.WORLD_BEDROCK_HEIGHT - 125.0f;
		_mineralTypes[5]._minHeight = GameScene.WORLD_BEDROCK_HEIGHT - 135.0f;
		
		//Initialise chances
		_totalChance = 0;
		for(int i = 0; i < _mineralTypes.length; ++i)
		{
			_mineralTypes[i]._accumChance = _mineralTypes[i]._chance + _totalChance;
			_totalChance = _mineralTypes[i]._accumChance;
		}
	}
	
	public int getRandomMineral()
	{
		//Get random integer
		int randWeight = Utils.randomI(1, _totalChance);
		
		//Find mineral
		int totalMinerals = _mineralTypes.length;
		for(int i = 0; i < totalMinerals; ++i)
		{
			if(randWeight <= _mineralTypes[i]._accumChance)
			{
				//Return mineral index
				return i;
			}
		}
		
		return -5;
	}
}
