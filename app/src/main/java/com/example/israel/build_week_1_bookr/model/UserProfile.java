package com.example.israel.build_week_1_bookr.model;

public class UserProfile {

    public UserProfile(int id, String username, String firstName, String lastName, String thumbnailUrl) {
        this.id = id;
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.thumbnailUrl = thumbnailUrl;
    }

    private int id;
    private String username;
    private String firstName;
    private String lastName;
    private String thumbnailUrl;

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
