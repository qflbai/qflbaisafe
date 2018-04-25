package com.qflbai.mobilesafe.service;

import android.app.Service;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.telephony.SmsManager;

public class
LocationService extends Service {
    public LocationService() {
    }
    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return null;
    }
    @Override
    public void onCreate() {
        // 获取手机的经纬度坐标
        // 获取位置管理者对象
        LocationManager lm = (LocationManager) getSystemService(LOCATION_SERVICE);
        // 以最优的方式获取经纬度坐标
        Criteria criteria = new Criteria();
        // 允许花费
        criteria.setCostAllowed(true);
        // 获取经纬度的精确
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        String bestProvider = lm.getBestProvider(criteria, true);
        // 在一定的时间间隔和移动一定距离后获取经纬度坐标
        lm.requestLocationUpdates(bestProvider, 0, 0, new LocationListener() {
            @Override
            public void onStatusChanged(String provider, int status,
                                        Bundle extras) {
            }
            @Override
            public void onProviderEnabled(String provider) {
            }

            @Override
            public void onProviderDisabled(String provider) {
            }
            public void onLocationChanged(Location location) {
                double jd = location.getLongitude();// ----获取经度
                double wd = location.getLatitude();// ----获取维度
                // 发送短信
                SmsManager sm = SmsManager.getDefault();
                sm.sendTextMessage("5556", null, "jd or wd:" + "(" + jd + ","
                        + wd + ")", null, null);
                System.out.println(jd+""+wd);
            }
        });
        super.onCreate();
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
