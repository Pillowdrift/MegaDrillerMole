package com.pillowdrift.drillergame.scoreboard;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;

import com.pillowdrift.drillergame.entities.menu.buttons.SubmitScoreOnlineHighscores;
import com.pillowdrift.drillergame.other.Base64;

public class SendScoreThread
	extends Thread
{
	private SubmitScoreOnlineHighscores _button;
	private String _name;
	private long _score;
	
	public SendScoreThread(SubmitScoreOnlineHighscores button, String name, long score)
	{
		_button = button;
		_name = name;
		_score = score;
	}
	
	public void run()
	{
		// Try submitting
		final HttpParams params = new BasicHttpParams();
		HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
		HttpProtocolParams.setUseExpectContinue(params, false);
		
		HttpClient httpclient = new DefaultHttpClient(params);
		HttpPost httppost = new HttpPost("http://www.pillowdrift.com/mole/");
		HttpResponse response = null;
		
		try
		{			
			// Encode the data to base 64 so it's both safe to POST and harder to identify
			String postData = _name + "," + _score;
			String encoded = Base64.encodeBytes(Base64.encodeBytesToBytes(postData.getBytes()));
			
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
			nameValuePairs.add(new BasicNameValuePair("data", encoded));
			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
			
			response = httpclient.execute(httppost);
		}
		catch (Exception e)
		{
			_button.requestComplete(false, 0, 0);
		}
		
		if (response != null)
		{		
			Header hOverall = response.getFirstHeader("overall");
			Header hWeekly = response.getFirstHeader("weekly");
			
			if (hOverall != null && hWeekly != null)
			{
				long overall = Long.parseLong(hOverall.getValue());
				long weekly = Long.parseLong(hWeekly.getValue());
				
				_button.requestComplete(true, weekly, overall);
				
				return;
			}
		}
			
		_button.requestComplete(false, 0, 0);
	}
}
