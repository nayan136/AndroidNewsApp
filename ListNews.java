package com.example.nayanjyoti.androidnewsapp;

import android.app.AlertDialog;
import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.nayanjyoti.androidnewsapp.Adapter.ListNewsAdapter;
import com.example.nayanjyoti.androidnewsapp.Common.Common;
import com.example.nayanjyoti.androidnewsapp.Interface.NewsService;
import com.example.nayanjyoti.androidnewsapp.Model.Article;
import com.example.nayanjyoti.androidnewsapp.Model.News;
import com.flaviofaria.kenburnsview.KenBurnsView;
import com.github.florent37.diagonallayout.DiagonalLayout;
import com.squareup.picasso.Picasso;

import java.util.List;

import dmax.dialog.SpotsDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ListNews extends AppCompatActivity {

    KenBurnsView kbv;
    DiagonalLayout diagonalLayout;
    AlertDialog dialog;
    NewsService mService;
    TextView topAuthor, topTitle;
    SwipeRefreshLayout swipeRefreshLayout;

    String source="", sortBy="", webHostUrl="";

    ListNewsAdapter adapter;
    RecyclerView listNews;
    RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_news);

//      service
        mService = Common.getNewsService();

        dialog = new  SpotsDialog.Builder().setContext(this).build();

//      view
        swipeRefreshLayout = findViewById(R.id.swipeRefresh);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadNews(source, true);
            }
        });
        diagonalLayout = findViewById(R.id.diagonal_layout);
        diagonalLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//              click for latest news
                Intent detail = new Intent(getBaseContext(), DetailsArticle.class);
                detail.putExtra("webURL",webHostUrl);
                startActivity(detail);

            }
        });
        kbv = findViewById(R.id.top_image);
        topAuthor = findViewById(R.id.top_author);
        topTitle = findViewById(R.id.top_title);

        listNews = findViewById(R.id.list_news);
        listNews.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        listNews.setLayoutManager(layoutManager);

        if(getIntent() != null){
            source = getIntent().getStringExtra("source");
            //sortBy = getIntent().getStringExtra("sortBy");
            sortBy = "top";
            if(!source.isEmpty() && !sortBy.isEmpty()){
                loadNews(source,false);
            }
        }
    }

    private void loadNews(String source, boolean isRefreshed) {

        if(!isRefreshed){
            dialog.show();
            Log.d("source_data_news", Common.getAPIUrl(source,sortBy,Common.API_KEY));
            mService.getNewsArticle(Common.getAPIUrl(source,sortBy,Common.API_KEY))
                    .enqueue(new Callback<News>() {
                        @Override
                        public void onResponse(Call<News> call, Response<News> response) {
                            dialog.dismiss();

                            Picasso.get()
                                    .load(response.body().getArticles().get(0).getUrlToImage())
                                    .into(kbv);

                            topTitle.setText(response.body().getArticles().get(0).getTitle());
                            topAuthor.setText(response.body().getArticles().get(0).getAuthor());
                            webHostUrl = response.body().getArticles().get(0).getUrl();

//                          Load remaining articles
//                          remove first article as it is already loaded
                            List<Article> removeFirstItem = response.body().getArticles();
                            removeFirstItem.remove(0);
                            adapter = new ListNewsAdapter(removeFirstItem,getBaseContext());
                            adapter.notifyDataSetChanged();
                            listNews.setAdapter(adapter);
                        }

                        @Override
                        public void onFailure(Call<News> call, Throwable t) {

                        }
                    });
        }
    }
}
