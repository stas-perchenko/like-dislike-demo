package com.sperchenko.likedislikedemo.activities;

import android.os.Bundle;
import android.util.Log;

import com.sperchenko.likedislikedemo.tasks.ProgressAsyncTaskCompat;

/**
 * Created by Stas on 28.11.2015.
 */
public class SplashActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        new ProgressAsyncTaskCompat<Void, Void, Void>(this, true, true) {
            @Override
            protected Void doInBackground(Void... params) {

                for (int i=0; i<100; i++) {
                    if (isCancelled()) return null;
                    try {
                        Thread.sleep(100);
                    } catch (Exception e){}
                }
                return null;
            }

            @Override
            protected void onCancelled(Void aVoid) {
                super.onCancelled(aVoid);
                finish();
            }
        }.safeExecute();

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Log.d("test", "onBackPressed()");
    }
}
