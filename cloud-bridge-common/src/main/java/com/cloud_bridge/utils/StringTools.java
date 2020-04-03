package com.cloud_bridge.utils;

import java.io.UnsupportedEncodingException;

/**
 * @author 王战伟
 * @email wangzhanwei@lumlord.com
 * @date 2020/3/25  9:28
 */
public class StringTools {

    /**
     *
     * @param data
     * @return
     */
    public static String toStrUTF(byte[] data){
        if(null!=data){
            try {
                return new String(data,"utf-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * String topic 转 byte
     * @param topic
     * @return
     */
    public static byte[] ecodeTopic(String topic){
        return MD5Util.getPwd(topic).substring(0, 12).getBytes();
    }
}
