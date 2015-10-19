package com.example.administrator.myapplication;

import android.app.Application;
import android.app.Notification;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v4.app.NotificationCompat;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.example.administrator.myapplication.utils.DeviceUuidFactory;
import com.example.administrator.myapplication.utils.L;
import com.umeng.message.PushAgent;
import com.umeng.message.UTrack;
import com.umeng.message.UmengMessageHandler;
import com.umeng.message.UmengNotificationClickHandler;
import com.umeng.message.UmengRegistrar;
import com.umeng.message.entity.UMessage;

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
    public int DeviceWidth, DeviceHeight;
    //UUID(设备ID)
    public java.util.UUID UUID;
    //VersionCode
    public int versionCode;
    //versionName
    public String versionName;

    private PushAgent mPushAgent;


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
            if (data.getInt("state") == 0) {
                host = data.getString("host_test");
            } else {
                host = data.getString("host_normal");
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        spf = getSharedPreferences("data", 0);
        new DeviceUuidFactory(getApplicationContext());
        UUID = DeviceUuidFactory.uuid;

        umengInit();

    }

    private void umengInit() {
        mPushAgent = PushAgent.getInstance(getApplicationContext());
        mPushAgent.setDebugMode(true);
        UmengMessageHandler messageHandler = new UmengMessageHandler() {
            @Override
            public void dealWithCustomMessage(final Context context, final UMessage msg) {
                new Handler(getMainLooper()).post(new Runnable() {

                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
                        UTrack.getInstance(getApplicationContext()).trackMsgClick(msg);
                        Toast.makeText(context, msg.custom, Toast.LENGTH_LONG).show();
                    }
                });
            }

            @Override
            public Notification getNotification(Context context,
                                                UMessage msg) {
                switch (msg.builder_id) {
                    case 1:
                        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
                        RemoteViews myNotificationView = new RemoteViews(context.getPackageName(), R.layout.notification_view);
                        myNotificationView.setTextViewText(R.id.notification_title, msg.title);
                        myNotificationView.setTextViewText(R.id.notification_text, msg.text);
                        myNotificationView.setImageViewBitmap(R.id.notification_large_icon, getLargeIcon(context, msg));
                        myNotificationView.setImageViewResource(R.id.notification_small_icon, getSmallIconId(context, msg));
                        builder.setContent(myNotificationView);
                        builder.setAutoCancel(true);
                        Notification mNotification = builder.build();
                        //由于Android v4包的bug，在2.3及以下系统，Builder创建出来的Notification，并没有设置RemoteView，故需要添加此代码
                        mNotification.contentView = myNotificationView;
                        return mNotification;
                    default:
                        //默认为0，若填写的builder_id并不存在，也使用默认。
                        return super.getNotification(context, msg);
                }
            }
        };
        mPushAgent.setMessageHandler(messageHandler);

        /**
         * 该Handler是在BroadcastReceiver中被调用，故
         * 如果需启动Activity，需添加Intent.FLAG_ACTIVITY_NEW_TASK
         * */
        UmengNotificationClickHandler notificationClickHandler = new UmengNotificationClickHandler() {
            @Override
            public void dealWithCustomAction(Context context, UMessage msg) {
                Toast.makeText(context, msg.custom, Toast.LENGTH_LONG).show();
            }
        };
        mPushAgent.setNotificationClickHandler(notificationClickHandler);
        mPushAgent.enable();
        String info = String.format("enabled:%s  isRegistered:%s  DeviceToken:%s",
                mPushAgent.isEnabled(), mPushAgent.isRegistered(),
                UmengRegistrar.getRegistrationId(this));
        L.i("switch Push:" + info);
        L.d(mPushAgent.isEnabled() + "");

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

    //读取本地JSON
    public JSONObject fileReadJson(String fileName) {
        JSONObject obj = null;
        try {
            obj = new JSONObject(fileRead(fileName));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return obj;
    }

    //拼合URL地址

    public String getUrl(String page) throws JSONException {
        return host + data.getJSONObject("page_url").getString(page);
    }


}
