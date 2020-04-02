package com.cloud_bridge.conf;

import com.cloud_bridge.utils.PropertyUtil;

import java.io.IOException;
import java.util.HashMap;

/**
 * @author 王战伟
 * @email wangzhanwei@lumlord.com
 * @date 2020/3/25  10:04
 */
public class PropertyConfigFactory implements ConfigFactory{

    private static ServerConfig serverConfig=null;

    private static HashMap<String,String> config=null;

    static{
        try {
            config = PropertyUtil.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public  ServerConfig getConfig(String filePath) {
        if(serverConfig==null){
            serverConfig=new ServerConfig();
            serverConfig.setConnectTimeout(Integer.parseInt(config.get("server.connectTimeout")));
            serverConfig.setHost(config.get("server.host"));
            serverConfig.setPort(config.get("server.port"));
            serverConfig.setRetryCount(Integer.parseInt(config.get("server.retryCount")));
            serverConfig.setServers(config.get("cluster.servers"));
            serverConfig.setHttpProt(Integer.parseInt(config.get("httpserver.prot")));
            // 设置连接密码
            HashMap<String, String> hashMap = new HashMap<>();
            hashMap.put("wzw", "666");
            serverConfig.setVerifiedAccount(hashMap);
        }
        return serverConfig;
    }

    public  ServerConfig getConfig(){
        return getConfig(null);
    }




}
