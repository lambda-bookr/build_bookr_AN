package com.example.israel.build_week_1_bookr.adapter;

import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.israel.build_week_1_bookr.R;
import com.example.israel.build_week_1_bookr.fragment.BookDetailsFragment;
import com.example.israel.build_week_1_bookr.model.Book;

import java.util.ArrayList;

public class BookListAdapter extends RecyclerView.Adapter<BookListAdapter.ViewHolder> {

    public BookListAdapter(FragmentActivity fragmentActivity, int bookDetailsFragmentParentId) {
        this.fragmentActivity = fragmentActivity;
        this.bookDetailsFragmentParentId = bookDetailsFragmentParentId;
    }

    private FragmentActivity fragmentActivity;
    private int bookDetailsFragmentParentId;
    private ArrayList<Book> books = new ArrayList<>();

    @NonNull
    @Override
    public BookListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_item_book, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull BookListAdapter.ViewHolder viewHolder, int i) {
        final Book book = books.get(i);

        viewHolder.titleTextView.setText(book.getTitle());
        viewHolder.averageRatingRatingBar.setRating((float)book.getAverageRating());

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BookDetailsFragment.addBooksDetailsFragment(fragmentActivity, book, bookDetailsFragmentParentId);
            }
        });
    }

    @Override
    public int getItemCount() {
        return books.size();
    }

    public void setBookList(@NonNull ArrayList<Book> books) {
        this.books = books;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            titleTextView = itemView.findViewById(R.id.list_item_book_text_view_title);
            averageRatingRatingBar = itemView.findViewById(R.id.list_item_book_rating_bar_average_rating);
        }

        private TextView titleTextView;
        private RatingBar averageRatingRatingBar;
    }

}
