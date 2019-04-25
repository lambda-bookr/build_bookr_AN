package com.example.israel.build_week_1_bookr.fragment;


import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.os.AsyncTask;
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

import com.example.israel.build_week_1_bookr.R;
import com.example.israel.build_week_1_bookr.adapter.BookListAdapter;
import com.example.israel.build_week_1_bookr.dao.BookrAPIDAO;
import com.example.israel.build_week_1_bookr.dao.SessionDAO;
import com.example.israel.build_week_1_bookr.model.Book;
import com.example.israel.build_week_1_bookr.network.NetworkAdapter;

import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

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
    private DownloadBookImagesThread downloadBookImagesThread;

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
        cancelDownloadBookImages();
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

        cancelDownloadBookImages(); // a new book list will be used
        bookListAdapter.removeAllBooks();

        bookListRecyclerView.setVisibility(View.INVISIBLE);

        getBooksCall = BookrAPIDAO.apiService.getBooks(SessionDAO.getSessionToken(getActivity()));
        getBooksCall.enqueue(new Callback<ArrayList<Book>>() {
            @Override
            public void onResponse(Call<ArrayList<Book>> call, Response<ArrayList<Book>> response) {
                if (call.isCanceled()) {
                    return;
                }

                getBooksCall = null;

                if (response.isSuccessful()) {
                    bookListRecyclerView.setVisibility(View.VISIBLE);

                    bookListAdapter.setBookList(response.body());

                    // start downloading the images
                    downloadBookImagesThread = new DownloadBookImagesThread(new ArrayList<>(response.body()));
                    downloadBookImagesThread.start();

                    bookListSwipeRefreshLayout.setRefreshing(false);
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Book>> call, Throwable t) {
                if (call.isCanceled()) {
                    return;
                }

                getBooksCall = null;

                bookListSwipeRefreshLayout.setRefreshing(false);
            }
        });
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

    // TODO MEDIUM create a Thread that will wait for a book to request an image downloaded
    // rather than downloading every image we should only download the one that is (being viewed + n)

    public class DownloadBookImagesThread extends Thread {

        public DownloadBookImagesThread(ArrayList<Book> books) {
            this.books = books;
        }

        private ArrayList<Book> books;
        private AtomicBoolean isCancelled = new AtomicBoolean(false);

        @Override
        public void run() {
            super.run();

            // Download the images one by one then update the corresponding view
            for (int i = 0; i < books.size(); ++i) {
                final Book book = books.get(i);

                try {
                    new URL(book.getImageUrl());
                } catch (MalformedURLException e) {
                    // no need to try downloading
                    continue;
                }

                final Bitmap bookImageBitmap = NetworkAdapter.httpImageRequestGET(book.getImageUrl());
                if (isCancelled.get() || // the adapter list is now different. It will not crash but it's useless to set the images
                        getActivity() == null) {
                    break;
                }

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        bookListAdapter.setBookImageBitmap(book, bookImageBitmap);
                    }
                });
            }

            if (getActivity() != null) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        downloadBookImagesThread = null;
                    }
                });
            }
        }
    }

    private void cancelDownloadBookImages() {
        if (downloadBookImagesThread != null) {
            downloadBookImagesThread.isCancelled.set(true);
            downloadBookImagesThread = null;
        }
    }

}
