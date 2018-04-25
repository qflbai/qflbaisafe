package com.qflbai.mobilesafe.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;

import com.qflbai.mobilesafe.engine.ProcessInfoProvider;

public class LockScreenService extends Service {

    private InnerReceiver mInnerReceiver;

    public LockScreenService() {
    }
    @Override
    public IBinder onBind(Intent intent) {
      return null;
    }

    @Override
    public void onCreate() {

        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_SCREEN_OFF);

        //创建一个广播接受者
        mInnerReceiver = new InnerReceiver();

        //注册一个广播
        registerReceiver(mInnerReceiver,filter);
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {

        if(mInnerReceiver != null){
            unregisterReceiver(mInnerReceiver);
        }
        super.onDestroy();
    }

   class InnerReceiver extends BroadcastReceiver{

       @Override
       public void onReceive(Context context, Intent intent) {
           //收到广播，清理手机正在运行的进程
           ProcessInfoProvider.killProcessAll(context);
       }
   }
}
