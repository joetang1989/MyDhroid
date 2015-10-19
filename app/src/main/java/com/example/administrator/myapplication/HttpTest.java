package com.example.administrator.myapplication;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.administrator.myapplication.httpsutils.CallServer;
import com.example.administrator.myapplication.utils.HttpUtils;
import com.example.administrator.myapplication.utils.L;

import net.duohuo.dhroid.activity.BaseActivity;
import net.duohuo.dhroid.ioc.annotation.InjectView;

import java.util.HashMap;

/**
 * Author:JsonLu
 * DateTime:2015/9/17 15:45
 * Email:luxd@sumpay.com
 * Desc:
 **/
public class HttpTest extends BaseActivity {

    @InjectView(id = R.id.scanningback, click = "clickEvent")
    Button scanningBack;
    @InjectView(id = R.id.scanningclose, click = "clickEvent")
    Button scanningClose;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrollview);
    }

    public void clickEvent(View v) {
        switch (v.getId()) {
            case R.id.scanningclose:
                HttpUtils.doGetAsyn("http://192.168.8.116", new CallBack());
                break;
            case R.id.scanningback:
                HashMap<String, String> param = new HashMap<String, String>();
                param.put("data", "data");
                String data = CallServer.getInstance().callServer("", param, HttpTest.this);
                L.d("HTTPS:" + data);
                break;
        }
    }

    //HttpUtil中回调接口
    class CallBack implements HttpUtils.CallBack {

        @Override
        public void onRequestComplete(String result) {
            L.e(result);
        }
    }
}
