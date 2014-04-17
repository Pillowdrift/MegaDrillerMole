package com.pillowdrift.drillergame.entities.menu.buttons;

import com.pillowdrift.drillergame.framework.Scene;

/**
 * Button to move to the RetryScene from the HighRetryScene when a new highscore has been posted.
 * @author cake_cruncher_7
 *
 */
public class SubmitMenuButton extends GenericMenuButton
{
	//CONSTRUCTION
	public SubmitMenuButton(Scene parent) {
		super(parent, "Submit");
	}

	//FUNCTION
	@Override
	protected void onRelease()
	{
		super.onRelease();
		
		//Activate the records scene
		_parent.getOwner().getScene("RetryScene").activate();
		//Deactivate our parent scene
		_parent.deactivate();
	}
}
