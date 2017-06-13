package com.lock.lib.common.util;

import android.app.Activity;
import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Environment;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.CharacterStyle;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.widget.Toast;

import com.lock.lib.common.R;
import com.lock.lib.common.constants.Constants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Formatter;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static android.os.Environment.MEDIA_MOUNTED;

/**
 * Created by chenrong on 15/8/26.
 */
public class AppUtil {

    private AppUtil() {
    }

    public final static String DOWNLOAD_PATH
            = Environment.getExternalStorageDirectory() + "/lock/";

    public static final File getHomePath(final Context context) {
        File home = null;
        if (MEDIA_MOUNTED.equals(Environment.getExternalStorageState()) && hasExternalStoragePermission(context)) {
            final File externalDir = context.getExternalFilesDir(null);
            if ((externalDir != null) && (externalDir.exists()) && (externalDir.isDirectory())) {
                home = new File(externalDir, Constants.Path.HOME);
            }
        }

        if (home == null) {
            home = context.getDir(Constants.Path.HOME, Context.MODE_WORLD_READABLE);
        }

        return home;
    }

    public static final void ensurePath(String path) {
        File dir = new File(path);
        if (dir.exists()) {
            if (!dir.isDirectory()) {
                dir.delete();
                dir.mkdir();
                dir.setExecutable(true, false);
            }
        } else {
            dir.mkdir();
            dir.setExecutable(true, false);
        }
    }

    public static final void ensurePath(File dir) {
        if (dir != null) {
            if (dir.exists()) {
                if (!dir.isDirectory()) {
                    dir.delete();
                    dir.mkdir();
                    dir.setExecutable(true, false);
                }
            } else {
                dir.mkdir();
                dir.setExecutable(true, false);
            }
        }
    }

    /**
     * 复制文本
     *
     * @param context 上下文
     * @param str     将要复制的文本
     */
    public static void copeStr(Context context, String str) {
        ClipboardManager cmb = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData mClipData = ClipData.newPlainText("一起装修", str);
        ToastUtil.showToast(context, "复制成功");

        cmb.setPrimaryClip(mClipData);
    }

    public static final boolean ensureJson(String jsonString) {
        boolean result = false;
        if (jsonString != null) {
            try {
                new JSONObject(jsonString);
                result = true;
            } catch (JSONException ignore) {
            }

            if (!result) {
                try {
                    new JSONArray(jsonString);
                    result = true;
                } catch (JSONException ignore) {
                }
            }
        }
        return result;
    }

    public static final void deleteFile(String path) {
        deleteFile(new File(path));
    }

    public static final void deleteFile(File file) {
        if (file.exists() && file.isFile()) {
            file.delete();
        }
    }

    public static final void deleteFolder(final File dir) {
        if (dir.exists() && dir.isDirectory()) {
            File[] files = dir.listFiles();
            for (int i = 0; i < files.length; i++) {
                if (files[i].isFile()) {
                    deleteFile(files[i]);
                } else {
                    deleteFolder(files[i]);
                }
            }
            dir.delete();
        }
    }

    public static final boolean hasExternalStoragePermission(Context context) {
        int perm = context.checkCallingOrSelfPermission("android.permission.WRITE_EXTERNAL_STORAGE");
        return perm == PackageManager.PERMISSION_GRANTED;
    }

    public static final boolean isSdcardMounted() {
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }

    public static final String getFileFolder(final String path) {
        String folder = null;
        if ((path != null) && (path.length() > 0)) {
            int last = path.lastIndexOf("/");
            if (last > -1) {
                String temp = path.substring(0, last);
                if ((temp != null) && (temp.length() > 0)) {
                    last = temp.lastIndexOf("/");
                    folder = temp.substring(last + 1);
                } else {
                    folder = "/";
                }
            }
        }
        return folder;
    }

    public static final String getFormattedSize(long size) {
        if (size < 1024) {
            return String.valueOf(size) + "B";
        }
        if (size < 1024 * 1024) {
            final Formatter formatter = new Formatter();
            final String result = formatter.format("%.2fK", (size / (1024.0F))).toString();
            formatter.close();
            return result;
        } else {
            final Formatter formatter = new Formatter();
            final String result = formatter.format("%.2fM", (size / (1024.0F * 1024.0F))).toString();
            formatter.close();
            return result;
        }
    }

    public static final boolean isInteger(final String number) {
        boolean result = false;
        if (number != null) {
            Pattern pattern = Pattern.compile("^-?\\d+$");
            Matcher matcher = pattern.matcher(number.trim());
            result = matcher.matches();
            if (result) {
                try {
                    Integer.valueOf(number);
                } catch (NumberFormatException e) {
                    result = false;
                }
            }
        }
        return result;
    }

    public static final boolean isFloat(final String number) {
        boolean result = false;
        if (number != null) {
            Pattern pattern = Pattern.compile("^(-?\\d+)(\\.\\d+)?$");
            Matcher matcher = pattern.matcher(number);
            result = matcher.matches();
        }
        return result;
    }

    public static final boolean isValidEmail(final String email) {
        boolean result = false;
        if (email != null) {
            // \\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*
            Pattern pattern = Pattern.compile("\\w+(\\.\\w+)*@\\w+(\\.\\w+)+");
            Matcher matcher = pattern.matcher(email.trim());
            result = matcher.matches();
        }
        return result;
    }

    public static final boolean isValidPhoneNumber(final String phoneNumber) {
        boolean result = false;
        if (phoneNumber != null) {
            // \\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*
            Pattern pattern = Pattern.compile("^((\\+86)|(86))?[1][3578]\\d{9}$");
            Matcher matcher = pattern.matcher(phoneNumber.trim());
            result = matcher.matches();
        }
        return result;
    }

    public static final boolean isValidPassword(final String password) {
        boolean result = false;
        if (password != null) {
            Pattern pattern = Pattern.compile("^[\\@A-Za-z0-9\\!\\#\\$\\%\\^\\&\\*\\.\\~\\*\\(\\)\\+\\-\\=\\_]{6,20}$");
            Matcher matcher = pattern.matcher(password.trim());
            result = matcher.matches();
        }
        return result;
    }

    public static final int getVersionCode(Context context, String packageName) {
        int versionCode = 0;
        if (context != null) {
            try {
                PackageInfo info = context.getPackageManager().getPackageInfo(packageName,
                        PackageManager.PERMISSION_GRANTED);
                if (info != null) {
                    versionCode = info.versionCode;
                }
            } catch (PackageManager.NameNotFoundException ignore) {
            }
        }
        return versionCode;
    }

    public static final int getVersionCode(Context context) {
        int versionCode = 0;
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(context.getPackageName(),
                    PackageManager.PERMISSION_GRANTED);
            if (info != null) {
                versionCode = info.versionCode;
            }
        } catch (PackageManager.NameNotFoundException e) {
        }
        return versionCode;
    }

    public static final String getVersionName(Context context) {
        String versionName = "unknown";
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(context.getPackageName(),
                    PackageManager.PERMISSION_GRANTED);
            versionName = info.versionName;
        } catch (PackageManager.NameNotFoundException e) {
        }
        return versionName;
    }

    public static final String getFileName(String fileName) {
        String name = null;
        if (fileName != null) {
            final int index = fileName.lastIndexOf(".");
            if (index == -1) {
                name = fileName;
            } else {
                name = fileName.substring(0, index);
            }
        }
        return name;
    }

    public static final String getFileName(File file) {
        String fileName = null;
        if (file != null) {
            final String name = file.getName();
            final int index = name.lastIndexOf(".");

            if (index == -1) {
                fileName = name;
            } else {
                fileName = name.substring(0, index);
            }
        }
        return fileName;
    }

    public static final String getFullFileName(String path) {
        String name = null;
        if (path != null) {
            final int index = path.lastIndexOf("/");
            if (index == -1) {
                name = path;
            } else {
                name = path.substring(index + 1);
            }
        }
        return name;
    }

    public static final String getFileExt(String fileName) {
        String ext = null;
        if (fileName != null) {
            final int index = fileName.lastIndexOf(".");
            if (index == -1) {
                ext = fileName;
            } else {
                ext = fileName.substring(index + 1);
            }
        }
        return ext;
    }

    public static final boolean isAppInstalled(Context context, String packageName) {
        boolean result = false;
        if (context != null) {
            try {
                PackageInfo pkginfo = context.getPackageManager().getPackageInfo(packageName,
                        PackageManager.PERMISSION_GRANTED);
                result = (pkginfo != null);
            } catch (PackageManager.NameNotFoundException ignore) {
            }
        }
        return result;
    }

    public static final String getMetaDataValue(final Context context, final String key) {
        Object value = null;
        PackageManager pm = context.getPackageManager();
        ApplicationInfo applicationInfo;
        try {
            applicationInfo = pm.getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
            if (applicationInfo != null && applicationInfo.metaData != null) {
                value = applicationInfo.metaData.get(key);
            }
        } catch (PackageManager.NameNotFoundException e) {
        }
        if (value == null) {
            return null;
        } else {
            return value.toString();
        }
    }

    public static final String getMetaDataValue(final Context context, final String key, final String defaultValue) {
        Object value = null;
        PackageManager pm = context.getPackageManager();
        ApplicationInfo applicationInfo;
        try {
            applicationInfo = pm.getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
            if (applicationInfo != null && applicationInfo.metaData != null) {
                value = applicationInfo.metaData.get(key);
            }
        } catch (PackageManager.NameNotFoundException e) {
        }
        if (value == null) {
            return defaultValue;
        } else {
            return value.toString();
        }
    }

    public static final String getBrand() {
        String brand = "unknown";
        try {
            brand = URLEncoder.encode(Build.BRAND, "UTF-8");
        } catch (UnsupportedEncodingException ignore) {
            brand = convertURL(Build.BRAND);
        }
        return brand;
    }

    public static final String getManufacturer() {
        String manufacturer = "unknown";
        try {
            manufacturer = URLEncoder.encode(Build.MANUFACTURER, "UTF-8");
        } catch (UnsupportedEncodingException ignore) {
            manufacturer = convertURL(Build.MANUFACTURER);
        }
        return manufacturer;
    }

    public static final String getModel() {
        String model = Build.MODEL.substring(Build.MODEL.lastIndexOf(" ") + 1);
        try {
            model = URLEncoder.encode(model, "UTF-8");
        } catch (UnsupportedEncodingException ignore) {
            model = convertURL(model);
        }
        return model;
    }

    public static final String urlEncode(final String url) {
        String encode = null;
        try {
            encode = URLEncoder.encode(url, "UTF-8");
        } catch (UnsupportedEncodingException ignore) {
            encode = convertURL(url);
        }
        return encode;
    }

    public static final String convertURL(String string) {
        return string.trim().replace(" ", "%20").replace("&", "%26").replace(",", "%2c").replace("(", "%28")
                .replace(")", "%29").replace("!", "%21").replace("=", "%3D").replace("<", "%3C").replace(">", "%3E")
                .replace("#", "%23").replace("$", "%24").replace("'", "%27").replace("*", "%2A").replace("-", "%2D")
                .replace(".", "%2E").replace("/", "%2F").replace(":", "%3A").replace(";", "%3B").replace("?", "%3F")
                .replace("@", "%40").replace("[", "%5B").replace("\\", "%5C").replace("]", "%5D").replace("_", "%5F")
                .replace("`", "%60").replace("{", "%7B").replace("|", "%7C").replace("}", "%7D");
    }

    public static final int getChineseNumber(final String string) {
        int count = 0;
        if ((string != null) && (string.length() > 1)) {
            Pattern ch = Pattern.compile("[\\u4e00-\\u9fa5]+");
            int length = string.length();
            for (int i = 0; i < length; i++) {
                if (ch.matcher(string.substring(i, i + 1)).find()) {
                    count++;
                }
            }
        }
        return count;
    }

    public static final String toSecretString(final String str) {
        if (str != null) {
            final int length = str.length();
            if (length == 0) {
                return "*";
            } else if (length == 1) {
                return "*";
            } else if (length == 2) {
                return str.substring(0, 1) + "*";
            } else if (length == 3) {
                return str.substring(0, 1) + "*" + str.substring(3);
            } else if (length == 4) {
                return str.substring(0, 1) + "**" + str.substring(4);
            } else if (length == 5) {
                return str.substring(0, 1) + "***" + str.substring(5);
            } else if (length == 6) {
                return str.substring(0, 2) + "****" + str.substring(6);
            } else {
                return str.substring(0, 3) + "*****" + str.substring(7);
            }
        }
        return "*";
    }


    public static final int getRandom(int num) {
        int rand = num == 0 ? (int) (Math.random() * 100 + 1) : num;

        Logger.d(Constants.TAG, "getRandom >> number : " + num + " ; rand : " + rand);

        return rand;
    }

    public static final SpannableStringBuilder formatLongMoney(Activity context, long fen, float size) {
        DisplayMetrics metric = new DisplayMetrics();
        context.getWindowManager().getDefaultDisplay().getMetrics(metric);
        float money = fen / 100f;
        String tmp = formatFloatToString(money);
        Logger.e(Constants.TAG, "formatLongMoney >> tmp : " + tmp);
        String value = context.getResources().getString(R.string.lib_price_txt_flag, tmp);
        SpannableStringBuilder style = new SpannableStringBuilder(value);
        style.setSpan(new AbsoluteSizeSpan((int) (28 / 2 * metric.density)), 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        style.setSpan(new AbsoluteSizeSpan((int) (size / 2 * metric.density)), 1, value.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return style;
    }

    public static final SpannableStringBuilder formatLongMoney(Activity context, long fen, float size, int resFlag) {
        DisplayMetrics metric = new DisplayMetrics();
        context.getWindowManager().getDefaultDisplay().getMetrics(metric);
        float money = fen / 100f;
        String tmp = formatFloatToString(money);
        Logger.e(Constants.TAG, "formatLongMoney >> tmp : " + tmp);
        String value = context.getResources().getString(resFlag, tmp);
        SpannableStringBuilder style = new SpannableStringBuilder(value);
        style.setSpan(new AbsoluteSizeSpan((int) (28 / 2 * metric.density)), 0, value.indexOf(tmp), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        style.setSpan(new AbsoluteSizeSpan((int) (size / 2 * metric.density)), value.indexOf(tmp), value.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return style;
    }

    public static final SpannableStringBuilder formatMoney(Activity context, int fen, float size) {
        DisplayMetrics metric = new DisplayMetrics();
        context.getWindowManager().getDefaultDisplay().getMetrics(metric);
        float money = fen / 100f;
        String tmp = formatFloatToString(money);
        String value = context.getResources().getString(R.string.lib_price_txt_flag, tmp);
        SpannableStringBuilder style = new SpannableStringBuilder(value);
        style.setSpan(new AbsoluteSizeSpan((int) (28 / 2 * metric.density)), 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        style.setSpan(new AbsoluteSizeSpan((int) (size / 2 * metric.density)), 1, value.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return style;
    }

    public static String fenToYuan(long fen) {
        float money = fen / 100f;
        return formatFloatToString(money);
    }

    public static String formatFloatToString(float value) {
        DecimalFormat df = new DecimalFormat("###");
        String valueString = String.valueOf(value);
        int index = valueString.indexOf(".");
        Logger.e(Constants.TAG, "formatFloatToString >> index : " + index);
        try {
            String tmp = valueString.substring(index + 1);
            Logger.e(Constants.TAG, "formatFloatToString >> tmp : " + tmp);
            if (Integer.parseInt(tmp) == 0) {
                return String.valueOf(value).substring(0, index);
            } else {
                df = new DecimalFormat("###.00");
            }
        } catch (Exception e) {

        }
        String str = String.valueOf(value);
        String res = "";
        if (value > 1) {
            BigDecimal num = new BigDecimal(str);
            res = df.format(num);
        } else {
            res = String.valueOf(Float.valueOf(df.format(value)));
        }

        return res;
    }


    /*public static String fetchIpAddress() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();) {
                NetworkInterface intf = en.nextElement();
                if (intf.getName().toLowerCase().equals("eth0") || intf.getName().toLowerCase().equals("wlan0")) {
                    for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();) {
                        InetAddress inetAddress = enumIpAddr.nextElement();
                        if (!inetAddress.isLoopbackAddress()&& InetAddressUtils.isIPv4Address(inetAddress
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
    }*/

    private static String int2ip(int ipInt) {
        StringBuilder sb = new StringBuilder();
        sb.append(ipInt & 0xFF).append(".");
        sb.append((ipInt >> 8) & 0xFF).append(".");
        sb.append((ipInt >> 16) & 0xFF).append(".");
        sb.append((ipInt >> 24) & 0xFF);
        return sb.toString();
    }

    public static int dip2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    public static String formatPhoneNumber(String phone) {
        if (!TextUtils.isEmpty(phone)) {
            StringBuilder builder = new StringBuilder();
            builder.append(phone.substring(0, 3));
            builder.append("****");
            builder.append(phone.substring(7));
            return builder.toString();
        }
        return phone;
    }

    /**
     * @param str     要处理的字符串内容
     * @param symb    要过滤的字符
     * @param delLast 是否删除后面的
     * @return
     */
    public static String delSymbol(String str, String symb, boolean delLast) {

        str = str.trim();

        StringBuffer stringBuffer = new StringBuffer(str);

        // 从头删除
        for (int i = 0, j = stringBuffer.length(); i < j; i++) {
            if (stringBuffer.charAt(i) == '\n' || stringBuffer.charAt(i) == '\r') {
                stringBuffer.delete(0, 1);
            } else {
                break;
            }
        }

        if (delLast) {
            // 从尾部删除
            for (int i = stringBuffer.length(); i > 0; i++) {
                if (stringBuffer.charAt(i - 1) == '\n' || stringBuffer.charAt(i - 1) == '\r') {
                    stringBuffer.deleteCharAt(stringBuffer.length() - 1);
                } else {
                    break;
                }
            }
        }
        return stringBuffer.toString();
    }

    /**
     * 关键字高亮显示
     *
     * @param target 需要高亮的关键字
     * @param text   需要显示的文字
     * @return spannable 处理完后的结果，记得不要toString()，否则没有效果
     */
    public static SpannableStringBuilder highlight(String text, String target) {
        SpannableStringBuilder spannable = new SpannableStringBuilder(text);
        CharacterStyle span = null;

        Pattern p = Pattern.compile(target);
        Matcher m = p.matcher(text);
        while (m.find()) {
            span = new ForegroundColorSpan(Color.parseColor("#00a051"));// 需要重复！
            spannable.setSpan(span, m.start(), m.end(),
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        return spannable;
    }

    /**
     * 关键字高亮显示
     *
     * @param targets 需要高亮的关键字
     * @param text    需要显示的文字
     * @return spannable 处理完后的结果，记得不要toString()，否则没有效果
     */
    public static SpannableStringBuilder highlight(String text, String... targets) {
        SpannableStringBuilder spannable = new SpannableStringBuilder(text);
        CharacterStyle span = null;

        if (targets != null) {
            int size = targets.length;
            for (int i = 0; i < size; i++) {
                String target = targets[i];
                Pattern p = Pattern.compile(target);
                Matcher m = p.matcher(text);
                while (m.find()) {
                    span = new ForegroundColorSpan(Color.parseColor("#00a051"));// 需要重复！
                    spannable.setSpan(span, m.start(), m.end(),
                            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                }
            }
        }

        return spannable;
    }

    /**
     * 关键字高亮显示
     *
     * @param targets 需要高亮的关键字
     * @param text    需要显示的文字
     * @return spannable 处理完后的结果，记得不要toString()，否则没有效果
     */
    public static SpannableStringBuilder athighlight(String text, String... targets) {
        SpannableStringBuilder spannable = new SpannableStringBuilder(text);
        CharacterStyle span = null;

        if (targets != null) {
            int size = targets.length;
            for (int i = 0; i < size; i++) {
                String target = targets[i];
                Pattern p = Pattern.compile(target);
                Matcher m = p.matcher(text);
                while (m.find()) {
                    span = new ForegroundColorSpan(Color.parseColor("#2d61af"));// 需要重复！
                    spannable.setSpan(span, m.start(), m.end(),
                            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                }
            }
        }

        return spannable;
    }

    public static SpannableStringBuilder reSetText(String text, String target, int color, int textSize, boolean isBold) {
        SpannableStringBuilder spannable = new SpannableStringBuilder(text);
        CharacterStyle span = null;
        CharacterStyle sizeSpan = null;
        StyleSpan styleSpan = null;
        Pattern p = Pattern.compile(target);
        Matcher m = p.matcher(text);
        while (m.find()) {
            span = new ForegroundColorSpan(color);// 需要重复！
            sizeSpan = new AbsoluteSizeSpan(textSize, true);
            styleSpan = new StyleSpan(Typeface.BOLD_ITALIC);
            spannable.setSpan(span, m.start(), m.end(),
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            spannable.setSpan(sizeSpan, m.start(), m.end(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            if (isBold) {
                spannable.setSpan(styleSpan, m.start(), m.end(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }
        return spannable;
    }

    public static SpannableStringBuilder reSetText(String text, ArrayList<String> tags, ArrayList<Integer> colors, ArrayList<Integer> textSizes, ArrayList<Boolean> isBolds) {
        SpannableStringBuilder spannable = new SpannableStringBuilder(text);
        CharacterStyle span = null;
        CharacterStyle sizeSpan = null;
        StyleSpan styleSpan = null;
        if (tags != null && !tags.isEmpty()) {
            int size = tags.size();
            for (int i = 0; i < size; i++) {
                String target = tags.get(i);
                if (!TextUtils.isEmpty(target)) {
                    Pattern p = Pattern.compile(target);
                    Matcher m = p.matcher(text);
                    while (m.find()) {
                        span = new ForegroundColorSpan(colors.get(i));// 需要重复！
                        sizeSpan = new AbsoluteSizeSpan(textSizes.get(i), true);
                        styleSpan = new StyleSpan(Typeface.BOLD);
                        spannable.setSpan(span, m.start(), m.end(),
                                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                        spannable.setSpan(sizeSpan, m.start(), m.end(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                        if (isBolds.get(i)) {
                            spannable.setSpan(styleSpan, m.start(), m.end(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                        }
                    }
                }
            }
        }

        return spannable;
    }


    /**
     * 获取application中指定的meta-data
     *
     * @return 如果没有获取成功(没有对应值，或者异常)，则返回值为空
     */
    public static String getAppMetaData(Context ctx, String key) {
        if (ctx == null || TextUtils.isEmpty(key)) {
            return null;
        }
        String resultData = null;
        try {
            PackageManager packageManager = ctx.getPackageManager();
            if (packageManager != null) {
                ApplicationInfo applicationInfo = packageManager.getApplicationInfo(ctx.getPackageName(), PackageManager.GET_META_DATA);
                if (applicationInfo != null) {
                    if (applicationInfo.metaData != null) {
                        resultData = applicationInfo.metaData.getString(key);
                    }
                }

            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        return resultData;
    }

    public static Toast showToast(Context context, String content) {
        Toast toast = Toast.makeText(context, content, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();

        return toast;

    }

    public static Toast showToast(Context context, int contentRes) {
        Toast toast = Toast.makeText(context, contentRes, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
        return toast;
    }

    public static Dialog getBottomDialog(Context context) {
        //TODO
       /* View view = LayoutInflater.from(context).inflate(R.layout.common_bottom_sheet, null);
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
        win.setGravity(Gravity.BOTTOM);*/
        return null;
    }


    public static float mScreenWidth = 0;

    public static int getAdapterMetricsWidth(Context context, float scale) {
        if (mScreenWidth != 0) {

        }
        DisplayMetrics dm = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(dm);
        int mScreenWidth = dm.widthPixels;// 获取屏幕分辨率宽度
        return (int) (mScreenWidth * scale);
    }

    public static int getAdapterMetricsHeigh(Context context, int sourceWidth, int sourceHeight, float scale) {
        if (mScreenWidth == 0) {
            DisplayMetrics dm = new DisplayMetrics();
            ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(dm);
            mScreenWidth = dm.widthPixels;// 获取屏幕分辨率宽度
        }
        // int mScreenHeight = dm.heightPixels;
        if (sourceWidth == 0) {
            sourceWidth = (int) mScreenWidth;
            sourceHeight = (int) (mScreenWidth * 1.3);
        }
        float descHeight = scale * mScreenWidth * sourceHeight / sourceWidth;
        // Log.d(TAG, "getMetricsHeigh mScreenWidth " + mScreenWidth +
        // ",sourceWidth " + sourceWidth + ",sourceHeight " + sourceHeight +
        // ", descHeight " + descHeight);
        return (int) descHeight;
    }


    public static final String getPackageName(Context context) {
        if (context != null) {
            return context.getPackageName();
        }
        return "";
    }

    public static String getUTF8XMLString(String xml) {
        // A StringBuffer Object
        StringBuffer sb = new StringBuffer();
        sb.append(xml);
        String xmString = "";
        String xmlUTF8 = "";
        try {
            xmString = new String(sb.toString().getBytes("UTF-8"));
            xmlUTF8 = URLEncoder.encode(xmString, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        // return to String Formed
        return xmlUTF8;
    }


    // 根据路径获得图片并压缩，返回bitmap用于显示
    public static File getSmallFile(String filePath) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, 320, 480);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;

        return saveBitmap(BitmapFactory.decodeFile(filePath, options));
    }

    public static File saveBitmap(Bitmap bm) {
        String picName = System.currentTimeMillis() + ".png";

        File dirFile = new File(DOWNLOAD_PATH);
        if (!dirFile.exists()) {
            dirFile.mkdir();
        }
        File myCaptureFile = new File(DOWNLOAD_PATH + picName);

        try {

            FileOutputStream out = new FileOutputStream(myCaptureFile);
            bm.compress(Bitmap.CompressFormat.PNG, 10, out);
            out.flush();
            out.close();
            Logger.e(Constants.TAG, "已经保存");
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return myCaptureFile;

    }

    // 计算图片的缩放值
    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        return inSampleSize;
    }


    public static String join(Iterator<String> iterator, String separator) {

        // handle null, zero and one elements before building a buffer
        if (iterator == null) {
            return null;
        }
        if (!iterator.hasNext()) {
            return "";
        }
        String first = iterator.next();
        if (!iterator.hasNext()) {
            return first;
        }

        // two or more elements
        StringBuilder buf = new StringBuilder(1024); // Java default is 16, probably too small
        if (first != null) {
            buf.append(first);
        }

        while (iterator.hasNext()) {
            if (separator != null) {
                buf.append(separator);
            }
            Object obj = iterator.next();
            if (obj != null) {
                buf.append(obj);
            }
        }
        return buf.toString();
    }

    public static int[] getScale(String path) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        // 通过这个bitmap获取图片的宽和高
        Bitmap bitmap = BitmapFactory.decodeFile(path, options);
        if (bitmap == null) {
            Logger.e(Constants.TAG, "bitmap为空");
        }
        float realWidth = options.outWidth;
        float realHeight = options.outHeight;
        Logger.e(Constants.TAG, "真实图片高度：" + realHeight + "宽度:" + realWidth);


        // 计算缩放比
        int scale = (int) ((realHeight > realWidth ? realHeight : realWidth) / mScreenWidth);
        if (scale <= 0) {
            scale = 1;
        }
        Logger.e(Constants.TAG, "getScale >> scale : " + scale);
        int[] size = new int[2];
        size[0] = (int) (realWidth / scale);
        size[1] = (int) (realHeight / scale);

        Logger.e(Constants.TAG, "getScale >> size[0] : " + size[0] + " ; size[1] : " + size[1]);
        /*options.inSampleSize = scale;
        options.inJustDecodeBounds = false;
        // 注意这次要把options.inJustDecodeBounds 设为 false,这次图片是要读取出来的。
        bitmap = BitmapFactory.decodeFile(path, options);
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();
        Logger.e(Constants.TAG,"缩略图高度：" + h + "宽度:" + w);*/
        return size;
    }


}
