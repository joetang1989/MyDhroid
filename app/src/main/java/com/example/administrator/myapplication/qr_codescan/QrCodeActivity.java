package com.example.administrator.myapplication.qr_codescan;


import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.administrator.myapplication.MyApplication;
import com.example.administrator.myapplication.R;
import com.example.administrator.myapplication.utils.T;

import net.duohuo.dhroid.activity.BaseActivity;
import net.duohuo.dhroid.ioc.annotation.InjectExtra;
import net.duohuo.dhroid.ioc.annotation.InjectView;
import net.duohuo.dhroid.net.DhNet;
import net.duohuo.dhroid.net.NetTask;
import net.duohuo.dhroid.net.Response;

import org.json.JSONException;
import org.json.JSONObject;

public class QrCodeActivity extends BaseActivity {
    @InjectExtra(name = "result")
    String result;
    @InjectView(id = R.id.result)
    TextView res;
    @InjectView(id = R.id.qrcode_bitmap)
    ImageView img;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrcode);

        String isbn_url = null;
        try {
            isbn_url = ((MyApplication) getApplication()).data.getJSONObject("services").getString("isbn_url")+result;
            T.showLong(this, isbn_url);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        DhNet net = new DhNet(isbn_url);
        net.doGet(new NetTask(this) {
            @Override
            public void doInUI(Response response, Integer transfer) throws JSONException {
                JSONObject jsonObject = response.jSON();
                T.showLong(QrCodeActivity.this, jsonObject+"");
            }
        });


    }

    @Override
    public boolean getNetWorkConnect() {
        return false;
    }


}
