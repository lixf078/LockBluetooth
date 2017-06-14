package com.lock.lib.api;

/**
 * Created by hubing on 16/4/12.
 */
public class Server {

    public static final String ERROR_SERVER_MSG = "数据异常，请稍后";
    private static boolean isOnLine;


    public static final class Page{
        public static final int  PAGE_START = 1;
        public static final int PAGE_SIZE = 20;
    }

    public static final class Permission {
        public static final String PERMISSION_YES = "yes";
        public static final String PERMISSION_NO = "no";
    }

    public static final void setOnLine(boolean onLine) {
        isOnLine = onLine;
    }

    public static final String getServerUrl() {

//        return BuildConfig.BBS_API_SERVER_URL;
        return "";
    }

    public static final String getBusinessServerUrl() {

//        return BuildConfig.HUI_API_SERVER_URL;
        return "";
    }

    public static final String getOrderServerUrl() {
//        return BuildConfig.WEB_SITE_SERVER_URL;
        return "";
    }

    public static final String getApiServerUrl() {
//        return BuildConfig.WEB_SITE_SERVER_URL;
        return "";
    }

    public static final class Code {
        public static final int SUCCESS = 0;
        public static final int FAIL = 1;
        public static final String PAY_RESULT_SUCCESS = "pay_success";
        public static final String PAY_RESULT_FAILURE = "pay_failure";
    }

    public static final class Param {
        public static final String POSITION = "position";
        public static final String COUNT = "count";
        public static final String FID = "fid";
        public static final String PAGE = "page";
        public static final String PAGE_NO = "page";
        public static final String PAGE_NO_APP = "pageNo";
        public static final String PAGE_SIZE = "pageSize";
        public static final String TID = "tid";
        public static final String KEY_WORD = "kw";
        public static final String UID = "uid";
        public static final String USER_ID = "userId";
        public static final String THREADSTICK = "threadsticky";
        public static final String ID = "id";
        public static final String TYPE = "type";
        public static final String TYPE_ID = "typeid";
        public static final String ORDER_BY = "orderby";
        public static final String VIEW_TYPE = "viewtype";
        public static final String IMG_WIDTH = "imgwidth";
        public static final String OP = "op";
        public static final String OPTION = "option";
        public static final String FUID = "fuid";
        public static final String PID = "pid";
        public static final String RTYPE = "rtype";

        public static final String TAG_ID = "tagid";
        public static final String CITY_ID = "city_id";
        public static final String CITY_NAME = "cityName";
        public static final String TAG_NAME = "name";
        public static final String MOBILE = "mobile";
        public static final String PHONE_NUMBER = "phoneNumber";
        public static final String USERNAME = "userName";
        public static final String PHONE_CODE = "phoneCode";
        public static final String PASSWORD = "password";
        public static final String CODE = "code";
        public static final String VERIFY_CODE = "verifyCode";

        public static final String PERMISSION = "haspermission";
        public static final String FEED_TYPE = "feed_type";
        public static final String CURRENT_TOPICNAME = "current_topicname";

        //和工地相关
        public static final String BUILDING_ID = "buildingId";
        public static final String PROGRESS_ID = "progressId";
        public static final String MESSAGE = "message";
        public static final String AT_LIST_JSON = "atList";
        public static final String UPLOAD_IMG_LIST = "imgSrc";
        public final static String TRACKER_ID = "trackId";
        public final static String VENDOR_ID = "vendorId";
        //和工地相关

        public static final String TAG_COMPLAIN = "complain";
        public static final String TAG_PRAISE = "praise";

        public static final String LONGITUDE = "longitude";
        public static final String LATITUDE = "latitude";

        public static final String CHANNEL_FIRST = "channelFirst";
        public static final String CHANNEL_SECOND = "channelSecond";
        public static final String CHANNEL_KEY = "channelKey";
        public static final String FRAMEWORK_ID = "frameworkId";

        public static final String USER_NAME = "";
        public static final String REAL_NAME = "realName";
        public static final String NICK_NAME = "nickName";
        public static final String CITY_ID_NEW = "cityId";
        public static final String USER_AVATAR = "avatar";
        public static final String USER_SIGNATURE = "signature";
        public static final String USER_GENDER = "gender";
        public static final String USER_PROVINCE_ID = "provinceId";
        public static final String USER_MOBILE = "mobile";


    }

}
