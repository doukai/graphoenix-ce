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

import static io.graphoenix.spi.utils.DocumentUtil.getImplementsInterfaces;
import static io.graphoenix.spi.utils.DocumentUtil.getStringValue;
import static io.graphoenix.spi.utils.StreamUtil.distinctByKey;

public class InterfaceType implements Definition {

    private final STGroupFile stGroupFile = new STGroupFile("stg/type/InterfaceType.stg");
    private String name;
    private Collection<String> interfaces;
    private Collection<Directive> directives;
    private Collection<FieldDefinition> fieldDefinitions;
    private String description;

    public InterfaceType() {
    }

    public InterfaceType(GraphqlParser.InterfaceTypeDefinitionContext interfaceTypeDefinitionContext) {
        this.name = interfaceTypeDefinitionContext.name().getText();
        if (interfaceTypeDefinitionContext.description() != null) {
            this.description = getStringValue(interfaceTypeDefinitionContext.description().StringValue());
        }
        if (interfaceTypeDefinitionContext.implementsInterfaces() != null) {
            this.interfaces = getImplementsInterfaces(interfaceTypeDefinitionContext.implementsInterfaces())
                    .collect(Collectors.toCollection(LinkedHashSet::new));
        }
        if (interfaceTypeDefinitionContext.directives() != null) {
            this.directives = interfaceTypeDefinitionContext.directives().directive().stream()
                    .map(Directive::new)
                    .collect(Collectors.toCollection(LinkedHashSet::new));
        }
        if (interfaceTypeDefinitionContext.fieldsDefinition() != null) {
            this.fieldDefinitions = interfaceTypeDefinitionContext.fieldsDefinition().fieldDefinition().stream()
                    .map(FieldDefinition::new)
                    .collect(Collectors.toCollection(LinkedHashSet::new));
        }
    }

    public InterfaceType merge(GraphqlParser.InterfaceTypeDefinitionContext... interfaceTypeDefinitionContexts) {
        return merge(
                Stream.of(interfaceTypeDefinitionContexts)
                        .map(InterfaceType::new)
                        .toArray(InterfaceType[]::new)
        );
    }

    public InterfaceType merge(InterfaceType... interfaceTypes) {
        directives = Stream.concat(
                Stream.ofNullable(directives),
                Stream.of(interfaceTypes).flatMap(item -> Stream.ofNullable(item.getDirectives()))
        )
                .flatMap(Collection::stream)
                .filter(distinctByKey(Directive::getName))
                .collect(Collectors.toCollection(LinkedHashSet::new));

        interfaces = Stream.concat(
                Stream.ofNullable(interfaces),
                Stream.of(interfaceTypes).flatMap(item -> Stream.ofNullable(item.getInterfaces()))
        )
                .flatMap(Collection::stream)
                .collect(Collectors.toCollection(LinkedHashSet::new));

        fieldDefinitions = Stream.concat(
                Stream.ofNullable(fieldDefinitions),
                Stream.of(interfaceTypes).flatMap(item -> Stream.ofNullable(item.getFields()))
        )
                .flatMap(Collection::stream)
                .filter(distinctByKey(FieldDefinition::getName))
                .collect(Collectors.toCollection(LinkedHashSet::new));
        return this;
    }

    public String getName() {
        return name;
    }

    public InterfaceType setName(String name) {
        this.name = name;
        return this;
    }

    public Collection<String> getInterfaces() {
        return interfaces;
    }

    public InterfaceType setInterfaces(Collection<String> interfaces) {
        this.interfaces = interfaces;
        return this;
    }

    public Collection<Directive> getDirectives() {
        return directives;
    }

    public InterfaceType setDirectives(Collection<Directive> directives) {
        if (directives != null) {
            this.directives = new LinkedHashSet<>(directives);
        }
        return this;
    }

    public InterfaceType addDirective(Directive directive) {
        if (this.directives == null) {
            this.directives = new LinkedHashSet<>();
        }
        this.directives.add(directive);
        return this;
    }

    public InterfaceType addDirectives(Collection<Directive> directives) {
        if (this.directives == null) {
            this.directives = new LinkedHashSet<>();
        }
        this.directives.addAll(directives);
        return this;
    }

    public Collection<FieldDefinition> getFields() {
        return fieldDefinitions;
    }

    public InterfaceType setFields(Collection<FieldDefinition> fieldDefinitions) {
        this.fieldDefinitions = fieldDefinitions;
        return this;
    }

    public InterfaceType addFields(Collection<FieldDefinition> fieldDefinitions) {
        if (this.fieldDefinitions == null) {
            this.fieldDefinitions = new LinkedHashSet<>();
        }
        this.fieldDefinitions.addAll(fieldDefinitions);
        return this;
    }

    public String getDescription() {
        return description;
    }

    public InterfaceType setDescription(String description) {
        this.description = description;
        return this;
    }

    @Override
    public boolean isInterface() {
        return true;
    }

    @Override
    public String toString() {
        ST st = stGroupFile.getInstanceOf("interfaceTypeDefinition");
        st.add("interfaceType", this);
        return st.render();
    }
}
