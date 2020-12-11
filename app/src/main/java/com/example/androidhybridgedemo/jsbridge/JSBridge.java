package com.example.androidhybridgedemo.jsbridge;

import android.net.Uri;
import android.text.TextUtils;
import android.webkit.WebView;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;

/**
 * 1、管理暴露给JS的类和方法
 * 2、根据JS传入的URL内容找到对应的Java类，并执行指定的java方法
 */
public class JSBridge {
    //存所有暴露给js层的类和该类的方法 <类名，<类名，方法>>
    private static Map<String, HashMap<String, Method>> exposeMethods = new HashMap<>();

    /**
     * 匹配类和方法暴露给JS层
     *
     * @param exposeName 类名
     * @param mClass     类
     */
    public static void register(String exposeName, Class<?> mClass) {
        if (!exposeMethods.containsKey(exposeName)) {
            exposeMethods.put(exposeName, getAllMethod(mClass));
        }

    }

    /**
     * 获取类中所有满足规则的方法
     *
     * @param injectedClass 类
     * @return
     */
    private static HashMap<String, Method> getAllMethod(Class injectedClass) {
        HashMap<String, Method> methodHashMap = new HashMap<>();
        Method[] methods = injectedClass.getDeclaredMethods();//获取该类中所有声明过的方法
        for (Method method : methods) {
            //过滤非PUBLIC ,STATIC,无方法名的方法  |：按位或，两侧的式子都会比较
            if (method.getModifiers() != (Modifier.PUBLIC | Modifier.STATIC) || method.getName() == null) {
                continue;
            }
            Class[] paramters = method.getParameterTypes();
            //参数不为空，参数的个数等于3
            if (paramters != null && paramters.length == 3) {
                if (paramters[0] == WebView.class && paramters[1] == JSONObject.class && paramters[2] == CallBack.class) {
                    methodHashMap.put(method.getName(), method);
                }
            }

        }
        return methodHashMap;
    }

    /**
     * http://host:port/path?param=value
     * jsbridge://className:callbackAddress/methodName?jsonObj
     *
     * @param webView
     * @param urlString
     * @return
     */
    public static String callJava(WebView webView, String urlString) {
        String className = "";
        String methodName = "";
        String param = "";
        String port = "";

        //url非空校验，且以JSBridge
        if (!TextUtils.isEmpty(urlString) && urlString.startsWith("JSBridge")) {
            Uri uri = Uri.parse(urlString);
            className = uri.getHost();
            port = String.valueOf(uri.getPort());
            methodName = uri.getPath().replace("/", "");
            param = uri.getQuery();
            //先看我们要找的类在不在这个暴露的Hash表中。
            if (exposeMethods.containsKey(className)) {
                //这个类中的所有暴露方法
                HashMap<String, Method> methodHashMap = exposeMethods.get(className);
                if (methodHashMap != null && methodHashMap.size() != 0 && methodHashMap.containsKey(methodName)) {
                    Method method = methodHashMap.get(methodName);
                    if (method != null) {
                        try {
                            //执行JS调用的方法
                            method.invoke(null, webView, new JSONObject(param), new CallBack(webView,port));
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        } catch (InvocationTargetException e) {
                            e.printStackTrace();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
                ;
            }
        }
        return null;
    }
}
