package com.lock.lib.api.site;

import android.content.Context;

import java.util.HashMap;

/**
 * Created by hubing on 16/3/23.
 */
public interface ISiteManager {
    public void addBuildingSiteTrack(Context context, HashMap<String, String> map); // 加跟进
    public void addBuildingSiteAppraise(Context context, HashMap<String, String> map); // 加评价
    public void listBuildingSiteAppraise(Context context, HashMap<String, String> map); // 加评价
    public void deleteBuildingSiteTrack(Context context, HashMap<String, String> map); // 删除跟进
    public void addSiteRemark(Context context, HashMap<String, String> map); // 跟进加评论
    public void listLatestLiveBuildingSites(Context context, HashMap<String, String> map); // 装修直播首页
    public void getLiveBuildingSite(Context context, HashMap<String, String> map); // 获取工地详情
    public void listBuildingSiteVendors(Context context, HashMap<String, String> map); // 一起团队成员信息
    public void listNearbyBuildingSites(Context context, HashMap<String, String> map); // 附近工地
    public void listBannerList(Context context, HashMap<String, String> map); // 获取整装顶部banner
    public void listBuildingSiteTrackById(Context context, HashMap<String, String> map); // 获取工地跟进列表
    public void ossToken(Context context, HashMap<String, String> map); // 获取阿里云token
}
