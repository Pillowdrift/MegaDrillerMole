package com.pillowdrift.drillergame.crossinterfaces.android;

import com.pillowdrift.drillergame.crossinterfaces.WebBrowserServiceApi;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;

/**
 * Implementation of web browser services for android
 * @author cake_cruncher_7
 *
 */
public class AndroidWebBrowserServiceApi implements WebBrowserServiceApi
{
	//DATA
	Activity _activity;
	
	//CONSTRUCTION
	public AndroidWebBrowserServiceApi(Activity activity)
	{
		//Store a reference to the activity we're serving
		_activity = activity;
	}
	
	//FUNCTION
	@Override
	public void openUrl(String url)
	{
		//Launch a web browser as requested
		Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
		_activity.startActivity(browserIntent);
	}

}
