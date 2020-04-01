package com.cloud_bridge.cluster;import com.cloud_bridge.api.PubAndSubClient;import com.cloud_bridge.api.PubAndSubClient.SubscribListener;import com.cloud_bridge.castable.TopicBroadcastable;import com.cloud_bridge.client.NettyPubAndSubClient;import com.cloud_bridge.conf.ConfigFactory;import com.cloud_bridge.conf.PropertyConfigFactory;import com.cloud_bridge.conf.ServerConfig;import com.cloud_bridge.halder.ChannelHolder;import com.cloud_bridge.handler.ClientChannelHolder;import com.cloud_bridge.model.Message;import com.cloud_bridge.server.impl.TCPServer;import com.cloud_bridge.utils.StringTools;import io.netty.channel.Channel;import lombok.extern.slf4j.Slf4j;import java.io.UnsupportedEncodingException;import java.util.Map;import java.util.concurrent.ExecutorService;import java.util.concurrent.Executors;@Slf4jpublic class ConnectServers {    PropertyConfigFactory configFac=new PropertyConfigFactory();    /**     * 创建线程池     */    private final ExecutorService executorService= Executors.newFixedThreadPool(1);    public  void connect() {        ServerConfig config = configFac.getConfig(ConfigFactory.filaPath);        // 首先获取所有的服务主机和端口号        ClusterConfig clusterConfig = new ClusterConfig(config);        Map<String, String> map = clusterConfig.getMap();        map.keySet().forEach(key ->{            NettyPubAndSubClient client = NettyPubAndSubClient.getInstance().connect(key,map.get(key));            // 连接成功后 发送认证信息            client.auth("wzw", "666", new PubAndSubClient.AutuListener() {                @Override                public void authOk(Message message) {                    log.info(key+" : "+map.get(key)+">>> 连接成功 >>> "+message.toString());                    // 保存连接 便于后续消息广播                    ClientChannelHolder.setMAP(key+map.get(key),client);                }                @Override                public void authFail(Message message) {                    log.error(key+" : "+map.get(key)+">>> 连接失败 >>> "+message.toString());                }            });            client.subscribe("mm",new SubscribListener(){                @Override                public void subOk(Message message) {                    log.info("订阅成功");                }                @Override                public void msgActive(Message message) {                    try {                        log.info("收到订阅消息 ："+new String(message.getData(),"utf-8"));                    } catch (UnsupportedEncodingException e) {                        // TODO Auto-generated catch block                        e.printStackTrace();                    }                }            });        });    }}