package com.lock.lib.base.discuz.user;

import android.content.Context;

import java.util.HashMap;

/**
 * Created by hubing on 16/4/13.
 */
public interface IUserManager {
    public void getUserInfo(Context context, HashMap<String,String> params);
    public void getUserFriends(Context context, HashMap<String,String> params);

    public void getUserFans(Context context, HashMap<String,String> params); // 关注我的 人
    public void getUserFollowers(Context context, HashMap<String,String> params);// 我关注的人
    public void getUserSiteFollowers(Context context, HashMap<String,String> params);// 我关注的人

    public void report(Context context, HashMap<String,String> params);
    public void focuseUser(Context context, HashMap<String,String> params);
    //public void cancelFocuseFeed(Context context, HashMap<String,String> params);
    public void cancelFavoriteFeed(Context context, HashMap<String,String> params);

    public void searchUser(Context context, HashMap<String,String> params);
    public void fetchDiscuzVerifyCode(Context context, HashMap<String,String> params);

    public void joinYiQiAction(Context context, HashMap<String,String> params);
    public void updateUserDetail(Context context, HashMap<String,String> params);
    public void getOwnerInfoAd(Context context, HashMap<String,String> params);
}
