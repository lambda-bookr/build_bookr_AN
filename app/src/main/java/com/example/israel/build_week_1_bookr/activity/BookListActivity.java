package com.example.israel.build_week_1_bookr.activity;

import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.israel.build_week_1_bookr.R;
import com.example.israel.build_week_1_bookr.controller.ActivityStarter;
import com.example.israel.build_week_1_bookr.dao.SessionDAO;
import com.example.israel.build_week_1_bookr.fragment.BookListFragment;
import com.example.israel.build_week_1_bookr.model.UserInfo;

// TODO MEDIUM custom toolbar. and header for nav layout to show user data
public class BookListActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_list);

        // setup navigation view
        NavigationView navigationView = findViewById(R.id.activity_book_list_nav_view);

        navigationView.findViewById(R.id.book_list_nav_header_text_view_full_name);
        navigationView.setNavigationItemSelectedListener(this);

        UserInfo userInfo = SessionDAO.getUserInfo(this);
        if (userInfo != null) {
            View navigationHeader = navigationView.getHeaderView(0);
            TextView usernameTextView = navigationHeader.findViewById(R.id.book_list_nav_header_text_view_username);
            usernameTextView.setText(userInfo.getUsername());

            TextView fullNameTextView = navigationHeader.findViewById(R.id.book_list_nav_header_text_view_full_name);
            String fullNameStr = userInfo.getFirstName() + " " + userInfo.getLastName();
            fullNameTextView.setText(fullNameStr);
        }

        // book list fragment
        addBookListFragment();

    }

    @Override
    public void onBackPressed() {
        // close the drawer instead of back press
        DrawerLayout drawer = findViewById(R.id.activity_book_list_drawer_layout_root);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

        int id = menuItem.getItemId();

        switch (id) {
            case R.id.menu_drawer_log_out: {
                logout();
            } break;
        }

        return true;
    }

    private void addBookListFragment() {
        BookListFragment bookListFragment = BookListFragment.newInstance();

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.activity_book_list_frame_layout, bookListFragment);
        // this fragment should always be underneath
        transaction.commit();
    }

    private void logout() {
        SessionDAO.invalidateSession(this);
        ActivityStarter.startLoginActivity(this);

        finish();
    }
}
