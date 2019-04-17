package com.example.israel.build_week_1_bookr.model;

import org.json.JSONException;
import org.json.JSONObject;

// TODO DEPENDS. if Book2 needs to be parcelable then this also needs to be parcelable
public class Review {

    private static final String KEY_JSON_ID = "id";
    private static final String KEY_JSON_USERNAME = "username";
    private static final String KEY_JSON_REVIEW = "review";
    private static final String KEY_JSON_RATING = "rating";
    private static final String KEY_JSON_THUMBNAIL_URL = "thumbnailUrl";

    public Review(JSONObject reviewJson) {

        try {
            id = reviewJson.getInt(KEY_JSON_ID);
        } catch (JSONException e) {
            e.printStackTrace();
            id = Book.INVALID_ID;
        }

        try {
            username = reviewJson.getString(KEY_JSON_USERNAME);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            review = reviewJson.getString(KEY_JSON_REVIEW);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            rating = reviewJson.getInt(KEY_JSON_RATING);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            thumbnailUrl = reviewJson.getString(KEY_JSON_THUMBNAIL_URL);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private int id;
    private String username;
    private String review;
    private int rating;
    private String thumbnailUrl;

    public int getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getReview() {
        return review;
    }

    public int getRating() {
        return rating;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }
}
