package com.example.nayanjyoti.androidnewsapp;


import android.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import dmax.dialog.SpotsDialog;

public class DetailsArticle extends AppCompatActivity {

    WebView webView;
    AlertDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_article);

        dialog = new  SpotsDialog.Builder().setContext(this).build();
        dialog.show();

        webView = findViewById(R.id.web_view);
        webView.getSettings().getJavaScriptEnabled();
        webView.setWebChromeClient(new WebChromeClient());
        webView.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageFinished(WebView view, String url) {
                dialog.dismiss();
            }
        });

        if(getIntent() != null){
            if(!getIntent().getStringExtra("webURL").isEmpty()){
                webView.loadUrl(getIntent().getStringExtra("webURL"));
            }
        }
    }
}
