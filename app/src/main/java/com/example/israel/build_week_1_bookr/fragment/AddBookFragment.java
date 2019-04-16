package com.example.israel.build_week_1_bookr.fragment;


import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.israel.build_week_1_bookr.R;
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

    private void createConfirmationDialog() {
        AddBookConfirmationDialogFragment addBookConfirmationDialogFragment = new AddBookConfirmationDialogFragment();
        addBookConfirmationDialogFragment.setTargetFragment(this, REQUEST_CONFIRM_ADD_BOOK);
        addBookConfirmationDialogFragment.show(getFragmentManager(), null);

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

    @SuppressLint("StaticFieldLeak")
    private void requestAddBook() {
        if (requestAddBookAsyncTask != null) {
            return;
        }

        // TODO this field cannot be empty, except description
        EditText titleEditText = fragmentView.findViewById(R.id.fragment_add_book_edit_text_title);
        String titleStr = titleEditText.getText().toString();
        EditText authorEditText = fragmentView.findViewById(R.id.fragment_add_book_edit_text_author);
        String authorStr = authorEditText.getText().toString();
        EditText publisherEditText = fragmentView.findViewById(R.id.fragment_book_details_text_view_publisher);
        String publisherStr = publisherEditText.getText().toString();
        EditText priceEditText = fragmentView.findViewById(R.id.fragment_add_book_edit_text_price);
        String priceStr = priceEditText.getText().toString();
        EditText descriptionEditText = fragmentView.findViewById(R.id.fragment_add_book_edit_text_description);
        String descriptionStr = descriptionEditText.getText().toString();


        // TODO progress bar

        requestAddBookAsyncTask = new RequestAddBookAsyncTask(
                SessionTokenDAO.getUserId(getActivity()),
                titleStr, authorStr, publisherStr, Double.parseDouble(priceStr), descriptionStr, "") {
            @Override
            protected void onPostExecute(Result result) {
                super.onPostExecute(result);

                if (isCancelled()) {
                    return;
                }

                requestAddBookAsyncTask = null;

                switch (result.result) {
                    case Result.SUCCESS: {
                        // TODO DEPENDS. add book to the list

                        // TODO toast
                    } break;

                    case Result.FAILED: {
                        // TODO toast
                    } break;
                }

            }
        };
        requestAddBookAsyncTask.execute();

    }
}
