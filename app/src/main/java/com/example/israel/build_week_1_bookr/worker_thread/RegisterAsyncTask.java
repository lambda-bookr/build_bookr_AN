package com.example.israel.build_week_1_bookr.worker_thread;

import android.os.AsyncTask;
import android.support.annotation.Nullable;

import com.example.israel.build_week_1_bookr.dao.BookrAPIDAO;
public class RegisterAsyncTask extends AsyncTask<Void, Void, String> {

    public RegisterAsyncTask(String username, String password, String firstName, String lastName) {
        this.username = username;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    private String username;
    private String password;
    private String firstName;
    private String lastName;

    @Override
    @Nullable
    protected String doInBackground(Void... voids) {
        return BookrAPIDAO.register(username, password, firstName, lastName);
    }
}
