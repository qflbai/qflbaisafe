package com.qflbai.mobilesafe.receiver;

import android.app.admin.DeviceAdminReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;

public class AdminReceiver extends DeviceAdminReceiver {

    private ComponentName mDeviceAdminSample;

    public AdminReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.
      /*  mDeviceAdminSample = new ComponentName(context, AdminReceiver.class);
        Intent intent1 = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
        intent1.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN,
                mDeviceAdminSample);
        intent1.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION,
               "设备管理器");
        context.startActivity(intent);*/
    }
}
