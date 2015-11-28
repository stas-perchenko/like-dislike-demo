package com.sperchenko.likedislikedemo.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by Stas on 28.11.2015.
 */
@DatabaseTable(tableName = Person.TABLE_NAME)
public class Person extends BaseDBModel {
    public static final String TABLE_NAME = "Person";

    public static final String USER_NAME_COLUMN = "userName";
    public static final String PASSWORD_COLUMN = "password";
    public static final String DISPLAYED_NAME_COLUMN = "displayedName";
    public static final String PHOTO_ID_COLUMN = "photoId";

    @DatabaseField(columnName = USER_NAME_COLUMN)
    private String userName;

    @DatabaseField(columnName = PASSWORD_COLUMN)
    private String password;

    @DatabaseField(columnName = DISPLAYED_NAME_COLUMN)
    private String displayedName;

    @DatabaseField(columnName = PHOTO_ID_COLUMN)
    private int photoId;


    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDisplayedName() {
        return displayedName;
    }

    public void setDisplayedName(String displayedName) {
        this.displayedName = displayedName;
    }

    public int getPhotoId() {
        return photoId;
    }

    public void setPhotoId(int photoId) {
        this.photoId = photoId;
    }
}
