package io.graphoenix.sql.translator;

import graphql.parser.antlr.GraphqlParser;
import io.graphoenix.core.handler.DocumentManager;
import io.graphoenix.core.handler.PackageManager;
import io.graphoenix.spi.graphql.Definition;
import io.graphoenix.spi.graphql.common.ValueWithVariable;
import io.graphoenix.spi.graphql.operation.Field;
import io.graphoenix.spi.graphql.type.FieldDefinition;
import io.graphoenix.spi.graphql.type.InputValue;
import io.graphoenix.spi.graphql.type.ObjectType;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.StringValue;
import net.sf.jsqlparser.expression.operators.relational.EqualsTo;

import java.util.AbstractMap;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static io.graphoenix.spi.constant.Hammurabi.INPUT_OPERATOR_INPUT_VALUE_EQ;
import static io.graphoenix.spi.constant.Hammurabi.INPUT_OPERATOR_INPUT_VALUE_OPR_NAME;
import static io.graphoenix.sql.utils.DBNameUtil.graphqlFieldToColumn;
import static io.graphoenix.sql.utils.DBValueUtil.leafValueToDBValue;

@ApplicationScoped
public class ArgumentsTranslator {

    private final DocumentManager documentManager;
    private final PackageManager packageManager;

    @Inject
    public ArgumentsTranslator(DocumentManager documentManager, PackageManager packageManager) {
        this.documentManager = documentManager;
        this.packageManager = packageManager;
    }

    protected Optional<Expression> argumentsToMultipleExpression(FieldDefinition fieldDefinition, Field field, int level) {
        ObjectType objectType = documentManager.getFieldTypeDefinition(fieldDefinition).asObject();
        Map<InputValue, ValueWithVariable> arguments = Stream.ofNullable(fieldDefinition.getArguments())
                .flatMap(Collection::stream)
                .flatMap(inputValue ->
                        field.getArguments().getArgument(inputValue.getName())
                                .or(() -> Optional.ofNullable(inputValue.getDefaultValue()))
                                .stream()
                                .map(valueWithVariable -> new AbstractMap.SimpleEntry<>(inputValue, valueWithVariable))
                )
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        objectType.getFields().stream()
                .flatMap(item ->
                        arguments.entrySet().stream()
                                .filter(entry -> entry.getKey().getName().equals(item.getName()))
                                .map(entry -> {

                                        }
                                )
                )
                .map(entry ->)


    }


    protected Expression inputValueToExpression(ObjectType objectType, FieldDefinition fieldDefinition, InputValue inputValue, ValueWithVariable valueWithVariable, int level) {
        Definition fieldTypeDefinition = documentManager.getFieldTypeDefinition(fieldDefinition);
        if (fieldTypeDefinition.isObject()) {

        } else {
            documentManager.getInputValueTypeDefinition(inputValue).asInputObject().getInputValues().stream()
                    .filter(item -> item.getName().equals(INPUT_OPERATOR_INPUT_VALUE_OPR_NAME))
                    .map(item ->
                            Optional.ofNullable(valueWithVariable.asObject().getValueWithVariableOrNull(item.getName()))
                                    .orElseGet(item::getDefaultValue)
                    )
                    .filter(ValueWithVariable::isEnum)
                    .map(ValueWithVariable::asEnum)
                    .map(enumValue -> {
                                switch (enumValue.getValue()) {
                                    case INPUT_OPERATOR_INPUT_VALUE_EQ:
                                        return new EqualsTo()
                                                .withLeftExpression(graphqlFieldToColumn(objectType.getName(), fieldDefinition.getName(), level))
                                                .withRightExpression(leafValueToDBValue(valueWithVariable));
                                }
                            }
                    )
        }
    }

    protected Expression scalarExpressionToExpression(FieldDefinition fieldDefinition, InputValue inputValue, ValueWithVariable valueWithVariable) {


        Definition fieldTypeDefinition = documentManager.getFieldTypeDefinition(fieldDefinition);
        switch (fieldTypeDefinition.getName()) {

        }


    }

}
