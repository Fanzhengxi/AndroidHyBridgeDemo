package com.example.mvc.model;

import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * Created by fan.zx
 * Date: 2020/12/29 15:05
 * Describe:
 */
public class LoadPagesModelImpl implements LoadPagesModel{

    @Override
    public void loadPages(WebView webView, String url) {
        webView.setWebViewClient(new WebViewClient());
//        webView.setWebChromeClient(new WebChromeClient());//设置ChromeClient，支持弹出对话框，否则无法弹出
        webView.loadUrl(url);
        webView.getSettings().setJavaScriptEnabled(true);
    }
}
