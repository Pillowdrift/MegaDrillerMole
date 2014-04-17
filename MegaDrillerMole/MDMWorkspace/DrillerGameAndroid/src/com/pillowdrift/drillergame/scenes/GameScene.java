package com.pillowdrift.drillergame.scenes;

import java.util.List;
import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.pillowdrift.drillergame.data.DataCache;
import com.pillowdrift.drillergame.entities.BedrockWorldPiece;
import com.pillowdrift.drillergame.entities.CargoTrain;
import com.pillowdrift.drillergame.entities.EarthWorldPiece;
import com.pillowdrift.drillergame.entities.GameplayController;
import com.pillowdrift.drillergame.entities.Mine;
import com.pillowdrift.drillergame.entities.Mineral;
import com.pillowdrift.drillergame.entities.MountainWorldPiece;
import com.pillowdrift.drillergame.entities.Player;
import com.pillowdrift.drillergame.entities.SkyWorldPiece;
import com.pillowdrift.drillergame.entities.enemies.Worm;
import com.pillowdrift.drillergame.framework.Entity;
import com.pillowdrift.drillergame.framework.GameEntity;
import com.pillowdrift.drillergame.framework.Scene;
import com.pillowdrift.drillergame.framework.SceneManager;
import com.pillowdrift.drillergame.framework.Utils;
import com.pillowdrift.drillergame.framework.particles.Pool;

/**
 * Main gameplay scene.
 * Adds the necessary entities for gameplay processing, provides functions for controlling the game state,
 * maintains a camera, and a collection of gameplay constants.
 * @author cake_cruncher_7
 *
 */
public class GameScene extends Scene
{
	//DATA
	//World constants
	public static final float STARTING_ALL_VELOCITY_X = -60.0f;
	public static final float MAX_ALL_VELOCITY_X = -180.0f;
	public static final float ACC_ALL_VELOCITY_X = 0.2f;
	public static final float WORLD_BEDROCK_HEIGHT = -300;
	public static final float WORLD_BEDROCK_VISIBILITY = WORLD_BEDROCK_HEIGHT - 240;
	public static final float WORLD_SURFACE_HEIGHT = 400.0f;
	public static final float WORLD_MOUNTAIN_HEIGHT = 443.0f;
	public static final float WORLD_ATMOSPHERIC_HEIGHT = 720;
	public static final float WORLD_GRAVITY_POWER = -250.0f;
	public static final float WORLD_GROUND_FRICTION = 9.0f;
	public static final float WORLD_AIR_FRICTION = 0.24f;
	public static final float CAMERA_SIT_HEIGHT = 445.0f;		//The height at which the camera sits when there is no player
	
	//Gameplay constants
	public static final int PLAYER_MAX_LIFE = 3;
	public static final int PLAYER_MAX_COMBO = 0;
	
	// Bomb effect
	private static final float BOMB_TIME_MAX = 0.2f;
	private static final float BOMB_PUSH_POWER = 10;
	
	//Tiles
	public static final float NUM_BACKGROUND_COLUMNS = 10;
	public static final float NUM_BACKGROUND_MOUNTAINS = 5;
	
	//World variables
	public float ALL_VELOCITY_X = STARTING_ALL_VELOCITY_X;
	private float _globalXOffset = 0;
	public float getGlobalXOffset()
	{
		return _globalXOffset;
	}
	
	//Camera
	protected OrthographicCamera _camera;
	protected Vector3 _camCntr;
	protected Vector3 _camOffset;
	protected float _halfTargetWidth;
	protected float _halfTargetHeight;
	
	//Particles
	protected Pool _particlePool01;
	
	//Stats
	protected final HUDScene _hud;
	protected long _player01Score;
	protected long _player01Load;
	protected int _player01Combo;
	protected int _player01Life;
	protected long _topHighScore;
	
	//Running state
	protected boolean _gameRunning = false;
	protected boolean _gameResetRequest = false;
	protected boolean _gamePaused = false;
	protected float _gameTime = 0.0f; // How long the game has been running for.
	
	// Bomb effect
	protected Vector2 _bombPos = new Vector2();
	protected float _bombTime = 0;

	
	//ACCESS
	public float getGameTime()
	{
		return _gameTime;
	}
	public float getHalfTargetWidth()
	{
		return _halfTargetWidth;
	}
	public float getHalfTargetHeight()
	{
		return _halfTargetHeight;
	}
	public Pool getParticlePool01()
	{
		return _particlePool01;
	}
	public long getPlayer01Score()
	{
		return _player01Score;
	}
	public long getPlayer01Load()
	{
		return _player01Load;
	}
	public int getPlayer01Combo()
	{
		return _player01Combo;
	}
	public int getPlayer01Life()
	{
		return _player01Life;
	}
	public long getTopHighScore()
	{
		return _topHighScore;
	}
	public void setPlayer01Score(long score)
	{
		_player01Score = score;
		_hud.updateScore(_player01Score);
		//If the score is more than the high score, update this on the display too
		if(_player01Score > _topHighScore)
			_hud.updateHighScore(_player01Score);
	}
	public void adjustPlayer01Score(long scoreAdj)
	{
		_player01Score += scoreAdj;
		_hud.updateScore(_player01Score);
		//If the score is more than the high score, update this on the display too
		if(_player01Score > _topHighScore)
			_hud.updateHighScore(_player01Score);
	}
	public void setPlayer01Load(long load)
	{
		_player01Load = load;
		_hud.updateLoad(_player01Load);
	}
	public void adjustPlayer01Load(long load)
	{
		_player01Load += load*_player01Combo;
		_hud.updateLoad(_player01Load);
	}
	public void setPlayer01Combo(int combo)
	{
		_player01Combo = combo;
		if((PLAYER_MAX_COMBO != 0) &&
		   (_player01Combo > PLAYER_MAX_COMBO))
		{
			_player01Combo = PLAYER_MAX_COMBO;
		}
	}
	public void adjustPlayer01Combo(int combo)
	{
		_player01Combo += combo;
		if((PLAYER_MAX_COMBO != 0) &&
		   (_player01Combo > PLAYER_MAX_COMBO))
		{
			_player01Combo = PLAYER_MAX_COMBO;
		}
	}
	/**
	 * Set's player01's life
	 * @param life
	 * @return true if this is a killing blow
	 */
	public boolean setPlayer01Life(int life)
	{
		_player01Life = life;
		if(_player01Life > PLAYER_MAX_LIFE)
		{
			_player01Life = PLAYER_MAX_LIFE;
		}
		_hud.updateLife(_player01Life);
		//Is it dead?
		return (_player01Life <= 0);
	}
	/**
	 * Adjust's player01's life
	 * @param life
	 * @return true if this is a killing blow
	 */
	public boolean adjustPlayer01Life(int life)
	{
		_player01Life += life;
		if(_player01Life > PLAYER_MAX_LIFE)
		{
			_player01Life = PLAYER_MAX_LIFE;
		}
		_hud.updateLife(_player01Life);
		//Is it dead?
		return (_player01Life <= 0);
	}
	
	//CONSTRUCTION
	public GameScene(SceneManager owner)
	{
		super(owner);
		
		// Catch back button so we handle it manually
		Gdx.input.setCatchBackKey(true);
		
		//Set depth
		_depth = 70;
		
		//Store HUD reference
		_owner.addScene("HUDScene", new HUDScene(_owner));
		_hud = (HUDScene)_owner.getScene("HUDScene");
		_hud.activate();
		
		//Add entities
		//Particle system
		_particlePool01 = (Pool)addEntity("ParticlePool", new Pool(this, 600, 33, WORLD_GRAVITY_POWER));
		_particlePool01.setDepth(7.0f);
		//Drop off point
		addEntity("Train", new CargoTrain(this));
		//Game controller
		addEntity("GameplayController", new GameplayController(this));
		
		//Add backdrop
		for(int i = 0; i < NUM_BACKGROUND_COLUMNS; ++i)
		{
			addEntity("BedrockWall", new BedrockWorldPiece(this, i, NUM_BACKGROUND_COLUMNS, WORLD_BEDROCK_HEIGHT));
			addEntity("DirtyWall", new EarthWorldPiece(this, i, NUM_BACKGROUND_COLUMNS, WORLD_SURFACE_HEIGHT));
			addEntity("SkyWall", new SkyWorldPiece(this, i, NUM_BACKGROUND_COLUMNS, WORLD_ATMOSPHERIC_HEIGHT));
		}
		for(int i = 0; i < NUM_BACKGROUND_MOUNTAINS; ++i)
		{
			addEntity("MountainWall", new MountainWorldPiece(this, i, NUM_BACKGROUND_MOUNTAINS, WORLD_MOUNTAIN_HEIGHT));
		}

		//Set camera centre such that 0.0, 0.0 is the bottom left
		_camCntr = new Vector3(_targetWidth/2.0f, 0.0f/*_targetHeight/2.0f*/, 0.0f);
		//Set camera offset to zero
		_camOffset = new Vector3(0.0f, 0.0f, 0.0f);
		//Set orthographic projection matrix
		_camera = new OrthographicCamera(_targetWidth, _targetHeight);
		//Set half sizes
		_halfTargetWidth = _targetWidth/2.0f;
		_halfTargetHeight = _targetHeight/2.0f;
		//Set camera starting height
		_camOffset.y = CAMERA_SIT_HEIGHT;
		
		//Start the particle system running
		_particlePool01.startThread();
		
		// Not active by default
		_active = false;
		update(0);
	}
	
	/**
	 * Signals to all entities that we are about to reset the
	 * game, allowing them to remove or reset themselves as appropriate.
	 */
	public void requestResetGame()
	{
		//Set game state as not running and in need of resetting
		_gameRunning = false;
		_gameResetRequest  = true;
		_gamePaused = false;
		
		ALL_VELOCITY_X = STARTING_ALL_VELOCITY_X;
		
		//Run through all entities and tell them we're resetting
		for(Map.Entry<String, List<Entity>> le : _entities.entrySet())
		{
			//Loop through each name
			List<Entity> lev = le.getValue();
			for(int e = 0; e < lev.size(); ++e)
			{
				//Loop through each entity with this name
				GameEntity curEntity = Utils.as(GameEntity.class, lev.get(e));
				if(curEntity != null)
				{
					curEntity.onGameReset();
				}
			}
		}
		
		// Reset the game timer.
		_gameTime = 0.0f;
	}
	
	/**
	 * Removes all mines, enemies and gems on screen
	 */
	public void removeOnScreen()
	{
		//Loop through entities
		for(Map.Entry<String, List<Entity>> le : _entities.entrySet())
		{
			//Loop through list
			List<Entity> lev = le.getValue();
			for(int e = 0; e < lev.size(); ++e)
			{
				//Remove minerals
				GameEntity curEntity = Utils.as(Mineral.class, lev.get(e));
				if(curEntity != null)
				{
					curEntity.onGameReset();
				}
				
				//Remove mines
				curEntity = Utils.as(Mine.class, lev.get(e));
				if(curEntity != null)
				{
					curEntity.onGameReset();
				}
				
				//Remove worms
				curEntity = Utils.as(Worm.class, lev.get(e));
				if(curEntity != null)
				{
					curEntity.onGameReset();
				}
			}
		}
	}
	
	/**
	 * Signals re-entry of the game from the user
	 */
	public void requestResumeGame()
	{
		_gamePaused = false;
		_gameRunning = true;
		unpauseEntities();
	}
	
	/**
	 * Function called one update after requesting the game be reset.
	 * We can now add all the stuff for the new run safely.
	 */
	protected void resetGame()
	{
		//Set game state as running and reset
		_gameRunning = true;
		_gameResetRequest = false;
		
		_globalXOffset = 0.0f;
		
		//Initialize scores
		_player01Score = 0;
		_hud.updateScore(_player01Score);
		_player01Load = 0;
		_hud.updateLoad(_player01Load);
		_player01Combo = 0;
		_topHighScore = DataCache.getBestScore();
		_hud.updateHighScore(_topHighScore);
		_player01Life = PLAYER_MAX_LIFE;
		_hud.updateLife(_player01Life);
		
		//Add player
		addEntity("Player", new Player(this));
	}
	
	/**
	 * Function to terminate the game due to player destruction
	 */
	public void endGame()
	{
		//Remove the player
		getEntityFirst("Player").remove();
		
		//Activate the retry scene
		_owner.getScene("HighRetryScene").activate();
	}
	
	@Override
	public void update(float dt)
	{
		if (!_gamePaused)
		{
			super.update(dt);
			// Increment the game time.
			_gameTime += dt;
			
			// Increase the offset and speed.
			_globalXOffset += ALL_VELOCITY_X * dt;
			if (ALL_VELOCITY_X > MAX_ALL_VELOCITY_X) {
				ALL_VELOCITY_X -= ACC_ALL_VELOCITY_X * dt;
			}
			
			//Reset now if we have been asked to.
			if(_gameResetRequest)
			{
				resetGame();
			}
			
			//Adjust the camera offset to follow the player
			Entity player = getEntityFirst("Player");
			if(player != null)
			{
				_camOffset.set(0.0f, player.getPosY(), 0.0f);
			}
			else
			{
				if(_camOffset.y < CAMERA_SIT_HEIGHT)
					++_camOffset.y;
				if(_camOffset.y > CAMERA_SIT_HEIGHT)
					--_camOffset.y;
			}
			
			//Ensure the camera stays within it's limits
			float cameraBoundOverlap;
			//Top
			//cameraBoundOverlap = WORLD_ATMOSPHERIC_HEIGHT - (_camOffset.y + _halfTargetHeight);
			//if(cameraBoundOverlap < 0.0f) _camOffset.y += cameraBoundOverlap;
			//Bottom
			cameraBoundOverlap = WORLD_BEDROCK_VISIBILITY - (_camOffset.y - _halfTargetHeight);
			if(cameraBoundOverlap > 0.0f) _camOffset.y += cameraBoundOverlap;
			
			//Check for menu or back press
			if((getInputManager().isMenuPressed() || getInputManager().isBackPressed()) && getEntityFirst("Player") != null)
			{
				//Activate the menu scene
				//getOwner().getScene("MenuScene").activate();
				//Deactivate our parent scene
				
				//((Player)getEntityFirst("Player")).instaKill();
				_gamePaused = true;
				_gameRunning = false;
				_owner.getScene("PauseScene").activate();
				
				// "Pause" all of the entities.
				pauseEntities();
			}
			
			// Do bomb effect
			if (_bombTime > 0)
			{
				bombUpdate(dt);
			}
		}
	}
	
	@Override
	public void draw()
	{
		_camera.position.set(_camCntr.x + _camOffset.x,
				  			 _camCntr.y + _camOffset.y,
				  			 _camCntr.z + _camOffset.z);
		_camera.update();
		_camera.apply(Gdx.gl10);
		_spriteBatch.setProjectionMatrix(_camera.combined);
		
		super.draw();
	}
	
	@Override
	public void pause()
	{
		_particlePool01.terminateThread();
		pauseEntities();
	}
	
	@Override
	public void resume()
	{
		_particlePool01.startThread();
		unpauseEntities();
	}
	
	public float getCamOffsetX()
	{
		return _camOffset.x;
	}
	public float getCamOffsetY()
	{
		return _camOffset.y;
	}
	public float getCamCntrX()
	{
		return 	_camCntr.x;
	}
	public float getCamCntrY()
	{
		return _camCntr.y;
	}
	
	public float getCamAdjustedTouchX()
	{
		return _camCntr.x + _camOffset.x - _halfTargetWidth + getAdjustedTouchX();
	}
	public float getCamAdjustedTouchY()
	{
		return _camCntr.y + _camOffset.y + _halfTargetHeight - getAdjustedTouchY();
	}
	
	public float getGroundFrictionEffect(float dt)
	{
		float ret = 1.0f-(WORLD_GROUND_FRICTION*dt);
		return ret;
	}
	
	public float getAirFrictionEffect(float dt)
	{
		return 1.0f-(WORLD_AIR_FRICTION*dt);
	}
	
	public void bomb(float x, float y)
	{
		// Start bomb effect
		_bombPos.set(x, y);
		_bombTime = BOMB_TIME_MAX;
	}
	
	private void bombUpdate(float dt)
	{
		_bombTime -= dt;
		
		// If this has caused the bomb time to end
		if (_bombTime <= 0)
		{
			// Destroy all bombable stuff onscreen
			removeOnScreen();
		}
		else
		{			
			// Move all entities away from the bomb
			for(Map.Entry<String, List<Entity>> le : _entities.entrySet())
			{
				//Loop through list
				List<Entity> lev = le.getValue();
				for(int e = 0; e < lev.size(); ++e)
				{
					// Move worms
					GameEntity curEntity = Utils.as(Mineral.class, lev.get(e));
					
					// Move mines
					if (curEntity == null)
						curEntity = Utils.as(Mine.class, lev.get(e));
		
					// Move worms
					if (curEntity == null)
						curEntity = Utils.as(Worm.class, lev.get(e));
					
					if(curEntity != null)
					{
						// Get position of entity
						Vector2 v = curEntity.getPosV2().cpy();
						
						// Get direction away from bomb
						v.sub(_bombPos);
						v.nor();
						v.mul(BOMB_PUSH_POWER);
						
						// Move entity in that position
						curEntity.setPosX(curEntity.getPosX() + v.x);
						curEntity.setPosY(curEntity.getPosY() + v.y);
					}
				}
			}
		}
	}
}
