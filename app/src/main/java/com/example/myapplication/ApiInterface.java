package com.example.myapplication;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiInterface {
    static final String NEWS_API_KEY = BuildConfig.NewsApiKey;

    @GET("everything?q=(education OR student OR MOE OR PSLE) AND singapore&domains=straitstimes.com,bloomberg.com,channelnewsasia.com&sortBy=publishedAt&apiKey=" + NEWS_API_KEY)
    Call<News> getNews(

    );
}
