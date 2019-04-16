package com.example.israel.build_week_1_bookr.fragment;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.israel.build_week_1_bookr.R;
import com.example.israel.build_week_1_bookr.controller.ActivityStarter;
import com.example.israel.build_week_1_bookr.dao.SessionTokenDAO;
import com.example.israel.build_week_1_bookr.worker_thread.LoginAsyncTask;

public class LoginFragment extends Fragment {

    private View fragmentView;
    private EditText passwordEditText;
    private LoginAsyncTask loginAsyncTask;

    public static LoginFragment newInstance() {

        Bundle args = new Bundle();

        LoginFragment fragment = new LoginFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public LoginFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        fragmentView = inflater.inflate(R.layout.fragment_login, container, false);

        passwordEditText = fragmentView.findViewById(R.id.fragment_login_edit_text_password);
        passwordEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    login();
                    return true;
                }

                return false;
            }
        });

        fragmentView.findViewById(R.id.fragment_login_button_log_in).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });

        fragmentView.findViewById(R.id.fragment_login_button_register).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RegisterFragment.replaceRegisterFragment(getActivity(), R.id.activity_login_root);
            }
        });

        return fragmentView;
    }

    public static void replaceLoginFragment(FragmentActivity fragmentActivity, int i) {
        LoginFragment loginFragment = LoginFragment.newInstance();

        FragmentTransaction transaction = fragmentActivity.getSupportFragmentManager().beginTransaction();
        transaction.replace(i, loginFragment);
        // login fragment should always be underneath
        transaction.commit();

    }

    @SuppressLint("StaticFieldLeak")
    private void login() {
        if (loginAsyncTask != null) {
            return;
        }

        final EditText usernameEditText = fragmentView.findViewById(R.id.fragment_login_edit_text_username);
        String usernameStr = usernameEditText.getText().toString();
        if (usernameStr.length() == 0) {
            usernameEditText.setError(getString(R.string.this_field_cannot_be_empty));
            usernameEditText.requestFocus();
            return;
        }

        String passwordStr = passwordEditText.getText().toString();
        if (passwordStr.length() == 0) {
            passwordEditText.setError(getString(R.string.this_field_cannot_be_empty));
            passwordEditText.requestFocus();
            return;
        }

        final ProgressBar loggingInProgressBar = fragmentView.findViewById(R.id.fragment_login_progress_bar_logging_in);
        loggingInProgressBar.setVisibility(View.VISIBLE);

        // try logging in with username and password
        loginAsyncTask = new LoginAsyncTask(usernameStr, passwordStr) {

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
                        SessionTokenDAO.setSessionToken(getActivity(), result.sessionToken);

                        ActivityStarter.startBookListActivity(getActivity());

                        // do not come back here, use log out instead
                        getActivity().finish();
                    } break;

                    case Result.WRONG_PASSWORD: {
                        passwordEditText.setError(getString(R.string.incorrect_password));
                        passwordEditText.requestFocus();
                    }
                }
            }
        };
        loginAsyncTask.execute();
    }

}
