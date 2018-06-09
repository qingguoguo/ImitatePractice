package com.qgg.practice.http;

import java.security.MessageDigest;

/**
 * 作者:qingguoguo
 * 创建日期：2018/3/23 on 22:29
 * 描述:
 */

public class MD5Util {

    /**
     * 利用MD5进行加密
     *
     * @param inStr 待加密的字符串
     * @return 加密后的字符串
     * NoSuchAlgorithmException  没有这种产生消息摘要的算法
     * UnsupportedEncodingException
     */
    public static String string2MD5(String inStr) {
        MessageDigest md5 = null;
        try {
            md5 = MessageDigest.getInstance("MD5");
        } catch (Exception e) {
            System.out.println(e.toString());
            e.printStackTrace();
            return "";
        }
        char[] charArray = inStr.toCharArray();
        byte[] byteArray = new byte[charArray.length];

        for (int i = 0; i < charArray.length; i++)
            byteArray[i] = (byte) charArray[i];
        byte[] md5Bytes = md5.digest(byteArray);
        StringBuffer hexValue = new StringBuffer();
        for (int i = 0; i < md5Bytes.length; i++) {
            int val = ((int) md5Bytes[i]) & 0xff;
            if (val < 16)
                hexValue.append("0");
            hexValue.append(Integer.toHexString(val));
        }
        return hexValue.toString();
    }
}
