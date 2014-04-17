package com.pillowdrift.drillergame.entities.menu.buttons;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.BitmapFont.TextBounds;
import com.pillowdrift.drillergame.framework.Scene;
import com.pillowdrift.drillergame.framework.Sprite;

public class TwitterMenuButton extends GenericMenuButton {

	boolean _loggedIn = false;
	
	public TwitterMenuButton(Scene parent) {	
		super(parent, "Log in to");
		
		_text01ScaleX = 0.77f;
		
		_loggedIn = _parent.getOwner().getGame().getTwitterService().isLoggedInToTwitter();
		
		_sprites.addSprite("TwitterLogo", new Sprite(parent.getResourceManager().getAtlasRegion("atlas01", "twitterIcon"), 40, 0));
	}

	@Override
	public void onRelease()
	{
		super.onRelease();
		
		if (!_loggedIn)
		{
			_parent.getOwner().getGame().getTwitterService().logInToTwitter();
		}
		else
			_parent.getOwner().getGame().getTwitterService().logOutOfTwitter();
	}
	
	@Override
	public void update(float dt)
	{
		super.update(dt);
		if (_parent.getOwner().getGame().getTwitterService().isLoggedInToTwitter())
			changeToLogout();
		else
			changeToLogin();
	}
	
	@Override
	public void draw(SpriteBatch spriteBatch)
	{
		super.draw(spriteBatch);
		//Draw sprite
		_sprites.setCurrentSpriteName("TwitterLogo");
		Sprite cur = _sprites.getCurrentSprite();
		spriteBatch.setColor(1.0f, 1.0f, 1.0f, 1.0f);
		spriteBatch.draw(cur.getCurrentFrame(), _x + 72, _y - 22, 40, 40);
		_sprites.setCurrentSpriteName("norm");
	}
	
	/**
	 * need to change to logged out state?
	 */
	public void changeToLogout() 
	{
		_text01 = "Log out of";
		_loggedIn = true;
		TextBounds textBounds = _font01.getBounds(_text01);
		_text01OriginX = (textBounds.width/2.0f + 20);
		_text01OriginY = -(textBounds.height/2.0f);	
	}

	/**
	 * need to change to logged in state?
	 */
	public void changeToLogin() 
	{
		_text01 = "Log in to";
		_loggedIn = false;
		TextBounds textBounds = _font01.getBounds(_text01);
		_text01OriginX = (textBounds.width/2.0f);
		_text01OriginY = -(textBounds.height/2.0f);	
	}
}
