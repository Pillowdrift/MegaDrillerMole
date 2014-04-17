package com.pillowdrift.drillergame.entities;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.pillowdrift.drillergame.framework.Sprite;
import com.pillowdrift.drillergame.scenes.GameScene;

/**
 * Alert bubble derrivative for displaying height bonuses gained when dropped off a load into the train
 * @author cake_cruncher_7
 *
 */
public class HeightAlertBubble extends AlertBubble
{
	String _text02 = "Default Text";
	float _text02ScaleX = 1.0f;
	float _text02ScaleY = 1.0f;
	float _text02OffsetX = 0.0f;
	float _text02OffsetY = 0.0f;
	
	public HeightAlertBubble(GameScene parent, float xPos, float yPos, int height)
	{
		super(parent);

		//Set starting position and depth
		_x = xPos;
		_y = yPos;
		_depth = 1.0f;
		
		//Set origin
		_originX = 64.0f;
		_originY = 48.0f;
		
		//Set scale
		_scaleX = 0.8f;
		_scaleY = 0.8f;
		
		//Get graphics
		_sprites.addSprite("norm", new Sprite(parent.getResourceManager().getAtlasRegion("atlas01", "alertBubble01"), 96, 0.0f));
		_sprites.setCurrentSpriteName("norm");
		
		//Set text01
		_text01 = "Height Bonus:";
		_text01OffsetX = -100.0f;
		_text01OffsetY = 60.0f;
		_text01ScaleX = 1.0f;
		_text01ScaleY = 1.0f;
		//Set text02
		_text02 = "" + height;
		_text02OffsetX = -26.0f;
		_text02OffsetY = 24.0f;
		_text02ScaleX = 1.5f;
		_text02ScaleY = 1.5f;
		
		//Set alpha
		_alphaFadeout = 1.0f;
		_timeToLive = 0.95f;
	}

	@Override
	public void draw(SpriteBatch spriteBatch)
	{
		super.draw(spriteBatch);
		
		//Draw text
		_font01.setScale(_scaleX * _text02ScaleX, _scaleY * _text02ScaleY);
		_font01.setColor(1.0f, 1.0f, 1.0f, _alpha);
		_font01.drawMultiLine(spriteBatch, _text02, _x + _text02OffsetX, _y + _text02OffsetY);
	}
}
