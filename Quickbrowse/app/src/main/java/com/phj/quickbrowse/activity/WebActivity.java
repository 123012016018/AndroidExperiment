package com.phj.quickbrowse.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.phj.quickbrowse.R;

public class WebActivity extends Activity {
    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.web_activity);
        webView = findViewById(R.id.webView);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        String url = bundle.getString("callback");
        String cookie = bundle.getString("cookie");
        startBrowser(url,cookie);

//        startBrowser(url.toString());
    }

    private void startBrowser(String url,String cookie) {
        if(url==null) return;
        setCookie(url,cookie);
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
