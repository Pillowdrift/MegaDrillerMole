package com.pillowdrift.drillergame.entities.menu;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.BitmapFont.TextBounds;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.pillowdrift.drillergame.framework.Entity;
import com.pillowdrift.drillergame.framework.Scene;

/**
 * Simple entity which allows the easy positioning of text within menus.
 * @author cake_cruncher_7
 *
 */
public class MenuText extends Entity
{
	//CONSTANTS
	public static final class Alignment
	{
		public static final int ALIGN_CNTR = 0;
		public static final int ALIGN_LFT = 1;
		public static final int ALIGN_RGHT = 2;
	}
	
	//DATA
	protected String _text01 = "Default Text";
	protected int _alignment;
	protected Color _colour;
	protected float _width = 0.0f;
	protected float _height = 0.0f;
	protected static BitmapFont _font01;
	
	//ACCESS
	public void setColour(Color colour)
	{
		_colour = colour;
	}
	public void setText(String text)
	{
		_text01 = text;
		//Calculate positioning
		TextBounds textBounds = _font01.getBounds(_text01);
		if(_alignment == Alignment.ALIGN_CNTR)
		{
			_originX = (textBounds.width/2.0f);
			_originY = -(textBounds.height/2.0f);
		}
		else if(_alignment == Alignment.ALIGN_LFT)
		{
			_originX = 0.0f;
			_originY = -textBounds.height;
		}
		else if(_alignment == Alignment.ALIGN_RGHT)
		{
			_originX = textBounds.width;
			_originY = -textBounds.height;
		}
	}
	
	//CONSTRUCTION
	public MenuText(Scene parent, String text, int alignment, float scaleX, float scaleY, Color colour)
	{
		super(parent);
		
		//Get font
		_font01 = _parent.getResourceManager().getFont("OSP-DIN");
				
		//Store text
		_text01 = text;
		
		//Store colour
		_colour = colour;
		
		//Setup text
		_alignment = alignment;
		setText(text);
		
		//Store scale
		_scaleX = scaleX;
		_scaleY = scaleY;
		
		//Set default depth
		_depth = 5;
	}

	//FUNCTION
	/**
	 * Override update to void updating the non-existent texture
	 */
	@Override
	public void update(float dt)
	{	
		//Do nothing
	}
	
	@Override
	public void draw(SpriteBatch spriteBatch)
	{
		//Draw text
		_font01.setScale(_scaleX, _scaleY);
		setText(_text01);
		_font01.setColor(_colour);
		_font01.drawMultiLine(spriteBatch, _text01, _x - _originX, _y - _originY);
	}
}
