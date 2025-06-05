package io.graphoenix.spi.graphql;

import io.graphoenix.spi.graphql.type.FieldDefinition;

import java.util.Collection;

@SuppressWarnings("ALL")
public interface FieldsType extends Definition {

    String getName();

    String getDescription();

    Collection<String> getInterfaces();

    FieldDefinition getField(String name);

    FieldDefinition getFieldOrError(String name);

    Collection<FieldDefinition> getFields();
}
