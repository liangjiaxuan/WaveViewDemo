package com.yxf.waveviewdemo.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;

import com.yxf.waveviewdemo.R;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class WaveView extends View {
    private Paint mPaint;
    private int mDuration = 2000;//2秒一次轮训
    private float mInitialRadius; //波纹的初始半径
    private float mMaxRadius;   // 最大波纹半径
    private int mSpeed = 500;   // 波纹的创建速度，每500ms创建一个
    private List<Circle> mCilcleList = new ArrayList<>();//存储圆的集合
    private boolean mIsRunning;
    private long mLastCreateTime;

    public static final int cup_count = Runtime.getRuntime().availableProcessors();
    public static final int corepoolsize = Math.max(2, Math.min(cup_count - 1, 4));
    public static final int maxPoolSize = 2 * cup_count + 1;
    public static final Long keepAliveTime = 60L;
    ExecutorService poolExecutor = Executors.newSingleThreadExecutor();
    private float mMaxRadiusRate = 0.85f;
    private Interpolator mInterpolator = new LinearInterpolator();


    private Runnable createCircleRunnable = new Runnable() {
        @Override
        public void run() {
            if (mIsRunning) {
                Log.d("createCircleRunnable b", "run: thread"+Thread.currentThread());
                newCircle();
                postDelayed(createCircleRunnable, mSpeed);

                Log.d("createCircleRunnable f", "run: thread"+Thread.currentThread());
            }

        }
    };

    /**
     * 计算半径
     * 计算透明度
     * 计算变化速率
     *
     * @param context
     */

    public WaveView(Context context) {
        this(context, null);
    }

    public WaveView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        setStyle(Paint.Style.FILL);
        setColor(ContextCompat.getColor(context, R.color.color_main));
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        mMaxRadius = Math.min(w, h) * mMaxRadiusRate / 2.0f;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Iterator<Circle> iterator = mCilcleList.iterator();
        while (iterator.hasNext()) {
            Circle Circle = iterator.next();
            if (System.currentTimeMillis() - Circle.mCreateTime < mDuration) {
                Log.d("alpha: ", Circle.getAlpha() + "");
                mPaint.setAlpha(Circle.getAlpha());
                canvas.drawCircle(getWidth() / 2, getHeight() / 2, Circle.getCurrentRadius(), mPaint);
            } else {
                iterator.remove();
            }

            if (mCilcleList.size() > 0) {
                postInvalidateDelayed(10);
            }

        }

    }

    private void setStyle(Paint.Style fill) {
        mPaint.setStyle(fill);
    }

    public void setColor(int color) {
        mPaint.setColor(color);
    }

    /**
     * 开始
     */
    public void start() {
        if (!mIsRunning) {
            mIsRunning = true;
            createCircleRunnable.run();
//            poolExecutor.execute(createCircleRunnable);

        }
    }

    /**
     * 停止
     */
    public void stop() {
        if (mIsRunning) {
            mIsRunning = false;
//            poolExecutor.shutdown();
        }
    }


    public class Circle {
        private long mCreateTime;

        public Circle() {
            this.mCreateTime = System.currentTimeMillis();
        }

        /**
         * 获取透明度
         *
         * @return
         */
        public int getAlpha() {
            return (int) ((1.0f - getPercent()) * 255);
        }

        /**
         * 获取半径大小
         *
         * @return
         */
        public float getCurrentRadius() {
            return mInitialRadius + (mMaxRadius - mInitialRadius) * getPercent();
        }

        /**
         * 当前圆的比例
         *
         * @return
         */
        public float getPercent() {
            return mInterpolator.getInterpolation((System.currentTimeMillis() - mCreateTime) * 1.0f / mDuration);
        }
    }


    public void newCircle() {
        long currentTime = System.currentTimeMillis();
        if (currentTime - mLastCreateTime < mSpeed) return;
        Circle circle = new Circle();
        mCilcleList.add(circle);
        invalidate();
        mLastCreateTime = currentTime;

        Log.d("newCircle", "run: thread"+Thread.currentThread());
    }


    public void setInterpolator(Interpolator interpolator) {
        mInterpolator = interpolator;
        if (mInterpolator == null) {
            mInterpolator = new LinearInterpolator();
        }
    }

    private static ExecutorService createDefaultExecutorService() {
        SynchronousQueue executorQueue = new SynchronousQueue();
        return new ThreadPoolExecutor(corepoolsize, maxPoolSize, keepAliveTime, TimeUnit.SECONDS,
                executorQueue);
    }

}
