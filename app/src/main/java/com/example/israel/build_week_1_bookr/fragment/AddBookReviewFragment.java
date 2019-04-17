package com.example.israel.build_week_1_bookr.fragment;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

import com.example.israel.build_week_1_bookr.R;
import com.example.israel.build_week_1_bookr.dao.SessionTokenDAO;
import com.example.israel.build_week_1_bookr.worker_thread.RequestAddReviewAsyncTask;
import com.example.israel.build_week_1_bookr.worker_thread.RequestRemoveReviewAsyncTask;

public class AddBookReviewFragment extends Fragment {

    private static final String ARG_BOOK_ID = "book_id";

    private View fragmentView;
    private int bookId;
    private RequestAddReviewAsyncTask requestAddReviewAsyncTask;

    public static AddBookReviewFragment newInstance(int bookId) {

        Bundle args = new Bundle();
        args.putInt(ARG_BOOK_ID, bookId);

        AddBookReviewFragment fragment = new AddBookReviewFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public AddBookReviewFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        bookId = getArguments().getInt(ARG_BOOK_ID);

        // Inflate the layout for this fragment
        fragmentView = inflater.inflate(R.layout.fragment_add_book_review, container, false);

        fragmentView.findViewById(R.id.fragment_add_book_review_constraint_layout_root).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // block the background
            }
        });

        fragmentView.findViewById(R.id.fragment_add_book_review_button_add_review).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // add review
                requestAddReview();
            }
        });

        fragmentView.findViewById(R.id.fragment_add_book_review_button_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // remove this fragment
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });

        return fragmentView;
    }

    @Override
    public void onDetach() {
        if (requestAddReviewAsyncTask != null) {
            requestAddReviewAsyncTask.cancel(false);
            requestAddReviewAsyncTask = null;
        }

        super.onDetach();
    }

    @SuppressLint("StaticFieldLeak")
    private void requestAddReview() {
        if (requestAddReviewAsyncTask != null) {
            return;
        }

        // TODO CRITICAL progress bar

        RatingBar ratingBar = fragmentView.findViewById(R.id.fragment_add_book_review_rating_bar);
        EditText reviewEditText = fragmentView.findViewById(R.id.fragment_add_book_review_edit_text);
        String reviewStr = reviewEditText.getText().toString();
        if (reviewStr.length() == 0) {
            reviewEditText.setError(getString(R.string.this_field_cannot_be_empty));
            reviewEditText.requestFocus();
            return;
        }

        int userId = SessionTokenDAO.getUserId(getActivity());

        requestAddReviewAsyncTask = new RequestAddReviewAsyncTask(bookId, userId, (int)ratingBar.getRating(), reviewStr) {

            @Override
            protected void onPostExecute(Boolean aBoolean) {
                super.onPostExecute(aBoolean);

                if (isCancelled()) {
                    return;
                }

                requestAddReviewAsyncTask = null;

                if (aBoolean) {
                    // TODO HIGH add to the review list

                    Toast toast = Toast.makeText(getActivity(), getString(R.string.add_review_success), Toast.LENGTH_SHORT);
                    toast.show();
                } else {
                    Toast toast = Toast.makeText(getActivity(), getString(R.string.add_review_failed), Toast.LENGTH_SHORT);
                    toast.show();
                }

                getActivity().getSupportFragmentManager().popBackStack();
            }
        };
        requestAddReviewAsyncTask.execute();
    }

    private void requestRemoveReview() {
        // TODO CRITICAL progress bar

    }

}
