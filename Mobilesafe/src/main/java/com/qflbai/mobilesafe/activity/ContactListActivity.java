package com.qflbai.mobilesafe.activity;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.qflbai.mobilesafe.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
public class ContactListActivity extends Activity {
    private Context mContext;
    private ListView lv;
    private MyAdapter mAdapter;
    private HashMap<String, String> hashMap;
    private List<HashMap<String, String>> lists;

    private Handler mHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            mAdapter = new MyAdapter();
            lv.setAdapter(mAdapter);
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_list);
        mContext = this;
        initUI();
        initData();
    }
    /**
     * 获取系统联系人数据方法
     */
    private void initData() {
        new Thread() {
            public void run() {
                initContentResolverData();
            };
        }.start();
    }
    /**
     * 从联系人数据库中获取数据，是通过内容解析者获取数据
     */
    private void initContentResolverData() {
        // 读取系统联系人数据步骤
        // [1]获取内容解析器
        ContentResolver contentResolver = getContentResolver();
        // 获取URL地址，查看系统联系人数据库，内容提供者源码
        // url:content://com.android.contacts/+表名(raw_contacts/data)
        Uri uri = Uri
                .parse("content://com.android.contacts/raw_contacts");
        Cursor query = contentResolver.query(uri,
                new String[] { "contact_id" }, null, null, null);
        lists = new ArrayList<HashMap<String, String>>();
        if (query != null && query.getCount() > 0) {
            while (query.moveToNext()) {
                String contact_id = query.getString(0);
                if(contact_id == null){
                    continue;
                }
                Uri uri_data = Uri
                        .parse("content://com.android.contacts/data");
                Cursor cursor = contentResolver.query(uri_data,
                        new String[] { "data1", "mimetype" },
                        "raw_contact_id=?",
                        new String[] { contact_id }, null);
                hashMap = new HashMap<String, String>();
                if (cursor != null && cursor.getCount() > 0) {

                    while (cursor.moveToNext()) {

                        String data1 = cursor.getString(0);
                        String mimetype = cursor.getString(1);
                        if (!TextUtils.isEmpty(data1)) {
                            if (mimetype
                                    .equals("vnd.android.cursor.item/phone_v2")) {
                                hashMap.put("phone", data1);
                            } else if (mimetype
                                    .equals("vnd.android.cursor.item/name")) {
                                hashMap.put("name", data1);
                            }
                        }
                    }
                    lists.add(hashMap);
                    cursor.close();
                }
            }
            query.close();
        }
        // 发送一个空的消息，告诉主线程可以去使用子线程已经填充好的数据
        mHandler.sendEmptyMessage(0);
    }
    /**
     * 初始UI控件的id值
     */
    private void initUI() {

        lv = (ListView) findViewById(R.id.lv_contact_list_activity);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                if (mAdapter != null) {
                    HashMap<String, String> map = mAdapter.getItem(position);
                    String phone = map.get("phone");
                    Intent intent = new Intent();
                    intent.putExtra("phone", phone);
                    setResult(0, intent);
                    finish();
                }

            }
        });
    }
    private class MyAdapter extends BaseAdapter {

        @Override
        public int getCount() {

            return lists.size();
        }

        @Override
        public HashMap<String, String> getItem(int position) {

            return lists.get(position);
        }

        @Override
        public long getItemId(int position) {

            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = View.inflate(mContext, R.layout.listview_contact_item,
                    null);
            TextView tvNumber = (TextView) view
                    .findViewById(R.id.tv_listview_contact_item_number);
            TextView tvDec = (TextView) view
                    .findViewById(R.id.tv_listview_contact_item_dec);
            HashMap<String, String> hashMap2 = lists.get(position);
            tvDec.setText(hashMap2.get("name"));
            tvNumber.setText(hashMap2.get("phone"));
            return view;
        }

    }
}

