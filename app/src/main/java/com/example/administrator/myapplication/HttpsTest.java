package com.example.administrator.myapplication;

import android.os.Bundle;

import com.example.administrator.myapplication.httpsutils.CallServer;
import com.example.administrator.myapplication.utils.L;

import net.duohuo.dhroid.activity.BaseActivity;
import net.duohuo.dhroid.net.DhNet;
import net.duohuo.dhroid.net.NetTask;
import net.duohuo.dhroid.net.Response;

import org.json.JSONException;

import java.io.IOException;
import java.util.HashMap;

/**
 * Author:JsonLu
 * DateTime:2015/9/17 10:13
 * Email:luxd@i_link.cc
 * Desc:
 **/
public class HttpsTest extends BaseActivity{
    private CallServer callServer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrollview);
        try {
            goHttps();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void goHttp() throws JSONException {
        DhNet net = new DhNet();
        net.setUrl("https://192.168.8.201/phoneserver/getLocationLists.do");
        net.doGet(new NetTask(this) {
            @Override
            public void doInUI(Response response, Integer transfer) throws JSONException {
                L.e(response.plain());
            }
        });
    }

    public HttpsTest(){
    }
    private void goHttps() throws IOException {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                HashMap<String, String> param = new HashMap<String, String>();
                param.put("data","data");
                String data = CallServer.getInstance().callServer("", param, HttpsTest.this);
                L.d("HTTPS:"+data);
//                DefaultHttpClient client = new DefaultHttpClient();
//                HttpGet get = new HttpGet("https://192.168.8.201/phoneserver/getLocationLists.do");
//                HttpResponse response = null;
//                try {
//                    response = client.execute(get);
//                    InputStream in = response.getEntity().getContent();
//                    ByteArrayOutputStream baos   =   new   ByteArrayOutputStream();
//                    int   i=-1;
//                    while((i=in.read())!=-1){
//                        baos.write(i);
//                    }
//                    L.e(baos.toString());
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
            }
        });
        thread.start();


    }

}
