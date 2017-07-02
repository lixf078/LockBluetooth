package com.example.bluetooth.le.adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
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
import com.lock.lib.qr.QRCodeUtil;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

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
//        holder.setText(R.id.device_address, "Display QR Code");

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

        View renameView = swipeLayout.findViewById(R.id.rename_device);
        renameView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog(deviceModel);

            }
        });

        View qrView = swipeLayout.findViewById(R.id.display_qr_code);
        qrView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("name", deviceModel.name);
                    jsonObject.put("macStr", deviceModel.mac.replace(":", ""));
                    jsonObject.put("secretKey2", deviceModel.key);
                    showQRDialog(jsonObject.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });

        View deleteView = swipeLayout.findViewById(R.id.device_delete);
        deleteView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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

    Dialog dia;

    public void showQRDialog(String str){
        dia = new Dialog(mContext, R.style.edit_AlertDialog_style);
        dia.setContentView(R.layout.unlock_dialog);
        ImageView imageView = (ImageView) dia.findViewById(R.id.start_img);
        Bitmap bitmap = QRCodeUtil.encodeQRBitmap(str);
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
        dia.show();
    }

}

