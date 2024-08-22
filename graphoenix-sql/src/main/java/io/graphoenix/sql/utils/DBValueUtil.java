package io.graphoenix.sql.utils;

import io.graphoenix.spi.error.GraphQLErrorType;
import io.graphoenix.spi.error.GraphQLErrors;
import io.graphoenix.spi.graphql.common.ValueWithVariable;
import io.graphoenix.spi.graphql.common.Variable;
import io.graphoenix.spi.graphql.type.InputValue;
import io.graphoenix.sql.expression.JsonTableFunction;
import net.sf.jsqlparser.expression.*;
import net.sf.jsqlparser.expression.operators.relational.EqualsTo;
import net.sf.jsqlparser.expression.operators.relational.ExpressionList;
import net.sf.jsqlparser.expression.operators.relational.GreaterThanEquals;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.schema.Table;
import net.sf.jsqlparser.statement.SetStatement;
import net.sf.jsqlparser.statement.create.table.ColDataType;
import net.sf.jsqlparser.statement.create.table.ColumnDefinition;
import net.sf.jsqlparser.statement.select.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static io.graphoenix.sql.utils.DBNameUtil.*;

public final class DBValueUtil {

    public static Expression getValueFromObjectVariable(ValueWithVariable valueWithVariable, InputValue inputValue) {
        return new Function()
                .withName("JSON_EXTRACT")
                .withParameters(
                        new ExpressionList<>(
                                variableToJdbcNamedParameter(valueWithVariable.asVariable()),
                                new StringValue("$." + inputValue.getName())
                        )
                );
    }

    public static Expression getValueFromArrayVariable(ValueWithVariable valueWithVariable, int index) {
        return new Function()
                .withName("JSON_EXTRACT")
                .withParameters(
                        new ExpressionList<>(
                                variableToJdbcNamedParameter(valueWithVariable.asVariable()),
                                new StringValue("$[" + index + "]")
                        )
                );
    }

    public static Expression leafValueToDBValue(ValueWithVariable valueWithVariable) {
        if (valueWithVariable.isString()) {
            return new StringValue(valueWithVariable.asString().getValue());
        } else if (valueWithVariable.isInt()) {
            return new LongValue(valueWithVariable.toString());
        } else if (valueWithVariable.isFloat()) {
            return new DoubleValue(valueWithVariable.toString());
        } else if (valueWithVariable.isBoolean()) {
            return valueWithVariable.asBoolean().getValue() ? new LongValue(1) : new LongValue(0);
        } else if (valueWithVariable.isEnum()) {
            return new StringValue(valueWithVariable.asEnum().getValue());
        } else if (valueWithVariable.isNull()) {
            return new NullValue();
        } else if (valueWithVariable.isVariable()) {
            return variableToJdbcNamedParameter(valueWithVariable.asVariable());
        }
        throw new GraphQLErrors(GraphQLErrorType.UNSUPPORTED_VALUE.bind(valueWithVariable.toString()));
    }

    public static Optional<Expression> idValueToDBValue(ValueWithVariable valueWithVariable) {
        if (valueWithVariable.isNull()) {
            return Optional.empty();
        }
        return Optional.of(leafValueToDBValue(valueWithVariable));
    }

    public static JdbcNamedParameter variableToJdbcNamedParameter(Variable variable) {
        return new JdbcNamedParameter(variable.getName());
    }

    public static SetStatement createInsertIdSetStatement(String typeName, String idFieldName, int level, int index) {
        String idVariableName = "@" + graphqlFieldNameToVariableName(typeName, idFieldName) + "_" + level + "_" + index;
        return new SetStatement(idVariableName, new ExpressionList<>(new Function().withName("LAST_INSERT_ID")));
    }

    public static SetStatement createUpdateIdSetStatement(String typeName, String idFieldName, int level, int index, Expression expression) {
        String idVariableName = "@" + graphqlFieldNameToVariableName(typeName, idFieldName) + "_" + level + "_" + index;
        return new SetStatement(idVariableName, new ExpressionList<>(expression));
    }

    public static Expression createGreaterThanLastInsertIDExpression(Table table, String idFieldName, ValueWithVariable valueWithVariable) {
        return new GreaterThanEquals()
                .withLeftExpression(graphqlFieldToColumn(table, idFieldName))
                .withRightExpression(
                        new Function()
                                .withName("IFNULL")
                                .withParameters(
                                        new ExpressionList<>(
                                                new ParenthesedSelect()
                                                        .withSelect(
                                                                new PlainSelect()
                                                                        .addSelectItem(new Column(graphqlFieldNameToColumnName(idFieldName)))
                                                                        .withFromItem(
                                                                                new JsonTableFunction()
                                                                                        .withJson(
                                                                                                new JdbcNamedParameter().withName(valueWithVariable.asVariable().getName())
                                                                                        )
                                                                                        .withPath(new StringValue("$[*]"))
                                                                                        .withColumnDefinitions(
                                                                                                new ColumnDefinition()
                                                                                                        .withColumnName(graphqlFieldNameToColumnName(idFieldName))
                                                                                                        .withColDataType(
                                                                                                                new ColDataType()
                                                                                                                        .withDataType("VARCHAR")
                                                                                                                        .withArgumentsStringList(Collections.singletonList("255"))
                                                                                                        )
                                                                                                        .addColumnSpecs("PATH", "'$." + idFieldName + "'")
                                                                                        )
                                                                                        .withAlias(new Alias(valueWithVariable.asVariable().getName()))
                                                                        )
                                                                        .withLimit(new Limit().withRowCount(new LongValue(1)))
                                                        ),
                                                new Function().withName("LAST_INSERT_ID")
                                        )
                                )
                );
    }

    public static Expression createEqualsToLastInsertIDExpression(Table table, String idFieldName, ValueWithVariable valueWithVariable) {
        return new EqualsTo()
                .withLeftExpression(graphqlFieldToColumn(table, idFieldName))
                .withRightExpression(
                        new Function()
                                .withName("IFNULL")
                                .withParameters(
                                        new ExpressionList<>(
                                                new ParenthesedSelect()
                                                        .withSelect(
                                                                new PlainSelect()
                                                                        .addSelectItem(new Column(graphqlFieldNameToColumnName(idFieldName)))
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
                                                                                                new ColumnDefinition()
                                                                                                        .withColumnName(graphqlFieldNameToColumnName(idFieldName))
                                                                                                        .withColDataType(
                                                                                                                new ColDataType()
                                                                                                                        .withDataType("VARCHAR")
                                                                                                                        .withArgumentsStringList(Collections.singletonList("255"))
                                                                                                        )
                                                                                                        .addColumnSpecs("PATH", "'$." + idFieldName + "'")
                                                                                        )
                                                                                        .withAlias(new Alias(valueWithVariable.asVariable().getName()))
                                                                        )
                                                                        .withLimit(new Limit().withRowCount(new LongValue(1)))
                                                        ),
                                                new Function().withName("LAST_INSERT_ID")
                                        )
                                )
                );
    }

    public static UserVariable createInsertIdUserVariable(String typeName, String idFieldName, int level, int index) {
        String idVariableName = graphqlFieldNameToVariableName(typeName, idFieldName) + "_" + level + "_" + index;
        return new UserVariable(idVariableName);
    }

    public static OrderByElement createIDOrderField(Column idColumn, List<Expression> userVariableList) {
        List<Expression> parameters = new ArrayList<>();
        parameters.add(idColumn);
        parameters.addAll(userVariableList);
        return new OrderByElement()
                .withExpression(
                        new Function()
                                .withName("FIELD")
                                .withParameters(new ExpressionList<>(parameters))
                );
    }
}
