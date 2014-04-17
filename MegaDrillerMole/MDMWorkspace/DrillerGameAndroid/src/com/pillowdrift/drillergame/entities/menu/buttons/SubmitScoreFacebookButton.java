package com.pillowdrift.drillergame.entities.menu.buttons;

import com.badlogic.gdx.graphics.g2d.BitmapFont.TextBounds;
import com.pillowdrift.drillergame.framework.Scene;

public class SubmitScoreFacebookButton extends GenericMenuButton {
	// Vars
	long _score;
	boolean _pressed = false;
	boolean _areYouSure = false;
	
	/**
	 * Create a facebook score button.
	 * @param parent
	 */	
	public SubmitScoreFacebookButton(Scene parent, long score) {
		super(parent, "Post score to Facebook");
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
			_parent.getOwner().getGame().getFacebookService().postScoreToFacebook(_score);
			_pressed = true;
			_text01 = "Posted to Facebook!";
			TextBounds textBounds = _font01.getBounds(_text01);
			_text01OriginX = (textBounds.width/2.0f);
			_text01OriginY = -(textBounds.height/2.0f);
		}
	}
}
