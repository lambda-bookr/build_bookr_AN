package com.example.israel.build_week_1_bookr.worker_thread;

import android.os.AsyncTask;

import com.example.israel.build_week_1_bookr.CommonStatics;
import com.example.israel.build_week_1_bookr.network.NetworkAdapter;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.HttpURLConnection;

public class RequestDeleteBookAsyncTask extends AsyncTask<Void, Void, RequestDeleteBookAsyncTask.Result> {

    private static final String BOOKS = "api/books/";

    public RequestDeleteBookAsyncTask(int bookId) {
        this.bookId = bookId;
    }

    private int bookId;

    @Override
    protected Result doInBackground(Void... voids) {

        Result result = new Result();

        NetworkAdapter.Result networkResult = NetworkAdapter.httpRequestDEL(CommonStatics.DATABASE_BASE_URL + BOOKS + Integer.toString(bookId));
        if (networkResult.responseCode == NetworkAdapter.Result.INVALID_RESPONSE_CODE) {
            result.result = Result.FAILED;
            return result;
        }

        if (networkResult.responseCode == HttpURLConnection.HTTP_OK) {
            try {
                result.deletedBookJson = new JSONObject((String)networkResult.resultObj);
                result.result = Result.SUCCESS;
            } catch (JSONException e) {
                e.printStackTrace();
                result.result = Result.FAILED;
            }
        }

        result.result = Result.SUCCESS;
        return result;
    }

    public class Result {
        public static final int SUCCESS = 0;
        public static final int FAILED = 1;

        public int result;
        public JSONObject deletedBookJson;
    }

}
