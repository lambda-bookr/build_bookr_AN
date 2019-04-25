package com.example.israel.build_week_1_bookr.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.json.JSONException;
import org.json.JSONObject;

import javax.annotation.Generated;

public class Book implements Parcelable {

    public Book() {
    }

    public Book(int userId, String author, String title, double price, String publisher, String description, String imageUrl) {
        this.userId = userId;
        this.author = author;
        this.title = title;
        this.price = price;
        this.publisher = publisher;
        this.description = description;
        this.imageUrl = imageUrl;
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
        averageRating = in.readDouble();
    }

    @SerializedName("id")
    @Expose(serialize = false) // don't send it, only receive it
    private int id;

    @SerializedName("user_id")
    @Expose
    private int userId;

    @SerializedName("author")
    @Expose
    private String author;

    @SerializedName("title")
    @Expose
    private String title;

    @SerializedName("price")
    @Expose
    private double price;

    @SerializedName("publisher")
    @Expose
    private String publisher;

    @SerializedName("description")
    @Expose
    private String description;

    @SerializedName("imageUrl")
    @Expose
    private String imageUrl;

    @SerializedName("rating")
    @Expose(serialize = false)
    private double averageRating;

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

    public double getAverageRating() {
        return averageRating;
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
        dest.writeDouble(averageRating);
    }
}
