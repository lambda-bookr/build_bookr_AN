package com.example.israel.build_week_1_bookr.fragment;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.israel.build_week_1_bookr.R;
import com.example.israel.build_week_1_bookr.adapter.ReviewListAdapter;
import com.example.israel.build_week_1_bookr.model.Book;
import com.example.israel.build_week_1_bookr.model.Book2;
import com.example.israel.build_week_1_bookr.worker_thread.RequestBook2AsyncTask;
import com.example.israel.build_week_1_bookr.worker_thread.RequestDeleteBookAsyncTask;
import com.example.israel.build_week_1_bookr.worker_thread.RequestImageByUrlAsyncTask;

// TODO CRITICAL. improve review list view. Move review view into a new fragment when the average rating is clicked
// TODO CRITICAL add review
// TODO MEDIUM surround description with scroll view
public class BookDetailsFragment extends Fragment {

    private static final int REQUEST_CONFIRM_DELETE_BOOK = 0;
    private static final String ARG_BOOK = "book";

    private View fragmentView;
    private Book book;
    private ReviewListAdapter reviewListAdapter;
    private RequestBook2AsyncTask requestBook2AsyncTask;
    private RequestImageByUrlAsyncTask requestBookImageByUrlAsyncTask;
    private RequestDeleteBookAsyncTask requestDeleteBookAsyncTask;

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
        book = getArguments().getParcelable(ARG_BOOK);

        fragmentView = inflater.inflate(R.layout.fragment_book_details, container, false);

        ImageButton expandMoreImageButton = fragmentView.findViewById(R.id.fragment_book_details_image_button_expand_more);
        expandMoreImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(getActivity(), v);
                popupMenu.getMenuInflater().inflate(R.menu.fragment_book_details_expand_more, popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        int id = item.getItemId();
                        switch (id) {
                            case R.id.menu_book_details_delete: {
                                createDeleteBookConfirmationDialog();
                            } break;
                        }

                        return true;
                    }
                });
                popupMenu.show();
            }
        });

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

        requestBookImage();

        return fragmentView;
    }

    public static void createBooksDetailsFragment(FragmentActivity fragmentActivity, Book book, int i) {
        BookDetailsFragment bookDetailsFragment = BookDetailsFragment.newInstance(book);

        // TODO MEDIUM animation
        FragmentTransaction transaction = fragmentActivity.getSupportFragmentManager().beginTransaction();
        transaction.replace(i, bookDetailsFragment); // refreshes the login fragment
        transaction.addToBackStack(null); // remove this fragment on back press
        transaction.commit();
    }

    @SuppressLint("StaticFieldLeak")
    private void requestBookImage() {
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

    private void createDeleteBookConfirmationDialog() {
        DeleteBookConfirmationDialogFragment deleteBookConfirmationDialogFragment = new DeleteBookConfirmationDialogFragment();
        deleteBookConfirmationDialogFragment.setTargetFragment(this, REQUEST_CONFIRM_DELETE_BOOK);
        deleteBookConfirmationDialogFragment.show(getFragmentManager(), null);
    }

    @SuppressLint("StaticFieldLeak")
    private void requestDeleteBook() {
        if (requestDeleteBookAsyncTask != null) {
            return;
        }

        requestDeleteBookAsyncTask = new RequestDeleteBookAsyncTask(book.getId()) {
            @Override
            protected void onPostExecute(Result result) {
                super.onPostExecute(result);

                if (isCancelled()) {
                    return;
                }

                requestDeleteBookAsyncTask = null;

                switch (result.result) {
                    case Result.SUCCESS: {
                        // TODO DEPENDS. remove the book from book list manually

                        getActivity().getSupportFragmentManager().popBackStack();

                        Toast toast = Toast.makeText(getActivity(), getString(R.string.delete_book_success), Toast.LENGTH_SHORT);
                        toast.show();
                    } break;
                    case Result.FAILED: {
                        Toast toast = Toast.makeText(getActivity(), getString(R.string.delete_book_failed), Toast.LENGTH_SHORT);
                        toast.show();
                    } break;
                }
            }
        };
        requestDeleteBookAsyncTask.execute();

    }

    public static class DeleteBookConfirmationDialogFragment extends DialogFragment {
        @NonNull
        @Override
        public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

            final BookDetailsFragment bookDetailsFragment = (BookDetailsFragment)getTargetFragment();

            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            // TODO LOW make this text red
            builder.setMessage(bookDetailsFragment.getString(R.string.delete_book_confirmation_message));
            builder.setPositiveButton(bookDetailsFragment.getString(R.string.yes), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    bookDetailsFragment.requestDeleteBook();
                }
            });

            builder.setNegativeButton(bookDetailsFragment.getString(R.string.no), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });

            return builder.create();
        }
    }
}
