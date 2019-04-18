package com.example.israel.build_week_1_bookr.worker_thread;

import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.SparseArray;

import com.example.israel.build_week_1_bookr.CommonStatics;
import com.example.israel.build_week_1_bookr.dao.BookrAPIDAO;
import com.example.israel.build_week_1_bookr.model.UserInfo;
import com.example.israel.build_week_1_bookr.network.NetworkAdapter;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.util.HashMap;

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
    @Nullable
    protected Result doInBackground(Void... voids) {

        SparseArray<String> userId_Token = BookrAPIDAO.login(username, password);
        if (userId_Token == null) {
            return null;
        }

        int userId = userId_Token.keyAt(0);
        String token = userId_Token.valueAt(0);

        UserInfo userInfo = BookrAPIDAO.getUserInfo(userId, token);
        if (userInfo == null) {
            return null;
        }

        Result result = new Result();
        result.userInfo = userInfo;
        result.sessionToken = token;
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
        public UserInfo userInfo;

    }

}
