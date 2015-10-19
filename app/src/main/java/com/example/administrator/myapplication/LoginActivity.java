package com.example.administrator.myapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.myapplication.utils.L;
import com.umeng.message.PushAgent;
import com.umeng.message.UmengRegistrar;

import net.duohuo.dhroid.activity.BaseActivity;
import net.duohuo.dhroid.ioc.annotation.InjectView;
import net.duohuo.dhroid.net.DhNet;
import net.duohuo.dhroid.net.NetTask;
import net.duohuo.dhroid.net.Response;
import net.duohuo.dhroid.util.ViewUtil;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Author:JsonLu
 * DateTime:2015/9/2 10:51
 * Email:luxd@i_link.cc
 */
public class LoginActivity extends BaseActivity {

    @InjectView(id = R.id.account_no)
    TextView accountNo;
    @InjectView(id = R.id.account_pwd)
    TextView accountPwd;
    @InjectView(id = R.id.btn_login, click = "toActivity")
    View btnLogin;
    @InjectView(id = R.id.btn_register, click = "toActivity")
    View btnRegister;
    @InjectView(id = R.id.remeber_pwd, click = "toActivity")
    ImageView remeberPwd;
    @InjectView(id = R.id.forget_pwd, click = "toActivity")
    TextView forgetPwd;

    private DhNet net;
    private int pwdRemeber = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initInput();

    }

    private void initInput() {
        SharedPreferences sdf = ((MyApplication) getApplication()).spf;
        accountNo.setText(sdf.getString("name", ""));
        accountPwd.setText(sdf.getString("pwd", ""));
    }


    private boolean checkInput() {
        if (accountNo.getText().length() < 3 || accountPwd.getText().length() < 3) {
            return false;
        }
        return true;
    }

    public void toActivity(View v) throws JSONException {
        net = new DhNet();
        switch (v.getId()) {
            case R.id.btn_login:
                if (checkInput()) {
                    net.setUrl(((MyApplication) getApplication()).getUrl("login"));
                    net.addParam("accountno", accountNo.getText()).addParam("accountpwd",
                            accountPwd.getText
                                    ());
                    net.doPost(new NetTask(this) {
                        @Override
                        public void doInUI(Response response, Integer transfer) throws JSONException {
                            if (response.isSuccess()) {
                                ((MyApplication) getApplication()).setAccountNo(accountNo.getText
                                        ().toString());
                                SharedPreferences spf = ((MyApplication) getApplication()).spf;
                                SharedPreferences.Editor editor = spf.edit();
                                if (pwdRemeber == 1) {

                                    editor.putString("name", accountNo.getText().toString());
                                    editor.putString("pwd", accountPwd.getText().toString());
                                    editor.commit();
                                } else {
                                    editor.clear();
                                    editor.commit();
                                }
                                Intent it = new Intent();
                                it.setClass(LoginActivity.this, MainActivity.class);
                                startActivity(it);
                            } else {

                                JSONObject jsonObject = ((MyApplication) getApplication())
                                        .fileReadJson
                                                ("Error_Msg.json");
                                String err_msg = response.getMsg();
                                Toast.makeText(getApplication(), jsonObject.getString(err_msg), Toast
                                        .LENGTH_LONG)
                                        .show();
                            }
                        }
                    });
                }
                break;

            case R.id.forget_pwd:
                Intent it = new Intent();
                it.setClass(this,CreateQrCode.class);
                startActivity(it);
                break;
            case R.id.remeber_pwd:
                if (pwdRemeber == 0) {
                    ViewUtil.bindView(remeberPwd, R.drawable.check_is_icon);
                    pwdRemeber = 1;

                } else {
                    ViewUtil.bindView(remeberPwd, R.drawable.check_not_icon);
                    pwdRemeber = 0;
                }
                break;
            case R.id.btn_register:
                PushAgent mPushAgent = PushAgent.getInstance(this);
                if (mPushAgent.isEnabled()){
                    mPushAgent.disable();
                }else {
                    mPushAgent.enable();
                }
                String info = String.format("enabled:%s  isRegistered:%s  DeviceToken:%s",
                        mPushAgent.isEnabled(), mPushAgent.isRegistered(),
                        UmengRegistrar.getRegistrationId(this));
                L.i("switch Push:" + info);
                L.d(mPushAgent.isEnabled() + "");

                break;
        }


    }


}
