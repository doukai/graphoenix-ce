package io.graphoenix.core.handler.before;

import io.graphoenix.core.handler.DocumentManager;
import io.graphoenix.core.handler.fetch.FetchItem;
import io.graphoenix.spi.error.GraphQLErrors;
import io.graphoenix.spi.graphql.AbstractDefinition;
import io.graphoenix.spi.graphql.Definition;
import io.graphoenix.spi.graphql.common.ValueWithVariable;
import io.graphoenix.spi.graphql.operation.Field;
import io.graphoenix.spi.graphql.operation.Operation;
import io.graphoenix.spi.graphql.type.FieldDefinition;
import io.graphoenix.spi.graphql.type.InputValue;
import io.graphoenix.spi.graphql.type.ObjectType;
import io.graphoenix.spi.handler.FetchHandler;
import io.graphoenix.spi.handler.MutationBeforeHandler;
import io.nozdormu.spi.context.BeanContext;
import jakarta.annotation.Priority;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.json.JsonValue;
import jakarta.json.spi.JsonProvider;
import jakarta.json.stream.JsonCollectors;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

import static io.graphoenix.spi.constant.Hammurabi.*;
import static io.graphoenix.spi.error.GraphQLErrorType.FETCH_WITH_TO_OBJECT_FIELD_NOT_EXIST;
import static io.graphoenix.spi.utils.NameUtil.getAliasFromPath;
import static io.graphoenix.spi.utils.NameUtil.typeNameToFieldName;

@ApplicationScoped
@Priority(100)
public class MutationBeforeFetchHandler implements MutationBeforeHandler {

    private final DocumentManager documentManager;
    private final JsonProvider jsonProvider;
    private final Map<String, FetchHandler> fetchHandlerMap = BeanContext.getMap(FetchHandler.class);

    @Inject
    public MutationBeforeFetchHandler(DocumentManager documentManager, JsonProvider jsonProvider) {
        this.documentManager = documentManager;
        this.jsonProvider = jsonProvider;
    }

    @Override
    public Mono<Operation> mutation(Operation operation, Map<String, JsonValue> variables) {
        return null;
    }

    public Stream<FetchItem> buildFetchItems(String fieldPath, Field field, String path, FieldDefinition fieldDefinition, InputValue inputValue, ValueWithVariable valueWithVariable) {
        if (valueWithVariable.isNull()) {
            return Stream.empty();
        }
        Definition fieldTypeDefinition = documentManager.getFieldTypeDefinition(fieldDefinition);
        if (fieldDefinition.isFetchField()) {
            String protocol = fieldDefinition.getFetchProtocolOrError().toLowerCase();
            String fetchFrom = fieldDefinition.getFetchFromOrError();
            Field fetchField = new Field();
            if (fieldDefinition.hasFetchWith()) {
                ObjectType fetchWithType = documentManager.getDocument().getObjectTypeOrError(fieldDefinition.getFetchWithTypeOrError());
                String packageName = fetchWithType.getPackageNameOrError();
                String fetchWithFrom = fieldDefinition.getFetchWithFromOrError();
                String fetchWithTo = fieldDefinition.getFetchWithToOrError();

                fetchField
                        .setAlias(getAliasFromPath(fieldPath) + "__" + getAliasFromPath(path + "/" + fetchFrom))
                        .addSelection(new Field(fetchWithFrom));

                if (fieldDefinition.getType().hasList()) {
                    fetchField
                            .setArguments(
                                    Map.of(
                                            INPUT_VALUE_LIST_NAME,
                                            valueWithVariable.asJsonArray().stream()
                                                    .map(jsonValue ->
                                                            jsonProvider.createObjectBuilder()
                                                                    .add(
                                                                            fetchWithType.getFields().stream()
                                                                                    .filter(withTypeFieldDefinition ->
                                                                                            Stream
                                                                                                    .concat(
                                                                                                            withTypeFieldDefinition.getMapFrom().stream(),
                                                                                                            withTypeFieldDefinition.getFetchFrom().stream()
                                                                                                    )
                                                                                                    .anyMatch(name -> name.equals(fetchWithTo))
                                                                                    )
                                                                                    .findFirst()
                                                                                    .map(AbstractDefinition::getName)
                                                                                    .orElseThrow(() -> new GraphQLErrors(FETCH_WITH_TO_OBJECT_FIELD_NOT_EXIST.bind(fetchWithTo))),
                                                                            jsonValue
                                                                    )
                                                                    .build()
                                                    )
                                                    .collect(JsonCollectors.toJsonArray())
                                    )
                            )
                            .setName(typeNameToFieldName(fetchWithType.getName()) + SUFFIX_LIST);
                } else {
                    fetchField
                            .setArguments(
                                    jsonProvider.createObjectBuilder()
                                            .add(
                                                    fetchWithType.getFields().stream()
                                                            .filter(withTypeFieldDefinition ->
                                                                    Stream
                                                                            .concat(
                                                                                    withTypeFieldDefinition.getMapFrom().stream(),
                                                                                    withTypeFieldDefinition.getFetchFrom().stream()
                                                                            )
                                                                            .anyMatch(name -> name.equals(fetchWithTo))
                                                            )
                                                            .findFirst()
                                                            .map(AbstractDefinition::getName)
                                                            .orElseThrow(() -> new GraphQLErrors(FETCH_WITH_TO_OBJECT_FIELD_NOT_EXIST.bind(fetchWithTo))),
                                                    valueWithVariable
                                            )
                                            .build()
                            )
                            .setName(typeNameToFieldName(fetchWithType.getName()));
                }
                return Stream.of(new FetchItem(packageName, protocol, path, fetchField, fetchWithFrom, field, fetchFrom));
            } else {
                String packageName = fieldTypeDefinition.asObject().getPackageNameOrError();
                String fetchTo = fieldDefinition.getFetchToOrError();
                valueWithVariable.asObject().put(INPUT_VALUE_GROUP_BY_NAME, Collections.singletonList(fetchTo));
                fetchField
                        .setAlias(getAliasFromPath(fieldPath) + "__" + getAliasFromPath(path + "/" + fetchFrom))
                        .setArguments(valueWithVariable.asObject())
                        .addSelection(new Field(fetchTo))
                        .setName(typeNameToFieldName(fieldTypeDefinition.getName()) + (fieldDefinition.getType().hasList() ? SUFFIX_LIST : ""));

                return Stream.of(new FetchItem(packageName, protocol, path, fetchField, fetchTo, field, fetchFrom));
            }
        } else if (fieldTypeDefinition.isObject() && !fieldTypeDefinition.isContainer()) {
            return Stream
                    .concat(
                            fieldTypeDefinition.asObject().getFields().stream()
                                    .flatMap(subFieldDefinition ->
                                            inputValue.asInputObject().getInputValue(subFieldDefinition.getName()).stream()
                                                    .filter(subInputValue -> subInputValue.getName().endsWith(SUFFIX_EXPRESSION))
                                                    .flatMap(subInputValue ->
                                                            Stream.ofNullable(valueWithVariable.asObject().getObjectValueWithVariable())
                                                                    .flatMap(objectValue ->
                                                                            Optional.ofNullable(objectValue.get(subInputValue.getName()))
                                                                                    .or(() -> Optional.ofNullable(subInputValue.getDefaultValue())).stream()
                                                                    )
                                                                    .flatMap(subValueWithVariable ->
                                                                            buildFetchItems(
                                                                                    fieldPath,
                                                                                    field,
                                                                                    path + "/" + fieldDefinition.getName(),
                                                                                    subFieldDefinition,
                                                                                    subInputValue,
                                                                                    subValueWithVariable
                                                                            )
                                                                    )
                                                    )
                                    ),
                            inputValue.asInputObject().getInputValue(INPUT_VALUE_WHERE_NAME).stream()
                                    .flatMap(whereInputValue ->
                                            valueWithVariable.asObject().getValueWithVariable(whereInputValue.getName())
                                                    .or(() -> Optional.ofNullable(whereInputValue.getDefaultValue())).stream()
                                                    .filter(ValueWithVariable::isObject)
                                                    .map(ValueWithVariable::asObject)
                                                    .flatMap(objectValueWithVariable ->
                                                            fieldTypeDefinition.asObject().getFields().stream()
                                                                    .flatMap(subFieldDefinition ->
                                                                            documentManager.getInputValueTypeDefinition(whereInputValue).asInputObject().getInputValue(subFieldDefinition.getName()).stream()
                                                                                    .filter(subInputValue -> subInputValue.getName().endsWith(SUFFIX_EXPRESSION))
                                                                                    .flatMap(subInputValue ->
                                                                                            objectValueWithVariable.getValueWithVariable(subInputValue.getName())
                                                                                                    .or(() -> Optional.ofNullable(subInputValue.getDefaultValue())).stream()
                                                                                                    .flatMap(subValueWithVariable ->
                                                                                                            buildFetchItems(
                                                                                                                    fieldPath,
                                                                                                                    field,
                                                                                                                    path + "/" + INPUT_VALUE_WHERE_NAME + "/" + fieldDefinition.getName(),
                                                                                                                    subFieldDefinition,
                                                                                                                    subInputValue,
                                                                                                                    subValueWithVariable
                                                                                                            )
                                                                                                    )
                                                                                    )
                                                                    )
                                                    )
                                    )
                    );
        }
        return Stream.empty();
    }
}
