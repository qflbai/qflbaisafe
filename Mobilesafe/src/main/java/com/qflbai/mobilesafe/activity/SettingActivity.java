package com.qflbai.mobilesafe.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.qflbai.mobilesafe.R;
import com.qflbai.mobilesafe.service.AddressService;
import com.qflbai.mobilesafe.service.BlackNumberService;
import com.qflbai.mobilesafe.service.RocketService;
import com.qflbai.mobilesafe.service.WatchDogService;
import com.qflbai.mobilesafe.util.ConstantValue;
import com.qflbai.mobilesafe.util.ServiceUtil;
import com.qflbai.mobilesafe.util.SpUtil;
import com.qflbai.mobilesafe.util.ThemeUtil;
import com.qflbai.mobilesafe.view.SettingItemOnClick;
import com.qflbai.mobilesafe.view.SettingItemView;

public class SettingActivity extends Activity {
    private Context mContext;
    private SettingItemView mSiv_update;
    private SettingItemView mSiv_address;
    private String[] mDes;
    private SettingItemOnClick mSic_toastStyle;
    private int mAnInt;
    private SettingItemView mRocket_start_close;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(SpUtil.getInt(this,ConstantValue.CHANGE_TO_THEME,0)!= -1){
            ThemeUtil.setActivityTheme(this);
        }
        setContentView(R.layout.activity_setting);
        mContext = this;
        initUpdate();
        initAddress();
        initToastStyle();
        initLocation();
        initRocket();
        initBlackNumber();
        initLock();
    }

    /**
     * 开启程序锁的初始化
     */
    private void initLock() {
        final SettingItemView siv_lock = (SettingItemView) findViewById
                (R.id.siv_lock);
        boolean running = ServiceUtil.isRunning(mContext,
                "WatchDogService");
        siv_lock.setCheck(running);
        siv_lock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isCheck = siv_lock.isCheck();
                siv_lock.setCheck(!isCheck);
                if(!isCheck){
                    startService(new Intent(mContext,WatchDogService.class));
                }else {
                    stopService(new Intent(mContext, WatchDogService.class));
                }
            }
        });
    }

    /**
     * 初始化黑名单拦截
     */
    private void initBlackNumber() {
        final SettingItemView siv_blackNumber = (SettingItemView) findViewById
                (R.id.siv_blackNumber);
        boolean running = ServiceUtil.isRunning(mContext,
                "BlackNumberService");
        siv_blackNumber.setCheck(running);
        siv_blackNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isCheck = siv_blackNumber.isCheck();
                siv_blackNumber.setCheck(!isCheck);
                if(!isCheck){
                    startService(new Intent(mContext, BlackNumberService.class));
                }else {
                    stopService(new Intent(mContext, BlackNumberService.class));
                }
            }
        });

    }
    /**
     * 开启小火箭和关闭小火箭
     */
    private void initRocket() {
        mRocket_start_close = (SettingItemView) findViewById(R.id
                .siv_startAndClose);
        boolean isRunning = ServiceUtil.isRunning(mContext, "RocketService");
        mRocket_start_close.setCheck(isRunning);
        mRocket_start_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isCheck = mRocket_start_close.isCheck();
                mRocket_start_close.setCheck(!isCheck);
                //如果点击按钮开启我们需要开启一个服务,服务中管理吐司的显示
                //如果关闭服务，我们关闭服务，
                if(!isCheck){
                    Intent intent = new Intent(mContext, RocketService.class);
                    startService(intent);
                    finish();
                }else {
                    stopService(new Intent(mContext, RocketService.class));
                    finish();
                }
            }
        });
    }
    /**
     * 双击居中view 所在屏幕的位置的处理方法
     */
    private void initLocation() {
        SettingItemOnClick sic_location = (SettingItemOnClick) findViewById(R.id.sic_location);
        sic_location.setTitle("归属地提示框的位置");
        sic_location.setDec("设置归属地提示框的位置");
        sic_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(mContext,ToastLocationActivity.class));
            }
        });
    }
    private void initToastStyle() {
        mSic_toastStyle = (SettingItemOnClick) findViewById(R.id.sic_toast_style);
        mSic_toastStyle.setTitle("电话归属地样式选择");
        mDes = new String[]{"水红色","橙色","蓝色","灰色","绿色","紫色"};
        mAnInt = SpUtil.getInt(mContext, ConstantValue.TOAST_STYLE, 0);
        mSic_toastStyle.setDec(mDes[mAnInt]);
        mSic_toastStyle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //弹出一个对话框，弹出单选框
                showToastStyleDialog();
            }
        });
    }
    protected void showToastStyleDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setIcon(R.mipmap.toast_style);
        builder.setTitle("请选择归属地样式");
        //选择单个条目事件监听
        /*
         * 1:string类型的数组描述颜色文字数组
         * 2:弹出对画框的时候的选中条目索引值
         * 3:点击某一个条目后触发的点击事件
         * */
        builder.setSingleChoiceItems(mDes,mAnInt,new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {//which选中的索引值
                //(1,记录选中的索引值,2,关闭对话框,3,显示选中色值文字)
                mAnInt = which;
                SpUtil.putInt(getApplicationContext(), ConstantValue.TOAST_STYLE, which);
                dialog.dismiss();
                mSic_toastStyle.setDec(mDes[which]);
            }
        });
        //消极按钮
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
    }
    /**
     * 初始化电话归属地
     */
    private void initAddress() {
        mSiv_address = (SettingItemView) findViewById(R.id.siv_Address);
        boolean isRunning = ServiceUtil.isRunning(mContext,
                "AddressService");
        mSiv_address.setCheck(isRunning);//根据服务的状态来显示当前条目的状态
        mSiv_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isCheck = mSiv_address.isCheck();
                mSiv_address.setCheck(!isCheck);
                //如果点击按钮开启我们需要开启一个服务,服务中管理吐司的显示
                //如果关闭服务，我们关闭服务，
                if(!isCheck){
                    Intent intent = new Intent(mContext, AddressService.class);
                    startService(intent);
                }else {
                    stopService(new Intent(mContext, AddressService.class));
                }
            }
        });
    }
    /**
     * 初始化settingview条目的更新控件
     */
    private void initUpdate() {
        //从SpUtil中获取存储的开关节点状态，用做显示
        mSiv_update = (SettingItemView) findViewById(R.id.siv_update);
        boolean defIsChack = SpUtil.getBoolean(mContext, ConstantValue.OPEN_UPDATE,false);
        mSiv_update.setCheck(defIsChack);
        mSiv_update.setOnClickListener(new View.OnClickListener() {
            @Override
            /**
             * 如果之前是选中状态的，点击过后是未选中，反之则选中
             */
            public void onClick(View view) {
                boolean isCheck = mSiv_update.isCheck();
                mSiv_update.setCheck(!isCheck);
                //存储当前开关状态，为了下次方便显示
                SpUtil.putBoolean(mContext,ConstantValue.OPEN_UPDATE,!isCheck);
            }
        });
    }
}
