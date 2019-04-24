package com.example.mybrower;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        webView = findViewById(R.id.webView);
        Intent intent = getIntent();
        Uri uri = intent.getData();
        if (uri==null) return;
        URL url = null;
        try {
            url = new URL(uri.getScheme(), uri.getHost(), uri.getPath());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        startBrowser(url.toString());
    }

    private void startBrowser(String url) {
        if(url==null) return;
        webView.loadUrl(url);
        webView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });

    }


}
