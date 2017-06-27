package com.example.bluetooth.le.adapter;

import android.app.Activity;
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

public class SettingLeDeviceListAdapter extends BaseAdapter {

    // Adapter for holding devices found through scanning.

    private List<DeviceModel> mLeDevices;
    private LayoutInflater mInflator;
    private Activity mContext;

    public SettingLeDeviceListAdapter(Activity c) {
        super();
        mContext = c;
        mLeDevices = new ArrayList<DeviceModel>();
        mInflator = mContext.getLayoutInflater();
    }

    public void addDevice(DeviceModel device) {
        if (!mLeDevices.contains(device)) {
            mLeDevices.add(device);
        }
    }

    public void setDevices(List<DeviceModel> devices){
        mLeDevices = devices;
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
        ViewHolder viewHolder;
        // General ListView optimization code.
        if (view == null) {
            view = mInflator.inflate(R.layout.setting_list_item_device_two, null);
            viewHolder = new ViewHolder();
            viewHolder.deviceName = (TextView) view.findViewById(R.id.device_name);
            viewHolder.editTextView = (TextView) view.findViewById(R.id.edit_device);
            viewHolder.deleteTextView = (TextView) view.findViewById(R.id.delete_device);
            viewHolder.shareTextView = (TextView) view.findViewById(R.id.share_device);
            viewHolder.optLayout = view.findViewById(R.id.opt_layout);

            viewHolder.deviceOptImg = (ImageView) view.findViewById(R.id.device_opt);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        DeviceModel device = mLeDevices.get(i);
        final String deviceName = device.name;
        if (deviceName != null && deviceName.length() > 0)
            viewHolder.deviceName.setText(deviceName);
        else
            viewHolder.deviceName.setText(R.string.unknown_device);
//
//        viewHolder.deviceOptImg.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//            }
//        });

        viewHolder.editTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        viewHolder.deleteTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        viewHolder.shareTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        return view;
    }

    class ViewHolder {
        TextView deviceName;
        ImageView deviceOptImg;
        TextView editTextView;
        TextView deleteTextView;
        TextView shareTextView;
        View optLayout;
    }
}
