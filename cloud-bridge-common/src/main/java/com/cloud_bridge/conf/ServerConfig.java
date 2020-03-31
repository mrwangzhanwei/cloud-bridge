package com.cloud_bridge.conf;

import java.util.Map;

/**
 * @author 王战伟
 * @email wangzhanwei@lumlord.com
 * @date 2020/3/25  10:03
 */
public class ServerConfig {

    private String host;

    private String port;

    private String serverName;

    private Integer retryCount;

    private Integer connectTimeout;

    private Integer pingTimeout;

    private Map<String,String> verifiedAccount;




    public ServerConfig(String host, String port) {
        this.host = host;
        this.port = port;
    }

    public ServerConfig() {
        // TODO Auto-generated constructor stub
    }
    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getServerName() {
        return serverName;
    }

    public void setServerName(String serverName) {
        this.serverName = serverName;
    }

    public Integer getRetryCount() {
        return retryCount;
    }

    public void setRetryCount(Integer retryCount) {
        this.retryCount = retryCount;
    }

    public Integer getConnectTimeout() {
        return connectTimeout;
    }

    public void setConnectTimeout(Integer connectTimeout) {
        this.connectTimeout = connectTimeout;
    }

    public Map<String, String> getVerifiedAccount() {
        return verifiedAccount;
    }

    public void setVerifiedAccount(Map<String, String> verifiedAccount) {
        this.verifiedAccount = verifiedAccount;
    }

    public Integer getPingTimeout() {
        return pingTimeout;
    }

    public void setPingTimeout(Integer pingTimeout) {
        this.pingTimeout = pingTimeout;
    }

}
