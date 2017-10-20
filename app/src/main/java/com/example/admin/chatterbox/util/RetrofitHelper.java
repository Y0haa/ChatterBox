package com.example.admin.chatterbox.util;

import com.example.admin.chatterbox.model.giphy.GiphyResponse;
import com.example.admin.chatterbox.model.giphyrand.GiphyRandomResponse;
import com.example.admin.chatterbox.model.giphytrend.GiphyTrendingResponse;

import io.reactivex.Observable;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by admin on 10/19/2017.
 */

public class RetrofitHelper {

    public static final String GIPHY_BASE_URL = "https://api.giphy.com/";

    public static Retrofit create(){
        return  new Retrofit.Builder()
                .baseUrl(GIPHY_BASE_URL)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public static Observable<Response<GiphyResponse>> createGiphyTranslate(String input) {
        Retrofit retrofit = create();
        ApiService apiService = retrofit.create(ApiService.class);
        return apiService.getGiphyTranslate(KeyContract.GIPHY_KEY, input);
    }

    public static Observable<Response<GiphyTrendingResponse>> createGiphyTrending() {
        Retrofit retrofit = create();
        ApiService apiService = retrofit.create(ApiService.class);
        return apiService.getGiphyTrending(KeyContract.GIPHY_KEY, "1", "PG-13");
    }

    public static Observable<Response<GiphyRandomResponse>> createGiphyRandom(String tag) {
        Retrofit retrofit = create();
        ApiService apiService = retrofit.create(ApiService.class);
        return apiService.getGiphyRandom(KeyContract.GIPHY_KEY, tag, "PG-13");
    }

    interface ApiService{

        @GET("v1/gifs/translate?")
        Observable<Response<GiphyResponse>> getGiphyTranslate(
                @Query("api_key") String key, @Query("s") String s);

        @GET("v1/gifs/trending?")
        Observable<Response<GiphyTrendingResponse>> getGiphyTrending(
                @Query("api_key") String key, @Query("limit") String limit, @Query("rating") String rating);

        @GET("v1/gifs/random?")
        Observable<Response<GiphyRandomResponse>> getGiphyRandom(
                @Query("api_key") String key, @Query("tag") String tag, @Query("rating") String rating);
    }

}
