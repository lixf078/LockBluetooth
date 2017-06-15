package com.example.bluetooth.le.fragment;

import android.os.Bundle;
import android.support.v4.app.FragmentTabHost;
import android.view.LayoutInflater;
import android.view.View;

import com.example.bluetooth.le.R;
import com.lock.lib.api.base.BaseFragment;
import com.lock.lib.api.event.ResponseEvent;

/**
 * Created by admin on 2017/6/14.
 */

public class SmartCoolFragment extends BaseFragment{

    @Override
    protected View createContentView(LayoutInflater inflater, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, null);
        initContentView(view);
        return view;
    }

    @Override
    protected void onEventResponse(ResponseEvent event) {

    }

    private void initContentView(View view){
        setHeadLayoutVisiable(View.VISIBLE);
        getHeadMiddelView().setText("Home");
        getHeadLeftView().setVisibility(View.GONE);
    }

}
