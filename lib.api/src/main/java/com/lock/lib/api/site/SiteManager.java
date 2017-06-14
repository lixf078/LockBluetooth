package com.lock.lib.api.site;

import android.content.Context;
import android.text.TextUtils;

import com.lock.lib.api.BaseManager;
import com.lock.lib.api.Server;
import com.lock.lib.api.VolleyManager;
import com.lock.lib.api.event.ResponseEvent;
import com.lock.lib.api.site.SiteBean.TrackMsg;
import com.lock.lib.common.constants.Constants;
import com.lock.lib.common.util.Logger;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by hubing on 16/3/17.
 */
public class SiteManager extends BaseManager implements ISiteManager {
    private static Object objClock = new Object();
    private static SiteManager mInstance;

    private SiteManager(Context context) {
        super(context);
    }

    public static SiteManager getInstance(Context context) {
        synchronized (objClock) {
            if (mInstance == null) {
                mInstance = new SiteManager(context);
            }
            return mInstance;
        }
    }

    @Override
    public void addBuildingSiteTrack(Context context, HashMap<String, String> map) {
        Logger.e(Constants.TAG, "addBuildingSiteTrack");

        HashMap<String, String> params = resolveAPIJsonToPost(context, map);

        post(context, formatBusinessUrl("/user/addBuildingSiteTrack"), params, new VolleyManager.ServerResponse<String>() {
            @Override
            public void onSuccess(String result) {

                try {
                    JSONObject jsonObject = new JSONObject(result);
                    JSONObject baseJson = jsonObject.getJSONObject("baseOutput");
                    String message = baseJson.optString("message");
                    if (baseJson != null) {
                        int code = baseJson.getInt("code");
                        if (isSuccess(code)) {
                            String info = jsonObject.optString("data");
                            Logger.e(Constants.TAG, "addBuildingSiteTrack info " + info);
                            postEvent(ResponseEvent.TYPE_SERVER_SITE_ADD_TRACK, info, Server.Code.SUCCESS, "");
                        } else {
                            postEvent(ResponseEvent.TYPE_SERVER_SITE_ADD_TRACK, null, code, message);
                        }
                    }

                } catch (Exception e) {
                    Logger.e(Constants.TAG, "register user error.", e);
                    postEvent(ResponseEvent.TYPE_SERVER_SITE_ADD_TRACK, null, -1, "数据异常");
                }

            }

            @Override
            public void onError(int errorCode, String errorMsg) {
                postEvent(ResponseEvent.TYPE_SERVER_SITE_ADD_TRACK, null, errorCode, errorMsg);
            }
        });
    }

    @Override
    public void addBuildingSiteAppraise(Context context, HashMap<String, String> map) {
        Logger.e(Constants.TAG, "addBuildingSiteAppraise");

        HashMap<String, String> params = resolveAPIJsonToPost(context, map);

        post(context, formatBusinessUrl("/housekeep/addBuildingSitComment"), params, new VolleyManager.ServerResponse<String>() {
            @Override
            public void onSuccess(String result) {

                try {
                    JSONObject jsonObject = new JSONObject(result);
                    JSONObject baseJson = jsonObject.getJSONObject("baseOutput");
                    String message = baseJson.optString("message");
                    if (baseJson != null) {
                        int code = baseJson.getInt("code");
                        if (isSuccess(code)) {
                            String info = jsonObject.optString("data");
                            Logger.e(Constants.TAG, "addBuildingSiteTrack info " + info);
                            postEvent(ResponseEvent.TYPE_SERVER_SITE_ADD_APPRAISE, info, Server.Code.SUCCESS, "");
                        } else {
                            postEvent(ResponseEvent.TYPE_SERVER_SITE_ADD_APPRAISE, null, code, message);
                        }
                    }

                } catch (Exception e) {
                    Logger.e(Constants.TAG, "register user error.", e);
                    postEvent(ResponseEvent.TYPE_SERVER_SITE_ADD_APPRAISE, null, -1, "数据异常");
                }

            }

            @Override
            public void onError(int errorCode, String errorMsg) {
                postEvent(ResponseEvent.TYPE_SERVER_SITE_ADD_APPRAISE, null, errorCode, errorMsg);
            }
        });
    }

    @Override
    public void listBuildingSiteAppraise(Context context, HashMap<String, String> map) {
        Logger.e(Constants.TAG, "addBuildingSiteTrack");

        final HashMap<String, String> params = resolveAPIJsonToPost(context, map);

        post(context, formatBusinessUrl("/housekeep/listBuildingSiteComment"), params, new VolleyManager.ServerResponse<String>() {
            @Override
            public void onSuccess(String result) {

                try {
                    JSONObject jsonObject = new JSONObject(result);
                    JSONObject baseJson = jsonObject.getJSONObject("baseOutput");
                    String message = baseJson.optString("message");
                    if (baseJson != null) {
                        int code = baseJson.getInt("code");
                        if (isSuccess(code)) {
                            ArrayList<SiteBean.SiteAppraiseMsg> list = new ArrayList<SiteBean.SiteAppraiseMsg>();
                            JSONArray jsonArray = jsonObject.optJSONArray("data");
                            if (jsonArray != null){
                                int size = jsonArray.length();
                                if (size > 0){
                                    for (int i = 0; i < size; i++){
                                        JSONObject appraiseJson = jsonArray.optJSONObject(i);
                                        SiteBean.SiteAppraiseMsg appraiseMsg = new SiteBean.SiteAppraiseMsg();
                                        appraiseMsg.appraiseId = appraiseJson.optString("commentId");
                                        appraiseMsg.buildingId = appraiseJson.optString("buildingId");
                                        appraiseMsg.decorationScore = appraiseJson.optString("rating");
                                        appraiseMsg.content = appraiseJson.optString("message");
                                        appraiseMsg.appraiseId = appraiseJson.optString("commentId");
                                        appraiseMsg.publishTime = appraiseJson.optLong("createTime");

                                        SiteBean.State state = new SiteBean.State();
                                        state.id = appraiseJson.optInt("progressId");
                                        state.name = appraiseJson.optString("progressName");
                                        appraiseMsg.state = state;
                                        list.add(appraiseMsg);
                                    }
                                }
                            }
                            JSONObject pageJson = jsonObject.optJSONObject("pageInfo");
                            String hasMore = "0";
                            if (pageJson != null){
                                int pageNo = pageJson.optInt("pageNo");
                                int pageSize = pageJson.optInt("pageSize");
                                int pageTotalNum = pageJson.optInt("pageTotalNum");
                                if ((++pageNo) * pageSize >= pageTotalNum){
                                    hasMore = "0";
                                }else{
                                    hasMore = "1";
                                }
                            }
                            postEvent(ResponseEvent.TYPE_SERVER_SITE_LIST_APPRAISE, list, Server.Code.SUCCESS, params.get("type") +  "," + hasMore);
                        } else {
                            postEvent(ResponseEvent.TYPE_SERVER_SITE_LIST_APPRAISE, null, code, message);
                        }
                    }

                } catch (Exception e) {
                    Logger.e(Constants.TAG, "register user error.", e);
                    postEvent(ResponseEvent.TYPE_SERVER_SITE_LIST_APPRAISE, null, -1, "数据异常");
                }

            }

            @Override
            public void onError(int errorCode, String errorMsg) {
                postEvent(ResponseEvent.TYPE_SERVER_SITE_LIST_APPRAISE, null, errorCode, errorMsg);
            }
        });
    }

    @Override
    public void deleteBuildingSiteTrack(Context context, HashMap<String, String> map) {
        Logger.e(Constants.TAG, "deleteBuildingSiteTrack");

        HashMap<String, String> params = resolveAPIJsonToPost(context, map);

        post(context, formatBusinessUrl("housekeep/deleteBuildingSiteTrack"), params, new VolleyManager.ServerResponse<String>() {
            @Override
            public void onSuccess(String result) {

                try {
                    JSONObject jsonObject = new JSONObject(result);
                    JSONObject baseJson = jsonObject.getJSONObject("baseOutput");
                    String message = baseJson.optString("message");
                    if (baseJson != null) {
                        int code = baseJson.getInt("code");
                        if (isSuccess(code)) {
                            String info = jsonObject.optString("data");

                            postEvent(ResponseEvent.TYPE_SERVER_SITE_TRACK_DELETEED, info, Server.Code.SUCCESS, "");
                        } else {
                            postEvent(ResponseEvent.TYPE_SERVER_SITE_TRACK_DELETEED, null, code, message);
                        }
                    }

                } catch (Exception e) {
                    Logger.e(Constants.TAG, "register user error.", e);
                    postEvent(ResponseEvent.TYPE_SERVER_SITE_TRACK_DELETEED, null, -1, "数据异常");
                }

            }

            @Override
            public void onError(int errorCode, String errorMsg) {
                postEvent(ResponseEvent.TYPE_SERVER_SITE_TRACK_DELETEED, null, errorCode, errorMsg);
            }
        });
    }

    @Override
    public void addSiteRemark(Context context, HashMap<String, String> map) {
        Logger.e(Constants.TAG, "addBuildingSiteTrack");

        HashMap<String, String> params = resolveAPIJsonToPost(context, map);

        post(context, formatBusinessUrl("/user/addBuildingReply"), params, new VolleyManager.ServerResponse<String>() {
            @Override
            public void onSuccess(String result) {

                try {
                    JSONObject jsonObject = new JSONObject(result);
                    JSONObject baseJson = jsonObject.getJSONObject("baseOutput");
                    String message = baseJson.optString("message");
                    if (baseJson != null) {
                        int code = baseJson.getInt("code");
                        if (isSuccess(code)) {
                            String info = jsonObject.optString("data");
                            postEvent(ResponseEvent.TYPE_SERVER_SITE_ADD_REMARK, info, Server.Code.SUCCESS, "");
                        } else {
                            postEvent(ResponseEvent.TYPE_SERVER_SITE_ADD_REMARK, null, code, message);
                        }
                    }

                } catch (Exception e) {
                    Logger.e(Constants.TAG, "register user error.", e);
                    postEvent(ResponseEvent.TYPE_SERVER_SITE_ADD_REMARK, null, -1, "数据异常");
                }

            }

            @Override
            public void onError(int errorCode, String errorMsg) {
                postEvent(ResponseEvent.TYPE_SERVER_SITE_ADD_REMARK, null, errorCode, errorMsg);
            }
        });
    }

    @Override
    public void listLatestLiveBuildingSites(Context context, HashMap<String, String> map) {
        Logger.e(Constants.TAG, "listLatestLiveBuildingSites");

        HashMap<String, String> params = resolveAPIJsonToPost(context, map);

        post(context, formatBusinessUrl("/housekeep/listLatestLiveBuildingSites"), params, new VolleyManager.ServerResponse<String>() {
            @Override
            public void onSuccess(String result) {

                try {
                    JSONObject jsonObject = new JSONObject(result);
                    JSONObject baseJson = jsonObject.getJSONObject("baseOutput");
                    String message = baseJson.optString("message");
                    if (baseJson != null) {
                        int code = baseJson.getInt("code");
                        if (isSuccess(code)) {
                            ArrayList<SiteBean.Item> items = new ArrayList<>();
                            JSONArray jsonArray = jsonObject.optJSONArray("data");
                            if (jsonArray != null){
                                for (int i = 0, j = jsonArray.length(); i<j; i++){
                                    JSONObject siteItemJson = jsonArray.optJSONObject(i);
                                    SiteBean.Item item = new SiteBean.Item();
                                    JSONObject buildingSite = siteItemJson.optJSONObject("buildingSite");
                                    item.siteId = buildingSite.optString("buildingId");
                                    item.orderId = buildingSite.optString("orderId");
                                    item.icon = siteItemJson.optString("imageUrl");
                                    JSONObject orderHouse = siteItemJson.optJSONObject("orderHouse");
                                    item.title = orderHouse.optString("community");
                                    House house = new House();
                                    house.type = orderHouse.optInt("newHouse");
                                    house.area = orderHouse.optString("area");
                                    House.Layout layout = new House.Layout();
                                    layout.name = orderHouse.optString("layout");
                                    house.layout = layout;
                                    item.decorationType = orderHouse.optString("source");
                                    item.house = house;
                                    items.add(item);
                                }
                            }
                            postEvent(ResponseEvent.TYPE_SERVER_SITE_GET_LATEST_LIST, items, Server.Code.SUCCESS, "");
                        } else {
                            postEvent(ResponseEvent.TYPE_SERVER_SITE_GET_LATEST_LIST, null, code, message);
                        }
                    }

                } catch (Exception e) {
                    Logger.e(Constants.TAG, "register user error.", e);
                    postEvent(ResponseEvent.TYPE_SERVER_SITE_GET_LATEST_LIST, null, -1, "数据异常");
                }

            }

            @Override
            public void onError(int errorCode, String errorMsg) {
                postEvent(ResponseEvent.TYPE_SERVER_SITE_GET_LATEST_LIST, null, errorCode, errorMsg);
            }
        });
    }

    @Override
    public void getLiveBuildingSite(Context context, HashMap<String, String> map) {
        Logger.e(Constants.TAG, "getLiveBuildingSite");

        HashMap<String, String> params = resolveAPIJsonToPost(context, map);

        post(context, formatBusinessUrl("/housekeep/getLiveBuildingSite"), params, new VolleyManager.ServerResponse<String>() {
            @Override
            public void onSuccess(String result) {

                try {
                    JSONObject jsonObject = new JSONObject(result);
                    JSONObject baseJson = jsonObject.getJSONObject("baseOutput");
                    String message = baseJson.optString("message");
                    if (baseJson != null) {
                        int code = baseJson.getInt("code");
                        if (isSuccess(code)) {
                            SiteBean.Detail detail = null;
                            JSONObject jsonDetail = jsonObject.optJSONObject("data");
                            if (jsonDetail != null){
                                detail = new SiteBean.Detail();
                                SiteBean.Item item = new SiteBean.Item();
                                JSONObject buildingSite = jsonDetail.optJSONObject("buildingSite");
                                item.siteId = buildingSite.optString("buildingId");
                                item.orderId = buildingSite.optString("orderId");
                                item.icon = jsonDetail.optString("imageUrl");
                                JSONObject orderHouse = jsonDetail.optJSONObject("orderHouse");
                                item.title = orderHouse.optString("community");
                                JSONObject eventJO = jsonDetail.optJSONObject("eventAlertView");
                                if (eventJO != null)
                                    item.progressName = eventJO.optString("processingName");
                                if (TextUtils.isEmpty(item.progressName))
                                    item.progressName = "暂无";
                                House house = new House();
                                house.type = orderHouse.optInt("newHouse");
                                house.area = orderHouse.optString("area");
                                House.Layout layout = new House.Layout();
                                layout.name = orderHouse.optString("layout");
                                house.layout = layout;
                                item.decorationType = orderHouse.optString("source");
                                item.house = house;

                                JSONArray stateArray = jsonDetail.optJSONArray("progress");
                                if (stateArray != null && stateArray.length() != 0){
                                    ArrayList<SiteBean.State> states = new ArrayList<SiteBean.State>();
                                    for (int i = 0, j = stateArray.length(); i < j; i++){
                                        JSONObject stateJson = stateArray.optJSONObject(i);
                                        SiteBean.State state = new SiteBean.State();
                                        state.id = stateJson.optInt("progressId");
                                        state.level = stateJson.optInt("progressStatus");
                                        state.name = stateJson.optString("progressName");
                                        if (state.level == 1){
                                            item.state = state;
                                        }
                                        states.add(state);
                                    }
                                    item.states = states;
                                }
                                detail.item = item;
                                detail.latestTrackProgressId = jsonDetail.optInt("latestTrackProgressId");


                                ArrayList<TrackMsg> msgArrayList = new ArrayList<TrackMsg>();
                                JSONArray tracksJson = jsonDetail.optJSONArray("tracks");
                                if(tracksJson!=null) {
                                    int size = tracksJson.length();
                                    for (int i = 0; i < size; i++) {
                                        JSONObject itemJson = tracksJson.optJSONObject(i);
                                        TrackMsg msg = new TrackMsg();
                                        msg.content = itemJson.optString("message");
                                        msg.publishTime = itemJson.optLong("createTime");
                                        msg.trackId = String.valueOf(itemJson.optLong("trackId"));
                                        msg.buildingId = String.valueOf(itemJson.optLong("buildingId"));
                                        String imagesJson = itemJson.optString("imgSrc");
                                        if (!TextUtils.isEmpty(imagesJson)) {
                                            ArrayList<String> trackImages = new ArrayList<String>();
                                            String[] imgs = imagesJson.split(",");
                                            int atSize = imgs.length;
                                            for (int j = 0; j < atSize; j++) {
                                                trackImages.add(imgs[j]);
                                            }
                                            msg.trackImgs = trackImages;
                                        }
                                        JSONArray replyJson = itemJson.optJSONArray("replyList");
                                        if (replyJson != null) {
                                            ArrayList<SiteBean.Comment> trackComments = new ArrayList<SiteBean.Comment>();
                                            for (int k = 0, t = replyJson.length(); k < t; k++) {
                                                JSONObject replyJsonObj = replyJson.optJSONObject(k);
                                                SiteBean.Comment comment = new SiteBean.Comment();
                                                comment.id = replyJsonObj.optString("replyId");
                                                comment.replyId = replyJsonObj.optString("trackId");
                                                comment.content = replyJsonObj.optString("message");
                                                comment.publishTime = replyJsonObj.optLong("createTime");
                                                trackComments.add(comment);
                                            }
                                            msg.trackComments = trackComments;
                                        }
                                        msgArrayList.add(msg);
                                        detail.trackMsgs = msgArrayList;
                                    }
                                }
                            }

                            postEvent(ResponseEvent.TYPE_SERVER_SITE_GET_LIVE_BUILDING, detail, Server.Code.SUCCESS, "");
                        } else {
                            postEvent(ResponseEvent.TYPE_SERVER_SITE_GET_LIVE_BUILDING, null, code, message);
                        }
                    }

                } catch (Exception e) {
                    Logger.e(Constants.TAG, "register user error.", e);
                    postEvent(ResponseEvent.TYPE_SERVER_SITE_GET_LIVE_BUILDING, null, -1, "数据异常");
                }
            }

            @Override
            public void onError(int errorCode, String errorMsg) {
                postEvent(ResponseEvent.TYPE_SERVER_SITE_GET_LIVE_BUILDING, null, errorCode, errorMsg);
            }
        });
    }

    @Override
    public void listBuildingSiteVendors(Context context, HashMap<String, String> map) {
        Logger.e(Constants.TAG, "listBuildingSiteVendors");

        final HashMap<String, String> params = resolveAPIJsonToPost(context, map);

        post(context, formatBusinessUrl("/housekeep/listBuildingSiteVendors"), params, new VolleyManager.ServerResponse<String>() {
            @Override
            public void onSuccess(String result) {

                try {
                    JSONObject jsonObject = new JSONObject(result);
                    JSONObject baseJson = jsonObject.getJSONObject("baseOutput");
                    String message = baseJson.optString("message");
                    if (baseJson != null) {
                        int code = baseJson.getInt("code");
                    }

                } catch (Exception e) {
                    Logger.e(Constants.TAG, "register user error.", e);
                    postEvent(ResponseEvent.TYPE_SERVER_SITE_BUILDING_VENDORS, params.get("type"), -1, "数据异常");
                }
            }

            @Override
            public void onError(int errorCode, String errorMsg) {
                postEvent(ResponseEvent.TYPE_SERVER_SITE_BUILDING_VENDORS, params.get("type"), errorCode, errorMsg);
            }
        });
    }

    @Override
    public void listNearbyBuildingSites(Context context, HashMap<String, String> map) {
        Logger.e(Constants.TAG, "listNearbyBuildingSites");

        HashMap<String, String> params = resolveAPIJsonToPost(context, map);

        post(context, formatBusinessUrl("/housekeep/listNearbyBuildingSites"), params, new VolleyManager.ServerResponse<String>() {
            @Override
            public void onSuccess(String result) {

                try {
                    JSONObject jsonObject = new JSONObject(result);
                    JSONObject baseJson = jsonObject.getJSONObject("baseOutput");
                    String message = baseJson.optString("message");
                    if (baseJson != null) {
                        int code = baseJson.getInt("code");
                        if (isSuccess(code)) {
                            ArrayList<SiteBean.Item> items = new ArrayList<>();
                            JSONArray jsonArray = jsonObject.optJSONArray("data");
                            if (jsonArray != null){
                                for (int i = 0, j = jsonArray.length(); i<j; i++){
                                    JSONObject siteItemJson = jsonArray.optJSONObject(i);
                                    SiteBean.Item item = new SiteBean.Item();
                                    JSONObject buildingSite = siteItemJson.optJSONObject("buildingSite");
                                    item.siteId = buildingSite.optString("buildingId");
                                    item.orderId = buildingSite.optString("orderId");
                                    item.icon = siteItemJson.optString("imageUrl");
                                    JSONObject orderHouse = siteItemJson.optJSONObject("orderHouse");
                                    if (orderHouse != null){
                                        item.title = orderHouse.optString("community");
                                        House house = new House();
                                        house.type = orderHouse.optInt("newHouse");
                                        house.area = orderHouse.optString("area");
                                        House.Layout layout = new House.Layout();
                                        layout.name = orderHouse.optString("layout");
                                        house.layout = layout;
                                        item.decorationType = orderHouse.optString("source");

                                        House.Address address = new House.Address();
                                        address.title = orderHouse.optString("address");
                                        address.specialAddress = orderHouse.optString("community");
                                        address.latitude = orderHouse.optDouble("lat");
                                        address.longitude = orderHouse.optDouble("lng");
                                        house.address = address;
                                        item.house = house;
                                    }else{
                                        Logger.e(Constants.TAG, "siteItemJson " + siteItemJson.toString());
                                    }

                                    items.add(item);
                                }
                            }
                            postEvent(ResponseEvent.TYPE_SERVER_SITE_NEARBY_BUILDING, items, Server.Code.SUCCESS, "");
                        } else {
                            postEvent(ResponseEvent.TYPE_SERVER_SITE_NEARBY_BUILDING, null, code, message);
                        }
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    Logger.e(Constants.TAG, "register user error.", e);
                    postEvent(ResponseEvent.TYPE_SERVER_SITE_NEARBY_BUILDING, null, -1, "数据异常");
                }

            }

            @Override
            public void onError(int errorCode, String errorMsg) {
                postEvent(ResponseEvent.TYPE_SERVER_SITE_NEARBY_BUILDING, null, errorCode, errorMsg);
            }
        });
    }

    @Override
    public void listBuildingSiteTrackById(Context context, HashMap<String, String> map) {
        HashMap<String, String> params = resolveAPIJsonToPost(context, map);

        post(context, formatBusinessUrl("/housekeep/listBuildingSiteTrackByProgress"), params, new VolleyManager.ServerResponse<String>() {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    JSONObject baseJson = jsonObject.getJSONObject("baseOutput");
                    if (baseJson != null) {
                        String message = baseJson.optString("message");
                        int code = baseJson.getInt("code");
                        if (isSuccess(code)) {
                            JSONArray jsonArray = jsonObject.getJSONArray("data");
                            ArrayList<TrackMsg> msgArrayList = new ArrayList<TrackMsg>();
                            if (jsonArray != null) {
                                int size = jsonArray.length();
                                for (int i = 0; i < size; i++) {
                                    try {
                                        JSONObject itemJson = jsonArray.getJSONObject(i);
                                        TrackMsg msg = new TrackMsg();
                                        msg.content = itemJson.getString("message");
                                        msg.publishTime = itemJson.optLong("createTime");
                                        msg.trackId = String.valueOf(itemJson.optLong("trackId"));
                                        msg.buildingId = String.valueOf(itemJson.optLong("buildingId"));
                                        String imagesJson = itemJson.optString("imgSrc");
                                        if (!TextUtils.isEmpty(imagesJson)) {
                                            ArrayList<String> trackImages = new ArrayList<String>();
                                            String[] imgs = imagesJson.split(",");
                                            int atSize = imgs.length;
                                            for (int j = 0; j < atSize; j++) {
                                                trackImages.add(imgs[j]);
                                            }
                                            msg.trackImgs = trackImages;
                                        }
                                        JSONArray replyJson = itemJson.optJSONArray("replyList");
                                        if (replyJson != null) {
                                            ArrayList<SiteBean.Comment> trackComments = new ArrayList<SiteBean.Comment>();
                                            for (int k = 0, t = replyJson.length(); k < t; k++) {
                                                JSONObject replyJsonObj = replyJson.optJSONObject(k);
                                                SiteBean.Comment comment = new SiteBean.Comment();
                                                comment.id = replyJsonObj.optString("replyId");
                                                comment.replyId = replyJsonObj.optString("trackId");
                                                comment.content = replyJsonObj.optString("message");
                                                comment.publishTime = replyJsonObj.optLong("createTime");
                                            }
                                            msg.trackComments = trackComments;
                                        }

                                        JSONObject shareJson = itemJson.optJSONObject("weiXinShare");
                                        if(shareJson!=null){
                                            SiteBean.Share share = new SiteBean.Share();
                                            share.content = shareJson.optString("content");
                                            share.linkUrl = shareJson.optString("linkUrl");
                                            share.news = shareJson.optString("news");
                                            share.title = shareJson.optString("title");
                                            msg.share = share;
                                        }

                                        msgArrayList.add(msg);
                                    } catch (JSONException e) {
                                        Logger.e(Constants.TAG, "", e);
                                    }
                                }
                            }
                            postEvent(ResponseEvent.TYPE_SERVER_SITE_TRACK_LIST, msgArrayList, Server.Code.SUCCESS, "");

                        } else {
                            postEvent(ResponseEvent.TYPE_SERVER_SITE_TRACK_LIST, null, Server.Code.SUCCESS, "");

                        }
                    }

                } catch (Exception e) {
                    postEvent(ResponseEvent.TYPE_SERVER_SITE_TRACK_LIST, null, Server.Code.SUCCESS, "");

                    Logger.e(Constants.TAG, "register user error.", e);
                }
            }

            @Override
            public void onError(int errorCode, String errorMsg) {
                postEvent(ResponseEvent.TYPE_SERVER_SITE_TRACK_LIST, null, Server.Code.FAIL, errorMsg);

            }
        });
    }

    @Override
    public void listBannerList(Context context, HashMap<String, String> map) {

        map.put("action", "integratedpackage");
        String url = resolveOrderAPIJsonToGet(context, map);
        get(context, formatOrderUrl("/AppManagerApi.php?version=1" + url), new VolleyManager.ServerResponse<String>() {
            @Override
            public void onSuccess(String result) {

                try {
                    JSONObject jsonObject = new JSONObject(result);
                    int errorCode = jsonObject.optInt("error");

                    if (jsonObject != null) {
                        String message = jsonObject.optString("message");
                        if (errorCode == 0) {
                            String jsonString = jsonObject.optString("data");

                            postEvent(ResponseEvent.TYPE_SERVER_SITE_GET_BANNER_LIST, jsonString, Server.Code.SUCCESS, "");

                        } else {
                            postEvent(ResponseEvent.TYPE_SERVER_SITE_GET_BANNER_LIST, null, errorCode, message);
                        }
                    } else {
                        postEvent(ResponseEvent.TYPE_SERVER_SITE_GET_BANNER_LIST, null, errorCode, "数据异常");
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onError(int errorCode, String errorMsg) {
                postEvent(ResponseEvent.TYPE_SERVER_SITE_GET_BANNER_LIST, null, errorCode, errorMsg);
            }
        });
    }

    @Override
    public void ossToken(Context context, HashMap<String, String> map) {
        HashMap<String, String> params = resolveAPIJsonToPost(context, map);
        volleyManager.post(context, formatBusinessUrl("/oss/token"), params, new VolleyManager.ServerResponse<String>() {
            @Override
            public void onSuccess(String result) {

                try {
                    JSONObject jsonObject = new JSONObject(result);
                    JSONObject baseJson = jsonObject.getJSONObject("baseOutput");
                    String message = baseJson.optString("message");
                    if (baseJson != null) {
                        int code = baseJson.getInt("code");
                        if (isSuccess(code)) {
                            JSONObject ossJson = jsonObject.optJSONObject("data");

                            OssToken ossToken = new OssToken();
                            if (ossJson != null) {
                                ossToken.accessKeyId = ossJson.optString("accessKeyId");
                                ossToken.accessKeySecret = ossJson.optString("accessKeySecret");
                                ossToken.expiration = ossJson.optString("expiration");
                                ossToken.securityToken = ossJson.optString("securityToken");
                            }
                            postEvent(ResponseEvent.TYPE_SERVER_SITE_FETCH_OSSTOKEN, ossToken, Server.Code.SUCCESS, "");
                        } else {
                            postEvent(ResponseEvent.TYPE_SERVER_SITE_FETCH_OSSTOKEN, null, code, message);
                        }
                    }

                } catch (Exception e) {
                    Logger.e(Constants.TAG, "register user error.", e);
                    postEvent(ResponseEvent.TYPE_SERVER_SITE_FETCH_OSSTOKEN, null, -1, "数据异常");
                }
            }

            @Override
            public void onError(int errorCode, String errorMsg) {
                postEvent(ResponseEvent.TYPE_SERVER_SITE_FETCH_OSSTOKEN, null, errorCode, errorMsg);
            }
        });
    }
}
