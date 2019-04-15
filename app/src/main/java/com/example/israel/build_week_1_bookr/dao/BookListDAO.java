package com.example.israel.build_week_1_bookr.dao;

import android.support.annotation.NonNull;
import android.support.annotation.WorkerThread;

import com.example.israel.build_week_1_bookr.model.Book;

import java.util.ArrayList;

public class BookListDAO {

    public static final String BASE_URL = ""; // TODO acquire from backend

    @WorkerThread
    @NonNull
    public static ArrayList<Book> getBookList() {
        ArrayList<Book> books = new ArrayList<>();

        // TODO
        for (int i = 0; i < 100; ++i) {
            books.add(new Book("Book Title Very Long Title Extra Long Title" + i));
        }

        return books;
    }

}
