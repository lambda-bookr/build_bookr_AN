package com.example.israel.build_week_1_bookr.worker_thread;

import android.os.AsyncTask;

public class ValidateSessionTokenAsyncTask extends AsyncTask<Void, Void, Boolean> {

    public ValidateSessionTokenAsyncTask(String sessionToken) {
        this.sessionToken = sessionToken;
    }

    private String sessionToken;

    @Override
    protected Boolean doInBackground(Void... voids) {

        // TODO

        return false;
    }
}
