package com.pillowdrift.drillergame.scenes;

import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.pillowdrift.drillergame.entities.menu.MenuText;
import com.pillowdrift.drillergame.entities.menu.buttons.LetsDoThatMenuButton;
import com.pillowdrift.drillergame.entities.menu.buttons.WhoTheMenuButton;
import com.pillowdrift.drillergame.framework.Entity;
import com.pillowdrift.drillergame.framework.Scene;
import com.pillowdrift.drillergame.framework.SceneManager;
import com.badlogic.gdx.graphics.Color;

/**
 * Scene displayed to ensure the user is sure they wish to leave.
 * @author cake_cruncher_7
 *
 */
public class QuitScene extends Scene
{
	protected static final String QUESTION = "You want to give up?";
	
	Entity _quitButton;
	Entity _resumeButton;
	MenuText _question;
	
	AtlasRegion _whitePixel;
	
	float _sinInput = 0.0f;
	float _sinChange = 1.0f;
	
	public QuitScene(SceneManager owner) {
		super(owner);

		//Add buttons
		_quitButton = addEntity("Button", new LetsDoThatMenuButton(this));
		_quitButton.setPos(_targetWidth*0.5f, _targetHeight*0.48f);
		_quitButton.setScale(_quitButton.getScaleX()*1.3f, _quitButton.getScaleY()*1.3f);
		_resumeButton = addEntity("Button", new WhoTheMenuButton(this));
		_resumeButton.setPos(_targetWidth*0.5f, _targetHeight*0.30f);
		_resumeButton.setScale(_resumeButton.getScaleX()*1.3f, _resumeButton.getScaleY()*1.3f);
		
		//Add text
		_question = (MenuText)addEntity("Text", new MenuText(this, QUESTION, MenuText.Alignment.ALIGN_CNTR, 1.4f, 1.4f, new Color(1.0f, 1.0f, 1.0f, 1.0f)));
		_question.setPos(_targetWidth*0.5f, _targetHeight*0.66f);
		
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
