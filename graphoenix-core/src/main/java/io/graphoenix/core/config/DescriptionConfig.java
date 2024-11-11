package io.graphoenix.core.config;

import com.typesafe.config.Optional;
import org.eclipse.microprofile.config.inject.ConfigProperties;

@ConfigProperties(prefix = "description")
public class DescriptionConfig {

    @Optional
    private String queryObject = "Query";

    @Optional
    private String mutationObject = "Mutation";

    @Optional
    private String subscriptionObject = "Subscription";

    @Optional
    private String connectionObject = "Connection Object for %s";

    @Optional
    private String edgeObject = "Edge Object for %s";

    @Optional
    private String relationObject = "Relationship Object between %s and %s";

    @Optional
    private String queryExpression = "Query Expression Input for %s";

    @Optional
    private String mutationInput = "Mutation Input for %s";

    @Optional
    private String subscriptionExpression = "Subscription Expression Input for %s";

    @Optional
    private String orderByInput = "Order Input for %s";

    @Optional
    private String queryArguments = "Query Arguments for %s";

    @Optional
    private String queryListArguments = "Query List Arguments for %s";

    @Optional
    private String queryConnectionArguments = "Query Connection Arguments for %s";

    @Optional
    private String mutationArguments = "Mutation Arguments for %s";

    @Optional
    private String mutationListArguments = "Mutation List Arguments for %s";

    @Optional
    private String subscriptionArguments = "Subscription Arguments for %s";

    @Optional
    private String subscriptionListArguments = "Subscription List Arguments for %s";

    @Optional
    private String subscriptionConnectionArguments = "Subscription Connection Arguments for %s";

    @Optional
    private String queryField = "Query Field for %s";

    @Optional
    private String queryListField = "Query Field for %s List";

    @Optional
    private String queryConnectionField = "Query Connection for %s";

    @Optional
    private String mutationField = "Mutation Field for %s";

    @Optional
    private String mutationListField = "Mutation Field for %s List";

    @Optional
    private String subscriptionField = "Subscription Field for %s";

    @Optional
    private String subscriptionListField = "Subscription Field for %s List";

    @Optional
    private String subscriptionConnectionField = "Subscription Connection for %s";

    @Optional
    private String idField = "ID";

    @Optional
    private String deprecatedField = "Is Deprecated";

    @Optional
    private String refField = "%s Reference";

    @Optional
    private String typeNameField = "Type Name";

    @Optional
    private String connectionField = "Connection Field for %s";

    @Optional
    private String aggField = "Aggregate Field for %s";

    @Optional
    private String totalCountField = "Total Count";

    @Optional
    private String pageInfoField = "Page Info";

    @Optional
    private String edgesField = "Edges";

    @Optional
    private String nodeField = "Node";

    @Optional
    private String cursorField = "Cursor";

    @Optional
    private String countField = "Count of %s";

    @Optional
    private String sumField = "Sum of %s";

    @Optional
    private String avgField = "Avg of %s";

    @Optional
    private String maxField = "Max of %s";

    @Optional
    private String minField = "Min of %s";

    @Optional
    private String versionField = "Version";

    @Optional
    private String realmIdField = "Realm ID";

    @Optional
    private String createUserIdField = "Create User ID";

    @Optional
    private String createTimeField = "Create Time";

    @Optional
    private String updateUserIdField = "Update User ID";

    @Optional
    private String updateTimeField = "Update Time";

    @Optional
    private String createGroupIdField = "Create Group ID";

    @Optional
    private String oprArgument = "Operators";

    @Optional
    private String valArgument = "Value";

    @Optional
    private String arrArgument = "Array";

    @Optional
    private String firstArgument = "First";

    @Optional
    private String lastArgument = "Last";

    @Optional
    private String offsetArgument = "Offset";

    @Optional
    private String sortArgument = "Sort";

    @Optional
    private String afterArgument = "After";

    @Optional
    private String beforeArgument = "Before";

    @Optional
    private String groupByArgument = "Group By";

    @Optional
    private String orderByArgument = "Order By";

    @Optional
    private String includeDeprecatedArgument = "Include Deprecated";

    @Optional
    private String notArgument = "Not";

    @Optional
    private String condArgument = "Condition";

    @Optional
    private String exsArgument = "Expressions";

    @Optional
    private String whereArgument = "Where";

    @Optional
    private String inputArgument = "Input";

    @Optional
    private String listArgument = "Input List";

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

    public String getConnectionObject() {
        return connectionObject;
    }

    public void setConnectionObject(String connectionObject) {
        this.connectionObject = connectionObject;
    }

    public String getEdgeObject() {
        return edgeObject;
    }

    public void setEdgeObject(String edgeObject) {
        this.edgeObject = edgeObject;
    }

    public String getRelationObject() {
        return relationObject;
    }

    public void setRelationObject(String relationObject) {
        this.relationObject = relationObject;
    }

    public String getQueryExpression() {
        return queryExpression;
    }

    public void setQueryExpression(String queryExpression) {
        this.queryExpression = queryExpression;
    }

    public String getMutationInput() {
        return mutationInput;
    }

    public void setMutationInput(String mutationInput) {
        this.mutationInput = mutationInput;
    }

    public String getSubscriptionExpression() {
        return subscriptionExpression;
    }

    public void setSubscriptionExpression(String subscriptionExpression) {
        this.subscriptionExpression = subscriptionExpression;
    }

    public String getOrderByInput() {
        return orderByInput;
    }

    public void setOrderByInput(String orderByInput) {
        this.orderByInput = orderByInput;
    }

    public String getQueryArguments() {
        return queryArguments;
    }

    public void setQueryArguments(String queryArguments) {
        this.queryArguments = queryArguments;
    }

    public String getQueryListArguments() {
        return queryListArguments;
    }

    public void setQueryListArguments(String queryListArguments) {
        this.queryListArguments = queryListArguments;
    }

    public String getQueryConnectionArguments() {
        return queryConnectionArguments;
    }

    public void setQueryConnectionArguments(String queryConnectionArguments) {
        this.queryConnectionArguments = queryConnectionArguments;
    }

    public String getMutationArguments() {
        return mutationArguments;
    }

    public void setMutationArguments(String mutationArguments) {
        this.mutationArguments = mutationArguments;
    }

    public String getMutationListArguments() {
        return mutationListArguments;
    }

    public void setMutationListArguments(String mutationListArguments) {
        this.mutationListArguments = mutationListArguments;
    }

    public String getSubscriptionArguments() {
        return subscriptionArguments;
    }

    public void setSubscriptionArguments(String subscriptionArguments) {
        this.subscriptionArguments = subscriptionArguments;
    }

    public String getSubscriptionListArguments() {
        return subscriptionListArguments;
    }

    public void setSubscriptionListArguments(String subscriptionListArguments) {
        this.subscriptionListArguments = subscriptionListArguments;
    }

    public String getSubscriptionConnectionArguments() {
        return subscriptionConnectionArguments;
    }

    public void setSubscriptionConnectionArguments(String subscriptionConnectionArguments) {
        this.subscriptionConnectionArguments = subscriptionConnectionArguments;
    }

    public String getQueryField() {
        return queryField;
    }

    public void setQueryField(String queryField) {
        this.queryField = queryField;
    }

    public String getQueryListField() {
        return queryListField;
    }

    public void setQueryListField(String queryListField) {
        this.queryListField = queryListField;
    }

    public String getQueryConnectionField() {
        return queryConnectionField;
    }

    public void setQueryConnectionField(String queryConnectionField) {
        this.queryConnectionField = queryConnectionField;
    }

    public String getMutationField() {
        return mutationField;
    }

    public void setMutationField(String mutationField) {
        this.mutationField = mutationField;
    }

    public String getMutationListField() {
        return mutationListField;
    }

    public void setMutationListField(String mutationListField) {
        this.mutationListField = mutationListField;
    }

    public String getSubscriptionField() {
        return subscriptionField;
    }

    public void setSubscriptionField(String subscriptionField) {
        this.subscriptionField = subscriptionField;
    }

    public String getSubscriptionListField() {
        return subscriptionListField;
    }

    public void setSubscriptionListField(String subscriptionListField) {
        this.subscriptionListField = subscriptionListField;
    }

    public String getSubscriptionConnectionField() {
        return subscriptionConnectionField;
    }

    public void setSubscriptionConnectionField(String subscriptionConnectionField) {
        this.subscriptionConnectionField = subscriptionConnectionField;
    }

    public String getIdField() {
        return idField;
    }

    public void setIdField(String idField) {
        this.idField = idField;
    }

    public String getDeprecatedField() {
        return deprecatedField;
    }

    public void setDeprecatedField(String deprecatedField) {
        this.deprecatedField = deprecatedField;
    }

    public String getRefField() {
        return refField;
    }

    public void setRefField(String refField) {
        this.refField = refField;
    }

    public String getTypeNameField() {
        return typeNameField;
    }

    public void setTypeNameField(String typeNameField) {
        this.typeNameField = typeNameField;
    }

    public String getConnectionField() {
        return connectionField;
    }

    public void setConnectionField(String connectionField) {
        this.connectionField = connectionField;
    }

    public String getAggField() {
        return aggField;
    }

    public void setAggField(String aggField) {
        this.aggField = aggField;
    }

    public String getTotalCountField() {
        return totalCountField;
    }

    public void setTotalCountField(String totalCountField) {
        this.totalCountField = totalCountField;
    }

    public String getPageInfoField() {
        return pageInfoField;
    }

    public void setPageInfoField(String pageInfoField) {
        this.pageInfoField = pageInfoField;
    }

    public String getEdgesField() {
        return edgesField;
    }

    public void setEdgesField(String edgesField) {
        this.edgesField = edgesField;
    }

    public String getNodeField() {
        return nodeField;
    }

    public void setNodeField(String nodeField) {
        this.nodeField = nodeField;
    }

    public String getCursorField() {
        return cursorField;
    }

    public void setCursorField(String cursorField) {
        this.cursorField = cursorField;
    }

    public String getCountField() {
        return countField;
    }

    public void setCountField(String countField) {
        this.countField = countField;
    }

    public String getSumField() {
        return sumField;
    }

    public void setSumField(String sumField) {
        this.sumField = sumField;
    }

    public String getAvgField() {
        return avgField;
    }

    public void setAvgField(String avgField) {
        this.avgField = avgField;
    }

    public String getMaxField() {
        return maxField;
    }

    public void setMaxField(String maxField) {
        this.maxField = maxField;
    }

    public String getMinField() {
        return minField;
    }

    public void setMinField(String minField) {
        this.minField = minField;
    }

    public String getVersionField() {
        return versionField;
    }

    public void setVersionField(String versionField) {
        this.versionField = versionField;
    }

    public String getRealmIdField() {
        return realmIdField;
    }

    public void setRealmIdField(String realmIdField) {
        this.realmIdField = realmIdField;
    }

    public String getCreateUserIdField() {
        return createUserIdField;
    }

    public void setCreateUserIdField(String createUserIdField) {
        this.createUserIdField = createUserIdField;
    }

    public String getCreateTimeField() {
        return createTimeField;
    }

    public void setCreateTimeField(String createTimeField) {
        this.createTimeField = createTimeField;
    }

    public String getUpdateUserIdField() {
        return updateUserIdField;
    }

    public void setUpdateUserIdField(String updateUserIdField) {
        this.updateUserIdField = updateUserIdField;
    }

    public String getUpdateTimeField() {
        return updateTimeField;
    }

    public void setUpdateTimeField(String updateTimeField) {
        this.updateTimeField = updateTimeField;
    }

    public String getCreateGroupIdField() {
        return createGroupIdField;
    }

    public void setCreateGroupIdField(String createGroupIdField) {
        this.createGroupIdField = createGroupIdField;
    }

    public String getOprArgument() {
        return oprArgument;
    }

    public void setOprArgument(String oprArgument) {
        this.oprArgument = oprArgument;
    }

    public String getValArgument() {
        return valArgument;
    }

    public void setValArgument(String valArgument) {
        this.valArgument = valArgument;
    }

    public String getArrArgument() {
        return arrArgument;
    }

    public void setArrArgument(String arrArgument) {
        this.arrArgument = arrArgument;
    }

    public String getFirstArgument() {
        return firstArgument;
    }

    public void setFirstArgument(String firstArgument) {
        this.firstArgument = firstArgument;
    }

    public String getLastArgument() {
        return lastArgument;
    }

    public void setLastArgument(String lastArgument) {
        this.lastArgument = lastArgument;
    }

    public String getOffsetArgument() {
        return offsetArgument;
    }

    public void setOffsetArgument(String offsetArgument) {
        this.offsetArgument = offsetArgument;
    }

    public String getSortArgument() {
        return sortArgument;
    }

    public void setSortArgument(String sortArgument) {
        this.sortArgument = sortArgument;
    }

    public String getAfterArgument() {
        return afterArgument;
    }

    public void setAfterArgument(String afterArgument) {
        this.afterArgument = afterArgument;
    }

    public String getBeforeArgument() {
        return beforeArgument;
    }

    public void setBeforeArgument(String beforeArgument) {
        this.beforeArgument = beforeArgument;
    }

    public String getGroupByArgument() {
        return groupByArgument;
    }

    public void setGroupByArgument(String groupByArgument) {
        this.groupByArgument = groupByArgument;
    }

    public String getOrderByArgument() {
        return orderByArgument;
    }

    public void setOrderByArgument(String orderByArgument) {
        this.orderByArgument = orderByArgument;
    }

    public String getIncludeDeprecatedArgument() {
        return includeDeprecatedArgument;
    }

    public void setIncludeDeprecatedArgument(String includeDeprecatedArgument) {
        this.includeDeprecatedArgument = includeDeprecatedArgument;
    }

    public String getNotArgument() {
        return notArgument;
    }

    public void setNotArgument(String notArgument) {
        this.notArgument = notArgument;
    }

    public String getCondArgument() {
        return condArgument;
    }

    public void setCondArgument(String condArgument) {
        this.condArgument = condArgument;
    }

    public String getExsArgument() {
        return exsArgument;
    }

    public void setExsArgument(String exsArgument) {
        this.exsArgument = exsArgument;
    }

    public String getWhereArgument() {
        return whereArgument;
    }

    public void setWhereArgument(String whereArgument) {
        this.whereArgument = whereArgument;
    }

    public String getInputArgument() {
        return inputArgument;
    }

    public void setInputArgument(String inputArgument) {
        this.inputArgument = inputArgument;
    }

    public String getListArgument() {
        return listArgument;
    }

    public void setListArgument(String listArgument) {
        this.listArgument = listArgument;
    }
}
