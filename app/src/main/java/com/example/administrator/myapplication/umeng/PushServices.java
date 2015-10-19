package com.example.administrator.myapplication.umeng;

import android.content.Context;
import android.content.Intent;

import com.example.administrator.myapplication.utils.L;
import com.umeng.message.UmengBaseIntentService;
import com.umeng.message.entity.UMessage;

import org.android.agoo.client.BaseConstants;
import org.json.JSONObject;

/**
 * Author:JsonLu
 * DateTime:2015/9/18 16:34
 * Email:luxd@sumpay.com
 * Desc:
 **/
public class PushServices extends UmengBaseIntentService {

    private final String TAG = getClass().getName();

    @Override
    protected void onMessage(Context context, Intent intent) {
        super.onMessage(context, intent);

        try {
            String message = intent.getStringExtra(BaseConstants.MESSAGE_BODY);
            UMessage msg = new UMessage(new JSONObject(message));
            L.d(TAG, "message=" + message);
            L.d(TAG, "custom=" + msg.custom);
        } catch (Exception e) {
            L.e(TAG, e.getMessage());
        }
    }
}
