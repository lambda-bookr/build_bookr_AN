package com.example.israel.build_week_1_bookr.worker_thread;

import android.os.AsyncTask;
import android.support.annotation.NonNull;

import com.example.israel.build_week_1_bookr.CommonStatics;
import com.example.israel.build_week_1_bookr.network.NetworkAdapter;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.HttpURLConnection;

public class LoginAsyncTask extends AsyncTask<Void, Void, LoginAsyncTask.Result> {

    static private final String LOGIN = "api/auth/login/";
    static private final String KEY_JSON_USERNAME = "username";
    static private final String KEY_JSON_PASSWORD = "password";
    static private final String KEY_JSON_TOKEN = "token";
    static private final String KEY_JSON_USER_ID = "userID";

    public LoginAsyncTask(@NonNull String username, @NonNull String password) {
        this.username = username;
        this.password = password;
    }

    private String username;
    private String password;

    @Override
    @NonNull
    protected LoginAsyncTask.Result doInBackground(Void... voids) {

        Result result = new Result();

        JSONObject credentialsJson = new JSONObject();
        try {
            credentialsJson.put(KEY_JSON_USERNAME, username);
            credentialsJson.put(KEY_JSON_PASSWORD, password);
        } catch (JSONException e) {
            e.printStackTrace();
            return result;
        }

        NetworkAdapter.Result requestResult = NetworkAdapter.httpRequestPOSTJson(CommonStatics.DATABASE_BASE_URL + LOGIN, credentialsJson);
        // unknown error
        if (requestResult.responseCode == NetworkAdapter.Result.INVALID_RESPONSE_CODE) {
            result.result = Result.UNKNOWN_ERROR;
            return result;
        }

        // cannot connect to server
        if (requestResult.responseCode == HttpURLConnection.HTTP_NOT_FOUND) {
            result.result = Result.CANNOT_CONNECT_TO_SERVER;
            return result;
        }

        if (requestResult.responseCode == HttpURLConnection.HTTP_CREATED) { // successful log in
            String replyStr = (String)requestResult.resultObj;
            try {
                JSONObject replyJson = new JSONObject(replyStr);
                result.sessionToken = replyJson.getString(KEY_JSON_TOKEN);
                result.userId = replyJson.getInt(KEY_JSON_USER_ID);
                result.result = Result.SUCCESS;
            } catch (JSONException e) {
                e.printStackTrace();
                result.result = Result.INVALID_SERVER_REPLY;
            }
        } else {
            result.result = Result.WRONG_PASSWORD;
        }

        return result;
    }

    public class Result {

        public static final int SUCCESS = 0;
        public static final int INVALID_USERNAME = 1;
        public static final int INVALID_PASSWORD = 2;
        public static final int USERNAME_NOT_FOUND = 3;
        public static final int WRONG_PASSWORD = 4;
        public static final int TIME_OUT = 5;
        public static final int INVALID_SERVER_REPLY = 6;
        public static final int UNKNOWN_ERROR = 7;
        public static final int CANNOT_CONNECT_TO_SERVER = 8;

        public int result;
        public String sessionToken;
        public int userId;

    }

}
