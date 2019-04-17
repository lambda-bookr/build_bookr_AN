package com.example.israel.build_week_1_bookr.fragment;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.israel.build_week_1_bookr.R;
import com.example.israel.build_week_1_bookr.adapter.ReviewListAdapter;
import com.example.israel.build_week_1_bookr.model.Book;
import com.example.israel.build_week_1_bookr.worker_thread.RequestBookReviewsAsyncTask;

public class BookReviewsFragment extends Fragment {

    private static final String ARG_BOOK = "book";

    private View fragmentView;
    private Book book;
    private RequestBookReviewsAsyncTask requestBookReviewsAsyncTask;
    private ReviewListAdapter reviewListAdapter;

    public static BookReviewsFragment newInstance(Book book) {

        Bundle args = new Bundle();
        args.putParcelable(ARG_BOOK, book);

        BookReviewsFragment fragment = new BookReviewsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public BookReviewsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        book = getArguments().getParcelable(ARG_BOOK);

        // Inflate the layout for this fragment
        fragmentView = inflater.inflate(R.layout.fragment_book_reviews, container, false);

        FloatingActionButton addReviewFAB = fragmentView.findViewById(R.id.fragment_book_reviews_fab_add_review);
        addReviewFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createAddBookReviewFragment();
            }
        });

        setupRecyclerView();
        requestReview();

        return fragmentView;
    }

    @Override
    public void onDetach() {
        if (requestBookReviewsAsyncTask != null) {
            requestBookReviewsAsyncTask.cancel(false);
            requestBookReviewsAsyncTask = null;
        }

        super.onDetach();
    }

    private void setupRecyclerView() {
        RecyclerView reviewsRecyclerView = fragmentView.findViewById(R.id.fragment_book_reviews_recycler_view_reviews);
        reviewsRecyclerView.setHasFixedSize(true); // TODO this might change if I implement resizing for reviews view

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        reviewsRecyclerView.setLayoutManager(layoutManager);

        reviewListAdapter = new ReviewListAdapter(this);
        reviewsRecyclerView.setAdapter(reviewListAdapter);
    }

    @SuppressLint("StaticFieldLeak")
    private void requestReview() {
        if (requestBookReviewsAsyncTask != null) {
            return;
        }

        requestBookReviewsAsyncTask = new RequestBookReviewsAsyncTask(book.getId()) {
            @Override
            protected void onPostExecute(Result result) {
                super.onPostExecute(result);

                if (isCancelled()) {
                    return;
                }

                requestBookReviewsAsyncTask = null;

                reviewListAdapter.setReviewList(result.reviews);

            }
        };
        requestBookReviewsAsyncTask.execute();
    }

    private void createAddBookReviewFragment() {
        AddBookReviewFragment addBookReviewFragment = AddBookReviewFragment.newInstance(book.getId());

        // TODO MEDIUM animation
        // TODO VERY LOW. use the creator fragment as the source fragment rather than the activity.
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.activity_book_list_frame_layout, addBookReviewFragment); // refreshes the login fragment
        transaction.addToBackStack(null); // remove this fragment on back press
        transaction.commit();
    }

}
