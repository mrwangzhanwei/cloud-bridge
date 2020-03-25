package com.cloud_bridge.conf;

/**
 * @author 王战伟
 * @email wangzhanwei@lumlord.com
 * @date 2020/3/25  10:03
 */
public interface ConfigFactory {

    String filaPath="config.properties";

    ServerConfig getConfig(String filePath);

}
