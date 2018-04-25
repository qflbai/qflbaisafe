package com.qflbai.mobilesafe.global;

import android.app.Application;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

/**
 * Created by Administrator on 2017/3/23.
 */

public class MyApplication extends Application{
    private static final String tag = "MyApplication";
    public MyApplication() {
        super();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        //捕获全局(应用任意模块)异常
        Thread.setDefaultUncaughtExceptionHandler(
                new Thread.UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(Thread thread, Throwable ex) {
                //在获取到了未捕获的异常后,处理的方法
                ex.printStackTrace();
                Log.i(tag, "捕获到了一个程序的异常");

                //将捕获的异常存储到sd卡中
                String path = Environment.getExternalStorageDirectory()
                        .getAbsoluteFile()+File.separator+"errorkong.log";
                File file = new File(path);
                try {
                    PrintWriter printWriter = new PrintWriter(file);
                    ex.printStackTrace(printWriter);
                    printWriter.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                //上传公司的服务器
                //结束应用
                System.exit(0);
            }
        });
    }
}
