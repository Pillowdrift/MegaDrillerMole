/**
 * @author Lorensius W. L. T <lorenz@londatiga.net>
 * 
 * http://www.londatiga.net
 */

package com.pillowdrift.drillergame.twitter;

import java.net.MalformedURLException;
import java.net.URLDecoder;

import oauth.signpost.OAuthProvider;
import oauth.signpost.basic.DefaultOAuthProvider;
import oauth.signpost.commonshttp.CommonsHttpOAuthConsumer;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import twitter4j.User;

import android.os.Handler;
import android.os.Message;

import android.content.Context;
import android.util.Log;
import java.net.URL;

public class TwitterApp {
	private Twitter _twitter;
	private TwitterSession _session;
	private AccessToken _accessToken;
	private CommonsHttpOAuthConsumer _httpOauthConsumer;
	private OAuthProvider _httpOauthprovider;
	private String _consumerKey;
	private String _secretKey;
	private TwDialogListener mListener;
	private Context context;
	private boolean mInit = true;
	
	public static final String CALLBACK_URL = "twitterapp://connect";
	private static final String TAG = "TwitterApp";
	
	public TwitterApp(Context context, String consumerKey, String secretKey) {
		this.context	= context;
		
		_twitter 		= new TwitterFactory().getInstance();
		_session		= new TwitterSession(context);
		
		_consumerKey 	= consumerKey;
		_secretKey	 	= secretKey;
	
		_httpOauthConsumer = new CommonsHttpOAuthConsumer(_consumerKey, _secretKey);
		_httpOauthprovider = new DefaultOAuthProvider("http://api.twitter.com/oauth/request_token",
													 "http://api.twitter.com/oauth/access_token",
													 "http://api.twitter.com/oauth/authorize");
		
		_accessToken	= _session.getAccessToken();
		
		configureToken();
	}
	
	public void setListener(TwDialogListener listener) {
		mListener = listener;
	}
	
	private void configureToken() {
		if (_accessToken != null) {
			if (mInit) {
				_twitter.setOAuthConsumer(_consumerKey, _secretKey);
				mInit = false;
			}
			
			_twitter.setOAuthAccessToken(_accessToken);
		}
	}
	
	public boolean hasAccessToken() {
		return (_accessToken == null) ? false : true;
	}
	
	public void resetAccessToken() {
		if (_accessToken != null) {
			_session.resetAccessToken();
		
			_accessToken = null;
		}
	}
	
	public String getUsername() {
		return _session.getUsername();
	}
	
	public void updateStatus(String status) throws Exception {
		try {
			_twitter.updateStatus(status);
		} catch (TwitterException e) {
			throw e;
		}
	}
	
	public void authorize() {
		
		new Thread() {
			@Override
			public void run() {
				String authUrl = "";
				int what = 1;
				
				try {
					authUrl = _httpOauthprovider.retrieveRequestToken(_httpOauthConsumer, CALLBACK_URL);	
					
					what = 0;
					
					Log.d(TAG, "Request token url " + authUrl);
				} catch (Exception e) {
					Log.d(TAG, "Failed to get request token");
					
					e.printStackTrace();
				}
				
				mHandler.sendMessage(mHandler.obtainMessage(what, 1, 0, authUrl));
			}
		}.start();
	}
	
	public void processToken(String callbackUrl)  {
		
		final String verifier = getVerifier(callbackUrl);

		new Thread() {
			@Override
			public void run() {
				int what = 1;
				
				try {
					_httpOauthprovider.retrieveAccessToken(_httpOauthConsumer, verifier);
		
					_accessToken = new AccessToken(_httpOauthConsumer.getToken(), _httpOauthConsumer.getTokenSecret());
				
					configureToken();
				
					User user = _twitter.verifyCredentials();
				
			        _session.storeAccessToken(_accessToken, user.getName());
			        
			        what = 0;
				} catch (Exception e){
					Log.d(TAG, "Error getting access token");
					
					e.printStackTrace();
				}
				
				mHandler.sendMessage(mHandler.obtainMessage(what, 2, 0));
			}
		}.start();
	}
	
	private String getVerifier(String callbackUrl) {
		String verifier	 = "";
		
		try {
			callbackUrl = callbackUrl.replace("twitterapp", "http");
			
			URL url 		= new URL(callbackUrl);
			String query 	= url.getQuery();
		
			String array[]	= query.split("&");

			for (String parameter : array) {
	             String v[] = parameter.split("=");
	             
	             if (URLDecoder.decode(v[0]).equals(oauth.signpost.OAuth.OAUTH_VERIFIER)) {
	            	 verifier = URLDecoder.decode(v[1]);
	            	 break;
	             }
	        }
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		
		return verifier;
	}
	
	private void showLoginDialog(String url) {
		final TwDialogListener listener = new TwDialogListener() {
			@Override
			public void onComplete(String value) {
				processToken(value);
			}
			
			@Override
			public void onError(String value) {
				mListener.onError("Failed opening authorization page");
			}
		};
		
		new TwitterDialog(context, url, listener).show();
	}
	
	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			
			if (msg.what == 1) {
				if (msg.arg1 == 1)
					mListener.onError("Error getting request token");
				else
					mListener.onError("Error getting access token");
			} else {
				if (msg.arg1 == 1)
					showLoginDialog((String) msg.obj);
				else
					mListener.onComplete("");
			}
		}
	};
	
	public interface TwDialogListener {
		public void onComplete(String value);		
		
		public void onError(String value);
	}
}