package com.lock.lib.base.discuz;

import android.text.TextUtils;

/**
 * Created by hubing on 16/4/14.
 */
public class Page {
    public int currentPage;
    public int totalCount;
    public int pageSize;
    public boolean isLastPage;


    public void checkLast(){
        if(currentPage * pageSize >= totalCount){
            isLastPage = true;
        }
    }

    public static int resolve(String page){
        if(!TextUtils.isEmpty(page) && !"null".equals(page)){
            return Integer.valueOf(page);
        }
        return 0;
    }

}
