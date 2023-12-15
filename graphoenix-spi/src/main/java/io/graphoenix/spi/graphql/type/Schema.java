package io.graphoenix.spi.graphql.type;

import graphql.parser.antlr.GraphqlParser;
import io.graphoenix.spi.graphql.Definition;
import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroupFile;

public class Schema implements Definition {

    private final STGroupFile stGroupFile = new STGroupFile("stg/type/Schema.stg");
    private String query;
    private String mutation;
    private String subscription;

    public Schema() {
    }

    public Schema(GraphqlParser.SchemaDefinitionContext schemaDefinitionContext) {
        this.query = schemaDefinitionContext.operationTypeDefinition().stream()
                .filter(operationTypeDefinitionContext -> operationTypeDefinitionContext.operationType().QUERY() != null)
                .findFirst()
                .map(operationTypeDefinitionContext -> operationTypeDefinitionContext.typeName().name().getText())
                .orElse(null);

        this.mutation = schemaDefinitionContext.operationTypeDefinition().stream()
                .filter(operationTypeDefinitionContext -> operationTypeDefinitionContext.operationType().MUTATION() != null)
                .findFirst()
                .map(operationTypeDefinitionContext -> operationTypeDefinitionContext.typeName().name().getText())
                .orElse(null);

        this.subscription = schemaDefinitionContext.operationTypeDefinition().stream()
                .filter(operationTypeDefinitionContext -> operationTypeDefinitionContext.operationType().SUBSCRIPTION() != null)
                .findFirst()
                .map(operationTypeDefinitionContext -> operationTypeDefinitionContext.typeName().name().getText())
                .orElse(null);
    }

    public String getQuery() {
        return query;
    }

    public Schema setQuery(String query) {
        this.query = query;
        return this;
    }

    public String getMutation() {
        return mutation;
    }

    public Schema setMutation(String mutation) {
        this.mutation = mutation;
        return this;
    }

    public String getSubscription() {
        return subscription;
    }

    public Schema setSubscription(String subscription) {
        this.subscription = subscription;
        return this;
    }

    @Override
    public boolean isSchema() {
        return true;
    }

    @Override
    public String toString() {
        ST st = stGroupFile.getInstanceOf("schemaDefinition");
        st.add("schema", this);
        return st.render();
    }
}
