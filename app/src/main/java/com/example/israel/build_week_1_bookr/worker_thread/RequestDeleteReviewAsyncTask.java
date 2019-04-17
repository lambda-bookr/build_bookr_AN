package com.example.israel.build_week_1_bookr.worker_thread;

import android.os.AsyncTask;

import com.example.israel.build_week_1_bookr.dao.BookrAPIDAO;

import org.json.JSONObject;

public class RequestDeleteReviewAsyncTask extends AsyncTask<Void, Void, JSONObject> {

    public RequestDeleteReviewAsyncTask(int reviewId) {
        this.reviewId = reviewId;
    }

    private int reviewId;

    @Override
    protected JSONObject doInBackground(Void... voids) {
        return BookrAPIDAO.removeReview(reviewId);
    }
}
