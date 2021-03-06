package com.lock.bluetooth.le.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;

import com.lock.bluetooth.le.DeviceModel;
import com.lock.bluetooth.le.DeviceShare;
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
            String name = jsonObject.getString("name");
            String macStr = jsonObject.optString("macStr");
            String secretKey2 = jsonObject.optString("secretKey2");
            Logger.e(Constants.TAG, "QrCodeActivity dealScanResult name " + name + ", macStr " + macStr + ", secretKey2 " + secretKey2);

            DeviceModel deviceModel = new DeviceModel();
            deviceModel.name = name;
            deviceModel.mac = dealString(macStr);
            deviceModel.key = secretKey2;
            deviceModel.deviceType = 1;
            DeviceShare.saveDevice(QrCodeActivity.this, deviceModel);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String dealString(String str){
        StringBuffer stringBuffer = new StringBuffer();
        String result = "";

        for (int i = 0, j = 6; i<j ; i ++){
            result += str.substring(2*i , i*2 +2) + ":";
        }
//        result.substring(0, result.length() -1);
        return result.substring(0, result.length() -1);
    }

}
