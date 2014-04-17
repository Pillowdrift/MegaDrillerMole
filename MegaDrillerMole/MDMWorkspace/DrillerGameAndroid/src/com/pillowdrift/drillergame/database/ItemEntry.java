package com.pillowdrift.drillergame.database;

public class ItemEntry {

	//Data
	private long _id;
	private String _name;
	private String _purchased;
	private String _using;
	
	//Type of item (Drill, powerup etc)
	private String _type;
		
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
	
	public String getPurchased()
	{
		return _purchased;
	}
	public void setPurchased(String purchased)
	{
		_purchased = purchased;
	}
	
	public String getUsing()
	{
		return _using;
	}
	public void setUsing(String using)
	{
		_using = using;
	}
	
	public String getType()
	{
		return _type;
	}
	public void setType(String type)
	{
		_type = type;
	}
	
	@Override 
	public String toString()
	{
		return _name + " " + _purchased + " " + _using + " " + _type;
	}
	
	public String toNameString()
	{
		return _name;
	}
	
	public String toPurchasedString()
	{
		return "" + _purchased;
	}
	
	public String toUsingString()
	{
		return "" + _using;
	}
	
	public String toTypeString()
	{
		return "" + _type;
	}
}
