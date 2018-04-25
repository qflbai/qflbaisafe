package com.qflbai.mobilesafe.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.qflbai.mobilesafe.R;
import com.qflbai.mobilesafe.service.LockScreenService;
import com.qflbai.mobilesafe.util.ConstantValue;
import com.qflbai.mobilesafe.util.ServiceUtil;
import com.qflbai.mobilesafe.util.SpUtil;

public class ProcessSettingActivity extends Activity {

    private CheckBox cb_show_system;
    private CheckBox cb_lock_clear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_process_setting);
        initUI();
        initShowSystem();
        initLockClear();
    }

    private void initLockClear() {

        boolean running = ServiceUtil.isRunning(this,
                "LockScreenService");
        cb_lock_clear.setChecked(running);
        if(running){
            cb_lock_clear.setText("锁屏清理已开启");
        }else{
            cb_lock_clear.setText("锁屏清理已关闭");
        }
        cb_lock_clear.setOnCheckedChangeListener(new CompoundButton
                .OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {
                Intent intent = new Intent(getApplicationContext(),
                        LockScreenService.class);
                if(isChecked){
                    //开启服务
                    cb_lock_clear.setText("锁屏清理已开启");
                    startService(intent);
                }else{
                    //关闭服务
                    cb_lock_clear.setText("锁屏清理已关闭");
                    stopService(intent);
                }
            }
        });
    }

    private void initShowSystem() {

        boolean is_show_system = SpUtil.getBoolean(this, ConstantValue.
                SHOW_SYSTEM, false);
        cb_show_system.setChecked(is_show_system);
        if(is_show_system){
            cb_show_system.setText("显示系统进程");
        }else {
            cb_show_system.setText("隐藏系统进程");
        }
        cb_show_system.setOnCheckedChangeListener(new CompoundButton
                .OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,  boolean
                    isChecked) {
                //isChecked就作为选中的状态
                if(isChecked){
                    cb_show_system.setText("显示系统进程");
                }else {
                    cb_show_system.setText("隐藏系统进程");
                }
                SpUtil.putBoolean(ProcessSettingActivity.this,ConstantValue
                        .SHOW_SYSTEM,isChecked);
            }
        });
    }

    private void initUI() {
        cb_lock_clear = (CheckBox) findViewById(R.id.cb_lock_clear);
        cb_show_system = (CheckBox) findViewById(R.id.cb_show_system);
    }
}
