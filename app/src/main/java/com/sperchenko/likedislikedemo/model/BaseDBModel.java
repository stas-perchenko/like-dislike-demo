package com.sperchenko.likedislikedemo.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by Stas on 28.11.2015.
 */
@DatabaseTable
public class BaseDBModel {
    public static final String ID_COLUMN = "_id";

    @DatabaseField(columnName = ID_COLUMN, id = true)
    private int _id;

    public int getId() {
        return _id;
    }

    public void setId(int id) {
        this._id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BaseDBModel that = (BaseDBModel) o;

        return _id == that._id;

    }

    @Override
    public int hashCode() {
        return _id;
    }
}
