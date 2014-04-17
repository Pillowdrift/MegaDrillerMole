package com.pillowdrift.drillergame.framework.particles;

import java.util.concurrent.ConcurrentLinkedQueue;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.pillowdrift.drillergame.data.DataCache;
import com.pillowdrift.drillergame.database.ConfigDAO;
import com.pillowdrift.drillergame.framework.Entity;
import com.pillowdrift.drillergame.framework.Scene;

/**
 * A collection of particles which is updated on a separate thread.
 * @author cake_cruncher_7
 *
 */
public class Pool extends Entity implements Runnable
{
	//DATA
	final int  _maxParticles;					//The maximum amount of particles we have and can activate
	final Particle[] _pool01;					//Our collections of particles
	final Particle[] _pool02;
	Particle[] _updatingPool;					//Handle to the pool which is being updated currently
	Particle[] _readyPool;						//Handle to the pool which is ready for drawing
	ConcurrentLinkedQueue<Particle> _addQueue;	//Queue of particles to be added
	int _liveParticles;							//Number of active particles in our pool
	int _readyLiveParticles;					//Number of activeParticles in our ready pool
	long _lastTime;								//The time at which the last update occurred
	long _currentTime;							//The time of the current update
	float _gravity;								//Gravity which affects particles per second
	
	boolean _running = false;					//Whether or not this pool is currently updating itself
	boolean _ready = false;						//Whether or not this pool is currently ready to be started
	Thread _thread;								//The thread on which this particle pool runs
	long _updateDelayTime;						//Time in milliseconds to wait between updates
	
	//ACCESS
	public int getMaxParticles()
	{
		return _maxParticles;
	}
	public int getLiveParticles()
	{
		return _liveParticles;
	}
	public boolean isRunning()
	{
		return _running;
	}
	public boolean isReady()
	{
		return _ready;
	}
	
	//CONSTRUCTION
	public Pool(Scene parent, int poolSize, long updateDelay, float gravity)
	{
		super(parent);
		
		//Allocate pool memory
		_maxParticles = poolSize;
		_pool01 = new Particle[poolSize];
		_pool02 = new Particle[poolSize];
		//Initialize pool memory
		for(int i = 0; i < poolSize; ++i)
		{
			_pool01[i] = new Particle();
			_pool02[i] = new Particle();
		}
		//Set current pools
		_updatingPool = _pool01;
		_readyPool = _pool02;
		//Set up addition queue
		_addQueue = new ConcurrentLinkedQueue<Particle>();
		//Set live counts
		_liveParticles = 0;
		_readyLiveParticles = 0;
		//Set starting times
		_currentTime = System.nanoTime();
		_lastTime = _currentTime;
		//Set gravity
		_gravity = gravity;
		//Set delay time
		_updateDelayTime = updateDelay;	
		
		//Indicate we are ready to run
		_ready = true;
	}

	//FUNCTION
	@Override
	public void update(float dt)
	{
		//super.update(dt);	
		//Do nothing.
		//Yeah that's right, nothing.
	}
	
	/**
	 * Begin executing this particle pool on a new thread
	 */
	public void startThread()
	{
		//If the database doesn't say not to, activate particles!
		if(!DataCache.getConfigSetting(ConfigDAO.SettingNames.PARTICLES).equals(ConfigDAO.OnOff.OFF))
		{
			if(_ready)
			{
				_running = true;
				_ready = false;
				
				// Clear the particles first
				_liveParticles = 0;
				
				//Create thread
				_thread = new Thread(this);
				//Start it
				_thread.start();
			}
		}
	}
	/**
	 * Allow the thread to terminate itself
	 */
	public void terminateThread()
	{
		_running = false;
	}
	
	@Override
	public void run()
	{
		//Run this thread until we're told to stop
		while(_running)
		{
			//Wait a certain amount of time between updates
			try {
				Thread.sleep(_updateDelayTime);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			//Get the current time and calculate the difference since the last update
			_lastTime = _currentTime;
			_currentTime = System.nanoTime();
			float dt = (float)(_currentTime - _lastTime)/1000000000.0f;
			if (dt > 0.1) dt = 0.1f;
			
			//Update all particles based on the ready buffer and time difference
			for(int i = 0; i < _liveParticles; ++i)
			{
				//Get current particle
				Particle curParticleTo = _updatingPool[i];
				Particle curParticleFrom = _readyPool[i];
				
				//Decrement time remaining
				curParticleTo._lifetime = curParticleFrom._lifetime - dt;
				//If the particle's time is up, remove it
				if(curParticleTo._lifetime <= 0.0f)
				{
					//Remove particles by copying the last live particle's data to them
					//and decrementing the number of live particles by one.
					curParticleTo.copyFrom(_updatingPool[--_liveParticles]);
					
					//Update this particle NOW using it's correct counterpart
					//Sadly this is the most elegant solution to this problem :C
					//Note that this means this particle will not be removed this frame if it should be
					curParticleFrom = _readyPool[_liveParticles];
				}
				
				//Update all elements of the particle
				curParticleTo._texture = curParticleFrom._texture;
				curParticleTo._originX = curParticleFrom._originX;
				curParticleTo._originY = curParticleFrom._originY;
				curParticleTo._width = curParticleFrom._width;
				curParticleTo._height = curParticleFrom._height;
				curParticleTo._x = curParticleFrom._x + curParticleFrom._dx*dt;
				curParticleTo._dx = curParticleFrom._dx;
				curParticleTo._dy = curParticleFrom._dy + curParticleFrom._gravityCo*_gravity*dt;
				curParticleTo._y = curParticleFrom._y + curParticleFrom._dy*dt;
				curParticleTo._gravityCo = curParticleFrom._gravityCo;
				curParticleTo._rot = curParticleFrom._rot + curParticleFrom._drot*dt;
				curParticleTo._drot = curParticleFrom._drot;
				curParticleTo._scale = curParticleFrom._scale + curParticleFrom._dscale*dt;
				curParticleTo._dscale = curParticleFrom._dscale;
				curParticleTo._alpha = curParticleFrom._alpha + curParticleFrom._dalpha*dt;
				curParticleTo._dalpha = curParticleFrom._dalpha;
				//Cap alpha at zero
				curParticleTo._alpha = Math.max(curParticleTo._alpha, 0.0f);
			}
			
			//Update the back buffer
			//Add new particles to the collection
			Particle toAdd;
			while((_liveParticles < _maxParticles) &&
				  (toAdd = _addQueue.poll()) != null)
			{
				//Copy the particle's data
				_updatingPool[_liveParticles++].copyFrom(toAdd);
				//Mark it as ready, in case it is an emitter's member particle -
				//see Emitter.emit()
				toAdd._ready = true;
			}
			_addQueue.clear();
			
			//Time to swap the buffers
			//Synchronize in case we are currently drawing the ready buffer
			synchronized (_readyPool)
			{
				//Temporary buffer for swapping
				Particle[] tempBuffer;
				//Swap the ready and updating pools
				tempBuffer = _readyPool;
				_readyPool = _updatingPool;
				_updatingPool = tempBuffer;
				//Update ready live particles
				_readyLiveParticles = _liveParticles;
			}
		}
		
		//We've stopped - indicate that we're ready to start again
		_ready = true;
	}
	
	@Override
	public void draw(SpriteBatch spriteBatch)
	{
		//Syncronize in case we are currently swapping buffers
		synchronized (_readyPool)
		{
			if (_running) {
				//Draw all particles in the ready set
				for(int i = 0; i < _readyLiveParticles; ++i)
				{
					Particle cur = _readyPool[i];
					spriteBatch.setColor(1.0f, 1.0f, 1.0f, cur._alpha);
					spriteBatch.draw(cur._texture, cur._x-cur._originX, cur._y-cur._originY, cur._originX, cur._originY, cur._width, cur._height, cur._scale, cur._scale, cur._rot);
				}
			}
		}
	}
	
	public void addParticle(Particle p)
	{
		//Add the particle to the queue of particles
		//to be added to the pool itself
		_addQueue.add(p);
	}
}
