package com.example.israel.build_week_1_bookr.worker_thread;

import android.os.AsyncTask;
import android.support.annotation.NonNull;

import com.example.israel.build_week_1_bookr.CommonStatics;
import com.example.israel.build_week_1_bookr.network.NetworkAdapter;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginAsyncTask extends AsyncTask<Void, Void, LoginAsyncTask.Result> {

    static public final String LOGIN = "auth/login/";

    public LoginAsyncTask(@NonNull String email, @NonNull String password) {
        this.email = email;
        this.password = password;
    }

    private String email;
    private String password;

    @Override
    @NonNull
    protected LoginAsyncTask.Result doInBackground(Void... voids) {

//        try { // TODO remove this
//            Thread.sleep(2000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }

        Result result = new Result();

        JSONObject credentialsJson = new JSONObject();
        try {
            credentialsJson.put("username", email);
            credentialsJson.put("password", password);
        } catch (JSONException e) {
            e.printStackTrace();
            return result;
        }

        String replyStr = NetworkAdapter.httpRequestPOSTJson(CommonStatics.DATABASE_BASE_URL + LOGIN, credentialsJson);
        if (replyStr == null) {
            result.result = Result.WRONG_PASSWORD;
            return result;
        }

        try {
            JSONObject replyJson = new JSONObject(replyStr);
            result.sessionToken = replyJson.getString("token");
            result.result = Result.SUCCESS;
        } catch (JSONException e) {
            e.printStackTrace();
            result.result = Result.INVALID_REPLY;
        }

        return result;
    }

    public class Result {

        public static final int SUCCESS = 0;
        public static final int INVALID_EMAIL = 1;
        public static final int INVALID_PASSWORD = 2;
        public static final int EMAIL_NOT_FOUND = 3;
        public static final int WRONG_PASSWORD = 4;
        public static final int TIME_OUT = 5;
        public static final int INVALID_REPLY = 6;

        public int result;
        public String sessionToken;

    }

}
