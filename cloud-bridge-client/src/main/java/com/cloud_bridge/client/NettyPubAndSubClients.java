package com.cloud_bridge.client;

import com.alibaba.fastjson.JSONObject;
import com.cloud_bridge.api.PubAndSubClient;
import com.cloud_bridge.event.EventBus;
import com.cloud_bridge.event.MyEventBus;
import com.cloud_bridge.halder.ChannelHolder;
import com.cloud_bridge.model.FuncodeEnum;
import com.cloud_bridge.model.Message;
import com.cloud_bridge.utils.MD5Util;
import com.cloud_bridge.utils.StringTools;
import io.netty.channel.Channel;

import static com.cloud_bridge.event.EventBus.setAutuListener;

/**
 * @author 王战伟
 * @email wangzhanwei@lumlord.com
 * @date 2020/4/2  15:14
 */
public class NettyPubAndSubClients implements PubAndSubClient {

    private  Channel channel = null;

    public NettyPubAndSubClients(Channel channel) {
            this.channel = channel;
    }

    //是否接收广播消息
    public NettyPubAndSubClients acceptBraodCast(SubscribListener subscribListener){
        subscribe(FuncodeEnum.MESSAGE_BROAD.name().concat("$"), subscribListener);
        return this;
    }


    @Override
    public void auth(String username, String password, AutuListener autuListener) {

    }

    @Override
    public void auth(String key,String username, String password, PubAndSubClient.AutuListener autuListener) {
        if(!checkConnect()){
            throw new RuntimeException("请连接后重试");
        }
        MyEventBus myEventBus = new MyEventBus(key);
        myEventBus.setAutuListener(autuListener);
        ChannelHolder.setMyEventBusMap(key,myEventBus);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("username", username);
        jsonObject.put("password", password);
        System.out.println(channel);
        channel.writeAndFlush(new Message(FuncodeEnum.AUTH_USER,(byte)0 , null, jsonObject.toString().getBytes().length, jsonObject.toString().getBytes()));
    }

    @Override
    public void subscribe(String String, SubscribListener subscribListener) {

    }

    @Override
    public void subscribe(String key,String topic, SubscribListener subscribListener) {
        MyEventBus myEventBus =ChannelHolder.getMyEventBusMap(key);
        myEventBus.setSubscribListener(topic,subscribListener);
        channel.writeAndFlush(new Message(FuncodeEnum.TOPIC_SUBSCRIBE, (byte)1, StringTools.ecodeTopic(topic),"subscribe".getBytes().length,"subscribe".getBytes()));
        SubRecorders recorders = ChannelHolder.getSubRecordersMap(key);
        recorders.record(MD5Util.getPwd(topic).substring(0, 12));
    }

    @Override
    public void unsubscribe(String topic) {

    }

    @Override
    public void unsubscribe(String key,String topic) {
        SubRecorders subRecorders = ChannelHolder.getSubRecordersMap(key);
        subRecorders.remove(MD5Util.getPwd(topic).substring(0, 12));
        channel.writeAndFlush(new Message(FuncodeEnum.TOPIC_UNSUBSCRIBE, (byte)1, StringTools.ecodeTopic(topic),"unsubscribe".getBytes().length,"unsubscribe".getBytes()));
    }

    @Override
    public void publish(String topic,String str) {
        try {
            channel.writeAndFlush(new Message(FuncodeEnum.MESSAGE_SEND, (byte)1, StringTools.ecodeTopic(topic),str.getBytes("utf-8").length,str.getBytes("utf-8")));
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Override
    public void broadcast(String data) {
        try {
            channel.writeAndFlush(new Message(FuncodeEnum.MESSAGE_BROAD, (byte)0, null,data.getBytes("utf-8").length,data.getBytes("utf-8")));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean checkConnect(){
        return null==channel?false:true;
    }


    public void shutdown(){

        TCPClient.INSTANCE.stop();
    }





}
