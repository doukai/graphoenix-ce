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
import net.sf.jsqlparser.statement.create.table.ColDataType;
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

    @Inject
    public MutationTranslator(DocumentManager documentManager, PackageManager packageManager, ArgumentsTranslator argumentsTranslator) {
        this.documentManager = documentManager;
        this.packageManager = packageManager;
        this.argumentsTranslator = argumentsTranslator;
    }

    public Stream<String> operationToStatementSQLStream(Operation operation) {
        return operationToStatementStream(operation).map(Object::toString);
    }

    public Stream<Statement> operationToStatementStream(Operation operation) {
        ObjectType operationType = documentManager.getOperationTypeOrError(operation);
        return operation.getFields().stream()
                .filter(field -> {
                            FieldDefinition fieldDefinition = operationType.getField(field.getName());
                            return packageManager.isLocalPackage(fieldDefinition) &&
                                    !fieldDefinition.isFetchField() &&
                                    !fieldDefinition.isInvokeField() &&
                                    !fieldDefinition.isFunctionField() &&
                                    !fieldDefinition.isConnectionField();
                        }
                )
                .flatMap(field -> fieldToMutationStatementStream(operationType, operationType.getField(field.getName()), field));
    }

    protected Stream<Statement> fieldToMutationStatementStream(ObjectType objectType, FieldDefinition fieldDefinition, Field field) {
        Definition fieldTypeDefinition = documentManager.getFieldTypeDefinition(fieldDefinition);
        if (fieldTypeDefinition.isObject()) {
            return fieldDefinition.getArgument(INPUT_VALUE_LIST_NAME)
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
                                        .flatMap(arguments -> arguments.getArgument(inputValue.getName()))
                                        .filter(valueWithVariable -> !valueWithVariable.isNull())
                                        .map(valueWithVariable -> {
                                                    if (valueWithVariable.isVariable()) {
                                                        return Stream.of(
                                                                (Statement) insertSelectExpression(
                                                                        table,
                                                                        leafFieldDefinitionList.stream()
                                                                                .map(subField -> graphqlFieldToColumn(table, subField.getName()))
                                                                                .collect(Collectors.toList()),
                                                                        selectVariablesFromJsonObjectArray(leafFieldDefinitionList, inputValue)
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
                    .orElseGet(() -> {
                                Map<InputValue, ValueWithVariable> inputValueValueWithVariableMap = Stream.ofNullable(fieldDefinition.getArguments())
                                        .flatMap(Collection::stream)
                                        .flatMap(argumentInput ->
                                                Optional.ofNullable(field.getArguments())
                                                        .flatMap(arguments -> arguments.getArgument(argumentInput.getName()))
                                                        .or(() -> Optional.ofNullable(argumentInput.getDefaultValue()))
                                                        .stream()
                                                        .map(valueWithVariable -> new AbstractMap.SimpleEntry<>(argumentInput, valueWithVariable))
                                        )
                                        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
                                return inputValueMapToMutationStatementStream(objectType, fieldDefinition, inputValueValueWithVariableMap);
                            }
                    );
        }
        throw new GraphQLErrors(UNSUPPORTED_FIELD_TYPE.bind(field.toString()));
    }

    protected Stream<Statement> objectToMutationStatementStream(ObjectType objectType, FieldDefinition fieldDefinition, InputValue inputValue, ValueWithVariable valueWithVariable) {
        return objectToMutationStatementStream(objectType, null, fieldDefinition, inputValue, valueWithVariable, 0, 0);
    }

    protected Stream<Statement> objectToMutationStatementStream(ObjectType objectType,
                                                                Expression parentIdExpression,
                                                                FieldDefinition fieldDefinition,
                                                                InputValue inputValue,
                                                                ValueWithVariable valueWithVariable,
                                                                int level,
                                                                int index) {

        Definition inputValueTypeDefinition = documentManager.getInputValueTypeDefinition(inputValue);
        Map<InputValue, ValueWithVariable> inputValueValueWithVariableMap = Stream.ofNullable(inputValueTypeDefinition.asInputObject().getInputValues())
                .flatMap(Collection::stream)
                .flatMap(fieldInput ->
                        Optional.ofNullable(valueWithVariable)
                                .filter(ValueWithVariable::isObject)
                                .map(ValueWithVariable::asObject)
                                .flatMap(objectValueWithVariable -> objectValueWithVariable.getValueWithVariable(fieldInput.getName()))
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
                                inputValueValueWithVariableMap.entrySet().stream()
                                        .filter(entry -> entry.getKey().getName().equals(fieldDefinition.getMapFromOrError()))
                                        .findFirst()
                                        .map(Map.Entry::getValue)
                                        .map(DBValueUtil::leafValueToDBValue)
                                        .orElse(null),
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
            return inputValueMapToMutationStatementStream(objectType, parentIdExpression, fieldDefinition, inputValueValueWithVariableMap, level, index);
        }
    }

    protected Stream<Statement> inputValueMapToMutationStatementStream(ObjectType objectType,
                                                                       FieldDefinition fieldDefinition,
                                                                       Map<InputValue, ValueWithVariable> inputValueValueWithVariableMap) {
        return inputValueMapToMutationStatementStream(objectType, null, fieldDefinition, inputValueValueWithVariableMap, 0, 0);

    }

    protected Stream<Statement> inputValueMapToMutationStatementStream(ObjectType objectType,
                                                                       Expression parentIdExpression,
                                                                       FieldDefinition fieldDefinition,
                                                                       Map<InputValue, ValueWithVariable> inputValueValueWithVariableMap,
                                                                       int level,
                                                                       int index) {
        Definition fieldTypeDefinition = documentManager.getFieldTypeDefinition(fieldDefinition);
        Table table = typeToTable(fieldTypeDefinition.asObject());

        List<FieldDefinition> leafFieldDefinitionList = fieldTypeDefinition.asObject().getFields().stream()
                .filter(subField -> !subField.isInvokeField())
                .filter(subField -> !subField.isFetchField())
                .filter(subField -> !subField.isFunctionField())
                .filter(subField -> !subField.getType().hasList())
                .filter(subField -> !documentManager.getFieldTypeDefinition(subField).isObject())
                .collect(Collectors.toList());

        Optional<Map.Entry<InputValue, ValueWithVariable>> whereInputValueEntry = inputValueValueWithVariableMap.entrySet().stream()
                .filter(entry -> entry.getKey().getName().equals(INPUT_VALUE_WHERE_NAME))
                .filter(entry -> entry.getValue().isObject())
                .findFirst();

        Map<String, ValueWithVariable> leafValueWithVariableMap = leafFieldDefinitionList.stream()
                .flatMap(subField ->
                        inputValueValueWithVariableMap.entrySet().stream()
                                .filter(entry -> entry.getKey().getName().equals(subField.getName()))
                )
                .map(entry -> new AbstractMap.SimpleEntry<>(entry.getKey().getName(), entry.getValue()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        Statement leafFieldMutationStatement = whereInputValueEntry
                .flatMap(entry -> argumentsTranslator.inputValueToWhereExpression(objectType, fieldDefinition, entry.getKey(), entry.getValue(), level))
                .map(whereExpression ->
                        (Statement) updateExpression(
                                table,
                                leafValueWithVariableMap.entrySet().stream()
                                        .map(entry ->
                                                new UpdateSet(
                                                        graphqlFieldToColumn(table, entry.getKey()),
                                                        leafValueToDBValue(entry.getValue()))
                                        )
                                        .collect(Collectors.toList()),
                                whereExpression
                        )
                )
                .orElseGet(() ->
                        insertExpression(
                                table,
                                leafValueWithVariableMap.keySet().stream()
                                        .map(name -> graphqlFieldToColumn(table, name))
                                        .collect(Collectors.toList()),
                                leafValueWithVariableMap.values().stream()
                                        .map(DBValueUtil::leafValueToDBValue)
                                        .collect(Collectors.toList()),
                                true
                        )
                );

        String idName = fieldTypeDefinition.asObject().getIDFieldOrError().getName();
        Stream<Statement> createInsertIdSetStatementStream = Stream.empty();
        if (inputValueValueWithVariableMap.entrySet().stream()
                .noneMatch(entry -> entry.getKey().getName().equals(idName))) {
            createInsertIdSetStatementStream = Stream.of(createInsertIdSetStatement(fieldTypeDefinition.asObject().getName(), idName, level, index));
        }

        Expression idExpression = inputValueValueWithVariableMap.entrySet().stream()
                .filter(entry -> entry.getKey().getName().equals(idName))
                .findFirst()
                .map(Map.Entry::getValue)
                .flatMap(DBValueUtil::idValueToDBValue)
                .or(() ->
                        whereInputValueEntry
                                .flatMap(entry -> entry.getValue().asObject().getValueWithVariable(idName))
                                .filter(ValueWithVariable::isObject)
                                .flatMap(valueWithVariable -> valueWithVariable.asObject().getValueWithVariable(INPUT_OPERATOR_INPUT_VALUE_VAL_NAME))
                                .flatMap(DBValueUtil::idValueToDBValue)
                )
                .orElseGet(() -> createInsertIdUserVariable(fieldTypeDefinition.asObject().getName(), idName, level, index));

        Optional<ValueWithVariable> fromValueWithVariable = fieldDefinition.getMapFrom()
                .flatMap(from ->
                        inputValueValueWithVariableMap.entrySet().stream()
                                .filter(entry -> entry.getKey().getName().equals(from))
                                .findFirst()
                )
                .map(Map.Entry::getValue);

        Optional<ValueWithVariable> toValueWithVariable = fieldDefinition.getMapTo()
                .flatMap(to ->
                        inputValueValueWithVariableMap.entrySet().stream()
                                .filter(entry -> entry.getKey().getName().equals(to))
                                .findFirst()
                )
                .map(Map.Entry::getValue);

        Stream<Statement> objectFieldMergeMapStatementStream = Stream.empty();
        if (parentIdExpression != null) {
            objectFieldMergeMapStatementStream = Stream.of(
                    mergeObjectMapStatement(
                            objectType,
                            fieldDefinition,
                            parentIdExpression,
                            idExpression,
                            fromValueWithVariable
                                    .map(DBValueUtil::leafValueToDBValue)
                                    .orElse(null),
                            toValueWithVariable
                                    .map(DBValueUtil::leafValueToDBValue)
                                    .orElse(null)
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
                                                whereInputValueEntry.isPresent(),
                                                fromValueWithVariable
                                                        .map(DBValueUtil::leafValueToDBValue)
                                                        .orElse(null),
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
                                                entry.getValue(),
                                                fromValueWithVariable
                                                        .map(DBValueUtil::leafValueToDBValue)
                                                        .orElse(null)
                                        )
                                )
                );

        return Streams.concat(
                Stream.of(leafFieldMutationStatement),
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
                                                                    Expression fromValueExpression,
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
                            selectVariablesFromJsonObjectArray(leafFieldDefinitionList, inputValue)
                    )
            );
        } else if (valueWithVariable.isArray()) {
            if (merge) {
                List<ValueWithVariable> mergeValueWithVariableList = valueWithVariable.asArray().getValueWithVariables().stream()
                        .filter(ValueWithVariable::isObject)
                        .map(ValueWithVariable::asObject)
                        .filter(objectValueWithVariable -> !objectValueWithVariable.containsKey(FIELD_DEPRECATED_NAME))
                        .filter(objectValueWithVariable ->
                                objectValueWithVariable.getValueWithVariable(FIELD_DEPRECATED_NAME)
                                        .map(isDeprecated -> isDeprecated.asBoolean().getValue())
                                        .orElse(false)
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
                                objectValueWithVariable.getValueWithVariable(idName)
                                        .or(() ->
                                                objectValueWithVariable.getValueWithVariable(INPUT_VALUE_WHERE_NAME)
                                                        .filter(ValueWithVariable::isObject)
                                                        .map(ValueWithVariable::asObject)
                                                        .flatMap(where -> where.getValueWithVariable(idName))
                                                        .filter(ValueWithVariable::isObject)
                                                        .map(ValueWithVariable::asObject)
                                                        .flatMap(id -> id.getValueWithVariable(INPUT_OPERATOR_INPUT_VALUE_VAL_NAME))
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
                                    fromValueExpression,
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
                        .filter(objectValueWithVariable -> objectValueWithVariable.containsKey(FIELD_DEPRECATED_NAME))
                        .filter(objectValueWithVariable ->
                                objectValueWithVariable.getValueWithVariable(FIELD_DEPRECATED_NAME)
                                        .map(isDeprecated -> isDeprecated.asBoolean().getValue())
                                        .orElse(false)
                        )
                        .flatMap(objectValueWithVariable ->
                                objectValueWithVariable.getValueWithVariable(idName)
                                        .or(() ->
                                                objectValueWithVariable.getValueWithVariable(INPUT_VALUE_WHERE_NAME)
                                                        .filter(ValueWithVariable::isObject)
                                                        .map(ValueWithVariable::asObject)
                                                        .flatMap(where -> where.getValueWithVariable(idName))
                                                        .filter(ValueWithVariable::isObject)
                                                        .map(ValueWithVariable::asObject)
                                                        .flatMap(id -> id.getValueWithVariable(INPUT_OPERATOR_INPUT_VALUE_VAL_NAME))
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
                                    fromValueExpression,
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
                        fromValueExpression,
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
                                valueWithVariable.asArray().getValueWithVariables().get(index).asObject().getValueWithVariable(idFieldName)
                                        .flatMap(DBValueUtil::idValueToDBValue)
                                        .orElseGet(() -> createInsertIdUserVariable(fieldTypeDefinition.asObject().getName(), idFieldName, level, index))
                        )
                        .collect(Collectors.toList());

                Statement removeMapStatement = removeMapStatement(
                        objectType,
                        fieldDefinition,
                        parentIdExpression,
                        fromValueExpression,
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
                                                          ValueWithVariable valueWithVariable,
                                                          Expression fromValueExpression) {

        Statement removeMapStatement = removeMapStatement(
                objectType,
                fieldDefinition,
                parentIdExpression,
                fromValueExpression,
                null
        );

        if (valueWithVariable.isVariable()) {
            return Stream.of(
                    removeMapStatement,
                    mergeLeafMapStatement(
                            objectType,
                            fieldDefinition,
                            valueWithVariable,
                            parentIdExpression,
                            fromValueExpression
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
                                            parentIdExpression,
                                            fromValueExpression
                                    )
                            )
            );
        }
    }

    protected Statement removeMapStatement(ObjectType objectType,
                                           FieldDefinition fieldDefinition,
                                           Expression parentIdExpression,
                                           Expression fromValueExpression,
                                           List<Expression> idValueExpressionList) {
        return removeMapStatement(objectType, fieldDefinition, parentIdExpression, fromValueExpression, idValueExpressionList, true);
    }

    protected Statement removeMapStatement(ObjectType objectType,
                                           FieldDefinition fieldDefinition,
                                           Expression parentIdExpression,
                                           Expression fromValueExpression,
                                           List<Expression> idValueExpressionList,
                                           boolean notIn) {
        Table parentTable = typeToTable(objectType);
        Column parentColumn = graphqlFieldToColumn(parentTable, fieldDefinition.getMapFromOrError());
        Column parentIdColumn = graphqlFieldToColumn(parentTable, objectType.getIDFieldOrError().getName());

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
                    .withLeftExpression(withParentColumn);
            if (fromValueExpression != null) {
                withParentColumnEqualsTo.setRightExpression(fromValueExpression);
            } else {
                withParentColumnEqualsTo.setRightExpression(parentColumnExpression);
            }
            if (idValueExpressionList != null && !idValueExpressionList.isEmpty()) {
                Definition fieldTypeDefinition = documentManager.getFieldTypeDefinition(fieldDefinition);
                Table table = typeToTable(fieldTypeDefinition.asObject());
                Column column = graphqlFieldToColumn(table, fieldDefinition.getMapToOrError());
                Column idColumn = graphqlFieldToColumn(table, fieldTypeDefinition.asObject().getIDFieldOrError().getName());
                Column withColumn = graphqlFieldToColumn(withTable, fieldDefinition.getMapWithToOrError());
                InExpression inExpression = new InExpression().withLeftExpression(withColumn);
                if (column.getColumnName().equals(idColumn.getColumnName())) {
                    inExpression.setRightExpression(new Parenthesis(new ExpressionList<>(idValueExpressionList)));
                } else {
                    inExpression.setRightExpression(selectFieldByIdExpressionList(table, column, idColumn, idValueExpressionList));
                }
                return removeExpression(withTable, new MultiAndExpression(Arrays.asList(withParentColumnEqualsTo, inExpression)));
            } else {
                return removeExpression(withTable, withParentColumnEqualsTo);
            }
        } else {
            Definition fieldTypeDefinition = documentManager.getFieldTypeDefinition(fieldDefinition);
            Table table = typeToTable(fieldTypeDefinition.asObject());
            Column column = graphqlFieldToColumn(table, fieldDefinition.getMapFromOrError());

            EqualsTo parentColumnEqualsTo = new EqualsTo()
                    .withLeftExpression(column);
            if (fromValueExpression != null) {
                parentColumnEqualsTo.setRightExpression(fromValueExpression);
            } else {
                parentColumnEqualsTo.setRightExpression(parentColumnExpression);
            }

            if (fieldDefinition.isMapAnchor()) {
                return updateExpression(
                        parentTable,
                        Collections.singletonList(new UpdateSet(parentColumn, new NullValue())),
                        new EqualsTo()
                                .withLeftExpression(parentIdColumn)
                                .withRightExpression(parentIdExpression)
                );
            } else {
                if (idValueExpressionList != null && !idValueExpressionList.isEmpty()) {
                    Column idColumn = graphqlFieldToColumn(table, fieldTypeDefinition.asObject().getIDFieldOrError().getName());
                    return updateExpression(
                            table,
                            Collections.singletonList(new UpdateSet(column, new NullValue())),
                            new MultiAndExpression(
                                    Arrays.asList(
                                            parentColumnEqualsTo,
                                            new InExpression()
                                                    .withLeftExpression(idColumn)
                                                    .withNot(notIn)
                                                    .withRightExpression(new ExpressionList<>(idValueExpressionList))
                                    )
                            )
                    );
                } else {
                    return updateExpression(
                            table,
                            Collections.singletonList(new UpdateSet(column, new NullValue())),
                            parentColumnEqualsTo
                    );
                }
            }
        }
    }

    protected Statement mergeObjectMapStatement(ObjectType objectType,
                                                FieldDefinition fieldDefinition,
                                                Expression parentIdExpression,
                                                Expression idExpression,
                                                Expression fromValueExpression,
                                                Expression toValueExpression) {
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

            if (fromValueExpression != null && toValueExpression != null) {
                return insertExpression(withTable, Arrays.asList(withParentColumn, withColumn), Arrays.asList(fromValueExpression, toValueExpression));
            } else if (fromValueExpression != null) {
                return insertExpression(withTable, Arrays.asList(withParentColumn, withColumn), Arrays.asList(fromValueExpression, columnExpression));
            } else if (toValueExpression != null) {
                return insertExpression(withTable, Arrays.asList(withParentColumn, withColumn), Arrays.asList(parentColumnExpression, toValueExpression));
            } else {
                return insertExpression(withTable, Arrays.asList(withParentColumn, withColumn), Arrays.asList(parentColumnExpression, columnExpression));
            }
        } else {
            if (fieldDefinition.isMapAnchor()) {
                EqualsTo parentIdEqualsTo = new EqualsTo()
                        .withLeftExpression(parentIdColumn)
                        .withRightExpression(parentIdExpression);
                return updateExpression(parentTable, Collections.singletonList(new UpdateSet(parentColumn, Objects.requireNonNullElse(toValueExpression, columnExpression))), parentIdEqualsTo);
            } else {
                EqualsTo idEqualsTo = new EqualsTo()
                        .withLeftExpression(idColumn)
                        .withRightExpression(idExpression);
                return updateExpression(table, Collections.singletonList(new UpdateSet(column, Objects.requireNonNullElse(fromValueExpression, parentColumnExpression))), idEqualsTo);
            }
        }
    }

    protected Statement mergeLeafMapStatement(ObjectType objectType,
                                              FieldDefinition fieldDefinition,
                                              ValueWithVariable valueWithVariable,
                                              Expression parentIdExpression,
                                              Expression fromValueExpression) {

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

        if (valueWithVariable.isVariable()) {
            return insertSelectExpression(withTable, Arrays.asList(withParentColumn, withColumn), selectVariablesFromJsonArray(parentColumnExpression, fieldDefinition, valueWithVariable));
        } else {
            return insertExpression(withTable, Arrays.asList(withParentColumn, withColumn), Arrays.asList(Objects.requireNonNullElse(fromValueExpression, parentColumnExpression), leafValueToDBValue(valueWithVariable)));
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

    protected Select selectVariablesFromJsonObjectArray(List<FieldDefinition> fieldDefinitionList, InputValue inputValue) {
        return new PlainSelect()
                .addSelectItems(new AllColumns())
                .withFromItem(
                        new JsonTableFunction()
                                .withJson(
                                        new JdbcNamedParameter().withName(inputValue.getName())
                                )
                                .withPath(new StringValue("$[*]"))
                                .withColumnDefinitions(
                                        fieldDefinitionList.stream()
                                                .map(subField ->
                                                        new ColumnDefinition()
                                                                .withColumnName(graphqlFieldNameToColumnName(subField.getName()))
                                                                .withColDataType(buildJsonColumnDataType(documentManager.getInputValueTypeDefinition(inputValue)))
                                                                .addColumnSpecs("PATH", "'$." + subField.getName() + "'")
                                                )
                                                .collect(Collectors.toList())
                                )
                                .withAlias(new Alias(inputValue.getName()))
                );
    }

    protected Select selectVariablesFromJsonArray(Expression parentColumnExpression, FieldDefinition fieldDefinition, ValueWithVariable valueWithVariable) {
        return new PlainSelect()
                .addSelectItems(parentColumnExpression, new Column(fieldDefinition.getName()))
                .withFromItem(
                        new JsonTableFunction()
                                .withJson(
                                        new JdbcNamedParameter().withName(valueWithVariable.asVariable().getName())
                                )
                                .withPath(new StringValue("$[*]"))
                                .withColumnDefinitions(
                                        new ColumnDefinition()
                                                .withColumnName(fieldDefinition.getName())
                                                .withColDataType(buildJsonColumnDataType(documentManager.getFieldTypeDefinition(fieldDefinition)))
                                                .addColumnSpecs("PATH", "'$'")
                                )
                                .withAlias(new Alias(valueWithVariable.asVariable().getName()))
                );
    }

    protected ColDataType buildJsonColumnDataType(Definition definition) {
        ColDataType colDataType = new ColDataType();
        if (definition.isEnum()) {
            colDataType.setDataType("INT");
        } else if (definition.isScalar()) {
            switch (definition.getName()) {
                case SCALA_ID_NAME:
                case SCALA_STRING_NAME:
                    colDataType
                            .withDataType("VARCHAR")
                            .setArgumentsStringList(Collections.singletonList("255"));
                    break;
                case SCALA_BOOLEAN_NAME:
                    colDataType.setDataType("BOOL");
                    break;
                case SCALA_INT_NAME:
                    colDataType.setDataType("INT");
                    break;
                case SCALA_FLOAT_NAME:
                    colDataType.setDataType("FLOAT");
                    break;
                case SCALA_BIG_INTEGER_NAME:
                    colDataType.setDataType("BIGINT");
                    break;
                case SCALA_BIG_DECIMAL_NAME:
                    colDataType.setDataType("DECIMAL");
                    break;
                case SCALA_DATE_NAME:
                    colDataType.setDataType("DATE");
                    break;
                case SCALA_TIME_NAME:
                    colDataType.setDataType("TIME");
                    break;
                case SCALA_DATE_TIME_NAME:
                    colDataType.setDataType("DATETIME");
                    break;
                case SCALA_TIMESTAMP_NAME:
                    colDataType.setDataType("TIMESTAMP");
                    break;
                default:
                    throw new GraphQLErrors(UNSUPPORTED_FIELD_TYPE.bind(definition.getName()));
            }
        } else {
            throw new GraphQLErrors(UNSUPPORTED_FIELD_TYPE.bind(definition.getName()));
        }
        return colDataType;
    }

    protected Table typeToTable(ObjectType objectType) {
        return graphqlTypeToTable(objectType.getName());
    }
}
