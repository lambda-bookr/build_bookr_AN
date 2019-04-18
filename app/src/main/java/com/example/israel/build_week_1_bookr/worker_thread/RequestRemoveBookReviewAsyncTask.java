package com.example.israel.build_week_1_bookr.worker_thread;

import android.os.AsyncTask;

import com.example.israel.build_week_1_bookr.dao.BookrAPIDAO;
import com.example.israel.build_week_1_bookr.model.Review;

public class RequestRemoveBookReviewAsyncTask extends AsyncTask<Void, Void, Review> {

    public RequestRemoveBookReviewAsyncTask(String token, int reviewId) {
        this.token = token;
        this.reviewId = reviewId;
    }

    private String token;
    private int reviewId;

    @Override
    protected Review doInBackground(Void... voids) {
        return BookrAPIDAO.removeReview(token, reviewId);
    }
}
