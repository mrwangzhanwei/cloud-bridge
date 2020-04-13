package com.cloud_bridge.factory;

import com.cloud_bridge.client.TCPClients;

/**
 * @author 王战伟
 * @email wangzhanwei@lumlord.com
 * @date 2020/4/2  15:05
 */
public class ClientFactory {
    public  TCPClients getClient(){
        return new TCPClients();
    }
}
