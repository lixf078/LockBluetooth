package com.lock.lib.api.site;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by hubing on 16/3/22.
 */
public class SiteBean {

    /*public static final class Item{
        public String orderId;
        public String siteId;
        public House house;
        public State state;
    }


    public final static class State{ //状态
        public int id;
        public int level;
        public String levelName;
        public String name;
    }

    public final static class TrackMsg{
        public Role publisher;//发布者
        public ArrayList<Role> atFriends;
        public String content;
        public long publishTime;
        public String trackId;
        public String buildingId;
        public ArrayList<String> trackImgs;
    }

    public final static class Comment{
        public Role publisher;//发布者
        public String content;

    }*/

    public final static class Detail{ //详情
        public Item item;
        public ArrayList<TrackMsg> trackMsgs;
        public int latestTrackProgressId;
    }


    public static final class Item{
        public String orderId;
        public String progressName;
        public String siteId;
        public House house;
        public State state;
        public ArrayList<State> states;
        public String icon; // 该工地icon相关图片
        public String title; // 工地名名称
        public String decorationType; // 装修套餐类型
    }

    public final static class State implements Serializable { //状态
        public int id;
        public int level;
        public String levelName;
        public String name;
    }

    public static class SiteMsg implements Serializable {
        public String buildingId;
        public String content;
        public long publishTime;
    }

    public final static class TrackMsg extends SiteMsg{
        public String trackId;
        public ArrayList<String> trackImgs;
        public ArrayList<Comment> trackComments;
        public Share share;
    }

    public static final class SiteAppraiseMsg extends SiteMsg{
        public String appraiseId;
        public String decorationScore;
        public State state;
    }

    public final static class Comment{
        public String id;
        public String replyId;
        public String content;
        public long publishTime;
    }

    public final static class Share{
        public String title;
        public String content;
        public String news;
        public String linkUrl;
    }

}
