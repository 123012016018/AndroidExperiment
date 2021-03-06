package com.example.mybrower;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    private static final String JWGL_URL = "http://jwgl.fjnu.edu.cn/";
    private static final String COOKIE_TEST = "UM_distinctid=1696fc8ec3e1b0-0585ff9addbe6b-1333062-100200-1696fc8ec3f2f4; ASP.NET_SessionId=u0yuy155z3nwod45twj5gbmy; Secure";

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
        setCookie(JWGL_URL,COOKIE_TEST);
        webView.loadUrl(url);
        webView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setUseWideViewPort(true);
        settings.setLoadWithOverviewMode(true);
        settings.setSupportZoom(true);
        settings.setBuiltInZoomControls(true);
    }

    private void setCookie(String url, String cookie) {
        CookieSyncManager.createInstance(this);
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.setAcceptCookie(true);
        cookieManager.removeSessionCookie();
        cookieManager.removeAllCookie();
        cookieManager.setCookie(getDomain(url), cookie);
        CookieSyncManager.getInstance().sync();
    }

    private String getDomain(String url) {
        url = url.replaceAll("http://", "")
                .replaceAll("https://", "");
        if (url.contains("/")) {
            url = url.substring(0, url.indexOf("/"));
        }
        return url;
    }


}
