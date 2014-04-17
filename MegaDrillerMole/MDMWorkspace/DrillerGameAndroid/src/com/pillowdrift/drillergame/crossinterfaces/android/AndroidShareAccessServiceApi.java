package com.pillowdrift.drillergame.crossinterfaces.android;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;

import com.pillowdrift.drillergame.crossinterfaces.ShareAccessServiceApi;

public class AndroidShareAccessServiceApi implements ShareAccessServiceApi {
	// Vars
	Activity _activity;
	
	/**
	 * Create a new share api.
	 */
	public AndroidShareAccessServiceApi(Activity activity) {
		_activity = activity;
	}
	
	/**
	 * Share a message.
	 */
	public void share(String title, String message) {
		// Create an intent.
		Intent sharing = new Intent(android.content.Intent.ACTION_SEND_MULTIPLE);
		
		// Set the type.
		sharing.setType("text/plain");
		
		// Set the text.
		sharing.putExtra(android.content.Intent.EXTRA_SUBJECT, title);
		sharing.putExtra(android.content.Intent.EXTRA_TEXT, message);
		sharing.putExtra(android.content.Intent.EXTRA_STREAM, Uri.parse("http://www.pillowdrift.com/wp-content/uploads/2012/06/titleImage1.png"));
		
		// Start the activity.
		_activity.startActivity(Intent.createChooser(sharing, "Share Via"));
	}
}
