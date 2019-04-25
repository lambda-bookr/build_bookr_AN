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
import android.widget.Toast;

import com.example.israel.build_week_1_bookr.R;
import com.example.israel.build_week_1_bookr.StaticHelpers;
import com.example.israel.build_week_1_bookr.dao.BookrAPIDAO;
import com.example.israel.build_week_1_bookr.json_object.RegistrationInfo;
import com.example.israel.build_week_1_bookr.json_object.RegistrationReply;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

// TODO HIGH toast
public class RegisterFragment extends Fragment {

    private Call<RegistrationReply> registerCall;
    private View fragmentView;
    private EditText usernameEditText;
    private ProgressBar registeringProgressBar;

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

        usernameEditText = fragmentView.findViewById(R.id.fragment_register_edit_text_username);
        registeringProgressBar = fragmentView.findViewById(R.id.fragment_register_progress_bar_registering);

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
        if (registerCall != null) {
            registerCall.cancel();
            registerCall = null;
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
        if (registerCall != null) {
            return;
        }

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

        registeringProgressBar.setVisibility(View.VISIBLE);

        RegistrationInfo registrationInfo = new RegistrationInfo(usernameStr, passwordStr, firstNameStr, lastNameStr);

        registerCall = BookrAPIDAO.apiService.register(registrationInfo);
        registerCall.enqueue(new Callback<RegistrationReply>() {
            @Override
            public void onResponse(Call<RegistrationReply> call, Response<RegistrationReply> response) {
                onRegisterCallFinished(false, response);
            }

            @Override
            public void onFailure(Call<RegistrationReply> call, Throwable t) {
                onRegisterCallFinished(true, null);
            }
        });

    }

    private void onRegisterCallFinished(boolean isFailure, Response<RegistrationReply> response) {
        if (registerCall.isCanceled() || getActivity() == null) {
            return;
        }

        registeringProgressBar.setVisibility(View.GONE);
        registerCall = null;

        if (isFailure || !response.isSuccessful()) {
            usernameEditText.setError(getString(R.string.username_taken));
            usernameEditText.requestFocus();
        } else {
            Toast toast = Toast.makeText(getActivity(), getString(R.string.registration_successful), Toast.LENGTH_LONG);
            toast.show();

            StaticHelpers.hideKeyboard(getActivity());
        }
    }

}
