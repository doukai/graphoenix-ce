package io.graphoenix.core.handler.before;

import com.google.common.collect.Streams;
import io.graphoenix.core.config.PackageConfig;
import io.graphoenix.core.handler.DocumentManager;
import io.graphoenix.core.handler.PackageManager;
import io.graphoenix.core.handler.fetch.FetchItem;
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
import jakarta.annotation.Priority;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Instance;
import jakarta.inject.Inject;
import jakarta.json.JsonObject;
import jakarta.json.JsonString;
import jakarta.json.JsonValue;
import jakarta.json.spi.JsonProvider;
import jakarta.json.stream.JsonCollectors;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static io.graphoenix.core.handler.before.ConnectionSplitter.CONNECTION_SPLITTER_PRIORITY;
import static io.graphoenix.core.handler.fetch.LocalFetchHandler.LOCAL_FETCH_NAME;
import static io.graphoenix.spi.constant.Hammurabi.*;
import static io.graphoenix.spi.utils.NameUtil.getAliasFromPath;
import static io.graphoenix.spi.utils.NameUtil.typeNameToFieldName;
import static io.nozdormu.spi.utils.CDIUtil.getNamedInstanceMap;

@ApplicationScoped
@Priority(MutationBeforeFetch2Handler.MUTATION_BEFORE_FETCH_HANDLER_PRIORITY)
public class MutationBeforeFetch2Handler implements OperationBeforeHandler {

    public static final int MUTATION_BEFORE_FETCH_HANDLER_PRIORITY = CONNECTION_SPLITTER_PRIORITY + 450;

    private final DocumentManager documentManager;
    private final PackageManager packageManager;
    private final PackageConfig packageConfig;
    private final JsonProvider jsonProvider;
    private final Map<String, FetchHandler> fetchHandlerMap;

    @Inject
    public MutationBeforeFetch2Handler(DocumentManager documentManager, PackageManager packageManager, PackageConfig packageConfig, JsonProvider jsonProvider, Instance<FetchHandler> fetchHandlerInstance) {
        this.documentManager = documentManager;
        this.packageManager = packageManager;
        this.packageConfig = packageConfig;
        this.jsonProvider = jsonProvider;
        this.fetchHandlerMap = getNamedInstanceMap(fetchHandlerInstance);
    }

    @Override
    public Mono<Operation> mutation(Operation operation, Map<String, JsonValue> variables) {
        ObjectType operationType = documentManager.getOperationTypeOrError(operation);
        return Flux
                .fromIterable(
                        operation.getFields().stream()
                                .flatMap(field -> buildFetchItems(operationType.getField(field.getName()), field))
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
                        Flux
                                .fromIterable(packageEntries.getValue().entrySet())
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
                                                        Flux
                                                                .fromIterable(
                                                                        FetchItem
                                                                                .buildMutationItems(protocolEntries.getValue())
                                                                                .collect(
                                                                                        Collectors.groupingBy(
                                                                                                FetchItem::getField,
                                                                                                Collectors.mapping(
                                                                                                        fetchItem -> {
                                                                                                            JsonValue fieldJsonValue = fetchJsonValue.asJsonObject().get(typeNameToFieldName(fetchItem.getTypeName()) + SUFFIX_LIST).asJsonArray().get(fetchItem.getIndex());
                                                                                                            return jsonProvider.createObjectBuilder()
                                                                                                                    .add("op", "add")
                                                                                                                    .add("path", fetchItem.getPath() + "/" + fetchItem.getFetchFrom())
                                                                                                                    .add("value", fieldJsonValue.asJsonObject().get(fetchItem.getTarget()))
                                                                                                                    .build();
                                                                                                        },
                                                                                                        Collectors.toList()
                                                                                                )
                                                                                        )
                                                                                )
                                                                                .entrySet()
                                                                )
                                                                .map(entry ->
                                                                        entry.getKey()
                                                                                .setArguments(
                                                                                        (JsonObject) jsonProvider
                                                                                                .createPatchBuilder(
                                                                                                        entry.getValue().stream()
                                                                                                                .collect(JsonCollectors.toJsonArray())
                                                                                                )
                                                                                                .build()
                                                                                                .apply(entry.getKey().getArguments())
                                                                                )
                                                                )
                                                )
                                )
                )
                .then()
                .thenReturn(operation);
    }

    public Stream<Field> buildFetchItems(FieldDefinition fieldDefinition, Field field) {
        Definition fieldTypeDefinition = documentManager.getFieldTypeDefinition(fieldDefinition);
        if (fieldTypeDefinition.isObject() && !fieldTypeDefinition.isContainer()) {
            FieldDefinition idField = fieldTypeDefinition.asObject().getIDFieldOrError();
            return Stream.ofNullable(fieldDefinition.getArguments())
                    .flatMap(Collection::stream)
                    .filter(inputValue -> inputValue.getName().equals(INPUT_VALUE_WHERE_NAME))
                    .findFirst()
                    .flatMap(inputValue ->
                            Stream.ofNullable(field.getArguments())
                                    .flatMap(arguments ->
                                            arguments.getArgumentOrEmpty(inputValue.getName())
                                                    .or(() -> Optional.ofNullable(inputValue.getDefaultValue())).stream()
                                    )
                                    .filter(ValueWithVariable::isObject)
                                    .map(valueWithVariable ->
                                            new Field(field.getName())
                                                    .setArguments(valueWithVariable.asObject())
                                                    .addSelections(argumentsToFields(fieldDefinition, field))
                                    )
                                    .findFirst()
                    )
                    .or(() ->
                            Stream.ofNullable(fieldDefinition.getArguments())
                                    .flatMap(Collection::stream)
                                    .filter(inputValue -> inputValue.getName().equals(idField.getName()))
                                    .findFirst()
                                    .flatMap(inputValue ->
                                            Stream.ofNullable(field.getArguments())
                                                    .flatMap(arguments ->
                                                            arguments.getArgumentOrEmpty(inputValue.getName())
                                                                    .or(() -> Optional.ofNullable(inputValue.getDefaultValue())).stream()
                                                    )
                                                    .filter(ValueWithVariable::isObject)
                                                    .map(valueWithVariable ->
                                                            new Field(field.getName())
                                                                    .setAlias(field.getAlias())
                                                                    .setArguments(
                                                                            Map.of(
                                                                                    idField.getName(),
                                                                                    Map.of(INPUT_OPERATOR_INPUT_VALUE_VAL_NAME, valueWithVariable)
                                                                            )
                                                                    )
                                                                    .addSelections(argumentsToFields(fieldDefinition, field))
                                                    )
                                                    .findFirst()
                                    )
                    )
                    .or(() ->
                            fieldDefinition.getArgumentOrEmpty(INPUT_VALUE_INPUT_NAME)
                                    .flatMap(inputValue ->
                                            Stream.ofNullable(field.getArguments())
                                                    .flatMap(arguments ->
                                                            arguments.getArgumentOrEmpty(inputValue.getName())
                                                                    .or(() -> Optional.ofNullable(inputValue.getDefaultValue())).stream()
                                                    )
                                                    .filter(ValueWithVariable::isObject)
                                                    .map(ValueWithVariable::asObject)
                                                    .flatMap(objectValueWithVariable -> objectValueWithVariable.getObjectValueWithVariable().entrySet().stream())
                                                    .filter(entry -> entry.getKey().equals(idField.getName()))
                                                    .map(entry ->
                                                            new Field(field.getName())
                                                                    .setAlias(field.getAlias())
                                                                    .setArguments(
                                                                            Map.of(
                                                                                    idField.getName(),
                                                                                    Map.of(INPUT_OPERATOR_INPUT_VALUE_VAL_NAME, entry.getValue())
                                                                            )
                                                                    )
                                                                    .addSelections(argumentsToFields(fieldDefinition, field))
                                                    )
                                                    .findFirst()
                                    )
                    )
                    .or(() ->
                            fieldDefinition.getArgumentOrEmpty(INPUT_VALUE_LIST_NAME)
                                    .map(inputValue ->
                                            new Field(field.getName())
                                                    .setAlias(field.getAlias())
                                                    .setArguments(
                                                            Map.of(
                                                                    idField.getName(),
                                                                    Map.of(
                                                                            INPUT_OPERATOR_INPUT_VALUE_OPR_NAME,
                                                                            new EnumValue(INPUT_OPERATOR_INPUT_VALUE_IN),
                                                                            INPUT_OPERATOR_INPUT_VALUE_ARR_NAME,
                                                                            Stream.ofNullable(field.getArguments())
                                                                                    .flatMap(arguments ->
                                                                                            arguments.getArgumentOrEmpty(inputValue.getName())
                                                                                                    .or(() -> Optional.ofNullable(inputValue.getDefaultValue())).stream()
                                                                                    )
                                                                                    .filter(ValueWithVariable::isArray)
                                                                                    .map(ValueWithVariable::asArray)
                                                                                    .flatMap(arrayValueWithVariable -> arrayValueWithVariable.getValueWithVariables().stream())
                                                                                    .filter(ValueWithVariable::isObject)
                                                                                    .map(ValueWithVariable::asObject)
                                                                                    .flatMap(objectValueWithVariable -> objectValueWithVariable.getObjectValueWithVariable().entrySet().stream())
                                                                                    .filter(entry -> entry.getKey().equals(idField.getName()))
                                                                                    .map(Map.Entry::getValue)
                                                                                    .collect(Collectors.toList())
                                                                    )
                                                            )
                                                    )
                                                    .addSelections(argumentsToFields(fieldDefinition, field))
                                    )
                    )
                    .or(() ->

                            Stream.ofNullable(fieldDefinition.getArguments())
                            .flatMap(Collection::stream)
                            .flatMap(inputValue ->
                                    Stream.ofNullable(field.getArguments())
                                            .flatMap(arguments ->
                                                    arguments.getArgumentOrEmpty(inputValue.getName())
                                                            .or(() -> Optional.ofNullable(inputValue.getDefaultValue())).stream()
                                            )
                                            .filter(ValueWithVariable::isObject)
                                            .map(valueWithVariable ->
                                                    new Field(field.getName())
                                                            .setAlias(field.getAlias())
                                                            .setArguments(
                                                                    Map.of(
                                                                            idField.getName(),
                                                                            Map.of(INPUT_OPERATOR_INPUT_VALUE_VAL_NAME, valueWithVariable)
                                                                    )
                                                            )
                                                            .addSelections(argumentsToFields(fieldDefinition, field))
                                            )
                                            .findFirst()
                            )
                    );
        }
        return Stream.empty();
    }

    public Stream<FetchItem> buildFetchItems(ObjectType objectType, Field field, String path, FieldDefinition fieldDefinition, InputValue inputValue, ValueWithVariable valueWithVariable) {
        if (valueWithVariable.isNull()) {
            return Stream.empty();
        }
        Definition fieldTypeDefinition = documentManager.getFieldTypeDefinition(fieldDefinition);
        if (fieldDefinition.isFetchField()) {
            if (!fieldDefinition.getType().hasList() && documentManager.isFetchAnchor(objectType, fieldDefinition)) {
                String protocol = fieldDefinition.getFetchProtocolOrError().getValue().toLowerCase();
                String fetchFrom = fieldDefinition.getFetchFromOrError();
                String packageName = fieldTypeDefinition.asObject().getPackageNameOrError();
                String fetchTo = fieldDefinition.getFetchToOrError();
                FieldDefinition idField = fieldTypeDefinition.asObject().getIDFieldOrError();
                String id;
                if (valueWithVariable.asJsonObject().containsKey(idField.getName())) {
                    id = getId(valueWithVariable.asJsonObject().get(idField.getName()));
                } else {
                    id = UUID.randomUUID().toString();
                }

                return Stream.of(
                        new FetchItem(
                                packageName,
                                protocol,
                                path,
                                fieldTypeDefinition.getName(),
                                valueWithVariable.asJsonObject(),
                                id,
                                fetchTo,
                                field,
                                fetchFrom
                        )
                );
            }
        } else if (fieldTypeDefinition.isObject() && !fieldTypeDefinition.isContainer()) {
            Definition inputValueTypeDefinition = documentManager.getInputValueTypeDefinition(inputValue);
            return inputValueTypeDefinition.asInputObject().getInputValues().stream()
                    .flatMap(subInputValue ->
                            Stream.ofNullable(fieldTypeDefinition.asObject().getField(subInputValue.getName()))
                                    .flatMap(subFieldDefinition -> {
                                                if (fieldDefinition.getType().hasList()) {
                                                    return IntStream.range(0, valueWithVariable.asArray().size())
                                                            .mapToObj(index ->
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
                                                                                            subValueWithVariable
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
                                                                            subValueWithVariable
                                                                    )
                                                            );
                                                }
                                            }
                                    )
                    );
        }
        return Stream.empty();
    }

    private String getId(JsonValue jsonValue) {
        if (jsonValue.getValueType().equals(JsonValue.ValueType.STRING)) {
            return ((JsonString) jsonValue).getString();
        } else {
            return jsonValue.toString();
        }
    }
}
