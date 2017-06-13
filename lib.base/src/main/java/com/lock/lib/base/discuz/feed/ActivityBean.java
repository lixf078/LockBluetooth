package com.lock.lib.base.discuz.feed;

import java.util.ArrayList;

/**
 * Created by hubing on 16/6/1.
 */
public class ActivityBean {

    public String attachments;
    public int activityClose;
    public String sourceUrl;
    public ArrayList<ItemBean> itemBeans;


    public static class ItemBean{
        public String title;
        public String value;
    }
}
