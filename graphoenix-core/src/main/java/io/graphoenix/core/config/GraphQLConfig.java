package io.graphoenix.core.config;

import com.typesafe.config.Optional;
import org.eclipse.microprofile.config.inject.ConfigProperties;

@ConfigProperties(prefix = "graphql")
public class GraphQLConfig {

    @Optional
    private String graphQL;

    @Optional
    private String graphQLFileName;

    @Optional
    private String graphQLPath = "graphql";

    @Optional
    private Boolean build = true;

    @Optional
    private Boolean mapToLocalFetch = false;

    @Optional
    private String defaultOperationHandlerName;

    public String getGraphQL() {
        return graphQL;
    }

    public void setGraphQL(String graphQL) {
        this.graphQL = graphQL;
    }

    public String getGraphQLFileName() {
        return graphQLFileName;
    }

    public void setGraphQLFileName(String graphQLFileName) {
        this.graphQLFileName = graphQLFileName;
    }

    public String getGraphQLPath() {
        return graphQLPath;
    }

    public void setGraphQLPath(String graphQLPath) {
        this.graphQLPath = graphQLPath;
    }

    public Boolean getBuild() {
        return build;
    }

    public void setBuild(Boolean build) {
        this.build = build;
    }

    public Boolean getMapToLocalFetch() {
        return mapToLocalFetch;
    }

    public void setMapToLocalFetch(Boolean mapToLocalFetch) {
        this.mapToLocalFetch = mapToLocalFetch;
    }

    public String getDefaultOperationHandlerName() {
        return defaultOperationHandlerName;
    }

    public void setDefaultOperationHandlerName(String defaultOperationHandlerName) {
        this.defaultOperationHandlerName = defaultOperationHandlerName;
    }
}
