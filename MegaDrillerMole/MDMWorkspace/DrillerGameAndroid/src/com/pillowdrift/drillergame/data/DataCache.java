package com.pillowdrift.drillergame.data;

import java.util.List;
import java.util.Map;

import com.pillowdrift.drillergame.crossinterfaces.MDMDataAccessServiceApi;
import com.pillowdrift.drillergame.database.ConfigDAO;
import com.pillowdrift.drillergame.database.ConfigEntry;
import com.pillowdrift.drillergame.database.ItemEntry;
import com.pillowdrift.drillergame.database.ScoreDAO;
import com.pillowdrift.drillergame.database.ScoreEntry;

/**
 * Singleton which keeps track of the last known set of scores and config and provides access to the database.
 * @author cake_cruncher_7
 *
 */
public final class DataCache
{
	static MDMDataAccessServiceApi _dataService;
	static List<ScoreEntry> _scores;
	static Map<String, ConfigEntry> _config;
	static Map<String, ItemEntry> _item;
	static long _coins;
	
	/**
	 * Set's the service from which we will try to obtain updated information.
	 * @param service
	 */
	public static void setDataService(MDMDataAccessServiceApi service)
	{
		_dataService = service;
	}
	/**
	 * Returns a sorted collection of scores from the cache.
	 * Collection is sorted in descending order.
	 */
	public static List<ScoreEntry> getScores()
	{
		return _scores;
	}
	/**
	 * Returns a sorted collection of scores from the database
	 * and updates our static cache.
	 * Collection is sorted in descending order.
	 * @return
	 */
	public static List<ScoreEntry> updateScores()
	{
		if(_dataService.serviceAvailable())
		{
			_scores = _dataService.getScoreDataAccessor().getSortedScores();
		}
		else
		{
			_scores = null;
		}
		
		return _scores;
	}
	/**
	 * Returns a map of configuration from the database
	 * and updates our static cache.
	 * @return
	 */
	public static Map<String, ConfigEntry> updateConfig()
	{
		if(_dataService.serviceAvailable())
		{
			_config = _dataService.getConfigDataAccessor().getConfigMap();
		}
		else
		{
			_config = null;
		}
		
		return _config;
	}

	/**
	 * Returns the score's potential place in the rankings - negative if not in them at all
	 * @param score
	 * @return
	 */
	public static int isScoreHigh(long score)
	{
		//Give up now if we don't have database access
		if(!_dataService.serviceAvailable())
			return -5;
		
		//Return -5 if not on there at all :C
		if(_scores.get(ScoreDAO.NUMBER_OF_SCORES-1).getScore() >= score)
			return -5;
		
		//We're on there somewhere, definitely at least pos 10
		int place = 10;
		//Start loop from pos 9 (NUMBER_OF_SCORES-2)
		for(int i = ScoreDAO.NUMBER_OF_SCORES-2; i >= 0; --i)
		{
			if(_scores.get(i).getScore() > score)
			{
				//We have been bested, report the previous position
				break;
			}

			//We are better than this score, decrease our position
			--place;
		}
		//Report position
		return place;
	}
	
	/**
	 * Adds the given name and score to the database if they have a place within it
	 * @param name
	 * @param score
	 */
	public static void proposeNewScoreEntry(String name, long score)
	{
		_dataService.getScoreDataAccessor().proposeNewEntry(name, score);
		updateScores();
	}
	
	/**
	 * Returns the highest current score
	 * @return
	 */
	public static long getBestScore()
	{
		if(_dataService.serviceAvailable())
			return _scores.get(0).getScore();
		else
			return 0;
	}
	
	public static String getConfigSetting(String name)
	{
		if(_dataService.serviceAvailable())
		{
			return _config.get(name).getSetting();
		}
		else
		{
			return ConfigDAO.OnOff.ERROR;
		}
	}
	
	/**
	 * Returns a map of items from the database
	 * and updates our static cache.
	 * @return
	 */
	public static Map<String, ItemEntry> updateItem()
	{
		if(_dataService.serviceAvailable())
		{
			_item = _dataService.getItemDataAccessor().getItemMap();
		}
		else
		{
			_item = null;
		}
		
		return _item;
	}
	
	public static String getItemPurchased(String name)
	{
		if(_dataService.serviceAvailable())
		{
			return _item.get(name).getPurchased();
		}
		else
		{
			return ConfigDAO.OnOff.ERROR;
		}
	}
	
	public static String getItemUsing(String name)
	{
		if(_dataService.serviceAvailable())
		{
			return _item.get(name).getUsing();
		}
		else
		{
			return ConfigDAO.OnOff.ERROR;
		}
	}
	
	public static long getCoins()
	{
		if(_dataService.serviceAvailable())
		{
			_coins = _dataService.getCoinsDataAccessor().getCoins();
		}
		else
		{
			_coins = -1;
		}
		
		return _coins;
	}
	
	public static void addCoins(long coins)
	{
		if (_dataService.serviceAvailable())
		{
			_dataService.getCoinsDataAccessor().addToCoins(coins);
		}
	}
	
	public static void removeCoins(long coins)
	{
		if (_dataService.serviceAvailable())
		{
			_dataService.getCoinsDataAccessor().removeCoins(coins);
		}
	}
}
