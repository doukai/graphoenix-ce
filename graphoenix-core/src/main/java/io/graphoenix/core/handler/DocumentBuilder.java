package io.graphoenix.core.handler;

import io.graphoenix.core.config.PackageConfig;
import io.graphoenix.spi.annotation.Application;
import io.graphoenix.spi.graphql.Definition;
import io.graphoenix.spi.graphql.Document;
import io.graphoenix.spi.graphql.common.Directive;
import io.graphoenix.spi.graphql.common.ObjectValueWithVariable;
import io.graphoenix.spi.graphql.type.*;
import jakarta.inject.Inject;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static io.graphoenix.core.utils.NameUtil.*;
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
                .map(Definition::asObject)
                .forEach(this::buildMapFields);

        document.getDefinitions().stream()
                .filter(Definition::isObject)
                .map(Definition::asObject)
                .forEach(this::buildFetchFields);

        document.addDefinitions(
                document.getDefinitions().stream()
                        .filter(Definition::isObject)
                        .map(Definition::asObject)
                        .filter(packageManager::isOwnPackage)
                        .filter(objectType -> !objectType.isContainerType())
                        .filter(objectType -> !documentManager.isOperationType(objectType))
                        .flatMap(objectType ->
                                objectType.getFields().stream()
                                        .filter(FieldDefinition::hasMapWith)
                                        .map(fieldDefinition -> buildMapWithObject(objectType, fieldDefinition))
                        )
                        .filter(distinctByKey(ObjectType::getName))
                        .collect(Collectors.toList())
        );

        document.addDefinitions(
                document.getDefinitions().stream()
                        .filter(Definition::isObject)
                        .map(Definition::asObject)
                        .filter(packageManager::isOwnPackage)
                        .filter(objectType -> !objectType.isContainerType())
                        .filter(objectType -> !documentManager.isOperationType(objectType))
                        .flatMap(objectType ->
                                objectType.getFields().stream()
                                        .filter(FieldDefinition::isFetchAnchor)
                                        .filter(FieldDefinition::hasFetchWith)
                                        .map(fieldDefinition -> buildFetchWithObject(objectType, fieldDefinition))
                        )
                        .filter(distinctByKey(ObjectType::getName))
                        .collect(Collectors.toList())
        );

        document.getDefinitions().stream()
                .filter(Definition::isObject)
                .map(Definition::asObject)
                .filter(packageManager::isOwnPackage)
                .filter(objectType -> !objectType.isContainerType())
                .filter(objectType -> !documentManager.isOperationType(objectType))
                .forEach(this::buildObject);

        return document;
    }

    public ObjectType buildMapFields(ObjectType objectType) {
        objectType.getFields().stream()
                .filter(fieldDefinition -> documentManager.getFieldTypeDefinition(fieldDefinition).isObject())
                .filter(fieldDefinition -> !fieldDefinition.isMapField())
                .filter(fieldDefinition -> packageManager.isLocalPackage(documentManager.getFieldTypeDefinition(fieldDefinition)))
                .forEach(fieldDefinition ->
                        fieldDefinition
                                .addDirective(
                                        new Directive(DIRECTIVE_MAP_NAME)
                                                .addArgument(DIRECTIVE_MAP_ARGUMENT_FROM_NAME, objectType.getIDFieldOrError())
                                                .addArgument(
                                                        DIRECTIVE_MAP_ARGUMENT_WITH_NAME,
                                                        ObjectValueWithVariable.of(
                                                                INPUT_VALUE_WITH_TYPE_NAME,
                                                                getRelationTypeName(objectType.getName(), fieldDefinition.getType().getTypeName().getName()),
                                                                INPUT_VALUE_WITH_FROM_NAME,
                                                                getTypeRefFieldName(objectType.getName()),
                                                                INPUT_VALUE_WITH_TO_NAME,
                                                                getTypeRefFieldName(fieldDefinition.getType().getTypeName().getName())
                                                        )
                                                )
                                                .addArgument(DIRECTIVE_MAP_ARGUMENT_TO_NAME, documentManager.getFieldTypeDefinition(fieldDefinition).asObject().getIDFieldOrError())
                                )
                );
        return objectType;
    }

    public ObjectType buildFetchFields(ObjectType objectType) {
        objectType.getFields().stream()
                .filter(fieldDefinition -> documentManager.getFieldTypeDefinition(fieldDefinition).isObject())
                .filter(fieldDefinition -> !fieldDefinition.isMapField())
                .filter(fieldDefinition -> !packageManager.isLocalPackage(documentManager.getFieldTypeDefinition(fieldDefinition)))
                .forEach(fieldDefinition ->
                        fieldDefinition
                                .addDirective(
                                        new Directive(DIRECTIVE_FETCH_NAME)
                                                .addArgument(DIRECTIVE_FETCH_ARGUMENT_FROM_NAME, objectType.getIDFieldOrError())
                                                .addArgument(
                                                        DIRECTIVE_FETCH_ARGUMENT_WITH_NAME,
                                                        ObjectValueWithVariable.of(
                                                                INPUT_VALUE_WITH_TYPE_NAME,
                                                                getRelationTypeName(objectType.getName(), fieldDefinition.getType().getTypeName().getName()),
                                                                INPUT_VALUE_WITH_FROM_NAME,
                                                                getTypeRefFieldName(objectType.getName()),
                                                                INPUT_VALUE_WITH_TO_NAME,
                                                                getTypeRefFieldName(fieldDefinition.getType().getTypeName().getName())
                                                        )
                                                )
                                                .addArgument(DIRECTIVE_FETCH_ARGUMENT_TO_NAME, documentManager.getFieldTypeDefinition(fieldDefinition).asObject().getIDFieldOrError())
                                )
                );
        return objectType;
    }

    public ObjectType buildMapWithObject(ObjectType objectType, FieldDefinition fieldDefinition) {
        ObjectType relationObjectType = new ObjectType(fieldDefinition.getMapWithType())
                .addField(new FieldDefinition(FIELD_ID_NAME).setType(SCALA_ID_NAME))
                .addField(
                        new FieldDefinition(fieldDefinition.getMapWithFrom())
                                .setType(documentManager.getFieldMapFromFieldDefinition(objectType, fieldDefinition).getTypeNameWithoutID()))
                .addField(
                        new FieldDefinition(typeNameToFieldName(objectType.getName()))
                                .setType(new TypeName(objectType.getName()))
                                .addDirective(
                                        new Directive(DIRECTIVE_MAP_NAME)
                                                .addArgument(DIRECTIVE_MAP_ARGUMENT_FROM_NAME, fieldDefinition.getMapWithFrom())
                                                .addArgument(DIRECTIVE_MAP_ARGUMENT_TO_NAME, fieldDefinition.getMapFrom())
                                                .addArgument(DIRECTIVE_MAP_ARGUMENT_ANCHOR_NAME, true)
                                )
                );

        documentManager.getFieldMapToFieldDefinition(fieldDefinition)
                .ifPresentOrElse(mapToFieldDefinition ->
                                relationObjectType
                                        .addField(
                                                new FieldDefinition(fieldDefinition.getMapWithTo())
                                                        .setType(mapToFieldDefinition.getTypeNameWithoutID())
                                        )
                                        .addField(
                                                new FieldDefinition(typeNameToFieldName(fieldDefinition.getType().getTypeName().getName()))
                                                        .setType(fieldDefinition.getType().getTypeName())
                                                        .addDirective(
                                                                new Directive(DIRECTIVE_MAP_NAME)
                                                                        .addArgument(DIRECTIVE_MAP_ARGUMENT_FROM_NAME, fieldDefinition.getMapWithTo())
                                                                        .addArgument(DIRECTIVE_MAP_ARGUMENT_TO_NAME, fieldDefinition.getMapTo())
                                                                        .addArgument(DIRECTIVE_MAP_ARGUMENT_ANCHOR_NAME, true)
                                                        )
                                        ),
                        () -> relationObjectType
                                .addField(new FieldDefinition(fieldDefinition.getMapWithTo())
                                        .setType(fieldDefinition.getType().getTypeName()))
                );

        return relationObjectType;
    }

    public ObjectType buildFetchWithObject(ObjectType objectType, FieldDefinition fieldDefinition) {
        ObjectType relationObjectType = new ObjectType(fieldDefinition.getFetchWithType())
                .addField(new FieldDefinition(FIELD_ID_NAME).setType(SCALA_ID_NAME))
                .addField(
                        new FieldDefinition(fieldDefinition.getFetchWithFrom())
                                .setType(documentManager.getFieldFetchFromFieldDefinition(objectType, fieldDefinition).getTypeNameWithoutID()))
                .addField(
                        new FieldDefinition(typeNameToFieldName(objectType.getName()))
                                .setType(new TypeName(objectType.getName()))
                                .addDirective(
                                        fieldDefinition.isFetchAnchor() ?
                                                new Directive(DIRECTIVE_MAP_NAME)
                                                        .addArgument(DIRECTIVE_MAP_ARGUMENT_FROM_NAME, fieldDefinition.getFetchWithFrom())
                                                        .addArgument(DIRECTIVE_MAP_ARGUMENT_TO_NAME, fieldDefinition.getFetchFrom())
                                                        .addArgument(DIRECTIVE_MAP_ARGUMENT_ANCHOR_NAME, true) :
                                                new Directive(DIRECTIVE_FETCH_NAME)
                                                        .addArgument(DIRECTIVE_FETCH_ARGUMENT_FROM_NAME, fieldDefinition.getFetchWithFrom())
                                                        .addArgument(DIRECTIVE_FETCH_ARGUMENT_TO_NAME, fieldDefinition.getFetchFrom())
                                                        .addArgument(DIRECTIVE_FETCH_ARGUMENT_ANCHOR_NAME, true)
                                                        .addArgument(DIRECTIVE_FETCH_ARGUMENT_PROTOCOL_NAME, new EnumValue(fieldDefinition.getFetchProtocol()))
                                )
                );

        documentManager.getFieldFetchToFieldDefinition(fieldDefinition)
                .ifPresentOrElse(fetchToFieldDefinition ->
                                relationObjectType
                                        .addField(
                                                new FieldDefinition(fieldDefinition.getFetchWithTo())
                                                        .setType(fetchToFieldDefinition.getTypeNameWithoutID())
                                        )
                                        .addField(
                                                new FieldDefinition(typeNameToFieldName(fieldDefinition.getType().getTypeName().getName()))
                                                        .setType(fieldDefinition.getType().getTypeName())
                                                        .addDirective(
                                                                new Directive(DIRECTIVE_FETCH_NAME)
                                                                        .addArgument(DIRECTIVE_FETCH_ARGUMENT_FROM_NAME, fieldDefinition.getFetchWithTo())
                                                                        .addArgument(DIRECTIVE_FETCH_ARGUMENT_TO_NAME, fieldDefinition.getFetchTo())
                                                                        .addArgument(DIRECTIVE_FETCH_ARGUMENT_ANCHOR_NAME, true)
                                                                        .addArgument(DIRECTIVE_FETCH_ARGUMENT_PROTOCOL_NAME, new EnumValue(fieldDefinition.getFetchProtocol()))
                                                        )
                                        ),
                        () -> relationObjectType
                                .addField(new FieldDefinition(fieldDefinition.getFetchWithTo())
                                        .setType(fieldDefinition.getType().getTypeName()))
                );

        return relationObjectType;
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

        objectType.getIDField()
                .ifPresent(fieldDefinition -> {
                            if (!fieldDefinition.hesDataType()) {
                                fieldDefinition.addDirective(
                                        new Directive(DIRECTIVE_DATA_TYPE_NAME)
                                                .addArgument(DIRECTIVE_DATA_TYPE_ARGUMENT_TYPE_NAME, SCALA_INT_NAME)
                                                .addArgument(DIRECTIVE_DATA_TYPE_ARGUMENT_AUTO_INCREMENT_NAME, true)
                                );
                            }
                        }
                );

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
                .addFields(
                        objectType.getFields().stream()
                                .filter(FieldDefinition::isFetchField)
                                .filter(FieldDefinition::isFetchAnchor)
                                .filter(FieldDefinition -> !FieldDefinition.hasFetchWith())
                                .filter(fieldDefinition -> objectType.getFields().stream().noneMatch(item -> item.getName().equals(fieldDefinition.getFetchFrom())))
                                .map(fieldDefinition ->
                                        new FieldDefinition(fieldDefinition.getFetchFrom())
                                                .setType(
                                                        documentManager.getFieldTypeDefinition(fieldDefinition).asObject()
                                                                .getField(fieldDefinition.getFetchToOrError())
                                                                .getTypeNameWithoutID()
                                                )
                                )
                                .collect(Collectors.toList())
                )
                .addFields(
                        objectType.getFields().stream()
                                .filter(FieldDefinition::isFetchField)
                                .filter(FieldDefinition::hasFetchWith)
                                .map(fieldDefinition ->
                                        (FieldDefinition) new FieldDefinition(typeNameToFieldName(fieldDefinition.getFetchWithType()))
                                                .setType(
                                                        fieldDefinition.getType().hasList() ?
                                                                new ListType(fieldDefinition.getType().getTypeName()) :
                                                                fieldDefinition.getType().getTypeName()
                                                )
                                                .addDirective(
                                                        fieldDefinition.isFetchAnchor() ?
                                                                new Directive(DIRECTIVE_MAP_NAME)
                                                                        .addArgument(DIRECTIVE_MAP_ARGUMENT_FROM_NAME, fieldDefinition.getFetchFrom())
                                                                        .addArgument(DIRECTIVE_MAP_ARGUMENT_TO_NAME, fieldDefinition.getFetchWithFrom()) :
                                                                new Directive(DIRECTIVE_FETCH_NAME)
                                                                        .addArgument(DIRECTIVE_FETCH_ARGUMENT_FROM_NAME, fieldDefinition.getFetchFrom())
                                                                        .addArgument(DIRECTIVE_FETCH_ARGUMENT_TO_NAME, fieldDefinition.getFetchWithFrom())
                                                                        .addArgument(DIRECTIVE_FETCH_ARGUMENT_PROTOCOL_NAME, new EnumValue(fieldDefinition.getFetchProtocol()))
                                                )
                                )
                                .collect(Collectors.toList())
                )
                .addFields(
                        objectType.getFields().stream()
                                .filter(FieldDefinition::isMapField)
                                .filter(FieldDefinition::isMapAnchor)
                                .filter(fieldDefinition -> !fieldDefinition.hasMapWith())
                                .map(fieldDefinition ->
                                        new FieldDefinition(fieldDefinition.getMapFrom())
                                                .setType(
                                                        documentManager.getFieldTypeDefinition(fieldDefinition).asObject()
                                                                .getField(fieldDefinition.getMapToOrError())
                                                                .getTypeNameWithoutID()
                                                )
                                )
                                .filter(fieldDefinition -> objectType.getFields().stream().noneMatch(item -> item.getName().equals(fieldDefinition.getMapFrom())))
                                .collect(Collectors.toList())
                )
                .addFields(
                        objectType.getFields().stream()
                                .filter(FieldDefinition::isMapField)
                                .filter(FieldDefinition::hasMapWith)
                                .map(fieldDefinition ->
                                        (FieldDefinition) new FieldDefinition(typeNameToFieldName(fieldDefinition.getMapWithType()))
                                                .setType(
                                                        fieldDefinition.getType().hasList() ?
                                                                new ListType(fieldDefinition.getType().getTypeName()) :
                                                                fieldDefinition.getType().getTypeName()
                                                )
                                                .addDirective(
                                                        new Directive(DIRECTIVE_MAP_NAME)
                                                                .addArgument(DIRECTIVE_MAP_ARGUMENT_FROM_NAME, fieldDefinition.getMapFrom())
                                                                .addArgument(DIRECTIVE_MAP_ARGUMENT_TO_NAME, fieldDefinition.getMapWithFrom())
                                                )
                                )
                                .collect(Collectors.toList())
                )
                .addFields(
                        objectType.getFields().stream()
                                .filter(fieldDefinition -> !fieldDefinition.isInvokeField())
                                .filter(fieldDefinition -> !fieldDefinition.isFetchField())
                                .filter(fieldDefinition -> !fieldDefinition.isFunctionField())
                                .filter(fieldDefinition -> !fieldDefinition.isConnectionField())
                                .filter(fieldDefinition -> !fieldDefinition.isAggregateField())
                                .filter(fieldDefinition -> !fieldDefinition.getName().equals(FIELD_TYPE_NAME_NAME))
                                .filter(fieldDefinition -> !documentManager.isMetaInterfaceField(fieldDefinition))
                                .filter(fieldDefinition -> fieldDefinition.getType().hasList())
                                .filter(fieldDefinition -> documentManager.getFieldTypeDefinition(fieldDefinition).isObject())
                                .flatMap(fieldDefinitionContext ->
                                        Stream.of(
                                                buildListObjectAggregateField(fieldDefinitionContext),
                                                buildListObjectConnectionField(fieldDefinitionContext)
                                        )
                                )
                                .collect(Collectors.toList())
                )
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

                if (fieldDefinition.hasMapWith()) {
                    ObjectType fieldMapWithTypeDefinition = documentManager.getFieldMapWithTypeDefinition(fieldDefinition);
                    fieldMapWithTypeDefinition.getCursorField()
                            .or(fieldMapWithTypeDefinition::getIDField)
                            .ifPresent(cursorField ->
                                    fieldDefinition
                                            .addArgument(new InputValue(INPUT_VALUE_AFTER_NAME).setType(cursorField.getType().getTypeName()))
                                            .addArgument(new InputValue(INPUT_VALUE_BEFORE_NAME).setType(cursorField.getType().getTypeName()))
                            );
                }
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
                            .addArgument(new InputValue(INPUT_VALUE_FIRST_NAME).setType(new TypeName(SCALA_INT_NAME)))
                            .addArgument(new InputValue(INPUT_VALUE_LAST_NAME).setType(new TypeName(SCALA_INT_NAME)))
                            .addArgument(new InputValue(INPUT_VALUE_OFFSET_NAME).setType(new TypeName(SCALA_INT_NAME)))
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
            return new InputValue(fieldTypeDefinition.getName()).setType(new TypeName(INPUT_VALUE_SORT_NAME));
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
        List<FieldDefinition> fieldDefinitions = objectType.getFields().stream()
                .filter(fieldDefinition -> !fieldDefinition.isInvokeField())
                .filter(fieldDefinition -> !fieldDefinition.isFetchField())
                .filter(fieldDefinition -> !fieldDefinition.isFunctionField())
                .filter(fieldDefinition -> !fieldDefinition.isConnectionField())
                .filter(fieldDefinition -> !fieldDefinition.isAggregateField())
                .filter(fieldDefinition -> !fieldDefinition.getName().equals(FIELD_TYPE_NAME_NAME))
                .filter(fieldDefinition -> !documentManager.isMetaInterfaceField(fieldDefinition))
                .filter(fieldDefinition -> fieldDefinition.getType().hasList())
                .collect(Collectors.toList());

        return Stream.concat(
                fieldDefinitions.stream()
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
                fieldDefinitions.stream()
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

    public FieldDefinition buildListObjectConnectionField(FieldDefinition fieldDefinition) {
        return new FieldDefinition(fieldDefinition.getName() + SUFFIX_CONNECTION)
                .setType(new TypeName(fieldDefinition.getType().getTypeName().getName() + SUFFIX_CONNECTION))
                .addArguments(buildArgumentsFromObjectType((ObjectType) documentManager.getFieldTypeDefinition(fieldDefinition), InputType.EXPRESSION))
                .addArgument(new InputValue(INPUT_VALUE_FIRST_NAME).setType(SCALA_INT_NAME))
                .addArgument(new InputValue(INPUT_VALUE_LAST_NAME).setType(SCALA_INT_NAME))
                .addArgument(new InputValue(INPUT_VALUE_OFFSET_NAME).setType(SCALA_INT_NAME))
                .addArgument(new InputValue(INPUT_VALUE_ORDER_BY_NAME).setType(new TypeName(fieldDefinition.getType().getTypeName().getName() + InputType.ORDER_BY)))
                .addArgument(new InputValue(INPUT_VALUE_GROUP_BY_NAME).setType(new ListType(new NonNullType(new TypeName(SCALA_STRING_NAME)))))
                .addDirective(
                        new Directive(DIRECTIVE_CONNECTION_NAME)
                                .addArgument(DIRECTIVE_CONNECTION_ARGUMENT_FIELD_NAME, fieldDefinition.getName())
                                .addArgument(DIRECTIVE_CONNECTION_ARGUMENT_AGG_NAME, fieldDefinition.getName() + SUFFIX_AGGREGATE)
                );
    }

    public FieldDefinition buildListObjectAggregateField(FieldDefinition fieldDefinition) {
        return new FieldDefinition(fieldDefinition.getName() + SUFFIX_AGGREGATE)
                .setType(fieldDefinition.getType().getTypeName())
                .addArguments(buildArgumentsFromObjectType((ObjectType) documentManager.getFieldTypeDefinition(fieldDefinition), InputType.EXPRESSION))
                .addArgument(new InputValue(INPUT_VALUE_FIRST_NAME).setType(SCALA_INT_NAME))
                .addArgument(new InputValue(INPUT_VALUE_LAST_NAME).setType(SCALA_INT_NAME))
                .addArgument(new InputValue(INPUT_VALUE_OFFSET_NAME).setType(SCALA_INT_NAME))
                .addArgument(new InputValue(INPUT_VALUE_ORDER_BY_NAME).setType(new TypeName(fieldDefinition.getType().getTypeName().getName() + InputType.ORDER_BY)))
                .addArgument(new InputValue(INPUT_VALUE_GROUP_BY_NAME).setType(new ListType(new NonNullType(new TypeName(SCALA_STRING_NAME)))))
                .setDirectives(fieldDefinition.getDirectives())
                .addDirective(new Directive(DIRECTIVE_AGGREGATE_NAME));
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
