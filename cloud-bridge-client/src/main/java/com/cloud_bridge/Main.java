package com.cloud_bridge;

import com.cloud_bridge.client.TCPClient;
import com.cloud_bridge.conf.ServerConfig;

/**
 * @author 王战伟
 * @email wangzhanwei@lumlord.com
 * @date 2020/3/25  22:43
 */
public class Main {
    public static void main(String[] args) {
        TCPClient client = TCPClient.INSTANCE;
        client.setConfig(new ServerConfig("127.0.0.1","9999"));
        client.start();
    }
}
