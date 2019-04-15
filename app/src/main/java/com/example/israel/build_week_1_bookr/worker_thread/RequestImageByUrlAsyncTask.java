package com.example.israel.build_week_1_bookr.worker_thread;

import android.graphics.Bitmap;
import android.os.AsyncTask;

import com.example.israel.build_week_1_bookr.network.NetworkAdapter;

public class RequestImageByUrlAsyncTask extends AsyncTask<Void, Void, Bitmap> {

    public RequestImageByUrlAsyncTask(String url) {
        this.url = url;
    }

    private String url;

    @Override
    protected Bitmap doInBackground(Void... voids) {
        if (this.url == null) {
            return null;
        }

        return NetworkAdapter.httpImageRequestGET(url);
    }
}
