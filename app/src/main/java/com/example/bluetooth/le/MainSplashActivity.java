package com.example.bluetooth.le;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;

import com.android.volley.ui.NetworkImageView;
import com.lock.lib.api.base.BaseActivity;

import java.io.File;
import java.util.Random;


/**
 * Created by lc on 2017/4/23.
 */

public class MainSplashActivity extends BaseActivity {

    private NetworkImageView mIvAd;

    private Handler mHandler;

    @Override
    protected void onStart() {
        super.onStart();
        mHandler = new Handler(getMainLooper()) {
            @Override
            public void handleMessage(Message msg) {
                Intent intent = new Intent(MainSplashActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        };
    }

    @Override
    protected void onResume() {
        super.onResume();
        mHandler.sendEmptyMessageDelayed(1, 1000);
    }


    private void setIvBg(String fileDir) {
        mIvAd.setBackgroundResource(R.mipmap.main_splash_default);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        mHandler.removeMessages(1);
        mHandler = null;
    }

    @Override
    protected View createContentView(LayoutInflater layoutInflater) {
        setHeadLayoutVisiable(View.GONE);
        return layoutInflater.inflate(R.layout.activity_main_splash, null);
    }

    @Override
    protected void onEventResponse(int eventType, Object value) {

    }
}
