package com.qflbai.mobilesafe.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.qflbai.mobilesafe.engine.ProcessInfoProvider;

/**
 * Created by Administrator on 2017/3/19.
 */

public class KillProcessReceiver extends BroadcastReceiver{
    @Override
    public void onReceive(Context context, Intent intent) {
        //杀死进程
        ProcessInfoProvider.killProcessAll(context);
    }
}
