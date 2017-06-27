package com.example.bluetooth.le.adapter;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.bluetooth.le.DeviceModel;
import com.example.bluetooth.le.DeviceShare;
import com.example.bluetooth.le.MainActivity;
import com.example.bluetooth.le.R;
import com.example.bluetooth.le.Utils;
import com.example.bluetooth.le.fragment.SettingFragment;
import com.example.bluetooth.le.view.EditDialog;
import com.example.bluetooth.le.view.SwipeLayout;
import com.example.bluetooth.le.view.SwipeLayoutManager;
import com.lock.lib.api.Server;
import com.lock.lib.api.event.ResponseEvent;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lixufeng on 16/4/11.
 */
public class DeviceAdapter extends CommonAdapter<DeviceModel> {

    private List<DeviceModel> blocks;

    private Context mContext;

    public DeviceAdapter(Context context, List<DeviceModel> datas, int layoutId) {
        super(context, datas, layoutId);
        mContext = context;
    }

    public void setItemArrayList(List<DeviceModel> blocks) {
        this.blocks = blocks;
    }

    public void addItemArrayList(List<DeviceModel> blocks) {
        this.blocks.addAll(blocks);
    }

    @Override
    public void convert(ViewHolder holder, final DeviceModel deviceModel) {

        holder.setText(R.id.device_name, deviceModel.name);
        holder.setText(R.id.device_address, deviceModel.mac);

        final SwipeLayout swipeLayout = holder.getView(R.id.swipelayout);

        swipeLayout.setOnSwipeLayoutClickListener(new SwipeLayout.OnSwipeLayoutClickListener() {
            @Override
            public void onClick() {
                showDialog(deviceModel);
            }
        });

        (swipeLayout.getDeleteView()).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SwipeLayoutManager.getInstance().closeOpenInstance();
                DeviceShare.deleteDevice(mContext, deviceModel);
                postResponseEvent(ResponseEvent.TYPE_DELETE_DEVICE, Server.Code.SUCCESS, "", deviceModel);
            }
        });
    }

    private void postResponseEvent(int eventType, int errorCode, String errorMsg, DeviceModel deviceModel) {
        ResponseEvent event = new ResponseEvent();
        event.eventType = eventType;
        event.errorCode = errorCode;
        event.errorMsg = errorMsg;
        event.resultObj = deviceModel;
        EventBus.getDefault().post(event);
    }

    void showDialog(final DeviceModel model) {
        EditDialog editDialog = new EditDialog(mContext, model.name);
        editDialog.show();
        editDialog.setOnPosNegClickListener(new EditDialog.OnPosNegClickListener() {
            @Override
            public void posClickListener(String value) {
                model.name = value;
                DeviceShare.editDevice(mContext, model);
                postResponseEvent(ResponseEvent.TYPE_EDIT_DEVICE, Server.Code.SUCCESS, "", model);
            }

            @Override
            public void negCliclListener(String value) {

            }
        });
    }
}

