package com.example.israel.build_week_1_bookr.worker_thread;

import android.os.AsyncTask;
import android.support.annotation.NonNull;

import com.example.israel.build_week_1_bookr.dao.BookrAPIDAO;
import com.example.israel.build_week_1_bookr.model.Book;

import java.util.ArrayList;

public class RequestBookListAsyncTask extends AsyncTask<Void, Void, ArrayList<Book>> {

    @Override
    @NonNull
    protected ArrayList<Book> doInBackground(Void... voids) {

        return BookrAPIDAO.getBookList();
    }

}
