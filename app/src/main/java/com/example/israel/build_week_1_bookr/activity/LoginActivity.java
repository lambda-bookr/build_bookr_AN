package com.example.israel.build_week_1_bookr.activity;

import android.annotation.SuppressLint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.israel.build_week_1_bookr.R;
import com.example.israel.build_week_1_bookr.controller.ActivityStarter;
import com.example.israel.build_week_1_bookr.fragment.LoginFragment;
import com.example.israel.build_week_1_bookr.fragment.RegisterFragment;
import com.example.israel.build_week_1_bookr.worker_thread.LoginAsyncTask;
import com.example.israel.build_week_1_bookr.dao.SessionTokenDAO;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        LoginFragment.replaceLoginFragment(this, R.id.activity_login_root);
    }
}
