package io.graphoenix.sql.translator;

import graphql.parser.antlr.GraphqlParser;
import io.graphoenix.core.handler.DocumentManager;
import io.graphoenix.core.handler.PackageManager;
import io.graphoenix.spi.error.GraphQLErrors;
import io.graphoenix.spi.graphql.Definition;
import io.graphoenix.spi.graphql.common.ValueWithVariable;
import io.graphoenix.spi.graphql.operation.Field;
import io.graphoenix.spi.graphql.operation.Operation;
import io.graphoenix.spi.graphql.type.FieldDefinition;
import io.graphoenix.spi.graphql.type.ObjectType;
import io.graphoenix.sql.expression.JsonAggregateFunction;
import io.graphoenix.sql.utils.DBValueUtil;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import net.sf.jsqlparser.expression.*;
import net.sf.jsqlparser.expression.operators.relational.ExpressionList;
import net.sf.jsqlparser.schema.Table;
import net.sf.jsqlparser.statement.select.*;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static io.graphoenix.spi.constant.Hammurabi.*;
import static io.graphoenix.spi.constant.Hammurabi.GROUP_BY_INPUT_NAME;
import static io.graphoenix.spi.error.GraphQLErrorType.OBJECT_SELECTION_NOT_EXIST;
import static io.graphoenix.sql.utils.DBNameUtil.*;
import static io.graphoenix.sql.utils.DBValueUtil.leafValueToDBValue;
import static net.sf.jsqlparser.expression.JsonFunctionType.ARRAY;
import static net.sf.jsqlparser.expression.JsonFunctionType.OBJECT;

@ApplicationScoped
public class QueryTranslator {

    private final DocumentManager documentManager;
    private final PackageManager packageManager;

    @Inject
    public QueryTranslator(DocumentManager documentManager, PackageManager packageManager) {
        this.documentManager = documentManager;
        this.packageManager = packageManager;
    }

    public Select translate(Operation operation) {
        ObjectType operationType = documentManager.getOperationTypeOrError(operation);


    }


    public PlainSelect objectFieldToPlainSelect(ObjectType objectType, FieldDefinition fieldDefinition, Field field, int level) {
        return objectFieldToPlainSelect(objectType, fieldDefinition, field, false, level);
    }


    public Stream<Expression> fieldToExpression(ObjectType objectType, FieldDefinition fieldDefinition, Field field, int level) {
        Definition fieldTypeDefinition = documentManager.getFieldTypeDefinition(fieldDefinition);
        StringValue selectionKey = new StringValue(Optional.ofNullable(field.getAlias()).orElse(field.getName()));
        if (fieldTypeDefinition.isObject()) {
            return Stream.of(
                    selectionKey,
                    jsonExtractFunction(
                            new ParenthesedSelect()
                                    .withSelect(objectFieldToPlainSelect(objectType, fieldDefinition, field, level + 1)),
                            fieldDefinition.getType().hasList()
                    )
            );
        } else {
            return Stream.of(
                    selectionKey,
                    fieldToExpression(typeName, fieldDefinitionContext, selectionContext, over, level)
            );
        }
    }


    public PlainSelect objectFieldToPlainSelect(ObjectType objectType, FieldDefinition fieldDefinition, Field field, boolean groupBy, int level) {
        if (field.getFields() == null || field.getFields().isEmpty()) {
            throw new GraphQLErrors(OBJECT_SELECTION_NOT_EXIST.bind(field.toString()));
        }
        PlainSelect plainSelect = new PlainSelect();
        Table table = typeToTable(objectType, level);
        Definition fieldTypeDefinition = documentManager.getFieldTypeDefinition(fieldDefinition);
        FromItem fromItem = !groupBy && field.hasGroupBy() && fieldDefinition.getType().hasList() ?
                new ParenthesedSelect()
                        .withSelect(objectFieldToPlainSelect(objectType, fieldDefinition, field, true, level))
                        .withAlias(new Alias(graphqlTypeNameToTableAliaName(objectType.getName(), level))) :
                typeToTable(objectType, level);

        Expression selectExpression = !groupBy && field.hasGroupBy() && fieldDefinition.getType().hasList() ?
                jsonExtractFunction(graphqlFieldToColumn(objectType.getName(), INPUT_VALUE_GROUP_BY_NAME, level), false):

        new PlainSelect()
                .withFromItem(
                        field.getFields().stream()
                                .filter(field1 -> f)
                )


    }

    protected Expression jsonObjectFunction(ExpressionList<? extends Expression> expressionList) {
        JsonFunction jsonFunction = new JsonFunction().withType(OBJECT);
        jsonFunction.getExpressions().addAll(expressionList.stream().map(JsonFunctionExpression::new).collect(Collectors.toList()));
        return jsonFunction;
    }

    protected Expression jsonAggregateFunction(ObjectType objectType, FieldDefinition fieldDefinition, Field field, ExpressionList<? extends Expression> expressionList, boolean sort, int level) {
        return (Expression) new JsonAggregateFunction()
                .withType(ARRAY)
                .withExpression(expressionList)
                .withLimit(argumentsToLimit(fieldDefinition, field))
                .withOrderByElements(sort ? argumentsToOrderByList(objectType, fieldDefinition, field, level) : null);
    }

    protected List<OrderByElement> argumentsToOrderByList(ObjectType objectType, FieldDefinition fieldDefinition, Field field, int level) {
        Definition fieldTypeDefinition = documentManager.getFieldTypeDefinition(fieldDefinition);
        if (fieldDefinition.getArguments() != null) {
            Table table = typeToTable(objectType, level);
            if (fieldTypeDefinition.isObject()) {
                return fieldDefinition.getArgument(INPUT_VALUE_ORDER_BY_NAME)
                        .flatMap(inputValue ->
                                field.getArguments().getArgument(inputValue.getName())
                                        .or(() -> Optional.ofNullable(inputValue.getDefaultValue()))
                        )
                        .filter(ValueWithVariable::isObject)
                        .map(valueWithVariable ->
                                valueWithVariable.asObject().getObjectValueWithVariable().entrySet().stream()
                                        .filter(entry -> entry.getValue().isEnum())
                                        .map(entry ->
                                                new OrderByElement()
                                                        .withAsc(!entry.getValue().asEnum().getValue().equals("DESC"))
                                                        .withExpression(graphqlFieldToColumn(table, entry.getKey()))
                                        )
                        )
                        .orElseGet(() ->
                                Stream.of(
                                        new OrderByElement()
                                                .withAsc(!field.getArguments().hasArgument(INPUT_VALUE_LAST_NAME))
                                                .withExpression(graphqlFieldToColumn(table, fieldTypeDefinition.asObject().getCursorField().orElseGet(() -> fieldTypeDefinition.asObject().getIDFieldOrError()).getName()))
                                )
                        )
                        .collect(Collectors.toList());
            } else {
                return fieldDefinition.getArgument(INPUT_VALUE_SORT_NAME)
                        .flatMap(inputValue ->
                                field.getArguments().getArgument(inputValue.getName())
                                        .or(() -> Optional.ofNullable(inputValue.getDefaultValue()))
                        )
                        .filter(ValueWithVariable::isEnum)
                        .map(ValueWithVariable::asEnum)
                        .map(enumValue ->
                                new OrderByElement()
                                        .withAsc(!enumValue.getValue().equals("DESC"))
                                        .withExpression(graphqlFieldToColumn(table, fieldDefinition.getName()))
                        )
                        .map(Collections::singletonList)
                        .orElseGet(Collections::emptyList);
            }
        }
        return Collections.emptyList();
    }

    protected Limit argumentsToLimit(FieldDefinition fieldDefinition, Field field) {
        if (fieldDefinition.getArguments() != null) {
            return fieldDefinition.getArgument(INPUT_VALUE_FIRST_NAME)
                    .flatMap(inputValue ->
                            field.getArguments().getArgument(inputValue.getName())
                                    .or(() -> Optional.ofNullable(inputValue.getDefaultValue()))
                    )
                    .or(() ->
                            fieldDefinition.getArgument(INPUT_VALUE_LAST_NAME)
                                    .flatMap(inputValue ->
                                            field.getArguments().getArgument(inputValue.getName())
                                                    .or(() -> Optional.ofNullable(inputValue.getDefaultValue()))
                                    )
                    )
                    .filter(ValueWithVariable::isInt)
                    .map(valueWithVariable ->
                            new Limit()
                                    .withRowCount(leafValueToDBValue(valueWithVariable))
                                    .withOffset(
                                            fieldDefinition.getArgument(INPUT_VALUE_FIRST_NAME)
                                                    .flatMap(inputValue ->
                                                            field.getArguments().getArgument(inputValue.getName())
                                                                    .or(() -> Optional.ofNullable(inputValue.getDefaultValue()))
                                                    )
                                                    .filter(ValueWithVariable::isInt)
                                                    .map(DBValueUtil::leafValueToDBValue)
                                                    .orElse(null)
                                    )
                    )
                    .orElse(null);
        }
        return null;
    }

    protected Expression jsonExtractFunction(Expression expression, boolean isList) {
        if (isList) {
            return new Function()
                    .withName("JSON_EXTRACT")
                    .withParameters(
                            new ExpressionList<>(
                                    new Function()
                                            .withName("IFNULL")
                                            .withParameters(
                                                    new ExpressionList<>(
                                                            expression,
                                                            new Function().withName("JSON_ARRAY")
                                                    )
                                            ),
                                    new StringValue("$")
                            )
                    );
        } else {
            return new Function()
                    .withName("JSON_EXTRACT")
                    .withParameters(new ExpressionList<>(expression, new StringValue("$")));
        }
    }

    protected Table typeToTable(ObjectType objectType, int level) {
        if (documentManager.isOperationType(objectType)) {
            return dualTable();
        }
        return graphqlTypeToTable(objectType.getName(), level);
    }
}
