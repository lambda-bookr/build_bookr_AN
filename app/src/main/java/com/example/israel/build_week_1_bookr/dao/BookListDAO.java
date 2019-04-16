package com.example.israel.build_week_1_bookr.dao;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.WorkerThread;

import com.example.israel.build_week_1_bookr.CommonStatics;
import com.example.israel.build_week_1_bookr.model.Book;
import com.example.israel.build_week_1_bookr.model.Book2;
import com.example.israel.build_week_1_bookr.network.NetworkAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class BookListDAO {

    private static final String BASE_URL = CommonStatics.DATABASE_BASE_URL;
    private static final String BOOK_LIST = "api/books/";

    @WorkerThread
    @NonNull
    public static ArrayList<Book> getBookList() {
        ArrayList<Book> books = new ArrayList<>();

        String booksJsonStr = NetworkAdapter.httpRequestGET(BASE_URL + BOOK_LIST);

        if (booksJsonStr == null) {
            return books;
        }

        try {
            JSONArray booksJsonArr = new JSONArray(booksJsonStr);
            books.ensureCapacity(booksJsonArr.length());
            for (int i = 0; i < booksJsonArr.length(); ++i) {
                JSONObject bookJson = booksJsonArr.getJSONObject(i);
                books.add(new Book(bookJson));
            }

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
