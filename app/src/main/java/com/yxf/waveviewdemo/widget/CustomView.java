package com.yxf.waveviewdemo.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.os.Message;
import android.util.AttributeSet;
import android.view.View;

import com.yxf.waveviewdemo.R;

public class CustomView extends View implements Runnable {

    private Paint mPaint;
    private Bitmap bitmap;

    public CustomView(Context context) {
        super(context);
    }

    public CustomView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initRes(context);
        initPaint();
    }

    private void initPaint() {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);//抗锯齿
        mPaint.setStyle(Paint.Style.FILL);
//        mPaint.setColor(Color.LTGRAY);
        mPaint.setColor(Color.argb(255, 253, 128, 103));

/*
// 生成色彩矩阵
        ColorMatrix colorMatrix = new ColorMatrix(new float[]{
                1, 0, 0, 0, 0,
                0, 1, 0, 0, 0,
                0, 0, 1, 0, 0,
                0, 0, 0, 1, 0,
        });
*/

        ColorMatrix colorMatrix = new ColorMatrix(new float[]{
                0.3f, 0, 0, 0, 0,
                0, 1, 0, 0, 0,
                0, 0, 1, 0, 0,
                0, 0, 0, 1, 0});
        mPaint.setColorFilter(new ColorMatrixColorFilter(colorMatrix));
        mPaint.setAntiAlias(true);
    }

    public void initRes(Context context){
        bitmap = BitmapFactory
                .decodeResource(context.getResources(), R.drawable.icon_alipay);

    }

    /**
     * paint集成了画的属性（颜色 抗锯齿 透明度）
     * canvas定义所要画的东西（圆  矩形  弧）
     *
     * @param canvas
     */
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mRadius <= 0) {
            mRadius = Math.min(getWidth() / 2, getHeight() / 2) * 0.85f;
        }
        canvas.drawCircle(getWidth() / 2, getHeight() / 2, mRadius, mPaint);


        canvas.drawBitmap(bitmap,getWidth() / 2, getHeight() / 2,mPaint);
    }

    private float mRadius;

    public synchronized void setRadius(float radius) {
        this.mRadius = radius;
        invalidate();
    }

    public void start() {
        this.start();
    }

    @Override
    public void run() {
        while (true) {
            try {
                if (mRadius <= 200) {
                    mRadius = mRadius + 10;
                    postInvalidate();
                } else {
                    mRadius = 0;
                }
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
