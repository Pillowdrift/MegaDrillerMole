package com.pillowdrift.drillergame.framework;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.pillowdrift.drillergame.DrillerGame01;

/**
 * Class to manage a collection of scenes and services such as the Input and Resource managers.
 * @author cake_cruncher_7
 *
 */
public final class SceneManager
{
	//DATA
	protected Map<String, Scene> _scenes; 			//Collection of the screens managed by this screen manager
	protected boolean _sortedScenesIsDirty = false; //Boolean value indicating that our sorted collection is out-dated.
	protected List<Scene> _sortedScenes; 			//Collection of the scenes sorted by depth
	protected ResourceManager _resourceManager; 	//Resource manager for this scene manager - will hold our textures, sounds, etc
	protected InputManager _inputManager;			//Input manager for this scene manager
	protected Color _clearColour = Color.BLACK;		//The colour to clear the screen to every frame
	protected DrillerGame01 _game = null;			//The game we belong to
	
	//ACCESS
	public ResourceManager getResourceManager()
	{
		return _resourceManager;
	}
	public InputManager getInputManager()
	{
		return _inputManager;
	}
	public void setClearColour(Color colour)
	{
		_clearColour = colour;
	}
	public Color getClearColour()
	{
		return _clearColour;
	}
	public DrillerGame01 getGame()
	{
		return _game;
	}
	
	
	//CONSTRUCTION
	public SceneManager(DrillerGame01 game)
	{
		//Store game reference
		_game = game;
		//Initialize collections
		_scenes = new HashMap<String, Scene>();
		_sortedScenes = new ArrayList<Scene>();
		//Initialize resource manager
		_resourceManager = new ResourceManager();
		//Initialize input manager
		_inputManager = new InputManager();
		//Mark input manager as our intput processor
		Gdx.input.setInputProcessor(_inputManager);
	}
	
	//FUNCTION
	public void update(float dt)
	{
		//If sorted collection is out-dated, regenerate it
		if(_sortedScenesIsDirty)
		{
			_sortedScenes = new ArrayList<Scene>();
			_sortedScenes.addAll(_scenes.values());
			Collections.sort(_sortedScenes);
		}
		
		//Run through all of our scenes and update them
		for(Scene s : _sortedScenes)
		{
			if(s.isActive())
			{
				s.update(dt);
			}
		}
	}
	
	public void draw()
	{

		GL10 gl = Gdx.graphics.getGL10();
		//Set the clear colour
		gl.glClearColor(_clearColour.r, _clearColour.g, _clearColour.b, _clearColour.a);
		//Clear the screen
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
		
		//Run through all of our scenes and draw them
		for(Scene s : _sortedScenes)
		{
			if(s.getDraw())
			{
				s.draw();
			}
		}
	}
	
	/**
	 * Call to pause all scenes
	 */
	public void pause()
	{
		//Pause all scenes
		for(Scene s : _sortedScenes)
		{
			s.pause();
		}
	}
	/**
	 * Call to resume all scenes
	 */
	public void resume()
	{
		//Resume all scenes
		for(Scene s : _sortedScenes)
		{
			s.resume();
		}
	}
	/**
	 * Called to deactivate and shutdown all of our scenes
	 */
	public void release()
	{
		//Deactivate and release all scenes
		for(Scene s : _sortedScenes)
		{
			s.deactivate();
			s.release();
		}
	}
	
	/*
	 * Attempts to add a scene to the manager.
	 * Scenes must have unique names.
	 * Returns a boolean indicating success or failure.
	 */
	public boolean addScene(String name, Scene newScene)
	{
		if(!_scenes.containsKey(name))
		{
			_scenes.put(name, newScene);
			_sortedScenesIsDirty = true;
			return true; //Success!
		}
		return false; //Failed to add new scene
	}
	/*
	 * Attempts to return the scene with the given name.
	 */
	public Scene getScene(String name)
	{
		if(_scenes.containsKey(name))
		{
			//We have it, so return it!
			return _scenes.get(name);
		}
		return null; //We do not have this scene, return null
	}
	/*
	 * Attempts to remove a scene from the manager.
	 * Returns false if the scene was not found.
	 */
	public boolean removeScene(String name)
	{
		if(_scenes.containsKey(name))
		{
			_scenes.remove(name);
			_sortedScenesIsDirty = true;
			return true; //Success!
		}
		return false; //Failed to remove scene
	}
}
