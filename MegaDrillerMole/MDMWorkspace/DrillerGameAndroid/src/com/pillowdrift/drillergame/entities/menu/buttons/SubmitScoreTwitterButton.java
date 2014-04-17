package com.pillowdrift.drillergame.entities.menu.buttons;

import com.badlogic.gdx.graphics.g2d.BitmapFont.TextBounds;
import com.pillowdrift.drillergame.framework.Scene;

/**
 * Button that allows the player to twitter to post their score
 * @author strawberries
 *
 */
public class SubmitScoreTwitterButton extends GenericMenuButton {
	
	long _score;
	boolean _pressed = false;
	boolean _areYouSure = false;
	
	public SubmitScoreTwitterButton(Scene parent, long score) {
		super(parent, "Tweet score with Twitter");
		
		_score = score;
		_text01ScaleX = 0.5f;
	}
	
	@Override
	protected void onRelease() {
		super.onRelease();
		
		// Switch to the are you sure.
		if (!_areYouSure) {
			_areYouSure = true;
			_text01 = "Are you sure?";
			TextBounds textBounds = _font01.getBounds(_text01);
			_text01OriginX = (textBounds.width/2.0f);
			_text01OriginY = -(textBounds.height/2.0f);	
		} else if (!_pressed) {
			_parent.getOwner().getGame().getTwitterService().postScoreToTwitter(_score);
			_pressed = true;
			_text01 = "Posted to Twitter!";
			TextBounds textBounds = _font01.getBounds(_text01);
			_text01OriginX = (textBounds.width/2.0f);
			_text01OriginY = -(textBounds.height/2.0f);
		}
	}

}
