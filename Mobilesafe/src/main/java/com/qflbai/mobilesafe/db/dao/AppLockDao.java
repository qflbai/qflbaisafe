package com.qflbai.mobilesafe.db.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import com.qflbai.mobilesafe.db.AppLockOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * 为了方便管理,我们创建一个单利模式来管理增删查改
 * 私有化构造函数
 *声明一个当前类的对象
 *提供一个方法来，如果当前类的对象为空，就创建一个新的对象
 * Created by Administrator on 2017/3/2.
 */
public class AppLockDao {

    private  Context context;
    private AppLockOpenHelper mDb;

    private AppLockDao(Context context){

        this.context = context;
        //创建数据库已有的对象
        mDb = new AppLockOpenHelper(context);
    }
    private static AppLockDao mAppLockDao = null;

    public static AppLockDao getInstance(Context context){
        if(mAppLockDao == null){
            mAppLockDao = new AppLockDao(context);
        }
        return mAppLockDao;
    }

    /**
     * @param packagename 应用包名
     */
    public void insert(String packagename){
        SQLiteDatabase db = mDb.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("packagename",packagename);
        db.insert("applock",null,values);
        db.close();
        context.getContentResolver()
                .notifyChange(Uri.parse("content://applock/change"),null);
    }

    public void delete(String packagename){
        SQLiteDatabase db = mDb.getWritableDatabase();
        db.delete("applock","packagename = ?",new String[]{packagename});
        db.close();
        context.getContentResolver()
                .notifyChange(Uri.parse("content://applock/change"),null);
    }
    public List<String> findAll(){
        SQLiteDatabase db = mDb.getWritableDatabase();
        Cursor cursor = db.query("applock", new String[]{"packagename"}, null,
                null, null, null, null);
        List<String> list = new ArrayList<>();
        if((cursor != null)&&(cursor.getCount() >0)){
            while (cursor.moveToNext()){
                String packageName = cursor.getString(0);
                list.add(packageName);
            }
            cursor.close();
        }
        db.close();
        return list;
    }
}
