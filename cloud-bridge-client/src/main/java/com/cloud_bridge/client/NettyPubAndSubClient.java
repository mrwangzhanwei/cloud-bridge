package com.cloud_bridge.client;

import com.alibaba.fastjson.JSONObject;
import com.cloud_bridge.api.PubAndSubClient;
import com.cloud_bridge.conf.ServerConfig;
import com.cloud_bridge.event.EventBus;
import com.cloud_bridge.halder.ChannelHolder;
import com.cloud_bridge.model.FuncodeEnum;
import com.cloud_bridge.model.Message;
import com.cloud_bridge.utils.MD5Util;

/**
 * @author 王战伟
 * @email wangzhanwei@lumlord.com
 * @date 2020/3/31  13:55
 */
public class NettyPubAndSubClient implements PubAndSubClient{

    private final static NettyPubAndSubClient nettyPubAndSubClient=new NettyPubAndSubClient();

    public static NettyPubAndSubClient getInstance(){
        return nettyPubAndSubClient;
    }

    public  NettyPubAndSubClient connect(String host,String port){
        TCPClient.INSTANCE.setConfig(new ServerConfig(host,port)).start();
        while(null==ChannelHolder.getChannel()){
            try {//等待异步启动之后返回
                Thread.sleep(10);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return nettyPubAndSubClient;
    }

    private NettyPubAndSubClient() {

    }

    //是否接收广播消息
    public NettyPubAndSubClient acceptBraodCast(SubscribListener subscribListener){
        subscribe(FuncodeEnum.MESSAGE_BROAD.name().concat("$"), subscribListener);
        return this;
    }


    @Override
    public void auth(String username, String password, PubAndSubClient.AutuListener autuListener) {
        if(!checkConnect()){
            throw new RuntimeException("请连接后重试");
        }
        EventBus.setAutuListener(autuListener);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("username", username);
        jsonObject.put("password", password);
        System.out.println(ChannelHolder.getChannel());
        ChannelHolder.getChannel().writeAndFlush(new Message(FuncodeEnum.AUTH_USER,(byte)0 , null, jsonObject.toString().getBytes().length, jsonObject.toString().getBytes()));
    }

    @Override
    public void auth(String key, String username, String password, AutuListener autuListener) {

    }

    @Override
    public void subscribe(String topic, SubscribListener subscribListener) {
        EventBus.setSubscribListener(topic,subscribListener);
        ChannelHolder.getChannel().writeAndFlush(new Message(FuncodeEnum.TOPIC_SUBSCRIBE, (byte)1, ecodeTopic(topic),"subscribe".getBytes().length,"subscribe".getBytes()));
        SubRecorder.record(MD5Util.getPwd(topic).substring(0, 12));
    }

    @Override
    public void subscribe(String key, String topic, SubscribListener subscribListener) {

    }

    @Override
    public void unsubscribe(String topic) {
        SubRecorder.remove(MD5Util.getPwd(topic).substring(0, 12));
        ChannelHolder.getChannel().writeAndFlush(new Message(FuncodeEnum.TOPIC_UNSUBSCRIBE, (byte)1, ecodeTopic(topic),"unsubscribe".getBytes().length,"unsubscribe".getBytes()));
    }

    @Override
    public void unsubscribe(String key, String topic) {

    }

    @Override
    public void publish(String topic,String str) {
        try {
            ChannelHolder.getChannel().writeAndFlush(new Message(FuncodeEnum.MESSAGE_SEND, (byte)1, ecodeTopic(topic),str.getBytes("utf-8").length,str.getBytes("utf-8")));
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Override
    public void broadcast(String data) {
        try {
            ChannelHolder.getChannel().writeAndFlush(new Message(FuncodeEnum.MESSAGE_BROAD, (byte)0, null,data.getBytes("utf-8").length,data.getBytes("utf-8")));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean checkConnect(){
        return null==ChannelHolder.getChannel()?false:true;
    }

    private byte[] ecodeTopic(String topic){
        return MD5Util.getPwd(topic).substring(0, 12).getBytes();
    }


    public void shutdown(){

        TCPClient.INSTANCE.stop();
    }





}
