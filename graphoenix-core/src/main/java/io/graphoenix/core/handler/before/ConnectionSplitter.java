package io.graphoenix.core.handler.before;

import com.google.common.collect.Streams;
import io.graphoenix.core.handler.DocumentManager;
import io.graphoenix.core.handler.PackageManager;
import io.graphoenix.spi.error.GraphQLErrors;
import io.graphoenix.spi.graphql.AbstractDefinition;
import io.graphoenix.spi.graphql.Definition;
import io.graphoenix.spi.graphql.common.Directive;
import io.graphoenix.spi.graphql.common.ValueWithVariable;
import io.graphoenix.spi.graphql.operation.Field;
import io.graphoenix.spi.graphql.operation.Operation;
import io.graphoenix.spi.graphql.type.FieldDefinition;
import io.graphoenix.spi.graphql.type.ObjectType;
import io.graphoenix.spi.handler.OperationBeforeHandler;
import io.graphoenix.spi.utils.StreamUtil;
import jakarta.annotation.Priority;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.json.JsonValue;
import reactor.core.publisher.Mono;

import java.util.AbstractMap;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static io.graphoenix.core.handler.before.EnumValueHandler.ENUM_VALUE_HANDLER_PRIORITY;
import static io.graphoenix.spi.constant.Hammurabi.*;
import static io.graphoenix.spi.error.GraphQLErrorType.OBJECT_SELECTION_NOT_EXIST;

@ApplicationScoped
@Priority(ConnectionSplitter.CONNECTION_SPLITTER_PRIORITY)
public class ConnectionSplitter implements OperationBeforeHandler {

    public static final int CONNECTION_SPLITTER_PRIORITY = ENUM_VALUE_HANDLER_PRIORITY + 100;

    private final DocumentManager documentManager;
    private final PackageManager packageManager;

    @Inject
    public ConnectionSplitter(DocumentManager documentManager, PackageManager packageManager) {
        this.documentManager = documentManager;
        this.packageManager = packageManager;
    }

    @Override
    public Mono<Operation> handle(Operation operation, Map<String, JsonValue> variables) {
        ObjectType operationType = documentManager.getOperationTypeOrError(operation);
        return Mono.just(
                operation
                        .setSelections(
                                operation.getFields().stream()
                                        .flatMap(field -> buildConnection(operationType, operationType.getField(field.getName()), field))
                                        .collect(Collectors.toList())
                        )
        );
    }

    private Stream<Field> buildConnection(ObjectType objectType, FieldDefinition fieldDefinition, Field field) {
        Definition fieldTypeDefinition = documentManager.getFieldTypeDefinition(fieldDefinition);
        if (packageManager.isLocalPackage(fieldDefinition) &&
                !fieldDefinition.isInvokeField() &&
                fieldTypeDefinition.isObject()) {
            if (fieldDefinition.isConnectionField()) {
                return splitConnections(objectType, fieldDefinition, field);
            } else {
                return Stream.of(
                        field.setSelections(
                                Stream.ofNullable(field.getFields())
                                        .flatMap(Collection::stream)
                                        .flatMap(subField -> buildConnection(fieldTypeDefinition.asObject(), fieldTypeDefinition.asObject().getField(subField.getName()), subField))
                                        .collect(Collectors.toList())
                        )
                );
            }
        } else {
            return Stream.of(field);
        }
    }

    private Stream<Field> splitConnections(ObjectType objectType, FieldDefinition fieldDefinition, Field field) {
        return Streams.concat(
                Stream.of(field),
                buildListField(objectType, fieldDefinition, field).stream(),
                buildAggField(objectType, fieldDefinition, field).stream()
        );
    }

    private Optional<Field> buildListField(ObjectType objectType, FieldDefinition fieldDefinition, Field field) {
        if (field.getFields() == null || field.getFields().isEmpty()) {
            throw new GraphQLErrors(OBJECT_SELECTION_NOT_EXIST.bind(field.toString()));
        }
        return Optional.ofNullable(field.getField(FIELD_EDGES_NAME))
                .map(edges -> {
                            if (edges.getFields() == null || edges.getFields().isEmpty()) {
                                throw new GraphQLErrors(OBJECT_SELECTION_NOT_EXIST.bind(edges.toString()));
                            }
                            FieldDefinition connectionFieldDefinition = objectType.getField(fieldDefinition.getConnectionFieldOrError());
                            ObjectType fieldTypeDefinition = documentManager.getFieldTypeDefinition(connectionFieldDefinition).asObject();
                            Stream<Field> fieldStream = Stream
                                    .concat(
                                            Stream.ofNullable(edges.getField(FIELD_CURSOR_NAME))
                                                    .map(cursor -> fieldTypeDefinition.getCursorField().orElseGet(fieldTypeDefinition::getIDFieldOrError))
                                                    .map(cursorDefinition -> new Field(cursorDefinition.getName())),
                                            Stream.ofNullable(edges.getField(FIELD_NODE_NAME))
                                                    .flatMap(node -> {
                                                                if (node.getFields() == null || node.getFields().isEmpty()) {
                                                                    throw new GraphQLErrors(OBJECT_SELECTION_NOT_EXIST.bind(node.toString()));
                                                                }
                                                                return node.getFields().stream()
                                                                        .flatMap(subField ->
                                                                                buildConnection(
                                                                                        fieldTypeDefinition.asObject(),
                                                                                        fieldTypeDefinition.asObject().getField(subField.getName()),
                                                                                        subField
                                                                                )
                                                                        );
                                                            }
                                                    )
                                    )
                                    .filter(StreamUtil.distinctByKey(AbstractDefinition::getName));

                            return new Field(connectionFieldDefinition.getName())
                                    .setSelections(fieldStream.collect(Collectors.toList()))
                                    .setArguments(
                                            Stream.ofNullable(field.getArguments())
                                                    .flatMap(arguments -> arguments.getArguments().entrySet().stream())
                                                    .map(entry -> {
                                                                if (entry.getKey().equals(INPUT_VALUE_FIRST_NAME) || entry.getKey().equals(INPUT_VALUE_LAST_NAME) && !entry.getValue().isNull()) {
                                                                    return new AbstractMap.SimpleEntry<>(entry.getKey(), ValueWithVariable.of(entry.getValue().asInt().getIntegerValue() + 1));
                                                                } else {
                                                                    return new AbstractMap.SimpleEntry<>(entry.getKey(), entry.getValue());
                                                                }
                                                            }
                                                    )
                                                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue))
                                    )
                                    .addDirective(new Directive(DIRECTIVE_HIDE_NAME));
                        }
                );
    }

    private Optional<Field> buildAggField(ObjectType objectType, FieldDefinition fieldDefinition, Field field) {
        if (field.getFields() == null || field.getFields().isEmpty()) {
            throw new GraphQLErrors(OBJECT_SELECTION_NOT_EXIST.bind(field.toString()));
        }
        return Optional.ofNullable(field.getField(FIELD_TOTAL_COUNT_NAME))
                .map(totalCount -> {
                            FieldDefinition connectionAggDefinition = objectType.getField(fieldDefinition.getConnectionAggOrError());
                            ObjectType fieldTypeDefinition = documentManager.getFieldTypeDefinition(connectionAggDefinition).asObject();
                            return new Field(connectionAggDefinition.getName())
                                    .addSelection(new Field(fieldTypeDefinition.getIDFieldOrError().getName() + SUFFIX_COUNT))
                                    .setArguments(
                                            Stream.ofNullable(field.getArguments())
                                                    .flatMap(arguments -> arguments.getArguments().entrySet().stream())
                                                    .filter(entry -> !entry.getKey().equals(INPUT_VALUE_FIRST_NAME) && !entry.getKey().equals(INPUT_VALUE_LAST_NAME))
                                                    .map(entry -> new AbstractMap.SimpleEntry<>(entry.getKey(), entry.getValue()))
                                                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue))
                                    )
                                    .addDirective(new Directive(DIRECTIVE_HIDE_NAME));
                        }
                );
    }
}
