package com.lock.bluetooth.le;

import android.text.TextUtils;

/**
 * Created by oriolexia on 17/6/25.
 */

public class DeviceModel {
    public String mac;
    public String name;
    public String key;

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (obj instanceof DeviceModel){
            DeviceModel deviceModel = (DeviceModel) obj;
            if (!TextUtils.isEmpty(mac) && !TextUtils.isEmpty(deviceModel.mac)){
                if (mac.equalsIgnoreCase(deviceModel.mac)){
                    return true;
                }
            }
            return false;
        }else{
            return false;
        }
    }
}
