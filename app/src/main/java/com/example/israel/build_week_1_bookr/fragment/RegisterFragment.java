package com.example.israel.build_week_1_bookr.fragment;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.transition.Slide;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.israel.build_week_1_bookr.R;
import com.example.israel.build_week_1_bookr.StaticHelpers;
import com.example.israel.build_week_1_bookr.worker_thread.RegisterAsyncTask;

// TODO HIGH toast
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

        fragmentView.findViewById(R.id.fragment_register_constraint_layout_root).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // blocker
            }
        });

        return fragmentView;
    }

    @Override
    public void onDetach() {
        if (registerAsyncTask != null) {
            registerAsyncTask.cancel(false);
            registerAsyncTask = null;
        }

        super.onDetach();
    }

    public static void createRegisterFragment(FragmentActivity fragmentActivity, int i) {
        RegisterFragment registerFragment = RegisterFragment.newInstance();

        registerFragment.setEnterTransition(new Slide());

        FragmentTransaction transaction = fragmentActivity.getSupportFragmentManager().beginTransaction();
        transaction.add(i, registerFragment);
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

        registerAsyncTask = new RegisterAsyncTask(usernameStr, passwordStr, firstNameStr, lastNameStr) {
            @Override
            protected void onPostExecute(String result) {
                super.onPostExecute(result);
                progressBar.setVisibility(View.GONE);

                if (isCancelled() || getActivity() == null) {
                    return;
                }

                registerAsyncTask = null;

                if (result != null) {
                    Toast toast = Toast.makeText(getActivity(), getString(R.string.registration_successful), Toast.LENGTH_LONG);
                    toast.show();

                    StaticHelpers.hideKeyboard(getActivity());
                } else {
                    usernameEditText.setError(getString(R.string.username_taken));
                    usernameEditText.requestFocus();
                }
            }
        };
        registerAsyncTask.execute();
    }

}
