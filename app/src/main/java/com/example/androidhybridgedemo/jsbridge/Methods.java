package com.example.androidhybridgedemo.jsbridge;

import android.webkit.WebView;

import com.example.androidhybridgedemo.util.ToastUtil;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 这个包含暴露给JS的方法
 */
public class Methods {
public static void showToast(WebView webView, JSONObject param,CallBack callBack){
    String message=param.optString("msg");
    ToastUtil.toastMsg(webView.getContext(),message);
    //回调
    if (callBack!=null){
        JSONObject result=new JSONObject();
        try {
            result.put("key1","value1");
            result.put("key2","value2");
            callBack.apply(result);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


}
}
