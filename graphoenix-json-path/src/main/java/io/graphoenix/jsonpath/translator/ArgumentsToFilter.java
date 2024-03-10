package io.graphoenix.jsonpath.translator;

import com.google.common.collect.Lists;
import com.google.common.collect.Streams;
import io.graphoenix.core.handler.DocumentManager;
import io.graphoenix.jsonpath.expression.*;
import io.graphoenix.jsonpath.expression.operators.*;
import io.graphoenix.spi.error.GraphQLErrors;
import io.graphoenix.spi.graphql.Definition;
import io.graphoenix.spi.graphql.common.ValueWithVariable;
import io.graphoenix.spi.graphql.operation.Field;
import io.graphoenix.spi.graphql.type.FieldDefinition;
import io.graphoenix.spi.graphql.type.InputObjectType;
import io.graphoenix.spi.graphql.type.InputValue;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static io.graphoenix.jsonpath.utils.JsonValueUtil.valueToJsonValue;
import static io.graphoenix.spi.constant.Hammurabi.*;
import static io.graphoenix.spi.error.GraphQLErrorType.UNSUPPORTED_VALUE;
import static io.graphoenix.spi.utils.ValueWithVariableUtil.*;

@ApplicationScoped
public class ArgumentsToFilter {

    private final DocumentManager documentManager;

    @Inject
    public ArgumentsToFilter(DocumentManager documentManager) {
        this.documentManager = documentManager;
    }

    public Optional<Expression> argumentsToMultipleExpression(FieldDefinition fieldDefinition, Field field) {
        String path = "@";
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

            List<Expression> expressionList = Streams
                    .concat(
                            inputValueValueWithVariableMap.entrySet().stream()
                                    .flatMap(entry ->
                                            Stream.ofNullable(fieldTypeDefinition.asObject().getField(entry.getKey().getName()))
                                                    .filter(subField -> !subField.isFetchField())
                                                    .flatMap(subField ->
                                                            inputValueToMultipleExpression(subField, entry.getKey(), entry.getValue(), path + "." + subField.getName()).stream()
                                                    )
                                    ),
                            inputValueValueWithVariableMap.entrySet().stream()
                                    .filter(entry -> entry.getKey().getName().equals(INPUT_VALUE_EXS_NAME))
                                    .flatMap(entry ->
                                            Stream.of(entry.getValue())
                                                    .filter(ValueWithVariable::isArray)
                                                    .flatMap(valueWithVariable -> valueWithVariable.asArray().getValueWithVariables().stream())
                                                    .flatMap(valueWithVariable -> inputValueToMultipleExpression(fieldDefinition, entry.getKey(), valueWithVariable, path, true).stream())
                                    ),
                            inputValueValueWithVariableMap.entrySet().stream()
                                    .anyMatch(entry ->
                                            entry.getKey().getName().equals(INPUT_VALUE_INCLUDE_DEPRECATED_NAME) &&
                                                    entry.getValue().isBoolean() &&
                                                    entry.getValue().asBoolean().getValue()
                                    ) ?
                                    Stream.empty() :
                                    Stream.of(new NotEqualsTo(path + "." + FIELD_DEPRECATED_NAME, new BooleanValue(true)))
                    )
                    .collect(Collectors.toList());

            return expressionListToMultipleExpression(expressionList, isOr(field.getArguments()), isNot(field.getArguments()), true);
        } else {
            return fieldDefinition.getArgument(INPUT_OPERATOR_INPUT_VALUE_OPR_NAME)
                    .flatMap(inputValue ->
                            Optional.ofNullable(field.getArguments())
                                    .map(arguments ->
                                            arguments.getArgument(inputValue.getName())
                                                    .filter(ValueWithVariable::isEnum)
                                                    .map(opr -> opr.asEnum().getValue())
                                                    .orElseGet(() ->
                                                            arguments.getArgument(INPUT_OPERATOR_INPUT_VALUE_VAL_NAME)
                                                                    .map(val -> INPUT_OPERATOR_INPUT_VALUE_EQ)
                                                                    .orElseGet(() ->
                                                                            arguments.getArgument(INPUT_OPERATOR_INPUT_VALUE_VAL_NAME)
                                                                                    .map(val -> INPUT_OPERATOR_INPUT_VALUE_EQ)
                                                                                    .orElseGet(() ->
                                                                                            arguments.getArgument(INPUT_OPERATOR_INPUT_VALUE_ARR_NAME)
                                                                                                    .map(val -> INPUT_OPERATOR_INPUT_VALUE_IN)
                                                                                                    .orElseGet(() -> inputValue.getDefaultValue().asEnum().getValue())
                                                                                    )
                                                                    )
                                                    )
                                    )
                    )
                    .flatMap(opr ->
                            Optional.of(field.getArguments())
                                    .flatMap(arguments ->
                                            arguments.getArgument(INPUT_OPERATOR_INPUT_VALUE_VAL_NAME)
                                                    .flatMap(val ->
                                                            operatorToExpression(path + "." + fieldDefinition.getName(), opr, val, skipNull(arguments))
                                                    )
                                                    .or(() ->
                                                            arguments.getArgument(INPUT_OPERATOR_INPUT_VALUE_ARR_NAME)
                                                                    .flatMap(arr -> operatorToExpression(path + "." + fieldDefinition.getName(), opr, arr, skipNull(arguments)))
                                                    )
                                    )
                    );
        }
    }

    protected Optional<Expression> inputValueToMultipleExpression(FieldDefinition fieldDefinition, InputValue inputValue, ValueWithVariable valueWithVariable, String path) {
        return inputValueToMultipleExpression(fieldDefinition, inputValue, valueWithVariable, path, false);
    }

    protected Optional<Expression> inputValueToMultipleExpression(FieldDefinition fieldDefinition, InputValue inputValue, ValueWithVariable valueWithVariable, String path, boolean exs) {
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

            List<Expression> expressionList = Streams
                    .concat(
                            inputValueValueWithVariableMap.entrySet().stream()
                                    .flatMap(entry ->
                                            Stream.ofNullable(fieldTypeDefinition.asObject().getField(entry.getKey().getName()))
                                                    .filter(subField -> !subField.isFetchField())
                                                    .flatMap(subField ->
                                                            inputValueToMultipleExpression(subField, entry.getKey(), entry.getValue(), path + "." + subField.getName()).stream()
                                                    )
                                    ),
                            inputValueValueWithVariableMap.entrySet().stream()
                                    .filter(entry -> entry.getKey().getName().equals(INPUT_VALUE_EXS_NAME))
                                    .flatMap(entry ->
                                            Stream.of(entry.getValue())
                                                    .filter(ValueWithVariable::isArray)
                                                    .flatMap(field -> field.asArray().getValueWithVariables().stream())
                                                    .flatMap(field -> inputValueToMultipleExpression(fieldDefinition, entry.getKey(), field, path, true).stream())
                                    ),
                            exs || inputValueValueWithVariableMap.entrySet().stream()
                                    .anyMatch(entry ->
                                            entry.getKey().getName().equals(INPUT_VALUE_INCLUDE_DEPRECATED_NAME) &&
                                                    entry.getValue().isBoolean() &&
                                                    entry.getValue().asBoolean().getValue()
                                    ) ?
                                    Stream.empty() :
                                    Stream.of(new NotEqualsTo(path + "." + FIELD_DEPRECATED_NAME, new BooleanValue(true)))
                    )
                    .collect(Collectors.toList());

            return expressionListToMultipleExpression(expressionList, isOr(valueWithVariable), isNot(valueWithVariable));
        } else {
            InputObjectType inputObject = documentManager.getInputValueTypeDefinition(inputValue).asInputObject();
            return inputObject.getInputValue(INPUT_OPERATOR_INPUT_VALUE_OPR_NAME)
                    .flatMap(fieldInput ->
                            Optional.ofNullable(valueWithVariable)
                                    .filter(ValueWithVariable::isObject)
                                    .map(ValueWithVariable::asObject)
                                    .map(objectValueWithVariable ->
                                            objectValueWithVariable.getValueWithVariable(fieldInput.getName())
                                                    .filter(ValueWithVariable::isEnum)
                                                    .map(opr -> opr.asEnum().getValue())
                                                    .orElseGet(() ->
                                                            objectValueWithVariable.getValueWithVariable(INPUT_OPERATOR_INPUT_VALUE_VAL_NAME)
                                                                    .map(val -> INPUT_OPERATOR_INPUT_VALUE_EQ)
                                                                    .orElseGet(() ->
                                                                            objectValueWithVariable.getValueWithVariable(INPUT_OPERATOR_INPUT_VALUE_VAL_NAME)
                                                                                    .map(val -> INPUT_OPERATOR_INPUT_VALUE_EQ)
                                                                                    .orElseGet(() ->
                                                                                            objectValueWithVariable.getValueWithVariable(INPUT_OPERATOR_INPUT_VALUE_ARR_NAME)
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
                                    .flatMap(objectValueWithVariable ->
                                            objectValueWithVariable.getValueWithVariable(INPUT_OPERATOR_INPUT_VALUE_VAL_NAME)
                                                    .flatMap(val ->
                                                            operatorToExpression(path, opr, val, skipNull(objectValueWithVariable))
                                                    )
                                                    .or(() ->
                                                            objectValueWithVariable.getValueWithVariable(INPUT_OPERATOR_INPUT_VALUE_ARR_NAME)
                                                                    .flatMap(arr -> operatorToExpression(path, opr, arr, skipNull(objectValueWithVariable)))
                                                    )
                                    )
                    );
        }
    }

    protected Optional<Expression> expressionListToMultipleExpression(List<Expression> expressionList, boolean isOr, boolean isNot) {
        return expressionListToMultipleExpression(expressionList, isOr, isNot, false);
    }

    protected Optional<Expression> expressionListToMultipleExpression(List<Expression> expressionList, boolean isOr, boolean isNot, boolean isRoot) {
        Expression expression;
        if (expressionList.isEmpty()) {
            return Optional.empty();
        } else if (!isRoot && expressionList.size() == 1) {
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

    private Optional<Expression> operatorToExpression(String path, String opr, ValueWithVariable val, boolean skipNull) {
        if (skipNull && val.isNull()) {
            return Optional.empty();
        }
        Expression expression;
        switch (opr) {
            case INPUT_OPERATOR_INPUT_VALUE_EQ:
                expression = new EqualsTo(path, valueToJsonValue(val));
                break;
            case INPUT_OPERATOR_INPUT_VALUE_NEQ:
                expression = new NotEqualsTo(path, valueToJsonValue(val));
                break;
            case INPUT_OPERATOR_INPUT_VALUE_LK:
                expression = new Like(path, val.asString().getValue());
                break;
            case INPUT_OPERATOR_INPUT_VALUE_NLK:
                expression = new NotLike(path, val.asString().getValue());
                break;
            case INPUT_OPERATOR_INPUT_VALUE_GT:
                expression = new GreaterThan(path, valueToJsonValue(val));
                break;
            case INPUT_OPERATOR_INPUT_VALUE_GTE:
                expression = new GreaterThanEquals(path, valueToJsonValue(val));
                break;
            case INPUT_OPERATOR_INPUT_VALUE_LT:
                expression = new MinorThan(path, valueToJsonValue(val));
                break;
            case INPUT_OPERATOR_INPUT_VALUE_LTE:
                expression = new MinorThanEquals(path, valueToJsonValue(val));
                break;
            case INPUT_OPERATOR_INPUT_VALUE_NIL:
                expression = new IsNullExpression(path);
                break;
            case INPUT_OPERATOR_INPUT_VALUE_NNIL:
                expression = new NotNullExpression(path);
                break;
            case INPUT_OPERATOR_INPUT_VALUE_IN:
                expression = new InExpression(path, valueToJsonValue(val));
                break;
            case INPUT_OPERATOR_INPUT_VALUE_NIN:
                expression = new NotInExpression(path, valueToJsonValue(val));
                break;
            case INPUT_OPERATOR_INPUT_VALUE_BT:
                expression = new MultiOrExpression(
                        Lists.partition(val.asArray().getValueWithVariables(), 2).stream()
                                .map(valueWithVariables -> {
                                            if (valueWithVariables.size() == 2) {
                                                return new MultiAndExpression(
                                                        Arrays.asList(
                                                                new GreaterThanEquals(path, valueToJsonValue(valueWithVariables.get(0))),
                                                                new MinorThanEquals(path, valueToJsonValue(valueWithVariables.get(1)))
                                                        )
                                                );
                                            } else {
                                                return new GreaterThanEquals(path, valueToJsonValue(valueWithVariables.get(0)));
                                            }
                                        }
                                )
                                .collect(Collectors.toList())
                );
                break;
            case INPUT_OPERATOR_INPUT_VALUE_NBT:
                expression = new MultiOrExpression(
                        Lists.partition(val.asArray().getValueWithVariables(), 2).stream()
                                .map(valueWithVariables -> {
                                            if (valueWithVariables.size() == 2) {
                                                return new MultiAndExpression(
                                                        Arrays.asList(
                                                                new MinorThanEquals(path, valueToJsonValue(valueWithVariables.get(0))),
                                                                new GreaterThanEquals(path, valueToJsonValue(valueWithVariables.get(1)))
                                                        )
                                                );
                                            } else {
                                                return new MinorThanEquals(path, valueToJsonValue(valueWithVariables.get(0)));
                                            }
                                        }
                                )
                                .collect(Collectors.toList())
                );
                break;
            default:
                throw new GraphQLErrors(UNSUPPORTED_VALUE.bind(opr));
        }
        return Optional.of(expression);
    }
}

