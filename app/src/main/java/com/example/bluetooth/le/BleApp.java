package com.example.bluetooth.le;

import android.app.Application;

/**
 * Created by admin on 2017/6/26.
 */

public class BleApp extends Application {

    static {
        System.loadLibrary("native_le-lib");
    }

    @Override
    public void onCreate() {
        super.onCreate();

    }
}
