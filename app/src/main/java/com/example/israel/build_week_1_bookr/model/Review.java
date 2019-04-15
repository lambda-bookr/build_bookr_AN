package com.example.israel.build_week_1_bookr.model;

import org.json.JSONException;
import org.json.JSONObject;

public class Review {

    public Review(JSONObject reviewJson) {

        try {
            id = reviewJson.getInt("id");
        } catch (JSONException e) {
            e.printStackTrace();
            id = Book.INVALID_ID;
        }

        try {
            username = reviewJson.getString("username");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            review = reviewJson.getString("review");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            rating = reviewJson.getInt("rating");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            thumbnailUrl = reviewJson.getString("thumbnailUrl");
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private int id;
    private String username;
    private String review;
    private int rating;
    private String thumbnailUrl;

}
