package com.qflbai.mobilesafe.receiver;

import android.app.admin.DevicePolicyManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.telephony.SmsMessage;

import com.qflbai.mobilesafe.R;
import com.qflbai.mobilesafe.service.LocationService;
import com.qflbai.mobilesafe.util.ConstantValue;
import com.qflbai.mobilesafe.util.SpUtil;

public class SmsReceiver extends BroadcastReceiver {
    private static MediaPlayer mp;
    private DevicePolicyManager mDpm;
    private ComponentName mComponentName;

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.
        mDpm = (DevicePolicyManager) context
                .getSystemService(Context.DEVICE_POLICY_SERVICE);
        mComponentName = new ComponentName(context, AdminReceiver.class);
        // 广播接受者在每个接收到一个广播事件，重新new 一个广播接收者
        boolean bl = SpUtil.getBoolean(context, ConstantValue.OPEN_SECURITY,
                false);
        if (bl) {
            // //[1]获取发短信送的号码 和内容,获取短信内容,
            Object[] object = (Object[]) intent.getExtras().get("pdus");
            for (Object smsString : object) {
                // [2]获取smsmessage实例
                SmsMessage sm = SmsMessage.createFromPdu((byte[]) smsString);
                // [3]获取发送短信的基本信息
                String messageBody = sm.getMessageBody();
                // [4]获取发送者
                // String address = sm.getOriginatingAddress();
                if (messageBody.contains("#*alarm*#")) {
                    //如果短信的内容包含了"#*alarm*#",播放音乐
                    if (mp != null) {
                        mp.release();// 释放资源
                    }
                    mp = MediaPlayer.create(context, R.raw.ylzs);
                    // mp.prepare();准备工作
                    mp.setLooping(true);
                    // 在播放音乐之前，将系统的音量设置成大
                    // mp.setVolume(10f,1.0f);//设置成最大音量，音量比例
                    // 声音管理者
                    AudioManager am = (AudioManager) context
                            .getSystemService(Context.AUDIO_SERVICE);
                    // index：声音的大小0代表最小，15最大，flags:指定信息标签，
                    // streamType代表声音类型
                    am.setStreamVolume(AudioManager.STREAM_MUSIC, 15, 0);
                    // 设置音量大小
                    mp.start();
                }
                // #*location*#
                // 如果短信内容中包含#*location*#，开启服务
                if (messageBody.contains("#*location*#")) {
                    // 开启位置的服务
                    Intent intent2 = new Intent(context, LocationService.class);
                    context.startService(intent2);
                }
                // 远程锁屏#*lockscreen*##*wipedata*#
                if (messageBody.contains("#*lockscreen*#")) {
                    // 获取设备管理者
                    // 代码实现激活超级管理员
                    Intent intent1 = new Intent(
                            DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
                    intent1.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN,
                            mComponentName);
                    intent1.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION,
                            "设备管理器");
                    startActivity(intent1);
                    if (mDpm.isAdminActive(mComponentName)) {
                        mDpm.lockNow();
                    }
                }
                // 远程清除数据#*wipedata*#
                if (messageBody.contains("#*wipedata*#")) {
                    if (mDpm.isAdminActive(mComponentName)) {
                        mDpm.wipeData(0);//远程删除数据
                    }

                    /**
                     * //没有作为设备管理器的应用，可以卸载
                     *  没有作为设备管理器中有做激活，不可卸载，系统会提示取消
                     *   在设备管理器中的激活，然后才可以卸载
                     *
                     */
                    Intent intent1 = new Intent("android.intent.action.VIEW");
                    intent1.addCategory("android.intent.category.DEFAULT");
                    intent1.setData(Uri.parse("package:"+context.getPackageName
                            ()));
                    context.startActivity(intent1);


                }
            }
        }
    }
    private void startActivity(Intent intent2) {
        // TODO Auto-generated method stub
    }
}
