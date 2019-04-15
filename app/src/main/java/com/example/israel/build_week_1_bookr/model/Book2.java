package com.example.israel.build_week_1_bookr.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

// TODO parcelable? the problem is the CREATOR
public class Book2 extends Book{

    public Book2(JSONObject book2Json) {
        super(book2Json);

        try {
            username = book2Json.getString("username");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            firstName = book2Json.getString("firstName");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            lastName = book2Json.getString("lastName");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            thumbnailUrl = book2Json.getString("thumbnailUrl");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            JSONArray reviewsJsonArr = book2Json.getJSONArray("reviews");
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
    private ArrayList<Review> reviews;


}
