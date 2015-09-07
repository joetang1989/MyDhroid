package com.example.administrator.myapplication;

import android.app.Application;
import android.content.SharedPreferences;

import net.duohuo.dhroid.Const;
import net.duohuo.dhroid.Dhroid;

import org.apache.http.util.EncodingUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;

/**
 * Author:JsonLu
 * DateTime:2015/8/31 09:13
 * Email:luxd@i_link.cc
 */
public class MyApplication extends Application {


    public JSONObject data;
    public String host;
    private String accountNo;
    public SharedPreferences spf;
    //设备宽高
    public int DeviceWidth,DeviceHeight;
    //UUID(设备ID)
    public java.util.UUID UUID;


    public String getAccountNo() {
        return accountNo;
    }

    public void setAccountNo(String accountNo) {
        this.accountNo = accountNo;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        dhroidint();
        data = fileReadJson("NetApi.json");
        try {
            host = data.getString("host_test");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        spf = getSharedPreferences("data", 0);
        new DeviceUuidFactory(this);
        UUID = DeviceUuidFactory.uuid;

    }


    // dhroidint开发框方法初始化
    private void dhroidint() {
        Const.netadapter_page_no = "request.pageNum";
        Const.netadapter_step = "request.pageSize";
        Const.response_data = "page1";
        Const.netadapter_step_default = 10;
        Const.netadapter_json_timeline = "pubdate";
        Const.DATABASE_VERSION = 20;
        Const.net_pool_size = 30;
        Const.net_error_try = true;
        Dhroid.init(this);

    }

    //从assets中读取文件
    public String fileRead(String fileName) {
        String res = null;
        try {
            InputStream in = getResources().getAssets().open(fileName);
            int length = in.available();
            byte[] buffer = new byte[length];
            in.read(buffer);
            res = EncodingUtils.getString(buffer, "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }

    //
    public JSONObject fileReadJson(String fileName) {
        JSONObject obj = null;
        try {
            obj = new JSONObject(fileRead(fileName));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return obj;
    }


}
