package com.lock.lib.base.discuz.sku;

import android.content.Context;

import com.lock.lib.base.BaseManager;
import com.lock.lib.base.Server;
import com.lock.lib.base.VolleyManager;
import com.lock.lib.base.event.ResponseEvent;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by hubing on 16/5/27.
 */
public class SkuManager extends BaseManager implements ISkuManager {

    private static Object clockObj = new Object();
    private static SkuManager mInstance;
    private SkuManager(Context context) {
        super(context);
    }

    public static SkuManager getInstance(Context context){
        synchronized (clockObj){
            if(mInstance == null){
                mInstance = new SkuManager(context);
            }
            return mInstance;
        }
    }

    @Override
    public void getCategoryList(Context context, HashMap<String, String> params) {
        if(params==null){
            params = new HashMap<String, String>();
        }
        params.put("m","misc");
        params.put("a","product");

        get(context, formatMapParamToString(context, params), new VolleyManager.ServerResponse<String>() {
            @Override
            public void onSuccess(String result) {
                resolveJson(result, new OnRepSuccessListener() {
                    @Override
                    public void onJsonArraySuccess(JSONArray jsonArray) {
                        super.onJsonArraySuccess(jsonArray);
                        ArrayList<SkuBean.CategoryItem> categoryItems = new ArrayList<SkuBean.CategoryItem>();
                        if(jsonArray!=null){
                            int len = jsonArray.length();
                            for(int i=0;i<len;i++){
                                JSONObject categoryItemJson = jsonArray.optJSONObject(i);
                                if(categoryItemJson!=null){
                                    SkuBean.CategoryItem categoryParentItem = new SkuBean.CategoryItem();
                                    String title = categoryItemJson.optString("title");
                                    String icon = categoryItemJson.optString("icon");
                                    categoryParentItem.title = title;
                                    categoryParentItem.icon = icon;
                                    categoryParentItem.isParent = true;
                                    categoryItems.add(categoryParentItem);
                                    JSONArray childItemJson = categoryItemJson.optJSONArray("children");
                                    if(childItemJson!=null){
                                        int itemLen = childItemJson.length();
                                        for(int j=0; j < itemLen;j++){
                                            JSONObject itemJson = childItemJson.optJSONObject(j);
                                            if(itemJson!=null){
                                                String id = itemJson.optString("id");
                                                String itemTitle = itemJson.optString("title");
                                                String itemIcon = itemJson.optString("icon");
                                                String threadsnum = itemJson.optString("threadsnum");
                                                int postsnum = itemJson.optInt("postsnum");

                                                SkuBean.CategoryItem categoryItem = new SkuBean.CategoryItem();
                                                categoryItem.id = id;
                                                categoryItem.icon = itemIcon;
                                                categoryItem.title = itemTitle;
                                                categoryItems.add(categoryItem);
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        postEvent(ResponseEvent.TYPE_SERVER_CATEGORY_LIST,categoryItems, Server.Code.SUCCESS,null);
                    }

                    @Override
                    public void onFail(int errorCode, String errorMsg) {
                        postEvent(ResponseEvent.TYPE_SERVER_CATEGORY_LIST,null, errorCode,errorMsg);
                    }
                });
            }

            @Override
            public void onError(int errorCode, String errorMsg) {
                postEvent(ResponseEvent.TYPE_SERVER_CATEGORY_LIST,null, errorCode,errorMsg);
            }
        });

    }
}
