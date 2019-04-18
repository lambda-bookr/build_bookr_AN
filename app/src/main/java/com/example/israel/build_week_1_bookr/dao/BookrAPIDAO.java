package com.example.israel.build_week_1_bookr.dao;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.WorkerThread;
import android.util.SparseArray;

import com.example.israel.build_week_1_bookr.model.Book;
import com.example.israel.build_week_1_bookr.model.Book2;
import com.example.israel.build_week_1_bookr.model.Review;
import com.example.israel.build_week_1_bookr.model.UserInfo;
import com.example.israel.build_week_1_bookr.network.NetworkAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

// TODO LOW move login/register here?
public class BookrAPIDAO {

    private static final String BASE_URL = "https://bookr-backend.herokuapp.com/";

    private static final String BOOKS = "api/books/";
    private static final String BOOK_REVIEWS = "reviews/";
    private static final String ADD_BOOK = "api/books/";
    private static final String KEY_JSON_ADD_BOOK_USER_ID = "user_id";
    private static final String KEY_JSON_ADD_BOOK_TITLE = "title";
    private static final String KEY_JSON_ADD_BOOK_AUTHOR = "author";
    private static final String KEY_JSON_ADD_BOOK_PUBLISHER = "publisher";
    private static final String KEY_JSON_ADD_BOOK_PRICE = "price";
    private static final String KEY_JSON_ADD_BOOK_DESCRIPTION = "description";
    private static final String KEY_JSON_ADD_BOOK_IMAGE_URL = "imageUrl";

    private static final String REVIEWS = "api/" + BOOK_REVIEWS;
    private static final String KEY_JSON_ADD_REVIEW_BOOK_ID = "book_id";
    private static final String KEY_JSON_ADD_REVIEW_USER_ID = "user_id";
    private static final String KEY_JSON_ADD_REVIEW_RATING = "rating";
    private static final String KEY_JSON_ADD_REVIEW_REVIEW = "review";

    static private final String REGISTER = "api/auth/register/";
    static private final String KEY_JSON_REGISTER_USERNAME = "username";
    static private final String KEY_JSON_REGISTER_PASSWORD = "password";
    static private final String KEY_JSON_REGISTER_FIRST_NAME = "firstName";
    static private final String KEY_JSON_REGISTER_LAST_NAME = "lastName";
    static private final String KEY_JSON_REGISTER_TOKEN = "token";

    static private final String LOGIN = "api/auth/login/";
    static private final String KEY_JSON_LOGIN_USERNAME = "username";
    static private final String KEY_JSON_LOGIN_PASSWORD = "password";
    static private final String KEY_JSON_LOGIN_TOKEN = "token";
    static private final String KEY_JSON_LOGIN_USER_ID = "userID";

    private static final String USERS = "api/users/";

    @WorkerThread
    @NonNull
    public static ArrayList<Book> getBookList(String token) {
        ArrayList<Book> books = new ArrayList<>();

        HashMap<String, String> header = new HashMap<>();
        header.put("Authorization", token);
        header.put("Content-Type", "application/json");

        String booksJsonStr = NetworkAdapter.httpRequest(BASE_URL + BOOKS, "GET", null, header);

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
    public static Book2 getBook2(String token, Book book) {
        HashMap<String, String> header = new HashMap<>();
        header.put("Authorization", token);
        header.put("Content-Type", "application/json");

        String book2JsonStr = NetworkAdapter.httpRequest(BASE_URL + BOOKS + book.getId(), "GET", null, header);
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
    @Nullable
    public static Book addBook(String token, int userId, String title, String author, String publisher, double price, String description, String imageUrl) {
        JSONObject outBookJson = new JSONObject();
        try {
            outBookJson.put(KEY_JSON_ADD_BOOK_USER_ID, userId);
            outBookJson.put(KEY_JSON_ADD_BOOK_TITLE, title);
            outBookJson.put(KEY_JSON_ADD_BOOK_AUTHOR, author);
            outBookJson.put(KEY_JSON_ADD_BOOK_PUBLISHER, publisher);
            outBookJson.put(KEY_JSON_ADD_BOOK_PRICE, price);
            outBookJson.put(KEY_JSON_ADD_BOOK_DESCRIPTION, description);
            outBookJson.put(KEY_JSON_ADD_BOOK_IMAGE_URL, imageUrl);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        HashMap<String, String> header = new HashMap<>();
        header.put("Authorization", token);
        header.put("Content-Type", "application/json");
        header.put("Accept", "application/json");

        String bookJsonStr = NetworkAdapter.httpRequest(BASE_URL + ADD_BOOK, "POST", outBookJson, header);

        if (bookJsonStr == null) {
            return null;
        }

        try {
            return new Book(new JSONObject(bookJsonStr));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    @WorkerThread
    @Nullable
    public static Book deleteBook(String token, int bookId) {

        HashMap<String, String> header = new HashMap<>();
        header.put("Authorization", token);
        header.put("Content-Type", "application/json");

        String bookJsonStr = NetworkAdapter.httpRequest(BASE_URL + BOOKS + Integer.toString(bookId), "DELETE", null, header);

        if (bookJsonStr == null) {
            return null;
        }

        try {
            return new Book(new JSONObject(bookJsonStr));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    @WorkerThread
    @NonNull
    public static ArrayList<Review> getReviews(String token, int bookId) {
        // TODO when pagination comes, it will start here
        ArrayList<Review> reviews = new ArrayList<>();

        HashMap<String, String> header = new HashMap<>();
        header.put("Authorization", token);
        header.put("Content-Type", "application/json");

        String reviewsJsonStr = NetworkAdapter.httpRequest(BASE_URL + BOOKS + bookId + "/" + BOOK_REVIEWS, "GET", null, header);

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
    @Nullable
    public static Review addReview(String token, int bookId, int userId, int rating, String review) {
        JSONObject outReviewJson = new JSONObject();
        try {
            outReviewJson.put(KEY_JSON_ADD_REVIEW_BOOK_ID, bookId);
            outReviewJson.put(KEY_JSON_ADD_REVIEW_USER_ID, userId);
            outReviewJson.put(KEY_JSON_ADD_REVIEW_RATING, rating);
            outReviewJson.put(KEY_JSON_ADD_REVIEW_REVIEW, review);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        HashMap<String, String> header = new HashMap<>();
        header.put("Authorization", token);
        header.put("Content-Type", "application/json");
        header.put("Accept", "application/json");

        String reviewJSONStr = NetworkAdapter.httpRequest(BASE_URL + REVIEWS, "POST", outReviewJson, header);

        if (reviewJSONStr == null) {
            return null;
        }

        try {
            return new Review(new JSONObject(reviewJSONStr));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    @WorkerThread
    @Nullable
    public static Review removeReview(String token, int reviewId) {
        HashMap<String, String> header = new HashMap<>();
        header.put("Authorization", token);
        header.put("Content-Type", "application/json");

        String reviewJsonStr = NetworkAdapter.httpRequest(BASE_URL + REVIEWS + reviewId, "DELETE", null, header);

        if (reviewJsonStr == null) {
            return null;
        }

        try {
            return new Review(new JSONObject(reviewJsonStr));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    @WorkerThread
    @Nullable
    public static String register(String username, String password, String firstName, String lastName) {
        JSONObject registerFormJson = new JSONObject();
        try {
            registerFormJson.put(KEY_JSON_REGISTER_USERNAME, username);
            registerFormJson.put(KEY_JSON_REGISTER_PASSWORD, password);
            registerFormJson.put(KEY_JSON_REGISTER_FIRST_NAME, firstName);
            registerFormJson.put(KEY_JSON_REGISTER_LAST_NAME, lastName);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        HashMap<String, String> header = new HashMap<>();
        header.put("Content-Type", "application/json");
        header.put("Accept", "application/json");

        String replyStr = NetworkAdapter.httpRequest(BASE_URL + REGISTER, "POST", registerFormJson, header);

        if (replyStr == null) {
            return null;
        }

        try {
            JSONObject replyJson = new JSONObject(replyStr);
            return replyJson.getString(KEY_JSON_REGISTER_TOKEN);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    @WorkerThread
    @Nullable
    public static SparseArray<String> login(String username, String password) {
        JSONObject credentialsJson = new JSONObject();
        try {
            credentialsJson.put(KEY_JSON_LOGIN_USERNAME, username);
            credentialsJson.put(KEY_JSON_LOGIN_PASSWORD, password);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        HashMap<String, String> header = new HashMap<>();
        header.put("Content-Type", "application/json");
        header.put("Accept", "application/json");

        String replyStr = NetworkAdapter.httpRequest(BASE_URL + LOGIN, "POST", credentialsJson, header);

        if (replyStr == null) {
            return null;
        }

        try {
            JSONObject replyJson = new JSONObject(replyStr);
            String token = replyJson.getString(KEY_JSON_LOGIN_TOKEN);
            int userId = replyJson.getInt(KEY_JSON_LOGIN_USER_ID);

            SparseArray<String> ret = new SparseArray<>();
            ret.put(userId, token);
            return ret;

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    @WorkerThread
    @Nullable
    public static UserInfo getUserInfo(int userId, String token) {
        HashMap<String, String> header = new HashMap<>();
        header.put("Authorization", token);
        header.put("Content-Type", "application/json");

        String reply = NetworkAdapter.httpRequest(BASE_URL + USERS + userId, "GET", null, header);

        if (reply == null) {
            return null;
        }

        try {
            return new UserInfo(new JSONObject(reply));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }



}
