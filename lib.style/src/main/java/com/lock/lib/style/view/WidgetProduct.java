package com.lock.lib.style.view;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;

import com.lock.lib.style.R;

/**
 * Created by lixufeng on 16/3/28.
 * Class desc 创建公用组件
 */
public class WidgetProduct {

    public static Dialog getBottomDialog(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.common_bottom_sheet, null);
        Dialog dialog = new Dialog(context, R.style.selectorDialog);
        dialog.setContentView(view);
        dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            public boolean onKey(DialogInterface dialog, int keyCode,
                                 KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_MENU)// 监听按键
                    dialog.dismiss();
                return false;
            }
        });
        Window win = dialog.getWindow();
        win.setGravity(Gravity.BOTTOM);
        return dialog;
    }
}
