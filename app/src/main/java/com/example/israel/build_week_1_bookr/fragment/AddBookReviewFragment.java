package com.example.israel.build_week_1_bookr.fragment;


import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.israel.build_week_1_bookr.R;

public class AddBookReviewFragment extends Fragment {

    private View fragmentView;

    public static AddBookReviewFragment newInstance() {

        Bundle args = new Bundle();

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

            }
        });

        fragmentView.findViewById(R.id.fragment_add_book_review_button_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //remove this fragment
            }
        });

        return fragmentView;
    }

}
