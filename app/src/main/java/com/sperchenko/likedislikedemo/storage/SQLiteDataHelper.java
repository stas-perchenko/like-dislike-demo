package com.sperchenko.likedislikedemo.storage;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import com.sperchenko.likedislikedemo.model.CrossRating;
import com.sperchenko.likedislikedemo.model.Person;

import java.sql.SQLException;

/**
 * Created by Stas on 28.11.2015.
 */
public class SQLiteDataHelper extends OrmLiteSqliteOpenHelper {
    public final static int DB_VERSION = 1;
    public final static String DB_NAME = "like_dislike_demo.db";

    private PreparedQueries preparedQueries;

    private RuntimeExceptionDao<Person, Integer> personDAO = null;
    private RuntimeExceptionDao<CrossRating, String> crossRatingDAO = null;


    public SQLiteDataHelper(Context context, String databaseName, SQLiteDatabase.CursorFactory factory, int databaseVersion) {
        super(context, databaseName, factory, databaseVersion);
        //Init prepared queries
        this.preparedQueries = new PreparedQueries();
    }

    public SQLiteDataHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        //Init prepared queries
        this.preparedQueries = new PreparedQueries();
    }

    @Override
    public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {
        try {
            TableUtils.createTableIfNotExists(connectionSource, Person.class);
            TableUtils.createTableIfNotExists(connectionSource, CrossRating.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion, int newVersion) {

    }

    public RuntimeExceptionDao<Person, Integer> getPersonDAO() {
        if (personDAO == null)
            personDAO = getRuntimeExceptionDao(Person.class);
        return personDAO;
    }

    public RuntimeExceptionDao<CrossRating, String> getCrossRatingDAO() {
        if (crossRatingDAO == null)
            crossRatingDAO = getRuntimeExceptionDao(CrossRating.class);
        return crossRatingDAO;
    }


    public PreparedQueries getPreparedQueries() {
        return preparedQueries;
    }


    public class PreparedQueries {
        //TODO
    }
}
