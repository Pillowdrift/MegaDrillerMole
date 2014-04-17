package com.pillowdrift.drillergame.entities.menu.buttons;

import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.pillowdrift.drillergame.entities.menu.MenuButton;
import com.pillowdrift.drillergame.framework.Scene;
import com.pillowdrift.drillergame.framework.Sprite;
import com.pillowdrift.drillergame.framework.SpriteCollection;

/**
 * Menu button which takes a string (char array), index, and increment value.
 * The value of the character at the given index will be incremented by the increment
 * value, within the bounds of A and Z (inclusive, naturally).
 * @author cake_cruncher_7
 *
 */
public class CharAdjustMenuButton extends MenuButton
{
	public static final int MINIMUM_CHAR_ACSII = 65;
	public static final int MAXIMUM_CHAR_ACSII = 90;
	
	
	char[] _data;
	int _iChar;
	protected final int _adj;
	
	/**
	 * 
	 * @param parent parent scene of this button
	 * @param data array of chars to affect
	 * @param iChar char index to affect
	 * @param adj direction and amount to increment character by
	 */
	public CharAdjustMenuButton(Scene parent, char[] data, int iChar, int adj)
	{
		super(parent, "");
		
		_data = data;
		_iChar = iChar;
		_adj = adj;
		
		//Change settings for this button
		_sprites = new SpriteCollection();
		//Set up sprites
		//Get normal texture
		_sprites.addSprite("norm", getNormalSprite());
		_sprites.setCurrentSpriteName("norm");
		//Set width and height for collision
		_width = _sprites.getCurrentSprite().getWidth();
		_halfWidth = _width*0.5f;
		_height = _sprites.getCurrentSprite().getHeight();
		_halfHeight = _height*0.5f;
		//Set default depth
		_depth = 5;
		//Set origin to the center
		_originX = _halfWidth;
		_originY = _halfHeight;
		//Get overlay texture
		_overlaySprite = getOverlaySprite();
	}
	
	@Override
	protected void onPress() {
		super.onPress();
		
		//Increment character
		_data[_iChar] += _adj;
		
		//Cap character values
		if(_data[_iChar] < MINIMUM_CHAR_ACSII)
			_data[_iChar] = MAXIMUM_CHAR_ACSII;
		else if(_data[_iChar] > MAXIMUM_CHAR_ACSII)
			_data[_iChar] = MINIMUM_CHAR_ACSII;
	}
	
	//Sprite functions
	@Override
	protected Sprite getNormalSprite()
	{
		if(_adj >= 0)
		{
			AtlasRegion texRegion = _parent.getResourceManager().getAtlasRegion("atlas01", "buttonArrowUp");
			return new Sprite(texRegion, texRegion.getRegionWidth(), 0.0f);
		}
		else
		{
			AtlasRegion texRegion = _parent.getResourceManager().getAtlasRegion("atlas01", "buttonArrowDown");
			return new Sprite(texRegion, texRegion.getRegionWidth(), 0.0f);
		}
	}
	@Override
	protected Sprite getOverlaySprite()
	{
		if(_adj >= 0)
		{
			AtlasRegion texRegion = _parent.getResourceManager().getAtlasRegion("atlas01", "buttonArrowUpoverlay");
			return new Sprite(texRegion, texRegion.getRegionWidth(), 0.0f);
		}
		else
		{
			AtlasRegion texRegion = _parent.getResourceManager().getAtlasRegion("atlas01", "buttonArrowDownoverlay");
			return new Sprite(texRegion, texRegion.getRegionWidth(), 0.0f);
		}
	}
}
