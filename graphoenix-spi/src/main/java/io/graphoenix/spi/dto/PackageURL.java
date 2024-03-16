package io.graphoenix.spi.dto;

import java.util.Map;

public class PackageURL {
    public static final String PACKAGE_NAME_NAME = "packageName";
    public static final String PROTOCOL_NAME = "protocol";
    public static final String HOST_NAME = "host";
    public static final String PORT_NAME = "port";
    public static final String PATH_NAME = "path";

    private String packageName;
    private String protocol;
    private String host;
    private Integer port;
    private String path;

    public PackageURL(String packageName, String protocol, String host, Integer port, String path) {
        this.packageName = packageName;
        this.protocol = protocol;
        this.host = host;
        this.port = port;
        this.path = path;
    }

    public PackageURL(Map<String, Object> map) {
        this.packageName = (String) map.get(PACKAGE_NAME_NAME);
        this.protocol = (String) map.get(PROTOCOL_NAME);
        this.host = (String) map.get(HOST_NAME);
        this.port = (int) map.getOrDefault(PORT_NAME, -1);
        this.path = (String) map.getOrDefault(PATH_NAME, "");
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

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

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    @Override
    public String toString() {
        return "PackageURL{" +
                "packageName='" + packageName + '\'' +
                ", protocol='" + protocol + '\'' +
                ", host='" + host + '\'' +
                ", port=" + port +
                ", path='" + path + '\'' +
                '}';
    }
}
