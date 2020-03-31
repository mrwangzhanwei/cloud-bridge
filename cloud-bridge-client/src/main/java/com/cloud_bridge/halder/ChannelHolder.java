package com.cloud_bridge.halder;

import io.netty.channel.Channel;

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




}
