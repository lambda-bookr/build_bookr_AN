package com.isra.israel.build_week_1_bookr.json_object;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LoginReply {

    @SerializedName("token")
    @Expose
    public String token;

    @SerializedName("userID")
    @Expose
    public int userId;

}
