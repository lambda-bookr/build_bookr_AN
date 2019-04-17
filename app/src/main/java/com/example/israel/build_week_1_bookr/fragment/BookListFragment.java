package com.example.israel.build_week_1_bookr.fragment;


import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.transition.Slide;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.ScrollView;

import com.example.israel.build_week_1_bookr.R;
import com.example.israel.build_week_1_bookr.adapter.BookListAdapter;
import com.example.israel.build_week_1_bookr.model.Book;
import com.example.israel.build_week_1_bookr.network.NetworkAdapter;
import com.example.israel.build_week_1_bookr.worker_thread.RequestBookListAsyncTask;

import java.util.ArrayList;

// TODO MEDIUM preserve last position when coming back from the details. Hint savedInstance
public class BookListFragment extends Fragment {

    private View fragmentView;
    private static final int GRID_SPAN_COUNT = 1;
    private RecyclerView bookListRecyclerView;
    private BookListAdapter bookListAdapter;
    private RequestBookListAsyncTask requestBookListAsyncTask;
    private SwipeRefreshLayout bookListSwipeRefreshLayout;

    public static BookListFragment newInstance() {

        Bundle args = new Bundle();

        BookListFragment fragment = new BookListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public BookListFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        fragmentView = inflater.inflate(R.layout.fragment_book_list, container, false);

        setupBookListRecyclerView();
        requestBookList();
        bookListSwipeRefreshLayout = fragmentView.findViewById(R.id.fragment_book_list_swipe_refresh_layout_book_list);
        bookListSwipeRefreshLayout.setRefreshing(true);
        bookListSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                requestBookList();
            }
        });

        FloatingActionButton addBookFAB = fragmentView.findViewById(R.id.fragment_book_list_fab_add_book);
        addBookFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createAddBookFragment();
            }
        });

        return fragmentView;
    }

    private void setupBookListRecyclerView() {
        bookListRecyclerView = fragmentView.findViewById(R.id.fragment_book_list_recycler_view_book_list);
        // @NOTE: do not set recycler view to GONE
        bookListRecyclerView.setHasFixedSize(true);

        RecyclerView.LayoutManager layoutManager =
                new GridLayoutManager(getActivity(), GRID_SPAN_COUNT, GridLayoutManager.VERTICAL, false);

        bookListRecyclerView.setLayoutManager(layoutManager);

        bookListAdapter = new BookListAdapter(this, R.id.activity_book_list_frame_layout);
        bookListRecyclerView.setAdapter(bookListAdapter);
    }

    @SuppressLint("StaticFieldLeak")
    private void requestBookList() {
        if (requestBookListAsyncTask != null) {
            return;
        }

        bookListAdapter.removeAllBooks();

        bookListRecyclerView.setVisibility(View.INVISIBLE);

        requestBookListAsyncTask = new RequestBookListAsyncTask() {
            @Override
            protected void onPostExecute(Result result) {
                super.onPostExecute(result);

                if (isCancelled()) {
                    return;
                }
                requestBookListAsyncTask = null;

                bookListRecyclerView.setVisibility(View.VISIBLE);

                //bookListAdapter.setBookList(result.books);
                populateBookListAdapter(result.books);

                bookListSwipeRefreshLayout.setRefreshing(false);
            }
        };
        requestBookListAsyncTask.execute();
    }

    private void createAddBookFragment() {
        AddBookFragment addBookFragment = AddBookFragment.newInstance();

        addBookFragment.setEnterTransition(new Slide());

        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.activity_book_list_frame_layout, addBookFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private void populateBookListAdapter(final ArrayList<Book> books) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (final Book book : books) {
                    final Bitmap bookImageBitmap = NetworkAdapter.httpImageRequestGET(book.getImageUrl());
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            bookListAdapter.addBook(book, bookImageBitmap);
                        }
                    });
                }

            }
        }).start();
    }

    public void removeBook(int bookPosition) {
        bookListAdapter.removeBook(bookPosition);
    }

}
