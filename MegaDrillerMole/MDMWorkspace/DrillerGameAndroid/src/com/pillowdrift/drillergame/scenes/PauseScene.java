package com.pillowdrift.drillergame.scenes;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.pillowdrift.drillergame.entities.menu.MenuText;
import com.pillowdrift.drillergame.entities.menu.buttons.MenuMenuButton;
import com.pillowdrift.drillergame.entities.menu.buttons.ResumeGameMenuButton;
import com.pillowdrift.drillergame.entities.menu.buttons.RetryMenuButton;
import com.pillowdrift.drillergame.framework.Entity;
import com.pillowdrift.drillergame.framework.Scene;
import com.pillowdrift.drillergame.framework.SceneManager;

/**
 * Scene allowing the player to choose whether or not they want to keep playing
 * @author strawberries
 *
 */
public class PauseScene extends Scene {
	
	public static final String MESSAGE = "Paused";
	
	Entity _quitButton;
	Entity _resumeButton;
	Entity _restartButton;
	MenuText _question;
	
	AtlasRegion _whitePixel;
	
	float _sinInput = 0.0f;
	float _sinChange = 1.0f;
	
	public PauseScene(SceneManager owner) {
		super(owner);

		//Add buttons
		_restartButton = addEntity("Button", new RetryMenuButton(this));
		_restartButton.setPos(_targetWidth*0.5f, _targetHeight*0.55f);
		_restartButton.setScale(_restartButton.getScaleX()*1.5f, _restartButton.getScaleY()*1.5f);
		_resumeButton = addEntity("Button", new ResumeGameMenuButton(this));
		_resumeButton.setPos(_targetWidth*0.5f, _targetHeight*0.37f);
		_resumeButton.setScale(_resumeButton.getScaleX()*1.5f, _resumeButton.getScaleY()*1.5f);
		_quitButton = addEntity("Button", new MenuMenuButton(this));
		_quitButton.setPos(_targetWidth*0.5f, _targetHeight*0.19f);
		_quitButton.setScale(_quitButton.getScaleX()*1.5f, _quitButton.getScaleY()*1.5f);
		
		//Add text
		_question = (MenuText)addEntity("Text", new MenuText(this, MESSAGE, MenuText.Alignment.ALIGN_CNTR, 1.4f, 1.4f, new Color(1.0f, 1.0f, 1.0f, 1.0f)));
		_question.setPos(_targetWidth*0.5f, _targetHeight*0.73f);

		//Set depth
		_depth = 5;
		
		//Get white pixel texture
		_whitePixel = getResourceManager().getAtlasRegion("atlas01", "singleWhitePixel");
	}
	
	@Override
	public void update(float dt)
	{
		super.update(dt);
		
		//Make question text blink
		_sinInput += _sinChange*dt;
		_question.setColour(new Color(1.0f, 1.0f, 1.0f, (float)Math.abs(Math.sin(_sinInput))));
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
