package com.pillowdrift.drillergame.database;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteOpenHelper;

import com.pillowdrift.drillergame.crossinterfaces.MDMCoinsServiceApi;

public class CoinsDAO extends DatabaseAccessObject implements MDMCoinsServiceApi {

	public static final long DEFAULT_COINS = 0;
	public static final String DEFAULT_NAME = "MDM";
	
	public CoinsDAO(Context context) {
		super(context);
		
		//Temporarily open the database
		open();
		
		//Check if the coin table is empty
		Cursor cursor = _database.query(MDMDatabaseHelper.TABLE_COIN_NAME,
									   MDMDatabaseHelper.TABLE_COIN_COLUMNS,
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
			clearCoinToDefault();
		}
		cursor.close();
		
		//Close the database
		close();
	}

	@Override
	public long getCoins() {
		CoinsEntry coins = new CoinsEntry(); 
		Cursor cursor = _database.query(MDMDatabaseHelper.TABLE_COIN_NAME,
									   MDMDatabaseHelper.TABLE_COIN_COLUMNS,
									   null,
									   null,
									   null,
									   null,
									   null);
		cursor.moveToFirst();
		while(!cursor.isAfterLast())
		{
			coins.setId(cursor.getLong(0));
			coins.setName(cursor.getString(1));
			coins.setCoins(cursor.getLong(2));
			cursor.moveToNext();
		}
		//Close the cursor
		cursor.close();
		
		return coins.getCoins();
	}

	@Override
	public void addToCoins(long points) {
		long coins = getCoins();
		coins += points;
		_database.execSQL("UPDATE " + MDMDatabaseHelper.TABLE_COIN_NAME + " " +
		  		  "SET " + MDMDatabaseHelper.TABLE_COIN_COLUMN_COINS + "='" + coins + "' " +
		  		  "WHERE " + MDMDatabaseHelper.TABLE_COIN_COLUMN_NAME + "='" + DEFAULT_NAME + "'");
	}

	@Override
	public void removeCoins(long points) {
		long coins = getCoins();
		coins -= points;
		_database.execSQL("UPDATE " + MDMDatabaseHelper.TABLE_COIN_NAME + " " +
		  		  "SET " + MDMDatabaseHelper.TABLE_COIN_COLUMN_COINS + "='" + coins + "' " +
		  		  "WHERE " + MDMDatabaseHelper.TABLE_COIN_COLUMN_NAME + "='" + DEFAULT_NAME + "'");
	}

	@Override
	public void initCoins() {
		long coins = 0;
		_database.execSQL("UPDATE " + MDMDatabaseHelper.TABLE_COIN_NAME + " " +
		  		  "SET " + MDMDatabaseHelper.TABLE_COIN_COLUMN_COINS + "='" + coins + "' " +
		  		  "WHERE " + MDMDatabaseHelper.TABLE_COIN_COLUMN_NAME + "='" + DEFAULT_NAME + "'");
	}

	@Override
	public SQLiteOpenHelper specifyHelper(Context context) {
		return new MDMDatabaseHelper(context);
	}

	@Override
	public void clearCoinEntry() {
		_database.execSQL("DELETE from " + MDMDatabaseHelper.TABLE_COIN_NAME);
	}

	@Override
	public void clearCoinToDefault() {
		clearCoinEntry();
		
		ContentValues values = new ContentValues();
		//Set values from default lists
		values.put(MDMDatabaseHelper.TABLE_COIN_COLUMN_NAME, DEFAULT_NAME);
		values.put(MDMDatabaseHelper.TABLE_COIN_COLUMN_COINS, DEFAULT_COINS);
		_database.insert(MDMDatabaseHelper.TABLE_COIN_NAME, null, values);
	}

}
