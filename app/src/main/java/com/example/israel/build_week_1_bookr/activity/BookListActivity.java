package com.example.israel.build_week_1_bookr.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.israel.build_week_1_bookr.R;
import com.example.israel.build_week_1_bookr.fragment.BookListFragment;

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
