package com.isra.israel.build_week_1_bookr.activity;

import android.annotation.SuppressLint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.isra.israel.build_week_1_bookr.R;
import com.isra.israel.build_week_1_bookr.controller.ActivityStarter;
import com.isra.israel.build_week_1_bookr.dao.SessionDAO;

// TODO VERY LOW. isTablet
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        validateSession();
    }

    @SuppressLint("StaticFieldLeak")
    private void validateSession() {
        if (SessionDAO.isSessionValid(this)) {
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
