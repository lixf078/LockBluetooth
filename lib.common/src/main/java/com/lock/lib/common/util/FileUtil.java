package com.lock.lib.common.util;

import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;

import java.io.File;
import java.io.RandomAccessFile;

/**
 * Created by admin on 2017/6/27.
 */

public class FileUtil {

    static String  mFilePath = Environment.getExternalStorageDirectory().getPath() + "/";
    static String mFileName = "log.txt";

    public void initData() {
        writeTxtToFile("txt content", mFilePath, mFileName);
    }

    public static void deleteFile(String filePath, String fileName){
        if (TextUtils.isEmpty(filePath)){
            filePath = mFilePath;
        }
        if (TextUtils.isEmpty(fileName)){
            fileName = mFileName;
        }

        File file = new File(filePath, fileName);
        file.deleteOnExit();
    }

    public static void writeTxtToFile(String strContent) {

        writeTxtToFile(strContent + "\r\n", mFilePath, mFileName);
    }

    // 将字符串写入到文本文件中
    public static void writeTxtToFile(String strcontent, String filePath, String fileName) {

        if (TextUtils.isEmpty(filePath)){
            filePath = mFilePath;
        }
        if (TextUtils.isEmpty(fileName)){
            fileName = mFileName;
        }

        //生成文件夹之后，再生成文件，不然会出错
        makeFilePath(filePath, fileName);

        String strFilePath = filePath+fileName;
        // 每次写入时，都换行写
        String strContent = strcontent + "\r\n";
        try {
            File file = new File(strFilePath);
            if (!file.exists()) {
                Log.d("TestFile", "Create the file:" + strFilePath);
                file.getParentFile().mkdirs();
                file.createNewFile();
            }
            RandomAccessFile raf = new RandomAccessFile(file, "rwd");
            raf.seek(file.length());
            raf.write(strContent.getBytes());
            raf.close();
        } catch (Exception e) {
            Log.e("TestFile", "Error on write File:" + e);
        }
    }

    // 生成文件
    public static File makeFilePath(String filePath, String fileName) {
        File file = null;
        makeRootDirectory(filePath);
        try {
            file = new File(filePath + fileName);
            if (!file.exists()) {
                file.createNewFile();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return file;
    }

    // 生成文件夹
    public static void makeRootDirectory(String filePath) {
        File file = null;
        try {
            file = new File(filePath);
            if (!file.exists()) {
                file.mkdir();
            }
        } catch (Exception e) {
            Log.i("error:", e+"");
        }
    }
}
