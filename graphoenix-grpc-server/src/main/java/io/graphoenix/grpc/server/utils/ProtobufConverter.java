package io.graphoenix.grpc.server.utils;

import com.google.common.base.CaseFormat;
import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.Message;
import com.google.protobuf.MessageOrBuilder;
import com.google.protobuf.util.JsonFormat;
import io.graphoenix.core.handler.DocumentManager;
import io.graphoenix.spi.graphql.Definition;
import io.graphoenix.spi.graphql.type.FieldDefinition;
import io.graphoenix.spi.graphql.type.InputValue;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.json.JsonString;
import jakarta.json.JsonValue;
import jakarta.json.spi.JsonProvider;
import jakarta.json.stream.JsonCollectors;

import java.io.StringReader;
import java.util.AbstractMap;

@ApplicationScoped
public class ProtobufConverter {

    private final DocumentManager documentManager;
    private final JsonProvider jsonProvider;

    @Inject
    public ProtobufConverter(DocumentManager documentManager, JsonProvider jsonProvider) {
        this.documentManager = documentManager;
        this.jsonProvider = jsonProvider;
    }

    public JsonValue toJsonValue(MessageOrBuilder messageOrBuilder, FieldDefinition fieldDefinition) {
        try {
            return toGraphQLJsonValue(jsonProvider.createReader(new StringReader(JsonFormat.printer().print(messageOrBuilder))).readValue(), fieldDefinition);
        } catch (InvalidProtocolBufferException e) {
            throw new RuntimeException(e);
        }
    }

    public Message fromJsonValue(JsonValue jsonValue, Message.Builder builder, FieldDefinition fieldDefinition) {
        try {
            JsonFormat.parser().ignoringUnknownFields().merge(toProtobufJsonValue(jsonValue, fieldDefinition).toString(), builder);
            return builder.build();
        } catch (InvalidProtocolBufferException e) {
            throw new RuntimeException(e);
        }
    }

    private JsonValue toGraphQLJsonValue(JsonValue jsonValue, FieldDefinition fieldDefinition) {
        if (jsonValue.getValueType().equals(JsonValue.ValueType.NULL)) {
            return JsonValue.EMPTY_JSON_OBJECT;
        }
        return jsonValue.asJsonObject().entrySet().stream()
                .map(entry ->
                        new AbstractMap.SimpleEntry<>(
                                CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, entry.getKey()),
                                toGraphQLJsonValue(entry.getValue(), fieldDefinition.getArgumentOrNull(CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, entry.getKey())))
                        )
                )
                .collect(JsonCollectors.toJsonObject());
    }

    private JsonValue toGraphQLJsonValue(JsonValue jsonValue, InputValue inputValue) {
        Definition inputValueTypeDefinition = documentManager.getInputValueTypeDefinition(inputValue);
        switch (jsonValue.getValueType()) {
            case OBJECT:
                return jsonValue.asJsonObject().entrySet().stream()
                        .map(entry ->
                                new AbstractMap.SimpleEntry<>(
                                        CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, entry.getKey()),
                                        toGraphQLJsonValue(entry.getValue(), inputValueTypeDefinition.asInputObject().getInputValueOrNull(CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, entry.getKey())))
                                )
                        )
                        .collect(JsonCollectors.toJsonObject());
            case ARRAY:
                return jsonValue.asJsonArray().stream()
                        .map(item -> toGraphQLJsonValue(item, inputValue))
                        .collect(JsonCollectors.toJsonArray());
            case NULL:
                return jsonValue;
            default:
                if (inputValueTypeDefinition.isEnum()) {
                    String enumSuffix = "_" + CaseFormat.UPPER_CAMEL.to(CaseFormat.UPPER_UNDERSCORE, inputValueTypeDefinition.getName());
                    String enumValue = ((JsonString) jsonValue).getString();
                    return jsonProvider.createValue(enumValue.substring(0, enumValue.lastIndexOf(enumSuffix) + 1));
                }
                return jsonValue;
        }
    }

    private JsonValue toProtobufJsonValue(JsonValue jsonValue, FieldDefinition fieldDefinition) {
        Definition fieldTypeDefinition = documentManager.getFieldTypeDefinition(fieldDefinition);
        switch (jsonValue.getValueType()) {
            case OBJECT:
                return jsonValue.asJsonObject().entrySet().stream()
                        .map(entry ->
                                new AbstractMap.SimpleEntry<>(
                                        CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, entry.getKey()),
                                        toProtobufJsonValue(entry.getValue(), fieldDefinition.asObject().getField(CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, entry.getKey())))
                                )
                        )
                        .collect(JsonCollectors.toJsonObject());
            case ARRAY:
                return jsonValue.asJsonArray().stream()
                        .map(item -> toProtobufJsonValue(item, fieldDefinition))
                        .collect(JsonCollectors.toJsonArray());
            case NULL:
                return jsonValue;
            default:
                if (fieldTypeDefinition.isEnum()) {
                    String enumSuffix = "_" + CaseFormat.UPPER_CAMEL.to(CaseFormat.UPPER_UNDERSCORE, fieldTypeDefinition.getName());
                    String enumValue = ((JsonString) jsonValue).getString();
                    return jsonProvider.createValue(enumValue + enumSuffix);
                }
                return jsonValue;
        }
    }
}
