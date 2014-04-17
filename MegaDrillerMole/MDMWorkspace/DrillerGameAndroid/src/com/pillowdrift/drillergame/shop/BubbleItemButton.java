package com.pillowdrift.drillergame.shop;

import com.pillowdrift.drillergame.database.ItemDAO;
import com.pillowdrift.drillergame.framework.Scene;
import com.pillowdrift.drillergame.framework.Sprite;

public class BubbleItemButton extends ShopItemButton {

	public BubbleItemButton(Scene parent) {
		super(parent, ItemDAO.ItemNames.BUBBLE, "This bubble will save you from the\nfirst hit you take (single use)", ItemDAO.ItemTypes.POWERUP, 200);
		_sprites.addSprite("Bubble", new Sprite(parent.getResourceManager().getAtlasRegion("atlas01", "bubbleIcon"), 64, 0.0f));
		_sprites.setCurrentSpriteName("Bubble");
		_image = _sprites.getCurrentSprite();

		// Make the normal sprite the current one
		_sprites.setCurrentSpriteName("norm");
	}
	
	@Override
	public void update(float dt) {
		super.update(dt);
		//It's in use as soon as you purchase it
		if(!_inUse && _purchased)
			_inUse = true;
	
	}
	
	//This can be updated at all times because it's single use
	public void setPurchased(boolean purchased) 
	{
		if (purchased)
		{
			_purchased = true;
			_inUse = true;
		}
		else
		{
			_purchased = false;
			_inUse = false;
			_areYouSure = false;
			_pressed = false;
			_description = _originalDescription;
		}
	}

}
