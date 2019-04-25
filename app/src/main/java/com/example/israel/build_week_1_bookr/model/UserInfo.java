package com.example.israel.build_week_1_bookr.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserInfo {

    public UserInfo(int id, String username, String firstName, String lastName, String thumbnailUrl) {
        this.id = id;
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.thumbnailUrl = thumbnailUrl;
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

}
