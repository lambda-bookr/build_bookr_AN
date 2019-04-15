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
import com.example.israel.build_week_1_bookr.worker_thread.LoginAsyncTask;
import com.example.israel.build_week_1_bookr.dao.SessionTokenDAO;

public class LoginActivity extends AppCompatActivity {

    private EditText passwordEditText;
    private LoginAsyncTask loginAsyncTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        passwordEditText = findViewById(R.id.activity_login_edit_text_password);
        passwordEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    loginWithEmailAndPassword();
                    return true;
                }

                return false;
            }
        });

        findViewById(R.id.activity_login_button_log_in).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginWithEmailAndPassword();
            }
        });
    }

    @Override
    protected void onDestroy() {
        if (loginAsyncTask != null) {
            loginAsyncTask.cancel(false);
            loginAsyncTask = null;
        }

        super.onDestroy();
    }

    @SuppressLint("StaticFieldLeak")
    private void loginWithEmailAndPassword() {
        if (loginAsyncTask != null) {
            return;
        }

        final EditText emailEditText = findViewById(R.id.activity_login_edit_text_email);
        passwordEditText = findViewById(R.id.activity_login_edit_text_password);

        final ProgressBar loggingInProgressBar = findViewById(R.id.activity_login_progress_bar_logging_in);
        loggingInProgressBar.setVisibility(View.VISIBLE);

        // try logging in with email and password
        loginAsyncTask = new LoginAsyncTask(
                emailEditText.getText().toString(),
                passwordEditText.getText().toString()) {

            @Override
            protected void onPostExecute(Result result) {
                super.onPostExecute(result);

                if (isCancelled()) {
                    return;
                }
                loginAsyncTask = null;

                loggingInProgressBar.setVisibility(View.GONE);

                switch (result.result) {
                    case Result.SUCCESS: {
                        // store session token
                        SessionTokenDAO.setSessionToken(LoginActivity.this, result.sessionToken);

                        // TODO start book list activity

                        // do not come back here, use log out instead
                        finish();
                    } break;

                    case Result.INVALID_EMAIL: {
                        emailEditText.setError(getString(R.string.invalid_email));
                        emailEditText.requestFocus();
                    } break;

                    case Result.EMAIL_NOT_FOUND: {
                        // TODO if implemented
                    } break;

                    case Result.WRONG_PASSWORD: {
                        passwordEditText.setError(getString(R.string.incorrect_password));
                        passwordEditText.requestFocus();
                    }

                    case Result.TIME_OUT: {
                        // TODO
                    }
                }
            }
        };
        loginAsyncTask.execute();
    }
}
