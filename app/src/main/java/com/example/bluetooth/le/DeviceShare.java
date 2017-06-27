package com.example.bluetooth.le;

import android.content.Context;
import android.text.TextUtils;

import com.lock.lib.common.util.ShareUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by oriolexia on 17/6/25.
 */

public class DeviceShare {

    public static void saveDevice(Context context, DeviceModel deviceModel){
        List<DeviceModel> list = getDevices(context);
        if (list == null){
            list = new ArrayList<>();
        }
        list.add(deviceModel);
        saveDevices(context, list);
    }

    public static void deleteDevice(Context context, DeviceModel deviceModel){
        List<DeviceModel> list = getDevices(context);
        if (list == null){
            list = new ArrayList<>();
        }
        for (int i = 0, j = list.size(); i<j; i++){
            DeviceModel model = list.get(i);
            if (model.mac.equals(deviceModel.mac)){
                list.remove(model);
                break;
            }
        }

        saveDevices(context, list);
    }

    public static void editDevice(Context context, DeviceModel deviceModel){
        List<DeviceModel> list = getDevices(context);
        if (list == null){
            list = new ArrayList<>();
        }
        for (int i = 0, j = list.size(); i<j; i++){
            DeviceModel model = list.get(i);
            if (model.mac.equals(deviceModel.mac)){
                model.name = deviceModel.name;
            }
        }
        saveDevices(context, list);
    }

    public static void saveDevices(Context context, List<DeviceModel> deviceModels){
        JSONArray jsonArray = new JSONArray();
        for (DeviceModel model: deviceModels){
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("name", model.name);
                jsonObject.put("mac", model.mac);
                jsonObject.put("key", model.key);
                jsonArray.put(jsonObject);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        if (jsonArray.length() > 0){
            ShareUtil.getInstance(context).save("devices", jsonArray.toString());
        }else{
            ShareUtil.getInstance(context).save("devices", "");
        }
    }

    public static List<DeviceModel> getDevices(Context context){
        String jsonString = ShareUtil.getInstance(context).getStringValue("devices", "");
        List<DeviceModel> list = new ArrayList<>();
        try {
            if (!TextUtils.isEmpty(jsonString)){
                JSONArray jsonArray = new JSONArray(jsonString);
                if (jsonArray != null && jsonArray.length()>0){
                    list = new ArrayList<>();
                    for(int i = 0,j = jsonArray.length(); i<j; i++){
                        JSONObject jsonObject = jsonArray.optJSONObject(i);
                        DeviceModel device = new DeviceModel();
                        device.name = jsonObject.optString("name");
                        device.mac = jsonObject.optString("mac");
                        device.key = jsonObject.optString("key");
                        list.add(device);
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return list;
    }
}
