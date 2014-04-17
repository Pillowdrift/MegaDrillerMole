package com.pillowdrift.drillergame.entities.menu.buttons;

import com.pillowdrift.drillergame.framework.Scene;
import com.pillowdrift.drillergame.scenes.GameScene;

/**
 * Button to reset the GameScene and deactivate it's parent scene.
 * @author cake_cruncher_7
 *
 */
public class RetryMenuButton extends GenericMenuButton
{
	//CONSTRUCTION
	public RetryMenuButton(Scene parent) {
		super(parent, "Retry");
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
