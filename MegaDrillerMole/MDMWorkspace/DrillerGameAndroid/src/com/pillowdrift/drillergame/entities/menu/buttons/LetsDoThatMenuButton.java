package com.pillowdrift.drillergame.entities.menu.buttons;

import com.badlogic.gdx.Gdx;
import com.pillowdrift.drillergame.framework.Scene;

/**
 * Button fulfilling the specific requirements of quitting the game from the QuitScene.
 * @author cake_cruncher_7
 *
 */
public class LetsDoThatMenuButton extends GenericMenuButton
{
	//CONSTRUCTION
	public LetsDoThatMenuButton(Scene parent) {
		super(parent, "Yes");
		
		_scaleX = 1.0f;
		_text01ScaleX = 1.0f;
	}

	//FUNCTION
	@Override
	protected void onRelease()
	{
		super.onRelease();
		Gdx.app.exit();
	}
}
