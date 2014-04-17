package com.pillowdrift.drillergame.framework;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

/**
 * Abstract class which represents the basic requirements of an item that is dynamically managed
 * and displayed by a Scene. Entities only have properties pertaining to their aesthetics by default.
 * The update and draw methods should be overridden so that the entity is updated and drawn automatically
 * as required.
 * @author cake_cruncher_7
 *
 */
public abstract class Entity implements Comparable<Entity>
{
	//DATA
	protected boolean _removeFlag = false;
	protected boolean _visibleFlag = true;
	protected Scene _parent;
	protected float _depth = 0;
	protected SpriteCollection _sprites;
	protected String _name = "";
	
	// Color
	protected Color _color = Color.WHITE;
	
	//Spatial data
	//Pos
	protected float _x = 0.0f;
	protected float _y = 0.0f;
	//Rot
	protected float _rotation = 0.0f;
	//Scale
	protected float _scaleX = 1.0f;
	protected float _scaleY = 1.0f;
	//Origin
	protected float _originX = 0.0f;
	protected float _originY = 0.0f;
	
	
	//ACCESS
	public void setRemoveFlag() {
		_removeFlag = true;
	}
	public float getRotation() {
		return _rotation;
	}
	public void setRotation(float rot) {
		_rotation = rot;
	}
	public Color getColor() {
		return _color;
	}
	public void setColor(Color color) {
		_color = color;
	}
	public String getName() {
		return _name;
	}
	public void setName(String name) {
		_name = name;
	}
	public float getPosX()
	{
		return _x;
	}
	public float getPosY()
	{
		return _y;
	}
	public void setPosX(float x)
	{
		_x = x;
	}
	public void setPosY(float y)
	{
		_y = y;
	}
	public void setPos(float x, float y)
	{
		_x = x;
		_y = y;
	}
	public Vector2 getPosV2()
	{
		return new Vector2(_x, _y);
	}
	public Vector3 getPosV3()
	{
		return new Vector3(_x, _y, 0.0f);
	}
	public float getDepth()
	{
		return _depth;
	}
	public void setDepth(float depth)
	{
		_depth = depth;
	}
	public float getScaleX()
	{
		return _scaleX;
	}
	public float getScaleY()
	{
		return _scaleY;
	}
	public void setScaleX(float scale)
	{
		_scaleX = scale;
	}
	public void setScaleY(float scale)
	{
		_scaleY = scale;
	}
	public void setScale(float x, float y)
	{
		_scaleX = x;
		_scaleY = y;
	}
	public boolean getRemoveFlag()
	{
		return _removeFlag;
	}
	public boolean getVisibleFlag()
	{
		return _visibleFlag;
	} 
	
	
	//CONSTRUCTION
	public Entity(Scene parent)
	{
		_parent = parent;
		_sprites = new SpriteCollection();
		
		//Add default sprite
		Sprite def = new Sprite(_parent.getResourceManager().getAtlasRegion("atlas01", "noSprite"), 32, 0.0f);
		_sprites.addSprite("default", def);
	}
	
	
	//FUNCTION
	public void update(float dt)
	{
		//Advance sprite by default
		_sprites.update(dt);
	}
	
	public void draw(SpriteBatch spriteBatch)
	{
		//Draw sprite by default
		Sprite cur = _sprites.getCurrentSprite();
		spriteBatch.setColor(_color);
		spriteBatch.draw(cur.getCurrentFrame(), _x-_originX, _y-_originY, _originX, _originY, cur.getWidth(), cur.getHeight(), _scaleX, _scaleY, _rotation);
	}
	
	public void remove()
	{
		_removeFlag = true;
	}
	
	public int compareTo(Entity entity)
	{
		if(entity == null) return 1;
		
		if(_depth > entity._depth)
		{
			return -1;
		}
		else if(_depth < entity._depth)
		{
			return 1;
		}
		else
		{
			return 0;
		}
	}
	
	/**
	 * Called when the game is paused.
	 */
	public void pause() {
	}
	
	/**
	 * Called when the game is unpaused.
	 */
	public void resume() {
	}
	
	//EVENT FUNCTIONS
	public void onRemove()
	{
		
	}
}
