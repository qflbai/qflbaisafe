package com.qflbai.mobilesafe.util;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by Administrator on 2017/2/22.
 */

public class ToastUtil {

    /**
     * @param context---上下文环境
     * @param message----文本内容
     * @param i-----时长
     */
    public static void show(Context context, String message, int i) {
        Toast.makeText(context,message,i).show();
    }
}
