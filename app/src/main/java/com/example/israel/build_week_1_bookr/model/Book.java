package com.example.israel.build_week_1_bookr.model;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONObject;

public class Book implements Parcelable {

    // TODO incomplete
    public Book(String title) {
        this.title = title;
    }

    public Book(JSONObject bookJson) {
        // TODO
    }

    // TODO do not forget parcel
    private String title;

    protected Book(Parcel in) {
        title = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Book> CREATOR = new Creator<Book>() {
        @Override
        public Book createFromParcel(Parcel in) {
            return new Book(in);
        }

        @Override
        public Book[] newArray(int size) {
            return new Book[size];
        }
    };

    public String getTitle() {
        return title;
    }

}
