package io.graphoenix.core.handler.fetch;

import io.graphoenix.core.handler.DocumentManager;
import io.graphoenix.spi.graphql.AbstractDefinition;
import io.graphoenix.spi.graphql.common.EnumValue;
import io.graphoenix.spi.graphql.operation.Field;
import io.graphoenix.spi.graphql.operation.Operation;
import io.graphoenix.spi.graphql.type.FieldDefinition;
import io.graphoenix.spi.graphql.type.ObjectType;
import io.graphoenix.spi.handler.FetchHandler;
import io.nozdormu.spi.context.BeanContext;
import jakarta.enterprise.context.Dependent;
import jakarta.inject.Inject;
import jakarta.json.JsonObject;
import jakarta.json.JsonString;
import jakarta.json.JsonValue;
import jakarta.json.spi.JsonProvider;
import jakarta.json.stream.JsonCollectors;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;
import reactor.util.function.Tuples;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static io.graphoenix.spi.constant.Hammurabi.*;
import static io.graphoenix.spi.utils.NameUtil.typeNameToFieldName;

@Dependent
public class QueryDataLoader {

    private final DocumentManager documentManager;

    private final Map<String, FetchHandler> fetchHandlerMap = BeanContext.getMap(FetchHandler.class);

    private final JsonProvider jsonProvider;

    private final Map<String, Map<String, List<Tuple2<Field, String>>>> fetchMap = new HashMap<>();

    private final Map<String, Map<String, Map<String, Field>>> operationFetchMap = new HashMap<>();

    @Inject
    public QueryDataLoader(DocumentManager documentManager, JsonProvider jsonProvider) {
        this.documentManager = documentManager;
        this.jsonProvider = jsonProvider;
    }

    private void register(String path, FieldDefinition fieldDefinition, Field field, JsonValue jsonValue) {
        ObjectType objectType = documentManager.getFieldTypeDefinition(fieldDefinition).asObject();
        String packageName = objectType.getPackageNameOrError();
        String protocol = fieldDefinition.getFetchProtocolOrError();
        fetchMap.computeIfAbsent(packageName, k -> new HashMap<>());
        fetchMap.get(packageName).computeIfAbsent(protocol, k -> new ArrayList<>());

        Field fetchField = new Field();
        String fetchFrom = fieldDefinition.getFetchFromOrError();
        if (fieldDefinition.hasFetchWith()) {
            String fetchWithType = fieldDefinition.getFetchWithTypeOrError();
            String fetchWithFrom = fieldDefinition.getFetchWithFromOrError();
            String fetchWithTo = fieldDefinition.getFetchWithToOrError();

            fetchField
                    .setAlias(getAliasFromPath(path))
                    .setArguments(
                            Map.of(
                                    fetchWithFrom,
                                    Map.of(
                                            INPUT_OPERATOR_INPUT_VALUE_OPR_NAME,
                                            new EnumValue(INPUT_OPERATOR_INPUT_VALUE_EQ),
                                            INPUT_OPERATOR_INPUT_VALUE_VAL_NAME,
                                            getKey(jsonValue.asJsonObject().get(fetchFrom))
                                    )
                            )
                    )
                    .addSelection(
                            fieldDefinition.getFetchTo()
                                    .flatMap(fetchTo ->
                                            documentManager.getDocument().getObjectTypeOrError(fetchWithType).getFields().stream()
                                                    .filter(withTypeFieldDefinition -> withTypeFieldDefinition.getFetchFrom().isPresent())
                                                    .filter(withTypeFieldDefinition -> withTypeFieldDefinition.getFetchFromOrError().equals(fetchWithTo))
                                                    .findFirst()
                                                    .map(withTypeFieldDefinition ->
                                                            new Field(withTypeFieldDefinition.getName())
                                                                    .setSelections(field.getSelections())
                                                    )
                                    )
                                    .orElseGet(() -> new Field(fetchWithTo))
                                    .setArguments(
                                            fieldDefinition.getArguments().stream()
                                                    .flatMap(inputValue ->
                                                            field.getArguments().getArgument(inputValue.getName())
                                                                    .or(() -> Optional.ofNullable(inputValue.getDefaultValue())).stream()
                                                                    .map(valueWithVariable -> new AbstractMap.SimpleEntry<>(inputValue.getName(), valueWithVariable))
                                                    )
                                                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (x, y) -> y))
                                    )
                    )
                    .setName(typeNameToFieldName(fetchWithType) + (fieldDefinition.getType().hasList() ? SUFFIX_LIST : ""));
            fetchMap.get(packageName).get(protocol)
                    .add(
                            Tuples.of(
                                    fetchField,
                                    fieldDefinition.getFetchTo()
                                            .flatMap(fetchTo ->
                                                    documentManager.getDocument().getObjectTypeOrError(fetchWithType).getFields().stream()
                                                            .filter(withTypeFieldDefinition -> withTypeFieldDefinition.getFetchFrom().isPresent())
                                                            .filter(withTypeFieldDefinition -> withTypeFieldDefinition.getFetchFromOrError().equals(fetchWithTo))
                                                            .findFirst()
                                                            .map(AbstractDefinition::getName)
                                            )
                                            .orElse(fetchWithTo)
                            )
                    );
        } else {
            String fetchTo = fieldDefinition.getFetchToOrError();
            fetchField
                    .setAlias(getAliasFromPath(path))
                    .setArguments(
                            Stream
                                    .concat(
                                            Stream.of(
                                                    new AbstractMap.SimpleEntry<>(
                                                            fetchTo,
                                                            Map.of(
                                                                    INPUT_OPERATOR_INPUT_VALUE_OPR_NAME,
                                                                    new EnumValue(INPUT_OPERATOR_INPUT_VALUE_EQ),
                                                                    INPUT_OPERATOR_INPUT_VALUE_VAL_NAME,
                                                                    getKey(jsonValue.asJsonObject().get(fetchFrom))
                                                            )
                                                    )
                                            ),
                                            fieldDefinition.getArguments().stream()
                                                    .flatMap(inputValue ->
                                                            field.getArguments().getArgument(inputValue.getName())
                                                                    .or(() -> Optional.ofNullable(inputValue.getDefaultValue())).stream()
                                                                    .map(valueWithVariable -> new AbstractMap.SimpleEntry<>(inputValue.getName(), valueWithVariable))
                                                    )
                                    )
                                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (x, y) -> y))
                    )
                    .setSelections(field.getSelections())
                    .setName(typeNameToFieldName(objectType.getName()) + (fieldDefinition.getType().hasList() ? SUFFIX_LIST : ""));
            fetchMap.get(packageName).get(protocol).add(Tuples.of(fetchField, ""));
        }
    }

    protected Mono<JsonValue> fetchAll(JsonObject jsonObject) {
        return Flux.just(fetchMap.entrySet())
                .flatMap(entries -> Flux.fromStream(entries.stream()))
                .flatMap(packageEntry ->
                        Flux.just(packageEntry.getValue())
                                .flatMap(protocolMap -> Flux.just((protocolMap.entrySet()))
                                        .flatMap(entries -> Flux.fromStream(entries.stream()))
                                        .flatMap(protocolEntry ->
                                                fetchHandlerMap.get(packageEntry.getKey())
                                                        .request(
                                                                packageEntry.getKey(),
                                                                new Operation()
                                                                        .setSelections(
                                                                                protocolEntry.getValue().stream()
                                                                                        .map(Tuple2::getT1)
                                                                                        .collect(Collectors.toList())
                                                                        )
                                                        )
                                                        .flatMapMany(jsonValue ->
                                                                Flux.fromStream(
                                                                        protocolEntry.getValue().stream()
                                                                                .map(tuple2 -> {
                                                                                            if (tuple2.getT2().isBlank()) {
                                                                                                return jsonProvider.createObjectBuilder()
                                                                                                        .add("op", "add")
                                                                                                        .add("value", jsonValue.asJsonObject().getJsonObject(getPathFromAlias(tuple2.getT1().getAlias())))
                                                                                                        .build();
                                                                                            } else {
                                                                                                if (jsonValue.getValueType().equals(JsonValue.ValueType.ARRAY)) {
                                                                                                    return jsonProvider.createObjectBuilder()
                                                                                                            .add("op", "add")
                                                                                                            .add("value",
                                                                                                                    jsonValue.asJsonObject().getJsonArray(getPathFromAlias(tuple2.getT1().getAlias())).stream()
                                                                                                                            .map(item -> item.asJsonObject().get(tuple2.getT2()))
                                                                                                                            .collect(JsonCollectors.toJsonArray())
                                                                                                            )
                                                                                                            .build();
                                                                                                } else {
                                                                                                    return jsonProvider.createObjectBuilder()
                                                                                                            .add("op", "add")
                                                                                                            .add("value", jsonValue.asJsonObject().getJsonObject(getPathFromAlias(tuple2.getT1().getAlias())).get(tuple2.getT2()))
                                                                                                            .build();
                                                                                                }
                                                                                            }
                                                                                        }
                                                                                )
                                                                )
                                                        )
                                        )

                                )
                )
                .collectList()
                .map(patchList -> jsonProvider.createPatchBuilder(patchList.stream().collect(JsonCollectors.toJsonArray())).build().apply(jsonObject));
    }

    private String getKey(JsonValue jsonValue) {
        if (jsonValue.getValueType().equals(JsonValue.ValueType.STRING)) {
            return ((JsonString) jsonValue).getString();
        } else {
            return jsonValue.toString();
        }
    }

    private String getAliasFromPath(String path) {
        return path.replaceAll("/", "_");
    }

    private String getPathFromAlias(String alias) {
        return alias.replaceAll("_", "/");
    }
}
