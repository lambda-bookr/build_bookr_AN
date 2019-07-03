package com.isra.israel.build_week_1_bookr.json_object;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OutReview {

    public OutReview(int bookId, int userId, int rating, String review) {
        this.bookId = bookId;
        this.userId = userId;
        this.rating = rating;
        this.review = review;
    }

    @SerializedName("book_id")
    @Expose
    private int bookId;

    @SerializedName("user_id")
    @Expose
    private int userId;

    @SerializedName("rating")
    @Expose
    private int rating;

    @SerializedName("review")
    @Expose
    private String review;
}
