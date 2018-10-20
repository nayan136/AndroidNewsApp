package com.example.nayanjyoti.androidnewsapp.Common;

import com.example.nayanjyoti.androidnewsapp.Interface.IconBetterIdeaService;
import com.example.nayanjyoti.androidnewsapp.Interface.NewsService;
import com.example.nayanjyoti.androidnewsapp.Remote.IconBetterIdeaClient;
import com.example.nayanjyoti.androidnewsapp.Remote.RetrofitClient;

public class Common {

    private static final String BASE_URL="https://newsapi.org/";
    public static final String API_KEY = "e1523ebbb3984e54b477b5dd15596c09";

    public static NewsService getNewsService(){
        return RetrofitClient.getClient(BASE_URL).create(NewsService.class);
    }

    public static IconBetterIdeaService getIconService(){
        return IconBetterIdeaClient.getClient().create(IconBetterIdeaService.class);
    }

//    https://newsapi.org/v1/articles?source=the-verge&apiKey=e1523ebbb3984e54b477b5dd15596c09
    public static String getAPIUrl(String source, String sortBy, String apiKey){

        StringBuilder apiUrl = new StringBuilder("https://newsapi.org/v1/articles?source=");
        return  apiUrl.append(source)
                .append("&sortBy=")
                .append(sortBy)
                .append("&apiKey=")
                .append(apiKey)
                .toString();
    }
}
