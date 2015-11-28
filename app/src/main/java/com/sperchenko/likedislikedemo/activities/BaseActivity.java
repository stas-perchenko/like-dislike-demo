package com.sperchenko.likedislikedemo.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.logger.Logger;
import com.j256.ormlite.logger.LoggerFactory;
import com.j256.ormlite.support.ConnectionSource;
import com.sperchenko.likedislikedemo.storage.SQLiteDataHelper;


/**
 * Created by Stas on 28.11.2015.
 */
public abstract class BaseActivity extends AppCompatActivity {
    private volatile SQLiteDataHelper helper;
    private volatile boolean created = false;
    private volatile boolean destroyed = false;
    private static Logger logger = LoggerFactory.getLogger(BaseActivity.class);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(this.helper == null) {
            this.helper = OpenHelperManager.getHelper(this, SQLiteDataHelper.class);
            logger.trace("{}: got new helper {} from OpenHelperManager", this, helper);
            this.created = true;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        OpenHelperManager.releaseHelper();
        logger.trace("{}: helper {} was released, set to null", this, helper);
        this.helper = null;
        this.destroyed = true;
    }

    public SQLiteDataHelper getHelper() {
        if(this.helper == null) {
            if(!this.created) {
                throw new IllegalStateException("A call has not been made to onCreate() yet so the helper is null");
            } else if(this.destroyed) {
                throw new IllegalStateException("A call to onDestroy has already been made and the helper cannot be used after that point");
            } else {
                throw new IllegalStateException("Helper is null for some unknown reason");
            }
        } else {
            return this.helper;
        }
    }

    public ConnectionSource getConnectionSource() {
        return this.getHelper().getConnectionSource();
    }
}
