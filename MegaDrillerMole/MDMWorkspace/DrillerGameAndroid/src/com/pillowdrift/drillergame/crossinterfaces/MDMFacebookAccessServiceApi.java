package com.pillowdrift.drillergame.crossinterfaces;

public interface MDMFacebookAccessServiceApi {
	public void postScoreToFacebook(long score);
	public void logInToFacebook();
	public void logOutOfFacebook();
	public void extendFacebookToken();
	public boolean isLoggedInToFacebook();
}
