package com.qflbai.mobilesafe.engine;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Xml;

import org.xmlpull.v1.XmlSerializer;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 *
 * 一个类的初始化过程(继承)
 *静态代码块>构造代码块>默认初始化>显示初始化>构造方法初始化
 *先进行父类初始化，然后进行子类初始化。
 * Created by Administrator on 2017/3/6.
 */

public class SmsBackUp {
    private static int sIndex = 0;
    private static FileOutputStream sFos;
    private static Cursor sCursor;

    /**
     * 备份短信的方法
     * @param context  上下文环境
     * @param path    备份文件夹的路径
     * @param pd     精度条的对象，用于备份过程中的进度更新
     */
    public static void backUp(Context context, String path, CallBack pd){
        File file = new File(path);
        ContentResolver contentResolver = context.getContentResolver();
        Uri uri = Uri.parse("content://sms/");
        sCursor = contentResolver.query(uri,new String[]
                {"address","date","type","body"},null, null, null, null);
        try {
            //读写文件的输出流
            sFos = new FileOutputStream(file);
           //系列化一个XML文件
            XmlSerializer xs = Xml.newSerializer();
            //给xml做相应的配置文件
            xs.setOutput(sFos,"utf-8");//设置输出流和编码方式
            xs.startDocument("utf-8",true);//设置XML规范
            xs.startTag(null,"smss");//设置跟节点
                //读取数据库中的每一条数据写入到XML中
            int count = sCursor.getCount();
            if(pd != null){
                pd.setMax(count);
            }
            while (sCursor.moveToNext()){
                xs.startTag(null,"sms");

                xs.startTag(null,"address");
                xs.text(sCursor.getString(0));
                xs.endTag(null,"address");

                xs.startTag(null,"date");
                xs.text(sCursor.getString(1));
                xs.endTag(null,"date");

                xs.startTag(null,"type");
                xs.text(sCursor.getString(2));
                xs.endTag(null,"type");

                xs.startTag(null,"body");
                xs.text(sCursor.getString(3));
                xs.endTag(null,"body");

                xs.endTag(null,"sms");
                //每次循环一次，就要更新进度条
                sIndex++;
                Thread.sleep(200);
                if(pd != null){
                    pd.setProgress(sIndex);//可以在子线程更新的UI
                }
            }
            xs.endTag(null,"smss");
            xs.endDocument();
            xs.flush();
        } catch(FileNotFoundException e) {
        } catch(IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            if((sFos != null)&&(sCursor != null)){
                try {
                    sCursor.close();
                    sFos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 接口回调的步骤
     *1.定义一个接口
     *2,定义接口中未实现的业务逻辑方法(短信总数设置,备份过程中短信百分比更新)
     *3.传递一个实现了此接口的类的对象(至备份短信的工具类中),接口的实现类,一定实现了上诉两个为实现方法(就决定了使用对话框,还是进度条)
     *4.获取传递进来的对象,在合适的地方(设置总数,设置百分比的地方)做方法的调用
     */
    public interface CallBack{
        public void setMax(int max);
        public void setProgress(int value);
    }
}
