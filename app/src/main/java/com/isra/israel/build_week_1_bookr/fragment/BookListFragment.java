package com.isra.israel.build_week_1_bookr.fragment;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.isra.israel.build_week_1_bookr.R;
import com.isra.israel.build_week_1_bookr.adapter.BookListAdapter;
import com.isra.israel.build_week_1_bookr.dao.BookrAPIDAO;
import com.isra.israel.build_week_1_bookr.dao.SessionDAO;
import com.isra.israel.build_week_1_bookr.model.Book;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

// TODO HIGH preserve last position when coming back from the details. Hint savedInstance
// TODO MEDIUM fragment's animation
public class BookListFragment extends Fragment {

    private View fragmentView;
    private static final int GRID_SPAN_COUNT = 1;
    private RecyclerView bookListRecyclerView;
    private BookListAdapter bookListAdapter;
    private Call<ArrayList<Book>> getBooksCall;
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

    @Override
    public void onDetach() {
        // when we log out this fragment can be detached
        cancelRequestBookList();

        super.onDetach();

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
        if (getBooksCall != null) {
            return;
        }

        bookListAdapter.removeAllBooks();

        bookListRecyclerView.setVisibility(View.INVISIBLE);

        getBooksCall = BookrAPIDAO.apiService.getBooks(SessionDAO.getSessionToken(getActivity()));
        getBooksCall.enqueue(new Callback<ArrayList<Book>>() {
            @Override
            public void onResponse(Call<ArrayList<Book>> call, Response<ArrayList<Book>> response) {
                onGetBookCallFinished(response);
            }

            @Override
            public void onFailure(Call<ArrayList<Book>> call, Throwable t) {
                onGetBookCallFinished(null);
            }
        });
    }

    private void onGetBookCallFinished(Response<ArrayList<Book>> response) {
        if (getBooksCall.isCanceled()) {
            return;
        }

        getBooksCall = null;
        bookListSwipeRefreshLayout.setRefreshing(false);

        if (response != null && response.isSuccessful()) {
            bookListRecyclerView.setVisibility(View.VISIBLE);

            bookListAdapter.setBookList(response.body());

            bookListSwipeRefreshLayout.setRefreshing(false);
        }
    }

    private void cancelRequestBookList() {
        if (getBooksCall != null) {
            getBooksCall.cancel();
            getBooksCall = null;
        }
    }

    private void createAddBookFragment() {
        AddBookFragment addBookFragment = AddBookFragment.newInstance();
        addBookFragment.setTargetFragment(this, 0);
        //addBookFragment.setEnterTransition(new Slide());

        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(android.R.anim.slide_in_left,0, 0, android.R.anim.slide_out_right);
        transaction.add(R.id.activity_book_list_frame_layout, addBookFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    public void addBook(Book book) {
        bookListAdapter.addBook(book);
    }

    public void removeBook(int bookPosition) {
        bookListAdapter.removeBook(bookPosition);
    }

}
