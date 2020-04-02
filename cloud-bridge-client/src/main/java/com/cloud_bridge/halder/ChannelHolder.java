package com.cloud_bridge.halder;

import com.cloud_bridge.client.SubRecorders;
import com.cloud_bridge.event.MyEventBus;
import io.netty.channel.Channel;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author 王战伟
 * @email wangzhanwei@lumlord.com
 * @date 2020/3/31  9:54
 */
public class ChannelHolder {

    private volatile static Channel channel;

    public static Channel getChannel() {
        while(channel==null){};
        return channel;
    }

    public static void setChannel(Channel chan) {
        channel = chan;
    }

    private static Map<String,MyEventBus> myEventBusMap = new ConcurrentHashMap<>();

    public static MyEventBus getMyEventBusMap(String key) {
        return myEventBusMap.get(key);
    }

    public static void setMyEventBusMap(String key, MyEventBus myEventBus) {
        myEventBusMap.put(key, myEventBus);
    }

    /**
     * SubRecorders
     */
    private static Map<String,SubRecorders> subRecordersMap = new ConcurrentHashMap<>();

    public static SubRecorders getSubRecordersMap(String key) {
        return subRecordersMap.get(key);
    }

    public static void setSubRecordersMap(String key, SubRecorders subRecorders) {
        subRecordersMap.put(key, subRecorders);
    }

}
