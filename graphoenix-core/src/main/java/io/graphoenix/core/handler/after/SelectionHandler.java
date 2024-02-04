package io.graphoenix.core.handler.after;

import io.graphoenix.core.handler.DocumentManager;
import io.graphoenix.core.handler.ScalarFormatter;
import io.graphoenix.spi.error.GraphQLErrorType;
import io.graphoenix.spi.error.GraphQLErrors;
import io.graphoenix.spi.graphql.Definition;
import io.graphoenix.spi.graphql.operation.Field;
import io.graphoenix.spi.graphql.operation.Operation;
import io.graphoenix.spi.graphql.type.FieldDefinition;
import io.graphoenix.spi.graphql.type.ObjectType;
import io.graphoenix.spi.handler.OperationAfterHandler;
import jakarta.annotation.Priority;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.json.JsonObject;
import jakarta.json.JsonValue;
import jakarta.json.bind.Jsonb;
import jakarta.json.spi.JsonProvider;
import jakarta.json.stream.JsonCollectors;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Collection;
import java.util.Optional;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static io.graphoenix.spi.constant.Hammurabi.*;

@ApplicationScoped
@Priority(Integer.MAX_VALUE - 100)
public class SelectionHandler implements OperationAfterHandler {

    private final DocumentManager documentManager;
    private final ScalarFormatter scalarFormatter;
    private final JsonProvider jsonProvider;
    private final Jsonb jsonb;

    @Inject
    public SelectionHandler(DocumentManager documentManager, ScalarFormatter scalarFormatter, JsonProvider jsonProvider, Jsonb jsonb) {
        this.documentManager = documentManager;
        this.scalarFormatter = scalarFormatter;
        this.jsonProvider = jsonProvider;
        this.jsonb = jsonb;
    }

    @Override
    public Mono<JsonValue> query(Operation operation, JsonValue jsonValue) {
        return handle(operation, jsonValue);
    }

    @Override
    public Mono<JsonValue> mutation(Operation operation, JsonValue jsonValue) {
        return handle(operation, jsonValue);
    }

    @Override
    public Mono<JsonValue> subscription(Operation operation, JsonValue jsonValue) {
        return handle(operation, jsonValue);
    }

    public Mono<JsonValue> handle(Operation operation, JsonValue jsonValue) {
        ObjectType operationType = documentManager.getOperationTypeOrError(operation);
        return Mono.just(
                jsonProvider
                        .createPatchBuilder(
                                operation.getFields().stream()
                                        .flatMap(field -> {
                                                    String selectionName = Optional.ofNullable(field.getAlias()).orElse(field.getName());
                                                    return Stream
                                                            .concat(
                                                                    buildFormat("/" + selectionName, operationType.getField(field.getName()), field, jsonValue.asJsonObject().get(selectionName)),
                                                                    hideField("/" + selectionName, operationType.getField(field.getName()), field, jsonValue.asJsonObject().get(selectionName))
                                                            );
                                                }
                                        )
                                        .collect(JsonCollectors.toJsonArray())
                        )
                        .build()
                        .apply(jsonValue.asJsonObject())
        );
    }

    public Stream<JsonObject> buildFormat(String path, FieldDefinition fieldDefinition, Field field, JsonValue jsonValue) {
        if (jsonValue.getValueType().equals(JsonValue.ValueType.NULL)) {
            return Stream.empty();
        }

        Definition fieldTypeDefinition = documentManager.getFieldTypeDefinition(fieldDefinition);
        if (fieldTypeDefinition.isObject()) {
            if (fieldDefinition.getType().hasList()) {
                return IntStream.range(0, jsonValue.asJsonArray().size())
                        .mapToObj(index ->
                                Stream.ofNullable(field.getFields())
                                        .flatMap(Collection::stream)
                                        .flatMap(subField -> {
                                                    String subSelectionName = Optional.ofNullable(subField.getAlias()).orElse(subField.getName());
                                                    return buildFormat(path + "/" + index + "/" + subSelectionName, fieldTypeDefinition.asObject().getField(subField.getName()), subField, jsonValue.asJsonArray().get(index).asJsonObject().get(subSelectionName));
                                                }
                                        )
                        )
                        .flatMap(stream -> stream);
            } else {
                return Stream.ofNullable(field.getFields())
                        .flatMap(Collection::stream)
                        .flatMap(subField -> {
                                    String subSelectionName = Optional.ofNullable(subField.getAlias()).orElse(subField.getName());
                                    return buildFormat(path + "/" + subSelectionName, fieldTypeDefinition.asObject().getField(subField.getName()), subField, jsonValue.asJsonObject().get(subSelectionName));
                                }
                        );
            }
        } else {
            if (field.hasFormat()) {
                if (fieldDefinition.getType().hasList()) {
                    return IntStream.range(0, jsonValue.asJsonArray().size())
                            .mapToObj(index ->
                                    jsonProvider.createObjectBuilder()
                                            .add("op", "replace")
                                            .add("path", path + "/" + index)
                                            .add("value", formatField(fieldTypeDefinition, field.getFormatValueOrNull(), field.getFormatLocaleOrNull(), jsonValue.asJsonArray().get(index)))
                                            .build()
                            );
                } else {
                    return Stream.of(
                            jsonProvider.createObjectBuilder()
                                    .add("op", "replace")
                                    .add("path", path)
                                    .add("value", formatField(fieldTypeDefinition, field.getFormatValueOrNull(), field.getFormatLocaleOrNull(), jsonValue))
                                    .build()
                    );
                }
            } else if (fieldDefinition.hasFormat()) {
                if (fieldDefinition.getType().hasList()) {
                    return IntStream.range(0, jsonValue.asJsonArray().size())
                            .mapToObj(index ->
                                    jsonProvider.createObjectBuilder()
                                            .add("op", "replace")
                                            .add("path", path + "/" + index)
                                            .add("value", formatField(fieldTypeDefinition, fieldDefinition.getFormatValueOrNull(), fieldDefinition.getFormatLocaleOrNull(), jsonValue.asJsonArray().get(index)))
                                            .build()
                            );
                } else {
                    return Stream.of(
                            jsonProvider.createObjectBuilder()
                                    .add("op", "replace")
                                    .add("path", path)
                                    .add("value", formatField(fieldTypeDefinition, fieldDefinition.getFormatValueOrNull(), fieldDefinition.getFormatLocaleOrNull(), jsonValue))
                                    .build()
                    );
                }
            }
            return Stream.empty();
        }
    }

    public JsonValue formatField(Definition fieldTypeDefinition, String value, String locale, JsonValue jsonValue) {
        switch (fieldTypeDefinition.getName()) {
            case SCALA_BOOLEAN_NAME:
                return scalarFormatter.format(value, locale, jsonb.fromJson(jsonValue.toString(), Boolean.class));
            case SCALA_ID_NAME:
            case SCALA_STRING_NAME:
                return scalarFormatter.format(value, locale, jsonb.fromJson(jsonValue.toString(), String.class));
            case SCALA_DATE_NAME:
                return scalarFormatter.format(value, locale, jsonb.fromJson(jsonValue.toString(), LocalDate.class));
            case SCALA_TIME_NAME:
                return scalarFormatter.format(value, locale, jsonb.fromJson(jsonValue.toString(), LocalTime.class));
            case SCALA_DATE_TIME_NAME:
            case SCALA_TIMESTAMP_NAME:
                return scalarFormatter.format(value, locale, jsonb.fromJson(jsonValue.toString(), LocalDateTime.class));
            case SCALA_INT_NAME:
                return scalarFormatter.format(value, locale, jsonb.fromJson(jsonValue.toString(), Integer.class));
            case SCALA_BIG_INTEGER_NAME:
                return scalarFormatter.format(value, locale, jsonb.fromJson(jsonValue.toString(), BigInteger.class));
            case SCALA_FLOAT_NAME:
                return scalarFormatter.format(value, locale, jsonb.fromJson(jsonValue.toString(), Float.class));
            case SCALA_BIG_DECIMAL_NAME:
                return scalarFormatter.format(value, locale, jsonb.fromJson(jsonValue.toString(), BigDecimal.class));
            default:
                throw new GraphQLErrors(GraphQLErrorType.UNSUPPORTED_FIELD_TYPE.bind(fieldTypeDefinition.toString()));
        }
    }

    public Stream<JsonObject> hideField(String path, FieldDefinition fieldDefinition, Field field, JsonValue jsonValue) {
        if (jsonValue.getValueType().equals(JsonValue.ValueType.NULL)) {
            return Stream.empty();
        }

        if (field.isHide()) {
            return Stream.of(
                    jsonProvider.createObjectBuilder()
                            .add("op", "remove")
                            .add("path", path)
                            .build()
            );
        } else {
            Definition fieldTypeDefinition = documentManager.getFieldTypeDefinition(fieldDefinition);
            if (fieldTypeDefinition.isObject()) {
                if (fieldDefinition.getType().hasList()) {
                    return IntStream.range(0, jsonValue.asJsonArray().size())
                            .mapToObj(index ->
                                    Stream.ofNullable(field.getFields())
                                            .flatMap(Collection::stream)
                                            .flatMap(subField -> {
                                                        String subSelectionName = Optional.ofNullable(subField.getAlias()).orElse(subField.getName());
                                                        return hideField(path + "/" + index + "/" + subSelectionName, fieldTypeDefinition.asObject().getField(subField.getName()), subField, jsonValue.asJsonArray().get(index).asJsonObject().get(subSelectionName));
                                                    }
                                            )
                            )
                            .flatMap(stream -> stream);
                } else {
                    return Stream.ofNullable(field.getFields())
                            .flatMap(Collection::stream)
                            .flatMap(subField -> {
                                        String subSelectionName = Optional.ofNullable(subField.getAlias()).orElse(subField.getName());
                                        return hideField(path + "/" + subSelectionName, fieldTypeDefinition.asObject().getField(subField.getName()), subField, jsonValue.asJsonObject().get(subSelectionName));
                                    }
                            );
                }
            } else {
                return Stream.empty();
            }
        }
    }
}
