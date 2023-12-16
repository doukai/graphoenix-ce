package io.graphoenix.spi.graphql.type;

import graphql.parser.antlr.GraphqlParser;
import io.graphoenix.spi.graphql.AbstractDefinition;
import jakarta.json.JsonString;
import jakarta.json.JsonValue;
import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroupFile;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static io.graphoenix.spi.constant.Hammurabi.DATA_TYPE_DIRECTIVE_NAME;
import static io.graphoenix.spi.constant.Hammurabi.DATA_TYPE_DIRECTIVE_TYPE_NAME;

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
        return Stream.ofNullable(getDirectives())
                .flatMap(Collection::stream)
                .filter(directive -> directive.getName().equals(DATA_TYPE_DIRECTIVE_NAME))
                .flatMap(directive -> directive.getArguments().entrySet().stream())
                .filter(entry -> entry.getKey().equals(DATA_TYPE_DIRECTIVE_TYPE_NAME))
                .filter(entry -> entry.getValue().getValueType().equals(JsonValue.ValueType.STRING))
                .map(entry -> ((JsonString) entry.getValue()).getString())
                .findFirst();
    }

    @Override
    public String toString() {
        ST st = stGroupFile.getInstanceOf("fieldDefinitionDefinition");
        st.add("filedDefinition", this);
        return st.render();
    }
}
