package io.graphoenix.java.builder;

import com.dslplatform.json.CompiledJson;
import com.google.common.base.CaseFormat;
import com.google.common.collect.Streams;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.*;
import io.graphoenix.core.handler.DocumentManager;
import io.graphoenix.core.handler.PackageManager;
import io.graphoenix.java.config.GeneratorConfig;
import io.graphoenix.spi.error.GraphQLErrors;
import io.graphoenix.spi.graphql.AbstractDefinition;
import io.graphoenix.spi.graphql.Definition;
import io.graphoenix.spi.graphql.type.*;
import jakarta.annotation.Generated;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.graphql.Enum;
import org.eclipse.microprofile.graphql.Type;
import org.eclipse.microprofile.graphql.*;
import org.tinylog.Logger;

import javax.lang.model.SourceVersion;
import javax.lang.model.element.Modifier;
import java.lang.annotation.*;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static com.squareup.javapoet.TypeName.*;
import static io.graphoenix.java.utils.NameUtil.getFieldGetterMethodName;
import static io.graphoenix.java.utils.NameUtil.getFieldSetterMethodName;
import static io.graphoenix.java.utils.TypeNameUtil.toClassName;
import static io.graphoenix.spi.constant.Hammurabi.*;
import static io.graphoenix.spi.error.GraphQLErrorType.*;
import static io.graphoenix.spi.utils.StreamUtil.distinctByKey;

@ApplicationScoped
public class TypeSpecBuilder {

    private final DocumentManager documentManager;
    private final PackageManager packageManager;
    private final GeneratorConfig generatorConfig;

    @Inject
    public TypeSpecBuilder(DocumentManager documentManager, PackageManager packageManager, GeneratorConfig generatorConfig) {
        this.documentManager = documentManager;
        this.packageManager = packageManager;
        this.generatorConfig = generatorConfig;
    }

    private AnnotationSpec getGeneratedAnnotationSpec() {
        return AnnotationSpec.builder(Generated.class)
                .addMember("value", "$S", getClass().getName())
                .build();
    }

    public TypeSpec buildType(ObjectType objectType) {
        TypeSpec.Builder builder = TypeSpec.classBuilder(objectType.getName())
                .addModifiers(Modifier.PUBLIC)
                .addAnnotation(Type.class)
                .addAnnotation(CompiledJson.class)
                .addAnnotation(getGeneratedAnnotationSpec());

        List<FieldSpec> fieldSpecs = objectType.getFields().stream()
                .map(this::buildField)
                .collect(Collectors.toList());

        List<MethodSpec> methodSpecs = fieldSpecs.stream()
                .flatMap(fieldSpec ->
                        Stream
                                .concat(
                                        Stream.of(buildGetter(fieldSpec, objectType.getInterfaces())),
                                        buildSetterList(fieldSpec, objectType.getInterfaces()).stream()
                                )
                )
                .collect(Collectors.toList());

        builder
                .addFields(fieldSpecs)
                .addMethods(methodSpecs);

        if (objectType.getInterfaces() != null && !objectType.getInterfaces().isEmpty()) {
            builder.addSuperinterfaces(
                    objectType.getInterfaces().stream()
                            .map(name -> documentManager.getDocument().getInterfaceTypeOrError(name))
                            .map(interfaceType -> toClassName(interfaceType.getClassNameOrError()))
                            .collect(Collectors.toList())
            );
        }
        if (objectType.getDescription() != null) {
            builder
                    .addJavadoc("$L", objectType.getDescription())
                    .addAnnotation(
                            AnnotationSpec.builder(Description.class)
                                    .addMember("value", "$S", objectType.getDescription())
                                    .build()
                    );
        }
        Logger.info("class {} build success", objectType.getName());
        return builder.build();
    }

    public TypeSpec buildType(InputObjectType inputObjectType) {
        TypeSpec.Builder builder;
        if (inputObjectType.isInputInterface()) {
            builder = TypeSpec.interfaceBuilder(inputObjectType.getName());
            List<FieldSpec> fieldSpecs = inputObjectType.getInputValues().stream()
                    .map(this::buildInterfaceField)
                    .collect(Collectors.toList());
            List<MethodSpec> methodSpecs = fieldSpecs.stream()
                    .flatMap(fieldSpec ->
                            Stream.of(
                                    buildInterfaceGetter(fieldSpec),
                                    buildInputObjectInterfaceSetter(fieldSpec, inputObjectType.getInterfaces())
                            )
                    )
                    .collect(Collectors.toList());
            builder
                    .addFields(fieldSpecs)
                    .addMethods(methodSpecs);
        } else {
            builder = TypeSpec.classBuilder(inputObjectType.getName())
                    .addAnnotation(CompiledJson.class);
            List<FieldSpec> fieldSpecs = inputObjectType.getInputValues().stream()
                    .map(this::buildField)
                    .collect(Collectors.toList());
            List<MethodSpec> methodSpecs = fieldSpecs.stream()
                    .flatMap(fieldSpec ->
                            Stream
                                    .concat(
                                            Stream.of(buildInputValueGetter(fieldSpec, inputObjectType.getInterfaces())),
                                            buildInputValueSetterList(fieldSpec, inputObjectType.getInterfaces()).stream()
                                    )
                    )
                    .collect(Collectors.toList());
            builder
                    .addFields(fieldSpecs)
                    .addMethods(methodSpecs);
        }
        builder.addModifiers(Modifier.PUBLIC)
                .addAnnotation(Input.class)
                .addAnnotation(getGeneratedAnnotationSpec());

        if (inputObjectType.getInterfaces() != null && !inputObjectType.getInterfaces().isEmpty()) {
            builder.addSuperinterfaces(
                    inputObjectType.getInterfaces().stream()
                            .map(name -> documentManager.getDocument().getInputObjectTypeOrError(name))
                            .map(inputInterfaceType -> toClassName(inputInterfaceType.getClassNameOrError()))
                            .collect(Collectors.toList())
            );
        }
        if (inputObjectType.getDescription() != null) {
            builder
                    .addJavadoc("$L", inputObjectType.getDescription())
                    .addAnnotation(
                            AnnotationSpec.builder(Description.class)
                                    .addMember("value", "$S", inputObjectType.getDescription())
                                    .build()
                    );
        }
        Logger.info("input class {} build success", inputObjectType.getName());
        return builder.build();
    }

    public Stream<TypeSpec> buildOperationTypeAnnotations() {
        return Streams.concat(
                documentManager.getDocument().getQueryOperationType().map(this::buildOperationTypeAnnotation).stream(),
                documentManager.getDocument().getMutationOperationType().map(this::buildOperationTypeAnnotation).stream(),
                documentManager.getDocument().getSubscriptionOperationType().map(this::buildOperationTypeAnnotation).stream()
        );
    }

    public TypeSpec buildOperationTypeAnnotation(ObjectType objectType) {
        TypeSpec.Builder builder = TypeSpec.annotationBuilder(objectType.getName())
                .addModifiers(Modifier.PUBLIC)
                .addAnnotation(getGeneratedAnnotationSpec());

        List<FieldDefinition> fieldDefinitions = objectType.getFields().stream()
                .filter(packageManager::isLocalPackage)
                .filter(fieldDefinition -> !fieldDefinition.isInvokeField())
                .collect(Collectors.toList());

        if (!fieldDefinitions.isEmpty()) {
            builder.addMethods(
                    fieldDefinitions.stream()
                            .map(fieldDefinition -> {
                                        InputObjectType argumentInput = getArgumentInput(objectType, fieldDefinition);
                                        return MethodSpec.methodBuilder(fieldDefinition.getName())
                                                .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                                                .returns(toClassName(argumentInput.getAnnotationNameOrError()))
                                                .defaultValue(CodeBlock.of("@$T", toClassName(argumentInput.getAnnotationNameOrError())))
                                                .build();
                                    }
                            )
                            .collect(Collectors.toList())
            );
        }
        builder
                .addAnnotation(AnnotationSpec.builder(Documented.class).build())
                .addAnnotation(
                        AnnotationSpec.builder(Retention.class)
                                .addMember("value", "$T.$L", RetentionPolicy.class, RetentionPolicy.SOURCE)
                                .build()
                )
                .addAnnotation(
                        AnnotationSpec.builder(Target.class)
                                .addMember("value", "$T.$L", ElementType.class, ElementType.METHOD)
                                .build()
                );

        if (objectType.getDescription() != null) {
            builder
                    .addJavadoc("$L", objectType.getDescription())
                    .addAnnotation(
                            AnnotationSpec.builder(Description.class)
                                    .addMember("value", "$S", objectType.getDescription())
                                    .build()
                    );
        }
        Logger.info("operation type annotation {} build success", objectType.getName());
        return builder.build();
    }

    public InputObjectType getArgumentInput(ObjectType objectType, FieldDefinition fieldDefinition) {
        ObjectType fieldType = documentManager.getFieldTypeDefinition(fieldDefinition).asObject();
        if (fieldDefinition.getType().hasList()) {
            return documentManager.getDocument().getInputObjectTypeOrError(fieldType.getName() + SUFFIX_LIST + objectType.getName() + SUFFIX_ARGUMENTS);
        } else {
            return documentManager.getDocument().getInputObjectTypeOrError(fieldType.getName() + objectType.getName() + SUFFIX_ARGUMENTS);
        }
    }

    public Stream<TypeSpec> buildAnnotations(InputObjectType inputObjectType) {
        if (inputObjectType.getName().endsWith(SUFFIX_ARGUMENTS)) {
            return Stream.of(buildAnnotation(inputObjectType, 0));
        } else {
            return IntStream.range(0, generatorConfig.getAnnotationLevel())
                    .mapToObj(level -> buildAnnotation(inputObjectType, level));
        }
    }

    public TypeSpec buildAnnotation(InputObjectType inputObjectType, int level) {
        TypeSpec.Builder builder = TypeSpec.annotationBuilder(inputObjectType.getName() + (level == 0 ? "" : "" + level))
                .addModifiers(Modifier.PUBLIC)
                .addAnnotation(getGeneratedAnnotationSpec())
                .addAnnotation(AnnotationSpec.builder(Documented.class).build())
                .addAnnotation(
                        AnnotationSpec.builder(Retention.class)
                                .addMember("value", "$T.$L", RetentionPolicy.class, RetentionPolicy.SOURCE)
                                .build()
                )
                .addAnnotation(
                        AnnotationSpec.builder(Target.class)
                                .addMember("value", "$T.$L", ElementType.class, ElementType.METHOD)
                                .build()
                );

        List<MethodSpec> methodSpecs = inputObjectType.getInputValues().stream()
                .filter(inputValue -> documentManager.getInputValueTypeDefinition(inputValue).isLeaf() || level < generatorConfig.getAnnotationLevel() - 1)
                .map(inputValue -> buildAnnotationMethod(inputValue, level))
                .collect(Collectors.toList());

        builder.addMethods(methodSpecs);

        List<MethodSpec> variableMethodSpecs = inputObjectType.getInputValues().stream()
                .filter(inputValue -> documentManager.getInputValueTypeDefinition(inputValue).isLeaf() || level < generatorConfig.getAnnotationLevel() - 1)
                .map(inputValue ->
                        MethodSpec.methodBuilder("$" + inputValue.getName())
                                .addModifiers(Modifier.ABSTRACT, Modifier.PUBLIC)
                                .returns(String.class)
                                .defaultValue("$S", "")
                                .build()
                )
                .collect(Collectors.toList());

        builder.addMethods(variableMethodSpecs);

        if (inputObjectType.getDescription() != null) {
            builder
                    .addJavadoc("$L", inputObjectType.getDescription())
                    .addAnnotation(
                            AnnotationSpec.builder(Description.class)
                                    .addMember("value", "$S", inputObjectType.getDescription())
                                    .build()
                    );
        }
        Logger.info("annotation class {} build success", inputObjectType.getName());
        return builder.build();
    }

    public TypeSpec buildType(EnumType enumType) {
        TypeSpec.Builder builder = TypeSpec.enumBuilder(enumType.getName())
                .addModifiers(Modifier.PUBLIC)
                .addAnnotation(Enum.class)
                .addAnnotation(getGeneratedAnnotationSpec());

        enumType.getEnumValues().stream()
                .map(AbstractDefinition::getName)
                .forEach(builder::addEnumConstant);

        if (enumType.getDescription() != null) {
            builder
                    .addJavadoc("$L", enumType.getDescription())
                    .addAnnotation(
                            AnnotationSpec.builder(Description.class)
                                    .addMember("value", "$S", enumType.getDescription())
                                    .build()
                    );
        }
        Logger.info("enum {} build success", enumType.getName());
        return builder.build();
    }

    public TypeSpec buildType(InterfaceType interfaceType) {
        TypeSpec.Builder builder = TypeSpec.interfaceBuilder(interfaceType.getName())
                .addModifiers(Modifier.PUBLIC)
                .addAnnotation(Interface.class)
//                .addAnnotation(CompiledJson.class)
                .addAnnotation(getGeneratedAnnotationSpec());

        List<FieldSpec> fieldSpecs = interfaceType.getFields().stream()
                .map(this::buildInterfaceField)
                .collect(Collectors.toList());

        List<MethodSpec> methodSpecs = fieldSpecs.stream()
                .flatMap(fieldSpec ->
                        Stream.of(
                                buildInterfaceGetter(fieldSpec),
                                buildInterfaceSetter(fieldSpec, interfaceType.getInterfaces())
                        )
                )
                .collect(Collectors.toList());

        builder
                .addFields(fieldSpecs)
                .addMethods(methodSpecs);

        if (interfaceType.getInterfaces() != null && !interfaceType.getInterfaces().isEmpty()) {
            builder.addSuperinterfaces(
                    interfaceType.getInterfaces().stream()
                            .map(name -> documentManager.getDocument().getInterfaceTypeOrError(name))
                            .map(implementInterfaceType -> toClassName(implementInterfaceType.getClassNameOrError()))
                            .collect(Collectors.toList())
            );
        }
        if (interfaceType.getDescription() != null) {
            builder
                    .addJavadoc("$L", interfaceType.getDescription())
                    .addAnnotation(
                            AnnotationSpec.builder(Description.class)
                                    .addMember("value", "$S", interfaceType.getDescription())
                                    .build()
                    );
        }
        Logger.info("interface {} build success", interfaceType.getName());
        return builder.build();
    }

    public TypeSpec buildType(DirectiveDefinition directiveDefinition) {
        TypeSpec.Builder builder = TypeSpec.annotationBuilder(CaseFormat.LOWER_CAMEL.to(CaseFormat.UPPER_CAMEL, directiveDefinition.getName()))
                .addModifiers(Modifier.PUBLIC)
                .addAnnotation(getGeneratedAnnotationSpec());

        if (directiveDefinition.getArguments() != null && !directiveDefinition.getArguments().isEmpty()) {
            builder.addMethods(
                    directiveDefinition.getArguments().stream()
                            .map(inputValue -> buildAnnotationMethod(inputValue, -1))
                            .collect(Collectors.toList())
            );
        }
        builder
                .addAnnotation(AnnotationSpec.builder(Documented.class).build())
                .addAnnotation(
                        AnnotationSpec.builder(Retention.class)
                                .addMember("value", "$T.$L", RetentionPolicy.class, RetentionPolicy.SOURCE)
                                .build()
                )
                .addAnnotation(
                        AnnotationSpec.builder(io.graphoenix.spi.annotation.Directive.class)
                                .addMember("value", "$S", directiveDefinition.getName())
                                .build()
                );

        CodeBlock.Builder codeBuilder = CodeBlock.builder()
                .add("{")
                .add(
                        CodeBlock.join(
                                directiveDefinition.getDirectiveLocations().stream()
                                        .map(this::buildElementType)
                                        .distinct()
                                        .map(elementType -> CodeBlock.of("$T.$L", ElementType.class, elementType))
                                        .collect(Collectors.toList()),
                                ","
                        )
                )
                .add("}");
        builder.addAnnotation(
                AnnotationSpec.builder(Target.class)
                        .addMember("value", codeBuilder.build())
                        .build()
        );
        if (directiveDefinition.getDescription() != null) {
            builder
                    .addJavadoc("$L", directiveDefinition.getDescription())
                    .addAnnotation(
                            AnnotationSpec.builder(Description.class)
                                    .addMember("value", "$S", directiveDefinition.getDescription())
                                    .build()
                    );
        }
        Logger.info("directive annotation {} build success", directiveDefinition.getName());
        return builder.build();
    }

    public ElementType buildElementType(String locationName) {
        switch (locationName) {
            case "QUERY":
            case "MUTATION":
            case "SUBSCRIPTION":
                return ElementType.METHOD;
            case "FIELD":
            case "FIELD_DEFINITION":
            case "ENUM_VALUE":
            case "INPUT_FIELD_DEFINITION":
                return ElementType.FIELD;
            case "SCHEMA":
            case "OBJECT":
            case "INTERFACE":
            case "ENUM":
            case "UNION":
            case "INPUT_OBJECT":
            case "FRAGMENT_DEFINITION":
                return ElementType.TYPE;
            case "SCALAR":
            case "FRAGMENT_SPREAD":
            case "INLINE_FRAGMENT":
                return ElementType.TYPE_USE;
            case "ARGUMENT_DEFINITION":
                return ElementType.PARAMETER;
            default:
                throw new GraphQLErrors(UNSUPPORTED_LOCATION_NAME.bind(locationName));
        }
    }

    public FieldSpec buildField(FieldDefinition fieldDefinition) {
        boolean isKeyword = SourceVersion.isKeyword(fieldDefinition.getName());
        FieldSpec.Builder builder = FieldSpec.builder(buildType(fieldDefinition.getType()), isKeyword ? "_" + fieldDefinition.getName() : fieldDefinition.getName(), Modifier.PRIVATE);
        if (isKeyword) {
            builder.addAnnotation(
                    AnnotationSpec
                            .builder(Name.class)
                            .addMember("value", "$S", fieldDefinition.getName())
                            .build()
            );
        }
        if (fieldDefinition.getType().getTypeName().getName().equals(SCALA_ID_NAME)) {
            builder.addAnnotation(Id.class);
        }
        if (fieldDefinition.getType().isNonNull()) {
            builder.addAnnotation(NonNull.class);
        }
        if (fieldDefinition.getDescription() != null) {
            builder
                    .addJavadoc("$L", fieldDefinition.getDescription())
                    .addAnnotation(
                            AnnotationSpec.builder(Description.class)
                                    .addMember("value", "$S", fieldDefinition.getDescription())
                                    .build()
                    );
        }
        Logger.info("class field {} build success", fieldDefinition.getName());
        return builder.build();
    }

    public FieldSpec buildInterfaceField(FieldDefinition fieldDefinition) {
        boolean isKeyword = SourceVersion.isKeyword(fieldDefinition.getName());
        FieldSpec.Builder builder = FieldSpec.builder(buildType(fieldDefinition.getType()), isKeyword ? "_" + fieldDefinition.getName() : fieldDefinition.getName(), Modifier.STATIC, Modifier.FINAL, Modifier.PUBLIC);
        if (isKeyword) {
            builder.addAnnotation(
                    AnnotationSpec
                            .builder(Name.class)
                            .addMember("value", "$S", fieldDefinition.getName())
                            .build()
            );
        }
        builder.initializer("$L", "null");
        if (fieldDefinition.getType().isNonNull()) {
            builder.addAnnotation(NonNull.class);
        }
        if (fieldDefinition.getDescription() != null) {
            builder
                    .addJavadoc("$L", fieldDefinition.getDescription())
                    .addAnnotation(
                            AnnotationSpec.builder(Description.class)
                                    .addMember("value", "$S", fieldDefinition.getDescription())
                                    .build()
                    );
        }
        Logger.info("class field {} build success", fieldDefinition.getName());
        return builder.build();
    }

    public FieldSpec buildField(InputValue inputValue) {
        boolean isKeyword = SourceVersion.isKeyword(inputValue.getName());
        FieldSpec.Builder builder = FieldSpec.builder(buildType(inputValue.getType()), isKeyword ? "_" + inputValue.getName() : inputValue.getName(), Modifier.PRIVATE);
        if (isKeyword) {
            builder.addAnnotation(
                    AnnotationSpec.builder(Name.class)
                            .addMember("value", "$S", inputValue.getName())
                            .build()
            );
        }
        if (inputValue.getDefaultValue() != null) {
            builder.addAnnotation(
                    AnnotationSpec.builder(DefaultValue.class)
                            .addMember("value", "$S", inputValue.getDefaultValue())
                            .build()
            );
        }
        if (inputValue.getType().isNonNull()) {
            builder.addAnnotation(NonNull.class);
        }
        if (inputValue.getDescription() != null) {
            builder
                    .addJavadoc("$L", inputValue.getDescription())
                    .addAnnotation(
                            AnnotationSpec.builder(Description.class)
                                    .addMember("value", "$S", inputValue.getDescription())
                                    .build()
                    );
        }
        Logger.info("input class field {} build success", inputValue.getName());
        return builder.build();
    }

    public FieldSpec buildInterfaceField(InputValue inputValue) {
        boolean isKeyword = SourceVersion.isKeyword(inputValue.getName());
        FieldSpec.Builder builder = FieldSpec.builder(buildType(inputValue.getType()), isKeyword ? "_" + inputValue.getName() : inputValue.getName(), Modifier.STATIC, Modifier.FINAL, Modifier.PUBLIC);
        if (isKeyword) {
            builder.addAnnotation(
                    AnnotationSpec.builder(Name.class)
                            .addMember("value", "$S", inputValue.getName())
                            .build()
            );
        }
        builder.initializer("$L", "null");
        if (inputValue.getType().isNonNull()) {
            builder.addAnnotation(NonNull.class);
        }
        if (inputValue.getDescription() != null) {
            builder
                    .addJavadoc("$L", inputValue.getDescription())
                    .addAnnotation(
                            AnnotationSpec.builder(Description.class)
                                    .addMember("value", "$S", inputValue.getDescription())
                                    .build()
                    );
        }
        Logger.info("interface field {}.{} build success", inputValue.getName());
        return builder.build();
    }

    public MethodSpec buildAnnotationMethod(InputValue inputValue, int level) {
        boolean isKeyword = SourceVersion.isKeyword(inputValue.getName());
        MethodSpec.Builder builder = MethodSpec.methodBuilder(isKeyword ? "_" + inputValue.getName() : inputValue.getName())
                .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                .returns(buildAnnotationType(inputValue.getType(), level));
        if (isKeyword) {
            builder.addAnnotation(
                    AnnotationSpec.builder(Name.class)
                            .addMember("value", "$S", inputValue.getName())
                            .build()
            );
        }
        if (inputValue.getDefaultValue() != null) {
            builder.defaultValue(buildDefaultValue(inputValue, inputValue.getDefaultValue().toString()));
        } else {
            builder.defaultValue(buildAnnotationDefaultValue(inputValue, level));
        }
        if (inputValue.getDescription() != null) {
            builder
                    .addJavadoc("$L", inputValue.getDescription())
                    .addAnnotation(
                            AnnotationSpec.builder(Description.class)
                                    .addMember("value", "$S", inputValue.getDescription())
                                    .build()
                    );
        }
        Logger.info("input annotation field {}.{} build success", inputValue.getName());
        return builder.build();
    }

    private CodeBlock buildDefaultValue(InputValue inputValue, String defaultValue) {
        Definition inputValueTypeDefinition = documentManager.getInputValueTypeDefinition(inputValue);
        if (inputValueTypeDefinition.isScalar()) {
            return CodeBlock.of("$L", defaultValue);
        } else if (inputValueTypeDefinition.isEnum()) {
            return CodeBlock.of(
                    "$T.$L",
                    toClassName(inputValueTypeDefinition.getClassNameOrError()),
                    defaultValue
            );
        } else {
            throw new GraphQLErrors(UNSUPPORTED_DEFAULT_VALUE.bind(defaultValue));
        }
    }

    public CodeBlock buildAnnotationDefaultValue(InputValue inputValue, int level) {
        if (inputValue.getType().hasList()) {
            return CodeBlock.of("$L", "{}");
        }
        Definition inputValueTypeDefinition = documentManager.getInputValueTypeDefinition(inputValue);
        if (inputValueTypeDefinition.isScalar()) {
            return buildAnnotationDefaultValue(inputValueTypeDefinition.asScalar());
        } else if (inputValueTypeDefinition.isEnum()) {
            EnumType enumType = inputValueTypeDefinition.asEnum();
            return CodeBlock.of(
                    "$T.$L",
                    toClassName(enumType.getClassNameOrError()),
                    new ArrayList<>(enumType.getEnumValues()).get(0).getName()
            );
        } else if (inputValueTypeDefinition.isInputObject()) {
            return CodeBlock.of(
                    "@$T",
                    toClassName(inputValueTypeDefinition.asInputObject().getAnnotationNameOrError() + ((level + 1) == 0 ? "" : "" + (level + 1)))
            );
        }
        throw new GraphQLErrors(UNSUPPORTED_FIELD_TYPE.bind(inputValue.getType().getTypeName()));
    }

    public CodeBlock buildAnnotationDefaultValue(ScalarType scalarType) {
        switch (scalarType.getName()) {
            case SCALA_ID_NAME:
            case SCALA_STRING_NAME:
            case SCALA_DATE_NAME:
            case SCALA_TIME_NAME:
            case SCALA_DATE_TIME_NAME:
            case SCALA_TIMESTAMP_NAME:
                return CodeBlock.of("$S", "");
            case SCALA_BOOLEAN_NAME:
                return CodeBlock.of("$L", false);
            case SCALA_INT_NAME:
            case SCALA_BIG_INTEGER_NAME:
            case SCALA_FLOAT_NAME:
            case SCALA_BIG_DECIMAL_NAME:
                return CodeBlock.of("$L", 0);
        }
        throw new GraphQLErrors(UNSUPPORTED_FIELD_TYPE.bind(scalarType.getName()));
    }

    public TypeName buildType(io.graphoenix.spi.graphql.type.Type type) {
        if (type.isList()) {
            Definition typeDefinition = documentManager.getDocument().getDefinition(type.getTypeName().getName());
            if (typeDefinition.isInterface() || typeDefinition.isInputObject() && typeDefinition.asInputObject().isInputInterface()) {
                return ParameterizedTypeName.get(ClassName.get(Collection.class), WildcardTypeName.subtypeOf(buildType(type.asListType().getType())));
            }
            return ParameterizedTypeName.get(ClassName.get(Collection.class), buildType(type.asListType().getType()));
        } else if (type.isNonNull()) {
            return buildType(type.asNonNullType().getType());
        } else {
            return buildType(type.asTypeName());
        }
    }

    public TypeName buildAnnotationType(io.graphoenix.spi.graphql.type.Type type, int level) {
        if (type.isList()) {
            return ArrayTypeName.of(buildAnnotationType(type.asListType().getType(), level));
        } else if (type.isNonNull()) {
            return buildAnnotationType(type.asNonNullType().getType(), level);
        } else {
            return buildAnnotationType(type.asTypeName(), level);
        }
    }

    public TypeName buildType(io.graphoenix.spi.graphql.type.TypeName typeName) {
        Definition typeDefinition = documentManager.getDocument().getDefinition(typeName.getName());
        if (typeDefinition.isScalar()) {
            return buildType(typeDefinition.asScalar());
        } else {
            return toClassName(typeDefinition.getClassNameOrError());
        }
    }

    public TypeName buildAnnotationType(io.graphoenix.spi.graphql.type.TypeName typeName, int level) {
        Definition typeDefinition = documentManager.getDocument().getDefinition(typeName.getName());
        if (typeDefinition.isScalar()) {
            return buildAnnotationType(typeDefinition.asScalar());
        } else if (typeDefinition.isEnum()) {
            return toClassName(typeDefinition.getClassNameOrError());
        } else {
            return toClassName(typeDefinition.getAnnotationNameOrError() + ((level + 1) == 0 ? "" : "" + (level + 1)));
        }
    }

    public TypeName buildType(ScalarType scalarType) {
        switch (scalarType.getName()) {
            case SCALA_ID_NAME:
            case SCALA_STRING_NAME:
                return TypeName.get(String.class);
            case SCALA_BOOLEAN_NAME:
                return TypeName.get(Boolean.class);
            case SCALA_INT_NAME:
                return TypeName.get(Integer.class);
            case SCALA_FLOAT_NAME:
                return TypeName.get(Float.class);
            case SCALA_BIG_INTEGER_NAME:
                return TypeName.get(BigInteger.class);
            case SCALA_BIG_DECIMAL_NAME:
                return TypeName.get(BigDecimal.class);
            case SCALA_DATE_NAME:
                return TypeName.get(LocalDate.class);
            case SCALA_TIME_NAME:
                return TypeName.get(LocalTime.class);
            case SCALA_DATE_TIME_NAME:
            case SCALA_TIMESTAMP_NAME:
                return TypeName.get(LocalDateTime.class);
            default:
                throw new GraphQLErrors(UNSUPPORTED_FIELD_TYPE.bind(scalarType.getName()));
        }
    }

    public TypeName buildAnnotationType(ScalarType scalarType) {
        switch (scalarType.getName()) {
            case SCALA_ID_NAME:
            case SCALA_STRING_NAME:
            case SCALA_DATE_NAME:
            case SCALA_TIME_NAME:
            case SCALA_DATE_TIME_NAME:
            case SCALA_TIMESTAMP_NAME:
                return TypeName.get(String.class);
            case SCALA_BOOLEAN_NAME:
                return BOOLEAN;
            case SCALA_INT_NAME:
            case SCALA_BIG_INTEGER_NAME:
                return INT;
            case SCALA_FLOAT_NAME:
            case SCALA_BIG_DECIMAL_NAME:
                return FLOAT;
            default:
                throw new GraphQLErrors(UNSUPPORTED_FIELD_TYPE.bind(scalarType.getName()));
        }
    }

    private List<MethodSpec> buildSetterList(FieldSpec fieldSpec, Collection<String> implementsInterfaces) {
        List<TypeName> typeNames = getFieldTypeStream(fieldSpec.name, implementsInterfaces)
                .map(this::buildType)
                .collect(Collectors.toList());

        List<MethodSpec> methodSpecList = typeNames.stream()
                .map(typeName -> {
                            String setterName = getFieldSetterMethodName(fieldSpec.name);
                            MethodSpec.Builder methodBuilder = MethodSpec.methodBuilder(setterName).addModifiers(Modifier.PUBLIC);
                            methodBuilder.addAnnotation(Override.class);
                            methodBuilder.addParameter(typeName, fieldSpec.name);
                            methodBuilder.addStatement("this." + fieldSpec.name + " = ($T)" + fieldSpec.name, fieldSpec.type);
                            return methodBuilder.build();
                        }
                )
                .collect(Collectors.toList());

        if (typeNames.stream().noneMatch(typeName -> typeName.toString().equals(fieldSpec.type.toString()))) {
            String setterName = getFieldSetterMethodName(fieldSpec.name);
            MethodSpec.Builder methodBuilder = MethodSpec.methodBuilder(setterName).addModifiers(Modifier.PUBLIC);
            methodBuilder.addParameter(fieldSpec.type, fieldSpec.name);
            methodBuilder.addStatement("this." + fieldSpec.name + " = " + fieldSpec.name);
            methodSpecList.add(methodBuilder.build());

        }
        return methodSpecList;
    }

    private MethodSpec buildGetter(FieldSpec fieldSpec, Collection<String> implementsInterfaces) {
        String getterName = getFieldGetterMethodName(fieldSpec.name);
        MethodSpec.Builder methodBuilder = MethodSpec.methodBuilder(getterName).returns(fieldSpec.type).addModifiers(Modifier.PUBLIC);
        methodBuilder.addStatement("return this." + fieldSpec.name);

        if (Stream.ofNullable(implementsInterfaces)
                .flatMap(Collection::stream)
                .map(name -> documentManager.getDocument().getInterfaceTypeOrError(name))
                .flatMap(interfaceType -> Stream.ofNullable(interfaceType.getFields()).flatMap(Collection::stream))
                .anyMatch(fieldDefinition -> fieldSpec.name.equals(fieldDefinition.getName()))
        ) {
            methodBuilder.addAnnotation(Override.class);
        }
        return methodBuilder.build();
    }

    private List<MethodSpec> buildInputValueSetterList(FieldSpec fieldSpec, Collection<String> implementsInterfaces) {
        List<TypeName> typeNames = getInputValueTypeStream(fieldSpec.name, implementsInterfaces)
                .map(this::buildType)
                .collect(Collectors.toList());

        List<MethodSpec> methodSpecList = typeNames.stream()
                .map(typeName -> {
                            String setterName = getFieldSetterMethodName(fieldSpec.name);
                            MethodSpec.Builder methodBuilder = MethodSpec.methodBuilder(setterName).addModifiers(Modifier.PUBLIC);
                            methodBuilder.addAnnotation(Override.class);
                            methodBuilder.addParameter(typeName, fieldSpec.name);
                            methodBuilder.addStatement("this." + fieldSpec.name + " = ($T)" + fieldSpec.name, fieldSpec.type);
                            return methodBuilder.build();
                        }
                )
                .collect(Collectors.toList());

        if (typeNames.stream().noneMatch(typeName -> typeName.toString().equals(fieldSpec.type.toString()))) {
            String setterName = getFieldSetterMethodName(fieldSpec.name);
            MethodSpec.Builder methodBuilder = MethodSpec.methodBuilder(setterName).addModifiers(Modifier.PUBLIC);
            methodBuilder.addParameter(fieldSpec.type, fieldSpec.name);
            methodBuilder.addStatement("this." + fieldSpec.name + " = " + fieldSpec.name);
            methodSpecList.add(methodBuilder.build());
        }
        return methodSpecList;
    }

    private MethodSpec buildInputValueGetter(FieldSpec fieldSpec, Collection<String> implementsInterfaces) {
        String getterName = getFieldGetterMethodName(fieldSpec.name);
        MethodSpec.Builder methodBuilder = MethodSpec.methodBuilder(getterName).returns(fieldSpec.type).addModifiers(Modifier.PUBLIC);
        methodBuilder.addStatement("return this." + fieldSpec.name);

        if (Stream.ofNullable(implementsInterfaces)
                .flatMap(Collection::stream)
                .map(name -> documentManager.getDocument().getInputObjectTypeOrError(name))
                .flatMap(inputObjectType -> Stream.ofNullable(inputObjectType.getInputValues()).flatMap(Collection::stream))
                .anyMatch(inputValue -> fieldSpec.name.equals(inputValue.getName()))
        ) {
            methodBuilder.addAnnotation(Override.class);
        }
        return methodBuilder.build();
    }

    private MethodSpec buildInterfaceSetter(FieldSpec fieldSpec, Collection<String> implementsInterfaces) {
        String setterName = getFieldSetterMethodName(fieldSpec.name);
        MethodSpec.Builder methodBuilder = MethodSpec.methodBuilder(setterName)
                .addModifiers(Modifier.ABSTRACT, Modifier.PUBLIC)
                .addParameter(fieldSpec.type, fieldSpec.name);
        return methodBuilder.build();
    }

    private MethodSpec buildInputObjectInterfaceSetter(FieldSpec fieldSpec, Collection<String> implementsInterfaces) {
        String setterName = getFieldSetterMethodName(fieldSpec.name);
        MethodSpec.Builder methodBuilder = MethodSpec.methodBuilder(setterName)
                .addModifiers(Modifier.ABSTRACT, Modifier.PUBLIC)
                .addParameter(fieldSpec.type, fieldSpec.name);
        return methodBuilder.build();
    }

    public MethodSpec buildInterfaceGetter(FieldSpec fieldSpec) {
        String getterName = getFieldGetterMethodName(fieldSpec.name);
        return MethodSpec.methodBuilder(getterName)
                .returns(fieldSpec.type)
                .addModifiers(Modifier.ABSTRACT, Modifier.PUBLIC)
                .build();
    }

    private Stream<io.graphoenix.spi.graphql.type.Type> getFieldTypeStream(String fieldName, Collection<String> implementsInterfaces) {
        return Stream.ofNullable(implementsInterfaces)
                .flatMap(Collection::stream)
                .map(name -> documentManager.getDocument().getInterfaceTypeOrError(name))
                .flatMap(interfaceType ->
                        Stream.ofNullable(interfaceType.getFields())
                                .flatMap(Collection::stream)
                                .filter(fieldDefinition -> fieldName.equals(fieldDefinition.getName()))
                                .flatMap(fieldDefinition ->
                                        Stream.concat(
                                                Stream.of(fieldDefinition.getType()),
                                                getFieldTypeStream(fieldName, interfaceType.getInterfaces())
                                        )
                                )
                )
                .filter(distinctByKey(Object::toString));
    }

    private Stream<io.graphoenix.spi.graphql.type.Type> getInputValueTypeStream(String fieldName, Collection<String> implementsInterfaces) {
        return Stream.ofNullable(implementsInterfaces)
                .flatMap(Collection::stream)
                .map(name -> documentManager.getDocument().getInputObjectTypeOrError(name))
                .flatMap(inputObjectType ->
                        Stream.ofNullable(inputObjectType.getInputValues())
                                .flatMap(Collection::stream)
                                .filter(inputValue -> fieldName.equals(inputValue.getName()))
                                .flatMap(inputValue ->
                                        Stream.concat(
                                                Stream.of(inputValue.getType()),
                                                getInputValueTypeStream(fieldName, inputObjectType.getInterfaces())
                                        )
                                )
                )
                .filter(distinctByKey(Object::toString));
    }
}
