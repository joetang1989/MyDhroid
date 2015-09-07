package com.example.administrator.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.ImageView;

import net.duohuo.dhroid.activity.BaseActivity;
import net.duohuo.dhroid.ioc.annotation.InjectView;
import net.duohuo.dhroid.util.ViewUtil;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Author:JsonLu
 * DateTime:2015/9/1 14:53
 * Email:luxd@i_link.cc
 */
public class LaunchActivity extends BaseActivity {

    @InjectView(id = R.id.first_pic)
    ImageView firstPic;
    private Timer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
        WindowManager wm = this.getWindowManager();
        ((MyApplication) getApplication()).DeviceWidth = wm.getDefaultDisplay().getWidth();
        ((MyApplication) getApplication()).DeviceHeight = wm.getDefaultDisplay().getHeight();
    }

    private void init() {
        setContentView(R.layout.activity_launch);
        ViewUtil.bindView(firstPic, R.drawable.firstpic);
        timer = new Timer();
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                Intent it = new Intent();
                it.setClass(LaunchActivity.this, LoginActivity.class);
                startActivity(it);
                LaunchActivity.this.finish();
            }
        };
        timer.schedule(timerTask, 3 * 1000);
    }


}
