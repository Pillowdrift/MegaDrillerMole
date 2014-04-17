package com.pillowdrift.drillergame.database;

/**
 * Class representing an entry into the high score database
 * @author cake_cruncher_7
 *
 */
public final class ScoreEntry
{
	//Data
	private long _id;
	private String _name;
	private long _score;
	
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
	public long getScore()
	{
		return _score;
	}
	public void setScore(long score)
	{
		_score = score;
	}
	
	//String
	@Override
	public String toString()
	{
		return _name + "  " + _score;
	}
	public String toNameString()
	{
		return _name;
	}
	public String toScoreString()
	{
		return "" + _score;
	}
}
