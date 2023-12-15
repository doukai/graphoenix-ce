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

public class EnumType implements Definition {

    private final STGroupFile stGroupFile = new STGroupFile("stg/type/EnumType.stg");
    private String name;
    private Collection<Directive> directives;
    private Collection<EnumValue> enumValues;
    private String description;

    public EnumType() {
    }

    public EnumType(GraphqlParser.EnumTypeDefinitionContext enumTypeDefinitionContext) {
        this.name = enumTypeDefinitionContext.name().getText();
        if (enumTypeDefinitionContext.description() != null) {
            this.description = getStringValue(enumTypeDefinitionContext.description().StringValue());
        }
        if (enumTypeDefinitionContext.directives() != null) {
            this.directives = enumTypeDefinitionContext.directives().directive().stream()
                    .map(Directive::new)
                    .collect(Collectors.toCollection(LinkedHashSet::new));
        }
        if (enumTypeDefinitionContext.enumValueDefinitions() != null) {
            this.enumValues = enumTypeDefinitionContext.enumValueDefinitions().enumValueDefinition().stream()
                    .map(EnumValue::new)
                    .collect(Collectors.toCollection(LinkedHashSet::new));
        }
    }

    public EnumType merge(GraphqlParser.EnumTypeDefinitionContext... enumTypeDefinitionContexts) {
        return merge(
                Stream.of(enumTypeDefinitionContexts)
                        .map(EnumType::new)
                        .toArray(EnumType[]::new)
        );
    }

    public EnumType merge(EnumType... enumTypes) {
        directives = Stream.concat(
                Stream.ofNullable(directives),
                Stream.of(enumTypes).flatMap(item -> Stream.ofNullable(item.getDirectives()))
        )
                .flatMap(Collection::stream)
                .filter(distinctByKey(Directive::getName))
                .collect(Collectors.toCollection(LinkedHashSet::new));

        enumValues = Stream.concat(
                Stream.ofNullable(enumValues),
                Stream.of(enumTypes).flatMap(item -> Stream.ofNullable(item.getEnumValues()))
        )
                .flatMap(Collection::stream)
                .filter(distinctByKey(EnumValue::getName))
                .collect(Collectors.toCollection(LinkedHashSet::new));
        return this;
    }

    public String getName() {
        return name;
    }

    public EnumType setName(String name) {
        this.name = name;
        return this;
    }

    public Collection<Directive> getDirectives() {
        return directives;
    }

    public EnumType setDirectives(Collection<Directive> directives) {
        if (directives != null) {
            this.directives = new LinkedHashSet<>(directives);
        }
        return this;
    }

    public EnumType addDirective(Directive directive) {
        if (this.directives == null) {
            this.directives = new LinkedHashSet<>();
        }
        this.directives.add(directive);
        return this;
    }

    public EnumType addDirectives(Collection<Directive> directives) {
        if (this.directives == null) {
            this.directives = new LinkedHashSet<>();
        }
        this.directives.addAll(directives);
        return this;
    }

    public Collection<EnumValue> getEnumValues() {
        return enumValues;
    }

    public EnumType setEnumValues(Collection<EnumValue> enumValues) {
        this.enumValues = enumValues;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public EnumType setDescription(String description) {
        this.description = description;
        return this;
    }

    @Override
    public boolean isEnum() {
        return true;
    }

    @Override
    public String toString() {
        ST st = stGroupFile.getInstanceOf("enumTypeDefinition");
        st.add("enumType", this);
        return st.render();
    }
}
