package io.graphoenix.spi.utils;

import com.google.common.base.CaseFormat;
import org.eclipse.microprofile.graphql.*;
import org.eclipse.microprofile.graphql.Enum;

import javax.lang.model.element.Element;

import static javax.lang.model.element.ElementKind.ENUM;
import static javax.lang.model.element.ElementKind.METHOD;

public final class GraphQLUtil {

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
        } else if (element.getSimpleName().toString().startsWith("get")) {
            return CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_CAMEL, element.getSimpleName().toString().replaceFirst("get", ""));
        } else if (element.getSimpleName().toString().startsWith("set")) {
            return CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_CAMEL, element.getSimpleName().toString().replaceFirst("set", ""));
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

    public static String getDefaultValueFromElement(Element element) {
        DefaultValue defaultValue = element.getAnnotation(DefaultValue.class);
        if (defaultValue != null) {
            return defaultValue.value();
        } else {
            return null;
        }
    }
}
