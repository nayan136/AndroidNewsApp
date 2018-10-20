package com.example.nayanjyoti.androidnewsapp;


import android.app.AlertDialog;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.example.nayanjyoti.androidnewsapp.Adapter.ListSourceAdapter;
import com.example.nayanjyoti.androidnewsapp.Common.Common;
import com.example.nayanjyoti.androidnewsapp.Interface.NewsService;
import com.example.nayanjyoti.androidnewsapp.Model.WebSite;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import dmax.dialog.SpotsDialog;
import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    RecyclerView listWebsite;
    RecyclerView.LayoutManager layoutManager;
    NewsService mService;
    ListSourceAdapter adapter;
    AlertDialog dialog;
    SwipeRefreshLayout swipeLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//      Init cache
        Paper.init(this);

//      Init Service
        mService = Common.getNewsService();

//      Init view
        swipeLayout = findViewById(R.id.swipeRefresh);
        swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadWebsiteResource(true);
            }
        });

        listWebsite = findViewById(R.id.list_source);
        listWebsite.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        listWebsite.setLayoutManager(layoutManager);
        dialog = new  SpotsDialog.Builder().setContext(this).build();

        loadWebsiteResource(false);
    }

    private void loadWebsiteResource(boolean isRefreshed) {
        if(!isRefreshed){
            String cache = Paper.book().read("cache-news");

            if(cache != null && !cache.isEmpty()){
                Log.d("source_data", "Cache present");
                Log.d("source_data", cache);
                WebSite webSite = new Gson().fromJson(cache,WebSite.class);//convert cache from json to object
                adapter = new ListSourceAdapter(getBaseContext(),webSite);
                adapter.notifyDataSetChanged();
                listWebsite.setAdapter(adapter);
            }else{ // if no cache
                dialog.show();
//              Fetch new data
                mService.getSources().enqueue(new Callback<WebSite>() {
                    @Override
                    public void onResponse(Call<WebSite> call, Response<WebSite> response) {
                        Log.d("source_data", "Cache Absent");
                        adapter = new ListSourceAdapter(getBaseContext(),response.body());
                        adapter.notifyDataSetChanged();
                        listWebsite.setAdapter(adapter);
                        Log.d("source_data",response.body().getStatus());

//                      dialog dismiss
                        dialog.dismiss();

//                      save to cache
                        Paper.book().write("cache-news", new Gson().toJson(response.body()));
                    }

                    @Override
                    public void onFailure(Call<WebSite> call, Throwable t) {
                        Toast.makeText(getBaseContext(),"Error",Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }else{ // when swipe to refresh
            dialog.show();
//              Fetch new data
            mService.getSources().enqueue(new Callback<WebSite>() {
                @Override
                public void onResponse(Call<WebSite> call, Response<WebSite> response) {
                    adapter = new ListSourceAdapter(getBaseContext(),response.body());
                    adapter.notifyDataSetChanged();
                    listWebsite.setAdapter(adapter);

//                  save to cache
                    Paper.book().write("cache", new Gson().toJson(response.body()));

//                  dialog dismiss
                    dialog.dismiss();

//                  Dismiss refresh progressing
                    swipeLayout.setRefreshing(false);
                }

                @Override
                public void onFailure(Call<WebSite> call, Throwable t) {

                }
            });
        }
    }
}
