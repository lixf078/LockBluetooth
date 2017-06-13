/*
 * Copyright (C) 2008 ZXing authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.zxing.client.android;

import com.google.zxing.QrUtil;
import com.google.zxing.ResultPoint;
import com.google.zxing.client.android.camera.CameraManager;
import com.aiyiqi.lib.qr.R;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * This view is overlaid on top of the camera preview. It adds the viewfinder rectangle and partial
 * transparency outside it, as well as the laser scanner animation and result points.
 *
 * @author dswitkin@google.com (Daniel Switkin)
 */
public final class ViewfinderView extends View {

    private static final int[] SCANNER_ALPHA = {0, 64, 128, 192, 255, 192, 128, 64};

    private static final int CURRENT_POINT_OPACITY = 0xA0;
    private static final int MAX_RESULT_POINTS = 20;
    private static final int POINT_SIZE = 6;

    private CameraManager cameraManager;
    private Bitmap resultBitmap;
    private final int maskColor;
    private final int resultColor;
    private final int laserColor;
    private final int resultPointColor;
    private int scannerAlpha;
    private List<ResultPoint> possibleResultPoints;
    private List<ResultPoint> lastPossibleResultPoints;

    /**
     * 刷新界面的时间
     */
    private static final long ANIMATION_DELAY = 15L;
    private static final int OPAQUE = 0xFF;


    private int CORNER_PADDING;

    /**
     * 扫描框中的中间线的宽度
     */
    private static int MIDDLE_LINE_WIDTH;

    /**
     * 扫描框中的中间线的与扫描框左右的间隙
     */
    private static int MIDDLE_LINE_PADDING;

    /**
     * 中间那条线每次刷新移动的距离
     */
    private static final int SPEEN_DISTANCE = 10;

    /**
     * 画笔对象的引用
     */
    private final Paint paint;

    /**
     * 中间滑动线的最顶端位置
     */
    private int slideTop;

    /**
     * 中间滑动线的最底端位置
     */
    private int slideBottom;

    /**
     * 第一次绘制控件
     */
    boolean isFirst = true;

    private Bitmap bitmapCornerTopleft;
    private Bitmap bitmapCornerTopright;
    private Bitmap bitmapCornerBottomLeft;
    private Bitmap bitmapCornerBottomRight;
    private Bitmap bitmapScanLine;

    private Context mContext;


    // This constructor is used when the class is built from an XML resource.
    public ViewfinderView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        // Initialize these once for performance rather than calling them every time in onDraw().
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        Resources resources = getResources();
        maskColor = resources.getColor(R.color.viewfinder_mask);
        resultColor = resources.getColor(R.color.result_view);
        laserColor = resources.getColor(R.color.viewfinder_laser);
        resultPointColor = resources.getColor(R.color.possible_result_points);
        scannerAlpha = 0;
        possibleResultPoints = new ArrayList<>(5);
        lastPossibleResultPoints = null;

        CORNER_PADDING = dip2px(context, -3.0F);//将edge放在扫描框的外围
        MIDDLE_LINE_PADDING = dip2px(context, 10.0F);
        MIDDLE_LINE_WIDTH = dip2px(context, 3.0F);

        bitmapCornerTopleft = BitmapFactory.decodeResource(resources,
                R.drawable.qrcode_corner_top_left);
        bitmapCornerTopright = BitmapFactory.decodeResource(resources,
                R.drawable.qrcode_corner_top_right);
        bitmapCornerBottomLeft = BitmapFactory.decodeResource(resources,
                R.drawable.qrcode_corner_bottom_left);
        bitmapCornerBottomRight = BitmapFactory.decodeResource(
                resources, R.drawable.qrcode_corner_bottom_right);
        bitmapScanLine = BitmapFactory.decodeResource(resources,R.drawable.qrcode_laser);
    }

    public void setCameraManager(CameraManager cameraManager) {
        this.cameraManager = cameraManager;
    }

    @SuppressLint("DrawAllocation")
    @Override
    public void onDraw(Canvas canvas) {
        if (cameraManager == null) {
            return; // not ready yet, early draw before done configuring
        }
        Rect frame = cameraManager.getFramingRect();
        Rect previewFrame = cameraManager.getFramingRectInPreview();
        if (frame == null || previewFrame == null) {
            return;
        }
        int width = canvas.getWidth();
        int height = canvas.getHeight();

        // Draw the exterior (i.e. outside the framing rect) darkened
        paint.setColor(resultBitmap != null ? resultColor : maskColor);
        canvas.drawRect(0, 0, width, frame.top, paint);
        canvas.drawRect(0, frame.top, frame.left, frame.bottom + 1, paint);
        canvas.drawRect(frame.right + 1, frame.top, width, frame.bottom + 1, paint);
        canvas.drawRect(0, frame.bottom + 1, width, height, paint);

//        canvas.drawRect(0, 0, width, frame.top - 800, paint);
//        canvas.drawRect(0, frame.top - 800, frame.left, frame.bottom + 1 - 800, paint);
//        canvas.drawRect(frame.right + 1, frame.top - 800, width, frame.bottom + 1 - 800, paint);
//        canvas.drawRect(0, frame.bottom + 1 - 800, width, height, paint);

        if (resultBitmap != null) {
            // Draw the opaque result bitmap over the scanning rectangle
            paint.setAlpha(CURRENT_POINT_OPACITY);
            canvas.drawBitmap(resultBitmap, null, frame, paint);
        } else {

            // Draw a red "laser scanner" line through the middle to show decoding is active
//            paint.setColor(laserColor);
//            paint.setAlpha(SCANNER_ALPHA[scannerAlpha]);
//            scannerAlpha = (scannerAlpha + 1) % SCANNER_ALPHA.length;
//            int middle = frame.height() / 2 + frame.top;
//            canvas.drawRect(frame.left + 2, middle - 1, frame.right - 1, middle + 2, paint);

            // 画扫描框边上的角
            drawRectEdges(canvas, frame);

            // 绘制扫描线
            drawScanningLine(canvas, frame);

            //绘制提示文字
            drawText(canvas, frame);

            float scaleX = frame.width() / (float) previewFrame.width();
            float scaleY = frame.height() / (float) previewFrame.height();

            List<ResultPoint> currentPossible = possibleResultPoints;
            List<ResultPoint> currentLast = lastPossibleResultPoints;
            int frameLeft = frame.left;
            int frameTop = frame.top;
            if (currentPossible.isEmpty()) {
                lastPossibleResultPoints = null;
            } else {
                possibleResultPoints = new ArrayList<>(5);
                lastPossibleResultPoints = currentPossible;
                paint.setAlpha(CURRENT_POINT_OPACITY);
                paint.setColor(resultPointColor);
                synchronized (currentPossible) {
                    for (ResultPoint point : currentPossible) {
                        canvas.drawCircle(frameLeft + (int) (point.getX() * scaleX),
                                frameTop + (int) (point.getY() * scaleY),
                                POINT_SIZE, paint);
                    }
                }
            }
            if (currentLast != null) {
                paint.setAlpha(CURRENT_POINT_OPACITY / 2);
                paint.setColor(resultPointColor);
                synchronized (currentLast) {
                    float radius = POINT_SIZE / 2.0f;
                    for (ResultPoint point : currentLast) {
                        canvas.drawCircle(frameLeft + (int) (point.getX() * scaleX),
                                frameTop + (int) (point.getY() * scaleY),
                                radius, paint);
                    }
                }
            }

            // Request another update at the animation interval, but only repaint the laser line,
            // not the entire viewfinder mask.
            postInvalidateDelayed(ANIMATION_DELAY,
                    frame.left - POINT_SIZE,
                    frame.top - POINT_SIZE,
                    frame.right + POINT_SIZE,
                    frame.bottom + POINT_SIZE);
        }
    }

    public void drawViewfinder() {
        Bitmap resultBitmap = this.resultBitmap;
        this.resultBitmap = null;
        if (resultBitmap != null) {
            resultBitmap.recycle();
        }
        invalidate();
    }

    /**
     * Draw a bitmap with the result points highlighted instead of the live scanning display.
     *
     * @param barcode An image of the decoded barcode.
     */
    public void drawResultBitmap(Bitmap barcode) {
        resultBitmap = barcode;
        invalidate();
    }

    public void addPossibleResultPoint(ResultPoint point) {
        List<ResultPoint> points = possibleResultPoints;
        synchronized (points) {
            points.add(point);
            int size = points.size();
            if (size > MAX_RESULT_POINTS) {
                // trim it
                points.subList(0, size - MAX_RESULT_POINTS / 2).clear();
            }
        }
    }

    /**
     * 描绘方形的四个角
     *
     * @param canvas
     * @param frame
     */
    private void drawRectEdges(Canvas canvas, Rect frame) {

        paint.setColor(Color.WHITE);
        paint.setAlpha(OPAQUE);

        canvas.drawBitmap(bitmapCornerTopleft, frame.left + 2 * CORNER_PADDING,
                frame.top + 2 * CORNER_PADDING, paint);
        canvas.drawBitmap(bitmapCornerTopright, frame.right - 2 * CORNER_PADDING
                        - bitmapCornerTopright.getWidth(), frame.top + 2 * CORNER_PADDING,
                paint);
        canvas.drawBitmap(bitmapCornerBottomLeft, frame.left + 2 * CORNER_PADDING,
                (frame.bottom - 2 * CORNER_PADDING - bitmapCornerBottomLeft
                        .getHeight()), paint);
        canvas.drawBitmap(bitmapCornerBottomRight, frame.right - 2 * CORNER_PADDING
                - bitmapCornerBottomRight.getWidth(),(frame.bottom
                - 2 * CORNER_PADDING -  bitmapCornerBottomRight.getHeight()), paint);

    }


    /**
     * 绘制扫描线
     *
     * @param canvas
     * @param frame  扫描框
     */
    private void drawScanningLine(Canvas canvas, Rect frame) {

        // 初始化中间线滑动的最上边和最下边
        if (isFirst) {
            isFirst = false;
            slideTop = frame.top;
            slideBottom = frame.bottom;
        }

        // 绘制中间的线,每次刷新界面，中间的线往下移动SPEEN_DISTANCE
        slideTop += SPEEN_DISTANCE;
        if (slideTop >= slideBottom) {
            slideTop = frame.top;
        }

        // 从图片资源画扫描线
        Rect lineRect = new Rect();
        lineRect.left = frame.left + MIDDLE_LINE_PADDING;
        lineRect.right = frame.right - MIDDLE_LINE_PADDING;
        lineRect.top = slideTop;
        lineRect.bottom = (slideTop + MIDDLE_LINE_WIDTH);
        canvas.drawBitmap(bitmapScanLine, null,
                lineRect, paint);

    }


    private void drawText(Canvas canvas,Rect frame){
        // 获取屏幕的宽和高
        int width = canvas.getWidth();
        int height = canvas.getHeight();

        TextPaint textPaint = new TextPaint();

        textPaint.setTextSize(QrUtil.dip2px(mContext, 14));
        textPaint.setColor(getResources().getColor(R.color.qrcode_point_color));
        textPaint.setTextAlign(Paint.Align.CENTER);


        Paint.FontMetrics fontMetrics = paint.getFontMetrics();
        float fontHeight = fontMetrics.bottom-fontMetrics.top;

        canvas.drawText(getResources().getString(R.string.qrcode_point), width / 2, frame.bottom + fontHeight + dip2px(mContext, 30), textPaint);

    }

    /**
     * dp转px
     *
     * @param context
     * @param dipValue
     * @return
     */
    public int dip2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }
    
}
