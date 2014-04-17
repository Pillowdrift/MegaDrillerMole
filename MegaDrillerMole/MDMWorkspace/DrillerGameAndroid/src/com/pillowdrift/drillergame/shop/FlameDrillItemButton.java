package com.pillowdrift.drillergame.shop;

import com.pillowdrift.drillergame.database.ItemDAO;
import com.pillowdrift.drillergame.framework.Scene;
import com.pillowdrift.drillergame.framework.Sprite;

public class FlameDrillItemButton extends ShopItemButton {

	public FlameDrillItemButton(Scene parent) {
		super(parent, ItemDAO.ItemNames.FLAMEDRILL, "Red hot flames burn within this\ndrill to let you boost for longer.", ItemDAO.ItemTypes.DRILL, 500);
		
		// Add the drill sprite
		_sprites.addSprite("Flame Drill", new Sprite(parent.getResourceManager().getAtlasRegion("atlas01", "playerForwardRed"), 64, 24.0f));
		_sprites.setCurrentSpriteName("Flame Drill");
		_image = _sprites.getCurrentSprite();
		
		// Make the normal sprite the current one
		_sprites.setCurrentSpriteName("norm");
	}
}
