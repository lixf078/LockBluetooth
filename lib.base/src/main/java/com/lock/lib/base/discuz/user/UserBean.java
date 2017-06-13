package com.lock.lib.base.discuz.user;

import com.lock.lib.base.discuz.Page;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by hubing on 16/4/14.
 */
public class UserBean extends Page {
    public String uid;//

    public ArrayList<User> users;

    public static class UserDetail extends User{
        public String credits;
        public String email;
        public ArrayList<Extcredits> extredits;
        public String friends;
        public String grouptitle;
        public String lastactivity;
        public String lastpost;
        public String lastvisit;
        public String oltime;
        public String posts;
        public String regdate;
        public String threads;
        public String uid;
        public String username;
        public String followee;//关注我的人数
        public String follower;//我关注的人数
        public String followstatus;//是否关注了这个人
        public String forumlist;//如果是他人的主页显示两条最新帖子
    }

    public static class Extcredits{
        public String title;
        public String value;
    }

    public static class SearchUserBean extends Page{
        public ArrayList<User> items;
    }

    public static class User implements Serializable{
        public static final int UNFOLLOWED = 0;
        public static final int FOLLOWED = 1;
        public static final int SELF_FOLLOWED = 2;
        public String uid;
        public String username;
        public String avtUrl;
        public int followstatus;
        public String typeName;
        public String typeId;
    }

    public static class YiQiGroupUser extends User{
        public String decorationCase;
        public String decorationScore;

    }
}
