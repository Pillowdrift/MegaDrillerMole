package com.pillowdrift.drillergame.entities.menu.buttons;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.pillowdrift.drillergame.framework.Scene;
import com.pillowdrift.drillergame.framework.Sprite;

public class ShopMenuButton extends GenericMenuButton {

	//The coin sprite;
	Sprite _coin;
	
	public ShopMenuButton(Scene parent) {
		super(parent, "Shop");
		
		// Add the coin sprite
		_sprites.addSprite("DrillerCoin", new Sprite(parent.getResourceManager().getAtlasRegion("atlas01", "drillerCoin"), 40, 0.0f));
		_sprites.setCurrentSpriteName("DrillerCoin");
		_coin = _sprites.getCurrentSprite();
		
		// Make the normal sprite the current one
		_sprites.setCurrentSpriteName("norm");
	}
	
	@Override
	public void draw(SpriteBatch spriteBatch)
	{
		super.draw(spriteBatch);
		spriteBatch.setColor(1.0f, 1.0f, 1.0f, 1.0f);
		spriteBatch.draw(_coin.getCurrentFrame(), _x + 53, _y - 22, 40, 40);
	}
	
	//FUNCTION
	@Override
	protected void onRelease()
	{
		super.onRelease();
		
		//Activate the shop scene
		_parent.getOwner().getScene("ShopScene").activate();
		//Deactivate our parent scene
		_parent.deactivate();
	}

}
