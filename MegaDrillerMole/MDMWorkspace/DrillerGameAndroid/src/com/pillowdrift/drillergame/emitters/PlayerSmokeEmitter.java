package com.pillowdrift.drillergame.emitters;

import com.pillowdrift.drillergame.framework.particles.Emitter;
import com.pillowdrift.drillergame.framework.particles.Particle;
import com.pillowdrift.drillergame.framework.particles.Pool;
import com.pillowdrift.drillergame.scenes.GameScene;

/**
 * Emitter for the smoke trail left behind the player when dying
 * @author cake_cruncher_7
 *
 */
public class PlayerSmokeEmitter extends Emitter {

	public PlayerSmokeEmitter(Pool pool, GameScene parent)
	{	
		//Allow parent setup
		super(pool);
		
		//Setup particle bounds
		//MIN
		_minParticle = new Particle();
		_minParticle._rot = 0.0f;
		_minParticle._drot = -30.0f;
		_minParticle._scale = 0.6f;
		_minParticle._dscale = 0.2f;
		_minParticle._lifetime = 1.0f;
		_minParticle._alpha = 1.0f;
		_minParticle._dalpha = -1.0f/_minParticle._lifetime;
		_minParticle._gravityCo = 0.0f;
		_minParticle._texture = parent.getResourceManager().getAtlasRegion("atlas01", "particleExmoke01");
		_minParticle._width = _minParticle._texture.getRegionWidth();
		_minParticle._height = _minParticle._texture.getRegionHeight();
		_minParticle._originX = _minParticle._width*0.5f;
		_minParticle._originY = _minParticle._height*0.5f;
		_minParticle._dx = -2.0f+parent.ALL_VELOCITY_X;
		_minParticle._dy = -2.0f;
		_minParticle._x = -10.0f;
		_minParticle._y = -10.0f;
		//MAX
		_maxParticle = new Particle();
		_maxParticle._rot = 360.0f;
		_maxParticle._drot = 30.0f;
		_maxParticle._scale = 1.9f;
		_maxParticle._dscale = 0.6f;
		_maxParticle._lifetime = 1.4f;
		_maxParticle._alpha = 1.0f;
		_maxParticle._dalpha = -1.0f/_maxParticle._lifetime;
		_maxParticle._gravityCo = 0.0f;
		_maxParticle._dx = 2.0f+parent.ALL_VELOCITY_X;
		_maxParticle._dy = 2.0f;
		_maxParticle._x = 10.0f;
		_maxParticle._y = 10.0f;
		
		//Set up times
		_minTime = 0.01f;
		_maxTime = 0.07f;
		setRandomTime();
	}

	public void update(float dt, float x, float y)
	{
		//Change position
		_x = x;
		_y = y;
		//Update emitter
		update(dt);
	}
}
