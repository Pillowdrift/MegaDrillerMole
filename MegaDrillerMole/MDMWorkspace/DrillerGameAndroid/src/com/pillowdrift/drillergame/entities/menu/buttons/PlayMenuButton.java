package com.pillowdrift.drillergame.entities.menu.buttons;

import com.pillowdrift.drillergame.framework.Scene;
import com.pillowdrift.drillergame.scenes.GameScene;

/**
 * Button to reset the game scene and deactivate the parent scene.
 * @author cake_cruncher_7
 *
 */
public class PlayMenuButton extends GenericMenuButton
{
	//CONSTRUCTION
	public PlayMenuButton(Scene parent) {
		super(parent, "GO!!");
	}

	//FUNCTION
	@Override
	protected void onRelease()
	{
		super.onRelease();
		
		//Activate the game scene
		GameScene gameScene = (GameScene)_parent.getOwner().getScene("GameScene");
		gameScene.requestResetGame();
		//Deactivate our parent scene
		_parent.deactivate();
	}
}
