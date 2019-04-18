package com.example.israel.build_week_1_bookr.worker_thread;

import android.os.AsyncTask;
import android.support.annotation.Nullable;

import com.example.israel.build_week_1_bookr.dao.BookrAPIDAO;
import com.example.israel.build_week_1_bookr.model.Book;
import com.example.israel.build_week_1_bookr.model.Book2;

public class RequestBook2AsyncTask extends AsyncTask<Void, Void, Book2> {

    public RequestBook2AsyncTask(String token, Book book) {
        this.token = token;
        this.book = book;
    }

    private String token;
    private Book book;

    @Override
    @Nullable
    protected Book2 doInBackground(Void... voids) {
        return BookrAPIDAO.getBook2(token, book);
    }
}
