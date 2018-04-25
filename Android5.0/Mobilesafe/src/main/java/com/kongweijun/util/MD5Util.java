package com.kongweijun.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by Administrator on 2017/2/21.
 */

public class MD5Util {

    /**
     * @param pwd
     *            需要加密的密码 给定字符串按照MD5算法加密
     * @return 返回加密后的字符串
     */
    public static String encrypt(String pwd) {

        // [0]加盐

        pwd = pwd + "my360Mobilesafe";

        // [1]指定加密算法类型

        try {

            // 单例模式
            MessageDigest md = MessageDigest.getInstance("MD5");

            // [2]将需要加密的字符串转换成byte类型的数组，然后进行随机哈希
            byte[] bs = md.digest(pwd.getBytes());
            // [3]循环遍历bs,然后让某生成32位字符串，固定写法
            // [4]拼接字符串
            StringBuffer buffer = new StringBuffer();
            for (byte b : bs) {
                int i = b & 0xff;
                // [4.1]int类型的i需要转换成16进制字符

                String hexString = Integer.toHexString(i);
                if (hexString.length() < 2) {
                    hexString = "0" + hexString;
                }

                buffer.append(hexString);
            }
            return buffer.toString();
        } catch (NoSuchAlgorithmException e) {

            e.printStackTrace();
        }
        return null;
    }
}
