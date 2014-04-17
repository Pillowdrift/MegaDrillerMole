package com.pillowdrift.drillergame;

import com.pillowdrift.drillergame.R;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

/**
 * Splash screen activity with options the leave, go to the options menu, or launch the main game.
 * @author cake_cruncher_7
 *
 */
public class DrillerGameSplashActivity extends Activity {
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        //Force landscape
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        //Set title view
        setContentView(R.layout.main);
        
        //Set button event listeners
        //Launch
        ((Button)findViewById(R.id.btnLaunch)).setOnClickListener(new OnClickListener()
        {
        	public void onClick(View v)
        	{
        		//Launch game activity
        		Intent optionsIntent = new Intent();
        		optionsIntent.setComponent(new ComponentName("com.pillowdrift.drillergame", "com.pillowdrift.drillergame.DrillerGameAndroidActivity"));
        		startActivity(optionsIntent);
        		//End our activity
        		finish();
        	}
        });
        //Options
        ((Button)findViewById(R.id.btnOptions)).setOnClickListener(new OnClickListener()
        {
        	public void onClick(View v)
        	{
        		//Launch options activity
        		Intent optionsIntent = new Intent();
        		optionsIntent.setComponent(new ComponentName("com.pillowdrift.drillergame", "com.pillowdrift.drillergame.DrillerGameOptionsActivity"));
        		startActivity(optionsIntent);
        	}
        });
        //Exit
        ((Button)findViewById(R.id.btnExit)).setOnClickListener(new OnClickListener()
        {
        	public void onClick(View v)
        	{
        		//Quit!
        		finish();
        	}
        });
    }
}
