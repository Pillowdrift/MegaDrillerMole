package com.pillowdrift.drillergame.facebook;

import com.pillowdrift.drillergame.facebook.Facebook.DialogListener;
import com.pillowdrift.drillergame.facebook.SessionEvents.AuthListener;
import com.pillowdrift.drillergame.facebook.SessionEvents.LogoutListener;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;

public class LoginData {
    private Facebook mFb;
    private Handler mHandler;
    private SessionListener mSessionListener = new SessionListener();
    private String[] mPermissions;
    private Activity mActivity;
    private int mActivityCode;
    
    private boolean _loggedIn = false;

    public LoginData(final Activity activity, final int activityCode, final Facebook fb,
            final String[] permissions) {
        mActivity = activity;
        mActivityCode = activityCode;
        mFb = fb;
        mPermissions = permissions;
        mHandler = new Handler();
        
        _loggedIn = fb.isSessionValid();

        SessionEvents.addAuthListener(mSessionListener);
        SessionEvents.addLogoutListener(mSessionListener);
    }

    public void logIn() {
       if (!mFb.isSessionValid()) {
    	   mFb.authorize(mActivity, mPermissions, mActivityCode, new LoginDialogListener());
       }
    }
    
    public void logOut() {
    	if (mFb.isSessionValid()) {
            SessionEvents.onLogoutBegin();
            AsyncFacebookRunner asyncRunner = new AsyncFacebookRunner(mFb);
            asyncRunner.logout(mActivity.getBaseContext(), new LogoutRequestListener());
        }
    }

    private final class LoginDialogListener implements DialogListener {
        @Override
        public void onComplete(Bundle values) {
            SessionEvents.onLoginSuccess();
        }

        @Override
        public void onFacebookError(FacebookError error) {
            SessionEvents.onLoginError(error.getMessage());
        }

        @Override
        public void onError(DialogError error) {
            SessionEvents.onLoginError(error.getMessage());
        }

        @Override
        public void onCancel() {
            SessionEvents.onLoginError("Action Canceled");
        }
    }

    private class LogoutRequestListener extends BaseRequestListener {
        @Override
        public void onComplete(String response, final Object state) {
            /*
             * callback should be run in the original thread, not the background
             * thread
             */
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    SessionEvents.onLogoutFinish();
                }
            });
        }
    }

    private class SessionListener implements AuthListener, LogoutListener {

        @Override
        public void onAuthSucceed() {
            SessionStore.save(mFb, mActivity.getBaseContext());
            _loggedIn = true;
        }

        @Override
        public void onAuthFail(String error) {
        }

        @Override
        public void onLogoutBegin() {
        }

        @Override
        public void onLogoutFinish() {
            SessionStore.clear(mActivity.getBaseContext());
            _loggedIn = false;
        }
    }
}
