package com.example.bluetooth.le;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTabHost;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.example.bluetooth.le.fragment.SettingFragment;
import com.example.bluetooth.le.fragment.ShoppingFragment;
import com.example.bluetooth.le.fragment.SmartCoolFragment;
import com.lock.lib.api.Server;
import com.lock.lib.api.event.ResponseEvent;
import com.lock.lib.common.constants.Constants;
import com.lock.lib.common.util.Logger;
import com.lock.lib.common.util.ShareUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONObject;

/**
 * Created by admin on 2017/6/14.
 */

public class DeviceConnectActivity extends Activity {


    BleConnectUtil bleConnectUtil = null;

    private TextView mDismissView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_search_device);

        EventBus.getDefault().register(this);

        bleConnectUtil = new BleConnectUtil(DeviceConnectActivity.this, null, null);
        mDismissView = (TextView) findViewById(R.id.touch_dismiss);
        mDismissView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bleConnectUtil.disconnectDevice();
                finish();
            }
        });

    }


    @Override
    protected void onResume() {
        super.onResume();

        requestPermission();
    }

    @Override
    protected void onStop() {
        super.onStop();
        bleConnectUtil.disconnectDevice();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe
    public void onEventMainThread(ResponseEvent event) {
        if (event != null) {
            if (event.eventType == ResponseEvent.TYPE_ACTIVITY_DEVICE) {
                if (event.errorCode == Server.Code.SUCCESS) {
                    finish();
                } else {
//                    resolveError(event.errorCode, event.errorMsg);
                }
            }
        }
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
                        new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                        REQUEST_CODE_ACCESS_COARSE_LOCATION);
            }else{
                bleConnectUtil.connectDevice("");
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
                boolean flag = isLocationEnable(DeviceConnectActivity.this);
                if (flag){
                    bleConnectUtil.connectDevice("");
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
                bleConnectUtil.connectDevice("");
            } else {
                //定位依然没有打开的处理
            }
        } else super.onActivityResult(requestCode, resultCode, data);
    }
}
