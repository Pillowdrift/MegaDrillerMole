package com.pillowdrift.drillergame.database;

public class CoinsEntry {
	
	private long _id;
	private String _name;
	private long _coins;
	
	//Access
	public long getId()
	{
		return _id;
	}
	public void setId(long id)
	{
		_id = id;
	}
	
	public String getName()
	{
		return _name;
	}
	public void setName(String name)
	{
		_name = name;
	}
	
	public long getCoins()
	{
		return _coins;
	}
	public void setCoins(long coins)
	{
		_coins = coins;
	}
	
	@Override
	public String toString()
	{
		return _name + " " + _coins;
	}
	
	public String toCoinsString()
	{
		return "" + _coins;
	}
}
