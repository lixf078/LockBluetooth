package com.lock.bluetooth.le.fragment;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.lock.bluetooth.le.DeviceConnectActivity;
import com.lock.bluetooth.le.DeviceModel;
import com.lock.bluetooth.le.DeviceShare;
import com.lock.bluetooth.le.R;
import com.lock.bluetooth.le.activity.QrCodeActivity;
import com.lock.bluetooth.le.adapter.DeviceAdapter;
import com.lock.bluetooth.le.adapter.SettingLeDeviceListAdapter;
import com.lock.bluetooth.le.view.SwipeLayoutManager;
import com.lock.lib.api.Server;
import com.lock.lib.api.base.BaseFragment;
import com.lock.lib.api.event.ResponseEvent;
import com.lock.lib.common.util.DialogUtil;
import com.lock.lib.common.util.ToastUtil;
import com.lock.lib.qr.QRCodeUtil;

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

    Dialog dia;

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
                    if ("true".equalsIgnoreCase(event.errorMsg)){
                        final Dialog dialog = DialogUtil.showDialog(getActivity(), R.layout.custom_alert_dialog);

                        TextView yes = (TextView) dialog.findViewById(R.id.action_yes);

                        yes.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                            }
                        });

                    }
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
//                showQRDialog();
                break;

            }
            case R.id.ble_connect_device: {
                int result = checkBluetooth();
                if (result == 1){
                    Intent intent = new Intent(SettingFragment.this.getContext(), DeviceConnectActivity.class);
                    startActivity(intent);
                }else if (result == -1){
                    ToastUtil.showToast(SettingFragment.this.getContext(), R.string.error_bluetooth_not_supported);
                }else if (result == 0){
//                    Intent settingIntent = new Intent(Settings.ACTION_SETTINGS);
//                    startActivity(settingIntent);
                }
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
        DeviceModel deviceModel = new DeviceModel();
        deviceModel.name = "Digital Ant-TB";
        deviceModel.mac = "afafafafafaf";
//        list.add(deviceModel);
//        list.add(deviceModel);
//        list.addAll(list);
        hiddenLoadingView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public void showQRDialog(){
        dia = new Dialog(SettingFragment.this.getContext(), R.style.edit_AlertDialog_style);
        dia.setContentView(R.layout.unlock_dialog);
        ImageView imageView = (ImageView) dia.findViewById(R.id.start_img);
        Bitmap bitmap = QRCodeUtil.encodeQRBitmap("{ \"name\" : \"Digital Ant-B5\", \"macStr\" : \"E6983577CEB5\", \"secretKey2\" : \"8DF9A4704A226BC1D341B7EAA16D988F\" }");
        imageView.setImageBitmap(bitmap);
//        imageView.setBackgroundResource(R.drawable.unlock_animation);

        //选择true的话点击其他地方可以使dialog消失，为false的话不会消失
        dia.setCanceledOnTouchOutside(true); // Sets whether this dialog is
        Window w = dia.getWindow();
        WindowManager.LayoutParams lp = w.getAttributes();
        lp.x = 0;
        lp.y = 40;
        dia.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {

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
        dia.show();
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
