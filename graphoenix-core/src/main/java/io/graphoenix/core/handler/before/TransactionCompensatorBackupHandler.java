package io.graphoenix.core.handler.before;

import com.google.common.collect.Streams;
import io.graphoenix.core.config.MutationConfig;
import io.graphoenix.core.config.PackageConfig;
import io.graphoenix.core.handler.DocumentManager;
import io.graphoenix.core.handler.PackageManager;
import io.graphoenix.core.handler.TransactionCompensator;
import io.graphoenix.core.handler.fetch.FetchItem;
import io.graphoenix.spi.error.GraphQLErrors;
import io.graphoenix.spi.graphql.AbstractDefinition;
import io.graphoenix.spi.graphql.Definition;
import io.graphoenix.spi.graphql.common.*;
import io.graphoenix.spi.graphql.operation.Field;
import io.graphoenix.spi.graphql.operation.Operation;
import io.graphoenix.spi.graphql.type.FieldDefinition;
import io.graphoenix.spi.graphql.type.InputValue;
import io.graphoenix.spi.graphql.type.ObjectType;
import io.graphoenix.spi.handler.OperationBeforeHandler;
import io.graphoenix.spi.handler.PackageFetchHandler;
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
import static io.graphoenix.spi.error.GraphQLErrorType.FETCH_WITH_TO_OBJECT_FIELD_NOT_EXIST;
import static io.graphoenix.spi.utils.NameUtil.getAliasFromPath;
import static io.graphoenix.spi.utils.NameUtil.typeNameToFieldName;
import static io.nozdormu.spi.utils.CDIUtil.getNamedInstanceMap;

@ApplicationScoped
@Priority(TransactionCompensatorBackupHandler.TRANSACTION_COMPENSATOR_BACKUP_HANDLER_PRIORITY)
public class TransactionCompensatorBackupHandler implements OperationBeforeHandler {

    public static final int TRANSACTION_COMPENSATOR_BACKUP_HANDLER_PRIORITY = MUTATION_BEFORE_FETCH_HANDLER_PRIORITY - 1;

    private final DocumentManager documentManager;
    private final PackageManager packageManager;
    private final PackageConfig packageConfig;
    private final MutationConfig mutationConfig;
    private final Provider<Mono<TransactionCompensator>> transactionCompensatorProvider;
    private final Map<String, PackageFetchHandler> packageFetchHandlerMap;

    @Inject
    public TransactionCompensatorBackupHandler(DocumentManager documentManager, PackageManager packageManager, MutationConfig mutationConfig, PackageConfig packageConfig, Provider<Mono<TransactionCompensator>> transactionCompensatorProvider, Instance<PackageFetchHandler> fetchHandlerInstance) {
        this.documentManager = documentManager;
        this.packageManager = packageManager;
        this.packageConfig = packageConfig;
        this.mutationConfig = mutationConfig;
        this.transactionCompensatorProvider = transactionCompensatorProvider;
        this.packageFetchHandlerMap = getNamedInstanceMap(fetchHandlerInstance);
    }

    @Override
    public Mono<Operation> mutation(Operation operation, Map<String, JsonValue> variables) {
        if (!mutationConfig.getCompensatingTransaction()) {
            return Mono.just(operation);
        }
        ObjectType operationType = documentManager.getOperationTypeOrError(operation);
        return transactionCompensatorProvider.get()
                .flatMap(transactionCompensator -> {
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
                                                            packageFetchHandlerMap.get(protocolEntries.getKey())
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
                                            transactionCompensator
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
                                                            .filter(fetchItem -> fetchItem.getTarget() == null)
                                                            .collect(
                                                                    Collectors.groupingBy(
                                                                            FetchItem::getTypeName,
                                                                            Collectors.toList()
                                                                    )
                                                            )
                                                    )
                                                    .addRelationTypeValueListMap(fetchItemList.stream()
                                                            .filter(fetchItem -> fetchItem.getFetchField() == null)
                                                            .filter(fetchItem -> fetchItem.getTarget() != null)
                                                            .collect(
                                                                    Collectors.groupingBy(
                                                                            FetchItem::getTypeName,
                                                                            Collectors.toList()
                                                                    )
                                                            )
                                                    )
                                    )
                                    .thenReturn(
                                            operation
                                                    .mergeSelection(
                                                            operation.getFields().stream()
                                                                    .flatMap(field -> mergeIDField(operationType.getField(field.getName()), field))
                                                                    .collect(Collectors.toList())
                                                    )
                                    );
                        }
                )
                .defaultIfEmpty(operation);
    }

    public Stream<FetchItem> buildFetchItems(FieldDefinition fieldDefinition, Field field) {
        Definition fieldTypeDefinition = documentManager.getFieldTypeDefinition(fieldDefinition);
        if (fieldTypeDefinition.isObject() && !fieldTypeDefinition.isContainer()) {
            String alias = Optional.ofNullable(field.getAlias()).orElseGet(field::getName);
            FieldDefinition idField = fieldTypeDefinition.asObject().getIDFieldOrError();
            String protocol = packageManager.isLocalPackage(fieldDefinition) ?
                    ENUM_PROTOCOL_ENUM_VALUE_LOCAL :
                    fieldDefinition.getFetchProtocol().map(EnumValue::getValue).orElse(packageConfig.getDefaultFetchProtocol());

            String packageName = fieldDefinition.getPackageNameOrError();
            ValueWithVariable idValueWithVariable = field.getArguments().getArgument(idField.getName());

            Stream<FetchItem> fetchItemStream = Stream.empty();
            if (!packageManager.isLocalPackage(packageName)) {
                if (field.getArguments().hasArgument(idField.getName()) || field.getArguments().hasArgument(INPUT_VALUE_WHERE_NAME)) {
                    Field fetchField = new Field(field.getName())
                            .setAlias(alias)
                            .setSelections(
                                    field.getArguments().getArguments().keySet().stream()
                                            .filter(key -> documentManager.getFieldTypeDefinition(fieldTypeDefinition.asObject().getField(key)).isLeaf())
                                            .map(Field::new)
                                            .collect(Collectors.toList())
                            )
                            .mergeSelection(new Field(idField.getName()))
                            .mergeSelection(new Field(FIELD_DEPRECATED_NAME));
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
                                .mergeSelection(new Field(idField.getName()))
                                .mergeSelection(new Field(FIELD_DEPRECATED_NAME));
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
                                        "/" + idField.getName(),
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
                                                    .mergeSelection(new Field(idField.getName()))
                                                    .mergeSelection(new Field(FIELD_DEPRECATED_NAME));
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
                                                    "/" + index + "/" + idField.getName(),
                                                    fieldTypeDefinition.getName()
                                            );
                                        }
                                    }
                            );
                } else {
                    fetchItemStream = Stream.of(
                            new FetchItem(
                                    packageName,
                                    protocol,
                                    field,
                                    "/" + idField.getName(),
                                    fieldTypeDefinition.getName()
                            )
                    );
                }
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
                                                                                    idValueWithVariable,
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
                                                                                                                    idValueWithVariable,
                                                                                                                    field,
                                                                                                                    "",
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
                                                                                                                                    idValueWithVariable,
                                                                                                                                    field,
                                                                                                                                    "/" + index,
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

    public Stream<FetchItem> buildFetchItems(ObjectType objectType, ValueWithVariable parentIDValueWithVariable, Field field, String path, FieldDefinition fieldDefinition, InputValue inputValue, ValueWithVariable valueWithVariable) {
        if (valueWithVariable.isNull()) {
            return Stream.empty();
        }
        Definition fieldTypeDefinition = documentManager.getFieldTypeDefinition(fieldDefinition);
        Definition inputValueTypeDefinition = documentManager.getInputValueTypeDefinition(inputValue);
        if (fieldTypeDefinition.isObject() && !fieldTypeDefinition.isContainer()) {
            FieldDefinition parentIdField = objectType.getIDFieldOrError();
            String alias = Optional.ofNullable(field.getAlias()).orElseGet(field::getName);
            FieldDefinition idField = fieldTypeDefinition.asObject().getIDFieldOrError();
            String fetchTo = fieldDefinition.getFetchTo().orElseGet(fieldDefinition::getMapToOrError);
            String packageName = fieldTypeDefinition.asObject().getPackageNameOrError();
            String protocol = packageManager.isLocalPackage(packageName) ?
                    ENUM_PROTOCOL_ENUM_VALUE_LOCAL :
                    fieldDefinition.getFetchProtocol().map(EnumValue::getValue).orElse(packageConfig.getDefaultFetchProtocol());

            Stream<FetchItem> fetchItemStream = Stream.empty();
            if (!packageManager.isLocalPackage(packageName)) {
                if (fieldDefinition.getType().hasList()) {
                    fetchItemStream = IntStream.range(0, valueWithVariable.asArray().size())
                            .mapToObj(index -> {
                                        ObjectValueWithVariable item = valueWithVariable.asArray().getValueWithVariable(index).asObject();
                                        if (item.containsKey(idField.getName()) || item.containsKey(INPUT_VALUE_WHERE_NAME)) {
                                            Field fetchField = new Field()
                                                    .setAlias(alias + "__" + getAliasFromPath(path) + "_" + fieldDefinition.getName() + "_" + index)
                                                    .setSelections(
                                                            item.getObjectValueWithVariable().keySet().stream()
                                                                    .filter(key -> documentManager.getInputValueTypeDefinition(inputValueTypeDefinition.asInputObject().getInputValue(key)).isLeaf())
                                                                    .map(Field::new)
                                                                    .collect(Collectors.toList())
                                                    )
                                                    .mergeSelection(new Field(fetchTo))
                                                    .mergeSelection(new Field(idField.getName()))
                                                    .mergeSelection(new Field(FIELD_DEPRECATED_NAME));
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
                                                        )
                                                        .setName(typeNameToFieldName(fieldTypeDefinition.getName()));
                                            } else if (item.containsKey(INPUT_VALUE_WHERE_NAME)) {
                                                fetchField
                                                        .setArguments(item.getValueWithVariable(INPUT_VALUE_WHERE_NAME).asObject())
                                                        .setName(typeNameToFieldName(fieldTypeDefinition.getName()) + SUFFIX_LIST);
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
                                                    path + "/" + fieldDefinition.getName() + "/" + index + "/" + idField.getName(),
                                                    fieldTypeDefinition.getName()
                                            );
                                        }
                                    }
                            );
                } else {
                    if (valueWithVariable.asObject().containsKey(idField.getName()) || valueWithVariable.asObject().containsKey(INPUT_VALUE_WHERE_NAME)) {
                        Field fetchField = new Field()
                                .setAlias(alias + "__" + getAliasFromPath(path) + "_" + fieldDefinition.getName())
                                .setSelections(
                                        valueWithVariable.asObject().getObjectValueWithVariable().keySet().stream()
                                                .filter(key -> documentManager.getInputValueTypeDefinition(inputValueTypeDefinition.asInputObject().getInputValue(key)).isLeaf())
                                                .map(Field::new)
                                                .collect(Collectors.toList())
                                )
                                .mergeSelection(new Field(fetchTo))
                                .mergeSelection(new Field(idField.getName()))
                                .mergeSelection(new Field(FIELD_DEPRECATED_NAME));
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
                                    )
                                    .setName(typeNameToFieldName(fieldTypeDefinition.getName()));

                        } else if (valueWithVariable.asObject().containsKey(INPUT_VALUE_WHERE_NAME)) {
                            fetchField
                                    .setArguments(valueWithVariable.asObject().getValueWithVariable(INPUT_VALUE_WHERE_NAME).asObject())
                                    .setName(typeNameToFieldName(fieldTypeDefinition.getName()) + SUFFIX_LIST);
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
                                        path + "/" + fieldDefinition.getName() + "/" + idField.getName(),
                                        fieldTypeDefinition.getName()
                                )
                        );
                    }
                }
            }

            Stream<FetchItem> fetchWithItemStream = Stream.empty();
            if (fieldDefinition.hasFetchWith() || fieldDefinition.hasMapWith()) {
                ObjectType fetchWithType = documentManager.getDocument().getObjectTypeOrError(fieldDefinition.getFetchWithType()
                        .orElseGet(fieldDefinition::getMapWithTypeOrError));
                String withTypePackageName = fetchWithType.getPackageNameOrError();
                if (!packageManager.isLocalPackage(withTypePackageName)) {
                    FieldDefinition withTypeIdField = fetchWithType.getIDFieldOrError();
                    String fetchWithFrom = fieldDefinition.getFetchWithFrom().orElseGet(fieldDefinition::getMapWithFromOrError);

                    FieldDefinition refFieldDefinition = objectType.getField(typeNameToFieldName(fetchWithType.getName()));
                    String withTypeProtocol = packageManager.isLocalPackage(withTypePackageName) ?
                            ENUM_PROTOCOL_ENUM_VALUE_LOCAL :
                            packageConfig.getDefaultFetchProtocol();
                    String target = fetchWithType.getFields().stream()
                            .filter(withTypeFieldDefinition ->
                                    Stream
                                            .concat(
                                                    withTypeFieldDefinition.getFetchFrom().stream(),
                                                    withTypeFieldDefinition.getMapFrom().stream()
                                            )
                                            .anyMatch(name -> name.equals(fetchWithFrom))
                            )
                            .findFirst()
                            .map(AbstractDefinition::getName)
                            .orElseThrow(() -> new GraphQLErrors(FETCH_WITH_TO_OBJECT_FIELD_NOT_EXIST.bind(fetchWithFrom)));

                    if (parentIDValueWithVariable != null) {
                        Field fetchField = new Field(typeNameToFieldName(fetchWithType.getName()) + SUFFIX_LIST)
                                .setAlias(alias + "__" + getAliasFromPath(path) + "_" + refFieldDefinition.getName())
                                .addSelection(new Field(withTypeIdField.getName()))
                                .addSelection(new Field(FIELD_DEPRECATED_NAME))
                                .setArguments(
                                        Map.of(
                                                target,
                                                Map.of(
                                                        parentIdField.getName(),
                                                        Map.of(
                                                                INPUT_OPERATOR_INPUT_VALUE_VAL_NAME,
                                                                parentIDValueWithVariable
                                                        )
                                                )
                                        )
                                );
                        fetchWithItemStream = Stream.of(
                                new FetchItem(
                                        withTypePackageName,
                                        withTypeProtocol,
                                        fetchWithType.getName(),
                                        fetchField
                                )
                        );
                    } else {
                        fetchWithItemStream = Stream.of(
                                new FetchItem(
                                        withTypePackageName,
                                        withTypeProtocol,
                                        field,
                                        path + "/" + idField.getName(),
                                        fetchWithType.getName(),
                                        target,
                                        parentIdField.getName()
                                )
                        );
                    }
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
                                                                                            valueWithVariable.asArray().getValueWithVariable(index).asObject().getValueWithVariable(idField.getName()),
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
                                                                            valueWithVariable.asObject().getValueWithVariable(idField.getName()),
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
            return Streams.concat(fetchItemStream, fetchWithItemStream, subFetchItemStream);
        }
        return Stream.empty();
    }

    private Stream<Field> mergeIDField(FieldDefinition fieldDefinition, Field field) {
        Definition fieldTypeDefinition = documentManager.getFieldTypeDefinition(fieldDefinition);
        if (fieldTypeDefinition.isObject() && !fieldDefinition.isConnectionField()) {
            return Stream.of(
                    field.mergeSelection(
                            Stream
                                    .concat(
                                            fieldTypeDefinition.asObject().getIDField()
                                                    .map(idFieldDefinition ->
                                                            (Field) new Field(idFieldDefinition.getName())
                                                                    .addDirective(new Directive(DIRECTIVE_HIDE_NAME))
                                                    )
                                                    .stream(),
                                            Stream.ofNullable(field.getFields())
                                                    .flatMap(Collection::stream)
                                                    .flatMap(subField -> mergeIDField(fieldTypeDefinition.asObject().getField(subField.getName()), subField))
                                    )
                                    .collect(Collectors.toList())
                    )
            );
        }
        return Stream.of(field);
    }
}
