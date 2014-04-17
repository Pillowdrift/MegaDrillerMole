package com.pillowdrift.drillergame.crossinterfaces.android;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Looper;

import com.pillowdrift.drillergame.crossinterfaces.MDMFacebookAccessServiceApi;
import com.pillowdrift.drillergame.facebook.Facebook;
import com.pillowdrift.drillergame.facebook.LoginData;
import com.pillowdrift.drillergame.facebook.SessionEvents;
import com.pillowdrift.drillergame.facebook.SessionEvents.AuthListener;
import com.pillowdrift.drillergame.facebook.SessionEvents.LogoutListener;
import com.pillowdrift.drillergame.facebook.SessionStore;

/**
 * Implementation of facebook post for Android
 * @author strawberries
 *
 */
public class AndroidMDMFacebookAccessServiceApi implements MDMFacebookAccessServiceApi {
	
	//DATA
	Activity _activity;
	MDMFacebookAccessServiceApi _facebookService;
	
	//The facebook
	Facebook _facebook = new Facebook("348761428527094");
	
	//Preferences
	private SharedPreferences _prefs;
	
	//The permissions we need for this application
	String[] _permissions = { "publish_stream" };
	
	//The logging in and out functions
	private LoginData _loginData;
	//private Handler	_handler;
	
	//CONSTRUCTION
	public AndroidMDMFacebookAccessServiceApi(Activity activity)
	{
		_activity = activity;
		init();
	}

	//Facebook service
	 private void init() {       
        
		 // restore session if one exists
		 SessionStore.restore(_facebook, _activity.getBaseContext());
		 SessionEvents.addAuthListener(new FbAPIsAuthListener());
		 SessionEvents.addLogoutListener(new FbAPIsLogoutListener());
         
		 _loginData = new LoginData(_activity, 0, _facebook, _permissions);
	}

    /**
     * Authorization for facebook
     */
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        _facebook.authorizeCallback(requestCode, resultCode, data);
    }
    
    /**
     * Logs out of facebook without revoking permissions
     */
    public void logOutOfFacebook() {
    	_loginData.logOut();
    }
    
    /**
     * Extends token on resume
     */
    public void extendFacebookToken()
    {
    	_facebook.extendAccessTokenIfNeeded(_activity.getBaseContext(), null);
    }
    
    /**
     * Logs in to facebook if current sessions isn't already authorised
     */
    public void logInToFacebook()
    {	
    	
    	Thread t = new Thread() {
    		public void run() {
    			try {
    				Looper.prepare();
    				_loginData.logIn();
    				Looper.loop();
    			} catch (Exception ex) {
    				ex.printStackTrace();
    			}
    		}
    	};
    	t.start();
    }

    /**
     * Posts the score to facebook
     */
	@Override
	public void postScoreToFacebook(final long score) {
        Thread t = new Thread() 
        {
            public void run() {
                try {
                    postScore(score);

                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        };
        t.start();
	}
	
	private void postScore(long score) {
		
	String message = "I just got " + score + " score in Mega Driller Mole for Android! "+
					 "Try to beat that!";
	
		logInToFacebook();
		if (_facebook.isSessionValid()) {
			Bundle parameters = new Bundle();
            parameters.putString("message", message);
            parameters.putString("link", "www.pillowdrift.com"); //or any other link
            parameters.putString("name", "Mega Driller Mole");
            try {
				_facebook.request("me/feed", parameters, "POST");
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
			
	}

	@Override
	public boolean isLoggedInToFacebook() {
		return _facebook.isSessionValid();
	}
	
	/*
     * The Callback for notifying the application when authorization succeeds or
     * fails.
     */

    public class FbAPIsAuthListener implements AuthListener {

        @Override
        public void onAuthSucceed() {
        }

        @Override
        public void onAuthFail(String error) {
        }
    }

    /*
     * The Callback for notifying the application when log out starts and
     * finishes.
     */
    public class FbAPIsLogoutListener implements LogoutListener {
        @Override
        public void onLogoutBegin() {
        }

        @Override
        public void onLogoutFinish() {
        }
    }
	
	
}
