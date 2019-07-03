package com.isra.israel.build_week_1_bookr.fragment;


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
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.isra.israel.build_week_1_bookr.R;
import com.isra.israel.build_week_1_bookr.adapter.ReviewListAdapter;
import com.isra.israel.build_week_1_bookr.dao.BookrAPIDAO;
import com.isra.israel.build_week_1_bookr.dao.SessionDAO;
import com.isra.israel.build_week_1_bookr.model.Book;
import com.isra.israel.build_week_1_bookr.model.Review;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BookReviewsFragment extends Fragment {

    private static final int REQUEST_CONFIRM_DELETE_REVIEW = 0;

    private static final String ARG_BOOK = "book";

    private View fragmentView;
    private Book book;
    private Call<ArrayList<Review>> getReviewsCall;
    private Call<Review> deleteReviewCall;
    private ReviewListAdapter reviewListAdapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    private int removeReviewId;
    private int removeReviewAdapterPosition;

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

        fragmentView.findViewById(R.id.fragment_book_reviews_constraint_layout_root).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // blocker
            }
        });

        swipeRefreshLayout = fragmentView.findViewById(R.id.fragment_book_reviews_swipe_refresh_layout_reviews);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                requestReviews();
            }
        });

        FloatingActionButton addReviewFAB = fragmentView.findViewById(R.id.fragment_book_reviews_fab_add_review);
        addReviewFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createAddBookReviewFragment();
            }
        });

        setupRecyclerView();
        swipeRefreshLayout.setRefreshing(true);
        requestReviews();

        return fragmentView;
    }

    @Override
    public void onDetach() {
        if (getReviewsCall != null) {
            getReviewsCall.cancel();
            getReviewsCall = null;
        }

        if (deleteReviewCall != null) {
            deleteReviewCall.cancel();
            deleteReviewCall = null;
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

    public void addReview(Review review) {
        reviewListAdapter.addReview(review);
    }

    @SuppressLint("StaticFieldLeak")
    private void requestReviews() {
        if (getReviewsCall != null) {
            return;
        }

        getReviewsCall = BookrAPIDAO.apiService.getReviews(SessionDAO.getSessionToken(getActivity()), book.getId());
        getReviewsCall.enqueue(new Callback<ArrayList<Review>>() {
            @Override
            public void onResponse(Call<ArrayList<Review>> call, Response<ArrayList<Review>> response) {
                onGetReviewsCallFinished(response);
            }

            @Override
            public void onFailure(Call<ArrayList<Review>> call, Throwable t) {
                onGetReviewsCallFinished(null);
            }
        });
    }

    private void onGetReviewsCallFinished(Response<ArrayList<Review>> response) {
        if (getReviewsCall.isCanceled()) {
            return;
        }

        getReviewsCall = null;
        swipeRefreshLayout.setRefreshing(false);

        if (response != null && response.isSuccessful()) {
            reviewListAdapter.setReviewList(response.body());
        }
    }

    private void createAddBookReviewFragment() {
        AddBookReviewFragment addBookReviewFragment = AddBookReviewFragment.newInstance(book.getId());
        // TODO FUTURE USE. use this fragment as the source fragment for the transaction rather than the activity. so we can use get parent fragment on AddBookReview fragment rather than getTargetFragment
        addBookReviewFragment.setTargetFragment(this, 0);

        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.activity_book_list_frame_layout, addBookReviewFragment); // refreshes the login fragment
        transaction.addToBackStack(null); // remove this fragment on back press
        transaction.commit();
    }

    @SuppressLint("StaticFieldLeak")
    private void requestRemoveReview() {
        if (deleteReviewCall != null) {
            return;
        }

        deleteReviewCall = BookrAPIDAO.apiService.deleteReview(SessionDAO.getSessionToken(getActivity()), removeReviewId);
        deleteReviewCall.enqueue(new Callback<Review>() {
            @Override
            public void onResponse(Call<Review> call, Response<Review> response) {
                onDeleteReviewCallFinished(response);
            }

            @Override
            public void onFailure(Call<Review> call, Throwable t) {
                onDeleteReviewCallFinished(null);
            }
        });
    }

    private void onDeleteReviewCallFinished(Response<Review> response) {
        if (deleteReviewCall.isCanceled() || getActivity() == null) {
            return;
        }

        deleteReviewCall = null;

        if (response != null && response.isSuccessful()) {
            reviewListAdapter.removeReview(removeReviewAdapterPosition);

            Toast toast = Toast.makeText(getActivity(), getString(R.string.delete_review_success), Toast.LENGTH_SHORT);
            toast.show();
        } else {
            Toast toast = Toast.makeText(getActivity(), getString(R.string.delete_review_failed), Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    public void createReviewPopupMenu(View v, final Review review, int reviewAdapterPosition) {
        removeReviewId = review.getId();
        this.removeReviewAdapterPosition = reviewAdapterPosition;
        PopupMenu popupMenu = new PopupMenu(getActivity(), v);
        popupMenu.getMenuInflater().inflate(R.menu.fragment_book_reviews_review_menu, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int id = item.getItemId();
                switch (id) {
                    case R.id.menu_book_reviews_review_delete: {
                        createRemoveReviewConfirmationDialogFragment();
                    } break;
                }

                return true;
            }
        });
        popupMenu.show();
    }

    private void createRemoveReviewConfirmationDialogFragment() {
        RemoveReviewConfirmationDialogFragment removeReviewConfirmationDialogFragment = new RemoveReviewConfirmationDialogFragment();
        removeReviewConfirmationDialogFragment.setTargetFragment(this, REQUEST_CONFIRM_DELETE_REVIEW);
        removeReviewConfirmationDialogFragment.show(getFragmentManager(), null);
    }

    public static class RemoveReviewConfirmationDialogFragment extends DialogFragment {

        @NonNull
        @Override
        public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

            final BookReviewsFragment bookReviewsFragment = (BookReviewsFragment)getTargetFragment();

            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            // TODO MEDIUM make this text red
            builder.setMessage(bookReviewsFragment.getString(R.string.remove_review_confirmation_message));
            builder.setPositiveButton(bookReviewsFragment.getString(R.string.yes), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    bookReviewsFragment.requestRemoveReview();
                }
            });

            builder.setNegativeButton(bookReviewsFragment.getString(R.string.no), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });

            return builder.create();
        }
    }

}
