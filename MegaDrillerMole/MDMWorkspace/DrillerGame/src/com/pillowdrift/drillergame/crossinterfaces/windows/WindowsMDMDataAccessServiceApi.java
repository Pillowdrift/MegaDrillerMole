package com.pillowdrift.drillergame.crossinterfaces.windows;

import com.pillowdrift.drillergame.crossinterfaces.MDMCoinsServiceApi;
import com.pillowdrift.drillergame.crossinterfaces.MDMConfigAccessServiceApi;
import com.pillowdrift.drillergame.crossinterfaces.MDMDataAccessServiceApi;
import com.pillowdrift.drillergame.crossinterfaces.MDMItemsServiceApi;
import com.pillowdrift.drillergame.crossinterfaces.MDMScoreAccessServiceApi;

/**
 * Unimplemented external data service for running on windows.
 * @author cake_cruncher_7
 *
 */
public class WindowsMDMDataAccessServiceApi implements MDMDataAccessServiceApi
{
	//DATA
	boolean _serviceAvailable;
	
	public WindowsMDMDataAccessServiceApi()
	{
		_serviceAvailable = false;
	}

	@Override
	public void begin() {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void pause() {
		// TODO Auto-generated method stub

	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub

	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean serviceAvailable() {
		return _serviceAvailable;
	}

	@Override
	public MDMScoreAccessServiceApi getScoreDataAccessor() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public MDMConfigAccessServiceApi getConfigDataAccessor() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public MDMItemsServiceApi getItemDataAccessor() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public MDMCoinsServiceApi getCoinsDataAccessor() {
		// TODO Auto-generated method stub
		return null;
	}

}
