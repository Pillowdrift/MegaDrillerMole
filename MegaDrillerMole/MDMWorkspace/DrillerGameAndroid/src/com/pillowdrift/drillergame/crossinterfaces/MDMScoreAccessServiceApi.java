package com.pillowdrift.drillergame.crossinterfaces;

import java.util.List;

import com.pillowdrift.drillergame.database.ScoreEntry;

/**
 * Interface listing the functionality required for score setting and retrieval
 * @author cake_cruncher_7
 *
 */
public interface MDMScoreAccessServiceApi
{
	public List<ScoreEntry> getScores();
	public List<ScoreEntry> getSortedScores();
	public ScoreEntry addScoreEntry(String name, long score);
	public ScoreEntry proposeNewEntry(String name, long score);
	public void clearScoreEntries();
	public void clearScoresToDefault();
}
