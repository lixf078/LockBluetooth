package com.lock.lib.base.discuz.msg;

import com.lock.lib.base.discuz.Page;

import java.util.ArrayList;

/**
 * Created by hubing on 16/4/15.
 */
public class MsgBean {
    public String total;
    public String at;
    public String post;
    public String zan;
    public String system;

    public static final class MsgItemBean extends Page{
        public ArrayList<MsgItem> itemList;
    }

    public static class MsgItem{
        public String author;
        public String authoravatar;
        public String authorid;
        public String category;
        public String dateline;
        public String fromId;
        public String fromIdType;
        public String fromNum;
        public String id;
        public String news;
        public String note;
        public String page;
        public String pid;
        public String tid;
        public String type;
        public String uid;

    }
}
