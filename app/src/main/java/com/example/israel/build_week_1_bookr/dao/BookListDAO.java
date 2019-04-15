package com.example.israel.build_week_1_bookr.dao;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.WorkerThread;

import com.example.israel.build_week_1_bookr.model.Book;
import com.example.israel.build_week_1_bookr.model.Book2;
import com.example.israel.build_week_1_bookr.network.NetworkAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class BookListDAO {

    public static final String BASE_URL = "https://bookr-backend.herokuapp.com/api/";
    public static final String BOOK_LIST = "books/";

    @WorkerThread
    @NonNull
    public static ArrayList<Book> getBookList() {
        ArrayList<Book> books = new ArrayList<>();

        String booksJsonStr = NetworkAdapter.httpRequestGET(BASE_URL + BOOK_LIST);

        if (booksJsonStr == null) {
            return books;
        }

        try {
            JSONObject booksJson = new JSONObject(booksJsonStr);
            JSONArray booksJsonArr = booksJson.getJSONArray("books");
            // TODO

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return books;
    }

    @WorkerThread
    @Nullable
    public static Book2 getBook2(Book book) {
        String book2JsonStr = NetworkAdapter.httpRequestGET(BASE_URL + BOOK_LIST + book.getId());
        if (book2JsonStr == null) {
            return null;
        }

        try {
            JSONObject book2Json = new JSONObject(book2JsonStr);
            return new Book2(book2Json);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

}
