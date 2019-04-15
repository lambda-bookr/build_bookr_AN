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
import com.example.israel.build_week_1_bookr.fragment.BookListFragment;
import com.example.israel.build_week_1_bookr.worker_thread.RequestBookListAsyncTask;

// TODO refresh functionality when pulling down
public class BookListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // TODO animation

        setContentView(R.layout.activity_book_list);

        // TODO create a hard code here
        BookListFragment.replaceBookListFragment(this, R.id.activity_book_list_root);

    }

}
