package com.pillowdrift.drillergame.scenes;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.pillowdrift.drillergame.entities.menu.CheckBox;
import com.pillowdrift.drillergame.entities.menu.MenuText;
import com.pillowdrift.drillergame.entities.menu.buttons.FacebookMenuButton;
import com.pillowdrift.drillergame.entities.menu.buttons.MenuMenuButton;
import com.pillowdrift.drillergame.entities.menu.buttons.TwitterMenuButton;
import com.pillowdrift.drillergame.entities.menu.buttons.ParticleCheckBox;
import com.pillowdrift.drillergame.framework.Entity;
import com.pillowdrift.drillergame.framework.Scene;
import com.pillowdrift.drillergame.framework.SceneManager;

/**
 * Options menu scene
 * @author strawberries
 *
 */
public class OptionsScene extends Scene {
	
	public static final String MESSAGE = "Options";
	
	//Buttons
	Entity _facebookButton;
	Entity _twitterButton;
	Entity _menuButton;
	CheckBox _particles;
	MenuText _title;
	
	AtlasRegion _whitePixel;
	
	public OptionsScene(SceneManager owner) {
		super(owner);
		
		_facebookButton = addEntity("Button", new FacebookMenuButton(this));
		_facebookButton.setPos(_targetWidth*0.5f, _targetHeight*0.55f);
		_facebookButton.setScale(1.5f, 1.3f);
		_twitterButton = addEntity("Button", new TwitterMenuButton(this));
		_twitterButton.setPos(_targetWidth*0.5f, _targetHeight*0.42f);
		_twitterButton.setScale(1.5f, 1.3f);
		_menuButton = addEntity("Button", new MenuMenuButton(this));
		_menuButton.setPos(_targetWidth*0.5f, _targetHeight*0.16f);
		_menuButton.setScale(1.5f, 1.3f);
		
		// Add the particle tickbox.
		_particles = new ParticleCheckBox(this);
		_particles.setPos(_targetWidth * 0.5f, _targetHeight * 0.29f);
		_particles.setScale(1.5f, 1.3f);
		addEntity("ParticlesCheckBox", _particles);
		
		//Add text
		_title = (MenuText)addEntity("Text", new MenuText(this, MESSAGE, MenuText.Alignment.ALIGN_CNTR, 1.4f, 1.4f, new Color(1.0f, 1.0f, 1.0f, 1.0f)));
		_title.setPos(_targetWidth*0.5f, _targetHeight*0.73f);

		//Set depth
		_depth = -5;
		
		//Get white pixel texture
		_whitePixel = getResourceManager().getAtlasRegion("atlas01", "singleWhitePixel");
	}
	
	@Override
	public void update(float dt)
	{
		super.update(dt);
	}
	
	@Override
	public void draw()
	{
		//Draw darkened background
		_spriteBatch.begin();
		_spriteBatch.setColor(0.0f, 0.0f, 0.0f, 0.29f);
		_spriteBatch.draw(_whitePixel, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 1.0f, _targetWidth, _targetHeight, 0.0f);
		_spriteBatch.end();
		
		super.draw();
	}

}
