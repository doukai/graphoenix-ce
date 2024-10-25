package io.graphoenix.sql.translator;

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
import io.graphoenix.sql.expression.JsonArrayAggregateFunction;
import io.graphoenix.sql.utils.DBValueUtil;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import net.sf.jsqlparser.expression.*;
import net.sf.jsqlparser.expression.operators.relational.*;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.schema.Table;
import net.sf.jsqlparser.statement.select.*;
import net.sf.jsqlparser.util.cnfexpression.MultiAndExpression;
import net.sf.jsqlparser.util.cnfexpression.MultiOrExpression;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static io.graphoenix.spi.constant.Hammurabi.*;
import static io.graphoenix.spi.error.GraphQLErrorType.OBJECT_SELECTION_NOT_EXIST;
import static io.graphoenix.sql.utils.DBNameUtil.*;
import static io.graphoenix.sql.utils.DBValueUtil.*;
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

    public Optional<String> operationToSelectSQL(Operation operation) {
        return operationToSelect(operation).map(Select::toString);
    }

    public Optional<Select> operationToSelect(Operation operation) {
        ObjectType operationType = documentManager.getOperationTypeOrError(operation);

        List<JsonKeyValuePair> jsonKeyValuePairList = operation.getFields().stream()
                .filter(field -> {
                            FieldDefinition fieldDefinition = operationType.getField(field.getName());
                            return packageManager.isLocalPackage(fieldDefinition) &&
                                    !fieldDefinition.isFetchField() &&
                                    !fieldDefinition.isInvokeField() &&
                                    !fieldDefinition.isConnectionField();
                        }
                )
                .map(field ->
                        new JsonKeyValuePair(
                                new StringValue(Optional.ofNullable(field.getAlias()).orElse(field.getName())).toString(),
                                fieldToExpression(operationType, operationType.getField(field.getName()), field, 0),
                                false,
                                false
                        )
                )
                .collect(Collectors.toList());

        if (jsonKeyValuePairList.isEmpty()) {
            return Optional.empty();
        }

        return Optional.of(
                new PlainSelect()
                        .addSelectItem(
                                jsonObjectFunction(jsonKeyValuePairList),
                                new Alias("`data`")
                        )
                        .withFromItem(dualTable())
        );
    }

    protected PlainSelect objectFieldToPlainSelect(ObjectType objectType, FieldDefinition fieldDefinition, Field field, int level) {
        return objectFieldToPlainSelect(objectType, fieldDefinition, field, false, level);
    }

    protected PlainSelect objectFieldToPlainSelect(ObjectType objectType, FieldDefinition fieldDefinition, Field field, boolean inGroupBy, int level) {
        if (field.getFields() == null || field.getFields().isEmpty()) {
            throw new GraphQLErrors(OBJECT_SELECTION_NOT_EXIST.bind(field.toString()));
        }
        PlainSelect plainSelect = new PlainSelect();
        Expression selectExpression;
        FromItem fromItem;
        ObjectType fieldTypeDefinition = documentManager.getFieldTypeDefinition(fieldDefinition).asObject();
        Table table = typeToTable(fieldTypeDefinition, level);
        boolean hasGroupBy = field.hasGroupBy();
        boolean hasGroupFunctionField = field.getFields().stream().anyMatch(item -> fieldTypeDefinition.getField(item.getName()).isGroupFunctionField());
        if (!inGroupBy && (hasGroupBy || hasGroupFunctionField) && fieldDefinition.getType().hasList()) {
            Column groupByColumn = graphqlFieldToColumn(fieldTypeDefinition.getName(), INPUT_VALUE_GROUP_BY_NAME, level);
            if (fieldDefinition.getType().hasList()) {
                selectExpression = jsonExtractFunction(jsonAggregateFunction(groupByColumn, null, null));
            } else {
                selectExpression = jsonExtractFunction(groupByColumn);
            }
            fromItem = new ParenthesedSelect()
                    .withSelect(objectFieldToPlainSelect(objectType, fieldDefinition, field, true, level))
                    .withAlias(new Alias(graphqlTypeNameToTableAliaName(fieldTypeDefinition.getName(), level)));
            return plainSelect
                    .withFromItem(fromItem)
                    .addSelectItem(selectExpression);
        } else {
            JsonFunction jsonObjectFunction =
                    jsonObjectFunction(
                            field.getFields().stream()
                                    .filter(subField -> {
                                                FieldDefinition subFieldDefinition = fieldTypeDefinition.asObject().getField(subField.getName());
                                                return !subFieldDefinition.isFetchField() &&
                                                        !subFieldDefinition.isInvokeField() &&
                                                        !subFieldDefinition.isConnectionField();
                                            }
                                    )
                                    .map(subField ->
                                            new JsonKeyValuePair(
                                                    new StringValue(Optional.ofNullable(subField.getAlias()).orElseGet(subField::getName)).toString(),
                                                    inGroupBy ?
                                                            groupFieldToExpression(
                                                                    fieldTypeDefinition.asObject(),
                                                                    fieldTypeDefinition.asObject().getField(subField.getName()),
                                                                    subField,
                                                                    level
                                                            ) :
                                                            fieldToExpression(
                                                                    fieldTypeDefinition.asObject(),
                                                                    fieldTypeDefinition.asObject().getField(subField.getName()),
                                                                    subField,
                                                                    !fieldDefinition.getType().hasList() && hasGroupBy,
                                                                    level
                                                            ),
                                                    false,
                                                    false
                                            )
                                    )
                                    .collect(Collectors.toList())
                    );
            if (fieldDefinition.getType().hasList() && !inGroupBy) {
                selectExpression = jsonExtractFunction(
                        jsonAggregateFunction(
                                jsonObjectFunction,
                                argumentsToOrderByStream(fieldDefinition, field, level).collect(Collectors.toList()),
                                argumentsToLimit(fieldDefinition, field)
                        )
                );
            } else {
                selectExpression = jsonExtractFunction(jsonObjectFunction);
            }
            fromItem = table;
        }

        if (inGroupBy) {
            plainSelect.addSelectItem(selectExpression, new Alias(graphqlFieldNameToColumnName(INPUT_VALUE_GROUP_BY_NAME)));
            plainSelect.setOrderByElements(argumentsToOrderByStream(fieldDefinition, field, level).collect(Collectors.toList()));
            plainSelect.setLimit(argumentsToLimit(fieldDefinition, field));
            plainSelect
                    .addJoins(
                            field.getFields().stream()
                                    .filter(subField -> {
                                                FieldDefinition subFieldDefinition = fieldTypeDefinition.asObject().getField(subField.getName());
                                                return !subFieldDefinition.isFetchField() &&
                                                        !subFieldDefinition.isInvokeField() &&
                                                        !subFieldDefinition.isConnectionField();
                                            }
                                    )
                                    .flatMap(subField -> groupFieldToJoinStream(fieldTypeDefinition.asObject(), fieldTypeDefinition.asObject().getField(subField.getName()), subField, level + 1))
                                    .collect(Collectors.toList())
                    );
        } else {
            plainSelect.addSelectItem(selectExpression);
        }

        plainSelect
                .withFromItem(fromItem)
                .setGroupByElement(argumentsToGroupBy(fieldDefinition, field, level));

        Optional<Expression> whereExpression;
        if (documentManager.isMutationOperationType(objectType)) {
            String idName = fieldTypeDefinition.asObject().getIDFieldOrError().getName();
            whereExpression = fieldDefinition.getArgumentOrEmpty(INPUT_VALUE_WHERE_NAME)
                    .flatMap(inputValue ->
                            Optional.ofNullable(field.getArguments())
                                    .flatMap(arguments -> arguments.getArgumentOrEmpty(inputValue.getName()))
                                    .flatMap(valueWithVariable -> argumentsTranslator.inputValueToWhereExpression(objectType, fieldDefinition, inputValue, valueWithVariable, level))
                    )
                    .or(() ->
                            fieldDefinition.getArgumentOrEmpty(INPUT_VALUE_LIST_NAME)
                                    .flatMap(inputValue ->
                                            Optional.ofNullable(field.getArguments())
                                                    .flatMap(arguments -> arguments.getArgumentOrEmpty(inputValue.getName()))
                                                    .filter(valueWithVariable -> !valueWithVariable.isNull())
                                                    .map(valueWithVariable -> {
                                                                if (valueWithVariable.isVariable()) {
                                                                    return createGreaterThanLastInsertIDExpression(table, idName, valueWithVariable);
                                                                } else {
                                                                    List<ValueWithVariable> valueWithVariableList = valueWithVariable.asArray().getValueWithVariables().stream()
                                                                            .filter(item -> !item.isNull())
                                                                            .collect(Collectors.toList());

                                                                    List<Expression> idExpressionList = IntStream.range(0, valueWithVariableList.size())
                                                                            .filter(index -> !valueWithVariableList.get(index).asObject().containsKey(INPUT_VALUE_WHERE_NAME) || valueWithVariableList.get(index).asObject().getValueWithVariable(INPUT_VALUE_WHERE_NAME).asObject().containsKey(idName))
                                                                            .mapToObj(index -> {
                                                                                        if (valueWithVariableList.get(index).isVariable()) {
                                                                                            return createInsertIdUserVariable(fieldTypeDefinition.getName(), idName, 0, index);
                                                                                        } else {
                                                                                            return valueWithVariableList.get(index).asObject()
                                                                                                    .getValueWithVariableOrEmpty(idName)
                                                                                                    .flatMap(DBValueUtil::idValueToDBValue)
                                                                                                    .orElseGet(() ->
                                                                                                            valueWithVariableList.get(index).asObject()
                                                                                                                    .getValueWithVariableOrEmpty(INPUT_VALUE_WHERE_NAME)
                                                                                                                    .flatMap(whereInputValue -> whereInputValue.asObject().getValueWithVariableOrEmpty(idName))
                                                                                                                    .flatMap(idInputValue -> idInputValue.asObject().getValueWithVariableOrEmpty(INPUT_OPERATOR_INPUT_VALUE_VAL_NAME))
                                                                                                                    .flatMap(DBValueUtil::idValueToDBValue)
                                                                                                                    .orElseGet(() -> createInsertIdUserVariable(fieldTypeDefinition.getName(), idName, 0, index))
                                                                                                    );
                                                                                        }
                                                                                    }
                                                                            )
                                                                            .collect(Collectors.toList());

                                                                    List<Expression> whereExpressionList = IntStream.range(0, valueWithVariableList.size())
                                                                            .filter(index -> valueWithVariableList.get(index).asObject().containsKey(INPUT_VALUE_WHERE_NAME))
                                                                            .filter(index -> !valueWithVariableList.get(index).asObject().getValueWithVariable(INPUT_VALUE_WHERE_NAME).asJsonObject().containsKey(idName))
                                                                            .mapToObj(index ->
                                                                                    argumentsTranslator
                                                                                            .inputValueToWhereExpression(
                                                                                                    objectType,
                                                                                                    fieldDefinition,
                                                                                                    documentManager.getInputValueTypeDefinition(inputValue).asInputObject().getInputValue(INPUT_VALUE_WHERE_NAME),
                                                                                                    valueWithVariableList.get(index).asObject().getValueWithVariable(INPUT_VALUE_WHERE_NAME),
                                                                                                    level
                                                                                            )
                                                                            )
                                                                            .flatMap(Optional::stream)
                                                                            .collect(Collectors.toList());

                                                                    if (!idExpressionList.isEmpty() && !whereExpressionList.isEmpty()) {
                                                                        whereExpressionList
                                                                                .add(
                                                                                        0,
                                                                                        new InExpression()
                                                                                                .withLeftExpression(graphqlFieldToColumn(table, idName))
                                                                                                .withRightExpression(new Parenthesis(new ExpressionList<>(idExpressionList)))
                                                                                );
                                                                        return new MultiOrExpression(whereExpressionList);
                                                                    } else if (!idExpressionList.isEmpty()) {
                                                                        return new InExpression()
                                                                                .withLeftExpression(graphqlFieldToColumn(table, idName))
                                                                                .withRightExpression(new Parenthesis(new ExpressionList<>(idExpressionList)));
                                                                    } else {
                                                                        return new MultiOrExpression(whereExpressionList);
                                                                    }
                                                                }
                                                            }
                                                    )
                                    )
                                    .or(() ->
                                            fieldDefinition.getArgumentOrEmpty(INPUT_VALUE_INPUT_NAME)
                                                    .flatMap(inputValue ->
                                                            Optional.ofNullable(field.getArguments())
                                                                    .flatMap(arguments -> arguments.getArgumentOrEmpty(inputValue.getName()))
                                                                    .filter(valueWithVariable -> !valueWithVariable.isNull())
                                                                    .map(valueWithVariable -> {
                                                                                if (valueWithVariable.isVariable()) {
                                                                                    return createEqualsToLastInsertIDExpression(table, idName, valueWithVariable);
                                                                                } else {
                                                                                    return new EqualsTo()
                                                                                            .withLeftExpression(graphqlFieldToColumn(table, idName))
                                                                                            .withRightExpression(
                                                                                                    valueWithVariable.asObject()
                                                                                                            .getValueWithVariableOrEmpty(idName)
                                                                                                            .flatMap(DBValueUtil::idValueToDBValue)
                                                                                                            .orElseGet(() -> createInsertIdUserVariable(fieldTypeDefinition.getName(), idName, 0, 0))
                                                                                            );
                                                                                }
                                                                            }
                                                                    )
                                                    )
                                                    .or(() ->
                                                            fieldDefinition.getArgumentOrEmpty(idName)
                                                                    .map(inputValue ->
                                                                            new EqualsTo()
                                                                                    .withLeftExpression(graphqlFieldToColumn(table, idName))
                                                                                    .withRightExpression(
                                                                                            Optional.ofNullable(field.getArguments())
                                                                                                    .flatMap(arguments -> arguments.getArgumentOrEmpty(inputValue.getName()))
                                                                                                    .flatMap(DBValueUtil::idValueToDBValue)
                                                                                                    .orElseGet(() -> createInsertIdUserVariable(fieldTypeDefinition.getName(), idName, 0, 0))
                                                                                    )
                                                                    )
                                                    )
                                    )
                    )
                    .map(expression ->
                            (Expression) new MultiAndExpression(
                                    new ExpressionList<>(
                                            expression,
                                            new NotEqualsTo()
                                                    .withLeftExpression(graphqlFieldToColumn(fieldTypeDefinition.asObject().getName(), FIELD_DEPRECATED_NAME, level))
                                                    .withRightExpression(new LongValue(1))
                                    )
                            )
                    )
                    .or(() ->
                            Optional.of(
                                    new NotEqualsTo()
                                            .withLeftExpression(graphqlFieldToColumn(fieldTypeDefinition.asObject().getName(), FIELD_DEPRECATED_NAME, level))
                                            .withRightExpression(new LongValue(1))
                            )
                    );

            fieldDefinition.getArgumentOrEmpty(INPUT_VALUE_LIST_NAME)
                    .flatMap(inputValue ->
                            Optional.ofNullable(field.getArguments())
                                    .flatMap(arguments -> arguments.getArgumentOrEmpty(inputValue.getName()))
                                    .filter(valueWithVariable -> !valueWithVariable.isNull())
                                    .filter(valueWithVariable -> !valueWithVariable.isVariable())
                                    .map(valueWithVariable ->
                                            valueWithVariable.asArray().getValueWithVariables().stream()
                                                    .filter(item -> !item.isNull())
                                                    .filter(item -> !item.asObject().containsKey(INPUT_VALUE_WHERE_NAME) || item.asObject().getValueWithVariable(INPUT_VALUE_WHERE_NAME).asObject().containsKey(idName))
                                                    .collect(Collectors.toList())
                                    )
                                    .filter(valueWithVariableList -> !valueWithVariableList.isEmpty())
                                    .map(valueWithVariableList ->
                                            createIDOrderField(
                                                    graphqlFieldToColumn(table, idName),
                                                    IntStream.range(0, valueWithVariableList.size())
                                                            .mapToObj(index -> {
                                                                        if (valueWithVariableList.get(index).isVariable()) {
                                                                            return createInsertIdUserVariable(fieldTypeDefinition.getName(), idName, 0, index);
                                                                        } else {
                                                                            return valueWithVariableList.get(index).asObject()
                                                                                    .getValueWithVariableOrEmpty(idName)
                                                                                    .flatMap(DBValueUtil::idValueToDBValue)
                                                                                    .orElseGet(() ->
                                                                                            valueWithVariableList.get(index).asObject()
                                                                                                    .getValueWithVariableOrEmpty(INPUT_VALUE_WHERE_NAME)
                                                                                                    .flatMap(whereInputValue -> whereInputValue.asObject().getValueWithVariableOrEmpty(idName))
                                                                                                    .flatMap(idInputValue -> idInputValue.asObject().getValueWithVariableOrEmpty(INPUT_OPERATOR_INPUT_VALUE_VAL_NAME))
                                                                                                    .flatMap(DBValueUtil::idValueToDBValue)
                                                                                                    .orElseGet(() -> createInsertIdUserVariable(fieldTypeDefinition.getName(), idName, 0, index))
                                                                                    );
                                                                        }
                                                                    }
                                                            )
                                                            .collect(Collectors.toList())
                                            )
                                    )
                    )
                    .ifPresent(plainSelect::addOrderByElements);
        } else {
            whereExpression = argumentsTranslator.argumentsToWhereExpression(objectType, fieldDefinition, field, level);
        }

        if (!fieldDefinition.getType().hasList()) {
            plainSelect
                    .setLimit(
                            new Limit()
                                    .withOffset(new LongValue(0))
                                    .withRowCount(new LongValue(1))
                    );
        }

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
                                                                new NotEqualsTo()
                                                                        .withLeftExpression(graphqlFieldToColumn(withTable, FIELD_DEPRECATED_NAME))
                                                                        .withRightExpression(new LongValue(1))
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

    protected Expression groupFieldToExpression(ObjectType objectType, FieldDefinition fieldDefinition, Field field, int level) {
        Definition fieldTypeDefinition = documentManager.getFieldTypeDefinition(fieldDefinition);
        if (fieldTypeDefinition.isObject()) {
            JsonFunction jsonObjectFunction =
                    jsonObjectFunction(
                            field.getFields().stream()
                                    .filter(subField -> {
                                                FieldDefinition subFieldDefinition = fieldTypeDefinition.asObject().getField(subField.getName());
                                                return !subFieldDefinition.isFetchField() &&
                                                        !subFieldDefinition.isInvokeField() &&
                                                        !subFieldDefinition.isConnectionField() &&
                                                        !subFieldDefinition.getType().hasList();
                                            }
                                    )
                                    .map(subField ->
                                            new JsonKeyValuePair(
                                                    new StringValue(Optional.ofNullable(subField.getAlias()).orElseGet(subField::getName)).toString(),
                                                    groupFieldToExpression(
                                                            fieldTypeDefinition.asObject(),
                                                            fieldTypeDefinition.asObject().getField(subField.getName()),
                                                            subField,
                                                            level + 1
                                                    ),
                                                    false,
                                                    false
                                            )
                                    )
                                    .collect(Collectors.toList())
                    );

            return jsonExtractFunction(jsonObjectFunction);
        } else {
            return leafFieldToExpression(objectType, fieldDefinition, field, false, level);
        }
    }

    protected Stream<Join> groupFieldToJoinStream(ObjectType objectType, FieldDefinition fieldDefinition, Field field, int level) {
        Definition fieldTypeDefinition = documentManager.getFieldTypeDefinition(fieldDefinition);
        if (!fieldTypeDefinition.isLeaf()) {
            if (field.getFields() == null || field.getFields().isEmpty()) {
                throw new GraphQLErrors(OBJECT_SELECTION_NOT_EXIST.bind(field.toString()));
            }
            Stream<Join> subFieldJoinStream = field.getFields().stream()
                    .filter(subField -> {
                                FieldDefinition subFieldDefinition = fieldTypeDefinition.asObject().getField(subField.getName());
                                return !subFieldDefinition.isFetchField() &&
                                        !subFieldDefinition.isInvokeField() &&
                                        !subFieldDefinition.isConnectionField();
                            }
                    )
                    .flatMap(subField -> groupFieldToJoinStream(fieldTypeDefinition.asObject(), fieldTypeDefinition.asObject().getField(subField.getName()), subField, level + 1));
            Table table = typeToTable(fieldTypeDefinition.asObject(), level);
            Table parentTable = typeToTable(objectType, level - 1);
            if (fieldDefinition.hasMapWith()) {
                Table withTable = graphqlTypeToTable(fieldDefinition.getMapWithTypeOrError(), level);
                return Stream
                        .concat(
                                Stream.of(
                                        new Join()
                                                .withLeft(true)
                                                .setFromItem(withTable)
                                                .addOnExpression(
                                                        new MultiAndExpression(
                                                                Arrays.asList(
                                                                        new EqualsTo()
                                                                                .withLeftExpression(graphqlFieldToColumn(withTable, fieldDefinition.getMapWithFromOrError()))
                                                                                .withRightExpression(graphqlFieldToColumn(parentTable, fieldDefinition.getMapFromOrError())),
                                                                        new NotEqualsTo()
                                                                                .withLeftExpression(graphqlFieldToColumn(withTable, FIELD_DEPRECATED_NAME))
                                                                                .withRightExpression(new LongValue(1))
                                                                )
                                                        )
                                                ),
                                        new Join()
                                                .withLeft(true)
                                                .setFromItem(table)
                                                .addOnExpression(
                                                        new MultiAndExpression(
                                                                Arrays.asList(
                                                                        new EqualsTo()
                                                                                .withLeftExpression(graphqlFieldToColumn(table, fieldDefinition.getMapToOrError()))
                                                                                .withRightExpression(graphqlFieldToColumn(withTable, fieldDefinition.getMapWithToOrError())),
                                                                        new NotEqualsTo()
                                                                                .withLeftExpression(graphqlFieldToColumn(table, FIELD_DEPRECATED_NAME))
                                                                                .withRightExpression(new LongValue(1))
                                                                )
                                                        )
                                                )
                                ),
                                subFieldJoinStream
                        );
            } else {
                return Stream
                        .concat(
                                Stream.of(
                                        new Join()
                                                .withLeft(true)
                                                .setFromItem(table)
                                                .addOnExpression(
                                                        new MultiAndExpression(
                                                                Arrays.asList(
                                                                        new EqualsTo()
                                                                                .withLeftExpression(graphqlFieldToColumn(table, fieldDefinition.getMapToOrError()))
                                                                                .withRightExpression(graphqlFieldToColumn(parentTable, fieldDefinition.getMapFromOrError())),
                                                                        new NotEqualsTo()
                                                                                .withLeftExpression(graphqlFieldToColumn(table, FIELD_DEPRECATED_NAME))
                                                                                .withRightExpression(new LongValue(1))
                                                                )
                                                        )
                                                )
                                ),
                                subFieldJoinStream
                        );
            }
        }
        return Stream.empty();
    }

    protected Expression fieldToExpression(ObjectType objectType, FieldDefinition fieldDefinition, Field field, int level) {
        return fieldToExpression(objectType, fieldDefinition, field, false, level);
    }

    protected Expression fieldToExpression(ObjectType objectType, FieldDefinition fieldDefinition, Field field, boolean over, int level) {
        Definition fieldTypeDefinition = documentManager.getFieldTypeDefinition(fieldDefinition);
        if (fieldTypeDefinition.isObject()) {
            return new ParenthesedSelect()
                    .withSelect(objectFieldToPlainSelect(objectType, fieldDefinition, field, level + 1));
        } else {
            return leafFieldToExpression(objectType, fieldDefinition, field, over, level);
        }
    }

    protected Expression leafFieldToExpression(ObjectType objectType, FieldDefinition fieldDefinition, Field field, boolean over, int level) {
        if (fieldDefinition.getType().hasList()) {
            Table table = typeToTable(objectType, level);
            ObjectType withType = documentManager.getDocument().getObjectTypeOrError(fieldDefinition.getMapWithTypeOrError());
            FieldDefinition withToFieldDefinition = withType.getField(fieldDefinition.getMapWithToOrError());
            Table withTable = typeToTable(withType, level);
            Expression selectExpression;

            if (fieldDefinition.isFunctionField()) {
                Function function = new Function()
                        .withName(fieldDefinition.getFunctionNameOrError())
                        .withParameters(graphqlFieldToColumn(withType.getName(), withToFieldDefinition.getName(), level));
                if (fieldDefinition.getFunctionNameOrError().equals("COUNT")) {
                    function = new Function()
                            .withName("CONVERT")
                            .withParameters(
                                    function,
                                    new HexValue("INT")
                            );
                }
                if (over) {
                    selectExpression = new AnalyticExpression(function).withType(OVER);
                } else {
                    selectExpression = function;
                }
            } else {
                Expression column = fieldToColumn(withType, withToFieldDefinition, level);
                selectExpression = jsonExtractFunction(
                        jsonAggregateFunction(
                                column,
                                argumentsToOrderByStream(fieldDefinition, field, level).collect(Collectors.toList()),
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
                            new NotEqualsTo()
                                    .withLeftExpression(graphqlFieldToColumn(withTable, FIELD_DEPRECATED_NAME))
                                    .withRightExpression(new LongValue(1))
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
                Expression function = new Function()
                        .withName(fieldDefinition.getFunctionNameOrError())
                        .withParameters(graphqlFieldToColumn(objectType.getName(), objectType.getField(fieldDefinition.getFunctionFieldOrError()).getName(), level));
                if (over) {
                    function = new AnalyticExpression((Function) function).withType(OVER);
                }
                if (fieldDefinition.getFunctionNameOrError().equals("COUNT")) {
                    function = new Function()
                            .withName("CONVERT")
                            .withParameters(
                                    function,
                                    new HexValue("INT")
                            );
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

            List<Column> columnList = Stream.ofNullable(fieldDefinition.getArgument(INPUT_VALUE_GROUP_BY_NAME))
                    .flatMap(inputValue ->
                            Optional.ofNullable(field.getArguments())
                                    .flatMap(arguments -> arguments.getArgumentOrEmpty(inputValue.getName()))
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

    protected Stream<OrderByElement> argumentsToOrderByStream(FieldDefinition fieldDefinition, Field field, int level) {
        Definition fieldTypeDefinition = documentManager.getFieldTypeDefinition(fieldDefinition);
        if (fieldDefinition.getArguments() != null) {
            if (fieldTypeDefinition.isObject()) {
                Table table = typeToTable(fieldTypeDefinition.asObject(), level);
                return fieldDefinition.getArgumentOrEmpty(INPUT_VALUE_ORDER_BY_NAME)
                        .map(inputValue ->
                                Optional.ofNullable(field.getArguments())
                                        .flatMap(arguments -> arguments.getArgumentOrEmpty(inputValue.getName()))
                                        .or(() -> Optional.ofNullable(inputValue.getDefaultValue()))
                                        .stream()
                                        .filter(ValueWithVariable::isObject)
                                        .flatMap(valueWithVariable ->
                                                documentManager.getInputValueTypeDefinition(inputValue).asInputObject().getInputValues().stream()
                                                        .flatMap(subInputValue ->
                                                                valueWithVariable.asObject().getValueWithVariableOrEmpty(subInputValue.getName())
                                                                        .or(() -> Optional.ofNullable(subInputValue.getDefaultValue()))
                                                                        .stream()
                                                                        .flatMap(subValueWithVariable -> {
                                                                                    FieldDefinition subFieldDefinition = fieldTypeDefinition.asObject().getField(subInputValue.getName());
                                                                                    if (subValueWithVariable.isEnum()) {
                                                                                        Expression expression;
                                                                                        if (subFieldDefinition.isFunctionField()) {
                                                                                            expression = new Function()
                                                                                                    .withName(subFieldDefinition.getFunctionNameOrError())
                                                                                                    .withParameters(graphqlFieldToColumn(table, subFieldDefinition.getFunctionFieldOrError()));
                                                                                        } else {
                                                                                            expression = graphqlFieldToColumn(table, subFieldDefinition.getName());
                                                                                        }
                                                                                        return Stream.of(
                                                                                                new OrderByElement()
                                                                                                        .withAsc(!subValueWithVariable.asEnum().getValue().equals(INPUT_SORT_NAME_VALUE_DESC))
                                                                                                        .withExpression(expression)
                                                                                        );
                                                                                    } else {
                                                                                        return valueWithVariableToOrderByStream(
                                                                                                subFieldDefinition,
                                                                                                subInputValue,
                                                                                                subValueWithVariable,
                                                                                                level + 1
                                                                                        );
                                                                                    }
                                                                                }
                                                                        )
                                                        )
                                        )
                        )
                        .orElseGet(() ->
                                Stream.of(
                                        new OrderByElement()
                                                .withAsc(!Optional.ofNullable(field.getArguments())
                                                        .map(arguments -> arguments.hasArgument(INPUT_VALUE_LAST_NAME))
                                                        .orElse(false)
                                                )
                                                .withExpression(graphqlFieldToColumn(table, fieldTypeDefinition.asObject().getCursorField().orElseGet(() -> fieldTypeDefinition.asObject().getIDFieldOrError()).getName()))
                                )
                        );
            } else {
                Table withTable = graphqlTypeToTable(fieldDefinition.getMapWithTypeOrError(), level);
                return fieldDefinition.getArgumentOrEmpty(INPUT_VALUE_SORT_NAME)
                        .flatMap(inputValue ->
                                Optional.ofNullable(field.getArguments())
                                        .flatMap(arguments -> arguments.getArgumentOrEmpty(inputValue.getName()))
                                        .or(() -> Optional.ofNullable(inputValue.getDefaultValue()))
                        )
                        .filter(ValueWithVariable::isEnum)
                        .map(ValueWithVariable::asEnum)
                        .map(enumValue ->
                                new OrderByElement()
                                        .withAsc(!enumValue.getValue().equals(INPUT_SORT_NAME_VALUE_DESC))
                                        .withExpression(graphqlFieldToColumn(withTable, fieldDefinition.getMapWithToOrError()))
                        )
                        .stream();
            }
        }
        return Stream.empty();
    }

    protected Stream<OrderByElement> valueWithVariableToOrderByStream(FieldDefinition fieldDefinition, InputValue inputValue, ValueWithVariable valueWithVariable, int level) {
        if (valueWithVariable.isNull()) {
            return Stream.empty();
        }
        Definition fieldTypeDefinition = documentManager.getFieldTypeDefinition(fieldDefinition);
        Table table = typeToTable(fieldTypeDefinition.asObject(), level);
        return documentManager.getInputValueTypeDefinition(inputValue).asInputObject().getInputValues().stream()
                .flatMap(subInputValue ->
                        valueWithVariable.asObject().getValueWithVariableOrEmpty(subInputValue.getName())
                                .or(() -> Optional.ofNullable(subInputValue.getDefaultValue()))
                                .stream()
                                .flatMap(subValueWithVariable -> {
                                            FieldDefinition subFieldDefinition = fieldTypeDefinition.asObject().getField(subInputValue.getName());
                                            if (subValueWithVariable.isEnum()) {
                                                Expression expression;
                                                if (subFieldDefinition.isFunctionField()) {
                                                    expression = new Function()
                                                            .withName(subFieldDefinition.getFunctionNameOrError())
                                                            .withParameters(graphqlFieldToColumn(table, subFieldDefinition.getFunctionFieldOrError()));
                                                } else {
                                                    expression = graphqlFieldToColumn(table, subFieldDefinition.getName());
                                                }
                                                return Stream.of(
                                                        new OrderByElement()
                                                                .withAsc(!subValueWithVariable.asEnum().getValue().equals(INPUT_SORT_NAME_VALUE_DESC))
                                                                .withExpression(expression)
                                                );
                                            } else {
                                                return valueWithVariableToOrderByStream(
                                                        subFieldDefinition,
                                                        subInputValue,
                                                        subValueWithVariable,
                                                        level + 1
                                                );
                                            }
                                        }
                                )
                );
    }

    protected Limit argumentsToLimit(FieldDefinition fieldDefinition, Field field) {
        if (fieldDefinition.getArguments() != null) {
            return fieldDefinition.getArgumentOrEmpty(INPUT_VALUE_FIRST_NAME)
                    .flatMap(inputValue ->
                            Optional.ofNullable(field.getArguments())
                                    .flatMap(arguments -> arguments.getArgumentOrEmpty(inputValue.getName()))
                                    .or(() -> Optional.ofNullable(inputValue.getDefaultValue()))
                    )
                    .or(() ->
                            fieldDefinition.getArgumentOrEmpty(INPUT_VALUE_LAST_NAME)
                                    .flatMap(inputValue ->
                                            Optional.ofNullable(field.getArguments())
                                                    .flatMap(arguments -> arguments.getArgumentOrEmpty(inputValue.getName()))
                                                    .or(() -> Optional.ofNullable(inputValue.getDefaultValue()))
                                    )
                    )
                    .filter(ValueWithVariable::isInt)
                    .map(valueWithVariable ->
                            new Limit()
                                    .withRowCount(leafValueToDBValue(valueWithVariable))
                                    .withOffset(
                                            fieldDefinition.getArgumentOrEmpty(INPUT_VALUE_OFFSET_NAME)
                                                    .flatMap(inputValue ->
                                                            Optional.ofNullable(field.getArguments())
                                                                    .flatMap(arguments -> arguments.getArgumentOrEmpty(inputValue.getName()))
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
