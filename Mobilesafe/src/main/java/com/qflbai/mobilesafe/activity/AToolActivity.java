package com.qflbai.mobilesafe.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.TextView;

import com.qflbai.mobilesafe.engine.SmsBackUp;
import com.qflbai.mobilesafe.R;
import com.qflbai.mobilesafe.util.ToastUtil;

import java.io.File;

public class AToolActivity extends Activity {

    private View tv_Address;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_atool);
        mContext = this;
        initPhoneAddress();//归属地的查询
        initSmsBackup();//备份短信的方法
        initCommonNumberQuery();//常用号码查询
        initAppLock();
        initRestoresms();
    }

    /**
     * 短信还原
     */
    private void initRestoresms() {

        TextView tv_restoresms = (TextView) findViewById(R.id
                .tv_aToolActivity_restoresms);
        tv_restoresms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //解析xml
                //XmlPullParser xmlPullParser = Xml.newPullParser();
                //插入短信
                ContentResolver resolver = getContentResolver();
                Uri uri = Uri.parse("content://sms");
                ContentValues values = new ContentValues();
                values.put("address", "95588");
                values.put("date", System.currentTimeMillis());
                values.put("type", 1);
                values.put("body", "zhuan zhang le $10000000000000000000");
                resolver.insert(uri, values);
                ToastUtil.show(getApplication(),"短信还原成功!",0);
            }
        });
    }

    /**
     * 给程序枷锁和解锁
     */
    private void initAppLock() {
        TextView tv_applock = (TextView) findViewById(R.id
                .tv_applock);
        tv_applock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(mContext,
                        AppLockActivity.class));
           /* finish();*/
            }
        });
    }

    /**
     * 常用号码查询
     */
    private void initCommonNumberQuery() {
        TextView tv_commonnumber_query = (TextView) findViewById(R.id.
                tv_commonnumber_query);
       tv_commonnumber_query.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               startActivity(new Intent(mContext,
                       CommonNumberQueryActivity.class));
           /* finish();*/
           }
       });
    }

    /**
     * 备份短信的初始化方法
     */
    private void initSmsBackup() {
        TextView tv_Backup = (TextView) findViewById(R.id.tv_aToolActivity_backup);
        tv_Backup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //设置带进度条的一个对话框
                final ProgressDialog pd = new ProgressDialog(mContext);
                pd.setIcon(R.mipmap.sms_backup);
                pd.setTitle("短信备份");
                //指定进度条的样式(进度条水平)
                pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                pd.show();
                new Thread(){
                    @Override
                    public void run() {
                        String path = Environment.getExternalStorageDirectory().
                            getAbsolutePath() + File.separator + "SmsBackUp.xml";
                        SmsBackUp.backUp(mContext, path, new SmsBackUp.CallBack(){
                            @Override
                            public void setMax(int max) {
                                pd.setMax(max);
                            }
                            @Override
                            public void setProgress(int value) {
                                pd.setProgress(value);
                            }
                        });
                        pd.dismiss();
                    }
                }.start();
            }
        });
    }

    private void initPhoneAddress() {
        tv_Address = findViewById(R.id.tv_aToolActivity_Address);
        tv_Address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(mContext,QueryAddressActivity.class));
            }
        });
    }
}
