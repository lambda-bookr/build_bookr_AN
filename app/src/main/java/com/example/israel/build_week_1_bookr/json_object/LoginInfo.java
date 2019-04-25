package com.example.israel.build_week_1_bookr.json_object;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LoginInfo {

    public LoginInfo(String username, String password) {
        this.username = username;
        this.password = password;
    }

    @SerializedName("username")
    @Expose
    private String username;

    @SerializedName("password")
    @Expose
    private String password;
}
