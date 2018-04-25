package com.qflbai.mobilesafe.util;

import android.app.ActivityManager;
import android.content.Context;

import java.util.List;

/**
 * Created by Administrator on 2017/2/27.
 */

public class ServiceUtil {

    private static ActivityManager mAm;

    /**
     * @param context 上下文环境
     * @param serviceName 开启服务的名称
     */
    public static boolean isRunning(Context context, String serviceName){
        //获取activity管理者对象
        mAm = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> runningServices = mAm
                .getRunningServices(100);//获取手机正在运行的服务 (多少个)
        for (ActivityManager.RunningServiceInfo rAP:runningServices){
            String className = rAP.service.getClassName();
            //遍历集合，获取所有服务进行和正在运行对比能够匹配。
            if(serviceName.equals(className)){
                return true;
            }
        }
        return false;
    }
}
