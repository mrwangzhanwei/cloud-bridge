package com.cloud_bridge.event;

import com.cloud_bridge.api.PubAndSubClient;
import com.cloud_bridge.halder.Handler;
import com.cloud_bridge.handler.ClientChannelHolder;
import com.cloud_bridge.model.FuncodeEnum;
import com.cloud_bridge.model.LastLoginRecord;
import com.cloud_bridge.model.Message;
import com.cloud_bridge.utils.MD5Util;
import jodd.util.StringUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * @author 王战伟
 * @email wangzhanwei@lumlord.com
 * @date 2020/4/2  17:34
 */
public class MyEventBus {

    private String key;

    public MyEventBus(String key){
        this.key = key;
    }


    public Map<String,PubAndSubClient.SubscribListener> subscribListenerTable=new HashMap<>(16);

    public PubAndSubClient.AutuListener autuListener;



    public  PubAndSubClient.SubscribListener selectSubscribListener(Message obj){
        String topic = new String(obj.getTopic());
        PubAndSubClient.SubscribListener subscribListener = subscribListenerTable.get(topic);
        return subscribListener;
    }


    public  void setAutuListener(PubAndSubClient.AutuListener autuListener) {
        this.autuListener = autuListener;
    }

    public  void setSubscribListener(String topic,PubAndSubClient.SubscribListener subscribListener) {
        if(StringUtil.isEmpty(topic)&&subscribListener==null){
            throw new RuntimeException("topic and subscribListener not null");
        }
        subscribListenerTable.put(MD5Util.getPwd(topic).substring(0, 12), subscribListener);
    }




}
