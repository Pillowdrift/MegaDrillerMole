package com.pillowdrift.drillergame.crossinterfaces.android;

import android.app.Activity;

import com.pillowdrift.drillergame.crossinterfaces.MDMCoinsServiceApi;
import com.pillowdrift.drillergame.crossinterfaces.MDMConfigAccessServiceApi;
import com.pillowdrift.drillergame.crossinterfaces.MDMDataAccessServiceApi;
import com.pillowdrift.drillergame.crossinterfaces.MDMItemsServiceApi;
import com.pillowdrift.drillergame.crossinterfaces.MDMScoreAccessServiceApi;
import com.pillowdrift.drillergame.database.CoinsDAO;
import com.pillowdrift.drillergame.database.ConfigDAO;
import com.pillowdrift.drillergame.database.ItemDAO;
import com.pillowdrift.drillergame.database.ScoreDAO;

/**
 * Working android implementation fo the external data access service used to maintain scores and configuration.
 * @author cake_cruncher_7
 *
 */
public class AndroidMDMDataAccessServiceApi implements MDMDataAccessServiceApi
{
	//DATA
	ScoreDAO _scoreDao;
	ConfigDAO _configDao;
	ItemDAO _itemDao;
	CoinsDAO _coinsDao;
	Activity _activity;
	boolean _serviceAvailable;
		
	//CONSTRUCTION
	public AndroidMDMDataAccessServiceApi(Activity activity)
	{
		//Store a reference to the activity we're serving
		_activity = activity;
		
		//Create our database access objects
		_scoreDao = new ScoreDAO(activity);
		_configDao = new ConfigDAO(activity);
		_itemDao = new ItemDAO(activity);
		_coinsDao = new CoinsDAO(activity);
	}
	
	void openDAOs()
	{
		closeDAOs();
		_serviceAvailable = true;
		_serviceAvailable &= _scoreDao.open();
		_serviceAvailable &= _configDao.open();
		_serviceAvailable &= _itemDao.open();
		_serviceAvailable &= _coinsDao.open();
	}
	
	void closeDAOs()
	{
		_scoreDao.close();
		_configDao.close();
		_itemDao.close();
		_coinsDao.close();
		_serviceAvailable = false;
	}
	
	@Override
	public void begin()
	{
		openDAOs();
	}
	                                                                    
	@Override
	public void pause()
	{
		closeDAOs();
	}

	@Override
	public void resume()
	{
		openDAOs();
	}

	@Override
	public void dispose()
	{
		closeDAOs();
	}

	@Override
	public boolean serviceAvailable() {
		return _serviceAvailable;
	}

	@Override
	public MDMScoreAccessServiceApi getScoreDataAccessor() {
		return _scoreDao;
	}

	@Override
	public MDMConfigAccessServiceApi getConfigDataAccessor() {
		return _configDao;
	}

	@Override
	public MDMItemsServiceApi getItemDataAccessor() {
		return _itemDao;
	}

	@Override
	public MDMCoinsServiceApi getCoinsDataAccessor() {
		return _coinsDao;
	}

}
