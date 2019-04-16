package com.example.israel.build_week_1_bookr.fragment;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.example.israel.build_week_1_bookr.R;
import com.example.israel.build_week_1_bookr.adapter.BookListAdapter;
import com.example.israel.build_week_1_bookr.worker_thread.RequestBookListAsyncTask;

// TODO MEDIUM. should the book list refresh after book details closes. currently it doesn't refresh
// TODO VERY HIGH. this fragment can still receive action because we are adding the details activity instead of replacing
// TODO CRITICAL. Review list
public class BookListFragment extends Fragment {

    public static final int GRID_SPAN_COUNT = 2;
    private RecyclerView bookListRecyclerView;
    private BookListAdapter bookListAdapter;
    private RequestBookListAsyncTask requestBookListAsyncTask;

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
        View view = inflater.inflate(R.layout.fragment_book_list, container, false);

        setupBookListRecyclerView(view);

        requestBookList(view);

        return view;
    }

    public static void replaceBookListFragment(FragmentActivity fragmentActivity, int i) {
        BookListFragment bookListFragment = BookListFragment.newInstance();

        // TODO MEDIUM animation
        FragmentTransaction transaction = fragmentActivity.getSupportFragmentManager().beginTransaction();
        transaction.replace(i, bookListFragment);
        // login fragment should always be underneath
        transaction.commit();
    }

    private void setupBookListRecyclerView(View v) {
        bookListRecyclerView = v.findViewById(R.id.fragment_book_list_recycler_view_book_list);
        // @NOTE: do not set recycler view to GONE
        bookListRecyclerView.setHasFixedSize(true);

        RecyclerView.LayoutManager layoutManager =
                new GridLayoutManager(getActivity(), GRID_SPAN_COUNT, GridLayoutManager.VERTICAL, false);

        bookListRecyclerView.setLayoutManager(layoutManager);

        bookListAdapter = new BookListAdapter(getActivity(), R.id.activity_book_list_frame_layout);
        bookListRecyclerView.setAdapter(bookListAdapter);
    }

    @SuppressLint("StaticFieldLeak")
    private void requestBookList(View v) {
        if (requestBookListAsyncTask != null) {
            return;
        }

        final ProgressBar requestingBookListProgressBar = v.findViewById(R.id.fragment_book_list_progress_bar_requesting_book_list);
        requestingBookListProgressBar.setVisibility(View.VISIBLE);
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
                requestingBookListProgressBar.setVisibility(View.GONE);

                bookListAdapter.setBookList(result.books);
            }
        };
        requestBookListAsyncTask.execute();
    }

}
