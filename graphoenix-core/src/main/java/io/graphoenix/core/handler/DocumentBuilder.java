package io.graphoenix.core.handler;

import io.graphoenix.core.config.PackageConfig;
import io.graphoenix.spi.annotation.Application;
import io.graphoenix.spi.graphql.Definition;
import io.graphoenix.spi.graphql.Document;
import io.graphoenix.spi.graphql.common.Directive;
import io.graphoenix.spi.graphql.type.*;
import jakarta.inject.Inject;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static io.graphoenix.core.utils.NameUtil.getGrpcTypeName;
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
                .map(definition -> (ObjectType) definition)
                .filter(packageManager::isOwnPackage)
                .filter(objectType -> !objectType.isContainerType())
                .filter(objectType -> !documentManager.isOperationType(objectType))
                .forEach(this::buildObject);

        return document;
    }

    public ObjectType buildObject(ObjectType objectType) {
        if (objectType.getPackageName().isEmpty()) {
            objectType.addDirective(
                    new Directive(DIRECTIVE_PACKAGE_INFO_NAME)
                            .addArgument(DIRECTIVE_PACKAGE_INFO_PACKAGE_NAME_NAME, packageConfig.getPackageName())
                            .addArgument(DIRECTIVE_GRPC_PACKAGE_INFO_PACKAGE_NAME_NAME, packageConfig.getGrpcPackageName())
            );
        }

        if (objectType.getClassName().isEmpty()) {
            objectType.addDirective(
                    new Directive(DIRECTIVE_CLASS_INFO_NAME)
                            .addArgument(DIRECTIVE_CLASS_INFO_CLASS_NAME_NAME, packageConfig.getObjectTypePackageName() + "." + objectType.getName())
                            .addArgument(DIRECTIVE_GRPC_CLASS_INFO_CLASS_NAME_NAME, packageConfig.getGrpcObjectTypePackageName() + "." + getGrpcTypeName(objectType.getName()))
            );
        }

        objectType
                .addInterface(INTERFACE_META_NAME)
                .addFields(getMetaInterfaceFields())
                .setFields(
                        objectType.getFields().stream()
                                .map(fieldDefinition ->
                                        fieldDefinition.isInvokeField() ?
                                                fieldDefinition :
                                                buildField(objectType, fieldDefinition)
                                )
                                .collect(Collectors.toCollection(LinkedHashSet::new))
                )
                .addField(buildTypeNameField(objectType))
                .addFields(buildFunctionFieldList(objectType));

        return objectType;
    }

    public FieldDefinition buildField(ObjectType objectType, FieldDefinition fieldDefinition) {
        Definition fieldTypeDefinition = documentManager.getFieldTypeDefinition(fieldDefinition);
        if (fieldTypeDefinition.isLeaf()) {
            if (fieldDefinition.getType().hasList()) {
                fieldDefinition
                        .addArgument(new InputValue(INPUT_VALUE_OPERATOR_OPR_NAME).setType(new TypeName(INPUT_OPERATOR_NAME)).setDefaultValue(INPUT_VALUE_OPERATOR_OPR_EQ))
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
            return Stream
                    .concat(
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
            return Stream
                    .concat(
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

    public List<FieldDefinition> getMetaInterfaceFields() {
        return documentManager.getMetaInterface().stream()
                .flatMap(interfaceType -> interfaceType.getFields().stream())
                .collect(Collectors.toList());
    }

    public FieldDefinition buildTypeNameField(ObjectType objectType) {
        return new FieldDefinition(FIELD_TYPE_NAME_NAME)
                .setType(SCALA_STRING_NAME)
                .addDirective(new Directive(DIRECTIVE_DATA_TYPE_NAME).addArgument(DIRECTIVE_DATA_TYPE_DEFAULT_NAME, objectType.getName()));
    }

    public List<FieldDefinition> buildFunctionFieldList(ObjectType objectType) {
        return Stream.concat(
                objectType.getFields().stream()
                        .filter(fieldDefinition -> !fieldDefinition.isInvokeField())
                        .filter(fieldDefinition -> !fieldDefinition.isFetchField())
                        .filter(fieldDefinition -> !fieldDefinition.isFunctionField())
                        .filter(fieldDefinition -> !fieldDefinition.isConnectionField())
                        .filter(fieldDefinition -> !fieldDefinition.isAggregateField())
                        .filter(fieldDefinition -> !fieldDefinition.getName().equals(FIELD_TYPE_NAME_NAME))
                        .filter(fieldDefinition -> !documentManager.isMetaInterfaceField(fieldDefinition))
                        .filter(fieldDefinition -> fieldDefinition.getType().hasList())
                        .filter(fieldDefinition ->
                                fieldDefinition.getType().getTypeName().getName().equals(SCALA_ID_NAME) ||
                                        fieldDefinition.getType().getTypeName().getName().equals(SCALA_STRING_NAME) ||
                                        fieldDefinition.getType().getTypeName().getName().equals(SCALA_DATE_NAME) ||
                                        fieldDefinition.getType().getTypeName().getName().equals(SCALA_TIME_NAME) ||
                                        fieldDefinition.getType().getTypeName().getName().equals(SCALA_DATE_TIME_NAME) ||
                                        fieldDefinition.getType().getTypeName().getName().equals(SCALA_TIMESTAMP_NAME)
                        )
                        .flatMap(fieldDefinition ->
                                Stream.of(
                                        Function.COUNT.toField(
                                                fieldDefinition.getName(),
                                                SCALA_INT_NAME,
                                                fieldDefinition.getType().getTypeName().getName(),
                                                fieldDefinition.getType().hasList()
                                        ),
                                        Function.MAX.toField(
                                                fieldDefinition.getName(),
                                                fieldDefinition.getTypeNameWithoutID(),
                                                fieldDefinition.getTypeNameWithoutID(),
                                                fieldDefinition.getType().hasList()
                                        ),
                                        Function.MIN.toField(
                                                fieldDefinition.getName(),
                                                fieldDefinition.getTypeNameWithoutID(),
                                                fieldDefinition.getTypeNameWithoutID(),
                                                fieldDefinition.getType().hasList()
                                        )
                                )
                        ),
                objectType.getFields().stream()
                        .filter(fieldDefinition -> !fieldDefinition.isInvokeField())
                        .filter(fieldDefinition -> !fieldDefinition.isFetchField())
                        .filter(fieldDefinition -> !fieldDefinition.isFunctionField())
                        .filter(fieldDefinition -> !fieldDefinition.isConnectionField())
                        .filter(fieldDefinition -> !fieldDefinition.isAggregateField())
                        .filter(fieldDefinition -> !fieldDefinition.getName().equals(FIELD_TYPE_NAME_NAME))
                        .filter(fieldDefinition -> !documentManager.isMetaInterfaceField(fieldDefinition))
                        .filter(fieldDefinition -> fieldDefinition.getType().hasList())
                        .filter(fieldDefinition ->
                                fieldDefinition.getType().getTypeName().getName().equals(SCALA_INT_NAME) ||
                                        fieldDefinition.getType().getTypeName().getName().equals(SCALA_FLOAT_NAME) ||
                                        fieldDefinition.getType().getTypeName().getName().equals(SCALA_BIG_INTEGER_NAME) ||
                                        fieldDefinition.getType().getTypeName().getName().equals(SCALA_BIG_DECIMAL_NAME)
                        )
                        .flatMap(fieldDefinition ->
                                Stream.of(
                                        Function.COUNT.toField(
                                                fieldDefinition.getName(),
                                                SCALA_INT_NAME,
                                                fieldDefinition.getTypeNameWithoutID(),
                                                fieldDefinition.getType().hasList()
                                        ),
                                        Function.SUM.toField(
                                                fieldDefinition.getName(),
                                                fieldDefinition.getTypeNameWithoutID(),
                                                fieldDefinition.getTypeNameWithoutID(),
                                                fieldDefinition.getType().hasList()
                                        ),
                                        Function.AVG.toField(
                                                fieldDefinition.getName(),
                                                fieldDefinition.getTypeNameWithoutID(),
                                                fieldDefinition.getTypeNameWithoutID(),
                                                fieldDefinition.getType().hasList()
                                        ),
                                        Function.MAX.toField(
                                                fieldDefinition.getName(),
                                                fieldDefinition.getTypeNameWithoutID(),
                                                fieldDefinition.getTypeNameWithoutID(),
                                                fieldDefinition.getType().hasList()
                                        ),
                                        Function.MIN.toField(
                                                fieldDefinition.getName(),
                                                fieldDefinition.getTypeNameWithoutID(),
                                                fieldDefinition.getTypeNameWithoutID(),
                                                fieldDefinition.getType().hasList()
                                        )
                                )
                        )
        ).collect(Collectors.toList());
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

    private enum Function {
        COUNT(SUFFIX_COUNT),
        SUM(SUFFIX_SUM),
        AVG(SUFFIX_AVG),
        MAX(SUFFIX_MAX),
        MIN(SUFFIX_MIN);

        private final String name;

        Function(String name) {
            this.name = name;
        }

        public FieldDefinition toField(String fieldName, String returnTypeName, String fieldTypeName, boolean isList) {
            FieldDefinition fieldDefinition = new FieldDefinition(fieldName + name)
                    .setType(returnTypeName)
                    .addDirective(
                            new Directive()
                                    .setName(DIRECTIVE_FUNC_NAME)
                                    .addArgument(DIRECTIVE_FUNC_ARGUMENT_NAME_NAME, new EnumValue(this.name()))
                                    .addArgument(DIRECTIVE_FUNC_ARGUMENT_FIELD_NAME, fieldName)
                    );
            if (isList) {
                fieldDefinition
                        .addArgument(new InputValue(INPUT_VALUE_OPERATOR_OPR_NAME).setType(new TypeName(INPUT_OPERATOR_NAME)).setDefaultValue(INPUT_VALUE_OPERATOR_OPR_EQ))
                        .addArgument(new InputValue(INPUT_VALUE_OPERATOR_VAL_NAME).setType(new TypeName(fieldTypeName)))
                        .addArgument(new InputValue(INPUT_VALUE_OPERATOR_ARR_NAME).setType(new ListType(new TypeName(fieldTypeName))));
            }
            return fieldDefinition;
        }
    }
}
