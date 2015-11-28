package com.sperchenko.likedislikedemo.model;

/**
 * Created by Stas on 28.11.2015.
 */
public enum LikeStatus {
    NOT_CHOSEN(0), LIKED(1), DISLIKED(2);

    private int numberRepresentation;

    LikeStatus(int numberRepresentation) {
        this.numberRepresentation = numberRepresentation;
    }

    public int getNumberRepresentation() {
        return numberRepresentation;
    }
}
