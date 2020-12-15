package com.example.androidhybridgedemo;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.ClientCertRequest;
import android.webkit.HttpAuthHandler;
import android.webkit.JsPromptResult;
import android.webkit.RenderProcessGoneDetail;
import android.webkit.SafeBrowsingResponse;
import android.webkit.SslErrorHandler;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.example.androidhybridgedemo.jsbridge.JSBridge;
import com.example.androidhybridgedemo.jsbridge.JSBridgeChromeClient;
import com.example.androidhybridgedemo.jsbridge.Methods;
import com.example.androidhybridgedemo.safejsbridge.HostJsScope;
import com.example.androidhybridgedemo.safejsbridge.SafeChromeClient;
import com.example.androidhybridgedemo.util.ToastUtil;

public class MainActivity extends AppCompatActivity {
    private WebView myWebView;
    private static final String INDEX_URL="file:///android_asset/index.html";
    private static final String JSWEB_URL="file:///android_asset/jsweb.html";
    private static  final String JSBRIDGE_URL="file:///android_asset/jsbridge.html";
    private static final String SAVE_JSBRIDGE_URL="file:///android_asset/safejsbridge.html";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        myWebView = findViewById(R.id.myWebView);
//        jsCallAndroid();



//        如果除了加载html的haunted，只需要用webviewclient即可，但是在进行互联网兼容附加js的页面的时候和调用js对话框的时候，或者功能较为复杂的内嵌操作的时候，建议使用webchromeclient
        //使用webview Client监听网络开始加载和加载成功
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {//如果点击了返回键
            if (myWebView.canGoBack()) {//如果有上一页
                myWebView.goBack();//返回上一页
                return true;
            }

        }
        return super.onKeyDown(keyCode, event);
    }

    private void setWebViewClient() {
        myWebView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                ToastUtil.toastMsg(MainActivity.this, "开始网页请求了");
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                ToastUtil.toastMsg(MainActivity.this, "网页请求成功了");
            }
        });
    }

    public void webRequest(View view) {
        //toast网页开始加载和加载结束
        setWebViewClient();
        myWebView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
//                ToastUtil.toastMsg(MainActivity.this,"progress:"+newProgress);
            }

            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
                ToastUtil.toastMsg(MainActivity.this, "title:" + title);
            }
        });
        myWebView.loadUrl("http://www.jikexueyuan.com");

        //网页缩放按钮显示
        WebSettings webSettings = myWebView.getSettings();
        webSettings.setSupportZoom(true);//设置缩放
        webSettings.setBuiltInZoomControls(true);//创建缩放按钮
        webSettings.setDisplayZoomControls(true);//显示缩放按钮
        webSettings.setJavaScriptEnabled(true);
    }

    /**
     * 加载本地目录下的网页
     *
     * @param view
     */
    public void localWebView(View view) {
        //assets目录下主要存放四种文件：文本文件、图像文件、网页文件（包括html中引用的js/ccs/jpg等资源）、音频视频文件
        myWebView.setWebChromeClient(new WebChromeClient());
        showWebView(myWebView,INDEX_URL);
    }
    public void showWebView(WebView webView,String url){
        webView.setWebViewClient(new WebViewClient());
//        webView.setWebChromeClient(new WebChromeClient());//设置ChromeClient，支持弹出对话框，否则无法弹出
        webView.loadUrl(url);
        webView.getSettings().setJavaScriptEnabled(true);
    }

    /**
     * JS调用android
     *
     * @param view
     */
    public void onJSCallsAndroid(View view) {
    }

    /**
     * Android 调用js 无参方法
     * js输出弹窗“Android calls Js with no parameters!”
     * @param view
     */
    public void onAndroidCallJS1(View view) {
        myWebView.setWebChromeClient(new WebChromeClient());
       showWebView(myWebView,JSWEB_URL);
//       WebView通过loadUrl方法调用JS无参方法
        myWebView.loadUrl("javascript:showAlert()");
    }

    /**
     * Android 调用Js有参方法
     * Js输出弹窗，参数在弹窗中显示
     * @param view
     */
    public void onAndroidsCallJs2(View view) {
        myWebView.setWebChromeClient(new WebChromeClient());
        showWebView(myWebView,JSWEB_URL);
        myWebView.loadUrl("javascript:showAlertWithParam('Android calls Js with parameters')");
    }

    /**
     * Android 调用Js有参数方法，有返回值
     * Android 将返回值Toast出来
     * @param view
     */
    public void onAndroidCallJs3(View view) {
        myWebView.setWebChromeClient(new WebChromeClient());
        showWebView(myWebView,JSWEB_URL);
//        方法1：最稳妥，但是要和js商议好回调接口JSInterface和方法jsMethod
        myWebView.loadUrl("javascript:showAlertWithResult('Hello JavaScript！')");
        myWebView.addJavascriptInterface(new JsToJava(),"JSInterface");//接收js返回结果
//项目最低支持版本在4.4以上，则可以使用
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            myWebView.evaluateJavascript("javascript:showAlertWithReturn('Hello JavaScript！')", new ValueCallback<String>() {
//                @Override
//                public void onReceiveValue(String value) {
//                    ToastUtil.toastMsg(MainActivity.this,value);
//                }
//            });
//        }
    }




    /**
     * 开放给JS调用的方法
     */
    private class JsToJava{
        @android.webkit.JavascriptInterface//标记这个方法在JavascriptInterface接口中，可以被JS调用
        public void jsMethod(String paramFromJs){
            ToastUtil.toastMsg(MainActivity.this,paramFromJs);

        }
        @android.webkit.JavascriptInterface
        public void showToast(){
            Toast.makeText(MainActivity.this,"Js 调用了Android的方法", Toast.LENGTH_SHORT).show();
        }
        @android.webkit.JavascriptInterface
        public String showWithReturn(){
            return "Return string from android";
        }
    }

    /**
     * android4.2以上 js调用Android中的方法
     */
    private void jsCallAndroid(){
        showWebView(myWebView,JSWEB_URL);
        myWebView.addJavascriptInterface(new JsToJava(),"JSInterface" );
    }

    /**
     * android4.2以下，js想Android的安全通信，通过WebChromeClient中的onJsp
     * @param view
     */
    public void onJsBridge(View view) {
        showWebView(myWebView,JSBRIDGE_URL);
        myWebView.setWebChromeClient(new WebChromeClient(){
            /**
             消息提示弹窗
             */
            @Override
            public boolean onJsPrompt(WebView view, String url, String message, String defaultValue, JsPromptResult result) {
                ToastUtil.toastMsg(MainActivity.this,"java收到的消息:"+message);
                return super.onJsPrompt(view, url, message, defaultValue, result);

            }
        });
    }

    /**
     * JSBridge实现
     * @param view
     */
    public void jsBridge(View view) {
        myWebView.setWebChromeClient(new JSBridgeChromeClient());
        showWebView(myWebView,JSBRIDGE_URL);
        JSBridge.register("JSBridge", Methods.class);
    }
    public void onSafeJsBridge(View view) {
        showWebView(myWebView,SAVE_JSBRIDGE_URL);
        //注入的接口名是HostApp,JS层根据【HostApp.方法】调用HostJsScope类中的方法
        myWebView.setWebChromeClient(new SafeChromeClient("HostApp", HostJsScope.class));
    }
}