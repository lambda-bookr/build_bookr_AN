package com.example.israel.build_week_1_bookr.worker_thread;

import android.os.AsyncTask;

import com.example.israel.build_week_1_bookr.dao.BookrAPIDAO;
import com.example.israel.build_week_1_bookr.model.UserInfo;
import com.example.israel.build_week_1_bookr.network.NetworkAdapter;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class RequestUserInfoAsyncTask extends AsyncTask<Void, Void, UserInfo> {

    public RequestUserInfoAsyncTask(int userId, String token) {
        this.userId = userId;
        this.token = token;
    }

    private int userId;
    private String token;

    @Override
    protected UserInfo doInBackground(Void... voids) {
        return BookrAPIDAO.getUserInfo(userId, token);
    }
}
