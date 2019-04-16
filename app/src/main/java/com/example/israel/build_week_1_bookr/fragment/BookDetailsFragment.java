package com.example.israel.build_week_1_bookr.fragment;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.israel.build_week_1_bookr.R;
import com.example.israel.build_week_1_bookr.adapter.ReviewListAdapter;
import com.example.israel.build_week_1_bookr.model.Book;
import com.example.israel.build_week_1_bookr.model.Book2;
import com.example.israel.build_week_1_bookr.worker_thread.RequestBook2AsyncTask;
import com.example.israel.build_week_1_bookr.worker_thread.RequestImageByUrlAsyncTask;

// TODO MEDIUM. improve review list view. Move review view into a new fragment when the average rating is clicked
// TODO CRITICAL add review
public class BookDetailsFragment extends Fragment {

    private static final String ARG_BOOK = "book";

    private RequestBook2AsyncTask requestBook2AsyncTask;
    private RequestImageByUrlAsyncTask requestBookImageByUrlAsyncTask;
    private View fragmentView;

    public static BookDetailsFragment newInstance(Book book) {

        Bundle args = new Bundle();
        args.putParcelable(ARG_BOOK, book);

        BookDetailsFragment fragment = new BookDetailsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        Book book = getArguments().getParcelable(ARG_BOOK);

        fragmentView = inflater.inflate(R.layout.fragment_book_details, container, false);
        TextView titleTextView = fragmentView.findViewById(R.id.fragment_book_details_text_view_title);
        titleTextView.setText(book.getTitle());

        TextView authorTextView = fragmentView.findViewById(R.id.fragment_book_details_text_view_author);
        authorTextView.setText(book.getAuthor());

        TextView publisherTextView = fragmentView.findViewById(R.id.fragment_book_details_text_view_publisher);
        publisherTextView.setText(book.getPublisher());

        TextView priceTextView = fragmentView.findViewById(R.id.fragment_book_details_text_view_price);
        priceTextView.setText(Double.toString(book.getPrice()));

        RatingBar averageRatingRatingBar = fragmentView.findViewById(R.id.fragment_book_review_rating_bar_average_rating);
        averageRatingRatingBar.setRating((float)book.getAverageRating());

        TextView descriptionTextView = fragmentView.findViewById(R.id.fragment_book_details_text_view_description);
        descriptionTextView.setText(book.getDescription());

        requestBookImage(book);

        requestBook2(book); // for the reviews

        return fragmentView;
    }

    public static void addBooksDetailsFragment(FragmentActivity fragmentActivity, Book book, int i) {
        BookDetailsFragment bookDetailsFragment = BookDetailsFragment.newInstance(book);

        // TODO MEDIUM animation
        FragmentTransaction transaction = fragmentActivity.getSupportFragmentManager().beginTransaction();
        transaction.replace(i, bookDetailsFragment); // refreshes the login fragment
        transaction.addToBackStack(null); // remove this fragment on back press
        transaction.commit();
    }

    @SuppressLint("StaticFieldLeak")
    private void requestBook2(Book book) {
        if (requestBook2AsyncTask != null) {
            return;
        }

        final ProgressBar requestingReviewsProgressBar = fragmentView.findViewById(R.id.fragment_book_details_progress_bar_requesting_reviews);
        requestingReviewsProgressBar.setVisibility(View.VISIBLE);

        final RecyclerView reviewsRecyclerView = fragmentView.findViewById(R.id.fragment_book_details_recycler_view_reviews);
        reviewsRecyclerView.setVisibility(View.INVISIBLE);

        requestBook2AsyncTask = new RequestBook2AsyncTask(book) {
            @Override
            protected void onPostExecute(Book2 book2) {
                super.onPostExecute(book2);
                reviewsRecyclerView.setVisibility(View.VISIBLE);
                requestingReviewsProgressBar.setVisibility(View.INVISIBLE);


                if (isCancelled()) {
                    return;
                }

                requestBook2AsyncTask = null;

                if (book2 == null) {
                    return;
                }

                setupReviewsRecyclerView(book2);
            }
        };
        requestBook2AsyncTask.execute();
    }

    private void setupReviewsRecyclerView(Book2 book2) {
        RecyclerView reviewsRecyclerView = fragmentView.findViewById(R.id.fragment_book_details_recycler_view_reviews);
        reviewsRecyclerView.setHasFixedSize(true); // TODO this might change if I implement resizing for reviews view

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        reviewsRecyclerView.setLayoutManager(layoutManager);

        ReviewListAdapter reviewListAdapter = new ReviewListAdapter(book2.getReviews());
        reviewsRecyclerView.setAdapter(reviewListAdapter);
    }

    @SuppressLint("StaticFieldLeak")
    private void requestBookImage(Book book) {
        if (requestBookImageByUrlAsyncTask != null) {
            return;
        }

        final ImageView bookImageImageView = fragmentView.findViewById(R.id.fragment_book_details_image_view_image);
        bookImageImageView.setVisibility(View.GONE);

        final ProgressBar requestingImageProgressBar = fragmentView.findViewById(R.id.fragment_book_details_progress_bar_requesting_image);
        requestingImageProgressBar.setVisibility(View.VISIBLE);

        requestBookImageByUrlAsyncTask = new RequestImageByUrlAsyncTask(book.getImageUrl()) {
            @Override
            protected void onPostExecute(Bitmap bitmap) {
                super.onPostExecute(bitmap);

                requestingImageProgressBar.setVisibility(View.GONE);
                bookImageImageView.setVisibility(View.VISIBLE);

                if (isCancelled()) {
                    return;
                }

                requestBookImageByUrlAsyncTask = null;

                // TODO LOW. if there's no image, should we set a default image
                bookImageImageView.setImageBitmap(bitmap);

            }
        };

        requestBookImageByUrlAsyncTask.execute();
    }
}
