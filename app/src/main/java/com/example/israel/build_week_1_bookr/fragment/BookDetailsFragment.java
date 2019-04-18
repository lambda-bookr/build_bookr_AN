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
import android.transition.Slide;
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
import com.example.israel.build_week_1_bookr.dao.SessionDAO;
import com.example.israel.build_week_1_bookr.model.Book;
import com.example.israel.build_week_1_bookr.model.UserInfo;
import com.example.israel.build_week_1_bookr.worker_thread.RequestDeleteBookAsyncTask;
import com.example.israel.build_week_1_bookr.worker_thread.RequestImageByUrlAsyncTask;

public class BookDetailsFragment extends Fragment {

    private static final int REQUEST_CONFIRM_DELETE_BOOK = 0;
    private static final String ARG_BOOK = "book";
    private static final String ARG_BOOK_LIST_POSITION = "book_list_position";

    private View fragmentView;
    private Book book;
    private RequestImageByUrlAsyncTask requestBookImageByUrlAsyncTask;
    private RequestDeleteBookAsyncTask requestDeleteBookAsyncTask;
    private int bookListPosition;

    public static BookDetailsFragment newInstance(Book book, int bookListPosition) {

        Bundle args = new Bundle();
        args.putParcelable(ARG_BOOK, book);
        args.putInt(ARG_BOOK_LIST_POSITION, bookListPosition);

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
        bookListPosition = getArguments().getInt(ARG_BOOK_LIST_POSITION);

        fragmentView = inflater.inflate(R.layout.fragment_book_details, container, false);

        fragmentView.findViewById(R.id.fragment_book_details_constraint_layout_root).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // blocker
            }
        });

        // create more options popup
        ImageButton moreOptionsImageButton = fragmentView.findViewById(R.id.fragment_book_details_image_button_more_options);
        moreOptionsImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserInfo userInfo = SessionDAO.getUserInfo(getActivity());

                // must be the creator to delete it
                if (book.getUserId() == userInfo.getId()) {
                    PopupMenu popupMenu = new PopupMenu(getActivity(), v);
                    popupMenu.getMenuInflater().inflate(R.menu.fragment_book_details_more_options, popupMenu.getMenu());
                    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            int id = item.getItemId();
                            switch (id) {
                                case R.id.menu_book_details_book_delete: {
                                    createDeleteBookConfirmationDialog();
                                } break;
                            }

                            return true;
                        }
                    });
                    popupMenu.show();
                }
            }
        });

        TextView titleTextView = fragmentView.findViewById(R.id.fragment_book_details_text_view_title);
        titleTextView.setText(book.getTitle());

        TextView authorTextView = fragmentView.findViewById(R.id.fragment_book_details_text_view_author);
        String authorStr = getString(R.string.by) + " " + book.getAuthor();
        authorTextView.setText(authorStr);

        TextView publisherTextView = fragmentView.findViewById(R.id.fragment_book_details_text_view_publisher);
        String publisherStr = getString(R.string.published_by) + " " + book.getPublisher();
        publisherTextView.setText(publisherStr);

        TextView priceTextView = fragmentView.findViewById(R.id.fragment_book_details_text_view_price);
        String priceStr = "$" + Double.toString(book.getPrice());
        priceTextView.setText(priceStr);

        RatingBar averageRatingRatingBar = fragmentView.findViewById(R.id.fragment_book_review_rating_bar_average_rating);
        averageRatingRatingBar.setRating((float)book.getAverageRating());

        fragmentView.findViewById(R.id.fragment_book_details_text_view_reviews).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createBookReviewsFragment();
            }
        });

        TextView descriptionTextView = fragmentView.findViewById(R.id.fragment_book_details_text_view_description);
        descriptionTextView.setText(book.getDescription());

        requestBookImage();

        return fragmentView;
    }

    @Override
    public void onDetach() {
        if (requestBookImageByUrlAsyncTask != null) {
            requestBookImageByUrlAsyncTask.cancel(false);
            requestBookImageByUrlAsyncTask = null;
        }

        if (requestDeleteBookAsyncTask != null) {
            requestDeleteBookAsyncTask.cancel(false);
            requestDeleteBookAsyncTask = null;
        }

        super.onDetach();
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

                // TODO MEDIUM. if there's no image, should we set a default image
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

        requestDeleteBookAsyncTask = new RequestDeleteBookAsyncTask(SessionDAO.getSessionToken(getActivity()), book.getId()) {
            @Override
            protected void onPostExecute(Book book) {
                super.onPostExecute(book);

                if (isCancelled() || getActivity() == null) {
                    return;
                }

                requestDeleteBookAsyncTask = null;

                if (book != null) {
                    // remove from the book list
                    ((BookListFragment)getTargetFragment()).removeBook(bookListPosition);
                    getActivity().getSupportFragmentManager().popBackStack();

                    Toast toast = Toast.makeText(getActivity(), getString(R.string.delete_book_success), Toast.LENGTH_SHORT);
                    toast.show();
                } else {
                    Toast toast = Toast.makeText(getActivity(), getString(R.string.delete_book_failed), Toast.LENGTH_SHORT);
                    toast.show();
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
            // TODO MEDIUM make this text red
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

    private void createBookReviewsFragment() {
        BookReviewsFragment bookReviewsFragment = BookReviewsFragment.newInstance(book);

        bookReviewsFragment.setEnterTransition(new Slide());

        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.activity_book_list_frame_layout, bookReviewsFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
