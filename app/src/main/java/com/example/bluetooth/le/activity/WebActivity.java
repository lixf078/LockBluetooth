package com.example.bluetooth.le.activity;

import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebView;

import com.lock.lib.api.base.BaseWebActivity;

/**
 * Created by admin on 2017/6/15.
 */

public class WebActivity extends BaseWebActivity {

    @Override
    protected View createContentView(LayoutInflater layoutInflater) {
        return null;
    }

    @Override
    protected void onEventResponse(int eventType, Object value) {

    }

    @Override
    protected void showToast(String msg) {

    }

    @Override
    protected boolean isNetworkAvabile() {
        return false;
    }

    @Override
    protected void onStartLoadUrl() {

    }

    @Override
    protected void initViewStub(View contentView) {

    }

    @Override
    protected View onCreateConentView(LayoutInflater inflater) {
        return null;
    }

    @Override
    protected WebView onCreateWebView(View contentView) {
        return null;
    }

    @Override
    protected void onWebViewProgressChanged(WebView view, int newProgress) {

    }
}
