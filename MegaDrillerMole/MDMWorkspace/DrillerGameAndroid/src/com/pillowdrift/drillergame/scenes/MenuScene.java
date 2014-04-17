package com.pillowdrift.drillergame.scenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.pillowdrift.drillergame.entities.menu.MenuText;
import com.pillowdrift.drillergame.entities.menu.buttons.OboMenuButton;
import com.pillowdrift.drillergame.entities.menu.buttons.OptionsMenuButton;
import com.pillowdrift.drillergame.entities.menu.buttons.PlayMenuButton;
import com.pillowdrift.drillergame.entities.menu.buttons.QuitMenuButton;
import com.pillowdrift.drillergame.entities.menu.buttons.RecordsMenuButton;
import com.pillowdrift.drillergame.entities.menu.buttons.AboutMenuButton;
import com.pillowdrift.drillergame.entities.menu.buttons.ShopMenuButton;
import com.pillowdrift.drillergame.entities.menu.buttons.TutorialButton;
import com.pillowdrift.drillergame.framework.Entity;
import com.pillowdrift.drillergame.framework.Scene;
import com.pillowdrift.drillergame.framework.SceneManager;

/**
 * Main menu scene for the game.
 * @author cake_cruncher_7
 *
 */
public class MenuScene extends Scene
{
	public static final String TITLE = "MEGA DRILLER MOLE";
	
	Entity _playButton;
	Entity _tutorialButton;
	Entity _recordsButton;
	Entity _trophiesButton;
	Entity _oboButton;
	Entity _optionsButton;
	Entity _shopButton;
	MenuText _title;
	
	AtlasRegion _whitePixel;
	AtlasRegion _moleImage;
	AtlasRegion _clickMe;
	
	float _sinInput = 0.0f;
	float _sinChange = 1.0f;
	
	//The last back button state
	boolean _oldState;
	
	public MenuScene(SceneManager owner) {
		super(owner);

		//Add buttons
		_playButton = addEntity("Button", new PlayMenuButton(this));
		_playButton.setPos(_targetWidth*0.8f, _targetHeight*0.59f);
		_playButton.setScale(1.3f, 1.3f);
		_tutorialButton = addEntity("Button", new TutorialButton(this));
		_tutorialButton.setPos(_targetWidth*0.8f, _targetHeight*0.46f);
		_tutorialButton.setScale(1.3f, 1.3f);
		_recordsButton = addEntity("Button", new RecordsMenuButton(this));
		_recordsButton.setPos(_targetWidth*0.8f, _targetHeight*0.33f);
		_recordsButton.setScale(1.3f, 1.3f);
		_trophiesButton = addEntity("Button", new AboutMenuButton(this));
		_trophiesButton.setPos(_targetWidth*0.8f, _targetHeight*0.20f);
		_trophiesButton.setScale(1.3f, 1.3f);
		_oboButton = addEntity("Button", new OboMenuButton(this));
		_oboButton.setPos(_targetWidth*0.06f, _targetHeight*0.77f);
		_oboButton.setScale(0.4f, 0.4f);
		_shopButton = addEntity("Button", new ShopMenuButton(this));
		_shopButton.setPos(_targetWidth*0.8f, _targetHeight*0.07f);
		_shopButton.setScale(1.3f, 1.3f);
		
		if (getOwner().getGame().getDataService().getConfigDataAccessor() != null) {
			_optionsButton = addEntity("Button", new OptionsMenuButton(this));
			_optionsButton.setPos(_targetWidth * 0.05f, _targetHeight * 0.075f);
			_optionsButton.setScale(0.6f, 0.6f);
		}
		
		//Add text
		_title = (MenuText)addEntity("Text", new MenuText(this, TITLE, MenuText.Alignment.ALIGN_CNTR, 2.35f, 2.35f, new Color(1.0f, 1.0f, 1.0f, 1.0f)));
		_title.setPos(_targetWidth*0.53f, _targetHeight*0.755f);
		
		//Set depth
		_depth = 5;
		
		//Get white pixel texture
		_whitePixel = getResourceManager().getAtlasRegion("atlas01", "singleWhitePixel");
		//Get mole texture
		_moleImage = getResourceManager().getAtlasRegion("atlas01", "titleImage");
		//Get speech bubble
		_clickMe = getResourceManager().getAtlasRegion("atlas01", "clickme");
		
		// Deactivated by default
		_active = false;
		
		// Catch back button so we handle it manually
		Gdx.input.setCatchBackKey(true);
		
		_oldState = getInputManager().isBackPressed();
		
		update(0);
	}

	@Override
	public void activate() {
		super.activate();
		_oldState = getInputManager().isBackPressed();
	}
	
	@Override
	public void update(float dt)
	{
		super.update(dt);
		
		//Make menu text blink
		_sinInput += _sinChange*dt;
		_title.setColour(new Color(1.0f, 1.0f, 1.0f, (float)Math.abs(Math.sin(_sinInput))));
		
		
		if (getInputManager().isBackPressed())
		{
			if (!_oldState) 
			{
				getOwner().getScene("QuitScene").activate();
				this.deactivate();
			}
		}
		
		_oldState = getInputManager().isBackPressed();
	}
	
	@Override
	public void deactivate() {
		super.deactivate();
	}
	
	@Override
	public void draw()
	{
		_spriteBatch.begin();
		//Draw darkened background
		_spriteBatch.setColor(0.0f, 0.0f, 0.0f, 0.29f);
		_spriteBatch.draw(_whitePixel, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 1.0f, _targetWidth, _targetHeight, 0.0f);
		//Draw mole
		_spriteBatch.setColor(1.0f, 1.0f, 1.0f, 1.0f);
		_spriteBatch.draw(_moleImage, 0.0f, 0.0f, 0.0f, 0.0f, _moleImage.getRegionWidth(), _moleImage.getRegionHeight(), 1.25f, 1.25f, 0.0f);
		//Draw speech bubble
		_spriteBatch.draw(_clickMe, 60.0f, 400.0f);
		_spriteBatch.end();
		
		super.draw();
	}
}
