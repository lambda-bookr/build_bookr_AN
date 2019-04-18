package com.example.israel.build_week_1_bookr.dao;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.WorkerThread;
import android.util.SparseArray;

import com.example.israel.build_week_1_bookr.CommonStatics;
import com.example.israel.build_week_1_bookr.model.Book;
import com.example.israel.build_week_1_bookr.model.Book2;
import com.example.israel.build_week_1_bookr.model.Review;
import com.example.israel.build_week_1_bookr.model.UserInfo;
import com.example.israel.build_week_1_bookr.network.NetworkAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.HashMap;

// TODO LOW move login/register here?
public class BookrAPIDAO {

    private static final String BOOKS = "api/books/";
    private static final String BOOK_REVIEWS = "reviews/";
    private static final String REVIEWS = "api/" + BOOK_REVIEWS;
    private static final String USERS = "api/users/";

    private static final String ADD_BOOK = "api/books/";
    private static final String KEY_JSON_ADD_BOOK_USER_ID = "user_id";
    private static final String KEY_JSON_ADD_BOOK_TITLE = "title";
    private static final String KEY_JSON_ADD_BOOK_AUTHOR = "author";
    private static final String KEY_JSON_ADD_BOOK_PUBLISHER = "publisher";
    private static final String KEY_JSON_ADD_BOOK_PRICE = "price";
    private static final String KEY_JSON_ADD_BOOK_DESCRIPTION = "description";
    private static final String KEY_JSON_ADD_BOOK_IMAGE_URL = "imageUrl";

    private static final String KEY_JSON_ADD_REVIEW_BOOK_ID = "book_id";
    private static final String KEY_JSON_ADD_REVIEW_USER_ID = "user_id";
    private static final String KEY_JSON_ADD_REVIEW_RATING = "rating";
    private static final String KEY_JSON_ADD_REVIEW_REVIEW = "review";

    static private final String LOGIN = "api/auth/login/";
    static private final String KEY_JSON_LOGIN_USERNAME = "username";
    static private final String KEY_JSON_LOGIN_PASSWORD = "password";
    static private final String KEY_JSON_LOGIN_TOKEN = "token";
    static private final String KEY_JSON_LOGIN_USER_ID = "userID";

    static private final String REGISTER = "api/auth/register/";
    static private final String KEY_JSON_REGISTER_USERNAME = "username";
    static private final String KEY_JSON_REGISTER_PASSWORD = "password";
    static private final String KEY_JSON_REGISTER_FIRST_NAME = "firstName";
    static private final String KEY_JSON_REGISTER_LAST_NAME = "lastName";
    static private final String KEY_JSON_REGISTER_TOKEN = "token";

    @WorkerThread
    @NonNull
    public static ArrayList<Book> getBookList() {
        ArrayList<Book> books = new ArrayList<>();

        String booksJsonStr = NetworkAdapter.httpRequestGET(CommonStatics.DATABASE_BASE_URL + BOOKS);

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
        String book2JsonStr = NetworkAdapter.httpRequestGET(CommonStatics.DATABASE_BASE_URL + BOOKS + book.getId());
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
    public static Book addBook(int userId, String title, String author, String publisher, double price, String description, String imageUrl) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(KEY_JSON_ADD_BOOK_USER_ID, userId);
            jsonObject.put(KEY_JSON_ADD_BOOK_TITLE, title);
            jsonObject.put(KEY_JSON_ADD_BOOK_AUTHOR, author);
            jsonObject.put(KEY_JSON_ADD_BOOK_PUBLISHER, publisher);
            jsonObject.put(KEY_JSON_ADD_BOOK_PRICE, price);
            jsonObject.put(KEY_JSON_ADD_BOOK_DESCRIPTION, description);
            jsonObject.put(KEY_JSON_ADD_BOOK_IMAGE_URL, imageUrl);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        NetworkAdapter.Result networkResult = NetworkAdapter.httpRequestPOSTJson(CommonStatics.DATABASE_BASE_URL + ADD_BOOK, jsonObject);

        if (networkResult.responseCode == NetworkAdapter.Result.INVALID_RESPONSE_CODE) {
            return null;
        }

        if (networkResult.responseCode == HttpURLConnection.HTTP_CREATED) {
            try {
                return new Book(new JSONObject((String)networkResult.resultObj));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return null;
    }

    @WorkerThread
    @Nullable
    public static Book deleteBook(int bookId) {
        NetworkAdapter.Result networkResult = NetworkAdapter.httpRequestDEL(CommonStatics.DATABASE_BASE_URL + BOOKS + Integer.toString(bookId));
        if (networkResult.responseCode == NetworkAdapter.Result.INVALID_RESPONSE_CODE) {
            return null;
        }

        if (networkResult.responseCode == HttpURLConnection.HTTP_OK) {
            try {
                return new Book(new JSONObject((String)networkResult.resultObj));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return null;
    }

    @WorkerThread
    @NonNull
    public static ArrayList<Review> getReviews(int bookId) {
        // TODO when pagination comes, it will start here
        ArrayList<Review> reviews = new ArrayList<>();
        String reviewsJsonStr = NetworkAdapter.httpRequestGET(CommonStatics.DATABASE_BASE_URL + BOOKS + bookId + "/" + BOOK_REVIEWS);
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
    public static Review addReview(int bookId, int userId, int rating, String review) {
        JSONObject outReviewJson = new JSONObject();
        try {
            outReviewJson.put(KEY_JSON_ADD_REVIEW_BOOK_ID, bookId);
            outReviewJson.put(KEY_JSON_ADD_REVIEW_USER_ID, userId);
            outReviewJson.put(KEY_JSON_ADD_REVIEW_RATING, rating);
            outReviewJson.put(KEY_JSON_ADD_REVIEW_REVIEW, review);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        NetworkAdapter.Result result = NetworkAdapter.httpRequestPOSTJson(CommonStatics.DATABASE_BASE_URL + REVIEWS, outReviewJson);
        if (result.responseCode == NetworkAdapter.Result.INVALID_RESPONSE_CODE) {
            return null;
        }

        if (result.responseCode == HttpURLConnection.HTTP_CREATED) {
            try {
                return new Review(new JSONObject((String)result.resultObj));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return null;
    }

    @WorkerThread
    @Nullable
    public static Review removeReview(int reviewId) {
        NetworkAdapter.Result result = NetworkAdapter.httpRequestDEL(CommonStatics.DATABASE_BASE_URL + REVIEWS + reviewId);
        if (result.responseCode == NetworkAdapter.Result.INVALID_RESPONSE_CODE) {
            return null;
        }

        if (result.responseCode == HttpURLConnection.HTTP_OK) {
            try {
                return new Review(new JSONObject((String)result.resultObj));
            } catch (JSONException e) {
                e.printStackTrace();
            }
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

        NetworkAdapter.Result requestResult = NetworkAdapter.httpRequestPOSTJson(CommonStatics.DATABASE_BASE_URL + LOGIN, credentialsJson);

        // unknown error
        if (requestResult.responseCode == NetworkAdapter.Result.INVALID_RESPONSE_CODE) {
            return null;
        }

        // cannot connect to server
        if (requestResult.responseCode == HttpURLConnection.HTTP_NOT_FOUND) {
            return null;
        }

        if (requestResult.responseCode == HttpURLConnection.HTTP_CREATED) { // successful log in
            String replyStr = (String)requestResult.resultObj;
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
        }

        return null;
    }

    @WorkerThread
    @Nullable
    public static UserInfo getUserInfo(int userId, String token) {
        HashMap<String, String> header = new HashMap<>();
        header.put("Authorization", token);
        header.put("Content-Type", "application/json");

        String reply = NetworkAdapter.httpRequest(CommonStatics.DATABASE_BASE_URL + USERS + userId, "GET", null, header);

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

        NetworkAdapter.Result requestResult = NetworkAdapter.httpRequestPOSTJson(CommonStatics.DATABASE_BASE_URL + REGISTER, registerFormJson);
        // unknown error
        if (requestResult.responseCode == NetworkAdapter.Result.INVALID_RESPONSE_CODE) {
            return null;
        }

        if (requestResult.responseCode == HttpURLConnection.HTTP_CREATED) { // success
            String replyStr = (String)requestResult.resultObj;

            try {
                JSONObject replyJson = new JSONObject(replyStr);
                return replyJson.getString(KEY_JSON_REGISTER_TOKEN);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return null;
    }


}
