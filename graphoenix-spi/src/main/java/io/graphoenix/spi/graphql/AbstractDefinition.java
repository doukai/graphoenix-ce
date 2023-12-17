package io.graphoenix.spi.graphql;

import graphql.parser.antlr.GraphqlParser;
import io.graphoenix.spi.graphql.common.Directive;
import io.graphoenix.spi.graphql.common.StringValue;
import io.graphoenix.spi.graphql.common.ValueWithVariable;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static io.graphoenix.spi.constant.Hammurabi.*;
import static io.graphoenix.spi.utils.DocumentUtil.getStringValue;
import static io.graphoenix.spi.utils.StreamUtil.distinctByKey;

public abstract class AbstractDefinition implements Definition {

    private String name;
    private String description;
    private Map<String, Directive> directiveMap;

    public AbstractDefinition() {
        this.directiveMap = new LinkedHashMap<>();
    }

    public AbstractDefinition(String name) {
        this.name = name;
    }

    public AbstractDefinition(GraphqlParser.DescriptionContext description) {
        if (description != null) {
            this.description = getStringValue(description.StringValue());
        }
    }

    public AbstractDefinition(GraphqlParser.DescriptionContext description, GraphqlParser.DirectivesContext directivesContext) {
        this(description);
        if (directivesContext != null) {
            this.directiveMap = directivesContext.directive().stream()
                    .map(Directive::new)
                    .collect(
                            Collectors.toMap(
                                    Directive::getName,
                                    directive -> directive,
                                    (x, y) -> y,
                                    LinkedHashMap::new
                            )
                    );
        }
    }

    public AbstractDefinition(GraphqlParser.NameContext nameContext, GraphqlParser.DescriptionContext description, GraphqlParser.DirectivesContext directivesContext) {
        this(description, directivesContext);
        this.name = nameContext.getText();
    }

    public AbstractDefinition(GraphqlParser.NameContext nameContext, GraphqlParser.DescriptionContext description) {
        this(nameContext, description, null);
    }

    public AbstractDefinition merge(AbstractDefinition... abstractDefinitions) {
        directiveMap.putAll(
                Stream
                        .concat(
                                Stream.ofNullable(directiveMap.values()),
                                Stream.of(abstractDefinitions).flatMap(item -> Stream.ofNullable(item.getDirectives()))
                        )
                        .flatMap(Collection::stream)
                        .filter(distinctByKey(Directive::getName))
                        .collect(
                                Collectors.toMap(
                                        Directive::getName,
                                        directive -> directive
                                )
                        )
        );
        return this;
    }

    public String getName() {
        return name;
    }

    public AbstractDefinition setName(String name) {
        this.name = name;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public AbstractDefinition setDescription(String description) {
        this.description = description;
        return this;
    }

    public Map<String, Directive> getDirectiveMap() {
        return directiveMap;
    }

    public AbstractDefinition setDirectiveMap(Map<String, Directive> directiveMap) {
        this.directiveMap = directiveMap;
        return this;
    }

    public Collection<Directive> getDirectives() {
        return directiveMap.values();
    }

    public AbstractDefinition setDirectives(Collection<Directive> directives) {
        this.directiveMap.clear();
        return addDirectives(directives);
    }

    public AbstractDefinition addDirective(Directive directive) {
        this.directiveMap.put(directive.getName(), directive);
        return this;
    }

    public AbstractDefinition addDirectives(Collection<Directive> directives) {
        this.directiveMap.putAll(
                directives.stream()
                        .collect(
                                Collectors.toMap(
                                        Directive::getName,
                                        directive -> directive
                                )
                        )
        );
        return this;
    }

    public Optional<String> getPackageName() {
        return Optional.ofNullable(directiveMap.get(DIRECTIVE_PACKAGE_INFO_NAME))
                .flatMap(directive -> Optional.ofNullable(directive.getArgument(DIRECTIVE_PACKAGE_INFO_PACKAGE_NAME_NAME)))
                .filter(ValueWithVariable::isString)
                .map(valueWithVariable -> (StringValue) valueWithVariable)
                .map(StringValue::getString);
    }

    public Optional<String> getClassName() {
        return Optional.ofNullable(directiveMap.get(DIRECTIVE_CLASS_INFO_NAME))
                .flatMap(directive -> Optional.ofNullable(directive.getArgument(DIRECTIVE_CLASS_INFO_CLASS_NAME_NAME)))
                .filter(ValueWithVariable::isString)
                .map(valueWithVariable -> (StringValue) valueWithVariable)
                .map(StringValue::getString);
    }

    public boolean isContainerType() {
        return Optional.ofNullable(directiveMap.get(DIRECTIVE_CONTAINER_TYPE_NAME))
                .isPresent();
    }

    public boolean isQueryOperationType() {
        return isObject() && getName().equals(TYPE_QUERY_NAME);
    }

    public boolean isMutationOperationType() {
        return isObject() && getName().equals(TYPE_MUTATION_NAME);
    }

    public boolean isSubscriptionOperationType() {
        return isObject() && getName().equals(TYPE_SUBSCRIPTION_NAME);
    }

    public boolean isOperationType() {
        return isQueryOperationType() || isMutationOperationType() || isSubscriptionOperationType();
    }
}
