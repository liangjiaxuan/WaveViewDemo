package com.yxf.waveviewdemo;

import android.databinding.DataBindingUtil;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.yxf.waveviewdemo.databinding.ActivityMainBinding;



public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private ActivityMainBinding viewDataBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewDataBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        viewDataBinding.setOnClick(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_start:
                viewDataBinding.waveview.start();
                viewDataBinding.waveView2.start();
                viewDataBinding.waveView3.start();
//                viewDataBinding.customview.start();
//                new Thread(viewDataBinding.customview).start();
                break;
            case R.id.btn_stop:
                viewDataBinding.waveview.stop();
                viewDataBinding.waveView2.stop();
                viewDataBinding.waveView3.stop();
                break;
        }
    }


}
