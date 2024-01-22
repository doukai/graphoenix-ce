package io.graphoenix.http.server.config;

import com.typesafe.config.Optional;
import org.eclipse.microprofile.config.inject.ConfigProperties;

@ConfigProperties(prefix = "http")
public class HttpServerConfig {

    @Optional
    private String graphqlContextPath = "graphql";

    @Optional
    private String subscriptionsContextPath = "subscriptions";

    @Optional
    private Integer port = 8080;

    @Optional
    private Boolean ssl = false;

    @Optional
    private Integer SessionTimeoutMillis;

    @Optional
    private Integer connectTimeoutMillis;

    @Optional
    private Integer maxMessagesPerRead;

    @Optional
    private Integer maxMessagesPerWrite;

    @Optional
    private Integer writeSpinCount;

    @Optional
    private Integer writeBufferLowWaterMark;

    @Optional
    private Integer writeBufferWaterMark;

    @Optional
    private Boolean allowHalfClosure;

    @Optional
    private Boolean autoRead;

    @Optional
    private Boolean autoClose;

    @Optional
    private Boolean soBroadcast;

    @Optional
    private Boolean soKeepalive = false;

    @Optional
    private Integer soSndbuf;

    @Optional
    private Integer soRcvbuf;

    @Optional
    private Boolean soReuseaddr;

    @Optional
    private Integer soLinger;

    @Optional
    private Integer soBacklog = 128;

    @Optional
    private Integer soTimeout;

    @Optional
    private Integer ipTos;

    @Optional
    private Integer ipMulticastTtl;

    @Optional
    private Boolean ipMulticastLoopDisabled;

    @Optional
    private Boolean tcpNodelay = true;

    @Optional
    private Boolean tcpFastopenConnect;

    @Optional
    private Integer tcpFastopen;

    @Optional
    private Boolean datagramChannelActiveOnRegistration;

    @Optional
    private Boolean singleEventexecutorPerGroup;

    public String getGraphqlContextPath() {
        if (graphqlContextPath.startsWith("/")) {
            return graphqlContextPath;
        } else {
            return "/" + graphqlContextPath;
        }
    }

    public void setGraphqlContextPath(String graphqlContextPath) {
        this.graphqlContextPath = graphqlContextPath;
    }

    public String getSubscriptionsContextPath() {
        if (subscriptionsContextPath.startsWith("/")) {
            return subscriptionsContextPath;
        } else {
            return "/" + subscriptionsContextPath;
        }
    }

    public void setSubscriptionsContextPath(String subscriptionsContextPath) {
        this.subscriptionsContextPath = subscriptionsContextPath;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public Boolean getSsl() {
        return ssl;
    }

    public void setSsl(Boolean ssl) {
        this.ssl = ssl;
    }

    public Integer getSessionTimeoutMillis() {
        return SessionTimeoutMillis;
    }

    public void setSessionTimeoutMillis(Integer sessionTimeoutMillis) {
        SessionTimeoutMillis = sessionTimeoutMillis;
    }

    public Integer getConnectTimeoutMillis() {
        return connectTimeoutMillis;
    }

    public void setConnectTimeoutMillis(Integer connectTimeoutMillis) {
        this.connectTimeoutMillis = connectTimeoutMillis;
    }

    public Integer getMaxMessagesPerRead() {
        return maxMessagesPerRead;
    }

    public void setMaxMessagesPerRead(Integer maxMessagesPerRead) {
        this.maxMessagesPerRead = maxMessagesPerRead;
    }

    public Integer getMaxMessagesPerWrite() {
        return maxMessagesPerWrite;
    }

    public void setMaxMessagesPerWrite(Integer maxMessagesPerWrite) {
        this.maxMessagesPerWrite = maxMessagesPerWrite;
    }

    public Integer getWriteSpinCount() {
        return writeSpinCount;
    }

    public void setWriteSpinCount(Integer writeSpinCount) {
        this.writeSpinCount = writeSpinCount;
    }

    public Integer getWriteBufferLowWaterMark() {
        return writeBufferLowWaterMark;
    }

    public void setWriteBufferLowWaterMark(Integer writeBufferLowWaterMark) {
        this.writeBufferLowWaterMark = writeBufferLowWaterMark;
    }

    public Integer getWriteBufferWaterMark() {
        return writeBufferWaterMark;
    }

    public void setWriteBufferWaterMark(Integer writeBufferWaterMark) {
        this.writeBufferWaterMark = writeBufferWaterMark;
    }

    public Boolean getAllowHalfClosure() {
        return allowHalfClosure;
    }

    public void setAllowHalfClosure(Boolean allowHalfClosure) {
        this.allowHalfClosure = allowHalfClosure;
    }

    public Boolean getAutoRead() {
        return autoRead;
    }

    public void setAutoRead(Boolean autoRead) {
        this.autoRead = autoRead;
    }

    public Boolean getAutoClose() {
        return autoClose;
    }

    public void setAutoClose(Boolean autoClose) {
        this.autoClose = autoClose;
    }

    public Boolean getSoBroadcast() {
        return soBroadcast;
    }

    public void setSoBroadcast(Boolean soBroadcast) {
        this.soBroadcast = soBroadcast;
    }

    public Boolean getSoKeepalive() {
        return soKeepalive;
    }

    public void setSoKeepalive(Boolean soKeepalive) {
        this.soKeepalive = soKeepalive;
    }

    public Integer getSoSndbuf() {
        return soSndbuf;
    }

    public void setSoSndbuf(Integer soSndbuf) {
        this.soSndbuf = soSndbuf;
    }

    public Integer getSoRcvbuf() {
        return soRcvbuf;
    }

    public void setSoRcvbuf(Integer soRcvbuf) {
        this.soRcvbuf = soRcvbuf;
    }

    public Boolean getSoReuseaddr() {
        return soReuseaddr;
    }

    public void setSoReuseaddr(Boolean soReuseaddr) {
        this.soReuseaddr = soReuseaddr;
    }

    public Integer getSoLinger() {
        return soLinger;
    }

    public void setSoLinger(Integer soLinger) {
        this.soLinger = soLinger;
    }

    public Integer getSoBacklog() {
        return soBacklog;
    }

    public void setSoBacklog(Integer soBacklog) {
        this.soBacklog = soBacklog;
    }

    public Integer getSoTimeout() {
        return soTimeout;
    }

    public void setSoTimeout(Integer soTimeout) {
        this.soTimeout = soTimeout;
    }

    public Integer getIpTos() {
        return ipTos;
    }

    public void setIpTos(Integer ipTos) {
        this.ipTos = ipTos;
    }

    public Integer getIpMulticastTtl() {
        return ipMulticastTtl;
    }

    public void setIpMulticastTtl(Integer ipMulticastTtl) {
        this.ipMulticastTtl = ipMulticastTtl;
    }

    public Boolean getIpMulticastLoopDisabled() {
        return ipMulticastLoopDisabled;
    }

    public void setIpMulticastLoopDisabled(Boolean ipMulticastLoopDisabled) {
        this.ipMulticastLoopDisabled = ipMulticastLoopDisabled;
    }

    public Boolean getTcpNodelay() {
        return tcpNodelay;
    }

    public void setTcpNodelay(Boolean tcpNodelay) {
        this.tcpNodelay = tcpNodelay;
    }

    public Boolean getTcpFastopenConnect() {
        return tcpFastopenConnect;
    }

    public void setTcpFastopenConnect(Boolean tcpFastopenConnect) {
        this.tcpFastopenConnect = tcpFastopenConnect;
    }

    public Integer getTcpFastopen() {
        return tcpFastopen;
    }

    public void setTcpFastopen(Integer tcpFastopen) {
        this.tcpFastopen = tcpFastopen;
    }

    public Boolean getDatagramChannelActiveOnRegistration() {
        return datagramChannelActiveOnRegistration;
    }

    public void setDatagramChannelActiveOnRegistration(Boolean datagramChannelActiveOnRegistration) {
        this.datagramChannelActiveOnRegistration = datagramChannelActiveOnRegistration;
    }

    public Boolean getSingleEventexecutorPerGroup() {
        return singleEventexecutorPerGroup;
    }

    public void setSingleEventexecutorPerGroup(Boolean singleEventexecutorPerGroup) {
        this.singleEventexecutorPerGroup = singleEventexecutorPerGroup;
    }
}
