package com.isra.israel.build_week_1_bookr.dao;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.MainThread;
import android.support.annotation.Nullable;

import com.isra.israel.build_week_1_bookr.model.UserInfo;
import com.google.gson.Gson;

public class SessionDAO {

    private static final String NAME_SP = "session_token";
    private static final String KEY_SESSION_TOKEN = "session_token";
    private static final String KEY_SESSION_CREATED_TIME = "session_created_time";
    private static final long TIME_MILLIS_SESSION_VALID_MAX = (24 * 3600 * 7) - 3600; // 7 days minus 1 hour

    private static final String KEY_USER_INFO = "user_info";

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
        return elapsedTime < TIME_MILLIS_SESSION_VALID_MAX;
    }

    @MainThread
    public static void invalidateSession(Context context) {
        SharedPreferences sp = getSP(context);
        SharedPreferences.Editor editor = sp.edit();
        editor.putLong(KEY_SESSION_CREATED_TIME, Long.MAX_VALUE);
        editor.apply();
    }

    @MainThread
    public static void setUserInfo(Context context, UserInfo userInfo) {
        SharedPreferences sp = getSP(context);
        SharedPreferences.Editor editor = sp.edit();

        Gson gson = new Gson();
        editor.putString(KEY_USER_INFO, gson.toJson(userInfo));
        editor.apply();
    }

    @MainThread
    @Nullable
    public static UserInfo getUserInfo(Context context) {
        String userInfoJsonStr = getSP(context).getString(KEY_USER_INFO, null);
        Gson gson = new Gson();
        return gson.fromJson(userInfoJsonStr, UserInfo.class);
    }

}
