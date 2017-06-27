package com.example.bluetooth.le.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.bluetooth.le.DeviceConnectActivity;
import com.example.bluetooth.le.DeviceModel;
import com.example.bluetooth.le.DeviceShare;
import com.example.bluetooth.le.R;
import com.example.bluetooth.le.activity.QrCodeActivity;
import com.example.bluetooth.le.adapter.DeviceAdapter;
import com.example.bluetooth.le.adapter.SettingLeDeviceListAdapter;
import com.example.bluetooth.le.view.SwipeLayoutManager;
import com.lock.lib.api.Server;
import com.lock.lib.api.base.BaseFragment;
import com.lock.lib.api.event.ResponseEvent;

import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import in.srain.cube.views.loadmore.LoadMoreContainer;
import in.srain.cube.views.loadmore.LoadMoreHandler;
import in.srain.cube.views.loadmore.LoadMoreListViewContainer;

/**
 * Created by admin on 2017/6/14.
 */

public class SettingFragment extends BaseFragment implements View.OnClickListener, LoadMoreHandler/*, AdapterView.OnItemClickListener */{

    private TextView mConnectDeviceView, mScanQRCodeView;

    private LoadMoreListViewContainer mLoadMoreContainer;

    private ListView mListView;
    private DeviceAdapter mDeviceAdapter;
//    private SettingLeDeviceListAdapter mDeviceAdapter;
    List<DeviceModel> list = new ArrayList<DeviceModel>();

//    private ExpandableListView expandableListView;


    @Override
    protected View createContentView(LayoutInflater inflater, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_setting, null);
        initContentView(view);
        return view;
    }

//    @Override
//    protected void onEventResponse(ResponseEvent event) {
//
//    }

    @Override
    public void onResume() {
        super.onResume();
        initData();
        mDeviceAdapter = new DeviceAdapter(getContext(), list, R.layout.setting_list_item_device);
        mDeviceAdapter.setItemArrayList(list);
        if (mListView != null){
            mListView.setAdapter(mDeviceAdapter);
        }
    }

    @Override
    public void onEventResponse(ResponseEvent event) {
        if (event != null) {
            if (event.eventType == ResponseEvent.TYPE_DELETE_DEVICE) {
                if (event.errorCode == Server.Code.SUCCESS) {
                    initData();
                    mDeviceAdapter = new DeviceAdapter(getContext(), list, R.layout.setting_list_item_device);
                    mDeviceAdapter.setItemArrayList(list);
                    mListView.setAdapter(mDeviceAdapter);
                } else {
//                    resolveError(event.errorCode, event.errorMsg);
                }
            }else if (event.eventType == ResponseEvent.TYPE_EDIT_DEVICE) {
                if (event.errorCode == Server.Code.SUCCESS) {
                    initData();
                    mDeviceAdapter.setItemArrayList(list);
                    mDeviceAdapter.notifyDataSetChanged();
                } else {
//                    resolveError(event.errorCode, event.errorMsg);
                }
            }else if (event.eventType == ResponseEvent.TYPE_ACTIVITY_DEVICE_SUCCESS) {
                if (event.errorCode == Server.Code.SUCCESS) {
                    initData();
                    mDeviceAdapter = new DeviceAdapter(getContext(), list, R.layout.setting_list_item_device);
                    mDeviceAdapter.setItemArrayList(list);
                    mListView.setAdapter(mDeviceAdapter);
                } else {
//                    resolveError(event.errorCode, event.errorMsg);
                }
            }


        }
    }

    private void initContentView(View view) {
        setHeadLayoutVisiable(View.VISIBLE);
        getHeadMiddelView().setText("Setting");
        getHeadLeftView().setVisibility(View.GONE);

        mConnectDeviceView = (TextView) view.findViewById(R.id.ble_connect_device);
        mScanQRCodeView = (TextView) view.findViewById(R.id.ble_scan_qr_code);

        mConnectDeviceView.setOnClickListener(this);
        mScanQRCodeView.setOnClickListener(this);
        mLoadMoreContainer = (LoadMoreListViewContainer) view.findViewById(R.id.fragment_load_more_container);
        mListView = (ListView) view.findViewById(R.id.list_view);
//        expandableListView = (ExpandableListView) view.findViewById(R.id.list_view);
//        initData();
//        mDeviceAdapter = new DeviceAdapter(getContext(), list, R.layout.setting_list_item_device);
//        mDeviceAdapter.setItemArrayList(list);
//        mListView.setAdapter(mDeviceAdapter);

        // 侧滑打来的时候滑动没有想到什么好的办法解决，只能这样了。
        mListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if(scrollState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                    // 如果listView跟随手机拖动，关闭已经打开的SwipeLayout
                    SwipeLayoutManager.getInstance().closeOpenInstance();
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            }
        });

//        mDeviceAdapter = new SettingLeDeviceListAdapter(getActivity());
//        mDeviceAdapter.setDevices(list);
//        mListView.setAdapter(mDeviceAdapter);
//        mListView.setOnItemClickListener(this);
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
                Intent intent = new Intent(SettingFragment.this.getContext(), DeviceConnectActivity.class);
                startActivity(intent);
                break;
            }
        }
    }

    @Override
    public void onLoadMore(LoadMoreContainer loadMoreContainer) {

    }

    private void initData() {
        showLoadingView();
        list = DeviceShare.getDevices(SettingFragment.this.getContext());
        hiddenLoadingView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

//    @Override
//    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
////        DeviceModel model = list.get(position);
////        Utils.showEditDialog(SettingFragment.this.getContext(), model);
//        View optLayout = view.findViewById(R.id.opt_layout);
//        if (optLayout.isShown()){
//            Log.e("lxf", "GONE");
//            optLayout.setVisibility(View.GONE);
//        }else{
//            Log.e("lxf", "VISIBLE");
//            optLayout.setVisibility(View.VISIBLE);
//        }
//    }
}
