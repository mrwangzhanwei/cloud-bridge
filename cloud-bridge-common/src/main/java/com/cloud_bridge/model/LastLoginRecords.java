package com.cloud_bridge.model;

import com.cloud_bridge.utils.MD5Util;

import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author 王战伟
 * @email wangzhanwei@lumlord.com
 * @date 2020/4/2  16:58
 */
public class LastLoginRecords {

    private static Map<String,Date> lastLoginTable;
//    private static LastLoginRecord lastLoginRecord=new LastLoginRecord();
    private String lastToken;

    public LastLoginRecords(){
        lastLoginTable=new ConcurrentHashMap<>();
    }

//    public static LastLoginRecord INSTANCE(){
//        return lastLoginRecord;
//    }


    public  boolean isLogin(String username,String password ){
        return lastLoginTable.get(MD5Util.getPwd(username+password).substring(0, 16))==null?false:true;
    }

    public boolean isLogin(String token){
        return lastLoginTable.get(token)==null?false:true;
    }

    public void register(String username,String password){
        lastLoginTable.put(MD5Util.getPwd(username+password).substring(0, 16),new Date());
    }

    public void setToken(Message message){
        String token=new String(message.getData());
        this.lastToken=token;
        lastLoginTable.put(token, new Date());
    }


    public String getLastToken() {
        return lastToken;
    }

}
