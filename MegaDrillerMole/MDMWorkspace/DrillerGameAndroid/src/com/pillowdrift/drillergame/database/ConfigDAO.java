package com.pillowdrift.drillergame.database;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteOpenHelper;

import com.pillowdrift.drillergame.crossinterfaces.MDMConfigAccessServiceApi;

public class ConfigDAO extends DatabaseAccessObject implements MDMConfigAccessServiceApi
{
	//CONSTANTS
	public static final class OnOff
	{
		public static final String ON = "boolon";
		public static final String OFF = "booloff";
		public static final String ERROR = "boolerror";
	}
	
	public static final class SettingNames
	{
		public static final String PARTICLES = "Particles";
		public static final String MUSIC = "Music";
		public static final String SFX = "Sfx";
	}
	
	public static final String[] DEFAULT_NAMES =
	{
		SettingNames.PARTICLES,
		SettingNames.MUSIC,
		SettingNames.SFX
	};
	
	public static final String[] DEFAULT_SETTINGS =
	{
		OnOff.ON,
		OnOff.ON,
		OnOff.ON
	};
	
	//CONSTRUCTION
	public ConfigDAO(Context context)
	{
		super(context);
		
		//Temporarily open the database
		open();
		
		//Check if the config table is empty
		Cursor cursor = _database.query(MDMDatabaseHelper.TABLE_CONFIG_NAME,
									   MDMDatabaseHelper.TABLE_CONFIG_COLUMNS,
									   null,
									   null,
									   null,
									   null,
									   null);
		//Get a count of all rows in the table
		int i = cursor.getCount();
		
		//Set default values if there are no rows currently
		if(i == 0)
		{
			clearConfigToDefault();
		}
		cursor.close();
		//Close the database
		close();
	}
	
	/**
	 * Returns a collection of all entries in the config table
	 * @return
	 */
	public List<ConfigEntry> getConfig()
	{
		List<ConfigEntry> config = new ArrayList<ConfigEntry>();
		Cursor cursor = _database.query(MDMDatabaseHelper.TABLE_CONFIG_NAME,
									   MDMDatabaseHelper.TABLE_CONFIG_COLUMNS,
									   null,
									   null,
									   null,
									   null,
									   null);
		cursor.moveToFirst();
		while(!cursor.isAfterLast())
		{
			ConfigEntry entry = cursorToEntry(cursor);
			config.add(entry);
			cursor.moveToNext();
		}
		//Close the cursor
		cursor.close();
		return config;
	}
	
	/**
	 * Returns a hashed collection of all entries in the config table
	 * @return
	 */
	public Map<String, ConfigEntry> getConfigMap()
	{
		Map<String, ConfigEntry> config = new HashMap<String, ConfigEntry>();
		Cursor cursor = _database.query(MDMDatabaseHelper.TABLE_CONFIG_NAME,
									   MDMDatabaseHelper.TABLE_CONFIG_COLUMNS,
									   null,
									   null,
									   null,
									   null,
									   null);
		cursor.moveToFirst();
		while(!cursor.isAfterLast())
		{
			ConfigEntry entry = cursorToEntry(cursor);
			config.put(entry.getName(), entry);
			cursor.moveToNext();
		}
		//Close the cursor
		cursor.close();
		return config;
	}
	
	/**
	 * Returns the entry associated with the given cursor
	 */
	private ConfigEntry cursorToEntry(Cursor cursor)
	{
		ConfigEntry entry = new ConfigEntry();
		entry.setId(cursor.getLong(0));
		entry.setName(cursor.getString(1));
		entry.setSetting(cursor.getString(2));
		return entry;
	}
	
	/**
	 * Seeks an entry with the name provided and gives it the given setting
	 */
	public void modifyConfigEntry(String name, String setting)
	{
		_database.execSQL("UPDATE " + MDMDatabaseHelper.TABLE_CONFIG_NAME + " " +
						  "SET " + MDMDatabaseHelper.TABLE_CONFIG_COLUMN_SETTING + "='" + setting + "' " +
						  "WHERE " + MDMDatabaseHelper.TABLE_CONFIG_COLUMN_NAME + "='" + name + "'");
	}
	
	/**
	 * Removes all entries from the score table
	 */
	public void clearConfigEntries()
	{
		_database.execSQL("DELETE from " + MDMDatabaseHelper.TABLE_CONFIG_NAME);
	}
	
	/**
	 * Removes all entries from the score table and inserts the default names and scores
	 */
	@Override
	public void clearConfigToDefault()
	{
		clearConfigEntries();
		
		for(int i = 0; i < DEFAULT_NAMES.length; ++i)
		{
			ContentValues values = new ContentValues();
			//Set values from default lists
			values.put(MDMDatabaseHelper.TABLE_CONFIG_COLUMN_NAME, DEFAULT_NAMES[i]);
			values.put(MDMDatabaseHelper.TABLE_CONFIG_COLUMN_SETTING, DEFAULT_SETTINGS[i]);
			_database.insert(MDMDatabaseHelper.TABLE_CONFIG_NAME, null, values);
		}
	}

	//FUNCTION
	//Override obligatory functionality
	@Override
	public SQLiteOpenHelper specifyHelper(Context context) {
		return new MDMDatabaseHelper(context);
	}

}
