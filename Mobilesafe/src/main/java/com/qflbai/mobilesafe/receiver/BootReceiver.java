package com.qflbai.mobilesafe.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.util.Log;
import com.qflbai.mobilesafe.util.ConstantValue;
import com.qflbai.mobilesafe.util.SpUtil;
public class BootReceiver extends BroadcastReceiver {
    private static final String TAG = "BootReceiver";
    private TelephonyManager tm;
    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.
        Log.i(TAG, "onReceive:我接受到了广播");
        // 1获取手机的sim卡的系列号
        tm = (TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE);
        String simSerialNumber = tm.getSimSerialNumber();
        // 2获取sp中的系列卡号和手机的作比较
        String sim_number = SpUtil.getString(context,
                ConstantValue.SIM_NUMBER, "");
        if (!simSerialNumber.equals(sim_number)) {
            // 3发送短信给选中联系的人
            SmsManager sms = SmsManager.getDefault();
            sms.sendTextMessage("5554", null, "sim change!!!", null, null);
        }
    }
}
