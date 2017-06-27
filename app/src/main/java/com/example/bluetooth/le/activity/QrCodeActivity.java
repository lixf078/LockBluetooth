package com.example.bluetooth.le.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;

import com.example.bluetooth.le.DeviceModel;
import com.example.bluetooth.le.DeviceShare;
import com.google.zxing.client.android.CaptureActivity;
import com.lock.lib.common.constants.Constants;
import com.lock.lib.common.util.Logger;
import com.lock.lib.common.util.ToastUtil;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by admin on 17/6/15.
 */
public class QrCodeActivity extends CaptureActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void dealScanResult(String result){
        Logger.e(Constants.TAG, "QrCodeActivity dealScanResult result " + result);
        if(TextUtils.isEmpty(result)){
            ToastUtil.showToast(this,"QRcode error !");
            return;
        }
        Intent intent = null;
        try {
            JSONObject jsonObject = new JSONObject(result);
            String name = jsonObject.optString("name");
            String macStr = jsonObject.optString("macStr");
            String secretKey2 = jsonObject.optString("secretKey2");
            Logger.e(Constants.TAG, "QrCodeActivity dealScanResult name " + name + ", macStr " + macStr + ", secretKey2 " + secretKey2);

            DeviceModel deviceModel = new DeviceModel();
            deviceModel.name = name;
            deviceModel.mac = macStr;
            deviceModel.key = secretKey2;
            DeviceShare.saveDevice(QrCodeActivity.this, deviceModel);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


}
