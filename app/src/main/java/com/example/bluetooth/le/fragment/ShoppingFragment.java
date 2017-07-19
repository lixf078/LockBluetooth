package com.example.bluetooth.le.fragment;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.webkit.WebView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.bluetooth.le.R;
import com.lock.lib.api.base.BaseFragment;
import com.lock.lib.api.base.BaseWebFragment;
import com.lock.lib.api.event.ResponseEvent;
import com.lock.lib.common.util.NetUtil;
import com.lock.lib.common.util.ToastUtil;

/**
 * Created by admin on 2017/6/14.
 */

public class ShoppingFragment extends BaseWebFragment {

    private String webUrl = "https://www.amazon.com/dp/B073P3VS3K/ref=olp_product_details/145-0881945-7442921?_encoding=UTF8&me=";
    private ProgressBar progressBar;

    @Override
    protected void onEventResponse(ResponseEvent event) {

    }

    @Override
    protected void showToast(String msg) {
        ToastUtil.showToast(getActivity(),msg);
    }

    @Override
    protected boolean isNetworkAvailable() {
        return NetUtil.checkNetwork(getActivity());
    }

    @Override
    protected void onStartLoadUrl() {
        if(!TextUtils.isEmpty(webUrl)){
            loadWebUrl(webUrl);
        }
    }

    @Override
    protected void initViewStub(View contentView) {
        mLoadingViewStub = (ViewStub) contentView.findViewById(R.id.loading_view_stub);
        mNoNetStub = (ViewStub) contentView.findViewById(R.id.no_net_stub);
        mNoDateStub = (ViewStub) contentView.findViewById(R.id.no_data_stub);
    }

    @Override
    protected View onCreateContentView(LayoutInflater inflater) {
        View view = inflater.inflate(R.layout.fragment_shopping_layout,null,false);
        initContentView(view);
        return view;
    }

    @Override
    protected WebView onCreateWebView(View contentView) {
        progressBar = (ProgressBar) contentView.findViewById(R.id.pb_act_web);
        mWebView = (WebView) contentView.findViewById(R.id.web_detail);

        //点击后退按钮,让WebView后退一页(也可以覆写Activity的onKeyDown方法)
        mWebView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (keyCode == KeyEvent.KEYCODE_BACK && mWebView.canGoBack()) {  //表示按返回键 时的操作
                        mWebView.goBack();   //后退

                        //webview.goForward();//前进
                        return true;    //已处理
                    }
                }
                return false;
            }
        });
        return mWebView;
    }

    @Override
    protected void onWebViewProgressChanged(WebView view, int newProgress) {
        if (newProgress == 100) {
            progressBar.setVisibility(View.INVISIBLE);
        } else {
            if (progressBar.getVisibility() == View.INVISIBLE) {
                progressBar.setVisibility(View.VISIBLE);
            }
            progressBar.setProgress(newProgress);
        }
    }

    private void initContentView(View view){

        view.findViewById(R.id.head_left_view).setVisibility(View.GONE);
        ((TextView)view.findViewById(R.id.head_middel_view)).setText("Shopping");

//        initHeadView(view, null);
//        setHeadLayoutVisiable(View.VISIBLE);
//        getHeadMiddelView().setText("Shopping");
//        getHeadLeftView().setVisibility(View.GONE);
    }



}
