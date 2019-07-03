package com.isra.israel.build_week_1_bookr.activity;

import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.isra.israel.build_week_1_bookr.R;
import com.isra.israel.build_week_1_bookr.fragment.LoginFragment;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);

        // TODO VERY LOW. hard code this to xml
        addLoginFragment();

    }

    private void addLoginFragment() {
        LoginFragment loginFragment = LoginFragment.newInstance();

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.activity_login_root, loginFragment);
        // login fragment should always be underneath
        transaction.commit();
    }
}
