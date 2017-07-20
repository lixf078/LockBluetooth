package com.lock.bluetooth.le.permission;

public interface OnPermissionListener {
    void onPermissionResult(int code, @PermissionResult int result);
}