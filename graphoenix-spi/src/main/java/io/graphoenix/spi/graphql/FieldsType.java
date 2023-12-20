package io.graphoenix.spi.graphql;

import graphql.parser.antlr.GraphqlParser;
import io.graphoenix.spi.graphql.common.Directive;
import io.graphoenix.spi.graphql.common.StringValue;
import io.graphoenix.spi.graphql.common.ValueWithVariable;
import io.graphoenix.spi.graphql.type.FieldDefinition;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static io.graphoenix.spi.constant.Hammurabi.*;
import static io.graphoenix.spi.utils.DocumentUtil.getStringValue;
import static io.graphoenix.spi.utils.StreamUtil.distinctByKey;

@SuppressWarnings("ALL")
public interface FieldsType extends Definition {

    String getName();

    Collection<String> getInterfaces();

    FieldDefinition getField(String name);

    Collection<FieldDefinition> getFields();
}
