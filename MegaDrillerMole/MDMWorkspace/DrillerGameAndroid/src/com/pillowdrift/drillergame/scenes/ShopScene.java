package com.pillowdrift.drillergame.scenes;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.BitmapFont.TextBounds;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.pillowdrift.drillergame.data.DataCache;
import com.pillowdrift.drillergame.entities.menu.MenuText;
import com.pillowdrift.drillergame.entities.menu.buttons.MenuMenuButton;
import com.pillowdrift.drillergame.framework.Entity;
import com.pillowdrift.drillergame.framework.Scene;
import com.pillowdrift.drillergame.framework.SceneManager;
import com.pillowdrift.drillergame.shop.BubbleItemButton;
import com.pillowdrift.drillergame.shop.ElectricDrillItemButton;
import com.pillowdrift.drillergame.shop.FeatherDrillItemButton;
import com.pillowdrift.drillergame.shop.FlameDrillItemButton;
import com.pillowdrift.drillergame.shop.OriginalDrillItemButton;
import com.pillowdrift.drillergame.shop.ShopItemButton;

public class ShopScene extends Scene {
	
	// The menu button
	Entity _quitButton;
	AtlasRegion _whitePixel;
	AtlasRegion _coin;
	long _numCoins = 0;
	boolean _countdown = false;
	
	Entity bubble;
	
	public ShopScene(SceneManager owner) {
		super(owner);
		
		// Add a menu button
		_quitButton = addEntity("Button", new MenuMenuButton(this));
		_quitButton.setPos(_targetWidth*0.82f, _targetHeight*0.09f);
		_quitButton.setScale(_quitButton.getScaleX()*1.5f, _quitButton.getScaleY()*1.5f);
		
		//Add the shop item buttons
		
		Entity firstDrill = addEntity("Button", new OriginalDrillItemButton(this));
		firstDrill.setPos(_targetWidth*0.25f, _targetHeight*0.75f);
		
		Entity featherDrill = addEntity("Button", new FeatherDrillItemButton(this));
		featherDrill.setPos(_targetWidth*0.25f, _targetHeight*0.50f);
		
		Entity flameDrill = addEntity("Button", new FlameDrillItemButton(this));
		flameDrill.setPos(_targetWidth*0.75f, _targetHeight*0.75f);
		
		Entity electricDrill = addEntity("Button", new ElectricDrillItemButton(this));
		electricDrill.setPos(_targetWidth*0.75f, _targetHeight*0.50f);
		
		bubble = addEntity("Button", new BubbleItemButton(this));
		bubble.setPos(_targetWidth*0.25f, _targetHeight * 0.25f);
		
		//Set depth
		_depth = 0;
		
		//Add coin count
		_numCoins = DataCache.getCoins();
		
		//Get white pixel texture and coin
		_whitePixel = getResourceManager().getAtlasRegion("atlas01", "singleWhitePixel");
		_coin = getResourceManager().getAtlasRegion("atlas01", "drillerCoin");
		
	}
	
	@Override
	public void draw()
	{
		if (_numCoins < 0)
			_numCoins = 0;
		//Draw darkened background
		_spriteBatch.begin();
		_spriteBatch.setColor(0.0f, 0.0f, 0.0f, 0.29f);
		_spriteBatch.draw(_whitePixel, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 1.0f, _targetWidth, _targetHeight, 0.0f);
		_spriteBatch.setColor(Color.WHITE);
		_spriteBatch.draw(_coin, _targetWidth * 0.9f, _targetHeight * 0.19f);
		
		//Handle font drawing ourselves because it can change
		BitmapFont font = getResourceManager().getFont("OSP-DIN");
		font.setColor(Color.WHITE);
		
		TextBounds bounds = font.getBounds(Long.toString(_numCoins));
		font.draw(_spriteBatch, Long.toString(_numCoins), ((_targetWidth * 0.9f) - bounds.width), _targetHeight * 0.25f);
		_spriteBatch.end();
		super.draw();
	}
	
	@Override
	public void activate()
	{
		super.activate();
		if (getOwner().getGame().getDataService().serviceAvailable())
		{
			boolean purchasedBubble = getOwner().getGame().getDataService().getItemDataAccessor().hasBubble();
			((BubbleItemButton)bubble).setPurchased(purchasedBubble);
		}
	}
	
	public void setCoins(long coins)
	{
		_numCoins = coins;
	}
	
	public boolean isCountingDown()
	{
		return _countdown;
	}
	
	public void setCountingDown(boolean value)
	{
		_countdown = value;
	}

}
