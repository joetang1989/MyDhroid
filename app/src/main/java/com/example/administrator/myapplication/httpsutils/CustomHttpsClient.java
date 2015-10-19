package com.example.administrator.myapplication.httpsutils;

import android.content.Context;

import com.example.administrator.myapplication.utils.L;

import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.SingleClientConnManager;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

/**
 * Author:JsonLu
 * DateTime:2015/9/17 9:16
 * Email:luxd@i_link.cc
 * Desc:
 **/
public class CustomHttpsClient extends DefaultHttpClient {

    private Context context;

    public CustomHttpsClient(Context context) {
        super();
        this.context = context;
    }

    @Override
    protected ClientConnectionManager createClientConnectionManager() {

        try {
            KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
            trustStore.load(null, null);
            CustomSSLSocketFactory mySSlSocketFactory = new CustomSSLSocketFactory(trustStore);
            SchemeRegistry registry = new SchemeRegistry();
            registry.register(new Scheme("http", PlainSocketFactory
                    .getSocketFactory(), 80));
            registry.register(new Scheme("https", mySSlSocketFactory, 443));

            return new SingleClientConnManager(getParams(), registry);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    private class CustomSSLSocketFactory extends SSLSocketFactory {
        SSLContext sslContext = SSLContext.getInstance("TLS");


        public CustomSSLSocketFactory(KeyStore truststore) throws NoSuchAlgorithmException,
                KeyManagementException, KeyStoreException,
                UnrecoverableKeyException {
            super(truststore);
            sslContext.init(null, new TrustManager[]{new CustomTrustManager()}, null);
        }

        @Override
        public Socket createSocket() throws IOException {
            return sslContext.getSocketFactory().createSocket();
        }


        @Override
        public Socket createSocket(Socket socket, String host, int port,
                                   boolean autoClose) throws IOException, UnknownHostException {
            return sslContext.getSocketFactory().createSocket(socket, host, port, autoClose);
        }
    }

    private class CustomTrustManager implements X509TrustManager {

        @Override
        public void checkClientTrusted(X509Certificate[] chain, String authType)
                throws CertificateException {
            L.i("CustomTrustManager----");

        }

        @Override
        public void checkServerTrusted(X509Certificate[] chain, String authType) {
            L.i("checkServerTrusted----");
            Certificate cer = null;
            InputStream ins;
            try {
                ins = context.getAssets().open("new_sumpay.cer");
                CertificateFactory cerFactory = CertificateFactory.getInstance("X.509");
                cer = cerFactory.generateCertificate(ins);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (CertificateException e) {
                L.e("no peer cer----->");
            }

            for (int i = 0; i < chain.length; i++) {
                Certificate certificate = chain[i];
                if (cer.equals(certificate)) {
                    L.i("success----->");
                    return;
                }
            }


        }

        @Override
        public X509Certificate[] getAcceptedIssuers() {
            L.i("getAcceptedIssuers----");
            return null;
        }

    }


}
