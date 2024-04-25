package io.graphoenix.sql.translator;

import com.google.common.collect.Lists;
import com.google.common.collect.Streams;
import io.graphoenix.core.handler.DocumentManager;
import io.graphoenix.spi.error.GraphQLErrorType;
import io.graphoenix.spi.error.GraphQLErrors;
import io.graphoenix.spi.graphql.Definition;
import io.graphoenix.spi.graphql.common.ValueWithVariable;
import io.graphoenix.spi.graphql.operation.Field;
import io.graphoenix.spi.graphql.type.FieldDefinition;
import io.graphoenix.spi.graphql.type.InputObjectType;
import io.graphoenix.spi.graphql.type.InputValue;
import io.graphoenix.spi.graphql.type.ObjectType;
import io.graphoenix.sql.expression.JsonTableFunction;
import io.graphoenix.sql.utils.DBValueUtil;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import net.sf.jsqlparser.expression.*;
import net.sf.jsqlparser.expression.operators.relational.*;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.schema.Table;
import net.sf.jsqlparser.statement.create.table.ColDataType;
import net.sf.jsqlparser.statement.create.table.ColumnDefinition;
import net.sf.jsqlparser.statement.select.AllColumns;
import net.sf.jsqlparser.statement.select.Join;
import net.sf.jsqlparser.statement.select.ParenthesedSelect;
import net.sf.jsqlparser.statement.select.PlainSelect;
import net.sf.jsqlparser.util.cnfexpression.MultiAndExpression;
import net.sf.jsqlparser.util.cnfexpression.MultiOrExpression;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static io.graphoenix.spi.constant.Hammurabi.*;
import static io.graphoenix.spi.error.GraphQLErrorType.UNSUPPORTED_FIELD_TYPE;
import static io.graphoenix.spi.utils.ValueWithVariableUtil.*;
import static io.graphoenix.sql.utils.DBNameUtil.*;
import static io.graphoenix.sql.utils.DBValueUtil.getValueFromArrayVariable;
import static io.graphoenix.sql.utils.DBValueUtil.leafValueToDBValue;

@ApplicationScoped
public class ArgumentsTranslator {

    private final DocumentManager documentManager;

    @Inject
    public ArgumentsTranslator(DocumentManager documentManager) {
        this.documentManager = documentManager;
    }

    public Optional<Expression> argumentsToWhereExpression(ObjectType objectType, FieldDefinition fieldDefinition, Field field, int level) {
        Definition fieldTypeDefinition = documentManager.getFieldTypeDefinition(fieldDefinition);
        if (fieldTypeDefinition.isObject()) {
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

            List<Expression> expressionList = Streams
                    .concat(
                            inputValueValueWithVariableMap.entrySet().stream()
                                    .flatMap(entry ->
                                            Stream.ofNullable(fieldTypeDefinition.asObject().getField(entry.getKey().getName()))
                                                    .filter(subField -> !subField.isFetchField())
                                                    .flatMap(subField ->
                                                            documentManager.getFieldTypeDefinition(subField).isObject() ?
                                                                    inputValueToWhereExpression(fieldTypeDefinition.asObject(), subField, entry.getKey(), entry.getValue(), level + 1).stream()
                                                                            .map(expression -> existsExpression(selectFromFieldType(fieldTypeDefinition.asObject(), subField, expression, level + 1))) :
                                                                    inputValueToWhereExpression(fieldTypeDefinition.asObject(), subField, entry.getKey(), entry.getValue(), level).stream()
                                                    )
                                    ),
                            inputValueValueWithVariableMap.entrySet().stream()
                                    .filter(entry -> entry.getKey().getName().equals(INPUT_VALUE_EXS_NAME))
                                    .flatMap(entry ->
                                            Stream.of(entry.getValue())
                                                    .filter(ValueWithVariable::isArray)
                                                    .flatMap(valueWithVariable -> valueWithVariable.asArray().getValueWithVariables().stream())
                                                    .flatMap(valueWithVariable -> inputValueToWhereExpression(objectType, fieldDefinition, entry.getKey(), valueWithVariable, level, true).stream())
                                    ),
                            inputValueValueWithVariableMap.entrySet().stream()
                                    .anyMatch(entry ->
                                            entry.getKey().getName().equals(INPUT_VALUE_INCLUDE_DEPRECATED_NAME) &&
                                                    entry.getValue().isBoolean() &&
                                                    entry.getValue().asBoolean().getValue()
                                    ) ?
                                    Stream.empty() :
                                    Stream.of(
                                            new NotEqualsTo()
                                                    .withLeftExpression(graphqlFieldToColumn(fieldTypeDefinition.asObject().getName(), FIELD_DEPRECATED_NAME, level))
                                                    .withRightExpression(new LongValue(1))
                                    )
                    )
                    .collect(Collectors.toList());

            return expressionListToMultipleExpression(expressionList, isOr(field.getArguments()), isNot(field.getArguments()));
        } else {
            return fieldDefinition.getArgumentOrEmpty(INPUT_OPERATOR_INPUT_VALUE_OPR_NAME)
                    .flatMap(inputValue ->
                            Optional.ofNullable(field.getArguments())
                                    .map(arguments ->
                                            arguments.getArgumentOrEmpty(inputValue.getName())
                                                    .filter(ValueWithVariable::isEnum)
                                                    .map(opr -> opr.asEnum().getValue())
                                                    .orElseGet(() ->
                                                            arguments.getArgumentOrEmpty(INPUT_OPERATOR_INPUT_VALUE_VAL_NAME)
                                                                    .map(val -> INPUT_OPERATOR_INPUT_VALUE_EQ)
                                                                    .orElseGet(() ->
                                                                            arguments.getArgumentOrEmpty(INPUT_OPERATOR_INPUT_VALUE_VAL_NAME)
                                                                                    .map(val -> INPUT_OPERATOR_INPUT_VALUE_EQ)
                                                                                    .orElseGet(() ->
                                                                                            arguments.getArgumentOrEmpty(INPUT_OPERATOR_INPUT_VALUE_ARR_NAME)
                                                                                                    .map(val -> INPUT_OPERATOR_INPUT_VALUE_IN)
                                                                                                    .orElseGet(() -> inputValue.getDefaultValue().asEnum().getValue())
                                                                                    )
                                                                    )
                                                    )
                                    )
                    )
                    .flatMap(opr ->
                            Optional.of(field.getArguments())
                                    .flatMap(arguments -> {
                                                if (fieldDefinition.getType().hasList()) {
                                                    Column column = graphqlFieldToColumn(fieldDefinition.getMapWithTypeOrError(), fieldDefinition.getMapWithToOrError(), level);
                                                    return arguments.getArgumentOrEmpty(INPUT_OPERATOR_INPUT_VALUE_VAL_NAME)
                                                            .flatMap(val ->
                                                                    valToExpression(column, opr, val, skipNull(arguments))
                                                            )
                                                            .or(() ->
                                                                    arguments.getArgumentOrEmpty(INPUT_OPERATOR_INPUT_VALUE_ARR_NAME)
                                                                            .flatMap(arr -> arrToExpression(column, opr, fieldDefinition.getArgument(INPUT_OPERATOR_INPUT_VALUE_ARR_NAME), arr, skipNull(arguments)))
                                                            );
                                                } else {
                                                    Column column = graphqlFieldToColumn(objectType.getName(), fieldDefinition.getName(), level);
                                                    return arguments.getArgumentOrEmpty(INPUT_OPERATOR_INPUT_VALUE_VAL_NAME)
                                                            .flatMap(val ->
                                                                    valToExpression(column, opr, val, skipNull(arguments))
                                                            )
                                                            .or(() ->
                                                                    arguments.getArgumentOrEmpty(INPUT_OPERATOR_INPUT_VALUE_ARR_NAME)
                                                                            .flatMap(arr -> arrToExpression(column, opr, fieldDefinition.getArgument(INPUT_OPERATOR_INPUT_VALUE_ARR_NAME), arr, skipNull(arguments)))
                                                            );
                                                }
                                            }
                                    )
                    );
        }
    }

    protected Optional<Expression> inputValueToWhereExpression(ObjectType objectType, FieldDefinition fieldDefinition, InputValue inputValue, ValueWithVariable valueWithVariable, int level) {
        return inputValueToWhereExpression(objectType, fieldDefinition, inputValue, valueWithVariable, level, false);
    }

    protected Optional<Expression> inputValueToWhereExpression(ObjectType objectType, FieldDefinition fieldDefinition, InputValue inputValue, ValueWithVariable valueWithVariable, int level, boolean exs) {
        Definition fieldTypeDefinition = documentManager.getFieldTypeDefinition(fieldDefinition);
        Definition inputValueTypeDefinition = documentManager.getInputValueTypeDefinition(inputValue);
        if (fieldTypeDefinition.isObject()) {
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

            List<Expression> expressionList = Streams
                    .concat(
                            inputValueValueWithVariableMap.entrySet().stream()
                                    .flatMap(entry ->
                                            Stream.ofNullable(fieldTypeDefinition.asObject().getField(entry.getKey().getName()))
                                                    .filter(subField -> !subField.isFetchField())
                                                    .flatMap(subField ->
                                                            documentManager.getFieldTypeDefinition(subField).isObject() ?
                                                                    inputValueToWhereExpression(fieldTypeDefinition.asObject(), subField, entry.getKey(), entry.getValue(), level + 1).stream()
                                                                            .map(expression -> existsExpression(selectFromFieldType(fieldTypeDefinition.asObject(), subField, expression, level + 1))) :
                                                                    inputValueToWhereExpression(fieldTypeDefinition.asObject(), subField, entry.getKey(), entry.getValue(), level).stream()
                                                    )
                                    ),
                            inputValueValueWithVariableMap.entrySet().stream()
                                    .filter(entry -> entry.getKey().getName().equals(INPUT_VALUE_EXS_NAME))
                                    .flatMap(entry ->
                                            Stream.of(entry.getValue())
                                                    .filter(ValueWithVariable::isArray)
                                                    .flatMap(field -> field.asArray().getValueWithVariables().stream())
                                                    .flatMap(field -> inputValueToWhereExpression(objectType, fieldDefinition, entry.getKey(), field, level, true).stream())
                                    ),
                            exs || inputValueValueWithVariableMap.entrySet().stream()
                                    .anyMatch(entry ->
                                            entry.getKey().getName().equals(INPUT_VALUE_INCLUDE_DEPRECATED_NAME) &&
                                                    entry.getValue().isBoolean() &&
                                                    entry.getValue().asBoolean().getValue()
                                    ) ?
                                    Stream.empty() :
                                    Stream.of(
                                            new NotEqualsTo()
                                                    .withLeftExpression(graphqlFieldToColumn(fieldTypeDefinition.asObject().getName(), FIELD_DEPRECATED_NAME, level))
                                                    .withRightExpression(new LongValue(1))
                                    )
                    )
                    .collect(Collectors.toList());

            return expressionListToMultipleExpression(expressionList, isOr(valueWithVariable), isNot(valueWithVariable));
        } else {
            InputObjectType inputObject = documentManager.getInputValueTypeDefinition(inputValue).asInputObject();
            return inputObject.getInputValueOrEmpty(INPUT_OPERATOR_INPUT_VALUE_OPR_NAME)
                    .flatMap(fieldInput ->
                            Optional.ofNullable(valueWithVariable)
                                    .filter(ValueWithVariable::isObject)
                                    .map(ValueWithVariable::asObject)
                                    .map(objectValueWithVariable ->
                                            objectValueWithVariable.getValueWithVariableOrEmpty(fieldInput.getName())
                                                    .filter(ValueWithVariable::isEnum)
                                                    .map(opr -> opr.asEnum().getValue())
                                                    .orElseGet(() ->
                                                            objectValueWithVariable.getValueWithVariableOrEmpty(INPUT_OPERATOR_INPUT_VALUE_VAL_NAME)
                                                                    .map(val -> INPUT_OPERATOR_INPUT_VALUE_EQ)
                                                                    .orElseGet(() ->
                                                                            objectValueWithVariable.getValueWithVariableOrEmpty(INPUT_OPERATOR_INPUT_VALUE_VAL_NAME)
                                                                                    .map(val -> INPUT_OPERATOR_INPUT_VALUE_EQ)
                                                                                    .orElseGet(() ->
                                                                                            objectValueWithVariable.getValueWithVariableOrEmpty(INPUT_OPERATOR_INPUT_VALUE_ARR_NAME)
                                                                                                    .map(val -> INPUT_OPERATOR_INPUT_VALUE_IN)
                                                                                                    .orElseGet(() -> fieldInput.getDefaultValue().asEnum().getValue())
                                                                                    )
                                                                    )
                                                    )
                                    )
                    )
                    .flatMap(opr ->
                            Optional.of(valueWithVariable)
                                    .filter(ValueWithVariable::isObject)
                                    .map(ValueWithVariable::asObject)
                                    .flatMap(objectValueWithVariable -> {
                                                if (fieldDefinition.getType().hasList()) {
                                                    Column column = graphqlFieldToColumn(fieldDefinition.getMapWithTypeOrError(), fieldDefinition.getMapWithToOrError(), level);
                                                    return objectValueWithVariable.getValueWithVariableOrEmpty(INPUT_OPERATOR_INPUT_VALUE_VAL_NAME)
                                                            .flatMap(val ->
                                                                    valToExpression(column, opr, val, skipNull(objectValueWithVariable))
                                                            )
                                                            .or(() ->
                                                                    objectValueWithVariable.getValueWithVariableOrEmpty(INPUT_OPERATOR_INPUT_VALUE_ARR_NAME)
                                                                            .flatMap(arr -> arrToExpression(column, opr, inputObject.getInputValue(INPUT_OPERATOR_INPUT_VALUE_ARR_NAME), arr, skipNull(objectValueWithVariable)))
                                                            )
                                                            .map(expression -> existsExpression(selectFromFieldType(objectType, fieldDefinition, expression, level)));
                                                } else {
                                                    Column column = graphqlFieldToColumn(objectType.getName(), fieldDefinition.getName(), level);
                                                    return objectValueWithVariable.getValueWithVariableOrEmpty(INPUT_OPERATOR_INPUT_VALUE_VAL_NAME)
                                                            .flatMap(val ->
                                                                    valToExpression(column, opr, val, skipNull(objectValueWithVariable))
                                                            )
                                                            .or(() ->
                                                                    objectValueWithVariable.getValueWithVariableOrEmpty(INPUT_OPERATOR_INPUT_VALUE_ARR_NAME)
                                                                            .flatMap(arr -> arrToExpression(column, opr, inputObject.getInputValue(INPUT_OPERATOR_INPUT_VALUE_ARR_NAME), arr, skipNull(objectValueWithVariable)))
                                                            );
                                                }
                                            }
                                    )
                    );
        }
    }

    protected Optional<Expression> expressionListToMultipleExpression(List<Expression> expressionList, boolean isOr, boolean isNot) {
        Expression expression;
        if (expressionList.isEmpty()) {
            return Optional.empty();
        } else if (expressionList.size() == 1) {
            expression = expressionList.get(0);
        } else {
            if (isOr) {
                expression = new MultiOrExpression(expressionList);
            } else {
                expression = new MultiAndExpression(expressionList);
            }
        }
        if (isNot) {
            return Optional.of(new NotExpression(expression));
        }
        return Optional.of(expression);
    }

    protected PlainSelect selectFromFieldType(ObjectType objectType, FieldDefinition fieldDefinition, Expression expression, int level) {
        Definition fieldTypeDefinition = documentManager.getFieldTypeDefinition(fieldDefinition);
        if (fieldTypeDefinition.isObject()) {
            Table parentTable = typeToTable(objectType, level - 1);
            Table table = typeToTable(fieldTypeDefinition.asObject(), level);

            PlainSelect plainSelect = new PlainSelect()
                    .addSelectItems(new AllColumns())
                    .withFromItem(table);

            if (documentManager.isOperationType(objectType)) {
                return plainSelect.withWhere(expression);
            }

            if (fieldDefinition.hasMapWith()) {
                Table withTable = graphqlTypeToTable(fieldDefinition.getMapWithTypeOrError(), level);
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
                                new MultiAndExpression(
                                        Arrays.asList(
                                                new EqualsTo()
                                                        .withLeftExpression(graphqlFieldToColumn(withTable, fieldDefinition.getMapWithFromOrError()))
                                                        .withRightExpression(graphqlFieldToColumn(parentTable, fieldDefinition.getMapFromOrError())),
                                                expression
                                        )
                                )
                        );
            } else {
                return plainSelect
                        .withWhere(
                                new MultiAndExpression(
                                        Arrays.asList(
                                                new EqualsTo()
                                                        .withLeftExpression(graphqlFieldToColumn(table, fieldDefinition.getMapToOrError()))
                                                        .withRightExpression(graphqlFieldToColumn(parentTable, fieldDefinition.getMapFromOrError())),
                                                expression
                                        )
                                )
                        );
            }
        } else {
            Table table = graphqlTypeToTable(fieldDefinition.getMapWithTypeOrError(), level);
            return new PlainSelect()
                    .addSelectItems(new AllColumns())
                    .withFromItem(table)
                    .withWhere(expression);
        }
    }

    protected Optional<Expression> valToExpression(Column column, String opr, ValueWithVariable val, boolean skipNull) {
        if (skipNull && val.isNull()) {
            return Optional.empty();
        }
        Expression value = leafValueToDBValue(val);
        Expression where;
        switch (opr) {
            case INPUT_OPERATOR_INPUT_VALUE_EQ:
                where = new EqualsTo()
                        .withLeftExpression(column)
                        .withRightExpression(value);
                break;
            case INPUT_OPERATOR_INPUT_VALUE_NEQ:
                where = new NotEqualsTo()
                        .withLeftExpression(column)
                        .withRightExpression(value);
                break;
            case INPUT_OPERATOR_INPUT_VALUE_LK:
                where = new LikeExpression()
                        .withLeftExpression(column)
                        .withRightExpression(value);
                break;
            case INPUT_OPERATOR_INPUT_VALUE_NLK:
                where = new LikeExpression()
                        .withNot(true)
                        .withLeftExpression(column)
                        .withRightExpression(value);
                break;
            case INPUT_OPERATOR_INPUT_VALUE_GT:
                where = new GreaterThan()
                        .withLeftExpression(column)
                        .withRightExpression(value);
                break;
            case INPUT_OPERATOR_INPUT_VALUE_GTE:
                where = new GreaterThanEquals()
                        .withLeftExpression(column)
                        .withRightExpression(value);
                break;
            case INPUT_OPERATOR_INPUT_VALUE_LT:
                where = new MinorThan()
                        .withLeftExpression(column)
                        .withRightExpression(value);
                break;
            case INPUT_OPERATOR_INPUT_VALUE_LTE:
                where = new MinorThanEquals()
                        .withLeftExpression(column)
                        .withRightExpression(value);
                break;
            case INPUT_OPERATOR_INPUT_VALUE_NIL:
                where = new IsNullExpression()
                        .withLeftExpression(column);
                break;
            case INPUT_OPERATOR_INPUT_VALUE_NNIL:
                where = new IsNullExpression()
                        .withNot(true)
                        .withLeftExpression(column);
                break;
            default:
                throw new GraphQLErrors(GraphQLErrorType.UNSUPPORTED_OPERATOR.bind(opr));
        }
        if (skipNull) {
            return Optional.of(skipNullExpression(value, where));
        }
        return Optional.of(where);
    }

    protected Optional<Expression> arrToExpression(Column column, String opr, InputValue inputValue, ValueWithVariable arr, boolean skipNull) {
        if (skipNull && arr.isNull()) {
            return Optional.empty();
        }
        if (arr.isVariable()) {
            Expression where;
            switch (opr) {
                case INPUT_OPERATOR_INPUT_VALUE_IN:
                    where = new InExpression()
                            .withLeftExpression(column)
                            .withRightExpression(selectValueFromJsonArray(inputValue, arr));
                    break;
                case INPUT_OPERATOR_INPUT_VALUE_NIN:
                    where = new InExpression()
                            .withNot(true)
                            .withLeftExpression(column)
                            .withRightExpression(selectValueFromJsonArray(inputValue, arr));
                    break;
                case INPUT_OPERATOR_INPUT_VALUE_BT:
                    where = new MultiAndExpression(
                            Arrays.asList(
                                    new GreaterThanEquals()
                                            .withLeftExpression(column)
                                            .withRightExpression(getValueFromArrayVariable(arr, 0)),
                                    new MinorThanEquals()
                                            .withLeftExpression(column)
                                            .withRightExpression(getValueFromArrayVariable(arr, 1))
                            )
                    );
                    break;
                case INPUT_OPERATOR_INPUT_VALUE_NBT:
                    where = new MultiAndExpression(
                            Arrays.asList(
                                    new MinorThanEquals()
                                            .withLeftExpression(column)
                                            .withRightExpression(getValueFromArrayVariable(arr, 0)),
                                    new GreaterThanEquals()
                                            .withLeftExpression(column)
                                            .withRightExpression(getValueFromArrayVariable(arr, 1))
                            )
                    );
                    break;
                default:
                    throw new GraphQLErrors(GraphQLErrorType.UNSUPPORTED_OPERATOR.bind(opr));
            }
            if (skipNull) {
                return Optional.of(skipNullExpression(leafValueToDBValue(arr), where));
            }
            return Optional.of(where);
        } else {
            List<ValueWithVariable> valList = arr.isNull() ?
                    Collections.singletonList(arr) :
                    arr.asArray().getValueWithVariables();

            Expression value = new Parenthesis(new ExpressionList<>(valList.stream().map(DBValueUtil::leafValueToDBValue).collect(Collectors.toList())));
            Expression where;
            switch (opr) {
                case INPUT_OPERATOR_INPUT_VALUE_IN:
                    where = new InExpression()
                            .withLeftExpression(column)
                            .withRightExpression(value);
                    break;
                case INPUT_OPERATOR_INPUT_VALUE_NIN:
                    where = new InExpression()
                            .withNot(true)
                            .withLeftExpression(column)
                            .withRightExpression(value);
                    break;
                case INPUT_OPERATOR_INPUT_VALUE_BT:
                    where = new MultiOrExpression(
                            Lists.partition(valList, 2).stream()
                                    .map(valueWithVariables -> {
                                                if (valueWithVariables.size() == 2) {
                                                    return new MultiAndExpression(
                                                            Arrays.asList(
                                                                    new GreaterThanEquals()
                                                                            .withLeftExpression(column)
                                                                            .withRightExpression(leafValueToDBValue(valueWithVariables.get(0))),
                                                                    new MinorThanEquals()
                                                                            .withLeftExpression(column)
                                                                            .withRightExpression(leafValueToDBValue(valueWithVariables.get(1)))
                                                            )
                                                    );
                                                } else {
                                                    return new GreaterThanEquals()
                                                            .withLeftExpression(column)
                                                            .withRightExpression(leafValueToDBValue(valueWithVariables.get(0)));
                                                }
                                            }
                                    )
                                    .collect(Collectors.toList())
                    );
                    break;
                case INPUT_OPERATOR_INPUT_VALUE_NBT:
                    where = new MultiOrExpression(
                            Lists.partition(valList, 2).stream()
                                    .map(valueWithVariables -> {
                                                if (valueWithVariables.size() == 2) {
                                                    return new MultiAndExpression(
                                                            Arrays.asList(
                                                                    new MinorThanEquals()
                                                                            .withLeftExpression(column)
                                                                            .withRightExpression(leafValueToDBValue(valueWithVariables.get(0))),
                                                                    new GreaterThanEquals()
                                                                            .withLeftExpression(column)
                                                                            .withRightExpression(leafValueToDBValue(valueWithVariables.get(1)))
                                                            )
                                                    );
                                                } else {
                                                    return new MinorThanEquals()
                                                            .withLeftExpression(column)
                                                            .withRightExpression(leafValueToDBValue(valueWithVariables.get(0)));
                                                }
                                            }
                                    )
                                    .collect(Collectors.toList())
                    );
                    break;
                default:
                    throw new GraphQLErrors(GraphQLErrorType.UNSUPPORTED_OPERATOR.bind(opr));
            }
            return Optional.of(where);
        }
    }

    protected Expression skipNullExpression(Expression value, Expression where) {
        return new Function().withName("IF").withParameters(new IsNullExpression().withLeftExpression(value), new LongValue(1), where);
    }

    protected ParenthesedSelect selectValueFromJsonArray(InputValue inputValue, ValueWithVariable valueWithVariable) {
        Definition inputValueTypeDefinition = documentManager.getInputValueTypeDefinition(inputValue);
        ColDataType colDataType = new ColDataType();
        if (inputValueTypeDefinition.isEnum()) {
            colDataType.setDataType("INT");
        } else if (inputValueTypeDefinition.isScalar()) {
            switch (inputValueTypeDefinition.getName()) {
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
            }
        } else {
            throw new GraphQLErrors(UNSUPPORTED_FIELD_TYPE.bind(inputValueTypeDefinition.getName()));
        }
        return new ParenthesedSelect()
                .withSelect(
                        new PlainSelect()
                                .addSelectItem(new AllColumns())
                                .withFromItem(
                                        new JsonTableFunction()
                                                .withJson(leafValueToDBValue(valueWithVariable))
                                                .withPath(new StringValue("$[*]"))
                                                .withColumnDefinitions(
                                                        new ColumnDefinition()
                                                                .withColumnName(graphqlFieldNameToColumnName(inputValue.getName()))
                                                                .withColDataType(colDataType)
                                                                .addColumnSpecs("PATH", "'$'")
                                                )
                                                .withAlias(new Alias(inputValue.getName()))
                                )
                );
    }

    protected ExistsExpression existsExpression(PlainSelect plainSelect) {
        return new ExistsExpression()
                .withRightExpression(
                        new ParenthesedSelect()
                                .withSelect(plainSelect)
                );
    }

    protected Table typeToTable(ObjectType objectType, int level) {
        return graphqlTypeToTable(objectType.getName(), level);
    }
}
