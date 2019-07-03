package com.isra.israel.build_week_1_bookr.controller;

import android.content.Context;
import android.content.Intent;

import com.isra.israel.build_week_1_bookr.activity.BookListActivity;
import com.isra.israel.build_week_1_bookr.activity.LoginActivity;

public class ActivityStarter {

    public static void startLoginActivity(Context context) {
        Intent intent = new Intent(context, LoginActivity.class);
        context.startActivity(intent);
    }

    public static void startBookListActivity(Context context) {
        Intent intent = new Intent(context, BookListActivity.class);
        context.startActivity(intent);
    }

}
