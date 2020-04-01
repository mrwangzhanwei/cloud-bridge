package com.cloud_bridge.handler;

import com.cloud_bridge.client.NettyPubAndSubClient;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author 王战伟
 * @email wangzhanwei@lumlord.com
 * @date 2020/4/1  14:01
 */
public class ClientChannelHolder {
    private static Map<String,NettyPubAndSubClient> MAP = new ConcurrentHashMap<>();

    public static NettyPubAndSubClient getMAP(String key) {
        return MAP.get(key);
    }

    public static void setMAP(String key, NettyPubAndSubClient client) {
        MAP.put(key, client);
    }
}
