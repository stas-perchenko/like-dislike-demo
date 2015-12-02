package com.sperchenko.likedislikedemo.storage;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import com.sperchenko.likedislikedemo.model.CrossRating;
import com.sperchenko.likedislikedemo.model.Person;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

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

        private String TABLE_PEOPLE_RATINGS = String.format("%1$s as P LEFT JOIN %2%s as CR on P.%3$s = CR.%4$s", Person.TABLE_NAME, CrossRating.TABLE_NAME, Person.ID_COLUMN, CrossRating.THIS_USER_COLUMN);

        public List<Person> getPersonsByNameIfHaveRatings(String userName) {
            //TODO Implement this
            // Ratings LEFT JOIN Person
            return new ArrayList<>();
        }

        public List<Person> getAllPersonsWithRatings() {
            //TODO Implement this
            // Ratings LEFT JOIN Person
            return new ArrayList<>();
        }
    }
}
