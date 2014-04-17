package com.pillowdrift.drillergame;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.mobclix.android.sdk.MobclixAdViewListener;
import com.mobclix.android.sdk.MobclixFullScreenAdView;
import com.mobclix.android.sdk.MobclixMMABannerXLAdView;
import com.mobclix.android.sdk.MobclixAdView;
import com.pillowdrift.drillergame.crossinterfaces.MDMAdsServiceApi;
import com.pillowdrift.drillergame.crossinterfaces.MDMFacebookAccessServiceApi;
import com.pillowdrift.drillergame.crossinterfaces.MDMTwitterAccessServiceApi;
import com.pillowdrift.drillergame.crossinterfaces.android.AndroidMDMDataAccessServiceApi;
import com.pillowdrift.drillergame.crossinterfaces.android.AndroidMDMFacebookAccessServiceApi;
import com.pillowdrift.drillergame.crossinterfaces.android.AndroidMDMTwitterAccessServiceApi;
import com.pillowdrift.drillergame.crossinterfaces.android.AndroidWebBrowserServiceApi;
import com.pillowdrift.drillergame.crossinterfaces.android.AndroidMDMFacebookAccessServiceApi.FbAPIsAuthListener;
import com.pillowdrift.drillergame.crossinterfaces.android.AndroidMDMFacebookAccessServiceApi.FbAPIsLogoutListener;
import com.pillowdrift.drillergame.facebook.Facebook;
import com.pillowdrift.drillergame.facebook.LoginData;
import com.pillowdrift.drillergame.facebook.SessionEvents;
import com.pillowdrift.drillergame.facebook.SessionStore;
import com.pillowdrift.drillergame.facebook.SessionEvents.AuthListener;
import com.pillowdrift.drillergame.facebook.SessionEvents.LogoutListener;
import com.pillowdrift.drillergame.twitter.TwitterApp;
import com.pillowdrift.drillergame.twitter.TwitterApp.TwDialogListener;

/**
 * Android wrapper around the basic game functionality.
 * Creates an android web browser service, and an android external data access service to pass to the game.
 * Scores/config databases are functional in the android implementation.
 * @author cake_cruncher_7
 *
 */
public class DrillerGameAndroidActivity extends AndroidApplication implements MDMAdsServiceApi, MDMTwitterAccessServiceApi, MDMFacebookAccessServiceApi {
	
	MobclixAdView _adView; // An adview
	boolean isShowing = false;
	
    private final int SHOW_ADS = 1;
    private final int HIDE_ADS = 0;
    private final int LOG_IN_TWITTER = 2;
    private final int LOG_IN_FACEBOOK = 3;
    
	private static final String CONSUMER_KEY = "jX0q9ShzIb00CEZvEQgeUA";
	private static final String SECRET_KEY = "lTDPBEPjJlBYVB9zIQ0tbfFa5ar5VFDR9EgkDfKW6c";
	
	//The twitter
	private TwitterApp _twitter;
	
	//The facebook
	Facebook _facebook = new Facebook("348761428527094");
	
	//Preferences
	private SharedPreferences _prefs;
	
	//The permissions we need for this application
	String[] _permissions = { "publish_stream" };
	
	//The logging in and out functions
	private LoginData _loginData;
	
    protected Handler handler = new Handler()
    {
        @Override
        public void handleMessage(Message msg) {
            switch(msg.what) {
                case SHOW_ADS:
                {
                    _adView.setVisibility(View.VISIBLE);
                    break;
                }
                case HIDE_ADS:
                {
                    _adView.setVisibility(View.GONE);
                    break;
                }
                case LOG_IN_TWITTER:
                {
                	_twitter.authorize();
                	break;
                }
                case LOG_IN_FACEBOOK:
                {
                	_loginData.logIn();
                	break;
                }
            }
        }
    };
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
		 // restore session if one exists
		 SessionStore.restore(_facebook, this.getBaseContext());
		 SessionEvents.addAuthListener(new FbAPIsAuthListener());
		 SessionEvents.addLogoutListener(new FbAPIsLogoutListener());
        
		 _loginData = new LoginData(this, 0, _facebook, _permissions);
        
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        
        //twitter init
        _twitter = new TwitterApp(this, CONSUMER_KEY, SECRET_KEY);
		_twitter.setListener(mTwLoginDialogListener);
		
		// layout for ads
        RelativeLayout layout = new RelativeLayout(this);
        
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, 
                        WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
        
        _adView = new MobclixMMABannerXLAdView(this);
        _adView.getAd();
        _adView.setVisibility(View.GONE);
        
        // Create the libgdx View
        View gameView = initializeForView(new DrillerGame01(new AndroidWebBrowserServiceApi(this),
				 						  new AndroidMDMDataAccessServiceApi(this),
				 						  this,
				 						  this,
				 						  this), false);
        
        layout.addView(gameView);
        RelativeLayout.LayoutParams adParams = 
                new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, 
                                RelativeLayout.LayoutParams.WRAP_CONTENT);
        adParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        adParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        
        layout.addView(_adView, adParams);
        
        // Hook it all up
        setContentView(layout);
    }
    
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
    	_facebook.authorizeCallback(requestCode, resultCode, data);
    }
	
	@Override
	public void postScoreToTwitter(long score) {
		String message = "I just got " + score + " score in Mega Driller Mole for Android! "+
				 "Try to beat that! #megadrillermole";
		
		try {
			_twitter.updateStatus(message);
		} catch (Exception e) {

			e.printStackTrace();
		}
	}

	@Override
	public void logInToTwitter() {
		/*if (!isLoggedIn())
		{
			new Thread() {
				@Override
				public void run() {
					Looper.prepare();
					_twitter.authorize(); 
					Looper.loop();
				}
			}.start();
		}*/
		handler.sendEmptyMessage(LOG_IN_TWITTER);
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
            //String username = _twitter.getUsername();
            //username        = (username.equals("")) ? "No Name" : username;
 
            //Toast.makeText(_activity.getBaseContext(), "Connected to Twitter as " + username, Toast.LENGTH_LONG).show();
        }
 
        @Override
        public void onError(String value) {
 
            //Toast.makeText(_activity.getBaseContext(), "Twitter connection failed", Toast.LENGTH_LONG).show();
        }
    };
    
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
    	_facebook.extendAccessTokenIfNeeded(this.getBaseContext(), null);
    }
    
    /**
     * Logs in to facebook if current sessions isn't already authorised
     */
    public void logInToFacebook()
    {	
    	
    	/*Thread t = new Thread() {
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
    	t.start();*/
    	handler.sendEmptyMessage(LOG_IN_FACEBOOK);
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
            parameters.putString("link", "http://www.pillowdrift.com/index.php/games/"); //or any other link
            parameters.putString("name", "Mega Driller Mole");
            try {
				_facebook.request("me/feed", parameters, "POST");
			} catch (FileNotFoundException e) {

				e.printStackTrace();
			} catch (MalformedURLException e) {

				e.printStackTrace();
			} catch (IOException e) {

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

	@Override
	public void turnOnAds() {
		// TODO Auto-generated method stub
		isShowing = true;
		handler.sendEmptyMessage(SHOW_ADS);
	}

	@Override
	public void turnOffAds() {
		isShowing = false;
		handler.sendEmptyMessage(HIDE_ADS);
		// TODO Auto-generated method stub
		
	}
	
	
}