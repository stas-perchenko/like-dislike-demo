package com.sperchenko.likedislikedemo.tasks;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Build;

import com.sperchenko.likedislikedemo.R;

/**
 * Created by Stas on 28.11.2015.
 */
public abstract class ProgressAsyncTaskCompat<T, G, F> extends AsyncTask<T, G, F> {
    private Context mContext;
    private boolean showProgress;
    private boolean isProgressCancellable;

    private ProgressDialog progress;

    ProgressAsyncTaskCompat(Context context, boolean showProgress, boolean isProgressCancellable) {
        mContext = context;
        this.showProgress = showProgress;
        this.isProgressCancellable = isProgressCancellable;
    }

    @Override
    protected void onPreExecute() {
        if (showProgress) {
            progress = new ProgressDialog(mContext, R.style.ProgressDialog) {
                @Override
                public void onDetachedFromWindow() {
                    // To prevent "not attached to window manager" error on dismissing.
                    super.onDetachedFromWindow();
                    progress = null;
                }
            };
            progress.setCanceledOnTouchOutside(false);
            progress.setCancelable(isProgressCancellable);
            progress.show();
            progress.setContentView(R.layout.custom_progress_indeterminate);
            progress.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    if (!ProgressAsyncTaskCompat.this.isCancelled()) {
                        ProgressAsyncTaskCompat.this.cancel(true);
                    }
                    progress = null;
                }
            });
        }
    }

    @Override
    protected void onPostExecute(F t) {
        if (progress != null) {
            progress.dismiss();
            progress = null;
        }
    }

    @Override
    protected void onCancelled(F f) {
        if (progress != null) {
            progress.dismiss();
            progress = null;
        }
    }

    @SuppressLint("NewApi")
    public void safeExecute(T... params) {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.HONEYCOMB) {
            super.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, params);
        } else {
            super.execute(params);
        }
    }
}
