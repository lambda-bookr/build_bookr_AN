package com.example.israel.build_week_1_bookr.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

// TODO DEPENDS. parcelable if needed. the problem is the CREATOR
// TODO MEDIUM use UserProfile
public class Book2 extends Book{

    private static final String KEY_JSON_USERNAME = "username";
    private static final String KEY_JSON_FIRST_NAME = "firstName";
    private static final String KEY_JSON_LAST_NAME = "lastName";
    private static final String KEY_JSON_THUMBNAIL_URL = "thumbnailUrl";
    private static final String KEY_JSON_REVIEWS = "reviews";

    public Book2(JSONObject book2Json) {
        super(book2Json);

        try {
            username = book2Json.getString(KEY_JSON_USERNAME);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            firstName = book2Json.getString(KEY_JSON_FIRST_NAME);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            lastName = book2Json.getString(KEY_JSON_LAST_NAME);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            thumbnailUrl = book2Json.getString(KEY_JSON_THUMBNAIL_URL);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            JSONArray reviewsJsonArr = book2Json.getJSONArray(KEY_JSON_REVIEWS);
            if (reviewsJsonArr != null) {
                reviews.ensureCapacity(reviewsJsonArr.length());
                for (int i = 0; i < reviewsJsonArr.length(); ++i) {
                    JSONObject reviewJson = reviewsJsonArr.getJSONObject(i);
                    reviews.add(new Review(reviewJson));
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private String username;
    private String firstName;
    private String lastName;
    private String thumbnailUrl;
    private ArrayList<Review> reviews = new ArrayList<>();

    public String getUsername() {
        return username;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public ArrayList<Review> getReviews() {
        return reviews;
    }
}
