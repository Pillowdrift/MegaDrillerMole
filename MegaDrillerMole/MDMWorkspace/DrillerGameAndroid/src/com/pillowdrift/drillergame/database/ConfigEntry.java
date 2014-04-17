package com.pillowdrift.drillergame.database;

/**
 * Class representing an entry into the configuration database.
 * @author cake_cruncher_7
 *
 */
public final class ConfigEntry
{
	//Data
	private long _id;
	private String _name;
	private String _setting;
	
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
	public String getSetting()
	{
		return _setting;
	}
	public void setSetting(String setting)
	{
		_setting = setting;
	}
	
	//String
	@Override
	public String toString()
	{
		return _name + "  " + _setting;
	}
	public String toNameString()
	{
		return _name;
	}
	public String toSettingString()
	{
		return "" + _setting;
	}
}
