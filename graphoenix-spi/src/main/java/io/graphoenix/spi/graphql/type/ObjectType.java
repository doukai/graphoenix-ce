package io.graphoenix.spi.graphql.type;

import graphql.parser.antlr.GraphqlParser;
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

public class ObjectType {

    private final STGroupFile stGroupFile = new STGroupFile("stg/type/ObjectType.stg");
    private String name;
    private Collection<String> interfaces;
    private Collection<Directive> directives;
    private Collection<FieldDefinition> fieldDefinitions;
    private String description;

    public ObjectType() {
    }

    public ObjectType(String name) {
        this.name = name;
    }

    public ObjectType(GraphqlParser.ObjectTypeDefinitionContext objectTypeDefinitionContext) {
        this.name = objectTypeDefinitionContext.name().getText();
        if (objectTypeDefinitionContext.description() != null) {
            this.description = getStringValue(objectTypeDefinitionContext.description().StringValue());
        }
        if (objectTypeDefinitionContext.implementsInterfaces() != null) {
            this.interfaces = getImplementsInterfaces(objectTypeDefinitionContext.implementsInterfaces())
                    .collect(Collectors.toCollection(LinkedHashSet::new));
        }
        if (objectTypeDefinitionContext.directives() != null) {
            this.directives = objectTypeDefinitionContext.directives().directive().stream()
                    .map(Directive::new)
                    .collect(Collectors.toCollection(LinkedHashSet::new));
        }
        if (objectTypeDefinitionContext.fieldsDefinition() != null) {
            this.fieldDefinitions = objectTypeDefinitionContext.fieldsDefinition().fieldDefinition().stream()
                    .map(FieldDefinition::new)
                    .collect(Collectors.toCollection(LinkedHashSet::new));
        }
    }

    public ObjectType merge(GraphqlParser.ObjectTypeDefinitionContext... objectTypeDefinitionContexts) {
        return merge(
                Stream.of(objectTypeDefinitionContexts)
                        .map(ObjectType::new)
                        .toArray(ObjectType[]::new)
        );
    }

    public ObjectType merge(ObjectType... objectTypes) {
        directives = Stream.concat(
                        Stream.ofNullable(directives),
                        Stream.of(objectTypes).flatMap(item -> Stream.ofNullable(item.getDirectives()))
                )
                .flatMap(Collection::stream)
                .filter(distinctByKey(Directive::getName))
                .collect(Collectors.toCollection(LinkedHashSet::new));

        interfaces = Stream.concat(
                        Stream.ofNullable(interfaces),
                        Stream.of(objectTypes).flatMap(item -> Stream.ofNullable(item.getInterfaces()))
                )
                .flatMap(Collection::stream)
                .collect(Collectors.toCollection(LinkedHashSet::new));

        fieldDefinitions = Stream.concat(
                        Stream.ofNullable(fieldDefinitions),
                        Stream.of(objectTypes).flatMap(item -> Stream.ofNullable(item.getFields()))
                )
                .flatMap(Collection::stream)
                .filter(distinctByKey(FieldDefinition::getName))
                .collect(Collectors.toCollection(LinkedHashSet::new));
        return this;
    }

    public String getName() {
        return name;
    }

    public ObjectType setName(String name) {
        this.name = name;
        return this;
    }

    public Collection<String> getInterfaces() {
        return interfaces;
    }

    public ObjectType setInterfaces(Collection<String> interfaces) {
        this.interfaces = interfaces;
        return this;
    }

    public ObjectType addInterface(String interfaceType) {
        if (this.interfaces == null) {
            this.interfaces = new LinkedHashSet<>();
        }
        this.interfaces.add(interfaceType);
        return this;
    }

    public Collection<Directive> getDirectives() {
        return directives;
    }

    public ObjectType setDirectives(Collection<Directive> directives) {
        if (directives != null) {
            this.directives = new LinkedHashSet<>(directives);
        }
        return this;
    }

    public ObjectType addDirective(Directive directive) {
        if (this.directives == null) {
            this.directives = new LinkedHashSet<>();
        }
        this.directives.add(directive);
        return this;
    }

    public ObjectType addDirectives(Collection<Directive> directives) {
        if (this.directives == null) {
            this.directives = new LinkedHashSet<>();
        }
        this.directives.addAll(directives);
        return this;
    }

    public Collection<FieldDefinition> getFields() {
        return fieldDefinitions;
    }

    public ObjectType setFields(Collection<FieldDefinition> fieldDefinitions) {
        this.fieldDefinitions = fieldDefinitions;
        return this;
    }

    public ObjectType addFields(Collection<FieldDefinition> fieldDefinitions) {
        if (this.fieldDefinitions == null) {
            this.fieldDefinitions = new LinkedHashSet<>();
        }
        this.fieldDefinitions.addAll(fieldDefinitions.stream().filter(fieldDefinition -> this.fieldDefinitions.stream().noneMatch(item -> item.getName().equals(fieldDefinition.getName()))).collect(Collectors.toCollection(LinkedHashSet::new)));
        return this;
    }

    public ObjectType addField(FieldDefinition fieldDefinition) {
        if (this.fieldDefinitions == null) {
            this.fieldDefinitions = new LinkedHashSet<>();
        }
        if (this.fieldDefinitions.stream().noneMatch(item -> item.getName().equals(fieldDefinition.getName()))) {
            this.fieldDefinitions.add(fieldDefinition);
        }
        return this;
    }

    public String getDescription() {
        return description;
    }

    public ObjectType setDescription(String description) {
        this.description = description;
        return this;
    }

    @Override
    public String toString() {
        ST st = stGroupFile.getInstanceOf("objectTypeDefinition");
        st.add("objectType", this);
        return st.render();
    }
}
