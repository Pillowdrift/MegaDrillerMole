package com.pillowdrift.drillergame.entities.menu.buttons;

import com.pillowdrift.drillergame.framework.Scene;

/**
 * Button to go back to the menu scene from the QuitScene.
 * @author cake_cruncher_7
 *
 */
public class WhoTheMenuButton extends GenericMenuButton
{
	//CONSTRUCTION
	public WhoTheMenuButton(Scene parent) {
		super(parent, "No");
		
		_scaleX = 1.0f;
		_text01ScaleX = 1.0f;
	}

	//FUNCTION
	@Override
	protected void onRelease()
	{
		super.onRelease();
		
		//Activate the menu scene
		_parent.getOwner().getScene("MenuScene").activate();
		//Deactivate our parent scene
		_parent.deactivate();
	}
}
