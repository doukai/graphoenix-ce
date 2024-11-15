package io.graphoenix.introspection.handler;

import com.google.common.collect.Streams;
import io.graphoenix.core.handler.DocumentManager;
import io.graphoenix.introspection.bo.*;
import io.graphoenix.spi.error.GraphQLErrors;
import io.graphoenix.spi.graphql.Definition;
import io.graphoenix.spi.graphql.common.ObjectValueWithVariable;
import io.graphoenix.spi.graphql.operation.Field;
import io.graphoenix.spi.graphql.operation.Operation;
import io.graphoenix.spi.graphql.type.*;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.tinylog.Logger;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static io.graphoenix.spi.constant.Hammurabi.INPUT_VALUE_LIST_NAME;
import static io.graphoenix.spi.constant.Hammurabi.PREFIX_INTROSPECTION;
import static io.graphoenix.spi.error.GraphQLErrorType.UNSUPPORTED_FIELD_TYPE;

@ApplicationScoped
public class IntrospectionMutationBuilder {

    private final DocumentManager documentManager;

    private final int levelThreshold;

    @Inject
    public IntrospectionMutationBuilder(DocumentManager documentManager) {
        this.documentManager = documentManager;
        this.levelThreshold = 1;
    }

    public Operation buildIntrospectionSchemaMutation() {
        __Schema schema = buildIntrospectionSchema();

        Operation operation = new Operation()
                .setOperationType("mutation")
                .addSelection(
                        new Field("__schema")
                                .setArguments(schema.toArguments())
                                .addSelection(new Field("id"))
                )
                .setName("introspection");

        List<ObjectValueWithVariable> ofTypes =
                Stream.ofNullable(schema.getTypes())
                        .flatMap(Collection::stream)
                        .flatMap(type -> {
                                    switch (type.getKind()) {
                                        case SCALAR:
                                        case OBJECT:
                                        case INTERFACE:
                                        case UNION:
                                        case ENUM:
                                        case INPUT_OBJECT:
                                            return Stream.of(
                                                    buildNonNullType(type),
                                                    buildListType(type),
                                                    buildNonNullListType(type),
                                                    buildListNonNullType(type),
                                                    buildNonNullListNonNullType(type)
                                            );
                                        default:
                                            return Stream.empty();
                                    }
                                }
                        )
                        .map(__Type::toObjectValue)
                        .collect(Collectors.toList());

        if (!ofTypes.isEmpty()) {
            operation.addSelection(
                    new Field("__typeList")
                            .addArgument(INPUT_VALUE_LIST_NAME, ofTypes)
                            .addSelection(new Field("name"))
            );
        }

        List<ObjectValueWithVariable> interfacesObjectValues = documentManager.getDocument().getObjectTypes()
                .map(this::buildType)
                .flatMap(__Type::getInterfacesObjectValues)
                .collect(Collectors.toList());

        if (!interfacesObjectValues.isEmpty()) {
            operation.addSelection(
                    new Field("__typeInterfacesList")
                            .addArgument(INPUT_VALUE_LIST_NAME, interfacesObjectValues)
                            .addSelection(new Field("id"))
            );
        }

        List<ObjectValueWithVariable> possibleTypesObjectValues = documentManager.getDocument().getInterfaceTypes()
                .map(this::buildType)
                .flatMap(__Type::getPossibleTypesObjectValues)
                .collect(Collectors.toList());

        if (!possibleTypesObjectValues.isEmpty()) {
            operation.addSelection(
                    new Field("__typePossibleTypesList")
                            .addArgument(INPUT_VALUE_LIST_NAME, possibleTypesObjectValues)
                            .addSelection(new Field("id"))
            );
        }

        Logger.info("introspection schema mutation build success");
        return operation;
    }

    public __Schema buildIntrospectionSchema() {
        __Schema schema = new __Schema();
        schema.setTypes(
                Streams.concat(
                        documentManager.getDocument().getObjectTypes()
                                .filter(objectType -> !objectType.getName().startsWith(PREFIX_INTROSPECTION))
                                .map(this::buildType),
                        documentManager.getDocument().getInterfaceTypes()
                                .filter(interfaceType -> !interfaceType.getName().startsWith(PREFIX_INTROSPECTION))
                                .map(this::buildType),
                        documentManager.getDocument().getInputObjectTypes()
                                .filter(inputObjectType -> !inputObjectType.getName().startsWith(PREFIX_INTROSPECTION))
                                .map(this::buildType),
                        documentManager.getDocument().getEnums()
                                .filter(enumType -> !enumType.getName().startsWith(PREFIX_INTROSPECTION))
                                .map(this::buildType),
                        documentManager.getDocument().getScalarTypes()
                                .map(this::buildType)
                ).collect(Collectors.toCollection(LinkedHashSet::new))
        );

        documentManager.getDocument().getQueryOperationType()
                .map(this::buildType)
                .ifPresent(schema::setQueryType);

        documentManager.getDocument().getMutationOperationType()
                .map(this::buildType)
                .ifPresent(schema::setMutationType);

        documentManager.getDocument().getSubscriptionOperationType()
                .map(this::buildType)
                .ifPresent(schema::setSubscriptionType);

        schema.setDirectives(
                documentManager.getDocument().getDirectives()
                        .map(this::buildDirective)
                        .collect(Collectors.toCollection(LinkedHashSet::new))
        );

        Logger.info("introspection schema build success");
        return schema;
    }

    private __Type buildType(ObjectType objectType) {
        return buildType(objectType, 0);
    }

    private __Type buildType(InterfaceType interfaceType) {
        return buildType(interfaceType, 0);
    }

    private __Type buildType(ObjectType objectType, int level) {
        __Type __type = new __Type();
        __type.setKind(__TypeKind.OBJECT);
        __type.setName(objectType.getName());

        if (level == 0) {
            if (objectType.getInterfaces() != null) {
                __type.setInterfaces(
                        getInterfaces(objectType.getInterfaces(), level + 1)
                                .collect(Collectors.toCollection(LinkedHashSet::new))
                );
            } else {
                __type.setInterfaces(new LinkedHashSet<>());
            }

            if (objectType.getDescription() != null) {
                __type.setDescription(objectType.getDescription());
            }
            __type.setFields(
                    objectType.getFields().stream()
                            .filter(fieldDefinition -> !fieldDefinition.getName().startsWith(PREFIX_INTROSPECTION))
                            .map(fieldDefinition -> buildField(fieldDefinition, level + 1))
                            .collect(Collectors.toCollection(LinkedHashSet::new))
            );
        }
        return __type;
    }

    private __Type buildType(InterfaceType interfaceType, int level) {
        __Type __type = new __Type();
        __type.setKind(__TypeKind.INTERFACE);
        __type.setName(interfaceType.getName());

        if (level == 0) {
            if (interfaceType.getInterfaces() != null) {
                __type.setInterfaces(getInterfaces(interfaceType.getInterfaces(), level + 1).collect(Collectors.toCollection(LinkedHashSet::new)));
            } else {
                __type.setInterfaces(new LinkedHashSet<>());
            }

            if (interfaceType.getDescription() != null) {
                __type.setDescription(interfaceType.getDescription());
            }
            __type.setFields(
                    interfaceType.getFields().stream()
                            .filter(fieldDefinition -> !fieldDefinition.getName().startsWith(PREFIX_INTROSPECTION))
                            .map(fieldDefinition -> buildField(fieldDefinition, level + 1))
                            .collect(Collectors.toCollection(LinkedHashSet::new))
            );
        }
        return __type;
    }

    private Stream<__Type> getInterfaces(Collection<String> implementsInterfaceNames, int level) {
        return implementsInterfaceNames.stream()
                .flatMap(implementsInterfaceName -> documentManager.getDocument().getInterfaceType(implementsInterfaceName).stream())
                .map(interfaceType -> buildType(interfaceType, level));
    }

    private __Field buildField(FieldDefinition fieldDefinition, int level) {
        __Field __field = new __Field();
        __field.setName(fieldDefinition.getName());

        if (fieldDefinition.getDescription() != null) {
            __field.setDescription(fieldDefinition.getDescription());
        }

        if (fieldDefinition.getArguments() != null) {
            __field.setArgs(
                    fieldDefinition.getArguments().stream()
                            .filter(inputValue -> !inputValue.getName().startsWith(PREFIX_INTROSPECTION))
                            .filter(inputValue -> !documentManager.getInputValueTypeDefinition(inputValue).getName().startsWith(PREFIX_INTROSPECTION))
                            .map(this::buildInputValue)
                            .collect(Collectors.toCollection(LinkedHashSet::new))
            );
        } else {
            __field.setArgs(new LinkedHashSet<>());
        }
        __field.setType(buildType(fieldDefinition.getType(), level));
        return __field;
    }

    private __Type buildType(Type type) {
        return buildType(type, 0);
    }

    private __Type buildType(Type type, int level) {
        if (level > levelThreshold) {
            return null;
        }
        if (type.isList()) {
            __Type listType = new __Type();
            listType.setKind(__TypeKind.LIST);
            listType.setOfType(buildType(type.asListType().getType(), level));
            listType.setName("[" + listType.getOfType().getName() + "]");
            return listType;
        } else if (type.isNonNull()) {
            __Type nonNullType = new __Type();
            nonNullType.setKind(__TypeKind.NON_NULL);
            nonNullType.setOfType(buildType(type.asNonNullType().getType(), level));
            nonNullType.setName(nonNullType.getOfType().getName() + "!");
            return nonNullType;
        } else {
            Definition definition = documentManager.getDocument().getDefinition(type.asTypeName().getName());
            if (definition.isScalar()) {
                return buildType(definition.asScalar(), level);
            } else if (definition.isObject()) {
                return buildType(definition.asObject(), level);
            } else if (definition.isEnum()) {
                return buildType(definition.asEnum(), level);
            } else if (definition.isInputObject()) {
                return buildType(definition.asInputObject(), level);
            }
        }
        throw new GraphQLErrors(UNSUPPORTED_FIELD_TYPE.bind(type.toString()));
    }

    private __Type buildType(EnumType enumType) {
        return buildType(enumType, 0);
    }

    private __Type buildType(EnumType enumType, int level) {
        __Type __type = new __Type();
        __type.setKind(__TypeKind.ENUM);
        __type.setName(enumType.getName());

        if (level == 0) {
            if (enumType.getDescription() != null) {
                __type.setDescription(enumType.getDescription());
            }
            __type.setEnumValues(
                    enumType.getEnumValues().stream()
                            .map(this::buildEnumValue)
                            .collect(Collectors.toCollection(LinkedHashSet::new))
            );
        }
        return __type;
    }

    private __EnumValue buildEnumValue(EnumValueDefinition enumValueDefinition) {
        __EnumValue __enumValue = new __EnumValue();
        __enumValue.setName(enumValueDefinition.getName());

        if (enumValueDefinition.getDescription() != null) {
            __enumValue.setDescription(enumValueDefinition.getDescription());
        }
        return __enumValue;
    }

    private __InputValue buildInputValue(InputValue inputValue) {
        return buildInputValue(inputValue, 0);
    }

    private __InputValue buildInputValue(InputValue inputValue, int level) {
        __InputValue __inputValue = new __InputValue();
        __inputValue.setName(inputValue.getName());

        if (inputValue.getDescription() != null) {
            __inputValue.setDescription(inputValue.getDescription());
        }

        if (inputValue.getDefaultValue() != null) {
            if (!inputValue.getDefaultValue().isObject()) {
                __inputValue.setDefaultValue(inputValue.getDefaultValue().toString());
            }
        }
        __inputValue.setType(buildType(inputValue.getType(), level));
        return __inputValue;
    }

    private __Type buildType(InputObjectType inputObjectType) {
        return buildType(inputObjectType, 0);
    }

    private __Type buildType(InputObjectType inputObjectType, int level) {
        __Type __type = new __Type();
        __type.setKind(__TypeKind.INPUT_OBJECT);
        __type.setName(inputObjectType.getName());

        if (level == 0) {
            if (inputObjectType.getDescription() != null) {
                __type.setDescription(inputObjectType.getDescription());
            }
            __type.setInputFields(
                    inputObjectType.getInputValues().stream()
                            .filter(inputValue -> !inputValue.getName().startsWith(PREFIX_INTROSPECTION))
                            .filter(inputValue -> !documentManager.getInputValueTypeDefinition(inputValue).getName().startsWith(PREFIX_INTROSPECTION))
                            .map(inputValue -> buildInputValue(inputValue, level + 1))
                            .collect(Collectors.toCollection(LinkedHashSet::new))
            );
        }
        return __type;
    }

    private __Type buildType(ScalarType scalarType) {
        return buildType(scalarType, 0);
    }

    private __Type buildType(ScalarType scalarType, int level) {
        __Type __type = new __Type();
        __type.setKind(__TypeKind.SCALAR);
        __type.setName(scalarType.getName());

        if (level == 0) {
            if (scalarType.getDescription() != null) {
                __type.setDescription(scalarType.getDescription());
            }
        }
        return __type;
    }

    private __Type buildNonNullType(__Type __type) {
        __Type nonNullType = new __Type();
        nonNullType.setName(__type.getName() + "!");
        nonNullType.setOfType(__type);
        nonNullType.setKind(__TypeKind.NON_NULL);
        return nonNullType;
    }

    private __Type buildListType(__Type __type) {
        __Type listType = new __Type();
        listType.setName("[" + __type.getName() + "]");
        listType.setOfType(__type);
        listType.setKind(__TypeKind.LIST);
        return listType;
    }

    private __Type buildNonNullListType(__Type __type) {
        return buildNonNullType(buildListType(__type));
    }

    private __Type buildListNonNullType(__Type __type) {
        return buildListType(buildNonNullType(__type));
    }

    private __Type buildNonNullListNonNullType(__Type __type) {
        return buildNonNullType(buildListNonNullType(__type));
    }

    private __Directive buildDirective(DirectiveDefinition directiveDefinition) {
        __Directive __directive = new __Directive();
        __directive.setName(directiveDefinition.getName());

        if (directiveDefinition.getDescription() != null) {
            __directive.setDescription(directiveDefinition.getDescription());
        }

        if (directiveDefinition.getArguments() != null) {
            __directive.setArgs(
                    directiveDefinition.getArguments().stream()
                            .map(this::buildInputValue)
                            .collect(Collectors.toCollection(LinkedHashSet::new))
            );
        } else {
            __directive.setArgs(new LinkedHashSet<>());
        }

        __directive.setLocations(buildDirectiveLocationList(directiveDefinition.getDirectiveLocations()));
        return __directive;
    }

    public Set<__DirectiveLocation> buildDirectiveLocationList(Collection<String> directiveLocationNames) {
        return Stream.ofNullable(directiveLocationNames)
                .flatMap(Collection::stream)
                .map(__DirectiveLocation::valueOf)
                .collect(Collectors.toSet());
    }
}
