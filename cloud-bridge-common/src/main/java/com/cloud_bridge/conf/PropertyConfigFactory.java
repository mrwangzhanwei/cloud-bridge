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
            // TODO Auto-generated catch block
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
            serverConfig.setEnableCluster(Boolean.parseBoolean(config.get("server.enableCluster")));

            //设置zk配置
            serverConfig.setZkServers(config.get("zkServer.servers"));
            serverConfig.setZkSessionTimeout(Integer.parseInt(config.get("zkServer.sessionTimeout")));
            serverConfig.setZkRootPath(config.get("zkServer.rootPath"));




            HashMap<String, String> hashMap = new HashMap<>();
            hashMap.put("dbw", "123");
            serverConfig.setVerifiedAccount(hashMap);
        }
        return serverConfig;
    }

    public  ServerConfig getConfig(){
        return getConfig(null);
    }




}
