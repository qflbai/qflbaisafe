package com.qflbai.mobilesafe.engine;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.qflbai.mobilesafe.been.Child;
import com.qflbai.mobilesafe.been.Group;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/3/18.
 */

public class CommonNumberDao {
    //指定访问数据库的路径
    public static  String path = "/data/data/com.kongweijun" +
            ".mobilesafe/files/commonnum.db";

    /**
     * 开启数组(组)
     */
    public static  List<Group> getGroup() {
        SQLiteDatabase db = SQLiteDatabase.openDatabase(path, null,
                SQLiteDatabase.OPEN_READONLY);
        Cursor cursor = db.query("classlist", new String[]{"name","idx"},
                null, null, null,null, null);
        List<Group> groupList = new ArrayList<>();
        if((cursor.getCount()>0)&&(cursor != null)){
            while (cursor.moveToNext()){
                Group group = new Group();
                String name = cursor.getString(0);
                String idx = cursor.getString(1);
                group.setName(name);
                group.setIdx(idx);
                group.setChildList(getChild(idx));
                groupList.add(group);
            }
            cursor.close();
        }
        db.close();
        return groupList;
    }

    public static  List<Child> getChild(String idx){
        SQLiteDatabase db = SQLiteDatabase.openDatabase(path, null,
                SQLiteDatabase.OPEN_READONLY);
        Cursor cursor = db.query("table"+idx, new String[]{"_id","number","name"},
                null, null, null,null, null);
        List<Child> childList = new ArrayList<>();
        if((cursor.getCount()>0)&&(cursor != null)){
            while (cursor.moveToNext()){
                String id = cursor.getString(0);
                String number = cursor.getString(1);
                String name = cursor.getString(2);
                Child child = new Child(id, number, name);
                childList.add(child);
            }
            cursor.close();
        }
        db.close();
        return childList;
    }
}
