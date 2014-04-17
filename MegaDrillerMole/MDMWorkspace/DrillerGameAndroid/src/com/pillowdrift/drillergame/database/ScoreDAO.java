package com.pillowdrift.drillergame.database;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteOpenHelper;

import com.pillowdrift.drillergame.crossinterfaces.MDMScoreAccessServiceApi;

/**
 * Score database access object inheriting the base database access object.
 * @author cake_cruncher_7
 *
 */
public class ScoreDAO extends DatabaseAccessObject implements MDMScoreAccessServiceApi
{	
	//CONSTANTS
	public static final int NUMBER_OF_SCORES = 10;
	public static final String[] DEFAULT_NAMES =
	{
		"GGR",
		"OER",
		"WNE",
		"ATN",
		"TOL",
		"CPA",
		"HPG",
		"TAA",
		"EGN",
		"NUN"
	};
	public static final long[] DEFAULT_SCORES =
	{
		128000,
		64000,
		32000,
		16000,
		8000,
		4000,
		2000,
		1000,
		500,
		250
	};
	
	/**
	 * Basic constructor for the database access object
	 * @param context
	 */
	public ScoreDAO(Context context)
	{
		super(context);
		
		//Temporarily open the database
		open();
		
		//Check if the scores table is empty
		Cursor cursor = _database.query(MDMDatabaseHelper.TABLE_SCORES_NAME,
									   MDMDatabaseHelper.TABLE_SCORES_COLUMNS,
									   null,
									   null,
									   null,
									   null,
									   null);
		//Get a count of all rows in the table
		int i = cursor.getCount();
		cursor.close();
		//Set default values if there are no rows currently
		if(i == 0)
		{
			clearScoresToDefault();
		}
		
		//Close the database
		close();
	}
	
	/**
	 * Returns a collection of all entries in the score table
	 * @return
	 */
	public List<ScoreEntry> getScores()
	{
		List<ScoreEntry> scores = new ArrayList<ScoreEntry>();
		Cursor cursor = _database.query(MDMDatabaseHelper.TABLE_SCORES_NAME,
									   MDMDatabaseHelper.TABLE_SCORES_COLUMNS,
									   null,
									   null,
									   null,
									   null,
									   null);
		cursor.moveToFirst();
		while(!cursor.isAfterLast())
		{
			ScoreEntry entry = cursorToEntry(cursor);
			scores.add(entry);
			cursor.moveToNext();
		}
		//Close the cursor
		cursor.close();
		return scores;
	}
	
	/**
	 * Returns a collection of all entries in the score table, sorted descending by score
	 * @return
	 */
	public List<ScoreEntry> getSortedScores()
	{
		List<ScoreEntry> scores = new ArrayList<ScoreEntry>();
		Cursor cursor = _database.query(MDMDatabaseHelper.TABLE_SCORES_NAME,
									   MDMDatabaseHelper.TABLE_SCORES_COLUMNS,
									   null,
									   null,
									   null,
									   null,
									   MDMDatabaseHelper.TABLE_SCORES_COLUMN_SCORE + " DESC");
		cursor.moveToFirst();
		while(!cursor.isAfterLast())
		{
			ScoreEntry entry = cursorToEntry(cursor);
			scores.add(entry);
			cursor.moveToNext();
		}
		//Close the cursor
		cursor.close();
		return scores;
	}
	
	/**
	 * Returns the entry associated with the given cursor
	 */
	private ScoreEntry cursorToEntry(Cursor cursor)
	{
		ScoreEntry entry = new ScoreEntry();
		entry.setId(cursor.getLong(0));
		entry.setName(cursor.getString(1));
		entry.setScore(cursor.getLong(2));
		return entry;
	}
	
	/**
	 * Adds an entry to the score table
	 */
	public ScoreEntry addScoreEntry(String name, long score)
	{
		ContentValues values = new ContentValues();
		values.put(MDMDatabaseHelper.TABLE_SCORES_COLUMN_NAME, name);
		values.put(MDMDatabaseHelper.TABLE_SCORES_COLUMN_SCORE, score);
		long id = _database.insert(MDMDatabaseHelper.TABLE_SCORES_NAME, null, values);
		Cursor cursor = _database.query(MDMDatabaseHelper.TABLE_SCORES_NAME,
									   MDMDatabaseHelper.TABLE_SCORES_COLUMNS,
									   MDMDatabaseHelper.TABLE_SCORES_COLUMN_ID + " = " + id,
									   null,
									   null,
									   null,
									   null,
									   null);
		cursor.moveToFirst();
		return cursorToEntry(cursor);
	}
	
	/**
	 * Find out whether there is an entry with a lower score than the suggested entry.
	 * Find the lowest scoring entry and replace it if so.
	 * Return the added entry. Return null if none is added
	 */
	public ScoreEntry proposeNewEntry(String name, long score)
	{
		//Get a cursor to the table's data sorted by score, ascending
		Cursor cursor = _database.query(MDMDatabaseHelper.TABLE_SCORES_NAME,
									   MDMDatabaseHelper.TABLE_SCORES_COLUMNS,
									   null,
									   null,
									   null,
									   null,
									   MDMDatabaseHelper.TABLE_SCORES_COLUMN_SCORE + " ASC");
		cursor.moveToFirst();
		//The first cursor should be the lowest score
		long lowestScore = cursor.getLong(2);
		//If the proposed score beats the lowest score
		if(score > lowestScore)
		{
			//Delete the lowest scoring entry
			long lowestId = cursor.getLong(0);
			_database.execSQL("DELETE from " + MDMDatabaseHelper.TABLE_SCORES_NAME + " WHERE " + MDMDatabaseHelper.TABLE_SCORES_COLUMN_ID + " =  " + lowestId);
			//Insert a new entry for the proposed values
			return addScoreEntry(name, score);
		}
		else
		{
			//Otherwise quit and return null
			return null;
		}
	}
	
	/**
	 * Removes all entries from the score table
	 */
	public void clearScoreEntries()
	{
		_database.execSQL("DELETE from " + MDMDatabaseHelper.TABLE_SCORES_NAME);
	}
	
	/**
	 * Removes all entries from the score table and inserts the default names and scores
	 */
	@Override
	public void clearScoresToDefault()
	{
		clearScoreEntries();
		
		for(int i = 0; i < NUMBER_OF_SCORES; ++i)
		{
			ContentValues values = new ContentValues();
			//Set values from default lists
			values.put(MDMDatabaseHelper.TABLE_SCORES_COLUMN_NAME, DEFAULT_NAMES[i]);
			values.put(MDMDatabaseHelper.TABLE_SCORES_COLUMN_SCORE, DEFAULT_SCORES[i]);
			_database.insert(MDMDatabaseHelper.TABLE_SCORES_NAME, null, values);
		}
	}
	
	/**
	 * Inserts the given number of blank entries into the score table
	 */
	public void insertBlankEntries(int num)
	{
		for(int i = 0; i < num; ++i)
		{
			ContentValues values = new ContentValues();
			//Set values from default lists
			values.put(MDMDatabaseHelper.TABLE_SCORES_COLUMN_NAME, " ");
			values.put(MDMDatabaseHelper.TABLE_SCORES_COLUMN_SCORE, 0);
			_database.insert(MDMDatabaseHelper.TABLE_SCORES_NAME, null, values);
		}
	}
	
	//Override obligatory functionality
	@Override
	public SQLiteOpenHelper specifyHelper(Context context) {
		return new MDMDatabaseHelper(context);
	}
}
