package com.example.israel.build_week_1_bookr.fragment;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.israel.build_week_1_bookr.R;
import com.example.israel.build_week_1_bookr.StaticHelpers;
import com.example.israel.build_week_1_bookr.controller.ActivityStarter;
import com.example.israel.build_week_1_bookr.dao.SessionTokenDAO;
import com.example.israel.build_week_1_bookr.worker_thread.LoginAsyncTask;

// TODO MEDIUM toast
public class LoginFragment extends Fragment {

    private View fragmentView;
    private EditText usernameEditText;
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

        usernameEditText = fragmentView.findViewById(R.id.fragment_login_edit_text_username);
        usernameEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    passwordEditText.requestFocus();
                    return true;
                }

                return false;
            }
        });

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

        fragmentView.findViewById(R.id.fragment_login_text_view_register).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RegisterFragment.createRegisterFragment(getActivity(), R.id.activity_login_root);
            }
        });

        return fragmentView;
    }

    @Override
    public void onDetach() {
        if (loginAsyncTask != null) {
            loginAsyncTask.cancel(false);
            loginAsyncTask = null;
        }

        super.onDetach();
    }

    @SuppressLint("StaticFieldLeak")
    private void login() {
        StaticHelpers.hideKeyboard(getActivity());

        if (loginAsyncTask != null) {
            return;
        }

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

                loggingInProgressBar.setVisibility(View.INVISIBLE);

                switch (result.result) {
                    case Result.SUCCESS: {
                        // store session token
                        SessionTokenDAO.setSessionToken(getActivity(), result.sessionToken);
                        SessionTokenDAO.setUserId(getActivity(), result.userId);

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
