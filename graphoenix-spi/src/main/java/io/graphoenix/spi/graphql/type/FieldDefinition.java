package io.graphoenix.spi.graphql.type;

import graphql.parser.antlr.GraphqlParser;
import io.graphoenix.spi.graphql.AbstractDefinition;
import io.graphoenix.spi.graphql.common.Directive;
import io.graphoenix.spi.graphql.common.StringValue;
import io.graphoenix.spi.graphql.common.ValueWithVariable;
import jakarta.json.JsonString;
import jakarta.json.JsonValue;
import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroupFile;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static io.graphoenix.spi.constant.Hammurabi.*;

public class FieldDefinition extends AbstractDefinition {

    private final STGroupFile stGroupFile = new STGroupFile("stg/type/FieldDefinition.stg");
    private Map<String, InputValue> argumentMap;
    private Type type;

    public FieldDefinition() {
        super();
        this.argumentMap = new LinkedHashMap<>();
    }

    public FieldDefinition(String name) {
        super(name);
        this.argumentMap = new LinkedHashMap<>();
    }

    public FieldDefinition(GraphqlParser.FieldDefinitionContext fieldDefinitionContext) {
        super(fieldDefinitionContext.name(), fieldDefinitionContext.description(), fieldDefinitionContext.directives());
        this.type = Type.of(fieldDefinitionContext.type());
        if (fieldDefinitionContext.argumentsDefinition() != null) {
            this.argumentMap = fieldDefinitionContext.argumentsDefinition().inputValueDefinition().stream()
                    .map(InputValue::new)
                    .collect(
                            Collectors.toMap(
                                    InputValue::getName,
                                    inputValue -> inputValue,
                                    (x, y) -> y,
                                    LinkedHashMap::new
                            )
                    );
        }
    }

    public Collection<InputValue> getArguments() {
        return argumentMap.values();
    }

    public FieldDefinition setArguments(Collection<InputValue> arguments) {
        this.argumentMap.clear();
        return addArguments(arguments);
    }

    public FieldDefinition addArguments(Collection<InputValue> arguments) {
        this.argumentMap.putAll(
                arguments.stream()
                        .collect(
                                Collectors.toMap(
                                        InputValue::getName,
                                        inputValue -> inputValue
                                )
                        )
        );
        return this;
    }

    public FieldDefinition addArgument(InputValue argument) {
        this.argumentMap.put(argument.getName(), argument);
        return this;
    }

    public Type getType() {
        return type;
    }

    public FieldDefinition setType(Type type) {
        this.type = type;
        return this;
    }

    public FieldDefinition setType(String typeName) {
        this.type = Type.of(typeName);
        return this;
    }

    public Optional<String> getDataTypeName() {
        return Stream.ofNullable(getDirective(DIRECTIVE_DATA_TYPE_NAME))
                .flatMap(directive -> directive.getArguments().entrySet().stream())
                .filter(entry -> entry.getKey().equals(DIRECTIVE_DATA_TYPE_TYPE_NAME))
                .filter(entry -> entry.getValue().getValueType().equals(JsonValue.ValueType.STRING))
                .map(entry -> ((JsonString) entry.getValue()).getString())
                .findFirst();
    }

    public String getTypeNameWithoutID() {
        String fieldTypeName = getDataTypeName().orElseGet(() -> getType().getTypeName().getName());
        if (SCALA_ID_NAME.equals(fieldTypeName)) {
            return SCALA_STRING_NAME;
        }
        return fieldTypeName;
    }

    public Optional<Directive> getMap() {
        return Stream.ofNullable(getDirective(DIRECTIVE_MAP_NAME))
                .findFirst();
    }

    public Optional<String> getMapWithTypeName() {
        return getMap()
                .flatMap(directive -> Optional.ofNullable(directive.getArgument(DIRECTIVE_ARGUMENT_WITH_NAME)))
                .filter(ValueWithVariable::isString)
                .map(valueWithVariable -> ((StringValue) valueWithVariable).getString());
    }

    public boolean isInvokeField() {
        return hasDirective(DIRECTIVE_INVOKE_NAME);
    }

    public boolean isFunctionField() {
        return hasDirective(DIRECTIVE_FUNC_NAME);
    }

    public boolean isConnectionField() {
        return hasDirective(DIRECTIVE_CONNECTION_NAME);
    }

    public boolean isAggregateField() {
        return hasDirective(DIRECTIVE_AGGREGATE_NAME);
    }

    public boolean isFetchField() {
        return hasDirective(DIRECTIVE_FETCH_NAME);
    }

    @Override
    public String toString() {
        ST st = stGroupFile.getInstanceOf("fieldDefinitionDefinition");
        st.add("filedDefinition", this);
        return st.render();
    }
}
