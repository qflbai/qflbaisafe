package com.qflbai.mobilesafe.receiver;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.qflbai.mobilesafe.service.UpdateWidgetService;

/**
 * Created by Administrator on 2017/3/19.
 */

public class MyAppWidgetProvider extends AppWidgetProvider {

    private static final String TAG = "MyAppWidgetProvider";
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i(TAG, "onReceive............");
        super.onReceive(context, intent);
    }
    @Override
    public void onEnabled(Context context) {
        //创建第一个窗体小部件的方法
        Log.i(TAG, "onEnabled 创建第一个窗体小部件调用方法");
        //开启服务(onCreate)
        context.startService(new Intent(context, UpdateWidgetService.class));
        super.onEnabled(context);
    }
    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager,
                         int[] appWidgetIds) {
        Log.i(TAG, "onUpdate 创建多一个窗体小部件调用方法");
        //开启服务
        context.startService(new Intent(context, UpdateWidgetService.class));
        super.onUpdate(context, appWidgetManager, appWidgetIds);
    }
    @Override
    public void onAppWidgetOptionsChanged(Context context,
                                          AppWidgetManager appWidgetManager, int appWidgetId,
                                          Bundle newOptions) {
        //当窗体小部件宽高发生改变的时候调用方法,创建小部件的时候,也调用此方法
        //开启服务
        context.startService(new Intent(context, UpdateWidgetService.class));
        Log.i(TAG, "onAppWidgetOptionsChanged 创建多一个窗体小部件调用方法");
        super.onAppWidgetOptionsChanged(context, appWidgetManager, appWidgetId,
                newOptions);
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        Log.i(TAG, "onDeleted 删除一个窗体小部件调用方法");
        super.onDeleted(context, appWidgetIds);
    }

    @Override
    public void onDisabled(Context context) {
        Log.i(TAG, "onDisabled 删除最后一个窗体小部件调用方法");
        //关闭服务
        context.stopService(new Intent(context, UpdateWidgetService.class));
        super.onDisabled(context);
    }
}
