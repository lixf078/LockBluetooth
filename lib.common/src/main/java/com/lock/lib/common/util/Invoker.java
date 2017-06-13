package com.lock.lib.common.util;

import android.util.Log;


import com.lock.lib.common.constants.Constants;

import java.lang.reflect.Method;


public class Invoker {
    private Invoker() {
    }

    public static final Object invoke(Object object, String method, Class<?>[] paramTypes, Object[] args) {
        if (hasMethod(object.getClass(), method)) {
            try {
                return object.getClass().getMethod(method, paramTypes).invoke(object, args);
            } catch (Exception e) {
                Log.e(Constants.TAG, "[ERROR] can not invoke: " + method + e.getMessage());
            }
        }
        return null;
    }

    public static final Object invoke(String clazz, String method, Class<?>[] paramTypes, Object[] args) {
        try {
            Class<?> localClass = Class.forName(clazz);
            if (hasMethod(localClass, method)) {
                return localClass.getMethod(method, paramTypes).invoke(localClass, args);
            }
        } catch (Exception e) {
        }
        return null;
    }

    public static final boolean hasMethod(Class<?> clazz, String method) {
        Method[] methods = clazz.getDeclaredMethods();
        for (Method dm : methods) {
            if (dm.getName().equals(method)) {
                return true;
            }
        }
        return false;
    }
}
