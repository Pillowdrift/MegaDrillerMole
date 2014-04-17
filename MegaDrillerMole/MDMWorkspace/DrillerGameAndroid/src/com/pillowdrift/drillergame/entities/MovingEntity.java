package com.pillowdrift.drillergame.entities;

import com.badlogic.gdx.math.Vector2;
import com.pillowdrift.drillergame.framework.GameEntity;
import com.pillowdrift.drillergame.scenes.GameScene;

/**
 * An entity with a velocity field
 * 
 * @author Alex Cunliffe
 *
 */
public class MovingEntity
	extends GameEntity
{
	protected Vector2 _velocity;

	public MovingEntity(GameScene parentGameScene)
	{
		super(parentGameScene);
	}
	
	public Vector2 getVelocity()
	{
		return _velocity;
	}
}
