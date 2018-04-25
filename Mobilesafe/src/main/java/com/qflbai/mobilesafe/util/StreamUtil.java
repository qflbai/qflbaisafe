package com.qflbai.mobilesafe.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Administrator on 2017/2/22.
 */

public class StreamUtil {

    /**
     * 流转换字符串
     * @param is 流对象
     * @return 返回流转换字符串的对象 ，返回null代表异常
     */
    /**
     * @param is 流对象
     * @return 返回null代表异常，否则返回流转化成的字符串
     */
    public static String streamToSting(InputStream is) {
        //[6.1]在读取过程中，将读取的内容存储在缓存中，然后一次性的转化成字符串返回
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        //[6.2]读取流操作，读到没有为止
        int len = 0;
        byte[] bys = new byte[1024];
        try {
            while ((len = is.read(bys)) != -1) {
                bos.write(bys, 0, len);
            }
            return bos.toString();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
                bos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}