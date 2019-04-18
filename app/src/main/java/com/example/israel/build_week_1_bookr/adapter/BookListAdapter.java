package com.example.israel.build_week_1_bookr.adapter;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
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
import com.example.israel.build_week_1_bookr.worker_thread.RequestImageByUrlAsyncTask;

import java.util.ArrayList;

public class BookListAdapter extends RecyclerView.Adapter<BookListAdapter.ViewHolder> {

    public BookListAdapter(BookListFragment bookListFragment, int bookDetailsFragmentSlotId) {
        this.bookListFragment = bookListFragment;
        this.bookDetailsFragmentSlotId = bookDetailsFragmentSlotId;
    }

    private BookListFragment bookListFragment ;
    private int bookDetailsFragmentSlotId;
    private ArrayList<Book> books = new ArrayList<>();
    private ArrayList<Bitmap> bookImageBitmaps = new ArrayList<>();
    private int lastPosition = -1;

    @NonNull
    @Override
    public BookListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_item_book, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final BookListAdapter.ViewHolder viewHolder, int i) {
        final Book book = books.get(i);
        Bitmap bookImageBitmap = bookImageBitmaps.get(i);

        viewHolder.titleTextView.setText(book.getTitle());
        viewHolder.authorTextView.setText(book.getAuthor());
        viewHolder.averageRatingRatingBar.setRating((float)book.getAverageRating());
        if (bookImageBitmaps.get(i) == null) {
            viewHolder.bookImageImageView.setVisibility(View.INVISIBLE);
            viewHolder.requestingImageImageView.setVisibility(View.VISIBLE);
        } else {
            viewHolder.bookImageImageView.setVisibility(View.VISIBLE);
            viewHolder.requestingImageImageView.setVisibility(View.INVISIBLE);
            viewHolder.bookImageImageView.setImageBitmap(bookImageBitmap);
        }

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BookDetailsFragment bookDetailsFragment = BookDetailsFragment.newInstance(book, viewHolder.getLayoutPosition());
                bookDetailsFragment.setTargetFragment(bookListFragment, 0);

                bookDetailsFragment.setEnterTransition(new Slide());

                FragmentTransaction transaction = bookListFragment.getActivity().getSupportFragmentManager().beginTransaction();
                //transaction.setCustomAnimations(android.R.anim.slide_in_left,0, 0, android.R.anim.slide_out_right);
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
    public int getItemCount() {
        return books.size();
    }

    public void setBookList(@NonNull ArrayList<Book> books) {
        this.books = books;
        this.bookImageBitmaps.ensureCapacity(books.size());
        for (int i = 0; i < books.size(); ++i) {
            this.bookImageBitmaps.add(null);
        }

        notifyDataSetChanged();
    }

    public void addBook(Book book) {
        books.add(book);
        bookImageBitmaps.add(null);
        // TODO request image
        notifyItemInserted(books.size() - 1);
    }

    public void removeBook(int i) {
        lastPosition -= 1;
        books.remove(i);
        bookImageBitmaps.remove(i);

        notifyItemRemoved(i);
    }

    public void removeAllBooks() {
        lastPosition = 0;
        books.clear();
        bookImageBitmaps.clear();

        notifyDataSetChanged();
    }

    public void setBookImageBitmap(int i, Bitmap bookImageBitmap) {
        bookImageBitmaps.set(i, bookImageBitmap);
        notifyItemChanged(i);
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

    }

}
