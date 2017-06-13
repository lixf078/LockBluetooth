package com.aiyiqi.lib.api.base;/**
 * Created by cpoopc on 2015/8/28.
 */

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.aiyiqi.lib.api.R;
import com.aiyiqi.lib.api.Server;
import com.aiyiqi.lib.api.event.RequestEvent;
import com.aiyiqi.lib.api.event.ResponseEvent;
import com.aiyiqi.lib.common.constants.Constants;
import com.aiyiqi.lib.common.util.Logger;
import com.aiyiqi.lib.common.util.NetUtil;
import com.aiyiqi.lib.common.util.PermissionUtil;
import com.aiyiqi.lib.common.util.ShareUtil;
import com.aiyiqi.lib.common.util.ToastUtil;
import com.aiyiqi.lib.share.SocialShareUtil;
import com.aiyiqi.lib.style.view.DrawableCenterTextView;
import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.HashMap;

import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;
import in.srain.cube.views.scrolllayout.ScrollAbleFragment;
import in.srain.cube.views.scrolllayout.ScrollableHelper;

/**
 * User: cpoopc
 * Date: 2015-08-28
 * Time: 11:45
 * Ver.: 0.1
 */
public abstract class BaseScrollAbleFragment extends ScrollAbleFragment {
    public View mHeadLayout;
    public TextView mHeadLeftView;
    public TextView mHeadMiddleView;
    public TextView mHeadRightView;

    protected TextView mFootView;
    public ViewGroup mContentView;

    public View mLoadingLayout;
    public View mNoDataLayout;
    public TextView mNoDataTextView;
    public View mNoNetLayout;
    public View mNoAttentionLayout;
    public View mNoFansLayout;

    public View mLoadingView;
    public View mNoDataView;
    public View mNoNetView;
    public View mNoAttentionView;
    public View mNoFansView;

    private DrawableCenterTextView mRefreshView;
    protected String noMoreMsg,noMsg;
    protected LayoutInflater mLayoutInflater;

    protected String cityName = "";
    protected int cityId = Constants.INVALID;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        EventBus.getDefault().register(this);
        Logger.e(Constants.TAG, "BaseFragment ---> onCreateView");

        mLayoutInflater = inflater;
        View contentView = inflater.inflate(R.layout.lib_base_scrollable_fragment_base,null,false);
        noMoreMsg = noMsg = getResources().getString(R.string.lib_style_no_more);
        initHeadView(contentView,savedInstanceState);
        loadData();
        return contentView;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
    }

    protected abstract View createContentView(LayoutInflater inflater, Bundle savedInstanceState);

    protected abstract void onEventResponse(ResponseEvent event);

    protected RequestEvent onPostEventData(RequestEvent requestEvent){
        return null;
    };


    private void loadData(){
        checkPermission(new String[]{Manifest.permission.READ_PHONE_STATE}, new PermissionAction() {
            @Override
            public void done(boolean confirm) {
                RequestEvent requestEvent = new RequestEvent();
                requestEvent = onPostEventData(requestEvent);
                if(requestEvent!=null) {
                    HashMap<String,String> map = null;
                    Logger.e(Constants.TAG,"loadData >> checkPermission >> requestEvent : "+requestEvent);
                    if(requestEvent.eventMap == null){
                        map = new HashMap<String, String>();
                    }else{
                        map = requestEvent.eventMap;
                    }
                    map.put(Server.Param.PERMISSION,confirm ? Server.Permission.PERMISSION_YES : Server.Permission.PERMISSION_NO);
                    requestEvent.eventMap = map;
                    if (NetUtil.checkNetwork(getActivity())) {
                        showLoadingView();
                        postEvent(requestEvent);
                    } else {
                        showNoNetVew(new OnNoNetRefreshListener() {
                            @Override
                            public void onNoNetRefresh() {
                                loadData();
                            }
                        });
                    }
                }
            }
        });

    }

    protected void postEvent(RequestEvent requestEvent){
        EventBus.getDefault().post(requestEvent);
    }
    @Subscribe
    public void onEvent(ResponseEvent event){
        if(event!=null){
            onEventResponse(event);
        }
    }

    protected void setCanSrollView(View scrollView,OnPullRefreshListener onPullRefreshListener){
    }

    protected void setHeadLayoutVisiable(int visiable){
        if(mHeadLayout!=null){
            mHeadLayout.setVisibility(visiable);
        }
    }

    protected TextView getHeadLeftView() {
        return mHeadLeftView;
    }

    protected TextView getHeadMiddleView() {
        return mHeadMiddleView;
    }

    protected TextView getHeadRightView() {
        return mHeadRightView;
    }

    protected TextView getFootView(){
        return mFootView;
    }

    protected void showLoadingView(){
        /*if(mLoadingView == null){
            mLoadingView = mLoadingStub.inflate();
        }*/
        mLoadingLayout.setVisibility(View.VISIBLE);
    }

    protected void hiddenLoadingView(){
        if(mLoadingLayout!=null && mLoadingLayout.getVisibility()==View.VISIBLE){
            mLoadingLayout.setVisibility(View.GONE);
        }
    }

    protected void showNoDataVew(){
        /*if(mNoDataView == null){
            mNoDataView = mNoDataStub.inflate();
        }*/
        mNoDataLayout.setVisibility(View.VISIBLE);
    }
    protected void showNoDataVew(String emptyText){
        /*if(mNoDataView == null){
            mNoDataView = mNoDataStub.inflate();
        }*/
        mNoDataTextView = (TextView) mNoDataLayout.findViewById(R.id.empty_text_des);
        mNoDataTextView.setText(emptyText);
        mNoDataLayout.setVisibility(View.VISIBLE);
    }

    protected void hiddenNoDataView(){
        if(mNoDataLayout!=null && mNoDataLayout.getVisibility()==View.VISIBLE){
            mNoDataLayout.setVisibility(View.GONE);
        }
    }

    protected void showNoNetVew(final OnNoNetRefreshListener listener){
        /*if(mNoNetView == null){
            mNoNetView = mNoNetStub.inflate();
        }*/
        mNoNetLayout.setVisibility(View.VISIBLE);
        mRefreshView = (DrawableCenterTextView) mNoNetLayout.findViewById(R.id.refresh);
        mRefreshView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (NetUtil.isNetworkAvailable(getActivity())) {
                    if (listener != null) {
                        listener.onNoNetRefresh();
                        ;
                    }
                } else {
                    ToastUtil.showToast(getActivity(), "暂无网络，请稍后重试");
                }
            }
        });
    }

    protected void hiddenNoNetView(){
        if(mNoNetLayout!=null && mNoNetLayout.getVisibility()==View.VISIBLE){
            mNoNetLayout.setVisibility(View.GONE);
        }
    }

    public void initHeadView(View contentView,Bundle savedInstanceState){
        mHeadLayout = contentView.findViewById(R.id.head_layout);
        mHeadLeftView = (TextView) contentView.findViewById(R.id.head_left_view);
        mHeadMiddleView = (TextView) contentView.findViewById(R.id.head_middel_view);
        mHeadRightView = (TextView) contentView.findViewById(R.id.head_right_view);

        mFootView = (TextView) contentView.findViewById(R.id.foot_view);

        mContentView = (ViewGroup) contentView.findViewById(R.id.content_layout);

        mLoadingLayout =  contentView.findViewById(R.id.stub_loading);
        mNoDataLayout =  contentView.findViewById(R.id.stub_no_data);
        mNoNetLayout =  contentView.findViewById(R.id.stub_no_net);

        mNoAttentionLayout = contentView.findViewById(R.id.stub_no_attention);
        mNoFansLayout = contentView.findViewById(R.id.stub_no_fans);

        mLoadingLayout.setVisibility(View.GONE);
        mNoDataLayout.setVisibility(View.GONE);
        mNoNetLayout.setVisibility(View.GONE);
        mNoAttentionLayout.setVisibility(View.GONE);
        mNoFansLayout.setVisibility(View.GONE);

        mContentView.addView(createContentView(mLayoutInflater,savedInstanceState));
    }


    protected void resolveError(int errorCode,String errorMsg){

        Logger.e(Constants.TAG, "resolveError >> errorCode : " + errorCode + " ;  errorMsg : " + errorMsg);

        if(errorCode == 110002){
            Intent intent = new Intent();
            intent.setClassName(getActivity(),"com.aiyiqi.galaxy.login.activity.LoginActivity");
            startActivity(intent);
            getActivity().finish();
        }else{
            ToastUtil.showToast(getActivity(), errorMsg);
        }
    }

    public interface OnPullRefreshListener{
        public void onPullRefresh();
    }

    public interface OnNoNetRefreshListener{
        public void onNoNetRefresh();
    }

    protected void checkPermission(String[] permissions, final PermissionAction action){
        boolean hasPermisssions = PermissionUtil.hasAllPermission(getActivity(), permissions);
        PermissionUtil.getInstance().requestPermissionsIfNecessaryForResult(getActivity(), permissions, new PermissionUtil.PermissionListener() {
            @Override
            public void onGranted() {
                if(action!=null){
                    action.done(true);
                }
            }
            @Override
            public void onDenied(String permission) {
                if(action!=null){
                    action.done(false);
                }

            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        PermissionUtil.notifyPermissionsChange(permissions,grantResults);
    }

    protected  interface PermissionAction{
        public void done(boolean confirm);
    }

}
