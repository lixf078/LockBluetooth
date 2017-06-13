package com.aiyiqi.lib.api.discuz.msg;

import android.content.Context;

import com.aiyiqi.lib.api.BaseManager;
import com.aiyiqi.lib.api.Server;
import com.aiyiqi.lib.api.VolleyManager;
import com.aiyiqi.lib.api.discuz.Page;
import com.aiyiqi.lib.api.event.ResponseEvent;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by hubing on 16/4/15.
 */
public class MsgManager extends BaseManager implements IMsgManager {

    private static Object clockObj = new Object();
    private static MsgManager mInstance;
    private MsgManager (Context context){
        super(context);
    }

    public static MsgManager getInstance(Context context){
        synchronized (clockObj){
            if(mInstance == null){
                mInstance = new MsgManager(context);
            }
            return mInstance;
        }
    }
    @Override
    public void getMsgTotalCount(Context context, HashMap<String, String> params) {
        //m=user&c=userNotice&a=noticeNum

        if(params==null){
            params = new HashMap<String, String>();
        }
        params.put("m","user");
        params.put("c","userNotice");
        params.put("a","noticeNum");

        get(context, formatMapParamToString(context, params), new VolleyManager.ServerResponse<String>() {
            @Override
            public void onSuccess(String result) {
                resolveJson(result, new OnRepSuccessListener() {

                    @Override
                    public void onSuccess(JSONObject json) {
                        super.onSuccess(json);
                        MsgBean msgBean = new MsgBean();
                        if(json!=null){
                            msgBean.total = String.valueOf(json.optInt("num"));
                            msgBean.at = String.valueOf(json.optInt("at"));
                            msgBean.post = String.valueOf(json.optInt("post"));
                            msgBean.zan = String.valueOf(json.optInt("zan"));
                            msgBean.system = String.valueOf(json.optInt("system"));

                        }

                        postEvent(ResponseEvent.TYPE_SERVER_GET_MSG_TOTAL_COUNT,msgBean,Server.Code.SUCCESS,"");
                    }

                    @Override
                    public void onFail(int errorCode, String errorMsg) {
                        postEvent(ResponseEvent.TYPE_SERVER_GET_MSG_TOTAL_COUNT,null,errorCode,errorMsg);
                    }
                });
            }

            @Override
            public void onError(int errorCode, String errorMsg) {
                postEvent(ResponseEvent.TYPE_SERVER_GET_MSG_TOTAL_COUNT,null,errorCode,errorMsg);
            }
        });

    }

    @Override
    public void getMsgListByType(Context context, HashMap<String, String> params) {
        //m=user&c=userMsg&a=notice
        if(params==null){
            params = new HashMap<String, String>();
        }
        params.put("m","user");
        params.put("c","userMsg");
        params.put("a","notice");

        get(context, formatMapParamToString(context, params), new VolleyManager.ServerResponse<String>() {
            @Override
            public void onSuccess(String result) {
                resolveJson(result, new OnRepSuccessListener() {
                    @Override
                    public void onJsonArrayWidthPageSuccess(JSONArray jsonArray, String currentPage, String total) {
                        super.onJsonArrayWidthPageSuccess(jsonArray, currentPage, total);
                        MsgBean.MsgItemBean msgItemBean = new MsgBean.MsgItemBean();
                        if(jsonArray!=null){
                            int size = jsonArray.length();
                            ArrayList<MsgBean.MsgItem> msgList = new ArrayList<MsgBean.MsgItem>();
                            for(int i=0;i<size;i++){
                                JSONObject msgItemJson = jsonArray.optJSONObject(i);
                                if(msgItemJson!=null){
                                    MsgBean.MsgItem msgItem = new MsgBean.MsgItem();
                                    msgItem.author = msgItemJson.optString("author");
                                    msgItem.authoravatar = msgItemJson.optString("authoravatar");
                                    msgItem.authorid = msgItemJson.optString("authorid");
                                    msgItem.category = msgItemJson.optString("category");
                                    msgItem.dateline = msgItemJson.optString("dateline");
                                    msgItem.fromId = msgItemJson.optString("from_id");
                                    msgItem.fromIdType = msgItemJson.optString("from_idtype");
                                    msgItem.fromNum = msgItemJson.optString("from_num");
                                    msgItem.id= msgItemJson.optString("id");
                                    msgItem.news = msgItemJson.optString("news");
                                    msgItem.note = msgItemJson.optString("note");
                                    msgItem.page = msgItemJson.optString("page");
                                    msgItem.pid = msgItemJson.optString("pid");
                                    msgItem.tid = msgItemJson.optString("tid");
                                    msgItem.type = msgItemJson.optString("type");
                                    msgItem.uid = msgItemJson.optString("uid");
                                    msgList.add(msgItem);
                                }
                            }
                            msgItemBean.itemList = msgList;
                        }
                        msgItemBean.currentPage = Page.resolve(currentPage);
                        msgItemBean.totalCount = Page.resolve(total);
                        postEvent(ResponseEvent.TYPE_SERVER_GET_MSG_LIST_BY_TYPE,msgItemBean, Server.Code.SUCCESS,"");
                    }

                    @Override
                    public void onFail(int errorCode, String errorMsg) {
                        postEvent(ResponseEvent.TYPE_SERVER_GET_MSG_LIST_BY_TYPE,null,errorCode,errorMsg);
                    }
                });
            }

            @Override
            public void onError(int errorCode, String errorMsg) {
                postEvent(ResponseEvent.TYPE_SERVER_GET_MSG_LIST_BY_TYPE,null,errorCode,errorMsg);
            }
        });


    }
}
