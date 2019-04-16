package com.example.israel.build_week_1_bookr.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.israel.build_week_1_bookr.R;
import com.example.israel.build_week_1_bookr.fragment.LoginFragment;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // TODO animation

        setContentView(R.layout.activity_login);

        // TODO create a hard code here
        LoginFragment.replaceLoginFragment(this, R.id.activity_login_root);
    }
}
