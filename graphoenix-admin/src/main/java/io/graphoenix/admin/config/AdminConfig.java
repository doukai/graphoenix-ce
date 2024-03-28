package io.graphoenix.admin.config;

import com.typesafe.config.Optional;
import org.eclipse.microprofile.config.inject.ConfigProperties;

@ConfigProperties(prefix = "admin")
public class AdminConfig {

    @Optional
    private Integer port = 8906;

    @Optional
    private String graphQLPath = "http://localhost:8080/graphql";

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public String getGraphQLPath() {
        return graphQLPath;
    }

    public void setGraphQLPath(String graphQLPath) {
        this.graphQLPath = graphQLPath;
    }
}
