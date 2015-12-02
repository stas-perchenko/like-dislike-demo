package com.sperchenko.likedislikedemo.storage;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.sperchenko.likedislikedemo.model.CrossRating;
import com.sperchenko.likedislikedemo.model.Person;

/**
 * Created by Stas on 29.11.2015.
 */
public class DBUtils {

    public static void initDatabase(SQLiteDataHelper helper) {
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {

        }


        createPerson(helper, 2457, "testUser1", "pass1", "mr. John Smith", 100);
        createPerson(helper, 2460, "testUser2", "1234", "Silvia Blue", 101);
        createPerson(helper, 2471, "qwerty@mail.com", "0000", "Amanda Hollower", 102);

        helper.getCrossRatingDAO().createIfNotExists(new CrossRating(2457, 2460, 5));

    }

    private static void createPerson(@NonNull SQLiteDataHelper helper, int id, @NonNull String userName, @Nullable String password, @NonNull String displayedName, int photoId) {
        Person p = new Person();
        p.setId(id);
        p.setUserName(userName);
        p.setPassword(password);
        p.setDisplayedName(displayedName);
        p.setPhotoId(photoId);
        helper.getPersonDAO().createIfNotExists(p);
    }

}
