package com.sperchenko.likedislikedemo.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by Stas on 28.11.2015.
 */
@DatabaseTable(tableName = CrossRating.TABLE_NAME)
public class CrossRating {
    public static final String TABLE_NAME = "CrossRating";

    public static final String COMPOSITE_ID_COLUMN = "composite_id";
    public static final String THIS_USER_COLUMN = "thisUser";
    public static final String OTHER_USER_COLUMN = "otherUser";
    public static final String SCORE_COLUMN = "score";
    public static final String LIKE_STATUS_COLUMN = "likeStatus";
    public static final String COMMITTED_COLUMNS_COLUMN = "committed";


    @DatabaseField(columnName = COMPOSITE_ID_COLUMN, id = true, useGetSet = true)
    private String id;

    @DatabaseField(columnName = THIS_USER_COLUMN)
    private int thisUserId;

    @DatabaseField(foreign = true, columnName = OTHER_USER_COLUMN, foreignColumnName = Person.ID_COLUMN)
    private Person otherUser;

    @DatabaseField(columnName = SCORE_COLUMN)
    private int score;

    @DatabaseField(columnName = LIKE_STATUS_COLUMN)
    private LikeStatus likeStatus;

    @DatabaseField(columnName = COMMITTED_COLUMNS_COLUMN)
    private boolean committed;

    public CrossRating(int thisUserId, int otherUserId, int score) {
        setThisUserId(thisUserId);
        setOtherUser(otherUserId);
        this.score = score;
    }

    public String getId() {
        return thisUserId+"_"+otherUser.getId();
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getThisUserId() {
        return thisUserId;
    }

    public void setThisUserId(int thisUserId) {
        this.thisUserId = thisUserId;
    }

    public Person getOtherUser() {
        return otherUser;
    }

    public void setOtherUser(Person otherUser) {
        this.otherUser = otherUser;
    }

    public void setOtherUser(int otherUserId) {
        if (this.otherUser == null) {
            this.otherUser = new Person();
        }
        this.otherUser.setId(otherUserId);
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public LikeStatus getLikeStatus() {
        return likeStatus;
    }

    public void setLikeStatus(LikeStatus likeStatus) {
        this.likeStatus = likeStatus;
    }

    public boolean isCommitted() {
        return committed;
    }

    public void setCommitted(boolean committed) {
        this.committed = committed;
    }
}
