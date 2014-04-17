package com.pillowdrift.drillergame.scenes;

import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.pillowdrift.drillergame.data.DataCache;
import com.pillowdrift.drillergame.database.ScoreDAO;
import com.pillowdrift.drillergame.database.ScoreEntry;
import com.pillowdrift.drillergame.entities.menu.MenuText;
import com.pillowdrift.drillergame.entities.menu.buttons.MenuMenuButton;
import com.pillowdrift.drillergame.entities.menu.buttons.ScoresModeChangeButton;
import com.pillowdrift.drillergame.framework.Entity;
import com.pillowdrift.drillergame.framework.Scene;
import com.pillowdrift.drillergame.framework.SceneManager;
import com.pillowdrift.drillergame.scoreboard.OnlineDatabase;

/**
 * Scene which displays the top ten scores, recovered from the database.
 * @author cake_cruncher_7
 *
 */
public class RecordsScene extends Scene
{
	//CONSTANTS
	public static final String TITLE = "RECORDS";
	public static final float SCORE_DISP_NAME_STARTING_X = 30.0f;
	public static final float SCORE_DISP_SCORE_STARTING_X = 348.0f;
	public static final float SCORE_DISP_SCORE_STARTING_Y = 317.0f;
	public static final float SCORE_DISP_OFFSET_Y = -32.0f;
	public static final float SCORE_DISP_SCALE = 0.91f;
	public static final int SCORE_PADDING = 8;
	
	// AN ENUM FOR WHICH MODE WE ARE IN.
	public static final int LOCAL = 0;
	public static final int WEEKLY = 1;
	public static final int OVERALL = 2;
	int _state = LOCAL;
	
	//DATA
	Entity _backButton;
	AtlasRegion _whitePixel;
	
	//title
	MenuText _title;
	float _sinInput = 0.0f;
	float _sinChange = 1.0f;
	
	//Score display
	MenuText[] _scoreDisplays;
	MenuText[] _nameDisplays;
	
	// The buttons
	ScoresModeChangeButton _localButton;
	ScoresModeChangeButton _weeklyButton;
	ScoresModeChangeButton _overallButton;
	
	//CONSTRUCTION
	public RecordsScene(SceneManager owner)
	{
		super(owner);
		
		// Catch back button so we handle it manually
		Gdx.input.setCatchBackKey(true);

		//Add buttons
		_backButton = addEntity("Button", new MenuMenuButton(this));
		_backButton.setPos(_targetWidth*0.8f, _targetHeight*0.12f);
		_backButton.setScale(1.5f, 1.5f);
		
		// The score buttons
		_localButton = new ScoresModeChangeButton(this, LOCAL, "Local Scores");
		_weeklyButton = new ScoresModeChangeButton(this, WEEKLY, "Weekly Scores");
		_overallButton = new ScoresModeChangeButton(this, OVERALL, "Overall Scores");
		_localButton.setPos(_targetWidth*0.83f, _targetHeight*0.65f);
		_weeklyButton.setPos(_targetWidth*0.83f, _targetHeight*0.50f);
		_overallButton.setPos(_targetWidth*0.83f, _targetHeight*0.35f);
		addEntity("LocalButton", _localButton);
		addEntity("WeeklyButton", _weeklyButton);
		addEntity("OverallButton", _overallButton);
		
		//Add text
		_title = (MenuText)addEntity("Text", new MenuText(this, TITLE, MenuText.Alignment.ALIGN_CNTR, 2.35f, 2.35f, new Color(1.0f, 1.0f, 1.0f, 1.0f)));
		_title.setPos(_targetWidth*0.50f, _targetHeight*0.855f);
		
		//Set depth
		_depth = 5;
		
		//Get white pixel texture
		_whitePixel = getResourceManager().getAtlasRegion("atlas01", "singleWhitePixel");
	}

	//Set the state.
	public void setState(int state)
	{
		_state = state;
		updateTable();
	}
	
	//FUNCTION
	@Override
	public void update(float dt)
	{
		super.update(dt);
		
		if (getInputManager().isBackPressed())
		{
			getOwner().getScene("MenuScene").activate();
			this.deactivate();
		}
		
		//Make menu text blink
		_sinInput += _sinChange*dt;
		_title.setColour(new Color(1.0f, 1.0f, 1.0f, (float)Math.abs(Math.sin(_sinInput))));
		
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
	
	@Override
	public void onActivate()
	{
 		super.onActivate();
		
		//Update our displayed scores every time we are activated.
		updateTable();
	}
	
	/**
	 * Updates the collection of text objects which display the scores
	 */
	public void updateTable()
	{
		// Depending on our mode, we need to get the scores in a different way.
		List<ScoreEntry> scores = null;
		switch (_state) {
		case LOCAL:
			scores = DataCache.getScores();
			_title.setText("Local Scores");
			break;
			
		case WEEKLY:
			scores = OnlineDatabase.getWeekly();
			_title.setText("Global Weekly Scores");
			break;
			
		case OVERALL:
			scores = OnlineDatabase.getOverall();
			_title.setText("Global Overall Scores");
			break;
			
		default:
			break;
		}
		
		//Remove all current scores
		if(_scoreDisplays != null)
		{
			for(MenuText m : _scoreDisplays)
			{
				if (m != null)
					m.remove();
			}
		}
		if(_nameDisplays != null)
		{
			for(MenuText m : _nameDisplays)
			{
				if (m != null)
					m.remove();
			}
		}
		
		//Add new score displays
		if(scores != null)
		{
			// Remove error text.
			if (getEntityFirst("Score") != null)
				getEntityFirst("Score").remove();
			
			int scoreCount = ScoreDAO.NUMBER_OF_SCORES;
			
			if (scores.size() < scoreCount)
				scoreCount = scores.size();
			
			//Add actual scores
			_scoreDisplays = new MenuText[scoreCount];
			_nameDisplays = new MenuText[scoreCount];
			for(int i = 0; i < scoreCount; ++i)
			{
				MenuText curText;
				//Score
				if (scores.get(i).getScore() >= 0)
				{
					_scoreDisplays[i] = new MenuText(this, scores.get(i).toScoreString(), MenuText.Alignment.ALIGN_RGHT, SCORE_DISP_SCALE, SCORE_DISP_SCALE, Color.WHITE);
					curText = _scoreDisplays[i];
					curText.setPos(SCORE_DISP_SCORE_STARTING_X, SCORE_DISP_SCORE_STARTING_Y + (SCORE_DISP_OFFSET_Y*i));
					//Add the new text item to the collection of entities
					addEntity("Score", curText);
				}
				//Name
				_nameDisplays[i] = new MenuText(this, scores.get(i).toNameString(), MenuText.Alignment.ALIGN_LFT, SCORE_DISP_SCALE, SCORE_DISP_SCALE, Color.WHITE);
				curText = _nameDisplays[i];
				curText.setPos(SCORE_DISP_NAME_STARTING_X, SCORE_DISP_SCORE_STARTING_Y + (SCORE_DISP_OFFSET_Y*i));
				//Add the new text item to the collection of entities
				addEntity("Name", curText);
			}
		}
		else
		{
			if (getEntityFirst("Score") == null) {
				//Add error text
				_scoreDisplays = new MenuText[1];
				_nameDisplays = null;
				MenuText curScore = _scoreDisplays[0];
				curScore = new MenuText(this, "Couldn't find scores!", MenuText.Alignment.ALIGN_RGHT, 1.0f, 1.0f, Color.WHITE);
				curScore.setPos(SCORE_DISP_SCORE_STARTING_X, SCORE_DISP_SCORE_STARTING_Y);
				//Add the new text item to the collection of entities
				addEntity("Score", curScore);
			}
		}
	}
}
