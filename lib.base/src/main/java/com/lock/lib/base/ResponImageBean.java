package com.lock.lib.base;

import android.graphics.Bitmap;

import java.util.HashMap;

/**
 * 响应图片对象
 * Created by houmengjie on 17/3/15.
 */

public class ResponImageBean {
    private Bitmap bitmap;
    private HashMap<String,String> map;

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public HashMap<String, String> getMap() {
        return map;
    }

    public void setMap(HashMap<String, String> map) {
        this.map = map;
    }
}
