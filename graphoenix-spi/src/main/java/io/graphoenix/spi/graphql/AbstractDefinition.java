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

@SuppressWarnings("ALL")
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

    public <T extends AbstractDefinition> T merge(AbstractDefinition... abstractDefinitions) {
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
        return (T) this;
    }

    public String getName() {
        return name;
    }

    public <T extends AbstractDefinition> T setName(String name) {
        this.name = name;
        return (T) this;
    }

    public String getDescription() {
        return description;
    }

    public <T extends AbstractDefinition> T setDescription(String description) {
        this.description = description;
        return (T) this;
    }

    public Map<String, Directive> getDirectiveMap() {
        return directiveMap;
    }

    public <T extends AbstractDefinition> T setDirectiveMap(Map<String, Directive> directiveMap) {
        this.directiveMap = directiveMap;
        return (T) this;
    }

    public Directive getDirective(String name) {
        return directiveMap.get(name);
    }

    public boolean hasDirective(String name) {
        return directiveMap.containsKey(name);
    }

    public Collection<Directive> getDirectives() {
        return directiveMap.values();
    }

    public <T extends AbstractDefinition> T setDirectives(Collection<Directive> directives) {
        this.directiveMap.clear();
        return addDirectives(directives);
    }

    public <T extends AbstractDefinition> T addDirective(Directive directive) {
        this.directiveMap.put(directive.getName(), directive);
        return (T) this;
    }

    public <T extends AbstractDefinition> T addDirectives(Collection<Directive> directives) {
        this.directiveMap.putAll(
                directives.stream()
                        .collect(
                                Collectors.toMap(
                                        Directive::getName,
                                        directive -> directive
                                )
                        )
        );
        return (T) this;
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
        return hasDirective(DIRECTIVE_CONTAINER_TYPE_NAME);
    }
}
