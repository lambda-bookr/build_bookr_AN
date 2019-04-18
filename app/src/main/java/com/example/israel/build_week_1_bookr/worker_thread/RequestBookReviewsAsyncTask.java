package com.example.israel.build_week_1_bookr.worker_thread;

import android.os.AsyncTask;

import com.example.israel.build_week_1_bookr.dao.BookrAPIDAO;
import com.example.israel.build_week_1_bookr.model.Review;

import java.util.ArrayList;

public class RequestBookReviewsAsyncTask extends AsyncTask<Void, Void, ArrayList<Review>> {

    public RequestBookReviewsAsyncTask(String token, int bookId) {
        this.token = token;
        this.bookId = bookId;
    }

    private String token;
    private int bookId;

    @Override
    protected ArrayList<Review> doInBackground(Void... voids) {

        return BookrAPIDAO.getReviews(token, bookId);
    }
}
