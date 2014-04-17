package com.pillowdrift.drillergame.shop;

import com.pillowdrift.drillergame.database.ItemDAO;
import com.pillowdrift.drillergame.framework.Scene;
import com.pillowdrift.drillergame.framework.Sprite;

public class FeatherDrillItemButton extends ShopItemButton {

	public FeatherDrillItemButton(Scene parent) {
		super(parent, ItemDAO.ItemNames.FEATHERDRILL, "This drill has been lightened to\nallow you to boost higher in the air.", ItemDAO.ItemTypes.DRILL, 2500);
		
		// Add the drill sprite
		_sprites.addSprite("Feather Drill", new Sprite(parent.getResourceManager().getAtlasRegion("atlas01", "playerForwardFeather"), 64, 24.0f));
		_sprites.setCurrentSpriteName("Feather Drill");
		_image = _sprites.getCurrentSprite();
				
		// Make the normal sprite the current one
		_sprites.setCurrentSpriteName("norm");
	}

}
