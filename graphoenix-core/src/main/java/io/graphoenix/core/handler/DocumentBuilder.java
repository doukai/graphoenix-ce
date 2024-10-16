package io.graphoenix.core.handler;

import com.google.common.collect.Streams;
import io.graphoenix.core.config.PackageConfig;
import io.graphoenix.spi.error.GraphQLErrorType;
import io.graphoenix.spi.error.GraphQLErrors;
import io.graphoenix.spi.graphql.Definition;
import io.graphoenix.spi.graphql.Document;
import io.graphoenix.spi.graphql.FieldsType;
import io.graphoenix.spi.graphql.common.ArrayValueWithVariable;
import io.graphoenix.spi.graphql.common.Directive;
import io.graphoenix.spi.graphql.common.EnumValue;
import io.graphoenix.spi.graphql.common.ObjectValueWithVariable;
import io.graphoenix.spi.graphql.type.*;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static io.graphoenix.spi.constant.Hammurabi.*;
import static io.graphoenix.spi.utils.NameUtil.*;
import static io.graphoenix.spi.utils.StreamUtil.distinctByKey;

@ApplicationScoped
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

    public Document build() {
        return build(documentManager.getDocument());
    }

    public Document build(Document document) {
        document.getObjectTypes()
                .filter(objectType -> !objectType.isContainer())
                .filter(objectType -> !documentManager.isOperationType(objectType))
                .forEach(this::buildMapFields);

        document.getObjectTypes()
                .filter(objectType -> !objectType.isContainer())
                .filter(objectType -> !documentManager.isOperationType(objectType))
                .forEach(this::buildFetchFields);

        document
                .addDefinitions(
                        document.getObjectTypes()
                                .filter(packageManager::isOwnPackage)
                                .filter(objectType -> !objectType.isContainer())
                                .filter(objectType -> !documentManager.isOperationType(objectType))
                                .flatMap(objectType ->
                                        objectType.getFields().stream()
                                                .filter(FieldDefinition::hasMapWith)
                                                .map(fieldDefinition -> buildMapWithObject(objectType, fieldDefinition))
                                )
                                .filter(distinctByKey(ObjectType::getName))
                                .collect(Collectors.toList())
                )
                .addDefinitions(
                        document.getObjectTypes()
                                .filter(packageManager::isOwnPackage)
                                .filter(objectType -> !objectType.isContainer())
                                .filter(objectType -> !documentManager.isOperationType(objectType))
                                .flatMap(objectType ->
                                        objectType.getFields().stream()
                                                .filter(FieldDefinition::hasFetchWith)
                                                .map(fieldDefinition -> buildFetchWithObject(objectType, fieldDefinition))
                                )
                                .filter(distinctByKey(ObjectType::getName))
                                .collect(Collectors.toList())
                );

        document.getObjectTypes()
                .filter(packageManager::isOwnPackage)
                .filter(objectType -> !objectType.isContainer())
                .filter(objectType -> !documentManager.isOperationType(objectType))
                .forEach(this::buildObject);

        document.getInterfaceTypes()
                .filter(packageManager::isOwnPackage)
                .filter(interfaceType -> !interfaceType.isContainer())
                .forEach(this::buildInterface);

        document.getInputObjectTypes()
                .filter(packageManager::isOwnPackage)
                .filter(inputObjectType -> !inputObjectType.isContainer())
                .forEach(this::buildInputObject);

        document.getEnums()
                .filter(packageManager::isOwnPackage)
                .filter(enumType -> !enumType.isContainer())
                .forEach(this::buildEnum);

        document
                .addDefinitions(buildInputObjects(document))
                .addDefinitions(buildContainerTypeObjects(document));

        if (document.getObjectTypes().anyMatch(objectType -> !objectType.isContainer())) {

            ObjectType queryType = document.getQueryOperationType()
                    .orElseGet(() -> new ObjectType(TYPE_QUERY_NAME))
                    .setFields(buildQueryTypeFields(document));

            ObjectType mutationType = document.getMutationOperationType()
                    .orElseGet(() -> new ObjectType(TYPE_MUTATION_NAME))
                    .setFields(buildMutationTypeFields(document));

            ObjectType subscriptionType = document.getSubscriptionOperationType()
                    .orElseGet(() -> new ObjectType(TYPE_SUBSCRIPTION_NAME))
                    .setFields(buildSubscriptionTypeFields(document));

            document.getQueryOperationType()
                    .ifPresentOrElse(
                            objectType -> objectType.merge(queryType),
                            () -> document.addDefinition(queryType)
                    );

            document.getMutationOperationType()
                    .ifPresentOrElse(
                            objectType -> objectType.merge(mutationType),
                            () -> document.addDefinition(mutationType)
                    );

            document.getSubscriptionOperationType()
                    .ifPresentOrElse(
                            objectType -> objectType.merge(subscriptionType),
                            () -> document.addDefinition(subscriptionType)
                    );

            document.addDefinition(
                    new Schema()
                            .setQuery(queryType.getName())
                            .setMutation(mutationType.getName())
                            .setSubscription(subscriptionType.getName())
            );

            document
                    .addDefinitions(buildQueryTypeFieldArguments(document))
                    .addDefinitions(buildMutationTypeFieldsArguments(document))
                    .addDefinitions(buildSubscriptionTypeFieldsArguments(document));
        }
        return document;
    }

    public Document buildInvoker() {
        return buildInvoker(documentManager.getDocument());
    }

    public Document buildInvoker(Document document) {
        return document
                .addDefinitions(buildQueryTypeInvokeFieldArguments(document))
                .addDefinitions(buildMutationTypeInvokeFieldsArguments(document))
                .addDefinitions(buildSubscriptionTypeInvokeFieldsArguments(document));
    }

    public ObjectType buildMapFields(ObjectType objectType) {
        objectType.getFields().stream()
                .filter(fieldDefinition -> documentManager.getFieldTypeDefinition(fieldDefinition).isObject())
                .filter(fieldDefinition -> !documentManager.getFieldTypeDefinition(fieldDefinition).isContainer())
                .filter(fieldDefinition -> !fieldDefinition.isMapField())
                .filter(fieldDefinition -> !fieldDefinition.isFetchField())
                .filter(fieldDefinition -> packageManager.isOwnPackage(documentManager.getFieldTypeDefinition(fieldDefinition)))
                .filter(fieldDefinition -> !objectType.getName().equals(fieldDefinition.getType().getTypeName().getName()))
                .forEach(fieldDefinition ->
                        fieldDefinition
                                .addDirective(
                                        new Directive(DIRECTIVE_MAP_NAME)
                                                .addArgument(DIRECTIVE_MAP_ARGUMENT_FROM_NAME, objectType.getIDFieldOrError().getName())
                                                .addArgument(
                                                        DIRECTIVE_MAP_ARGUMENT_WITH_NAME,
                                                        ObjectValueWithVariable.of(
                                                                INPUT_WITH_INPUT_VALUE_TYPE_NAME,
                                                                getRelationTypeName(objectType.getName(), fieldDefinition.getType().getTypeName().getName()),
                                                                INPUT_WITH_INPUT_VALUE_FROM_NAME,
                                                                getTypeRefFieldName(objectType.getName()),
                                                                INPUT_WITH_INPUT_VALUE_TO_NAME,
                                                                getTypeRefFieldName(fieldDefinition.getType().getTypeName().getName())
                                                        )
                                                )
                                                .addArgument(DIRECTIVE_MAP_ARGUMENT_TO_NAME, documentManager.getFieldTypeDefinition(fieldDefinition).asObject().getIDFieldOrError().getName())
                                )
                );
        objectType.getFields().stream()
                .filter(fieldDefinition -> documentManager.getFieldTypeDefinition(fieldDefinition).isLeaf())
                .filter(fieldDefinition -> fieldDefinition.getType().hasList())
                .filter(fieldDefinition -> !fieldDefinition.isMapField())
                .filter(fieldDefinition -> !fieldDefinition.isFetchField())
                .forEach(fieldDefinition ->
                        fieldDefinition
                                .addDirective(
                                        new Directive(DIRECTIVE_MAP_NAME)
                                                .addArgument(DIRECTIVE_MAP_ARGUMENT_FROM_NAME, objectType.getIDFieldOrError().getName())
                                                .addArgument(
                                                        DIRECTIVE_MAP_ARGUMENT_WITH_NAME,
                                                        ObjectValueWithVariable.of(
                                                                INPUT_WITH_INPUT_VALUE_TYPE_NAME,
                                                                getFieldRelationTypeName(objectType.getName(), fieldDefinition.getName()),
                                                                INPUT_WITH_INPUT_VALUE_FROM_NAME,
                                                                getTypeRefFieldName(objectType.getName()),
                                                                INPUT_WITH_INPUT_VALUE_TO_NAME,
                                                                getTypeRefFieldName(fieldDefinition.getName())
                                                        )
                                                )
                                )
                );
        return objectType;
    }

    public ObjectType buildFetchFields(ObjectType objectType) {
        objectType.getFields().stream()
                .filter(fieldDefinition -> documentManager.getFieldTypeDefinition(fieldDefinition).isObject())
                .filter(fieldDefinition -> !documentManager.getFieldTypeDefinition(fieldDefinition).isContainer())
                .filter(fieldDefinition -> !fieldDefinition.isMapField())
                .filter(fieldDefinition -> !fieldDefinition.isFetchField())
                .filter(fieldDefinition -> !packageManager.isOwnPackage(documentManager.getFieldTypeDefinition(fieldDefinition)))
                .filter(fieldDefinition -> !objectType.getName().equals(fieldDefinition.getType().getTypeName().getName()))
                .forEach(fieldDefinition ->
                        fieldDefinition
                                .addDirective(
                                        new Directive(DIRECTIVE_FETCH_NAME)
                                                .addArgument(DIRECTIVE_FETCH_ARGUMENT_FROM_NAME, objectType.getIDFieldOrError().getName())
                                                .addArgument(
                                                        DIRECTIVE_FETCH_ARGUMENT_WITH_NAME,
                                                        ObjectValueWithVariable.of(
                                                                INPUT_WITH_INPUT_VALUE_TYPE_NAME,
                                                                getRelationTypeName(objectType.getName(), fieldDefinition.getType().getTypeName().getName()),
                                                                INPUT_WITH_INPUT_VALUE_FROM_NAME,
                                                                getTypeRefFieldName(objectType.getName()),
                                                                INPUT_WITH_INPUT_VALUE_TO_NAME,
                                                                getTypeRefFieldName(fieldDefinition.getType().getTypeName().getName())
                                                        )
                                                )
                                                .addArgument(DIRECTIVE_FETCH_ARGUMENT_TO_NAME, documentManager.getFieldTypeDefinition(fieldDefinition).asObject().getIDFieldOrError().getName())
                                )
                );
        return objectType;
    }

    public void buildFetchFieldsProtocol() {
        documentManager.getDocument().getObjectTypes()
                .flatMap(objectType -> objectType.getFields().stream())
                .filter(FieldDefinition::isFetchField)
                .forEach(fieldDefinition ->
                        fieldDefinition
                                .getDirective(DIRECTIVE_FETCH_NAME)
                                .addArgument(
                                        DIRECTIVE_FETCH_ARGUMENT_PROTOCOL_NAME,
                                        packageManager.isLocalPackage(documentManager.getFieldTypeDefinition(fieldDefinition)) ?
                                                new EnumValue(ENUM_PROTOCOL_ENUM_VALUE_LOCAL) :
                                                fieldDefinition.getProtocol().orElseGet(() -> new EnumValue(packageConfig.getDefaultFetchProtocol()))
                                )
                );
    }

    public ObjectType buildMapWithObject(ObjectType objectType, FieldDefinition fieldDefinition) {
        ObjectType relationObjectType = new ObjectType(fieldDefinition.getMapWithTypeOrError())
                .addField(new FieldDefinition(FIELD_ID_NAME).setType(new TypeName(SCALA_ID_NAME)))
                .addField(
                        new FieldDefinition(fieldDefinition.getMapWithFromOrError())
                                .setType(documentManager.getFieldMapFromFieldDefinition(objectType, fieldDefinition).getTypeNameWithoutID())
                )
                .addField(
                        new FieldDefinition(getTypeRefTypeFieldName(fieldDefinition.getMapWithFromOrError()))
                                .setType(new TypeName(objectType.getName()))
                                .addDirective(
                                        new Directive(DIRECTIVE_MAP_NAME)
                                                .addArgument(DIRECTIVE_MAP_ARGUMENT_FROM_NAME, fieldDefinition.getMapWithFromOrError())
                                                .addArgument(DIRECTIVE_MAP_ARGUMENT_TO_NAME, fieldDefinition.getMapFromOrError())
                                                .addArgument(DIRECTIVE_MAP_ARGUMENT_ANCHOR_NAME, true)
                                )
                )
                .addDirective(
                        new Directive(DIRECTIVE_PACKAGE_NAME)
                                .addArgument(DIRECTIVE_PACKAGE_ARGUMENT_NAME_NAME, packageConfig.getPackageName())
                )
                .addDirective(
                        new Directive(DIRECTIVE_CLASS_NAME)
                                .addArgument(DIRECTIVE_CLASS_ARGUMENT_NAME_NAME, packageConfig.getObjectTypePackageName() + "." + fieldDefinition.getMapWithTypeOrError())
                )
                .addDirective(
                        new Directive(DIRECTIVE_GRPC_NAME)
                                .addArgument(DIRECTIVE_GRPC_ARGUMENT_NAME_NAME, packageConfig.getGrpcObjectTypePackageName() + "." + getGrpcName(fieldDefinition.getMapWithTypeOrError()))
                );

        documentManager.getFieldMapToFieldDefinition(fieldDefinition)
                .ifPresentOrElse(mapToFieldDefinition ->
                                relationObjectType
                                        .addField(
                                                new FieldDefinition(fieldDefinition.getMapWithToOrError())
                                                        .setType(new TypeName(mapToFieldDefinition.getTypeNameWithoutID()))
                                        )
                                        .addField(
                                                new FieldDefinition(getTypeRefTypeFieldName(fieldDefinition.getMapWithToOrError()))
                                                        .setType(fieldDefinition.getType().getTypeName())
                                                        .addDirective(
                                                                new Directive(DIRECTIVE_MAP_NAME)
                                                                        .addArgument(DIRECTIVE_MAP_ARGUMENT_FROM_NAME, fieldDefinition.getMapWithToOrError())
                                                                        .addArgument(DIRECTIVE_MAP_ARGUMENT_TO_NAME, fieldDefinition.getMapToOrError())
                                                                        .addArgument(DIRECTIVE_MAP_ARGUMENT_ANCHOR_NAME, true)
                                                        )
                                        ),
                        () -> relationObjectType
                                .addField(new FieldDefinition(fieldDefinition.getMapWithToOrError())
                                        .setType(fieldDefinition.getType().getTypeName()))
                );

        return relationObjectType;
    }

    public ObjectType buildFetchWithObject(ObjectType objectType, FieldDefinition fieldDefinition) {
        ObjectType relationObjectType = new ObjectType(fieldDefinition.getFetchWithTypeOrError())
                .addField(new FieldDefinition(FIELD_ID_NAME).setType(new TypeName(SCALA_ID_NAME)))
                .addField(
                        new FieldDefinition(fieldDefinition.getFetchWithFromOrError())
                                .setType(documentManager.getFieldFetchFromFieldDefinition(objectType, fieldDefinition).getTypeNameWithoutID())
                )
                .addField(
                        new FieldDefinition(getTypeRefTypeFieldName(fieldDefinition.getFetchWithFromOrError()))
                                .setType(new TypeName(objectType.getName()))
                                .addDirective(
                                        new Directive(DIRECTIVE_MAP_NAME)
                                                .addArgument(DIRECTIVE_MAP_ARGUMENT_FROM_NAME, fieldDefinition.getFetchWithFromOrError())
                                                .addArgument(DIRECTIVE_MAP_ARGUMENT_TO_NAME, fieldDefinition.getFetchFromOrError())
                                                .addArgument(DIRECTIVE_MAP_ARGUMENT_ANCHOR_NAME, true)
                                )
                )
                .addDirective(
                        new Directive(DIRECTIVE_PACKAGE_NAME)
                                .addArgument(DIRECTIVE_PACKAGE_ARGUMENT_NAME_NAME, packageConfig.getPackageName())
                )
                .addDirective(
                        new Directive(DIRECTIVE_CLASS_NAME)
                                .addArgument(DIRECTIVE_CLASS_ARGUMENT_NAME_NAME, packageConfig.getObjectTypePackageName() + "." + fieldDefinition.getFetchWithTypeOrError())
                )
                .addDirective(
                        new Directive(DIRECTIVE_GRPC_NAME)
                                .addArgument(DIRECTIVE_GRPC_ARGUMENT_NAME_NAME, packageConfig.getGrpcObjectTypePackageName() + "." + getGrpcName(fieldDefinition.getFetchWithTypeOrError()))
                );

        documentManager.getFieldFetchToFieldDefinition(fieldDefinition)
                .ifPresentOrElse(fetchToFieldDefinition ->
                                relationObjectType
                                        .addField(
                                                new FieldDefinition(fieldDefinition.getFetchWithToOrError())
                                                        .setType(new TypeName(fetchToFieldDefinition.getTypeNameWithoutID()))
                                        )
                                        .addField(
                                                new FieldDefinition(getTypeRefTypeFieldName(fieldDefinition.getFetchWithToOrError()))
                                                        .setType(fieldDefinition.getType().getTypeName())
                                                        .addDirective(
                                                                new Directive(DIRECTIVE_FETCH_NAME)
                                                                        .addArgument(DIRECTIVE_FETCH_ARGUMENT_FROM_NAME, fieldDefinition.getFetchWithToOrError())
                                                                        .addArgument(DIRECTIVE_FETCH_ARGUMENT_TO_NAME, fieldDefinition.getFetchToOrError())
                                                                        .addArgument(DIRECTIVE_FETCH_ARGUMENT_ANCHOR_NAME, true)
                                                        )
                                        ),
                        () -> relationObjectType
                                .addField(new FieldDefinition(fieldDefinition.getFetchWithToOrError())
                                        .setType(fieldDefinition.getType().getTypeName()))
                );

        return relationObjectType;
    }

    public ObjectType buildObject(ObjectType objectType) {
        if (objectType.getPackageName().isEmpty()) {
            objectType.addDirective(
                    new Directive(DIRECTIVE_PACKAGE_NAME)
                            .addArgument(DIRECTIVE_PACKAGE_ARGUMENT_NAME_NAME, packageConfig.getPackageName())
            );
        }

        if (objectType.getClassName().isEmpty()) {
            objectType.addDirective(
                    new Directive(DIRECTIVE_CLASS_NAME)
                            .addArgument(DIRECTIVE_CLASS_ARGUMENT_NAME_NAME, packageConfig.getObjectTypePackageName() + "." + objectType.getName())
            );
        }

        if (objectType.getGrpcName().isEmpty()) {
            objectType.addDirective(
                    new Directive(DIRECTIVE_GRPC_NAME)
                            .addArgument(DIRECTIVE_GRPC_ARGUMENT_NAME_NAME, packageConfig.getGrpcObjectTypePackageName() + "." + getGrpcName(objectType.getName()))
            );
        }

        objectType.getIDField()
                .ifPresent(fieldDefinition -> {
                            if (!fieldDefinition.hasOptions()) {
                                fieldDefinition.addDirective(
                                        new Directive(DIRECTIVE_OPTIONS_NAME)
                                                .addArgument(DIRECTIVE_OPTIONS_ARGUMENT_TYPE_NAME, SCALA_INT_NAME)
                                                .addArgument(DIRECTIVE_OPTIONS_ARGUMENT_AUTO_INCREMENT_NAME, true)
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
                                .filter(fieldDefinition -> documentManager.isFetchAnchor(objectType, fieldDefinition))
                                .filter(FieldDefinition -> !FieldDefinition.hasFetchWith())
                                .filter(fieldDefinition -> objectType.getFields().stream().noneMatch(item -> item.getName().equals(fieldDefinition.getFetchFromOrError())))
                                .map(fieldDefinition ->
                                        new FieldDefinition(fieldDefinition.getFetchFromOrError())
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
                                        buildField(
                                                documentManager.getDocument().getObjectTypeOrError(fieldDefinition.getFetchWithTypeOrError()),
                                                new FieldDefinition(typeNameToFieldName(fieldDefinition.getFetchWithTypeOrError()))
                                                        .setType(new ListType(new TypeName(fieldDefinition.getFetchWithTypeOrError())))
                                                        .addDirective(
                                                                new Directive(DIRECTIVE_FETCH_NAME)
                                                                        .addArgument(DIRECTIVE_FETCH_ARGUMENT_FROM_NAME, fieldDefinition.getFetchFromOrError())
                                                                        .addArgument(DIRECTIVE_FETCH_ARGUMENT_TO_NAME, fieldDefinition.getFetchWithFromOrError())
                                                        )
                                        )
                                )
                                .collect(Collectors.toList())
                )
                .addFields(
                        objectType.getFields().stream()
                                .filter(FieldDefinition::isMapField)
                                .filter(fieldDefinition -> documentManager.isMapAnchor(objectType, fieldDefinition))
                                .filter(fieldDefinition -> !fieldDefinition.hasMapWith())
                                .filter(fieldDefinition -> objectType.getFields().stream().noneMatch(item -> item.getName().equals(fieldDefinition.getMapFromOrError())))
                                .map(fieldDefinition ->
                                        new FieldDefinition(fieldDefinition.getMapFromOrError())
                                                .setType(
                                                        documentManager.getFieldTypeDefinition(fieldDefinition).asObject()
                                                                .getField(fieldDefinition.getMapToOrError())
                                                                .getTypeNameWithoutID()
                                                )
                                )
                                .collect(Collectors.toList())
                )
                .addFields(
                        objectType.getFields().stream()
                                .filter(FieldDefinition::isMapField)
                                .filter(FieldDefinition::hasMapWith)
                                .map(fieldDefinition ->
                                        buildField(
                                                documentManager.getDocument().getObjectTypeOrError(fieldDefinition.getMapWithTypeOrError()),
                                                new FieldDefinition(typeNameToFieldName(fieldDefinition.getMapWithTypeOrError()))
                                                        .setType(new ListType(new TypeName(fieldDefinition.getMapWithTypeOrError())))
                                                        .addDirective(
                                                                new Directive(DIRECTIVE_MAP_NAME)
                                                                        .addArgument(DIRECTIVE_MAP_ARGUMENT_FROM_NAME, fieldDefinition.getMapFromOrError())
                                                                        .addArgument(DIRECTIVE_MAP_ARGUMENT_TO_NAME, fieldDefinition.getMapWithFromOrError())
                                                        )
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
                                .filter(fieldDefinition -> !fieldDefinition.getName().equals(FIELD_TYPENAME_NAME))
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

    public InterfaceType buildInterface(InterfaceType interfaceType) {
        if (interfaceType.getPackageName().isEmpty()) {
            interfaceType.addDirective(
                    new Directive(DIRECTIVE_PACKAGE_NAME)
                            .addArgument(DIRECTIVE_PACKAGE_ARGUMENT_NAME_NAME, packageConfig.getPackageName())
            );
        }

        if (interfaceType.getClassName().isEmpty()) {
            interfaceType.addDirective(
                    new Directive(DIRECTIVE_CLASS_NAME)
                            .addArgument(DIRECTIVE_CLASS_ARGUMENT_NAME_NAME, packageConfig.getInterfaceTypePackageName() + "." + interfaceType.getName())
            );
        }

        if (interfaceType.getGrpcName().isEmpty()) {
            interfaceType.addDirective(
                    new Directive(DIRECTIVE_GRPC_NAME)
                            .addArgument(DIRECTIVE_GRPC_ARGUMENT_NAME_NAME, packageConfig.getGrpcInterfaceTypePackageName() + "." + getGrpcName(interfaceType.getName()))
            );
        }
        return interfaceType;
    }

    public InputObjectType buildInputObject(InputObjectType inputObjectType) {
        if (inputObjectType.getPackageName().isEmpty()) {
            inputObjectType.addDirective(
                    new Directive(DIRECTIVE_PACKAGE_NAME)
                            .addArgument(DIRECTIVE_PACKAGE_ARGUMENT_NAME_NAME, packageConfig.getPackageName())
            );
        }

        if (inputObjectType.getClassName().isEmpty()) {
            inputObjectType.addDirective(
                    new Directive(DIRECTIVE_CLASS_NAME)
                            .addArgument(DIRECTIVE_CLASS_ARGUMENT_NAME_NAME, packageConfig.getInputObjectTypePackageName() + "." + inputObjectType.getName())
            );
        }

        if (inputObjectType.getAnnotationName().isEmpty()) {
            inputObjectType.addDirective(
                    new Directive(DIRECTIVE_ANNOTATION_NAME)
                            .addArgument(DIRECTIVE_ANNOTATION_ARGUMENT_NAME_NAME, packageConfig.getAnnotationPackageName() + "." + inputObjectType.getName())
            );
        }

        if (inputObjectType.getGrpcName().isEmpty()) {
            inputObjectType.addDirective(
                    new Directive(DIRECTIVE_GRPC_NAME)
                            .addArgument(DIRECTIVE_GRPC_ARGUMENT_NAME_NAME, packageConfig.getGrpcInputObjectTypePackageName() + "." + getGrpcName(inputObjectType.getName()))
            );
        }
        return inputObjectType;
    }

    public EnumType buildEnum(EnumType enumType) {
        if (enumType.getPackageName().isEmpty()) {
            enumType.addDirective(
                    new Directive(DIRECTIVE_PACKAGE_NAME)
                            .addArgument(DIRECTIVE_PACKAGE_ARGUMENT_NAME_NAME, packageConfig.getPackageName())
            );
        }

        if (enumType.getClassName().isEmpty()) {
            enumType.addDirective(
                    new Directive(DIRECTIVE_CLASS_NAME)
                            .addArgument(DIRECTIVE_CLASS_ARGUMENT_NAME_NAME, packageConfig.getEnumTypePackageName() + "." + enumType.getName())
            );
        }

        if (enumType.getGrpcName().isEmpty()) {
            enumType.addDirective(
                    new Directive(DIRECTIVE_GRPC_NAME)
                            .addArgument(DIRECTIVE_GRPC_ARGUMENT_NAME_NAME, packageConfig.getGrpcEnumTypePackageName() + "." + getGrpcName(enumType.getName()))
            );
        }
        return enumType;
    }

    public FieldDefinition buildField(ObjectType objectType, FieldDefinition fieldDefinition) {
        Definition fieldTypeDefinition = documentManager.getFieldTypeDefinition(fieldDefinition);
        if (fieldTypeDefinition.isLeaf()) {
            if (fieldDefinition.getType().hasList()) {
                fieldDefinition
                        .addArgument(new InputValue(INPUT_OPERATOR_INPUT_VALUE_OPR_NAME).setType(new TypeName(INPUT_OPERATOR_NAME)).setDefaultValue(new EnumValue(INPUT_OPERATOR_INPUT_VALUE_EQ)))
                        .addArgument(new InputValue(INPUT_OPERATOR_INPUT_VALUE_VAL_NAME).setType(fieldDefinition.getType().getTypeName()))
                        .addArgument(new InputValue(INPUT_OPERATOR_INPUT_VALUE_ARR_NAME).setType(new ListType(fieldDefinition.getType().getTypeName())))
                        .addArgument(new InputValue(INPUT_VALUE_FIRST_NAME).setType(new TypeName(SCALA_INT_NAME)))
                        .addArgument(new InputValue(INPUT_VALUE_LAST_NAME).setType(new TypeName(SCALA_INT_NAME)))
                        .addArgument(new InputValue(INPUT_VALUE_OFFSET_NAME).setType(new TypeName(SCALA_INT_NAME)))
                        .addArgument(new InputValue(INPUT_VALUE_SORT_NAME).setType(new TypeName(INPUT_SORT_NAME)))
                        .addArgument(new InputValue(INPUT_VALUE_AFTER_NAME).setType(fieldDefinition.getType().getTypeName()))
                        .addArgument(new InputValue(INPUT_VALUE_BEFORE_NAME).setType(fieldDefinition.getType().getTypeName()));
            }
        } else {
            if (documentManager.isMutationOperationType(objectType)) {
                fieldDefinition.addArguments(buildInputValuesFromObjectType(fieldTypeDefinition.asObject(), InputType.INPUT));
            } else {
                fieldDefinition
                        .addArguments(buildInputValuesFromObjectType(fieldTypeDefinition.asObject(), InputType.EXPRESSION))
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

    public InputValue fieldToInputValue(FieldsType fieldsType, FieldDefinition fieldDefinition, InputType inputType) {
        Definition fieldTypeDefinition = documentManager.getFieldTypeDefinition(fieldDefinition);
        if (inputType.equals(InputType.INPUT) || inputType.equals(InputType.MUTATION_ARGUMENTS)) {
            if (fieldDefinition.getName().equals(FIELD_DEPRECATED_NAME)) {
                return new InputValue(FIELD_DEPRECATED_NAME).setType(new TypeName(SCALA_BOOLEAN_NAME)).setDefaultValue(false);
            } else if (fieldDefinition.getName().equals(FIELD_TYPENAME_NAME)) {
                return new InputValue(FIELD_TYPENAME_NAME).setType(new TypeName(SCALA_STRING_NAME)).setDefaultValue(fieldsType.getName());
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
            return new InputValue(fieldDefinition.getName()).setType(argumentType);
        } else if (inputType.equals(InputType.EXPRESSION) || inputType.equals(InputType.QUERY_ARGUMENTS) || inputType.equals(InputType.SUBSCRIPTION_ARGUMENTS)) {
            if (fieldDefinition.getName().equals(FIELD_DEPRECATED_NAME)) {
                return new InputValue(INPUT_VALUE_INCLUDE_DEPRECATED_NAME).setType(new TypeName(SCALA_BOOLEAN_NAME)).setDefaultValue(false);
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
                case SCALA_UPLOAD_NAME:
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
            if (fieldTypeDefinition.isLeaf()) {
                return new InputValue(fieldDefinition.getName()).setType(new TypeName(INPUT_SORT_NAME));
            } else {
                return new InputValue(fieldDefinition.getName()).setType(new TypeName(fieldTypeDefinition.getName() + InputType.ORDER_BY));
            }
        }
        throw new GraphQLErrors(GraphQLErrorType.UNSUPPORTED_FIELD_TYPE.bind(inputType.toString()));
    }

    public Set<InputValue> buildInputValuesFromObjectType(FieldsType fieldsType, InputType inputType) {
        return Stream
                .concat(
                        fieldsType.getFields().stream(),
                        documentManager.getDocument().getMetaInterface().stream()
                                .flatMap(interfaceType -> interfaceType.getFields().stream())
                )
                .filter(distinctByKey(FieldDefinition::getName))
                .filter(fieldDefinition -> !fieldDefinition.isInvokeField())
                .filter(fieldDefinition -> !fieldDefinition.isConnectionField())
                .filter(fieldDefinition -> inputType.equals(InputType.ORDER_BY) || !fieldDefinition.isFunctionField())
                .filter(fieldDefinition -> inputType.equals(InputType.ORDER_BY) || !fieldDefinition.isAggregateField())
                .filter(fieldDefinition -> !documentManager.getFieldTypeDefinition(fieldDefinition).isContainer())
                .map(fieldDefinition -> fieldToInputValue(fieldsType, fieldDefinition, inputType))
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }

    public List<FieldDefinition> getMetaInterfaceFields() {
        return documentManager.getDocument().getMetaInterface().stream()
                .flatMap(interfaceType -> interfaceType.getFields().stream())
                .collect(Collectors.toList());
    }

    public FieldDefinition buildTypeNameField(ObjectType objectType) {
        return new FieldDefinition(FIELD_TYPENAME_NAME)
                .setType(new TypeName(SCALA_STRING_NAME))
                .addDirective(new Directive(DIRECTIVE_OPTIONS_NAME).addArgument(DIRECTIVE_OPTIONS_ARGUMENT_DEFAULT_NAME, objectType.getName()));
    }

    public List<InputObjectType> buildInputObjects(Document document) {
        return Streams.concat(
                document.getDefinitions().stream()
                        .filter(Definition::isObject)
                        .map(Definition::asObject)
                        .filter(packageManager::isOwnPackage)
                        .filter(objectType -> !objectType.isContainer())
                        .filter(objectType -> !documentManager.isOperationType(objectType))
                        .flatMap(objectType ->
                                Stream.of(
                                        fieldsToExpression(objectType),
                                        fieldsToInput(objectType),
                                        fieldsToOrderBy(objectType)
                                )
                        ),
                document.getDefinitions().stream()
                        .filter(Definition::isInterface)
                        .map(Definition::asInterface)
                        .filter(packageManager::isOwnPackage)
                        .filter(interfaceType -> !interfaceType.getName().equals(INTERFACE_META_NAME))
                        .flatMap(interfaceType ->
                                Stream.of(
                                        fieldsToExpression(interfaceType),
                                        fieldsToInput(interfaceType)
                                )
                        ),
                document.getDefinitions().stream()
                        .filter(Definition::isEnum)
                        .map(Definition::asEnum)
                        .filter(packageManager::isOwnPackage)
                        .map(this::enumToExpression)
        ).collect(Collectors.toList());
    }

    public List<ObjectType> buildContainerTypeObjects(Document document) {
        return document.getDefinitions().stream()
                .filter(Definition::isObject)
                .map(Definition::asObject)
                .filter(packageManager::isOwnPackage)
                .filter(objectType -> !objectType.isContainer())
                .filter(objectType -> !documentManager.isOperationType(objectType))
                .flatMap(objectType ->
                        Stream.of(
                                objectToConnection(objectType),
                                objectToEdge(objectType)
                        )
                )
                .collect(Collectors.toList());
    }

    public InputObjectType fieldsToExpression(FieldsType fieldsType) {
        InputObjectType inputObjectType = new InputObjectType(fieldsType.getName() + InputType.EXPRESSION)
                .setInputValues(buildInputValuesFromObjectType(fieldsType, InputType.EXPRESSION))
                .addInputValue(new InputValue(INPUT_VALUE_NOT_NAME).setType(new TypeName(SCALA_BOOLEAN_NAME)).setDefaultValue(false))
                .addInputValue(new InputValue(INPUT_VALUE_COND_NAME).setType(new TypeName(INPUT_CONDITIONAL_NAME)).setDefaultValue(new EnumValue(INPUT_CONDITIONAL_INPUT_VALUE_AND)))
                .addDirective(
                        new Directive(DIRECTIVE_PACKAGE_NAME)
                                .addArgument(DIRECTIVE_PACKAGE_ARGUMENT_NAME_NAME, packageConfig.getPackageName())
                )
                .addDirective(
                        new Directive(DIRECTIVE_CLASS_NAME)
                                .addArgument(DIRECTIVE_CLASS_ARGUMENT_NAME_NAME, packageConfig.getInputObjectTypePackageName() + "." + fieldsType.getName() + InputType.EXPRESSION)
                )
                .addDirective(
                        new Directive(DIRECTIVE_ANNOTATION_NAME)
                                .addArgument(DIRECTIVE_ANNOTATION_ARGUMENT_NAME_NAME, packageConfig.getAnnotationPackageName() + "." + fieldsType.getName() + InputType.EXPRESSION)
                )
                .addDirective(
                        new Directive(DIRECTIVE_GRPC_NAME)
                                .addArgument(DIRECTIVE_GRPC_ARGUMENT_NAME_NAME, packageConfig.getGrpcInputObjectTypePackageName() + "." + getGrpcName(fieldsType.getName() + InputType.EXPRESSION))
                )
                .addDirective(
                        new Directive(DIRECTIVE_IMPLEMENTS_NAME)
                                .addArgument(DIRECTIVE_IMPLEMENTS_ARGUMENT_INTERFACES_NAME,
                                        new ArrayValueWithVariable(
                                                Stream
                                                        .concat(
                                                                Stream.ofNullable(fieldsType.getInterfaces()).flatMap(Collection::stream),
                                                                Stream.of(INTERFACE_META_NAME)
                                                        )
                                                        .map(interfaceName -> interfaceName + SUFFIX_EXPRESSION)
                                                        .distinct()
                                                        .collect(Collectors.toList())
                                        )
                                )
                );
        if (fieldsType instanceof InterfaceType) {
            inputObjectType.addDirective(new Directive(DIRECTIVE_INTERFACE_NAME));
        } else {
            inputObjectType.addInputValue(new InputValue(INPUT_VALUE_EXS_NAME).setType(new ListType(new TypeName(fieldsType.getName() + InputType.EXPRESSION))));
        }
        return inputObjectType;
    }

    public InputObjectType fieldsToInput(FieldsType fieldsType) {
        InputObjectType inputObjectType = new InputObjectType(fieldsType.getName() + InputType.INPUT)
                .setInputValues(buildInputValuesFromObjectType(fieldsType, InputType.INPUT))
                .addDirective(
                        new Directive(DIRECTIVE_PACKAGE_NAME)
                                .addArgument(DIRECTIVE_PACKAGE_ARGUMENT_NAME_NAME, packageConfig.getPackageName())
                )
                .addDirective(
                        new Directive(DIRECTIVE_CLASS_NAME)
                                .addArgument(DIRECTIVE_CLASS_ARGUMENT_NAME_NAME, packageConfig.getInputObjectTypePackageName() + "." + fieldsType.getName() + InputType.INPUT)
                )
                .addDirective(
                        new Directive(DIRECTIVE_ANNOTATION_NAME)
                                .addArgument(DIRECTIVE_ANNOTATION_ARGUMENT_NAME_NAME, packageConfig.getAnnotationPackageName() + "." + fieldsType.getName() + InputType.INPUT)
                )
                .addDirective(
                        new Directive(DIRECTIVE_GRPC_NAME)
                                .addArgument(DIRECTIVE_GRPC_ARGUMENT_NAME_NAME, packageConfig.getGrpcInputObjectTypePackageName() + "." + getGrpcName(fieldsType.getName() + InputType.INPUT))
                )
                .addDirective(
                        new Directive(DIRECTIVE_IMPLEMENTS_NAME)
                                .addArgument(DIRECTIVE_IMPLEMENTS_ARGUMENT_INTERFACES_NAME,
                                        new ArrayValueWithVariable(
                                                Stream
                                                        .concat(
                                                                Stream.ofNullable(fieldsType.getInterfaces()).flatMap(Collection::stream),
                                                                Stream.of(INTERFACE_META_NAME)
                                                        )
                                                        .map(interfaceName -> interfaceName + SUFFIX_INPUT)
                                                        .distinct()
                                                        .collect(Collectors.toList())
                                        )
                                )
                );
        if (fieldsType instanceof InterfaceType) {
            inputObjectType.addDirective(new Directive(DIRECTIVE_INTERFACE_NAME));
        } else {
            inputObjectType.addInputValue(new InputValue(INPUT_VALUE_WHERE_NAME).setType(new TypeName(fieldsType.getName() + InputType.EXPRESSION)));
        }
        return inputObjectType;
    }

    public InputObjectType fieldsToOrderBy(FieldsType fieldsType) {
        InputObjectType inputObjectType = new InputObjectType(fieldsType.getName() + InputType.ORDER_BY)
                .setInputValues(buildInputValuesFromObjectType(fieldsType, InputType.ORDER_BY))
                .addDirective(
                        new Directive(DIRECTIVE_PACKAGE_NAME)
                                .addArgument(DIRECTIVE_PACKAGE_ARGUMENT_NAME_NAME, packageConfig.getPackageName())
                )
                .addDirective(
                        new Directive(DIRECTIVE_CLASS_NAME)
                                .addArgument(DIRECTIVE_CLASS_ARGUMENT_NAME_NAME, packageConfig.getInputObjectTypePackageName() + "." + fieldsType.getName() + InputType.ORDER_BY)
                )
                .addDirective(
                        new Directive(DIRECTIVE_ANNOTATION_NAME)
                                .addArgument(DIRECTIVE_ANNOTATION_ARGUMENT_NAME_NAME, packageConfig.getAnnotationPackageName() + "." + fieldsType.getName() + InputType.ORDER_BY)
                )
                .addDirective(
                        new Directive(DIRECTIVE_GRPC_NAME)
                                .addArgument(DIRECTIVE_GRPC_ARGUMENT_NAME_NAME, packageConfig.getGrpcInputObjectTypePackageName() + "." + getGrpcName(fieldsType.getName() + InputType.ORDER_BY))
                );
        if (fieldsType instanceof InterfaceType) {
            inputObjectType.addDirective(new Directive(DIRECTIVE_INTERFACE_NAME));
        }
        return inputObjectType;
    }

    public InputObjectType enumToExpression(EnumType enumType) {
        return new InputObjectType(enumType.getName() + InputType.EXPRESSION)
                .addInputValue(new InputValue(INPUT_OPERATOR_INPUT_VALUE_OPR_NAME).setType(new TypeName(INPUT_OPERATOR_NAME)).setDefaultValue(new EnumValue(INPUT_OPERATOR_INPUT_VALUE_EQ)))
                .addInputValue(new InputValue(INPUT_OPERATOR_INPUT_VALUE_VAL_NAME).setType(new TypeName(enumType.getName())))
                .addInputValue(new InputValue(INPUT_OPERATOR_INPUT_VALUE_ARR_NAME).setType(new ListType(new TypeName(enumType.getName()))))
                .addDirective(
                        new Directive(DIRECTIVE_PACKAGE_NAME)
                                .addArgument(DIRECTIVE_PACKAGE_ARGUMENT_NAME_NAME, packageConfig.getPackageName())
                )
                .addDirective(
                        new Directive(DIRECTIVE_CLASS_NAME)
                                .addArgument(DIRECTIVE_CLASS_ARGUMENT_NAME_NAME, packageConfig.getInputObjectTypePackageName() + "." + enumType.getName() + InputType.EXPRESSION)
                )
                .addDirective(
                        new Directive(DIRECTIVE_ANNOTATION_NAME)
                                .addArgument(DIRECTIVE_ANNOTATION_ARGUMENT_NAME_NAME, packageConfig.getAnnotationPackageName() + "." + enumType.getName() + InputType.EXPRESSION)
                )
                .addDirective(
                        new Directive(DIRECTIVE_GRPC_NAME)
                                .addArgument(DIRECTIVE_GRPC_ARGUMENT_NAME_NAME, packageConfig.getGrpcInputObjectTypePackageName() + "." + getGrpcName(enumType.getName() + InputType.EXPRESSION))
                );
    }

    public ObjectType objectToConnection(ObjectType objectType) {
        return new ObjectType(objectType.getName() + InputType.CONNECTION)
                .addField(new FieldDefinition(FIELD_TOTAL_COUNT_NAME).setType(new TypeName(SCALA_INT_NAME)))
                .addField(new FieldDefinition(FIELD_PAGE_INFO_NAME).setType(new TypeName(TYPE_PAGE_INFO_NAME)))
                .addField(new FieldDefinition(FIELD_EDGES_NAME).setType(new ListType(new TypeName(objectType.getName() + InputType.EDGE))))
                .addDirective(
                        new Directive(DIRECTIVE_PACKAGE_NAME)
                                .addArgument(DIRECTIVE_PACKAGE_ARGUMENT_NAME_NAME, packageConfig.getPackageName())
                )
                .addDirective(
                        new Directive(DIRECTIVE_CLASS_NAME)
                                .addArgument(DIRECTIVE_CLASS_ARGUMENT_NAME_NAME, packageConfig.getObjectTypePackageName() + "." + objectType.getName() + InputType.CONNECTION)
                )
                .addDirective(
                        new Directive(DIRECTIVE_GRPC_NAME)
                                .addArgument(DIRECTIVE_GRPC_ARGUMENT_NAME_NAME, packageConfig.getGrpcObjectTypePackageName() + "." + getGrpcName(objectType.getName() + InputType.CONNECTION))
                )
                .addDirective(new Directive(DIRECTIVE_CONTAINER_NAME));
    }

    public ObjectType objectToEdge(ObjectType objectType) {
        FieldDefinition cursorFieldDefinition = objectType.getCursorField()
                .orElseGet(objectType::getIDFieldOrError);

        return new ObjectType(objectType.getName() + InputType.EDGE)
                .addField(new FieldDefinition(FIELD_NODE_NAME).setType(new TypeName(objectType.getName())))
                .addField(new FieldDefinition(FIELD_CURSOR_NAME).setType(cursorFieldDefinition.getCursorTypeNameWithoutID()))
                .addDirective(
                        new Directive(DIRECTIVE_PACKAGE_NAME)
                                .addArgument(DIRECTIVE_PACKAGE_ARGUMENT_NAME_NAME, packageConfig.getPackageName())
                )
                .addDirective(
                        new Directive(DIRECTIVE_CLASS_NAME)
                                .addArgument(DIRECTIVE_CLASS_ARGUMENT_NAME_NAME, packageConfig.getObjectTypePackageName() + "." + objectType.getName() + InputType.EDGE)
                )
                .addDirective(
                        new Directive(DIRECTIVE_GRPC_NAME)
                                .addArgument(DIRECTIVE_GRPC_ARGUMENT_NAME_NAME, packageConfig.getGrpcObjectTypePackageName() + "." + getGrpcName(objectType.getName() + InputType.EDGE))
                )
                .addDirective(new Directive(DIRECTIVE_CONTAINER_NAME));
    }

    public List<FieldDefinition> buildQueryTypeFields(Document document) {
        return document.getDefinitions().stream()
                .filter(Definition::isObject)
                .map(Definition::asObject)
                .filter(packageManager::isOwnPackage)
                .filter(objectType -> !objectType.isContainer())
                .filter(objectType -> !documentManager.isOperationType(objectType))
                .flatMap(objectType ->
                        Stream.of(
                                buildSchemaTypeField(objectType, InputType.QUERY_ARGUMENTS),
                                buildSchemaTypeFieldList(objectType, InputType.QUERY_ARGUMENTS),
                                buildSchemaTypeFieldConnection(objectType, InputType.QUERY_ARGUMENTS)
                        )
                )
                .collect(Collectors.toList());
    }

    public List<FieldDefinition> buildMutationTypeFields(Document document) {
        return document.getDefinitions().stream()
                .filter(Definition::isObject)
                .map(Definition::asObject)
                .filter(packageManager::isOwnPackage)
                .filter(objectType -> !objectType.isContainer())
                .filter(objectType -> !documentManager.isOperationType(objectType))
                .flatMap(objectType ->
                        Stream.of(
                                buildSchemaTypeField(objectType, InputType.MUTATION_ARGUMENTS),
                                buildSchemaTypeFieldList(objectType, InputType.MUTATION_ARGUMENTS)
                        )
                )
                .collect(Collectors.toList());
    }

    public List<FieldDefinition> buildSubscriptionTypeFields(Document document) {
        return document.getDefinitions().stream()
                .filter(Definition::isObject)
                .map(Definition::asObject)
                .filter(packageManager::isOwnPackage)
                .filter(objectType -> !objectType.isContainer())
                .filter(objectType -> !documentManager.isOperationType(objectType))
                .flatMap(objectType ->
                        Stream.of(
                                buildSchemaTypeField(objectType, InputType.SUBSCRIPTION_ARGUMENTS),
                                buildSchemaTypeFieldList(objectType, InputType.SUBSCRIPTION_ARGUMENTS),
                                buildSchemaTypeFieldConnection(objectType, InputType.SUBSCRIPTION_ARGUMENTS)
                        )
                )
                .collect(Collectors.toList());
    }

    public FieldDefinition buildSchemaTypeField(ObjectType objectType, InputType inputType) {
        FieldDefinition fieldDefinition = new FieldDefinition(typeNameToFieldName(objectType.getName()))
                .setType(new TypeName(objectType.getName()))
                .addArguments(buildInputValuesFromObjectType(objectType, inputType))
                .addDirective(
                        new Directive(DIRECTIVE_PACKAGE_NAME)
                                .addArgument(DIRECTIVE_PACKAGE_ARGUMENT_NAME_NAME, packageConfig.getPackageName())
                );
        if (inputType.equals(InputType.QUERY_ARGUMENTS) || inputType.equals(InputType.SUBSCRIPTION_ARGUMENTS)) {
            fieldDefinition.addArgument(new InputValue(INPUT_VALUE_GROUP_BY_NAME).setType(new ListType(new NonNullType(new TypeName(SCALA_STRING_NAME)))))
                    .addArgument(new InputValue(INPUT_VALUE_NOT_NAME).setType(new TypeName(SCALA_BOOLEAN_NAME)).setDefaultValue(false))
                    .addArgument(new InputValue(INPUT_VALUE_COND_NAME).setType(new TypeName(INPUT_CONDITIONAL_NAME)).setDefaultValue(new EnumValue(INPUT_CONDITIONAL_INPUT_VALUE_AND)))
                    .addArgument(new InputValue(INPUT_VALUE_EXS_NAME).setType(new ListType(new TypeName(objectType.getName() + InputType.EXPRESSION))));
        } else if (inputType.equals(InputType.MUTATION_ARGUMENTS)) {
            fieldDefinition
                    .addArgument(new InputValue(INPUT_VALUE_INPUT_NAME).setType(new TypeName(objectType.getName() + InputType.INPUT)))
                    .addArgument(new InputValue(INPUT_VALUE_WHERE_NAME).setType(new TypeName(objectType.getName() + InputType.EXPRESSION)));
        }
        buildSecurity(objectType, fieldDefinition);
        return fieldDefinition;
    }

    public FieldDefinition buildSchemaTypeFieldList(ObjectType objectType, InputType inputType) {
        FieldDefinition fieldDefinition = new FieldDefinition(typeNameToFieldName(objectType.getName()) + SUFFIX_LIST)
                .addArguments(buildInputValuesFromObjectType(objectType, inputType))
                .setType(new ListType(new TypeName(objectType.getName())))
                .addDirective(
                        new Directive(DIRECTIVE_PACKAGE_NAME)
                                .addArgument(DIRECTIVE_PACKAGE_ARGUMENT_NAME_NAME, packageConfig.getPackageName())
                );

        if (inputType.equals(InputType.QUERY_ARGUMENTS) || inputType.equals(InputType.SUBSCRIPTION_ARGUMENTS)) {
            fieldDefinition
                    .addArgument(new InputValue(INPUT_VALUE_ORDER_BY_NAME).setType(new TypeName(objectType.getName() + InputType.ORDER_BY)))
                    .addArgument(new InputValue(INPUT_VALUE_GROUP_BY_NAME).setType(new ListType(new NonNullType(new TypeName(SCALA_STRING_NAME)))))
                    .addArgument(new InputValue(INPUT_VALUE_NOT_NAME).setType(new TypeName(SCALA_BOOLEAN_NAME)).setDefaultValue(false))
                    .addArgument(new InputValue(INPUT_VALUE_COND_NAME).setType(new TypeName(INPUT_CONDITIONAL_NAME)).setDefaultValue(new EnumValue(INPUT_CONDITIONAL_INPUT_VALUE_AND)))
                    .addArgument(new InputValue(INPUT_VALUE_EXS_NAME).setType(new ListType(new TypeName(objectType.getName() + InputType.EXPRESSION))))
                    .addArgument(new InputValue(INPUT_VALUE_FIRST_NAME).setType(new TypeName(SCALA_INT_NAME)))
                    .addArgument(new InputValue(INPUT_VALUE_LAST_NAME).setType(new TypeName(SCALA_INT_NAME)))
                    .addArgument(new InputValue(INPUT_VALUE_OFFSET_NAME).setType(new TypeName(SCALA_INT_NAME)));

            objectType.getCursorField()
                    .or(objectType::getIDField)
                    .ifPresent(cursorField ->
                            fieldDefinition
                                    .addArgument(new InputValue(INPUT_VALUE_AFTER_NAME).setType(cursorField.getType().getTypeName()))
                                    .addArgument(new InputValue(INPUT_VALUE_BEFORE_NAME).setType(cursorField.getType().getTypeName()))
                    );

        } else if (inputType.equals(InputType.MUTATION_ARGUMENTS)) {
            fieldDefinition
                    .addArgument(new InputValue(INPUT_VALUE_LIST_NAME).setType(new ListType(new TypeName(objectType.getName() + InputType.INPUT))))
                    .addArgument(new InputValue(INPUT_VALUE_WHERE_NAME).setType(new TypeName(objectType.getName() + InputType.EXPRESSION)));
        }
        buildSecurity(objectType, fieldDefinition);
        return fieldDefinition;
    }

    public FieldDefinition buildSchemaTypeFieldConnection(ObjectType objectType, InputType inputType) {
        FieldDefinition fieldDefinition = new FieldDefinition(typeNameToFieldName(objectType.getName()) + SUFFIX_CONNECTION)
                .setType(new TypeName(objectType.getName() + SUFFIX_CONNECTION))
                .addArguments(buildInputValuesFromObjectType(objectType, inputType))
                .addArgument(new InputValue(INPUT_VALUE_ORDER_BY_NAME).setType(new TypeName(objectType.getName() + InputType.ORDER_BY)))
                .addArgument(new InputValue(INPUT_VALUE_GROUP_BY_NAME).setType(new ListType(new NonNullType(new TypeName(SCALA_STRING_NAME)))))
                .addDirective(
                        new Directive()
                                .setName(DIRECTIVE_CONNECTION_NAME)
                                .addArgument(DIRECTIVE_CONNECTION_ARGUMENT_FIELD_NAME, typeNameToFieldName(objectType.getName()) + SUFFIX_LIST)
                                .addArgument(DIRECTIVE_CONNECTION_ARGUMENT_AGG_NAME, typeNameToFieldName(objectType.getName()))
                )
                .addDirective(
                        new Directive(DIRECTIVE_PACKAGE_NAME)
                                .addArgument(DIRECTIVE_PACKAGE_ARGUMENT_NAME_NAME, packageConfig.getPackageName())
                );

        if (inputType.equals(InputType.QUERY_ARGUMENTS) || inputType.equals(InputType.SUBSCRIPTION_ARGUMENTS)) {
            fieldDefinition
                    .addArgument(new InputValue(INPUT_VALUE_NOT_NAME).setType(new TypeName(SCALA_BOOLEAN_NAME)).setDefaultValue(false))
                    .addArgument(new InputValue(INPUT_VALUE_COND_NAME).setType(new TypeName(INPUT_CONDITIONAL_NAME)).setDefaultValue(new EnumValue(INPUT_CONDITIONAL_INPUT_VALUE_AND)))
                    .addArgument(new InputValue(INPUT_VALUE_EXS_NAME).setType(new ListType(new TypeName(objectType.getName() + InputType.EXPRESSION))))
                    .addArgument(new InputValue(INPUT_VALUE_FIRST_NAME).setType(new TypeName(SCALA_INT_NAME)))
                    .addArgument(new InputValue(INPUT_VALUE_LAST_NAME).setType(new TypeName(SCALA_INT_NAME)))
                    .addArgument(new InputValue(INPUT_VALUE_OFFSET_NAME).setType(new TypeName(SCALA_INT_NAME)));

            objectType.getCursorField()
                    .or(objectType::getIDField)
                    .ifPresent(cursorField ->
                            fieldDefinition
                                    .addArgument(new InputValue(INPUT_VALUE_AFTER_NAME).setType(cursorField.getType().getTypeName()))
                                    .addArgument(new InputValue(INPUT_VALUE_BEFORE_NAME).setType(cursorField.getType().getTypeName()))
                    );
        }
        buildSecurity(objectType, fieldDefinition);
        return fieldDefinition;
    }

    public void buildSecurity(ObjectType objectType, FieldDefinition fieldDefinition) {
        Stream.ofNullable(objectType.getDirectives())
                .flatMap(Collection::stream)
                .filter(directive ->
                        directive.getName().equals(DIRECTIVE_PERMIT_ALL) ||
                                directive.getName().equals(DIRECTIVE_DENY_ALL) ||
                                directive.getName().equals(DIRECTIVE_ROLES_ALLOWED)
                )
                .findAny()
                .ifPresent(fieldDefinition::addDirective);
    }

    public List<FieldDefinition> buildFunctionFieldList(ObjectType objectType) {
        List<FieldDefinition> fieldDefinitions = objectType.getFields().stream()
                .filter(fieldDefinition -> !fieldDefinition.isInvokeField())
                .filter(fieldDefinition -> !fieldDefinition.isFetchField())
                .filter(fieldDefinition -> !fieldDefinition.isFunctionField())
                .filter(fieldDefinition -> !fieldDefinition.isConnectionField())
                .filter(fieldDefinition -> !fieldDefinition.isAggregateField())
                .filter(fieldDefinition -> !fieldDefinition.getName().equals(FIELD_TYPENAME_NAME))
                .filter(fieldDefinition -> !documentManager.isMetaInterfaceField(fieldDefinition))
                .filter(fieldDefinition -> !fieldDefinition.getType().hasList())
                .collect(Collectors.toList());

        return Stream.concat(
                fieldDefinitions.stream()
                        .filter(fieldDefinition ->
                                fieldDefinition.getType().getTypeName().getName().equals(SCALA_ID_NAME) ||
                                        fieldDefinition.getType().getTypeName().getName().equals(SCALA_STRING_NAME) ||
                                        fieldDefinition.getType().getTypeName().getName().equals(SCALA_DATE_NAME) ||
                                        fieldDefinition.getType().getTypeName().getName().equals(SCALA_TIME_NAME) ||
                                        fieldDefinition.getType().getTypeName().getName().equals(SCALA_DATE_TIME_NAME) ||
                                        fieldDefinition.getType().getTypeName().getName().equals(SCALA_TIMESTAMP_NAME) ||
                                        fieldDefinition.getType().getTypeName().getName().equals(SCALA_UPLOAD_NAME) ||
                                        documentManager.getFieldTypeDefinition(fieldDefinition).isEnum()
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
                .addArguments(buildInputValuesFromObjectType(documentManager.getFieldTypeDefinition(fieldDefinition).asObject(), InputType.EXPRESSION))
                .addArgument(new InputValue(INPUT_VALUE_FIRST_NAME).setType(new TypeName(SCALA_INT_NAME)))
                .addArgument(new InputValue(INPUT_VALUE_LAST_NAME).setType(new TypeName(SCALA_INT_NAME)))
                .addArgument(new InputValue(INPUT_VALUE_OFFSET_NAME).setType(new TypeName(SCALA_INT_NAME)))
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
                .addArguments(buildInputValuesFromObjectType(documentManager.getFieldTypeDefinition(fieldDefinition).asObject(), InputType.EXPRESSION))
                .addArgument(new InputValue(INPUT_VALUE_FIRST_NAME).setType(new TypeName(SCALA_INT_NAME)))
                .addArgument(new InputValue(INPUT_VALUE_LAST_NAME).setType(new TypeName(SCALA_INT_NAME)))
                .addArgument(new InputValue(INPUT_VALUE_OFFSET_NAME).setType(new TypeName(SCALA_INT_NAME)))
                .addArgument(new InputValue(INPUT_VALUE_ORDER_BY_NAME).setType(new TypeName(fieldDefinition.getType().getTypeName().getName() + InputType.ORDER_BY)))
                .addArgument(new InputValue(INPUT_VALUE_GROUP_BY_NAME).setType(new ListType(new NonNullType(new TypeName(SCALA_STRING_NAME)))))
                .setDirectives(fieldDefinition.getDirectives())
                .addDirective(new Directive(DIRECTIVE_AGGREGATE_NAME));
    }

    public List<InputObjectType> buildQueryTypeFieldArguments(Document document) {
        return document.getDefinitions().stream()
                .filter(Definition::isObject)
                .map(Definition::asObject)
                .filter(packageManager::isOwnPackage)
                .filter(objectType -> !objectType.isContainer())
                .filter(objectType -> !documentManager.isOperationType(objectType))
                .flatMap(objectType ->
                        Stream.of(
                                buildSchemaTypeFieldArguments(objectType, InputType.QUERY_ARGUMENTS),
                                buildSchemaTypeFieldListArguments(objectType, InputType.QUERY_ARGUMENTS),
                                buildSchemaTypeFieldConnectionArguments(objectType, InputType.QUERY_ARGUMENTS)
                        )
                )
                .collect(Collectors.toList());
    }

    public List<InputObjectType> buildMutationTypeFieldsArguments(Document document) {
        return document.getDefinitions().stream()
                .filter(Definition::isObject)
                .map(Definition::asObject)
                .filter(packageManager::isOwnPackage)
                .filter(objectType -> !objectType.isContainer())
                .filter(objectType -> !documentManager.isOperationType(objectType))
                .flatMap(objectType ->
                        Stream.of(
                                buildSchemaTypeFieldArguments(objectType, InputType.MUTATION_ARGUMENTS),
                                buildSchemaTypeFieldListArguments(objectType, InputType.MUTATION_ARGUMENTS)
                        )
                )
                .collect(Collectors.toList());
    }

    public List<InputObjectType> buildSubscriptionTypeFieldsArguments(Document document) {
        return document.getDefinitions().stream()
                .filter(Definition::isObject)
                .map(Definition::asObject)
                .filter(packageManager::isOwnPackage)
                .filter(objectType -> !objectType.isContainer())
                .filter(objectType -> !documentManager.isOperationType(objectType))
                .flatMap(objectType ->
                        Stream.of(
                                buildSchemaTypeFieldArguments(objectType, InputType.SUBSCRIPTION_ARGUMENTS),
                                buildSchemaTypeFieldListArguments(objectType, InputType.SUBSCRIPTION_ARGUMENTS),
                                buildSchemaTypeFieldConnectionArguments(objectType, InputType.SUBSCRIPTION_ARGUMENTS)
                        )
                )
                .collect(Collectors.toList());
    }

    public InputObjectType buildSchemaTypeFieldArguments(ObjectType objectType, InputType inputType) {
        InputObjectType inputObjectType = new InputObjectType()
                .addInputValues(buildInputValuesFromObjectType(objectType, inputType))
                .addDirective(
                        new Directive(DIRECTIVE_PACKAGE_NAME)
                                .addArgument(DIRECTIVE_PACKAGE_ARGUMENT_NAME_NAME, packageConfig.getPackageName())
                );
        if (inputType.equals(InputType.QUERY_ARGUMENTS)) {
            ObjectType queryOperationType = documentManager.getDocument().getQueryOperationTypeOrError();
            inputObjectType
                    .addInputValue(new InputValue(INPUT_VALUE_GROUP_BY_NAME).setType(new ListType(new NonNullType(new TypeName(SCALA_STRING_NAME)))))
                    .addInputValue(new InputValue(INPUT_VALUE_NOT_NAME).setType(new TypeName(SCALA_BOOLEAN_NAME)).setDefaultValue(false))
                    .addInputValue(new InputValue(INPUT_VALUE_COND_NAME).setType(new TypeName(INPUT_CONDITIONAL_NAME)).setDefaultValue(new EnumValue(INPUT_CONDITIONAL_INPUT_VALUE_AND)))
                    .addInputValue(new InputValue(INPUT_VALUE_EXS_NAME).setType(new ListType(new TypeName(objectType.getName() + InputType.EXPRESSION))))
                    .setName(objectType.getName() + queryOperationType.getName() + inputType)
                    .addDirective(
                            new Directive(DIRECTIVE_CLASS_NAME)
                                    .addArgument(DIRECTIVE_CLASS_ARGUMENT_NAME_NAME, packageConfig.getInputObjectTypePackageName() + "." + objectType.getName() + queryOperationType.getName() + inputType)
                    )
                    .addDirective(
                            new Directive(DIRECTIVE_ANNOTATION_NAME)
                                    .addArgument(DIRECTIVE_ANNOTATION_ARGUMENT_NAME_NAME, packageConfig.getAnnotationPackageName() + "." + objectType.getName() + queryOperationType.getName() + inputType)
                    )
                    .addDirective(
                            new Directive(DIRECTIVE_GRPC_NAME)
                                    .addArgument(DIRECTIVE_GRPC_ARGUMENT_NAME_NAME, packageConfig.getGrpcInputObjectTypePackageName() + "." + getGrpcName(objectType.getName() + queryOperationType.getName() + inputType))
                    )
                    .addDirective(
                            new Directive(DIRECTIVE_IMPLEMENTS_NAME)
                                    .addArgument(DIRECTIVE_IMPLEMENTS_ARGUMENT_INTERFACES_NAME,
                                            new ArrayValueWithVariable(
                                                    Stream
                                                            .concat(
                                                                    Stream.ofNullable(objectType.getInterfaces()).flatMap(Collection::stream),
                                                                    Stream.of(INTERFACE_META_NAME)
                                                            )
                                                            .map(interfaceName -> interfaceName + SUFFIX_EXPRESSION)
                                                            .distinct()
                                                            .collect(Collectors.toList())
                                            )
                                    )
                    );
        } else if (inputType.equals(InputType.SUBSCRIPTION_ARGUMENTS)) {
            ObjectType subscriptionOperation = documentManager.getDocument().getSubscriptionOperationTypeOrError();
            inputObjectType
                    .addInputValue(new InputValue(INPUT_VALUE_GROUP_BY_NAME).setType(new ListType(new NonNullType(new TypeName(SCALA_STRING_NAME)))))
                    .addInputValue(new InputValue(INPUT_VALUE_NOT_NAME).setType(new TypeName(SCALA_BOOLEAN_NAME)).setDefaultValue(false))
                    .addInputValue(new InputValue(INPUT_VALUE_COND_NAME).setType(new TypeName(INPUT_CONDITIONAL_NAME)).setDefaultValue(new EnumValue(INPUT_CONDITIONAL_INPUT_VALUE_AND)))
                    .addInputValue(new InputValue(INPUT_VALUE_EXS_NAME).setType(new ListType(new TypeName(objectType.getName() + InputType.EXPRESSION))))
                    .setName(objectType.getName() + subscriptionOperation.getName() + inputType)
                    .addDirective(
                            new Directive(DIRECTIVE_CLASS_NAME)
                                    .addArgument(DIRECTIVE_CLASS_ARGUMENT_NAME_NAME, packageConfig.getInputObjectTypePackageName() + "." + objectType.getName() + subscriptionOperation.getName() + inputType)
                    )
                    .addDirective(
                            new Directive(DIRECTIVE_ANNOTATION_NAME)
                                    .addArgument(DIRECTIVE_ANNOTATION_ARGUMENT_NAME_NAME, packageConfig.getAnnotationPackageName() + "." + objectType.getName() + subscriptionOperation.getName() + inputType)
                    )
                    .addDirective(
                            new Directive(DIRECTIVE_GRPC_NAME)
                                    .addArgument(DIRECTIVE_GRPC_ARGUMENT_NAME_NAME, packageConfig.getGrpcInputObjectTypePackageName() + "." + getGrpcName(objectType.getName() + subscriptionOperation.getName() + inputType))
                    )
                    .addDirective(
                            new Directive(DIRECTIVE_IMPLEMENTS_NAME)
                                    .addArgument(DIRECTIVE_IMPLEMENTS_ARGUMENT_INTERFACES_NAME,
                                            new ArrayValueWithVariable(
                                                    Stream
                                                            .concat(
                                                                    Stream.ofNullable(objectType.getInterfaces()).flatMap(Collection::stream),
                                                                    Stream.of(INTERFACE_META_NAME)
                                                            )
                                                            .map(interfaceName -> interfaceName + SUFFIX_EXPRESSION)
                                                            .distinct()
                                                            .collect(Collectors.toList())
                                            )
                                    )
                    );
        } else if (inputType.equals(InputType.MUTATION_ARGUMENTS)) {
            ObjectType mutationOperationType = documentManager.getDocument().getMutationOperationTypeOrError();
            inputObjectType
                    .addInputValue(new InputValue(INPUT_VALUE_INPUT_NAME).setType(new TypeName(objectType.getName() + InputType.INPUT)))
                    .addInputValue(new InputValue(INPUT_VALUE_WHERE_NAME).setType(new TypeName(objectType.getName() + InputType.EXPRESSION)))
                    .setName(objectType.getName() + mutationOperationType.getName() + inputType)
                    .addDirective(
                            new Directive(DIRECTIVE_CLASS_NAME)
                                    .addArgument(DIRECTIVE_CLASS_ARGUMENT_NAME_NAME, packageConfig.getInputObjectTypePackageName() + "." + objectType.getName() + mutationOperationType.getName() + inputType)
                    )
                    .addDirective(
                            new Directive(DIRECTIVE_ANNOTATION_NAME)
                                    .addArgument(DIRECTIVE_ANNOTATION_ARGUMENT_NAME_NAME, packageConfig.getAnnotationPackageName() + "." + objectType.getName() + mutationOperationType.getName() + inputType)
                    )
                    .addDirective(
                            new Directive(DIRECTIVE_GRPC_NAME)
                                    .addArgument(DIRECTIVE_GRPC_ARGUMENT_NAME_NAME, packageConfig.getGrpcInputObjectTypePackageName() + "." + getGrpcName(objectType.getName() + mutationOperationType.getName() + inputType))
                    )
                    .addDirective(
                            new Directive(DIRECTIVE_IMPLEMENTS_NAME)
                                    .addArgument(DIRECTIVE_IMPLEMENTS_ARGUMENT_INTERFACES_NAME,
                                            new ArrayValueWithVariable(
                                                    Stream
                                                            .concat(
                                                                    Stream.ofNullable(objectType.getInterfaces()).flatMap(Collection::stream),
                                                                    Stream.of(INTERFACE_META_NAME)
                                                            )
                                                            .map(interfaceName -> interfaceName + SUFFIX_INPUT)
                                                            .distinct()
                                                            .collect(Collectors.toList())
                                            )
                                    )
                    );
        }
        return inputObjectType;
    }

    public InputObjectType buildSchemaTypeFieldListArguments(ObjectType objectType, InputType inputType) {
        InputObjectType inputObjectType = new InputObjectType()
                .addInputValues(buildInputValuesFromObjectType(objectType, inputType))
                .addDirective(
                        new Directive(DIRECTIVE_PACKAGE_NAME)
                                .addArgument(DIRECTIVE_PACKAGE_ARGUMENT_NAME_NAME, packageConfig.getPackageName())
                );

        if (inputType.equals(InputType.QUERY_ARGUMENTS)) {
            ObjectType queryOperationType = documentManager.getDocument().getQueryOperationTypeOrError();
            inputObjectType
                    .addInputValue(new InputValue(INPUT_VALUE_ORDER_BY_NAME).setType(new TypeName(objectType.getName() + InputType.ORDER_BY)))
                    .addInputValue(new InputValue(INPUT_VALUE_GROUP_BY_NAME).setType(new ListType(new NonNullType(new TypeName(SCALA_STRING_NAME)))))
                    .addInputValue(new InputValue(INPUT_VALUE_NOT_NAME).setType(new TypeName(SCALA_BOOLEAN_NAME)).setDefaultValue(false))
                    .addInputValue(new InputValue(INPUT_VALUE_COND_NAME).setType(new TypeName(INPUT_CONDITIONAL_NAME)).setDefaultValue(new EnumValue(INPUT_CONDITIONAL_INPUT_VALUE_AND)))
                    .addInputValue(new InputValue(INPUT_VALUE_EXS_NAME).setType(new ListType(new TypeName(objectType.getName() + InputType.EXPRESSION))))
                    .addInputValue(new InputValue(INPUT_VALUE_FIRST_NAME).setType(new TypeName(SCALA_INT_NAME)))
                    .addInputValue(new InputValue(INPUT_VALUE_LAST_NAME).setType(new TypeName(SCALA_INT_NAME)))
                    .addInputValue(new InputValue(INPUT_VALUE_OFFSET_NAME).setType(new TypeName(SCALA_INT_NAME)))
                    .setName(objectType.getName() + SUFFIX_LIST + queryOperationType.getName() + inputType)
                    .addDirective(
                            new Directive(DIRECTIVE_CLASS_NAME)
                                    .addArgument(DIRECTIVE_CLASS_ARGUMENT_NAME_NAME, packageConfig.getInputObjectTypePackageName() + "." + objectType.getName() + SUFFIX_LIST + queryOperationType.getName() + inputType)
                    )
                    .addDirective(
                            new Directive(DIRECTIVE_ANNOTATION_NAME)
                                    .addArgument(DIRECTIVE_ANNOTATION_ARGUMENT_NAME_NAME, packageConfig.getAnnotationPackageName() + "." + objectType.getName() + SUFFIX_LIST + queryOperationType.getName() + inputType)
                    )
                    .addDirective(
                            new Directive(DIRECTIVE_GRPC_NAME)
                                    .addArgument(DIRECTIVE_GRPC_ARGUMENT_NAME_NAME, packageConfig.getGrpcInputObjectTypePackageName() + "." + getGrpcName(objectType.getName() + SUFFIX_LIST + queryOperationType.getName() + inputType))
                    )
                    .addDirective(
                            new Directive(DIRECTIVE_IMPLEMENTS_NAME)
                                    .addArgument(DIRECTIVE_IMPLEMENTS_ARGUMENT_INTERFACES_NAME,
                                            new ArrayValueWithVariable(
                                                    Stream
                                                            .concat(
                                                                    Stream.ofNullable(objectType.getInterfaces()).flatMap(Collection::stream),
                                                                    Stream.of(INTERFACE_META_NAME)
                                                            )
                                                            .map(interfaceName -> interfaceName + SUFFIX_EXPRESSION)
                                                            .distinct()
                                                            .collect(Collectors.toList())
                                            )
                                    )
                    );
            objectType.getCursorField()
                    .or(objectType::getIDField)
                    .ifPresent(cursorField ->
                            inputObjectType
                                    .addInputValue(new InputValue(INPUT_VALUE_AFTER_NAME).setType(cursorField.getType().getTypeName()))
                                    .addInputValue(new InputValue(INPUT_VALUE_BEFORE_NAME).setType(cursorField.getType().getTypeName()))
                    );
        } else if (inputType.equals(InputType.SUBSCRIPTION_ARGUMENTS)) {
            ObjectType subscriptionOperationType = documentManager.getDocument().getSubscriptionOperationTypeOrError();
            inputObjectType
                    .addInputValue(new InputValue(INPUT_VALUE_ORDER_BY_NAME).setType(new TypeName(objectType.getName() + InputType.ORDER_BY)))
                    .addInputValue(new InputValue(INPUT_VALUE_GROUP_BY_NAME).setType(new ListType(new NonNullType(new TypeName(SCALA_STRING_NAME)))))
                    .addInputValue(new InputValue(INPUT_VALUE_NOT_NAME).setType(new TypeName(SCALA_BOOLEAN_NAME)).setDefaultValue(false))
                    .addInputValue(new InputValue(INPUT_VALUE_COND_NAME).setType(new TypeName(INPUT_CONDITIONAL_NAME)).setDefaultValue(new EnumValue(INPUT_CONDITIONAL_INPUT_VALUE_AND)))
                    .addInputValue(new InputValue(INPUT_VALUE_EXS_NAME).setType(new ListType(new TypeName(objectType.getName() + InputType.EXPRESSION))))
                    .addInputValue(new InputValue(INPUT_VALUE_FIRST_NAME).setType(new TypeName(SCALA_INT_NAME)))
                    .addInputValue(new InputValue(INPUT_VALUE_LAST_NAME).setType(new TypeName(SCALA_INT_NAME)))
                    .addInputValue(new InputValue(INPUT_VALUE_OFFSET_NAME).setType(new TypeName(SCALA_INT_NAME)))
                    .setName(objectType.getName() + SUFFIX_LIST + subscriptionOperationType.getName() + inputType)
                    .addDirective(
                            new Directive(DIRECTIVE_CLASS_NAME)
                                    .addArgument(DIRECTIVE_CLASS_ARGUMENT_NAME_NAME, packageConfig.getInputObjectTypePackageName() + "." + objectType.getName() + SUFFIX_LIST + subscriptionOperationType.getName() + inputType)
                    )
                    .addDirective(
                            new Directive(DIRECTIVE_ANNOTATION_NAME)
                                    .addArgument(DIRECTIVE_ANNOTATION_ARGUMENT_NAME_NAME, packageConfig.getAnnotationPackageName() + "." + objectType.getName() + SUFFIX_LIST + subscriptionOperationType.getName() + inputType)
                    )
                    .addDirective(
                            new Directive(DIRECTIVE_GRPC_NAME)
                                    .addArgument(DIRECTIVE_GRPC_ARGUMENT_NAME_NAME, packageConfig.getGrpcInputObjectTypePackageName() + "." + getGrpcName(objectType.getName() + SUFFIX_LIST + subscriptionOperationType.getName() + inputType))
                    )
                    .addDirective(
                            new Directive(DIRECTIVE_IMPLEMENTS_NAME)
                                    .addArgument(DIRECTIVE_IMPLEMENTS_ARGUMENT_INTERFACES_NAME,
                                            new ArrayValueWithVariable(
                                                    Stream
                                                            .concat(
                                                                    Stream.ofNullable(objectType.getInterfaces()).flatMap(Collection::stream),
                                                                    Stream.of(INTERFACE_META_NAME)
                                                            )
                                                            .map(interfaceName -> interfaceName + SUFFIX_EXPRESSION)
                                                            .distinct()
                                                            .collect(Collectors.toList())
                                            )
                                    )
                    );
            objectType.getCursorField()
                    .or(objectType::getIDField)
                    .ifPresent(cursorField ->
                            inputObjectType
                                    .addInputValue(new InputValue(INPUT_VALUE_AFTER_NAME).setType(cursorField.getType().getTypeName()))
                                    .addInputValue(new InputValue(INPUT_VALUE_BEFORE_NAME).setType(cursorField.getType().getTypeName()))
                    );
        } else if (inputType.equals(InputType.MUTATION_ARGUMENTS)) {
            ObjectType mutationOperationType = documentManager.getDocument().getMutationOperationTypeOrError();
            inputObjectType
                    .addInputValue(new InputValue(INPUT_VALUE_LIST_NAME).setType(new ListType(new TypeName(objectType.getName() + InputType.INPUT))))
                    .addInputValue(new InputValue(INPUT_VALUE_WHERE_NAME).setType(new TypeName(objectType.getName() + InputType.EXPRESSION)))
                    .setName(objectType.getName() + SUFFIX_LIST + mutationOperationType.getName() + inputType)
                    .addDirective(
                            new Directive(DIRECTIVE_CLASS_NAME)
                                    .addArgument(DIRECTIVE_CLASS_ARGUMENT_NAME_NAME, packageConfig.getInputObjectTypePackageName() + "." + objectType.getName() + SUFFIX_LIST + mutationOperationType.getName() + inputType)
                    )
                    .addDirective(
                            new Directive(DIRECTIVE_ANNOTATION_NAME)
                                    .addArgument(DIRECTIVE_ANNOTATION_ARGUMENT_NAME_NAME, packageConfig.getAnnotationPackageName() + "." + objectType.getName() + SUFFIX_LIST + mutationOperationType.getName() + inputType)
                    )
                    .addDirective(
                            new Directive(DIRECTIVE_GRPC_NAME)
                                    .addArgument(DIRECTIVE_GRPC_ARGUMENT_NAME_NAME, packageConfig.getGrpcInputObjectTypePackageName() + "." + getGrpcName(objectType.getName() + SUFFIX_LIST + mutationOperationType.getName() + inputType))
                    )
                    .addDirective(
                            new Directive(DIRECTIVE_IMPLEMENTS_NAME)
                                    .addArgument(DIRECTIVE_IMPLEMENTS_ARGUMENT_INTERFACES_NAME,
                                            new ArrayValueWithVariable(
                                                    Stream
                                                            .concat(
                                                                    Stream.ofNullable(objectType.getInterfaces()).flatMap(Collection::stream),
                                                                    Stream.of(INTERFACE_META_NAME)
                                                            )
                                                            .map(interfaceName -> interfaceName + SUFFIX_INPUT)
                                                            .distinct()
                                                            .collect(Collectors.toList())
                                            )
                                    )
                    );
        }
        return inputObjectType;
    }

    public InputObjectType buildSchemaTypeFieldConnectionArguments(ObjectType objectType, InputType inputType) {
        InputObjectType inputObjectType = new InputObjectType()
                .addInputValues(buildInputValuesFromObjectType(objectType, inputType))
                .addDirective(
                        new Directive(DIRECTIVE_PACKAGE_NAME)
                                .addArgument(DIRECTIVE_PACKAGE_ARGUMENT_NAME_NAME, packageConfig.getPackageName())
                );

        if (inputType.equals(InputType.QUERY_ARGUMENTS)) {
            ObjectType queryOperationType = documentManager.getDocument().getQueryOperationTypeOrError();
            inputObjectType
                    .addInputValue(new InputValue(INPUT_VALUE_ORDER_BY_NAME).setType(new TypeName(objectType.getName() + InputType.ORDER_BY)))
                    .addInputValue(new InputValue(INPUT_VALUE_GROUP_BY_NAME).setType(new ListType(new NonNullType(new TypeName(SCALA_STRING_NAME)))))
                    .addInputValue(new InputValue(INPUT_VALUE_NOT_NAME).setType(new TypeName(SCALA_BOOLEAN_NAME)).setDefaultValue(false))
                    .addInputValue(new InputValue(INPUT_VALUE_COND_NAME).setType(new TypeName(INPUT_CONDITIONAL_NAME)).setDefaultValue(new EnumValue(INPUT_CONDITIONAL_INPUT_VALUE_AND)))
                    .addInputValue(new InputValue(INPUT_VALUE_EXS_NAME).setType(new ListType(new TypeName(objectType.getName() + InputType.EXPRESSION))))
                    .addInputValue(new InputValue(INPUT_VALUE_FIRST_NAME).setType(new TypeName(SCALA_INT_NAME)))
                    .addInputValue(new InputValue(INPUT_VALUE_LAST_NAME).setType(new TypeName(SCALA_INT_NAME)))
                    .addInputValue(new InputValue(INPUT_VALUE_OFFSET_NAME).setType(new TypeName(SCALA_INT_NAME)))
                    .setName(objectType.getName() + SUFFIX_CONNECTION + queryOperationType.getName() + inputType)
                    .addDirective(
                            new Directive(DIRECTIVE_CLASS_NAME)
                                    .addArgument(DIRECTIVE_CLASS_ARGUMENT_NAME_NAME, packageConfig.getInputObjectTypePackageName() + "." + objectType.getName() + SUFFIX_CONNECTION + queryOperationType.getName() + inputType)
                    )
                    .addDirective(
                            new Directive(DIRECTIVE_ANNOTATION_NAME)
                                    .addArgument(DIRECTIVE_ANNOTATION_ARGUMENT_NAME_NAME, packageConfig.getAnnotationPackageName() + "." + objectType.getName() + SUFFIX_CONNECTION + queryOperationType.getName() + inputType)
                    )
                    .addDirective(
                            new Directive(DIRECTIVE_GRPC_NAME)
                                    .addArgument(DIRECTIVE_GRPC_ARGUMENT_NAME_NAME, packageConfig.getGrpcInputObjectTypePackageName() + "." + getGrpcName(objectType.getName() + SUFFIX_CONNECTION + queryOperationType.getName() + inputType))
                    )
                    .addDirective(
                            new Directive(DIRECTIVE_IMPLEMENTS_NAME)
                                    .addArgument(DIRECTIVE_IMPLEMENTS_ARGUMENT_INTERFACES_NAME,
                                            new ArrayValueWithVariable(
                                                    Stream
                                                            .concat(
                                                                    Stream.ofNullable(objectType.getInterfaces()).flatMap(Collection::stream),
                                                                    Stream.of(INTERFACE_META_NAME)
                                                            )
                                                            .map(interfaceName -> interfaceName + SUFFIX_EXPRESSION)
                                                            .distinct()
                                                            .collect(Collectors.toList())
                                            )
                                    )
                    );
            objectType.getCursorField()
                    .or(objectType::getIDField)
                    .ifPresent(cursorField ->
                            inputObjectType
                                    .addInputValue(new InputValue(INPUT_VALUE_AFTER_NAME).setType(cursorField.getType().getTypeName()))
                                    .addInputValue(new InputValue(INPUT_VALUE_BEFORE_NAME).setType(cursorField.getType().getTypeName()))
                    );
        } else if (inputType.equals(InputType.SUBSCRIPTION_ARGUMENTS)) {
            ObjectType subscriptionOperationType = documentManager.getDocument().getSubscriptionOperationTypeOrError();
            inputObjectType
                    .addInputValue(new InputValue(INPUT_VALUE_ORDER_BY_NAME).setType(new TypeName(objectType.getName() + InputType.ORDER_BY)))
                    .addInputValue(new InputValue(INPUT_VALUE_GROUP_BY_NAME).setType(new ListType(new NonNullType(new TypeName(SCALA_STRING_NAME)))))
                    .addInputValue(new InputValue(INPUT_VALUE_NOT_NAME).setType(new TypeName(SCALA_BOOLEAN_NAME)).setDefaultValue(false))
                    .addInputValue(new InputValue(INPUT_VALUE_COND_NAME).setType(new TypeName(INPUT_CONDITIONAL_NAME)).setDefaultValue(new EnumValue(INPUT_CONDITIONAL_INPUT_VALUE_AND)))
                    .addInputValue(new InputValue(INPUT_VALUE_EXS_NAME).setType(new ListType(new TypeName(objectType.getName() + InputType.EXPRESSION))))
                    .addInputValue(new InputValue(INPUT_VALUE_FIRST_NAME).setType(new TypeName(SCALA_INT_NAME)))
                    .addInputValue(new InputValue(INPUT_VALUE_LAST_NAME).setType(new TypeName(SCALA_INT_NAME)))
                    .addInputValue(new InputValue(INPUT_VALUE_OFFSET_NAME).setType(new TypeName(SCALA_INT_NAME)))
                    .setName(objectType.getName() + SUFFIX_CONNECTION + subscriptionOperationType.getName() + inputType)
                    .addDirective(
                            new Directive(DIRECTIVE_CLASS_NAME)
                                    .addArgument(DIRECTIVE_CLASS_ARGUMENT_NAME_NAME, packageConfig.getInputObjectTypePackageName() + "." + objectType.getName() + SUFFIX_CONNECTION + subscriptionOperationType.getName() + inputType)
                    )
                    .addDirective(
                            new Directive(DIRECTIVE_ANNOTATION_NAME)
                                    .addArgument(DIRECTIVE_ANNOTATION_ARGUMENT_NAME_NAME, packageConfig.getAnnotationPackageName() + "." + objectType.getName() + SUFFIX_CONNECTION + subscriptionOperationType.getName() + inputType)
                    )
                    .addDirective(
                            new Directive(DIRECTIVE_GRPC_NAME)
                                    .addArgument(DIRECTIVE_GRPC_ARGUMENT_NAME_NAME, packageConfig.getGrpcInputObjectTypePackageName() + "." + getGrpcName(objectType.getName() + SUFFIX_CONNECTION + subscriptionOperationType.getName() + inputType))
                    )
                    .addDirective(
                            new Directive(DIRECTIVE_IMPLEMENTS_NAME)
                                    .addArgument(DIRECTIVE_IMPLEMENTS_ARGUMENT_INTERFACES_NAME,
                                            new ArrayValueWithVariable(
                                                    Stream
                                                            .concat(
                                                                    Stream.ofNullable(objectType.getInterfaces()).flatMap(Collection::stream),
                                                                    Stream.of(INTERFACE_META_NAME)
                                                            )
                                                            .map(interfaceName -> interfaceName + SUFFIX_EXPRESSION)
                                                            .distinct()
                                                            .collect(Collectors.toList())
                                            )
                                    )
                    );
            objectType.getCursorField()
                    .or(objectType::getIDField)
                    .ifPresent(cursorField ->
                            inputObjectType
                                    .addInputValue(new InputValue(INPUT_VALUE_AFTER_NAME).setType(cursorField.getType().getTypeName()))
                                    .addInputValue(new InputValue(INPUT_VALUE_BEFORE_NAME).setType(cursorField.getType().getTypeName()))
                    );
        }
        return inputObjectType;
    }

    public List<InputObjectType> buildQueryTypeInvokeFieldArguments(Document document) {
        return document.getQueryOperationType().stream()
                .flatMap(objectType ->
                        objectType.getFields().stream()
                                .filter(packageManager::isOwnPackage)
                                .filter(FieldDefinition::isInvokeField)
                                .filter(fieldDefinition -> !fieldDefinition.getArguments().isEmpty())
                                .map(fieldDefinition -> buildSchemaTypeInvokeFieldArguments(objectType, fieldDefinition))
                )
                .collect(Collectors.toList());
    }

    public List<InputObjectType> buildMutationTypeInvokeFieldsArguments(Document document) {
        return document.getMutationOperationType().stream()
                .flatMap(objectType ->
                        objectType.getFields().stream()
                                .filter(packageManager::isOwnPackage)
                                .filter(FieldDefinition::isInvokeField)
                                .filter(fieldDefinition -> !fieldDefinition.getArguments().isEmpty())
                                .map(fieldDefinition -> buildSchemaTypeInvokeFieldArguments(objectType, fieldDefinition))
                )
                .collect(Collectors.toList());
    }

    public List<InputObjectType> buildSubscriptionTypeInvokeFieldsArguments(Document document) {
        return document.getSubscriptionOperationType().stream()
                .flatMap(objectType ->
                        objectType.getFields().stream()
                                .filter(packageManager::isOwnPackage)
                                .filter(FieldDefinition::isInvokeField)
                                .filter(fieldDefinition -> !fieldDefinition.getArguments().isEmpty())
                                .map(fieldDefinition -> buildSchemaTypeInvokeFieldArguments(objectType, fieldDefinition))
                )
                .collect(Collectors.toList());
    }

    public InputObjectType buildSchemaTypeInvokeFieldArguments(ObjectType objectType, FieldDefinition fieldDefinition) {
        return new InputObjectType(objectType.getName() + "_" + fieldDefinition.getName() + "_" + SUFFIX_ARGUMENTS)
                .addInputValues(
                        Stream.ofNullable(fieldDefinition.getArguments())
                                .flatMap(Collection::stream)
                                .collect(Collectors.toList())
                )
                .addDirective(
                        new Directive(DIRECTIVE_PACKAGE_NAME)
                                .addArgument(DIRECTIVE_PACKAGE_ARGUMENT_NAME_NAME, packageConfig.getPackageName())
                )
                .addDirective(
                        new Directive(DIRECTIVE_CLASS_NAME)
                                .addArgument(DIRECTIVE_CLASS_ARGUMENT_NAME_NAME, packageConfig.getInputObjectTypePackageName() + "." + objectType.getName() + "_" + fieldDefinition.getName() + "_" + SUFFIX_ARGUMENTS)
                )
                .addDirective(
                        new Directive(DIRECTIVE_GRPC_NAME)
                                .addArgument(DIRECTIVE_GRPC_ARGUMENT_NAME_NAME, packageConfig.getGrpcInputObjectTypePackageName() + "." + getGrpcName(objectType.getName() + "_" + fieldDefinition.getName() + "_" + SUFFIX_ARGUMENTS))
                );
    }

    public enum InputType {
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

    public enum Function {
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
                    .setType(new TypeName(returnTypeName))
                    .addDirective(
                            new Directive()
                                    .setName(DIRECTIVE_FUNC_NAME)
                                    .addArgument(DIRECTIVE_FUNC_ARGUMENT_NAME_NAME, new EnumValue(this.name()))
                                    .addArgument(DIRECTIVE_FUNC_ARGUMENT_FIELD_NAME, fieldName)
                    );
            if (isList) {
                fieldDefinition
                        .addArgument(new InputValue(INPUT_OPERATOR_INPUT_VALUE_OPR_NAME).setType(new TypeName(INPUT_OPERATOR_NAME)).setDefaultValue(new EnumValue(INPUT_OPERATOR_INPUT_VALUE_EQ)))
                        .addArgument(new InputValue(INPUT_OPERATOR_INPUT_VALUE_VAL_NAME).setType(new TypeName(fieldTypeName)))
                        .addArgument(new InputValue(INPUT_OPERATOR_INPUT_VALUE_ARR_NAME).setType(new ListType(new TypeName(fieldTypeName))));
            }
            return fieldDefinition;
        }
    }

    public Document mapToLocalFetch() {
        return mapToLocalFetch(documentManager.getDocument());
    }

    public Document mapToLocalFetch(Document document) {
        document.getDefinitions().stream()
                .filter(Definition::isObject)
                .flatMap(definition -> definition.asObject().getFields().stream())
                .filter(FieldDefinition::isMapField)
                .forEach(fieldDefinition ->
                        fieldDefinition.getDirective(DIRECTIVE_MAP_NAME)
                                .setName(DIRECTIVE_FETCH_NAME)
                );
        return document;
    }
}
