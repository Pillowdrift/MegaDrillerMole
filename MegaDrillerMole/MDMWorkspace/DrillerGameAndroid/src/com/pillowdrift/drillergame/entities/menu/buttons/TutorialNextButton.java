package com.pillowdrift.drillergame.entities.menu.buttons;

import com.pillowdrift.drillergame.entities.Slideshow;
import com.pillowdrift.drillergame.framework.Scene;

public class TutorialNextButton extends GenericMenuButton {
	// Vars
	Slideshow _slideshow;
	
	/**
	 * Create a tutorial next image.
	 * @param scene
	 * @param slideshow
	 */
	public TutorialNextButton(Scene scene, Slideshow slideshow) {
		super(scene, "Next");
		_slideshow = slideshow;
	}
	
	@Override
	protected void onRelease() {
		super.onRelease();
		
		// Back to menu
		if (_slideshow.isLast()) {
			_parent.deactivate();
			_parent.getOwner().getScene("MenuScene").activate();
			_slideshow.next();
			_text01 = "Next";
			return;
		}
		
		// Go to the next item.
		_slideshow.next();
		if (_slideshow.isLast()) {
			_text01 = "Menu";
		}
	}
}
