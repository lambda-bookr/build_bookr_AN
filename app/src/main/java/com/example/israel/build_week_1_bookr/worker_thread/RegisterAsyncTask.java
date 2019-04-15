package com.example.israel.build_week_1_bookr.worker_thread;

import android.os.AsyncTask;

public class RegisterAsyncTask extends AsyncTask<Void, Void, Void> {

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
    protected Void doInBackground(Void... voids) {

        

        return null;
    }
}
