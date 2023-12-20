package io.graphoenix.core.handler;

import io.graphoenix.spi.annotation.Application;
import io.graphoenix.spi.graphql.Definition;
import io.graphoenix.spi.graphql.Document;
import io.graphoenix.spi.graphql.type.FieldDefinition;
import io.graphoenix.spi.graphql.type.InterfaceType;
import io.graphoenix.spi.graphql.type.ObjectType;
import io.graphoenix.spi.graphql.type.Schema;

import java.util.Optional;

import static io.graphoenix.spi.constant.Hammurabi.*;

@Application
public class DocumentManager {
    private Document document;

    public Document getDocument() {
        return document;
    }

    public DocumentManager setDocument(Document document) {
        this.document = document;
        return this;
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

    public Optional<ObjectType> getQueryOperationType() {
        return Optional.ofNullable(document.getDefinition(document.getSchema().map(Schema::getQuery).orElse(TYPE_QUERY_NAME)))
                .map(Definition::asObject);
    }

    public Optional<ObjectType> getMutationOperationType() {
        return Optional.ofNullable(document.getDefinition(document.getSchema().map(Schema::getMutation).orElse(TYPE_MUTATION_NAME)))
                .map(Definition::asObject);
    }

    public Optional<ObjectType> getSubscriptionOperationType() {
        return Optional.ofNullable(document.getDefinition(document.getSchema().map(Schema::getSubscription).orElse(TYPE_SUBSCRIPTION_NAME)))
                .map(Definition::asObject);
    }

    public Optional<InterfaceType> getMetaInterface() {
        return Optional.ofNullable(document.getDefinition(INTERFACE_META_NAME)).map(definition -> (InterfaceType) definition);
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

    public ObjectType getFieldMapWithTypeDefinition(FieldDefinition fieldDefinition) {
        return document.getDefinition(fieldDefinition.getMapWithType()).asObject();
    }

    public FieldDefinition getFieldMapFromFieldDefinition(ObjectType objectType, FieldDefinition fieldDefinition) {
        return objectType.getField(fieldDefinition.getMapFrom());
    }

    public Optional<FieldDefinition> getFieldMapToFieldDefinition(FieldDefinition fieldDefinition) {
        return fieldDefinition.getMapTo().map(mapTo -> getFieldTypeDefinition(fieldDefinition).asObject().getField(mapTo));
    }

    public ObjectType getFieldFetchWithTypeDefinition(FieldDefinition fieldDefinition) {
        return document.getDefinition(fieldDefinition.getFetchWithType()).asObject();
    }

    public FieldDefinition getFieldFetchFromFieldDefinition(ObjectType objectType, FieldDefinition fieldDefinition) {
        return objectType.getField(fieldDefinition.getFetchFrom());
    }

    public Optional<FieldDefinition> getFieldFetchToFieldDefinition(FieldDefinition fieldDefinition) {
        return fieldDefinition.getFetchTo().map(mapTo -> getFieldTypeDefinition(fieldDefinition).asObject().getField(mapTo));
    }
}
