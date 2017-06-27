package com.example.bluetooth.le;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTabHost;
import android.support.v4.content.ContextCompat;
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

    private static final int REQUEST_CODE_ACCESS_COARSE_LOCATION = 1;
    private void requestPermission(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {//如果 API level 是大于等于 23(Android 6.0) 时
            //判断是否具有权限
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                //判断是否需要向用户解释为什么需要申请该权限
                if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                        Manifest.permission.ACCESS_COARSE_LOCATION)) {
//                    showToast("自Android 6.0开始需要打开位置权限才可以搜索到Ble设备");
                }
                //请求权限
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        REQUEST_CODE_ACCESS_COARSE_LOCATION);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CODE_ACCESS_COARSE_LOCATION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //用户允许改权限，0表示允许，-1表示拒绝 PERMISSION_GRANTED = 0， PERMISSION_DENIED = -1
                //permission was granted, yay! Do the contacts-related task you need to do.
                //这里进行授权被允许的处理
                boolean flag = isLocationEnable(MainActivity.this);
                if (flag){
                }else {
                    setLocationService();
                }
            } else {
                //permission denied, boo! Disable the functionality that depends on this permission.
                //这里进行权限被拒绝的处理
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }


    /**
     * Location service if enable
     *
     * @param context
     * @return location is enable if return true, otherwise disable.
     */
    public static final boolean isLocationEnable(Context context) {
        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        boolean networkProvider = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        boolean gpsProvider = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if (networkProvider || gpsProvider) return true;
        return false;
    }
//    如果定位已经打开，OK 很好，可以搜索到 ble 设备；如果定位没有打开，则需要用户去打开，像下面这样：

    private static final int REQUEST_CODE_LOCATION_SETTINGS = 2;

    private void setLocationService() {
        Intent locationIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        this.startActivityForResult(locationIntent, REQUEST_CODE_LOCATION_SETTINGS);
    }
//    进入定位设置界面，让用户自己选择是否打开定位。选择的结果获取：

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_LOCATION_SETTINGS) {
            if (isLocationEnable(this)) {
                //定位已打开的处理
            } else {
                //定位依然没有打开的处理
            }
        } else super.onActivityResult(requestCode, resultCode, data);
    }

}
