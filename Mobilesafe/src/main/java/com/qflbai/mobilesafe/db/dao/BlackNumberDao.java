package com.qflbai.mobilesafe.db.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.qflbai.mobilesafe.db.BalckNumberOpenHelper;
import com.qflbai.mobilesafe.domain.BlackNumberInfo;

import java.util.ArrayList;
/**
 * 为了方便管理,我们创建一个单利模式来管理增删查改
 * 私有化构造函数
 *声明一个当前类的对象
 *提供一个方法来，如果当前类的对象为空，就创建一个新的对象
 * Created by Administrator on 2017/3/2.
 */
public class BlackNumberDao {

    private final BalckNumberOpenHelper mDb;
    private BlackNumberInfo mInfo;

    private  BlackNumberDao(Context context){
        //创建数据库已有的对象
        mDb = new BalckNumberOpenHelper(context);
    }
    private static BlackNumberDao mBlackNumberDao = null;

    public static BlackNumberDao getInstance(Context context){
        if(mBlackNumberDao == null){
            mBlackNumberDao = new BlackNumberDao(context);
        }
        return mBlackNumberDao;
    }

    /**
     * @param phone 增加的方法
     * @param mode 增加数据的模式（1代表电话，2代表短信，3代表所有）
     */
    //增加一个条目
    public void insert(String phone,String mode){
        //开启数据库，准备做写入操作
        SQLiteDatabase db = mDb.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("phone",phone);
        values.put("mode",mode);
        //table: 表名 , nullColumnHack：可以为空，标示添加一个空行, values:数据
        // 一行的值 , 返回值：代表添加这个新行的Id ，-1代表添加失败
        db.insert("blacknumber",null,values);
        db.close();
    }
    /**
     * @param phone 要删除的电话号码
     */
    public void delete(String phone){
        SQLiteDatabase db = mDb.getWritableDatabase();
        //table ：表名, whereClause: 删除条件, whereArgs：条件的占位符的参数 ;
        // 返回值：成功删除多少行
        db.delete("blacknumber","phone = ?",new String[]{phone});
        db.close();
    }
    /**
     * 根据电话号码更新拦截模式
     * @param phone 更新拦截模式的电话号码
     * @param mode 要更新为的模式
     */
    public void update(String phone,String mode){
        SQLiteDatabase db = mDb.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("mode",mode);
        //table:表名, values：更新的值, whereClause:更新的条件, whereArgs：
        // 更新条件的占位符的值,返回值：成功修改多少行
        db.update("blacknumber",values,"phone =",new String[]{phone});
        db.close();
    }
    public ArrayList<BlackNumberInfo> queryAll(){

        SQLiteDatabase db = mDb.getWritableDatabase();
        //table:表名, columns：查询的列名,如果null代表查询所有列； selection:
        // 查询条件, selectionArgs：条件占位符的参数值,
        //groupBy:按什么字段分组, having:分组的条件, orderBy:按什么字段排序
        Cursor query = db.query("blacknumber", new String[]{"phone", "mode"},
                null, null, null, null,
                "_id desc");
        ArrayList<BlackNumberInfo> list = new ArrayList<>();
       if(query != null && query.getCount()>0){ //判断行数是否大于0
           while (query.moveToNext()){
               String phone = query.getString(0);
               String mode = query.getString(1);
               mInfo = new BlackNumberInfo(phone,mode);
               list.add(mInfo);
           }
           query.close();
       }
        db.close();
        return list;
    }
    /**
     * 这是一个分页面算法
     * @param index  每次要从什么位置查询的索引值
     * @param item 要查询的条目数
     * @return 返回查到的结果
     */
    public ArrayList<BlackNumberInfo> query(int index, int item){
        SQLiteDatabase db = mDb.getWritableDatabase();
        String sql = "select * from blacknumber order by _id desc limit ?,?;";
        Cursor query = db.rawQuery(sql, new String[]{index + "", item + ""});
        ArrayList<BlackNumberInfo> list = new ArrayList<>();
        if(query != null && query.getCount()>0){ //判断行数是否大于0
            while (query.moveToNext()){
                String phone = query.getString(1);
                String mode = query.getString(2);
                mInfo = new BlackNumberInfo(phone,mode);
                list.add(mInfo);
            }
            query.close();
        }
        db.close();
        return list;
    }
    /**
     * 返回数据库的总条目的多少
     */
    public int getCount(){
        int count = 0;
        SQLiteDatabase db = mDb.getWritableDatabase();
        String sql = "select count(*)from blacknumber;";
        Cursor query = db.rawQuery(sql,null);
        if(query.moveToNext()){
            count = query.getInt(0);
        }
        query.close();
        db.close();
        return count;
    }

    /**
     * @param phone 作为查询条件的电话号码
     * @return 传入电话号码的拦截模式
     */
    public int getMode(String phone){
        int mode = 0;
        SQLiteDatabase db = mDb.getWritableDatabase();
        Cursor query = db.query("blacknumber", new String[]{"mode"}, "phone = ?", new
                String[]{phone}, null, null, null);
        if(query != null && query.getCount()>0){
            if (query.moveToNext()){
                mode =Integer.parseInt( query.getString(0));
            }
            query.close();
        }
        db.close();
        return mode;
    }
}
