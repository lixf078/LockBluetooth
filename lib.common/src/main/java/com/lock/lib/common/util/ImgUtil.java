package com.lock.lib.common.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Log;

import com.lock.lib.common.constants.Constants;
import com.facebook.binaryresource.BinaryResource;
import com.facebook.binaryresource.FileBinaryResource;
import com.facebook.cache.common.CacheKey;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.cache.DefaultCacheKeyFactory;
import com.facebook.imagepipeline.core.ImagePipeline;
import com.facebook.imagepipeline.core.ImagePipelineFactory;
import com.facebook.imagepipeline.request.ImageRequest;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by hubing on 16/4/21.
 */
public class ImgUtil {

    public static boolean isImageDownloaded(Uri loadUri) {
        if (loadUri == null) {
            return false;
        }
        CacheKey cacheKey = DefaultCacheKeyFactory.getInstance().getEncodedCacheKey(ImageRequest.fromUri(loadUri));
        return ImagePipelineFactory.getInstance().getMainDiskStorageCache().hasKey(cacheKey) || ImagePipelineFactory.getInstance().getSmallImageDiskStorageCache().hasKey(cacheKey);
    }

    //return file or null
    public static File getCachedImageOnDisk(Uri loadUri) {
        File localFile = null;
        if (loadUri != null) {
            CacheKey cacheKey = DefaultCacheKeyFactory.getInstance().getEncodedCacheKey(ImageRequest.fromUri(loadUri));
            if (ImagePipelineFactory.getInstance().getMainDiskStorageCache().hasKey(cacheKey)) {
                BinaryResource resource = ImagePipelineFactory.getInstance().getMainDiskStorageCache().getResource(cacheKey);
                localFile = ((FileBinaryResource) resource).getFile();
            } else if (ImagePipelineFactory.getInstance().getSmallImageDiskStorageCache().hasKey(cacheKey)) {
                BinaryResource resource = ImagePipelineFactory.getInstance().getSmallImageDiskStorageCache().getResource(cacheKey);
                localFile = ((FileBinaryResource) resource).getFile();
            }

            if(localFile!=null){
                localFile = saveFileToPng(localFile);
            }
        }
        return localFile;
    }

    //return file or null
    public static File getCachedImageOnDisk(Uri loadUri,boolean newFile) {
        File localFile = null;
        if (loadUri != null) {
            CacheKey cacheKey = DefaultCacheKeyFactory.getInstance().getEncodedCacheKey(ImageRequest.fromUri(loadUri));
            if (ImagePipelineFactory.getInstance().getMainDiskStorageCache().hasKey(cacheKey)) {
                BinaryResource resource = ImagePipelineFactory.getInstance().getMainDiskStorageCache().getResource(cacheKey);
                localFile = ((FileBinaryResource) resource).getFile();
            } else if (ImagePipelineFactory.getInstance().getSmallImageDiskStorageCache().hasKey(cacheKey)) {
                BinaryResource resource = ImagePipelineFactory.getInstance().getSmallImageDiskStorageCache().getResource(cacheKey);
                localFile = ((FileBinaryResource) resource).getFile();
            }

            if(newFile){
                if(localFile!=null){
                    localFile = saveFileToPng(localFile);
                }
            }

        }
        return localFile;
    }

    public static void removeFile(Uri loadUri){

        Logger.e(Constants.TAG,"removeFile >> loadUri : "+loadUri);

        if(loadUri!=null){

            ImagePipeline imagePipeline = Fresco.getImagePipeline();
            Logger.e(Constants.TAG,"removeFile >> imagePipeline : "+imagePipeline);
            if(imagePipeline!=null){
                Logger.e(Constants.TAG,"removeFile >> imagePipeline.isInBitmapMemoryCache(loadUri) : "+imagePipeline.isInBitmapMemoryCache(loadUri));
                if(imagePipeline.isInBitmapMemoryCache(loadUri)){
                    imagePipeline.evictFromCache(loadUri);
                }
            }
        }
    }


    public static File saveFileToPng(File f)  {
        File file = new File(AppUtil.DOWNLOAD_PATH + "tmp.png");
        if (file.exists()) {
            file.delete();
        }
        BufferedInputStream bis = null;
        BufferedOutputStream bos = null;
        try {
            bis = new BufferedInputStream(new FileInputStream(f));
            bos = new BufferedOutputStream(new FileOutputStream(file));

            byte[] bytes = new byte[1024];
            int len = -1;
            while ((len = bis.read(bytes)) != -1){
                bos.write(bytes, 0 , len);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(bos!=null){
                try {
                    bos.flush();
                    bos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(bis!=null){
                try {
                    bis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
        return file;
    }


    public static String saveFileToPng(String path){
        Bitmap bitmap = decodeBitmap(path);
        String p = null;
        if(bitmap!=null){
             p = saveMyBitmap(bitmap);
        }

        return p;
    }

    private static Bitmap decodeBitmap(String path){
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        // 通过这个bitmap获取图片的宽和高
        Bitmap bitmap = BitmapFactory.decodeFile(path, options);
        if (bitmap == null){
            System.out.println("bitmap为空");
        }
        float realWidth = options.outWidth;
        float realHeight = options.outHeight;
        Logger.e(Constants.TAG,"真实图片高度：" + realHeight + "宽度:" + realWidth);
        // 计算缩放比
        int scale = (int) ((realHeight > realWidth ? realHeight : realWidth) / 100);
        if (scale <= 0)
        {
            scale = 1;
        }
        options.inSampleSize = scale;
        options.inJustDecodeBounds = false;
        // 注意这次要把options.inJustDecodeBounds 设为 false,这次图片是要读取出来的。
        bitmap = BitmapFactory.decodeFile(path, options);
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();
        Logger.e(Constants.TAG,"缩略图高度：" + h + "宽度:" + w);
        return bitmap;
    }

    private static String saveMyBitmap(Bitmap mBitmap){
        File f = new File(AppUtil.DOWNLOAD_PATH + "tmp.png");
        if(f.exists()){
            Logger.e(Constants.TAG,"saveMyBitmap >> file >> delete : "+f.delete());
        }
        Logger.e(Constants.TAG,"saveMyBitmap >> file >> exists : "+f.exists());
        try {
            f.createNewFile();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            Logger.e(Constants.TAG,"在保存图片时出错："+e.toString());
        }
        FileOutputStream fOut = null;
        try {
            fOut = new FileOutputStream(f);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        mBitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut);
        try {
            fOut.flush();
        } catch (IOException e) {
            Logger.e(Constants.TAG,"在保存图片时出错：",e);
        }
        try {
            fOut.close();
        } catch (IOException e) {
            Logger.e(Constants.TAG,"在保存图片时出错：",e);
        }

        return f.getAbsolutePath();
    }



}
