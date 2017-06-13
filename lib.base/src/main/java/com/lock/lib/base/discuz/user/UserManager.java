package com.lock.lib.base.discuz.user;

import android.content.Context;
import android.text.TextUtils;

import com.lock.lib.base.BaseManager;
import com.lock.lib.base.Server;
import com.lock.lib.base.VolleyManager;
import com.lock.lib.base.discuz.Page;
import com.lock.lib.base.event.ResponseEvent;
import com.lock.lib.common.constants.Constants;
import com.lock.lib.common.util.Logger;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by hubing on 16/4/13.
 */
public class UserManager extends BaseManager implements IUserManager {

    private static Object clockObj = new Object();
    private static UserManager mInstance;
    private UserManager(Context context) {
        super(context);
    }
    public static UserManager getInstance(Context context){
        synchronized (clockObj){
            if(mInstance == null){
                mInstance = new UserManager(context);
            }
            return  mInstance;
        }
    }

    @Override
    public void getUserInfo(Context context, HashMap<String, String> params) {
        //m=user&c=userInfo&a=userInfo
        if(params==null){
            params = new HashMap<String, String>();
        }
        params.put("m","user");
        params.put("c","userInfo");
        params.put("a","userInfo");

        get(context, formatMapParamToString(context, params), new VolleyManager.ServerResponse<String>() {
            @Override
            public void onSuccess(String result) {
                resolveJson(result, new OnRepSuccessListener() {
                    @Override
                    public void onSuccess(JSONObject json) {
                        super.onSuccess(json);
                        UserBean.UserDetail user = new UserBean.UserDetail();
                        if(json!=null){
                            user.avtUrl = json.optString("avtUrl");
                            user.credits  =json.optString("credits");
                            user.email = json.optString("email");
                            user.followee = json.optString("followee");
                            user.follower =json.optString("follower");
                            user.followstatus = String.valueOf(json.optInt("followstatus"));
                            user.forumlist = json.optString("forumlist");
                            user.friends = json.optString("friends");
                            user.grouptitle = json.optString("grouptitle");
                            user.lastactivity = json.optString("lastactivity");
                            user.lastpost = json.optString("lastpost");
                            user.lastvisit = json.optString("lastvisit");
                            user.oltime = json.optString("oltime");
                            user.posts = json.optString("posts");
                            user.regdate = json.optString("regdate");
                            user.threads = json.optString("threads");
                            user.uid = json.optString("uid");
                            user.username = json.optString("username");
                            ArrayList<UserBean.Extcredits> extcreditses = new ArrayList<UserBean.Extcredits>();
                            JSONArray extcredJson = json.optJSONArray("extcredits");
                            if(extcredJson!=null){
                                int size = extcredJson.length();
                                for(int i=0;i<size;i++){
                                    JSONObject extcreditItemJson = extcredJson.optJSONObject(i);
                                    if(extcreditItemJson!=null){
                                        UserBean.Extcredits extcredits = new UserBean.Extcredits(); //
                                        extcredits.title = extcreditItemJson.optString("title");
                                        extcredits.value = extcreditItemJson.optString("value");
                                        extcreditses.add(extcredits);
                                    }
                                }
                                user.extredits = extcreditses;
                            }
                        }
                        postEvent(ResponseEvent.TYPE_SERVER_GET_USER_INFO,user, Server.Code.SUCCESS,"");
                    }

                    @Override
                    public void onFail(int errorCode, String errorMsg) {
                        postEvent(ResponseEvent.TYPE_SERVER_GET_USER_INFO,null, errorCode,errorMsg);
                    }
                });
            }

            @Override
            public void onError(int errorCode, String errorMsg) {
                postEvent(ResponseEvent.TYPE_SERVER_GET_USER_INFO,null, errorCode,errorMsg);
            }
        });
    }

    @Override
    public void getUserFriends(Context context, HashMap<String, String> params) {
    //m=user&c=userInfo&a=userFriends
        if(params==null){
            params = new HashMap<String, String>();
        }
        params.put("m","user");
        params.put("c","userInfo");
        params.put("a","userFriends");

        get(context, formatMapParamToString(context, params), new VolleyManager.ServerResponse<String>() {
            @Override
            public void onSuccess(String result) {
                resolveJson(result, new OnRepSuccessListener() {
                    @Override
                    public void onJsonArrayWidthPageSuccess(JSONArray jsonArray, String currentPage, String total) {
                        super.onJsonArrayWidthPageSuccess(jsonArray, currentPage, total);
                        UserBean userBean = new UserBean();
                        if(jsonArray!=null){
                            ArrayList<UserBean.User> fansItems = new ArrayList<UserBean.User>();
                            int size = jsonArray.length();
                            for(int i=0; i< size;i++){
                                JSONObject fansItemJson = jsonArray.optJSONObject(i);
                                if(fansItemJson!=null){
                                    UserBean.User fansItem = new UserBean.User();
                                    fansItem.avtUrl = fansItemJson.optString("avatarUrl");
                                    fansItem.username = fansItemJson.optString("username");
                                    fansItem.uid = fansItemJson.optString("uid");
                                    fansItems.add(fansItem);
                                }
                            }
                            userBean.users = fansItems;
                        }
                        userBean.currentPage = Integer.valueOf(currentPage);
                        userBean.totalCount = Integer.valueOf(total);
                        postEvent(ResponseEvent.TYPE_SERVER_GET_USER_FRIENDS,userBean,Server.Code.SUCCESS,"");
                    }

                    @Override
                    public void onFail(int errorCode, String errorMsg) {
                        postEvent(ResponseEvent.TYPE_SERVER_GET_USER_FRIENDS,null,errorCode,errorMsg);
                    }
                });
            }

            @Override
            public void onError(int errorCode, String errorMsg) {
                postEvent(ResponseEvent.TYPE_SERVER_GET_USER_FRIENDS,null,errorCode,errorMsg);
            }
        });

    }

    @Override
    public void getUserFans(Context context, HashMap<String, String> params) {
        //m=user&c=userFollow&a=fans
        if(params==null){
            params = new HashMap<String, String>();
        }
        params.put("m","user");
        params.put("c","userFollow");
        params.put("a","fans");

        resolverUser(context,params,ResponseEvent.TYPE_SERVER_GET_USER_FANS);
    }

    @Override
    public void getUserFollowers(Context context, HashMap<String, String> params) {
        //m=user&c=userFollow&a=followers
        if(params==null){
            params = new HashMap<String, String>();
        }
        params.put("m","user");
        params.put("c","userFollow");
        params.put("a","followers");

        resolverUser(context,params,ResponseEvent.TYPE_SERVER_GET_USER_FOLLOWERS);
    }

    @Override
    public void getUserSiteFollowers(Context context, HashMap<String, String> map) {

        HashMap<String, String> params = resolveAPIJsonToPost(context, map);

        post(context, formatBusinessUrl("/user/listVendorBySite"), params, new VolleyManager.ServerResponse<String>() {
            @Override
            public void onSuccess(String result) {

                try {
                    JSONObject jsonObject = new JSONObject(result);
                    JSONObject baseJson = jsonObject.getJSONObject("baseOutput");
                    String message = baseJson.optString("message");
                    if (baseJson != null) {
                        int code = baseJson.getInt("code");
                        if (isSuccess(code)) {
                            JSONArray jsonArray = jsonObject.getJSONArray("data");
                            if (jsonArray != null) {
                                int size = jsonArray.length();
                                ArrayList<UserBean.User> roles = new ArrayList<UserBean.User>();
                                UserBean userBean = new UserBean();
                                for (int i = 0; i < size; i++) {
                                    try {
                                        JSONObject userJson = jsonArray.getJSONObject(i);
                                        UserBean.User role = new UserBean.User();
                                        role.uid = String.valueOf(userJson.optInt("vendorId"));
                                        role.avtUrl = userJson.optString("avatar");
                                        role.username = userJson.optString("vendorName");
                                        roles.add(role);
                                    } catch (JSONException e) {
                                        Logger.e(Constants.TAG, "", e);
                                    }
                                }
                                userBean.users = roles;
                                postEvent(ResponseEvent.TYPE_SERVER_FOLLOW_AT_FRIENDS, userBean, Server.Code.SUCCESS, "");
                            }
                        } else {
                            postEvent(ResponseEvent.TYPE_SERVER_FOLLOW_AT_FRIENDS, null, code, message);
                        }
                    }

                } catch (Exception e) {
                    Logger.e(Constants.TAG, "register user error.", e);
                    postEvent(ResponseEvent.TYPE_SERVER_FOLLOW_AT_FRIENDS, null, -1, "数据异常");
                }

            }

            @Override
            public void onError(int errorCode, String errorMsg) {
                postEvent(ResponseEvent.TYPE_SERVER_FOLLOW_AT_FRIENDS, null, errorCode, errorMsg);
            }
        });

    }

    private void resolverUser(final Context context, final HashMap<String, String> params,final int eventType){
        get(context, formatMapParamToString(context,params), new VolleyManager.ServerResponse<String>() {
            @Override
            public void onSuccess(String result) {
                resolveJson(result, new OnRepSuccessListener() {

                    @Override
                    public void onJsonArrayWidthPageSuccess(JSONArray jsonArray, String currentPage, String total) {
                        super.onJsonArrayWidthPageSuccess(jsonArray, currentPage, total);
                        UserBean userBean = new UserBean();
                        if(params.containsKey(Server.Param.UID)){
                            userBean.uid = params.get(Server.Param.UID);
                        }else{
                            userBean.uid = getUid(context);
                        }
                        if(jsonArray!=null){
                            ArrayList<UserBean.User> users = new ArrayList<UserBean.User>();
                            int size = jsonArray.length();
                            for(int i=0;i<size;i++){
                                JSONObject itemJson = jsonArray.optJSONObject(i);
                                if(itemJson!=null){
                                    UserBean.User user = new UserBean.User();
                                    user.avtUrl = itemJson.optString("avtUrl");
                                    user.username = itemJson.optString("username");
                                    user.uid = itemJson.optString("uid");
                                    user.followstatus = itemJson.optInt("followstatus");
                                    users.add(user);

                                }
                            }
                            userBean.users = users;
                        }
                        userBean.currentPage = Page.resolve(currentPage);
                        userBean.totalCount = Page.resolve(total);
                        postEvent(eventType,userBean,Server.Code.SUCCESS,"");
                    }

                    @Override
                    public void onFail(int errorCode, String errorMsg) {
                        postEvent(eventType,null,errorCode,errorMsg);
                    }
                });
            }

            @Override
            public void onError(int errorCode, String errorMsg) {
                postEvent(eventType,null,errorCode,errorMsg);
            }
        });
    }

    @Override
    public void report(Context context, HashMap<String, String> params) {
        //m=misc&c=miscReport&a=report
        if(params==null){
            params = new HashMap<String, String>();
        }
        params.put("m","misc");
        params.put("c","miscReport");
        params.put("a","report");

        resolveUser(context,params,ResponseEvent.TYPE_SERVER_REPORT);
    }

    @Override
    public void focuseUser(Context context, HashMap<String, String> params) {
        //m=user&c=userFollow&a=follow
        if(params==null){
            params = new HashMap<String, String>();
        }
        params.put("m","user");
        params.put("c","userFollow");
        params.put("a","follow");

        resolveUser(context,params,ResponseEvent.TYPE_SERVER_FOCUSE_USER);
    }

    /*@Override
    public void cancelFocuseFeed(Context context, HashMap<String, String> params) {
        //m=user&c=userFollow&a=unFollow
        if(params==null){
            params = new HashMap<String, String>();
        }
        params.put("m","user");
        params.put("c","userFollow");
        params.put("a","unFollow");

        resolveUser(context,params,ResponseEvent.TYPE_SERVER_CANCEL_FOCUSE_USER);
    }*/

    @Override
    public void cancelFavoriteFeed(Context context, HashMap<String, String> params) {
        //m=home&c=favorite&a=favoriteDel
        if(params==null){
            params = new HashMap<String, String>();
        }
        params.put("m","home");
        params.put("c","favorite");
        params.put("a","favoriteDel");

        resolveUser(context,params,ResponseEvent.TYPE_SERVER_CANCEL_FAVORITE);
    }

    private void resolveUser(Context context, final HashMap<String, String> params,final int eventType){
        get(context, formatMapParamToString(context, params), new VolleyManager.ServerResponse<String>() {
            @Override
            public void onSuccess(String result) {
                resolveJson(result, new OnRepSuccessListener() {
                    @Override
                    public void onSuccess() {
                        super.onSuccess();
                        HashMap<String, String> map = new HashMap<String, String>();
                        map.put(Server.Param.POSITION, params.containsKey(Server.Param.POSITION) ? params.get(Server.Param.POSITION) : "");
                        map.put(Server.Param.OPTION, params.containsKey(Server.Param.OPTION) ? params.get(Server.Param.OPTION) : "");
                        map.put(Server.Param.FUID, params.containsKey(Server.Param.FUID) ? params.get(Server.Param.FUID) : "");
                        postEvent(eventType, map, Server.Code.SUCCESS, "");
                    }

                    @Override
                    public void onSuccess(JSONObject json) {
                        super.onSuccess(json);
                        postEvent(eventType, null, Server.Code.SUCCESS, "");
                    }

                    @Override
                    public void onFail(int errorCode, String errorMsg) {
                        postEvent(eventType, null, errorCode, errorMsg);
                    }
                });
            }

            @Override
            public void onError(int errorCode, String errorMsg) {
                postEvent(eventType, null, errorCode, errorMsg);
            }
        });
    }

    @Override
    public void searchUser(final Context context, HashMap<String, String> params) {
        //m=search&c=search&a=searchUser
        if(params==null){
            params = new HashMap<String, String>();
        }
        params.put("m","search");
        params.put("c","search");
        params.put("a","searchUser");

        get(context, formatMapParamToString(context, params), new VolleyManager.ServerResponse<String>() {
            @Override
            public void onSuccess(String result) {
                resolveJson(result, new OnRepSuccessListener() {

                    @Override
                    public void onSuccess() {
                        super.onSuccess();
                        postEvent(ResponseEvent.TYPE_SERVER_SEARCH_USER, null, Server.Code.SUCCESS, "");
                    }
                    @Override
                    public void onJsonArrayWidthPageSuccess(JSONArray jsonArray, String currentPage, String total) {
                        super.onJsonArrayWidthPageSuccess(jsonArray, currentPage, total);
                        UserBean.SearchUserBean searchUserBean = new UserBean.SearchUserBean();
                        Logger.e(Constants.TAG,"json------>arr"+ jsonArray);
                        if(jsonArray!=null){
                            ArrayList<UserBean.User> items = new ArrayList<UserBean.User>();
                            int size =jsonArray.length();
                            Logger.e(Constants.TAG,"json------>arrsize"+ size);
                            for(int i=0;i<size;i++){
                                JSONObject userItemJson = jsonArray.optJSONObject(i);
                                if(userItemJson!=null){
                                    UserBean.User searchUser = new UserBean.User();
                                    searchUser.uid = userItemJson.optString("uid");
                                    searchUser.username = userItemJson.optString("username");
                                    searchUser.avtUrl = userItemJson.optString("avtUrl");
                                    items.add(searchUser);
                                }
                            }
                            searchUserBean.items = items;
                        }
                        searchUserBean.currentPage = Page.resolve(currentPage);
                        searchUserBean.totalCount = Page.resolve(total);
                        postEvent(ResponseEvent.TYPE_SERVER_SEARCH_USER,searchUserBean,Server.Code.SUCCESS,"");
                    }

                    @Override
                    public void onFail(int errorCode, String errorMsg) {
                        postEvent(ResponseEvent.TYPE_SERVER_SEARCH_USER,null,errorCode,errorMsg);
                    }
                });
            }

            @Override
            public void onError(int errorCode, String errorMsg) {
                postEvent(ResponseEvent.TYPE_SERVER_SEARCH_USER,null,errorCode,errorMsg);
            }
        });
    }

    @Override
    public void fetchDiscuzVerifyCode(Context context, HashMap<String, String> params) {
        if(params==null){
            params = new HashMap<String, String>();
        }
        //m=forum&c=forumNewThread&a=getActivityCode
        params.put("m","forum");
        params.put("c","forumNewThread");
        params.put("a","getActivityCode");

        get(context, formatMapParamToString(context, params), new VolleyManager.ServerResponse<String>() {
            @Override
            public void onSuccess(String result) {

                resolveJson(result, new OnRepSuccessListener() {

                    @Override
                    public void onSuccess(JSONObject json) {
                        super.onSuccess(json);
                        Logger.e(Constants.TAG,"fetchDiscuzVerifyCode >> json : "+json);
                        postEvent(ResponseEvent.TYPE_SERVER_DISCUZ_VERIFY_CODE,null,Server.Code.SUCCESS,null);
                    }

                    @Override
                    public void onFail(int errorCode, String errorMsg) {
                        postEvent(ResponseEvent.TYPE_SERVER_DISCUZ_VERIFY_CODE,null,errorCode,errorMsg);
                    }
                });
            }

            @Override
            public void onError(int errorCode, String errorMsg) {
                postEvent(ResponseEvent.TYPE_SERVER_DISCUZ_VERIFY_CODE,null,errorCode,errorMsg);
            }
        });

    }

    @Override
    public void joinYiQiAction(Context context, HashMap<String, String> map) {

        String cityName = map.get("cityName");
        String frameworkId = "0";
        map.put(Server.Param.CHANNEL_FIRST, "Android");
        if (!TextUtils.isEmpty(cityName)){
            if (cityName.contains("北京")){
                frameworkId = "3";
            }else if (cityName.contains("上海")){
                frameworkId = "40";
            }else if (cityName.contains("哈尔滨")){
                frameworkId = "50";
            }else if (cityName.contains("石家庄")){
                frameworkId = "60";
            }else if (cityName.contains("天津")){
                frameworkId = "70";
            }else if (cityName.contains("西安")){
                frameworkId = "80";
            }else if (cityName.contains("武汉")){
                frameworkId = "90";
            }else if (cityName.contains("成都")){
                frameworkId = "100";
            }else if (cityName.contains("大连")){
                frameworkId = "110";
            }else if (cityName.contains("济南")){
                frameworkId = "120";
            }
        }

        // 西安 80，武汉 90，成都 100，大连 110，济南 120
        map.put("frameworkId", frameworkId);
        HashMap<String, String> params = resolveAPIJsonToPost(context, map);

        post(context, formatBusinessUrl("/housekeepAdmin/addHousekeepOrder4App"), params, new VolleyManager.ServerResponse<String>() {
            @Override
            public void onSuccess(String result) {

                try {
                    JSONObject jsonObject = new JSONObject(result);
                    JSONObject baseJson = jsonObject.getJSONObject("baseOutput");
                    String message = baseJson.optString("message");
                    if (baseJson != null) {
                        int code = baseJson.getInt("code");
                        if (isSuccess(code)) {
                            String data = jsonObject.optString("data");

                            postEvent(ResponseEvent.TYPE_SERVER_JOIN_YIQI_ACTION, "", Server.Code.SUCCESS, "");
                        } else {
                            postEvent(ResponseEvent.TYPE_SERVER_JOIN_YIQI_ACTION, null, code, message);
                        }
                    }

                } catch (Exception e) {
                    Logger.e(Constants.TAG, "register user error.", e);
                    postEvent(ResponseEvent.TYPE_SERVER_JOIN_YIQI_ACTION, null, -1, "数据异常");
                }

            }

            @Override
            public void onError(int errorCode, String errorMsg) {
                postEvent(ResponseEvent.TYPE_SERVER_JOIN_YIQI_ACTION, null, errorCode, errorMsg);
            }
        });
    }

    @Override
    public void updateUserDetail(Context context, HashMap<String, String> map) {

        HashMap<String, String> params = resolveAPIJsonToPost(context, map);

        post(context, formatBusinessUrl("/user/updateUserDetail"), params, new VolleyManager.ServerResponse<String>() {
            @Override
            public void onSuccess(String result) {

                try {
                    JSONObject jsonObject = new JSONObject(result);
                    JSONObject baseJson = jsonObject.getJSONObject("baseOutput");
                    String message = baseJson.optString("message");
                    if (baseJson != null) {
                        int code = baseJson.getInt("code");
                        if (isSuccess(code)) {
                            String data = jsonObject.optString("data");
                            postEvent(ResponseEvent.TYPE_SERVER_EDIT_USER_INFO, "", Server.Code.SUCCESS, "");
                        } else {
                            postEvent(ResponseEvent.TYPE_SERVER_EDIT_USER_INFO, null, code, message);
                        }
                    }

                } catch (Exception e) {
                    Logger.e(Constants.TAG, "register user error.", e);
                    postEvent(ResponseEvent.TYPE_SERVER_EDIT_USER_INFO, null, -1, "数据异常");
                }

            }

            @Override
            public void onError(int errorCode, String errorMsg) {
                postEvent(ResponseEvent.TYPE_SERVER_JOIN_YIQI_ACTION, null, errorCode, errorMsg);
            }
        });
    }

    @Override
    public void getOwnerInfoAd(Context context, HashMap<String, String> paramap) {

        if(paramap==null){
            paramap = new HashMap<String, String>();
        }
        paramap.put("action","getownerinfoad");
        get(context, formatApiMapParamToString(context, "/AppManagerApi.php?version=1", paramap), new VolleyManager.ServerResponse<String>() {
            @Override
            public void onSuccess(String result) {
                resolveJson(result, new OnRepSuccessListener() {

                    @Override
                    public void onJsonArraySuccess(JSONArray json) {
                        super.onJsonArraySuccess(json);
                        if (json != null && json.length() > 0){
                            postEvent(ResponseEvent.TYPE_SERVER_GET_YIQI_AD, json.optJSONObject(0), Server.Code.SUCCESS, null);
                        }else{
                            postEvent(ResponseEvent.TYPE_SERVER_GET_YIQI_AD, null, 10000, "");
                        }
                    }

                    @Override
                    public void onFail(int errorCode, String errorMsg) {
                        postEvent(ResponseEvent.TYPE_SERVER_GET_YIQI_AD, null, errorCode, errorMsg);
                    }
                });
            }

            @Override
            public void onError(int errorCode, String errorMsg) {
                postEvent(ResponseEvent.TYPE_SERVER_GET_YIQI_AD, null, errorCode, errorMsg);
            }
        });
    }


}
