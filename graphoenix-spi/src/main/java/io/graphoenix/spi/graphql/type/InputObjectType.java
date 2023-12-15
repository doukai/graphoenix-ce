package io.graphoenix.spi.graphql.type;

import graphql.parser.antlr.GraphqlParser;
import io.graphoenix.spi.graphql.Definition;
import io.graphoenix.spi.graphql.common.Directive;
import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroupFile;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static io.graphoenix.spi.utils.DocumentUtil.getStringValue;
import static io.graphoenix.spi.utils.StreamUtil.distinctByKey;

public class InputObjectType implements Definition {

    private final STGroupFile stGroupFile = new STGroupFile("stg/type/InputObjectType.stg");
    private String name;
    private Collection<Directive> directives;
    private Collection<InputValue> inputValues;
    private String description;

    public InputObjectType() {
    }

    public InputObjectType(String name) {
        this.name = name;
    }

    public InputObjectType(GraphqlParser.InputObjectTypeDefinitionContext inputObjectTypeDefinitionContext) {
        this.name = inputObjectTypeDefinitionContext.name().getText();
        if (inputObjectTypeDefinitionContext.description() != null) {
            this.description = getStringValue(inputObjectTypeDefinitionContext.description().StringValue());
        }
        if (inputObjectTypeDefinitionContext.directives() != null) {
            this.directives = inputObjectTypeDefinitionContext.directives().directive().stream()
                    .map(Directive::new)
                    .collect(Collectors.toCollection(LinkedHashSet::new));
        }
        if (inputObjectTypeDefinitionContext.inputObjectValueDefinitions() != null) {
            this.inputValues = inputObjectTypeDefinitionContext.inputObjectValueDefinitions().inputValueDefinition().stream()
                    .map(InputValue::new)
                    .collect(Collectors.toCollection(LinkedHashSet::new));
        }
    }

    public InputObjectType merge(GraphqlParser.InputObjectTypeDefinitionContext... inputObjectTypeDefinitionContexts) {
        return merge(
                Stream.of(inputObjectTypeDefinitionContexts)
                        .map(InputObjectType::new)
                        .toArray(InputObjectType[]::new)
        );
    }

    public InputObjectType merge(InputObjectType... inputObjectTypes) {
        directives = Stream.concat(
                Stream.ofNullable(directives),
                Stream.of(inputObjectTypes).flatMap(item -> Stream.ofNullable(item.getDirectives()))
        )
                .flatMap(Collection::stream)
                .filter(distinctByKey(Directive::getName))
                .collect(Collectors.toCollection(LinkedHashSet::new));

        inputValues = Stream.concat(
                Stream.ofNullable(inputValues),
                Stream.of(inputObjectTypes).flatMap(item -> Stream.ofNullable(item.getInputValues()))
        )
                .flatMap(Collection::stream)
                .filter(distinctByKey(InputValue::getName))
                .collect(Collectors.toCollection(LinkedHashSet::new));
        return this;
    }

    public String getName() {
        return name;
    }

    public InputObjectType setName(String name) {
        this.name = name;
        return this;
    }

    public Collection<Directive> getDirectives() {
        return directives;
    }

    public InputObjectType setDirectives(Collection<Directive> directives) {
        if (directives != null) {
            this.directives = new LinkedHashSet<>(directives);
        }
        return this;
    }

    public InputObjectType addDirective(Directive directive) {
        if (this.directives == null) {
            this.directives = new LinkedHashSet<>();
        }
        this.directives.add(directive);
        return this;
    }

    public InputObjectType addDirectives(Collection<Directive> directives) {
        if (this.directives == null) {
            this.directives = new LinkedHashSet<>();
        }
        this.directives.addAll(directives);
        return this;
    }

    public Collection<InputValue> getInputValues() {
        return inputValues;
    }

    public InputObjectType setInputValues(Collection<InputValue> inputValues) {
        this.inputValues = inputValues;
        return this;
    }

    public InputObjectType addInputValue(InputValue inputValue) {
        if (this.inputValues == null) {
            this.inputValues = new LinkedHashSet<>();
        }
        this.inputValues.add(inputValue);
        return this;
    }

    public InputObjectType addInputValues(Collection<InputValue> inputValues) {
        if (this.inputValues == null) {
            this.inputValues = new LinkedHashSet<>();
        }
        this.inputValues.addAll(inputValues);
        return this;
    }

    public String getDescription() {
        return description;
    }

    public InputObjectType setDescription(String description) {
        this.description = description;
        return this;
    }

    @Override
    public boolean isInputObject() {
        return true;
    }

    @Override
    public String toString() {
        ST st = stGroupFile.getInstanceOf("inputObjectTypeDefinition");
        st.add("inputObjectType", this);
        return st.render();
    }
}
