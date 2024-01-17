package io.graphoenix.sql.translator;

import graphql.parser.antlr.GraphqlParser;
import io.graphoenix.core.handler.DocumentManager;
import io.graphoenix.core.handler.PackageManager;
import io.graphoenix.spi.graphql.Definition;
import io.graphoenix.spi.graphql.common.ObjectValueWithVariable;
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
import net.sf.jsqlparser.expression.UserVariable;
import net.sf.jsqlparser.expression.operators.relational.EqualsTo;
import net.sf.jsqlparser.expression.operators.relational.ExpressionList;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.schema.Table;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.insert.Insert;
import net.sf.jsqlparser.statement.select.ParenthesedSelect;
import net.sf.jsqlparser.statement.select.PlainSelect;
import net.sf.jsqlparser.statement.select.Values;
import net.sf.jsqlparser.statement.update.Update;
import net.sf.jsqlparser.statement.update.UpdateSet;

import java.util.*;
import java.util.stream.Collectors;
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

    public Stream<Statement> inputValueToWhereExpression(ObjectType objectType, Expression parentIdExpression, FieldDefinition fieldDefinition, InputValue inputValue, ValueWithVariable valueWithVariable, int level) {
        Definition fieldTypeDefinition = documentManager.getFieldTypeDefinition(fieldDefinition);
        Definition inputValueTypeDefinition = documentManager.getInputValueTypeDefinition(inputValue);
        if (fieldTypeDefinition.isObject()) {
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
        }

    }

    public Statement removeWithTypeStatement(ObjectType objectType, Expression parentIdExpression, FieldDefinition fieldDefinition, InputValue inputValue, ValueWithVariable valueWithVariable, int level) {
        ObjectType fieldTypeDefinition = documentManager.getFieldTypeDefinition(fieldDefinition).asObject();
        Table parentTable = typeToTable(objectType, level - 1);
        Table table = typeToTable(fieldTypeDefinition, level);
        if (fieldDefinition.hasMapWith()) {
            String mapWithType = fieldDefinition.getMapWithTypeOrError();
            Table withTable = graphqlTypeToTable(mapWithType, level);
            Expression parentColumnExpression;
            if (fieldDefinition.getMapFromOrError().equals(objectType.getIDFieldOrError().getName())) {
                parentColumnExpression = parentIdExpression;
            } else {
                Column parentColumn = graphqlFieldToColumn(parentTable, fieldDefinition.getMapFromOrError());
                Column parentIdColumn = graphqlFieldToColumn(parentTable, objectType.getIDFieldOrError().getName());
                parentColumnExpression = selectFieldByIdExpression(parentTable, parentColumn, parentIdColumn, parentIdExpression);
            }


        }

    }

    protected Insert insertExpression(Table table,
                                      List<Column> columnList,
                                      ExpressionList<Expression> expressionList) {
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
