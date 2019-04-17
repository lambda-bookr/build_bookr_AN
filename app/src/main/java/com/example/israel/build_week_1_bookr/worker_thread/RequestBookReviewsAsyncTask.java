package com.example.israel.build_week_1_bookr.worker_thread;

import android.os.AsyncTask;

import com.example.israel.build_week_1_bookr.dao.BookrAPIDAO;
import com.example.israel.build_week_1_bookr.model.Review;
import com.example.israel.build_week_1_bookr.network.NetworkAdapter;

import java.util.ArrayList;

public class RequestBookReviewsAsyncTask extends AsyncTask<Void, Void, RequestBookReviewsAsyncTask.Result> {

    public RequestBookReviewsAsyncTask(int bookId) {
        this.bookId = bookId;
    }

    private int bookId;

    @Override
    protected Result doInBackground(Void... voids) {

        // TODO better Result
        Result result = new Result();
        result.reviews = BookrAPIDAO.getReviews(bookId);
        return result;
    }

    public class Result {
        public ArrayList<Review> reviews;

    }
}
