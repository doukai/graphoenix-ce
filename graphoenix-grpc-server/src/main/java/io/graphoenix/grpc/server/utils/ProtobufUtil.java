package io.graphoenix.grpc.server.utils;

import com.google.common.base.CaseFormat;
import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.Message;
import com.google.protobuf.MessageOrBuilder;
import com.google.protobuf.util.JsonFormat;
import jakarta.json.JsonValue;
import jakarta.json.spi.JsonProvider;
import jakarta.json.stream.JsonCollectors;

import java.io.StringReader;
import java.util.AbstractMap;

public final class ProtobufUtil {

    private static final JsonProvider jsonProvider = JsonProvider.provider();

    public static JsonValue toJsonValue(MessageOrBuilder messageOrBuilder) {
        try {
            return toGraphQLJsonValue(jsonProvider.createReader(new StringReader(JsonFormat.printer().print(messageOrBuilder))).readValue());
        } catch (InvalidProtocolBufferException e) {
            throw new RuntimeException(e);
        }
    }

    public static Message fromJsonValue(JsonValue jsonValue, Message.Builder builder) {
        try {
            JsonFormat.parser().ignoringUnknownFields().merge(toProtobufJsonValue(jsonValue).toString(), builder);
            return builder.build();
        } catch (InvalidProtocolBufferException e) {
            throw new RuntimeException(e);
        }
    }

    private static JsonValue toGraphQLJsonValue(JsonValue jsonValue) {
        switch (jsonValue.getValueType()) {
            case OBJECT:
                return jsonValue.asJsonObject().entrySet().stream()
                        .map(entry ->
                                new AbstractMap.SimpleEntry<>(
                                        CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, entry.getKey()),
                                        toGraphQLJsonValue(entry.getValue())
                                )
                        )
                        .collect(JsonCollectors.toJsonObject());
            case ARRAY:
                return jsonValue.asJsonArray().stream()
                        .map(ProtobufUtil::toGraphQLJsonValue)
                        .collect(JsonCollectors.toJsonArray());
            default:
                return jsonValue;
        }
    }

    private static JsonValue toProtobufJsonValue(JsonValue jsonValue) {
        switch (jsonValue.getValueType()) {
            case OBJECT:
                return jsonValue.asJsonObject().entrySet().stream()
                        .map(entry ->
                                new AbstractMap.SimpleEntry<>(
                                        CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, entry.getKey()),
                                        toProtobufJsonValue(entry.getValue())
                                )
                        )
                        .collect(JsonCollectors.toJsonObject());
            case ARRAY:
                return jsonValue.asJsonArray().stream()
                        .map(ProtobufUtil::toProtobufJsonValue)
                        .collect(JsonCollectors.toJsonArray());
            default:
                return jsonValue;
        }
    }
}
