package com.example.administrator.myapplication.scrollview;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.example.administrator.myapplication.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Author:JsonLu
 * DateTime:2015/9/15 15:51
 * Email:luxd@i_link.cc
 * Desc:
 **/
public class ScrollviewActivity extends Activity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrollview);
        ListView listView = (ListView)findViewById(R.id.title_bar1);

        SimpleAdapter simpleAdapter = new SimpleAdapter(this, getData(), R.layout.item_scrollview, new String[]{"title", "name", "sex", "age"}, new int[]{R.id.test1, R.id.test2, R.id.test3, R.id.test4});

        listView.setAdapter(simpleAdapter);
        setListViewHeightBasedOnChildren(listView);

    }

    private List<Map<String, Object>> getData() {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("title", "是谁");
        map.put("name", "JsonLu");
        map.put("sex", "12");
        map.put("age", "男");
        list.add(map);
        map = new HashMap<String, Object>();
        map.put("title", "是谁");
        map.put("name", "JsonLu");
        map.put("sex", "12");
        map.put("age", "男");
        list.add(map);
        map = new HashMap<String, Object>();
        map.put("title", "是谁");
        map.put("name", "JsonLu");
        map.put("sex", "12");
        map.put("age", "男");
        list.add(map);
        map = new HashMap<String, Object>();
        map.put("title", "是谁");
        map.put("name", "JsonLu");
        map.put("sex", "12");
        map.put("age", "男");
        list.add(map);
        map = new HashMap<String, Object>();
        map.put("title", "是谁");
        map.put("name", "JsonLu");
        map.put("sex", "12");
        map.put("age", "男");
        list.add(map);
        map = new HashMap<String, Object>();
        map.put("title", "是谁");
        map.put("name", "JsonLu");
        map.put("sex", "12");
        map.put("age", "男");
        list.add(map);map = new HashMap<String, Object>();
        map.put("title", "是谁");
        map.put("name", "JsonLu");
        map.put("sex", "12");
        map.put("age", "男");
        list.add(map);map = new HashMap<String, Object>();
        map.put("title", "是谁");
        map.put("name", "JsonLu");
        map.put("sex", "12");
        map.put("age", "男");
        list.add(map);map = new HashMap<String, Object>();
        map.put("title", "是谁");
        map.put("name", "JsonLu");
        map.put("sex", "12");
        map.put("age", "男");
        list.add(map);
        map = new HashMap<String, Object>();
        map.put("title", "是谁");
        map.put("name", "JsonLu");
        map.put("sex", "12");
        map.put("age", "男");
        list.add(map);
        return list;
    }

    private void setListViewHeightBasedOnChildren(ListView listView) {

        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }

        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight
                + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }

}
