package com.example.israel.build_week_1_bookr.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.israel.build_week_1_bookr.R;
import com.example.israel.build_week_1_bookr.dao.SessionDAO;
import com.example.israel.build_week_1_bookr.fragment.BookReviewsFragment;
import com.example.israel.build_week_1_bookr.model.Review;
import com.example.israel.build_week_1_bookr.model.UserInfo;

import java.util.ArrayList;

public class ReviewListAdapter extends RecyclerView.Adapter<ReviewListAdapter.ViewHolder> {

    public ReviewListAdapter(BookReviewsFragment bookReviewsFragment) {
        this.bookReviewsFragment = bookReviewsFragment;
    }

    private BookReviewsFragment bookReviewsFragment;
    private ArrayList<Review> reviews = new ArrayList<>();

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_item_review, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, int i) {
        final Review review = reviews.get(i);

        viewHolder.usernameTextView.setText(review.getUsername());
        viewHolder.ratingRatingBar.setRating(review.getRating());
        viewHolder.reviewTextView.setText(review.getReview());

        viewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                UserInfo userInfo = SessionDAO.getUserInfo(bookReviewsFragment.getActivity());

                // must be the creator to delete it
                if (review.getUsername().equals(userInfo.getUsername())) {
                    bookReviewsFragment.createReviewPopupMenu(v, review, viewHolder.getAdapterPosition());
                    return true;
                }

                return false;
            }
        });
    }

    @Override
    public int getItemCount() {
        return reviews.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            usernameTextView = itemView.findViewById(R.id.list_item_review_text_view_username);
            ratingRatingBar = itemView.findViewById(R.id.list_item_review_rating_bar_rating);
            reviewTextView = itemView.findViewById(R.id.list_item_review_text_view_review);
        }

        private TextView usernameTextView;
        private RatingBar ratingRatingBar;
        private TextView reviewTextView;
    }

    public void setReviewList(ArrayList<Review> reviews) {
        this.reviews = reviews;
        notifyDataSetChanged();
    }

    public void addReview(Review review) {
        reviews.add(review);
        notifyItemInserted(reviews.size() - 1);
    }

    public void removeReview(int i) {
        if ( i >= reviews.size()) {
            return;
        }

        reviews.remove(i);
        notifyItemRemoved(i);
    }
}
