package com.cloud_bridge.conf;

import java.util.Map;

/**
 * @author 王战伟
 * @email wangzhanwei@lumlord.com
 * @date 2020/3/25  10:03
 */
public class ServerConfig {

    /**
     * 主机
     */
    private String host;

    /**
     * 端口号
     */
    private String port;

    /**
     * 服务名
     */
    private String serverName;

    /**
     * 重试次数
     */
    private Integer retryCount;

    /**
     * 连接超时时间
     */
    private Integer connectTimeout;

    /**
     * ping 超时时间
     */
    private Integer pingTimeout;

    /**
     * HttpProt http端口号
     */
    private Integer httpProt;

    /**
     * 是否开启集群
     */
    private Boolean enableCluster;

    /**
     * 集群主机
     */
    private String servers;

    public Integer getHttpProt() {
        return httpProt;
    }

    public void setHttpProt(Integer httpProt) {
        this.httpProt = httpProt;
    }

    /**
     * 账号密码
     */
    private Map<String,String> verifiedAccount;

    public Boolean getEnableCluster() {
        return enableCluster;
    }

    public void setEnableCluster(Boolean enableCluster) {
        this.enableCluster = enableCluster;
    }

    public String getServers() {
        return servers;
    }

    public void setServers(String servers) {
        this.servers = servers;
    }

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
