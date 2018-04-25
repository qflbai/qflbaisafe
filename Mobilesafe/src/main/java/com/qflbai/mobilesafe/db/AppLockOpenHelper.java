package com.qflbai.mobilesafe.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Administrator on 2017/3/2.
 * 创建表的SQL语句
 * create table blacknumber(_id integer primary key autoincrement ,phone varchar(20),mode varchar(5));
 */
public class AppLockOpenHelper extends SQLiteOpenHelper {

    public AppLockOpenHelper(Context context) {
        super(context,"applock.db",null,1);
    }

    /**
     * 创建一个数据库表名
     * @param db 创建数据库对象
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " +
                "applock(_id integer primary key autoincrement ," +
                "packagename varchar(50));");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}
