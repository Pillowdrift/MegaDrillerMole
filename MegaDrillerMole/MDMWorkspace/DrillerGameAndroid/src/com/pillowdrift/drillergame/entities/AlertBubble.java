package com.pillowdrift.drillergame.entities;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.pillowdrift.drillergame.framework.GameEntity;
import com.pillowdrift.drillergame.framework.Sprite;
import com.pillowdrift.drillergame.scenes.GameScene;

/**
 * An image and text display at some point in the gamefield.
 * Fades out at the given rate
 * @author cake_cruncher_7
 *
 */
public abstract class AlertBubble extends GameEntity
{
	//DATA
	float _timeToLive = 5.0f;
	float _alpha = 1.0f;
	float _alphaFadeout = 0.2f;
	String _text01 = "Default Text";
	float _text01ScaleX = 1.0f;
	float _text01ScaleY = 1.0f;
	float _text01OffsetX = 0.0f;
	float _text01OffsetY = 0.0f;
	static BitmapFont _font01;
	
	//ACCESS
	public float getTimeToLive()
	{
		return _timeToLive;
	}
	public void setTimeToLive(float seconds)
	{
		_timeToLive = seconds;
	}
	public float getAlpha()
	{
		return _alpha;
	}
	public void setAlpha(float alpha)
	{
		_alpha = alpha;
	}
	public float getAlphaFadeout()
	{
		return _alphaFadeout;
	}
	public void setAlphaFadeout(float fadePerSecond)
	{
		_alphaFadeout = fadePerSecond;
	}
	public String getText01()
	{
		return _text01;
	}
	public void setText01(String text)
	{
		_text01 = text;
	}
	public float getText01ScaleX()
	{
		return _text01ScaleX;
	}
	public void setText01ScaleX(float scale)
	{
		_text01ScaleX = scale;
	}
	public float getText01ScaleY()
	{
		return _text01ScaleY;
	}
	public void setText01ScaleY(float scale)
	{
		_text01ScaleY = scale;
	}
	public float getText01OffsetX()
	{
		return _text01OffsetX;
	}
	public void setText01OffsetX(float offset)
	{
		_text01OffsetX = offset;
	}
	public float getText01OffsetY()
	{
		return _text01OffsetY;
	}
	public void setText01OffsetY(float offset)
	{
		_text01OffsetY = offset;
	}
	
	
	
	//CONSTRUCTION
	public AlertBubble(GameScene parent)
	{
		super(parent);
		
		//Get font
		_font01 = _parent.getResourceManager().getFont("OSP-DIN");
	}

	//FUNCTION
	@Override
	public void update(float dt)
	{
		super.update(dt);
		
		_alpha -= _alphaFadeout*dt;
		_timeToLive -= dt;
		if(_timeToLive <= 0.0f)
		{
			remove();
		}
	}

	@Override
	public void draw(SpriteBatch spriteBatch)
	{
		//Draw sprite
		Sprite cur = _sprites.getCurrentSprite();
		spriteBatch.setColor(1.0f, 1.0f, 1.0f, _alpha);
		spriteBatch.draw(cur.getCurrentFrame(), _x-_originX, _y-_originY, _originX, _originY, cur.getWidth(), cur.getHeight(), _scaleX, _scaleY, _rotation);
		//Draw text
		_font01.setScale(_scaleX * _text01ScaleX, _scaleY * _text01ScaleY);
		_font01.setColor(1.0f, 1.0f, 1.0f, _alpha);
		_font01.drawMultiLine(spriteBatch, _text01, _x + _text01OffsetX, _y + _text01OffsetY);
	}
}
