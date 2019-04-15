package com.example.israel.build_week_1_bookr.activity;

import android.annotation.SuppressLint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.israel.build_week_1_bookr.R;
import com.example.israel.build_week_1_bookr.controller.ActivityStarter;
import com.example.israel.build_week_1_bookr.dao.SessionTokenDAO;
import com.example.israel.build_week_1_bookr.worker_thread.ValidateSessionTokenAsyncTask;

// TODO isTablet
// TODO DrawerLayout for navigation
public class MainActivity extends AppCompatActivity {

    ValidateSessionTokenAsyncTask validateSessionTokenAsyncTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        validateSession();
    }

    @Override
    protected void onDestroy() {
        if (validateSessionTokenAsyncTask != null) {
            validateSessionTokenAsyncTask.cancel(false);
            validateSessionTokenAsyncTask = null;
        }

        super.onDestroy();
    }

    @SuppressLint("StaticFieldLeak")
    private void validateSession() {
        if (validateSessionTokenAsyncTask != null) {
            return;
        }

        validateSessionTokenAsyncTask = new ValidateSessionTokenAsyncTask(SessionTokenDAO.getSessionToken(this)) {
            @Override
            protected void onPostExecute(Boolean isValid) {
                super.onPostExecute(isValid);

                if (isCancelled()) {
                    return;
                }

                validateSessionTokenAsyncTask = null;

                if (isValid) {
                    // go directly to the book list activity
                    ActivityStarter.startBookListActivity(MainActivity.this);
                } else {
                    // go directly to the login activity
                    ActivityStarter.startLoginActivity(MainActivity.this);
                }

                // prevents the user from coming back here
                finish();
            }
        };
        validateSessionTokenAsyncTask.execute();

    }
}
