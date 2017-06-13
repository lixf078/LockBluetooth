package com.aiyiqi.lib.api.base;

import android.webkit.JavascriptInterface;

/**
 * Created by hubing on 16/2/25.
 */
public class BaseWebJS {
    
    @JavascriptInterface
    public String getDeviceSource(){
        return "3";
    }

    @JavascriptInterface
    public boolean fromClient(){
        return true;
    }
}
