package com.example.israel.build_week_1_bookr.dao;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.MainThread;

public class SessionTokenDAO {

    private static final String NAME_SP = "session_token";
    private static final String KEY_SESSION_TOKEN = "session_token";
    private static final String KEY_SESSION_CREATED_TIME = "session_created_time";
    private static final long TIME_MILLIS_SESSION_VALID_MAX = (24 * 3600 * 7) - 3600; // 7 days minus 1 hour

    private static SharedPreferences getSP(Context context) {
        return context.getSharedPreferences(NAME_SP, Context.MODE_PRIVATE);
    }

    @MainThread
    public static String getSessionToken(Context context) {
        SharedPreferences sp = getSP(context);
        return sp.getString(KEY_SESSION_TOKEN, "");
    }

    @MainThread
    public static void setSessionToken(Context context, String sessionToken) {
        SharedPreferences sp = getSP(context);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(KEY_SESSION_TOKEN, sessionToken);
        editor.putLong(KEY_SESSION_CREATED_TIME, System.currentTimeMillis());
        editor.apply();
    }

    @MainThread
    public static boolean isSessionValid(Context context) {
        SharedPreferences sp = getSP(context);
        long currentTime = System.currentTimeMillis();
        long elapsedTime = sp.getLong(KEY_SESSION_CREATED_TIME, Long.MAX_VALUE) - currentTime;
        return TIME_MILLIS_SESSION_VALID_MAX < elapsedTime;
    }

    @MainThread
    public static void invalidateSession(Context context) {
        SharedPreferences sp = getSP(context);
        SharedPreferences.Editor editor = sp.edit();
        editor.putLong(KEY_SESSION_CREATED_TIME, Long.MAX_VALUE);
        editor.apply();
    }

}
