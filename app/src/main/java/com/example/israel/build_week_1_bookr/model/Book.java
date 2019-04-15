package com.example.israel.build_week_1_bookr.model;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

// TODO reviews
public class Book implements Parcelable {

    public static final int INVALID_ID = -1;

    public Book() {

    }

    public Book(JSONObject bookJson) {
        try {
            id = bookJson.getInt("id");
        } catch (JSONException e) {
            e.printStackTrace();
            id = INVALID_ID;
        }

        try {
            userId = bookJson.getInt("user_id");
        } catch (JSONException e) {
            e.printStackTrace();
            userId = INVALID_ID;
        }

        try {
            author = bookJson.getString("author");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            title = bookJson.getString("name");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            price = bookJson.getDouble("price");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            publisher = bookJson.getString("publisher");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            description = bookJson.getString("description");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            imageUrl = bookJson.getString("imageUrl");
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

    // TODO do not forget parcel
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
