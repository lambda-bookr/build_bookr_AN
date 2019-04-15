package com.example.israel.build_week_1_bookr.dao;

import android.content.Context;
import android.content.SharedPreferences;

public class SessionTokenDAO {

    private static final String NAME_SP = "session_token";
    private static final String KEY_SESSION_TOKEN = "session_token";

    private static SharedPreferences getSP(Context context) {
        return context.getSharedPreferences(NAME_SP, Context.MODE_PRIVATE);
    }

    public static String getSessionToken(Context context) {
        SharedPreferences sp = getSP(context);
        return sp.getString(KEY_SESSION_TOKEN, "");
    }

    public static void setSessionToken(Context context, String sessionToken) {
        SharedPreferences sp = getSP(context);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(KEY_SESSION_TOKEN, sessionToken);
        editor.apply();
    }

}
