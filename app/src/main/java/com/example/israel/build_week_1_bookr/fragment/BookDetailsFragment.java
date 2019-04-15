package com.example.israel.build_week_1_bookr.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.israel.build_week_1_bookr.R;
import com.example.israel.build_week_1_bookr.model.Book;

public class BookDetailsFragment extends Fragment {

    private static final String ARG_BOOK = "book";

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

        View view = inflater.inflate(R.layout.fragment_book_details, container, false);
        TextView titleTextView = view.findViewById(R.id.fragment_book_details_title);
        titleTextView.setText(book.getTitle());

        return view;
    }

    public static void replaceBooksDetailsFragment(FragmentActivity fragmentActivity, Book book, int i) {
        BookDetailsFragment bookDetailsFragment = BookDetailsFragment.newInstance(book);

        FragmentTransaction transaction = fragmentActivity.getSupportFragmentManager().beginTransaction();
        transaction.replace(i, bookDetailsFragment);
        transaction.addToBackStack(null); // remove this fragment on back press
        transaction.commit();
    }
}
