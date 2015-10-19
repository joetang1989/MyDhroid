package com.example.administrator.myapplication;

import android.os.Bundle;
import android.view.WindowManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import net.duohuo.dhroid.activity.BaseActivity;
import net.duohuo.dhroid.ioc.annotation.InjectView;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Author:JsonLu
 * DateTime:2015/9/6 11:52
 * Email:luxd@i_link.cc
 */
public class OAuthLoginActivity extends BaseActivity {

    @InjectView(id = R.id.webView)
    WebView webView;

    private JSONObject jsonObject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_oauth);
        WindowManager.LayoutParams p = getWindow().getAttributes();
        p.height = (int) (((MyApplication) getApplication()).DeviceHeight * 0.8);
        p.width = (int) (((MyApplication) getApplication()).DeviceWidth * 0.8);

        getWindow().setAttributes(p);
        try {
            initWebView();
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
    
    private void initWebView() throws JSONException {
        jsonObject = ((MyApplication) getApplication()).data.getJSONObject("oauth");
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl(jsonObject.getString("sae"));
        webView.setWebViewClient(new MyWebClient());
    }

    private class MyWebClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            Toast.makeText(OAuthLoginActivity.this, "时间：" + System.currentTimeMillis(), Toast
                    .LENGTH_LONG).show();
            return true;
        }
    }
}
