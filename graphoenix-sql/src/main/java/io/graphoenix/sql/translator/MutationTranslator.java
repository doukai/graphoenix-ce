package io.graphoenix.sql.translator;

import com.google.common.collect.Streams;
import io.graphoenix.core.handler.DocumentManager;
import io.graphoenix.core.handler.PackageManager;
import io.graphoenix.spi.error.GraphQLErrors;
import io.graphoenix.spi.graphql.Definition;
import io.graphoenix.spi.graphql.common.ValueWithVariable;
import io.graphoenix.spi.graphql.operation.Field;
import io.graphoenix.spi.graphql.operation.Operation;
import io.graphoenix.spi.graphql.type.FieldDefinition;
import io.graphoenix.spi.graphql.type.InputValue;
import io.graphoenix.spi.graphql.type.ObjectType;
import io.graphoenix.sql.expression.JsonTableFunction;
import io.graphoenix.sql.utils.DBValueUtil;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import net.sf.jsqlparser.expression.*;
import net.sf.jsqlparser.expression.operators.relational.EqualsTo;
import net.sf.jsqlparser.expression.operators.relational.ExpressionList;
import net.sf.jsqlparser.expression.operators.relational.InExpression;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.schema.Table;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.create.table.ColumnDefinition;
import net.sf.jsqlparser.statement.insert.Insert;
import net.sf.jsqlparser.statement.select.*;
import net.sf.jsqlparser.statement.update.Update;
import net.sf.jsqlparser.statement.update.UpdateSet;
import net.sf.jsqlparser.util.cnfexpression.MultiAndExpression;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static io.graphoenix.spi.constant.Hammurabi.*;
import static io.graphoenix.spi.error.GraphQLErrorType.UNSUPPORTED_FIELD_TYPE;
import static io.graphoenix.sql.utils.DBNameUtil.*;
import static io.graphoenix.sql.utils.DBValueUtil.*;

@ApplicationScoped
public class MutationTranslator {

    private final DocumentManager documentManager;
    private final PackageManager packageManager;
    private final ArgumentsTranslator argumentsTranslator;
    private final TypeTranslator typeTranslator;

    @Inject
    public MutationTranslator(DocumentManager documentManager, PackageManager packageManager, ArgumentsTranslator argumentsTranslator, TypeTranslator typeTranslator) {
        this.documentManager = documentManager;
        this.packageManager = packageManager;
        this.argumentsTranslator = argumentsTranslator;
        this.typeTranslator = typeTranslator;
    }

    public Stream<String> operationToStatementSQLStream(Operation operation) {
        return operationToStatementStream(operation).map(Object::toString);
    }

    public Stream<Statement> operationToStatementStream(Operation operation) {
        ObjectType operationType = documentManager.getOperationTypeOrError(operation);
        return operation.getFields().stream()
                .filter(field -> {
                            FieldDefinition fieldDefinition = operationType.getFieldOrError(field.getName());
                            return packageManager.isLocalPackage(fieldDefinition) &&
                                    !fieldDefinition.isFetchField() &&
                                    !fieldDefinition.isInvokeField() &&
                                    !fieldDefinition.isFunctionField() &&
                                    !fieldDefinition.isConnectionField();
                        }
                )
                .flatMap(field -> fieldToMutationStatementStream(operationType, operationType.getFieldOrError(field.getName()), field));
    }

    protected Stream<Statement> fieldToMutationStatementStream(ObjectType objectType, FieldDefinition fieldDefinition, Field field) {
        Definition fieldTypeDefinition = documentManager.getFieldTypeDefinition(fieldDefinition);
        if (fieldTypeDefinition.isObject()) {
            return fieldDefinition.getArgumentOrEmpty(INPUT_VALUE_LIST_NAME)
                    .flatMap(inputValue -> {
                                Table table = typeToTable(fieldTypeDefinition.asObject());
                                List<FieldDefinition> leafFieldDefinitionList = fieldTypeDefinition.asObject().getFields().stream()
                                        .filter(subField -> !subField.isInvokeField())
                                        .filter(subField -> !subField.isFetchField())
                                        .filter(subField -> !subField.isFunctionField())
                                        .filter(subField -> !subField.getType().hasList())
                                        .filter(subField -> !documentManager.getFieldTypeDefinition(subField).isObject())
                                        .collect(Collectors.toList());

                                return Optional.ofNullable(field.getArguments())
                                        .flatMap(arguments -> arguments.getArgumentOrEmpty(inputValue.getName()))
                                        .filter(valueWithVariable -> !valueWithVariable.isNull())
                                        .map(valueWithVariable -> {
                                                    if (valueWithVariable.isVariable()) {
                                                        return Stream.of(
                                                                (Statement) insertSelectExpression(
                                                                        table,
                                                                        leafFieldDefinitionList.stream()
                                                                                .map(subField -> graphqlFieldToColumn(table, subField.getName()))
                                                                                .collect(Collectors.toList()),
                                                                        selectVariablesFromJsonObjectArray(leafFieldDefinitionList, inputValue, valueWithVariable.asVariable())
                                                                )
                                                        );
                                                    } else {
                                                        return IntStream.range(0, valueWithVariable.asArray().size())
                                                                .mapToObj(index ->
                                                                        objectToMutationStatementStream(
                                                                                objectType,
                                                                                null,
                                                                                fieldDefinition,
                                                                                inputValue,
                                                                                valueWithVariable.asArray().getValueWithVariable(index),
                                                                                field.isMerge(),
                                                                                0,
                                                                                index
                                                                        )

                                                                )
                                                                .flatMap(stream -> stream);
                                                    }
                                                }
                                        );
                            }
                    )
                    .orElseGet(() ->
                            fieldDefinition.getArgumentOrEmpty(INPUT_VALUE_INPUT_NAME)
                                    .flatMap(inputValue -> {
                                                Table table = typeToTable(fieldTypeDefinition.asObject());
                                                List<FieldDefinition> leafFieldDefinitionList = fieldTypeDefinition.asObject().getFields().stream()
                                                        .filter(subField -> !subField.isInvokeField())
                                                        .filter(subField -> !subField.isFetchField())
                                                        .filter(subField -> !subField.isFunctionField())
                                                        .filter(subField -> !subField.getType().hasList())
                                                        .filter(subField -> !documentManager.getFieldTypeDefinition(subField).isObject())
                                                        .collect(Collectors.toList());

                                                return Optional.ofNullable(field.getArguments())
                                                        .flatMap(arguments -> arguments.getArgumentOrEmpty(inputValue.getName()))
                                                        .filter(valueWithVariable -> !valueWithVariable.isNull())
                                                        .map(valueWithVariable -> {
                                                                    if (valueWithVariable.isVariable()) {
                                                                        return Stream.of(
                                                                                (Statement) insertSelectExpression(
                                                                                        table,
                                                                                        leafFieldDefinitionList.stream()
                                                                                                .map(subField -> graphqlFieldToColumn(table, subField.getName()))
                                                                                                .collect(Collectors.toList()),
                                                                                        selectVariablesFromJsonObject(leafFieldDefinitionList, inputValue, valueWithVariable.asVariable())
                                                                                )
                                                                        );
                                                                    } else {
                                                                        return objectToMutationStatementStream(
                                                                                objectType,
                                                                                null,
                                                                                fieldDefinition,
                                                                                inputValue,
                                                                                valueWithVariable,
                                                                                field.isMerge(),
                                                                                0,
                                                                                0
                                                                        );
                                                                    }
                                                                }
                                                        );
                                            }
                                    )
                                    .orElseGet(() -> {
                                                Map<InputValue, ValueWithVariable> inputValueValueWithVariableMap = Stream.ofNullable(fieldDefinition.getArguments())
                                                        .flatMap(Collection::stream)
                                                        .flatMap(argumentInput ->
                                                                Optional.ofNullable(field.getArguments())
                                                                        .flatMap(arguments -> arguments.getArgumentOrEmpty(argumentInput.getName()))
                                                                        .or(() -> Optional.ofNullable(argumentInput.getDefaultValue()))
                                                                        .stream()
                                                                        .map(valueWithVariable -> new AbstractMap.SimpleEntry<>(argumentInput, valueWithVariable))
                                                        )
                                                        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

                                                return inputValueMapToMutationStatementStream(objectType, fieldDefinition, inputValueValueWithVariableMap, field.isMerge());
                                            }
                                    )
                    );
        }
        throw new GraphQLErrors(UNSUPPORTED_FIELD_TYPE.bind(field.toString()));
    }

    protected Stream<Statement> objectToMutationStatementStream(ObjectType objectType,
                                                                Expression parentIdExpression,
                                                                FieldDefinition fieldDefinition,
                                                                InputValue inputValue,
                                                                ValueWithVariable valueWithVariable,
                                                                boolean merge,
                                                                int level,
                                                                int index) {

        Definition inputValueTypeDefinition = documentManager.getInputValueTypeDefinition(inputValue);
        Map<InputValue, ValueWithVariable> inputValueValueWithVariableMap = Stream.ofNullable(inputValueTypeDefinition.asInputObject().getInputValues())
                .flatMap(Collection::stream)
                .flatMap(fieldInput ->
                        Optional.ofNullable(valueWithVariable)
                                .filter(ValueWithVariable::isObject)
                                .map(ValueWithVariable::asObject)
                                .flatMap(objectValueWithVariable -> objectValueWithVariable.getValueWithVariableOrEmpty(fieldInput.getName()))
                                .or(() -> Optional.ofNullable(fieldInput.getDefaultValue()))
                                .stream()
                                .map(field -> new AbstractMap.SimpleEntry<>(fieldInput, field))
                )
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        if (valueWithVariable == null || valueWithVariable.isNull()) {
            if (parentIdExpression != null) {
                return Stream.of(
                        removeMapStatement(
                                objectType,
                                fieldDefinition,
                                parentIdExpression,
                                null
                        )
                );
            } else {
                return Stream.empty();
            }
        }

        if (valueWithVariable.isVariable()) {
            Definition fieldTypeDefinition = documentManager.getFieldTypeDefinition(fieldDefinition);
            List<FieldDefinition> leafFieldDefinitionList = fieldTypeDefinition.asObject().getFields().stream()
                    .filter(subField -> !subField.isInvokeField())
                    .filter(subField -> !subField.isFetchField())
                    .filter(subField -> !subField.isFunctionField())
                    .filter(subField -> !subField.getType().hasList())
                    .filter(subField -> !documentManager.getFieldTypeDefinition(subField).isObject())
                    .collect(Collectors.toList());
            Table table = typeToTable(fieldTypeDefinition.asObject());

            return Stream.of(
                    insertExpression(
                            table,
                            leafFieldDefinitionList.stream()
                                    .map(subField -> graphqlFieldToColumn(table, subField.getName()))
                                    .collect(Collectors.toList()),
                            leafFieldDefinitionList.stream()
                                    .map(subField -> getValueFromObjectVariable(valueWithVariable, inputValue))
                                    .collect(Collectors.toList()),
                            true
                    )
            );
        } else {
            return inputValueMapToMutationStatementStream(objectType, parentIdExpression, fieldDefinition, inputValueValueWithVariableMap, merge, level, index);
        }
    }

    protected Stream<Statement> inputValueMapToMutationStatementStream(ObjectType objectType,
                                                                       FieldDefinition fieldDefinition,
                                                                       Map<InputValue, ValueWithVariable> inputValueValueWithVariableMap,
                                                                       boolean merge) {

        return inputValueMapToMutationStatementStream(objectType, null, fieldDefinition, inputValueValueWithVariableMap, merge, 0, 0);

    }

    protected Stream<Statement> inputValueMapToMutationStatementStream(ObjectType objectType,
                                                                       Expression parentIdExpression,
                                                                       FieldDefinition fieldDefinition,
                                                                       Map<InputValue, ValueWithVariable> inputValueValueWithVariableMap,
                                                                       boolean merge,
                                                                       int level,
                                                                       int index) {
        Definition fieldTypeDefinition = documentManager.getFieldTypeDefinition(fieldDefinition);

        List<FieldDefinition> leafFieldDefinitionList = fieldTypeDefinition.asObject().getFields().stream()
                .filter(subField -> !subField.isInvokeField())
                .filter(subField -> !subField.isFetchField())
                .filter(subField -> !subField.isFunctionField())
                .filter(subField -> !subField.getType().hasList())
                .filter(subField -> !documentManager.getFieldTypeDefinition(subField).isObject())
                .collect(Collectors.toList());

        Map<String, ValueWithVariable> leafValueWithVariableMap = leafFieldDefinitionList.stream()
                .flatMap(subField ->
                        inputValueValueWithVariableMap.entrySet().stream()
                                .filter(entry -> entry.getKey().getName().equals(subField.getName()))
                )
                .map(entry -> new AbstractMap.SimpleEntry<>(entry.getKey().getName(), entry.getValue()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        Optional<Map.Entry<InputValue, ValueWithVariable>> whereInputValueEntry = inputValueValueWithVariableMap.entrySet().stream()
                .filter(entry -> entry.getKey().getName().equals(INPUT_VALUE_WHERE_NAME))
                .filter(entry -> entry.getValue().isObject())
                .findFirst();

        String idName = fieldTypeDefinition.asObject().getIDFieldOrError().getName();
        Expression idExpression = inputValueValueWithVariableMap.entrySet().stream()
                .filter(entry -> entry.getKey().getName().equals(idName))
                .findFirst()
                .map(Map.Entry::getValue)
                .flatMap(DBValueUtil::idValueToDBValue)
                .or(() ->
                        whereInputValueEntry
                                .flatMap(entry -> entry.getValue().asObject().getValueWithVariableOrEmpty(idName))
                                .filter(ValueWithVariable::isObject)
                                .flatMap(valueWithVariable -> valueWithVariable.asObject().getValueWithVariableOrEmpty(INPUT_OPERATOR_INPUT_VALUE_VAL_NAME))
                                .flatMap(DBValueUtil::idValueToDBValue)
                )
                .orElseGet(() -> createInsertIdUserVariable(fieldTypeDefinition.asObject().getName(), idName, level, index));

        Stream<Statement> leafFieldMutationStatement = Stream.of(
                whereInputValueEntry
                        .flatMap(entry -> argumentsTranslator.inputValueToWhereExpression(objectType, fieldDefinition, entry.getKey(), entry.getValue(), level))
                        .map(whereExpression -> {
                                    Table table = typeToTable(fieldTypeDefinition.asObject(), level);
                                    return (Statement) updateExpression(
                                            table,
                                            leafValueWithVariableMap.entrySet().stream()
                                                    .map(entry ->
                                                            new UpdateSet(
                                                                    graphqlFieldToColumn(table, entry.getKey()),
                                                                    leafValueToDBValue(entry.getValue()))
                                                    )
                                                    .collect(Collectors.toList()),
                                            whereExpression
                                    );
                                }
                        )
                        .orElseGet(() -> {
                                    Table table = typeToTable(fieldTypeDefinition.asObject());
                                    return insertExpression(
                                            table,
                                            leafValueWithVariableMap.keySet().stream()
                                                    .map(name -> graphqlFieldToColumn(table, name))
                                                    .collect(Collectors.toList()),
                                            leafValueWithVariableMap.values().stream()
                                                    .map(DBValueUtil::leafValueToDBValue)
                                                    .collect(Collectors.toList()),
                                            true
                                    );
                                }
                        )
        );
        Stream<Statement> createInsertIdSetStatementStream;
        if (whereInputValueEntry.isPresent()) {
            createInsertIdSetStatementStream = whereInputValueEntry
                    .flatMap(entry -> entry.getValue().asObject().getValueWithVariableOrEmpty(idName))
                    .filter(ValueWithVariable::isObject)
                    .flatMap(valueWithVariable -> valueWithVariable.asObject().getValueWithVariableOrEmpty(INPUT_OPERATOR_INPUT_VALUE_VAL_NAME))
                    .flatMap(DBValueUtil::idValueToDBValue).stream()
                    .map(expression -> createUpdateIdSetStatement(fieldTypeDefinition.asObject().getName(), idName, level, index, expression));
        } else if (inputValueValueWithVariableMap.entrySet().stream()
                .anyMatch(entry -> entry.getKey().getName().equals(idName))) {
            createInsertIdSetStatementStream = inputValueValueWithVariableMap.entrySet().stream()
                    .filter(entry -> entry.getKey().getName().equals(idName))
                    .findFirst()
                    .map(Map.Entry::getValue)
                    .flatMap(DBValueUtil::idValueToDBValue)
                    .map(expression -> (Statement) createUpdateIdSetStatement(fieldTypeDefinition.asObject().getName(), idName, level, index, expression))
                    .stream();
        } else {
            createInsertIdSetStatementStream = Stream.of(createInsertIdSetStatement(fieldTypeDefinition.asObject().getName(), idName, level, index));
        }

        Stream<Statement> objectFieldMergeMapStatementStream = Stream.empty();
        if (parentIdExpression != null) {
            objectFieldMergeMapStatementStream = Stream.of(
                    mergeObjectMapStatement(
                            objectType,
                            fieldDefinition,
                            parentIdExpression,
                            idExpression
                    )
            );
        }

        Stream<Statement> objectFieldMutationStatementStream = fieldTypeDefinition.asObject().getFields().stream()
                .filter(subField -> !subField.isFetchField())
                .filter(subField -> !subField.getType().hasList())
                .filter(subField -> documentManager.getFieldTypeDefinition(subField).isObject())
                .flatMap(subField ->
                        inputValueValueWithVariableMap.entrySet().stream()
                                .filter(entry -> entry.getKey().getName().equals(subField.getName()))
                                .flatMap(entry ->
                                        objectToMutationStatementStream(
                                                fieldTypeDefinition.asObject(),
                                                idExpression,
                                                subField,
                                                entry.getKey(),
                                                entry.getValue(),
                                                merge,
                                                level + 1,
                                                index
                                        )
                                )
                );

        Stream<Statement> listObjectFieldMutationStatementStream = fieldTypeDefinition.asObject().getFields().stream()
                .filter(subField -> !subField.isFetchField())
                .filter(subField -> subField.getType().hasList())
                .filter(subField -> documentManager.getFieldTypeDefinition(subField).isObject())
                .flatMap(subField ->
                        inputValueValueWithVariableMap.entrySet().stream()
                                .filter(entry -> entry.getKey().getName().equals(subField.getName()))
                                .flatMap(entry ->
                                        listObjectToMutationStatementStream(
                                                fieldTypeDefinition.asObject(),
                                                idExpression,
                                                subField,
                                                entry.getKey(),
                                                entry.getValue(),
                                                merge,
                                                level + 1
                                        )
                                )
                );

        Stream<Statement> listLeafFieldMutationStatementStream = fieldTypeDefinition.asObject().getFields().stream()
                .filter(subField -> !subField.isFetchField())
                .filter(subField -> subField.getType().hasList())
                .filter(subField -> !documentManager.getFieldTypeDefinition(subField).isObject())
                .flatMap(subField ->
                        inputValueValueWithVariableMap.entrySet().stream()
                                .filter(entry -> entry.getKey().getName().equals(subField.getName()))
                                .flatMap(entry ->
                                        listLeafToStatementStream(
                                                fieldTypeDefinition.asObject(),
                                                idExpression,
                                                subField,
                                                entry.getValue()
                                        )
                                )
                );

        return Streams.concat(
                leafFieldMutationStatement,
                createInsertIdSetStatementStream,
                objectFieldMergeMapStatementStream,
                objectFieldMutationStatementStream,
                listObjectFieldMutationStatementStream,
                listLeafFieldMutationStatementStream
        );
    }

    protected Stream<Statement> listObjectToMutationStatementStream(ObjectType objectType,
                                                                    Expression parentIdExpression,
                                                                    FieldDefinition fieldDefinition,
                                                                    InputValue inputValue,
                                                                    ValueWithVariable valueWithVariable,
                                                                    boolean merge,
                                                                    int level) {
        Stream<Statement> mutationStatementStream = Stream.empty();
        if (valueWithVariable.isVariable()) {
            Definition fieldTypeDefinition = documentManager.getFieldTypeDefinition(fieldDefinition);
            Table table = typeToTable(fieldTypeDefinition.asObject());
            List<FieldDefinition> leafFieldDefinitionList = fieldTypeDefinition.asObject().getFields().stream()
                    .filter(subField -> !subField.isInvokeField())
                    .filter(subField -> !subField.isFetchField())
                    .filter(subField -> !subField.isFunctionField())
                    .filter(subField -> !subField.getType().hasList())
                    .filter(subField -> !documentManager.getFieldTypeDefinition(subField).isObject())
                    .collect(Collectors.toList());

            mutationStatementStream = Stream.of(
                    insertSelectExpression(
                            table,
                            leafFieldDefinitionList.stream()
                                    .map(subField -> graphqlFieldToColumn(table, subField.getName()))
                                    .collect(Collectors.toList()),
                            selectVariablesFromJsonObjectArray(leafFieldDefinitionList, inputValue, valueWithVariable.asVariable())
                    )
            );
        } else if (valueWithVariable.isArray()) {
            if (merge) {
                List<ValueWithVariable> mergeValueWithVariableList = valueWithVariable.asArray().getValueWithVariables().stream()
                        .filter(ValueWithVariable::isObject)
                        .map(ValueWithVariable::asObject)
                        .filter(objectValueWithVariable ->
                                !objectValueWithVariable.containsKey(FIELD_DEPRECATED_NAME) ||
                                        !objectValueWithVariable.getBoolean(FIELD_DEPRECATED_NAME)
                        )
                        .collect(Collectors.toList());

                if (!mergeValueWithVariableList.isEmpty()) {
                    mutationStatementStream = IntStream.range(0, mergeValueWithVariableList.size())
                            .mapToObj(index ->
                                    objectToMutationStatementStream(
                                            objectType,
                                            parentIdExpression,
                                            fieldDefinition,
                                            inputValue,
                                            valueWithVariable.asArray().getValueWithVariables().get(index),
                                            merge,
                                            level,
                                            index
                                    )
                            )
                            .flatMap(statementStream -> statementStream);
                }
            } else {
                mutationStatementStream = IntStream.range(0, valueWithVariable.asArray().size())
                        .mapToObj(index ->
                                objectToMutationStatementStream(
                                        objectType,
                                        parentIdExpression,
                                        fieldDefinition,
                                        inputValue,
                                        valueWithVariable.asArray().getValueWithVariables().get(index),
                                        merge,
                                        level,
                                        index
                                )
                        )
                        .flatMap(statementStream -> statementStream);
            }
        }

        if (merge) {
            Definition fieldTypeDefinition = documentManager.getFieldTypeDefinition(fieldDefinition);
            String idName = fieldTypeDefinition.asObject().getIDFieldOrError().getName();
            if (fieldDefinition.hasMapWith()) {
                List<ValueWithVariable> idValueWithVariableList = valueWithVariable.asArray().getValueWithVariables().stream()
                        .filter(ValueWithVariable::isObject)
                        .map(ValueWithVariable::asObject)
                        .flatMap(objectValueWithVariable ->
                                objectValueWithVariable.getValueWithVariableOrEmpty(idName)
                                        .or(() ->
                                                objectValueWithVariable.getValueWithVariableOrEmpty(INPUT_VALUE_WHERE_NAME)
                                                        .filter(ValueWithVariable::isObject)
                                                        .map(ValueWithVariable::asObject)
                                                        .flatMap(where -> where.getValueWithVariableOrEmpty(idName))
                                                        .filter(ValueWithVariable::isObject)
                                                        .map(ValueWithVariable::asObject)
                                                        .flatMap(id -> id.getValueWithVariableOrEmpty(INPUT_OPERATOR_INPUT_VALUE_VAL_NAME))
                                        )
                                        .stream()
                        )
                        .collect(Collectors.toList());

                Stream<Statement> removeMapStatementStream = Stream.empty();
                if (!idValueWithVariableList.isEmpty()) {
                    removeMapStatementStream = Stream.of(
                            removeMapStatement(
                                    objectType,
                                    fieldDefinition,
                                    parentIdExpression,
                                    idValueWithVariableList.stream()
                                            .map(DBValueUtil::leafValueToDBValue)
                                            .collect(Collectors.toList())
                            )
                    );
                }
                return Stream
                        .concat(
                                removeMapStatementStream,
                                mutationStatementStream
                        );
            } else {
                List<ValueWithVariable> removeIdValueWithVariableList = valueWithVariable.asArray().getValueWithVariables().stream()
                        .filter(ValueWithVariable::isObject)
                        .map(ValueWithVariable::asObject)
                        .filter(objectValueWithVariable ->
                                objectValueWithVariable.containsKey(FIELD_DEPRECATED_NAME) &&
                                        objectValueWithVariable.getBoolean(FIELD_DEPRECATED_NAME)
                        )
                        .flatMap(objectValueWithVariable ->
                                objectValueWithVariable.getValueWithVariableOrEmpty(idName)
                                        .or(() ->
                                                objectValueWithVariable.getValueWithVariableOrEmpty(INPUT_VALUE_WHERE_NAME)
                                                        .filter(ValueWithVariable::isObject)
                                                        .map(ValueWithVariable::asObject)
                                                        .flatMap(where -> where.getValueWithVariableOrEmpty(idName))
                                                        .filter(ValueWithVariable::isObject)
                                                        .map(ValueWithVariable::asObject)
                                                        .flatMap(id -> id.getValueWithVariableOrEmpty(INPUT_OPERATOR_INPUT_VALUE_VAL_NAME))
                                        )
                                        .stream()
                        )
                        .collect(Collectors.toList());

                Stream<Statement> removeMapStatementStream = Stream.empty();
                if (!removeIdValueWithVariableList.isEmpty()) {
                    removeMapStatementStream = Stream.of(
                            removeMapStatement(
                                    objectType,
                                    fieldDefinition,
                                    parentIdExpression,
                                    removeIdValueWithVariableList.stream()
                                            .map(DBValueUtil::leafValueToDBValue)
                                            .collect(Collectors.toList()),
                                    false
                            )
                    );
                }
                return Stream
                        .concat(
                                mutationStatementStream,
                                removeMapStatementStream
                        );
            }
        } else {
            if (fieldDefinition.hasMapWith()) {
                Statement removeMapStatement = removeMapStatement(
                        objectType,
                        fieldDefinition,
                        parentIdExpression,
                        null
                );
                return Stream
                        .concat(
                                Stream.of(removeMapStatement),
                                mutationStatementStream
                        );
            } else {
                Definition fieldTypeDefinition = documentManager.getFieldTypeDefinition(fieldDefinition);
                String idFieldName = fieldTypeDefinition.asObject().getIDFieldOrError().getName();
                List<Expression> idValueExpressionList = IntStream.range(0, valueWithVariable.asArray().size())
                        .filter(index -> valueWithVariable.asArray().getValueWithVariables().get(index).isObject())
                        .mapToObj(index ->
                                valueWithVariable.asArray().getValueWithVariables().get(index).asObject().getValueWithVariableOrEmpty(idFieldName)
                                        .flatMap(DBValueUtil::idValueToDBValue)
                                        .orElseGet(() -> createInsertIdUserVariable(fieldTypeDefinition.asObject().getName(), idFieldName, level, index))
                        )
                        .collect(Collectors.toList());

                Statement removeMapStatement = removeMapStatement(
                        objectType,
                        fieldDefinition,
                        parentIdExpression,
                        idValueExpressionList
                );
                return Stream
                        .concat(
                                mutationStatementStream,
                                Stream.of(removeMapStatement)
                        );
            }
        }
    }

    protected Stream<Statement> listLeafToStatementStream(ObjectType objectType,
                                                          Expression parentIdExpression,
                                                          FieldDefinition fieldDefinition,
                                                          ValueWithVariable valueWithVariable) {

        Statement removeMapStatement = removeMapStatement(
                objectType,
                fieldDefinition,
                parentIdExpression,
                null
        );

        if (valueWithVariable.isVariable()) {
            return Stream.of(
                    removeMapStatement,
                    mergeLeafMapStatement(
                            objectType,
                            fieldDefinition,
                            valueWithVariable,
                            parentIdExpression
                    )
            );
        } else {
            return Stream.concat(
                    Stream.of(removeMapStatement),
                    valueWithVariable.asArray().getValueWithVariables().stream()
                            .map(item ->
                                    mergeLeafMapStatement(
                                            objectType,
                                            fieldDefinition,
                                            item,
                                            parentIdExpression
                                    )
                            )
            );
        }
    }

    protected Statement removeMapStatement(ObjectType objectType,
                                           FieldDefinition fieldDefinition,
                                           Expression parentIdExpression,
                                           List<Expression> idValueExpressionList) {
        return removeMapStatement(objectType, fieldDefinition, parentIdExpression, idValueExpressionList, true);
    }

    protected Statement removeMapStatement(ObjectType objectType,
                                           FieldDefinition fieldDefinition,
                                           Expression parentIdExpression,
                                           List<Expression> idValueExpressionList,
                                           boolean notIn) {
        Table parentTable = typeToTable(objectType);
        Column parentColumn = graphqlFieldToColumn(parentTable, fieldDefinition.getMapFromOrError());
        Column parentIdColumn = graphqlFieldToColumn(parentTable, objectType.getIDFieldOrError().getName());

        Definition fieldTypeDefinition = documentManager.getFieldTypeDefinition(fieldDefinition);

        Expression parentColumnExpression;
        if (fieldDefinition.getMapFromOrError().equals(objectType.getIDFieldOrError().getName())) {
            parentColumnExpression = parentIdExpression;
        } else {
            parentColumnExpression = selectFieldByIdExpression(parentTable, parentColumn, parentIdColumn, parentIdExpression);
        }

        if (fieldDefinition.hasMapWith()) {
            String mapWithType = fieldDefinition.getMapWithTypeOrError();
            Table withTable = graphqlTypeToTable(mapWithType);
            Column withParentColumn = graphqlFieldToColumn(withTable, fieldDefinition.getMapWithFromOrError());

            EqualsTo withParentColumnEqualsTo = new EqualsTo()
                    .withLeftExpression(withParentColumn)
                    .withRightExpression(parentColumnExpression);

            if (idValueExpressionList != null && !idValueExpressionList.isEmpty()) {
                Column withColumn = graphqlFieldToColumn(withTable, fieldDefinition.getMapWithToOrError());
                InExpression inExpression = new InExpression().withLeftExpression(withColumn);
                if (fieldDefinition.getMapToOrError().equals(fieldTypeDefinition.asObject().getIDFieldOrError().getName())) {
                    inExpression.setRightExpression(new Parenthesis(new ExpressionList<>(idValueExpressionList)));
                } else {
                    Table table = typeToTable(fieldTypeDefinition.asObject());
                    Column column = graphqlFieldToColumn(table, fieldDefinition.getMapToOrError());
                    Column idColumn = graphqlFieldToColumn(table, fieldTypeDefinition.asObject().getIDFieldOrError().getName());
                    inExpression.setRightExpression(selectFieldByIdExpressionList(table, column, idColumn, idValueExpressionList));
                }
                return removeExpression(withTable, new MultiAndExpression(Arrays.asList(withParentColumnEqualsTo, inExpression)));
            } else {
                return removeExpression(withTable, withParentColumnEqualsTo);
            }
        } else {
            if (documentManager.isMapAnchor(objectType, fieldDefinition)) {
                return updateExpression(
                        parentTable,
                        Collections.singletonList(new UpdateSet(parentColumn, new NullValue())),
                        new EqualsTo()
                                .withLeftExpression(parentIdColumn)
                                .withRightExpression(parentIdExpression)
                );
            } else {
                Table table = typeToTable(fieldTypeDefinition.asObject());
                Column column = graphqlFieldToColumn(table, fieldDefinition.getMapToOrError());
                Column idColumn = graphqlFieldToColumn(table, fieldTypeDefinition.asObject().getIDFieldOrError().getName());
                EqualsTo columnEqualsTo = new EqualsTo()
                        .withLeftExpression(column)
                        .withRightExpression(parentColumnExpression);

                if (idValueExpressionList != null && !idValueExpressionList.isEmpty()) {
                    return updateExpression(
                            table,
                            Collections.singletonList(new UpdateSet(column, new NullValue())),
                            new MultiAndExpression(
                                    Arrays.asList(
                                            columnEqualsTo,
                                            new InExpression()
                                                    .withLeftExpression(idColumn)
                                                    .withNot(notIn)
                                                    .withRightExpression(
                                                            new Parenthesis(
                                                                    new ExpressionList<>(idValueExpressionList)
                                                            )
                                                    )
                                    )
                            )
                    );
                } else {
                    return updateExpression(
                            table,
                            Collections.singletonList(new UpdateSet(column, new NullValue())),
                            columnEqualsTo
                    );
                }
            }
        }
    }

    protected Statement mergeObjectMapStatement(ObjectType objectType,
                                                FieldDefinition fieldDefinition,
                                                Expression parentIdExpression,
                                                Expression idExpression) {
        Table parentTable = typeToTable(objectType);
        Column parentColumn = graphqlFieldToColumn(parentTable, fieldDefinition.getMapFromOrError());
        Column parentIdColumn = graphqlFieldToColumn(parentTable, objectType.getIDFieldOrError().getName());
        Expression parentColumnExpression;
        if (fieldDefinition.getMapFromOrError().equals(objectType.getIDFieldOrError().getName())) {
            parentColumnExpression = parentIdExpression;
        } else {
            parentColumnExpression = selectFieldByIdExpression(parentTable, parentColumn, parentIdColumn, parentIdExpression);
        }

        Definition fieldTypeDefinition = documentManager.getFieldTypeDefinition(fieldDefinition);
        Table table = typeToTable(fieldTypeDefinition.asObject());
        Column column = graphqlFieldToColumn(table, fieldDefinition.getMapToOrError());
        Column idColumn = graphqlFieldToColumn(table, fieldTypeDefinition.asObject().getIDFieldOrError().getName());
        Expression columnExpression;
        if (column.getColumnName().equals(idColumn.getColumnName())) {
            columnExpression = idExpression;
        } else {
            columnExpression = selectFieldByIdExpression(table, column, idColumn, idExpression);
        }

        if (fieldDefinition.hasMapWith()) {
            String mapWithType = fieldDefinition.getMapWithTypeOrError();
            Table withTable = graphqlTypeToTable(mapWithType);
            Column withParentColumn = graphqlFieldToColumn(withTable, fieldDefinition.getMapWithFromOrError());
            Column withColumn = graphqlFieldToColumn(withTable, fieldDefinition.getMapWithToOrError());
            Column isDeprecatedColumn = graphqlFieldToColumn(withTable, FIELD_DEPRECATED_NAME);
            return insertExpression(withTable, Arrays.asList(withParentColumn, withColumn, isDeprecatedColumn), Arrays.asList(parentColumnExpression, columnExpression, new LongValue(0)));
        } else {
            if (documentManager.isMapAnchor(objectType, fieldDefinition)) {
                EqualsTo parentIdEqualsTo = new EqualsTo()
                        .withLeftExpression(parentIdColumn)
                        .withRightExpression(parentIdExpression);
                return updateExpression(parentTable, Collections.singletonList(new UpdateSet(parentColumn, columnExpression)), parentIdEqualsTo);
            } else {
                EqualsTo idEqualsTo = new EqualsTo()
                        .withLeftExpression(idColumn)
                        .withRightExpression(idExpression);
                return updateExpression(table, Collections.singletonList(new UpdateSet(column, parentColumnExpression)), idEqualsTo);
            }
        }
    }

    protected Statement mergeLeafMapStatement(ObjectType objectType,
                                              FieldDefinition fieldDefinition,
                                              ValueWithVariable valueWithVariable,
                                              Expression parentIdExpression) {

        Table parentTable = typeToTable(objectType);
        Column parentColumn = graphqlFieldToColumn(parentTable, fieldDefinition.getMapFromOrError());
        Column parentIdColumn = graphqlFieldToColumn(parentTable, objectType.getIDFieldOrError().getName());
        Expression parentColumnExpression;
        if (fieldDefinition.getMapFromOrError().equals(objectType.getIDFieldOrError().getName())) {
            parentColumnExpression = parentIdExpression;
        } else {
            parentColumnExpression = selectFieldByIdExpression(parentTable, parentColumn, parentIdColumn, parentIdExpression);
        }

        String mapWithType = fieldDefinition.getMapWithTypeOrError();
        Table withTable = graphqlTypeToTable(mapWithType);
        Column withParentColumn = graphqlFieldToColumn(withTable, fieldDefinition.getMapWithFromOrError());
        Column withColumn = graphqlFieldToColumn(withTable, fieldDefinition.getMapWithToOrError());
        Column isDeprecatedColumn = graphqlFieldToColumn(withTable, FIELD_DEPRECATED_NAME);
        if (valueWithVariable.isVariable()) {
            return insertSelectExpression(withTable, Arrays.asList(withParentColumn, withColumn, isDeprecatedColumn), selectVariablesFromJsonArray(parentColumnExpression, fieldDefinition, valueWithVariable));
        } else {
            return insertExpression(withTable, Arrays.asList(withParentColumn, withColumn, isDeprecatedColumn), Arrays.asList(parentColumnExpression, leafValueToDBValue(valueWithVariable), new LongValue(0)));
        }
    }

    protected Insert insertExpression(Table table, List<Column> columnList, List<Expression> expressionList) {
        return insertExpression(table, columnList, expressionList, false);
    }

    protected Insert insertExpression(Table table, List<Column> columnList, List<Expression> expressionList, boolean useDuplicate) {
        Insert insert = new Insert()
                .withTable(table)
                .addColumns(columnList)
                .withSelect(
                        new Values()
                                .addExpressions(expressionList)
                );
        if (useDuplicate && !columnList.isEmpty()) {
            insert.withDuplicateUpdateSets(
                    columnList.stream()
                            .map(column -> new UpdateSet(column, new Values().addExpressions(column)))
                            .collect(Collectors.toList())
            );
        }
        return insert;
    }

    protected Insert insertSelectExpression(Table table, List<Column> columnList, Select select) {
        Insert insert = new Insert()
                .withTable(table)
                .addColumns(columnList)
                .withSelect(select);
        if (!columnList.isEmpty()) {
            insert.withDuplicateUpdateSets(
                    columnList.stream()
                            .map(column -> new UpdateSet(column, new Values().addExpressions(column)))
                            .collect(Collectors.toList())
            );
        }
        return insert;
    }

    protected Update updateExpression(Table table, List<UpdateSet> updateSetList, Expression where) {
        return new Update()
                .withTable(table)
                .withUpdateSets(updateSetList)
                .withWhere(where);
    }

    protected Update removeExpression(Table table, Expression whereExpression) {
        return new Update()
                .withTable(table)
                .addUpdateSet(new UpdateSet(graphqlFieldToColumn(table, FIELD_DEPRECATED_NAME), new LongValue(1)))
                .withWhere(whereExpression);
    }

    protected ParenthesedSelect selectFieldByIdExpression(Table table, Column selectColumn, Column idColumn, Expression idFieldValueExpression) {
        return new ParenthesedSelect()
                .withSelect(
                        new PlainSelect()
                                .addSelectItem(selectColumn)
                                .withFromItem(table)
                                .withWhere(
                                        new EqualsTo()
                                                .withLeftExpression(idColumn)
                                                .withRightExpression(idFieldValueExpression)
                                )
                );
    }

    protected ParenthesedSelect selectFieldByIdExpressionList(Table table, Column selectColumn, Column idColumn, List<Expression> idFieldValueExpressionList) {
        return new ParenthesedSelect()
                .withSelect(
                        new PlainSelect()
                                .addSelectItem(selectColumn)
                                .withFromItem(table)
                                .withWhere(
                                        new InExpression()
                                                .withLeftExpression(idColumn)
                                                .withRightExpression(
                                                        new Parenthesis(
                                                                new ExpressionList<>(idFieldValueExpressionList)
                                                        )
                                                )
                                )
                );
    }

    protected Select selectVariablesFromJsonObjectArray(List<FieldDefinition> fieldDefinitionList, InputValue inputValue, ValueWithVariable valueWithVariable) {
        return new PlainSelect()
                .addSelectItems(new AllColumns())
                .withFromItem(
                        new JsonTableFunction()
                                .withJson(
                                        new JdbcNamedParameter().withName(valueWithVariable.asVariable().getName())
                                )
                                .withPath(new StringValue("$[*]"))
                                .withColumnDefinitions(
                                        fieldDefinitionList.stream()
                                                .map(subField ->
                                                        new ColumnDefinition()
                                                                .withColumnName(graphqlFieldNameToColumnName(subField.getName()))
                                                                .withColDataType(typeTranslator.createColDataType(subField, true))
                                                                .addColumnSpecs("PATH", "'$." + subField.getName() + "'")
                                                )
                                                .collect(Collectors.toList())
                                )
                                .withAlias(new Alias(nameToDBEscape(inputValue.getName())))
                );
    }

    protected Select selectVariablesFromJsonObject(List<FieldDefinition> fieldDefinitionList, InputValue inputValue, ValueWithVariable valueWithVariable) {
        return new PlainSelect()
                .addSelectItems(new AllColumns())
                .withFromItem(
                        new JsonTableFunction()
                                .withJson(
                                        new Function()
                                                .withName("CONCAT")
                                                .withParameters(
                                                        new StringValue("["),
                                                        new JdbcNamedParameter().withName(valueWithVariable.asVariable().getName()),
                                                        new StringValue("]")
                                                )
                                )
                                .withPath(new StringValue("$[*]"))
                                .withColumnDefinitions(
                                        fieldDefinitionList.stream()
                                                .map(subField ->
                                                        new ColumnDefinition()
                                                                .withColumnName(graphqlFieldNameToColumnName(subField.getName()))
                                                                .withColDataType(typeTranslator.createColDataType(subField, true))
                                                                .addColumnSpecs("PATH", "'$." + subField.getName() + "'")
                                                )
                                                .collect(Collectors.toList())
                                )
                                .withAlias(new Alias(nameToDBEscape(inputValue.getName())))
                );
    }

    protected Select selectVariablesFromJsonArray(Expression parentColumnExpression, FieldDefinition fieldDefinition, ValueWithVariable valueWithVariable) {
        return new PlainSelect()
                .addSelectItems(parentColumnExpression, new Column(fieldDefinition.getName()), new LongValue(0))
                .withFromItem(
                        new JsonTableFunction()
                                .withJson(
                                        new JdbcNamedParameter().withName(valueWithVariable.asVariable().getName())
                                )
                                .withPath(new StringValue("$[*]"))
                                .withColumnDefinitions(
                                        new ColumnDefinition()
                                                .withColumnName(fieldDefinition.getName())
                                                .withColDataType(typeTranslator.createColDataType(fieldDefinition, true))
                                                .addColumnSpecs("PATH", "'$'")
                                )
                                .withAlias(new Alias(nameToDBEscape(valueWithVariable.asVariable().getName())))
                );
    }

    protected Table typeToTable(ObjectType objectType) {
        return graphqlTypeToTable(objectType.getName());
    }

    protected Table typeToTable(ObjectType objectType, int level) {
        return graphqlTypeToTable(objectType.getName(), level);
    }
}
