package com.pillowdrift.drillergame.entities.menu.buttons;

import com.pillowdrift.drillergame.framework.Scene;
import com.pillowdrift.drillergame.scenes.GameScene;

/**
 * Button to resume gameplay after pause
 * @author strawberries
 *
 */
public class ResumeGameMenuButton extends GenericMenuButton{

	//CONSTRUCTION
	public ResumeGameMenuButton(Scene parent) {
		super(parent, "Resume");
	}

	//FUNCTION
	@Override
	protected void onRelease()
	{
		super.onRelease();
			
		//Resume the game scene
		GameScene gameScene = (GameScene)_parent.getOwner().getScene("GameScene");
		gameScene.requestResumeGame();
		//Deactivate our parent scene
		_parent.deactivate();
	}
}
