package com.example.israel.build_week_1_bookr.model;

import android.support.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.json.JSONException;
import org.json.JSONObject;

public class UserInfo {

    public static final String KEY_JSON_ID = "id";
    public static final String KEY_JSON_USERNAME = "username";
    public static final String KEY_JSON_FIRST_NAME = "firstName";
    public static final String KEY_JSON_LAST_NAME = "lastName";
    public static final String KEY_JSON_THUMBNAIL_URL = "thumbnailUrl";

    public UserInfo(int id, String username, String firstName, String lastName, String thumbnailUrl) {
        this.id = id;
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.thumbnailUrl = thumbnailUrl;
    }

    public UserInfo(JSONObject userInfoJson) {
        try {
            id = userInfoJson.getInt(KEY_JSON_ID);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            username = userInfoJson.getString(KEY_JSON_USERNAME);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            firstName = userInfoJson.getString(KEY_JSON_FIRST_NAME);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            lastName = userInfoJson.getString(KEY_JSON_LAST_NAME);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            thumbnailUrl = userInfoJson.getString(KEY_JSON_THUMBNAIL_URL);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @SerializedName("id")
    @Expose
    private int id;

    @SerializedName("username")
    @Expose
    private String username;

    @SerializedName("firstName")
    @Expose
    private String firstName;

    @SerializedName("lastName")
    @Expose
    private String lastName;

    @SerializedName("thumbnailUrl")
    @Expose
    private String thumbnailUrl;

    // password is not stored because that's not secure

    public int getId() {
        return id;
    }

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

    @NonNull
    @Override
    public String toString() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(KEY_JSON_ID, id);
            jsonObject.put(KEY_JSON_USERNAME, username);
            jsonObject.put(KEY_JSON_FIRST_NAME, firstName);
            jsonObject.put(KEY_JSON_LAST_NAME, lastName);
            jsonObject.put(KEY_JSON_THUMBNAIL_URL, thumbnailUrl);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return jsonObject.toString();
    }
}
