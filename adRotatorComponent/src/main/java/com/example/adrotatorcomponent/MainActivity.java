package com.example.adrotatorcomponent;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.adrotatorcomponent.view.Advertisements;
import com.example.adrotatorcomponent.volley.RequestManager;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

public class MainActivity extends Activity {

	private LinearLayout llAdvertiseBoard;
	private LayoutInflater inflater;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		RequestManager.init(this);
		inflater = LayoutInflater.from(this);
		initViews();
	}

	private void initViews() {
		llAdvertiseBoard = (LinearLayout) this.findViewById(R.id.llAdvertiseBoard);
		// 添加图片的Url地址
		JSONArray advertiseArray = new JSONArray();
		try {
			JSONObject head_img0 = new JSONObject();
			head_img0.put("head_img","http://pic.nipic.com/2008-08-12/200881211331729_2.jpg");
			JSONObject head_img1 = new JSONObject();
			head_img1.put("head_img","http://pic1.ooopic.com/uploadfilepic/sheji/2010-01-12/OOOPIC_1982zpwang407_20100112ae3851a13c83b1c4.jpg");
			JSONObject head_img2 = new JSONObject();
			head_img2.put("head_img","http://pic1.ooopic.com/uploadfilepic/sheji/2009-09-12/OOOPIC_wenneng837_200909122b2c8368339dd52a.jpg");
			JSONObject head_img3 = new JSONObject();
			head_img3.put("head_img","http://img.xiaba.cvimage.cn/4cbc56c1a57e26873c140000.jpg");
			advertiseArray.put(head_img0);
			advertiseArray.put(head_img1);
			advertiseArray.put(head_img2);
			advertiseArray.put(head_img3);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		llAdvertiseBoard.addView(new Advertisements(this, true, inflater, 3000).initView(advertiseArray));
	}

}
