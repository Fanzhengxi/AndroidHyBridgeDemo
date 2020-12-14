package com.example.androidhybridgedemo.safejsbridge;

import cn.pedant.SafeWebViewBridge.InjectedChromeClient;

public class SafeChromeClient extends InjectedChromeClient {

    public SafeChromeClient(String injectedName, Class injectedCls) {
        super(injectedName, injectedCls);
    }
}
