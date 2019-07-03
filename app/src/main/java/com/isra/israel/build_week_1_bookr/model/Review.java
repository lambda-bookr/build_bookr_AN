package com.isra.israel.build_week_1_bookr.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

// TODO DEPENDS. if Book2 needs to be parcelable then this also needs to be parcelable
public class Review {

    public Review() {

    }

    public Review(String username, String review, int rating, String thumbnailUrl) {
        this.username = username;
        this.review = review;
        this.rating = rating;
        this.thumbnailUrl = thumbnailUrl;
    }

    @SerializedName("id")
    @Expose(serialize = false) // don't send it, only receive it
    private int id;

    @SerializedName("username")
    @Expose
    private String username;

    @SerializedName("review")
    @Expose
    private String review;

    @SerializedName("rating")
    @Expose
    private int rating;

    @SerializedName("thumbnailUrl")
    @Expose
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
