package com.example.israel.build_week_1_bookr.json_object;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RegistrationReply {

    @SerializedName("token")
    @Expose
    public String token;
}
