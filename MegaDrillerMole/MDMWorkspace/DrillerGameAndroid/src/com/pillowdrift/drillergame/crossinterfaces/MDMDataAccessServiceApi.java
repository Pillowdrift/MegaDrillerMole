package com.pillowdrift.drillergame.crossinterfaces;

/**
 * Interface for multiple implementations of external data storage
 * for this specific game. Scores, settings, trophies, etc
 * @author cake_cruncher_7
 *
 */
public interface MDMDataAccessServiceApi
{
	public MDMScoreAccessServiceApi getScoreDataAccessor();
	public MDMConfigAccessServiceApi getConfigDataAccessor();
	public MDMItemsServiceApi getItemDataAccessor();
	public MDMCoinsServiceApi getCoinsDataAccessor();
	public boolean serviceAvailable();
	public void begin();
	public void pause();
	public void resume();
	public void dispose();
}
