package com.sperchenko.likedislikedemo.images;

import android.widget.ImageView;

import java.io.File;

/**
 * Created by stanislav.perchenko on 12/2/2015.
 */
public class IconLoader {

    private static IconLoader instance;

    public static IconLoader getInstance() {
        if (instance == null) {
            synchronized (IconLoader.class) {
                if (instance == null) {
                    instance = new IconLoader();
                }
            }
        }
        return instance;
    }

    public static void loadIcon(File iconFile, ImageView v) {
        getInstance().loadIcon(v, iconFile);
    }


    private IconLoader(){}


    public void loadIcon(ImageView v, File iconFile) {
        //TODO
    }
}
