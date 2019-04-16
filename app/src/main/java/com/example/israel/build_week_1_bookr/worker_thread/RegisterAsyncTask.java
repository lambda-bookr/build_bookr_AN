package com.example.israel.build_week_1_bookr.worker_thread;

import android.os.AsyncTask;

import com.example.israel.build_week_1_bookr.CommonStatics;
import com.example.israel.build_week_1_bookr.network.NetworkAdapter;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.HttpURLConnection;

public class RegisterAsyncTask extends AsyncTask<Void, Void, RegisterAsyncTask.Result> {

    static private final String REGISTER = "api/auth/register/";
    static private final String KEY_JSON_USERNAME = "username";
    static private final String KEY_JSON_PASSWORD = "password";
    static private final String KEY_JSON_FIRST_NAME = "firstname";
    static private final String KEY_JSON_LAST_NAME = "lastname";
    static private final String KEY_JSON_TOKEN = "token";

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
    protected Result doInBackground(Void... voids) {

        JSONObject registerFormJson = new JSONObject();
        try {
            registerFormJson.put(KEY_JSON_USERNAME, username);
            registerFormJson.put(KEY_JSON_PASSWORD, password);
            registerFormJson.put(KEY_JSON_FIRST_NAME, firstName);
            registerFormJson.put(KEY_JSON_LAST_NAME, lastName);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Result result = new Result();
        NetworkAdapter.Result requestResult = NetworkAdapter.httpRequestPOSTJson(CommonStatics.DATABASE_BASE_URL + REGISTER, registerFormJson);
        // unknown error
        if (requestResult.responseCode == NetworkAdapter.Result.INVALID_RESPONSE_CODE) {
            result.code = Result.UNKNOWN_ERROR;
            return result;
        }

        if (requestResult.responseCode == HttpURLConnection.HTTP_CREATED) { // success
            String replyStr = (String)requestResult.resultObj;

            try {
                JSONObject replyJson = new JSONObject(replyStr);
                result.token = replyJson.getString(KEY_JSON_TOKEN);
                result.code = Result.SUCCESS;
            } catch (JSONException e) {
                e.printStackTrace();
                result.code = Result.INVALID_SERVER_REPLY;
            }

        } else if (requestResult.responseCode == HttpURLConnection.HTTP_INTERNAL_ERROR) { // username taken
            result.code = Result.USERNAME_TAKEN;
        } else {
            result.code = Result.UNKNOWN_ERROR;
        }

        return result;
    }

    public class Result {
        public static final int SUCCESS = 0;
        public static final int USERNAME_TAKEN = 1;
        public static final int UNKNOWN_ERROR = 2;
        public static final int INVALID_SERVER_REPLY = 3;

        public int code;
        public String token;
    }
}
