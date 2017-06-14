package com.example.bluetooth.le.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import com.example.bluetooth.le.R;
import com.lock.lib.api.base.BaseFragment;
import com.lock.lib.api.event.ResponseEvent;

/**
 * Created by admin on 2017/6/14.
 */

public class SettingFragment extends BaseFragment{


    @Override
    protected View createContentView(LayoutInflater inflater, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, null);
//        initContentView(view);
        return view;
    }

    @Override
    protected void onEventResponse(ResponseEvent event) {

    }

}
