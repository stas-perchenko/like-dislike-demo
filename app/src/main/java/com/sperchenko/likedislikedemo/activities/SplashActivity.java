package com.sperchenko.likedislikedemo.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.sperchenko.likedislikedemo.R;
import com.sperchenko.likedislikedemo.storage.DBUtils;
import com.sperchenko.likedislikedemo.storage.SQLiteDataHelper;
import com.sperchenko.likedislikedemo.tasks.ProgressAsyncTaskCompat;

/**
 * Created by Stas on 28.11.2015.
 */
public class SplashActivity extends BaseActivity {
    private SharedPreferences sPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        sPref = getPreferences(MODE_PRIVATE);

        getSupportActionBar().hide();

        long nUsers = getHelper().getPersonDAO().countOf();
        if (nUsers == 0) {
            new ProgressAsyncTaskCompat<Void, Void, Void>(this, true, false) {
                @Override
                protected Void doInBackground(Void... params) {
                    SQLiteDataHelper helper = OpenHelperManager.getHelper(SplashActivity.this, SQLiteDataHelper.class);
                    try {
                        DBUtils.initDatabase(helper);
                    } finally {
                        OpenHelperManager.releaseHelper();
                    }
                    return null;
                }

                @Override
                protected void onPostExecute(Void t) {
                    super.onPostExecute(t);
                    if (isStarted) {
                        testLoggedIn();
                    } else {
                        pendingLoginTest = true;
                    }
                }
            }.safeExecute();
        } else {
            testLoggedIn();
        }
    }

    private boolean pendingLoginTest;
    private boolean isStarted;

    @Override
    protected void onStart() {
        super.onStart();
        isStarted = true;
        if (pendingLoginTest) {
            pendingLoginTest = false;
            testLoggedIn();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        isStarted = false;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Log.d("test", "onBackPressed()");
    }

    private static final String PREF_LOGGED_IN_USER = "logged_in_user_id";

    private static final int REQUEST_LOGIN = 101;

    private void testLoggedIn() {
        int loggedInUserId = sPref.getInt(PREF_LOGGED_IN_USER, -1);
        if (loggedInUserId > 0) {
            startMain(loggedInUserId);
            finish();
        } else {
            startActivityForResult(new Intent(this, SelectUserLoginActivity.class), REQUEST_LOGIN);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_LOGIN) {
            if (resultCode == RESULT_OK) {
                int id = data.getIntExtra(SelectUserLoginActivity.RESULT_LOGGED_IN_USER_ID, -1);
                if (id > 0) {
                    startMain(id);
                }
            }
            finish(); // Leave the application
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void startMain(int userId) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra(MainActivity.ARG_LOGGED_IN_USER_ID, userId);
        startActivity(intent);
    }
}
