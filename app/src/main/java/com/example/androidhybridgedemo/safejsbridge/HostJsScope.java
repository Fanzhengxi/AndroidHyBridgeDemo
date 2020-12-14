package com.example.androidhybridgedemo.safejsbridge;

import android.webkit.WebView;
import android.widget.Toast;

import com.example.androidhybridgedemo.util.ToastUtil;

/**
 * 暴露给JS层的类，类中的方法可供JS调用，方法具有统一的格式
 */
public class HostJsScope {
    public static void toast(WebView webView,String message){
        ToastUtil.toastMsg(webView.getContext(),message);
    }
}
