package com.pillowdrift.drillergame.entities;

import com.pillowdrift.drillergame.framework.Sprite;
import com.pillowdrift.drillergame.scenes.GameScene;

/**
 * Alert bubble derivative specifically for representing combos
 * @author cake_cruncher_7
 *
 */
public class ComboAlertBubble extends AlertBubble {

	public ComboAlertBubble(GameScene parent, float xPos, float yPos, int comboCount) {
		super(parent);

		//Set starting position and depth
		_x = xPos;
		_y = yPos;
		_depth = 1.0f;
		
		//Set origin
		_originX = 64.0f;
		_originY = 48.0f;
		
		//Set scale
		_scaleX = 0.5f;
		_scaleY = 0.5f;
		
		//Get graphics
		_sprites.addSprite("norm", new Sprite(parent.getResourceManager().getAtlasRegion("atlas01", "alertBubble01"), 96, 0.0f));
		_sprites.setCurrentSpriteName("norm");
		
		//Set text
		_text01 = "x" + comboCount;
		_text01OffsetX = -19.0f;
		_text01OffsetY = 22.0f;
		_text01ScaleX = 2.6f;
		_text01ScaleY = 2.6f;
		
		//Set alpha
		_alphaFadeout = 1.0f;
		_timeToLive = 0.95f;
	}
}
