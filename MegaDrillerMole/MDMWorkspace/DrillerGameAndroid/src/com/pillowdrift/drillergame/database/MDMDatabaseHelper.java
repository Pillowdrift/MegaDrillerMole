package com.pillowdrift.drillergame.database;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Database helper class specific to Mega Driller Mole!
 * @author cake_cruncher_7
 *
 */
public class MDMDatabaseHelper extends SQLiteOpenHelper
{ 
	//Database
	public static final String DATABASE_NAME = "mdmDatabase.db";
	public static final int DATABASE_VERSION = 4;
	//Tables
	//Scores
	public static final String TABLE_SCORES_NAME = "highscores";
	public static final String TABLE_SCORES_COLUMN_ID = "_id";
	public static final String TABLE_SCORES_COLUMN_NAME = "name";
	public static final String TABLE_SCORES_COLUMN_SCORE = "score";
	public static final String[] TABLE_SCORES_COLUMNS = {
															TABLE_SCORES_COLUMN_ID,
															TABLE_SCORES_COLUMN_NAME,
															TABLE_SCORES_COLUMN_SCORE
	};
	public static final String SCORES_CREATE = "CREATE TABLE IF NOT EXISTS " + TABLE_SCORES_NAME + " ( " +
											   TABLE_SCORES_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
											   TABLE_SCORES_COLUMN_NAME + " STRING, " +
											   TABLE_SCORES_COLUMN_SCORE + " INTEGER" +
											   ");";
	//Config
	public static final String TABLE_CONFIG_NAME = "config";
	public static final String TABLE_CONFIG_COLUMN_ID = "_id";
	public static final String TABLE_CONFIG_COLUMN_NAME = "name";
	public static final String TABLE_CONFIG_COLUMN_SETTING = "setting";
	public static final String[] TABLE_CONFIG_COLUMNS = {
															TABLE_CONFIG_COLUMN_ID,
															TABLE_CONFIG_COLUMN_NAME,
															TABLE_CONFIG_COLUMN_SETTING
	};
	public static final String CONFIG_CREATE = "CREATE TABLE IF NOT EXISTS " + TABLE_CONFIG_NAME + " ( " +
											    TABLE_CONFIG_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
											    TABLE_CONFIG_COLUMN_NAME + " STRING, " +
											    TABLE_CONFIG_COLUMN_SETTING + " STRING" +
											    ");";
	//Items
	public static final String TABLE_ITEM_NAME = "items";
	public static final String TABLE_ITEM_COLUMN_ID = "_id";
	public static final String TABLE_ITEM_COLUMN_NAME = "name";
	public static final String TABLE_ITEM_COLUMN_PURCHASED = "purchased";
	public static final String TABLE_ITEM_COLUMN_USING = "isUsing";
	public static final String TABLE_ITEM_COLUMN_TYPE = "type";
	public static final String[] TABLE_ITEM_COLUMNS = {
															TABLE_ITEM_COLUMN_ID,
															TABLE_ITEM_COLUMN_NAME,
															TABLE_ITEM_COLUMN_PURCHASED,
															TABLE_ITEM_COLUMN_USING,
															TABLE_ITEM_COLUMN_TYPE
	};	
	public static final String ITEM_CREATE = "CREATE TABLE IF NOT EXISTS " + TABLE_ITEM_NAME + " ( " +
		    								  TABLE_ITEM_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
		    								  TABLE_ITEM_COLUMN_NAME + " STRING, " +
		    								  TABLE_ITEM_COLUMN_PURCHASED + " STRING, " +
		    								  TABLE_ITEM_COLUMN_USING + " STRING, " +
		    								  TABLE_ITEM_COLUMN_TYPE + " STRING" +
		    								  ");";
	
	public static final String TABLE_COIN_NAME = "totalcoins";
	public static final String TABLE_COIN_COLUMN_ID = "_id";
	public static final String TABLE_COIN_COLUMN_NAME = "name";
	public static final String TABLE_COIN_COLUMN_COINS = "coins";
	public static final String[] TABLE_COIN_COLUMNS = {
													  		TABLE_COIN_COLUMN_ID,
													  		TABLE_COIN_COLUMN_NAME,
													  		TABLE_COIN_COLUMN_COINS
	};
	public static final String COIN_CREATE = "CREATE TABLE IF NOT EXISTS " + TABLE_COIN_NAME + " ( " +
											  TABLE_COIN_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
											  TABLE_COIN_COLUMN_NAME + " STRING, " +
											  TABLE_COIN_COLUMN_COINS + " INTEGER" +
											  ");";
	
	public MDMDatabaseHelper(Context context)
	{
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(SCORES_CREATE);
		db.execSQL(CONFIG_CREATE);
		db.execSQL(ITEM_CREATE);
		db.execSQL(COIN_CREATE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		
		//Create if not exists
		db.execSQL(SCORES_CREATE);
		db.execSQL(CONFIG_CREATE);
		db.execSQL(ITEM_CREATE);
		db.execSQL(COIN_CREATE);
		
		//Get all columns
		/*List<String> columns = GetColumns(db, TABLE_SCORES_NAME);
		List<String> columns2 = GetColumns(db, TABLE_CONFIG_NAME);
		
		db.execSQL("(ALTER table " + TABLE_SCORES_NAME + " RENAME TO 'temp_" + TABLE_SCORES_NAME + ")");
		db.execSQL("(ALTER table " + TABLE_CONFIG_NAME + " RENAME TO 'temp_" + TABLE_CONFIG_NAME + ")");
		
		db.execSQL(SCORES_CREATE);
		db.execSQL(CONFIG_CREATE);
		db.execSQL(ITEM_CREATE);
		db.execSQL(COIN_CREATE);
		
		columns.retainAll(GetColumns(db, TABLE_SCORES_NAME));
		columns2.retainAll(GetColumns(db, TABLE_CONFIG_NAME));
		
		String cols = join(columns, ","); 
		db.execSQL(String.format( "INSERT INTO %s (%s) SELECT %s from temp_%s", TABLE_SCORES_NAME, cols, cols, TABLE_SCORES_NAME));
		
		String cols2 = join(columns2, ","); 
		db.execSQL(String.format( "INSERT INTO %s (%s) SELECT %s from temp_%s", TABLE_CONFIG_NAME, cols2, cols2, TABLE_CONFIG_NAME));
		
		db.execSQL("(DROP table 'temp_" + TABLE_SCORES_NAME + ")");
		db.execSQL("(DROP table 'temp_" + TABLE_CONFIG_NAME + ")");*/
	}
	
	public static List<String> GetColumns(SQLiteDatabase db, String tableName) {
	    List<String> ar = null;
	    Cursor c = null;
	    try {
	        c = db.rawQuery("select * from " + tableName + " limit 1", null);
	        if (c != null) {
	            ar = new ArrayList<String>(Arrays.asList(c.getColumnNames()));
	        }
	    } catch (Exception e) {
	        Log.v(tableName, e.getMessage(), e);
	        e.printStackTrace();
	    } finally {
	        if (c != null)
	            c.close();
	    }
	    return ar;
	}

	public static String join(List<String> list, String delim) {
	    StringBuilder buf = new StringBuilder();
	    int num = list.size();
	    for (int i = 0; i < num; i++) {
	        if (i != 0)
	            buf.append(delim);
	        buf.append((String) list.get(i));
	    }
	    return buf.toString();
	}

}
