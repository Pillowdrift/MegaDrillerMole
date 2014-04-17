package com.pillowdrift.drillergame.database;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Base database access object.
 * @author cake_cruncher_7
 *
 */
public abstract class DatabaseAccessObject
{
	//DATA
	protected SQLiteDatabase _database;
	protected SQLiteOpenHelper _openHelper;
	protected boolean _open = false;
	
	//ACCESS
	public boolean isOpen()
	{
		return _open;
	}
	public boolean isClosed()
	{
		return !_open;
	}
	
	//CONSTRUCTION
	public DatabaseAccessObject(Context context)
	{
		_openHelper = specifyHelper(context);
	}
	
	//FUNCTION
	/**
	 * Open access to this database
	 */
	public boolean open() throws SQLException
	{
		try
		{
			_database = _openHelper.getWritableDatabase();
			_open = true;
			return true;
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return false;
		}
	}
	
	/**
	 * Close access to this database
	 */
	public boolean close()
	{
		try
		{
			_openHelper.close();
			_database.close();
			_open = false;
			return true;
		}
		catch(Exception e)
		{
			return false;
		}
	}
	
	//Functions must be overridden
	/**
	 * Returns the helper which will be used by this access object
	 */
	public abstract SQLiteOpenHelper specifyHelper(Context context);
}
