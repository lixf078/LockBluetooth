package com.example.bluetooth.le.permission;

public interface OnPermissionListener {
    void onPermissionResult(int code, @PermissionResult int result);
}