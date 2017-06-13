package com.lock.lib.base.discuz.sku;

import android.content.Context;

import java.util.HashMap;

/**
 * Created by hubing on 16/5/27.
 */
public interface ISkuManager {

    public void getCategoryList(Context context, HashMap<String, String> params);
}
