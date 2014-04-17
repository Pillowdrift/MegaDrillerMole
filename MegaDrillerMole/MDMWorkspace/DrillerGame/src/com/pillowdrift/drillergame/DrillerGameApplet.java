package com.pillowdrift.drillergame;

import com.badlogic.gdx.backends.lwjgl.LwjglApplet;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.pillowdrift.drillergame.DrillerGame01;
import com.pillowdrift.drillergame.crossinterfaces.MDMAdsServiceApi;
import com.pillowdrift.drillergame.crossinterfaces.windows.WindowsMDMDataAccessServiceApi;
import com.pillowdrift.drillergame.crossinterfaces.windows.WindowsMDMFacebookAccessServiceApi;
import com.pillowdrift.drillergame.crossinterfaces.windows.WindowsMDMTwitterAccessServiceApi;
import com.pillowdrift.drillergame.crossinterfaces.windows.WindowsWebBrowserServiceApi;

/**
 * PC wrapper around the basic game functionality.
 * Creates a windows web browser service, and a fake windows data access service to pass to the game.
 * Scores/config databases not functional on Windows
 * @author cake_cruncher_7
 *
 */
public class DrillerGameApplet extends LwjglApplet 
{
	
	public DrillerGameApplet()
	{	
		super(new DrillerGame01(new WindowsWebBrowserServiceApi(),
			      new WindowsMDMDataAccessServiceApi(),
			      new WindowsMDMFacebookAccessServiceApi(),
				  new WindowsMDMTwitterAccessServiceApi(),
				  new AdsService()), false);
	
	}

}

class AdsService implements MDMAdsServiceApi
{
	@Override
	public void turnOnAds() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void turnOffAds() {
		// TODO Auto-generated method stub
		
	}
}
