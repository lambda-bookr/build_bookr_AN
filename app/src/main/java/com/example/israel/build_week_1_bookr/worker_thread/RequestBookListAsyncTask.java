package com.example.israel.build_week_1_bookr.worker_thread;

import android.os.AsyncTask;
import android.support.annotation.NonNull;

import com.example.israel.build_week_1_bookr.dao.BookrAPIDAO;
import com.example.israel.build_week_1_bookr.model.Book;

import java.util.ArrayList;

public class RequestBookListAsyncTask extends AsyncTask<Void, Void, RequestBookListAsyncTask.Result> {

    @Override
    @NonNull
    protected Result doInBackground(Void... voids) {

        // TODO better Result
        Result result = new Result();
        result.books = BookrAPIDAO.getBookList();

        return result;
    }

    public class Result {
        public ArrayList<Book> books;
    }

}
