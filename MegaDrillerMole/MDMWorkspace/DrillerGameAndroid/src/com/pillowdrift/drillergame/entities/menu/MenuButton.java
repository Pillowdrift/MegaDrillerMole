package com.pillowdrift.drillergame.entities.menu;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.BitmapFont.TextBounds;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.pillowdrift.drillergame.framework.Entity;
import com.pillowdrift.drillergame.framework.Scene;
import com.pillowdrift.drillergame.framework.Sprite;

/**
 * Abstract class to provide base functionality of a touch button within menu screens.
 * @author cake_cruncher_7
 *
 */
public abstract class MenuButton extends Entity
{
	//DATA
	protected boolean _pressed = false;
	protected Sprite _overlaySprite;
	protected String _text01 = "Default Text";
	protected float _text01ScaleX = 1.0f;
	protected float _text01ScaleY = 1.0f;
	protected float _text01OriginX = 0.0f;
	protected float _text01OriginY = 0.0f;
	protected float _width = 0.0f;
	protected float _halfWidth = 0.0f;
	protected float _height = 0.0f;
	protected float _halfHeight = 0.0f;
	protected static BitmapFont _font01;
	
	//CONSTRUCTION
	public MenuButton(Scene parent, String text)
	{
		super(parent);
		
		//Get font
		_font01 = _parent.getResourceManager().getFont("OSP-DIN");
				
		//Store text
		_text01 = text;
		
		//Calculate positioning
		TextBounds textBounds = _font01.getBounds(_text01);
		_text01OriginX = (textBounds.width/2.0f);
		_text01OriginY = -(textBounds.height/2.0f);
		
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

	//FUNCTION
	@Override
	public void update(float dt) {
		super.update(dt);
		
		//Update potential overlay animation
		_overlaySprite.update(dt);
		
		//Check for interaction - we only listen to pointer 0
		if(_parent.getInputManager().isTouchedBy(0))
		{
			//If the screen is touched, determine whether it's over our button
			float tX = _parent.getAdjustedTouchX(0);
			float tY = _parent.getAdjustedTouchYFlipped(0);
			if((tX > _x-_halfWidth*_scaleX && tX < _x+_halfWidth*_scaleX) &&
			   (tY > _y-_halfHeight*_scaleY && tY < _y+_halfHeight*_scaleY))
			{
				//Determine whether this is a press for us
				if(_pressed == false)
				{
					_pressed = true;
					//Signal a fresh press
					onPress();
				}
			}
			else
			{
				//Dragging off does not count as a release
				_pressed = false;
			}
		}
		else
		{
			//If the screen is not touched, determine whether this is a release for us
			if(_pressed == true)
			{
				_pressed = false;
				//Signal a fresh release
				onRelease();
			}
		}
		
	}
	
	@Override
	public void draw(SpriteBatch spriteBatch)
	{
		//Draw sprite
		super.draw(spriteBatch);
		//Draw text
		_font01.setScale(_scaleX * _text01ScaleX, _scaleY * _text01ScaleY);
		BitmapFont.TextBounds bounds = _font01.getBounds(_text01);
		_text01OriginX = bounds.width/2.0f;
		_text01OriginY = -bounds.height/2.0f;
		_font01.setColor(1.0f, 1.0f, 1.0f, 1.0f);
		_font01.drawMultiLine(spriteBatch, _text01, _x - _text01OriginX, _y - _text01OriginY);
		//Draw overlay if we're held down
		if(_pressed)
		{
			Sprite cur = _sprites.getCurrentSprite();
			spriteBatch.setColor(1.0f, 1.0f, 1.0f, 1.0f);
			spriteBatch.draw(_overlaySprite.getTexture(), _x-_originX, _y-_originY, _originX, _originY, cur.getWidth(), cur.getHeight(), _scaleX, _scaleY, _rotation);
		}
	}
	
	//CUSTOM
	//Response functions
	protected void onPress()
	{
		//Do nothing by default
	}
	
	protected void onRelease()
	{
		// Play a sound
		_parent.getResourceManager().getSound("button").play();
	}
	
	//Sprite functions - should be overridden in derived classes
	protected Sprite getNormalSprite()
	{
		return null;
	}
	
	protected Sprite getOverlaySprite()
	{
		return null;
	}
}
