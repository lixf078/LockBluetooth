package com.lock.lib.api;

import android.content.Context;

import com.lock.lib.api.site.ISiteManager;
import com.lock.lib.api.site.SiteManager;

import java.util.HashMap;

/**
 * Created by hubing on 16/4/12.
 */
public class DataFactory implements ISiteManager{

    private static Object clockObj = new Object();
    private static DataFactory mInstance;
    private SiteManager siteManager;
    private DataFactory(Context context){
        siteManager = SiteManager.getInstance(context);
    }
    public static DataFactory getInstance(Context context){
        synchronized (clockObj){
            if(mInstance == null){
                mInstance = new DataFactory(context);
            }
            return mInstance;
        }
    }

    @Override
    public void addBuildingSiteTrack(Context context, HashMap<String, String> map) {
        siteManager.addBuildingSiteTrack(context, map);
    }

    @Override
    public void addBuildingSiteAppraise(Context context, HashMap<String, String> map) {
        siteManager.addBuildingSiteAppraise(context, map);
    }
    @Override
    public void listBuildingSiteAppraise(Context context, HashMap<String, String> map) {
        siteManager.listBuildingSiteAppraise(context, map);
    }

    @Override
    public void deleteBuildingSiteTrack(Context context, HashMap<String, String> map) {
        siteManager.deleteBuildingSiteTrack(context, map);
    }

    @Override
    public void addSiteRemark(Context context, HashMap<String, String> map) {
        siteManager.addSiteRemark(context, map);
    }

    @Override
    public void listLatestLiveBuildingSites(Context context, HashMap<String, String> map) {
        siteManager.listLatestLiveBuildingSites(context, map);
    }

    @Override
    public void getLiveBuildingSite(Context context, HashMap<String, String> map) {
        siteManager.getLiveBuildingSite(context, map);
    }

    @Override
    public void listBuildingSiteVendors(Context context, HashMap<String, String> map) {
        siteManager.listBuildingSiteVendors(context, map);
    }

    @Override
    public void listNearbyBuildingSites(Context context, HashMap<String, String> map) {
        siteManager.listNearbyBuildingSites(context, map);
    }

    @Override
    public void listBuildingSiteTrackById(Context context, HashMap<String, String> map) {
        siteManager.listBuildingSiteTrackById(context, map);
    }

    @Override
    public void listBannerList(Context context, HashMap<String, String> map) {
        siteManager.listBannerList(context, map);
    }

    @Override
    public void ossToken(Context context, HashMap<String, String> map) {
        siteManager.ossToken(context, map);
    }
}
