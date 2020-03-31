package com.cloud_bridge.client;

import com.cloud_bridge.halder.ChannelHolder;
import com.cloud_bridge.model.FuncodeEnum;
import com.cloud_bridge.model.Message;

import java.util.HashSet;
import java.util.Set;

/**
 * @author 王战伟
 * @email wangzhanwei@lumlord.com
 * @date 2020/3/31  9:55
 */
public class SubRecorder {

    private static Set<String> topicRecorder=new HashSet<>();


    public static void record(String topic){
        topicRecorder.add(topic);
    }

    public static void remove(String topic){
        topicRecorder.remove(topic);
    }

    public static void recover(){
        if(topicRecorder.size()>0){
            topicRecorder.forEach((topic)->{
                ChannelHolder.getChannel().writeAndFlush(new Message(FuncodeEnum.TOPIC_SUBSCRIBE, (byte)1, topic.getBytes(),"subscribe".getBytes().length,"subscribe".getBytes()));
            });
        }
    }

}
