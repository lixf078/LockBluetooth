package com.example.bluetooth.le.fragment;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.example.bluetooth.le.R;
import com.example.bluetooth.le.activity.QrCodeActivity;
import com.example.bluetooth.le.adapter.DeviceAdapter;
import com.example.bluetooth.le.view.SlideListView;
import com.lock.lib.api.Server;
import com.lock.lib.api.base.BaseFragment;
import com.lock.lib.api.device.LockBluetoothDevice;
import com.lock.lib.api.event.RequestEvent;
import com.lock.lib.api.event.ResponseEvent;
import com.lock.lib.common.util.AppUtil;

import java.util.ArrayList;
import java.util.HashMap;

import cn.iwgang.familiarrecyclerview.FamiliarRecyclerView;
import in.srain.cube.views.loadmore.LoadMoreContainer;
import in.srain.cube.views.loadmore.LoadMoreHandler;
import in.srain.cube.views.loadmore.LoadMoreListViewContainer;
import in.srain.cube.views.loadmore.LoadMoreRecycleViewContainer;

/**
 * Created by admin on 2017/6/14.
 */

public class SettingFragment extends BaseFragment implements View.OnClickListener, LoadMoreHandler {

    private TextView mConnectDeviceView, mScanQRCodeView;

    private LoadMoreListViewContainer mLoadMoreContainer;

    private SlideListView mListView;
    private DeviceAdapter mDeviceAdapter;
    ArrayList<LockBluetoothDevice> list = new ArrayList<LockBluetoothDevice>();

    @Override
    protected View createContentView(LayoutInflater inflater, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list_view_layout, null);
        initContentView(view);
        return view;
    }

    @Override
    protected void onEventResponse(ResponseEvent event) {

    }

    private void initContentView(View view) {
        setHeadLayoutVisiable(View.VISIBLE);
        getHeadMiddelView().setText("Setting");
        getHeadLeftView().setVisibility(View.GONE);

        mConnectDeviceView = (TextView) view.findViewById(R.id.ble_connect_device);
        mScanQRCodeView = (TextView) view.findViewById(R.id.ble_scan_qr_code);

        mConnectDeviceView.setOnClickListener(this);
        mScanQRCodeView.setOnClickListener(this);
        mLoadMoreContainer = (LoadMoreListViewContainer) view.findViewById(R.id.load_more_container);
        mListView = (SlideListView) view.findViewById(R.id.id_recycler_view);
        initData();
        mDeviceAdapter = new DeviceAdapter(getContext());
        mDeviceAdapter.setItemArrayList(list);
        mListView.setAdapter(mDeviceAdapter);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.ble_scan_qr_code: {
                Intent intent = new Intent(SettingFragment.this.getContext(), QrCodeActivity.class);
                startActivity(intent);
                break;
            }
            case R.id.ble_connect_device: {

                break;
            }
        }
    }

    @Override
    public void onLoadMore(LoadMoreContainer loadMoreContainer) {

    }

    private void initData() {

        for (int i = 0;i < 9;i ++){
            LockBluetoothDevice bluetoothDevice = new LockBluetoothDevice();
            bluetoothDevice.name = "SC Device";
            bluetoothDevice.mac = "A3DF8NF9HFD88";
            list.add(bluetoothDevice);
        }

        showLoadingView();
        checkPermission(new String[]{Manifest.permission.READ_PHONE_STATE}, new PermissionAction() {
            @Override
            public void done(boolean confirm) {
//                RequestEvent requestEvent = new RequestEvent();
//                requestEvent.eventType = RequestEvent.TYPE_SERVER_GET_FEED_LATEST_LIST;
//                HashMap<String, String> map = new HashMap<>();
//                map.put(Server.Param.PAGE, String.valueOf(pageNum));
//                map.put(Server.Param.PAGE_SIZE, String.valueOf(Server.Page.PAGE_SIZE));
//                map.put(Server.Param.PERMISSION,confirm ? Server.Permission.PERMISSION_YES : Server.Permission.PERMISSION_NO);
//                if(!TextUtils.isEmpty(cityName)){
//                    map.put(Server.Param.CITY_NAME, AppUtil.getUTF8XMLString(cityName));
//                }
//                requestEvent.eventMap = map;
//                postEvent(requestEvent);

                //
            }
        });
    }


}
