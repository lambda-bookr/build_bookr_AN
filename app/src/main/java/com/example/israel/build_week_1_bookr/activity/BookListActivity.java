package com.example.israel.build_week_1_bookr.activity;

import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.israel.build_week_1_bookr.R;
import com.example.israel.build_week_1_bookr.controller.ActivityStarter;
import com.example.israel.build_week_1_bookr.dao.SessionTokenDAO;
import com.example.israel.build_week_1_bookr.fragment.BookListFragment;

// TODO refresh functionality when pulling down
public class BookListActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // TODO animation

        setContentView(R.layout.activity_book_list);

        // TODO create a hard code here
        BookListFragment.replaceBookListFragment(this, R.id.activity_book_list_frame_layout);

        NavigationView navigationView = findViewById(R.id.activity_book_list_nav_view);
        navigationView.setNavigationItemSelectedListener(this);

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

    private void logout() {
        SessionTokenDAO.invalidateSession(this);
        ActivityStarter.startLoginActivity(this);

        finish();
    }
}
