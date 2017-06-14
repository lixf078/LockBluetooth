package com.lock.lib.api;

import android.content.Context;
import android.text.TextUtils;

import com.lock.lib.api.event.ResponseEvent;
import com.lock.lib.common.constants.Constants;
import com.lock.lib.common.util.DeviceUtil;
import com.lock.lib.common.util.Logger;
import com.lock.lib.common.util.ShareUtil;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by hubing on 16/4/12.
 */
public class BaseManager {
    public VolleyManager volleyManager;
    public static final int SUCCESS_CODE = 0;

    public static class Params {
        public final static String BUSINESS_TOKEN = "token";
        public static final String TYPE = "type";
    }


    public BaseManager(Context context) {
        volleyManager = VolleyManager.getInstance(context);
    }

    protected void post(Context context, String url, HashMap<String, String> map, VolleyManager.ServerResponse<String> response) {
        volleyManager.post(context, url, map, response);
    }

    protected void get(Context context, String url, VolleyManager.ServerResponse<String> response) {
        volleyManager.get(context, url, response);
    }

    protected void getJson(Context context, String url, JSONObject jsonRequest, VolleyManager.ServerResponse<JSONObject> response) {
        volleyManager.getJson(context, url, jsonRequest, response);
    }

    protected boolean isResponseSuccess(int code) {
        return Server.Code.SUCCESS == code;
    }


    public static class API {
        public final static String API_TOKEN = "api_token";
        public final static String API_UID = "api_uid";
    }

    protected void uploadFile2Server(Context context, HashMap<String, String> paramsMap) {

        if (null == paramsMap) {
            paramsMap = new HashMap<String, String>();
        }
        final String filePath = paramsMap.get("origin_file_path");
        final String compressPath = paramsMap.get("compress_file_path");
        volleyManager.uploadFile2Server(context, compressPath, paramsMap, new VolleyManager.ServerListener<String>() {
            @Override
            public void onSuccess(String result) {
                resolveJson(result, new OnRepSuccessListener() {

                    @Override
                    public void onSuccessString(String result) {
                        super.onSuccessString(result);
                        postEvent(ResponseEvent.TYPE_SERVER_UPLOAD_IMG, filePath + "," + compressPath + "," + result, Server.Code.SUCCESS, "");
                    }

                    @Override
                    public void onFail(int errorCode, String errorMsg) {
                        postEvent(ResponseEvent.TYPE_SERVER_UPLOAD_IMG, filePath, errorCode, errorMsg);
                    }
                });
            }

            @Override
            public void onError(int errorCode, String errorMsg) {
                postEvent(ResponseEvent.TYPE_SERVER_UPLOAD_IMG, filePath, errorCode, errorMsg);
            }

            @Override
            public void onProgress(long transferredBytes, long totalSize) {
                Logger.e(Constants.TAG, "base manager uploadFile2Server transferredBytes " + transferredBytes + ", totalSize " + totalSize);
            }
        });
    }

    protected String formatUrl(String url) {
        StringBuilder builder = new StringBuilder();
        builder.append(Server.getServerUrl());
        builder.append(url);

        return builder.toString();
    }

    protected String formatBusinessUrl(String url) {
        StringBuilder builder = new StringBuilder();
        builder.append(Server.getBusinessServerUrl());
        builder.append(url);
        return builder.toString();
    }

    protected String formatOrderUrl(String url) {
        StringBuilder builder = new StringBuilder();
        builder.append(Server.getOrderServerUrl());
        builder.append(url + "&model=android");
        return builder.toString();
    }

    protected String formatApiUrl(String url) {
        StringBuilder builder = new StringBuilder();
        builder.append(Server.getBusinessServerUrl());
        builder.append(url);

        return builder.toString();
    }

    protected String formatApiMapParamToString(Context context, String url, HashMap<String, String> params) {
        StringBuilder builder = new StringBuilder(Server.getApiServerUrl());
        builder.append(url);
        builder.append("&");
        if (params != null && !params.isEmpty()) {
            params.put("sessionToken", getToken(context));


            Iterator iter = params.entrySet().iterator();
            while (iter.hasNext()) {
                Map.Entry entry = (Map.Entry) iter.next();
                String key = (String) entry.getKey();
                String val = (String) entry.getValue();
                builder.append("&");
                builder.append(key);
                builder.append("=");
                builder.append(val);

            }
        }
        return builder.toString().replaceFirst("&", "");
    }

    protected String resolveJsonToGet(JSONObject json) {
        StringBuilder builder = new StringBuilder();
        if (json != null) {
            Iterator<?> it = json.keys();
            while (it.hasNext()) {
                String key = it.next().toString();
                try {
                    String value = json.getString(key);
                    builder.append("&");
                    builder.append(key);
                    builder.append("=");
                    builder.append(value);
                } catch (JSONException e) {
                    Logger.e(Constants.TAG, "resolve json to get error.", e);
                }
            }
        }

        return builder.toString();
    }

    protected HashMap<String, String> resolveJsonToPost(Context context, HashMap<String, String> map) {
        String token = getToken(context);
        if (map == null) {
            map = new HashMap<String, String>();
        }
        if (!TextUtils.isEmpty(token)) {
            map.put(Params.BUSINESS_TOKEN, getAPIToken(context));
        }
        /*if(json!=null){
            Iterator<?> it = json.keys();
            while (it.hasNext()){
                String key = it.next().toString();
                try {
                    String value = json.getString(key);
                    map.put(key,value);
                } catch (JSONException e) {
                    Logger.e(Constants.TAG,"resolve json to post error.",e);
                }
            }
        }*/

        return map;
    }

    protected HashMap<String, String> resolveAPIJsonToPost(Context context, HashMap<String, String> map) {
        String token = getAPIToken(context);
        if (map == null) {
            map = new HashMap<String, String>();
        }
        if (!TextUtils.isEmpty(token)) {
            map.put(Params.BUSINESS_TOKEN, getAPIToken(context));
        }
        /*if(json!=null){
            Iterator<?> it = json.keys();
            while (it.hasNext()){
                String key = it.next().toString();
                try {
                    String value = json.getString(key);
                    map.put(key,value);
                } catch (JSONException e) {
                    Logger.e(Constants.TAG,"resolve json to post error.",e);
                }
            }
        }*/

        return map;
    }

    protected HashMap<String, String> resolveOrderAPIJsonToPost(Context context, HashMap<String, String> map) {
        String token = getAPIToken(context);
        if (map == null) {
            map = new HashMap<String, String>();
        }
        if (!TextUtils.isEmpty(token)) {
            map.put(Params.BUSINESS_TOKEN, getAPIToken(context));
        }
        /*if(json!=null){
            Iterator<?> it = json.keys();
            while (it.hasNext()){
                String key = it.next().toString();
                try {
                    String value = json.getString(key);
                    map.put(key,value);
                } catch (JSONException e) {
                    Logger.e(Constants.TAG,"resolve json to post error.",e);
                }
            }
        }*/

        return map;
    }

    protected String resolveOrderAPIJsonToGet(Context context, HashMap<String, String> map) {
        StringBuilder builder = new StringBuilder();
        if (map != null) {
            Iterator<?> it = map.keySet().iterator();
            while (it.hasNext()) {
                String key = it.next().toString();
                try {
                    String value = map.get(key);
                    builder.append("&");
                    builder.append(key);
                    builder.append("=");
                    builder.append(value);
                } catch (Exception e) {
                    Logger.e(Constants.TAG, "resolve json to get error.", e);
                }
            }
        }

        return builder.toString();
    }


    public String getToken(Context context) {
        return ShareUtil.getInstance(context).getStringValue(Constants.Key.KEY_SESSION_CODE, "");
    }

    public String getAPIToken(Context context) {
        return ShareUtil.getInstance(context).getStringValue(API.API_TOKEN, "");
    }

    public String getUid(Context context) {
        long uid = ShareUtil.getInstance(context).getLongValue(Constants.Key.KEY_USER_ID, Constants.INVALID);
        if (uid != Constants.INVALID) {
            return String.valueOf(uid);
        }
        return null;
    }

    protected String formatMapParamToString(Context context, HashMap<String, String> params) {
        StringBuilder builder = new StringBuilder(Server.getServerUrl());
        if (params != null && !params.isEmpty()) {
            params.put("sessionToken", getToken(context));
            params.put("uuid", (TextUtils.equals(params.get(Server.Param.PERMISSION), Server.Permission.PERMISSION_YES)) ? DeviceUtil.getDeviceId(context, true) : DeviceUtil.getDeviceId(context, false));
            params.put("model", "android");
            String uid = getUid(context);
            if (!TextUtils.isEmpty(uid) && !params.containsKey("uid")) {
                params.put("uid", uid);
            }

            Iterator iter = params.entrySet().iterator();
            while (iter.hasNext()) {
                Map.Entry entry = (Map.Entry) iter.next();
                String key = (String) entry.getKey();
                String val = (String) entry.getValue();
                builder.append("&");
                builder.append(key);
                builder.append("=");
                builder.append(val);
            }
        }
        return builder.toString().replaceFirst("&", "");
    }

    protected void postEvent(int eventType, Object resultObj, int errorCode, String msg) {
        ResponseEvent event = new ResponseEvent();
        event.eventType = eventType;
        event.errorCode = errorCode;
        if (resultObj != null) {
            event.resultObj = resultObj;
        }
        event.errorMsg = msg;
        EventBus.getDefault().post(event);
    }

    protected void resolveJson(String json, OnRepSuccessListener listener) {
        if (!TextUtils.isEmpty(json)) {
            try {
                JSONObject jsonObject = new JSONObject(json);
                int code = Integer.valueOf(jsonObject.optString("error"));
                String msg = jsonObject.optString("message");
                if (isResponseSuccess(code)) {
                    if (jsonObject.has("data")) {
                        if (jsonObject.opt("data") instanceof JSONObject) {
                            if (jsonObject.has("currentPage")) {
                                listener.onJsonObjWidthPageSuccess(jsonObject.optJSONObject("data"), String.valueOf(jsonObject.optInt("currentPage")), String.valueOf(jsonObject.optInt("totalCount")));
                            } else {
                                listener.onSuccess(jsonObject.optJSONObject("data"));
                            }

                        } else if (jsonObject.opt("data") instanceof JSONArray) {
                            if (jsonObject.has("currentPage")) {
                                if (jsonObject.has("newCount")) {
                                    JSONObject otherJson = new JSONObject();
                                    otherJson.put("currentPage", String.valueOf(jsonObject.optInt("currentPage")));
                                    otherJson.put("totalCount", String.valueOf(jsonObject.optInt("totalCount")));
                                    otherJson.put("newCount", String.valueOf(jsonObject.optInt("newCount")));
                                    listener.onJsonArrayWidthPageSuccess(jsonObject.getJSONArray("data"), otherJson);
                                } else {
                                    listener.onJsonArrayWidthPageSuccess(jsonObject.getJSONArray("data"), String.valueOf(jsonObject.optInt("currentPage")), String.valueOf(jsonObject.optInt("totalCount")));
                                }
                            } else {
                                listener.onJsonArraySuccess(jsonObject.getJSONArray("data"));
                            }

                        } else if (jsonObject.opt("data") instanceof String) {
                            listener.onSuccessString(jsonObject.optString("data"));
                        } else if (jsonObject.opt("data") instanceof Integer) {
                            listener.onSuccessInteger(jsonObject.optInt("data"));
                        }
                    } else {
                        listener.onSuccess();
                    }
                } else {
                    listener.onFail(code, msg);
                }
            } catch (Exception e) {
                Logger.e(Constants.TAG, "", e);
                listener.onFail(Constants.INVALID, Server.ERROR_SERVER_MSG);
            }
        }
    }

    protected void resolveJsonJava(String json, OnRepSuccessListener listener) {
        if (!TextUtils.isEmpty(json)) {
            try {
                JSONObject jsonObject = new JSONObject(json);
                jsonObject = jsonObject.optJSONObject("baseOutput");
                if (jsonObject != null) {
                    int code = jsonObject.optInt("code");
                    String msg = jsonObject.optString("message");
                    if (isResponseSuccess(code)) {
                        if (jsonObject.has("data")) {
                            if (jsonObject.opt("data") instanceof JSONObject) {
                                if (jsonObject.has("currentPage")) {
                                    listener.onJsonObjWidthPageSuccess(jsonObject.optJSONObject("data"), String.valueOf(jsonObject.optInt("currentPage")), String.valueOf(jsonObject.optInt("totalCount")));
                                } else {
                                    listener.onSuccess(jsonObject.optJSONObject("data"));
                                }

                            } else if (jsonObject.opt("data") instanceof JSONArray) {
                                if (jsonObject.has("currentPage")) {
                                    if (jsonObject.has("newCount")) {
                                        JSONObject otherJson = new JSONObject();
                                        otherJson.put("currentPage", String.valueOf(jsonObject.optInt("currentPage")));
                                        otherJson.put("totalCount", String.valueOf(jsonObject.optInt("totalCount")));
                                        otherJson.put("newCount", String.valueOf(jsonObject.optInt("newCount")));
                                        listener.onJsonArrayWidthPageSuccess(jsonObject.getJSONArray("data"), otherJson);
                                    } else {
                                        listener.onJsonArrayWidthPageSuccess(jsonObject.getJSONArray("data"), String.valueOf(jsonObject.optInt("currentPage")), String.valueOf(jsonObject.optInt("totalCount")));
                                    }
                                } else {
                                    listener.onJsonArraySuccess(jsonObject.getJSONArray("data"));
                                }

                            } else if (jsonObject.opt("data") instanceof String) {
                                listener.onSuccessString(jsonObject.optString("data"));
                            } else if (jsonObject.opt("data") instanceof Integer) {
                                listener.onSuccessInteger(jsonObject.optInt("data"));
                            }
                        } else {
                            listener.onSuccess();
                        }
                    } else {
                        listener.onFail(code, msg);
                    }
                }
            } catch (Exception e) {
                Logger.e(Constants.TAG, "", e);
                listener.onFail(Constants.INVALID, Server.ERROR_SERVER_MSG);
            }
        }
    }

    protected abstract class OnRepSuccessListener {
        public void onSuccess() {
        }

        ;

        public void onSuccess(JSONObject json) {
        }

        ;

        public void onJsonObjWidthPageSuccess(JSONObject json, String currentPage, String total) {
        }

        ;

        public void onSuccessString(String result) {
        }

        ;

        public void onJsonArraySuccess(JSONArray jsonArray) {
        }

        ;

        public void onJsonArrayWidthPageSuccess(JSONArray jsonArray, JSONObject json) {
        }

        ;

        public void onJsonArrayWidthPageSuccess(JSONArray jsonArray, String currentPage, String total) {
        }

        ;

        public void onSuccessInteger(int result) {
        }

        ;

        public abstract void onFail(int errorCode, String errorMsg);
    }

    protected boolean isSuccess(int code) {
        return SUCCESS_CODE == code;
    }
}
