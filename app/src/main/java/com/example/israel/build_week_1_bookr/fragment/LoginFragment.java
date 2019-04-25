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
import android.widget.Toast;

import com.example.israel.build_week_1_bookr.R;
import com.example.israel.build_week_1_bookr.StaticHelpers;
import com.example.israel.build_week_1_bookr.controller.ActivityStarter;
import com.example.israel.build_week_1_bookr.dao.BookrAPIDAO;
import com.example.israel.build_week_1_bookr.dao.SessionDAO;
import com.example.israel.build_week_1_bookr.json_object.LoginInfo;
import com.example.israel.build_week_1_bookr.json_object.LoginReply;
import com.example.israel.build_week_1_bookr.model.UserInfo;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

// TODO MEDIUM toast
public class LoginFragment extends Fragment {

    private View fragmentView;
    private EditText usernameEditText;
    private EditText passwordEditText;
    private Call<LoginReply> loginCall;
    private Call<UserInfo> getUserInfoCall;
    private ProgressBar loggingInProgressBar;

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
        if (loginCall != null) {
            loginCall.cancel();
            loginCall = null;
        }

        super.onDetach();
    }

    @SuppressLint("StaticFieldLeak")
    private void login() {
        StaticHelpers.hideKeyboard(getActivity());

        if (loginCall != null) {
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

        loggingInProgressBar = fragmentView.findViewById(R.id.fragment_login_progress_bar_logging_in);
        loggingInProgressBar.setVisibility(View.VISIBLE);

        LoginInfo loginInfo = new LoginInfo(usernameStr, passwordStr);

        loginCall = BookrAPIDAO.apiService.login(loginInfo);
        loginCall.enqueue(new Callback<LoginReply>() {
            @Override
            public void onResponse(Call<LoginReply> call, Response<LoginReply> response) {
                onLoginCallFinished(false, response);
            }

            @Override
            public void onFailure(Call<LoginReply> call, Throwable t) {
                onLoginCallFinished(true, null);
            }
        });

    }

    private void onLoginCallFinished(boolean isFailure, final Response<LoginReply> response) {
        if (loginCall.isCanceled() || getActivity() == null) {
            return;
        }

        loginCall = null;

        if (isFailure || !response.isSuccessful()) {
            loggingInProgressBar.setVisibility(View.INVISIBLE);
            passwordEditText.setError(getString(R.string.incorrect_password));
            passwordEditText.requestFocus();
        } else {
            // store session token
            SessionDAO.setSessionToken(getActivity(), response.body().token);

            getUserInfoCall = BookrAPIDAO.apiService.getUserInfo(response.body().token, response.body().userId);
            getUserInfoCall.enqueue(new Callback<UserInfo>() {
                @Override
                public void onResponse(Call<UserInfo> call, Response<UserInfo> response) {
                    onGetUserInfoCallCallFinished(false, response);
                }

                @Override
                public void onFailure(Call<UserInfo> call, Throwable t) {
                    onGetUserInfoCallCallFinished(false, null);
                }
            });
        }
    }

    private void onGetUserInfoCallCallFinished(boolean isFailure, Response<UserInfo> response) {
        if (getUserInfoCall.isCanceled() || getActivity() == null) {
            return;
        }

        getUserInfoCall = null;
        loggingInProgressBar.setVisibility(View.INVISIBLE);

        if (isFailure || !response.isSuccessful()) {
            SessionDAO.isSessionValid(getActivity());
            Toast toast = Toast.makeText(getActivity(), getString(R.string.failed_to_login), Toast.LENGTH_SHORT);
            toast.show();
        } else { // success
            SessionDAO.setUserInfo(getActivity(), response.body());

            ActivityStarter.startBookListActivity(getActivity());

            // do not come back here, use log out instead
            getActivity().finish();
        }

    }

}
