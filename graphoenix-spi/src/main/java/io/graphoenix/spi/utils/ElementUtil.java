package io.graphoenix.spi.utils;

import com.google.common.base.CaseFormat;
import io.graphoenix.spi.graphql.common.Arguments;
import io.graphoenix.spi.graphql.common.Directive;
import io.graphoenix.spi.graphql.common.ValueWithVariable;
import io.graphoenix.spi.graphql.type.InputValue;
import io.graphoenix.spi.graphql.type.ListType;
import io.graphoenix.spi.graphql.type.NonNullType;
import io.graphoenix.spi.graphql.type.TypeName;
import jakarta.json.JsonValue;
import jakarta.json.spi.JsonProvider;
import jakarta.json.stream.JsonCollectors;
import org.eclipse.microprofile.graphql.Enum;
import org.eclipse.microprofile.graphql.*;
import org.eclipse.microprofile.graphql.Name;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.lang.model.element.*;
import javax.lang.model.type.ArrayType;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Types;
import java.io.StringReader;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static io.graphoenix.spi.constant.Hammurabi.*;
import static javax.lang.model.element.ElementKind.ENUM;
import static javax.lang.model.element.ElementKind.METHOD;
import static javax.lang.model.type.TypeKind.ARRAY;

public final class ElementUtil {

    public static String getNameFromElement(Element element) {
        Name nameAnnotation = element.getAnnotation(Name.class);
        if (nameAnnotation != null) {
            return nameAnnotation.value();
        } else if (element.getKind().isClass()) {
            Type typeAnnotation = element.getAnnotation(Type.class);
            if (typeAnnotation != null && !typeAnnotation.value().isBlank()) {
                return typeAnnotation.value();
            }
            Input inputAnnotation = element.getAnnotation(Input.class);
            if (inputAnnotation != null && !inputAnnotation.value().isBlank()) {
                return inputAnnotation.value();
            }
        } else if (element.getKind().isInterface()) {
            Interface interfaceAnnotation = element.getAnnotation(Interface.class);
            if (interfaceAnnotation != null && !interfaceAnnotation.value().isBlank()) {
                return interfaceAnnotation.value();
            }
        } else if (element.getKind().equals(ENUM)) {
            Enum enumAnnotation = element.getAnnotation(Enum.class);
            if (enumAnnotation != null && !enumAnnotation.value().isBlank()) {
                return enumAnnotation.value();
            }
        } else if (element.getKind().equals(METHOD)) {
            Query queryAnnotation = element.getAnnotation(Query.class);
            if (queryAnnotation != null && !queryAnnotation.value().isBlank()) {
                return queryAnnotation.value();
            }
            Mutation mutationAnnotation = element.getAnnotation(Mutation.class);
            if (mutationAnnotation != null && !mutationAnnotation.value().isBlank()) {
                return mutationAnnotation.value();
            }
            if (element.getSimpleName().toString().startsWith("get")) {
                return CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_CAMEL, element.getSimpleName().toString().replaceFirst("get", ""));
            } else if (element.getSimpleName().toString().startsWith("set")) {
                return CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_CAMEL, element.getSimpleName().toString().replaceFirst("set", ""));
            }
        }
        return element.getSimpleName().toString();
    }

    public static String getDescriptionFromElement(Element element) {
        Description description = element.getAnnotation(Description.class);
        if (description != null) {
            return description.value();
        } else {
            return null;
        }
    }

    public static ValueWithVariable getDefaultValueFromElement(Element element) {
        DefaultValue defaultValue = element.getAnnotation(DefaultValue.class);
        if (defaultValue != null) {
            return ValueWithVariable.of(JsonProvider.provider().createReader(new StringReader(defaultValue.value())).readValue());
        } else {
            return null;
        }
    }

    public static List<Directive> getDirectivesFromElement(Element element) {
        return element.getAnnotationMirrors().stream()
                .filter(annotationMirror -> annotationMirror.getAnnotationType().getAnnotation(io.graphoenix.spi.annotation.Directive.class) != null)
                .map(Directive::new)
                .collect(Collectors.toList());
    }

    public static io.graphoenix.spi.graphql.type.Type executableElementToTypeName(ExecutableElement executableElement, Types types) {
        TypeMirror typeMirror;
        String typeName = getTypeName(executableElement.getReturnType(), types);
        if (typeName.equals(Flux.class.getCanonicalName())) {
            typeMirror = ((DeclaredType) (executableElement).getReturnType()).getTypeArguments().get(0);
            return new ListType(elementToTypeName(executableElement, typeMirror, types));
        } else if (typeName.equals(Mono.class.getCanonicalName())) {
            typeMirror = ((DeclaredType) (executableElement).getReturnType()).getTypeArguments().get(0);
            return elementToTypeName(executableElement, typeMirror, types);
        } else {
            typeMirror = executableElement.getReturnType();
            return elementToTypeName(executableElement, typeMirror, types);
        }
    }

    public static io.graphoenix.spi.graphql.type.Type variableElementToTypeName(VariableElement variableElement, Types types) {
        TypeMirror typeMirror;
        String typeName = getTypeName(variableElement.asType(), types);
        if (typeName.equals(Flux.class.getCanonicalName())) {
            typeMirror = ((DeclaredType) variableElement.asType()).getTypeArguments().get(0);
            return new ListType(elementToTypeName(variableElement, typeMirror, types));
        } else if (typeName.equals(Mono.class.getCanonicalName())) {
            typeMirror = ((DeclaredType) variableElement.asType()).getTypeArguments().get(0);
            return elementToTypeName(variableElement, typeMirror, types);
        } else {
            typeMirror = variableElement.asType();
            return elementToTypeName(variableElement, typeMirror, types);
        }
    }

    public static io.graphoenix.spi.graphql.type.Type elementToTypeName(Element element, TypeMirror typeMirror, Types types) {
        io.graphoenix.spi.graphql.type.Type elementType;
        String typeName = getTypeName(typeMirror, types);
        if (element.getAnnotation(Id.class) != null) {
            elementType = new TypeName(SCALA_ID_NAME);
        } else if (typeMirror.getKind().equals(ARRAY)) {
            elementType = new ListType(elementToTypeName(element, ((ArrayType) typeMirror).getComponentType(), types));
        } else if (typeName.equals(int.class.getCanonicalName()) ||
                typeName.equals(long.class.getCanonicalName()) ||
                typeName.equals(short.class.getCanonicalName()) ||
                typeName.equals(byte.class.getCanonicalName()) ||
                typeName.equals(Integer.class.getCanonicalName()) ||
                typeName.equals(Long.class.getCanonicalName()) ||
                typeName.equals(Short.class.getCanonicalName()) ||
                typeName.equals(Byte.class.getCanonicalName())) {
            elementType = new TypeName(SCALA_INT_NAME);
        } else if (typeName.equals(float.class.getCanonicalName()) ||
                typeName.equals(double.class.getCanonicalName()) ||
                typeName.equals(Float.class.getCanonicalName()) ||
                typeName.equals(Double.class.getCanonicalName())) {
            elementType = new TypeName(SCALA_FLOAT_NAME);
        } else if (typeName.equals(char.class.getCanonicalName()) ||
                typeName.equals(String.class.getCanonicalName()) ||
                typeName.equals(Character.class.getCanonicalName())) {
            elementType = new TypeName(SCALA_STRING_NAME);
        } else if (typeName.equals(boolean.class.getCanonicalName()) ||
                typeName.equals(Boolean.class.getCanonicalName())) {
            elementType = new TypeName(SCALA_BOOLEAN_NAME);
        } else if (typeName.equals(BigInteger.class.getCanonicalName())) {
            elementType = new TypeName(SCALA_BIG_INTEGER_NAME);
        } else if (typeName.equals(BigDecimal.class.getCanonicalName())) {
            elementType = new TypeName(SCALA_BIG_DECIMAL_NAME);
        } else if (typeName.equals(LocalDate.class.getCanonicalName())) {
            elementType = new TypeName(SCALA_DATE_NAME);
        } else if (typeName.equals(LocalTime.class.getCanonicalName())) {
            elementType = new TypeName(SCALA_TIME_NAME);
        } else if (typeName.equals(LocalDateTime.class.getCanonicalName())) {
            elementType = new TypeName(SCALA_DATE_TIME_NAME);
        } else if (typeName.equals(Collection.class.getCanonicalName()) ||
                typeName.equals(List.class.getCanonicalName()) ||
                typeName.equals(Set.class.getCanonicalName())) {
            elementType = new ListType(elementToTypeName(element, ((DeclaredType) typeMirror).getTypeArguments().get(0), types));
        } else {
            elementType = new TypeName(getNameFromElement(types.asElement(typeMirror)));
        }

        if (element.getAnnotation(NonNull.class) != null || typeMirror.getKind().isPrimitive()) {
            return new NonNullType(elementType);
        } else {
            return elementType;
        }
    }

    public static String getTypeName(TypeMirror typeMirror, Types types) {
        if (typeMirror.getKind().isPrimitive()) {
            return typeMirror.getKind().toString().toLowerCase();
        } else if (typeMirror.getKind().equals(ARRAY)) {
            return getTypeName(((ArrayType) typeMirror).getComponentType(), types) + "[]";
        } else if (typeMirror.getKind().equals(TypeKind.DECLARED)) {
            return ((TypeElement) types.asElement(typeMirror)).getQualifiedName().toString();
        }
        throw new RuntimeException("illegal typeMirror: " + typeMirror);
    }

    public static Optional<Directive> getFormatDirective(Element element) {
        NumberFormat numberFormat = element.getAnnotation(NumberFormat.class);
        if (numberFormat != null) {
            return Optional.ofNullable(
                    new Directive(DIRECTIVE_FORMAT_NAME)
                            .addArgument(DIRECTIVE_FORMAT_ARGUMENT_VALUE_NAME, numberFormat.value())
                            .addArgument(DIRECTIVE_FORMAT_ARGUMENT_LOCALE_NAME, numberFormat.locale())
            );
        }
        DateFormat dateFormat = element.getAnnotation(DateFormat.class);
        if (dateFormat != null) {
            return Optional.ofNullable(
                    new Directive(DIRECTIVE_FORMAT_NAME)
                            .addArgument(DIRECTIVE_FORMAT_ARGUMENT_VALUE_NAME, dateFormat.value())
                            .addArgument(DIRECTIVE_FORMAT_ARGUMENT_LOCALE_NAME, dateFormat.locale())
            );
        }
        return Optional.empty();
    }

    public static List<InputValue> executableElementParametersToInputValues(ExecutableElement executableElement, Types typeUtils) {
        if (executableElement.getParameters() != null) {
            return executableElement.getParameters().stream()
                    .filter(variableElement -> variableElement.getAnnotation(Source.class) == null)
                    .map(variableElement -> new InputValue(variableElement, typeUtils))
                    .collect(Collectors.toList());
        } else {
            return Collections.emptyList();
        }
    }

    public static String getTypeNameFromTypeMirror(TypeMirror typeMirror, Types types) {
        if (typeMirror.getKind().isPrimitive()) {
            return typeMirror.getKind().toString().toLowerCase();
        } else if (typeMirror.getKind().equals(ARRAY)) {
            return getTypeNameFromTypeMirror(((ArrayType) typeMirror).getComponentType(), types) + "[]";
        } else if (typeMirror.getKind().equals(TypeKind.DECLARED)) {
            DeclaredType declaredType = (DeclaredType) typeMirror;
            if (declaredType.getTypeArguments() != null && !declaredType.getTypeArguments().isEmpty()) {
                return ((TypeElement) types.asElement(declaredType)).getQualifiedName().toString() +
                        "<" +
                        declaredType.getTypeArguments().stream().map(argumentTypeMirror -> getTypeNameFromTypeMirror(argumentTypeMirror, types))
                                .collect(Collectors.joining(", ")) +
                        ">";
            }
            return ((TypeElement) types.asElement(declaredType)).getQualifiedName().toString();
        }
        throw new RuntimeException("illegal typeMirror: " + typeMirror);
    }

    public static String getAsyncMethodName(ExecutableElement executableElement, Types typeUtils) {
        return Stream
                .concat(
                        Stream.of(executableElement.getSimpleName().toString() + "Async"),
                        executableElement.getParameters().stream()
                                .map(parameter -> typeUtils.asElement(parameter.asType()).getSimpleName().toString())
                )
                .collect(Collectors.joining("_"));
    }
}
