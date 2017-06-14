package com.lock.lib.api.base;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.lock.lib.api.R;
import com.lock.lib.api.event.ResponseEvent;
import com.lock.lib.common.constants.Constants;
import com.lock.lib.common.util.AppUtil;
import com.lock.lib.common.util.DeviceUtil;
import com.lock.lib.common.util.Logger;
import com.lock.lib.style.view.DrawableCenterTextView;

/**
 * Created by hubing on 16/5/27.
 */
public abstract class BaseWebFragment extends BaseFragment {

    private static final String TAG = "web";
    protected WebView mWebView;

    protected ViewStub mLoadingViewStub;
    private View mLoadingView;
    private ProgressBar mProgressLoading;

    protected ViewStub mNoNetStub;
    private View mNoNetView;
    protected ViewStub mNoDateStub;
    private View mNoDataView;
    private TextView mTextNoData;

    private DrawableCenterTextView mNoNetRefreshView;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View contentView = onCreateConentView(inflater);
        mWebView = onCreateWebView(contentView);
        WebSettings settings = mWebView.getSettings();
        settings.setJavaScriptEnabled(true);
        mWebView.addJavascriptInterface(new BaseWebJS(), "AppJs");
        settings.setAllowFileAccess(true);
        settings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        settings.setAppCacheEnabled(true);
        settings.setUseWideViewPort(true);
        settings.setLoadWithOverviewMode(true);
        settings.setPluginState(WebSettings.PluginState.ON);
        String ua = DeviceUtil.getUserAgent(AppUtil.getVersionName(getActivity()));
        settings.setUserAgentString(ua);
        settings.setTextSize(WebSettings.TextSize.SMALLER);
        mWebView.setHorizontalScrollbarOverlay(true);
        mWebView.setVerticalScrollbarOverlay(true);
        mWebView.setHorizontalScrollBarEnabled(false);
        mWebView.setVerticalScrollBarEnabled(false);
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                Logger.e(Constants.TAG,"BaseWebViewActivity >> shouldOverrideUrlLoading >> url : "+url);
                view.loadUrl(url);
                return true;
            }
        });
        mWebView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                onWebViewProgressChanged(view, newProgress);
                super.onProgressChanged(view, newProgress);
            }
        });
        initViewStub(contentView);
        onStartLoadUrl();
        return contentView;
    }

    protected void loadWebUrl(String url) {
        if (isNetworkAvabile()) {
            hiddenNoNetView();
            if (!TextUtils.isEmpty(url)) {
                hiddenNoDataView();
                StringBuilder builder = new StringBuilder(url);
                if(url.contains("?")){
                    builder.append("&model=android");
                }else{
                    builder.append("?model=android");
                }
                Logger.e(Constants.TAG,"BaseWebViewActivity >> loadWebUrl >> url : "+builder.toString());
                mWebView.loadUrl(builder.toString());
            } else {
                showNoDataView(url);
                showToast(getString(R.string.fail_web_loading));
            }
        } else {
            showNoNetView();
        }
    }

    protected void showNoDataView(final String url) {
        if (mNoDataView == null) {
            mNoDataView = mNoDateStub.inflate();
            if (mTextNoData == null) {
                mTextNoData = (TextView) mNoDataView.findViewById(R.id.empty_text);
                mTextNoData.setText(R.string.no_acq_data);
                mTextNoData.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        loadWebUrl(url);
                    }
                });
            }
        }
        mNoDataView.setVisibility(View.VISIBLE);
    }

    protected void hiddenNoDataView() {
        if (mNoDataView != null && mNoDataView.getVisibility() == View.VISIBLE) {
            mNoDataView.setVisibility(View.GONE);
        }
    }

    protected void showNoNetView() {
        if (mNoNetView == null) {
            mNoNetView = mNoNetStub.inflate();
        }

        if (mNoNetRefreshView == null) {
            mNoNetRefreshView = (DrawableCenterTextView) mNoNetView.findViewById(R.id.refresh);
            mNoNetRefreshView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (isNetworkAvabile()) {
                        mWebView.reload();
                    } else {
                        showToast(getString(R.string.fail_web_loading));
                    }
                }
            });
        }
        mNoNetView.setVisibility(View.VISIBLE);
    }

    protected void hiddenNoNetView() {
        if (mNoNetView != null && mNoNetView.getVisibility() == View.VISIBLE) {
            mNoNetView.setVisibility(View.GONE);
        }
    }

    //私有方法
    protected void showLoadingView() {
        if (mLoadingView == null) {
            mLoadingView = mLoadingViewStub.inflate();
        }

        if (mProgressLoading == null) {
            mProgressLoading = (ProgressBar) mLoadingView.findViewById(R.id.rotate_loading);
        }
        mProgressLoading.setVisibility(View.VISIBLE);
        mLoadingView.setVisibility(View.VISIBLE);

    }

    protected void hideLoadingView() {
        if (mProgressLoading != null && mProgressLoading.getVisibility() == View.VISIBLE) {
            mProgressLoading.setVisibility(View.GONE);
        }
        if (mLoadingView != null && mLoadingView.getVisibility() == View.VISIBLE) {
            mLoadingView.setVisibility(View.GONE);
        }
    }

    @Override
    protected View createContentView(LayoutInflater inflater, Bundle savedInstanceState) {
        return null;
    }

    @Override
    protected void onEventResponse(ResponseEvent event) {

    }
    protected abstract void showToast(String msg);
    protected abstract boolean isNetworkAvabile();
    protected abstract void onStartLoadUrl();
    protected abstract void initViewStub(View contentView);
    protected abstract View onCreateConentView(LayoutInflater inflater);
    protected abstract WebView onCreateWebView(View contentView);
    protected abstract void onWebViewProgressChanged(WebView view, int newProgress);
}
