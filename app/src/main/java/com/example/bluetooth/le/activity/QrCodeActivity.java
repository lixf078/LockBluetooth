package com.example.bluetooth.le.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;

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
            ToastUtil.showToast(this,"主人，识别出错啦，请确认二维码是否正确");
            return;
        }
        Intent intent = null;
        try {
            JSONObject jsonObject = new JSONObject(result);
            int action = jsonObject.optInt("action");
            String value = jsonObject.optString("value");
            String qrCodeId = jsonObject.optString("qrCodeId");
            Logger.e(Constants.TAG, "QrCodeActivity dealScanResult action " + action + ", value " + value);

            switch (action){
                case 0:{
                    intent = new Intent(QrCodeActivity.this, WebActivity.class);
                    intent.putExtra(Constants.Key.Key_WEB_URL, "" + value);
                    break;
                }
                default:{
                    Toast.makeText(this, "维护中", Toast.LENGTH_SHORT).show();
                }
            }
        } catch (JSONException e) {
            if(isURL(result)){
                if(!result.toUpperCase().startsWith("HTTP")){
                    result += "http://";
                }
                intent = new Intent(QrCodeActivity.this, WebActivity.class);
                intent.putExtra(Constants.Key.Key_WEB_URL, result);
            }else{
                Toast.makeText(QrCodeActivity.this, "错误的二维码信息:" + result, Toast.LENGTH_SHORT).show();
            }
            e.printStackTrace();
        }
        if(null != intent){
            startActivity(intent);
        }
    }


}
