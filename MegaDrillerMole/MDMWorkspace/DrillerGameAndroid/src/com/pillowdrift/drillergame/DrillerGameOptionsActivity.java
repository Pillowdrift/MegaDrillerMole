package com.pillowdrift.drillergame;

import java.util.Map;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ToggleButton;

import com.pillowdrift.drillergame.R;
import com.pillowdrift.drillergame.crossinterfaces.MDMDataAccessServiceApi;
import com.pillowdrift.drillergame.crossinterfaces.android.AndroidMDMDataAccessServiceApi;
import com.pillowdrift.drillergame.database.ConfigDAO;
import com.pillowdrift.drillergame.database.ConfigEntry;

/**
 * Options screen activity providing access to change configuration and clear play records.
 * @author cake_cruncher_7
 *
 */
public class DrillerGameOptionsActivity extends Activity
{
	//Database service
	MDMDataAccessServiceApi _dataService;
	
	//Query for clear data sureness
	AlertDialog.Builder _clearMsg;
	
	//Not implemented message
	AlertDialog.Builder _audioNotImp;
	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        //Force landscape
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        //Create data service
        _dataService = new AndroidMDMDataAccessServiceApi(this);
        //Open the databases
        _dataService.begin();
        
        //Set options view
        setContentView(R.layout.options);
        
        //Set up clear warning and response function
        _clearMsg = new AlertDialog.Builder(this);
        //Response function
        DialogInterface.OnClickListener clearMsgCListen = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface sure, int response) {
                switch (response){
                case DialogInterface.BUTTON_POSITIVE:
                    //Yes button clicked
                	//Clear scores to default
                	_dataService.getScoreDataAccessor().clearScoresToDefault();
                    break;

                case DialogInterface.BUTTON_NEGATIVE:
                    //No button clicked
                	//Do nothing
                    break;
                }
            }
        };
        //Message
        _clearMsg.setMessage("Are you sure you want to delete all saved data?");
        //Options
        _clearMsg.setPositiveButton("Yes",  clearMsgCListen);
        _clearMsg.setNegativeButton("No", clearMsgCListen);

        //Set up not implemented message
        _audioNotImp = new AlertDialog.Builder(this);
        //Message
        _audioNotImp.setMessage("Audio not yet implemented");
        //Options
        _audioNotImp.setPositiveButton("OK", null);
        
        //Get config state
        Map<String, ConfigEntry> config = _dataService.getConfigDataAccessor().getConfigMap();
        
        //Set button event listeners
        //Back
        ((Button)findViewById(R.id.btnBack)).setOnClickListener(new OnClickListener()
        {
        	public void onClick(View v)
        	{
        		//Close database access
        		_dataService.dispose();
        		//Go back to the previous screen
        		finish();
        	}
        });
        //Clear scores
        ((Button)findViewById(R.id.btnClear)).setOnClickListener(new OnClickListener()
        {
        	public void onClick(View v)
        	{
        		_clearMsg.show();
        	}
        });
        //Particle toggle
        //Default button to database state
        ((ToggleButton)findViewById(R.id.togParticles)).setChecked(config.get(ConfigDAO.SettingNames.PARTICLES).getSetting().equals(ConfigDAO.OnOff.ON));
        ((ToggleButton)findViewById(R.id.togParticles)).setOnClickListener(new OnClickListener()
        {
        	public void onClick(View v)
        	{
        		//Set particles setting to match button state
        		_dataService.getConfigDataAccessor().modifyConfigEntry(ConfigDAO.SettingNames.PARTICLES, ((ToggleButton)v).isChecked()? ConfigDAO.OnOff.ON : ConfigDAO.OnOff.OFF);
        	}
        });
        //Audio options
        //Default button to database state
        ((ToggleButton)findViewById(R.id.togMusic)).setChecked(config.get(ConfigDAO.SettingNames.MUSIC).getSetting().equals(ConfigDAO.OnOff.ON));
        ((ToggleButton)findViewById(R.id.togMusic)).setOnClickListener(new OnClickListener()
        {
        	public void onClick(View v)
        	{
        		//Set music setting to match button state
        		_dataService.getConfigDataAccessor().modifyConfigEntry(ConfigDAO.SettingNames.MUSIC, ((ToggleButton)v).isChecked()? ConfigDAO.OnOff.ON : ConfigDAO.OnOff.OFF);
        		//Show unimplemented warning
        		_audioNotImp.show();
        	}
        });
        //Default button to database state
        ((ToggleButton)findViewById(R.id.togSfx)).setChecked(config.get(ConfigDAO.SettingNames.SFX).getSetting().equals(ConfigDAO.OnOff.ON));
        ((ToggleButton)findViewById(R.id.togSfx)).setOnClickListener(new OnClickListener()
        {
        	public void onClick(View v)
        	{
        		//Set sfx setting to match button state
        		_dataService.getConfigDataAccessor().modifyConfigEntry(ConfigDAO.SettingNames.SFX, ((ToggleButton)v).isChecked()? ConfigDAO.OnOff.ON : ConfigDAO.OnOff.OFF);
        		//Show unimplemented warning
        		_audioNotImp.show();
        	}
        });
    }
}
