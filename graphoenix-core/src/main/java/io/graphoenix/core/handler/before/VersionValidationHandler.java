package io.graphoenix.core.handler.before;

import com.google.common.collect.Streams;
import io.graphoenix.core.config.MutationConfig;
import io.graphoenix.core.handler.DocumentManager;
import io.graphoenix.spi.error.GraphQLError;
import io.graphoenix.spi.error.GraphQLErrors;
import io.graphoenix.spi.graphql.Definition;
import io.graphoenix.spi.graphql.common.*;
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
import reactor.util.function.Tuple5;
import reactor.util.function.Tuples;

import java.util.AbstractMap;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static io.graphoenix.core.handler.before.UniqueValidationHandler.UNIQUE_VALIDATION_HANDLER_PRIORITY;
import static io.graphoenix.spi.constant.Hammurabi.*;
import static io.graphoenix.spi.error.GraphQLErrorType.RESOURCE_HAS_BEEN_UPDATED;
import static io.graphoenix.spi.utils.NameUtil.typeNameToFieldName;

@ApplicationScoped
@Priority(VersionValidationHandler.VERSION_VALIDATION_HANDLER_PRIORITY)
public class VersionValidationHandler implements OperationBeforeHandler {

    public static final int VERSION_VALIDATION_HANDLER_PRIORITY = UNIQUE_VALIDATION_HANDLER_PRIORITY + 1;

    private final DocumentManager documentManager;
    private final JsonProvider jsonProvider;
    private final OperationHandler operationHandler;
    private final MutationConfig mutationConfig;

    @Inject
    public VersionValidationHandler(DocumentManager documentManager, JsonProvider jsonProvider, OperationHandler operationHandler, MutationConfig mutationConfig) {
        this.documentManager = documentManager;
        this.jsonProvider = jsonProvider;
        this.operationHandler = operationHandler;
        this.mutationConfig = mutationConfig;
    }

    @Override
    public Mono<Operation> mutation(Operation operation, Map<String, JsonValue> variables) {
        if (!mutationConfig.getOcc()) {
            return Mono.just(operation);
        }
        ObjectType operationType = documentManager.getOperationTypeOrError(operation);
        return Flux
                .fromStream(
                        operation.getFields().stream()
                                .flatMap(field -> buildVersionItems(operationType.getField(field.getName()), field))
                                .collect(
                                        Collectors.groupingBy(
                                                Tuple5::getT1,
                                                Collectors.toList()
                                        )
                                )
                                .entrySet().stream()
                                .map(typeEntry ->
                                        new AbstractMap.SimpleEntry<>(
                                                new Field(typeNameToFieldName(typeEntry.getKey().getName()) + SUFFIX_LIST)
                                                        .setAlias(typeNameToFieldName(typeEntry.getKey().getName()) + SUFFIX_LIST)
                                                        .setArguments(
                                                                jsonProvider.createObjectBuilder()
                                                                        .add(typeEntry.getKey().getIDFieldOrError().getName(),
                                                                                jsonProvider.createObjectBuilder()
                                                                                        .add(INPUT_OPERATOR_INPUT_VALUE_OPR_NAME, new EnumValue(INPUT_OPERATOR_INPUT_VALUE_IN))
                                                                                        .add(
                                                                                                INPUT_OPERATOR_INPUT_VALUE_ARR_NAME,
                                                                                                typeEntry.getValue().stream()
                                                                                                        .map(Tuple5::getT5)
                                                                                                        .collect(JsonCollectors.toJsonArray())
                                                                                        )
                                                                        )
                                                                        .build()
                                                        )
                                                        .addSelection(new Field(typeEntry.getKey().getIDFieldOrError().getName()))
                                                        .addSelection(new Field("version")),
                                                typeEntry.getValue()
                                        )
                                )
                )
                .collectList()
                .flatMapMany(typeEntryList ->
                        Mono
                                .from(
                                        operationHandler.handle(
                                                new Operation()
                                                        .setOperationType(OPERATION_QUERY_NAME)
                                                        .setSelections(
                                                                typeEntryList.stream()
                                                                        .map(AbstractMap.SimpleEntry::getKey)
                                                                        .collect(Collectors.toList())
                                                        )
                                        )
                                )
                                .flatMapMany(jsonValue ->
                                        Flux
                                                .fromStream(
                                                        typeEntryList.stream()
                                                                .filter(typeEntry -> !jsonValue.asJsonObject().isNull(typeEntry.getKey().getAlias()))
                                                                .flatMap(typeEntry ->
                                                                        typeEntry.getValue().stream()
                                                                                .filter(tuple5 ->
                                                                                        jsonValue.asJsonObject().getJsonArray(typeEntry.getKey().getAlias()).stream()
                                                                                                .filter(item -> item.getValueType().equals(JsonValue.ValueType.OBJECT))
                                                                                                .anyMatch(item ->
                                                                                                        item.asJsonObject().get(tuple5.getT1().getIDFieldOrError().getName()).toString().equals(tuple5.getT5().toString()) &&
                                                                                                                !item.asJsonObject().get(tuple5.getT2()).toString().equals(tuple5.getT3().toString())
                                                                                                )
                                                                                )
                                                                                .map(tuple5 -> new GraphQLError(RESOURCE_HAS_BEEN_UPDATED).setPath(tuple5.getT4()))
                                                                )
                                                )
                                )
                )
                .collectList()
                .flatMap(graphQLErrors -> {
                            if (!graphQLErrors.isEmpty()) {
//                                return Mono.error(new GraphQLErrors().addAll(graphQLErrors));
                                return Mono.error(new GraphQLErrors(RESOURCE_HAS_BEEN_UPDATED));
                            }
                            return Mono.just(operation);
                        }
                );
    }

    public Stream<Tuple5<ObjectType, String, ValueWithVariable, String, ValueWithVariable>> buildVersionItems(FieldDefinition fieldDefinition, Field field) {
        Definition fieldTypeDefinition = documentManager.getFieldTypeDefinition(fieldDefinition);
        if (fieldTypeDefinition.isObject() && !fieldTypeDefinition.isContainer()) {
            FieldDefinition idField = fieldTypeDefinition.asObject().getIDFieldOrError();
            ValueWithVariable idValueWithVariable = getIDValueWithVariable(idField.getName(), field.getArguments()).orElse(new NullValue());
            return Streams
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
                                                                            buildVersionItems(
                                                                                    fieldTypeDefinition.asObject(),
                                                                                    "",
                                                                                    idValueWithVariable,
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
                                                    .flatMap(objectValueWithVariable -> {
                                                                ValueWithVariable itemIdValueWithVariable = getIDValueWithVariable(idField.getName(), objectValueWithVariable).orElse(new NullValue());
                                                                return documentManager.getInputValueTypeDefinition(inputValue).asInputObject().getInputValues().stream()
                                                                        .flatMap(subInputValue ->
                                                                                Stream.ofNullable(fieldTypeDefinition.asObject().getField(subInputValue.getName()))
                                                                                        .flatMap(subFieldDefinition ->
                                                                                                objectValueWithVariable.getValueWithVariableOrEmpty(subInputValue.getName())
                                                                                                        .or(() -> Optional.ofNullable(subInputValue.getDefaultValue())).stream()
                                                                                                        .flatMap(subValueWithVariable ->
                                                                                                                buildVersionItems(
                                                                                                                        fieldTypeDefinition.asObject(),
                                                                                                                        "/" + INPUT_VALUE_INPUT_NAME,
                                                                                                                        itemIdValueWithVariable,
                                                                                                                        subFieldDefinition,
                                                                                                                        subInputValue,
                                                                                                                        subValueWithVariable
                                                                                                                )
                                                                                                        )
                                                                                        )
                                                                        );
                                                            }
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
                                                                    .mapToObj(index -> {
                                                                                ValueWithVariable itemIdValueWithVariable = getIDValueWithVariable(idField.getName(), arrayValueWithVariable.getValueWithVariable(index).asObject()).orElse(new NullValue());
                                                                                return documentManager.getInputValueTypeDefinition(listInputValue).asInputObject().getInputValues().stream()
                                                                                        .flatMap(subInputValue ->
                                                                                                Stream.ofNullable(fieldTypeDefinition.asObject().getField(subInputValue.getName()))
                                                                                                        .flatMap(subFieldDefinition ->
                                                                                                                arrayValueWithVariable.getValueWithVariable(index).asObject().getValueWithVariableOrEmpty(subInputValue.getName())
                                                                                                                        .or(() -> Optional.ofNullable(subInputValue.getDefaultValue())).stream()
                                                                                                                        .flatMap(subValueWithVariable ->
                                                                                                                                buildVersionItems(
                                                                                                                                        fieldTypeDefinition.asObject(),
                                                                                                                                        "/" + INPUT_VALUE_LIST_NAME + "/" + index,
                                                                                                                                        itemIdValueWithVariable,
                                                                                                                                        subFieldDefinition,
                                                                                                                                        subInputValue,
                                                                                                                                        subValueWithVariable
                                                                                                                                )
                                                                                                                        )
                                                                                                        )
                                                                                        );
                                                                            }
                                                                    )
                                                                    .flatMap(stream -> stream)
                                                    )
                                    )
                    );
        }
        return Stream.empty();
    }

    public Stream<Tuple5<ObjectType, String, ValueWithVariable, String, ValueWithVariable>> buildVersionItems(ObjectType objectType, String path, ValueWithVariable parentIDValueWithVariable, FieldDefinition fieldDefinition, InputValue inputValue, ValueWithVariable valueWithVariable) {
        if (valueWithVariable.isNull()) {
            return Stream.empty();
        }
        Definition fieldTypeDefinition = documentManager.getFieldTypeDefinition(fieldDefinition);
        if (fieldDefinition.getName().equals("version") && !parentIDValueWithVariable.isNull()) {
            return Stream.of(Tuples.of(objectType, fieldDefinition.getName(), valueWithVariable, path + "/" + fieldDefinition.getName(), parentIDValueWithVariable));
        } else if (fieldTypeDefinition.isObject() && !fieldTypeDefinition.isContainer()) {
            FieldDefinition idField = fieldTypeDefinition.asObject().getIDFieldOrError();
            Definition inputValueTypeDefinition = documentManager.getInputValueTypeDefinition(inputValue);
            return inputValueTypeDefinition.asInputObject().getInputValues().stream()
                    .flatMap(subInputValue ->
                            Stream.ofNullable(fieldTypeDefinition.asObject().getField(subInputValue.getName()))
                                    .flatMap(subFieldDefinition -> {
                                                if (fieldDefinition.getType().hasList()) {
                                                    return IntStream.range(0, valueWithVariable.asArray().size())
                                                            .mapToObj(index -> {
                                                                        ValueWithVariable idValueWithVariable = getIDValueWithVariable(idField.getName(), valueWithVariable.asArray().getValueWithVariable(index).asObject()).orElse(new NullValue());
                                                                        return Stream.ofNullable(valueWithVariable.asArray().getValueWithVariable(index).asObject().getObjectValueWithVariable())
                                                                                .flatMap(objectValue ->
                                                                                        Optional.ofNullable(objectValue.get(subInputValue.getName()))
                                                                                                .or(() -> Optional.ofNullable(subInputValue.getDefaultValue())).stream()
                                                                                )
                                                                                .flatMap(subValueWithVariable ->
                                                                                        buildVersionItems(
                                                                                                fieldTypeDefinition.asObject(),
                                                                                                path + "/" + fieldDefinition.getName() + "/" + index,
                                                                                                idValueWithVariable,
                                                                                                subFieldDefinition,
                                                                                                subInputValue,
                                                                                                subValueWithVariable
                                                                                        )
                                                                                );
                                                                    }
                                                            )
                                                            .flatMap(stream -> stream);
                                                } else {
                                                    ValueWithVariable idValueWithVariable = getIDValueWithVariable(idField.getName(), valueWithVariable.asObject()).orElse(new NullValue());
                                                    return Stream.ofNullable(valueWithVariable.asObject().getObjectValueWithVariable())
                                                            .flatMap(objectValue ->
                                                                    Optional.ofNullable(objectValue.get(subInputValue.getName()))
                                                                            .or(() -> Optional.ofNullable(subInputValue.getDefaultValue())).stream()
                                                            )
                                                            .flatMap(subValueWithVariable ->
                                                                    buildVersionItems(
                                                                            fieldTypeDefinition.asObject(),
                                                                            path + "/" + fieldDefinition.getName(),
                                                                            idValueWithVariable,
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

    private Optional<ValueWithVariable> getIDValueWithVariable(String idFieldName, ObjectValueWithVariable objectValueWithVariable) {
        if (objectValueWithVariable != null && objectValueWithVariable.containsKey(idFieldName)) {
            return Optional.of(objectValueWithVariable.getValueWithVariable(idFieldName));
        } else if (objectValueWithVariable != null &&
                objectValueWithVariable.containsKey(INPUT_VALUE_WHERE_NAME) &&
                objectValueWithVariable.getValueWithVariable(INPUT_VALUE_WHERE_NAME).asObject().containsKey(idFieldName) &&
                objectValueWithVariable.getValueWithVariable(INPUT_VALUE_WHERE_NAME).asObject().getValueWithVariable(idFieldName).asObject().containsKey(INPUT_OPERATOR_INPUT_VALUE_VAL_NAME)) {
            return Optional.of(
                    objectValueWithVariable.getValueWithVariable(INPUT_VALUE_WHERE_NAME).asObject()
                            .getValueWithVariable(idFieldName).asObject()
                            .getValueWithVariable(INPUT_OPERATOR_INPUT_VALUE_VAL_NAME)
            );
        }
        return Optional.empty();
    }

    private Optional<ValueWithVariable> getIDValueWithVariable(String idFieldName, Arguments arguments) {
        if (arguments != null && arguments.containsKey(idFieldName)) {
            return Optional.of(arguments.getArgument(idFieldName));
        } else if (arguments != null &&
                arguments.containsKey(INPUT_VALUE_WHERE_NAME) &&
                arguments.getArgument(INPUT_VALUE_WHERE_NAME).asObject().containsKey(idFieldName) &&
                arguments.getArgument(INPUT_VALUE_WHERE_NAME).asObject().getValueWithVariable(idFieldName).asObject().containsKey(INPUT_OPERATOR_INPUT_VALUE_VAL_NAME)) {
            return Optional.of(
                    arguments.getArgument(INPUT_VALUE_WHERE_NAME).asObject()
                            .getValueWithVariable(idFieldName).asObject()
                            .getValueWithVariable(INPUT_OPERATOR_INPUT_VALUE_VAL_NAME)
            );
        }
        return Optional.empty();
    }
}
