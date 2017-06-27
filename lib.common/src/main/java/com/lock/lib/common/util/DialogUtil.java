package com.lock.lib.common.util;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.text.SpannableString;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.lock.lib.common.constants.Constants;

/**
 * Created by hubing on 16/3/22.
 */
public class DialogUtil {
    private DialogUtil(){}

    public static AlertDialog showDialog(Activity activity, int dialogLayout,int clickRes, final OnDialogClickListener clickListener){
        final AlertDialog dialog = new AlertDialog.Builder(activity).create();
        Window window = dialog.getWindow();
        window.setBackgroundDrawableResource(android.R.color.transparent);
        dialog.show();
        dialog.setContentView(dialogLayout);
        dialog.getWindow().findViewById(clickRes).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickListener.onConfirmClick();
                dialog.dismiss();
            }
        });

        return dialog;
    }

    public static AlertDialog showDialog(Activity activity, int dialogLayout){
        final AlertDialog dialog = new AlertDialog.Builder(activity).create();
        Window window = dialog.getWindow();
        window.setBackgroundDrawableResource(android.R.color.transparent);
        dialog.show();
        dialog.setContentView(dialogLayout);
        return dialog;
    }

    public static Dialog showCommonDialog(Activity activity, int dialogLayout,int style){
        final Dialog dialog = new Dialog(activity,style);
        dialog.setContentView(dialogLayout);
        WindowManager windowManager = activity.getWindowManager();
        Window window = dialog.getWindow();
        window.setBackgroundDrawableResource(android.R.color.transparent);
        Display display = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.width = (int)(display.getWidth()) - 2 * AppUtil.dip2px(activity,15); //设置宽度
        window.setAttributes(lp);
        dialog.show();


        return dialog;
    }


    public interface OnDialogClickListener{
        public void onConfirmClick();
    }

    public static AlertDialog showEditDialog(final Context context) {
        final EditText et = new EditText(context);

        return new AlertDialog.Builder(context).setTitle("Edit")
                .setIcon(android.R.drawable.ic_dialog_info)
                .setView(et)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        String input = et.getText().toString();
                        if (input.equals("")) {
                            Toast.makeText(context, "Please enter device name!" + input, Toast.LENGTH_LONG).show();
                        } else {

                        }
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }
}
