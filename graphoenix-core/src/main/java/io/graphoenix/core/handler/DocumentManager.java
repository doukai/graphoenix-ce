package io.graphoenix.core.handler;

import io.graphoenix.spi.error.GraphQLErrors;
import io.graphoenix.spi.graphql.Definition;
import io.graphoenix.spi.graphql.Document;
import io.graphoenix.spi.graphql.common.ObjectValueWithVariable;
import io.graphoenix.spi.graphql.common.ValueWithVariable;
import io.graphoenix.spi.graphql.operation.Operation;
import io.graphoenix.spi.graphql.type.*;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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

    public Definition getInputBelong(Definition definition) {
        String typeName = null;
        if (definition.getName().endsWith(SUFFIX_INPUT)) {
            typeName = definition.getName().substring(0, definition.getName().lastIndexOf(SUFFIX_INPUT));
        } else if (definition.getName().endsWith(SUFFIX_EXPRESSION)) {
            typeName = definition.getName().substring(0, definition.getName().lastIndexOf(SUFFIX_EXPRESSION));
        } else {
            String queryTypeName = getDocument().getQueryOperationTypeOrError().getName();
            if (definition.getName().endsWith(SUFFIX_LIST + queryTypeName + SUFFIX_ARGUMENTS)) {
                typeName = definition.getName().substring(0, definition.getName().lastIndexOf(SUFFIX_LIST + queryTypeName + SUFFIX_ARGUMENTS));
            } else if (definition.getName().endsWith(queryTypeName + SUFFIX_ARGUMENTS)) {
                typeName = definition.getName().substring(0, definition.getName().lastIndexOf(queryTypeName + SUFFIX_ARGUMENTS));
            }

            String mutationTypeName = getDocument().getMutationOperationTypeOrError().getName();
            if (definition.getName().endsWith(SUFFIX_LIST + mutationTypeName + SUFFIX_ARGUMENTS)) {
                typeName = definition.getName().substring(0, definition.getName().lastIndexOf(SUFFIX_LIST + mutationTypeName + SUFFIX_ARGUMENTS));
            } else if (definition.getName().endsWith(mutationTypeName + SUFFIX_ARGUMENTS)) {
                typeName = definition.getName().substring(0, definition.getName().lastIndexOf(mutationTypeName + SUFFIX_ARGUMENTS));
            }

            String subscriptionTypeName = getDocument().getSubscriptionOperationTypeOrError().getName();
            if (definition.getName().endsWith(SUFFIX_LIST + subscriptionTypeName + SUFFIX_ARGUMENTS)) {
                typeName = definition.getName().substring(0, definition.getName().lastIndexOf(SUFFIX_LIST + subscriptionTypeName + SUFFIX_ARGUMENTS));
            } else if (definition.getName().endsWith(subscriptionTypeName + SUFFIX_ARGUMENTS)) {
                typeName = definition.getName().substring(0, definition.getName().lastIndexOf(subscriptionTypeName + SUFFIX_ARGUMENTS));
            }
        }
        if (typeName != null) {
            return getDocument().getDefinition(typeName);
        }
        return null;
    }

    public ObjectType getInputObjectBelong(InputObjectType inputObjectType) {
        Definition definition = getInputBelong(inputObjectType);
        if (definition != null) {
            if (definition.isObject()) {
                return definition.asObject();
            }
        }
        return null;
    }

    public List<ObjectValueWithVariable> getDirectiveInvokes(Definition definition) {
        return Stream.ofNullable(definition.getDirectives())
                .flatMap(Collection::stream)
                .flatMap(directive -> document.getDirective(directive.getName()).stream())
                .flatMap(directiveDefinition ->
                        Stream.ofNullable(directiveDefinition.getArguments())
                                .flatMap(Collection::stream)
                                .filter(inputValue -> inputValue.getName().equals(DIRECTIVE_INVOKES_NAME))
                                .flatMap(inputValue ->
                                        inputValue.getDefaultValue().asObject().getValueWithVariableOrEmpty(DIRECTIVE_INVOKES_METHODS_NAME).stream()
                                                .filter(ValueWithVariable::isArray)
                                                .map(ValueWithVariable::asArray)
                                                .flatMap(arrayValueWithVariable -> arrayValueWithVariable.getValueWithVariables().stream())
                                                .filter(ValueWithVariable::isObject)
                                                .map(ValueWithVariable::asObject)
                                )
                )
                .collect(Collectors.toList());
    }
}
