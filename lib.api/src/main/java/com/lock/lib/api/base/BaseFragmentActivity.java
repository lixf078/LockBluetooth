package com.lock.lib.api.base;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.TextView;

import com.lock.lib.api.R;
import com.lock.lib.api.Server;
import com.lock.lib.api.event.RequestEvent;
import com.lock.lib.api.event.ResponseEvent;
import com.lock.lib.common.constants.Constants;
import com.lock.lib.common.util.ImgUtil;
import com.lock.lib.common.util.Logger;
import com.lock.lib.common.util.NetUtil;
import com.lock.lib.common.util.PermissionUtil;
import com.lock.lib.common.util.ToastUtil;
import com.lock.lib.style.view.DrawableCenterTextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.File;

import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;

/**
 * Created by hubing on 16/4/12.
 */
public abstract class BaseFragmentActivity extends FragmentActivity {

    public View mHeadLayout;
    public TextView mHeadLeftView;
    public TextView mHeadMiddleView;
    public TextView mHeadRightView;
    public ViewGroup mContentView;

    public ViewStub mLoadingStub;
    public ViewStub mNoDataStub;
    public ViewStub mNoNetStub;
    public ViewStub mNoAttentionStub;
    public ViewStub mNoFansStub;

    public View mLoadingView;
    public View mNoDataView;
    public View mNoNetView;
    public View mNoAttentionView;
    public View mNoFansView;


    private DrawableCenterTextView mRefreshView;
    private PtrClassicFrameLayout mPtrFrame;
    private View mCanSrollView;
    private OnPullRefreshListener mPullRefreshListener;
    protected String noMoreMsg,noMsg;
    protected LayoutInflater mLayoutInflater;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        this.setContentView(R.layout.lib_base_activity_base);
        mLayoutInflater = LayoutInflater.from(this);
        noMoreMsg = noMsg = getResources().getString(R.string.lib_style_no_more);
        initBaseView();
        loadData();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    protected abstract View createContentView(LayoutInflater layoutInflater);

    protected abstract void onEventResponse(int eventType,Object value);

    protected RequestEvent onPostEventData(RequestEvent requestEvent){
        return null;
    };

    private void loadData(){
        RequestEvent requestEvent = new RequestEvent();
        requestEvent = onPostEventData(requestEvent);
        if(requestEvent!=null) {
            if (NetUtil.checkNetwork(this)) {
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

    protected void postEvent(RequestEvent requestEvent){
        EventBus.getDefault().post(requestEvent);
    }

    @Subscribe
    public void onEvent(ResponseEvent event){
        Logger.e(Constants.TAG, "BaseActivity onEvent");
        hiddenLoadingView();
        hiddenNoDataView();
        hiddenNoNetView();
        if(event!=null){
            if(event.errorCode == Server.Code.SUCCESS){
                onEventResponse(event.eventType,event.resultObj);
            }else{
                resolveError(event.errorCode,event.errorMsg);
            }
        }
    }

    private void initBaseView(){
        mHeadLayout = this.findViewById(R.id.head_layout);
        mHeadLeftView = (TextView) this.findViewById(R.id.head_left_view);
        mHeadLeftView.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mHeadMiddleView = (TextView) this.findViewById(R.id.head_middel_view);
        mHeadRightView = (TextView) this.findViewById(R.id.head_right_view);
        mContentView = (ViewGroup) this.findViewById(R.id.content_layout);

        mLoadingStub = (ViewStub) this.findViewById(R.id.stub_loading);
        mNoDataStub = (ViewStub) this.findViewById(R.id.stub_no_data);
        mNoNetStub = (ViewStub) this.findViewById(R.id.stub_no_net);

        mNoAttentionStub = (ViewStub) this.findViewById(R.id.stub_no_attention);
        mNoFansStub = (ViewStub) this.findViewById(R.id.stub_no_fans);



        mPtrFrame = (PtrClassicFrameLayout) this.findViewById(R.id.rotate_header_frg_order);
        mPtrFrame.setLastUpdateTimeRelateObject(this);
        mPtrFrame.setPtrHandler(new PtrHandler() {
            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                if(mCanSrollView == null){
                    return false;
                }
                return PtrDefaultHandler.checkContentCanBePulledDown(frame, mCanSrollView, header);
            }

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                if(NetUtil.isNetworkAvailable(BaseFragmentActivity.this)){
                    if(mPullRefreshListener!=null){
                        mPullRefreshListener.onPullRefresh();
                    }
                }else{
                    ToastUtil.showToast(BaseFragmentActivity.this, "暂无网络，请稍后重试");
                }
                mPtrFrame.refreshComplete();
            }
        });
        mPtrFrame.setResistance(1.7f);
        mPtrFrame.setRatioOfHeaderHeightToRefresh(1.2f);
        mPtrFrame.setDurationToClose(200);
        mPtrFrame.setDurationToCloseHeader(1000);
        mPtrFrame.setPullToRefresh(false);
        View view = createContentView(mLayoutInflater);
        if(view != null){
            mContentView.addView(view);
        }

    }

    protected void setCanScrollView(View scrollView, OnPullRefreshListener onPullRefreshListener){
        this.mCanSrollView = scrollView;
        this.mPullRefreshListener = onPullRefreshListener;
    }

    protected void setHeadLayoutVisiable(int visiable){
        if(mHeadLayout!=null){
            mHeadLayout.setVisibility(visiable);
        }
    }

    public View getHeadLayout() {
        return mHeadLayout;
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

    protected void showLoadingView(){
        if(mLoadingView == null){
            mLoadingView = mLoadingStub.inflate();
        }
        mLoadingView.setVisibility(View.VISIBLE);
    }

    protected void hiddenLoadingView(){
        if(mLoadingView!=null && mLoadingView.getVisibility()== View.VISIBLE){
            mLoadingView.setVisibility(View.GONE);
        }
    }

    protected void showNoDataVew(){
        if(mNoDataView == null){
            mNoDataView = mNoDataStub.inflate();
        }
        mNoDataView.setVisibility(View.VISIBLE);
    }

    protected void hiddenNoDataView(){
        if(mNoDataView!=null && mNoDataView.getVisibility()== View.VISIBLE){
            mNoDataView.setVisibility(View.GONE);
        }
    }

    protected void showNoNetVew(final OnNoNetRefreshListener listener){
        if(mNoNetView == null){
            mNoNetView = mNoNetStub.inflate();
        }
        mNoNetView.setVisibility(View.VISIBLE);
        mRefreshView = (DrawableCenterTextView) mNoNetView.findViewById(R.id.refresh);
        mRefreshView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hiddenNoNetView();
                if (NetUtil.isNetworkAvailable(BaseFragmentActivity.this)) {
                    if(listener!=null){
                        listener.onNoNetRefresh();;
                    }
                } else {
                    ToastUtil.showToast(BaseFragmentActivity.this, "暂无网络，请稍后重试");
                }
            }
        });
    }

    protected void hiddenNoNetView(){
        if(mNoNetView!=null && mNoNetView.getVisibility()== View.VISIBLE){
            mNoNetView.setVisibility(View.GONE);
        }
    }

    protected void showNoAttentionVew(){
        if(mNoAttentionStub == null){
            mNoAttentionView = mNoAttentionStub.inflate();
        }
        mNoAttentionView.setVisibility(View.VISIBLE);
    }

    protected void hiddenNoAttentionView(){
        if(mNoAttentionView!=null && mNoAttentionView.getVisibility()== View.VISIBLE){
            mNoAttentionView.setVisibility(View.GONE);
        }
    }

    protected void showNoFansVew(){
        if(mNoFansStub == null){
            mNoFansView = mNoFansStub.inflate();
        }
        mNoFansView.setVisibility(View.VISIBLE);
    }

    protected void hiddenNoFansView(){
        if(mNoFansView!=null && mNoFansView.getVisibility()== View.VISIBLE){
            mNoFansView.setVisibility(View.GONE);
        }
    }

    protected void resolveError(int errorCode,String errorMsg){
        if(errorCode == 3){
            Intent intent = new Intent();
            intent.setClassName(this,"com.lock.galaxy.login.activity.LoginActivity");
            startActivity(intent);
            finish();
        }else{
            ToastUtil.showToast(this, errorMsg);
        }
    }

    protected  int getScreenWidth(){
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        return dm.widthPixels;
    }

    protected int getScreenHeight(){
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        return dm.heightPixels;
    }

    protected float getDensity(){
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        return dm.density;
    }

    public interface OnPullRefreshListener{
        public void onPullRefresh();
    }

    public interface OnNoNetRefreshListener{
        public void onNoNetRefresh();
    }

    protected void checkPermission(String[] permissions, final PermissionAction action){
        PermissionUtil.getInstance().requestPermissionsIfNecessaryForResult(this, permissions, new PermissionUtil.PermissionListener() {
            @Override
            public void onGranted() {
                if(action!=null){
                    action.done(true);
                }
            }

            @Override
            public void onDenied(String permission) {
                action.done(false);
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
