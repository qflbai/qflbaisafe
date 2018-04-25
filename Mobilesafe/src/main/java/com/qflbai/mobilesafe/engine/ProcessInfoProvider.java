package com.qflbai.mobilesafe.engine;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;

import com.qflbai.mobilesafe.been.ProcessInfo;
import com.qflbai.mobilesafe.R;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/3/9.
 */

public class ProcessInfoProvider {
    //获取进程总数的方法
    public static int getProcessCount(Context context){
        //获取Activity管理者对象
        ActivityManager am  = (ActivityManager) context.getSystemService
                (Context.ACTIVITY_SERVICE);
        //2,获取正在运行的进程总数
        List<ActivityManager.RunningAppProcessInfo> rap = am
                .getRunningAppProcesses();
        int count = rap.size();
        return count;
    }

    /**
     *
     * @param context
     * @return返回可用的内存bytes大小
     */
    public static long getAvailSpace(Context context){
        ActivityManager am  = (ActivityManager) context.getSystemService
                (Context.ACTIVITY_SERVICE);
       //构建存储可用内存的对象
        ActivityManager.MemoryInfo memoryInfo = new ActivityManager.MemoryInfo();
        //给memoryInfo对象赋值
        am.getMemoryInfo(memoryInfo);
        //获取memoryInfo中响应的可用内存大小
        long availMem = memoryInfo.availMem;
        return availMem;
    }

    /**
     * @param context
     * @return 返回总内存的大小
     */
    public static long getTotalSpace(Context context){
        ActivityManager am  = (ActivityManager) context.getSystemService
                (Context.ACTIVITY_SERVICE);
        //构建存储可用内存的对象
        ActivityManager.MemoryInfo memoryInfo = new ActivityManager.MemoryInfo();
        //给memoryInfo对象赋值
        am.getMemoryInfo(memoryInfo);
        //获取memoryInfo中响应的可用内存大小
        long totalMem = memoryInfo.totalMem;
        return totalMem;
    }

    public static long getTotalSppce(){
        //内存大小写入文件中,读取proc/meminfo文件,读取第一行,获取数字字符,转换成bytes返回
        FileReader fileReader  = null;
        BufferedReader bufferedReader = null;
        try {
            fileReader= new FileReader("proc/meminfo");
            bufferedReader = new BufferedReader(fileReader);
            String lineOne = bufferedReader.readLine();
            //将字符串转换成字符的数组
            char[] charArray = lineOne.toCharArray();
            //循环遍历每一个字符,如果此字符的ASCII码在0到9的区域内,说明此字符有效
            StringBuffer stringBuffer = new StringBuffer();
            for (char c : charArray) {
                if(c>='0' && c<='9'){
                    stringBuffer.append(c);
                }
            }
            return Long.parseLong(stringBuffer.toString())*1024;
        } catch (Exception e) {
            e.printStackTrace();
        }finally{
            try {
                if(fileReader!=null && bufferedReader!=null){
                    fileReader.close();
                    bufferedReader.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return 0;
    }

    public static List<ProcessInfo> getProcessInfo(Context context){
        //获取进程相关信息
        List<ProcessInfo> processInfoList = new ArrayList<ProcessInfo>();
        //1,activityManager管理者对象和PackageManager管理者对象
        ActivityManager am = (ActivityManager)context.getSystemService(Context.
                ACTIVITY_SERVICE);
        PackageManager pm =context.getPackageManager();
        //2,获取正在运行的进程的集合
        List<ActivityManager.RunningAppProcessInfo> runningAppProcesses = am .
                getRunningAppProcesses();
        //3,循环遍历上诉集合,获取进程相关信息(名称,包名,图标,使用内存大小,
        // 是否为系统进程(状态机))
        for (ActivityManager.RunningAppProcessInfo info : runningAppProcesses) {
            ProcessInfo processInfo = new ProcessInfo();
            //4,获取进程的名称 == 应用的包名
            processInfo.setPackageName(info.processName);
            //5,获取进程占用的内存大小(传递一个进程对应的pid数组)
            android.os.Debug.MemoryInfo[] processMemoryInfo = am
                    .getProcessMemoryInfo(new int[]{info.pid});
            //6,返回数组中索引位置为0的对象,为当前进程的内存信息的对象
            android.os.Debug.MemoryInfo memoryInfo = processMemoryInfo[0];
            //7,获取已使用的大小
            processInfo.setMemSize(memoryInfo.getTotalPrivateDirty() * 1024);
            try {
                ApplicationInfo applicationInfo = pm.getApplicationInfo
                        (processInfo.getPackageName(), 0);
                //8,获取应用的名称
                processInfo.setName(applicationInfo.loadLabel(pm).toString());
                //9,获取应用的图标
                processInfo.setIcon(applicationInfo.loadIcon(pm));
                //10,判断是否为系统进程
                if((applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) ==
                        ApplicationInfo.FLAG_SYSTEM){
                    processInfo.setSystem(true);
                    //用户程序
                }else{
                    processInfo.setSystem(false);
                    //系统程序
                }
            } catch (PackageManager.NameNotFoundException e) {
                //需要处理
                processInfo.setName(info.processName);
                processInfo.setIcon(context.getResources().getDrawable
                        (R.drawable.ic_launcher));
                processInfo.setSystem(true);
                e.printStackTrace();
            }
            processInfoList.add(processInfo);
        }
        return processInfoList;
    }
    /**
     * 杀进程方法
     * @param context 上下文环境
     * @param processInfo 杀死进程所在的javabean的对象
     */
    public static void killProcess(Context context,ProcessInfo processInfo) {
        //1,获取activityManager
        ActivityManager am = (ActivityManager)context.getSystemService(Context
                .ACTIVITY_SERVICE);
        //2,杀死指定包名进程(权限)
        am.killBackgroundProcesses(processInfo.getPackageName());
    }

    public static void killProcessAll(Context context) {
        //1,获取activityManager
        ActivityManager am = (ActivityManager)context.getSystemService(Context
                .ACTIVITY_SERVICE);
        //2,杀死指定包名进程(权限)
        List<ActivityManager.RunningAppProcessInfo> runningAppProcesses = am .
                getRunningAppProcesses();
        for (ActivityManager.RunningAppProcessInfo info :
        runningAppProcesses) {

            if(info.processName.equals(context.getPackageName())){
                continue;
            }
            am.killBackgroundProcesses(info.processName);
        }
    }
}
