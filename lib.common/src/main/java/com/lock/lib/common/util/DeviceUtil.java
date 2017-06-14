package com.lock.lib.common.util;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import com.lock.lib.common.constants.Constants;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.Locale;
import java.util.regex.Pattern;

/**
 * Created by hubing on 16/4/12.
 */
public class DeviceUtil {

    public static final String getUserAgent(String versionName) {
        StringBuilder sb = new StringBuilder();
        sb.append("Mozilla/5.0 (Linux; U; Android ")
                .append(Build.VERSION.RELEASE)
                .append("; ")
                .append(Locale.getDefault().getLanguage())
                .append("; ")
                .append(Build.BRAND)
                .append("_")
                .append(Build.DEVICE)
                .append("_")
                .append(Build.MODEL)
                .append(" ")
                .append(Build.DISPLAY)
                .append(" Android_" + versionName
                        + ") AppleWebKit/533.1 (KHTML, like Gecko) Version/4.0 Mobile Safari/533.1");
        return sb.toString();
    }
    public static final String getAndroidId(final Context context) {
        return android.provider.Settings.Secure.getString(context.getContentResolver(),
                android.provider.Settings.Secure.ANDROID_ID);
    }

    public static String fetchIpAddress() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();) {
                NetworkInterface intf = en.nextElement();
                if (intf.getName().toLowerCase().equals("eth0") || intf.getName().toLowerCase().equals("wlan0")) {
                    for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();) {
                        InetAddress inetAddress = enumIpAddr.nextElement();
                        if (!inetAddress.isLoopbackAddress()&& isIPv4Address(inetAddress
                                .getHostAddress())) {
                            String ipaddress = inetAddress.getHostAddress().toString();
                            if(!ipaddress.contains("::")){//ipV6的地址
                                return ipaddress;
                            }
                        }
                    }
                }else {
                    continue;
                }
            }
        } catch (SocketException ex) {
            Logger.e(Constants.TAG, "fetch ip error.",ex);
        }
        return "192.168.1.100";
    }

    /**
     * Ipv4地址检查
     */
    private static final Pattern IPV4_PATTERN = Pattern.compile("^(([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])\\.){3}([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])$");
    /**
     * 检查是否是有效的IPV4地址
     * @param input the address string to check for validity
     * @return true if the input parameter is a valid IPv4 address
     */
    public static boolean isIPv4Address(final String input) {
        return IPV4_PATTERN.matcher(input).matches();
    }

    public static String getDeviceId(Context context) {
        TelephonyManager mTM = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        String id = mTM.getDeviceId();

        if (TextUtils.isEmpty(id)) {
            id = (String) Invoker.invoke(mTM, "getDeviceIdGemini", new Class[]{Integer.TYPE},
                    new Object[]{Integer.valueOf(1)});
        }

        if (TextUtils.isEmpty(id)) {
            Object result = Invoker.invoke("android.provider.MultiSIMUtils", "getDefault",
                    new Class[]{Context.class}, new Object[]{context});

            if (result != null) {
                id = (String) Invoker.invoke(result, "getDeviceId", new Class[]{Integer.TYPE},
                        new Object[]{Integer.valueOf(1)});
            }
        }

        if (TextUtils.isEmpty(id)) {
            final WifiInfo wifiInfo = ((WifiManager) context.getSystemService(Context.WIFI_SERVICE))
                    .getConnectionInfo();
            final String mac = wifiInfo.getMacAddress();
            if (!TextUtils.isEmpty(mac)) {
                id = mac.replaceAll(":", "");
            }
        }

        if (TextUtils.isEmpty(id)) {
            id = getAndroidId(context);
        }
        return id;
    }

    public static String getDeviceId(Context context,boolean hasPermission) {
        String id = "";
        if(hasPermission){
            TelephonyManager mTM = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            id = mTM.getDeviceId();

            if (TextUtils.isEmpty(id)) {
                id = (String) Invoker.invoke(mTM, "getDeviceIdGemini", new Class[]{Integer.TYPE},
                        new Object[]{Integer.valueOf(1)});
            }

            if (TextUtils.isEmpty(id)) {
                Object result = Invoker.invoke("android.provider.MultiSIMUtils", "getDefault",
                        new Class[]{Context.class}, new Object[]{context});

                if (result != null) {
                    id = (String) Invoker.invoke(result, "getDeviceId", new Class[]{Integer.TYPE},
                            new Object[]{Integer.valueOf(1)});
                }
            }
        }


        if (TextUtils.isEmpty(id)) {
            final WifiInfo wifiInfo = ((WifiManager) context.getSystemService(Context.WIFI_SERVICE))
                    .getConnectionInfo();
            final String mac = wifiInfo.getMacAddress();
            if (!TextUtils.isEmpty(mac)) {
                id = mac.replaceAll(":", "");
            }
        }

        if (TextUtils.isEmpty(id)) {
            id = getAndroidId(context);
        }
        return id;
    }

    public static int getOsVersion(){
        int osVersion;
        try{
            osVersion = Integer.valueOf(Build.VERSION.SDK);
        }
        catch (NumberFormatException e){
            osVersion = 0;
        }

        return osVersion;
    }
}
