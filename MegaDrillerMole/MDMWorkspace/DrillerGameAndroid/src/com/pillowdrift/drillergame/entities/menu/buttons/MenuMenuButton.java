package com.pillowdrift.drillergame.entities.menu.buttons;

import com.pillowdrift.drillergame.entities.Player;
import com.pillowdrift.drillergame.framework.Scene;
import com.pillowdrift.drillergame.scenes.GameScene;

/**
 * Button which will deactivate the parent scene and activate the menu scene.
 * @author cake_cruncher_7
 *
 */
public class MenuMenuButton extends GenericMenuButton
{
	//CONSTRUCTION
	public MenuMenuButton(Scene parent) {
		super(parent, "Menu");
	}

	//FUNCTION
	@Override
	protected void onRelease()
	{
		super.onRelease();
		
		// Kill the player if it exists.
		Scene gamescene = _parent.getOwner().getScene("GameScene");
		if (gamescene != null) {
			Player player = (Player)_parent.getOwner().getScene("GameScene").getEntityFirst("Player");
			if (player != null) {
				player.remove();
			}
			
			((GameScene)gamescene).requestResumeGame();
		}
		
		//Activate the menu scene
		_parent.getOwner().getScene("MenuScene").activate();
		//Deactivate our parent scene
		_parent.deactivate();
	}
}
