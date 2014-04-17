package com.pillowdrift.drillergame.framework;

import com.badlogic.gdx.math.Vector2;
import com.pillowdrift.drillergame.scenes.GameScene;

/**
 * Lightweight Entity extension which forces the parent to be an
 * instance of GameScene rather than Scene, providing direct access
 * to methods and data not directly available in Scene.
 * ALL__VELOCITY_X, for example, will be accessed every frame by most
 * entities, so I consider this an optimization.
 * @author cake_cruncher_7
 *
 */
public class GameEntity extends Entity
{
	protected Vector2 _velocity;
	
	//Keep a handle to the parent as a GameScene
	protected GameScene _parentGameScene;
	
	public GameEntity(GameScene parentGameScene)
	{
		super(parentGameScene);
		
		_velocity = new Vector2();
		_parentGameScene = parentGameScene;
	}

	//Function which can be called on this entity when the game is reset
	//Allows the entity to reset or remove itself as appropriate
	public void onGameReset()
	{
		//Do nothing by default
	}

	public Vector2 getVelocity()
	{
		return _velocity;
	}
}
