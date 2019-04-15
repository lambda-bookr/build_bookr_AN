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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.israel.build_week_1_bookr.R;
import com.example.israel.build_week_1_bookr.activity.LoginActivity;
import com.example.israel.build_week_1_bookr.controller.ActivityStarter;
import com.example.israel.build_week_1_bookr.dao.SessionTokenDAO;
import com.example.israel.build_week_1_bookr.worker_thread.LoginAsyncTask;

public class LoginFragment extends Fragment {

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

        final View view = inflater.inflate(R.layout.fragment_login, container, false);

        passwordEditText = view.findViewById(R.id.fragment_login_edit_text_password);
        passwordEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    loginWithEmailAndPassword(view);
                    return true;
                }

                return false;
            }
        });

        view.findViewById(R.id.fragment_login_button_log_in).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginWithEmailAndPassword(view);
            }
        });

        view.findViewById(R.id.fragment_login_button_register).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RegisterFragment.replaceRegisterFragment(getActivity(), R.id.activity_login_root);
            }
        });

        return view;
    }

    public static void replaceLoginFragment(FragmentActivity fragmentActivity, int i) {
        LoginFragment loginFragment = LoginFragment.newInstance();

        FragmentTransaction transaction = fragmentActivity.getSupportFragmentManager().beginTransaction();
        transaction.replace(i, loginFragment);
        // login fragment should always be underneath
        transaction.commit();

    }

    @SuppressLint("StaticFieldLeak")
    private void loginWithEmailAndPassword(View v) {
        if (loginAsyncTask != null) {
            return;
        }

        final EditText emailEditText = v.findViewById(R.id.fragment_login_edit_text_email);

        final ProgressBar loggingInProgressBar = v.findViewById(R.id.fragment_login_progress_bar_logging_in);
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
                        SessionTokenDAO.setSessionToken(getActivity(), result.sessionToken);

                        ActivityStarter.startBookListActivity(getActivity());

                        // do not come back here, use log out instead
                        getActivity().finish();
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
