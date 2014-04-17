package com.pillowdrift.drillergame.framework.particles;

import com.pillowdrift.drillergame.framework.Utils;

/**
 * Emitter which creates particles within a particle pool. Should be inherritted to create specific
 * emitters for effects such as explosions, smoke, fire, etc
 * @author cake_cruncher_7
 *
 */
public abstract class Emitter
{
	//DATA
	protected Pool _particlePool;		//The pool this emitted adds particles to
	protected float _x;					//Position of the emitter
	protected float _y;					
	protected Particle _minParticle;	//Minimum and maximum bounds for random variables. Texture is taken from _minParticle
	protected Particle _maxParticle;
	protected Particle _emitParticle;	//The particle used for emitting without memory allocation.
	protected float _timeTillNext;		//Time in seconds until the next particle will be spawned
	protected float _minTime;			//Minumum time between spawning particles
	protected float _maxTime;			//Maximum time between spawning particles
	
	
	//ACCESS
	public float getPosX()
	{
		return _x;
	}
	public void setPosX(float x)
	{
		_x = x;
	}
	public float getPosY()
	{
		return _y;
	}
	public void setPosY(float y)
	{
		_y = y;
	}
	public Particle getMinParticle()
	{
		return _minParticle;
	}
	public void setMinParticle(Particle p)
	{
		_minParticle = p;
	}
	public Particle getMaxParticle()
	{
		return _maxParticle;
	}
	public void setMaxParticle(Particle p)
	{
		_maxParticle = p;
	}
	public float getTimeTillNext()
	{
		return _timeTillNext;
	}
	public void setTimeTillNext(float time)
	{
		_timeTillNext = time;
	}
	public float getMinTime()
	{
		return _minTime;
	}
	public void setMinTime(float time)
	{
		_minTime = time;
	}
	public float getMaxTime()
	{
		return _maxTime;
	}
	public void setMaxTime(float time)
	{
		_maxTime = time;
	}
	public void setRandomTime()
	{
		_timeTillNext = Utils.randomF(_minTime, _maxTime);
	}
	
	
	//CONSTRUCTION
	public Emitter(Pool pool)
	{
		//Store reference to pool
		_particlePool = pool;
		//Create emitter particle
		_emitParticle = new Particle();
		_emitParticle._ready = true;
	}
	
	
	//FUNCTION
	public void update(float dt)
	{
		//Decrement time and decide whether to spawn a particle
		_timeTillNext -= dt;
		if(_timeTillNext <= 0.0f)
		{
			//Emit a particle
			emit();
			
			//Get new time
			_timeTillNext = Utils.randomF(_minTime, _maxTime);
		}
	}
	
	/**
	 * Function called to emit our personal particle instance. This avoids
	 * memory allocation and deallocation during particle spawning entirely.
	 * If our current particle is in use, a particle will not be spawned,
	 * and the function will return false, allowing you to respond to the
	 * situation appropriately through overrides.
	 * This may occur if the pool's thread has not added new particles
	 * since we last spawned.
	 */
	public boolean emit()
	{
		if(_emitParticle._ready)
		{
			//Set up a new particle
			_emitParticle._ready = false;
			//Vars
			//Texture vars - these come from minParticle only
			_emitParticle._texture = _minParticle._texture;
			_emitParticle._originX = _minParticle._originX;
			_emitParticle._originY = _minParticle._originY;
			_emitParticle._width = _minParticle._width;
			_emitParticle._height = _minParticle._height;
			//others - random between minParticle and maxParticle
			_emitParticle._x = _x + Utils.randomF(_minParticle._x, _maxParticle._x);
			_emitParticle._y = _y + Utils.randomF(_minParticle._y, _maxParticle._y);
			_emitParticle._dx = Utils.randomF(_minParticle._dx, _maxParticle._dx);
			_emitParticle._dy = Utils.randomF(_minParticle._dy, _maxParticle._dy);
			_emitParticle._gravityCo = Utils.randomF(_minParticle._gravityCo, _maxParticle._gravityCo);
			_emitParticle._rot = Utils.randomF(_minParticle._rot, _maxParticle._rot);
			_emitParticle._drot = Utils.randomF(_minParticle._drot, _maxParticle._drot);
			_emitParticle._scale = Utils.randomF(_minParticle._scale, _maxParticle._scale);
			_emitParticle._dscale = Utils.randomF(_minParticle._dscale, _maxParticle._dscale);
			_emitParticle._alpha = Utils.randomF(_minParticle._alpha, _maxParticle._alpha);
			_emitParticle._dalpha = Utils.randomF(_minParticle._dalpha, _maxParticle._dalpha);
			_emitParticle._lifetime = Utils.randomF(_minParticle._lifetime, _maxParticle._lifetime);
			//Add the particle to the allocated pool
			_particlePool.addParticle(_emitParticle);
		}
		
		//No particle spawned
		return false;
	}
	
	/**
	 * Function called to emit a particle created using the 'new' operator.
	 * This eliminates the memory allocation advantage of the particle pool,
	 * but may be necessary if you desire to spawn multiple particles in the 
	 * same frame.
	 */
	public void emitUnique()
	{
		//Set up a new particle
		Particle newParticle = new Particle();
		//Vars
		//Texture vars - these come from minParticle only
		newParticle._texture = _minParticle._texture;
		newParticle._originX = _minParticle._originX;
		newParticle._originY = _minParticle._originY;
		newParticle._width = _minParticle._width;
		newParticle._height = _minParticle._height;
		//others - random between minParticle and maxParticle
		newParticle._x = _x + Utils.randomF(_minParticle._x, _maxParticle._x);
		newParticle._y = _y + Utils.randomF(_minParticle._y, _maxParticle._y);
		newParticle._dx = Utils.randomF(_minParticle._dx, _maxParticle._dx);
		newParticle._dy = Utils.randomF(_minParticle._dy, _maxParticle._dy);
		newParticle._gravityCo = Utils.randomF(_minParticle._gravityCo, _maxParticle._gravityCo);
		newParticle._rot = Utils.randomF(_minParticle._rot, _maxParticle._rot);
		newParticle._drot = Utils.randomF(_minParticle._drot, _maxParticle._drot);
		newParticle._scale = Utils.randomF(_minParticle._scale, _maxParticle._scale);
		newParticle._dscale = Utils.randomF(_minParticle._dscale, _maxParticle._dscale);
		newParticle._alpha = Utils.randomF(_minParticle._alpha, _maxParticle._alpha);
		newParticle._dalpha = Utils.randomF(_minParticle._dalpha, _maxParticle._dalpha);
		newParticle._lifetime = Utils.randomF(_minParticle._lifetime, _maxParticle._lifetime);
		//Add the particle to the allocated pool
		_particlePool.addParticle(newParticle);
	}
}
