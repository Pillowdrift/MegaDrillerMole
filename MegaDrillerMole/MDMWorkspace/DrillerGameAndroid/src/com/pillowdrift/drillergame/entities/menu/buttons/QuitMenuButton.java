package com.pillowdrift.drillergame.entities.menu.buttons;

import com.pillowdrift.drillergame.framework.Scene;

/**
 * Button to take you to the QuitScene.
 * @author cake_cruncher_7
 *
 */
public class QuitMenuButton extends GenericMenuButton
{
	//CONSTRUCTION
	public QuitMenuButton(Scene parent) {
		super(parent, "Quit");
	}

	//FUNCTION
	@Override
	protected void onRelease()
	{
		super.onRelease();
		
		//Activate the records scene
		_parent.getOwner().getScene("QuitScene").activate();
		//Deactivate our parent scene
		_parent.deactivate();
	}
}
