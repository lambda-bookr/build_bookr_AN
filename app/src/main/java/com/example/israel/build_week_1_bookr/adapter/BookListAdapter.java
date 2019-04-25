package com.example.israel.build_week_1_bookr.adapter;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.transition.AutoTransition;
import android.transition.ChangeBounds;
import android.transition.ChangeImageTransform;
import android.transition.ChangeTransform;
import android.transition.Fade;
import android.transition.Slide;
import android.transition.TransitionSet;
import android.util.Pair;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.israel.build_week_1_bookr.R;
import com.example.israel.build_week_1_bookr.fragment.BookDetailsFragment;
import com.example.israel.build_week_1_bookr.fragment.BookListFragment;
import com.example.israel.build_week_1_bookr.model.Book;
import com.example.israel.build_week_1_bookr.network.NetworkAdapter;
import com.example.israel.build_week_1_bookr.utils.CircularDictionary;
import com.example.israel.build_week_1_bookr.worker_thread.RequestImageByUrlAsyncTask;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

public class BookListAdapter extends RecyclerView.Adapter<BookListAdapter.ViewHolder> {

    private static final int IMAGE_CACHE_SIZE = 20;

    public BookListAdapter(BookListFragment bookListFragment, int bookDetailsFragmentSlotId) {
        this.bookListFragment = bookListFragment;
        this.bookDetailsFragmentSlotId = bookDetailsFragmentSlotId;
    }

    private BookListFragment bookListFragment ;
    private int bookDetailsFragmentSlotId;
    private ArrayList<Book> books = new ArrayList<>();
    private int lastPosition = -1;
    private CircularDictionary<Integer, Bitmap> imageCache = new CircularDictionary<>(IMAGE_CACHE_SIZE);

    @NonNull
    @Override
    public BookListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_item_book, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final BookListAdapter.ViewHolder viewHolder, int i) {
        final Book book = books.get(i);

        viewHolder.titleTextView.setText(book.getTitle());
        viewHolder.authorTextView.setText(book.getAuthor());
        viewHolder.averageRatingRatingBar.setRating((float)book.getAverageRating());

        try {
            new URL(book.getImageUrl());

            final Bitmap imageBitmap = imageCache.get(book.getId());
            if (imageBitmap == null) {
                // request image
                viewHolder.requestingImageImageView.setVisibility(View.VISIBLE);
                viewHolder.bookImageImageView.setVisibility(View.INVISIBLE);

                final AtomicBoolean cancel = new AtomicBoolean(false);
                viewHolder.cancelImageRequest = cancel;

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        final Bitmap requestedBitmap = NetworkAdapter.httpImageRequestGET(book.getImageUrl());
                        if (cancel.get()) {
                            return;
                        }

                        if (bookListFragment.getActivity() != null) {
                            bookListFragment.getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    viewHolder.requestingImageImageView.setVisibility(View.INVISIBLE);
                                    viewHolder.bookImageImageView.setVisibility(View.VISIBLE);

                                    if (requestedBitmap == null) {
                                        viewHolder.bookImageImageView.setImageResource(R.drawable.ic_broken_image_white_24dp);
                                    } else {
                                        imageCache.put(book.getId(), requestedBitmap);
                                        viewHolder.bookImageImageView.setImageBitmap(requestedBitmap);
                                    }
                                }
                            });
                        }
                    }
                }).start();

            } else { // has image in cache
                viewHolder.requestingImageImageView.setVisibility(View.INVISIBLE);
                viewHolder.bookImageImageView.setVisibility(View.VISIBLE);
                viewHolder.bookImageImageView.setImageBitmap(imageBitmap);
            }
        } catch (MalformedURLException e) { // invalid url
            viewHolder.requestingImageImageView.setVisibility(View.INVISIBLE);
            viewHolder.bookImageImageView.setVisibility(View.VISIBLE);
            viewHolder.bookImageImageView.setImageResource(R.drawable.ic_broken_image_white_24dp);
        }

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BookDetailsFragment bookDetailsFragment = BookDetailsFragment.newInstance(book, viewHolder.getLayoutPosition());
                bookDetailsFragment.setTargetFragment(bookListFragment, 0);

                //bookDetailsFragment.setEnterTransition(new Slide());

                FragmentTransaction transaction = bookListFragment.getActivity().getSupportFragmentManager().beginTransaction();
                transaction.setCustomAnimations(android.R.anim.slide_in_left,0, 0, android.R.anim.slide_out_right);
                transaction.add(bookDetailsFragmentSlotId, bookDetailsFragment);
                transaction.addToBackStack(null); // remove this fragment on back press
                transaction.commit();
            }
        });

        if (i > lastPosition) {
            Animation animation = AnimationUtils.loadAnimation(viewHolder.itemView.getContext(), android.R.anim.slide_in_left);
            viewHolder.itemView.startAnimation(animation);
            lastPosition = i;
        }
    }

    @Override
    public void onViewDetachedFromWindow(@NonNull ViewHolder holder) {
        super.onViewDetachedFromWindow(holder);

        if (holder.cancelImageRequest != null) {
            holder.cancelImageRequest.set(true);
            holder.cancelImageRequest = null;
        }
    }

    @Override
    public int getItemCount() {
        return books.size();
    }

    /** Reset this adapter */
    public void setBookList(@NonNull ArrayList<Book> books) {
        this.books = books;
        this.imageCache = new CircularDictionary<>(IMAGE_CACHE_SIZE);

        notifyDataSetChanged();
    }

    public void addBook(final Book book) {
        books.add(book);

        notifyItemInserted(books.size() - 1);
    }

    public void removeBook(int i) {
        lastPosition -= 1;
        books.remove(i);

        notifyItemRemoved(i);
    }

    public void removeAllBooks() {
        lastPosition = 0;
        books.clear();

        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            titleTextView = itemView.findViewById(R.id.list_item_book_text_view_title);
            authorTextView = itemView.findViewById(R.id.list_item_book_text_view_author);
            averageRatingRatingBar = itemView.findViewById(R.id.list_item_book_rating_bar_average_rating);
            bookImageImageView = itemView.findViewById(R.id.list_item_book_image_view_image);
            requestingImageImageView = itemView.findViewById(R.id.list_item_book_progress_bar_requesting_image);
        }

        private TextView titleTextView;
        private TextView authorTextView;
        private RatingBar averageRatingRatingBar;
        private ImageView bookImageImageView;
        private ProgressBar requestingImageImageView;
        private AtomicBoolean cancelImageRequest;

    }

}
