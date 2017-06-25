package com.example.bluetooth.le.adapter;

import android.content.Context;
import android.widget.Toast;

import com.example.bluetooth.le.DeviceModel;
import com.example.bluetooth.le.R;
import com.example.bluetooth.le.view.SwipeLayout;

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
    public void convert(ViewHolder holder, final DeviceModel DeviceModel) {
        holder.setText(R.id.device_name, DeviceModel.name);
        holder.setText(R.id.device_address, DeviceModel.mac);

        final SwipeLayout swipeLayout = holder.getView(R.id.swipelayout);

        swipeLayout.setOnSwipeLayoutClickListener(new SwipeLayout.OnSwipeLayoutClickListener() {
            @Override
            public void onClick() {
                Toast.makeText(mContext, DeviceModel.name, Toast.LENGTH_SHORT).show();
            }
        });

//        ((LinearLayout)swipeLayout.getDeleteView()).getChildAt(0).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(mContext, "call", Toast.LENGTH_SHORT).show();
//            }
//        });



//        ((LinearLayout)swipeLayout.getDeleteView()).getChildAt(1).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                SwipeLayoutManager.getInstance().closeOpenInstance();
////                datas.remove(s);
////                adapter.notifyDataSetChanged();
////                Toast.makeText(mContext, "datas.size():" + datas.size(), Toast.LENGTH_SHORT).show();
//            }
//        });
    }
}

