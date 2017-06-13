package com.lock.lib.base.config;

import android.content.Context;

import java.util.HashMap;

/**
 * Created by hubing on 16/12/20.
 */

public interface IConfigManager {
    public void fetchYQAvdConfig(Context context, HashMap<String,String> params);
}
