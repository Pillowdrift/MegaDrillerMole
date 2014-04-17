package com.pillowdrift.drillergame.emitters;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.pillowdrift.drillergame.entities.CargoTrain;
import com.pillowdrift.drillergame.framework.Utils;
import com.pillowdrift.drillergame.framework.particles.Emitter;
import com.pillowdrift.drillergame.framework.particles.Particle;
import com.pillowdrift.drillergame.framework.particles.Pool;
import com.pillowdrift.drillergame.scenes.GameScene;

public class GemEmitter extends Emitter {
	// Vars
	TextureRegion[] _textures;
	float _lifespan = 0.0f;
	static final float FADEOUT_TIME = 0.4f;
	float _fadeout = 0.0f;
	GameScene _parent;
	Sound _gemSound;
	long _soundId = -1;
	
	/** 
	 * Create a new gem emitter.
	 * @param pool
	 * @param parent
	 */
	public GemEmitter(Pool pool, GameScene parent)
	{
		super(pool);
		
		// Set the parent
		_parent = parent;
		
		//Setup textures
		_textures = new TextureRegion[] {
				parent.getResourceManager().getAtlasRegion("atlas01", "mineralCopper"),
				parent.getResourceManager().getAtlasRegion("atlas01", "mineralSilver"),
				parent.getResourceManager().getAtlasRegion("atlas01", "mineralGold"),
		};
		
		//Setup particle bounds
		//MIN
		_minParticle = new Particle();
		_minParticle._rot = 0.0f;
		_minParticle._drot = -120.0f;
		_minParticle._scale = 0.9f;
		_minParticle._dscale = 1.0f;
		_minParticle._lifetime = 1.0f;
		_minParticle._alpha = 1.0f;
		_minParticle._dalpha = -1.0f/_minParticle._lifetime;
		_minParticle._gravityCo = 0.0f;
		_minParticle._texture = _textures[0];
		_minParticle._width = _minParticle._texture.getRegionWidth();
		_minParticle._height = _minParticle._texture.getRegionHeight();
		_minParticle._originX = _minParticle._width*0.5f;
		_minParticle._originY = _minParticle._height*0.5f;
		_minParticle._dx = 0;
		_minParticle._dy = -50.0f;
		_minParticle._x = -8.0f;
		_minParticle._y = -8.0f;
		//MAX
		_maxParticle = new Particle();
		_maxParticle._rot = 360.0f;
		_maxParticle._drot = 120.0f;
		_maxParticle._scale = 0.7f;
		_maxParticle._dscale = 1.0f;
		_maxParticle._lifetime = 1.4f;
		_maxParticle._alpha = 1.0f;
		_maxParticle._dalpha = -1.0f/_maxParticle._lifetime;
		_maxParticle._gravityCo = 0.0f;
		_maxParticle._dx = 0;
		_maxParticle._dy = -70.0f;
		_maxParticle._x = 8.0f;
		_maxParticle._y = 8.0f;
		
		//Set up times
		_minTime = 0.01f;
		_maxTime = 0.03f;
		setRandomTime();
		
		// Load the gem sound.
		_gemSound = parent.getResourceManager().getSound("unload");
	}
	
	// Start the emitter.
	public void start(float height, float score) {
		_lifespan = 0.001f * height;
		_minTime = 100.0f / score;
		_maxTime = 100.2f / score;
		
		// Start playing the sound.
		_soundId = _gemSound.loop();
		_fadeout = FADEOUT_TIME;
	}
	
	public void stop() {
		_gemSound.stop(_soundId);
	}
	
	//Update function to change position
	public void update(float dt, float x, float y)
	{	
		// Update the lifetime
		_lifespan -= dt;
		
		// Stop the sound if we need to
		if (_soundId != -1 && _lifespan < 0 && _fadeout < 0)
			_gemSound.stop(_soundId);
		if (_soundId != -1 && _lifespan < 0 && _fadeout > 0) {
			_gemSound.setVolume(_soundId, _fadeout / FADEOUT_TIME);
			_fadeout -= dt;
		}
		
		//Change position
		_x = x;
		_y = y;
		
		//Update emitter
		super.update(dt);
	}
	
	//Emit function to change texture randomly
	@Override
	public boolean emit()
	{
		if (_lifespan > 0) {
			// If the train is here move the particles towards it.
			CargoTrain train = (CargoTrain)_parent.getEntityFirst("Train");
			if (train != null) {
				// Get the direction to the train.
				Vector2 dir = train.getPosV2().sub(new Vector2(_x, _y));
				float len = dir.len();
				dir.nor();
				_minParticle._dx = dir.x * 0.5f * len;
				_minParticle._dy = dir.y * 0.5f * len;
				_maxParticle._dx = dir.x * 0.7f * len;
				_maxParticle._dy = dir.y * 0.7f * len;
			}
			
			
			//Randomly choose a texture
			_minParticle._texture = _textures[Utils.randomI(0, _textures.length - 1)];
			
			//Try to emit a particle
			return super.emit();
		}		
		
		return false;
	}
}
