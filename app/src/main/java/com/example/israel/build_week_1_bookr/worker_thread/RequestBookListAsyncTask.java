package com.example.israel.build_week_1_bookr.worker_thread;

import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.example.israel.build_week_1_bookr.dao.BookListDAO;
import com.example.israel.build_week_1_bookr.model.Book;

import java.util.ArrayList;

public class RequestBookListAsyncTask extends AsyncTask<Void, Void, RequestBookListAsyncTask.Result> {

    @Override
    @NonNull
    protected Result doInBackground(Void... voids) {

        // simulation
//        try {
//            Thread.sleep(2000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }

        Result result = new Result();
        result.books = BookListDAO.getBookList();

        return result;
    }

    public class Result {
        public ArrayList<Book> books;
    }

}
