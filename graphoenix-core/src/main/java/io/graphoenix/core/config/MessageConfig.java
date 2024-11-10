package io.graphoenix.core.config;

import com.typesafe.config.Optional;
import org.eclipse.microprofile.config.inject.ConfigProperties;

@ConfigProperties(prefix = "message")
public class MessageConfig {

    @Optional
    private String queryObject = "Query";

    @Optional
    private String mutationObject = "Mutation";

    @Optional
    private String subscriptionObject = "Subscription";

    @Optional
    private String relationObject = "The relationship object between {0} and {1}";

    @Optional
    private String idField = "ID";

    @Optional
    private String refField = "{0} reference";

    public String getQueryObject() {
        return queryObject;
    }

    public void setQueryObject(String queryObject) {
        this.queryObject = queryObject;
    }

    public String getMutationObject() {
        return mutationObject;
    }

    public void setMutationObject(String mutationObject) {
        this.mutationObject = mutationObject;
    }

    public String getSubscriptionObject() {
        return subscriptionObject;
    }

    public void setSubscriptionObject(String subscriptionObject) {
        this.subscriptionObject = subscriptionObject;
    }

    public String getRelationObject() {
        return relationObject;
    }

    public void setRelationObject(String relationObject) {
        this.relationObject = relationObject;
    }

    public String getIdField() {
        return idField;
    }

    public void setIdField(String idField) {
        this.idField = idField;
    }

    public String getRefField() {
        return refField;
    }

    public void setRefField(String refField) {
        this.refField = refField;
    }
}
