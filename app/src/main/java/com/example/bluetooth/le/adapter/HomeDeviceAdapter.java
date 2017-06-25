package com.example.bluetooth.le.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.bluetooth.le.DeviceModel;
import com.example.bluetooth.le.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lixufeng on 16/4/11.
 */
public class HomeDeviceAdapter extends BaseAdapter {

    // Adapter for holding devices found through scanning.

    private List<DeviceModel> mLeDevices;
    private LayoutInflater mInflator;
    private Activity mContext;

    public HomeDeviceAdapter(Activity c) {
        super();
        mContext = c;
        mLeDevices = new ArrayList<DeviceModel>();
        mInflator = mContext.getLayoutInflater();
    }

    public void setDevices(List<DeviceModel> devices){
        mLeDevices = devices;
    }

    public void addDevice(DeviceModel device) {
        if (!mLeDevices.contains(device)) {
            mLeDevices.add(device);
        }
    }

    public DeviceModel getDevice(int position) {
        return mLeDevices.get(position);
    }

    public void clear() {
        mLeDevices.clear();
    }

    @Override
    public int getCount() {
        return mLeDevices.size();
    }

    @Override
    public Object getItem(int i) {
        return mLeDevices.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        HomeDeviceAdapter.ViewHolder viewHolder;
        // General ListView optimization code.
        if (view == null) {
            view = mInflator.inflate(R.layout.home_list_item_device, null);
            viewHolder = new HomeDeviceAdapter.ViewHolder();
            viewHolder.imageView = (ImageView) view.findViewById(R.id.device_img);
//            viewHolder.deviceAddress = (TextView) view
//                    .findViewById(R.id.device_address);
            viewHolder.deviceName = (TextView) view
                    .findViewById(R.id.device_name);
            view.setTag(viewHolder);
        } else {
            viewHolder = (HomeDeviceAdapter.ViewHolder) view.getTag();
        }

        DeviceModel device = mLeDevices.get(i);
        final String deviceName = device.name;
        if (deviceName != null && deviceName.length() > 0)
            viewHolder.deviceName.setText(deviceName);
        else
            viewHolder.deviceName.setText(R.string.unknown_device);
//        viewHolder.deviceAddress.setText(device.mac);


        return view;
    }

    class ViewHolder {
        ImageView imageView;
        TextView deviceName;
        TextView deviceAddress;
    }
}

