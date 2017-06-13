package com.lock.lib.base.config;

import android.content.Context;
import android.text.TextUtils;

import com.lock.lib.base.BaseManager;
import com.lock.lib.base.Server;
import com.lock.lib.base.VolleyManager;
import com.lock.lib.base.event.ResponseEvent;
import com.lock.lib.common.constants.Constants;
import com.lock.lib.common.util.Logger;
import com.lock.lib.common.util.ShareUtil;

import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by hubing on 16/12/20.
 */

public class ConfigManager extends BaseManager implements IConfigManager {
    private static Object objClock = new Object();
    private static ConfigManager mInstance;
    private ConfigManager(Context context){
        super(context);
    }
    public static ConfigManager getInstance(Context context){
        synchronized (objClock){
            if(mInstance == null){
                mInstance = new  ConfigManager(context);
            }
            return mInstance;
        }
    }
    @Override
    public void fetchYQAvdConfig(final Context context, HashMap<String, String> map) {
        HashMap<String, String> params = resolveAPIJsonToPost(context, map);


        getJson(context,"http://hui.17house.com/static/ios/adv/yqAdv.json",null,new VolleyManager.ServerResponse<JSONObject>(){

            @Override
            public void onSuccess(JSONObject result) {
                Logger.e(Constants.TAG,"fetchYQAvdConfig >> result : "+result.toString());
                if(result!=null){
                    String interval = result.optString("interval");
                    String bannerUrl = result.optString("banner_url");
                    String avdImgUrl = result.optString("imagesrc");
                    boolean showAvd = !TextUtils.isEmpty(interval) && TextUtils.isDigitsOnly(interval) && Integer.valueOf(interval) > 0 ? true : false;
                    if(showAvd){
                        ShareUtil.getInstance(context).save("interval",interval);
                    }else{
                        ShareUtil.getInstance(context).remove("interval");
                    }

                    if(!TextUtils.isEmpty(bannerUrl) && showAvd){
                        ShareUtil.getInstance(context).save("bannerUrl",bannerUrl);
                    }else{
                        ShareUtil.getInstance(context).remove("bannerUrl");
                    }

                    if(!TextUtils.isEmpty(avdImgUrl) && showAvd){
                        String currentAvd = ShareUtil.getInstance(context).getStringValue("avdImgUrl",null);
                        if(!TextUtils.equals(currentAvd,avdImgUrl)){
                            ShareUtil.getInstance(context).save("avdImgUrl",avdImgUrl);
                            ShareUtil.getInstance(context).remove("avd_img_exists");
                        }
                    }else{
                        ShareUtil.getInstance(context).remove("avdImgUrl");
                        ShareUtil.getInstance(context).remove("avd_img_exists");
                    }

                    postEvent(ResponseEvent.TYPE_SERVER_FEATCH_AVD_CONFIG, null, Server.Code.SUCCESS, "");
                }else{
                    postEvent(ResponseEvent.TYPE_SERVER_FEATCH_AVD_CONFIG, null, -1, "数据异常");
                }
            }

            @Override
            public void onError(int errorCode, String errorMsg) {
                //ShareUtil.getInstance(context).remove("bannerUrl");
                //ShareUtil.getInstance(context).remove("interval");
                //ShareUtil.getInstance(context).remove("avdImgUrl");
                //ShareUtil.getInstance(context).remove("avd_img_exists");
                Logger.e(Constants.TAG,"remove share ----------------------------------------");
                postEvent(ResponseEvent.TYPE_SERVER_FEATCH_AVD_CONFIG, null, -1, "数据异常");
            }
        });
/*        get(context,"http://hui.17house.com/static/ios/adv/yqAdv.json",new VolleyManager.ServerResponse<String>(){

            @Override
            public void onSuccess(String result) {
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    String interval = jsonObject.optString("interval");
                    String bannerUrl = jsonObject.optString("banner_url");
                    String avdImgUrl = jsonObject.optString("imagesrc");

                    if(!TextUtils.isEmpty(interval)){
                        ShareUtil.getInstance(context).save("interval",interval);
                    }else{
                        ShareUtil.getInstance(context).remove("interval");
                    }

                    if(!TextUtils.isEmpty(bannerUrl)){
                        ShareUtil.getInstance(context).save("bannerUrl",bannerUrl);
                    }else{
                        ShareUtil.getInstance(context).remove("bannerUrl");
                    }

                    if(!TextUtils.isEmpty(avdImgUrl)){
                        ShareUtil.getInstance(context).save("avdImgUrl",avdImgUrl);
                    }else{
                        ShareUtil.getInstance(context).remove("avdImgUrl");
                    }

                    ShareUtil.getInstance(context).save("bannerUrl",bannerUrl);
                    ShareUtil.getInstance(context).save("avdImgUrl",avdImgUrl);

                    postEvent(ResponseEvent.TYPE_SERVER_FEATCH_AVD_CONFIG, null, Server.Code.SUCCESS, "");
                } catch (Exception e) {
                    postEvent(ResponseEvent.TYPE_SERVER_FEATCH_AVD_CONFIG, null, -1, "数据异常");
                }
            }

            @Override
            public void onError(int errorCode, String errorMsg) {
                postEvent(ResponseEvent.TYPE_SERVER_FEATCH_AVD_CONFIG, null, -1, "数据异常");
            }
        });*/


    }
}
