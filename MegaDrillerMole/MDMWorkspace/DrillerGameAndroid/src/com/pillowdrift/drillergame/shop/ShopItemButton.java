package com.pillowdrift.drillergame.shop;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.BitmapFont.TextBounds;
import com.pillowdrift.drillergame.database.ItemDAO;
import com.pillowdrift.drillergame.entities.menu.buttons.GenericMenuButton;
import com.pillowdrift.drillergame.framework.Scene;
import com.pillowdrift.drillergame.framework.Sprite;
import com.pillowdrift.drillergame.scenes.ShopScene;

public class ShopItemButton extends GenericMenuButton 
{

	// The item and driller coin sprite
	protected Sprite _image;
	private Sprite _coin;
	
	// The name and description of the item
	private String _itemName;
	private String _itemType;
	protected String _description;
	protected String _originalDescription;
	
	// The price of this item in drillercoins
	private int _price;
	
	// Is this currently in use?
	protected boolean _inUse = false;
	
	// Is this already purchased?
	protected boolean _purchased = false;
	
	// Is this pressed?
	protected boolean _pressed = false;
	protected boolean _areYouSure = false;
	
	// Is there a data service?
	boolean _service = false;
	
	// Do we need to start a countdown timer for the desc?
	boolean _countdown = false;
	float _countdownTime = 0.0f;
	
	// How much coins are shown
	long _shownCoins = 0;
	
	// Do we need to start a countdown timer for the coins?
	boolean _coinCountdown = false;
	int _howMuchDown = 0;
	int _currentDown = 0;
	
	public ShopItemButton(Scene parent, String itemName, String description, String type, int price) {
		super(parent, "");
		
		// Add the coin sprite
		_sprites.addSprite("DrillerCoin", new Sprite(parent.getResourceManager().getAtlasRegion("atlas01", "drillerCoin"), 40, 0.0f));
		_sprites.setCurrentSpriteName("DrillerCoin");
		_coin = _sprites.getCurrentSprite();
		
		// drill info
		_itemName = itemName;
		
		// Make the normal sprite the current one
		_sprites.setCurrentSpriteName("norm");
		_service = _parent.getOwner().getGame().getDataService().serviceAvailable();
		if (_service)
		{
			_purchased = _parent.getOwner().getGame().getDataService().getItemDataAccessor().getItemMap().get(_itemName).getPurchased().equals(ItemDAO.OnOff.ON);
			_inUse = _parent.getOwner().getGame().getDataService().getItemDataAccessor().getItemMap().get(_itemName).getUsing().equals(ItemDAO.OnOff.ON);
			_shownCoins = _parent.getOwner().getGame().getDataService().getCoinsDataAccessor().getCoins();
		}
		
		
		_itemType = type;
		_description = description;
		_originalDescription = description;
		_price = price;
		
		// set scale
		_scaleX = 2.0f;
		_scaleY = 2.5f;
		
		// reset button sprite
		_sprites.setCurrentSpriteName("norm");
	}
	
	@Override
	public void draw(SpriteBatch spriteBatch)
	{
		setColor(_purchased ? Color.WHITE : Color.DARK_GRAY);
		if (_inUse)
			setColor(Color.GREEN);
		if (_areYouSure)
			setColor(Color.YELLOW);
		super.draw(spriteBatch);
		_font01.setColor(_purchased ? Color.WHITE : Color.GRAY);
		_font01.setScale(0.76f);
		_font01.draw(spriteBatch, _itemName, _x - 72, _y + 35);
		
		String price = Integer.toString(_price);
		TextBounds bounds = _font01.getBounds(price);
		_font01.draw(spriteBatch, price, (_x + 110) - bounds.width, _y + 35);
		_font01.setScale(0.6f);
		_font01.drawMultiLine(spriteBatch, _description, _x - 110, _y);
		spriteBatch.setColor(_purchased ? Color.WHITE : Color.DARK_GRAY);
		spriteBatch.draw(_image.getCurrentFrame(), _x - 150, _y - 12, 64, 64);
		spriteBatch.draw(_coin.getCurrentFrame(), _x + 110, _y , 40, 40);
		
		//reset the colour
		_font01.setColor(Color.WHITE);
	}
	
	public boolean isPurchased()
	{
		return _purchased;
	}
	
	public boolean isInUse()
	{
		return _inUse;
	}
	
	protected boolean attemptPurchase()
	{
		if (_service)
		{
			if (!_purchased && _parent.getOwner().getGame().getDataService().getCoinsDataAccessor().getCoins() >= _price)
			{
				_parent.getOwner().getGame().getDataService().getItemDataAccessor().modifyItemEntry(_itemName, ItemDAO.OnOff.ON, ItemDAO.OnOff.OFF);
				_parent.getOwner().getGame().getDataService().getCoinsDataAccessor().removeCoins(_price);
				startCoinCountdown(_price);
				_purchased = true;
				return true;
			}
		}
		return false;
	}
	
	protected boolean setToUse()
	{
		if (_service)
		{
			if (_purchased && _itemType.equals(ItemDAO.ItemTypes.DRILL) && _parent.getOwner().getGame().getDataService().getItemDataAccessor().getCurrentDrillInUse() != _itemName)
			{
				_parent.getOwner().getGame().getDataService().getItemDataAccessor().setCurrentDrillInUse(_itemName);
				_inUse = true;
				return true;
			}
		}
		return false;
	}
	
	@Override 
	public void onRelease()
	{
		super.onRelease();
		
		if (!_purchased)
		{
			// Switch to the are you sure.
			if (!_areYouSure) {
				_areYouSure = true;
				_description = "Click again to purchase.";
	
			} else if (!_pressed) {
				_pressed = true;
				if (!attemptPurchase())
				{
					_description = "You don't have enough Driller Coins!\nKeep playing to get more.";
					_countdown = true;
					_areYouSure = false;
					_pressed = false;
					_countdownTime = 3.0f;
				}
				else
				{
					_description = "Purchase successful.";
					_countdown = true;
					_areYouSure = false;
					_countdownTime = 3.0f;				
				}
			}
		}
		else if (!_inUse)
		{
			setToUse();
		}
	}
	
	@Override
	public void update(float dt)
	{
		super.update(dt);
		if (_service)
			_inUse = _parent.getOwner().getGame().getDataService().getItemDataAccessor().getItemMap().get(_itemName).getUsing().equals(ItemDAO.OnOff.ON);
	
		if(_countdown)
		{
			_countdownTime -= dt;
			if (_countdownTime <= 0.0f)
			{
				_description = _originalDescription;
				_countdown = false;
			}
		}
		
		if (_coinCountdown)
		{
			_currentDown += 47;
			if (_currentDown <= _howMuchDown)
			{
				_shownCoins -= 47;
				((ShopScene)_parent).setCoins(_shownCoins);
				((ShopScene)_parent).setCountingDown(true);
			}
			else
			{
				_coinCountdown = false;
				_currentDown = 0;
				_shownCoins = _parent.getOwner().getGame().getDataService().getCoinsDataAccessor().getCoins();
				((ShopScene)_parent).setCoins(_shownCoins);
				((ShopScene)_parent).setCountingDown(false);
			}
		}
		else
		{
			if (!((ShopScene)_parent).isCountingDown() && _service)
			{
				_shownCoins = _parent.getOwner().getGame().getDataService().getCoinsDataAccessor().getCoins();
				((ShopScene)_parent).setCoins(_shownCoins);
			}
		}
		
	}
	
	private void startCoinCountdown(int coins)
	{
		_coinCountdown = true;
		_howMuchDown = coins;
		_currentDown = 0;
	}

}
