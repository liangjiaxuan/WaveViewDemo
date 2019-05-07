package com.yxf.waveviewdemo.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.yxf.waveviewdemo.R;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class WaveView3 extends View {

    private float initRadius;//初始化圆半径
    private float maxRadius;//最大圆半径
    private float mMaxRadiusRate = 0.85f;
    private int mSpeed = 500;//速度
    private int mDuration = 2000;//波纹创建到消失持续的时间
    private Paint mPaint;
    private long mLastCreateTime;
    private List<Circle> circleList = new ArrayList<>();
    private boolean mIsRunning;

    private Runnable creatCircle=new Runnable() {
        @Override
        public void run() {
            if (mIsRunning) {
                newCircle();
                postDelayed(creatCircle, mSpeed);
            }
        }
    };


    public WaveView3(Context context) {
        super(context);
    }

    public WaveView3(Context context, AttributeSet attrs) {
        super(context, attrs);
        initPaint(context);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        maxRadius = Math.min(w, h) * mMaxRadiusRate / 2.0f;
    }

    /**
     * 画圆
     * @param canvas
     */
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Iterator<Circle> iterator = circleList.iterator();
        while (iterator.hasNext()) {
            Circle circle = iterator.next();
            if (System.currentTimeMillis() - circle.createTime < mDuration) {
                mPaint.setAlpha(circle.getAlpha());
                Log.d("---","alpha :"+circle.getAlpha());
                Log.d("---","radius :"+circle.getCurrentRadius());
                Log.d("---","percent :"+circle.getPercent());
                canvas.drawCircle(getWidth()/2,getHeight()/2,circle.getCurrentRadius(),mPaint);
            } else {
                iterator.remove();
            }
        }

        if (circleList.size()>0) {
            postInvalidateDelayed(10);
        }

    }

    public void initPaint(Context context) {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(ContextCompat.getColor(context, R.color.color_main));
    }

    public void start(){
        if (!mIsRunning) {
            mIsRunning=true;
            creatCircle.run();
        }
    }

    public void stop(){
        if (mIsRunning) {
            mIsRunning=false;
        }
    }


    public class Circle {
        private long createTime;
        public Circle() {
            this.createTime = System.currentTimeMillis();
        }

        public float getCurrentRadius(){
            return initRadius+(maxRadius-initRadius)*getPercent();
        }

        public int getAlpha(){
            return (int) ((1-getPercent())*255);
        }

        public float getPercent(){
            return (System.currentTimeMillis()-createTime)*1.0f/mDuration;
        }

    }

    public void newCircle() {
        long currentTime = System.currentTimeMillis();
        if (currentTime - mLastCreateTime < mSpeed) return;

        Circle circle = new Circle();
        circleList.add(circle);

        invalidate();

        mLastCreateTime=currentTime;


    }

}
