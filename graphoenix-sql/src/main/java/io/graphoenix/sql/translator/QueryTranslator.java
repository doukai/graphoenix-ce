package io.graphoenix.sql.translator;

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
import net.sf.jsqlparser.expression.operators.relational.EqualsTo;
import net.sf.jsqlparser.expression.operators.relational.ExpressionList;
import net.sf.jsqlparser.expression.operators.relational.IsNullExpression;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.schema.Table;
import net.sf.jsqlparser.statement.select.*;
import net.sf.jsqlparser.util.cnfexpression.MultiAndExpression;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static io.graphoenix.spi.constant.Hammurabi.*;
import static io.graphoenix.spi.error.GraphQLErrorType.OBJECT_SELECTION_NOT_EXIST;
import static io.graphoenix.sql.utils.DBNameUtil.*;
import static io.graphoenix.sql.utils.DBValueUtil.leafValueToDBValue;
import static net.sf.jsqlparser.expression.AnalyticType.OVER;
import static net.sf.jsqlparser.expression.JsonFunctionType.ARRAY;
import static net.sf.jsqlparser.expression.JsonFunctionType.OBJECT;

@ApplicationScoped
public class QueryTranslator {

    private final DocumentManager documentManager;
    private final PackageManager packageManager;
    private final ArgumentsTranslator argumentsTranslator;

    @Inject
    public QueryTranslator(DocumentManager documentManager, PackageManager packageManager, ArgumentsTranslator argumentsTranslator) {
        this.documentManager = documentManager;
        this.packageManager = packageManager;
        this.argumentsTranslator = argumentsTranslator;
    }

    public Select operationToSelect(Operation operation) {
        ObjectType operationType = documentManager.getOperationTypeOrError(operation);
        return new PlainSelect()
                .addSelectItem(
                        jsonObjectFunction(
                                operation.getFields().stream()
                                        .filter(packageManager::isLocalPackage)
                                        .filter(item -> !operationType.getField(item.getName()).isFetchField())
                                        .filter(item -> !operationType.getField(item.getName()).isInvokeField())
                                        .flatMap(item ->
                                                Stream.of(
                                                        new StringValue(Optional.ofNullable(item.getAlias()).orElse(item.getName())),
                                                        new ParenthesedSelect()
                                                                .withSelect(
                                                                        objectFieldToPlainSelect(
                                                                                operationType,
                                                                                operationType.getField(item.getName()),
                                                                                item,
                                                                                0
                                                                        )
                                                                )
                                                )
                                        )
                                        .collect(Collectors.toList())
                        ),
                        new Alias("data")
                )
                .withFromItem(dualTable());
    }

    public PlainSelect objectFieldToPlainSelect(ObjectType objectType, FieldDefinition fieldDefinition, Field field, int level) {
        return objectFieldToPlainSelect(objectType, fieldDefinition, field, false, level);
    }

    public PlainSelect objectFieldToPlainSelect(ObjectType objectType, FieldDefinition fieldDefinition, Field field, boolean groupBy, int level) {
        if (field.getFields() == null || field.getFields().isEmpty()) {
            throw new GraphQLErrors(OBJECT_SELECTION_NOT_EXIST.bind(field.toString()));
        }
        PlainSelect plainSelect = new PlainSelect();
        Expression selectExpression;
        FromItem fromItem;

        ObjectType fieldTypeDefinition = documentManager.getFieldTypeDefinition(fieldDefinition).asObject();
        Table table = typeToTable(fieldTypeDefinition, level);

        if (!groupBy && field.hasGroupBy() && fieldDefinition.getType().hasList()) {
            selectExpression = jsonExtractFunction(graphqlFieldToColumn(fieldTypeDefinition.getName(), INPUT_VALUE_GROUP_BY_NAME, level));
            fromItem = new ParenthesedSelect()
                    .withSelect(objectFieldToPlainSelect(objectType, fieldDefinition, field, true, level))
                    .withAlias(new Alias(graphqlTypeNameToTableAliaName(fieldTypeDefinition.getName(), level)));
        } else {
            selectExpression = fieldToExpression(objectType, fieldDefinition, field, level);
            fromItem = table;
        }

        plainSelect
                .addSelectItem(selectExpression)
                .withFromItem(fromItem)
                .setGroupByElement(argumentsToGroupBy(fieldDefinition, field, level));

        if (!documentManager.isOperationType(objectType)) {
            Table parentTable = typeToTable(objectType, level - 1);
            Optional<Expression> whereExpression = argumentsTranslator.argumentsToWhereExpression(objectType, fieldDefinition, field, level);
            if (fieldDefinition.hasMapWith()) {
                Table withTable = graphqlTypeToTable(fieldDefinition.getMapWithTypeOrError(), level);
                EqualsTo equalsTo = new EqualsTo()
                        .withLeftExpression(graphqlFieldToColumn(withTable, fieldDefinition.getMapWithFromOrError()))
                        .withRightExpression(graphqlFieldToColumn(parentTable, fieldDefinition.getMapFromOrError()));
                return plainSelect
                        .addJoins(
                                new Join()
                                        .withLeft(true)
                                        .setFromItem(withTable)
                                        .addOnExpression(
                                                new MultiAndExpression(
                                                        Arrays.asList(
                                                                new EqualsTo()
                                                                        .withLeftExpression(graphqlFieldToColumn(withTable, fieldDefinition.getMapWithToOrError()))
                                                                        .withRightExpression(graphqlFieldToColumn(table, fieldDefinition.getMapToOrError())),
                                                                new IsNullExpression()
                                                                        .withLeftExpression(graphqlFieldToColumn(withTable, FIELD_DEPRECATED_NAME))
                                                        )
                                                )
                                        )
                        )
                        .withWhere(
                                whereExpression
                                        .map(expression -> (Expression) new MultiAndExpression(Arrays.asList(expression, equalsTo)))
                                        .orElse(equalsTo)
                        );
            } else {
                EqualsTo equalsTo = new EqualsTo()
                        .withLeftExpression(graphqlFieldToColumn(table, fieldDefinition.getMapToOrError()))
                        .withRightExpression(graphqlFieldToColumn(parentTable, fieldDefinition.getMapFromOrError()));
                return plainSelect
                        .withWhere(
                                whereExpression
                                        .map(expression -> (Expression) new MultiAndExpression(Arrays.asList(expression, equalsTo)))
                                        .orElse(equalsTo)
                        );
            }
        }
        return plainSelect;
    }

    public Expression fieldToExpression(ObjectType objectType, FieldDefinition fieldDefinition, Field field, int level) {
        Definition fieldTypeDefinition = documentManager.getFieldTypeDefinition(fieldDefinition);
        if (fieldTypeDefinition.isObject()) {
            JsonFunction jsonObjectFunction =
                    jsonObjectFunction(
                            field.getFields().stream()
                                    .filter(item -> !fieldTypeDefinition.asObject().getField(item.getName()).isFetchField())
                                    .filter(item -> !fieldTypeDefinition.asObject().getField(item.getName()).isInvokeField())
                                    .flatMap(item ->
                                            Stream.of(
                                                    new StringValue(Optional.ofNullable(item.getAlias()).orElse(item.getName())),
                                                    new ParenthesedSelect()
                                                            .withSelect(
                                                                    objectFieldToPlainSelect(
                                                                            fieldTypeDefinition.asObject(),
                                                                            fieldTypeDefinition.asObject().getField(item.getName()),
                                                                            item,
                                                                            level + 1)
                                                            )
                                            )
                                    )
                                    .collect(Collectors.toList())
                    );
            if (fieldDefinition.getType().hasList()) {
                return jsonExtractFunction(
                        jsonAggregateFunction(
                                jsonObjectFunction,
                                argumentsToOrderByList(objectType, fieldDefinition, field, level),
                                argumentsToLimit(fieldDefinition, field)
                        )
                );
            }
            return jsonExtractFunction(jsonObjectFunction);
        } else {
            return leafFieldToExpression(objectType, fieldDefinition, field, level);
        }
    }

    protected Expression leafFieldToExpression(ObjectType objectType, FieldDefinition fieldDefinition, Field field, int level) {
        return leafFieldToExpression(objectType, fieldDefinition, field, false, level);
    }

    protected Expression leafFieldToExpression(ObjectType objectType, FieldDefinition fieldDefinition, Field field, boolean over, int level) {
        if (fieldDefinition.getType().hasList()) {
            Table table = typeToTable(objectType, level);
            Table withTable = graphqlTypeToTable(fieldDefinition.getMapWithTypeOrError(), level);
            Expression selectExpression;
            if (fieldDefinition.isFunctionField()) {
                Function function = new Function()
                        .withName(fieldDefinition.getFunctionNameOrError())
                        .withParameters(fieldToColumn(objectType, objectType.getField(fieldDefinition.getFunctionFieldOrError()), level));
                if (over) {
                    selectExpression = new AnalyticExpression(function).withType(OVER);
                } else {
                    selectExpression = function;
                }
            } else {
                selectExpression = jsonExtractFunction(
                        jsonAggregateFunction(
                                fieldToColumn(objectType, fieldDefinition, level),
                                argumentsToOrderByList(objectType, fieldDefinition, field, level),
                                argumentsToLimit(fieldDefinition, field)
                        )
                );
            }

            Optional<Expression> whereExpression = argumentsTranslator.argumentsToWhereExpression(objectType, fieldDefinition, field, level);
            MultiAndExpression multiAndExpression = new MultiAndExpression(
                    Arrays.asList(
                            new EqualsTo()
                                    .withLeftExpression(graphqlFieldToColumn(withTable, fieldDefinition.getMapWithFromOrError()))
                                    .withRightExpression(graphqlFieldToColumn(table, fieldDefinition.getMapFromOrError())),
                            new IsNullExpression()
                                    .withLeftExpression(graphqlFieldToColumn(withTable, FIELD_DEPRECATED_NAME))
                    )
            );
            return jsonExtractFunction(
                    new ParenthesedSelect()
                            .withSelect(
                                    new PlainSelect()
                                            .addSelectItem(selectExpression)
                                            .withFromItem(withTable)
                                            .withWhere(
                                                    whereExpression
                                                            .map(expression -> (Expression) new MultiAndExpression(Arrays.asList(expression, multiAndExpression)))
                                                            .orElse(multiAndExpression)
                                            )
                            )
            );
        } else {
            if (fieldDefinition.isFunctionField()) {
                Function function = new Function()
                        .withName(fieldDefinition.getFunctionNameOrError())
                        .withParameters(fieldToColumn(objectType, objectType.getField(fieldDefinition.getFunctionFieldOrError()), level));
                if (over) {
                    return new AnalyticExpression(function).withType(OVER);
                }
                return function;
            } else {
                return fieldToColumn(objectType, fieldDefinition, level);
            }
        }
    }

    protected Expression fieldToColumn(ObjectType objectType, FieldDefinition fieldDefinition, int level) {
        Definition fieldTypeDefinition = documentManager.getFieldTypeDefinition(fieldDefinition);
        Column column = graphqlFieldToColumn(objectType.getName(), fieldDefinition.getName(), level);
        switch (fieldTypeDefinition.getName()) {
            case SCALA_ID_NAME:
                return new Function()
                        .withName("CONVERT")
                        .withParameters(
                                column,
                                new HexValue("CHAR")
                        );
            case SCALA_BOOLEAN_NAME:
                return new Function()
                        .withName("IF")
                        .withParameters(
                                column,
                                new HexValue("TRUE"),
                                new HexValue("FALSE")
                        );
            default:
                return column;
        }
    }

    protected Expression jsonExtractFunction(JsonAggregateFunction expression) {
        return new Function()
                .withName("JSON_EXTRACT")
                .withParameters(
                        new ExpressionList<>(
                                new Function()
                                        .withName("IFNULL")
                                        .withParameters(
                                                new ExpressionList<>(
                                                        expression,
                                                        new JsonFunction().withType(ARRAY)
                                                )
                                        ),
                                new StringValue("$")
                        )
                );
    }

    protected Expression jsonExtractFunction(Expression expression) {
        return new Function()
                .withName("JSON_EXTRACT")
                .withParameters(new ExpressionList<>(expression, new StringValue("$")));
    }

    protected JsonFunction jsonObjectFunction(List<Expression> expressionList) {
        JsonFunction jsonFunction = new JsonFunction().withType(OBJECT);
        jsonFunction.getExpressions().addAll(expressionList.stream().map(JsonFunctionExpression::new).collect(Collectors.toList()));
        return jsonFunction;
    }

    protected JsonAggregateFunction jsonAggregateFunction(Expression expression, List<OrderByElement> orderByElementList, Limit limit) {
        return (JsonAggregateFunction) new JsonAggregateFunction()
                .withType(ARRAY)
                .withExpression(expression)
                .withLimit(limit)
                .withOrderByElements(orderByElementList);
    }

    protected GroupByElement argumentsToGroupBy(FieldDefinition fieldDefinition, Field field, int level) {
        if (fieldDefinition.getArguments() != null) {
            ObjectType fieldTypeDefinition = documentManager.getFieldTypeDefinition(fieldDefinition).asObject();
            return new GroupByElement()
                    .withGroupByExpressions(
                            new ExpressionList<>(
                                    Stream.ofNullable(fieldDefinition.getArgumentOrNull(INPUT_VALUE_GROUP_BY_NAME))
                                            .flatMap(inputValue ->
                                                    Stream.concat(
                                                            Stream.ofNullable(field.getArguments().getArgumentOrNull(inputValue.getName())),
                                                            Stream.ofNullable(inputValue.getDefaultValue())
                                                    )
                                            )
                                            .filter(ValueWithVariable::isArray)
                                            .flatMap(valueWithVariable -> valueWithVariable.asArray().getValueWithVariables().stream())
                                            .filter(ValueWithVariable::isString)
                                            .map(valueWithVariable -> graphqlFieldToColumn(typeToTable(fieldTypeDefinition, level), valueWithVariable.asString().getValue()))
                                            .collect(Collectors.toList())
                            )
                    );
        }
        return null;
    }

    protected List<OrderByElement> argumentsToOrderByList(ObjectType objectType, FieldDefinition fieldDefinition, Field field, int level) {
        Definition fieldTypeDefinition = documentManager.getFieldTypeDefinition(fieldDefinition);
        if (fieldDefinition.getArguments() != null) {
            if (fieldTypeDefinition.isObject()) {
                Table table = typeToTable(fieldTypeDefinition.asObject(), level);
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
                Table table = typeToTable(objectType, level);
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

    protected Table typeToTable(ObjectType objectType, int level) {
        return graphqlTypeToTable(objectType.getName(), level);
    }
}
