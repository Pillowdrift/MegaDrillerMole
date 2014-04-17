package com.pillowdrift.drillergame.framework;

import java.util.HashMap;
import java.util.Map;

/**
 * A collection of named sprites which can be switched out to allow an entity to
 * animate differently under different circumstances.
 * @author cake_cruncher_7
 *
 */
public final class SpriteCollection {
	//DATA
	private Map<String, Sprite> _spriteMap;
	String _currentSprite = "";
	
	//ACCESS
	//Function to retrieve the current sprite
	public Sprite getCurrentSprite()
	{
		return _spriteMap.get(_currentSprite);
	}
	//Function to retrieve the name of the current sprite
	public String getCurrentSpriteName()
	{
		return _currentSprite;
	}
	//Function to select the current sprite and reset to frame zero if it differs from the current
	public void setCurrentSpriteName(String name)
	{
		if(_currentSprite != name)
		{
			_currentSprite = name;
			_spriteMap.get(name).setCurrentFrameNumber(0);
		}
	}
	//Function to select the  current sprite and force reset to frame zero
	public void setCurrentSpriteNameOrReset(String name)
	{
		_currentSprite = name;
		_spriteMap.get(name).setCurrentFrameNumber(0);
	}
	//Function to add a sprite
	public void addSprite(String name, Sprite sprite)
	{
		//Add the sprite to our collection
		_spriteMap.put(name, sprite);
		//If it is our only sprite, make it our active sprite
		if(_spriteMap.size() == 1)
		{
			_currentSprite = name;
		}
	}
	//Function to remove a sprite
	public void removeSprite(String name)
	{
		_spriteMap.remove(name);
	}
	
	
	//CONSTRUCTION
	public SpriteCollection()
	{
		//Initialise our hash map
		_spriteMap = new HashMap<String, Sprite>();
	}
	
	//FUNCTION
	//Update the current sprite
	public void update(float dt)
	{
		_spriteMap.get(_currentSprite).update(dt);
	}
	
}
