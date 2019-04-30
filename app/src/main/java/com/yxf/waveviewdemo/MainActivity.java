package com.yxf.waveviewdemo;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.graphics.Paint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AccelerateInterpolator;

import com.yxf.waveviewdemo.databinding.ActivityMainBinding;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private ActivityMainBinding viewDataBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewDataBinding = DataBindingUtil.setContentView(this,R.layout.activity_main);
        viewDataBinding.setOnClick(this);
//        viewDataBinding.waveView2.setInterpolator(new AccelerateInterpolator(1.2f));
//        viewDataBinding.waveView.setDuration(5000);
//        viewDataBinding.waveView.setStyle(Paint.Style.STROKE);
//        viewDataBinding.waveView.setSpeed(400);
//        viewDataBinding.waveView.setInterpolator(new AccelerateInterpolator(1.2f));
//        viewDataBinding.waveView.start();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_start:
                viewDataBinding.waveview.start();
                viewDataBinding.waveView2.start();
                break;
            case R.id.btn_stop:
                viewDataBinding.waveview.stop();
                viewDataBinding.waveView2.stop();
                break;
        }
    }
}
