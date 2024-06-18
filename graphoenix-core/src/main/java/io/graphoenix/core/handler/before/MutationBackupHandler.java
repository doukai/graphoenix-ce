package io.graphoenix.core.handler.before;

import com.google.common.collect.Streams;
import io.graphoenix.core.config.MutationConfig;
import io.graphoenix.core.handler.DocumentManager;
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
import io.graphoenix.spi.handler.OperationBeforeHandler;
import io.nozdormu.spi.context.PublisherBeanContext;
import jakarta.annotation.Priority;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Instance;
import jakarta.inject.Inject;
import jakarta.inject.Provider;
import jakarta.json.JsonValue;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static io.graphoenix.core.handler.before.MutationBeforeFetchHandler.MUTATION_BEFORE_FETCH_HANDLER_PRIORITY;
import static io.graphoenix.spi.constant.Hammurabi.*;
import static io.graphoenix.spi.utils.NameUtil.getAliasFromPath;
import static io.graphoenix.spi.utils.NameUtil.typeNameToFieldName;
import static io.nozdormu.spi.utils.CDIUtil.getNamedInstanceMap;

@ApplicationScoped
@Priority(MutationBackupHandler.MUTATION_BACKUP_HANDLER_PRIORITY)
public class MutationBackupHandler implements OperationBeforeHandler {

    public static final int MUTATION_BACKUP_HANDLER_PRIORITY = MUTATION_BEFORE_FETCH_HANDLER_PRIORITY - 1;

    private final DocumentManager documentManager;
    private final MutationConfig mutationConfig;
    private final Provider<TransactionCompensator> transactionCompensatorProvider;
    private final Map<String, FetchHandler> fetchHandlerMap;

    @Inject
    public MutationBackupHandler(DocumentManager documentManager, MutationConfig mutationConfig, Provider<TransactionCompensator> transactionCompensatorProvider, Instance<FetchHandler> fetchHandlerInstance) {
        this.documentManager = documentManager;
        this.mutationConfig = mutationConfig;
        this.transactionCompensatorProvider = transactionCompensatorProvider;
        this.fetchHandlerMap = getNamedInstanceMap(fetchHandlerInstance);
    }

    @Override
    public Mono<Operation> mutation(Operation operation, Map<String, JsonValue> variables) {
        if (!mutationConfig.getCompensatingTransaction()) {
            return Mono.just(operation);
        }
        ObjectType operationType = documentManager.getOperationTypeOrError(operation);
        List<FetchItem> fetchItemList = operation.getFields().stream()
                .flatMap(field -> buildFetchItems(operationType.getField(field.getName()), field))
                .collect(Collectors.toList());
        return Flux
                .fromIterable(
                        fetchItemList.stream()
                                .filter(fetchItem -> fetchItem.getFetchField() != null)
                                .collect(
                                        Collectors.groupingBy(
                                                FetchItem::getPackageName,
                                                Collectors.mapping(
                                                        fetchItem -> fetchItem,
                                                        Collectors.groupingBy(
                                                                FetchItem::getProtocol,
                                                                Collectors.toList()
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
                                                                .setOperationType(OPERATION_QUERY_NAME)
                                                                .setSelections(
                                                                        protocolEntries.getValue().stream()
                                                                                .map(FetchItem::getFetchField)
                                                                                .collect(Collectors.toList())
                                                                )
                                                )
                                                .flatMapMany(fetchJsonValue ->
                                                        Flux
                                                                .fromIterable(protocolEntries.getValue())
                                                                .filter(fetchItem -> fetchItem.getFetchField() != null)
                                                                .map(fetchItem ->
                                                                        new AbstractMap.SimpleEntry<>(fetchItem.getTypeName(), fetchJsonValue.asJsonObject().get(fetchItem.getFetchField().getAlias()))
                                                                )
                                                )
                                )
                )
                .collectList()
                .map(entryList ->
                        transactionCompensatorProvider.get()
                                .addTypeValueListMap(
                                        entryList.stream()
                                                .collect(
                                                        Collectors.groupingBy(
                                                                Map.Entry::getKey,
                                                                Collectors.mapping(
                                                                        Map.Entry::getValue,
                                                                        Collectors.toList()
                                                                )
                                                        )
                                                )
                                )
                                .addTypeFetchItemListMap(fetchItemList.stream()
                                        .filter(fetchItem -> fetchItem.getFetchField() == null)
                                        .collect(
                                                Collectors.groupingBy(
                                                        FetchItem::getTypeName,
                                                        Collectors.toList()
                                                )
                                        )
                                )
                )
                .flatMap(transactionCompensator ->
                        Mono.just(operation)
                                .contextWrite(PublisherBeanContext.of(TransactionCompensator.class, transactionCompensator))
                );
    }

    public Stream<FetchItem> buildFetchItems(FieldDefinition fieldDefinition, Field field) {
        Definition fieldTypeDefinition = documentManager.getFieldTypeDefinition(fieldDefinition);
        if (fieldTypeDefinition.isObject() && !fieldTypeDefinition.isContainer()) {
            String alias = Optional.ofNullable(field.getAlias()).orElseGet(field::getName);
            FieldDefinition idField = fieldTypeDefinition.asObject().getIDFieldOrError();
            String protocol = fieldDefinition.getFetchProtocolOrError().getValue().toLowerCase();
            String packageName = fieldDefinition.getPackageNameOrError();
            Stream<FetchItem> fetchItemStream = Stream.empty();

            if (field.getArguments().hasArgument(idField.getName()) || field.getArguments().hasArgument(INPUT_VALUE_WHERE_NAME)) {
                Field fetchField = new Field(field.getName())
                        .setAlias(alias)
                        .setSelections(
                                field.getArguments().getArguments().keySet().stream()
                                        .filter(key -> documentManager.getFieldTypeDefinition(fieldTypeDefinition.asObject().getField(key)).isLeaf())
                                        .map(Field::new)
                                        .collect(Collectors.toList())
                        )
                        .mergeSelection(new Field(idField.getName()));
                if (field.getArguments().hasArgument(idField.getName())) {
                    fetchField
                            .setArguments(
                                    Map.of(
                                            idField.getName(),
                                            Map.of(
                                                    INPUT_OPERATOR_INPUT_VALUE_VAL_NAME,
                                                    field.getArguments().getArgument(idField.getName())
                                            )
                                    )
                            );
                } else if (field.getArguments().hasArgument(INPUT_VALUE_WHERE_NAME)) {
                    fetchField.setArguments(field.getArguments().getArgument(INPUT_VALUE_WHERE_NAME).asObject());
                }
                fetchItemStream = Stream.of(
                        new FetchItem(
                                packageName,
                                protocol,
                                fieldTypeDefinition.getName(),
                                fetchField
                        )
                );
            } else if (field.getArguments().hasArgument(INPUT_VALUE_INPUT_NAME)) {
                ObjectValueWithVariable input = field.getArguments().getArgument(INPUT_VALUE_INPUT_NAME).asObject();
                if (input.containsKey(idField.getName()) || input.containsKey(INPUT_VALUE_WHERE_NAME)) {
                    Field fetchField = new Field(field.getName())
                            .setAlias(alias + "__" + INPUT_VALUE_INPUT_NAME)
                            .setSelections(
                                    input.getObjectValueWithVariable().keySet().stream()
                                            .filter(key -> documentManager.getFieldTypeDefinition(fieldTypeDefinition.asObject().getField(key)).isLeaf())
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
                    } else if (input.containsKey(INPUT_VALUE_WHERE_NAME)) {
                        fetchField.setArguments(input.getValueWithVariable(INPUT_VALUE_WHERE_NAME).asObject());
                    }
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
                                    "/" + INPUT_VALUE_INPUT_NAME + "/" + idField.getName(),
                                    fieldTypeDefinition.getName()
                            )
                    );
                }
            } else if (field.getArguments().hasArgument(INPUT_VALUE_LIST_NAME)) {
                ArrayValueWithVariable list = field.getArguments().getArgument(INPUT_VALUE_LIST_NAME).asArray();
                fetchItemStream = IntStream.range(0, list.size())
                        .mapToObj(index -> {
                                    ObjectValueWithVariable item = list.getValueWithVariable(index).asObject();
                                    if (item.containsKey(idField.getName()) || item.containsKey(INPUT_VALUE_WHERE_NAME)) {
                                        Field fetchField = new Field(field.getName())
                                                .setAlias(alias + "__" + INPUT_VALUE_LIST_NAME + "_" + index)
                                                .setSelections(
                                                        item.getObjectValueWithVariable().keySet().stream()
                                                                .filter(key -> documentManager.getFieldTypeDefinition(fieldTypeDefinition.asObject().getField(key)).isLeaf())
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
                                        } else if (item.containsKey(INPUT_VALUE_WHERE_NAME)) {
                                            fetchField.setArguments(item.getValueWithVariable(INPUT_VALUE_WHERE_NAME).asObject());
                                        }
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
                                                "/" + INPUT_VALUE_LIST_NAME + "/" + index + "/" + idField.getName(),
                                                fieldTypeDefinition.getName()
                                        );
                                    }
                                }
                        );
            }

            Stream<FetchItem> subFetchItemStream = Streams
                    .concat(
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
                                                                                    fieldTypeDefinition.asObject(),
                                                                                    field,
                                                                                    "",
                                                                                    subFieldDefinition,
                                                                                    inputValue,
                                                                                    valueWithVariable
                                                                            )
                                                                    )
                                                    )
                                    ),
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
                                                                                                                    fieldTypeDefinition.asObject(),
                                                                                                                    field,
                                                                                                                    "/" + INPUT_VALUE_INPUT_NAME,
                                                                                                                    subFieldDefinition,
                                                                                                                    subInputValue,
                                                                                                                    subValueWithVariable
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
                                                                                                                                    fieldTypeDefinition.asObject(),
                                                                                                                                    field,
                                                                                                                                    "/" + INPUT_VALUE_LIST_NAME + "/" + index,
                                                                                                                                    subFieldDefinition,
                                                                                                                                    subInputValue,
                                                                                                                                    subValueWithVariable
                                                                                                                            )
                                                                                                                    )
                                                                                                    )
                                                                                    )
                                                                    )
                                                                    .flatMap(stream -> stream)
                                                    )
                                    )
                    );
            return Stream.concat(fetchItemStream, subFetchItemStream);
        }
        return Stream.empty();
    }

    public Stream<FetchItem> buildFetchItems(ObjectType objectType, Field field, String path, FieldDefinition fieldDefinition, InputValue inputValue, ValueWithVariable valueWithVariable) {
        if (valueWithVariable.isNull()) {
            return Stream.empty();
        }
        Definition fieldTypeDefinition = documentManager.getFieldTypeDefinition(fieldDefinition);
        Definition inputValueTypeDefinition = documentManager.getInputValueTypeDefinition(inputValue);
        if (fieldTypeDefinition.isObject() && !fieldTypeDefinition.isContainer()) {
            String alias = Optional.ofNullable(field.getAlias()).orElseGet(field::getName);
            FieldDefinition idField = fieldTypeDefinition.asObject().getIDFieldOrError();
            String fetchTo = fieldDefinition.getFetchToOrError();
            Stream<FetchItem> fetchItemStream = Stream.empty();

            if (fieldDefinition.getType().hasList()) {

            } else {
                String protocol = fieldDefinition.getFetchProtocolOrError().getValue().toLowerCase();
                String packageName = fieldTypeDefinition.asObject().getPackageNameOrError();
                if (valueWithVariable.asObject().containsKey(idField.getName()) || valueWithVariable.asObject().containsKey(INPUT_VALUE_WHERE_NAME)) {
                    Field fetchField = new Field(typeNameToFieldName(fieldTypeDefinition.getName()))
                            .setAlias(alias + "__" + getAliasFromPath(path) + "_" + fieldDefinition.getName())
                            .setSelections(
                                    valueWithVariable.asObject().getObjectValueWithVariable().keySet().stream()
                                            .filter(key -> documentManager.getInputValueTypeDefinition(inputValueTypeDefinition.asInputObject().getInputValue(key)).isLeaf())
                                            .map(Field::new)
                                            .collect(Collectors.toList())
                            )
                            .mergeSelection(new Field(fetchTo))
                            .mergeSelection(new Field(idField.getName()));
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

                    } else if (valueWithVariable.asObject().containsKey(INPUT_VALUE_WHERE_NAME)) {
                        fetchField.setArguments(valueWithVariable.asObject().getValueWithVariable(INPUT_VALUE_WHERE_NAME).asObject());
                    }
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

            Stream<FetchItem> subFetchItemStream = inputValueTypeDefinition.asInputObject().getInputValues().stream()
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
            return Stream.concat(fetchItemStream, subFetchItemStream);
        }
        return Stream.empty();
    }
}
