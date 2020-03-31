package com.cloud_bridge.cluster;

import com.cloud_bridge.conf.ServerConfig;
import java.util.Map;
import java.util.HashMap;

public class ClusterConfig {
    /**
     * 存放所有的主机地址和端口
     */
    private  Map<String,String>  map = new HashMap<>();

    ClusterConfig(ServerConfig config){
        // 将配置添加到内存中
        setMap(config.getServers());
    }

    public  void setMap(String servers) {
        if (servers.contains(",")){
            String[] ipAndPorts = servers.split(",");
            for (String str:ipAndPorts) { // 多个地址 ， 分割 循环添加
                String[] ipAndPort = str.split(":");
                map.put(ipAndPort[0],ipAndPort[1]);
            }
        }else{ // 单个地址
            String[] ipAndPort = servers.split(":");
            map.put(ipAndPort[0],ipAndPort[1]);
        }
    }
    public Map<String,String> getMap(){
        return this.map;
    }
}
