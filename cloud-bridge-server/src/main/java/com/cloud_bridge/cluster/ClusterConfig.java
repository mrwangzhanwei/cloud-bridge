package com.cloud_bridge.cluster;

import com.cloud_bridge.conf.ServerConfig;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

public class ClusterConfig {
    /**
     * 存放所有的主机地址和端口
     */
    private  List<String>  list = new ArrayList<>();

    ClusterConfig(ServerConfig config){
        // 将配置添加到内存中
        setList(config.getServers());
    }

    public  void setList(String servers) {
        if (servers.contains(",")){
            String[] ipAndPorts = servers.split(",");
            for (String str:ipAndPorts) { // 多个地址 ， 分割 循环添加
                list.add(str);
            }
            return;
        }
        list.add(servers);
    }
    public  List<String> getList(){
        return this.list;
    }
}
