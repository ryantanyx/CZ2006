package com.example.myapplication;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiInterface {

    @GET("everything?q=(education OR student OR MOE OR PSLE) AND singapore&domains=straitstimes.com,bloomberg.com,channelnewsasia.com&sortBy=publishedAt&apiKey=6a5074d4afa44275a25bce2662fa7bad")
    Call<News> getNews(

    );
}
