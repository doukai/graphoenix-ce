package io.graphoenix.rabbitmq.config;

import com.typesafe.config.Optional;
import org.eclipse.microprofile.config.inject.ConfigProperties;

import static java.util.concurrent.TimeUnit.MINUTES;

@ConfigProperties(prefix = "rabbitmq")
public class RabbitMQConfig {

    @Optional
    private String host = "localhost";

    @Optional
    private Integer port = 5672;

    private String username;

    private String password;

    @Optional
    private String virtualHost = "/";

    @Optional
    private Integer requestedChannelMax = 2047;

    @Optional
    private Integer requestedFrameMax = 0;

    @Optional
    private Integer requestedHeartbeat = 60;

    @Optional
    private Integer connectionTimeout = 60000;

    @Optional
    private Integer handshakeTimeout = 10000;

    @Optional
    private Integer shutdownTimeout = 10000;

    @Optional
    private Boolean automaticRecovery = true;

    @Optional
    private Boolean topologyRecovery = true;

    @Optional
    private Long networkRecoveryInterval = 5000L;

    @Optional
    private Boolean nio = false;

    @Optional
    private Integer channelRpcTimeout = (int) MINUTES.toMillis(10);

    @Optional
    private Boolean channelShouldCheckRpcResponseType = false;

    @Optional
    private Integer workPoolTimeout = -1;

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getVirtualHost() {
        return virtualHost;
    }

    public void setVirtualHost(String virtualHost) {
        this.virtualHost = virtualHost;
    }

    public Integer getRequestedChannelMax() {
        return requestedChannelMax;
    }

    public void setRequestedChannelMax(Integer requestedChannelMax) {
        this.requestedChannelMax = requestedChannelMax;
    }

    public Integer getRequestedFrameMax() {
        return requestedFrameMax;
    }

    public void setRequestedFrameMax(Integer requestedFrameMax) {
        this.requestedFrameMax = requestedFrameMax;
    }

    public Integer getRequestedHeartbeat() {
        return requestedHeartbeat;
    }

    public void setRequestedHeartbeat(Integer requestedHeartbeat) {
        this.requestedHeartbeat = requestedHeartbeat;
    }

    public Integer getConnectionTimeout() {
        return connectionTimeout;
    }

    public void setConnectionTimeout(Integer connectionTimeout) {
        this.connectionTimeout = connectionTimeout;
    }

    public Integer getHandshakeTimeout() {
        return handshakeTimeout;
    }

    public void setHandshakeTimeout(Integer handshakeTimeout) {
        this.handshakeTimeout = handshakeTimeout;
    }

    public Integer getShutdownTimeout() {
        return shutdownTimeout;
    }

    public void setShutdownTimeout(Integer shutdownTimeout) {
        this.shutdownTimeout = shutdownTimeout;
    }

    public Boolean getAutomaticRecovery() {
        return automaticRecovery;
    }

    public void setAutomaticRecovery(Boolean automaticRecovery) {
        this.automaticRecovery = automaticRecovery;
    }

    public Boolean getTopologyRecovery() {
        return topologyRecovery;
    }

    public void setTopologyRecovery(Boolean topologyRecovery) {
        this.topologyRecovery = topologyRecovery;
    }

    public Long getNetworkRecoveryInterval() {
        return networkRecoveryInterval;
    }

    public void setNetworkRecoveryInterval(Long networkRecoveryInterval) {
        this.networkRecoveryInterval = networkRecoveryInterval;
    }

    public Boolean getNio() {
        return nio;
    }

    public void setNio(Boolean nio) {
        this.nio = nio;
    }

    public Integer getChannelRpcTimeout() {
        return channelRpcTimeout;
    }

    public void setChannelRpcTimeout(Integer channelRpcTimeout) {
        this.channelRpcTimeout = channelRpcTimeout;
    }

    public Boolean getChannelShouldCheckRpcResponseType() {
        return channelShouldCheckRpcResponseType;
    }

    public void setChannelShouldCheckRpcResponseType(Boolean channelShouldCheckRpcResponseType) {
        this.channelShouldCheckRpcResponseType = channelShouldCheckRpcResponseType;
    }

    public Integer getWorkPoolTimeout() {
        return workPoolTimeout;
    }

    public void setWorkPoolTimeout(Integer workPoolTimeout) {
        this.workPoolTimeout = workPoolTimeout;
    }
}
