package com.lock.lib.qr;


import android.graphics.Bitmap;
import android.util.Log;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

import java.util.HashMap;
import java.util.Map;

import static android.graphics.Color.BLACK;
import static android.graphics.Color.WHITE;


/**
 * Created by admin on 2017/6/26.
 */

public class QRCodeUtil {
    public static Bitmap encodeQRBitmap(String str) {
        Bitmap bitmap = null;
        BitMatrix result = null;
        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
        try {
            Map<EncodeHintType,String> hints = new HashMap<>();
            hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
            result = multiFormatWriter.encode(str, BarcodeFormat.QR_CODE, 200, 200,hints);
            // 使用 ZXing Android Embedded 要写的代码
//            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
//            bitmap = barcodeEncoder.createBitmap(result);
        } catch (WriterException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException iae) { // ?
            return null;
        }

        // 如果不使用 ZXing Android Embedded 的话，要写的代码
        int w = result.getWidth();
        int h = result.getHeight();
        int[] pixels = new int[w * h];
        for (int y = 0; y < h; y++) {
            int offset = y * w;
            for (int x = 0; x < w; x++) {
                pixels[offset + x] = result.get(x, y) ? BLACK : WHITE;
            }
        }

        bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        // Log.e("lxf", "encodeQRBitmap w " + w + " h " + h);
        bitmap.setPixels(pixels, 0, 200, 0, 0, w, h);

        return bitmap;
    }
}
