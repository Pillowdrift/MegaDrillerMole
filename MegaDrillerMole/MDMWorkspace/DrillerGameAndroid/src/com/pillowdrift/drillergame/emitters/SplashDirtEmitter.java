package com.pillowdrift.drillergame.emitters;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.pillowdrift.drillergame.entities.Player;
import com.pillowdrift.drillergame.framework.particles.Emitter;
import com.pillowdrift.drillergame.framework.particles.Particle;
import com.pillowdrift.drillergame.framework.particles.Pool;
import com.pillowdrift.drillergame.scenes.GameScene;

public class SplashDirtEmitter extends Emitter {
	// Vars
	float _x, _y;
	GameScene _parent;
	boolean _playerOnSurface;
	TextureRegion enter;
	TextureRegion exit;
	
	/**
	 * Create a splash emitter.
	 * @param pool
	 */
	public SplashDirtEmitter(Pool pool, GameScene parent) {
		super(pool);
		
		_x = 0;
		_y = 0;
		
		Player player = (Player)parent.getEntityFirst("Player");
		if (player != null)
			_playerOnSurface = player.onSurface();
		
		// Set up the textures
		exit = parent.getResourceManager().getAtlasRegion("atlas01", "dirtsplash");
		enter = parent.getResourceManager().getAtlasRegion("atlas01", "dirtenter");
		
		// Set up the _minParticle.
		_minParticle = new Particle();
		_minParticle._rot = 0.0f;
		_minParticle._drot = 0.0f;
		_minParticle._scale = 0.9f;
		_minParticle._dscale = 1.0f;
		_minParticle._lifetime = 2.0f;
		_minParticle._alpha = 1.0f;
		_minParticle._dalpha = -1.0f/_minParticle._lifetime;
		_minParticle._gravityCo = 0.0f;
		_minParticle._texture = enter;
		_minParticle._width = _minParticle._texture.getRegionWidth();
		_minParticle._height = _minParticle._texture.getRegionHeight();
		_minParticle._originX = _minParticle._width*0.5f;
		_minParticle._originY = 0;//_minParticle._height*0.5f;
		_minParticle._dx = GameScene.STARTING_ALL_VELOCITY_X;
		_minParticle._dy = 0.0f;
		_minParticle._x = 0.0f;
		_minParticle._y = 0.0f;	
		
		_maxParticle = _minParticle;
	}
	
	public void update(float dt, float x, float y) {
		// TODO Auto-generated method stub
		//super.update(dt);
		
		_x = x;
		_y = y;
	}
	
	public boolean emit(boolean onSurface) {
		if (onSurface != _playerOnSurface) {
			
			// Emit a particle.
			_minParticle._x = _x;
			_minParticle._y = _y;
			_minParticle._texture = onSurface ? enter : exit;
			Particle part = new Particle();
			part.copyFrom(_minParticle);
			_particlePool.addParticle(part);
			_playerOnSurface = onSurface;
			
			return true;
		}
		return false;
	}
}
