package com.example.israel.build_week_1_bookr.fragment;


import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.israel.build_week_1_bookr.R;
import com.example.israel.build_week_1_bookr.StaticHelpers;
import com.example.israel.build_week_1_bookr.dao.SessionTokenDAO;
import com.example.israel.build_week_1_bookr.worker_thread.RequestAddBookAsyncTask;

public class AddBookFragment extends Fragment {

    private static final int REQUEST_CONFIRM_ADD_BOOK = 0;

    private View fragmentView;
    private RequestAddBookAsyncTask requestAddBookAsyncTask;

    public static AddBookFragment newInstance() {

        Bundle args = new Bundle();

        AddBookFragment fragment = new AddBookFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public AddBookFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        fragmentView = inflater.inflate(R.layout.fragment_add_book, container, false);

        Button addBookButton = fragmentView.findViewById(R.id.fragment_add_book_button_add_book);
        addBookButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createConfirmationDialog();
            }
        });

        return fragmentView;
    }

    @Override
    public void onDetach() {
        if (requestAddBookAsyncTask != null) {
            requestAddBookAsyncTask.cancel(false);
            requestAddBookAsyncTask = null;
        }

        super.onDetach();
    }

    private void createConfirmationDialog() {
        AddBookConfirmationDialogFragment addBookConfirmationDialogFragment = new AddBookConfirmationDialogFragment();
        addBookConfirmationDialogFragment.setTargetFragment(this, REQUEST_CONFIRM_ADD_BOOK);
        addBookConfirmationDialogFragment.show(getFragmentManager(), null);

    }

    @SuppressLint("StaticFieldLeak")
    private void requestAddBook() {
        if (requestAddBookAsyncTask != null) {
            return;
        }

        // TODO warning this field cannot be empty, except description
        EditText titleEditText = fragmentView.findViewById(R.id.fragment_add_book_edit_text_title);
        String titleStr = titleEditText.getText().toString();
        if (titleStr.length() == 0) {
            titleEditText.setError(getString(R.string.this_field_cannot_be_empty));
            titleEditText.requestFocus();
            return;
        }
        EditText authorEditText = fragmentView.findViewById(R.id.fragment_add_book_edit_text_author);
        String authorStr = authorEditText.getText().toString();
        if (authorStr.length() == 0) {
            authorEditText.setError(getString(R.string.this_field_cannot_be_empty));
            authorEditText.requestFocus();
            return;
        }
        EditText publisherEditText = fragmentView.findViewById(R.id.fragment_add_book_edit_text_publisher);
        String publisherStr = publisherEditText.getText().toString();
        if (publisherStr.length() == 0) {
            publisherEditText.setError(getString(R.string.this_field_cannot_be_empty));
            publisherEditText.requestFocus();
            return;
        }
        EditText priceEditText = fragmentView.findViewById(R.id.fragment_add_book_edit_text_price);
        String priceStr = priceEditText.getText().toString();
        if (priceStr.length() == 0) {
            priceEditText.setError(getString(R.string.this_field_cannot_be_empty));
            priceEditText.requestFocus();
            return;
        }
        EditText descriptionEditText = fragmentView.findViewById(R.id.fragment_add_book_edit_text_description);
        String descriptionStr = descriptionEditText.getText().toString();

        final ProgressBar requestingAddBookProgressBar = fragmentView.findViewById(R.id.fragment_add_book_progress_bar_requesting_add_book);
        requestingAddBookProgressBar.setVisibility(View.VISIBLE);

        requestAddBookAsyncTask = new RequestAddBookAsyncTask(
                SessionTokenDAO.getUserId(getActivity()),
                titleStr, authorStr, publisherStr, Double.parseDouble(priceStr), descriptionStr, "http://lorempixel.com/640/480") { // TODO proper image url
            @Override
            protected void onPostExecute(Result result) {
                super.onPostExecute(result);
                requestingAddBookProgressBar.setVisibility(View.GONE);

                if (isCancelled()) {
                    return;
                }

                requestAddBookAsyncTask = null;

                switch (result.result) {
                    case Result.SUCCESS: {
                        // TODO DEPENDS. add the book to book list manually
                        Toast toast = Toast.makeText(getActivity(), getString(R.string.add_book_success), Toast.LENGTH_SHORT);
                        toast.show();
                    } break;

                    case Result.FAILED: {
                        Toast toast = Toast.makeText(getActivity(), getString(R.string.add_book_failed), Toast.LENGTH_SHORT);
                        toast.show();
                    } break;
                }

            }
        };
        requestAddBookAsyncTask.execute();
    }

    public static class AddBookConfirmationDialogFragment extends DialogFragment {

        @NonNull
        @Override
        public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

            final AddBookFragment addBookFragment = (AddBookFragment) getTargetFragment();

            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setMessage(addBookFragment.getString(R.string.add_book_confirm_message));
            builder.setPositiveButton(addBookFragment.getString(R.string.yes), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    addBookFragment.requestAddBook();
                    StaticHelpers.hideKeyboard(getActivity());
                }
            });

            builder.setNegativeButton(addBookFragment.getString(R.string.no), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });

            return builder.create();
        }
    }
}
