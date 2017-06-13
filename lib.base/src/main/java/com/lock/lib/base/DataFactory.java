package com.lock.lib.base;

import android.content.Context;

import com.lock.lib.base.config.ConfigManager;
import com.lock.lib.base.config.IConfigManager;
import com.lock.lib.base.discuz.feed.IFeedManager;
import com.lock.lib.base.discuz.sku.ISkuManager;
import com.lock.lib.base.discuz.sku.SkuManager;
import com.lock.lib.base.discuz.user.IUserManager;
import com.lock.lib.base.discuz.user.UserManager;

import java.util.HashMap;

/**
 * Created by hubing on 16/4/12.
 */
public class DataFactory implements IFeedManager,IUserManager,IConfigManager {

    private static Object clockObj = new Object();
    private static DataFactory mInstance;
    private UserManager userManager;
    private SkuManager skuManager;
    private ConfigManager mConfigManager;


    private DataFactory(Context context){
        userManager = UserManager.getInstance(context);
        skuManager = SkuManager.getInstance(context);
        mConfigManager = ConfigManager.getInstance(context);
    };
    public static DataFactory getInstance(Context context){
        synchronized (clockObj){
            if(mInstance == null){
                mInstance = new DataFactory(context);
            }
            return mInstance;
        }
    }

    @Override
    public void getFeedDigestList(Context context, HashMap<String, String> params) {
    }

    @Override
    public void getFeedDigestListByTag(Context context, HashMap<String, String> params) {
    }

    @Override
    public void getFeedLatestList(Context context, HashMap<String, String> params) {
    }

    @Override
    public void getFeedBlock(Context context, HashMap<String, String> params) {
    }

    @Override
    public void getFeedSpecialBlock(Context context, HashMap<String, String> params) {
    }

    @Override
    public void getFeedSpecialTag(Context context, HashMap<String, String> params) {
    }

    @Override
    public void getFeedListByTag(Context context, HashMap<String, String> params) {
    }

    @Override
    public void getFeedListByBlock(Context context, HashMap<String, String> params) {
    }

    @Override
    public void getFeedDetail(Context context, HashMap<String, String> params) {
    }

    @Override
    public void getFeedReplyDetail(Context context, HashMap<String, String> params) {
    }

    @Override
    public void postFeed(Context context, HashMap<String, String> params) {
    }

    @Override
    public void replyFeed(Context context, HashMap<String, String> params) {
    }

    @Override
    public void favoriteFeed(Context context, HashMap<String, String> params) {
    }

    @Override
    public void zanFeed(Context context, HashMap<String, String> params) {
    }

    @Override
    public void uploadImg(Context context, HashMap<String, String> params) {
    }

    @Override
    public void delImg(Context context, HashMap<String, String> params) {
    }

    @Override
    public void searchFeed(Context context, HashMap<String, String> params) {
    }

    @Override
    public void searchUser(Context context, HashMap<String, String> params) {
        userManager.searchUser(context, params);
    }

    @Override
    public void fetchDiscuzVerifyCode(Context context, HashMap<String, String> params) {
        userManager.fetchDiscuzVerifyCode(context,params);
    }

    @Override
    public void getUserPostFeedList(Context context, HashMap<String, String> params) {
    }

    @Override
    public void getUserFavoriteFeedList(Context context, HashMap<String, String> params) {
    }

    @Override
    public void getZanUsersFromFeed(Context context, HashMap<String, String> params) {
    }

    @Override
    public void getComplainTag(Context context, HashMap<String, String> params) {
    }

    @Override
    public void getDiaryTag(Context context, HashMap<String, String> params) {
    }

    @Override
    public void getOrderTag(Context context, HashMap<String, String> params) {
    }

    @Override
    public void getHomeFeedList(Context context, HashMap<String, String> params) {
    }

    @Override
    public void getHotTags(Context context, HashMap<String, String> params) {
    }

    @Override
    public void getSearchTags(Context context, HashMap<String, String> params) {
    }

    @Override
    public void getComplainOrPraiseTags(Context context, HashMap<String, String> params) {
    }

    @Override
    public void getActivityFeedList(Context context, HashMap<String, String> params) {
    }

    @Override
    public void joinActivity(Context context, HashMap<String, String> params) {
    }

    @Override
    public void getAppraiseFeedListByTag(Context context, HashMap<String, String> params) {
    }

    @Override
    public void getUserInfo(Context context, HashMap<String, String> params) {
        userManager.getUserInfo(context, params);
    }

    @Override
    public void getUserFriends(Context context, HashMap<String, String> params) {
        userManager.getUserFriends(context, params);
    }

    @Override
    public void getUserFans(Context context, HashMap<String, String> params) {
        userManager.getUserFans(context, params);
    }

    @Override
    public void getUserFollowers(Context context, HashMap<String, String> params) {
        userManager.getUserFollowers(context, params);
    }

    @Override
    public void report(Context context, HashMap<String, String> params) {
        userManager.report(context, params);
    }

    @Override
    public void focuseUser(Context context, HashMap<String, String> params) {
        userManager.focuseUser(context, params);
    }

   /* @Override
    public void cancelFocuseFeed(Context context, HashMap<String, String> params) {
        userManager.cancelFocuseFeed(context, params);
    }*/

    @Override
    public void cancelFavoriteFeed(Context context, HashMap<String, String> params) {
        userManager.cancelFavoriteFeed(context, params);
    }

    public void uploadFile2Server(Context context, final HashMap<String,String> paramsMap) {
    }

    @Override
    public void getUserSiteFollowers(Context context, HashMap<String, String> map) {
        userManager.getUserSiteFollowers(context, map);
    }

    @Override
    public void joinYiQiAction(Context context, HashMap<String, String> params) {
        userManager.joinYiQiAction(context, params);
    }
    @Override
    public void updateUserDetail(Context context, HashMap<String, String> params) {
        userManager.updateUserDetail(context, params);
    }

    @Override
    public void getOwnerInfoAd(Context context, HashMap<String, String> params) {
        userManager.getOwnerInfoAd(context, params);
    }

    @Override
    public void fetchYQAvdConfig(Context context, HashMap<String, String> params) {
        mConfigManager.fetchYQAvdConfig(context,params);
    }
}
