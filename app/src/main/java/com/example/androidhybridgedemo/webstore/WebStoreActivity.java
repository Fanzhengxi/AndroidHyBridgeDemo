package com.example.androidhybridgedemo.webstore;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.webkit.WebView;

import com.example.androidhybridgedemo.R;
import com.example.androidhybridgedemo.safejsbridge.HostJsScope;
import com.example.androidhybridgedemo.safejsbridge.SafeChromeClient;

public class WebStoreActivity extends AppCompatActivity {
    private WebView webView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_store);
        webView=findViewById(R.id.webView);
        webView.getSettings().setJavaScriptEnabled(true);
        //给JS注入的接口名是HostApp,对应的类是HostJsScope
        webView.setWebChromeClient(new SafeChromeClient("HostApp", HostJsScope.class));
        webView.loadUrl("file:///android_asset/webstore.html");
    }
}