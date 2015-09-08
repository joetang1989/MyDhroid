package com.example.administrator.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import com.example.administrator.myapplication.utils.AppUtils;
import com.example.administrator.myapplication.utils.L;
import com.example.administrator.myapplication.utils.NetUtils;
import com.example.administrator.myapplication.utils.T;

import net.duohuo.dhroid.activity.BaseActivity;
import net.duohuo.dhroid.ioc.annotation.InjectView;
import net.duohuo.dhroid.net.DhNet;
import net.duohuo.dhroid.net.NetTask;
import net.duohuo.dhroid.net.Response;
import net.duohuo.dhroid.util.ViewUtil;

import org.json.JSONException;
import org.json.JSONObject;

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
        setContentView(R.layout.activity_launch);
        ViewUtil.bindView(firstPic, R.drawable.firstpic);
        if(!getNetWorkConnect()) {
            T.showLong(this, "net");
        }
        try {
            init();
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public boolean getNetWorkConnect() {
        if(NetUtils.isConnected(this)){
            return  true;
        }
        return false;
    }

    public void init() throws JSONException {
        final int versionCode = AppUtils.getVersionCode(this);
        String update_url = (((MyApplication) getApplication()).getUrl("update"));
        DhNet net = new DhNet(update_url);
        net.doGet(new NetTask(this) {

            @Override
            public void doInUI(Response response, Integer transfer) throws JSONException {
                if (response.isSuccess()) {
                    JSONObject jsonObject = response.jSON();
                    if (jsonObject.getInt("versionCode") > versionCode) {
                        L.e("update apk");
                        //选择是否要升级
                        turnByTime();

                    } else
                        turnByTime();
                }
            }
        });
    }

    //计时3s跳转
    private void turnByTime() {
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
