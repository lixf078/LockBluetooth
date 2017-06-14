package com.lock.lib.api;

import android.content.ContentResolver;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.widget.ImageView;

import com.lock.lib.common.constants.Constants;
import com.lock.lib.common.util.AppUtil;
import com.lock.lib.common.util.DeviceUtil;
import com.lock.lib.common.util.Logger;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.error.AuthFailureError;
import com.android.volley.error.VolleyError;
import com.android.volley.request.ImageRequest;
import com.android.volley.request.JsonObjectRequest;
import com.android.volley.request.SimpleMultiPartRequest;
import com.android.volley.request.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by hubing on 16/3/17.
 */
public class VolleyManager {
    private static final String SERVER_INTERFACE_VERSION = "1.1";

    private static final String UPLOAD_FILE_URL = "http://bbs.17house.com/motnt/index.php?a=uploadImg";

    private static Object clockObj = new Object();
    private static VolleyManager mInstance;

    private final RequestQueue mRequestQueue;
    private final HashMap<String, String> mHeaders = new HashMap<String, String>();
    private final HashMap<String, String> mFileHeaders = new HashMap<>();
    private StringBuilder stringBuilder = new StringBuilder();

    private VolleyManager (Context context){
        mRequestQueue = Volley.newRequestQueue(context);
        //final TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        mHeaders.put("model", AppUtil.getBrand() + AppUtil.getModel());
        //mHeaders.put("imei", tm.getDeviceId());
        mHeaders.put("uuid", "");
        mHeaders.put("deviceId", DeviceUtil.getAndroidId(context));
        //mHeaders.put("netType", NetUtil.getNetworkTypeName(tm.getNetworkType()));
        mHeaders.put("appVersion", AppUtil.getVersionName(context));
        mHeaders.put("location", "");
        mHeaders.put("channel", AppUtil.getMetaDataValue(context, Constants.Key.KEY_CHANNEL, ""));
        mHeaders.put("platform", "Android");
        mFileHeaders.putAll(mHeaders);
    }
    public static VolleyManager getInstance(Context context){
        synchronized (clockObj){
            if(mInstance == null){
                mInstance = new VolleyManager(context);
            }
            return mInstance;
        }
    }

    public void get(Context context, String url, ServerResponse<String> response){
        StringBuilder builder = new StringBuilder(url);
        builder.append("&app_version=");
        builder.append("android_");
        builder.append(AppUtil.getPackageName(context));
        builder.append("_");
        builder.append(SERVER_INTERFACE_VERSION);
//        builder.append("&");
//        builder.append(Server.Param.CITY_NAME);
//        builder.append("=");
//        builder.append("");
        requestStringDataFromServer(Request.Method.GET,builder.toString(),null,response);
    }

    public void post(Context context, String url, HashMap<String,String> paramsMap, ServerResponse<String> response){
        StringBuilder builder = new StringBuilder();
        builder.append("android_");
        builder.append(AppUtil.getPackageName(context));
        builder.append("_");
        builder.append(SERVER_INTERFACE_VERSION);
        paramsMap.put("app_version", builder.toString());
        requestStringDataFromServer(Request.Method.POST,url,paramsMap,response);
    }

    public void postImage(Context context, String url, HashMap<String, String> paramsMap, ServerResponse<ResponImageBean> response) {
        StringBuilder builder = new StringBuilder();
        builder.append("android_");
        builder.append(AppUtil.getPackageName(context));
        builder.append("_");
        builder.append(SERVER_INTERFACE_VERSION);
        paramsMap.put("app_version", builder.toString());
        requestImageForServer(context, url, paramsMap, response);
    }
    private String getMap(String url, HashMap<String, String> paramsMap) {
        if (stringBuilder == null) return url;
        stringBuilder.delete(0, stringBuilder.length());
        stringBuilder.append(url);
        stringBuilder.append("?");
        Iterator iter = paramsMap.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry entry = (Map.Entry) iter.next();
            stringBuilder.append(entry.getKey());
            stringBuilder.append("=");
            stringBuilder.append(entry.getValue());
            stringBuilder.append("&");
        }
        stringBuilder.delete(stringBuilder.length() - 1, stringBuilder.length());
        return stringBuilder.toString();
    }
    private void requestImageForServer(final Context context, final String url, final HashMap<String, String> paramsMap, final ServerResponse<ResponImageBean> serverResponse) {

        final ResponImageBean responImageBean = new ResponImageBean();
        if (serverResponse == null) {
            throw new IllegalStateException("call back is null");
        }
        if (paramsMap == null || paramsMap.size() < 1) {
            Logger.e(Constants.TAG, "request url:  { " + url + " }");
        } else {
            Logger.e(Constants.TAG, "request url:  { " + getMap(url, paramsMap));
        }
        final ImageRequest request = new ImageRequest(url, null, new ContentResolver(context) {
            @Nullable
            @Override
            public String[] getStreamTypes(@NonNull Uri url, @NonNull String mimeTypeFilter) {
                return super.getStreamTypes(url, mimeTypeFilter);
            }
        }, new Response.Listener<Bitmap>() {

            @Override
            public void onResponse(Bitmap response) {
                responImageBean.setBitmap(response);
                serverResponse.onSuccess(responImageBean);
            }
        }, 0, 0, ImageView.ScaleType.CENTER_INSIDE, Bitmap.Config.RGB_565, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                final int statusCode = ((error != null) && (error.networkResponse != null)) ? error.networkResponse.statusCode : Constants.INVALID;
                final String errorMsg = error != null ? (!TextUtils.isEmpty(error.getMessage()) ? "网络异常，请稍后再试" : "网络异常，请稍后再试") : "网络异常，请稍后再试";
                serverResponse.onError(statusCode, errorMsg);
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return mHeaders;
            }

            @Override
            protected Response<Bitmap> parseNetworkResponse(NetworkResponse response) {
                String string = new String(response.headers.get("Set-Cookie"));
                if (!TextUtils.isEmpty(string)){
                    HashMap<String,String> headMap = new HashMap<>();
                    String[] array2;
                    String[] array1 = string.split(",");
                    for (int i = 0; i < array1.length; i++) {
                        array2 = array1[i].split("=");
                        if (array2.length == 2){
                            headMap.put(array2[0],array2[1]);
                        }
                    }
                    responImageBean.setMap(headMap);
                }
                return super.parseNetworkResponse(response);
            }
        };
        request.setShouldCache(false);
        mRequestQueue.add(request);
    }

    public void getJson(Context context, String url, JSONObject jsonRequest, ServerResponse<JSONObject> response){
        requestJsonDataFromServer(Request.Method.GET,url,jsonRequest,response);
    }

    private void requestJsonDataFromServer(int method, final String url, final JSONObject jsonRequest, final ServerResponse<JSONObject> serverResponse){
        if(serverResponse == null){
            throw new IllegalStateException("call back is null");
        }
        Logger.e(Constants.TAG, "request json url:  { " + url + " }");
        final JsonObjectRequest request = new JsonObjectRequest(method,url,jsonRequest,new Response.Listener<JSONObject>(){

            @Override
            public void onResponse(JSONObject response) {
                serverResponse.onSuccess(response);
            }

        },new Response.ErrorListener(){

            @Override
            public void onErrorResponse(VolleyError error) {
                final int statusCode = ((error != null) && (error.networkResponse != null)) ? error.networkResponse.statusCode : Constants.INVALID;
                final String errorMsg = error!=null ? (!TextUtils.isEmpty(error.getMessage())  ? statusCode == Constants.INVALID ? "主人，网络开小差了，请一会再试试吧" : error.getMessage() : "主人，网络开小差了，请一会再试试吧！") : "主人，网络开小差了，请一会再试试吧！";
                serverResponse.onError(statusCode,errorMsg);
                Logger.e(Constants.TAG, "json error response for url: {" + url + "} " + error.getMessage(), error);
            }
        });
        request.setShouldCache(false);
        mRequestQueue.add(request);
    }

    private void requestStringDataFromServer(int method, final String url, final HashMap<String,String> paramsMap, final ServerResponse<String> serverResponse){

        if(serverResponse == null){
            throw new IllegalStateException("call back is null");
        }
        Logger.e(Constants.TAG, "request url:  { " + url + " }");
        final StringRequest request = new StringRequest(method, url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Logger.e(Constants.TAG, "request url:  { " + url + " } ; response : "+response);
                serverResponse.onSuccess(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                final int statusCode = ((error != null) && (error.networkResponse != null)) ? error.networkResponse.statusCode : Constants.INVALID;
                final String errorMsg = error!=null ? (!TextUtils.isEmpty(error.getMessage())  ? statusCode == Constants.INVALID ? "主人，网络开小差了，请一会再试试吧" : error.getMessage() : "主人，网络开小差了，请一会再试试吧！") : "主人，网络开小差了，请一会再试试吧！";
                serverResponse.onError(statusCode,errorMsg);
                Logger.e(Constants.TAG, "error response for url: {" + url + "} " + error.getMessage(), error);
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return mHeaders;
            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                final int method = getMethod();
                if (method == Method.GET) {
                    return null;
                } else if (method == Method.POST) {
                    Logger.e(Constants.TAG, "request post data: " + paramsMap.toString());
                    return paramsMap;
                }
                return null;
            }
        };
        request.setShouldCache(false);
        mRequestQueue.add(request);
    }

    public void uploadFile2Server(Context context, final String url, final HashMap<String,String> paramsMap, final ServerListener<String> serverResponse){

        StringBuilder builder = new StringBuilder();
        builder.append("android_");
        builder.append(AppUtil.getPackageName(context));
        builder.append("_");
        builder.append(SERVER_INTERFACE_VERSION);
        paramsMap.put("app_version", builder.toString());
        paramsMap.put("a", "uploadImg");
        paramsMap.put("file", url);

        if(serverResponse == null){
            throw new IllegalStateException("call back is null");
        }
        Logger.e(Constants.TAG, "request url:  { " + url + " }");
        final SimpleMultiPartRequest request = new SimpleMultiPartRequest(Request.Method.POST, UPLOAD_FILE_URL, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Logger.e(Constants.TAG, "request url:  { " + url + " } ; response : "+response);
                serverResponse.onSuccess(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                final int statusCode = ((error != null) && (error.networkResponse != null)) ? error.networkResponse.statusCode : Constants.INVALID;
                final String errorMsg = error!=null ? TextUtils.isEmpty(error.getMessage()) ? "主人，网络开小差了，请一会再试试吧！" : "主人，网络开小差了，请一会再试试吧！" : "主人，网络开小差了，请一会再试试吧！";
                serverResponse.onError(statusCode,errorMsg);
                Logger.e(Constants.TAG, "error response for url: {" + url + "} " + error.getMessage(), error);
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return mHeaders;
            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                final int method = getMethod();
                if (method == Method.GET) {
                    return null;
                } else if (method == Method.POST) {
                    Logger.e(Constants.TAG, "request post data: " + paramsMap.toString());
                    return paramsMap;
                }
                return null;
            }
        };
        request.addFile("file", url);

        request.setShouldCache(false);
        mRequestQueue.add(request);
    }



    public static interface ServerResponse<T>{
        public void onSuccess(T result);
        public void onError(int errorCode, String errorMsg);
    }

    public static interface ServerListener<T>{
        public void onSuccess(T result);
        public void onError(int errorCode, String errorMsg);
        public void onProgress(long transferredBytes, long totalSize);
    }

}
