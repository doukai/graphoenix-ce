package io.graphoenix.core.handler.before;

import io.graphoenix.core.handler.DocumentManager;
import io.graphoenix.spi.error.GraphQLError;
import io.graphoenix.spi.error.GraphQLErrors;
import io.graphoenix.spi.graphql.Definition;
import io.graphoenix.spi.graphql.common.EnumValue;
import io.graphoenix.spi.graphql.common.ValueWithVariable;
import io.graphoenix.spi.graphql.operation.Field;
import io.graphoenix.spi.graphql.operation.Operation;
import io.graphoenix.spi.graphql.type.FieldDefinition;
import io.graphoenix.spi.graphql.type.InputValue;
import io.graphoenix.spi.graphql.type.ObjectType;
import io.graphoenix.spi.handler.OperationBeforeHandler;
import io.graphoenix.spi.handler.OperationHandler;
import jakarta.annotation.Priority;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.json.JsonValue;
import jakarta.json.spi.JsonProvider;
import jakarta.json.stream.JsonCollectors;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple4;
import reactor.util.function.Tuples;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static io.graphoenix.spi.constant.Hammurabi.*;
import static io.graphoenix.spi.error.GraphQLErrorType.EXISTED_UNIQUE_VALUES;
import static io.graphoenix.spi.utils.NameUtil.typeNameToFieldName;

@ApplicationScoped
@Priority(105)
public class UniqueValidationHandler implements OperationBeforeHandler {

    private final DocumentManager documentManager;
    private final JsonProvider jsonProvider;
    private final OperationHandler operationHandler;

    @Inject
    public UniqueValidationHandler(DocumentManager documentManager, JsonProvider jsonProvider, OperationHandler operationHandler) {
        this.documentManager = documentManager;
        this.jsonProvider = jsonProvider;
        this.operationHandler = operationHandler;
    }

    @Override
    public Mono<Operation> mutation(Operation operation, Map<String, JsonValue> variables) {
        ObjectType operationType = documentManager.getOperationTypeOrError(operation);
        return Flux
                .fromIterable(
                        operation.getFields().stream()
                                .flatMap(field -> buildUniqueItems(operationType.getField(field.getName()), field))
                                .collect(
                                        Collectors.groupingBy(
                                                Tuple4::getT1,
                                                Collectors.toList()
                                        )
                                )
                                .entrySet()
                )
                .flatMap(typeEntry ->
                        Flux
                                .fromIterable(
                                        typeEntry.getValue().stream()
                                                .collect(
                                                        Collectors.groupingBy(
                                                                Tuple4::getT2,
                                                                Collectors.toList()
                                                        )
                                                )
                                                .entrySet()
                                )
                                .map(fieldEntry ->
                                        new AbstractMap.SimpleEntry<>(
                                                new Field(typeNameToFieldName(typeEntry.getKey()) + SUFFIX_LIST)
                                                        .setAlias(typeNameToFieldName(typeEntry.getKey()) + "_" + fieldEntry.getKey())
                                                        .setArguments(
                                                                jsonProvider.createObjectBuilder()
                                                                        .add(fieldEntry.getKey(),
                                                                                jsonProvider.createObjectBuilder()
                                                                                        .add(INPUT_OPERATOR_INPUT_VALUE_OPR_NAME, new EnumValue(INPUT_OPERATOR_INPUT_VALUE_IN))
                                                                                        .add(
                                                                                                INPUT_OPERATOR_INPUT_VALUE_ARR_NAME,
                                                                                                fieldEntry.getValue().stream()
                                                                                                        .map(Tuple4::getT3)
                                                                                                        .collect(JsonCollectors.toJsonArray())
                                                                                        )
                                                                        )
                                                                        .build()
                                                        )
                                                        .addSelection(new Field(fieldEntry.getKey())),
                                                fieldEntry.getValue()
                                        )
                                )
                )
                .collectList()
                .flatMapMany(fieldEntryList ->
                        Mono
                                .from(
                                        operationHandler.handle(
                                                new Operation()
                                                        .setOperationType(OPERATION_QUERY_NAME)
                                                        .setSelections(
                                                                fieldEntryList.stream()
                                                                        .map(AbstractMap.SimpleEntry::getKey)
                                                                        .collect(Collectors.toList())
                                                        )
                                        )

                                )
                                .flatMapMany(jsonValue ->
                                        Flux
                                                .fromIterable(fieldEntryList)
                                                .filter(entry -> !jsonValue.asJsonObject().isNull(entry.getKey().getAlias()))
                                                .flatMap(fieldEntry ->
                                                        Flux
                                                                .fromIterable(
                                                                        fieldEntry.getValue().stream()
                                                                                .collect(
                                                                                        Collectors.groupingBy(
                                                                                                Tuple4::getT3,
                                                                                                Collectors.mapping(
                                                                                                        Tuple4::getT4,
                                                                                                        Collectors.toList()
                                                                                                )
                                                                                        )
                                                                                )
                                                                                .entrySet()
                                                                )
                                                                .filter(valueEntry ->
                                                                        jsonValue.asJsonObject().getJsonArray(fieldEntry.getKey().getAlias()).stream()
                                                                                .anyMatch(item -> item.toString().equals(valueEntry.getKey().toString()))
                                                                )
                                                                .flatMap(valueEntry ->
                                                                        Flux
                                                                                .fromIterable(
                                                                                        valueEntry.getValue()
                                                                                )
                                                                                .map(path ->
                                                                                        new GraphQLError(valueEntry.getKey().toString())
                                                                                                .setSchemaPath(path)
                                                                                )
                                                                )
                                                )
                                )
                )
                .collectList()
                .flatMap(graphQLErrors -> {
                            if (!graphQLErrors.isEmpty()) {
                                return Mono.error(new GraphQLErrors(EXISTED_UNIQUE_VALUES).addAll(graphQLErrors));
                            }
                            return Mono.just(operation);
                        }
                );
    }

    public Stream<Tuple4<String, String, ValueWithVariable, String>> buildUniqueItems(FieldDefinition fieldDefinition, Field field) {
        Definition fieldTypeDefinition = documentManager.getFieldTypeDefinition(fieldDefinition);
        if (fieldTypeDefinition.isObject() && !fieldTypeDefinition.isContainer()) {
            return Stream
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
                                                                            buildUniqueItems(
                                                                                    fieldTypeDefinition.asObject(),
                                                                                    "",
                                                                                    subFieldDefinition,
                                                                                    inputValue,
                                                                                    valueWithVariable
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
                                                                                                                            buildUniqueItems(
                                                                                                                                    fieldTypeDefinition.asObject(),
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
        }
        return Stream.empty();

    }

    public Stream<Tuple4<String, String, ValueWithVariable, String>> buildUniqueItems(ObjectType objectType, String path, FieldDefinition fieldDefinition, InputValue inputValue, ValueWithVariable valueWithVariable) {
        if (valueWithVariable.isNull()) {
            return Stream.empty();
        }
        Definition fieldTypeDefinition = documentManager.getFieldTypeDefinition(fieldDefinition);
        if (fieldDefinition.isUnique()) {
            return Stream.of(Tuples.of(objectType.getName(), fieldDefinition.getName(), valueWithVariable, path));
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
                                                                                    buildUniqueItems(
                                                                                            fieldTypeDefinition.asObject(),
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
                                                                    buildUniqueItems(
                                                                            fieldTypeDefinition.asObject(),
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
}
