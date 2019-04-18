package com.example.israel.build_week_1_bookr.worker_thread;

import android.os.AsyncTask;

import com.example.israel.build_week_1_bookr.CommonStatics;
import com.example.israel.build_week_1_bookr.dao.BookrAPIDAO;
import com.example.israel.build_week_1_bookr.model.Book;
import com.example.israel.build_week_1_bookr.network.NetworkAdapter;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.HttpURLConnection;

public class RequestDeleteBookAsyncTask extends AsyncTask<Void, Void, Book> {

    public RequestDeleteBookAsyncTask(int bookId) {
        this.bookId = bookId;
    }

    private int bookId;

    @Override
    protected Book doInBackground(Void... voids) {

    return BookrAPIDAO.deleteBook(bookId);
    }

}
