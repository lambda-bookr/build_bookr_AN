package com.example.israel.build_week_1_bookr.fragment;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.israel.build_week_1_bookr.R;
import com.example.israel.build_week_1_bookr.worker_thread.RegisterAsyncTask;

public class RegisterFragment extends Fragment {

    private RegisterAsyncTask registerAsyncTask;
    private View fragmentView;

    public static RegisterFragment newInstance() {

        Bundle args = new Bundle();

        RegisterFragment fragment = new RegisterFragment();
        fragment.setArguments(args);
        return fragment;
    }


    public RegisterFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        fragmentView = inflater.inflate(R.layout.fragment_register, container, false);

        fragmentView.findViewById(R.id.fragment_register_button_confirm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register();
            }
        });

        return fragmentView;
    }

    public static void replaceRegisterFragment(FragmentActivity fragmentActivity, int i) {
        RegisterFragment registerFragment = RegisterFragment.newInstance();

        // TODO animation
        FragmentTransaction transaction = fragmentActivity.getSupportFragmentManager().beginTransaction();
        transaction.replace(i, registerFragment);
        transaction.addToBackStack(null); // remove this fragment on back press
        transaction.commit();
    }

    @SuppressLint("StaticFieldLeak")
    private void register() {
        if (registerAsyncTask != null) {
            return;
        }

        final EditText usernameEditText = fragmentView.findViewById(R.id.fragment_register_edit_text_username);
        String usernameStr = usernameEditText.getText().toString();
        if (usernameStr.length() == 0) {
            usernameEditText.setError(getString(R.string.this_field_cannot_be_empty));
            usernameEditText.requestFocus();
            return;
        }
        EditText passwordEditText = fragmentView.findViewById(R.id.fragment_register_edit_text_password);
        String passwordStr = passwordEditText.getText().toString();
        if (passwordStr.length() == 0) {
            passwordEditText.setError(getString(R.string.this_field_cannot_be_empty));
            passwordEditText.requestFocus();
            return;
        }
        EditText firstNameEditText = fragmentView.findViewById(R.id.fragment_register_edit_text_first_name);
        String firstNameStr = firstNameEditText.getText().toString();
        if (firstNameStr.length() == 0) {
            firstNameEditText.setError(getString(R.string.this_field_cannot_be_empty));
            firstNameEditText.requestFocus();
            return;
        }
        EditText lastNameEditText = fragmentView.findViewById(R.id.fragment_register_edit_text_last_name);
        String lastNameStr = lastNameEditText.getText().toString();
        if (lastNameStr.length() == 0) {
            lastNameEditText.setError(getString(R.string.this_field_cannot_be_empty));
            lastNameEditText.requestFocus();
            return;
        }

        final ProgressBar progressBar = fragmentView.findViewById(R.id.fragment_register_progress_bar_registering);
        progressBar.setVisibility(View.VISIBLE);

        final TextView resultTextView = fragmentView.findViewById(R.id.fragment_register_text_view_result);
        resultTextView.setVisibility(View.INVISIBLE);

        registerAsyncTask = new RegisterAsyncTask(usernameStr, passwordStr, firstNameStr, lastNameStr) {
            @Override
            protected void onPostExecute(Result result) {
                super.onPostExecute(result);
                progressBar.setVisibility(View.GONE);

                if (isCancelled()) {
                    return;
                }

                registerAsyncTask = null;

                switch (result.code) {
                    case Result.SUCCESS: {
                        resultTextView.setVisibility(View.VISIBLE);
                        // TODO go directly to BookListActivity
                    } break;

                    case Result.USERNAME_TAKEN: {
                        usernameEditText.setError(getString(R.string.username_taken));
                        usernameEditText.requestFocus();
                    } break;
                }

            }
        };
        registerAsyncTask.execute();
    }

}
