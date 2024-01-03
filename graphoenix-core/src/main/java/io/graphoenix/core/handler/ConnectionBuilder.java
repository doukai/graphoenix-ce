package io.graphoenix.core.handler;

import io.graphoenix.spi.error.GraphQLErrors;
import io.graphoenix.spi.graphql.operation.Field;
import io.graphoenix.spi.graphql.type.FieldDefinition;
import io.graphoenix.spi.graphql.type.ObjectType;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.json.JsonArray;
import jakarta.json.JsonObjectBuilder;
import jakarta.json.JsonValue;
import jakarta.json.spi.JsonProvider;
import jakarta.json.stream.JsonCollectors;

import java.util.Collections;

import static io.graphoenix.spi.constant.Hammurabi.*;
import static io.graphoenix.spi.error.GraphQLErrorType.CONNECTION_NOT_EXIST;
import static io.graphoenix.spi.error.GraphQLErrorType.OBJECT_SELECTION_NOT_EXIST;
import static jakarta.json.JsonValue.EMPTY_JSON_OBJECT;
import static jakarta.json.JsonValue.NULL;

@ApplicationScoped
public class ConnectionBuilder {

    private final DocumentManager documentManager;
    private final JsonProvider jsonProvider;

    @Inject
    public ConnectionBuilder(DocumentManager documentManager, JsonProvider jsonProvider) {
        this.documentManager = documentManager;
        this.jsonProvider = jsonProvider;
    }

    public JsonValue build(String typeName, Field connectionField, JsonValue jsonValue) {
        if (jsonValue == null || jsonValue.getValueType().equals(JsonValue.ValueType.NULL)) {
            return NULL;
        }
        if (jsonValue.asJsonObject().isEmpty()) {
            return EMPTY_JSON_OBJECT;
        }
        JsonObjectBuilder connectionObjectBuilder = jsonProvider.createObjectBuilder();
        if (connectionField.getFields() != null && !connectionField.getFields().isEmpty()) {
            ObjectType objectType = documentManager.getDocument().getObjectTypeOrError(typeName);
            FieldDefinition connectionFieldDefinition = objectType.getField(connectionField.getName());
            ObjectType fieldTypeDefinition = documentManager.getFieldTypeDefinition(connectionFieldDefinition).asObject();
            if (connectionFieldDefinition.isConnectionField()) {
                String connectionFieldName = connectionFieldDefinition.getConnectionFieldOrError();
                String connectionAggFieldName = connectionFieldDefinition.getConnectionAggOrError();
                FieldDefinition cursorFieldDefinition = objectType.getCursorField()
                        .orElseGet(objectType::getIDFieldOrError);

                for (Field field : connectionField.getFields()) {
                    JsonArray nodeArray = jsonValue.asJsonObject().get(connectionFieldName).asJsonArray();
                    int limit;
                    boolean isLast;
                    boolean isBefore;
                    boolean isAfter;
                    if (field.getArguments() != null && !field.getArguments().isEmpty()) {
                        limit = field.getArguments().getArguments().entrySet().stream()
                                .filter(entry -> entry.getKey().equals(INPUT_VALUE_FIRST_NAME))
                                .filter(entry -> entry.getValue().isInt())
                                .findFirst()
                                .map(entry -> entry.getValue().asInt().getIntegerValue())
                                .orElseGet(() ->
                                        field.getArguments().getArguments().entrySet().stream()
                                                .filter(entry -> entry.getKey().equals(INPUT_VALUE_AFTER_NAME))
                                                .filter(entry -> entry.getValue().isInt())
                                                .findFirst()
                                                .map(entry -> entry.getValue().asInt().getIntegerValue())
                                                .orElse(nodeArray.size())
                                );
                        isLast = field.getArguments().getArguments().entrySet().stream().anyMatch(entry -> entry.getKey().equals(INPUT_VALUE_LAST_NAME));
                        isBefore = field.getArguments().getArguments().entrySet().stream().anyMatch(entry -> entry.getKey().equals(INPUT_VALUE_BEFORE_NAME));
                        isAfter = field.getArguments().getArguments().entrySet().stream().anyMatch(entry -> entry.getKey().equals(INPUT_VALUE_AFTER_NAME));
                    } else {
                        limit = nodeArray.size();
                        isLast = false;
                        isBefore = false;
                        isAfter = false;
                    }
                    boolean isFirst = !isLast;

                    switch (field.getName()) {
                        case FIELD_TOTAL_COUNT_NAME:
                            FieldDefinition idField = fieldTypeDefinition.getIDFieldOrError();
                            connectionObjectBuilder.add(FIELD_TOTAL_COUNT_NAME, jsonValue.asJsonObject().get(connectionAggFieldName).asJsonObject().get(idField.getName() + SUFFIX_COUNT));
                            break;
                        case FIELD_EDGES_NAME:
                            if (field.getFields() == null || field.getFields().isEmpty()) {
                                throw new GraphQLErrors(OBJECT_SELECTION_NOT_EXIST.bind(field.toString()));
                            }
                            JsonArray edges = nodeArray.stream()
                                    .limit(limit)
                                    .map(node -> {
                                                JsonObjectBuilder edge = jsonProvider.createObjectBuilder();
                                                for (Field edgeField : field.getFields()) {
                                                    if (edgeField.getName().equals(FIELD_CURSOR_NAME)) {
                                                        edge.add(FIELD_CURSOR_NAME, node.asJsonObject().get(cursorFieldDefinition.getName()));
                                                    } else if (edgeField.getName().equals(FIELD_NODE_NAME)) {
                                                        edge.add(FIELD_NODE_NAME, node.asJsonObject());
                                                    }
                                                }
                                                return edge.build();
                                            }
                                    )
                                    .collect(JsonCollectors.toJsonArray());
                            if (isLast) {
                                Collections.reverse(edges);
                            }
                            connectionObjectBuilder.add(FIELD_EDGES_NAME, edges);
                            break;
                        case FIELD_PAGE_INFO_NAME:
                            if (field.getFields() == null || field.getFields().isEmpty()) {
                                throw new GraphQLErrors(OBJECT_SELECTION_NOT_EXIST.bind(field.toString()));
                            }
                            JsonObjectBuilder pageInfoBuilder = jsonProvider.createObjectBuilder();
                            for (Field pageInfoField : field.getFields()) {
                                switch (pageInfoField.getName()) {
                                    case FIELD_HAS_NEXT_PAGE_NAME:
                                        if (isFirst) {
                                            pageInfoBuilder.add(FIELD_HAS_NEXT_PAGE_NAME, nodeArray != null && limit < nodeArray.size());
                                        } else {
                                            pageInfoBuilder.add(FIELD_HAS_NEXT_PAGE_NAME, isBefore);
                                        }
                                        break;
                                    case FIELD_HAS_PREVIOUS_PAGE_NAME:
                                        if (isLast) {
                                            pageInfoBuilder.add(FIELD_HAS_PREVIOUS_PAGE_NAME, nodeArray != null && limit < nodeArray.size());
                                        } else {
                                            pageInfoBuilder.add(FIELD_HAS_PREVIOUS_PAGE_NAME, isAfter);
                                        }
                                        break;
                                    case FIELD_START_CURSOR_NAME:
                                        if (nodeArray != null && !nodeArray.isEmpty()) {
                                            if (limit < nodeArray.size()) {
                                                if (isFirst) {
                                                    pageInfoBuilder.add(FIELD_START_CURSOR_NAME, nodeArray.get(0).asJsonObject().get(connectionField.getName()));
                                                } else {
                                                    pageInfoBuilder.add(FIELD_START_CURSOR_NAME, nodeArray.get(nodeArray.size() - 2).asJsonObject().get(connectionField.getName()));
                                                }
                                            } else {
                                                if (isFirst) {
                                                    pageInfoBuilder.add(FIELD_START_CURSOR_NAME, nodeArray.get(0).asJsonObject().get(connectionField.getName()));
                                                } else {
                                                    pageInfoBuilder.add(FIELD_START_CURSOR_NAME, nodeArray.get(nodeArray.size() - 1).asJsonObject().get(connectionField.getName()));
                                                }
                                            }
                                        } else {
                                            pageInfoBuilder.add(FIELD_START_CURSOR_NAME, NULL);
                                        }
                                        break;
                                    case FIELD_END_CURSOR_NAME:
                                        if (nodeArray != null && !nodeArray.isEmpty()) {
                                            if (limit < nodeArray.size()) {
                                                if (isFirst) {
                                                    pageInfoBuilder.add(FIELD_END_CURSOR_NAME, nodeArray.get(nodeArray.size() - 2).asJsonObject().get(connectionField.getName()));
                                                } else {
                                                    pageInfoBuilder.add(FIELD_END_CURSOR_NAME, nodeArray.get(0).asJsonObject().get(connectionField.getName()));
                                                }
                                            } else {
                                                if (isFirst) {
                                                    pageInfoBuilder.add(FIELD_END_CURSOR_NAME, nodeArray.get(nodeArray.size() - 1).asJsonObject().get(connectionField.getName()));
                                                } else {
                                                    pageInfoBuilder.add(FIELD_END_CURSOR_NAME, nodeArray.get(0).asJsonObject().get(connectionField.getName()));
                                                }
                                            }
                                        } else {
                                            pageInfoBuilder.add("endCursor", NULL);
                                        }
                                        break;
                                }
                            }
                            connectionObjectBuilder.add("pageInfo", pageInfoBuilder);
                            break;
                    }
                }
            } else {
                throw new GraphQLErrors(CONNECTION_NOT_EXIST.bind(connectionField.toString()));
            }
        }
        return connectionObjectBuilder.build();
    }
}
