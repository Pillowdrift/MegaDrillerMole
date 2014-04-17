package com.pillowdrift.drillergame.entities.menu.buttons;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.BitmapFont.TextBounds;
import com.pillowdrift.drillergame.framework.Scene;
import com.pillowdrift.drillergame.framework.Sprite;

/**
 * Button to let the user post their score to Facebook
 * @author strawberries
 *
 */
public class FacebookMenuButton extends GenericMenuButton {
	
	//Whether we are logging in our out;
	boolean _loggedIn = false;

	//CONSTRUCTION
	public FacebookMenuButton(Scene parent) {
		super(parent, "Log in to");
		_text01ScaleX = 0.77f;
		
		_loggedIn = _parent.getOwner().getGame().getFacebookService().isLoggedInToFacebook();
		_sprites.addSprite("FacebookLogo", new Sprite(parent.getResourceManager().getAtlasRegion("atlas01", "facebookIcon"), 40, 0));

	}
	
	@Override
	public void onRelease()
	{
		super.onRelease();
		
		if (_loggedIn)
		{
			_parent.getOwner().getGame().getFacebookService().logInToFacebook();
		}
		else
			_parent.getOwner().getGame().getFacebookService().logOutOfFacebook();
	}
	
	@Override
	public void update(float dt)
	{
		super.update(dt);
		if (_parent.getOwner().getGame().getFacebookService().isLoggedInToFacebook())
			changeToLogout();
		else
			changeToLogin();
	}
	
	@Override
	public void draw(SpriteBatch spriteBatch)
	{
		super.draw(spriteBatch);
		//Draw sprite
		_sprites.setCurrentSpriteName("FacebookLogo");
		Sprite cur = _sprites.getCurrentSprite();
		spriteBatch.setColor(1.0f, 1.0f, 1.0f, 1.0f);
		spriteBatch.draw(cur.getCurrentFrame(), _x + 72, _y - 22, 40, 40);
		_sprites.setCurrentSpriteName("norm");
	}
	
	/**
	 * need to change to logged out state?
	 * @param newString
	 */
	public void changeToLogout() 
	{
		_text01 = "Log out of";
		_loggedIn = false;
		TextBounds textBounds = _font01.getBounds(_text01);
		_text01OriginX = (textBounds.width/2.0f + 20);
		_text01OriginY = -(textBounds.height/2.0f);	
	}

	/**
	 * need to change to logged in state?
	 * @param newString
	 */
	public void changeToLogin() 
	{
		_text01 = "Log in to";
		_loggedIn = true;
		TextBounds textBounds = _font01.getBounds(_text01);
		_text01OriginX = (textBounds.width/2.0f);
		_text01OriginY = -(textBounds.height/2.0f);	
	}
}
