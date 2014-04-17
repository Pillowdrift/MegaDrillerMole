package com.pillowdrift.drillergame.entities;

import com.pillowdrift.drillergame.framework.Sprite;
import com.pillowdrift.drillergame.scenes.GameScene;

/**
 * A generic alert bubble for displaying text passed into the constructor.
 * @author cake_cruncher_7
 *
 */
public class GenericTextAlertBubble extends AlertBubble
{
	public GenericTextAlertBubble(GameScene parent, float xPos, float yPos, String text) {
		super(parent);
		
		//Set starting position and depth
		_x = xPos;
		_y = yPos;
		_depth = 1.0f;
		
		//Get graphics
		_sprites.addSprite("norm", new Sprite(parent.getResourceManager().getAtlasRegion("atlas01", "alertBubble01"), 96, 0.0f));
		_sprites.setCurrentSpriteName("norm");
		
		//Set text
		_text01 = text;
		
		//Set alpha
		_alphaFadeout = 1.0f;
		_timeToLive = 0.95f;
	}

}
