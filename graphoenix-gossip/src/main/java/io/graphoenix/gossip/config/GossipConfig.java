package io.graphoenix.gossip.config;

import com.typesafe.config.Optional;
import org.eclipse.microprofile.config.inject.ConfigProperties;

@ConfigProperties(prefix = "gossip")
public class GossipConfig {

    @Optional
    private Integer port = 3000;

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }
}
