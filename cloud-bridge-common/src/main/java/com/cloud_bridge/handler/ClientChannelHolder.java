package com.cloud_bridge.handler;

import com.cloud_bridge.model.LastLoginRecords;
import io.netty.channel.Channel;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author 王战伟
 * @email wangzhanwei@lumlord.com
 * @date 2020/4/1  14:01
 */
public class ClientChannelHolder {

    /**
     * 客户端 channel
     */
    private static Map<String,Channel> MAP = new ConcurrentHashMap<>();

    public static Channel getMAP(String key) {
        return MAP.get(key);
    }

    public static void setMAP(String key, Channel client) {
        MAP.put(key, client);
    }

    public static Map<String, Channel> getMAP() {
        return MAP;
    }

    /**
     *  登录存储 LastLoginRecords
     */

    private static Map<String,LastLoginRecords> LastLoginRecordsMAP = new ConcurrentHashMap<>();

    public static LastLoginRecords getLastLoginRecords(String key) {
        return LastLoginRecordsMAP.get(key);
    }

    public static void setLastLoginRecords(String key, LastLoginRecords lastLoginRecords) {
        LastLoginRecordsMAP.put(key, lastLoginRecords);
    }

}
