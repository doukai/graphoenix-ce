package io.graphoenix.sql.translator;

import io.graphoenix.core.handler.DocumentManager;
import io.graphoenix.core.handler.PackageManager;
import io.graphoenix.spi.graphql.Definition;
import io.graphoenix.spi.graphql.common.ValueWithVariable;
import io.graphoenix.spi.graphql.operation.Field;
import io.graphoenix.spi.graphql.operation.Operation;
import io.graphoenix.spi.graphql.type.FieldDefinition;
import io.graphoenix.spi.graphql.type.InputValue;
import io.graphoenix.spi.graphql.type.ObjectType;
import io.graphoenix.sql.utils.DBValueUtil;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.LongValue;
import net.sf.jsqlparser.expression.NullValue;
import net.sf.jsqlparser.expression.operators.relational.EqualsTo;
import net.sf.jsqlparser.expression.operators.relational.ExpressionList;
import net.sf.jsqlparser.expression.operators.relational.InExpression;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.schema.Table;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.insert.Insert;
import net.sf.jsqlparser.statement.select.ParenthesedSelect;
import net.sf.jsqlparser.statement.select.PlainSelect;
import net.sf.jsqlparser.statement.select.Values;
import net.sf.jsqlparser.statement.update.Update;
import net.sf.jsqlparser.statement.update.UpdateSet;
import net.sf.jsqlparser.util.cnfexpression.MultiAndExpression;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static io.graphoenix.spi.constant.Hammurabi.*;
import static io.graphoenix.sql.utils.DBNameUtil.graphqlFieldToColumn;
import static io.graphoenix.sql.utils.DBNameUtil.graphqlTypeToTable;
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

    public String operationToStatementSQL(Operation operation) {
        return operationToStatementStream(operation).map(Object::toString).collect(Collectors.joining(";"));
    }

    public Stream<Statement> operationToStatementStream(Operation operation) {
        ObjectType operationType = documentManager.getOperationTypeOrError(operation);


    }

    public Stream<Statement> fieldToToStatementStream(ObjectType objectType, FieldDefinition fieldDefinition, Field field, int level) {
        Definition fieldTypeDefinition = documentManager.getFieldTypeDefinition(fieldDefinition);
        if (fieldTypeDefinition.isObject()) {
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

            Optional<Map.Entry<InputValue, ValueWithVariable>> whereInputValueEntry = inputValueValueWithVariableMap.entrySet().stream()
                    .filter(entry -> entry.getKey().getName().equals(INPUT_VALUE_WHERE_NAME))
                    .filter(entry -> entry.getValue().isObject())
                    .findFirst();

            Map<String, ValueWithVariable> leafValueWithVariableMap = fieldTypeDefinition.asObject().getFields().stream()
                    .filter(subField -> !subField.getType().hasList())
                    .filter(subField -> !documentManager.getFieldTypeDefinition(subField).isObject())
                    .flatMap(subField ->
                            inputValueValueWithVariableMap.entrySet().stream()
                                    .filter(entry -> entry.getKey().getName().equals(subField.getName()))
                    )
                    .map(entry -> new AbstractMap.SimpleEntry<>(entry.getKey().getName(), entry.getValue()))
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

            Table table = typeToTable(fieldTypeDefinition.asObject(), 0);

            Statement leafFieldStatement = whereInputValueEntry
                    .flatMap(entry -> argumentsTranslator.inputValueToWhereExpression(objectType, fieldDefinition, entry.getKey(), entry.getValue(), 0))
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

            Expression idExpression = inputValueValueWithVariableMap.entrySet().stream()
                    .filter(entry -> entry.getKey().getName().equals(idName))
                    .findFirst()
                    .map(Map.Entry::getValue)
                    .flatMap(DBValueUtil::idValueToDBValue)
                    .or(() ->
                            whereInputValueEntry
                                    .flatMap(entry -> entry.getValue().asObject().getValueWithVariable(idName))
                                    .flatMap(DBValueUtil::idValueToDBValue)
                    )
                    .orElseGet(() -> createInsertIdUserVariable(fieldTypeDefinition.asObject().getName(), idName, 0, 0));


            fieldTypeDefinition.asObject().getFields().stream()
                    .flatMap(subField ->
                            inputValueValueWithVariableMap.entrySet().stream()
                                    .filter(entry -> entry.getKey().getName().equals(subField.getName()))
                                    .flatMap(entry -> inputValueToWhereExpression(fieldTypeDefinition.asObject(), idExpression, subField, entry.getKey(), entry.getValue(), level + 1).stream())
                    )


        }


    }

    public Stream<Statement> inputValueToWhereExpression(ObjectType objectType,
                                                         Expression parentIdExpression,
                                                         FieldDefinition fieldDefinition,
                                                         InputValue inputValue,
                                                         ValueWithVariable valueWithVariable,
                                                         int level,
                                                         int index) {
        Definition fieldTypeDefinition = documentManager.getFieldTypeDefinition(fieldDefinition);
        if (fieldTypeDefinition.isObject()) {
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

            Optional<Map.Entry<InputValue, ValueWithVariable>> whereInputValueEntry = inputValueValueWithVariableMap.entrySet().stream()
                    .filter(entry -> entry.getKey().getName().equals(INPUT_VALUE_WHERE_NAME))
                    .filter(entry -> entry.getValue().isObject())
                    .findFirst();

            Statement removeMapStatement = removeMapStatement(
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
            );

            Map<String, ValueWithVariable> leafValueWithVariableMap = fieldTypeDefinition.asObject().getFields().stream()
                    .filter(subField -> !subField.getType().hasList())
                    .filter(subField -> !documentManager.getFieldTypeDefinition(subField).isObject())
                    .flatMap(subField ->
                            inputValueValueWithVariableMap.entrySet().stream()
                                    .filter(entry -> entry.getKey().getName().equals(subField.getName()))
                    )
                    .map(entry -> new AbstractMap.SimpleEntry<>(entry.getKey().getName(), entry.getValue()))
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

            Table table = typeToTable(fieldTypeDefinition.asObject(), level);

            Statement leafFieldStatement = whereInputValueEntry
                    .flatMap(entry -> argumentsTranslator.inputValueToWhereExpression(objectType, fieldDefinition, entry.getKey(), entry.getValue(), 0))
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

            Expression idExpression = inputValueValueWithVariableMap.entrySet().stream()
                    .filter(entry -> entry.getKey().getName().equals(idName))
                    .findFirst()
                    .map(Map.Entry::getValue)
                    .flatMap(DBValueUtil::idValueToDBValue)
                    .or(() ->
                            whereInputValueEntry
                                    .flatMap(entry -> entry.getValue().asObject().getValueWithVariable(idName))
                                    .flatMap(DBValueUtil::idValueToDBValue)
                    )
                    .orElseGet(() -> createInsertIdUserVariable(fieldTypeDefinition.asObject().getName(), idName, level, index));


            Stream<Statement> objectFieldStatementStream = fieldTypeDefinition.asObject().getFields().stream()
                    .filter(subField -> !subField.isFetchField())
                    .filter(subField -> !subField.getType().hasList())
                    .filter(subField -> documentManager.getFieldTypeDefinition(subField).isObject())
                    .flatMap(subField ->
                            inputValueValueWithVariableMap.entrySet().stream()
                                    .filter(entry -> entry.getKey().getName().equals(subField.getName()))
                                    .flatMap(entry ->
                                            inputValueToWhereExpression(
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

            Statement mergeMapStatement = mergeMapStatement(
                    objectType,
                    fieldDefinition,
                    parentIdExpression,
                    idExpression,
                    inputValueValueWithVariableMap.entrySet().stream()
                            .filter(entry -> entry.getKey().getName().equals(fieldDefinition.getMapFromOrError()))
                            .findFirst()
                            .map(Map.Entry::getValue)
                            .map(DBValueUtil::leafValueToDBValue)
                            .orElse(null),
                    inputValueValueWithVariableMap.entrySet().stream()
                            .filter(entry -> entry.getKey().getName().equals(fieldDefinition.getMapToOrError()))
                            .findFirst()
                            .map(Map.Entry::getValue)
                            .map(DBValueUtil::leafValueToDBValue)
                            .orElse(null)
            );

            Stream<Statement> listObjectFieldStatementStream = fieldTypeDefinition.asObject().getFields().stream()
                    .filter(subField -> !subField.isFetchField())
                    .filter(subField -> !subField.getType().hasList())
                    .filter(subField -> documentManager.getFieldTypeDefinition(subField).isObject())

        }


    }

    public Stream<Statement> arrayInputValueToWhereExpression(ObjectType objectType,
                                                              Expression parentIdExpression,
                                                              FieldDefinition fieldDefinition,
                                                              InputValue inputValue,
                                                              ValueWithVariable valueWithVariable,
                                                              Expression fromValueExpression,
                                                              int level) {

        Statement removeMapStatement = removeMapStatement(
                objectType,
                fieldDefinition,
                parentIdExpression,
                fromValueExpression,
                null
        );

        if (valueWithVariable == null || !valueWithVariable.isArray()) {
            return Stream.of(removeMapStatement);
        }

        IntStream.range(0, valueWithVariable.asArray().size())
                .mapToObj(index ->
                        inputValueToWhereExpression(
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

        removeMapStatement(
                objectType,
                fieldDefinition,
                parentIdExpression,
                fromValueExpression,
                idValueExpressionList
        );

    }

    public Statement removeMapStatement(ObjectType objectType,
                                        FieldDefinition fieldDefinition,
                                        Expression parentIdExpression,
                                        Expression fromValueExpression,
                                        List<Expression> idValueExpressionList) {
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
            return removeExpression(withTable, withParentColumnEqualsTo);
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
                                                    .withNot(true)
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

    public Statement mergeMapStatement(ObjectType objectType,
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

    protected Insert insertExpression(Table table,
                                      List<Column> columnList,
                                      List<Expression> expressionList) {
        return insertExpression(table, columnList, expressionList, false);
    }

    protected Insert insertExpression(Table table,
                                      List<Column> columnList,
                                      List<Expression> expressionList,
                                      boolean useDuplicate) {
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
                            .map(column -> new UpdateSet(column, new Values(new ExpressionList<>(column))))
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

    protected Table typeToTable(ObjectType objectType) {
        return graphqlTypeToTable(objectType.getName());
    }

    protected Table typeToTable(ObjectType objectType, int level) {
        return graphqlTypeToTable(objectType.getName(), level);
    }
}
