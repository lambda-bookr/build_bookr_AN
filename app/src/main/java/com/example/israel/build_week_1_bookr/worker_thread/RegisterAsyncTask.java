package com.example.israel.build_week_1_bookr.worker_thread;

import android.os.AsyncTask;

import com.example.israel.build_week_1_bookr.CommonStatics;
import com.example.israel.build_week_1_bookr.network.NetworkAdapter;

import org.json.JSONException;
import org.json.JSONObject;

public class RegisterAsyncTask extends AsyncTask<Void, Void, RegisterAsyncTask.Result> {

    static public String REGISTER = "auth/register/";

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
            registerFormJson.put("username", username);
            registerFormJson.put("password", password);
            registerFormJson.put("firstName", firstName);
            registerFormJson.put("lastName", lastName);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        String replyStr = NetworkAdapter.httpRequestPOSTJson(CommonStatics.DATABASE_BASE_URL + REGISTER, registerFormJson);

        Result result = new Result();
        if (replyStr == null) {
            result.code = Result.USERNAME_TAKEN;
            return result;
        }

        try {
            JSONObject replyJson = new JSONObject(replyStr);
            result.token = replyJson.getString("token");
            result.code = Result.SUCCESS;
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return result;
    }

    public class Result {
        public static final int SUCCESS = 0;
        public static final int USERNAME_TAKEN = 1;

        public int code;
        public String token;
    }
}
