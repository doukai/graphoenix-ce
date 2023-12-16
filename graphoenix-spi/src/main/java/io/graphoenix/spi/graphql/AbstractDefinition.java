package io.graphoenix.spi.graphql;

import graphql.parser.antlr.GraphqlParser;
import io.graphoenix.spi.graphql.common.Directive;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static io.graphoenix.spi.utils.DocumentUtil.getStringValue;
import static io.graphoenix.spi.utils.StreamUtil.distinctByKey;

public abstract class AbstractDefinition {

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
}
