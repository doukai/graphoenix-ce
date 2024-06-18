package io.graphoenix.core.handler.after;

import com.google.common.collect.Streams;
import io.graphoenix.core.config.MutationConfig;
import io.graphoenix.core.handler.DocumentManager;
import io.graphoenix.core.handler.PackageManager;
import io.graphoenix.core.handler.TransactionCompensator;
import io.graphoenix.core.handler.fetch.FetchItem;
import io.graphoenix.spi.graphql.Definition;
import io.graphoenix.spi.graphql.common.ArrayValueWithVariable;
import io.graphoenix.spi.graphql.common.ObjectValueWithVariable;
import io.graphoenix.spi.graphql.common.ValueWithVariable;
import io.graphoenix.spi.graphql.operation.Field;
import io.graphoenix.spi.graphql.operation.Operation;
import io.graphoenix.spi.graphql.type.FieldDefinition;
import io.graphoenix.spi.graphql.type.InputValue;
import io.graphoenix.spi.graphql.type.ObjectType;
import io.graphoenix.spi.handler.FetchHandler;
import io.graphoenix.spi.handler.OperationAfterHandler;
import jakarta.annotation.Priority;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Instance;
import jakarta.inject.Inject;
import jakarta.inject.Provider;
import jakarta.json.JsonValue;
import jakarta.json.spi.JsonProvider;
import jakarta.json.stream.JsonCollectors;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static io.graphoenix.core.handler.after.MutationAfterFetchHandler.MUTATION_AFTER_FETCH_HANDLER_PRIORITY;
import static io.graphoenix.spi.constant.Hammurabi.*;
import static io.graphoenix.spi.utils.NameUtil.getAliasFromPath;
import static io.graphoenix.spi.utils.NameUtil.typeNameToFieldName;
import static io.nozdormu.spi.utils.CDIUtil.getNamedInstanceMap;

@ApplicationScoped
@Priority(MutationAfterBackupHandler.MUTATION_AFTER_FETCH_BACKUP_PRIORITY)
public class MutationAfterBackupHandler implements OperationAfterHandler {

    public static final int MUTATION_AFTER_FETCH_BACKUP_PRIORITY = MUTATION_AFTER_FETCH_HANDLER_PRIORITY - 1;

    private final DocumentManager documentManager;
    private final PackageManager packageManager;
    private final MutationConfig mutationConfig;
    private final Provider<TransactionCompensator> transactionCompensatorProvider;
    private final JsonProvider jsonProvider;
    private final Map<String, FetchHandler> fetchHandlerMap;

    @Inject
    public MutationAfterBackupHandler(DocumentManager documentManager, PackageManager packageManager, MutationConfig mutationConfig, Provider<TransactionCompensator> transactionCompensatorProvider, JsonProvider jsonProvider, Instance<FetchHandler> fetchHandlerInstance) {
        this.documentManager = documentManager;
        this.packageManager = packageManager;
        this.mutationConfig = mutationConfig;
        this.transactionCompensatorProvider = transactionCompensatorProvider;
        this.jsonProvider = jsonProvider;
        this.fetchHandlerMap = getNamedInstanceMap(fetchHandlerInstance);
    }

    @Override
    public Mono<JsonValue> mutation(Operation operation, JsonValue jsonValue) {
        if (!mutationConfig.getCompensatingTransaction()) {
            return Mono.just(jsonValue);
        }
        ObjectType operationType = documentManager.getOperationTypeOrError(operation);
        return Flux
                .fromIterable(
                        operation.getFields().stream()
                                .flatMap(field -> {
                                            String selectionName = Optional.ofNullable(field.getAlias()).orElseGet(field::getName);
                                            return buildFetchItems(operationType, operationType.getField(field.getName()), field, jsonValue.asJsonObject().get(selectionName));
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
                                                                .setOperationType(OPERATION_MUTATION_NAME)
                                                                .setSelections(
                                                                        FetchItem.buildMutationFields(protocolEntries.getValue())
                                                                )
                                                )
                                                .flatMapMany(fetchJsonValue ->
                                                        Flux.fromStream(
                                                                protocolEntries.getValue().stream()
                                                                        .filter(fetchItem -> fetchItem.getFetchField() != null)
                                                                        .map(fetchItem -> {
                                                                                    String path = fetchItem.getPath();
                                                                                    JsonValue fieldJsonValue = fetchJsonValue.asJsonObject().get(fetchItem.getFetchField().getAlias());
                                                                                    return jsonProvider.createObjectBuilder()
                                                                                            .add("op", "add")
                                                                                            .add("path", path)
                                                                                            .add("value", fieldJsonValue)
                                                                                            .build();
                                                                                }
                                                                        )
                                                        )
                                                )
                                )
                )
                .collectList()
                .map(patchList ->
                        (JsonValue) jsonProvider.createPatchBuilder(patchList.stream().collect(JsonCollectors.toJsonArray()))
                                .build()
                                .apply(jsonValue.asJsonObject())
                )
                .defaultIfEmpty(jsonValue);
    }

    public Stream<FetchItem> buildFetchItems(ObjectType objectType, FieldDefinition fieldDefinition, Field field, JsonValue jsonValue) {
        if (jsonValue == null || jsonValue.getValueType().equals(JsonValue.ValueType.NULL)) {
            return Stream.empty();
        }
        Definition fieldTypeDefinition = documentManager.getFieldTypeDefinition(fieldDefinition);
        if (fieldTypeDefinition.isObject() && !fieldTypeDefinition.isContainer()) {
            if (fieldDefinition.getType().hasList()) {
                return Streams
                        .concat(
                                fieldDefinition.getArgumentOrEmpty(INPUT_VALUE_INPUT_NAME).stream()
                                        .flatMap(inputValue ->
                                                Stream.ofNullable(field.getArguments())
                                                        .flatMap(arguments ->
                                                                arguments.getArgumentOrEmpty(inputValue.getName())
                                                                        .or(() -> Optional.ofNullable(inputValue.getDefaultValue())).stream()
                                                        )
                                                        .filter(ValueWithVariable::isObject)
                                                        .map(ValueWithVariable::asObject)
                                                        .flatMap(objectValueWithVariable ->
                                                                documentManager.getInputValueTypeDefinition(inputValue).asInputObject().getInputValues().stream()
                                                                        .flatMap(subInputValue ->
                                                                                Stream.ofNullable(fieldTypeDefinition.asObject().getField(subInputValue.getName()))
                                                                                        .flatMap(subFieldDefinition ->
                                                                                                objectValueWithVariable.getValueWithVariableOrEmpty(subInputValue.getName())
                                                                                                        .or(() -> Optional.ofNullable(subInputValue.getDefaultValue())).stream()
                                                                                                        .flatMap(subValueWithVariable ->
                                                                                                                buildFetchItems(
                                                                                                                        objectType,
                                                                                                                        field,
                                                                                                                        "/" + INPUT_VALUE_INPUT_NAME,
                                                                                                                        subFieldDefinition,
                                                                                                                        subInputValue,
                                                                                                                        subValueWithVariable,
                                                                                                                        jsonValue.asJsonObject()
                                                                                                                )
                                                                                                        )
                                                                                        )
                                                                        )
                                                        )
                                        ),
                                fieldDefinition.getArgumentOrEmpty(INPUT_VALUE_LIST_NAME).stream()
                                        .flatMap(listInputValue ->
                                                Stream.ofNullable(field.getArguments())
                                                        .flatMap(arguments ->
                                                                arguments.getArgumentOrEmpty(listInputValue.getName())
                                                                        .or(() -> Optional.ofNullable(listInputValue.getDefaultValue())).stream()
                                                        )
                                                        .filter(ValueWithVariable::isArray)
                                                        .map(ValueWithVariable::asArray)
                                                        .flatMap(arrayValueWithVariable ->
                                                                IntStream.range(0, arrayValueWithVariable.size())
                                                                        .mapToObj(index ->
                                                                                documentManager.getInputValueTypeDefinition(listInputValue).asInputObject().getInputValues().stream()
                                                                                        .flatMap(subInputValue ->
                                                                                                Stream.ofNullable(fieldTypeDefinition.asObject().getField(subInputValue.getName()))
                                                                                                        .flatMap(subFieldDefinition ->
                                                                                                                arrayValueWithVariable.getValueWithVariable(index).asObject().getValueWithVariableOrEmpty(subInputValue.getName())
                                                                                                                        .or(() -> Optional.ofNullable(subInputValue.getDefaultValue())).stream()
                                                                                                                        .flatMap(subValueWithVariable ->
                                                                                                                                buildFetchItems(
                                                                                                                                        objectType,
                                                                                                                                        field,
                                                                                                                                        "/" + INPUT_VALUE_LIST_NAME + "/" + index,
                                                                                                                                        subFieldDefinition,
                                                                                                                                        subInputValue,
                                                                                                                                        subValueWithVariable,
                                                                                                                                        jsonValue.asJsonArray().get(index)
                                                                                                                                )
                                                                                                                        )
                                                                                                        )
                                                                                        )
                                                                        )
                                                                        .flatMap(stream -> stream)
                                                        )
                                        ),
                                jsonValue.asJsonArray().stream()
                                        .map(item ->
                                                Stream.ofNullable(fieldDefinition.getArguments())
                                                        .flatMap(Collection::stream)
                                                        .flatMap(inputValue ->
                                                                Stream.ofNullable(fieldTypeDefinition.asObject().getField(inputValue.getName()))
                                                                        .flatMap(subFieldDefinition ->
                                                                                Stream.ofNullable(field.getArguments())
                                                                                        .flatMap(arguments ->
                                                                                                arguments.getArgumentOrEmpty(inputValue.getName())
                                                                                                        .or(() -> Optional.ofNullable(inputValue.getDefaultValue())).stream()
                                                                                        )
                                                                                        .flatMap(valueWithVariable ->
                                                                                                buildFetchItems(
                                                                                                        objectType,
                                                                                                        field,
                                                                                                        "",
                                                                                                        subFieldDefinition,
                                                                                                        inputValue,
                                                                                                        valueWithVariable,
                                                                                                        item
                                                                                                )
                                                                                        )
                                                                        )
                                                        )
                                        )
                                        .flatMap(stream -> stream)
                        );
            } else {
                return Stream.ofNullable(fieldDefinition.getArguments())
                        .flatMap(Collection::stream)
                        .flatMap(inputValue ->
                                Stream.ofNullable(fieldTypeDefinition.asObject().getField(inputValue.getName()))
                                        .flatMap(subFieldDefinition ->
                                                Stream.ofNullable(field.getArguments())
                                                        .flatMap(arguments ->
                                                                arguments.getArgumentOrEmpty(inputValue.getName())
                                                                        .or(() -> Optional.ofNullable(inputValue.getDefaultValue())).stream()
                                                        )
                                                        .flatMap(valueWithVariable ->
                                                                buildFetchItems(
                                                                        objectType,
                                                                        field,
                                                                        "",
                                                                        subFieldDefinition,
                                                                        inputValue,
                                                                        valueWithVariable,
                                                                        jsonValue
                                                                )
                                                        )
                                        )
                        );
            }
        }
        return Stream.empty();
    }

    public Stream<FetchItem> buildFetchItems(ObjectType objectType, Field field, String path, FieldDefinition fieldDefinition, InputValue inputValue, ValueWithVariable valueWithVariable, JsonValue jsonValue) {
        if (valueWithVariable.isNull() || jsonValue == null || jsonValue.getValueType().equals(JsonValue.ValueType.NULL)) {
            return Stream.empty();
        }
        Definition fieldTypeDefinition = documentManager.getFieldTypeDefinition(fieldDefinition);
        Definition inputValueTypeDefinition = documentManager.getInputValueTypeDefinition(inputValue);
        String alias = Optional.ofNullable(field.getAlias()).orElseGet(field::getName);
        FieldDefinition idField = fieldTypeDefinition.asObject().getIDFieldOrError();
        Stream<FetchItem> fetchItemStream = Stream.empty();
        Stream<FetchItem> subFetchItemStream = Stream.empty();
        if (documentManager.isMutationOperationType(objectType) && !packageManager.isLocalPackage(fieldDefinition)) {
            String protocol = fieldDefinition.getFetchProtocolOrError().getValue().toLowerCase();
            String packageName = fieldDefinition.getPackageNameOrError();
            Field fetchField = new Field(typeNameToFieldName(fieldTypeDefinition.getName()));

            if (field.getArguments().hasArgument(idField.getName())) {
                fetchField
                        .setAlias(alias)
                        .setArguments(
                                Map.of(
                                        idField.getName(),
                                        Map.of(
                                                INPUT_OPERATOR_INPUT_VALUE_VAL_NAME,
                                                field.getArguments().getArgument(idField.getName())
                                        )
                                )
                        )
                        .setSelections(
                                fetchField.getArguments().getArguments().keySet().stream()
                                        .filter(key -> documentManager.getInputValueTypeDefinition(inputValueTypeDefinition.asInputObject().getInputValue(key)).isLeaf())
                                        .map(Field::new)
                                        .collect(Collectors.toList())
                        )
                        .mergeSelection(new Field(idField.getName()));
                fetchItemStream = Stream.of(
                        new FetchItem(
                                packageName,
                                protocol,
                                fieldTypeDefinition.getName(),
                                fetchField
                        )
                );
            } else if (field.getArguments().hasArgument(INPUT_VALUE_WHERE_NAME)) {
                fetchField
                        .setAlias(alias)
                        .setArguments(field.getArguments().getArgument(INPUT_VALUE_WHERE_NAME).asObject())
                        .setSelections(
                                fetchField.getArguments().getArguments().keySet().stream()
                                        .filter(key -> documentManager.getInputValueTypeDefinition(inputValueTypeDefinition.asInputObject().getInputValue(key)).isLeaf())
                                        .map(Field::new)
                                        .collect(Collectors.toList())
                        )
                        .mergeSelection(new Field(idField.getName()));
                fetchItemStream = Stream.of(
                        new FetchItem(
                                packageName,
                                protocol,
                                fieldTypeDefinition.getName(),
                                fetchField
                        )
                );
            } else if (field.getArguments().hasArgument(INPUT_VALUE_INPUT_NAME)) {
                ObjectValueWithVariable input = fetchField.getArguments().getArgument(INPUT_VALUE_INPUT_NAME).asObject();
                fetchField
                        .setAlias(alias + "__" + INPUT_VALUE_INPUT_NAME)
                        .setSelections(
                                input.getObjectValueWithVariable().keySet().stream()
                                        .filter(key -> documentManager.getInputValueTypeDefinition(inputValueTypeDefinition.asInputObject().getInputValue(key)).isLeaf())
                                        .map(Field::new)
                                        .collect(Collectors.toList())
                        )
                        .mergeSelection(new Field(idField.getName()));
                if (input.containsKey(idField.getName())) {
                    fetchField
                            .setArguments(
                                    Map.of(
                                            idField.getName(),
                                            Map.of(
                                                    INPUT_OPERATOR_INPUT_VALUE_VAL_NAME,
                                                    input.getValueWithVariable(idField.getName())
                                            )
                                    )
                            );
                    fetchItemStream = Stream.of(
                            new FetchItem(
                                    packageName,
                                    protocol,
                                    fieldTypeDefinition.getName(),
                                    fetchField
                            )
                    );
                } else if (input.containsKey(INPUT_VALUE_WHERE_NAME)) {
                    fetchField.setArguments(input.getValueWithVariable(INPUT_VALUE_WHERE_NAME).asObject());
                    fetchItemStream = Stream.of(
                            new FetchItem(
                                    packageName,
                                    protocol,
                                    fieldTypeDefinition.getName(),
                                    fetchField
                            )
                    );
                } else {
                    fetchItemStream = Stream.of(
                            new FetchItem(
                                    packageName,
                                    protocol,
                                    field,
                                    path + "/" + idField.getName(),
                                    fieldTypeDefinition.getName()
                            )
                    );
                }
            } else if (field.getArguments().hasArgument(INPUT_VALUE_LIST_NAME)) {
                ArrayValueWithVariable list = fetchField.getArguments().getArgument(INPUT_VALUE_LIST_NAME).asArray();
                fetchItemStream = IntStream.range(0, list.size())
                        .mapToObj(index -> {
                                    ObjectValueWithVariable item = list.getValueWithVariable(index).asObject();
                                    fetchField
                                            .setAlias(alias + "__" + INPUT_VALUE_LIST_NAME + "_" + index)
                                            .setSelections(
                                                    item.getObjectValueWithVariable().keySet().stream()
                                                            .filter(key -> documentManager.getInputValueTypeDefinition(inputValueTypeDefinition.asInputObject().getInputValue(key)).isLeaf())
                                                            .map(Field::new)
                                                            .collect(Collectors.toList())
                                            )
                                            .mergeSelection(new Field(idField.getName()));
                                    if (item.containsKey(idField.getName())) {
                                        fetchField
                                                .setArguments(
                                                        Map.of(
                                                                idField.getName(),
                                                                Map.of(
                                                                        INPUT_OPERATOR_INPUT_VALUE_VAL_NAME,
                                                                        item.getValueWithVariable(idField.getName())
                                                                )
                                                        )
                                                );
                                        return new FetchItem(
                                                packageName,
                                                protocol,
                                                fieldTypeDefinition.getName(),
                                                fetchField
                                        );
                                    } else if (item.containsKey(INPUT_VALUE_WHERE_NAME)) {
                                        fetchField.setArguments(item.getValueWithVariable(INPUT_VALUE_WHERE_NAME).asObject());
                                        return new FetchItem(
                                                packageName,
                                                protocol,
                                                fieldTypeDefinition.getName(),
                                                fetchField
                                        );
                                    } else {
                                        return new FetchItem(
                                                packageName,
                                                protocol,
                                                field,
                                                path + "/" + index + "/" + idField.getName(),
                                                fieldTypeDefinition.getName()
                                        );
                                    }
                                }
                        );
            }
        } else if (fieldDefinition.isFetchField()) {
            if (fieldTypeDefinition.isObject()) {
                String protocol = fieldDefinition.getFetchProtocolOrError().getValue().toLowerCase();
                String packageName = fieldTypeDefinition.asObject().getPackageNameOrError();
                String fetchTo = fieldDefinition.getFetchToOrError();
                if (fieldDefinition.getType().hasList()) {
                    fetchItemStream = IntStream.range(0, valueWithVariable.asArray().size())
                            .mapToObj(index -> {
                                        ObjectValueWithVariable item = valueWithVariable.asArray().getValueWithVariable(index).asObject();
                                        Field fetchField = new Field(typeNameToFieldName(fieldTypeDefinition.getName()))
                                                .setAlias(alias + "__" + getAliasFromPath(path) + "_" + fieldDefinition.getName() + "_" + index)
                                                .setSelections(
                                                        item.getObjectValueWithVariable().keySet().stream()
                                                                .filter(key -> documentManager.getInputValueTypeDefinition(inputValueTypeDefinition.asInputObject().getInputValue(key)).isLeaf())
                                                                .map(Field::new)
                                                                .collect(Collectors.toList())
                                                )
                                                .mergeSelection(new Field(fetchTo))
                                                .mergeSelection(new Field(idField.getName()));
                                        if (item.containsKey(idField.getName())) {
                                            fetchField
                                                    .setArguments(
                                                            Map.of(
                                                                    idField.getName(),
                                                                    Map.of(
                                                                            INPUT_OPERATOR_INPUT_VALUE_VAL_NAME,
                                                                            item.getValueWithVariable(idField.getName())
                                                                    )
                                                            )
                                                    );
                                            return new FetchItem(
                                                    packageName,
                                                    protocol,
                                                    fieldTypeDefinition.getName(),
                                                    fetchField
                                            );
                                        } else if (item.containsKey(INPUT_VALUE_WHERE_NAME)) {
                                            fetchField.setArguments(item.getValueWithVariable(INPUT_VALUE_WHERE_NAME).asObject());
                                            return new FetchItem(
                                                    packageName,
                                                    protocol,
                                                    fieldTypeDefinition.getName(),
                                                    fetchField
                                            );
                                        } else {
                                            return new FetchItem(
                                                    packageName,
                                                    protocol,
                                                    field,
                                                    path + "/" + index + "/" + idField.getName(),
                                                    fieldTypeDefinition.getName()
                                            );
                                        }
                                    }
                            );
                } else {
                    if (valueWithVariable.asObject().containsKey(idField.getName()) || valueWithVariable.asObject().containsKey(INPUT_VALUE_WHERE_NAME)) {
                        Field fetchField = new Field(typeNameToFieldName(fieldTypeDefinition.getName()))
                                .setAlias(alias + "__" + getAliasFromPath(path) + "_" + fieldDefinition.getName());
                        if (valueWithVariable.asObject().containsKey(idField.getName())) {
                            fetchField
                                    .setArguments(
                                            Map.of(
                                                    idField.getName(),
                                                    Map.of(
                                                            INPUT_OPERATOR_INPUT_VALUE_VAL_NAME,
                                                            valueWithVariable.asObject().getValueWithVariable(idField.getName())
                                                    )
                                            )
                                    );
                        } else {
                            fetchField.setArguments(valueWithVariable.asObject().getValueWithVariable(INPUT_VALUE_WHERE_NAME).asObject());
                        }
                        fetchField
                                .setSelections(
                                        valueWithVariable.asObject().getObjectValueWithVariable().keySet().stream()
                                                .filter(key -> documentManager.getInputValueTypeDefinition(inputValueTypeDefinition.asInputObject().getInputValue(key)).isLeaf())
                                                .map(Field::new)
                                                .collect(Collectors.toList())
                                )
                                .mergeSelection(new Field(fetchTo))
                                .mergeSelection(new Field(idField.getName()));

                        fetchItemStream = Stream.of(
                                new FetchItem(
                                        packageName,
                                        protocol,
                                        fieldTypeDefinition.getName(),
                                        fetchField
                                )
                        );
                    } else {
                        fetchItemStream = Stream.of(
                                new FetchItem(
                                        packageName,
                                        protocol,
                                        field,
                                        path + "/" + idField.getName(),
                                        fieldTypeDefinition.getName()
                                )
                        );
                    }

                }

            }
        }

        JsonValue fieldJsonValue = jsonValue.asJsonObject().get(fieldDefinition.getName());
        if (fieldTypeDefinition.isObject() && fieldJsonValue != null) {
            subFetchItemStream = inputValueTypeDefinition.asInputObject().getInputValues().stream()
                    .flatMap(subInputValue ->
                            Stream.ofNullable(fieldTypeDefinition.asObject().getField(subInputValue.getName()))
                                    .flatMap(subFieldDefinition -> {
                                                if (fieldDefinition.getType().hasList()) {
                                                    return IntStream.range(0, valueWithVariable.asArray().size())
                                                            .mapToObj(index ->
                                                                    fieldJsonValue.asJsonArray().stream()
                                                                            .filter(item -> item.getValueType().equals(JsonValue.ValueType.OBJECT))
                                                                            .map(JsonValue::asJsonObject)
                                                                            .filter(item ->
                                                                                    valueWithVariable.asArray().getValueWithVariable(index).asJsonObject().containsKey(idField.getName()) &&
                                                                                            valueWithVariable.asArray().getValueWithVariable(index).asJsonObject().get(idField.getName()).toString().equals(item.get(idField.getName()).toString()) ||
                                                                                            valueWithVariable.asArray().getValueWithVariable(index).asJsonObject().containsKey(INPUT_VALUE_WHERE_NAME) &&
                                                                                                    valueWithVariable.asArray().getValueWithVariable(index).asJsonObject().getJsonObject(INPUT_VALUE_WHERE_NAME)
                                                                                                            .getJsonObject(idField.getName())
                                                                                                            .get(INPUT_OPERATOR_INPUT_VALUE_VAL_NAME).toString()
                                                                                                            .equals(item.get(idField.getName()).toString())
                                                                            )
                                                                            .flatMap(item ->
                                                                                    Stream.ofNullable(valueWithVariable.asArray().getValueWithVariable(index).asObject().getObjectValueWithVariable())
                                                                                            .flatMap(objectValue ->
                                                                                                    Optional.ofNullable(objectValue.get(subInputValue.getName()))
                                                                                                            .or(() -> Optional.ofNullable(subInputValue.getDefaultValue())).stream()
                                                                                            )
                                                                                            .flatMap(subValueWithVariable ->
                                                                                                    buildFetchItems(
                                                                                                            fieldTypeDefinition.asObject(),
                                                                                                            field,
                                                                                                            path + "/" + fieldDefinition.getName() + "/" + index,
                                                                                                            subFieldDefinition,
                                                                                                            subInputValue,
                                                                                                            subValueWithVariable,
                                                                                                            item
                                                                                                    )
                                                                                            )
                                                                            )
                                                            )
                                                            .flatMap(stream -> stream);
                                                } else {
                                                    return Stream.ofNullable(valueWithVariable.asObject().getObjectValueWithVariable())
                                                            .flatMap(objectValue ->
                                                                    Optional.ofNullable(objectValue.get(subInputValue.getName()))
                                                                            .or(() -> Optional.ofNullable(subInputValue.getDefaultValue())).stream()
                                                            )
                                                            .flatMap(subValueWithVariable ->
                                                                    buildFetchItems(
                                                                            fieldTypeDefinition.asObject(),
                                                                            field,
                                                                            path + "/" + fieldDefinition.getName(),
                                                                            subFieldDefinition,
                                                                            subInputValue,
                                                                            subValueWithVariable,
                                                                            fieldJsonValue
                                                                    )
                                                            );
                                                }
                                            }
                                    )
                    );

        }
        return Stream.concat(fetchItemStream, subFetchItemStream);
    }
}
