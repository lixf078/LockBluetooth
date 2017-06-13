package com.aiyiqi.lib.api.discuz.msg;

import android.content.Context;

import java.util.HashMap;

/**
 * Created by hubing on 16/4/15.
 */
public interface IMsgManager {

    public void  getMsgTotalCount(Context context, HashMap<String, String> params);
    public void getMsgListByType(Context context, HashMap<String, String> params);

}
