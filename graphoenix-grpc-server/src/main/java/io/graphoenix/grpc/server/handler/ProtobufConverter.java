package io.graphoenix.grpc.server.handler;

import com.google.common.base.CaseFormat;
import com.google.protobuf.*;
import com.google.protobuf.util.JsonFormat;
import com.google.type.Date;
import com.google.type.Decimal;
import com.google.type.TimeOfDay;
import io.graphoenix.core.handler.DocumentManager;
import io.graphoenix.spi.graphql.Definition;
import io.graphoenix.spi.graphql.type.FieldDefinition;
import io.graphoenix.spi.graphql.type.InputValue;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.json.JsonString;
import jakarta.json.JsonValue;
import jakarta.json.bind.Jsonb;
import jakarta.json.spi.JsonProvider;
import jakarta.json.stream.JsonCollectors;

import java.io.StringReader;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.*;
import java.util.AbstractMap;

import static io.graphoenix.spi.constant.Hammurabi.*;

@ApplicationScoped
public class ProtobufConverter {

    private final DocumentManager documentManager;
    private final JsonProvider jsonProvider;
    private final Jsonb jsonb;

    @Inject
    public ProtobufConverter(DocumentManager documentManager, JsonProvider jsonProvider, Jsonb jsonb) {
        this.documentManager = documentManager;
        this.jsonProvider = jsonProvider;
        this.jsonb = jsonb;
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
            JsonFormat.parser().ignoringUnknownFields()
                    .merge(
                            jsonProvider.createObjectBuilder()
                                    .add(
                                            CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, fieldDefinition.getName()),
                                            toProtobufJsonValue(jsonValue, fieldDefinition)
                                    )
                                    .build()
                                    .toString(),
                            builder
                    );
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
                .filter(entry -> !entry.getKey().equals("selectionSet"))
                .map(entry ->
                        new AbstractMap.SimpleEntry<>(
                                entry.getKey(),
                                toGraphQLJsonValue(entry.getValue(), fieldDefinition.getArgument(entry.getKey()))
                        )
                )
                .collect(JsonCollectors.toJsonObject());
    }

    private JsonValue toGraphQLJsonValue(JsonValue jsonValue, InputValue inputValue) {
        try {
            Definition inputValueTypeDefinition = documentManager.getInputValueTypeDefinition(inputValue);
            switch (jsonValue.getValueType()) {
                case NULL:
                    return jsonValue;
                case OBJECT:
                    return jsonValue.asJsonObject().entrySet().stream()
                            .map(entry ->
                                    new AbstractMap.SimpleEntry<>(
                                            entry.getKey(),
                                            toGraphQLJsonValue(entry.getValue(), inputValueTypeDefinition.asInputObject().getInputValue(entry.getKey()))
                                    )
                            )
                            .collect(JsonCollectors.toJsonObject());
                case ARRAY:
                    return jsonValue.asJsonArray().stream()
                            .map(item -> toGraphQLJsonValue(item, inputValue))
                            .collect(JsonCollectors.toJsonArray());
                default:
                    if (inputValueTypeDefinition.isEnum()) {
                        String enumSuffix = "_" + CaseFormat.UPPER_CAMEL.to(CaseFormat.UPPER_UNDERSCORE, inputValueTypeDefinition.getName());
                        String enumValue = ((JsonString) jsonValue).getString();
                        return jsonProvider.createValue(enumValue.substring(0, enumValue.lastIndexOf(enumSuffix)));
                    } else if (inputValueTypeDefinition.getName().equals(SCALA_TIMESTAMP_NAME)) {
                        Timestamp.Builder builder = Timestamp.newBuilder();
                        JsonFormat.parser().merge(jsonValue.toString(), builder);
                        Timestamp timestamp = builder.build();
                        return jsonProvider.createValue(LocalDateTime.ofEpochSecond(timestamp.getSeconds(), timestamp.getNanos(), ZoneOffset.UTC).toString());
                    } else if (inputValueTypeDefinition.getName().equals(SCALA_DATE_NAME)) {
                        Date.Builder builder = Date.newBuilder();
                        JsonFormat.parser().merge(jsonValue.toString(), builder);
                        Date date = builder.build();
                        return jsonProvider.createValue(LocalDate.of(date.getYear(), date.getMonth(), date.getDay()).toString());
                    } else if (inputValueTypeDefinition.getName().equals(SCALA_TIME_NAME)) {
                        TimeOfDay.Builder builder = TimeOfDay.newBuilder();
                        JsonFormat.parser().merge(jsonValue.toString(), builder);
                        TimeOfDay timeOfDay = builder.build();
                        return jsonProvider.createValue(LocalTime.of(timeOfDay.getHours(), timeOfDay.getMinutes(), timeOfDay.getSeconds(), timeOfDay.getNanos()).toString());
                    } else if (inputValueTypeDefinition.getName().equals(SCALA_BIG_DECIMAL_NAME)) {
                        Decimal.Builder builder = Decimal.newBuilder();
                        JsonFormat.parser().merge(jsonValue.toString(), builder);
                        Decimal decimal = builder.build();
                        return jsonProvider.createValue(new BigDecimal(decimal.getValue()));
                    } else if (inputValueTypeDefinition.getName().equals(SCALA_BIG_INTEGER_NAME)) {
                        Decimal.Builder builder = Decimal.newBuilder();
                        JsonFormat.parser().merge(jsonValue.toString(), builder);
                        Decimal decimal = builder.build();
                        return jsonProvider.createValue(new BigInteger(decimal.getValue()));
                    }
                    return jsonValue;
            }
        } catch (InvalidProtocolBufferException e) {
            throw new RuntimeException(e);
        }
    }

    private JsonValue toProtobufJsonValue(JsonValue jsonValue, FieldDefinition fieldDefinition) {
        try {
            Definition fieldTypeDefinition = documentManager.getFieldTypeDefinition(fieldDefinition);
            switch (jsonValue.getValueType()) {
                case NULL:
                    return jsonValue;
                case OBJECT:
                    return jsonValue.asJsonObject().entrySet().stream()
                            .map(entry ->
                                    new AbstractMap.SimpleEntry<>(
                                            CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, entry.getKey()),
                                            toProtobufJsonValue(entry.getValue(), fieldTypeDefinition.asObject().getField(entry.getKey()))
                                    )
                            )
                            .collect(JsonCollectors.toJsonObject());
                case ARRAY:
                    return jsonValue.asJsonArray().stream()
                            .map(item -> toProtobufJsonValue(item, fieldDefinition))
                            .collect(JsonCollectors.toJsonArray());
                default:
                    if (fieldTypeDefinition.isEnum()) {
                        String enumSuffix = "_" + CaseFormat.UPPER_CAMEL.to(CaseFormat.UPPER_UNDERSCORE, fieldTypeDefinition.getName());
                        String enumValue = ((JsonString) jsonValue).getString();
                        return jsonProvider.createValue(enumValue + enumSuffix);
                    } else if (fieldTypeDefinition.getName().equals(SCALA_TIMESTAMP_NAME)) {
                        LocalDateTime localDateTime = jsonb.fromJson(jsonValue.toString(), LocalDateTime.class);
                        Instant instant = localDateTime.toInstant(ZoneOffset.UTC);
                        return jsonProvider.createValue(
                                JsonFormat.printer()
                                        .print(
                                                Timestamp.newBuilder()
                                                        .setSeconds(instant.getEpochSecond())
                                                        .setNanos(instant.getNano())
                                                        .build()
                                        )
                        );
                    } else if (fieldTypeDefinition.getName().equals(SCALA_DATE_NAME)) {
                        LocalDate localDate = jsonb.fromJson(jsonValue.toString(), LocalDate.class);
                        return jsonProvider.createValue(
                                JsonFormat.printer()
                                        .print(
                                                Date.newBuilder()
                                                        .setYear(localDate.getYear())
                                                        .setMonth(localDate.getMonthValue())
                                                        .setDay(localDate.getDayOfMonth())
                                                        .build()
                                        )
                        );
                    } else if (fieldTypeDefinition.getName().equals(SCALA_TIME_NAME)) {
                        LocalTime localTime = jsonb.fromJson(jsonValue.toString(), LocalTime.class);
                        return jsonProvider.createValue(
                                JsonFormat.printer()
                                        .print(
                                                TimeOfDay.newBuilder()
                                                        .setHours(localTime.getHour())
                                                        .setMinutes(localTime.getMinute())
                                                        .setSeconds(localTime.getSecond())
                                                        .setNanos(localTime.getNano())
                                                        .build()
                                        )
                        );
                    } else if (fieldTypeDefinition.getName().equals(SCALA_BIG_DECIMAL_NAME)) {
                        BigDecimal bigDecimal = jsonb.fromJson(jsonValue.toString(), BigDecimal.class);
                        return jsonProvider.createValue(
                                JsonFormat.printer()
                                        .print(
                                                Decimal.newBuilder()
                                                        .setValueBytes(ByteString.copyFrom(bigDecimal.unscaledValue().toByteArray()))
                                                        .build()
                                        )
                        );
                    } else if (fieldTypeDefinition.getName().equals(SCALA_BIG_INTEGER_NAME)) {
                        BigInteger bigInteger = jsonb.fromJson(jsonValue.toString(), BigInteger.class);
                        return jsonProvider.createValue(
                                JsonFormat.printer()
                                        .print(
                                                Decimal.newBuilder()
                                                        .setValueBytes(ByteString.copyFrom(bigInteger.toByteArray()))
                                                        .build()
                                        )
                        );
                    }
                    return jsonValue;
            }
        } catch (InvalidProtocolBufferException e) {
            throw new RuntimeException(e);
        }
    }
}
