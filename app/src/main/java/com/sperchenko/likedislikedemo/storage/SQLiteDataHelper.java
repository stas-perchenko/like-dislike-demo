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
            String where = String.format("(CR.%1$s <> NULL) AND (P.%2$s = '53$s')", CrossRating.SCORE_COLUMN, Person.USER_NAME_COLUMN, userName);
            return getPeopleFromJoinedTable(where);
        }

        public List<Person> getAllPersonsWithRatings() {
            String where = String.format("CR.%s <> NULL", CrossRating.SCORE_COLUMN);
            return getPeopleFromJoinedTable(where);
        }

        private List<Person> getPeopleFromJoinedTable(String where) {
            String[] columns = new String[]{Person.ID_COLUMN, Person.USER_NAME_COLUMN, Person.PASSWORD_COLUMN, Person.DISPLAYED_NAME_COLUMN, Person.PHOTO_ID_COLUMN};

            Cursor c = getReadableDatabase().query(true, TABLE_PEOPLE_RATINGS, columns, where, null, null, null, "P."+Person.DISPLAYED_NAME_COLUMN, null);
            try {
                List<Person> people = new ArrayList<>();
                if (c.moveToFirst()) {
                    do {
                        Person p = new Person(c.getInt(c.getColumnIndex(Person.ID_COLUMN)),
                                c.getString(c.getColumnIndex(Person.USER_NAME_COLUMN)),
                                c.getString(c.getColumnIndex(Person.PASSWORD_COLUMN)),
                                c.getString(c.getColumnIndex(Person.DISPLAYED_NAME_COLUMN)),
                                c.getInt(c.getColumnIndex(Person.PHOTO_ID_COLUMN)));
                        people.add(p);
                    } while (c.moveToNext());
                }
                return people;
            } finally {
                if (c != null) c.close();
            }
        }
    }
}
