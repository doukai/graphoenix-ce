package io.graphoenix.core.handler.before;

import io.graphoenix.core.handler.DocumentManager;
import io.graphoenix.core.handler.fetch.FetchItem;
import io.graphoenix.spi.error.GraphQLErrors;
import io.graphoenix.spi.graphql.Definition;
import io.graphoenix.spi.graphql.common.EnumValue;
import io.graphoenix.spi.graphql.common.ValueWithVariable;
import io.graphoenix.spi.graphql.operation.Field;
import io.graphoenix.spi.graphql.operation.Operation;
import io.graphoenix.spi.graphql.type.FieldDefinition;
import io.graphoenix.spi.graphql.type.InputValue;
import io.graphoenix.spi.graphql.type.ObjectType;
import io.graphoenix.spi.handler.FetchHandler;
import io.graphoenix.spi.handler.OperationBeforeHandler;
import io.nozdormu.spi.context.BeanContext;
import jakarta.annotation.Priority;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.json.JsonObject;
import jakarta.json.JsonValue;
import jakarta.json.spi.JsonProvider;
import jakarta.json.stream.JsonCollectors;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static io.graphoenix.spi.constant.Hammurabi.*;
import static io.graphoenix.spi.error.GraphQLErrorType.FETCH_WITH_TO_OBJECT_FIELD_NOT_EXIST;
import static io.graphoenix.spi.utils.NameUtil.typeNameToFieldName;

@ApplicationScoped
@Priority(600)
public class QueryBeforeFetchHandler implements OperationBeforeHandler {

    private final DocumentManager documentManager;
    private final JsonProvider jsonProvider;
    private final Map<String, FetchHandler> fetchHandlerMap = BeanContext.getMap(FetchHandler.class);

    @Inject
    public QueryBeforeFetchHandler(DocumentManager documentManager, JsonProvider jsonProvider) {
        this.documentManager = documentManager;
        this.jsonProvider = jsonProvider;
    }

    @Override
    public Mono<Operation> query(Operation operation, Map<String, JsonValue> variables) {
        return handle(operation, variables);
    }

    @Override
    public Mono<Operation> mutation(Operation operation, Map<String, JsonValue> variables) {
        return null;
    }

    @Override
    public Mono<Operation> subscription(Operation operation, Map<String, JsonValue> variables) {
        return handle(operation, variables);
    }

    public Mono<Operation> handle(Operation operation, Map<String, JsonValue> variables) {
        ObjectType operationType = documentManager.getOperationTypeOrError(operation);
        return Flux
                .fromIterable(
                        operation.getFields().stream()
                                .flatMap(field -> {
                                            String selectionName = Optional.ofNullable(field.getAlias()).orElse(field.getName());
                                            return buildFetchItems("/" + selectionName, operationType.getField(field.getName()), field);
                                        }
                                )
                                .collect(
                                        Collectors.groupingBy(
                                                FetchItem::getPackageName,
                                                Collectors.mapping(
                                                        fetchItem -> fetchItem,
                                                        Collectors.groupingBy(
                                                                FetchItem::getProtocol,
                                                                Collectors.mapping(
                                                                        fetchItem -> fetchItem,
                                                                        Collectors.toList()
                                                                )
                                                        )
                                                )
                                        )
                                )
                                .entrySet()
                )
                .flatMap(packageEntries ->
                        Flux.fromIterable(packageEntries.getValue().entrySet())
                                .flatMap(protocolEntries ->
                                        fetchHandlerMap.get(protocolEntries.getKey())
                                                .request(
                                                        packageEntries.getKey(),
                                                        new Operation()
                                                                .setSelections(
                                                                        protocolEntries.getValue().stream()
                                                                                .map(FetchItem::getFetchField)
                                                                                .filter(Objects::nonNull)
                                                                                .collect(Collectors.toList())
                                                                )
                                                )
                                                .flatMapMany(fetchJsonValue ->
                                                        Flux
                                                                .fromIterable(protocolEntries.getValue())
                                                                .doOnNext(fetchItem -> {
                                                                            String path = fetchItem.getPath();
                                                                            JsonValue fieldJsonValue = fetchJsonValue.asJsonObject().get(fetchItem.getFetchField().getAlias());
                                                                            jsonProvider.createPatchBuilder()
                                                                                    .add(
                                                                                            path,
                                                                                            jsonProvider.createObjectBuilder()
                                                                                                    .add(INPUT_OPERATOR_INPUT_VALUE_OPR_NAME, new EnumValue(INPUT_OPERATOR_INPUT_VALUE_IN))
                                                                                                    .add(
                                                                                                            INPUT_OPERATOR_INPUT_VALUE_ARR_NAME,
                                                                                                            fieldJsonValue.asJsonArray().stream()
                                                                                                                    .filter(item -> item.getValueType().equals(JsonValue.ValueType.OBJECT))
                                                                                                                    .map(item -> item.asJsonObject().get(fetchItem.getTarget()))
                                                                                                                    .collect(JsonCollectors.toJsonArray())
                                                                                                    )
                                                                                                    .build()
                                                                                    )
                                                                                    .build()
                                                                                    .apply(fetchItem.getJsonObject());
                                                                        }
                                                                )
                                                )
                                )
                )
                .then()
                .thenReturn(operation);
    }

    public Stream<FetchItem> buildFetchItems(String path, FieldDefinition fieldDefinition, Field field) {
        Definition fieldTypeDefinition = documentManager.getFieldTypeDefinition(fieldDefinition);
        if (fieldTypeDefinition.isObject() && !fieldTypeDefinition.isContainer()) {
            return Stream
                    .concat(
                            Stream.ofNullable(field.getFields())
                                    .flatMap(Collection::stream)
                                    .flatMap(subField -> {
                                                String selectionName = Optional.ofNullable(subField.getAlias()).orElse(subField.getName());
                                                return buildFetchItems(path + "/" + selectionName, fieldTypeDefinition.asObject().getField(subField.getName()), subField);
                                            }
                                    ),
                            fieldTypeDefinition.asObject().getFields().stream()
                                    .flatMap(subFieldDefinition ->
                                            fieldDefinition.getArgument(subFieldDefinition.getName()).stream()
                                                    .flatMap(inputValue ->
                                                            Stream.ofNullable(field.getArguments())
                                                                    .flatMap(arguments ->
                                                                            arguments.getArgument(inputValue.getName())
                                                                                    .or(() -> Optional.ofNullable(inputValue.getDefaultValue())).stream()
                                                                    )
                                                                    .flatMap(valueWithVariable ->
                                                                            buildFetchItems(
                                                                                    path,
                                                                                    field.getArguments(),
                                                                                    "/" + subFieldDefinition.getName(),
                                                                                    subFieldDefinition,
                                                                                    inputValue,
                                                                                    valueWithVariable
                                                                            )
                                                                    )
                                                    )
                                    )
                    );
        }
        return Stream.empty();
    }

    public Stream<FetchItem> buildFetchItems(String fieldPath, JsonObject jsonObject, String path, FieldDefinition fieldDefinition, InputValue inputValue, ValueWithVariable valueWithVariable) {
        Definition fieldTypeDefinition = documentManager.getFieldTypeDefinition(fieldDefinition);
        if (fieldDefinition.isFetchField()) {
            String protocol = fieldDefinition.getFetchProtocolOrError();
            Field fetchField = new Field();
            if (fieldDefinition.hasFetchWith()) {
                ObjectType fetchWithType = documentManager.getDocument().getObjectTypeOrError(fieldDefinition.getFetchWithTypeOrError());
                String packageName = fetchWithType.getPackageNameOrError();
                String fetchWithFrom = fieldDefinition.getFetchWithFromOrError();
                String fetchWithTo = fieldDefinition.getFetchWithToOrError();
                fetchField
                        .setAlias(getAliasFromPath(fieldPath) + "__" + getAliasFromPath(path))
                        .setArguments(
                                Map.of(
                                        fetchWithType.getFields().stream()
                                                .filter(withTypeFieldDefinition -> withTypeFieldDefinition.getFetchFrom().isPresent())
                                                .filter(withTypeFieldDefinition -> withTypeFieldDefinition.getFetchFromOrError().equals(fetchWithTo))
                                                .findFirst()
                                                .orElseThrow(() -> new GraphQLErrors(FETCH_WITH_TO_OBJECT_FIELD_NOT_EXIST.bind(fetchWithTo)))
                                                .getName(),
                                        valueWithVariable
                                )
                        )
                        .addSelection(new Field(fetchWithFrom))
                        .setName(typeNameToFieldName(fetchWithType.getName()) + (fieldDefinition.getType().hasList() ? SUFFIX_LIST : ""));

                return Stream.of(new FetchItem(packageName, protocol, path, fetchField, fetchWithFrom, jsonObject));
            } else {
                String packageName = fieldTypeDefinition.asObject().getPackageNameOrError();
                String fetchTo = fieldDefinition.getFetchToOrError();
                fetchField
                        .setAlias(getAliasFromPath(fieldPath) + "__" + getAliasFromPath(path))
                        .setArguments(valueWithVariable.asObject())
                        .addSelection(new Field(fetchTo))
                        .setName(typeNameToFieldName(fieldTypeDefinition.getName()) + (fieldDefinition.getType().hasList() ? SUFFIX_LIST : ""));

                return Stream.of(new FetchItem(packageName, protocol, path, fetchField, fetchTo, jsonObject));
            }
        } else if (fieldTypeDefinition.isObject() && !fieldTypeDefinition.isContainer()) {
            return fieldTypeDefinition.asObject().getFields().stream()
                    .flatMap(subFieldDefinition ->
                            inputValue.asInputObject().getInputValue(subFieldDefinition.getName()).stream()
                                    .flatMap(subInputValue ->
                                            Stream.ofNullable(valueWithVariable.asObject().getObjectValueWithVariable())
                                                    .flatMap(objectValue ->
                                                            Optional.ofNullable(objectValue.get(subInputValue.getName()))
                                                                    .or(() -> Optional.ofNullable(subInputValue.getDefaultValue())).stream()
                                                    )
                                                    .flatMap(subValueWithVariable ->
                                                            buildFetchItems(
                                                                    fieldPath,
                                                                    valueWithVariable.asObject(),
                                                                    path + "/" + subFieldDefinition.getName(),
                                                                    subFieldDefinition,
                                                                    subInputValue,
                                                                    subValueWithVariable
                                                            )
                                                    )
                                    )
                    );
        }
        return Stream.empty();
    }

    private String getAliasFromPath(String path) {
        return path.replaceAll("/", "_");
    }
}
