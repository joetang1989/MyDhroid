package com.example.administrator.myapplication.httpsutils;

/**
 * Author:JsonLu
 * DateTime:2015/9/17 9:51
 * Email:luxd@i_link.cc
 * Desc:
 **/

import android.app.Activity;
import android.content.Context;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;

import com.example.administrator.myapplication.utils.L;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.UUID;
import java.util.logging.Logger;


public class CallServer {
    private static CallServer _callServer;

    /**
     * 正式环境
     * private String HTTPS_URL = "https://m6.sumpay.cn/phoneserver/";
     * private String URL = "http://m6.sumpay.cn/phoneserver/";
     */
    // 测试环境（不需要SSL证书，无需HTTPS）
    private String HTTPS_URL = "https://m6.sumpay.cn/phoneserver/";
    private String URL = "http://192.168.8.201/phoneserver/";

    public synchronized static CallServer getInstance() {
        if (null == _callServer) {
            _callServer = new CallServer();
        }
        return _callServer;
    }

    private CallServer() {

    }

    /**
     * 使用HTTPS POST方式
     *
     * @param reqParmas
     * @return
     */
    private String goHttpsPost(String method,
                               HashMap<String, String> reqParmas, Context context) {
        L.i("http is : " + HTTPS_URL);
        L.i("method is : " + method);

        // String uri = HTTPS_URL + method + "?version=0";
        String uri = HTTPS_URL + method;
        L.i("uri is : " + uri);
        String result = null;
        HttpPost post = new HttpPost(HTTPS_URL + method);
        HttpResponse response;
        try {
            List<org.apache.http.NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
            Set<String> paramsKeySet = reqParmas.keySet();
            Iterator<String> ite = paramsKeySet.iterator();
            while (ite.hasNext()) {
                String key = ite.next();
                L.i("param is : " + key + " = " + reqParmas.get(key));
                nameValuePairs.add(new BasicNameValuePair(key, reqParmas
                        .get(key)));
            }
            post.setEntity(new UrlEncodedFormEntity(nameValuePairs, "utf-8"));
            /**
             * 创建Http Header
             */
            createPostHeader(context, post);

            DefaultHttpClient httpClient = (DefaultHttpClient) getHttpsClient(context);

            response = httpClient.execute(post);
            L.i("发出https请求----------了");

            if (response.getStatusLine().getStatusCode() != 404) {
                result = EntityUtils.toString(response.getEntity(), "utf-8");
            } else {
                L.i("" + response.getStatusLine().getStatusCode());
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        } finally {
            post.abort();
        }
        L.i("https请求服务器返回数据 : " + result);
        return result;
    }

    /**
     * HTTPS
     *
     * @return
     */
    private DefaultHttpClient getHttpsClient(Context context) {
        // DefaultHttpClient client = new MyHttpsClient(context);
        DefaultHttpClient client = new CustomHttpsClient(context);
        client.getParams().setIntParameter(
                HttpConnectionParams.CONNECTION_TIMEOUT,
                Constants.CONNECTION_TIME_OUT);
        client.getParams().setIntParameter(HttpConnectionParams.SO_TIMEOUT,
                Constants.CONNECTION_TIME_OUT);

        return client;
    }

    /**
     * HTTP
     *
     * @return
     */
    private static HttpClient getHttpClient() {
        HttpClient client = new DefaultHttpClient(new BasicHttpParams());
        client.getParams().setIntParameter(
                HttpConnectionParams.CONNECTION_TIMEOUT,
                Constants.CONNECTION_TIME_OUT);
        client.getParams().setIntParameter(HttpConnectionParams.SO_TIMEOUT,
                Constants.CONNECTION_TIME_OUT);
        return client;
    }

    /**
     * 使用HTTP POST方式
     *
     * @param post
     * @return
     */
    public String goHttpPost(String method, HashMap<String, String> reqParmas,
                             Context context) {
        L.i("url is : " + URL);
        L.i("method is : " + method);
        String result = null;
        HttpPost post = new HttpPost(URL + method);
        HttpResponse response;
        try {
            List<org.apache.http.NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
            Set<String> paramsKeySet = reqParmas.keySet();
            Iterator<String> ite = paramsKeySet.iterator();
            while (ite.hasNext()) {
                String key = ite.next();
                L.i("param is : " + key + " = " + reqParmas.get(key));
                nameValuePairs.add(new BasicNameValuePair(key, reqParmas
                        .get(key)));
            }
            post.setEntity(new UrlEncodedFormEntity(nameValuePairs, "utf-8"));

            /**
             * 创建Http Header
             */
            createPostHeader(context, post);

            DefaultHttpClient httpClient = (DefaultHttpClient) getHttpClient();

            response = httpClient.execute(post);

            if (response.getStatusLine().getStatusCode() != 404) {
                result = EntityUtils.toString(response.getEntity(), "utf-8");
            } else {
                L.i("" + response.getStatusLine().getStatusCode());
            }
        } catch (IOException e) {
            e.printStackTrace();
            L.i("CallServer | " + e.getMessage());
            return null;
        } finally {
            post.abort();
        }
        L.i("请求服务器返回数据 : " + result);
        return result;
    }

    /**
     * 请求服务器
     *
     * @param method
     * @param reqParmas
     * @return
     */
    public String callServer(String method, HashMap<String, String> reqParmas,
                             Context context) {
        return goHttpsPost(method, reqParmas, context);


    }

    /**
     * 创建头数据
     *
     * @param context
     */
    private void createPostHeader(Context context, HttpPost post) {
        TelephonyManager tm = (TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE);
        String imei = tm.getSimSerialNumber();
        String imsi = tm.getSubscriberId();

        DisplayMetrics metric = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay()
                .getMetrics(metric);
        int width = metric.widthPixels; // 屏幕宽度（像素）
        int height = metric.heightPixels; // 屏幕高度（像素）
        float density = metric.density; // 屏幕密度（0.75 / 1.0 / 1.5）
        int densityDpi = metric.densityDpi; // 屏幕密度DPI（120 / 160 / 240）

        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss",
                Locale.CHINESE);
        Date curDate = new Date(System.currentTimeMillis());// 获取当前时间
        String clientDate = formatter.format(curDate);

        L.i("imei is : " + imei);
        L.i("imsi is : " + imsi);
        L.i("width is : " + width);
        L.i("height is : " + height);
        L.i("density is : " + density);
        L.i("densityDpi is : " + densityDpi);
        L.i("Product Model: " + android.os.Build.MODEL + ","
                + android.os.Build.VERSION.RELEASE + ","
                + android.os.Build.MANUFACTURER);
        L.i("current date is : " + clientDate);

        post.setHeader("uniquecode", getMyUUID(context, tm));
        post.setHeader("imsi", imsi);
        post.setHeader("imei", imei);
        post.setHeader("ipaddr", getLocalIpAddress());
        post.setHeader("provider", tm.getSimOperatorName());
        post.setHeader("screenheight", String.valueOf(height));
        post.setHeader("screenwidth", String.valueOf(width));
        post.setHeader("ostype", "A");
        post.setHeader("osversion", android.os.Build.VERSION.RELEASE);
        post.setHeader("mobilefac", android.os.Build.MANUFACTURER);
        post.setHeader("mobilemod", android.os.Build.MODEL);
        post.setHeader("clientdate", clientDate);
    }

    /**
     * 获取ip地址
     *
     * @return
     */
    public String getLocalIpAddress() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface
                    .getNetworkInterfaces(); en.hasMoreElements(); ) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf
                        .getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress()) {
                        return inetAddress.getHostAddress().toString();
                    }
                }
            }
        } catch (SocketException ex) {
            L.e(ex.toString());
        }
        return "";
    }

    /**
     * 获取唯一标识码
     *
     * @param context
     * @param tm
     * @return
     */
    private String getMyUUID(Context context, TelephonyManager tm) {
        final String tmDevice, tmSerial, androidId;
        tmDevice = "" + tm.getDeviceId();
        tmSerial = "" + tm.getSimSerialNumber();
        androidId = ""
                + android.provider.Settings.Secure.getString(
                context.getContentResolver(),
                android.provider.Settings.Secure.ANDROID_ID);
        UUID deviceUuid = new UUID(androidId.hashCode(),
                ((long) tmDevice.hashCode() << 32) | tmSerial.hashCode());

        return deviceUuid.toString();
    }

}
