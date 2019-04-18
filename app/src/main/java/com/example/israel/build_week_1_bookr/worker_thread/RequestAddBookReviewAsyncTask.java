package com.example.israel.build_week_1_bookr.worker_thread;

import android.os.AsyncTask;

import com.example.israel.build_week_1_bookr.dao.BookrAPIDAO;
import com.example.israel.build_week_1_bookr.model.Review;

public class RequestAddBookReviewAsyncTask extends AsyncTask<Void, Void, Review> {

    public RequestAddBookReviewAsyncTask(String token, int bookId, int userId, int rating, String review) {
        this.token = token;
        this.bookId = bookId;
        this.userId = userId;
        this.rating = rating;
        this.review = review;
    }

    private String token;
    private int bookId;
    private int userId;
    private int rating;
    private String review;

    @Override
    protected Review doInBackground(Void... voids) {

        return BookrAPIDAO.addReview(token, bookId, userId, rating, review);
    }
}
