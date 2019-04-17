package com.example.israel.build_week_1_bookr.dao;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.WorkerThread;

import com.example.israel.build_week_1_bookr.CommonStatics;
import com.example.israel.build_week_1_bookr.model.Book;
import com.example.israel.build_week_1_bookr.model.Book2;
import com.example.israel.build_week_1_bookr.model.Review;
import com.example.israel.build_week_1_bookr.network.NetworkAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.util.ArrayList;

// TODO LOW move asynctask definition here
public class BookrAPIDAO {

    private static final String BASE_URL = CommonStatics.DATABASE_BASE_URL;
    private static final String BOOKS = "api/books/";
    private static final String REVIEWS = "reviews/";
    private static final String API_REVIEWS = "api/" + REVIEWS;

    private static final String KEY_JSON_ADD_REVIEW_BOOK_ID = "book_id";
    private static final String KEY_JSON_ADD_REVIEW_USER_ID = "user_id";
    private static final String KEY_JSON_ADD_REVIEW_RATING = "rating";
    private static final String KEY_JSON_ADD_REVIEW_REVIEW = "review";

    @WorkerThread
    @NonNull
    public static ArrayList<Book> getBookList() {
        ArrayList<Book> books = new ArrayList<>();

        String booksJsonStr = NetworkAdapter.httpRequestGET(BASE_URL + BOOKS);

        if (booksJsonStr == null) {
            return books;
        }

        try {
            JSONArray booksJsonArr = new JSONArray(booksJsonStr);
            books.ensureCapacity(booksJsonArr.length());
            for (int i = 0; i < booksJsonArr.length(); ++i) {
                JSONObject bookJson = booksJsonArr.getJSONObject(i);
                books.add(new Book(bookJson));
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return books;
    }

    @WorkerThread
    @Nullable
    public static Book2 getBook2(Book book) {
        String book2JsonStr = NetworkAdapter.httpRequestGET(BASE_URL + BOOKS + book.getId());
        if (book2JsonStr == null) {
            return null;
        }

        try {
            JSONObject book2Json = new JSONObject(book2JsonStr);
            return new Book2(book2Json);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    @WorkerThread
    @NonNull
    public static ArrayList<Review> getReviews(int bookId) {
        // TODO when pagination comes, it will start here
        ArrayList<Review> reviews = new ArrayList<>();
        String reviewsJsonStr = NetworkAdapter.httpRequestGET(BASE_URL + BOOKS + bookId + "/" + REVIEWS);
        if (reviewsJsonStr == null) {
            return reviews;
        }

        try {
            JSONArray reviewsJsonArr = new JSONArray(reviewsJsonStr);
            reviews.ensureCapacity(reviewsJsonArr.length());
            for (int i = 0; i < reviewsJsonArr.length(); ++i) {
                JSONObject reviewJson = reviewsJsonArr.getJSONObject(i);
                reviews.add(new Review(reviewJson));
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return reviews;
    }

    @WorkerThread
    public static boolean addReview(int bookId, int userId, int rating, String review) {
        JSONObject reviewJson = new JSONObject();
        try {
            reviewJson.put(KEY_JSON_ADD_REVIEW_BOOK_ID, bookId);
            reviewJson.put(KEY_JSON_ADD_REVIEW_USER_ID, userId);
            reviewJson.put(KEY_JSON_ADD_REVIEW_RATING, rating);
            reviewJson.put(KEY_JSON_ADD_REVIEW_REVIEW, review);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        NetworkAdapter.Result result = NetworkAdapter.httpRequestPOSTJson(BASE_URL + API_REVIEWS, reviewJson);
        if (result.responseCode == NetworkAdapter.Result.INVALID_RESPONSE_CODE) {
            return false;
        }

        return result.responseCode == HttpURLConnection.HTTP_CREATED;
    }

    @WorkerThread
    @Nullable
    public static JSONObject removeReview(int reviewId) {
        NetworkAdapter.Result result = NetworkAdapter.httpRequestDEL(BASE_URL + API_REVIEWS + reviewId);
        if (result.responseCode == NetworkAdapter.Result.INVALID_RESPONSE_CODE) {
            return null;
        }

        if (result.responseCode == HttpURLConnection.HTTP_OK) {
            try {
                return new JSONObject((String)result.resultObj);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return null;
    }

}
