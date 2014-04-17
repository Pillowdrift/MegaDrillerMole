package com.pillowdrift.drillergame.scoreboard;

import java.util.ArrayList;
import java.util.List;

import com.pillowdrift.drillergame.database.ScoreEntry;
import com.pillowdrift.drillergame.entities.menu.buttons.SubmitScoreOnlineHighscores;
import com.pillowdrift.drillergame.scenes.RecordsScene;

public class OnlineDatabase
{
	private static final int SCORE_RETRIEVAL_LIMIT = 10;
	
	private static ArrayList<ScoreEntry> _weekly = new ArrayList<ScoreEntry>();
	private static ArrayList<ScoreEntry> _overall = new ArrayList<ScoreEntry>();
	
	public static void postScore(SubmitScoreOnlineHighscores button, String name, long score)
	{
		SendScoreThread thread = new SendScoreThread(button, name, score);
		thread.start();
	}
	
	public static List<ScoreEntry> getWeekly()
	{
		return _weekly;
	}
	
	public static List<ScoreEntry> getOverall()
	{
		return _overall;
	}
	
	public static void updateWeekly(RecordsScene callback)
	{
		_weekly.clear();
		
		ScoreEntry defaultScore = new ScoreEntry();
		defaultScore.setName("Updating weekly scoreboard...");
		defaultScore.setScore(-1);
		
		_weekly.add(defaultScore);
		
		GetScoresThread thread = new GetScoresThread(_weekly, callback, RecordsScene.WEEKLY, SCORE_RETRIEVAL_LIMIT);
		thread.start();
	}
	
	public static void updateOverall(RecordsScene callback)
	{
		_overall.clear();
		
		ScoreEntry defaultScore = new ScoreEntry();
		defaultScore.setName("Updating overall scoreboard...");
		defaultScore.setScore(-1);
		
		_overall.add(defaultScore);
		
		GetScoresThread thread = new GetScoresThread(_overall, callback, RecordsScene.OVERALL, SCORE_RETRIEVAL_LIMIT);
		thread.start();
	}	
}
