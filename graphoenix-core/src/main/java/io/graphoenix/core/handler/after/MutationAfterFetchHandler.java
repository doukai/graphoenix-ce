package io.graphoenix.core.handler.after;

import io.graphoenix.core.handler.DocumentManager;
import io.graphoenix.core.handler.fetch.FetchItem;
import io.graphoenix.spi.error.GraphQLErrors;
import io.graphoenix.spi.graphql.AbstractDefinition;
import io.graphoenix.spi.graphql.Definition;
import io.graphoenix.spi.graphql.common.EnumValue;
import io.graphoenix.spi.graphql.common.ValueWithVariable;
import io.graphoenix.spi.graphql.operation.Field;
import io.graphoenix.spi.graphql.operation.Operation;
import io.graphoenix.spi.graphql.type.FieldDefinition;
import io.graphoenix.spi.graphql.type.InputValue;
import io.graphoenix.spi.graphql.type.ObjectType;
import io.graphoenix.spi.handler.FetchHandler;
import io.graphoenix.spi.handler.MutationAfterHandler;
import io.nozdormu.spi.context.BeanContext;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.json.JsonString;
import jakarta.json.JsonValue;
import jakarta.json.spi.JsonProvider;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.util.AbstractMap;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static io.graphoenix.spi.constant.Hammurabi.*;
import static io.graphoenix.spi.constant.Hammurabi.SUFFIX_LIST;
import static io.graphoenix.spi.error.GraphQLErrorType.FETCH_WITH_TO_OBJECT_FIELD_NOT_EXIST;
import static io.graphoenix.spi.utils.NameUtil.getAliasFromPath;
import static io.graphoenix.spi.utils.NameUtil.typeNameToFieldName;

@ApplicationScoped
public class MutationAfterFetchHandler implements MutationAfterHandler {

    private final DocumentManager documentManager;
    private final JsonProvider jsonProvider;
    private final Map<String, FetchHandler> fetchHandlerMap = BeanContext.getMap(FetchHandler.class);

    @Inject
    public MutationAfterFetchHandler(DocumentManager documentManager, JsonProvider jsonProvider) {
        this.documentManager = documentManager;
        this.jsonProvider = jsonProvider;
    }

    @Override
    public Mono<JsonValue> mutation(Operation operation, JsonValue jsonValue) {
        return null;
    }

    public Stream<FetchItem> buildFetchItems(String fieldPath, Field field, String path, FieldDefinition fieldDefinition, InputValue inputValue, ValueWithVariable valueWithVariable, JsonValue jsonValue) {
        if (valueWithVariable.isNull()) {
            return Stream.empty();
        }
        Definition fieldTypeDefinition = documentManager.getFieldTypeDefinition(fieldDefinition);
        if (fieldDefinition.isFetchField() && !fieldDefinition.getType().hasList() && fieldDefinition.isFetchAnchor()) {
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
                        .addSelection(new Field(fetchWithTo));
                if (fieldDefinition.getType().hasList()) {

                    fetchField.setArguments(
                            Map.of(
                                    INPUT_VALUE_LIST_NAME,
                                    valueWithVariable.asArray().stream()
                                            .map(item ->
                                                    jsonProvider.createObjectBuilder()
                                                            .add(fetchWithFrom, getFetchFrom(jsonValue.asJsonObject().get(fetchFrom), fetchWithType.getField(fetchWithFrom)))


                                            )
                            )
                    )
                } else {

                }
            } else {
                if (fieldDefinition.getType().hasList()) {

                } else {

                }
            }


            String packageName = fieldTypeDefinition.asObject().getPackageNameOrError();
            String fetchTo = fieldDefinition.getFetchToOrError();
            fetchField
                    .setAlias(getAliasFromPath(fieldPath) + "__" + getAliasFromPath(path + "/" + fetchFrom))
                    .setArguments(valueWithVariable.asObject())
                    .addSelection(new Field(fetchTo))
                    .setName(typeNameToFieldName(fieldTypeDefinition.getName()));

            return Stream.of(new FetchItem(packageName, protocol, path, fetchField, fetchTo, field, fetchFrom));
        } else if (fieldTypeDefinition.isObject() && !fieldTypeDefinition.isContainer()) {
            Definition inputValueTypeDefinition = documentManager.getInputValueTypeDefinition(inputValue);
            return inputValueTypeDefinition.asInputObject().getInputValues().stream()
                    .filter(subInputValue -> subInputValue.getName().endsWith(SUFFIX_INPUT))
                    .flatMap(subInputValue ->
                            Stream.ofNullable(fieldTypeDefinition.asObject().getField(subInputValue.getName()))
                                    .flatMap(subFieldDefinition ->
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

                    );
        }
        return Stream.empty();
    }

    private JsonValue getFetchFrom(JsonValue jsonValue, FieldDefinition fieldDefinition) {
        String typeName = documentManager.getFieldTypeDefinition(fieldDefinition).getName();
        if (typeName.equals(SCALA_INT_NAME) || typeName.equals(SCALA_BIG_INTEGER_NAME)) {
            if (jsonValue.getValueType().equals(JsonValue.ValueType.NUMBER)) {
                return jsonValue;
            } else if (jsonValue.getValueType().equals(JsonValue.ValueType.STRING)) {
                return jsonProvider.createValue(Integer.valueOf(((JsonString) jsonValue).getString()));
            } else {
                return jsonProvider.createValue(Integer.valueOf(jsonValue.toString()));
            }
        } else {
            return jsonValue;
        }
    }
}
