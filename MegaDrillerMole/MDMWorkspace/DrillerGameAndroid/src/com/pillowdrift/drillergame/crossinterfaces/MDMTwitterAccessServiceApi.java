package com.pillowdrift.drillergame.crossinterfaces;

public interface MDMTwitterAccessServiceApi {
	public void postScoreToTwitter(long score);
	public void logInToTwitter();
	public void logOutOfTwitter();
	public void extendTwitterToken();
	public boolean isLoggedInToTwitter();
}
