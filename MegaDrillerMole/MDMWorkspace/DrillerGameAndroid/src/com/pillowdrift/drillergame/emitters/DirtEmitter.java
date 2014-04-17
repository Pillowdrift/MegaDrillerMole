package com.pillowdrift.drillergame.emitters;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.pillowdrift.drillergame.framework.Utils;
import com.pillowdrift.drillergame.framework.particles.Emitter;
import com.pillowdrift.drillergame.framework.particles.Particle;
import com.pillowdrift.drillergame.framework.particles.Pool;
import com.pillowdrift.drillergame.scenes.GameScene;

/**
 * Emitter for the explosion effect which occurs on player's death
 * @author cake_cruncher_7
 *
 */
public class DirtEmitter extends Emitter
{
	TextureRegion[] _textures;

	public DirtEmitter(Pool pool, GameScene parent)
	{
		super(pool);
		
		//Setup textures
		_textures = new TextureRegion[] {
			parent.getResourceManager().getAtlasRegion("atlas01", "dirtparticle1"),
			parent.getResourceManager().getAtlasRegion("atlas01", "dirtparticle2"),
		};
		
		//Setup particle bounds
		//MIN
		_minParticle = new Particle();
		_minParticle._rot = 0.0f;
		_minParticle._drot = -120.0f;
		_minParticle._scale = 3.0f;
		_minParticle._dscale = -0.2f;
		_minParticle._lifetime = 0.1f;
		_minParticle._alpha = 1.0f;
		_minParticle._dalpha = -1.0f/_minParticle._lifetime;
		_minParticle._gravityCo = 2.0f;
		_minParticle._texture = _textures[0];
		_minParticle._width = _minParticle._texture.getRegionWidth();
		_minParticle._height = _minParticle._texture.getRegionHeight();
		_minParticle._originX = _minParticle._width*0.5f;
		_minParticle._originY = _minParticle._height*0.5f;
		_minParticle._dx = -34.0f + parent.ALL_VELOCITY_X;
		_minParticle._dy = -34.0f;
		_minParticle._x = -8.0f;
		_minParticle._y = -8.0f;
		//MAX
		_maxParticle = new Particle();
		_maxParticle._rot = 360.0f;
		_maxParticle._drot = 120.0f;
		_maxParticle._scale = 3.0f;
		_maxParticle._dscale = -0.2f;
		_maxParticle._lifetime = 1.67f;
		_maxParticle._alpha = 1.0f;
		_maxParticle._dalpha = -1.0f/_maxParticle._lifetime;
		_maxParticle._gravityCo = 2.0f;
		_maxParticle._dx = 34.0f + parent.ALL_VELOCITY_X;
		_maxParticle._dy = 34.0f;
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
