package io.graphoenix.core.handler;

import io.graphoenix.spi.error.GraphQLErrors;
import io.graphoenix.spi.graphql.Definition;
import io.graphoenix.spi.graphql.Document;
import io.graphoenix.spi.graphql.operation.Operation;
import io.graphoenix.spi.graphql.type.*;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.Optional;

import static io.graphoenix.spi.constant.Hammurabi.*;
import static io.graphoenix.spi.error.GraphQLErrorType.UNSUPPORTED_OPERATION_TYPE;

@ApplicationScoped
public class DocumentManager {
    private final Document document = new Document();

    public Document getDocument() {
        return document;
    }

    public boolean isQueryOperationType(Definition definition) {
        return definition.isObject() &&
                definition.getName().equals(document.getSchema().map(Schema::getQuery).orElse(TYPE_QUERY_NAME));
    }

    public boolean isMutationOperationType(Definition definition) {
        return definition.isObject() &&
                definition.getName().equals(document.getSchema().map(Schema::getMutation).orElse(TYPE_MUTATION_NAME));
    }

    public boolean isSubscriptionOperationType(Definition definition) {
        return definition.isObject() &&
                definition.getName().equals(document.getSchema().map(Schema::getSubscription).orElse(TYPE_SUBSCRIPTION_NAME));
    }

    public boolean isOperationType(Definition definition) {
        return isQueryOperationType(definition) || isMutationOperationType(definition) || isSubscriptionOperationType(definition);
    }

    public boolean isMetaInterfaceField(FieldDefinition fieldDefinition) {
        return Optional.ofNullable(document.getDefinition(INTERFACE_META_NAME))
                .map(definition -> (InterfaceType) definition).stream()
                .flatMap(interfaceType -> interfaceType.getFields().stream())
                .anyMatch(interfaceFieldDefinition -> interfaceFieldDefinition.getName().equals(fieldDefinition.getName()));
    }

    public Definition getFieldTypeDefinition(FieldDefinition fieldDefinition) {
        return document.getDefinition(fieldDefinition.getType().getTypeName().getName());
    }

    public boolean isMapAnchor(ObjectType objectType, FieldDefinition fieldDefinition) {
        return fieldDefinition.isMapAnchor() ||
                objectType.getIDField()
                        .flatMap(idFieldDefinition ->
                                fieldDefinition.getMapFrom()
                                        .map(mapFrom -> !idFieldDefinition.getName().equals(mapFrom))
                        )
                        .orElse(false);
    }

    public boolean isFetchAnchor(ObjectType objectType, FieldDefinition fieldDefinition) {
        return fieldDefinition.isFetchAnchor() ||
                objectType.getIDField()
                        .flatMap(idFieldDefinition ->
                                fieldDefinition.getFetchFrom()
                                        .map(mapFrom -> !idFieldDefinition.getName().equals(mapFrom))
                        )
                        .orElse(false);
    }

    public ObjectType getFieldMapWithTypeDefinition(FieldDefinition fieldDefinition) {
        return document.getDefinition(fieldDefinition.getMapWithTypeOrError()).asObject();
    }

    public FieldDefinition getFieldMapFromFieldDefinition(ObjectType objectType, FieldDefinition fieldDefinition) {
        return objectType.getField(fieldDefinition.getMapFromOrError());
    }

    public Optional<FieldDefinition> getFieldMapToFieldDefinition(FieldDefinition fieldDefinition) {
        return fieldDefinition.getMapTo().map(mapTo -> getFieldTypeDefinition(fieldDefinition).asObject().getField(mapTo));
    }

    public ObjectType getFieldFetchWithTypeDefinition(FieldDefinition fieldDefinition) {
        return document.getDefinition(fieldDefinition.getFetchWithTypeOrError()).asObject();
    }

    public FieldDefinition getFieldFetchFromFieldDefinition(ObjectType objectType, FieldDefinition fieldDefinition) {
        return objectType.getField(fieldDefinition.getFetchFromOrError());
    }

    public Optional<FieldDefinition> getFieldFetchToFieldDefinition(FieldDefinition fieldDefinition) {
        return fieldDefinition.getFetchTo().map(mapTo -> getFieldTypeDefinition(fieldDefinition).asObject().getField(mapTo));
    }

    public Definition getInputValueTypeDefinition(InputValue inputValue) {
        return document.getDefinition(inputValue.getType().getTypeName().getName());
    }

    public Optional<ObjectType> getOperationType(Operation operation) {
        switch (operation.getOperationType()) {
            case OPERATION_QUERY_NAME:
                return document.getQueryOperationType();
            case OPERATION_MUTATION_NAME:
                return document.getMutationOperationType();
            case OPERATION_SUBSCRIPTION_NAME:
                return document.getSubscriptionOperationType();
            default:
                throw new GraphQLErrors(UNSUPPORTED_OPERATION_TYPE.bind(operation.getOperationType()));
        }
    }

    public ObjectType getOperationTypeOrError(Operation operation) {
        switch (operation.getOperationType()) {
            case OPERATION_QUERY_NAME:
                return document.getQueryOperationTypeOrError();
            case OPERATION_MUTATION_NAME:
                return document.getMutationOperationTypeOrError();
            case OPERATION_SUBSCRIPTION_NAME:
                return document.getSubscriptionOperationTypeOrError();
            default:
                throw new GraphQLErrors(UNSUPPORTED_OPERATION_TYPE.bind(operation.getOperationType()));
        }
    }
}
