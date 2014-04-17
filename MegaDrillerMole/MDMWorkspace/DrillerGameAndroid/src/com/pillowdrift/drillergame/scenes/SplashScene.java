package com.pillowdrift.drillergame.scenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.pillowdrift.drillergame.framework.Scene;
import com.pillowdrift.drillergame.framework.SceneManager;

/**
 * Splashscreen scene
 * @author Acun + Bombpersons
 *
 */
public class SplashScene extends Scene {
	// Vars
	float _timer = SPLASHTIME;
	static final float FADEPOINT = 4.0f;
	static final float SPLASHTIME = 4.25f;
	Texture _logo;
	Texture _obo;
	
	/**
	 * Create a new spash scene.
	 * @param owner
	 */
	public SplashScene(SceneManager owner)
	{
		super(owner);
		
		// Load the logo.
		_logo = new Texture(Gdx.files.internal("data/graphics/logo.png"));
		
		// Make sure we are in front of everything.
		_depth = -100;
	}
	
	@Override
	public void update(float dt)
	{		
		super.update(dt);
		
		// Update the transparency.
		_timer -= dt;
		
		if (Gdx.input.justTouched()) {
			if (_timer > SPLASHTIME - FADEPOINT)
				_timer = SPLASHTIME - FADEPOINT;
		}
			
		if (_timer < 0) {
			_owner.getScene("GameScene").setActive(true);
			_owner.getScene("MenuScene").setActive(true);
			deactivate();
			_active = false;
			_draw = false;
			getOwner().getGame().getAdsService().turnOnAds();
			
			// Unload the textures
			_logo.dispose();
		}
	}
	
	@Override
	public void draw()
	{	
		// Draw the white background
		_spriteBatch.begin();
		_spriteBatch.setColor(Color.WHITE.cpy().mul((_timer) / (SPLASHTIME - FADEPOINT)));
		_spriteBatch.draw(_logo, 0, 0);
		_spriteBatch.end();
		
		super.draw();		
	}
}
