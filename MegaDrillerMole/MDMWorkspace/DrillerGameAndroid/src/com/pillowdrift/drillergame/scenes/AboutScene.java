package com.pillowdrift.drillergame.scenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.pillowdrift.drillergame.entities.menu.buttons.MenuMenuButton;
import com.pillowdrift.drillergame.framework.Entity;
import com.pillowdrift.drillergame.framework.Scene;
import com.pillowdrift.drillergame.framework.SceneManager;

/**
 * Scene which will display trophies earned during gameplay. (Not yet implemented).
 * @author cake_cruncher_7
 *
 */
public class AboutScene extends Scene
{
	Entity _backButton;
	AtlasRegion _whitePixel;
	TextureRegion _libgdx;
	TextureRegion _freesfx;
	TextureRegion _mole;
	TextureRegion _copyrightLogo;
	Texture _obo;
	Texture _logo;
	
	private Vector2 _molePos;
	private Vector2 _titlePos;
	private Vector2 _copyrightPos;
	private Vector2 _mainPos;
	private Vector2 _libgdxPos;
	private Vector2 _freesfxPos;
	private Vector2 _oboPos;
	private Vector2 _logoPos;
	private Vector2 _endPos;
	
	
	private static final String TITLE = "Mega Driller Mole";
	private static final String MAIN = "     2012 Pillowdrift LTD.\n"
									   + "Mega Driller Mole and all characters and artwork\n"
									   + "are trademarks of Pillowdrift LTD. \n"
									   + "\n"
									   + "Game Designer / Programmer\n"
									   + "Daniel Taylor\n"
									   + "\n"
									   + "Lead Programmer\n"
									   + "Caitlin Wilks\n"
									   + "\n"
									   + "Programmer / SFX\n"
									   + "Grant Livingston\n"
									   + "\n"
									   + "Q/A / Programmer\n"
									   + "Richard Webster-Noble\n"
									   + "\n"
									   + "Q/A / Programmer\n"
									   + "Jitesh Rawal\n"
									   + "\n"
									   + "Special Thanks\n"
									   + "DirtyPixel\n"
									   + "Mike Koenig\n"
									   + "Vanisha Waghela\n"
									   + "Marc Wilks\n"
									   + "\n";
									   
	private static final String END = "Thanks for playing!\n\n"
									   + "EULA at www.pillowdrift.com";
	
	private static final float UP_FACTOR = 50.0f;
	
	float _newUpdate = 0.0f; // how much is moved up for next frame.
	
	public AboutScene(SceneManager owner)
	{
		super(owner);
		
		// Catch back button so we handle it manually
		Gdx.input.setCatchBackKey(true);
		
		//init all positions
		resetEverything();
		
		//Add buttons
		_backButton = addEntity("Button", new MenuMenuButton(this));
		_backButton.setPos(_targetWidth*0.8f, _targetHeight*0.12f);
		_backButton.setScale(1.5f, 1.5f);
		
		//Set depth
		_depth = 5;
		
		//Get white pixel texture
		_whitePixel = getResourceManager().getAtlasRegion("atlas01", "singleWhitePixel");
		
		//Get the logos
		_libgdx = getResourceManager().getAtlasRegion("atlas01", "libgdx");
		_freesfx = getResourceManager().getAtlasRegion("atlas01", "freesfx");
		_copyrightLogo = getResourceManager().getAtlasRegion("atlas01", "copyright");
		_obo = new Texture(Gdx.files.internal("data/graphics/obo2.png"));
		_mole = getResourceManager().getAtlasRegion("atlas01", "titleImage");
		_logo = new Texture(Gdx.files.internal("data/graphics/logoplain.png"));
	}
	
	private void resetEverything() {
		//Positions
		_molePos = new Vector2(60, 0);
		_titlePos = new Vector2(60, _molePos.y - 40);
		_copyrightPos = new Vector2(60, _titlePos.y - 80);
		_mainPos = new Vector2(60, _copyrightPos.y + 32);
		_libgdxPos = new Vector2(60, _mainPos.y - 1080);
		_freesfxPos = new Vector2(60, _libgdxPos.y - 120);
		_oboPos = new Vector2(60, _freesfxPos.y - 320);
		_logoPos = new Vector2(60, _oboPos.y - 250);
		_endPos = new Vector2(60, _logoPos.y - 200);		
	}
	
	@Override
	public void update(float dt) 
	{
		super.update(dt);
		
		if (getInputManager().isBackPressed())
		{
			getOwner().getScene("MenuScene").activate();
			this.deactivate();
		}
		
		_newUpdate = UP_FACTOR * dt;
		
		// Update all y positions
		_molePos.y += _newUpdate;
		_titlePos.y += _newUpdate;
		_copyrightPos.y += _newUpdate;
		_mainPos.y += _newUpdate;
		_libgdxPos.y += _newUpdate;
		_freesfxPos.y += _newUpdate;
		_oboPos.y += _newUpdate;
		_logoPos.y += _newUpdate;
		_endPos.y += _newUpdate;
		
	}
	
	@Override
	public void activate() {
		super.activate();
		_newUpdate = 0.0f;
		resetEverything();
	}
	
	@Override
	public void deactivate() {
		super.deactivate();
		_newUpdate = 0.0f;
	}

	@Override
	public void draw()
	{
		//Draw darkened background
		_spriteBatch.begin();
		_spriteBatch.setColor(0.0f, 0.0f, 0.0f, 0.29f);
		_spriteBatch.draw(_whitePixel, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 1.0f, _targetWidth, _targetHeight, 0.0f);
		
		// Draw the credits.
		_spriteBatch.setColor(1.0f, 1.0f, 1.0f, 1.0f);
		_spriteBatch.draw(_mole, _molePos.x, _molePos.y);
		BitmapFont font = getResourceManager().getFont("OSP-DIN");
		font.setScale(1.0f);
		font.setColor(Color.WHITE);
		
		font.draw(_spriteBatch, TITLE, _titlePos.x, _titlePos.y);
		_spriteBatch.draw(_copyrightLogo, _copyrightPos.x, _copyrightPos.y, 30, 30);
		font.drawMultiLine(_spriteBatch, MAIN, _mainPos.x, _mainPos.y);
		_spriteBatch.draw(_libgdx, _libgdxPos.x, _libgdxPos.y);
		_spriteBatch.draw(_freesfx, _freesfxPos.x, _freesfxPos.y);
		_spriteBatch.draw(_obo, _oboPos.x, _oboPos.y );
		_spriteBatch.draw(_logo, _logoPos.x, _logoPos.y);
		font.drawMultiLine(_spriteBatch, END, _endPos.x, _endPos.y);
		
		_spriteBatch.end();
		
		super.draw();
	}
}
