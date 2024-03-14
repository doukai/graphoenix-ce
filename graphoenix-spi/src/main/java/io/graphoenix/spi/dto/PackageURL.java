package io.graphoenix.spi.dto;

import java.util.Map;

public class PackageURL {
    public static final String SCHEMA_NAME = "schema";
    public static final String HOST_NAME = "host";
    public static final String PORT_NAME = "port";
    public static final String PATH_NAME = "path";

    private String schema;
    private String host;
    private Integer port;
    private String path;

    public PackageURL(String schema, String host, Integer port, String path) {
        this.schema = schema;
        this.host = host;
        this.port = port;
        this.path = path;
    }

    public PackageURL(Map<String, Object> map) {
        this.schema = (String) map.get(SCHEMA_NAME);
        this.host = (String) map.get(HOST_NAME);
        this.port = (int) map.getOrDefault(PORT_NAME, -1);
        this.path = (String) map.getOrDefault(PATH_NAME, "");
    }

    public String getSchema() {
        return schema;
    }

    public void setSchema(String schema) {
        this.schema = schema;
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
                "schema='" + schema + '\'' +
                ", host='" + host + '\'' +
                ", port=" + port +
                ", path='" + path + '\'' +
                '}';
    }
}
