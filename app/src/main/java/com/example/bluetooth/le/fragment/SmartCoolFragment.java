package com.example.bluetooth.le.fragment;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.AnimationDrawable;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import com.example.bluetooth.le.BleConnectUtil;
import com.example.bluetooth.le.DeviceModel;
import com.example.bluetooth.le.DeviceShare;
import com.example.bluetooth.le.R;
import com.example.bluetooth.le.adapter.HomeDeviceAdapter;
import com.lock.lib.api.Server;
import com.lock.lib.api.base.BaseFragment;
import com.lock.lib.api.event.ResponseEvent;
import com.lock.lib.common.util.ToastUtil;

import java.util.List;

import in.srain.cube.views.loadmore.LoadMoreContainer;
import in.srain.cube.views.loadmore.LoadMoreHandler;
import in.srain.cube.views.loadmore.LoadMoreListViewContainer;

/**
 * Created by admin on 2017/6/14.
 */

public class SmartCoolFragment extends BaseFragment implements AdapterView.OnItemClickListener, View.OnClickListener, LoadMoreHandler {

    private LoadMoreListViewContainer mLoadMoreContainer;

    private ListView mListView;
    private HomeDeviceAdapter mDeviceAdapter;

    private List<DeviceModel> connectDevices = null;

    BleConnectUtil bleConnectUtil = null;

    private DeviceModel deviceModel;

    Dialog dia;

    @Override
    protected View createContentView(LayoutInflater inflater, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, null);
        bleConnectUtil = new BleConnectUtil(SmartCoolFragment.this.getContext(), null, null);
        connectDevices = DeviceShare.getDevices(SmartCoolFragment.this.getContext());
        initContentView(view);

        if (connectDevices.size() > 0){
            deviceModel = connectDevices.get(0);
        }
        return view;
    }

    @Override
    protected void onEventResponse(ResponseEvent event) {
        if (event != null) {
            if (event.eventType == ResponseEvent.TYPE_DELETE_DEVICE) {
                if (event.errorCode == Server.Code.SUCCESS) {
                    DeviceModel deviceModel = (DeviceModel) event.resultObj;
                    connectDevices.remove(deviceModel);
                } else {
//                    resolveError(event.errorCode, event.errorMsg);
                }
            }else if (event.eventType == ResponseEvent.TYPE_EDIT_DEVICE) {
                if (event.errorCode == Server.Code.SUCCESS) {

                } else {
//                    resolveError(event.errorCode, event.errorMsg);
                }
            }else if (event.eventType == ResponseEvent.TYPE_UNLOCK_DEVICE_SUCCESS) {
                bleConnectUtil.disconnectDevice();
                if (dia != null && dia.isShowing()){
                    dia.dismiss();
                }

                if (event.errorCode == Server.Code.SUCCESS) {
                    ToastUtil.showToast(SmartCoolFragment.this.getContext(), "Unlock Success !");
                } else {
//                    resolveError(event.errorCode, event.errorMsg);
                    ToastUtil.showToast(SmartCoolFragment.this.getContext(), "" + event.errorMsg);
                }
            }else if (event.eventType == ResponseEvent.TYPE_SCAN_TIME_OUT) {
                bleConnectUtil.disconnectDevice();
                if (dia != null && dia.isShowing()){
                    dia.dismiss();
                }
                if (event.errorCode == Server.Code.SUCCESS) {
                    ToastUtil.showToast(SmartCoolFragment.this.getContext(), "" + event.errorMsg);
                } else {
                    ToastUtil.showToast(SmartCoolFragment.this.getContext(), "" + event.errorMsg);
                }
            }
        }
    }

    private void initContentView(View view){
        setHeadLayoutVisiable(View.VISIBLE);
        getHeadMiddelView().setText("Home");

        getHeadLeftView().setVisibility(View.GONE);

        mLoadMoreContainer = (LoadMoreListViewContainer) view.findViewById(R.id.fragment_load_more_container);
        mListView = (ListView) view.findViewById(R.id.list_view);
        mDeviceAdapter = new HomeDeviceAdapter(getActivity());
        mDeviceAdapter.setDevices(connectDevices);
        mListView.setAdapter(mDeviceAdapter);
        mListView.setOnItemClickListener(this);
        initData();
    }

    @Override
    public void onResume() {
        super.onResume();
        connectDevices = DeviceShare.getDevices(SmartCoolFragment.this.getContext());
        if (connectDevices.size() > 0){
            deviceModel = connectDevices.get(0);
        }
        mDeviceAdapter.setDevices(connectDevices);
        requestPermission();
    }

    @Override
    public void onLoadMore(LoadMoreContainer loadMoreContainer) {

    }

    private void initData() {

        showLoadingView();
        connectDevices = DeviceShare.getDevices(SmartCoolFragment.this.getContext());
        hiddenLoadingView();
        if (connectDevices == null || connectDevices.size() == 0){
//            mNoDataTextView.setText("Please add a new device directly \n\r or by scanning the QR code");
//            mNoDataTextView.setTextSize(22);
//            mNoDateTitle.setText("");
            mLoadMoreContainer.setVisibility(View.GONE);
//            showNoDataVew();
        }else{
            mLoadMoreContainer.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
        deviceModel = connectDevices.get(position);
        if (bleConnectUtil != null){
             bleConnectUtil.connectDevice(deviceModel.mac);
            showDialog();
        }
    }

    @Override
    public void onClick(View view) {
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (bleConnectUtil != null){
            bleConnectUtil.disconnectDevice();
        }
    }

    private void showDialog(){
        dia = new Dialog(SmartCoolFragment.this.getContext(), R.style.edit_AlertDialog_style);
        dia.setContentView(R.layout.unlock_dialog);
        ImageView imageView = (ImageView) dia.findViewById(R.id.start_img);

        imageView.setBackgroundResource(R.drawable.unlock_animation);
        final AnimationDrawable animationDrawable=(AnimationDrawable)imageView.getBackground();

        //注意这里，如果你的图片控件用的是setImageResource ,你这里应该使用getDrawable();
        ViewTreeObserver.OnPreDrawListener preDrawListener=new ViewTreeObserver.OnPreDrawListener(){
            @Override
            public boolean onPreDraw(){
                animationDrawable.start();
                return true;//必须要有这个true返回
            }
        };
        imageView.getViewTreeObserver().addOnPreDrawListener(preDrawListener);

        //选择true的话点击其他地方可以使dialog消失，为false的话不会消失
        dia.setCanceledOnTouchOutside(true); // Sets whether this dialog is
        Window w = dia.getWindow();
        WindowManager.LayoutParams lp = w.getAttributes();
        lp.x = 0;
        lp.y = 40;
        dia.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                bleConnectUtil.disconnectDevice();
            }
        });
        dia.onWindowAttributesChanged(lp);
//        imageView.setOnClickListener(
//                new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        dia.dismiss();
//                    }
//                });

        dia.setCanceledOnTouchOutside(false);

        dia.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialogInterface, int i, KeyEvent keyEvent) {
                Log.e("lxf", "" + keyEvent.getKeyCode());
                if (KeyEvent.KEYCODE_BACK == keyEvent.getKeyCode()){
                    return true;
                }
                return false;
            }
        });

        dia.show();


    }

    private static final int REQUEST_CODE_ACCESS_COARSE_LOCATION = 1;
    private void requestPermission(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {//如果 API level 是大于等于 23(Android 6.0) 时
            //判断是否具有权限
            if (ContextCompat.checkSelfPermission(SmartCoolFragment.this.getActivity(),
                    Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                //判断是否需要向用户解释为什么需要申请该权限
                if (ActivityCompat.shouldShowRequestPermissionRationale(SmartCoolFragment.this.getActivity(),
                        Manifest.permission.ACCESS_COARSE_LOCATION)) {
//                    showToast("自Android 6.0开始需要打开位置权限才可以搜索到Ble设备");
                }
                //请求权限
                ActivityCompat.requestPermissions(SmartCoolFragment.this.getActivity(),
                        new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        REQUEST_CODE_ACCESS_COARSE_LOCATION);
            }else{
//                if (deviceModel != null){
//                    bleConnectUtil.connectDevice(deviceModel.mac);
//                }

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
                boolean flag = isLocationEnable(SmartCoolFragment.this.getActivity());
                if (flag){
//                    if (deviceModel != null){
//                        bleConnectUtil.connectDevice(deviceModel.mac);
//                    }
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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_LOCATION_SETTINGS) {
            if (isLocationEnable(SmartCoolFragment.this.getContext())) {
                //定位已打开的处理
//                if (deviceModel != null){
//                    bleConnectUtil.connectDevice(deviceModel.mac);
//                }
            } else {
                //定位依然没有打开的处理
            }
        } else super.onActivityResult(requestCode, resultCode, data);
    }

}
