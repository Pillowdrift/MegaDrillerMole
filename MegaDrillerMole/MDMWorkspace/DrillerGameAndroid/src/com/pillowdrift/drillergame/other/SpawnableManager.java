package com.pillowdrift.drillergame.other;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.EmptyStackException;
import java.util.List;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.pillowdrift.drillergame.entities.ManagableSpawnable;
import com.pillowdrift.drillergame.framework.GameEntity;
import com.pillowdrift.drillergame.framework.Utils;
import com.pillowdrift.drillergame.scenes.GameScene;

/**
 * Generic class to manage a collection of ManagableSpawnable entities.
 * @author cake_cruncher_7
 *
 */
public class SpawnableManager extends GameEntity
{
	private List<ManagableSpawnable> _pool; //Pool of instances we are able to allocate presently
	private float _minMaxTimeToSpawn;
	private float _maxMaxTimeToSpawn;
	private float _minMinTimeToSpawn;
	private float _maxMinTimeToSpawn;
	private float _maxTime;
	private float _timeToSpawn;
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public SpawnableManager(Class<? extends ManagableSpawnable> spawnableClass, GameScene parentGameScene, int numToGenerate,
								float minMaxTimeToSpawn, float maxMaxTimeToSpawn, float minMinTimeToSpawn, float maxMinTimeToSpawn,
									float maxTime)
	{
		super(parentGameScene);
		
		// Set the spawn times.
		_minMaxTimeToSpawn = minMaxTimeToSpawn;
		_maxMaxTimeToSpawn = maxMaxTimeToSpawn;
		_minMinTimeToSpawn = minMinTimeToSpawn;
		_maxMinTimeToSpawn = maxMinTimeToSpawn;
		_maxTime = maxTime;
		_timeToSpawn = 0;
		
		// Interpolate between minMaxSpawn and minMinSpawn
		float minSpawn = _minMaxTimeToSpawn - ((this._parentGameScene.getGameTime() / _maxTime) * (_minMaxTimeToSpawn - _minMinTimeToSpawn));
		float maxSpawn = _maxMaxTimeToSpawn - ((this._parentGameScene.getGameTime() / _maxTime) * (_maxMaxTimeToSpawn - _maxMinTimeToSpawn));
		if (minSpawn < _minMinTimeToSpawn)	
			minSpawn = _minMinTimeToSpawn;
		if (maxSpawn < _maxMinTimeToSpawn)
			maxSpawn = _maxMinTimeToSpawn;
		_timeToSpawn = Utils.randomF(minSpawn, maxSpawn);		
		
		// Allocate the pool.
		_pool = new ArrayList<ManagableSpawnable>();
		
		try {
			//Obtain the required constructor for class
			Class t = spawnableClass;
			//Specify the arguments
			Class[] ctorArgs = new Class[2];
			ctorArgs[0] = GameScene.class;
			ctorArgs[1] = SpawnableManager.class;
			Constructor<? extends ManagableSpawnable> tConstructor;
			tConstructor = t.getConstructor(ctorArgs);
			
			//Create the requested number of instances of T
			for(int i = 0; i < numToGenerate; ++i)
			{
				_pool.add((ManagableSpawnable) tConstructor.newInstance(parentGameScene, this));
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	@Override
	public void update(float dt) {
		super.update(dt);
		
		// Reduce the spawn time.
		_timeToSpawn -= dt;
		if (_timeToSpawn < 0) {
			// Interpolate between minMaxSpawn and minMinSpawn
			float minSpawn = _minMaxTimeToSpawn - ((this._parentGameScene.getGameTime() / _maxTime) * (_minMaxTimeToSpawn - _minMinTimeToSpawn));
			float maxSpawn = _maxMaxTimeToSpawn - ((this._parentGameScene.getGameTime() / _maxTime) * (_maxMaxTimeToSpawn - _maxMinTimeToSpawn));
			if (minSpawn < _minMinTimeToSpawn)	
				minSpawn = _minMinTimeToSpawn;
			if (maxSpawn < _maxMinTimeToSpawn)
				maxSpawn = _maxMinTimeToSpawn;
			_timeToSpawn = Utils.randomF(minSpawn, maxSpawn);
			
			// Spawn one.
			if (availability() != 0)
				_parent.addEntity(_name + "_spawn", getSpawnable());
		}
	}
	
	@Override
	public void draw(SpriteBatch spriteBatch) {
		// Suppress drawing by not calling super =)
	}
	
	//Return an unallocated member of our collection and activate it.
	public ManagableSpawnable getSpawnable()
	{
		if(_pool.isEmpty())
		{
			throw new EmptyStackException();
		}
		else
		{
			ManagableSpawnable ret = _pool.get(0);
			_pool.remove(0);
			ret.activate();
			return ret;
		}
	}
	
	//Absorb a new member for our collection.
	public void giveSpawnable(ManagableSpawnable sp)
	{
		_pool.add(sp);
	}
	
	//Report how many spawnables we have in the collection
	public int availability()
	{
		return _pool.size();
	}
}