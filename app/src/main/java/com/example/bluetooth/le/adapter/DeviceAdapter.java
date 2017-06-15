package com.example.bluetooth.le.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.bluetooth.le.R;
import com.facebook.drawee.view.SimpleDraweeView;
import com.lock.lib.api.device.LockBluetoothDevice;
import com.lock.lib.common.constants.Constants;
import com.lock.lib.common.util.Logger;

import java.util.ArrayList;

/**
 * Created by lixufeng on 16/4/11.
 */
public class DeviceAdapter extends GBaseAdapter {

    private ArrayList<LockBluetoothDevice> blocks;

    private Context mContext;

    public DeviceAdapter(Context context) {
        mContext = context;
    }

    public void setItemArrayList(ArrayList<LockBluetoothDevice> blocks) {
        this.blocks = blocks;
    }

    public void addItemArrayList(ArrayList<LockBluetoothDevice> blocks) {
        this.blocks.addAll(blocks);
    }

    @Override
    public int getCount() {
        return blocks != null ? blocks.size() : 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder holder = null;
        if (convertView == null) {
            holder = new Holder();
            convertView = View.inflate(mContext, R.layout.setting_list_item_device, null);
            holder.deviceName = (TextView) convertView.findViewById(R.id.device_name);
            holder.deviceMac = (TextView) convertView.findViewById(R.id.device_address);
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }

        LockBluetoothDevice item = blocks.get(position);
        if (item != null) {
            if (!TextUtils.isEmpty(item.name)) {
                holder.deviceName.setText(item.name);
            }

            if (!TextUtils.isEmpty(item.mac)) {
                holder.deviceMac.setText(item.mac);
            }
        }
        return convertView;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public LockBluetoothDevice getItem(int position) {
        return blocks.get(position);
    }

    class Holder {

        public TextView deviceName;
        public TextView deviceMac;
    }
}

