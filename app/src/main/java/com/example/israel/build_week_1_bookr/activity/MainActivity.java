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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        validateSession();
    }

    @SuppressLint("StaticFieldLeak")
    private void validateSession() {
        if (SessionTokenDAO.isSessionValid(this)) {
            // go directly to the book list activity
            ActivityStarter.startBookListActivity(MainActivity.this);
        } else {
            // go directly to the login activity
            ActivityStarter.startLoginActivity(MainActivity.this);
        }

        // prevents the user from coming back here
        finish();

    }
}
