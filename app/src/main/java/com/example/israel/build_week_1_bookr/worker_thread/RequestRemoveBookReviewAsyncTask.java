package com.example.israel.build_week_1_bookr.worker_thread;

import android.os.AsyncTask;

import com.example.israel.build_week_1_bookr.dao.BookrAPIDAO;
import com.example.israel.build_week_1_bookr.model.Review;

public class RequestRemoveBookReviewAsyncTask extends AsyncTask<Void, Void, Review> {

    public RequestRemoveBookReviewAsyncTask(int reviewId) {
        this.reviewId = reviewId;
    }

    private int reviewId;

    @Override
    protected Review doInBackground(Void... voids) {
        return BookrAPIDAO.removeReview(reviewId);
    }
}
