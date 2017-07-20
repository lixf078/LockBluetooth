package com.lock.bluetooth.le.permission;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.lock.lib.common.util.DeviceUtil;

import java.util.HashMap;
import java.util.Map;

public class PermissionChecker {

    private static final String TAG = PermissionChecker.class.getSimpleName();

    private Map<Integer, CheckTask> mMap;

    private static PermissionChecker mInst;

    private PermissionChecker() {
        mMap = new HashMap<>();
    }

    public static PermissionChecker inst() {
        if (mInst == null) {
            mInst = new PermissionChecker();
        }
        return mInst;
    }

    public boolean check(CheckTask t) {
        int code = t.getCode();

        mMap.put(code, t);

        OnPermissionListener l = t.getListener();
        Object host = t.getHost();
        String[] ps = t.getPermissions();

        boolean check = false;
        if (host instanceof Activity) {
            check = allow((Context) host, ps);
        } else if (host instanceof Fragment) {
            check = allow((((Fragment) host).getContext()), ps);
        }

        int version = DeviceUtil.getOsVersion();

        if (version <= 23) {
            // 用v4包里的方法检测6.0以下的版本
            if (check) {
                l.onPermissionResult(code, PermissionResult.granted);
                return true;
            } else {
                l.onPermissionResult(code, PermissionResult.denied);
                return false;
            }
        } else {
            if (!check) {
                requestPermission(t);
                return false;
            } else {
                l.onPermissionResult(code, PermissionResult.granted);
                return true;
            }
        }
    }

    /**
     * 权限是否允许
     *
     * @param context
     * @param permissions
     * @return
     */
    public static boolean allow(@NonNull Context context, String... permissions) {
        for (String p : permissions) {
            int check = ContextCompat.checkSelfPermission(context, p);
            if (check == PackageManager.PERMISSION_GRANTED) {
                continue;
            } else {
                return false;
            }
        }
        return true;
    }

    /**
     * 权限请求回调结果
     *
     * @param host
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    public void onRequestPermissionsResult(Object host, int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        int len = grantResults.length;
        if (len <= 0) {
            return;
        }

        // 寻找没有被允许的下标
        int idx = -1;
        for (int i = 0; i < len; ++i) {
            int result = grantResults[i];
            if (result == PackageManager.PERMISSION_GRANTED) {
                continue;
            } else {
                idx = i;
                break;
            }
        }

        CheckTask task = mMap.get(requestCode);
        if (idx == -1) {
            // 全部都允许
            Log.d(TAG, "onRequestPermissionsResult()" + "通过");
            mMap.remove(requestCode);
            task.getListener().onPermissionResult(task.getCode(), PermissionResult.granted);
        } else {
            // 含有不允许的
            Activity act = null;
            if (host instanceof Activity) {
                act = (Activity) host;
            } else if (host instanceof Fragment) {
                act = ((Fragment) host).getActivity();
            }

            if (ActivityCompat.shouldShowRequestPermissionRationale(act, permissions[idx])) {
                Log.d(TAG, "onRequestPermissionsResult()" + "再次询问");
                requestPermission(task);
            } else {
                Log.d(TAG, "onRequestPermissionsResult()" + "不通过");
                task.getListener().onPermissionResult(task.getCode(), PermissionResult.denied);
            }
        }
    }

    /**
     * 请求权限
     *
     * @param task
     */
    private void requestPermission(CheckTask task) {
        OnPermissionListener l = task.getListener();
        Object host = task.getHost();
        String[] ps = task.getPermissions();
        int code = task.getCode();

        for (String p : ps) {
            if (host instanceof Activity) {
                if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) host, p)) {
                    l.onPermissionResult(code, PermissionResult.never_ask);
                } else {
                    ActivityCompat.requestPermissions((Activity) host, ps, code);
                }
            } else if (host instanceof Fragment) {
                Fragment frag = (Fragment) host;
                if (frag.shouldShowRequestPermissionRationale(p)) {
                    l.onPermissionResult(code, PermissionResult.never_ask);
                } else {
                    frag.requestPermissions(ps, code);
                }
            }
        }

    }

}