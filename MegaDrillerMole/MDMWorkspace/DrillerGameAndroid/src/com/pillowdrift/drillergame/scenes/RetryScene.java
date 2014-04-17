package com.pillowdrift.drillergame.scenes;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.BitmapFont.TextBounds;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.pillowdrift.drillergame.data.DataCache;
import com.pillowdrift.drillergame.entities.menu.buttons.MenuMenuButton;
import com.pillowdrift.drillergame.entities.menu.buttons.RetryMenuButton;
import com.pillowdrift.drillergame.entities.menu.buttons.SubmitScoreFacebookButton;
import com.pillowdrift.drillergame.entities.menu.buttons.SubmitScoreOnlineHighscores;
import com.pillowdrift.drillergame.entities.menu.buttons.SubmitScoreTwitterButton;
import com.pillowdrift.drillergame.framework.Entity;
import com.pillowdrift.drillergame.framework.Scene;
import com.pillowdrift.drillergame.framework.SceneManager;

/**
 * Scene allowing the player to restart the game or quit ot the menu after being defeated.
 * @author cake_cruncher_7
 *
 */
public class RetryScene extends Scene
{
	private static final float FADE_RATE = 0.1f;
	
	Entity _retryButton;
	Entity _quitButton;
	
	AtlasRegion _whitePixel;
	
	boolean _isSplashing = false;
	float _currentSplash = 0.0f;
	
	AtlasRegion _coin;
	long _numCoins = 0;
	
	// how many coins were given this time
	long _currentGiven = 0;
	
	// is the number of coins going up?
	boolean _goingUp = false;
	long _currentlyGoneUp = 0;
	
	public RetryScene(SceneManager owner)
	{
		super(owner);

		//Add buttons
		_retryButton = addEntity("Button", new RetryMenuButton(this));
		_retryButton.setPos(_targetWidth*0.5f, _targetHeight*0.32f);
		_retryButton.setScale(_retryButton.getScaleX()*1.5f, _retryButton.getScaleY()*1.5f);
		_quitButton = addEntity("Button", new MenuMenuButton(this));
		_quitButton.setPos(_targetWidth*0.5f, _targetHeight*0.16f);
		_quitButton.setScale(_quitButton.getScaleX()*1.5f, _quitButton.getScaleY()*1.5f);
		
		//Set depth
		_depth = 5;
		
		//Get white pixel texture
		_whitePixel = getResourceManager().getAtlasRegion("atlas01", "singleWhitePixel");
		
		_coin = getResourceManager().getAtlasRegion("atlas01", "drillerCoin");
		//Add coin count
		_numCoins = DataCache.getCoins();
	}
	
	@Override
	public void update(float dt)
	{
		super.update(dt);
		if (_isSplashing)
		{
			_currentSplash -= dt;
			if (_currentSplash <= 0)
			{
				_currentSplash = 0.0f;
				_isSplashing = false;
			}
		}
		
		if (_goingUp)
		{
			_currentlyGoneUp += 3;
			if (!(_currentlyGoneUp > _currentGiven))
			{
				_numCoins += 3;
			}
			else
			{
				_goingUp = false;
				_currentlyGoneUp = 0;
				_numCoins = DataCache.getCoins();
			}
		}
	}

	@Override
	public void draw()
	{
		//Draw darkened background
		_spriteBatch.begin();
		_spriteBatch.setColor(0.0f, 0.0f, 0.0f, 0.29f);
		_spriteBatch.draw(_whitePixel, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 1.0f, _targetWidth, _targetHeight, 0.0f);
		_spriteBatch.setColor(Color.WHITE);
		_spriteBatch.draw(_coin, _targetWidth * 0.9f, _targetHeight * 0.1f);
		
		//Draw coins
		//Handle font drawing ourselves because it can change
		BitmapFont font = getResourceManager().getFont("OSP-DIN");
		font.setColor(Color.WHITE);
		
		TextBounds bounds = font.getBounds(Long.toString(_numCoins));
		font.draw(_spriteBatch, Long.toString(_numCoins), ((_targetWidth * 0.9f) - bounds.width), _targetHeight * 0.17f);

		_spriteBatch.end();
		
		super.draw();
		
		_spriteBatch.begin();
		if (_isSplashing)
		{
			font.setColor(1.0f, 1.0f, 1.0f, _currentSplash);
			font.setScale(8 - (_currentSplash * 8));
			bounds = font.getBounds(Long.toString(_numCoins));
			font.draw(_spriteBatch, "+" + Long.toString(_currentGiven) , (_targetWidth * 0.5f) - (bounds.width / 2.0f) , (_targetHeight * 0.45f)+ (bounds.height / 2.0f) );
		}
		_spriteBatch.end();
	}
	
	@Override
	public void activate()
	{
		super.activate();
		
		if (getEntityFirst("FacebookButton") == null) 
		{
			// Add the facebook button if we need it.
			if (getOwner().getGame().getFacebookService().isLoggedInToFacebook()) {
				Entity ent = addEntity("FacebookButton", new SubmitScoreFacebookButton(this, 
								((GameScene)getOwner().getScene("GameScene")).getPlayer01Score()));
				ent.setPos(_targetWidth * 0.5f, _targetHeight * 0.64f);
				ent.setScale(3.0f, 1.3f);
			}
		}
		
		if (getEntityFirst("TwitterButton") == null)
		{
			if (getOwner().getGame().getTwitterService().isLoggedInToTwitter()) {
				Entity twi = addEntity("TwitterButton", new SubmitScoreTwitterButton(this,
								((GameScene)getOwner().getScene("GameScene")).getPlayer01Score()));
				twi.setPos(_targetWidth * 0.5f, _targetHeight * 0.80f);
				twi.setScale(3.0f, 1.3f);
			}
		}
		
		// Check to see if this is a new local highscore (not necessary)
		HighRetryScene last = (HighRetryScene)getOwner().getScene("HighRetryScene");
		SubmitScoreOnlineHighscores onlineButton = (SubmitScoreOnlineHighscores)getEntityFirst("OnlineButton");
		if (onlineButton == null && last.isNewHigh())
		{
			Entity online = addEntity("OnlineButton", new SubmitScoreOnlineHighscores(this, last.getName(),
					((GameScene)getOwner().getScene("GameScene")).getPlayer01Score()));
			online.setPos(_targetWidth * 0.5f, _targetHeight * 0.48f);
			online.setScale(3.0f, 1.3f);
		}
		else if (onlineButton != null)
		{
			onlineButton.reset();
		}
		
		//Calculate number of coins to give
		_numCoins = DataCache.getCoins();
		long coinsToGive = (long)((float)((GameScene)getOwner().getScene("GameScene")).getPlayer01Score() / 1000.0f);
		if (coinsToGive > 0)
			doSplashCoins(coinsToGive);
		else
		{
			_currentGiven = 0;
		}
	}
	
	@Override
	public void deactivate()
	{
		super.deactivate();
		
		// Remove the facebook button
		if (getEntityFirst("FacebookButton") != null)
			if (getOwner().getGame().getFacebookService().isLoggedInToFacebook())
			{
				getEntityFirst("FacebookButton").remove();
				update(0);
		}
		
		if (getEntityFirst("TwitterButton") != null)
			if (getOwner().getGame().getTwitterService().isLoggedInToTwitter())
			{
				getEntityFirst("TwitterButton").remove();
				update(0);
		}
		
		if (getEntityFirst("OnlineButton") != null)
		{
				getEntityFirst("OnlineButton").remove();
				update(0);
		}
	}
	
	public void doSplashCoins(long coins)
	{
		_isSplashing = true;
		_goingUp = true;
		_currentSplash = 1.0f;
		_currentGiven = coins;
		DataCache.addCoins(coins);
	}
	
	public void setOnlineRank(boolean success, long weekly, long overall)
	{
		// If the web request to submit score succeeded
		if (success)
		{
			// Do something with these values
			
		}
	}
}
