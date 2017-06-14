package com.example.bluetooth.le;

import android.content.Context;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTabHost;
import android.view.LayoutInflater;
import android.view.View;

import com.example.bluetooth.le.fragment.SettingFragment;
import com.example.bluetooth.le.fragment.ShoppingFragment;
import com.example.bluetooth.le.fragment.SmartCoolFragment;

/**
 * Created by admin on 2017/6/14.
 */

public class MainActivity extends FragmentActivity {

    private LayoutInflater mInflater;
    // UI成员变量
    private FragmentTabHost mTabHost;

    private String mHome;
    private String mSetting;
    private String mShopping;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        mHome = getResources().getString(R.string.main_tab_home);
        mSetting = getResources().getString(R.string.main_tab_setting);
        mShopping = getResources().getString(R.string.main_tab_shopping);

        // 非UI成员变量初始化
        mInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        // UI成员变量初始化
        mTabHost = (FragmentTabHost) findViewById(android.R.id.tabhost);
        mTabHost.setup(this, getSupportFragmentManager(), R.id.tab_content);

        final View tabActivity = mInflater.inflate(R.layout.main_tab_home, null, false);
        mTabHost.addTab(mTabHost.newTabSpec(mHome).setIndicator(tabActivity),   SmartCoolFragment.class, null);

        final View tabSetting = mInflater.inflate(R.layout.main_tab_setting, null, false);
        mTabHost.addTab(mTabHost.newTabSpec(mSetting).setIndicator(tabSetting), SettingFragment.class, null);

        final View tabShopping = mInflater.inflate(R.layout.main_tab_shop, null, false);
        mTabHost.addTab(mTabHost.newTabSpec(mShopping).setIndicator(tabShopping), ShoppingFragment.class, null);
    }

}
