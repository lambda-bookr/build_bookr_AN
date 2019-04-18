package com.example.israel.build_week_1_bookr.worker_thread;

import android.os.AsyncTask;

import com.example.israel.build_week_1_bookr.dao.BookrAPIDAO;
import com.example.israel.build_week_1_bookr.model.Book;

public class RequestAddBookAsyncTask extends AsyncTask<Void, Void, Book> {

    public RequestAddBookAsyncTask(String token, int userId, String title, String author, String publisher, double price, String description, String imageUrl) {
        this.token = token;
        this.userId = userId;
        this.title = title;
        this.author = author;
        this.publisher = publisher;
        this.price = price;
        this.description = description;
        this.imageUrl = imageUrl;
    }

    private String token;
    private int userId;
    private String title;
    private String author;
    private String publisher;
    private double price;
    private String description;
    private String imageUrl;

    @Override
    protected Book doInBackground(Void... voids) {

        return BookrAPIDAO.addBook(token, userId, title, author, publisher, price, description, imageUrl);
    }

}
