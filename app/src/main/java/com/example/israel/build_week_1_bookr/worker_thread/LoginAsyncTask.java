package com.example.israel.build_week_1_bookr.worker_thread;

import android.os.AsyncTask;
import android.support.annotation.NonNull;

public class LoginAsyncTask extends AsyncTask<Void, Void, LoginAsyncTask.Result> {

    public LoginAsyncTask(@NonNull String email, @NonNull String password) {
        this.email = email;
        this.password = password;
    }

    private String email;
    private String password;

    @Override
    @NonNull
    protected LoginAsyncTask.Result doInBackground(Void... voids) {

        try { // TODO remove this
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Result result = new Result();
        // TODO
        result.result = Result.SUCCESS;
        result.sessionToken = "1234";

        return result;
    }

    public static boolean isValidEmail(String email) {
        return email.contains("@");
    }

    public class Result {

        public static final int SUCCESS = 0;
        public static final int INVALID_EMAIL = 1;
        public static final int EMAIL_NOT_FOUND = 2;
        public static final int WRONG_PASSWORD = 3;
        public static final int TIME_OUT = 4;

        public int result;
        public String sessionToken;

    }

}
