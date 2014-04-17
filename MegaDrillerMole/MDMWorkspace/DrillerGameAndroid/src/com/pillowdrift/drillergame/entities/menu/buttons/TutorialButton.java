package com.pillowdrift.drillergame.entities.menu.buttons;

import com.pillowdrift.drillergame.framework.Scene;

public class TutorialButton extends GenericMenuButton {
	public TutorialButton(Scene scene) {
		super(scene, "Tutorial");
	}
	
	@Override
	protected void onRelease() {
		super.onRelease();
		
		// Activate the tutorial.
		_parent.getOwner().getScene("TutorialScene").activate();
		_parent.getOwner().getScene("MenuScene").deactivate();
	}
}
