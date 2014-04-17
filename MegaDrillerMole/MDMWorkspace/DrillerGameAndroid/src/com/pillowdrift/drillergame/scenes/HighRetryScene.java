package com.pillowdrift.drillergame.scenes;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.BitmapFont.TextBounds;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.pillowdrift.drillergame.data.DataCache;
import com.pillowdrift.drillergame.entities.menu.MenuText;
import com.pillowdrift.drillergame.entities.menu.buttons.CharAdjustMenuButton;
import com.pillowdrift.drillergame.entities.menu.buttons.SubmitMenuButton;
import com.pillowdrift.drillergame.framework.Entity;
import com.pillowdrift.drillergame.framework.Scene;
import com.pillowdrift.drillergame.framework.SceneManager;

/**
 * Scene allowing a player with a high score to enter their name and have it submitted to the high scores database.
 * @author cake_cruncher_7
 *
 */
public class HighRetryScene extends Scene
{
	//CONSTANTS
	public static final String TITLE = "HIGHSCORE!!!";
	public static final float ENTRY_SCALE = 2.4f;
	public static final float ENTRY_BUTTON_OFFSET_Y = 0.13f;
	
	//DATA
	//Positioning
	public float ENTRY_1_POS_X;
	public final float ENTRY_2_POS_X;
	public final float ENTRY_3_POS_X;
	public final float ENTRY_POS_Y;
	//Title
	MenuText _title;
	float _sinInput = 0.0f;
	float _sinChange = 1.0f;
	//Pos
	MenuText _pos;
	boolean _newHigh = false;
	//Buttons
	Entity _submitButton;
	Entity _char01Up;
	Entity _char01Down;
	Entity _char02Up;
	Entity _char02Down;
	Entity _char03Up;
	Entity _char03Down;
	//Darken
	AtlasRegion _whitePixel;
	//Commit values
	char[] _name;
	long _score;
	//Font, for drawing name entry
	protected static BitmapFont _font01;
	
	
	public HighRetryScene(SceneManager owner)
	{
		super(owner);
		
		//Initialize constants
		ENTRY_1_POS_X = _targetWidth*0.33f;
		ENTRY_2_POS_X = _targetWidth*0.5f;
		ENTRY_3_POS_X = _targetWidth*0.67f;
		ENTRY_POS_Y = _targetHeight*0.5f;

		//Initialize name
		_name = new char[3];
		_name[0] = 'A';
		_name[1] = 'A';
		_name[2] = 'A';
		//Initialize score
		_score = 0;
		
		//Retrieve font
		_font01 = getResourceManager().getFont("OSP-DIN");
		
		//Add buttons
		_submitButton = addEntity("Button", new SubmitMenuButton(this));
		_submitButton.setPos(_targetWidth*0.5f, _targetHeight*0.19f);
		_submitButton.setScale(_submitButton.getScaleX()*1.5f, _submitButton.getScaleY()*1.5f);
		//Arrows
		//char01
		_char01Up = addEntity("Button", new CharAdjustMenuButton(this, _name, 0, 1));
		_char01Up.setPos(ENTRY_1_POS_X, ENTRY_POS_Y+(_targetHeight*ENTRY_BUTTON_OFFSET_Y));
		_char01Up.setScale(_char01Up.getScaleX()*1.2f, _char01Up.getScaleY()*1.5f);
		_char01Down = addEntity("Button", new CharAdjustMenuButton(this, _name, 0, -1));
		_char01Down.setPos(ENTRY_1_POS_X, ENTRY_POS_Y-(_targetHeight*ENTRY_BUTTON_OFFSET_Y));
		_char01Down.setScale(_char01Down.getScaleX()*1.2f, _char01Down.getScaleY()*1.5f);
		//char02
		_char02Up = addEntity("Button", new CharAdjustMenuButton(this, _name, 1, 1));
		_char02Up.setPos(ENTRY_2_POS_X, ENTRY_POS_Y+(_targetHeight*ENTRY_BUTTON_OFFSET_Y));
		_char02Up.setScale(_char02Up.getScaleX()*1.2f, _char02Up.getScaleY()*1.5f);
		_char02Down = addEntity("Button", new CharAdjustMenuButton(this, _name, 1, -1));
		_char02Down.setPos(ENTRY_2_POS_X, ENTRY_POS_Y-(_targetHeight*ENTRY_BUTTON_OFFSET_Y));
		_char02Down.setScale(_char02Down.getScaleX()*1.2f, _char02Down.getScaleY()*1.5f);
		//char03
		_char03Up = addEntity("Button", new CharAdjustMenuButton(this, _name, 2, 1));
		_char03Up.setPos(ENTRY_3_POS_X, ENTRY_POS_Y+(_targetHeight*ENTRY_BUTTON_OFFSET_Y));
		_char03Up.setScale(_char03Up.getScaleX()*1.2f, _char03Up.getScaleY()*1.5f);
		_char03Down = addEntity("Button", new CharAdjustMenuButton(this, _name, 2, -1));
		_char03Down.setPos(ENTRY_3_POS_X, ENTRY_POS_Y-(_targetHeight*ENTRY_BUTTON_OFFSET_Y));
		_char03Down.setScale(_char03Down.getScaleX()*1.2f, _char03Down.getScaleY()*1.5f);
		
		//Set depth
		_depth = 5;
		
		//Get white pixel texture
		_whitePixel = getResourceManager().getAtlasRegion("atlas01", "singleWhitePixel");
		
		//Add text
		_title = (MenuText)addEntity("Text", new MenuText(this, TITLE, MenuText.Alignment.ALIGN_CNTR, 2.35f, 2.35f, new Color(1.0f, 1.0f, 1.0f, 1.0f)));
		_title.setPos(_targetWidth*0.50f, _targetHeight*0.855f);
		_pos = (MenuText)addEntity("Text", new MenuText(this, "", MenuText.Alignment.ALIGN_CNTR, 1.25f, 1.25f, new Color(1.0f, 1.0f, 1.0f, 1.0f)));
		_pos.setPos(_targetWidth*0.50f, _targetHeight*0.735f);
	}
	
	@Override
	public void update(float dt)
	{
		super.update(dt);
		
		//Make menu text blink
		_sinInput += _sinChange*dt;
		_title.setColour(new Color(1.0f, 1.0f, 1.0f, (float)Math.abs(Math.sin(_sinInput))));
		_pos.setColour(new Color(1.0f, 1.0f, 1.0f, (float)Math.abs(Math.sin(_sinInput))));
	}

	@Override
	public void draw()
	{
		_spriteBatch.begin();
		//Draw darkened background
		_spriteBatch.setColor(0.0f, 0.0f, 0.0f, 0.29f);
		_spriteBatch.draw(_whitePixel, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 1.0f, _targetWidth, _targetHeight, 0.0f);
		
		//Draw name entry
		float _originX;
		float _originY;
		TextBounds textBounds;
		_font01.setScale(ENTRY_SCALE, ENTRY_SCALE);
		_font01.setColor(Color.WHITE);
		//Char01
		//Set centering offset for each character
		textBounds = _font01.getBounds(String.valueOf(_name[0]));
		_originX = (textBounds.width/2.0f);
		_originY = -(textBounds.height/2.0f);
		//Draw text
		_font01.drawMultiLine(_spriteBatch, String.valueOf(_name[0]), ENTRY_1_POS_X - _originX, ENTRY_POS_Y - _originY);
		//Char02
		//Set centering offset for each character
		textBounds = _font01.getBounds(String.valueOf(_name[1]));
		_originX = (textBounds.width/2.0f);
		_originY = -(textBounds.height/2.0f);
		//Draw text
		_font01.drawMultiLine(_spriteBatch, String.valueOf(_name[1]), ENTRY_2_POS_X - _originX, ENTRY_POS_Y - _originY);
		//Char03
		//Set centering offset for each character
		textBounds = _font01.getBounds(String.valueOf(_name[2]));
		_originX = (textBounds.width/2.0f);
		_originY = -(textBounds.height/2.0f);
		//Draw text
		_font01.drawMultiLine(_spriteBatch, String.valueOf(_name[2]), ENTRY_3_POS_X - _originX, ENTRY_POS_Y - _originY);
		_spriteBatch.end();
		
		super.draw();
	}
	
	@Override
	public void onActivate()
	{
		super.onActivate();
		
		//Is score high?
		_score = ((GameScene)_owner.getScene("GameScene")).getPlayer01Score();
		int pos = DataCache.isScoreHigh(_score);
		if(pos >= 0)
		{
			//Set the displayed position
			_pos.setText("POSITION " + String.valueOf(DataCache.isScoreHigh(_score)) + "!!!");
			_newHigh = true;
		}
		else
		{
			//Move quickly to the next scene if this one needn't be shown
			deactivate();
			_owner.getScene("RetryScene").activate();
			_newHigh = false;
		}
	}
	
	@Override
	public void onDeactivate()
	{
		super.onDeactivate();
		
		if(_newHigh)
		{
			//Commit the score to the database
			DataCache.proposeNewScoreEntry(String.valueOf(_name), _score);
			_newHigh = false;
		}
	}
	
	public boolean isNewHigh()
	{
		return _newHigh;
	}
	
	public String getName()
	{
		return new String(_name);
	}
}
