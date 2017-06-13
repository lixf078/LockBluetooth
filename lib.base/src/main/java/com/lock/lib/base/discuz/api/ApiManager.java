package com.aiyiqi.lib.api.discuz.api;

import android.content.Context;

import com.aiyiqi.lib.api.BaseManager;
import com.aiyiqi.lib.api.Server;
import com.aiyiqi.lib.api.VolleyManager;
import com.aiyiqi.lib.api.event.ResponseEvent;
import com.aiyiqi.lib.common.constants.Constants;
import com.aiyiqi.lib.common.util.Logger;

import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by hubing on 16/6/2.
 */
public class ApiManager extends BaseManager implements IApiManager{
    private static Object clockObj = new Object();
    private static ApiManager mInstance;

    public static ApiManager getInstance(Context context){
        synchronized (clockObj){
            if(mInstance == null){
                mInstance = new ApiManager(context);
            }
            return mInstance;
        }
    }
    private ApiManager(Context context) {
        super(context);
    }

    @Override
    public void fetchVerifyCode(Context context, HashMap<String, String> paramap) {

        if(paramap==null){
            paramap = new HashMap<String, String>();
        }
        paramap.put("action","getPhoneCode");

        get(context, formatApiMapParamToString(context,"/UserApi.php?version=1",paramap), new VolleyManager.ServerResponse<String>() {
            @Override
            public void onSuccess(String result) {
                resolveJson(result, new OnRepSuccessListener() {

                    @Override
                    public void onSuccess(JSONObject json) {
                        super.onSuccess(json);

                        postEvent(ResponseEvent.TYPE_SERVER_VERIFY_CODE,null, Server.Code.SUCCESS,null);
                    }

                    @Override
                    public void onFail(int errorCode, String errorMsg) {
                        postEvent(ResponseEvent.TYPE_SERVER_VERIFY_CODE,null, errorCode,errorMsg);
                    }
                });
            }

            @Override
            public void onError(int errorCode, String errorMsg) {
                postEvent(ResponseEvent.TYPE_SERVER_VERIFY_CODE,null, errorCode,errorMsg);
            }
        });
    }
}
