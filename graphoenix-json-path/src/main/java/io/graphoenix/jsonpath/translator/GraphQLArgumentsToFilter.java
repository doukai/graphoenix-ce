package io.graphoenix.jsonpath.translator;

import com.google.common.collect.Lists;
import com.google.common.collect.Streams;
import graphql.parser.antlr.GraphqlParser;
import io.graphoenix.core.handler.DocumentManager;
import io.graphoenix.jsonpath.expression.Expression;
import io.graphoenix.jsonpath.expression.MultiAndExpression;
import io.graphoenix.jsonpath.expression.MultiOrExpression;
import io.graphoenix.jsonpath.expression.operators.*;
import io.graphoenix.spi.error.GraphQLErrors;
import io.graphoenix.spi.graphql.Definition;
import io.graphoenix.spi.graphql.common.ValueWithVariable;
import io.graphoenix.spi.graphql.operation.Field;
import io.graphoenix.spi.graphql.type.FieldDefinition;
import io.graphoenix.spi.graphql.type.InputValue;
import io.graphoenix.spi.graphql.type.ObjectType;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static io.graphoenix.jsonpath.utils.JsonValueUtil.valueToJsonValue;
import static io.graphoenix.spi.constant.Hammurabi.*;
import static io.graphoenix.spi.error.GraphQLErrorType.UNSUPPORTED_VALUE;
import static io.graphoenix.spi.utils.ValueWithVariableUtil.*;
import static io.graphoenix.spi.utils.ValueWithVariableUtil.skipNull;

@ApplicationScoped
public class GraphQLArgumentsToFilter {

    private final DocumentManager documentManager;

    @Inject
    public GraphQLArgumentsToFilter(DocumentManager documentManager) {
        this.documentManager = documentManager;
    }

    public Optional<Expression> argumentsToMultipleExpression(ObjectType objectType, FieldDefinition fieldDefinition, Field field) {
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
                                                            documentManager.getFieldTypeDefinition(subField).isObject() ?
                                                                    inputValueToWhereExpression(fieldTypeDefinition.asObject(), subField, entry.getKey(), entry.getValue(), path + "." + subField.getName()).stream() :
                                                                    inputValueToWhereExpression(fieldTypeDefinition.asObject(), subField, entry.getKey(), entry.getValue(), path).stream()
                                                    )
                                    ),
                            inputValueValueWithVariableMap.entrySet().stream()
                                    .filter(entry -> entry.getKey().getName().equals(INPUT_VALUE_EXS_NAME))
                                    .flatMap(entry ->
                                            Stream.of(entry.getValue())
                                                    .filter(ValueWithVariable::isArray)
                                                    .flatMap(valueWithVariable -> valueWithVariable.asArray().getValueWithVariables().stream())
                                                    .flatMap(valueWithVariable -> inputValueToWhereExpression(objectType, fieldDefinition, entry.getKey(), valueWithVariable, path, true).stream())
                                    ),
                            inputValueValueWithVariableMap.entrySet().stream()
                                    .anyMatch(entry ->
                                            entry.getKey().getName().equals(INPUT_VALUE_INCLUDE_DEPRECATED_NAME) &&
                                                    entry.getValue().isBoolean() &&
                                                    entry.getValue().asBoolean().getValue()
                                    ) ?
                                    Stream.empty() :
                                    Stream.of(
                                            new IsNullExpression(FIELD_DEPRECATED_NAME)
                                    )
                    )
                    .collect(Collectors.toList());

            return expressionListToMultipleExpression(expressionList, isOr(field.getArguments()), isNot(field.getArguments()));

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
                                                            operatorToExpression(path, opr, val, skipNull(arguments))
                                                    )
                                                    .or(() ->
                                                            arguments.getArgument(INPUT_OPERATOR_INPUT_VALUE_ARR_NAME)
                                                                    .flatMap(arr -> operatorToExpression(path, opr, arr, skipNull(arguments)))
                                                    )
                                    )
                    );
        }

        Stream<Expression> expressionStream = argumentsDefinitionContext.inputValueDefinition().stream()
                .filter(inputValueDefinitionContext -> Arrays.stream(EXCLUDE_INPUT).noneMatch(name -> name.equals(inputValueDefinitionContext.name().getText())))
                .flatMap(inputValueDefinitionContext ->
                        documentManager.getArgumentFromInputValueDefinition(argumentsContext, inputValueDefinitionContext)
                                .map(argumentContext ->
                                        objectValueWithVariableContextToExpression(inputValueDefinitionContext.name().getText(), argumentContext.valueWithVariable(), inputValueDefinitionContext)
                                )
                                .or(() ->
                                        Optional.ofNullable(inputValueDefinitionContext.defaultValue())
                                                .map(defaultValueContext -> objectValueContextToExpression(inputValueDefinitionContext.name().getText(), defaultValueContext.value(), inputValueDefinitionContext))
                                )
                                .orElse(Stream.empty())
                );

        if (hasOrConditional(argumentsContext, argumentsDefinitionContext)) {
            return Optional.of(new MultiOrExpression(expressionStream.collect(Collectors.toList())));
        } else {
            return Optional.of(new MultiAndExpression(expressionStream.collect(Collectors.toList())));
        }
    }

    protected Optional<Expression> objectValueWithVariableToMultipleExpression(GraphqlParser.InputObjectTypeDefinitionContext inputObjectTypeDefinitionContext,
                                                                               GraphqlParser.ObjectValueWithVariableContext objectValueWithVariableContext,
                                                                               String path) {
        if (objectValueWithVariableContext == null) {
            return Optional.empty();
        }

        Stream<Expression> expressionStream = inputObjectTypeDefinitionContext.inputObjectValueDefinitions().inputValueDefinition().stream()
                .filter(inputValueDefinitionContext -> Arrays.stream(EXCLUDE_INPUT).noneMatch(name -> name.equals(inputValueDefinitionContext.name().getText())))
                .flatMap(inputValueDefinitionContext ->
                        documentManager.getObjectFieldWithVariableFromInputValueDefinition(objectValueWithVariableContext, inputValueDefinitionContext)
                                .map(objectFieldWithVariableContext ->
                                        objectValueWithVariableContextToExpression(path + "." + inputValueDefinitionContext.name().getText(), objectFieldWithVariableContext.valueWithVariable(), inputValueDefinitionContext)
                                )
                                .or(() ->
                                        Optional.ofNullable(inputValueDefinitionContext.defaultValue())
                                                .map(defaultValueContext -> objectValueContextToExpression(path + "." + inputValueDefinitionContext.name().getText(), defaultValueContext.value(), inputValueDefinitionContext))
                                )
                                .orElse(Stream.empty())
                );

        if (hasOrConditional(objectValueWithVariableContext, inputObjectTypeDefinitionContext)) {
            return Optional.of(new MultiOrExpression(expressionStream.collect(Collectors.toList())));
        } else {
            return Optional.of(new MultiAndExpression(expressionStream.collect(Collectors.toList())));
        }
    }

    protected Optional<Expression> objectValueToMultipleExpression(GraphqlParser.InputObjectTypeDefinitionContext inputObjectTypeDefinitionContext,
                                                                   GraphqlParser.ObjectValueContext objectValueContext,
                                                                   String path) {
        if (objectValueContext == null) {
            return Optional.empty();
        }

        Stream<Expression> expressionStream = inputObjectTypeDefinitionContext.inputObjectValueDefinitions().inputValueDefinition().stream()
                .filter(inputValueDefinitionContext -> Arrays.stream(EXCLUDE_INPUT).noneMatch(name -> name.equals(inputValueDefinitionContext.name().getText())))
                .flatMap(inputValueDefinitionContext ->
                        documentManager.getObjectFieldFromInputValueDefinition(objectValueContext, inputValueDefinitionContext)
                                .map(objectFieldContext ->
                                        objectValueContextToExpression(path + "." + inputValueDefinitionContext.name().getText(), objectFieldContext.value(), inputValueDefinitionContext)
                                )
                                .or(() ->
                                        Optional.ofNullable(inputValueDefinitionContext.defaultValue())
                                                .map(defaultValueContext -> objectValueContextToExpression(path + "." + inputValueDefinitionContext.name().getText(), defaultValueContext.value(), inputValueDefinitionContext))
                                )
                                .orElse(Stream.empty())
                );

        if (hasOrConditional(objectValueContext, inputObjectTypeDefinitionContext)) {
            return Optional.of(new MultiOrExpression(expressionStream.collect(Collectors.toList())));
        } else {
            return Optional.of(new MultiAndExpression(expressionStream.collect(Collectors.toList())));
        }
    }

    protected Stream<Expression> objectValueWithVariableContextToExpression(String element, GraphqlParser.ValueWithVariableContext valueWithVariableContext, GraphqlParser.InputValueDefinitionContext inputValueDefinitionContext) {
        String fieldTypeName = documentManager.getFieldTypeName(inputValueDefinitionContext.type());
        if (documentManager.isInputObject(fieldTypeName)) {
            if (documentManager.fieldTypeIsList(inputValueDefinitionContext.type())) {
                return Stream.ofNullable(valueWithVariableContext.arrayValueWithVariable())
                        .flatMap(arrayValueWithVariableContext -> arrayValueWithVariableContext.valueWithVariable().stream())
                        .flatMap(itemValueWithVariableContext -> objectValueWithVariableContextToExpression(element, itemValueWithVariableContext, inputValueDefinitionContext));
            } else {
                if (isOperatorObject(inputValueDefinitionContext)) {
                    return getOperatorExpression(element, valueWithVariableContext.objectValueWithVariable(), inputValueDefinitionContext).stream();
                } else if (isConditionalObject(inputValueDefinitionContext)) {
                    return documentManager.getInputObject(documentManager.getFieldTypeName(inputValueDefinitionContext.type()))
                            .flatMap(inputObjectTypeDefinitionContext -> objectValueWithVariableToMultipleExpression(inputObjectTypeDefinitionContext, valueWithVariableContext.objectValueWithVariable(), element))
                            .stream();
                }
            }
        }
        return Stream.empty();
    }

    protected Stream<Expression> objectValueContextToExpression(String element, GraphqlParser.ValueContext valueContext, GraphqlParser.InputValueDefinitionContext inputValueDefinitionContext) {
        String fieldTypeName = documentManager.getFieldTypeName(inputValueDefinitionContext.type());
        if (documentManager.isInputObject(fieldTypeName)) {
            if (documentManager.fieldTypeIsList(inputValueDefinitionContext.type())) {
                return Stream.ofNullable(valueContext.arrayValue())
                        .flatMap(arrayValueContext -> arrayValueContext.value().stream())
                        .flatMap(itemValueContext -> objectValueContextToExpression(element, itemValueContext, inputValueDefinitionContext));
            } else {
                if (isOperatorObject(inputValueDefinitionContext)) {
                    return getOperatorExpression(element, valueContext.objectValue(), inputValueDefinitionContext).stream();
                } else if (isConditionalObject(inputValueDefinitionContext)) {
                    return documentManager.getInputObject(documentManager.getFieldTypeName(inputValueDefinitionContext.type()))
                            .flatMap(inputObjectTypeDefinitionContext -> objectValueToMultipleExpression(inputObjectTypeDefinitionContext, valueContext.objectValue(), element))
                            .stream();
                }
            }
        }
        return Stream.empty();
    }

    private Optional<Expression> getOperatorExpression(String element, GraphqlParser.ObjectValueWithVariableContext objectValueWithVariableContext, GraphqlParser.InputValueDefinitionContext inputValueDefinitionContext) {
        return documentManager.getInputObject(documentManager.getFieldTypeName(inputValueDefinitionContext.type()))
                .flatMap(inputObjectTypeDefinition ->
                        inputObjectTypeDefinition.inputObjectValueDefinitions().inputValueDefinition().stream()
                                .filter(fieldInputValueDefinitionContext -> !documentManager.isEnum(fieldInputValueDefinitionContext.type().getText()) && !documentManager.getFieldTypeName(fieldInputValueDefinitionContext.type()).equals("Operator"))
                                .findFirst()
                                .map(fieldInputValueDefinitionContext ->
                                        documentManager.getObjectFieldWithVariableFromInputValueDefinition(objectValueWithVariableContext, fieldInputValueDefinitionContext)
                                                .map(objectFieldContext ->
                                                        operatorToExpression(
                                                                element,
                                                                getOperator(objectValueWithVariableContext, inputValueDefinitionContext),
                                                                objectFieldContext.valueWithVariable()
                                                        )
                                                )
                                                .orElse(
                                                        Optional.ofNullable(fieldInputValueDefinitionContext.defaultValue())
                                                                .flatMap(defaultValueContext ->
                                                                        operatorToExpression(
                                                                                element,
                                                                                getOperator(objectValueWithVariableContext, inputValueDefinitionContext),
                                                                                defaultValueContext.value()
                                                                        )
                                                                )
                                                )
                                )
                )
                .orElseThrow(() -> new GraphQLErrors(TYPE_NOT_EXIST.bind(documentManager.getFieldTypeName(inputValueDefinitionContext.type()))));
    }

    private Optional<Expression> getOperatorExpression(String element, GraphqlParser.ObjectValueContext objectValueContext, GraphqlParser.InputValueDefinitionContext inputValueDefinitionContext) {
        return documentManager.getInputObject(documentManager.getFieldTypeName(inputValueDefinitionContext.type()))
                .flatMap(inputObjectTypeDefinition ->
                        inputObjectTypeDefinition.inputObjectValueDefinitions().inputValueDefinition().stream()
                                .filter(fieldInputValueDefinitionContext -> !documentManager.isEnum(fieldInputValueDefinitionContext.type().getText()) && !documentManager.getFieldTypeName(fieldInputValueDefinitionContext.type()).equals("Operator"))
                                .findFirst()
                                .map(fieldInputValueDefinitionContext ->
                                        documentManager.getObjectFieldFromInputValueDefinition(objectValueContext, fieldInputValueDefinitionContext)
                                                .map(objectFieldContext ->
                                                        operatorToExpression(
                                                                element,
                                                                getOperator(objectValueContext, inputValueDefinitionContext),
                                                                objectFieldContext.value()
                                                        )
                                                )
                                                .orElse(
                                                        Optional.ofNullable(fieldInputValueDefinitionContext.defaultValue())
                                                                .flatMap(defaultValueContext ->
                                                                        operatorToExpression(
                                                                                element,
                                                                                getOperator(objectValueContext, inputValueDefinitionContext),
                                                                                defaultValueContext.value()
                                                                        )
                                                                )
                                                )
                                )
                )
                .orElseThrow(() -> new GraphQLErrors(TYPE_NOT_EXIST.bind(documentManager.getFieldTypeName(inputValueDefinitionContext.type()))));
    }

    private GraphqlParser.EnumValueContext getOperator(GraphqlParser.ObjectValueWithVariableContext objectValueWithVariableContext, GraphqlParser.InputValueDefinitionContext inputValueDefinitionContext) {
        return documentManager.getInputObject(documentManager.getFieldTypeName(inputValueDefinitionContext.type()))
                .flatMap(inputObjectTypeDefinition ->
                        inputObjectTypeDefinition.inputObjectValueDefinitions().inputValueDefinition().stream()
                                .filter(fieldInputValueDefinitionContext -> documentManager.isEnum(fieldInputValueDefinitionContext.type().getText()) && documentManager.getFieldTypeName(fieldInputValueDefinitionContext.type()).equals("Operator"))
                                .findFirst()
                                .map(fieldInputValueDefinitionContext ->
                                        documentManager.getObjectFieldWithVariableFromInputValueDefinition(objectValueWithVariableContext, fieldInputValueDefinitionContext)
                                                .map(objectFieldWithVariableContext -> objectFieldWithVariableContext.valueWithVariable().enumValue())
                                                .orElse(fieldInputValueDefinitionContext.defaultValue().value().enumValue())
                                )
                )
                .orElseThrow(() -> new GraphQLErrors(TYPE_NOT_EXIST.bind(documentManager.getFieldTypeName(inputValueDefinitionContext.type()))));
    }

    private GraphqlParser.EnumValueContext getOperator(GraphqlParser.ObjectValueContext objectValueContext, GraphqlParser.InputValueDefinitionContext inputValueDefinitionContext) {
        return documentManager.getInputObject(documentManager.getFieldTypeName(inputValueDefinitionContext.type()))
                .flatMap(inputObjectTypeDefinition ->
                        inputObjectTypeDefinition.inputObjectValueDefinitions().inputValueDefinition().stream()
                                .filter(fieldInputValueDefinitionContext -> documentManager.isEnum(fieldInputValueDefinitionContext.type().getText()) && documentManager.getFieldTypeName(fieldInputValueDefinitionContext.type()).equals("Operator"))
                                .findFirst()
                                .map(fieldInputValueDefinitionContext ->
                                        documentManager.getObjectFieldFromInputValueDefinition(objectValueContext, fieldInputValueDefinitionContext)
                                                .map(objectFieldContext -> objectFieldContext.value().enumValue())
                                                .orElse(fieldInputValueDefinitionContext.defaultValue().value().enumValue())
                                )
                )
                .orElseThrow(() -> new GraphQLErrors(TYPE_NOT_EXIST.bind(documentManager.getFieldTypeName(inputValueDefinitionContext.type()))));
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

    private boolean hasOrConditional(GraphqlParser.ArgumentsContext argumentsContext, GraphqlParser.ArgumentsDefinitionContext argumentsDefinitionContext) {
        return argumentsContext.argument().stream()
                .anyMatch(argumentContext ->
                        documentManager.getInputValueDefinitionFromArgumentsDefinitionContext(argumentsDefinitionContext, argumentContext)
                                .map(inputValueDefinitionContext -> isOrConditional(inputValueDefinitionContext, argumentContext.valueWithVariable()))
                                .orElse(false)
                );
    }

    private boolean hasOrConditional(GraphqlParser.ObjectValueWithVariableContext objectValueWithVariableContext, GraphqlParser.InputObjectTypeDefinitionContext inputObjectTypeDefinitionContext) {

        return objectValueWithVariableContext.objectFieldWithVariable().stream()
                .anyMatch(objectFieldWithVariableContext ->
                        documentManager.getInputValueDefinitionFromInputObjectTypeDefinitionContext(inputObjectTypeDefinitionContext, objectFieldWithVariableContext)
                                .map(inputValueDefinitionContext -> isOrConditional(inputValueDefinitionContext, objectFieldWithVariableContext.valueWithVariable()))
                                .orElse(false)
                );
    }

    private boolean hasOrConditional(GraphqlParser.ObjectValueContext objectValueContext, GraphqlParser.InputObjectTypeDefinitionContext inputObjectTypeDefinitionContext) {
        return objectValueContext.objectField().stream().anyMatch(objectFieldContext ->
                documentManager.getInputValueDefinitionFromInputObjectTypeDefinitionContext(inputObjectTypeDefinitionContext, objectFieldContext)
                        .map(inputValueDefinitionContext -> isOrConditional(inputValueDefinitionContext, objectFieldContext.value()))
                        .orElse(false));
    }

    private boolean isOrConditional(GraphqlParser.InputValueDefinitionContext inputValueDefinitionContext, GraphqlParser.ValueWithVariableContext valueWithVariableContext) {
        if (isConditional(inputValueDefinitionContext)) {
            return conditionalIsOr(valueWithVariableContext.enumValue());
        }
        return false;
    }

    private boolean isOrConditional(GraphqlParser.InputValueDefinitionContext inputValueDefinitionContext, GraphqlParser.ValueContext valueContext) {
        if (isConditional(inputValueDefinitionContext)) {
            return conditionalIsOr(valueContext.enumValue());
        }
        return false;
    }

    private boolean conditionalIsOr(GraphqlParser.EnumValueContext enumValueContext) {
        return enumValueContext != null && enumValueContext.enumValueName().getText().equals("OR");
    }

    private boolean isConditional(GraphqlParser.InputValueDefinitionContext inputValueDefinitionContext) {
        return inputValueDefinitionContext.type().typeName() != null && isConditional(inputValueDefinitionContext.type().typeName().name().getText());
    }

    private boolean isConditional(String typeName) {
        return typeName != null && documentManager.isEnum(typeName) && typeName.equals("Conditional");
    }

    private boolean isOperatorObject(GraphqlParser.InputValueDefinitionContext inputValueDefinitionContext) {
        return containsEnum(inputValueDefinitionContext, "Operator");
    }

    private boolean isConditionalObject(GraphqlParser.InputValueDefinitionContext inputValueDefinitionContext) {
        return containsEnum(inputValueDefinitionContext, "Conditional");
    }

    private boolean containsEnum(GraphqlParser.InputValueDefinitionContext inputValueDefinitionContext, String enumName) {
        String fieldTypeName = documentManager.getFieldTypeName(inputValueDefinitionContext.type());
        Optional<GraphqlParser.InputObjectTypeDefinitionContext> objectTypeDefinition = documentManager.getInputObject(fieldTypeName);
        return objectTypeDefinition
                .map(inputObjectTypeDefinitionContext ->
                        inputObjectTypeDefinitionContext.inputObjectValueDefinitions().inputValueDefinition().stream()
                                .anyMatch(fieldInputValueDefinitionContext ->
                                        documentManager.isEnum(fieldInputValueDefinitionContext.type().getText()) &&
                                                fieldInputValueDefinitionContext.type().typeName().name().getText().equals(enumName))
                )
                .orElse(false);
    }
}

