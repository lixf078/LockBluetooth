package com.lock.lib.common.constants;


/**
 * Created by hubing on 15/8/7.
 * <p/>
 * 常量
 */
public class Constants {
    public static final String TAG = "yi";
    public static final int INVALID = -1;
    public static final String HUANXIN_PASSWORD = "P8SVu2jzu8ClCSUEob9C3K";
    public static final String WEB_SITE = "http://m.17house.com";
    public static final String defaltSharePic = "http://s1.17house.com/app/images/yusuanbiao/01_2x.png";

    public static final String ADD_IMAGE_PATH_SAMPLE = "add_image_path_sample";
    public static String PICKED_IMAGES = "picked_images";
    public static int PICK_IMAGE_REQ_CODE = 10001;
    public static String SELECTED_IMAGES = "seldcted_images";
    public static final String FROM_MSG_POST = "msg_form_post";

    public static final class AppKey {
        public static final String QQ_ID = "1104899374";
        public static final String QQ_SCREATE = "2weFDaPznE4njcYJ";
        public static final String WEIXINID = "wxffc20d83f6cc074d";
        public static final String WEIXIIN_SCREATE = "d4624c36b6795d1d99dcf0547af5443d";
        public static final String WEIXIN_CIRCLE_ID = "wxffc20d83f6cc074d";
        public static final String WEIXIN_CIRCLE_SCREATE = "d4624c36b6795d1d99dcf0547af5443d";
    }

    public static class IntentKey {
        public static final String BUILDING_ID = "buildingId";
        public static final String PID = "pid";
        public static final String TID = "tid";
        public static final String FID = "fid";
        public static final String PAY_CHANNEL = "pay_channel";
        public static final String INTENT_KEY_FINISH= "intent_key_finish";
        public static final String REPLY_ID = "reply_id";
        public static final String REPLY_PARENT_ID = "reply_parent_id";
        public static final String MAI_DIAN = "maidian";

    }

    public static class Path {
        public static final String HOME = "17house";
        public static final String DISK_IMG_CACHE = "cache";
    }

    public static class Size {
        public static final int DISK_CACHE_SIZE = 100 * 1024 * 1024; //100M
    }

    public static class Mode {
        public static final int SINGLE = 0;
        public static final int MULTIPLE = 1;
    }

    public static final class State {
        public static final class Pay {
            public static final int STATE_UNPAY = 1; // 未支付
            public static final int STATE_DOING = 2; // 支付中
            public static final int STATE_FINISH = 3;// 支付完成
        }

        public static final class Order {
            public static final int CANCEL = 1; // 订单 被取消
            public static final int STATE_FINISH = 1; // 订单状态完成
            public static final int STATE_DONGING = 2;// 处理中
            public static final int STATE_UN_START = 3; // 即将处理
            public static final int STATE_SKIP = 4; // 已被忽略
        }

    }


    public static final class Param {
        public static final String TOKEN = "token";
        public static final String COMMODITY_ID = "commodity_id";
        public static final String USER_NAME = "user_name";
        public static final String USER_PHONE = "user_phone";
        public static final String USER_ADDRESS = "user_address";
        public static final String USER_REMARK = "user_remark";
        public static final String TIME = "time";
        public static final String CHECKOUT_ID = "checkoutId";
        public static final String ORDER_GROUP_STATE = "group_state";
        public static final String ORDER_ID = "orderId";
        public static final String BUILDING_ID = "buildingId";
        public static final String TRACK_ID = "trackId";
        public static final String CHANNEL = "channel";
        public static final String TYPE = "type";
        public static final String PAYMENT_SOURCE = "paymentSource";
        public static final String OPEN_ID = "openId";
        public static final String AUTH_CODE = "authCode";
        public static final String EXTRA_DATE = "extraData";
        public static final String POCKET_IDENTIFY = "identify";
    }

    public static final class ResultCode {
        // 公用结果
        public static final int RC_FAILED = 0;
        public static final int RC_SUCCESS = 1;
        public static final int RC_TAKE_PHOTO = 2;

        public static final int RC_UNABLE_CONNECT_SERVER = 13;
        public static final int RC_TIME_OUT = 14;
        public static final int RC_NETWORK_UNAVAILABLE = 15;
        public static final int RC_XIAOGUOTU = 16;//效果图
        public static final int RC_LINGGANTU = 17;//灵感图集
        public static final int RC_SETPHONE = 18;//申请设计，设置电话号码
        public static final int RC_LOGIN = 19;//登陆
        public static final int RC_UPDATA_FEED = 20;//更新feed列表
        public static final int RC_INPUT_USER_INFO = 21;//更新预订单信息
        public static final int RC_HUITIE= 22;//回帖
    }

    public static final class RequestCode {
        public static final int RQ_TUANGUO_BAOMING = 2;//团购报名
        public static final int RQ_YANFENG_SHEGQING = 3;//验房申请
        public static final int RQ_JIANLI_SHENGQINGG = 4;//免费监理管家申请
        public static final int RQ_SPECIAL_PIC_SHENGQING = 5;//专题申请设计
        public static final int RQ_BIG_PIC_SHENGQING = 6;//效果图申请设计
        public static final int RQ_POST_DIANZHAN = 7;//帖子（feed）点赞
        public static final int RQ_POST_HUIFU = 8;//帖子(feed)回复,评论
        public static final int RQ_POST_SHOUCHANG = 9;//帖子（feed）收藏
        public static final int RQ_FEED = 10;//feed返回刷新列表
        public static final int RQ_TOPIC_FEED = 11;//专题中feed返回
        public static final int RQ_UMCOM = 12;//umengCommunityfragment中的返回
        public static final int RQ_JUBAO = 13;//feed举报
        public static final int RQ_TOPIC_FOCUS = 14;//关注话题
        public static final int RQ_FEED_SEARCH = 15;//帖子搜索
        public static final int RQ_PRE_ORDER = 16;
        public static final int RQ_PRE_YEZHU = 17;
        public static final int RQ_OTHER_HOME = 18;
        public static final int RQ_PRODUCT =19;
        public final static int RQ_SELF_PAY = 20;

    }

    public static class Key {
        // preference keys
        public static final String KEY_JIAO_DIAN2_677="key_jiaodian2_677";
        public static final String Key_WEB_URL = "key_web_url";
        public static final String Key_WEB_IMG = "key_web_img";
        public static final String KEY_IP_HOST = "key_test_host";
        public static final String KEY_ACT_TYPE = "key_action_up"; //
        public static final String KEY_PROVINCE_CITY_INITED = "key_province_city_inited";
        public static final String KEY_ACCEPT_PUSH = "key_accept_push";
        public static final String KEY_USER_NAME = "key_user_name";
        public static final String KEY_USER_AVATAR = "key_user_avatar";
        public static final String KEY_USER_ID = "key_user_id"; // 17house 后台返回的UID
        public static final String KEY_USER_MOBILE = "key_user_mobile";
        public static final String KEY_COMMUNITY_NAME = "key_community_name";
        public static final String KEY_USER_COMMUNITY = "key_user_community";
        public static final String KEY_HOUSE_LAYOUT = "key_house_type";
        public static final String KEY_HOUSE_AREA = "key_house_area";
        public static final String KEY_HOUSE_STYLE = "key_house_style";
        public static final String KEY_HOUSE_BUDGET = "key_house_budget";
        public static final String KEY_FORUM_COUNT = "key_forum_count";
        public static final String KEY_DIGEST_COUNT = "key_digest_count";
        public static final String KEY_IS_FOCUSE = "key_is_focuse";
        public static final String KEY_POST_COUNT = "key_post_count";
        public static final String KEY_RENOVATION_DATE = "key_renovation_date";
        public static final String KEY_STAGE = "key_stage";
        public static final String KEY_IS_LOGIN = "key_is_login";
        public static final String KEY_WEIXIN_LOGIN = "key_weixin_login";
        public static final String KEY_SESSION_CODE = "key_session_code";
        public static final String KEY_WEIXIN_OPENID = "key_weixin_openid";
        public static final String KEY_SESSION_TIME = "key_session_time";
        public static final String KEY_SESSION_BUDGET = "key_session_budget";
        public static final String KEY_HUNXIN_REGISTERED = "key_hunxin_registered";
        public static final String KEY_CURRENT_CITY_ID = "key_current_city_id";
        public static final String KEY_BAIDU_CITY_CODE = "key_baidu_city_code";
        public static final String KEY_BAIDU_PROVINCE = "key_baidu_province";
        public static final String KEY_BAIDU_DISTRICTION = "key_baidu_distriction";
        public static final String KEY_BAIDU_ADDRSTR = "key_baidu_addrstr";
        public static final String KEY_IS_FIRST_LOADING = "is_first_loading";
        public static final String KEY_CITY_LOCAL_ID = "key_city_local_id";
        public static final String KEY_PHONE_NUMBER = "key_phone_number";
        public static final String KEY_SMS_CODE = "key_sms_code";
        public  static  final  String KEY_PASSWORD = "key_password";
        public static final String KEY_WEIXIN_PHONENUM = "key_weixin_phoneNum";
        public static final String KEY_HOT_LINE = "key_hot_line";
        public static final String KEY_LOGIN_USER_TYPE = "key_login_user_type";
        public static final String KEY_LOGIN_USER_BGCARDNUM = "key_login_user_bgCardNum";


        public static final String KEY_GPS_CITY_ID = "key_gps_city_id";
        public static final String KEY_GPS_CITY_NAME = "key_gps_city_name";
        public static final String KEY_SELECTED_CITY_ID = "key_selected_city_id";
        public static final String KEY_SELECTED_CITY_NAME = "key_selected_city_name";

        public static final String KEY_CURRENT_CITY_NAME = "key_current_city_name";
        public static final String KEY_CURRENT_LATITUDE = "key_current_latitude";
        public static final String KEY_CURRENT_LONGITUDE = "key_current_longitude";
        public static final String KEY_IMAGE = "key_image";
        public static final String KEY_IMAGES = "key_images";
        public static final String KEY_TAGS = "key_tags";
        public static final String KEY_RESULT_CODE = "key_result_code";
        public static final String KEY_BUDGET_SUM_NUM = "key_sum_num";
        public static final String KEY_BUDGET_SUM_MONEY = "key_sum_money";

        // message keys
        public static final String KEY_CLASS_NAME = "key_class_name";
        public static final String KEY_INTERESTED_MESSAGES = "key_interested_messages";
        public static final String KEY_MESSAGE_TYPE = "key_message_type";
        public static final String KEY_ERROR = "key_error";
        public static final String KEY_RELOAD = "key_reload";
        public static final String KEY_STATUS_CODE = "key_status_code";
        public static final String KEY_URL = "key_url";
        public static final String KEY_REQUEST_METHOD = "key_request_method";
        public static final String KEY_PARAMS = "key_params";
        public static final String KEY_POST_DATA = "key_post_data";
        public static final String KEY_MODE = "key_mode";
        public static final String KEY_AUTO_LOAD = "key_auto_load";
        public static final String KEY_FLAG = "key_flag";
        public static final String KEY_DATA = "key_data";
        public static final String KEY_FILES = "key_files";
        public static final String KEY_NAME = "key_name";
        public static final String KEY_FILE_NAME = "key_file_name";
        public static final String KEY_FILE_PATH = "key_file_path";
        public static final String KEY_FOLDER = "key_folder";
        public static final String KEY_NO_CACHE = "key_no_cache";
        public static final String KEY_STATUS = "key_status";
        public static final String KEY_STATUS_BAR = "key_status_bar";
        public static final String KEY_CHANNEL = "key_channel";
        public static final String KEY_INDEX = "key_index";
        public static final String KEY_DATE = "key_date";
        public static final String KEY_YEAR = "key_year";
        public static final String KEY_MONTH = "key_month";
        public static final String KEY_DAY = "key_day";
        public static final String KEY_HOUR = "key_hour";
        public static final String KEY_MINUTE = "key_minute";
        public static final String KEY_ID = "key_id";
        public static final String KEY_SKU_ID="key_sku_id";
        public static final String KEY_PIC_TO_BIG = "key_pic_to_big";
        public static final String KEY_PIC_GALLER_ID = "key_pic_galler_id";
        public static final String KEY_PIC_TAG_ID = "key_pic-tag_id";
        public static final String KEY_PIC_PAGE = "key_pic_page";
        public static final String KEY_XIAOGUOTU_BIAOQIAN_FENLEI = "key_xiaoguotu_biaoqian_fenlei";

        public static final String KEY_SPECIAL_ID = "special_id";
        public static final String KEY_SHARE_TITLE = "share_title";
        public static final String KEY_SHARE_DES = "share_des";
        public static final String KEY_SHARE_IMG = "share_img";
        public static final String KEY_UMENG_PUSH_AGENT = "push_agent";


        // Home keys

        public static final String KEY_CURRENT_COURSE_ID = "course_id";
        public static final String KEY_CURRENT_COURSE_NAME = "course_name";
        public static final String KEY_TEXT = "key_text";
        public static final String KEY_BUDGET_TYPE = "budget_type";
        public static final String KEY_BUDGET_ID = "budget_ID";
        public static final String KEY_BUDGET_CATEGORY = "budget_category";
        public static final String KEY_SHARE_BUDGET_ITEM_NUM = "budget_item_num";
        public static final String KEY_SHARE_BUDGET_TOTAL_NUM = "budget_total_num";
        public static final String KEY_SHARE_BUDGET_TOTAL_COST = "budget_total_cost";
        public static final String KEY_SHARE_BUDGET_ORI_TOTAL_COST = "budget_ori_total_cost";

        public static final String KEY_SHARE_BUDGET_DATE_FROM_SERVER = "budet_data_from_server";


        public static final String  KEY_FEED_PIC="key_feed_pic";
        public static final String   KEY_FEED_DES="key_feed_des";
        public static final String KEY_FROM = "from";
        public static final String KEY_TITLE = "key_title";
        public static final String KEY_FEED_ID = "feed_id";
        public static final String KEY_NEWS_ID = "news_id";
        public static final String KEY_TOPIC_ID = "topic_id";
        public static final String KEY_OTHER_ID = "budget_other_id";
        public static final String KEY_OTHER_USER_ID = "other_user_id";
        public static final String KEY_OTHER_HOUSE_INFO = "other_house_info";
        public static final String KEY_DETAIL_TITILE = "detail_title";
        public static final String KEY_BACK_TEXT = "back_title";
        public static final String KEY_COMMUNITY_TAB_ID = "key_community_tab_id";

        public static final String KEY_BUDGET_DISPLAY_CONFIRM = "budget_display_confirm";
        public static final String KEY_BUDGET_DISPLAY_DIALOG = "budget_display_dialog";
        public static final String KEY_BUDGET_REQUEST_SERVER = "budget_request_server";
        public static final String KEY_ACTIVITY_FROM_INTRODUCE = "activity_from_introduce";

        public static final String KEY_STAGE_SHOW_CONFIRM = "stage_show_confirm";
        public static final String KEY_STAGE_OPEN_BUDGET = "stage_open_budget";
        public static final String KEY_STAGE_SHOW_ACTION = "stage_show_action";
        public static final String KEY_STAGE_SHOW_DIALOG = "stage_show_dialog";
        public static final String KEY_STAGE_SHOW_SKIP = "stage_show_skip";
        public static final String KEY_STAGE_REQUEST_SERVER = "stage_request_server";
        public static final String KEY_STAGE_OPEN_MAIN = "stage_open_main";

        public static final String KEY_FIRST_ENTER = "fiser_enter";
        public static final String KEY_NEAD_PESOLVE = "nead_pesolve";

        public static final String KEY_UMENG_UID = "umeng_uid";
        public static final String KEY_UMENG_IS_LOGIN = "umeng_is_login";
        public static final String KEY_ENTER_SCHOOL = "enter_school";
        public static final String KEY_API_USER_ID = "api_uid"; //移动事业部后台返回的UID

        //惠一起预订单
        public static final String KEY_PRE_ORDER_USER_NAME = "pre_order_user_name";
        public static final String KEY_PRE_ORDER_PHONE_NUM = "pre_order_phone_num";
        public static final String KEY_PRE_ORDER_DETAIL_ADRESS = "pre_order_detail_adress";

        public static final String KEY_ORDER_ICON = "order_icon";
        public static final String KEY_ORDER_TITLE = "order_title";
        public static final String KEY_ORDER_ID = "order_id";
        public static final String KEY_PAY_POS_ZXING = "pay_pos_zxing";
        public static final String KEY_ORDER_REAL_PAY = "real_pay";
        public static final String KEY_COMMUNITY_ID = "community_id";
        public static final String KEY_CHECKOUT_ID = "checkout_id";
        public static final String KEY_PAY_CHANNEL = "pay_channel";
        public static final String KEY_PAY_TYPE = "pay_type";
        public static final String KEY_PAY_SOURCE = "pay_source";
        public static final String KEY_RESULT_ERROR_CODE = "error_code";
        public static final String KEY_RESULT_ERROR_MSG = "error_msg";
        public static final String KEY_PAY_RESULT = "pay_result";
        public static final String KEY_PRE_CHECKOUT_ID = "pre_checkout_id";
        public static final String KEY_PRE_PROCESS_ID = "processId";
        public static final String KEY_POINT_NAME = "Key_Point_Name";

        public static String KEY_ARTICLE_ID = "key_article_id";
        public static String KEY_ARTICLE_IS_ZAN = "key_article_isZan";
        public static String KEY_ARTICLE_IS_COMMENT = "key_article_isComment";
        public static String KEY_ARTICLE_COLLECTION_NUM = "Key_Article_Collection_Num";
        public static String KEY_ARTICLE_VIEWCOUNT = "Key_article_view_count";
        public static String KEY_TOPIC_DETAIL_UPDATE = "Key_topic_detail_update";
        public static String KEY_SELF_GOODS_NAME = "self_goods_name";

        public static String KEY_MSG_COMMENT_NUM = "key_msg_comment_num";
        public static String KEY_MSG_LIKE_NUM = "key_msg_like_num";
        public static String KEY_MSG_AT_NUM = "key_msg_at_num";
        public static String KEY_MSG_IS_FUN_USER = "Key_msg_is_fun_user";
        public static String KEY_MSG_IS_ATTENTION_USER = "Key_msg_is_attention_user";
        public static String KEY_MSG_COMMUNITY_ID = "Key_msg_community_id";
        public static String KEY_MSG_IS_SELF = "Key_msg_is_Self";
        public static String KEY_MSG_IS_FEED = "Key_msg_is_feed";
        public static String KEY_MSG_SYS_NUM = "key_msg_sys_num";
        public static String KEY_WEB_MIANFEIBAOJIAO = "key_web_mianfei";
        public static String KEY_WEB_ZHAOSHEJISHI = "key_web_zhaoshejishi";
        public static String KEY_WEB_FIND_677 = "key_web_find677";
        public static String KEY_WEB_FIND_377 = "key_web_find377";
        public static String KEY_WEB_YIQI_USER = "key_web_yiqi_user";
        public static String KEY_WEB_TYPE = "key_web_type";
        public static String KEY_WEB_TITLE = "key_web_title";
        public static String KEY_DEVICE_ID_TOKEN = "key_device_id_token";
        public static String KEY_BANNER_TYPE = "key_banner_type";
        public static String KEY_ATTENTION_NUM = "key_attention_num";
        public static String KEY_FANS_NUM = "key_fans_num";
        public static String KEY_TOPIC_NUM = "key_topic_num";
        public static String KEY_FEED_NUM = "key_feed_num";
        public static String KEY_ATENTION_USER = "key_attention_user";
        public static String KEY_FAN_USER = "key_fan_user";
        public static String KEY_THIRD_ID = "key_third_id";
        public static String KEY_PRODUCT_BUNDER = "key_product_bunder";
        public static  final String KEY_DISCOUNT_INF = "key_discount_info";
        public static  final  String KEY_DISCOUNT_LOG0= "key_discount_logo";
        public static  final  String KEY_DISCOUNT_BAND_NAME="key_discount_band_name";
        public static  final  String  KEY_DISCOUT_GROUP_TIME="key_discount_group_time";
        public static  final  String  KEY_DISCOUT_GROUP_NAME="key_discount_group_name";
        public static  final  String  KEY_FROM_NAME="key_from_name";
        public static  final  String  KEY_SELF_SUPPORT="key_support";

        public static final String KEY_DISCUZ_BLOCK_ID = "block_id";
        public static final String KEY_DISCUZ_BLOCK_NAME = "block_name"; // 版块名称
        public static final String KEY_DISCUZ_BLOCK_CITY_NAME = "block_city_name"; // 版块所属地
        public static final String KEY_DISCUZ_BLOCK_TYPE = "block_type"; // 版块类型 0 版块、1 精品选购、2 品牌热议
        public static final String KEY_DISCUZ_BLOCK_CONTENT_TYPE = "block_content_type"; // 0 全部、1 最新、精
        public static final String KEY_USER_POINTER_FROM_MOBILE = "key_user_pointer_from_mobile";
        public static final String KEY_HOME_FRESH_ID="key_home_fresh_id";
        public static  final  String KEY_HOME_FRESH_TYPE="key_home_fresh_type";

        public static  final  String KEY_FEED_TYPE="feed_type";
        public static  final  String KEY_FEED_TITLE="feed_title";
        public static  final  String KEY_FEED_CONTENT="feed_content";
        public static  final  String KEY_FEED_AT_USER="feed_at_user";
        public static  final  String KEY_FEED_ADDRESS="feed_address";
        public static  final  String KEY_FEED_IMAGES="feed_images";

        public static final String KEY_PUSH_AGENT = "push_agent";
        public static final String UPDATE_TYPE = "update_type";
        public static final String REPLY_PARENT_ID = "reply_parent_id";
        public static final String REPLY_ID = "reply_id";
        public static final String KEY_LIKE = "key_like";
        public static final String KEY_ITEM_POSITION = "key_item_position";

        public static final String KEY_STATIC_INFO_SKIP_NUM = "key_static_info_skip_num";
        public static final String KEY_STATIC_INFO_SUBMIT = "key_static_info_submit";

        public static final String KEY_NICK_NAME = "key_nick_name";
        public static final String KEY_SIGNATURE = "key_signature";
        public static final String KEY_BOSS_ID = "key_boss_id";

        public static final String KEY_FEED_IS_ACTIVITY = "key_feed_is_activity";
        public static final String KEY_SOP_ID = "sopid";
    }

    public static class FeedType {
        public final static int FEED_TYPE_LAOZHONGYI =3; // 老中医
        public final static int FEED_TYPE_SCHOOL = 1; // 装修学堂
        // public final static int FEED_TYPE_TOUTIAO = 2; // 装修头条
        public final static int FEED_TYPE_PIC_SPECIAL = 2; // 专题图片
    }

    public static class Action {

        public static final String ACTION_MAIN = "";
        public static String ACTION_MAIN_TITLE_SEARCH = "com.aiyiqi.galaxy.HomeSearchActivity";
        public static String ACTION_MAIN_GRID_RENDERINGS = "com.aiyiqi.galaxy.HomeRenderingsActivity";
        public static String ACTION_MAIN_GRID_FENGSHUI = "com.aiyiqi.galaxy.HomeFengShuiActivity";
        public static String ACTION_MAIN_GRID_GROUPPURCHASE = "com.aiyiqi.galaxy.HomeGroupPurchaseActivity";
        public static String ACTION_MAIN_GRID_SUPERVISION = "com.aiyiqi.galaxy.HomeSupervisionActivity";
        public static String ACTION_MAIN_GRID_INSPECTION = "com.aiyiqi.galaxy.HomeInspectionActivity";
        public static String ACTION_MAIN_GRID_TRAP = "com.aiyiqi.galaxy.HomeTrapActivity";
        public static String ACTION_MAIN_GRID_SCHOOL = "com.aiyiqi.galaxy.HomeSchoolActivity";
        public static String ACTION_UNFINISHED_BUDGET = "com.aiyiqi.galaxy.MyUnFinishBudgetActivity";
        public static String ACTION_CUSTOMIZE_BUDGET = "com.aiyiqi.galaxy.MyCustomizeBudgetActivity";
        public static String ACTION_INVENTORY = "com.aiyiqi.galaxy.InventoryActivity";
        public static String ACTION_NEARBY_MERCHANT = "com.aiyiqi.galaxy.NearbyMerchantActivity";
        public static String ACTION_MY_PROCESS = "com.aiyiqi.galaxy.MyProcessActivity";
        public static String ACTION_COLLECT_INFO = "com.aiyiqi.galaxy.CollectInfoActivity";
        public static String ACTION_CHART = "com.aiyiqi.galaxy.ChatActivity";
        public static String ACTION_PAY_RESULT_WX = "com.aiyiqi.galaxy.PAY_RESULT_WX";
        public static String ACTION_PAY_RESULT_POS = "com.aiyiqi.galaxy.PAY_RESULT_POS";
        public static String ACTION_FETCH_WX_PAY_STATE_FROM_SERVER = "com.aiyiqi.galaxy.FETCH_WX_PAY_STATE_FROM_SERVER";
        public static String ACTION_FETCH_POS_PAY_STATE_FROM_SERVER = "com.aiyiqi.galaxy.FETCH_POS_PAY_STATE_FROM_SERVER";
        public static String ACTION_PAY_RESULT_RETURN = "com.aiyiqi.galaxy.PAY_RESULT_RETURN";
        public static String ACTION_SESSION_EXPIRED = "com.aiyiqi.galaxy.ACTION_SESSION_EXPIRED";

    }

    public static class Message {
        // base message
        public static final int MSG_FAILED = 100;
        public static final int MSG_SUCCESS = 101;
        public static final int MSG_START = 102;
        public static final int MSG_EXIT = 103;
        public static final int MSG_REGISTER_CLIENT = 104;
        public static final int MSG_CLIENT_REGISTERED = 105;
        public static final int MSG_UNREGISTER_CLIENT = 106;
        public static final int MSG_APP_STOP = 107;
        public static final int MSG_STORAGE_UNAVAILABLE = 120;
        public static final int MSG_STORAGE_AVAILABLE = 121;
        public static final int MSG_NETWORK_AVAILABLE = 122;
        public static final int MSG_NETWORK_UNAVAILABLE = 123;
        public static final int MSG_DATA_RECEIVED = 124;
        public static final int MSG_CHANGE_HOST = 125;
        public static final int MSG_CHANGE_STATUS_BAR = 126;
        public static final int MSG_HUANXIN_ACCOUNT_CREATED = 127;
        public static final int MSG_COUNTDOWN = 128;
        public static final int MSG_LOCATE_COMPLETE = 129;
        public static final int IMG_REQUEST_CODE = 800;

        // business message
        public static final int MSG_SHANGJIA_XIANGQING = 179;//商家详情
        public static final int MSG_LOGIN = 180;  //登录
        public static final int MSG_AUTO_LOGIN = 181;  //自动登录
        public static final int MSG_LOGOUT = 182; //退出登录
        public static final int MSG_SESSION_EXPIRED = 183; //SESSION过期
        public static final int MSG_PROVINCE_CITY = 202; //城市数据
        public static final int MSG_XIAOGUOTU_LIEBIAO = 203; //效果图列表
        public static final int MSG_XIAOGUOTU_XIANGQING = 204; //效果图详情
        public static final int MSG_LINGGANTUJI_LIEBIAO = 205; //灵感图集列表
        public static final int MSG_LINGGANTUJI_XIANGQING = 206; //灵感图集详情

        public static final int MSG_GROUPON_SHENQING = 207; //团购申请

        public static final int MSG_GROUPON_BAOMING = 208; //团购报名
        public static final int MSG_GROUPON_SHANGPIN = 209; //团购参会商品
        public static final int MSG_ZIXUN_SOUSUO = 210; //资讯搜索
        public static final int MSG_ZIXUN_XIANGQING = 211; //资讯详情
        public static final int MSG_FENGSHUI_LIEBIAO = 212; //装修风水列表
        public static final int MSG_SHOUJIHAO_JIANCHA = 213; //手机号注册检查
        public static final int MSG_HUOQU_YANZHENGMA = 214; //获取验证码
        public static final int MSG_ZHUCE = 215; //手机验证码校验及注册
        public static final int MSG_MIMA_DENGLU = 216; //手机号+密码登录
        public static final int MSG_YANZHENGMA_DENGLU = 217; //手机号+验证码登录
        public static final int MSG_XIUGAI_YONGHUMING = 218; //修改用户名
        public static final int MSG_XIUGAI_MIMA = 219; //修改密码
        public static final int MSG_TUICHU_DENGLU = 220; //退出登录
        public static final int MSG_YONGHU_XINXI = 221; //获取用户信息
        public static final int MSG_SHEZHI_ZHUANGXIU_JIEDUAN = 222; //设置装修阶段
        public static final int MSG_HUOQU_ZHUANGXIU_JIEDUAN = 223; //获取装修阶段
        public static final int MSG_TIANJIA_SHOUCANG = 224; //添加收藏
        public static final int MSG_SHOUCANG_XINWEN_LIEBIAO = 225; //收藏帖子列表
        public static final int MSG_ZUIXIN_TIEZI_LIEBIAO = 226; //最新帖子列表
        public static final int MSG_BIEREN_TIEZI_LIEBIAO = 227; //别人的帖子列表
        public static final int MSG_ZIJI_TIEZI_LIEBIAO = 228; //自己的帖子列表
        public static final int MSG_BIEREN_JINGHUA_LIEBIAO = 229; //别人的精华列表
        public static final int MSG_ZIJI_JINGHUA_LIEBIAO = 230; //自己的精华列表
        public static final int MSG_BIEREN_HUIFU_LIEBIAO = 231; //别人的回复列表
        public static final int MSG_ZIJI_HUIFU_LIEBIAO = 232; //自己的回复列表
        public static final int MSG_GROUPON_SHUJU = 233; //团购数据
        public static final int MSG_GROUPON_HAIBAO = 234; //团购会海报
        public static final int MSG_24XIAOSHI_RETIE = 235; //24小时热帖
        public static final int MSG_JUBAO_TIEZI = 236; //举报帖子
        public static final int MSG_SOUSUO_TIEZI = 237; //搜索帖子
        public static final int MSG_YANZHENGMA_XIUGAI_MIMA = 238; //验证码修改密码
        public static final int MSG_FUJIN_SHANGJIA = 239; //附近商家
        public static final int MSG_YEZHU_REYI = 240; //业主热议
        public static final int MSG_FAITE = 241; //发帖接口
        public static final int MSG_HUITIE = 242; //回帖接口
        public static final int MSG_JIEDUAN_WEIZHANG = 243; //按装修阶段取文章列表接口
        public static final int MSG_JIEDUAN_CHAXUN_BIAOQIAN = 244; //根据装修阶段查询标签
        public static final int MSG_LAOZHONGYI_TIXING = 245; //老中医提醒
        public static final int MSG_SHOUCANG_TIEZI_LIEBIAO = 246; //收藏列表
        public static final int MSG_JINGQI_TUANGOU = 247; //近期团购接口
        public static final int MSG_BIAOQIAN_XINWEN_LIEBIAO = 248; //标签新闻列表
        public static final int MSG_TIEZI_XIANGQING = 249; //获取BBS帖子详情接口
        public static final int MSG_YONGHU_XIANGGUAN_XINXI = 250; //获取用户相关信息
        public static final int MSG_WEIXIN_DENGLU = 251; // 微信登录
        public static final int MSG_SHANCHU_TIEZI = 252; //删除BBS帖子接口
        public static final int MSG_YEZHUSHUO = 253; //业主说接口（独特）
        public static final int MSG_TIJIAO_ZHUANGXIU_XINXI_HUOQU_YUSUAN = 254; //提交或者编辑装修信息获取预算数据
        public static final int MSG_TONGBU_YUNDUAN_YUSUAN = 255; //同步云端装修预算数据
        public static final int MSG_HUOQU_YUNDUAN_YUSUAN = 256; //获取同步云端装修预算数据(实际采购数据)
        public static final int MSG_TAREN_ZHUANGXIU_XINXI = 257; //获取他人装修信息
        public static final int MSG_TAREN_YUSUAN_LIST = 258; //获取他人装修信息列表
        public static final int MSG_XIUGAI_XIAOQU = 259; //修改小区
        public static final int MSG_SHANGCHUAN_TUPIAN = 260; //上传图片
        public static final int MSG_WEIDU_XIAOXI = 261; //根据用户id获取最新未读取提醒消息
        public static final int MSG_HUOQU_BIAOQIAN_TIEZI = 262; //根据标签tagId获取帖子
        public static final int MSG_ZHUANGXIU_XUETANG = 263; //装修全部阶段的查询咨询
        public static final int MSG_WEIXIN_SHOUJIHAO_BANGDING = 264; //微信手机号绑定
        public static final int MSG_DIANZHAN_TIEZHI = 265;//点赞接口
        public static final int MSG_HUOQU_TIEZHI_DIANZHANSHU = 266;//根据帖子id获取帖子点赞总数
        public static final int MSG_XIAOGUOTU_BIAOQIAN_FENLEI = 267;//获取效果图分类id,name的对应关系接口
        public static final int MSG_XIUGAI_TOUXIANG = 268;//修改头像
        public static final int MSG_BIEREN_TIEZI = 269;//查看别人帖子列表（合并接口）
        public static final int MSG_TIEZHI_SHIFUO_SHOUCHANG = 270; //帖子是否收藏
        public static final int MSG_GET_DEC_COMPANY = 281;// 获取装修公司；
        public static final int MSG_ARTICLE_COMMENT = 282;//获取文章评论
        public static final int MSG_ADD_ARTICLE_COMMENT = 283;//添加文章评论


        // 通知消息
        public static final int MSG_NICK_NAME_CHANGED = 400; //昵称修改
        public static final int MSG_CHANGE_STAGE = 401; //装修流程修改
        public static final int MSG_CHANGE_BUDGET = 402; //房屋信息修改
        public static final int MSG_CHANGE_HOUSE_INFO = 403; //装修流程修改
        public static final int MSG_USER_IINFO_READY = 404; // 用户信息已经保存到Share中
        public static final int MSG_UMENG_LOGIN_READY = 405;
        public static final int MSG_FOCUSE_TOPIC = 406; // 某个话题加关注
        public static final int MSG_HOME_DATA_RESET = 407;
        public static final int MSG_FEED_REFRESH = 408;//回来刷新feed列表
        public static final int MSG_UMENG_USER_INFO_READY = 409; // 获取到umeng的个人信息
        public static final int MSG_PAY_SUCCESS = 410;
        public static final int MSG_HOME_BANNER_LIST = 411;// 新版首页上都banner图
        public static final int MSG_CHANGE_BUDGET_INFO = 412; // 修改我的预算信心
        public static final int MSG_STAGE_FROM_SERVER_SUCCESS = 413;
        public static final int MSG_SIGN = 414; // 签到
        public static final int MSG_MY_ATTENTION = 415; // 我的关注
        public static final int MSG_ARTICLE_ZAN = 421;//文章点赞列表同步
        public static final int MSG_ARTICLE_NUM_COMMENT = 422;//文章评论列表同步
        public static final int MSG_ARTICLE_SEEN = 423;//文章列表看过同步
        public static final int MSG_TOPIC_DETAIL_UPDATA = 424;//话题详情更新
        public static final int MSG_CHANGE_COMMUNITY_TAB = 425; //切换业主说的页签
        public static final int MSG_FOCUSE_USER = 426;//关注用户
        public static final int MSG_TOKEN_IS_FAIL = 427;//token 失效
        public static final int MSG_GET_BANNER_LIST = 428;//获取套餐类型
        public static final int MSG_TAO_CAN_BAO_MING = 429;//套餐报名
        public static final int MSG_ATTENTION_USER_FOCUS = 430;//我的主页关注
        public static final int MSG_FANS_USER_FOCUS = 431;//我的主页粉丝关注
        public static final int MSG_PIC_ADD_CANNEL_COLLECTION = 432;// 添加取消收藏图片收藏
        public static final int MSG_LOGIN_IS_USER_REGIST = 433;//根据手机号码或者邮箱判断是否注册
        public static final int MSG_GET_INFOR_BY_BUSINESSID = 434;//获取商家折扣信息
        public static final int MSG_GET_PINPAI_ZEKOU_INFO = 435;// 获取品牌折扣信息
        //public static final int MSG_UMENG_PUSH_AGENT = 436; // 关闭或者开启 友盟推送
        public static final int MSG_PUSH_AGENT = 436; // 关闭或者开启 友盟推送

        public static final int MSG_LIKE_SPECIAL = 437; // 喜欢专题
        public static final int MSG_LIKE_PICTURE = 438; // 喜欢图集
        public static final int MSG_MY_LIKE_SPECIAL_LIST = 439; // 喜欢专题列表
        public static final int MSG_MY_LIKE_PICTURE_LIST = 440; // 喜欢图集列表

        public static final int MSG_LIKE_SPECIAL_LIST_UI = 441; // 喜欢专题
        public static final int MSG_LIKE_PICTURE_LIST_UI = 442; // 喜欢图集
    }

    public static final class StatusBar {
        //TODO 状态栏
       /* public static final int BLUE = R.drawable.bg_home_budget_area;
        public static final int ORANGE = R.drawable.bg_status_bar_exceed;*/
    }

    public static final class Network {
        public static final int NETWORK_UNKNOWN = 0;
        public static final int NETWORK_WIFI = 1;
        public static final int NETWORK_2_G = 2;
        public static final int NETWORK_3_G = 3;
        public static final int NETWORK_4_G = 4;
    }

    public static class Http {
        public static final String HTTP = "http";
        public static final String JSON = ".json";

        public static final String POST = "POST";
        public static final String GET = "GET";
        public static final String CONTENT_TYPE = "Content-Type";
        public static final String CONTENT_TYPE_IMAGE_JPG = "image/jpeg";
        public static final String CONTENT_TYPE_IMAGE_PNG = "image/png";
        public static final String CONTENT_TYPE_ZIP = "application/zip";
        public static final String CONTENT_TYPE_OCTET_STREAM = "application/octet-stream";
        public static final String USER_AGENT = "User-Agent";
        public static final String CHARSET = "Charset";
        public static final String CACHE_CONTROL = "Cache-Control";
        public static final String CONNECTION = "Connection";

        // ---------Http Status-----------------
        public static final String STATUS_CODE_OK = "200";
        public static final String STATUS_CODE_NOT_MODIFIED = "304";
        public static final String STATUS_CODE_BAD_REQUEST = "000";

        public static final String RESPONSE_VALUE_OK = "OK";
        public static final String RESPONSE_KEY_MSG = "message";

        public static final int MODE_FORM = 0;
        public static final int MODE_JSON = 1;
    }

    public static final class Time {
        public static final int SECOND = 1000;
        public static final int MINUTE = 1000 * 60;
        public static final int HOUR = MINUTE * 60;
        public static final int DAY = HOUR * 24;
        public static final int YEAR = DAY * 365;
    }


    public static final class Tag {
        public static final String TAG_FROM_BUDGET = "from_budget";
        public static final String TAG_FROM_PROCESS = "from_process";
    }

    public static final class Type {
        public static final int TYPE_LIKE_SPEC_PIC = 2;
        public static final int TYPE_LIKE_BIG_PIC = 3;
        public static final int TYPE_BUDGET_FOR_SALE = 1000;
        public static final int TYPE_BUDGET_HAVE_BUY = 1001;
    }

    public static final class Event {
        public static final String DIAN_JI_LOGIN = "DianJiLogin";
        public static final String LOGIN_BUTTON = "LoginButton";
        public static final String REGISTER_BUTTON = "RegisterButton";

        public static final String LOGIN = "Login_New";
        // public static final String YAN_ZHENG_MA = "YanZhengMa";
        public static final String YAN_ZHENG_MA_RG = "YanZhengMa_Register";//注册时使用的验证

        public static final String YAN_ZHENG_MA_KG = "YanZhengMa_KuaiJie";//快捷登录使用的验证吗

        public static final String LOGIN_SUCCESS = "LoginSuccess_New";//短信快捷登录成功
        public static final String LOGIN_SUCCESS_PU = "LoginSuccess_NewPU";//普通登录成功
        public static final String REGISTER = "Register_OK_New";//注册
        public static final String REGISTER_SUCCESS = "RegisterSuccess";//注册成功
        public static final String DUANXIN_DENGLU = "DuanXin_Login";//短信快捷登录
        public static final String LOGIN_DUANXIN = "Login_Duanxin";//按短信登录的按钮

        public static final String SET_PASSWORD = "SetPassword";

        public static final String PICTURE_APPLICATION_DESIGN = "Picture_ApplicationDesign";
        public static final String SPECIAL_TOPIC_APPLICATION_DESIGN = "SpecialTopic_ApplicationDesign";
        public static final String PICTURE_COLLECTION = "Picture_Collection";
        public static final String SPECIAL_TOPIC_COLLECTION = "SpecialTopic_Collection";
        public static final String YU_SUAN_BIAO_SHU_JU_TONG_BU = "YuSuanBiao_ShuJuTongbu";
        public static final String TUAN_GOU_HUI_BAO_MING = "TuanGouhui_BaoMing";
        //   public static final String FA_BIAO_TIE_ZI = "FaBiaoTieZi";
        public static final String REPLAY_TIE_ZI = "ReplayTieZi";
        public static final String SHARE_TIE_ZI = "ShareTieZi";
        public static final String SHARE_BIG_PIC = "ShareBigPic";
        public static final String SHARE_SP_PIC = "ShareSpPic";
        public static final String COLLECTION_TIE_ZI = "CollectionTieZi";
        public static final String COLLECTION_ARTICLE = "CollectionArticle";
        public static final String ADD_COMMENT_ARTICLE = "AddCommentArticle";
        public static final String DIARY = "Diary";
        public static final String TOU_SU = "TouSu";
        public static final String COMPLAINT_FEEDBAKE = "ComplaintFeedbake";
        public static final String XIU_GAI_LIU_CHENG = "XiuGaiLiuCheng";
        public static final String LIU_CHENG_CHANGE_SUCCESS = "LiuChengChangeSuccess";
        public static final String ZHUANG_XIU_XIN_XI = "ZhuangXiuXinXi";
        public static final String HOUSE_TYPE = "HouseType";
        public static final String MIAN_JI = "MianJi";
        public static final String STYLE = "Style";
        public static final String BUDGET = "Budget";
        public static final String ZHUANG_XIU_XIN_XI_FINISH = "ZhuangXiuXinXiFinish";
        public static final String YE_ZHU_SHUO_FA_BU = "YeZhuShuo_FaBu";
        public static final String FA_TIE_SUCCESS = "FaTieSuccess";
        public static final String FEN_XIANG_XIN_DE_FA_BU = "FenXiangXinDe_Fabu";
        public static final String YEZHUSHUO_FATIE_SUCCESS = "YeZhuShuo_FaTieSuccess";
        public static final String WEI_XIN_ICON = "WeiXinIcon";
        public static final String YU_SUAN_BIAO_KEEP = "YuSuanBiaoKeep";
        public static final String TUAN_GOU_HUI = "TuanGouHui";
        public static final String TU_GOU_HUI_BAO_MING_SUCCESS = "TuGouHuiBaoMingSuccess";
        public static final String APPLICATION_DESIGN_SUCCESS = "ApplicationDesignSuccess";
        public static final String CONTACT_CUSTOMER_SERVICE = "ContactCustomerService";
        public static final String CUSTOMER_SERVICE_SEND = "CustomerServiceSend";
        public static final String LAO_ZHONG_YI_NEXT = "LaoZhongYiNext";

        public static final String MY_JOIN = "Join";
        public static final String SEARCH = "Search";
        public static final String ADD_GUAN_ZHU = "AddGuanZhu";
        public static final String ZAN = "Zan";
        public static final String FIND = "Find";
        public static final String GUANZHU = "FeedGuanZhu";
        public static final String COMMENT_TIE_ZI = "CommentTieZi";

        public static final String REGIST_OK = "Register_OK";
        public static final String WEIXIN_QUE_REN_LOGIN = "WeiXin_QueRenLogin";
        public static final String REGISTER_OK = "Register_OK";
        public static final String WEIXIN_QUEREN_LOGIN = "WeiXin_QueRenLogin";
        public static final String ZHUANGXIU_XUETANG_MORE = "ZhuangXiuXueTang_more";
        public static final String ZHUANGXIU_XUETANG_QIEHUAN_YEMIAN = "ZhuangXiuXueTang_QieHuanYeMIan";
        public static final String SHANGPINXIANGQING = "ShangPinXiangQing";
        public static final String YUYUE_LIANG_LIANGCHI = "YuYueLiangChi";
        public static final String QUEREN_ZHIFU_WEIXIN = "QueRenZhiFu_WeiXin";
        public static final String WEIXIN_ZHIFU_CHENGGONG = "WeiXinZhiFuChengGou";
        public static final String CHA_KAN_HETONG = "ChanKanHeTong";
        public static final String LI_JI_FUKUAN = "LiJiFuKuan";
        public static final String SHOUYINTAI_WEIXIN = "ShouYinTai_WeiXin";
        public static final String SHOUYINTAI_POSJi = "ShouYinTai_POSJi";
        public static final String ER_WEI_MA = "ErWeiMa";
        public static final String SHOUYINTAI_YIFU_KUAN = "ShouYinTai_YiFuKuan";
        public static final String SHOUHUO_YANSHOU = "ShouHuoYanShou";
        public static final String ANZHUANG_YANSHOU = "AnZhuangYanShou";
        public static final String QUXIAO_DINGDAN = "QuXiaoDingDan";
        public static final String MY_COLLECTION = "MyCollection";
        public static final String HOME_WEIZHANG_TUJIAN = "Home_ArticleTuiJian";
        public static final String ZHUANGXIUXUTANG_ARTICLE = "ZhuangXiuXueTang_ArticleTuiJian";
        public static final String MY_COLLCTION_ARTICLE_TUIJIAN = "MyCollectionArticle_ArticleTuiJian";

        public static final String JIAO_DIAN_TU_1 = "JiaoDianTu_1";//焦点图一
        public static final String JIAO_DIAN_TU_2 = "JiaoDianTu_2";
        public static final String JIAO_DIAN_TU_3 = "JiaoDianTu_3";
        public static final String JIAO_DIAN_TU_4 = "JiaoDianTu_4";
        public static final String JIAO_DIAN_TU_5 = "JiaoDianTu_5";
        public static final String QIU_ZHU_LAO_ZHONG_YI = "QiuZhuLaoZhongYi";//求助老中医

        // 1.2.3
        public static final String HOME_XIAO_GUO_TU = "HomePage_XiaoGuoTu";
        public static final String HOME_SCHOOL = "HomePage_ZhuangXiuXueTang";
        public static final String HOME_MY_BUDGET = "HomePage_WoDeYuSuan";
        public static final String HOME_ZHUANG_XIU_XIAN_JING = "HomePage_ZhuangXiuXianJing";
        public static final String HOME_HOT_TOPIC_LIST = "HomePage_ReMenTopic"; // 热门话题
        public static final String SHENQING_MIANFEIJIANLI = "MianFeiJianLi_LiJiShenQing";// 免费监理管家申请
        public static final String SHENQING_MIANFEIYANFANG = "MianFeiYanFang_LiJiShenQing";// 免费验房申请
        public static final String SOCIAL_SHARE = "Social_Share";
        public static final String HOME_FEATURE_QUOTED_PRICE = "home_feature_quoted_price";//免费报价
        public static final String HOME_FEATURE_DESIGN = "home_feature_designer";//找设计师
        public static final String HOME_FEATURE_PICTURE = "home_feature_picture";//效果图
        public static final String HOME_FEATURE_SCHOOL = "home_feature_school";//效果图
        public static final String HOME_FEATURE_MY_BUDGET = "home_feature_my_budget";//效果图
        public static final String HOME_FEATURE_TRAP = "home_feature_trap";//效果图
        public static final String HOME_FEATURE_FENGSHUI = "home_feature_fengshui";//效果图
        public static final String HOME_FEATURE_NEARY_BUSINESS = "home_feature_nearby_business";//效果图
        public static final String HOME_FEATURE_COMPANY = "home_feature_company";//效果图
        public static final String HOME_FEATURE_GROUP = "home_feature_group";//效果图
        public static final String HomePage_FEED = "HomePage_feed";
        public static final String DISCOVERY_WEB_377 = "discovery_web_377";
        public static final String DISCOVERY_WEB_677 = "discovery_web_677";
        public static final String ZiZhuXiaDan = "ZiZhuXiaDan";
        public static final String DianJiPinPai = "DianJiPinPai";
        public static final String QueRenxiaDan = "QueRenxiaDan";
    }

    public static final class Page {
        public static final String JIAO_DIAN_TU2_677 = "JiaoDianTu2_677";
        public static final String HOME_PAGE = "HomePage ";
        public static final String XIU_GAI_ZHUANG_XIU_LIU_CHENG = "XiuGaiZhuangXiuLiuCheng";
        public static final String XIU_GAI_ZHUANG_XIU_XIN_XI = "XiuGaiZhuangXiuXinXi";
        public static final String YU_SUAN_BIAO = "YuSuanBiao";
        public static final String QING_DAN_XIANG_QING = "QingDanXiangQing";
        public static final String FA_BIAO_TIE_ZI = "FaBiaoTieZi";
        public static final String HUI_TIE = "HuiTie";
        public static final String TUAN_GOU_HUI = "TuanGouHui";
        public static final String TUAN_GOU_HUI_XIANGQING = "TuanGouHuiXiangQing";
        public static final String HUO_DONG_XIANG_QING = "HuoDongXiangQing";
        public static final String FU_JIN_SHANG_JIA_LIE_BIAO_YE = "FuJinShangJia_LieBiaoYe";
        public static final String FU_JIN_SHANG_JIA_XIANG_QING_YE = "FuJinShangJia_XiangQingYe";
        public static final String ZHUANG_XIU_XUE_TANG_LIE_BIAO_YE = "ZhuangXiuXueTang_LieBiaoYe";
        public static final String MIAN_FEI_YAN_FANG_LIE_BIAO_YE = "MianFeiYanFang_LieBiaoYe";
        public static final String MIAN_FEI_JIAN_LI_LIE_BIAO_YE = "MianFeiJianLi_LieBiaoYe";
        public static final String ZHUANG_XIU_FENG_SHUI_LIE_BIAO_YE = "ZhuangXiuFengShui_LieBiaoYe";
        public static final String ZHUANG_XIU_XIAN_JING_LIE_BIAO_YE = "ZhuangXiuXianJing_LieBiaoYe";
        public static final String ZHUAN_TI_LIE_BIAO_YE = "ZhuanTi_LieBiaoYe";
        public static final String ZHUAN_TI_XIANG_QING_YE = "ZhuanTi_XiangQingYe";
        public static final String XIAO_GUO_TU_LIE_BIAO_YE = "XiaoGuoTu_LieBiaoYe";
        public static final String XIAO_GUO_TU_XIANG_QING_YE = "XiaoGuoTu_XiangQingYe";
        public static final String YE_ZHU_SHUO_ALL = "YeZhuShuo_All";
        public static final String YE_ZHU_SHUO_SAME_CITY = "YeZhuShuo_SameCity";
        public static final String YE_ZHU_SHUO_DIARY = "YeZhuShuo_Diary";
        public static final String YE_ZHU_SHUO_TOU_SU = "YeZhuShuo_TouSu";
        public static final String YE_ZHU_SHUO_COMPLAINT_FEEDBACK = "YeZhuShuo_ComplaintFeedback";
        public static final String TA_REN_ZHU_YE = "TaRenZhuYe";
        public static final String PERSONAL_CENTER = "PersonalCenter";
        public static final String LOGIN = "Login";
        public static final String PERSONAL_INFORMATION = "PersonalInformation";
        public static final String PERSONAL_PAGE = "PersonalPage";
        public static final String SET = "Set";
        public static final String CUSTOM_SERVICE = "CustomService";
        public static final String ARTICLE = "Article";
        public static final String FEED_DETAIL = "Feed_Detail";
        public static final String TOPIC_DETAIL = "Topic_Detail";
        public static final String TIE_ZI = "TieZi";
        public static final String INFORMATION_CENTER = "InformationCenter";
        public static final String TIAN_JIA_BIAO_QIAN = "TianJiaBiaoQian";

        public static final String HUI_YIQI = "HuiYiQi";
        public static final String SHANG_PIN_XXIANGQINGYE = "ShangPin_XiangQingYe";
        public static final String TIJIAO_YUYUE_DAN = "TiJiaoYuYueDan";
        public static final String WO_DE_DING_DAN = "WoDeDingDan";
        public static final String YEZHUDUAN_DINGDAN_XIANGQING = "YeZhuDuan_DingDanXiangQing";
        public static final String SHOU_YIN_TAI = "ShouYinTai";

        public static final String FIND = "Find";//发现
        public static final String XUE_YUSHUAN_ANLI = "XueYuSuanAnLi";// 学预算案例
        public static final String TAREN_ZHUANGXIU_YUSHUAN = "TaRenYuSuan_XiangQingYe";//他人装修预算
        public static final String FEED_FOUND = "Feed_Find";
        public static final String ZHUANG_XIU_GONG_SI = "ZhuangXiuGongSi";//装修 公司
        public static final String HenPianYi = "HenPianYi";//狠便宜
        public static final String FEED_TOPIC_SQUARE = "FeedTopicSquare";//话题广场

        public static final String COMMUNITY_SPECIAL = "YeZhuShuo_JingXuan";
        public static final String COMMUNITY_SPECIAL_NEW = "YeZhuShuo_JingHua";
        public static final String COMMUNITY_TOPIC_SQUARE = "YeZhuShuo_HuaTiGuangChang";
        public static final String COMMUNITY_INTEREST = "YeZhuShuo_GuanZhu";
        public static final String COMMUNITY_TALENT_MORE = "YeZhuShuoDaRen_More";
        public static final String YeZhuShuo_ZuiXin = "YeZhuShuo_ZuiXin"; //业主说 最新
        public static final String YeZhuShuo_Block = "YeZhuShuo_BanKuai"; //业主说 版块
        public static final String YeZhuShuo_BlockDetail = "YeZhuShuo_BanKuaiXiangQing"; //业主说 版块详情
        public static final String YeZhuShuo_BlockDetail_ZhuLunTan = "BanKuaiXiangQing_ZhuangXiuZhuLunTan"; //业主说 版块详情-装修主论坛
        public static final String YeZhuShuo_BlockDetail_XuanGou = "BanKuaiXiangQing_ZhuangXiuXuanGou"; //业主说 版块详情-装修选购
        public static final String YeZhuShuo_BlockDetail_PinPaiReYi = "BanKuaiXiangQing_PinPaiReYi"; //业主说 版块详情-装修选购
        public static final String MianFeiBaoJia = "MianFeiBaoJia"; //免费报价
        public static final String ZhaoSheJishi = "ZhaoSheJishi"; //找设计师
        public static final String Find_677 = "Find_677";
        public static final String Find_377 = "Find_377";

        public static final String ZiZhuXiaDan = "ZiZhuXiaDan";

//        public static  final  String  TOPIC_DETAIL="Topic_Detail_Page";//话题详情页
        //  public  static  final  String  FEED_DETAIL="Feed_Detail_Page";//帖子详情页

    }


    public static class OrderState {
        public final static int PENDING_PRE_ORDER_PAY = 10;//已提交预约单
        public final static int PENDING_CONFIRM_MEASURE_TIME = 20;//待确认量尺时间
        public final static int PENDING_VISIT_MEASURE_TIME = 30;//待上门量尺
        public final static int PENDING_PICTURE = 40;//待出图
        public final static int PENDING_INTO_THE_STORE = 50;//待进店
        public final static int PENDING_RE_MEASURE = 60;//待复尺
        public final static int PENDING_RE_MEASURE_REGISTER = 70;//待复尺签到
        public final static int PENDING_RE_MEASURE_PICTURE = 80;//待复尺出图
        public final static int PENDING_RE_MEASURE_INTO_THE_STORE = 90;//待复尺进店
        public final static int PENDING_ZHUAN_ORDER = 95;//转单
        public final static int PENDING_CONTRACT = 100;//待定合同款
        public final static int PENDING_PAY = 110;//待付款
        public final static int PENDING_PRODUCE = 120;//待安排生产
        public final static int PENDING_DELIVERY_REGISTER = 130;//待送货签到
        public final static int PENDING_DELIVERY_CONFIRM = 140;//待送货验收
        public final static int PENDING_INSTALL_REGISTER = 150;//待安装签到
        public final static int PENDING_INSTALL_CONFIRM = 160;//待安装验收
        public final static int PENDING_FINISH = 170; //已完成
        public final static int PENDING_CANCEL = 180;//已取消

    }


    public static class FeedEventType{
        public final static int FEED_TYPE_CREATE = 1000; // 创建贴子
        public final static int FEED_TYPE_TOPICS = 1001; // 保存话题
    }

    public static class Permission{
        public final static int PERMISSION_READ_PHONE_STATE = 1000;
    }

    public enum PostFeedType{
        // 0 回复、1 普通、2 晒单、3 装修日记、4 投诉、 5 品类选购、 6 品牌热议
        FEED_TYPE_REPLY,
        FEED_TYPE_COMMON,
        FEED_TYPE_SHAIDAN,
        FEED_TYPE_RIJI,
        FEED_TYPE_TOUSU,
        FEED_TYPE_PINLEI,
        FEED_TYPE_PINPAI,
        FEED_TYPE_PINGJIA;
    }

    public static class ActivityRequest{

    }
}
