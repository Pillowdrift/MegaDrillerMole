package com.pillowdrift.drillergame.crossinterfaces.android;

import java.io.InputStream;
import java.io.OutputStream;

import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import android.app.Activity;
import android.os.Looper;
import android.widget.Toast;

import com.pillowdrift.drillergame.crossinterfaces.MDMTwitterAccessServiceApi;
import com.pillowdrift.drillergame.twitter.TwitterApp;
import com.pillowdrift.drillergame.twitter.TwitterApp.TwDialogListener;
/**
 * Android implementation of the twitter api
 * @author strawberries
 *
 */
public class AndroidMDMTwitterAccessServiceApi implements MDMTwitterAccessServiceApi  {

	private static final String CONSUMER_KEY = "jX0q9ShzIb00CEZvEQgeUA";
	private static final String SECRET_KEY = "lTDPBEPjJlBYVB9zIQ0tbfFa5ar5VFDR9EgkDfKW6c";
	
	Activity _activity;
	
	private TwitterApp _twitter;
	
	
	//CONSTRUCTION
	public AndroidMDMTwitterAccessServiceApi(Activity activity)
	{
		_activity = activity;
		
		_twitter = new TwitterApp(_activity, CONSUMER_KEY, SECRET_KEY);
		_twitter.setListener(mTwLoginDialogListener);
	}
	
	@Override
	public void postScoreToTwitter(long score) {
		String message = "I just got " + score + " score in Mega Driller Mole for Android! "+
				 "Try to beat that! #megadrillermole";
		
		try {
			_twitter.updateStatus(message);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void logInToTwitter() {
		if (!isLoggedInToTwitter())
		{
			new Thread() {
				@Override
				public void run() {
					Looper.prepare();
					_twitter.authorize(); 
					Looper.loop();
				}
			}.start();
		}
	}

	@Override
	public void logOutOfTwitter() {
		_twitter.resetAccessToken();
	}

	@Override
	public void extendTwitterToken() {

		
	}

	@Override
	public boolean isLoggedInToTwitter() {
		// Whether or not we have an access token for this application
		return _twitter.hasAccessToken();
	}

    private final TwDialogListener mTwLoginDialogListener = new TwDialogListener() {
        @Override
        public void onComplete(String value) {
            String username = _twitter.getUsername();
            username        = (username.equals("")) ? "No Name" : username;
 
            Toast.makeText(_activity.getBaseContext(), "Connected to Twitter as " + username, Toast.LENGTH_LONG).show();
        }
 
        @Override
        public void onError(String value) {
 
            Toast.makeText(_activity.getBaseContext(), "Twitter connection failed", Toast.LENGTH_LONG).show();
        }
    };

}
