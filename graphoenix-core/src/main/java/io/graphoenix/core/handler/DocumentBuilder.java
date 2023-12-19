package io.graphoenix.core.handler;

import io.graphoenix.core.config.PackageConfig;
import io.graphoenix.spi.annotation.Application;
import io.graphoenix.spi.graphql.AbstractDefinition;
import io.graphoenix.spi.graphql.Definition;
import io.graphoenix.spi.graphql.Document;
import io.graphoenix.spi.graphql.common.Directive;
import io.graphoenix.spi.graphql.type.*;
import jakarta.inject.Inject;

import java.util.LinkedHashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static io.graphoenix.core.utils.NameUtil.getGrpcTypeName;
import static io.graphoenix.core.utils.PredicateUtil.not;
import static io.graphoenix.spi.constant.Hammurabi.*;
import static io.graphoenix.spi.utils.StreamUtil.distinctByKey;

@Application
public class DocumentBuilder {

    private final PackageConfig packageConfig;
    private final PackageManager packageManager;
    private final DocumentManager documentManager;

    @Inject
    public DocumentBuilder(PackageConfig packageConfig, PackageManager packageManager, DocumentManager documentManager) {
        this.packageConfig = packageConfig;
        this.packageManager = packageManager;
        this.documentManager = documentManager;
    }

    public Document build(Document document) {

        document.getDefinitions().stream()
                .filter(Definition::isObject)
                .map(definition -> (AbstractDefinition) definition)
                .filter(packageManager::isOwnPackage)
                .filter(abstractDefinition -> not(abstractDefinition.isContainerType()))
                .filter(abstractDefinition -> not(documentManager.isOperationType(abstractDefinition)))


        return document;
    }

    public ObjectType buildObject(ObjectType objectType) {
        if (objectType.getPackageName().isEmpty()) {
            objectType.addDirective(
                    new Directive(DIRECTIVE_PACKAGE_INFO_NAME)
                            .addArgument("packageName", packageConfig.getPackageName())
                            .addArgument("grpcPackageName", packageConfig.getGrpcPackageName())
            );
        }

        if (objectType.getClassName().isEmpty()) {
            objectType.addDirective(
                    new Directive(DIRECTIVE_CLASS_INFO_NAME)
                            .addArgument("className", packageConfig.getObjectTypePackageName() + "." + objectType.getName())
                            .addArgument("grpcClassName", packageConfig.getGrpcObjectTypePackageName() + "." + getGrpcTypeName(objectType.getName()))
            );
        }

        objectType.setFields(
                objectType.getFields().stream()
                        .map(fieldDefinition ->
                                fieldDefinition.isInvokeField() ?
                                        fieldDefinition :
                                        buildField(objectType, fieldDefinition)
                        )
                        .collect(Collectors.toCollection(LinkedHashSet::new))
        );
        return objectType;
    }

    public FieldDefinition buildField(ObjectType objectType, FieldDefinition fieldDefinition) {
        Definition fieldTypeDefinition = documentManager.getFieldTypeDefinition(fieldDefinition);
        if (fieldTypeDefinition.isLeaf()) {
            if (fieldDefinition.getType().hasList()) {
                fieldDefinition
                        .addArgument(new InputValue(INPUT_VALUE_OPERATOR_OPR_NAME).setType(INPUT_OPERATOR_NAME).setDefaultValue(INPUT_VALUE_OPERATOR_OPR_EQ))
                        .addArgument(new InputValue(INPUT_VALUE_OPERATOR_VAL_NAME).setType(fieldDefinition.getType().getTypeName()))
                        .addArgument(new InputValue(INPUT_VALUE_OPERATOR_ARR_NAME).setType(new ListType(fieldDefinition.getType().getTypeName())))
                        .addArgument(new InputValue(INPUT_VALUE_FIRST_NAME).setType(SCALA_INT_NAME))
                        .addArgument(new InputValue(INPUT_VALUE_LAST_NAME).setType(SCALA_INT_NAME))
                        .addArgument(new InputValue(INPUT_VALUE_OFFSET_NAME).setType(SCALA_INT_NAME))
                        .addArgument(new InputValue(INPUT_VALUE_SORT_NAME).setType(INPUT_SORT_NAME));

                documentManager.getFieldMapWithTypeDefinition(fieldDefinition)
                        .flatMap(mapWithTypeDefinition -> mapWithTypeDefinition.getCursorField()
                                .or(mapWithTypeDefinition::getIDField)
                        )
                        .ifPresent(cursorField ->
                                fieldDefinition
                                        .addArgument(new InputValue(INPUT_VALUE_AFTER_NAME).setType(cursorField.getType().getTypeName()))
                                        .addArgument(new InputValue(INPUT_VALUE_BEFORE_NAME).setType(cursorField.getType().getTypeName()))
                        );
            }
        } else {
            if (documentManager.isMutationOperationType(objectType)) {
                fieldDefinition.addArguments(buildArgumentsFromObjectType((ObjectType) fieldTypeDefinition, InputType.INPUT));
            } else {
                fieldDefinition
                        .addArguments(buildArgumentsFromObjectType((ObjectType) fieldTypeDefinition, InputType.EXPRESSION))
                        .addArgument(new InputValue(INPUT_VALUE_GROUP_BY_NAME).setType(new ListType(new NonNullType(new TypeName(SCALA_STRING_NAME)))));
                if (fieldDefinition.getType().hasList()) {
                    fieldDefinition
                            .addArgument(new InputValue(INPUT_VALUE_FIRST_NAME).setType(SCALA_INT_NAME))
                            .addArgument(new InputValue(INPUT_VALUE_LAST_NAME).setType(SCALA_INT_NAME))
                            .addArgument(new InputValue(INPUT_VALUE_OFFSET_NAME).setType(SCALA_INT_NAME))
                            .addArgument(new InputValue(INPUT_VALUE_ORDER_BY_NAME).setType(new TypeName(fieldTypeDefinition.getName() + InputType.ORDER_BY)));

                    objectType.getCursorField()
                            .or(objectType::getIDField)
                            .ifPresent(cursorField ->
                                    fieldDefinition
                                            .addArgument(new InputValue(INPUT_VALUE_AFTER_NAME).setType(cursorField.getType().getTypeName()))
                                            .addArgument(new InputValue(INPUT_VALUE_BEFORE_NAME).setType(cursorField.getType().getTypeName()))
                            );
                }
            }
        }
        return fieldDefinition;
    }

    public InputValue fieldToArgument(ObjectType objectType, FieldDefinition fieldDefinition, InputType inputType) {
        Definition fieldTypeDefinition = documentManager.getFieldTypeDefinition(fieldDefinition);
        if (inputType.equals(InputType.INPUT) || inputType.equals(InputType.MUTATION_ARGUMENTS)) {
            if (fieldDefinition.getName().equals(FIELD_TYPE_NAME_NAME)) {
                return new InputValue(FIELD_TYPE_NAME_NAME).setType(SCALA_STRING_NAME).setDefaultValue(objectType.getName());
            }
            Type argumentType;
            if (fieldTypeDefinition.isLeaf()) {
                argumentType = fieldDefinition.getType().getTypeName();
            } else {
                argumentType = new TypeName(fieldTypeDefinition.getName() + InputType.INPUT);
            }
            if (fieldDefinition.getType().hasList()) {
                argumentType = new ListType(argumentType);
            }
            InputValue inputValue = new InputValue(fieldDefinition.getName()).setType(argumentType);
            Optional.ofNullable(fieldDefinition.getDirective(DIRECTIVE_VALIDATION_NAME))
                    .ifPresent(inputValue::addDirective);
            return inputValue;
        } else if (inputType.equals(InputType.EXPRESSION) || inputType.equals(InputType.QUERY_ARGUMENTS) || inputType.equals(InputType.SUBSCRIPTION_ARGUMENTS)) {
            if (fieldDefinition.getName().equals(FIELD_DEPRECATED_NAME)) {
                return new InputValue(INPUT_VALUE_DEPRECATED_NAME).setType(SCALA_BOOLEAN_NAME).setDefaultValue(false);
            }
            Type argumentType;
            switch (fieldTypeDefinition.getName()) {
                case SCALA_BOOLEAN_NAME:
                    argumentType = new TypeName(SCALA_BOOLEAN_NAME + InputType.EXPRESSION);
                    break;
                case SCALA_ID_NAME:
                case SCALA_STRING_NAME:
                case SCALA_DATE_NAME:
                case SCALA_TIME_NAME:
                case SCALA_DATE_TIME_NAME:
                case SCALA_TIMESTAMP_NAME:
                    argumentType = new TypeName(SCALA_STRING_NAME + InputType.EXPRESSION);
                    break;
                case SCALA_INT_NAME:
                case SCALA_BIG_INTEGER_NAME:
                    argumentType = new TypeName(SCALA_INT_NAME + InputType.EXPRESSION);
                    break;
                case SCALA_FLOAT_NAME:
                case SCALA_BIG_DECIMAL_NAME:
                    argumentType = new TypeName(SCALA_FLOAT_NAME + InputType.EXPRESSION);
                    break;
                default:
                    argumentType = new TypeName(fieldTypeDefinition.getName() + InputType.EXPRESSION);
                    break;
            }
            return new InputValue(fieldDefinition.getName()).setType(argumentType);
        } else if (inputType.equals(InputType.ORDER_BY)) {
            return new InputValue(fieldTypeDefinition.getName()).setType(INPUT_VALUE_SORT_NAME);
        }
        throw new RuntimeException("unsupported input type:" + inputType);
    }

    public Set<InputValue> buildArgumentsFromObjectType(ObjectType objectType, InputType inputType) {
        if (inputType.equals(InputType.ORDER_BY)) {
            return Stream.concat(
                            objectType.getFields().stream(),
                            documentManager.getMetaInterface().stream()
                                    .flatMap(interfaceType -> interfaceType.getFields().stream())
                    )
                    .filter(fieldDefinition -> !fieldDefinition.isInvokeField())
                    .filter(fieldDefinition -> !fieldDefinition.isFunctionField())
                    .filter(fieldDefinition -> !fieldDefinition.isConnectionField())
                    .filter(fieldDefinition -> !fieldDefinition.isAggregateField())
                    .filter(fieldDefinition -> !fieldDefinition.getType().hasList())
                    .filter(fieldDefinition -> documentManager.getFieldTypeDefinition(fieldDefinition).isLeaf())
                    .map(fieldDefinition -> fieldToArgument(objectType, fieldDefinition, inputType))
                    .collect(Collectors.toCollection(LinkedHashSet::new));
        } else {
            return Stream.concat(
                            objectType.getFields().stream(),
                            documentManager.getMetaInterface().stream()
                                    .flatMap(interfaceType -> interfaceType.getFields().stream())
                    )
                    .filter(distinctByKey(FieldDefinition::getName))
                    .filter(fieldDefinition -> !fieldDefinition.isInvokeField())
                    .filter(fieldDefinition -> !fieldDefinition.isFunctionField())
                    .filter(fieldDefinition -> !fieldDefinition.isConnectionField())
                    .filter(fieldDefinition -> !fieldDefinition.isAggregateField())
                    .filter(fieldDefinition -> !documentManager.getFieldTypeDefinition(fieldDefinition).isContainerType())
                    .map(fieldDefinition -> fieldToArgument(objectType, fieldDefinition, inputType))
                    .collect(Collectors.toCollection(LinkedHashSet::new));
        }
    }

    private enum InputType {
        EXPRESSION(SUFFIX_EXPRESSION),
        INPUT(SUFFIX_INPUT),
        QUERY_ARGUMENTS(SUFFIX_ARGUMENTS),
        MUTATION_ARGUMENTS(SUFFIX_ARGUMENTS),
        SUBSCRIPTION_ARGUMENTS(SUFFIX_ARGUMENTS),
        ORDER_BY(SUFFIX_ORDER_BY),
        CONNECTION(SUFFIX_CONNECTION),
        EDGE(SUFFIX_EDGE);

        private final String suffix;

        InputType(String suffix) {
            this.suffix = suffix;
        }

        @Override
        public String toString() {
            return suffix;
        }
    }
}
