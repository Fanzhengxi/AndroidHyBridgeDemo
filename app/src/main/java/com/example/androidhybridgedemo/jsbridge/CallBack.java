package com.example.androidhybridgedemo.jsbridge;

import android.webkit.WebView;

import org.json.JSONObject;

public class CallBack {
    private String mPort;
    private WebView mWebView;
    public CallBack(WebView webView,String port){
        this.mWebView=webView;
        this.mPort=port;
    }
    public void apply(JSONObject jsonObject){
        if (mWebView!=null){
            //调用js的方法onAndroidFinished
            mWebView.loadUrl("javascript:onAndroidFinished('mPort',String.valueOf(jsonObject))");
        }
    }
}
