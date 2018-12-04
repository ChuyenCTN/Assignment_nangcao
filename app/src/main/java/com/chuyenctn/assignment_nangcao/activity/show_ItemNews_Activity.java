package com.chuyenctn.assignment_nangcao.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.chuyenctn.assignment_nangcao.R;

public class show_ItemNews_Activity extends AppCompatActivity {
    private WebView webviewNews;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show__item_news);
        webviewNews = (WebView) findViewById(R.id.webview_news);
        Intent  intent=getIntent();
        String link=intent.getStringExtra("linknews");
      webviewNews.loadUrl(link);
      webviewNews.setWebViewClient(new WebViewClient());
    }
}
