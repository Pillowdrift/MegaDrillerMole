package com.pillowdrift.drillergame.entities;

import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.pillowdrift.drillergame.framework.GameEntity;
import com.pillowdrift.drillergame.framework.Sprite;
import com.pillowdrift.drillergame.scenes.GameScene;

/**
 * Class to represent a column of terrain, sky, background, etc
 * Should be inherited to change the image used and other properties.
 * Note that the images used for these classes should be two wider than
 * is required, in order to avoid gaps appearing between columns.
 * @author cake_cruncher_7
 *
 */
public abstract class WorldPiece extends GameEntity
{
	int _width; //Width of our image
	float _widthMOne; //Width of our image minus one
	float _setWidth; //Width of the set to which we belong
	float _movFactor = 1.0f; //Amount of the GameScene's X velocity which affects us
	
	/**
	 * Basic constructor for a world piece.
	 * @param parent GameScene which owns this column
	 * @param xColumn Id in the set of columns to which we belong - used to calculate our starting position
	 * @param totalColumns number of columns in our set - used to effectively cycle column positions
	 * @param yPos the yPosition of the top left of the image
	 */
	public WorldPiece(GameScene parent, float xColumn, float totalColumns, float yPos)
	{
		super(parent);
		//Get texture
		AtlasRegion texRegion = parent.getResourceManager().getAtlasRegion(getAtlasName(), getRegionName());
		_sprites.addSprite("stat", new Sprite(texRegion, texRegion.getRegionWidth(), 0.0f));
		_sprites.setCurrentSpriteName("stat");
		
		_width = _sprites.getCurrentSprite().getWidth();
		_widthMOne = _width-1;
		
		//Calculate X position base on the given Id and image width
		_x = xColumn * _widthMOne;
		//Calculate set width based on totalColumns and image width
		_setWidth = (totalColumns) * _widthMOne;
		//Set y position to the given coordinate
		_y = yPos;
		//Set default depth
		_depth = 100;
		
		//Set y origin to the top of the image
		_originY = _sprites.getCurrentSprite().getHeight();
	}

	@Override
	public void update(float dt)
	{
		//Advance with the world
		_x += _parentGameScene.ALL_VELOCITY_X*_movFactor*dt;
		
		if(_x < -_widthMOne)
		{
			_x += _setWidth;
		}
	}
	
	//Texture functions - should be overridden in derived classes
	protected String getAtlasName()
	{
		return "invalidAtlas";
	}
	
	protected String getRegionName()
	{
		return "invalidRegion";
	}
}
