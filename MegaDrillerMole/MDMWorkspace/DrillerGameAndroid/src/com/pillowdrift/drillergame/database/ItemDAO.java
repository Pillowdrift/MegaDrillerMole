package com.pillowdrift.drillergame.database;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteOpenHelper;

import com.pillowdrift.drillergame.crossinterfaces.MDMItemsServiceApi;


public class ItemDAO extends DatabaseAccessObject implements MDMItemsServiceApi {

	//CONSTANTS
	public static final class OnOff
	{
		public static final String ON = "boolon";
		public static final String OFF = "booloff";
		public static final String ERROR = "boolerror";
	}
	
	public static final class ItemNames
	{
		public static final String FIRSTDRILL = "First Drill";
		public static final String FEATHERDRILL = "Feather Drill";
		public static final String FLAMEDRILL = "Flame Drill";
		public static final String ELECDRILL = "Magnetic Drill";
		public static final String BUBBLE = "Bubble Shield";
	}
	
	public static final class ItemTypes
	{
		public static final String DRILL = "Drill";
		public static final String POWERUP = "PowerUp";
	}
	
	public static final String[] DEFAULT_NAMES =
	{
		ItemNames.FIRSTDRILL,
		ItemNames.FEATHERDRILL,
		ItemNames.FLAMEDRILL,
		ItemNames.ELECDRILL,
		ItemNames.BUBBLE
	};
	
	public static final String[] DEFAULT_PURCHASED =
	{
		OnOff.ON,
		OnOff.OFF,
		OnOff.OFF,
		OnOff.OFF,
		OnOff.OFF
	};
	
	public static final String[] DEFAULT_USING =
	{
		OnOff.ON,
		OnOff.OFF,
		OnOff.OFF,
		OnOff.OFF,
		OnOff.OFF
	};
	
	public static final String[] DEFAULT_TYPE =
	{
		ItemTypes.DRILL,
		ItemTypes.DRILL,
		ItemTypes.DRILL,
		ItemTypes.DRILL,
		ItemTypes.POWERUP
	};	
	
	public ItemDAO(Context context) {
		super(context);
		
		open();
		
		//Check if the scores table is empty
		Cursor cursor = _database.query(MDMDatabaseHelper.TABLE_ITEM_NAME,
									   MDMDatabaseHelper.TABLE_ITEM_COLUMNS,
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
			clearItemsToDefault();
		}
		cursor.close();
		//Close the database
		close();
	}

	@Override
	public SQLiteOpenHelper specifyHelper(Context context) {
		return new MDMDatabaseHelper(context);
	}

	@Override
	public List<ItemEntry> getItems() {
		List<ItemEntry> item = new ArrayList<ItemEntry>();
		Cursor cursor = _database.query(MDMDatabaseHelper.TABLE_ITEM_NAME,
									   MDMDatabaseHelper.TABLE_ITEM_COLUMNS,
									   null,
									   null,
									   null,
									   null,
									   null);
		cursor.moveToFirst();
		while(!cursor.isAfterLast())
		{
			ItemEntry entry = cursorToEntry(cursor);
			item.add(entry);
			cursor.moveToNext();
		}
		//Close the cursor
		cursor.close();
		return item;
	}

	@Override
	public Map<String, ItemEntry> getItemMap() {
		Map<String, ItemEntry> item = new HashMap<String, ItemEntry>();
		Cursor cursor = _database.query(MDMDatabaseHelper.TABLE_ITEM_NAME,
									   MDMDatabaseHelper.TABLE_ITEM_COLUMNS,
									   null,
									   null,
									   null,
									   null,
									   null);
		cursor.moveToFirst();
		while(!cursor.isAfterLast())
		{
			ItemEntry entry = cursorToEntry(cursor);
			item.put(entry.getName(), entry);
			cursor.moveToNext();
		}
		//Close the cursor
		cursor.close();
		return item;
	}
	
	//gets an item entry from the database
	private ItemEntry cursorToEntry(Cursor cursor)
	{
		ItemEntry entry = new ItemEntry();
		entry.setId(cursor.getLong(0));
		entry.setName(cursor.getString(1));
		entry.setPurchased(cursor.getString(2));
		entry.setUsing(cursor.getString(3));
		entry.setType(cursor.getString(4));
		return entry;
	}

	@Override
	public void clearItemEntries() {
		_database.execSQL("DELETE from " + MDMDatabaseHelper.TABLE_ITEM_NAME);
	}

	@Override
	public void clearItemsToDefault() {
		clearItemEntries();
		
		for(int i = 0; i < DEFAULT_NAMES.length; ++i)
		{
			ContentValues values = new ContentValues();
			//Set values from default lists
			values.put(MDMDatabaseHelper.TABLE_ITEM_COLUMN_NAME, DEFAULT_NAMES[i]);
			values.put(MDMDatabaseHelper.TABLE_ITEM_COLUMN_PURCHASED, DEFAULT_PURCHASED[i]);
			values.put(MDMDatabaseHelper.TABLE_ITEM_COLUMN_USING, DEFAULT_USING[i]);
			values.put(MDMDatabaseHelper.TABLE_ITEM_COLUMN_TYPE, DEFAULT_TYPE[i]);
			_database.insert(MDMDatabaseHelper.TABLE_ITEM_NAME, null, values);
		}
	}

	@Override
	public void modifyItemEntry(String name, String purchased, String using) {
		_database.execSQL("UPDATE " + MDMDatabaseHelper.TABLE_ITEM_NAME + " " +
				  		  "SET " + MDMDatabaseHelper.TABLE_ITEM_COLUMN_PURCHASED + "='" + purchased + "' " +
				  		  "WHERE " + MDMDatabaseHelper.TABLE_ITEM_COLUMN_NAME + "='" + name + "'");
		
		_database.execSQL("UPDATE " + MDMDatabaseHelper.TABLE_ITEM_NAME + " " +
		  		  		  "SET " + MDMDatabaseHelper.TABLE_ITEM_COLUMN_USING + "='" + using + "' " +
		  		  		  "WHERE " + MDMDatabaseHelper.TABLE_ITEM_COLUMN_NAME + "='" + name + "'");
	}

	@Override
	public String getCurrentDrillInUse() {
		List<ItemEntry> _items = getItems();
		
		for (ItemEntry ent : _items)
		{
			if (ent.getUsing().equals(OnOff.ON) && ent.getType().equals(ItemTypes.DRILL))
			return ent.getName();
		}
		//couldn't find it, so return null
		return null;
	}

	@Override
	public void setCurrentDrillInUse(String drill) {
		// get the current drill in use
		String currentDrill = getCurrentDrillInUse();
		
		// set it to not in use
		modifyItemEntry(currentDrill, OnOff.ON, OnOff.OFF);
		
		// set the new drill to be used
		modifyItemEntry(drill, OnOff.ON, OnOff.ON);
	}

	@Override
	public boolean hasBubble() {
		List<ItemEntry> _items = getItems();
		
		for (ItemEntry ent : _items)
		{
			if (ent.getName().equals(ItemNames.BUBBLE))
			return ent.getPurchased().equals(OnOff.ON);
		}
		
		//couldn't find it, so return false
		return false;
	}

	@Override
	public void setBubble(boolean hasBubble) {
		// set the new drill to be used
		if(hasBubble)
			modifyItemEntry(ItemNames.BUBBLE, OnOff.ON, OnOff.ON);
		else
			modifyItemEntry(ItemNames.BUBBLE, OnOff.OFF, OnOff.OFF);
		
	}

}
