package io.graphoenix.core.handler.before;

import io.graphoenix.core.handler.DocumentManager;
import io.graphoenix.spi.graphql.Definition;
import io.graphoenix.spi.graphql.common.EnumValue;
import io.graphoenix.spi.graphql.common.ValueWithVariable;
import io.graphoenix.spi.graphql.operation.Field;
import io.graphoenix.spi.graphql.operation.Operation;
import io.graphoenix.spi.graphql.type.FieldDefinition;
import io.graphoenix.spi.graphql.type.InputValue;
import io.graphoenix.spi.graphql.type.ObjectType;
import io.graphoenix.spi.handler.QueryBeforeHandler;
import jakarta.annotation.Priority;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.json.JsonValue;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple4;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

import static io.graphoenix.spi.constant.Hammurabi.*;
import static io.graphoenix.spi.utils.NameUtil.typeNameToFieldName;

@ApplicationScoped
@Priority(100)
public class QueryBeforeFetchHandler implements QueryBeforeHandler {

    private final DocumentManager documentManager;

    @Inject
    public QueryBeforeFetchHandler(DocumentManager documentManager) {
        this.documentManager = documentManager;
    }

    @Override
    public Mono<Operation> query(Operation operation, Map<String, JsonValue> variables) {
        return null;
    }


    public Stream<Tuple4<String, String, Field, String>> buildFetchFields(String path, FieldDefinition fieldDefinition, Field field) {
        Definition fieldTypeDefinition = documentManager.getFieldTypeDefinition(fieldDefinition);
        if (fieldTypeDefinition.isObject() && !fieldTypeDefinition.isContainer()) {
            fieldTypeDefinition.asObject().getFields().stream()
                    .filter(FieldDefinition::isFetchField)
                    .flatMap(subFieldDefinition ->
                            Stream.ofNullable(fieldDefinition.getArguments())
                                    .flatMap(Collection::stream)
                                    .flatMap(inputValue ->
                                            Stream.ofNullable(field.getArguments())
                                                    .flatMap(arguments ->
                                                            arguments.getArgument(inputValue.getName())
                                                                    .or(() -> Optional.ofNullable(inputValue.getDefaultValue())).stream()
                                                    )
                                    )
                    )
        }
        return Stream.empty();
    }


    public Stream<Tuple4<String, String, Field, String>> buildFetchFields(String path, FieldDefinition fieldDefinition, InputValue inputValue, ValueWithVariable valueWithVariable) {
        Definition fieldTypeDefinition = documentManager.getFieldTypeDefinition(fieldDefinition);
        if (fieldDefinition.isFetchField()) {
            String protocol = fieldDefinition.getFetchProtocolOrError();
            String fetchFrom = fieldDefinition.getFetchFromOrError();
            Field fetchField = new Field();

            if (fieldDefinition.hasFetchWith()) {
                ObjectType fetchWithType = documentManager.getDocument().getObjectTypeOrError(fieldDefinition.getFetchWithTypeOrError());
                String fetchWithFrom = fieldDefinition.getFetchWithFromOrError();
                String packageName = fetchWithType.getPackageNameOrError();

                fetchField
                        .setAlias(getAliasFromPath(path))
                        .setArguments(valueWithVariable.asObject())
                        .addSelection(new Field(fetchWithFrom))
                        .setName(typeNameToFieldName(fetchWithType.getName()) + (fieldDefinition.getType().hasList() ? SUFFIX_LIST : ""));

            } else {

            }


        }
        if (fieldTypeDefinition.isObject() && !fieldTypeDefinition.isContainer()) {
            if (fieldDefinition.isFetchField()) {

            }
        }
        return Stream.empty();
    }

    private String getAliasFromPath(String path) {
        return path.replaceAll("/", "_");
    }
}
