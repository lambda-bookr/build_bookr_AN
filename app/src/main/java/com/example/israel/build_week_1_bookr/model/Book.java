package com.example.israel.build_week_1_bookr.model;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

public class Book implements Parcelable {

    public static final int INVALID_ID = -1;
    private static final String KEY_JSON_ID = "id";
    private static final String KEY_JSON_USER_ID = "user_id";
    private static final String KEY_JSON_AUTHOR = "author";
    private static final String KEY_JSON_TITLE = "title";
    private static final String KEY_JSON_PRICE = "price";
    private static final String KEY_JSON_PUBLISHER = "publisher";
    private static final String KEY_JSON_DESCRIPTION = "description";
    private static final String KEY_JSON_IMAGE_URL = "imageurl";

    public Book() {

    }

    public Book(JSONObject bookJson) {
        try {
            id = bookJson.getInt(KEY_JSON_ID);
        } catch (JSONException e) {
            e.printStackTrace();
            id = INVALID_ID;
        }

        try {
            userId = bookJson.getInt(KEY_JSON_USER_ID);
        } catch (JSONException e) {
            e.printStackTrace();
            userId = INVALID_ID;
        }

        try {
            author = bookJson.getString(KEY_JSON_AUTHOR);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            title = bookJson.getString(KEY_JSON_TITLE);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            price = bookJson.getDouble(KEY_JSON_PRICE);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            publisher = bookJson.getString(KEY_JSON_PUBLISHER);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            description = bookJson.getString(KEY_JSON_DESCRIPTION);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            imageUrl = bookJson.getString(KEY_JSON_IMAGE_URL);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    protected Book(Parcel in) {
        id = in.readInt();
        userId = in.readInt();
        author = in.readString();
        title = in.readString();
        price = in.readDouble();
        publisher = in.readString();
        description = in.readString();
        imageUrl = in.readString();
    }

    private int id;
    private int userId;
    private String author;
    private String title;
    private double price;
    private String publisher;
    private String description;
    private String imageUrl;

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

    public int getId() {
        return id;
    }

    public int getUserId() {
        return userId;
    }

    public String getAuthor() {
        return author;
    }

    public String getTitle() {
        return title;
    }

    public double getPrice() {
        return price;
    }

    public String getPublisher() {
        return publisher;
    }

    public String getDescription() {
        return description;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeInt(userId);
        dest.writeString(author);
        dest.writeString(title);
        dest.writeDouble(price);
        dest.writeString(publisher);
        dest.writeString(description);
        dest.writeString(imageUrl);
    }
}
