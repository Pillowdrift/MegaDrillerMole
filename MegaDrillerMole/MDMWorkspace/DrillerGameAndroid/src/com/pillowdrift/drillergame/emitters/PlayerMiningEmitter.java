package com.pillowdrift.drillergame.emitters;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.pillowdrift.drillergame.entities.PlayerAura;
import com.pillowdrift.drillergame.framework.Utils;
import com.pillowdrift.drillergame.framework.particles.Emitter;
import com.pillowdrift.drillergame.framework.particles.Particle;
import com.pillowdrift.drillergame.framework.particles.Pool;
import com.pillowdrift.drillergame.scenes.GameScene;

public class PlayerMiningEmitter extends Emitter {
	// Vars
	TextureRegion[] _textures;
	GameScene _parent;
	
	/**
	 * Create a new gem emitter.
	 * @param pool
	 * @param parent
	 */
	public PlayerMiningEmitter(Pool pool, GameScene parent)
	{
		super(pool);
		
		// Set the parent
		_parent = parent;
		
		//Setup textures
		_textures = new TextureRegion[] {
				parent.getResourceManager().getAtlasRegion("atlas01", "spark1"),
				parent.getResourceManager().getAtlasRegion("atlas01", "spark2"),
				parent.getResourceManager().getAtlasRegion("atlas01", "spark3"),
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
		_minParticle._dx = -100.0f;
		_minParticle._dy = -100.0f;
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
		_maxParticle._dx = 100.0f;
		_maxParticle._dy = 100.0f;
		_maxParticle._x = 8.0f;
		_maxParticle._y = 8.0f;
		
		//Set up times
		_minTime = 0.01f;
		_maxTime = 0.03f;
		setRandomTime();
	}
	
	//Update function to change position
	public void update(float dt, float x, float y)
	{
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
		//Randomly choose a texture
		_minParticle._texture = _textures[Utils.randomI(0, _textures.length - 1)];
		
		//Try to emit a particle
		return super.emit();	
	}
}

