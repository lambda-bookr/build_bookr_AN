package com.example.israel.build_week_1_bookr.dao;

import android.support.annotation.Nullable;
import android.support.annotation.WorkerThread;
import android.util.SparseArray;

import com.example.israel.build_week_1_bookr.BuildConfig;
import com.example.israel.build_week_1_bookr.model.Book;
import com.example.israel.build_week_1_bookr.json_object.LoginInfo;
import com.example.israel.build_week_1_bookr.json_object.LoginReply;
import com.example.israel.build_week_1_bookr.json_object.OutReview;
import com.example.israel.build_week_1_bookr.json_object.RegistrationInfo;
import com.example.israel.build_week_1_bookr.json_object.RegistrationReply;
import com.example.israel.build_week_1_bookr.model.Review;
import com.example.israel.build_week_1_bookr.model.UserInfo;
import com.example.israel.build_week_1_bookr.network.NetworkAdapter;
import com.google.gson.GsonBuilder;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import rx.Observable;

// TODO LOW move login/register here?
public class BookrAPIDAO {

    private static final String BASE_URL = "https://bookr-backend.herokuapp.com/";
    private static final String BOOKS = "api/books/";
    private static final String BOOK_REVIEWS = "reviews/";
    private static final String REVIEWS = "api/" + BOOK_REVIEWS;
    static private final String REGISTER = "api/auth/register/";
    static private final String LOGIN = "api/auth/login/";
    private static final String USERS = "api/users/";

    private static final int READ_TIMEOUT = 3000;
    private static final int CONNECT_TIMEOUT = 3000;

    public static final BookrApiEndpointInterface apiService;
    static {
        //RxJava2CallAdapterFactory rxAdapterFactory = RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io());

        // implementation 'com.squareup.okhttp3:logging-interceptor:3.8.1'
        // only use in debug
//        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
//        interceptor.setLevel(BuildConfig.DEBUG ? HttpLoggingInterceptor.Level.BODY : HttpLoggingInterceptor.Level.NONE);

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .readTimeout(READ_TIMEOUT, TimeUnit.MILLISECONDS)
                .connectTimeout(CONNECT_TIMEOUT, TimeUnit.MILLISECONDS)
                //.addInterceptor(interceptor)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create()))
                //.addCallAdapterFactory(rxAdapterFactory)
                .client(okHttpClient)
                .build();
        apiService = retrofit.create(BookrApiEndpointInterface.class);
    }

    public interface BookrApiEndpointInterface {

        @Headers({"Content-Type: application/json"})
        @GET(BOOKS)
        Call<ArrayList<Book>> getBooks(@Header("Authorization") String token);

        @GET(BOOKS)
        Observable<ArrayList<Book>> getBooksRx(@Header("Authorization") String token);

        @Headers({"Content-Type: application/json", "Accept: application/json"})
        @POST(BOOKS)
        Call<Book> addBook(@Header("Authorization") String token, @Body Book book);

        @Headers({"Accept: application/json"})
        @DELETE(BOOKS + "{bookId}")
        Call<Book> deleteBook(@Header("Authorization") String token, @Path("bookId") int bookId);

        @Headers({"Content-Type: application/json"})
        @GET(BOOKS + "{bookId}/" + BOOK_REVIEWS)
        Call<ArrayList<Review>> getReviews(@Header("Authorization") String token, @Path("bookId") int bookId);

        @Headers({"Content-Type: application/json", "Accept: application/json"})
        @POST(REVIEWS)
        Call<Review> addReview(@Header("Authorization") String token, @Body OutReview outReview);

        @Headers({"Content-Type: application/json", "Accept: application/json"})
        @DELETE(REVIEWS + "{reviewId}")
        Call<Review> deleteReview(@Header("Authorization") String token, @Path("reviewId") int reviewId);

        @Headers({"Content-Type: application/json", "Accept: application/json"})
        @POST(REGISTER)
        Call<RegistrationReply> register(@Body RegistrationInfo registrationInfo);

        @Headers({"Content-Type: application/json", "Accept: application/json"})
        @POST(LOGIN)
        Call<LoginReply> login(@Body LoginInfo loginInfo);

        @Headers({"Content-Type: application/json", "Accept: application/json"})
        @GET(USERS + "{userId}")
        Call<UserInfo> getUserInfo(@Header("Authorization") String token, @Path("userId") int userId);

    }

}
