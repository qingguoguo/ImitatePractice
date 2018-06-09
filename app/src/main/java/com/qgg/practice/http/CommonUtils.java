package com.qgg.practice.http;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

public class CommonUtils {

    /**
     * 生成随机数，每次调用接口不能重复，长度10到40的字母或数字组成
     */
    public static String createRandomString() {
        //长度临时设置为20位
        int length = 20;
        String base = "abcdefghijklmnopqrstuvwxyz0123456789";
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length; i++) {
            int number = random.nextInt(base.length());
            sb.append(base.charAt(number));
        }
        return sb.toString();
    }

    /**
     * MD5 加密
     *
     * @param data
     * @return
     */
    public static String toMD5(String data) {
        try {
            // 实例化一个指定摘要算法为MD5的MessageDigest对象
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            // 重置摘要以供再次使用
            md5.reset();
            // 使用bytes更新摘要
            md5.update(data.getBytes());
            // 使用指定的byte数组对摘要进行最的更新，然后完成摘要计算
            return toHexString(md5.digest());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 将字符串中的每个字符转换为十六进制
     *
     * @param bytes
     * @return
     */
    private static String toHexString(byte[] bytes) {
        StringBuilder hexString = new StringBuilder();
        for (byte b : bytes) {
            String hex = Integer.toHexString(0xFF & b);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }
}
