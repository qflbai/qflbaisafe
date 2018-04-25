package com.qflbai.mobilesafe.engine;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/2/26.
 */

public class VirusDao {
    //指定访问数据库的路径
    public static  String path = "/data/data/com.kongweijun" +
            ".mobilesafe/files/antivirus.db";

    /**
     * 打开一个已经存在的数据库
     */
    public static List<String> getVirusList(){

        SQLiteDatabase db = SQLiteDatabase.openDatabase(path, null, SQLiteDatabase
                .OPEN_READONLY);
        Cursor cursor = db.query("datable", new String[]{"md5"}, null, null,
                null, null, null);
        List<String> virusList = new ArrayList<>();
        if((cursor.getCount()>0&&(cursor != null))){
            while (cursor.moveToNext()){
                String md5 = cursor.getString(0);
                virusList.add(md5);
            }
            cursor.close();
        }
        db.close();
        return virusList;
    }
}
