package com.example.israel.build_week_1_bookr.activity;

import android.annotation.SuppressLint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;

import com.example.israel.build_week_1_bookr.R;
import com.example.israel.build_week_1_bookr.adapter.BookListAdapter;
import com.example.israel.build_week_1_bookr.worker_thread.RequestBookListAsyncTask;

public class BookListActivity extends AppCompatActivity {

    public static final int GRID_SPAN_COUNT = 2;
    private RecyclerView bookListRecyclerView;
    private BookListAdapter bookListAdapter;
    private RequestBookListAsyncTask requestBookListAsyncTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_list);

        // TODO should the book list refresh after book details closes

        setupBookListRecyclerView();

        requestBookList();

    }

    @Override
    protected void onDestroy() {
        if (requestBookListAsyncTask != null) {
            requestBookListAsyncTask.cancel(false);
            requestBookListAsyncTask = null;
        }

        super.onDestroy();
    }

    private void setupBookListRecyclerView() {
        bookListRecyclerView = findViewById(R.id.activity_book_list_recycler_view_book_list);
        // @NOTE: do not set recycler view to GONE
        bookListRecyclerView.setHasFixedSize(true);

        RecyclerView.LayoutManager layoutManager =
                new GridLayoutManager(this, GRID_SPAN_COUNT, GridLayoutManager.VERTICAL, false);

        bookListRecyclerView.setLayoutManager(layoutManager);

        bookListAdapter = new BookListAdapter(this, R.id.activity_book_list_root);
        bookListRecyclerView.setAdapter(bookListAdapter);
    }

    @SuppressLint("StaticFieldLeak")
    private void requestBookList() {
        if (requestBookListAsyncTask != null) {
            return;
        }

        final ProgressBar requestingBookListProgressBar = findViewById(R.id.activity_book_list_progress_bar_requesting_book_list);
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
