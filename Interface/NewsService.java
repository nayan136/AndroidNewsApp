package com.example.nayanjyoti.androidnewsapp.Interface;

import com.example.nayanjyoti.androidnewsapp.Model.News;
import com.example.nayanjyoti.androidnewsapp.Model.WebSite;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;

public interface NewsService {

    @GET("v1/sources?language=en")
    Call<WebSite> getSources();

    @GET
    Call<News> getNewsArticle(@Url String url);
}
