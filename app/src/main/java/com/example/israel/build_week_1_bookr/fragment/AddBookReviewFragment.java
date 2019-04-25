package com.example.israel.build_week_1_bookr.fragment;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.example.israel.build_week_1_bookr.R;
import com.example.israel.build_week_1_bookr.StaticHelpers;
import com.example.israel.build_week_1_bookr.custom_view.RatingView;
import com.example.israel.build_week_1_bookr.dao.BookrAPIDAO;
import com.example.israel.build_week_1_bookr.dao.SessionDAO;
import com.example.israel.build_week_1_bookr.json_object.OutReview;
import com.example.israel.build_week_1_bookr.model.Review;
import com.example.israel.build_week_1_bookr.model.UserInfo;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddBookReviewFragment extends Fragment {

    private static final String ARG_BOOK_ID = "book_id";

    private View fragmentView;
    private int bookId;
    private Call<Review> addBookReviewCall;

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
        if (addBookReviewCall != null) {
            addBookReviewCall.cancel();
            addBookReviewCall = null;
        }

        super.onDetach();
    }

    @SuppressLint("StaticFieldLeak")
    private void requestAddReview() {
        if (addBookReviewCall != null) {
            return;
        }

        RatingView ratingView = fragmentView.findViewById(R.id.fragment_add_book_review_rating_bar);
        EditText reviewEditText = fragmentView.findViewById(R.id.fragment_add_book_review_edit_text);
        String reviewStr = reviewEditText.getText().toString();
        if (reviewStr.length() == 0) {
            reviewEditText.setError(getString(R.string.this_field_cannot_be_empty));
            reviewEditText.requestFocus();
            return;
        }

        UserInfo userInfo = SessionDAO.getUserInfo(getActivity());
        int userId = userInfo.getId();

        OutReview outReview = new OutReview(bookId, userId, ratingView.getRating(), reviewStr);

        addBookReviewCall = BookrAPIDAO.apiService.addReview(SessionDAO.getSessionToken(getActivity()), outReview);
        addBookReviewCall.enqueue(new Callback<Review>() {
            @Override
            public void onResponse(Call<Review> call, Response<Review> response) {
                if (call.isCanceled() || getActivity() == null) {
                    return;
                }

                addBookReviewCall = null;

                if (response.isSuccessful()) {
                    ((BookReviewsFragment)getTargetFragment()).addReview(response.body()); // TODO interface

                    Toast toast = Toast.makeText(getActivity(), getString(R.string.add_review_success), Toast.LENGTH_SHORT);
                    toast.show();

                    StaticHelpers.hideKeyboard(getActivity());
                } else {
                    Toast toast = Toast.makeText(getActivity(), getString(R.string.add_review_failed), Toast.LENGTH_SHORT);
                    toast.show();
                }

                getActivity().getSupportFragmentManager().popBackStack();
            }

            @Override
            public void onFailure(Call<Review> call, Throwable t) {
                if (call.isCanceled() || getActivity() == null) {
                    return;
                }

                addBookReviewCall = null;

                Toast toast = Toast.makeText(getActivity(), getString(R.string.add_review_failed), Toast.LENGTH_SHORT);
                toast.show();

                getActivity().getSupportFragmentManager().popBackStack();
            }
        });
    }

}
