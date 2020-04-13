package com.cloud_bridge.cluster;

import com.cloud_bridge.api.PubAndSubClient;
import com.cloud_bridge.api.PubAndSubClient.SubscribListener;
import com.cloud_bridge.client.NettyPubAndSubClients;
import com.cloud_bridge.client.TCPClients;
import com.cloud_bridge.conf.ConfigFactory;
import com.cloud_bridge.conf.PropertyConfigFactory;
import com.cloud_bridge.conf.ServerConfig;
import com.cloud_bridge.factory.ClientFactory;
import com.cloud_bridge.handler.ClientChannelHolder;
import com.cloud_bridge.model.FuncodeEnum;
import com.cloud_bridge.model.Message;
import com.cloud_bridge.utils.StringTools;
import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
public class ConnectServers {

    static PropertyConfigFactory configFac = new PropertyConfigFactory();

    static ClientFactory factory = new ClientFactory();

    public static void connect() {
        ServerConfig config = configFac.getConfig(ConfigFactory.filaPath);
        // 首先获取所有的服务主机和端口号
        ClusterConfig clusterConfig = new ClusterConfig(config);
        List<String> list = clusterConfig.getList();
        list.forEach(server ->{
            String[] split = server.split(":");
            String ip = split[0];
            String port = split[1];
            TCPClients client = factory.getClient().setConfig(new ServerConfig(ip,port));
            client.start();
            Channel channel = ClientChannelHolder.getMAP(server);
            while(null==channel){
                    channel = ClientChannelHolder.getMAP(server);
            }
            NettyPubAndSubClients nettyPubAndSubClients = new NettyPubAndSubClients(channel);
            nettyPubAndSubClients.auth(server,"wzw", "666", new PubAndSubClient.AutuListener() {
                @Override
                public void authOk(Message message) {
                    log.info(server+">>> 连接成功 >>> "+message.toString());
                }

                @Override
                public void authFail(Message message) {
                    log.error(server+">>> 连接失败 >>> "+message.toString());
                }
            });
            nettyPubAndSubClients.subscribe(server,"mm",new SubscribListener(){
                @Override
                public void subOk(Message message) {
                    log.info(server+"订阅成功");
                }

                @Override
                public void msgActive(Message message) {
                        log.info(server+"收到订阅消息 ："+ message.toString());
                        sendAllChannel(message.toString());
                }
            });
        });
    }
    public static void sendAllChannel(String msg){
        Map<String, Channel> map = ClientChannelHolder.getMAP();
        map.forEach((k,v)->{
            try {
                v.writeAndFlush(new Message(FuncodeEnum.MESSAGE_SEND, (byte)1, StringTools.ecodeTopic("zf"),msg.getBytes("utf-8").length,msg.getBytes("utf-8")));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        });
    }
}
