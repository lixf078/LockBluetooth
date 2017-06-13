package com.aiyiqi.lib.api.discuz.api;

import android.content.Context;

import java.util.HashMap;

/**
 * Created by hubing on 16/6/2.
 */
public interface IApiManager {

    public void fetchVerifyCode(Context context,HashMap<String,String> paramap);
}
