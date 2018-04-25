package com.kongweijun.util;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by Administrator on 2017/2/19.
 */

public class ToastUtil {
    /**
     * @param context 上下文环境
     * @param content 显示内容
     * @param time 显示时长
     */
    public static  void show(Context context,String content,int time){
        Toast.makeText(context,content,time).show();
    }
}
