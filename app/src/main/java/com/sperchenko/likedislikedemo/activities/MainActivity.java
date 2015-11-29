package com.sperchenko.likedislikedemo.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

/**
 * Created by Stas on 29.11.2015.
 */
public class MainActivity extends BaseActivity {
    public static final String ARG_LOGGED_IN_USER_ID = "iser_id";

    private int mLoggedInUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            mLoggedInUserId = getIntent().getExtras().getInt(ARG_LOGGED_IN_USER_ID);
        } catch(NullPointerException e) {
            mLoggedInUserId = -1;
        }

        if (mLoggedInUserId < 1) {
            Toast.makeText(this, "A logged in user ID must be provided for the MainActivity", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
    }


}
