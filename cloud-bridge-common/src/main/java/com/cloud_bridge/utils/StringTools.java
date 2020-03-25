package com.cloud_bridge.utils;

import java.io.UnsupportedEncodingException;

/**
 * @author 王战伟
 * @email wangzhanwei@lumlord.com
 * @date 2020/3/25  9:28
 */
public class StringTools {

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

}
