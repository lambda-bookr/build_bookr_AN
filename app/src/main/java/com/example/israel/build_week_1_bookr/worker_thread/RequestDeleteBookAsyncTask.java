package com.example.israel.build_week_1_bookr.worker_thread;

import android.os.AsyncTask;

import com.example.israel.build_week_1_bookr.dao.BookrAPIDAO;
import com.example.israel.build_week_1_bookr.model.Book;

public class RequestDeleteBookAsyncTask extends AsyncTask<Void, Void, Book> {

    public RequestDeleteBookAsyncTask(String token, int bookId) {
        this.token = token;
        this.bookId = bookId;
    }

    private String token;
    private int bookId;

    @Override
    protected Book doInBackground(Void... voids) {

    return BookrAPIDAO.deleteBook(token, bookId);
    }

}
