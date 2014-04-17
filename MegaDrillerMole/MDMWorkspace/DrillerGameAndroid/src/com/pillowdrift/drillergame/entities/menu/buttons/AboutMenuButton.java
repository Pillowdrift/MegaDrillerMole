package com.pillowdrift.drillergame.entities.menu.buttons;

import com.pillowdrift.drillergame.framework.Scene;

/**
 * Button to move to the trophies scene from the main menu scene.
 * @author cake_cruncher_7
 *
 */
public class AboutMenuButton extends GenericMenuButton
{
	//CONSTRUCTION
	public AboutMenuButton(Scene parent) {
		super(parent, "About");
	}

	//FUNCTION
	@Override
	protected void onRelease()
	{
		super.onRelease();
		
		//Activate the trophies scene
		_parent.getOwner().getScene("AboutScene").activate();
		//Deactivate our parent scene
		_parent.deactivate();
	}
}
