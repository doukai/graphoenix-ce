package io.graphoenix.spi.graphql.type;

import graphql.parser.antlr.GraphqlParser;
import io.graphoenix.spi.graphql.common.Directive;
import jakarta.json.JsonString;
import jakarta.json.JsonValue;
import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroupFile;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static io.graphoenix.spi.constant.Hammurabi.DATA_TYPE_DIRECTIVE_NAME;
import static io.graphoenix.spi.utils.DocumentUtil.getStringValue;

public class FieldDefinition {

    private final STGroupFile stGroupFile = new STGroupFile("stg/type/FieldDefinition.stg");
    private String name;
    private Collection<InputValue> arguments;
    private Type type;
    private Collection<Directive> directives;
    private String description;

    public FieldDefinition() {
    }

    public FieldDefinition(String name) {
        this.name = name;
    }

    public FieldDefinition(GraphqlParser.FieldDefinitionContext fieldDefinitionContext) {
        this.name = fieldDefinitionContext.name().getText();
        this.type = Type.of(fieldDefinitionContext.type());
        if (fieldDefinitionContext.description() != null) {
            this.description = getStringValue(fieldDefinitionContext.description().StringValue());
        }
        if (fieldDefinitionContext.argumentsDefinition() != null) {
            this.arguments = fieldDefinitionContext.argumentsDefinition().inputValueDefinition().stream()
                    .map(InputValue::new)
                    .collect(Collectors.toCollection(LinkedHashSet::new));
        }
        if (fieldDefinitionContext.directives() != null) {
            this.directives = fieldDefinitionContext.directives().directive().stream()
                    .map(Directive::new)
                    .collect(Collectors.toCollection(LinkedHashSet::new));
        }
    }

    public String getName() {
        return name;
    }

    public FieldDefinition setName(String name) {
        this.name = name;
        return this;
    }

    public Collection<InputValue> getArguments() {
        return arguments;
    }

    public FieldDefinition setArguments(Collection<InputValue> arguments) {
        this.arguments = arguments;
        return this;
    }

    public FieldDefinition addArguments(Collection<InputValue> arguments) {
        if (this.arguments == null) {
            this.arguments = new LinkedHashSet<>();
        }
        this.arguments.addAll(arguments);
        return this;
    }

    public FieldDefinition addArgument(InputValue argument) {
        if (this.arguments == null) {
            this.arguments = new LinkedHashSet<>();
        }
        this.arguments.add(argument);
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

    public Collection<Directive> getDirectives() {
        return directives;
    }

    public FieldDefinition setStringDirectives(Collection<Directive> directives) {
        this.directives = directives;
        return this;
    }

    public FieldDefinition setDirectives(Collection<Directive> directives) {
        if (directives != null) {
            this.directives = new LinkedHashSet<>(directives);
        }
        return this;
    }

    public FieldDefinition addDirective(Directive directive) {
        if (this.directives == null) {
            this.directives = new LinkedHashSet<>();
        }
        this.directives.add(directive);
        return this;
    }

    public FieldDefinition addDirectives(Collection<Directive> directives) {
        if (this.directives == null) {
            this.directives = new LinkedHashSet<>();
        }
        this.directives.addAll(directives);
        return this;
    }

    public String getDescription() {
        return description;
    }

    public FieldDefinition setDescription(String description) {
        this.description = description;
        return this;
    }

    public Optional<String> getDataTypeName() {
        return Stream.ofNullable(this.directives)
                .flatMap(Collection::stream)
                .filter(directive -> directive.getName().equals(DATA_TYPE_DIRECTIVE_NAME))
                .flatMap(directive -> directive.getArguments().entrySet().stream())
                .filter(entry -> entry.getKey().equals("type"))
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
