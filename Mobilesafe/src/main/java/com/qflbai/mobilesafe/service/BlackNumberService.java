package com.qflbai.mobilesafe.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.telephony.PhoneStateListener;
import android.telephony.SmsMessage;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.android.internal.telephony.ITelephony;
import com.qflbai.mobilesafe.db.dao.BlackNumberDao;

import java.lang.reflect.Method;

public class BlackNumberService extends Service {

    public static final String TAG = "BlackNumberService";
    private InnerSmsReceiver mInnerSmsReceiver;
    private TelephonyManager mTM;
    private MyPhoneStateListener mPhoneStateListener;
    private BlackNumberDao mDao;
    private MyContentObserver mContentObserver;

    public BlackNumberService() {
    }
    @Override
    public IBinder onBind(Intent intent) {
       return  null;
    }
    @Override
    public void onCreate() {
        mDao = BlackNumberDao.getInstance(getApplicationContext());
       //注册一个广播事件，拦截短信
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.provider.Telephony.SMS_RECEIVED");
        filter.setPriority(Integer.MAX_VALUE);
        //注册广播
        mInnerSmsReceiver = new InnerSmsReceiver();
        registerReceiver(mInnerSmsReceiver,filter);

        /**
         * 拦截电话状态
         */
        mTM = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        //2,监听电话状态
        mPhoneStateListener = new MyPhoneStateListener();
        mTM.listen(mPhoneStateListener, PhoneStateListener.LISTEN_CALL_STATE);
    }

    class MyPhoneStateListener extends PhoneStateListener {
        //3,手动重写,电话状态发生改变会触发的方法
        @Override
        public void onCallStateChanged(int state, String incomingNumber) {
            switch (state) {
                case TelephonyManager.CALL_STATE_IDLE:
                    //空闲状态,没有任何活动(移除吐司)
                    Log.i(TAG, "挂断电话,空闲了.......................");
                    break;
                case TelephonyManager.CALL_STATE_OFFHOOK:
                    //摘机状态，至少有个电话活动。该活动或是拨打（dialing）或是通话
                    break;
                case TelephonyManager.CALL_STATE_RINGING:
                    Log.i(TAG, "响铃了.......................");
                    endCall(incomingNumber);
                    break;
            }
            super.onCallStateChanged(state, incomingNumber);
        }
    }
    private void endCall(String phone) {
        int mode = mDao.getMode(phone);
        if(mode == 2 || mode == 3){
            //ITelephony.Stub.asInterface(ServiceManager.getService(Context
            //       .TELEPHONY_SERVICE));
            //ServiceManager此类android对开发者隐藏,所以不能去直接调用其方法,
            // 需要反射调用
            try {
                //1,获取ServiceManager字节码文件import android.os.ServiceManager
                Class<?> clazz = Class.forName("android.os.ServiceManager");
                //2,获取方法
                Method method = clazz.getMethod("getService", String.class);
                //3,反射调用此方法
                IBinder iBinder = (IBinder) method.invoke(null,
                        Context.TELEPHONY_SERVICE);
                //4,调用获取aidl文件对象方法
               ITelephony iTelephony = ITelephony.Stub.asInterface(iBinder);
                //5,调用在aidl中隐藏的endCall方法
               iTelephony.endCall();
            } catch (Exception e) {
                e.printStackTrace();
            }
            //通过内容提供者和解析者删除通话记录的电话号码(要拦截的电话号码)
            //注册内容观察者监督数据的改变
            //6,在内容解析器上,去注册内容观察者,通过内容观察者,观察数据库(Uri决
            // 定那张表那个库)的变化
            mContentObserver = new MyContentObserver(new Handler(),phone);
            getContentResolver().registerContentObserver(
                    Uri.parse("content://call_log/calls"), true, mContentObserver);
        }

    }
    class InnerSmsReceiver extends BroadcastReceiver {
        //注册广播
        @Override
        public void onReceive(Context context, Intent intent) {
            //获取短信内容,获取发送短信电话号码,如果此电话号码在黑名单中,并且拦截
            // 模式也为1(短信)或者3(所有),拦截短信1,获取短信内容
            Object[] objects = (Object[]) intent.getExtras().get("pdus");
            //2,循环遍历短信过程
            for (Object object : objects) {
                //3,获取短信对象
                SmsMessage sms = SmsMessage.createFromPdu((byte[])object);
                //4,获取短信对象的基本信息
                String originatingAddress = sms.getOriginatingAddress();
                String messageBody = sms.getMessageBody();
                int mode = mDao.getMode(originatingAddress);
                if(mode == 1 || mode == 3){
                    //拦截短信(android 4.4版本失效短信数据库,删除)
                    abortBroadcast();//终止广播
                }
            }
        }
    }
    @Override
    public void onDestroy() {
        //当服务销毁的时候要反注册一下广播事件
        if(mInnerSmsReceiver != null){
            unregisterReceiver(mInnerSmsReceiver);
        }
        //注销内容观察者
        if(mContentObserver!=null){
            getContentResolver().unregisterContentObserver(mContentObserver);
        }
        //取消对电话状态的监听
        if(mPhoneStateListener!=null){
            mTM.listen(mPhoneStateListener,PhoneStateListener.LISTEN_NONE);
        }

        super.onDestroy();
    }
    class MyContentObserver extends ContentObserver {
        private String phone;
        public MyContentObserver(Handler handler, String phone) {
            super(handler);
            this.phone = phone;
        }
        //数据库中指定calls表发生改变的时候会去调用方法
        @Override
        public void onChange(boolean selfChange) {
            //插入一条数据后,再进行删除
            getContentResolver().delete(
                    Uri.parse("content://call_log/calls"), "number = ?", new String[]{phone});
            super.onChange(selfChange);
        }
    }
}
