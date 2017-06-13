package com.lock.lib.common.util;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.util.Log;

import com.lock.lib.common.constants.Constants;
import com.anthonycr.grant.PermissionsManager;
import com.anthonycr.grant.PermissionsResultAction;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

/**
 * Created by hubing on 16/5/3.
 *
 * 1 在使用App时，单个不被允许的权限只需要被弹出一次
 *
 * 2 当再次进入App时，未被允许的权限要再次弹出
 */

public class PermissionUtil {

    private String TAG = "PermissionUtil";

    private static Object clockObj = new Object();
    private static HashMap<String,Boolean> permissionMap = new HashMap<String,Boolean>();
    private static PermissionUtil mInstance;
    private PermissionUtil(){
    }

    public static PermissionUtil getInstance(){
        synchronized (clockObj){
            if(mInstance == null){
                mInstance = new PermissionUtil();
            }
            return mInstance;
        }
    }

    public static boolean hasPermission(Context context,String permission){
        return PermissionsManager.hasPermission(context,permission);
    }
    public static boolean hasAllPermission(Context context , String[] permission){
        return PermissionsManager.hasAllPermissions(context,permission);
    }

    public void requestAllManifestPermissionsIfNecessary(Activity activity,final PermissionListener listener){
        String[] permissions = getManifestPermissions(activity);
        ArrayList<String> permissionList = new ArrayList<String>();
        for(String permission : permissions){
            if(!permissionMap.containsKey(permission)){
                permissionList.add(permission);
            }else{
                listener.onDenied(permission);
            }
        }
        if(permissionList!=null && !permissionList.isEmpty()){
            PermissionsManager.getInstance().requestAllManifestPermissionsIfNecessary(activity,new PermissionsResultAction(){
                @Override
                public void onDenied(String permission) {
                    if(permissionMap.containsKey(permission)){
                        permissionMap.remove(permission);
                    }
                    permissionMap.put(permission,false);
                    listener.onDenied(permission);
                    listener.onDenied(permission);
                }

                @Override
                public void onGranted() {
                    listener.onGranted();
                }
            });
        }
    }

    /**
     *
     * 6.0 的权限，当退出应用后，需要清理权限
     *
     */
    public static void releasePermissions(){
        if(permissionMap!=null){
            permissionMap.clear();
        }
    }

    private synchronized String[] getManifestPermissions(@NonNull final Activity activity) {
        PackageInfo packageInfo = null;
        List<String> list = new ArrayList<>(1);
        try {
            Log.d(TAG, activity.getPackageName());
            packageInfo = activity.getPackageManager().getPackageInfo(activity.getPackageName(), PackageManager.GET_PERMISSIONS);
        } catch (PackageManager.NameNotFoundException e) {
            Log.e(TAG, "A problem occurred when retrieving permissions", e);
        }
        if (packageInfo != null) {
            String[] permissions = packageInfo.requestedPermissions;
            if (permissions != null) {
                for (String perm : permissions) {
                    Log.d(TAG, "Manifest contained permission: " + perm);
                    list.add(perm);
                }
            }
        }
        return list.toArray(new String[list.size()]);
    }

    public void requestPermissionsIfNecessaryForResult(Activity activity,String[] permissions,final PermissionListener listener){
        if(permissions!=null) {
            ArrayList<String> permissionList = new ArrayList<String>();
            for (String permission : permissions) {
                if (!permissionMap.containsKey(permission)) {
                    permissionList.add(permission);
                } else {
                    listener.onDenied(permission);
                }
            }
            if (permissionList != null && !permissionList.isEmpty()) {
                PermissionsManager.getInstance().requestPermissionsIfNecessaryForResult(activity, permissionList.toArray(new String[]{}), new PermissionsResultAction(){

                    @Override
                    public void onGranted() {
                        listener.onGranted();
                    }

                    @Override
                    public void onDenied(String permission) {
                        if(permissionMap.containsKey(permission)){
                            permissionMap.remove(permission);
                        }
                        permissionMap.put(permission,false);
                        listener.onDenied(permission);
                    }
                });
            }
        }
    }

    public static void notifyPermissionsChange( String[] permissions,  int[] results){
        PermissionsManager.getInstance().notifyPermissionsChange(permissions,results);
    }

    public static abstract class PermissionListener {
        public abstract void onGranted();
        public abstract void onDenied(String permission);
    }
}
