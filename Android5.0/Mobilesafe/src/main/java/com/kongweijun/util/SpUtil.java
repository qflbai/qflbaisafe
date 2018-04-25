package com.kongweijun.util;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Administrator on 2017/2/21.
 */

public class SpUtil {
    private static SharedPreferences sp;
    /**
     * @param context 上下文环境
     * @param key     存储键的名称
     * @param value   存储键的内容
     */
    public static void putBoolean(Context context, String key, boolean value) {
        if (sp == null) {

            sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);
        }
        SharedPreferences.Editor edit = sp.edit();
        edit.putBoolean(key, value);
        edit.commit();
    }

    /**
     * @param context  上下文环境
     * @param key      取出的键名称
     * @param defValue 默认取出的键的内容
     * @return
     */
    public static boolean getBoolean(Context context, String key,
                                     boolean defValue) {
        if (sp == null) {
            sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);
        }
        return sp.getBoolean(key, defValue);
    }
    public static void putString(Context context, String key, String value) {
        if (sp == null) {
            sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);
        }
        SharedPreferences.Editor edit = sp.edit();
        edit.putString(key, value);
        edit.commit();
    }
    public static String getString(Context context, String key, String defValue) {
        if (sp == null) {
            sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);
        }
        return sp.getString(key, defValue);
    }
    /**
     * @param context 上下文环境
     * @param key     要移除的节点
     */
    public static void remove(Context context, String key) {
        if (sp == null) {
            sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);
        }
        SharedPreferences.Editor edit = sp.edit();
        edit.remove(key);
        edit.commit();
    }
}
