package com.pillowdrift.drillergame.entities.menu.buttons;

import com.pillowdrift.drillergame.framework.Scene;

/**
 * Button to take you to the RecordsScene
 * @author cake_cruncher_7
 *
 */
public class RecordsMenuButton extends GenericMenuButton
{
	//CONSTRUCTION
	public RecordsMenuButton(Scene parent) {
		super(parent, "Hi-Scores");
	}

	//FUNCTION
	@Override
	protected void onRelease()
	{
		super.onRelease();
		
		//Activate the records scene
		_parent.getOwner().getScene("RecordsScene").activate();
		//Deactivate our parent scene
		_parent.deactivate();
	}
}
