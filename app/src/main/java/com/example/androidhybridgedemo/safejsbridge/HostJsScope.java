package com.example.androidhybridgedemo.safejsbridge;

import android.os.Handler;
import android.os.Looper;
import android.webkit.WebView;
import android.widget.Toast;
import com.example.androidhybridgedemo.util.ToastUtil;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import cn.pedant.SafeWebViewBridge.JsCallback;

/**
 * 暴露给JS层的类，类中的方法可供JS调用，方法具有统一的格式:
 * public static T  functionName(WebView webview,Type param1,Type param2 .....){}
 */
public class HostJsScope {
    public static void toast(WebView webView,String message){
        Toast.makeText(webView.getContext(),"String:"+message,Toast.LENGTH_SHORT).show();
    }
    public static void toast(WebView webView,int message){
        Toast.makeText(webView.getContext(),"int:"+message,Toast.LENGTH_SHORT).show();
    }

    /**
     * 给JS返回值
     * @param view
     * @return
     */
    public static String getTestString(WebView view){
        return "Android WebView";
    }
    public static void testJsCallback(WebView view, String backMessage, JsCallback jsCallback){
        try {
            jsCallback.apply(backMessage);
        } catch (JsCallback.JsCallbackException e) {
            e.printStackTrace();
        }

    }
    public static void delayJsCallback(WebView webView,int ms,String backMessage,JsCallback jsCallback){
//        TaskExecutor.scheduleTask(ms * 1000, new Runnable() {
//            @Override
//            public void run() {
//                try {
//                    jsCallback.setPermanent(true);// jsCallback.apply()只能执行1次，要多次执行时，就setPermanent为true
//                    jsCallback.apply(backMessage);
//                } catch (JsCallback.JsCallbackException e) {
//                    e.printStackTrace();
//                }
//            }
//        });
////        ScheduledExecutorService scheduledExecutorService =  Executors.newScheduledThreadPool(1);
//        scheduledExecutorService.scheduleAtFixedRate(new Runnable() {
//            @Override
//            public void run() {
//                ToastUtil.toastMsg(webView.getContext(),"我爱你中国");
//                jsCallback.setPermanent(true);
//                try {
//                    jsCallback.apply(backMessage);
//                } catch (JsCallback.JsCallbackException e) {
//                    e.printStackTrace();
//                }
//            }
//        },ms*1000,5, TimeUnit.SECONDS);
        Handler handler = new Handler(Looper.getMainLooper());//不同线程中操作了webview会抛出异常，解决方法便是抛给主线程来处理
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                /**
                 *要执行的操作
                 */
                jsCallback.setPermanent(true);// jsCallback.apply()只能执行1次，要多次执行时，就setPermanent为true
                try {
                    jsCallback.apply(backMessage);
                } catch (JsCallback.JsCallbackException e) {
                    e.printStackTrace();
                }
            }
        }, 3000);//3秒后执行Runnable中的run方法

    }

}
