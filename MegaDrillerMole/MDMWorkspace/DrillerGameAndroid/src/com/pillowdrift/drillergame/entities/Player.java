package com.pillowdrift.drillergame.entities;

import java.util.Currency;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.pillowdrift.drillergame.database.ItemDAO;
import com.pillowdrift.drillergame.emitters.DirtEmitter;
import com.pillowdrift.drillergame.emitters.GemEmitter;
import com.pillowdrift.drillergame.emitters.PlayerDeathsplosionEmitter;
import com.pillowdrift.drillergame.emitters.PlayerExplosionEmitter;
import com.pillowdrift.drillergame.emitters.PlayerFireEmitter;
import com.pillowdrift.drillergame.emitters.PlayerSmokeEmitter;
import com.pillowdrift.drillergame.emitters.SparkleEmitter;
import com.pillowdrift.drillergame.emitters.SpillGemEmitter;
import com.pillowdrift.drillergame.emitters.SplashDirtEmitter;
import com.pillowdrift.drillergame.framework.Entity;
import com.pillowdrift.drillergame.framework.GameEntity;
import com.pillowdrift.drillergame.framework.Sprite;
import com.pillowdrift.drillergame.framework.Utils;
import com.pillowdrift.drillergame.scenes.GameScene;

/**
 * Entity representing the player, which is controlled by the user and a vital part of the gameplay.
 * @author cake_cruncher_7
 *
 */
public class Player extends GameEntity
{

	//CONSTANTS
	private static final float ROTATE_SPEED = 230.0f;
	private static final float ACCEL_SPEED = 1160.0f;
	private static final float BOOST_MAX = 200.0f;
	private static final float BOOST_CONSUMPTION = 12.0f;
	private static final float BOOST_ACCEL_BONUS = 1050.0f;
	private static final float BOUND_DISTANCE = 20.0f;
	private final float BOUND_RIGHT;
	private final float BOUND_LEFT;
	private final float BOUND_BOTTOM;
	private static final Color HURT_COLOR = Color.RED;
	private static final float HURT_TIME = 0.3f;
	private static final float TOUCH_DISTANCE_SPEED_MULTIPLIER = 0.02f;
	private static final float TOUCH_DISTANCE_SPEED_MIN = 1.0f;
	private static final float TOUCH_DISTANCE_SPEED_START = 1.3f;
	private static final float TOUCH_DISTANCE_SPEED_MAX = 3.0f;
	private static final float AIR_ACC_MODIFIER = 0.03f;
	private static final long EXTRA_LIFE_SCORE = 100000;
	private static final float INVINCIBILITY_TIME = 2.5f;
	private static final float FLASH_TIME = 0.1f;
	
	//DATA
	boolean _start = true;
	
	private Vector2 _velocity;
	private float _boostFuel = 0.0f;
	private float _touchDistanceSpeedModifier = 0.1f;
	
	private Drill _activeDrill = null;
	
	//Emitters
	PlayerFireEmitter _fireEmitter;
	PlayerExplosionEmitter _explosionEmitter;
	PlayerSmokeEmitter _smokeEmitter;
	PlayerDeathsplosionEmitter _deathsplosionEmitter;
	GemEmitter _gemEmitter;
	DirtEmitter _dirtEmitter;
	SpillGemEmitter _spillEmitter;
	SparkleEmitter _sparkleEmitter;
	SplashDirtEmitter _splashDirtEmitter;
	
	//Life
	private float _invincibilityTimer = INVINCIBILITY_TIME;
	private boolean _alive = true;							//Whether we are currently alive
	float _lifeAfterDeath = 3.6f;							//Time we derp around for after dying
	float _explodeTime = 0.45f;								//Time to explode for
	float _deathWaitTime = 3.0f;							//Time tco wait for before showing game over
	float _hurtTimer = 0.0f;								//Timer to change the sprite to hurt color.
	long _livesGiven = 0;									//The amount of lives given due to score.
	float _flashTimer = 0;									//Time left for timer to flash
	boolean _hasBubble = true;								//Whether or not player has shield
	float _popTime = 0.0f;									//How long to pop bubble for
	
	//Sound
	boolean _digSoundPlaying = false;
	Sound _digSound;
	long _digSoundId;
	boolean _bedrockSoundPlaying = false;
	Sound _bedrockSound;
	long _bedrockSoundId;
	boolean _airSoundPlaying = false;
	Sound _airSound;
	long _airSoundId;
	Sound _explosionSound;
	boolean _exploded = false;
	boolean _boostSoundPlaying = false;
	Sound _boostSound;
	long _boostSoundId;
	
	private Rectangle _boundingBox;
	
	private Sprite _bubble;
	
	//ACCESS
	public Vector2 getVelocity()
	{
		return _velocity.cpy();
	}
	public void setVelocity(Vector2 vel)
	{
		_velocity = vel;
	}
	public boolean isAlive()
	{
		return _alive;
	}
	public boolean isBlownUp() {
		return !_alive && (_lifeAfterDeath > 0) && (_explodeTime > 0);
	}
	public Drill getActiveDrill() {
		return _activeDrill;
	}
	public void setActiveDrill(Drill activeDrill) {
		this._activeDrill = activeDrill;
	}
	
	/**
	 * Add the specified number of points to our load and increment our combo
	 * @param points
	 */
	public void givePoints(long points)
	{
		if(_alive)
		{
			_parentGameScene.adjustPlayer01Combo(1);
			_parentGameScene.adjustPlayer01Load(points);
			
			//Pop-up combo
			_parentGameScene.addEntity("ComboBubble", new ComboAlertBubble(_parentGameScene, _x, _y, _parentGameScene.getPlayer01Combo()));
		}
	}
	/**
	 * Add the specified number of points to our load
	 * @param points
	 */
	public void givePointsWithoutCombo(long points)
	{
		if(_alive)
		{
			if (_parentGameScene.getPlayer01Combo() == 0) _parentGameScene.adjustPlayer01Combo(1);
			_parentGameScene.adjustPlayer01Combo(0);
			_parentGameScene.adjustPlayer01Load(points);
			
			//Pop-up combo
			//_parentGameScene.addEntity("ComboBubble", new ComboAlertBubble(_parentGameScene, _x, _y, _parentGameScene.getPlayer01Combo()));
		}
	}	
	/**
	 * Transfer our load to our score, zero our combo
	 * @param heightBonus
	 */
	public void transferLoad(int heightBonus)
	{
		if(_alive)
		{
			//Show text depending on how much load was dropped off
			showFeedbackText();
			
			//Pop-up bonus
			if(heightBonus != 0)
			{
				//Display height bonus
				_parentGameScene.addEntity("HeightBubble", new HeightAlertBubble(_parentGameScene, _x, _y, heightBonus));
				//Display multiplier
				Entity comboMarker = _parentGameScene.addEntity("ComboBubble", new ComboAlertBubble(_parentGameScene, _x+47.0f, _y-8.0f, _parentGameScene.getPlayer01Combo()));
				comboMarker.setScaleX(0.3f);
				comboMarker.setScaleY(0.3f);
			}
			
			// Make the gem emitter emit gems.
			_gemEmitter.start(heightBonus, _parentGameScene.getPlayer01Load());	
			
			_parentGameScene.adjustPlayer01Load(heightBonus);
			_parentGameScene.adjustPlayer01Score(_parentGameScene.getPlayer01Load());
			_parentGameScene.setPlayer01Load(0);
			_parentGameScene.setPlayer01Combo(0);
		}
	}
	
	/**
	 * Displays text depending on the load the player has given to the train
	 */
	public void showFeedbackText()
	{
		//Display text
		if (_parentGameScene.getPlayer01Load() >= 1000 && _parentGameScene.getPlayer01Load() < 100000)
		{
			AlertBubble bubble = (AlertBubble) _parentGameScene.addEntity("NiceBubble", new GenericTextAlertBubble(_parentGameScene, _x, _y, "NICE!"));
			bubble.setScaleX(1.0f);
			bubble.setScaleY(0.5f);
			bubble.setText01OffsetX(9.0f);
			bubble.setText01OffsetY(32.0f);
		}
		
		//Display text
		if (_parentGameScene.getPlayer01Load() >= 100000 && _parentGameScene.getPlayer01Load() < 1000000)
		{
			AlertBubble bubble = (AlertBubble) _parentGameScene.addEntity("SuperBubble", new GenericTextAlertBubble(_parentGameScene, _x, _y, "COOL!"));
			bubble.setScaleX(1.0f);
			bubble.setScaleY(0.5f);
			bubble.setText01OffsetX(9.0f);
			bubble.setText01OffsetY(32.0f);
		}
		
		//Display text
		if (_parentGameScene.getPlayer01Load() >= 1000000 && _parentGameScene.getPlayer01Load() < 10000000)
		{
			AlertBubble bubble = (AlertBubble) _parentGameScene.addEntity("HolyBubble", new GenericTextAlertBubble(_parentGameScene, _x, _y, "HOLY!"));
			bubble.setScaleX(1.0f);
			bubble.setScaleY(0.5f);
			bubble.setText01OffsetX(9.0f);
			bubble.setText01OffsetY(32.0f);
		}	
		
		//Display text
		if (_parentGameScene.getPlayer01Load() >= 10000000)
		{
			AlertBubble bubble = (AlertBubble) _parentGameScene.addEntity("BestBubble", new GenericTextAlertBubble(_parentGameScene, _x, _y, "GOD LIKE!"));
			bubble.setScaleX(1.0f);
			bubble.setScaleY(0.5f);
			bubble.setText01OffsetX(9.0f);
			bubble.setText01OffsetY(32.0f);
		}
	}
	
	public void giveFuel(float value)
	{
		if(_alive)
		{
			//Display boost text
			AlertBubble bubble = (AlertBubble) _parentGameScene.addEntity("BoostBubble", new GenericTextAlertBubble(_parentGameScene, _x, _y, "BOOST"));
			bubble.setScaleX(1.0f);
			bubble.setScaleY(0.5f);
			bubble.setText01OffsetX(9.0f);
			bubble.setText01OffsetY(32.0f);
			
			_boostFuel += value;
			if(_boostFuel > BOOST_MAX)
				_boostFuel = BOOST_MAX;
		}
	}
	public void hurt()
	{
		if (!_hasBubble)
		{
			if (_invincibilityTimer <= 0)
			{
				// Drop all of our gems
				_spillEmitter.start(_parentGameScene.getPlayer01Load());
				
				if(_alive)
				{
					// Drop all of our gems
					_spillEmitter.start(_parentGameScene.getPlayer01Load());
					
					// Set the players score to 0
					_parentGameScene.setPlayer01Load(0);
					_parentGameScene.setPlayer01Combo(0);
					
					// Change the entities color to the hurt color for time.
					_hurtTimer = HURT_TIME;
					_invincibilityTimer = INVINCIBILITY_TIME;
					
					//Remove one hp and check feedback for death
					if(_parentGameScene.adjustPlayer01Life(-1))
					{
						//We died :C
						_alive = false;
						_explosionEmitter.emit(_x, _y);
						
						// If stop the gem sound if it's still running
						_gemEmitter.stop();
					}
				}
				// Make a small explosion
				_explosionEmitter.emit(_x, _y);	
				
				// Play an explode sound.
				_explosionSound.play();
			}
		}
		else
		{
			_invincibilityTimer = INVINCIBILITY_TIME;
			_popTime = 1.0f;
			_hasBubble = false;
		}
	}
	public void instaKill()
	{
		_alive = false;
		_lifeAfterDeath = 0.0f;
		
		// Play an explode sound.
		_explosionSound.play();
	}
	
	//CONSTRUCTION
	public Player(GameScene parent)
	{
		super(parent);		
		
		//toggle ads
		_parent.getOwner().getGame().getAdsService().turnOffAds();
		
		//Set starting position and depth
		_x = parent.getTargetWidth()/3.0f + 35;
		_y = parent.getTargetHeight() - 35;
		_depth = 9.0f;
		
		//Set starting velocity
		_velocity = new Vector2(150.0f, 200.0f);
		
		//Set origin
		_originX = 32.0f;
		_originY = 32.0f;
		
		//Set scale
		_scaleX = 1.0f;
		_scaleY = 1.0f;		

		// Set the drill
		if (_parent.getOwner().getGame().getDataService().serviceAvailable())
		{
			String currentDrill = _parent.getOwner().getGame().getDataService().getItemDataAccessor().getCurrentDrillInUse();
			if (currentDrill.equals(ItemDAO.ItemNames.FLAMEDRILL))
			{
				_activeDrill = new FireDrill(parent, this);
			}
			else if (currentDrill.equals(ItemDAO.ItemNames.FEATHERDRILL))
			{
				_activeDrill = new FeatherDrill(parent, this);
			}
			else if (currentDrill.equals(ItemDAO.ItemNames.ELECDRILL))
			{
				_activeDrill = new ElectricDrill(parent, this);
			}
			else if (currentDrill.equals(ItemDAO.ItemNames.FIRSTDRILL))
			{
				_activeDrill = new Drill(parent, this);
			}
		}
		else
		{
			_activeDrill = new Drill(parent, this);
		}
		
		
		_boundingBox = new Rectangle();
		_boundingBox.set(_x - _originX, _y - _originY, _activeDrill.getSprites().getCurrentSprite().getWidth(), _activeDrill.getSprites().getCurrentSprite().getHeight());
		
		//Set boundaries
		BOUND_RIGHT = _parent.getTargetWidth() - BOUND_DISTANCE;
		BOUND_LEFT = BOUND_DISTANCE;
		BOUND_BOTTOM = GameScene.WORLD_BEDROCK_VISIBILITY + BOUND_DISTANCE;
		
		//Set up particle emitters
		_fireEmitter = new PlayerFireEmitter(parent.getParticlePool01(), parent);
		_explosionEmitter = new PlayerExplosionEmitter(parent.getParticlePool01(), parent, 12);
		_smokeEmitter = new PlayerSmokeEmitter(parent.getParticlePool01(), parent);
		_deathsplosionEmitter = new PlayerDeathsplosionEmitter(parent.getParticlePool01(), parent);
		_gemEmitter = new GemEmitter(parent.getParticlePool01(), parent);
		_dirtEmitter = new DirtEmitter(parent.getParticlePool01(), parent);
		_spillEmitter = new SpillGemEmitter(parent.getParticlePool01(), parent);
		_splashDirtEmitter = new SplashDirtEmitter(parent.getParticlePool01(), parent);
		
		//Set touch distance modifier initial value
		_touchDistanceSpeedModifier = TOUCH_DISTANCE_SPEED_START;
		
		//Create the trail.
		if(_parent.getEntityFirst("DigTrail") == null)
			_parent.addEntity("DigTrail", new DigTrail(parent, this));
		
		//Get the dig sound.
		_digSound = parent.getResourceManager().getSound("dig");
		_airSound = parent.getResourceManager().getSound("air");
		_bedrockSound = parent.getResourceManager().getSound("bedrock");
		_boostSound = parent.getResourceManager().getSound("boost");
		// Get the explosion sound
		_explosionSound = parent.getResourceManager().getSound("explosion");
		
		// Create the aura
		parent.addEntity("PlayerAura", new PlayerAura(parent, 150));
		parent.addEntity("PlayerAura", new PlayerAura(parent, -150));
		_sparkleEmitter = new SparkleEmitter(parent.getParticlePool01(), parent);
		
		// Fire for when the player goes fast.
		parent.addEntity("PlayerFire", new PlayerFire(parent));		
		
		// Add bubble
		_sprites.addSprite("Bubble", new Sprite(parent.getResourceManager().getAtlasRegion("atlas01", "bubble"), 64, 0));
		_sprites.setCurrentSpriteName("Bubble");
		_bubble = _sprites.getCurrentSprite();
		
		if (_parent.getOwner().getGame().getDataService().serviceAvailable())
		{
			_hasBubble = _parent.getOwner().getGame().getDataService().getItemDataAccessor().hasBubble();
			// remove bubble if they have it cause it's now in use
			if (_hasBubble)
				_parent.getOwner().getGame().getDataService().getItemDataAccessor().setBubble(false);	
		}
	}

	//FUNCTION
	@Override
	public void update(float dt)
	{
		super.update(dt);
		_activeDrill.update(dt);
		// Constantly emit sparkles
		_sparkleEmitter.update(dt, _x, _y);
		_sparkleEmitter.emit();
		
		// Update the splash dirt
		_splashDirtEmitter.update(dt, _x, GameScene.WORLD_SURFACE_HEIGHT);
		_splashDirtEmitter.emit(onSurface());
		
		if (_start && _y < GameScene.WORLD_SURFACE_HEIGHT)
			_start = false;
		
		// Sound
		// Set the dig sound to be louder
		_digSound.setVolume(_digSoundId, getDigSoundVolume());
		// Air sound
		_airSound.setVolume(_airSoundId, getAirSoundVolume());
		// The bedrock sound
		_bedrockSound.setVolume(_bedrockSoundId, getBedrockSoundVolume());
		// The boost sound
		_boostSound.setVolume(_boostSoundId, getBoostSoundVolume());
		
		// Final explosion
		if (_explodeTime < 0 && !_exploded) {
			// Play an explode sound.
			_explosionSound.play();
			_exploded = true;
		}
		
		if (_popTime > 0)
		{
			_popTime -= dt;
			if ((_popTime - dt) < 0) {
				_parent.getResourceManager().getSound("pop").play(1.0f);
			}
		}
		
		//Acceleration value - will be modified based on situation
		float frameAccel = ACCEL_SPEED;
		
		//If we're alive
		if(_alive)
		{
			// Update the gem emitter.
			_gemEmitter.update(dt, _x, _y);
			_gemEmitter.emit();
			
			// The spill emitter
			_spillEmitter.update(dt, _x, _y);
			_spillEmitter.emit();
			
			//If the screen is touched, then we need to rotate towards this point for player control
			if(_parent.getInputManager().isTouched())
			{
				//Retrieve the coordinates of the last touch
				float touchX = _parentGameScene.getCamAdjustedTouchX();
				float touchY = _parentGameScene.getCamAdjustedTouchY();
				//Rotate towards the touch position
				rotateTowardsPoint(touchX, touchY, dt);
				
				_touchDistanceSpeedModifier = new Vector2(touchX - _x, touchY - _y).len() * TOUCH_DISTANCE_SPEED_MULTIPLIER;	
				_touchDistanceSpeedModifier = Math.min(Math.max(TOUCH_DISTANCE_SPEED_MIN, _touchDistanceSpeedModifier),TOUCH_DISTANCE_SPEED_MAX);	
			}
			
			// Emit dirt particles.
			if (!onSurface()) {
				_dirtEmitter.update(dt, _x, _y);
				_dirtEmitter.emit();
			}
		}
		else
		{
			//We've died, follow the death AI - spin out of control and explode
			if(_lifeAfterDeath > 0.0f)
			{
				//Decrement life after death counter
				_lifeAfterDeath -= dt;
				
				//Trail smoke
				_smokeEmitter.update(dt, _x, _y);
				
				//SpeedModifier
				_touchDistanceSpeedModifier = 1.0f;
				
				//Accelerate faster!
				frameAccel *= 1.65f;
				
				//Rotate and move randomly
				if(Utils.randomI(0, 10) < 9)
				{	
					rotateTowardsPoint(Utils.randomF(0.0f, _parent.getTargetWidth()), Utils.randomF(GameScene.WORLD_BEDROCK_HEIGHT, GameScene.WORLD_ATMOSPHERIC_HEIGHT), dt);
				}
			}
			else if(_explodeTime > 0.0f)
			{
				//Decrement explode counter
				_explodeTime -= dt;
				
				//Zero acceleration
				frameAccel = 0.0f;
				
				//Explode!
				_deathsplosionEmitter.update(dt, _x, _y);
				_deathsplosionEmitter.update(dt, _x, _y);
			}
			else if(_deathWaitTime > 0.0f)
			{
				//Zero acceleration
				frameAccel = 0.0f;
				
				//Wait
				_deathWaitTime -= dt;
			}
			else
			{
				//Zero acceleration
				frameAccel = 0.0f;
				
				//Trigger endgame
				_parentGameScene.endGame();
			}
		}
			
		//Boosting?
		if(_boostFuel > 0.0f)
		{
			//Accelerate faster!
			frameAccel += BOOST_ACCEL_BONUS;
			
			//Spawn fire
			_fireEmitter.update(dt, _x, _y);
			
			//Consume fuel
			if((_boostFuel -= (BOOST_CONSUMPTION * _activeDrill.boostModifier() * dt)) < 0.0f)
				_boostFuel = 0.0f;
		}	
		
		//Calculate a movement vector from the current rotation
		Vector2 moveDir = new Vector2(_touchDistanceSpeedModifier, 0.0f);
		moveDir.rotate(_rotation);
		//Multiply this by the acceleration speed
		Vector2 accelVector = moveDir.cpy().mul(frameAccel);	
		
		if(_y < GameScene.WORLD_SURFACE_HEIGHT)
		{
			//Set animation
			_activeDrill.getSprites().setCurrentSpriteName("forward");
			
			//Apply ground friction
			float frictionVal = GameScene.WORLD_GROUND_FRICTION;
			if(_y < GameScene.WORLD_BEDROCK_HEIGHT)
			{
				frictionVal *= 3.0f;
			}
			Vector2 frictionVector = _velocity.cpy().mul(frictionVal*dt);
			_velocity.x -= frictionVector.x;
			_velocity.y -= frictionVector.y;
			
			//If we are below ground
			//Affect the player's acceleration based on their current rotation
			//Add this to our velocity
			_velocity.x += accelVector.x*dt;
			_velocity.y += accelVector.y*dt;
		}
		else
		{	
			//If we're above ground apply gravity
			_velocity.y += GameScene.WORLD_GRAVITY_POWER * _activeDrill.gravityModifier() * dt;
			
			// Rotate the sprite to the angle of the velocity if the player just spawned
			if (_start)
				_rotation = _velocity.angle();
			
			//Multiply this by the acceleration speed
			//Add this to our velocity
			_velocity.x += accelVector.x*dt*AIR_ACC_MODIFIER;
			_velocity.y += accelVector.y*dt*AIR_ACC_MODIFIER;			
			
			//Set animation
			_activeDrill.getSprites().setCurrentSpriteName("air");
			
			//Also apply ground friction
			Vector2 frictionVector = _velocity.cpy().mul(GameScene.WORLD_AIR_FRICTION*dt);
			_velocity.x -= frictionVector.x;
			_velocity.y -= frictionVector.y;
		}
		
		//Wrap rotation between -179 and 180 degrees
		_rotation = Utils.wrapf(_rotation, 0.0f, 360.0f);
		
		//Move the player based on their new velocity and the world velocity
		_x += (_velocity.x + _parentGameScene.ALL_VELOCITY_X)*dt;
		_y += _velocity.y*dt;
		
		//Affect player with boundaries
		if(_x > BOUND_RIGHT)
		{
			//Right
			_x = BOUND_RIGHT;
			_velocity.x = -_velocity.x;
		}
		else if(_x < BOUND_LEFT)
		{
			//Left
			_x = BOUND_LEFT;
			if(_velocity.x < 0.0f)
				_velocity.x = -_velocity.x;
		}
		if(_y < BOUND_BOTTOM)
		{
			//Bottom
			_y = BOUND_BOTTOM;
			_velocity.y = -_velocity.y;
		}
		
		if (_invincibilityTimer > 0)
		{
			_invincibilityTimer -= dt;
			
			// Make the player flash.
			_flashTimer -= dt;
			if (_flashTimer < 0) {
				if (_color == Color.WHITE) {
					_color = Color.CLEAR;
				} else {
					_color = Color.WHITE;
				}
				_flashTimer = FLASH_TIME;
			}
		} else {
			_color = Color.WHITE;
		}
	}
	
	public float getBoostSoundVolume() {
		if (_boostFuel > 0) {
			if (!_boostSoundPlaying) {
				_boostSoundId = _boostSound.loop(1.0f, 0.6f, 0.0f);
				_boostSoundPlaying = true;
			}
			return 1.0f;
		}
		if (_boostSoundPlaying) {
			_boostSound.stop(_boostSoundId);
			_boostSoundPlaying = false;
		}
		return 0.0f;
	}
	
	/**
	 * Calculates the volume of the dig sound.
	 * @return
	 */
	public float getDigSoundVolume() {
		if (onSurface() || _y < GameScene.WORLD_BEDROCK_HEIGHT) {
			if (_digSoundPlaying) {
				_digSound.stop(_digSoundId);
				_digSoundPlaying = false;
			}
			return 0.0f;
		}
		if (!_digSoundPlaying) {
			_digSoundId = _digSound.loop();
			_digSoundPlaying = true;
		}	
		float vol = _velocity.len() / 1000.0f;
		if (vol > 1.0f)
			return 1.0f;
		return vol;
	}
	
	public float getBedrockSoundVolume() {
		if (onSurface() || _y > GameScene.WORLD_BEDROCK_HEIGHT) {
			if (_bedrockSoundPlaying) {
				_bedrockSound.stop(_bedrockSoundId);
				_bedrockSoundPlaying = false;
			}
			return 0.0f;
		}
		if (!_bedrockSoundPlaying) {
			_bedrockSoundId = _bedrockSound.loop();
			_bedrockSoundPlaying = true;
		}	
		float vol = _velocity.len() / 500.0f;
		if (vol > 1.0f)
			return 1.0f;
		return vol;
	}
	
	/**
	 * Get the volume of the air sound.
	 * @return
	 */
	public float getAirSoundVolume() {
		if (!onSurface()) {
			if (_airSoundPlaying) {
				_airSound.stop(_airSoundId);
				_airSoundPlaying = false;
			}
			return 0.0f;
		}
		if (!_airSoundPlaying) {
			_airSoundId = _airSound.loop();
			_airSoundPlaying = true;
		}	
		float vol = _velocity.len() / 500.0f;
		if (vol > 1.0f)
			return 1.0f;
		return vol;
	}
	
	//Override draw to account for circumstances
	@Override
	public void draw(SpriteBatch spriteBatch)
	{		
		if(_alive)
		{
			//If alive draw normally
			_activeDrill.draw(spriteBatch);
			if (_hasBubble)
			{
				spriteBatch.draw(_bubble.getCurrentFrame(), _x-32, _y-32);
			}
			if(_popTime > 0)
			{
				float scaleSize = (2.0f - _popTime) * 64;
				spriteBatch.draw(_bubble.getCurrentFrame(), _x-(scaleSize/2), _y-(scaleSize/2), scaleSize, scaleSize);
			}
		}
		else
		{
			//Otherwise
			//If derping
			if(_lifeAfterDeath > 0.0f)
			{
				_activeDrill.deadDraw(spriteBatch);
			}
		}
	}
	
	void rotateTowardsPoint(float pointX, float pointY, float dt)
	{
		//Get the difference from the player
		Vector2 diff = new Vector2(pointX - _x, pointY - _y);
		float distFactor = _touchDistanceSpeedModifier;
		
		//Get a rotation from the difference in positions
		float angleToTouch = diff.angle();
		//Get the difference between our rotations        
		float diffAngle = angleToTouch - _rotation;
		
		// fix for cross 360/0 boundary
        while (diffAngle < -180) diffAngle += 360;
        while (diffAngle > 180) diffAngle -= 360;
        
        if(diffAngle > 0)
        	diffAngle = Math.min(diffAngle, ROTATE_SPEED * dt * distFactor);
        else
        	diffAngle = Math.max(diffAngle, -ROTATE_SPEED * dt * distFactor);
        
		_rotation += diffAngle;
	}
	
	public boolean onSurface()
	{
		return (_y > GameScene.WORLD_SURFACE_HEIGHT);
	}
	
	public boolean isBoosting()
	{
		return (_boostFuel > 0.0f);
	}
	
	//Respond to resets by removing self
	@Override
	public void onGameReset()
	{
		super.onGameReset();
		_activeDrill.onGameReset();
		remove();
	}
	
	@Override
	public void pause() {
		super.pause();
		
		_activeDrill.pause();
		
		// Stop the sound.
		_airSound.setVolume(_airSoundId, 0);
		_digSound.setVolume(_digSoundId, 0);
		_bedrockSound.setVolume(_bedrockSoundId, 0);
		_boostSound.setVolume(_boostSoundId, 0);	
		
		_gemEmitter.stop();
	}
	
	@Override
	public void onRemove() {
		super.onRemove();
		_activeDrill.onRemove();
		
		// Stop playing dig sound
		_digSound.stop(_digSoundId);
		
		// Stop playing the air sound.
		_airSound.stop(_airSoundId);	
		
		// Stop the bedrock sound
		_bedrockSound.stop(_bedrockSoundId);
		
		// Stop the boost sound
		_boostSound.stop(_boostSoundId);
		
		_parent.getOwner().getGame().getAdsService().turnOnAds();
	}
	
	public Rectangle getBoundingbox()
	{
		_boundingBox.x = _x - _originX;
		_boundingBox.y = _y - _originY;
		
		return _boundingBox;
	}
	
	public boolean isInvincible()
	{
		return _invincibilityTimer > 0;
	}

}
