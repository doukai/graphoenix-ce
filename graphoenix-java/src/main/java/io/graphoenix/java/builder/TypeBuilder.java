package io.graphoenix.java.builder;

import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.TypeSpec;
import io.graphoenix.spi.graphql.type.*;

public interface TypeBuilder {

    default void buildObjectType(TypeSpec.Builder builder, ObjectType objectType) {
    }

    default void buildInterfaceType(TypeSpec.Builder builder, InterfaceType interfaceType) {
    }

    default void buildFieldDefinition(FieldSpec.Builder builder, FieldDefinition fieldDefinition) {
    }

    default void buildInputObjectType(TypeSpec.Builder builder, InputObjectType inputObjectType) {
    }

    default void buildInputValue(FieldSpec.Builder builder, InputValue inputValue) {
    }

    default void buildEnumType(TypeSpec.Builder builder, EnumType enumType) {
    }

    default void buildEnumValueDefinition(TypeSpec.Builder builder, EnumValueDefinition enumValueDefinition) {
    }
}
