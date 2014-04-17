package com.pillowdrift.drillergame.shop;

import com.pillowdrift.drillergame.database.ItemDAO;
import com.pillowdrift.drillergame.framework.Scene;
import com.pillowdrift.drillergame.framework.Sprite;

public class OriginalDrillItemButton extends ShopItemButton {

	public OriginalDrillItemButton(Scene parent) {
		super(parent, ItemDAO.ItemNames.FIRSTDRILL, "Your first ever drill, given\nto you by Grandpa Moley.", ItemDAO.ItemTypes.DRILL, 0);
		
		// Add the drill sprite
		_sprites.addSprite("Original Drill", new Sprite(parent.getResourceManager().getAtlasRegion("atlas01", "playerForward"), 64, 24.0f));
		_sprites.setCurrentSpriteName("Original Drill");
		_image = _sprites.getCurrentSprite();
		
		// This is the first drill so we have it
		_purchased = true;
		
		// Make the normal sprite the current one
		_sprites.setCurrentSpriteName("norm");
	}
}
