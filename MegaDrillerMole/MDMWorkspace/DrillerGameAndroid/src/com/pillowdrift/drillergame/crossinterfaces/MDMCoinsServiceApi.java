package com.pillowdrift.drillergame.crossinterfaces;

public interface MDMCoinsServiceApi 
{
	public long getCoins();
	public void addToCoins(long points);
	public void removeCoins(long points);
	public void initCoins();
	public void clearCoinEntry();
	public void clearCoinToDefault();
}
