package com.pillowdrift.drillergame.shop;

import com.pillowdrift.drillergame.database.ItemDAO;
import com.pillowdrift.drillergame.framework.Scene;
import com.pillowdrift.drillergame.framework.Sprite;

public class ElectricDrillItemButton extends ShopItemButton{

	public ElectricDrillItemButton(Scene parent) {
		super(parent, ItemDAO.ItemNames.ELECDRILL, "This drill has been magentized to let\nyou collect gems from further away.", ItemDAO.ItemTypes.DRILL, 5000);
		// Add the drill sprite
		_sprites.addSprite("Original Drill", new Sprite(parent.getResourceManager().getAtlasRegion("atlas01", "playerForwardGold"), 64, 24.0f));
		_sprites.setCurrentSpriteName("Original Drill");
		_image = _sprites.getCurrentSprite();
		
		// Make the normal sprite the current one
		_sprites.setCurrentSpriteName("norm");
	}

}
