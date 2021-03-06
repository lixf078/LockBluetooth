package com.lock.bluetooth.le.view;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.lock.bluetooth.le.R;
import com.lock.lib.common.util.ToastUtil;

/**
 * Created by admin on 2017/6/26.
 */

public class WarningDialog extends Dialog implements View.OnClickListener {

    private View mView;
    private Context mContext;
    private String mContent;

    private LinearLayout mBgLl;
    private TextView mTitleTv;
    private TextView mMsgEt;
    private TextView mNegBtn;
    private TextView mPosBtn;


    public WarningDialog(Context context, String content) {
        this(context, 0, null, content);
    }

    public WarningDialog(Context context, int theme, View contentView, String content) {
        super(context, theme == 0 ? R.style.MyDialogStyle : theme);
        mContent = content;
        this.mView = contentView;
        this.mContext = context;

        if (mView == null) {
            mView = View.inflate(mContext, R.layout.view_show_warning, null);
        }

        init();
        initView();
        initData();
        initListener();

    }

    private void init() {
        this.setContentView(mView);
    }

    private void initView() {
        mBgLl = (LinearLayout) mView.findViewById(R.id.lLayout_bg);
        mTitleTv = (TextView) mView.findViewById(R.id.txt_title);
        mMsgEt = (TextView) mView.findViewById(R.id.et_msg);
        mNegBtn = (TextView) mView.findViewById(R.id.btn_neg);
        mPosBtn = (TextView) mView.findViewById(R.id.btn_pos);
    }

    private void initData() {
        //设置背景是屏幕的0.85
        mBgLl.setLayoutParams(new FrameLayout.LayoutParams((int) (getMobileWidth(mContext) * 0.85), LayoutParams.WRAP_CONTENT));
//        mMsgEt.setHint(mContent);
        if (!TextUtils.isEmpty(mContent)){
            mMsgEt.setText(mContent);
        }
    }

    private void initListener() {
        mNegBtn.setOnClickListener(this);
        mPosBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_neg:	//取消,
                if(onPosNegClickListener != null) {
                    String mEtValue = mMsgEt.getText().toString();
                    if(!TextUtils.isEmpty(mEtValue)) {
                        onPosNegClickListener.negCliclListener(mEtValue);
                    }
                }
                this.dismiss();
                break;

            case R.id.btn_pos:	//确认
                if(onPosNegClickListener != null) {
                    String mEtValue = mMsgEt.getText().toString();
                    if(!TextUtils.isEmpty(mEtValue)) {
                        onPosNegClickListener.posClickListener(mEtValue);
                    }else{
                        ToastUtil.showToast(getContext(), "Please enter device name!");
                    }
                }
                this.dismiss();
                break;
        }
    }

    private OnPosNegClickListener onPosNegClickListener;

    public void setOnPosNegClickListener (OnPosNegClickListener onPosNegClickListener) {
        this.onPosNegClickListener = onPosNegClickListener;
    }

    public interface OnPosNegClickListener {
        void posClickListener(String value);
        void negCliclListener(String value);
    }



    /**
     * 工具类
     * @param context
     * @return
     */
    public static int getMobileWidth(Context context) {
        DisplayMetrics dm = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels; // 得到宽度
        return width;
    }
}