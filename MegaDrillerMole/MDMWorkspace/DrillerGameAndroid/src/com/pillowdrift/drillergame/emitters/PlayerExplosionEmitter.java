package com.pillowdrift.drillergame.emitters;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.pillowdrift.drillergame.framework.Utils;
import com.pillowdrift.drillergame.framework.particles.Emitter;
import com.pillowdrift.drillergame.framework.particles.Particle;
import com.pillowdrift.drillergame.framework.particles.Pool;
import com.pillowdrift.drillergame.scenes.GameScene;

/**
 * Emitter for an explosion effect
 * @author cake_cruncher_7
 *
 */
public class PlayerExplosionEmitter extends Emitter
{
	private static final int NUMBER_OF_TEXTURES = 3;
	private static final int MAX_TEXTURE_ID = NUMBER_OF_TEXTURES-1;
	
	private int _numEmits;
	TextureRegion[] _textures;

	public PlayerExplosionEmitter(Pool pool, GameScene parent, int numEmits)
	{
		super(pool);
		
		//Setup textures
		_textures = new TextureRegion[NUMBER_OF_TEXTURES];
		_textures[0] = parent.getResourceManager().getAtlasRegion("atlas01", "particleExplode01");
		_textures[1] = parent.getResourceManager().getAtlasRegion("atlas01", "particleExplode02");
		_textures[2] = parent.getResourceManager().getAtlasRegion("atlas01", "particleExplode03");
		
		//Setup particle bounds
		//MIN
		_minParticle = new Particle();
		_minParticle._rot = 0.0f;
		_minParticle._drot = -120.0f;
		_minParticle._scale = 0.7f;
		_minParticle._dscale = 8.7f;
		_minParticle._lifetime = 0.3f;
		_minParticle._alpha = 1.0f;
		_minParticle._dalpha = -1.0f/_minParticle._lifetime;
		_minParticle._gravityCo = 0.0f;
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
		_maxParticle._scale = 0.8f;
		_maxParticle._dscale = 15.3f;
		_maxParticle._lifetime = 0.67f;
		_maxParticle._alpha = 1.0f;
		_maxParticle._dalpha = -1.0f/_maxParticle._lifetime;
		_maxParticle._gravityCo = 0.0f;
		_maxParticle._dx = 34.0f + parent.ALL_VELOCITY_X;
		_maxParticle._dy = 34.0f;
		_maxParticle._x = 8.0f;
		_maxParticle._y = 8.0f;
		
		//Set up times
		_minTime = 0.01f;
		_maxTime = 0.1f;
		setRandomTime();
		
		//Store the number of emits to do per emission
		_numEmits = numEmits;
	}
	
	//Emit function to call emitUnique multiple times - does not work with update
	public void emit( float x, float y)
	{
		//Change position
		_x = x;
		_y = y;
		//Emit
		for(int i = 0; i < _numEmits; ++i)
		{
			//Randomly choose a texture
			_minParticle._texture = _textures[Utils.randomI(0, MAX_TEXTURE_ID)];
			
			//Force emitting unique particles
			super.emitUnique();
		}
	}

}
