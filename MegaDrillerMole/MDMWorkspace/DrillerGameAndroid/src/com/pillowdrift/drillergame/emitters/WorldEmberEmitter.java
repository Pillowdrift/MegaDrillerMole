package com.pillowdrift.drillergame.emitters;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.pillowdrift.drillergame.framework.Utils;
import com.pillowdrift.drillergame.framework.particles.Emitter;
import com.pillowdrift.drillergame.framework.particles.Particle;
import com.pillowdrift.drillergame.framework.particles.Pool;
import com.pillowdrift.drillergame.scenes.GameScene;

/**
 * Emitter for the embers spawned from the depths
 * @author cake_cruncher_7
 *
 */
public class WorldEmberEmitter extends Emitter
{
	private static final int NUMBER_OF_TEXTURES = 4;
	private static final int MAX_TEXTURE_ID = NUMBER_OF_TEXTURES-1;
	
	TextureRegion[] _textures;
	
	public WorldEmberEmitter(Pool pool, GameScene parent) {
		super(pool);

		//Set position
		_x = 0.0f;
		_y = 0.0f;
		
		//Setup textures
		_textures = new TextureRegion[NUMBER_OF_TEXTURES];
		_textures[0] = parent.getResourceManager().getAtlasRegion("atlas01", "particleEmber01");
		_textures[1] = parent.getResourceManager().getAtlasRegion("atlas01", "particleEmber02");
		_textures[2] = parent.getResourceManager().getAtlasRegion("atlas01", "particleEmber03");
		_textures[3] = parent.getResourceManager().getAtlasRegion("atlas01", "particleEmber04");
		
		//Setup particle bounds
		//MIN
		_minParticle = new Particle();
		_minParticle._rot = 0.0f;
		_minParticle._drot = 0.0f;
		_minParticle._scale = 0.8f;
		_minParticle._dscale = -0.3f;
		_minParticle._lifetime = 2.7f;
		_minParticle._alpha = 1.0f;
		_minParticle._dalpha = -1.0f/_minParticle._lifetime;
		_minParticle._gravityCo = -0.2f;
		_minParticle._texture = _textures[0];
		_minParticle._width = _minParticle._texture.getRegionWidth();
		_minParticle._height = _minParticle._texture.getRegionHeight();
		_minParticle._originX = _minParticle._width*0.5f;
		_minParticle._originY = _minParticle._height*0.5f;
		_minParticle._dx = parent.ALL_VELOCITY_X;
		_minParticle._dy = 1.5f;
		_minParticle._x = 0.0f;
		_minParticle._y = GameScene.WORLD_BEDROCK_VISIBILITY;
		//MAX
		_maxParticle = new Particle();
		_maxParticle._rot = 360.0f;
		_maxParticle._drot = 0.0f;
		_maxParticle._scale = 1.2f;
		_maxParticle._dscale = 0.2f;
		_maxParticle._lifetime = 4.7f;
		_maxParticle._alpha = 1.0f;
		_maxParticle._dalpha = -1.0f/_maxParticle._lifetime;
		_maxParticle._gravityCo = 0.0f;
		_maxParticle._dx = parent.ALL_VELOCITY_X;
		_maxParticle._dy = 3.1f;
		_maxParticle._x = parent.getTargetWidth()*1.2f;
		_maxParticle._y = GameScene.WORLD_BEDROCK_HEIGHT + ((GameScene.WORLD_BEDROCK_VISIBILITY-GameScene.WORLD_BEDROCK_HEIGHT)/2.0f);
		
		//Set up times
		_minTime = 0.005f;
		_maxTime = 0.07f;
		setRandomTime();
	}

	@Override
	public boolean emit() {
		//Randomly choose a texture
		_minParticle._texture = _textures[Utils.randomI(0, MAX_TEXTURE_ID)];
		
		//Try to emit a particle
		return super.emit();
	}
}
