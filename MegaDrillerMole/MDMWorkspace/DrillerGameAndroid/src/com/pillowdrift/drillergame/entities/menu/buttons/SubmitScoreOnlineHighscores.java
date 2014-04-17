package com.pillowdrift.drillergame.entities.menu.buttons;

import com.badlogic.gdx.graphics.g2d.BitmapFont.TextBounds;
import com.pillowdrift.drillergame.framework.Scene;
import com.pillowdrift.drillergame.scenes.RetryScene;
import com.pillowdrift.drillergame.scoreboard.OnlineDatabase;

/**
 * Button that allows the player to submit their score to the online scoreboards
 * 
 * @author Acun
 *
 */
public class SubmitScoreOnlineHighscores extends GenericMenuButton
{
	private String _name;
	private long _score;
	private boolean _enabled = true;
	
	public SubmitScoreOnlineHighscores(Scene parent, String name, long score)
	{
		super(parent, "");
		reset();
		
		_name = name;
		_score = score;
		_text01ScaleX = 0.5f;
	}
	
	@Override
	protected void onRelease()
	{
		super.onRelease();
		
		// If the button is enabled
		if (_enabled)
		{
			_enabled = false;
			setText("Submitting...");
			
			OnlineDatabase.postScore(this, _name, _score);
		}
	}
	
	public void setText(String text)
	{
		_text01 = text;
		TextBounds textBounds = _font01.getBounds(_text01);
		_text01OriginX = (textBounds.width/2.0f);
		_text01OriginY = -(textBounds.height/2.0f);
	}
	
	public void requestComplete(boolean success, long weekly, long overall)
	{		
		if (success)
		{
			setText("Success!");
			((RetryScene)_parent).setOnlineRank(success, weekly, overall);
		}
		else
		{
			setText("Try again?");
			_enabled = true;
		}
	}
	
	public void reset()
	{
		setText("Submit to online scoreboard");
		_enabled = true;
	}
}