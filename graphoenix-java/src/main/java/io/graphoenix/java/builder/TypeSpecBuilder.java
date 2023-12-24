package io.graphoenix.java.builder;

import com.dslplatform.json.CompiledJson;
import com.google.common.base.CaseFormat;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.*;
import io.graphoenix.core.handler.DocumentManager;
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
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static io.graphoenix.java.utils.NameUtil.getFieldGetterMethodName;
import static io.graphoenix.java.utils.NameUtil.getFieldSetterMethodName;
import static io.graphoenix.java.utils.TypeNameUtil.toClassName;
import static io.graphoenix.spi.constant.Hammurabi.*;
import static io.graphoenix.spi.error.GraphQLErrorType.UNSUPPORTED_FIELD_TYPE;

@ApplicationScoped
public class TypeSpecBuilder {

    private final DocumentManager documentManager;

    @Inject
    public TypeSpecBuilder(DocumentManager documentManager) {
        this.documentManager = documentManager;
    }

    private AnnotationSpec getGeneratedAnnotationSpec() {
        return AnnotationSpec.builder(Generated.class)
                .addMember("value", "$S", getClass().getName())
                .build();
    }

    private AnnotationSpec getIgnoreAnnotationSpec() {
        return AnnotationSpec.builder(Ignore.class)
                .build();
    }

    public TypeSpec buildType(ObjectType objectType) {
        TypeSpec.Builder builder = TypeSpec.classBuilder(objectType.getName())
                .addModifiers(Modifier.PUBLIC)
                .addAnnotation(Type.class)
                .addAnnotation(CompiledJson.class)
                .addAnnotation(getGeneratedAnnotationSpec())
                .addAnnotation(getIgnoreAnnotationSpec());

        List<FieldSpec> fieldSpecs = objectType.getFields().stream()
                .map(this::buildField)
                .collect(Collectors.toList());

        List<MethodSpec> methodSpecs = fieldSpecs.stream()
                .flatMap(fieldSpec ->
                        Stream.of(
                                buildGetter(fieldSpec, objectType.getInterfaces()),
                                buildSetter(fieldSpec, objectType.getInterfaces())
                        )
                )
                .collect(Collectors.toList());

        builder
                .addFields(fieldSpecs)
                .addMethods(methodSpecs);

        if (objectType.getInterfaces() != null && objectType.getInterfaces().size() > 0) {
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
                                    buildInterfaceSetter(fieldSpec)
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
                            Stream.of(
                                    buildInputValueGetter(fieldSpec, inputObjectType.getInterfaces()),
                                    buildInputValueSetter(fieldSpec, inputObjectType.getInterfaces())
                            )
                    )
                    .collect(Collectors.toList());
            builder
                    .addFields(fieldSpecs)
                    .addMethods(methodSpecs);
        }
        builder.addModifiers(Modifier.PUBLIC)
                .addAnnotation(Input.class)
                .addAnnotation(getGeneratedAnnotationSpec())
                .addAnnotation(getIgnoreAnnotationSpec());

        if (inputObjectType.getInterfaces() != null && inputObjectType.getInterfaces().size() > 0) {
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

    public TypeSpec buildType(EnumType enumType) {
        TypeSpec.Builder builder = TypeSpec.enumBuilder(enumType.getName())
                .addModifiers(Modifier.PUBLIC)
                .addAnnotation(Enum.class)
                .addAnnotation(getGeneratedAnnotationSpec())
                .addAnnotation(getIgnoreAnnotationSpec());

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
                .addAnnotation(getGeneratedAnnotationSpec())
                .addAnnotation(getIgnoreAnnotationSpec());

        List<FieldSpec> fieldSpecs = interfaceType.getFields().stream()
                .map(this::buildInterfaceField)
                .collect(Collectors.toList());

        List<MethodSpec> methodSpecs = fieldSpecs.stream()
                .flatMap(fieldSpec ->
                        Stream.of(
                                buildInterfaceGetter(fieldSpec),
                                buildInterfaceSetter(fieldSpec)
                        )
                )
                .collect(Collectors.toList());

        builder
                .addFields(fieldSpecs)
                .addMethods(methodSpecs);

        if (interfaceType.getInterfaces() != null && interfaceType.getInterfaces().size() > 0) {
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
                .addAnnotation(getGeneratedAnnotationSpec())
                .addAnnotation(getIgnoreAnnotationSpec());

        if (directiveDefinition.getArguments() != null && directiveDefinition.getArguments().size() > 0) {
            builder.addMethods(
                    directiveDefinition.getArguments().stream()
                            .map(this::buildAnnotationMethod)
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
        }
        return null;
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

    public MethodSpec buildAnnotationMethod(InputValue inputValue) {
        boolean isKeyword = SourceVersion.isKeyword(inputValue.getName());
        MethodSpec.Builder builder = MethodSpec.methodBuilder(isKeyword ? "_" + inputValue.getName() : inputValue.getName())
                .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                .returns(buildType(inputValue.getType()));
        if (isKeyword) {
            builder.addAnnotation(
                    AnnotationSpec.builder(Name.class)
                            .addMember("value", "$S", inputValue.getName())
                            .build()
            );
        }
        if (inputValue.getDefaultValue() != null) {
            builder.defaultValue(buildDefaultValue(inputValue, inputValue.getDefaultValue()));
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
        if (inputValue.isEnum()) {
            return CodeBlock.of(
                    "$T.$L",
                    toClassName(inputValueTypeDefinition.getClassNameOrError()),
                    defaultValue
            );
        } else {
            return CodeBlock.of("$L", defaultValue);
        }
    }

    public TypeName buildType(io.graphoenix.spi.graphql.type.Type type) {
        if (type.isList()) {
            return ParameterizedTypeName.get(ClassName.get(Collection.class), buildType(type.asListType().getType()));
        } else if (type.isNonNull()) {
            return buildType(type.asNonNullType().getType());
        } else {
            return buildType(type.asTypeName());
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

    private MethodSpec buildSetter(FieldSpec fieldSpec, Collection<String> implementsInterfaces) {
        String setterName = getFieldSetterMethodName(fieldSpec.name);
        MethodSpec.Builder methodBuilder = MethodSpec.methodBuilder(setterName).addModifiers(Modifier.PUBLIC);
        methodBuilder.addParameter(fieldSpec.type, fieldSpec.name);
        methodBuilder.addStatement("this." + fieldSpec.name + " = " + fieldSpec.name);

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

    private MethodSpec buildInputValueSetter(FieldSpec fieldSpec, Collection<String> implementsInterfaces) {
        String setterName = getFieldSetterMethodName(fieldSpec.name);
        MethodSpec.Builder methodBuilder = MethodSpec.methodBuilder(setterName).addModifiers(Modifier.PUBLIC);
        methodBuilder.addParameter(fieldSpec.type, fieldSpec.name);
        methodBuilder.addStatement("this." + fieldSpec.name + " = " + fieldSpec.name);

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

    private MethodSpec buildInterfaceSetter(FieldSpec fieldSpec) {
        String setterName = getFieldSetterMethodName(fieldSpec.name);
        return MethodSpec.methodBuilder(setterName)
                .addModifiers(Modifier.ABSTRACT, Modifier.PUBLIC)
                .addParameter(fieldSpec.type, fieldSpec.name)
                .build();
    }

    public MethodSpec buildInterfaceGetter(FieldSpec fieldSpec) {
        String getterName = getFieldGetterMethodName(fieldSpec.name);
        return MethodSpec.methodBuilder(getterName)
                .returns(fieldSpec.type)
                .addModifiers(Modifier.ABSTRACT, Modifier.PUBLIC)
                .build();
    }
}
