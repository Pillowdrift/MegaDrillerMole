package com.pillowdrift.drillergame.scenes;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.math.Vector2;
import com.pillowdrift.drillergame.framework.Scene;
import com.pillowdrift.drillergame.framework.SceneManager;

/**
 * Scene which acts as the Heads-up-Display during gameplay, showign player life,
 * score, highscore, load, and other useful information.
 * @author cake_cruncher_7
 *
 */
public class HUDScene extends Scene
{
	//Data
	//Stats
	protected final String _scoreLabel = "SCORE: ";
	protected CharSequence _scoreString = "";
	protected final Vector2 _scorePos = new Vector2(8.0f, _targetHeight-8.0f);
	protected final String _loadLabel = "LOAD: ";
	protected CharSequence _loadString = "";
	protected final Vector2 _loadPos = new Vector2(8.0f, _targetHeight-48.0f);
	protected final String _highScoreLabel = "HIGH: ";
	protected CharSequence _highScoreString = "";
	protected final Vector2 _highScorePos = new Vector2((_targetWidth*0.5f)-4.0f, _targetHeight-8.0f);
	protected int _lifeCount = 0;
	protected final Vector2 _lifeDrawPos = new Vector2((_targetWidth*0.006f), (_targetHeight*0.023f));
	protected final float _lifeDrawOffsetX = -16.0f;
	protected final float _lifeImageWidth;
	protected final float _lifeImageHeight;
	//Font
	protected final BitmapFont _dispFont01;
	protected final float _scoreScale = 1.0f;
	protected final float _loadScale = 0.7f;
	protected final Color _scoreColour01 = new Color(1.0f, 1.0f, 1.0f, 1.0f);
	protected final Color _scoreColour02 = new Color(0.0f, 0.0f, 0.0f, 1.0f);
	protected final Color _loadColour01 = new Color(0.96f, 0.95f, 0.72f, 1.0f);
	protected final Color _loadColour02 = new Color(0.0f, 0.0f, 0.0f, 1.0f);
	protected final Vector2 _shadowOffset = new Vector2(-3.0f, -3.0f);
	//Textures
	protected final AtlasRegion _lifeTexture;
	protected final AtlasRegion _deathTexture;
	
	//Access
	public void updateScore(long score)
	{
		_scoreString = _scoreLabel + score;
	}
	public void updateHighScore(long highscore)
	{
		_highScoreString = _highScoreLabel + highscore;
	}
	public void updateLoad(long load)
	{
		_loadString = _loadLabel + load;
	}
	public void updateLife(int life)
	{
		_lifeCount = life;
	}
	
	//Construction
	public HUDScene(SceneManager owner)
	{
		super(owner);
		
		//Set depth
		_depth = 20;
		
		//Retrieve font
		_dispFont01 = _owner.getResourceManager().getFont("OSP-DIN");
		
		//Retrieve textures
		_lifeTexture = _owner.getResourceManager().getAtlasRegion("atlas01", "playerLife");
		_deathTexture = _owner.getResourceManager().getAtlasRegion("atlas01", "playerDeath");
		_lifeImageWidth = _lifeTexture.getRegionWidth();
		_lifeImageHeight = _lifeTexture.getRegionHeight();
	}

	//Function
	@Override
	public void draw() {
		//super.draw();
		
		_spriteBatch.begin();
		//Draw text:
		//score
		_dispFont01.setScale(_scoreScale);
		_dispFont01.setColor(_scoreColour02);
		_dispFont01.draw(_spriteBatch, _scoreString, _scorePos.x + _shadowOffset.x, _scorePos.y + _shadowOffset.y);
		_dispFont01.draw(_spriteBatch, _highScoreString, _highScorePos.x + _shadowOffset.x, _highScorePos.y + _shadowOffset.y);
		_dispFont01.setColor(_scoreColour01);
		_dispFont01.draw(_spriteBatch, _scoreString, _scorePos.x, _scorePos.y);
		_dispFont01.draw(_spriteBatch, _highScoreString, _highScorePos.x, _highScorePos.y);
		//load
		_dispFont01.setScale(_loadScale);
		_dispFont01.setColor(_loadColour02);
		_dispFont01.draw(_spriteBatch, _loadString, _loadPos.x + _shadowOffset.x, _loadPos.y + _shadowOffset.y);
		_dispFont01.setColor(_loadColour01);
		_dispFont01.draw(_spriteBatch, _loadString, _loadPos.x, _loadPos.y);
		//Draw lives
		_spriteBatch.setColor(1.0f, 1.0f, 1.0f, 1.0f);
		for(int i = 0; i < GameScene.PLAYER_MAX_LIFE; ++i)
		{
			AtlasRegion drawTex;
			if(i < _lifeCount)
			{
				//Set life
				drawTex = _lifeTexture;
			}
			else
			{
				//Set death
				drawTex = _deathTexture;
			}
			//Draw image
			_spriteBatch.draw(drawTex, _lifeDrawPos.x+((_lifeDrawOffsetX+_lifeImageWidth)*i), _lifeDrawPos.y, 0.0f, 0.0f, _lifeImageWidth, _lifeImageHeight, 1.0f, 1.0f, 0.0f);
		}
		_spriteBatch.end();
	}
}
