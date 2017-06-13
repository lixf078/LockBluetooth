package com.lock.lib.base.discuz.feed;

import android.content.Context;

import java.util.HashMap;

/**
 * Created by hubing on 16/4/12.
 */
public interface IFeedManager {

    public void getFeedDigestList(Context context, HashMap<String,String> params);
    public void getFeedDigestListByTag(Context context, HashMap<String,String> params);
    public void getFeedLatestList(Context context, HashMap<String,String> params);
    public void getFeedBlock(Context context, HashMap<String,String> params);
    public void getFeedSpecialBlock(Context context, HashMap<String,String> params);
    public void getFeedSpecialTag(Context context, HashMap<String,String> params);
    public void getFeedListByTag(Context context, HashMap<String,String> params);
    public void getFeedListByBlock(Context context, HashMap<String,String> params);
    public void getFeedDetail(Context context, HashMap<String,String> params);
    public void getFeedReplyDetail(Context context, HashMap<String,String> params);

    public void postFeed(Context context, HashMap<String,String> params);
   /* public void postFeedForOrder(Context context, HashMap<String,String> params);
    public void postFeedForDiary(Context context, HashMap<String,String> params);
    public void postFeedForComplaints(Context context, HashMap<String,String> params);*/

    public void replyFeed(Context context, HashMap<String,String> params);
    public void favoriteFeed(Context context, HashMap<String,String> params);
    public void zanFeed(Context context, HashMap<String,String> params);
    public void uploadImg(Context context, HashMap<String,String> params);
    public void delImg(Context context, HashMap<String,String> params);
    public void searchFeed(Context context, HashMap<String,String> params);
    public void getUserPostFeedList(Context context, HashMap<String,String> params);
    public void getUserFavoriteFeedList(Context context, HashMap<String,String> params);
    public void getZanUsersFromFeed(Context context, HashMap<String,String> params);
    public void getComplainTag(Context context, HashMap<String,String> params);
    public void getDiaryTag(Context context, HashMap<String,String> params);
    public void getOrderTag(Context context, HashMap<String,String> params);

    public void getHomeFeedList(Context context, HashMap<String,String> params);

    public void getHotTags(Context context, HashMap<String,String> params);
    public void getSearchTags(Context context, HashMap<String,String> params);
    public void getComplainOrPraiseTags(Context context, HashMap<String,String> params);
    public void getActivityFeedList(Context context, HashMap<String, String> params);
    public void joinActivity(Context context,HashMap<String,String> params);
    public void getAppraiseFeedListByTag(Context context,HashMap<String,String> params);
}
