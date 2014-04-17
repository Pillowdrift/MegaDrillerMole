package com.pillowdrift.drillergame.scoreboard;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;

import com.pillowdrift.drillergame.database.ScoreEntry;
import com.pillowdrift.drillergame.scenes.RecordsScene;

public class GetScoresThread
	extends Thread
{	
	private static String[] SCORE_TYPES =
	{
		"local",
		"weekly",
		"overall"
	};
	
	ArrayList<ScoreEntry> _scoresList;
	private RecordsScene _callback;
	private int _state;
	private int _limit;
	
	public GetScoresThread(ArrayList<ScoreEntry> scores, RecordsScene callback, int state, int limit)
	{
		_scoresList = scores;
		_callback = callback;
		_state = state;
		_limit = limit;
	}
	
	public void run()
	{
		// Try submitting
		final HttpParams params = new BasicHttpParams();
		HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
		HttpProtocolParams.setUseExpectContinue(params, false);
		
		String url = "http://www.pillowdrift.com/mole/?type=" +
			SCORE_TYPES[_state] + "&limit=" + Integer.toString(_limit);
		
		HttpClient httpclient = new DefaultHttpClient(params);
		HttpGet httpget = new HttpGet(url);
		HttpResponse response = null;
		
		try
		{
			response = httpclient.execute(httpget); 
		}
		catch (Exception e)
		{

		}
		
		if (response != null)
		{
			try
			{
				// Get content of reply
				BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
				
				// Convert it to a string
				StringBuilder sb = new StringBuilder();
				String line = null;
				
				while ((line = reader.readLine()) != null)
					sb.append(line);
				
				String responseString = sb.toString();
				
				// Get individual scores
				String[] scores = responseString.split("\\.");

				// Add each one to the list
				_scoresList.clear();
				
				for (int i = 0; i < scores.length; ++i)
				{
					// Parse score
					String[] score = scores[i].split(",");
					
					// If there's at least a name and a score
					if (score.length >= 2)
					{
						ScoreEntry currentScore = new ScoreEntry();
						
						currentScore.setId(i+1);
						currentScore.setName(score[0]);
						currentScore.setScore(Integer.parseInt(score[1]));
						
						_scoresList.add(currentScore);
					}
				}
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
		
		_callback.updateTable();
	}
}
