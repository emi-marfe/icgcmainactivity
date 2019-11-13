package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;



public class Web extends AppCompatActivity {

    WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);


        // Locate the WebView in activity_webView.xml
        webView = findViewById(R.id.activity_web);
        // Force links and redirects to open in the WebView instead of in a browser
        webView.setWebViewClient(new WebViewClient());
        // Enable Javascript to run in WebView
        webView.getSettings().setJavaScriptEnabled(true);
        // Allow Zoom in/out controls
        webView.getSettings().setBuiltInZoomControls(true);
        // Zoom out the best fit screen
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setUseWideViewPort(true);
        // Load URL
        webView.setWebViewClient(new WebViewClient());
        webView.loadUrl("https://www.centralgospel.com/");
        // Load Resource
        // webView.loadUrl("file://android_asset/index.html");
    }

    // Prevent the back-button from closing the app
    @Override
    public void onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack();
        } else {
            super.onBackPressed();
        }
    }

   /* @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home)
        {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    } */
}
