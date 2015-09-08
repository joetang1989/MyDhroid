package com.example.administrator.myapplication;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.adrotatorcomponent.view.Advertisements;
import com.example.adrotatorcomponent.volley.RequestManager;

import net.duohuo.dhroid.activity.BaseActivity;
import net.duohuo.dhroid.ioc.annotation.InjectView;
import net.duohuo.dhroid.net.DhNet;
import net.duohuo.dhroid.net.NetTask;
import net.duohuo.dhroid.net.Response;

import org.json.JSONArray;
import org.json.JSONException;

/**
 * Author:JsonLu
 * DateTime:2015/8/31 09:13
 * Email:luxd@i_link.cc
 */
public class MainActivity extends BaseActivity {

    @InjectView(id = R.id.title_bar_back)
    TextView titleBarBack;
    @InjectView(id = R.id.title_bar_name)
    TextView titleBarName;
    @InjectView(id = R.id.title_bat_close)
    TextView titleBarClose;
    private LinearLayout llAdvertiseBoard;
    private LayoutInflater inflater;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initTitleBar();
        RequestManager.init(this);
        inflater = LayoutInflater.from(this);
        try {
            initViews();
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public boolean getNetWorkConnect() {
        return false;
    }

    private void initTitleBar() {
        titleBarBack.setVisibility(View.GONE);
        titleBarClose.setVisibility(View.GONE);
        titleBarName.setText(((MyApplication) getApplication()).UUID.toString());
    }


    private void initViews() throws JSONException {
        llAdvertiseBoard = (LinearLayout) this.findViewById(R.id.llAdvertiseBoard);
        DhNet net = new DhNet(((MyApplication) getApplication()).host + ((MyApplication) getApplication
                ()).data
                .getJSONObject
                        ("page_url").getString("index_pic"));
        net.doGet(new NetTask(MainActivity.this) {
            @Override
            public void doInUI(Response response, Integer transfer) {
                boolean state = response.isSuccess();
                if (!state) {

                } else {
                    JSONArray advertiseArray = response.jSONArrayFrom("data");
                    llAdvertiseBoard.addView(new Advertisements(MainActivity.this, true, inflater,
                            3000).initView
                            (advertiseArray));
                }

            }
        });


    }


}
