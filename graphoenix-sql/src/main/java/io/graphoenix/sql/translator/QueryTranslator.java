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
import io.graphoenix.sql.expression.JsonArrayAggregateFunction;
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
import static net.sf.jsqlparser.expression.JsonFunctionType.MYSQL_OBJECT;

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

    public String operationToSelectSQL(Operation operation) {
        return operationToSelect(operation).toString();
    }

    public Select operationToSelect(Operation operation) {
        ObjectType operationType = documentManager.getOperationTypeOrError(operation);
        return new PlainSelect()
                .addSelectItem(
                        jsonObjectFunction(
                                operation.getFields().stream()
                                        .filter(packageManager::isLocalPackage)
                                        .filter(field -> !operationType.getField(field.getName()).isFetchField())
                                        .filter(field -> !operationType.getField(field.getName()).isInvokeField())
                                        .map(field ->
                                                new JsonKeyValuePair(
                                                        new StringValue(Optional.ofNullable(field.getAlias()).orElse(field.getName())).toString(),
                                                        fieldToExpression(operationType, operationType.getField(field.getName()), field, 0),
                                                        false,
                                                        false
                                                )
                                        )
                                        .collect(Collectors.toList())
                        ),
                        new Alias("`data`")
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
            JsonFunction jsonObjectFunction =
                    jsonObjectFunction(
                            field.getFields().stream()
                                    .filter(subField -> !fieldTypeDefinition.asObject().getField(subField.getName()).isFetchField())
                                    .filter(subField -> !fieldTypeDefinition.asObject().getField(subField.getName()).isInvokeField())
                                    .map(subField ->
                                            new JsonKeyValuePair(
                                                    new StringValue(Optional.ofNullable(subField.getAlias()).orElse(subField.getName())).toString(),
                                                    fieldToExpression(
                                                            fieldTypeDefinition.asObject(),
                                                            fieldTypeDefinition.asObject().getField(subField.getName()),
                                                            subField,
                                                            field.hasGroupBy(),
                                                            level + 1),
                                                    false,
                                                    false
                                            )
                                    )
                                    .collect(Collectors.toList())
                    );
            if (fieldDefinition.getType().hasList()) {
                selectExpression = jsonExtractFunction(
                        jsonAggregateFunction(
                                jsonObjectFunction,
                                argumentsToOrderByList(fieldDefinition, field, level),
                                argumentsToLimit(fieldDefinition, field)
                        )
                );
            } else {
                selectExpression = jsonExtractFunction(jsonObjectFunction);
            }
            fromItem = table;
        }

        plainSelect
                .addSelectItem(selectExpression)
                .withFromItem(fromItem)
                .setGroupByElement(argumentsToGroupBy(fieldDefinition, field, level));

        Optional<Expression> whereExpression = argumentsTranslator.argumentsToWhereExpression(objectType, fieldDefinition, field, level);
        if (!documentManager.isOperationType(objectType)) {
            Table parentTable = typeToTable(objectType, level - 1);
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
        whereExpression.ifPresent(plainSelect::setWhere);
        return plainSelect;
    }

    public Expression fieldToExpression(ObjectType objectType, FieldDefinition fieldDefinition, Field field, int level) {
        return fieldToExpression(objectType, fieldDefinition, field, false, level);
    }

    public Expression fieldToExpression(ObjectType objectType, FieldDefinition fieldDefinition, Field field, boolean over, int level) {
        Definition fieldTypeDefinition = documentManager.getFieldTypeDefinition(fieldDefinition);
        if (fieldTypeDefinition.isObject()) {
            return new ParenthesedSelect()
                    .withSelect(objectFieldToPlainSelect(objectType, fieldDefinition, field, level));
        } else {
            return leafFieldToExpression(objectType, fieldDefinition, field, over, level);
        }
    }

    protected Expression leafFieldToExpression(ObjectType objectType, FieldDefinition fieldDefinition, Field field, boolean over, int level) {
        if (fieldDefinition.getType().hasList()) {
            Table table = typeToTable(objectType, level);
            ObjectType withType = documentManager.getDocument().getObjectTypeOrError(fieldDefinition.getMapWithTypeOrError());
            FieldDefinition withToFieldDefinition = objectType.getField(fieldDefinition.getMapWithToOrError());
            Table withTable = typeToTable(withType, level);
            Expression column = fieldToColumn(withType, withToFieldDefinition, level);
            Expression selectExpression;

            if (fieldDefinition.isFunctionField()) {
                Function function = new Function()
                        .withName(fieldDefinition.getFunctionNameOrError())
                        .withParameters(column);
                if (over) {
                    selectExpression = new AnalyticExpression(function).withType(OVER);
                } else {
                    selectExpression = function;
                }
            } else {
                selectExpression = jsonExtractFunction(
                        jsonAggregateFunction(
                                column,
                                argumentsToOrderByList(fieldDefinition, field, level),
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

    protected Expression jsonExtractFunction(JsonArrayAggregateFunction expression) {
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

    protected JsonFunction jsonObjectFunction(List<JsonKeyValuePair> jsonKeyValuePairList) {
        JsonFunction jsonFunction = new JsonFunction().withType(MYSQL_OBJECT);
        jsonFunction.getKeyValuePairs().addAll(jsonKeyValuePairList);
        return jsonFunction;
    }

    protected JsonArrayAggregateFunction jsonAggregateFunction(Expression expression, List<OrderByElement> orderByElementList, Limit limit) {
        return new JsonArrayAggregateFunction()
                .withExpression(expression)
                .withLimit(limit)
                .withOrderByElements(orderByElementList);
    }

    protected GroupByElement argumentsToGroupBy(FieldDefinition fieldDefinition, Field field, int level) {
        if (fieldDefinition.getArguments() != null) {
            ObjectType fieldTypeDefinition = documentManager.getFieldTypeDefinition(fieldDefinition).asObject();

            List<Column> columnList = Stream.ofNullable(fieldDefinition.getArgumentOrNull(INPUT_VALUE_GROUP_BY_NAME))
                    .flatMap(inputValue ->
                            Optional.ofNullable(field.getArguments())
                                    .flatMap(arguments -> arguments.getArgument(inputValue.getName()))
                                    .or(() -> Optional.ofNullable(inputValue.getDefaultValue()))
                                    .stream()
                    )
                    .filter(ValueWithVariable::isArray)
                    .flatMap(valueWithVariable -> valueWithVariable.asArray().getValueWithVariables().stream())
                    .filter(ValueWithVariable::isString)
                    .map(valueWithVariable -> graphqlFieldToColumn(typeToTable(fieldTypeDefinition, level), valueWithVariable.asString().getValue()))
                    .collect(Collectors.toList());

            if (!columnList.isEmpty()) {
                return new GroupByElement()
                        .withGroupByExpressions(new ExpressionList<>(columnList));
            }
        }
        return null;
    }

    protected List<OrderByElement> argumentsToOrderByList(FieldDefinition fieldDefinition, Field field, int level) {
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
                                                        .withAsc(!entry.getValue().asEnum().getValue().equals(INPUT_SORT_NAME_VALUE_DESC))
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
                Table withTable = graphqlTypeToTable(fieldDefinition.getMapWithTypeOrError(), level);
                return fieldDefinition.getArgument(INPUT_VALUE_SORT_NAME)
                        .flatMap(inputValue ->
                                field.getArguments().getArgument(inputValue.getName())
                                        .or(() -> Optional.ofNullable(inputValue.getDefaultValue()))
                        )
                        .filter(ValueWithVariable::isEnum)
                        .map(ValueWithVariable::asEnum)
                        .map(enumValue ->
                                new OrderByElement()
                                        .withAsc(!enumValue.getValue().equals(INPUT_SORT_NAME_VALUE_DESC))
                                        .withExpression(graphqlFieldToColumn(withTable, fieldDefinition.getMapWithToOrError()))
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