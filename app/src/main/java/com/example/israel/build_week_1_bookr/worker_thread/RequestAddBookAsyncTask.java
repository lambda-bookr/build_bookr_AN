package com.example.israel.build_week_1_bookr.worker_thread;

import android.os.AsyncTask;

import com.example.israel.build_week_1_bookr.CommonStatics;
import com.example.israel.build_week_1_bookr.dao.BookrAPIDAO;
import com.example.israel.build_week_1_bookr.model.Book;
import com.example.israel.build_week_1_bookr.network.NetworkAdapter;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.HttpURLConnection;

public class RequestAddBookAsyncTask extends AsyncTask<Void, Void, Book> {

    private static final String ADD_BOOK = "api/books/";
    private static final String KEY_JSON_USER_ID = "user_id";
    private static final String KEY_JSON_TITLE = "title";
    private static final String KEY_JSON_AUTHOR = "author";
    private static final String KEY_JSON_PUBLISHER = "publisher";
    private static final String KEY_JSON_PRICE = "price";
    private static final String KEY_JSON_DESCRIPTION = "description";
    private static final String KEY_JSON_IMAGE_URL = "imageUrl";

    public RequestAddBookAsyncTask(int userId, String title, String author, String publisher, double price, String description, String imageUrl) {
        this.userId = userId;
        this.title = title;
        this.author = author;
        this.publisher = publisher;
        this.price = price;
        this.description = description;
        this.imageUrl = imageUrl;
    }

    private int userId;
    private String title;
    private String author;
    private String publisher;
    private double price;
    private String description;
    private String imageUrl;

    @Override
    protected Book doInBackground(Void... voids) {

        return BookrAPIDAO.addBook(userId, title, author, publisher, price, description, imageUrl);
    }

}
