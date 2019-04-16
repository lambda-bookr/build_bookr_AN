package com.example.israel.build_week_1_bookr.worker_thread;

import android.os.AsyncTask;

import com.example.israel.build_week_1_bookr.CommonStatics;
import com.example.israel.build_week_1_bookr.network.NetworkAdapter;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.HttpURLConnection;

public class RequestAddBookAsyncTask extends AsyncTask<Void, Void, RequestAddBookAsyncTask.Result> {

    private static final String ADD_BOOK = "api/books/";
    private static final String KEY_JSON_USER_ID = "user_id";
    private static final String KEY_JSON_TITLE = "title";
    private static final String KEY_JSON_AUTHOR = "author";
    private static final String KEY_JSON_PUBLISHER = "publisher";
    private static final String KEY_JSON_PRICE = "price";
    private static final String KEY_JSON_DESCRIPTION = "description";
    private static final String KEY_JSON_IMAGE_URL = "imageUrl";

    public RequestAddBookAsyncTask(int userId, String title, String author, String publisher, double price, String description, String imageUrl) {
        this.userId = userId;
        this.title = title;
        this.author = author;
        this.publisher = publisher;
        this.price = price;
        this.description = description;
        this.imageUrl = imageUrl;
    }

    private int userId;
    private String title;
    private String author;
    private String publisher;
    private double price;
    private String description;
    private String imageUrl;

    @Override
    protected Result doInBackground(Void... voids) {

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(KEY_JSON_USER_ID, userId);
            jsonObject.put(KEY_JSON_TITLE, title);
            jsonObject.put(KEY_JSON_AUTHOR, author);
            jsonObject.put(KEY_JSON_PUBLISHER, publisher);
            jsonObject.put(KEY_JSON_PRICE, price);
            jsonObject.put(KEY_JSON_DESCRIPTION, description);
            jsonObject.put(KEY_JSON_IMAGE_URL, imageUrl);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        NetworkAdapter.Result networkResult = NetworkAdapter.httpRequestPOSTJson(CommonStatics.DATABASE_BASE_URL + ADD_BOOK, jsonObject);
        Result result = new Result();
        if (networkResult.responseCode == NetworkAdapter.Result.INVALID_RESPONSE_CODE) {
            result.result = Result.FAILED;
            return result;
        }

        if (networkResult.responseCode != HttpURLConnection.HTTP_CREATED) {
            result.result = Result.FAILED;
            return result;
        }

        try {
            result.addedBookJson = new JSONObject((String)networkResult.resultObj);
            result.result = Result.SUCCESS;
        } catch (JSONException e) {
            e.printStackTrace();
            result.result = Result.FAILED;
        }

        return result;
    }

    // TODO LOW. better failure reporting
    public class Result {
        public static final int SUCCESS = 0;
        public static final int FAILED = 1;

        public int result;
        public JSONObject addedBookJson;
    }

}
