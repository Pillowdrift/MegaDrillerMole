package com.pillowdrift.drillergame.framework;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * Abstract class representing a single scene within the application.
 * Scenes contain a collection of entities which they maintain by automatically
 * updating and drawing each, based on their own methods.
 * Scenes also remove entities when they are marked for removal.
 * @author cake_cruncher_7
 *
 */
public abstract class Scene implements Comparable<Scene>
{
	//Constants
	protected final int _targetWidth = 720;
	protected final int _targetHeight= 480;
	protected final float _screenToTargetX;
	protected final float _screenToTargetY;
	
	//Nested class to hold a pair of a name and entity
	public class NamedEntityPair
	{
		public String _name;
		public Entity _entity;
		public NamedEntityPair(String name, Entity entity)
		{
			_name = name;
			_entity = entity;
		}
	}
	
	//DATA
	//Collection
	protected SceneManager _owner; //Owner of this scene
	protected boolean _active = false; //Whether or not this scene's owner should update it.
	protected boolean _draw = false; //Whether or not to draw this scene.
	protected int _depth = 0; //Determines the order in which to draw this frame (lower is in front)
	//Entities
	protected Map<String, List<Entity>> _entities; //Collection of all entities controlled by this scene - these are automagically updated and drawn
	protected List<NamedEntityPair> _entitiesToAdd; //Collection of all entities waiting to be added to the main scene
	protected List<Entity> _entitiesToDraw; // Collection of all entities to draw (cached sorted list).
	protected boolean _drawListDirty = true;
	//Screen drawing properties
	protected final int _screenWidth; //Width and height of the display - this should never change, so we'll only bother to get it once
	protected final int _screenHeight;
	protected SpriteBatch _spriteBatch;
	
	//ACCESS
	public boolean getDraw() {
		return _draw;
	}
	public void setDraw(boolean draw) {
		_draw = draw;
	}
	public boolean getActive() {
		return _active;
	}
	public void setActive(boolean active) {
		_active = active;
	}
	public SceneManager getOwner()
	{
		return _owner;
	}
	public boolean isActive()
	{
		return _active;
	}
	public int depth()
	{
		return _depth;
	}
	public int getTargetWidth()
	{
		return _targetWidth;
	}
	public int getTargetHeight()
	{
		return _targetHeight;
	}
	public int getScreenWidth()
	{
		return _screenWidth;
	}
	public int getScreenHeight()
	{
		return _screenHeight;
	}
	public ResourceManager getResourceManager()
	{
		return _owner.getResourceManager();
	}
	public InputManager getInputManager()
	{
		return _owner.getInputManager();
	}
	
	
	//CONSTRUCTION
	public Scene(SceneManager owner)
	{
		//Store owner
		_owner = owner;
		//Initialize screen resolution
		_screenWidth = Gdx.graphics.getWidth();
		_screenHeight = Gdx.graphics.getHeight();
		//Create a factor to scale input positions by for correction on different resolutions
		_screenToTargetX = (float)_targetWidth / (float)_screenWidth;
		_screenToTargetY = (float)_targetHeight / (float)_screenHeight;
		//Initialize entity collection
		_entities = new HashMap<String, List<Entity>>();
		//Initialize collection of entities waiting to be added
		_entitiesToAdd = new ArrayList<NamedEntityPair>();
		//The draw list
		_entitiesToDraw = new ArrayList<Entity>();
		
		
		//Initialize sprite batch
		_spriteBatch = new SpriteBatch();
		_spriteBatch.getProjectionMatrix().setToOrtho2D(0, 0, _targetWidth, _targetHeight);
	}
	
	
	//FUNCTION
	public void update(float dt)
	{
		//Create a list of empty entity list's keys for removal after updating
		List<String> emptyLists = new ArrayList<String>();
		//Run through and update all of our entities
		for(Map.Entry<String, List<Entity>> le : _entities.entrySet())
		{
			//Loop through each name
			List<Entity> lev = le.getValue();
			for(int e = 0; e < lev.size(); ++e)
			{
				//Loop through each entity with this name
				Entity curEntity = lev.get(e);
				if(curEntity.getRemoveFlag())
				{
					//Remove this entity if it is marked for removal
					curEntity.onRemove();
					lev.remove(e);
					
					// Set the dirty flag, so that the draw
					// list will be regenerated.
					_drawListDirty = true;
				}
				else
				{
					//Update it if it is not marked for removal
					curEntity.update(dt);
				}
			}
			
			//If the name is no longer used mark it for removal
			if(lev.size() == 0)
				emptyLists.add(le.getKey());
		}
		
		//Add any entities that are waiting to be added
		while(_entitiesToAdd.size() > 0)
		{
			//Get the first entity from the collection of waiting entities
			NamedEntityPair curPair = _entitiesToAdd.get(0);
			//Retrieve it's name and self
			String name = curPair._name;
			Entity entity = curPair._entity;
			
			//Check if we already have a list for it's name
			if(_entities.containsKey(name))
			{
				//Add the entity to the list
				_entities.get(name).add(entity);
			}
			else
			{
				//Create a list for this name and add our entity to it
				_entities.put(name, new ArrayList<Entity>());
				_entities.get(name).add(entity);
			}
			
			//Remove this entity from _entitiesToAdd
			_entitiesToAdd.remove(0);
		}
		
		//Look through and remove each empty list that is still empty
		for(String s : emptyLists)
			if(_entities.get(s).size() == 0)
				_entities.remove(s);
	}
	
	public void draw()
	{
		//Draw entities
		if (_drawListDirty) {
			//Copy the collection to a single list and sort them for drawing
			_entitiesToDraw.clear();
			for(List<Entity> le : _entities.values())
				_entitiesToDraw.addAll(le);
			//Sort it
			Collections.sort(_entitiesToDraw);
			_drawListDirty = false;
		}
		
		//Start drawing
		_spriteBatch.begin();
		//Draw every entity in the sorted collection
		for(Entity e : _entitiesToDraw)
			if(e.getVisibleFlag())
				e.draw(_spriteBatch);
		//End drawing
		_spriteBatch.end();
	}
	
	/**
	 * Called when the application is paused
	 */
	public void pause()
	{
		//Nothing by default
	}
	/**
	 * Called when the application is resumed
	 */
	public void resume()
	{
		//Nothing by default
	}
	/**
	 * Called when the application is terminated
	 */
	public void release()
	{
		//Nothing by default
	}
	
	/**
	 * Called to activate this scene, making it visible
	 * and causing it to update itself and it's components
	 */
	public void activate()
	{
		if(_active != true)
		{
			_active = true;
			_draw = true;
			onActivate();
		}
	}
	/**
	 * Called to deactivate this scene, making it invisible
	 * and causing it not to be updated by the scene manager
	 */
	public void deactivate()
	{
		if(_active == true)
		{
			_active = false;
			_draw = false;
			onDeactivate();
		}
	}
	/**
	 * Called to reset the scene even if it's already active
	 * onActivate will be called
	 */
	public void reset()
	{
		_active = true;
		onActivate();
	}
	/**
	 * Called on successful activation
	 */
	public void onActivate()
	{
		//Nothing by default
	}
	/**
	 * Called on successful deactivation
	 */
	public void onDeactivate()
	{
		//Nothing by default
	}
	
	/**
	 * Function to add an entity to our collection. The
	 * entity will be added after the current update loop.
	 * @param name
	 * @param entity
	 * @return
	 */
	public Entity addEntity(String name, Entity entity)
	{
		// Set dirty flag
		_drawListDirty = true;
		
		_entitiesToAdd.add(new NamedEntityPair(name, entity));
		entity.setName(name);
		return entity;
	}
	/**
	 * Returns the first entity with the given name
	 * @param name
	 * @return
	 */
	public Entity getEntityFirst(String name)
	{
		if(!_entities.containsKey(name)) return null;
		if(_entities.get(name).isEmpty()) return null;
		//We have it!
		return _entities.get(name).get(0);
	}
	/**
	 * Returns all entities with the given name
	 * @param name
	 * @return
	 */
	public List<Entity> getEntities(String name)
	{
		if(!_entities.containsKey(name)) return null;
		//We have it!
		return _entities.get(name);
	}
	
	/**
	 * Get the X touch position of any input, adjusted from the
	 * screen resolution to our target resolution
	 * @return
	 */
	public float getAdjustedTouchX()
	{
		return (float)_owner.getInputManager().getFirstTouchedPositionX() * _screenToTargetX;
	}
	/**
	 * Get the Y touch position of any input, adjusted from the
	 * screen resolution to our target resolution
	 * @return
	 */
	public float getAdjustedTouchY()
	{
		return (float)_owner.getInputManager().getFirstTouchedPositionY() * _screenToTargetY;
	}
	/**
	 * Get the X touch position of a specific input, adjusted from the
	 * screen resolution to our target resolution
	 * @return
	 */
	public float getAdjustedTouchX(int pointer)
	{
		return (float)_owner.getInputManager().getPointerPositionX(pointer) * _screenToTargetX;
	}
	/**
	 * Get the Y touch position of a specific input, adjusted from the
	 * screen resolution to our target resolution
	 * @return
	 */
	public float getAdjustedTouchY(int pointer)
	{
		return (float)_owner.getInputManager().getPointerPositionY(pointer) * _screenToTargetY;
	}
	/**
	 * Get the Y touch position of a specific input, adjusted from the
	 * screen resolution to our target resolution and flipped vertically
	 * to match our graphics output / entity coordinate system.
	 * @return
	 */
	public float getAdjustedTouchYFlipped(int pointer)
	{
		return Math.abs(_targetHeight - (float)_owner.getInputManager().getPointerPositionY(pointer) * _screenToTargetY);
	}
	
	/**
	 * Inform all entities that they are being paused.
	 */
	public void pauseEntities() {
		for(List<Entity> le : _entities.values())
			for(Entity e : le)
				e.pause();
	}
	
	/**
	 * Inform all entities that they are being unpaused.
	 */
	public void unpauseEntities() {
		for(List<Entity> le : _entities.values())
			for(Entity e : le)
				e.resume();
	}	
	
	//Sorting function
	/**
	 * Compares scenes based on depth, for sorting
	 */
	public int compareTo(Scene scene)
	{
		if(scene == null) return 1;
		
		if(_depth > scene._depth)
		{
			return -1;
		}
		else if(_depth < scene._depth)
		{
			return 1;
		}
		else
		{
			return 0;
		}
	}
}
