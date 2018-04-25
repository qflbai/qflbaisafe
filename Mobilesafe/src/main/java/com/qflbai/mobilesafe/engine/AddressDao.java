package com.qflbai.mobilesafe.engine;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by Administrator on 2017/2/26.
 */

public class AddressDao {
    //指定访问数据库的路径
    public static  String path = "/data/data/com.kongweijun" +
            ".mobilesafe/files/address.db";
    private static String mIndexAddress;


    /**
     * 传递一个电话，开启数据库连接，返回一个归属地
     * 开启数据库连接，进行访问
     * @param phone 查询电话号码
     */
    public static String getAddress(String phone){
        SQLiteDatabase db = SQLiteDatabase.openDatabase(path, null,
                SQLiteDatabase.OPEN_READONLY);
        //手机号码的正则表达式
        String regular = "^1[3|4|5|8][0-9]\\d{8}";
        boolean matchesRegular = phone.matches(regular);
        if(matchesRegular) {
            //打开一个数据库连接(只读方式)
            phone = phone.substring(0, 7);
            Cursor query = db.query("data1", new String[]{"outkey"}, "id =?", new String[]{phone},
                    null, null, null);
            if (query.moveToNext()) {
                String outKey = query.getString(0);
                Cursor indexQuery = db.query("data2", new String[]{"location"}, "id =?",
                        new String[]{outKey},
                        null, null, null);
                if (indexQuery.moveToNext()) {
                    mIndexAddress = indexQuery.getString(0);
                    //return mIndexAddress;
                }
            }else {
                mIndexAddress = "未知号码";
            }
        }else{
            int length = phone.length();
            switch (length) {
                case 3:
                    mIndexAddress = "报警电话";
                    break;
                case 4:
                    mIndexAddress = "手机安卓模拟器";
                    break;
                case 5:
                    mIndexAddress = "服务电话";
                    break;
                case 7:
                case 8:
                    mIndexAddress = "本地电话";
                    break;
                case 11:
                    //查询data2表，区号+座机号码代表外地座机号
                    String subPhone = phone.substring(1,3);
                    Cursor queryLocation = db.query("data2", new
                                    String[]{"location"},
                            "area = ?",
                            new String[]{subPhone}, null, null, null);
                    if(queryLocation.moveToNext()){
                        String locationAdrees = queryLocation.getString(0);
                        mIndexAddress = locationAdrees;
                    }else{
                        mIndexAddress = "未知号码";
                    }
                    break;
                case 12:
                    //查询data2表，区号+座机号码代表外地座机号
                    String subPhone1 = phone.substring(1,4);
                    Cursor queryLocation1 = db.query("data2", new
                                    String[]{"location"},
                            "area = ?",
                            new String[]{subPhone1}, null, null, null);
                    if(queryLocation1.moveToNext()){
                        String locationAdrees = queryLocation1.getString(0);
                        mIndexAddress = locationAdrees;
                    }else{
                        mIndexAddress = "未知号码";
                    }
                    mIndexAddress = "本地电话";
                    break;
                default:
                    mIndexAddress = "未知号码";
                    break;
            }
        }
        return  mIndexAddress;
    }
}
