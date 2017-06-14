package com.lock.lib.api.event;


/**
 * Created by hubing on 16/3/23.
 */
public class ServerEvent extends BaseEvent {
    /*public static final int TYPE_SERVER_USER_LOGIN = 1000;//登录
    public static final int TYPE_SERVER_USER_LOGOUT = TYPE_SERVER_USER_LOGIN + 1;//登出*/
    public static final int TYPE_SERVER_GET_FEED_DIGEST_LIST = 2000;// 获取精选帖子列表
    public static final int TYPE_SERVER_GET_FEED_DIGEST_LIST_BY_TAG = TYPE_SERVER_GET_FEED_DIGEST_LIST + 1 ;;// 根据标签获取精选帖列表
    public static final int TYPE_SERVER_GET_FEED_DETAIL = TYPE_SERVER_GET_FEED_DIGEST_LIST_BY_TAG + 1 ;// 获取帖子详情
    public static final int TYPE_SERVER_GET_FEED_REPLY_DETAIL = TYPE_SERVER_GET_FEED_DETAIL + 1 ;// 获取回帖详情

    public static final int TYPE_SERVER_GET_FEED_LATEST_LIST = TYPE_SERVER_GET_FEED_REPLY_DETAIL + 1 ;// 获取最新帖子列表
    public static final int TYPE_SERVER_GET_FEED_BLOCK = TYPE_SERVER_GET_FEED_LATEST_LIST + 1 ;// 获取业主说 - 板块
    public static final int TYPE_SERVER_GET_FEED_SPECIAL_BLOCK = TYPE_SERVER_GET_FEED_BLOCK + 1 ;// 获取 某一具体版本 详情
    public static final int TYPE_SERVER_GET_FEED_SPECIAL_TAG = TYPE_SERVER_GET_FEED_SPECIAL_BLOCK + 1 ;// 获取 某一具体 品牌或者选购 详情

    public static final int TYPE_SERVER_GET_FEED_LIST_BY_BLOCK = TYPE_SERVER_GET_FEED_SPECIAL_TAG + 1;//根据 板块获取帖子列表
    public static final int TYPE_SERVER_GET_FEED_LIST_BY_TAG = TYPE_SERVER_GET_FEED_LIST_BY_BLOCK + 1;//根据 标签获取帖子列表
    public static final int TYPE_SERVER_GET_APPRAISE_FEED_LIST_BY_TAG = TYPE_SERVER_GET_FEED_LIST_BY_TAG + 1;//根据 标签评价获取帖子列表

    public static final int TYPE_SERVER_GET_COMPLAIN_TAG = TYPE_SERVER_GET_APPRAISE_FEED_LIST_BY_TAG + 1;//获取 投诉的标签
    public static final int TYPE_SERVER_GET_DIARY_TAG = TYPE_SERVER_GET_COMPLAIN_TAG + 1;//获取 装修日记的标签
    public static final int TYPE_SERVER_GET_ORDER_TAG = TYPE_SERVER_GET_DIARY_TAG + 1;//获取 晒单的标签

    public static final int TYPE_SERVER_POST_FEED = TYPE_SERVER_GET_ORDER_TAG+1;//发表帖子
    public static final int TYPE_SERVER_FEED_TYPE = TYPE_SERVER_POST_FEED + 1;// 贴子类型

    public static final int TYPE_SERVER_REPLY_FEED = TYPE_SERVER_FEED_TYPE+1;//回复帖子
    public static final int TYPE_SERVER_FAVORITE_FEED = TYPE_SERVER_REPLY_FEED+1;//收藏帖子
    public static final int TYPE_SERVER_ZAN_FEED = TYPE_SERVER_FAVORITE_FEED+1;//赞帖子

    public static final int TYPE_SERVER_ZAN_USER_LIST = TYPE_SERVER_ZAN_FEED+1;//获取赞帖子的人

    public static final int TYPE_SERVER_UPLOAD_IMG = TYPE_SERVER_ZAN_USER_LIST+1;//上传图片
    public static final int TYPE_SERVER_DEL_IMG = TYPE_SERVER_UPLOAD_IMG+1;//删除图片
    public static final int TYPE_SERVER_SEARCH_FEED = TYPE_SERVER_DEL_IMG + 1; //搜索 feed
    public static final int TYPE_SERVER_SEARCH_USER = TYPE_SERVER_SEARCH_FEED + 1; //搜索 User

    public static final int TYPE_SERVER_GET_USER_INFO = TYPE_SERVER_SEARCH_USER + 1;//获取用户信息
    public static final int TYPE_SERVER_GET_USER_FRIENDS = TYPE_SERVER_GET_USER_INFO + 1;//获取用户friends

    public static final int TYPE_SERVER_GET_USER_FANS = TYPE_SERVER_GET_USER_FRIENDS + 1;//获取用户fans
    public static final int TYPE_SERVER_GET_USER_FOLLOWERS = TYPE_SERVER_GET_USER_FANS + 1;//获取用户followers

    public static final int TYPE_SERVER_FOLLOW_AT_FRIENDS = TYPE_SERVER_GET_USER_FOLLOWERS + 1;// 发跟进时，at人员列表

    public static final int TYPE_SERVER_GET_USER_POST_FEED_LIST = TYPE_SERVER_FOLLOW_AT_FRIENDS + 1;//获取用户发的帖子
    public static final int TYPE_SERVER_GET_USER_FAVORITE_FEED_LIST = TYPE_SERVER_GET_USER_POST_FEED_LIST + 1;//获取用户收藏的帖子
    public static final int TYPE_SERVER_REPORT= TYPE_SERVER_GET_USER_FAVORITE_FEED_LIST + 1;//举报帖子,用户，主题
    public static final int TYPE_SERVER_FOCUSE_USER= TYPE_SERVER_REPORT + 1;//关注某人
    //public static final int TYPE_SERVER_CANCEL_FOCUSE_USER= TYPE_SERVER_FOCUSE_USER + 1;//取消关注某人
    public static final int TYPE_SERVER_CANCEL_FAVORITE= TYPE_SERVER_FOCUSE_USER + 1;//取消收藏
    public static final int TYPE_SERVER_GET_HOME_FEED_LIST= TYPE_SERVER_CANCEL_FAVORITE + 1;//获取首页的feed list

    public static final int TYPE_SERVER_GET_MSG_LIST_BY_TYPE = TYPE_SERVER_GET_HOME_FEED_LIST + 1;//根据消息类型
    public static final int TYPE_SERVER_GET_MSG_TOTAL_COUNT = TYPE_SERVER_GET_MSG_LIST_BY_TYPE + 1;//根据消息总数

    public static final int TYPE_SERVER_SITE_ADD_TRACK = TYPE_SERVER_GET_MSG_TOTAL_COUNT + 1 ; //添加跟进
    public static final int TYPE_SERVER_SITE_ADD_APPRAISE = TYPE_SERVER_SITE_ADD_TRACK + 1 ; //添加工地评价
    public static final int TYPE_SERVER_SITE_LIST_APPRAISE = TYPE_SERVER_SITE_ADD_APPRAISE + 1 ; //工地评价列表
    public static final int TYPE_SERVER_SITE_TRACK_DELETEED = TYPE_SERVER_SITE_LIST_APPRAISE + 1 ; //删除跟进
    public static final int TYPE_SERVER_SITE_FETCH_OSSTOKEN = TYPE_SERVER_SITE_TRACK_DELETEED + 1;// 获取 osstoken

    public static final int TYPE_SERVER_CITY_CHANGED = TYPE_SERVER_SITE_FETCH_OSSTOKEN + 1;// 更改城市信息
    public static final int TYPE_SERVER_ORDER_ADD_REMARK = TYPE_SERVER_CITY_CHANGED + 1;// 订单加备注
    public static final int TYPE_SERVER_SITE_ADD_REMARK = TYPE_SERVER_ORDER_ADD_REMARK + 1;// 跟进加备注
    public static final int TYPE_SERVER_SITE_GET_LATEST_LIST = TYPE_SERVER_SITE_ADD_REMARK + 1;// 获取装修直播10个最新的工地。用于工地直播首页
    public static final int TYPE_SERVER_SITE_GET_LIVE_BUILDING = TYPE_SERVER_SITE_GET_LATEST_LIST + 1;// 获取工地详情，包括工地信息、成员信息、进度状态、成员跟踪信息。用于工地直播页面展示工地详情
    public static final int TYPE_SERVER_SITE_BUILDING_VENDORS = TYPE_SERVER_SITE_GET_LIVE_BUILDING + 1;// 按角色（工长、监理管家、设计师、小秘书等）查看一起团队成员信息，支持分页
    public static final int TYPE_SERVER_SITE_NEARBY_BUILDING = TYPE_SERVER_SITE_BUILDING_VENDORS + 1;// 附近工地
    public static final int TYPE_SERVER_SITE_TRACK_LIST = TYPE_SERVER_SITE_NEARBY_BUILDING + 1;// 工地跟进列表
    public static final int TYPE_SERVER_SITE_GET_BANNER_LIST = TYPE_SERVER_SITE_TRACK_LIST + 1;// 获取整装套餐顶部banner图

    public static final int TYPE_SERVER_GET_HOT_TAGS = TYPE_SERVER_SITE_GET_BANNER_LIST + 1;//  获取热门标签
    public static final int TYPE_SERVER_SEARCH_TAGS = TYPE_SERVER_GET_HOT_TAGS + 1;//  搜索标签
    public static final int TYPE_SERVER_COMPLAINT_SPRAISE_TAG= TYPE_SERVER_SEARCH_TAGS + 1;//  获取投诉或者表扬的标签
    public static final int TYPE_SERVER_CATEGORY_LIST= TYPE_SERVER_COMPLAINT_SPRAISE_TAG + 1;//  获取品类集合

    public static final int TYPE_SERVER_ACIVITY_LIST= TYPE_SERVER_CATEGORY_LIST + 1;//  获取活动帖子列表
    public static final int TYPE_SERVER_VERIFY_CODE = TYPE_SERVER_ACIVITY_LIST + 1; // 获取验证码
    public static final int TYPE_SERVER_JOIN_ACTIVITY = TYPE_SERVER_VERIFY_CODE + 1; // 加入活动

    public static final int TYPE_SERVER_DISCUZ_VERIFY_CODE = TYPE_SERVER_JOIN_ACTIVITY + 1; // 获取Discuz验证码

    // 订单相关...
    public static final int TYPE_SERVER_GET_PINPAI_ZEKOU_INFO= TYPE_SERVER_DISCUZ_VERIFY_CODE + 1;//  获取品牌折扣信息
    public static final int TYPE_SERVER_GET_NEXT_PINPAI_ZEKOU_INFO= TYPE_SERVER_GET_PINPAI_ZEKOU_INFO + 1;//  获取下一场品牌折扣信息
    public static final int TYPE_SERVER_GET_PINPAI_SCORE= TYPE_SERVER_GET_NEXT_PINPAI_ZEKOU_INFO + 1;//  获取品牌评分
    public static final int TYPE_SERVER_GET_NEAR_BY_MERCHANT= TYPE_SERVER_GET_PINPAI_SCORE + 1;//  获取附近商家getNearByMerchant
    public static final int TYPE_SERVER_GET_PRODUCT_ITEM_INFO= TYPE_SERVER_GET_NEAR_BY_MERCHANT + 1;//  获取商品信息
    public static final int TYPE_SERVER_GET_BLOCK_ITEM_INFO= TYPE_SERVER_GET_PRODUCT_ITEM_INFO + 1;//  获取品牌信息（折扣信息，附近商家，好评率）


    public static final int TYPE_SERVER_JOIN_YIQI_ACTION= TYPE_SERVER_GET_BLOCK_ITEM_INFO + 1;//  报名参加一起网活动
    public static final int TYPE_SERVER_GET_YIQI_AD= TYPE_SERVER_JOIN_YIQI_ACTION + 1;//  获取一起网广告信息
    public static final int TYPE_SERVER_EDIT_USER_INFO= TYPE_SERVER_GET_YIQI_AD+ 1;//  更新用户信息


    public static final int TYPE_SERVER_FETCH_POCKET_GIFT_INTEGAL_CODE = TYPE_SERVER_EDIT_USER_INFO+1;// 获取宝钢积分的验证码
    public static final int TYPE_SERVER_PAY_BY_POCKET_GIFT_INTEGER = TYPE_SERVER_FETCH_POCKET_GIFT_INTEGAL_CODE+1; // 宝钢积分+验证码 支付
    public static final int TYPE_SERVER_FETCH_POCKET_GIFT_INTEGAL_CODE_JZGJ = TYPE_SERVER_PAY_BY_POCKET_GIFT_INTEGER+1;// 获取宝钢积分的验证码_通过扫家装管家
    public static final int TYPE_SERVER_PAY_BY_POCKET_GIFT_INTEGER_JZGJ = TYPE_SERVER_FETCH_POCKET_GIFT_INTEGAL_CODE_JZGJ+1; // 宝钢积分+验证码 支付_通过扫家装管家

    public static final int TYPE_SERVER_FEATCH_AVD_CONFIG = TYPE_SERVER_PAY_BY_POCKET_GIFT_INTEGER_JZGJ+1; // 获取一起装修闪屏的配置信息
    public static final int TYPE_SERVER_GET_YIQI_PACKAGE = TYPE_SERVER_FEATCH_AVD_CONFIG+1; // 获取一起装修的套餐
    public static final int TYPE_IMAGE_VERIFY_CODE = TYPE_SERVER_GET_YIQI_PACKAGE+1; // 获取图片验证码
    public static final int TYPE_SMS_VERIFY_CODE = TYPE_IMAGE_VERIFY_CODE + 1;//获取短信验证码
    public static final int TYPE_VERIFY = TYPE_SMS_VERIFY_CODE + 1;//验证短信验证码是否正确
    public static final int TYPE_RESET_PWD = TYPE_VERIFY + 1;//重置密码
    public static final int TYPE_SMS_LOADING = TYPE_RESET_PWD + 1;//短信登陆
    public static final int TYPE_REGIST_USER = TYPE_SMS_LOADING + 1;//注册接口
//    public static final int TYPE_SMS_VERIFY_CODE = TYPE_IMAGE_VERIFY_CODE + 1;//获取短信验证码

}
