package io.graphoenix.spi.graphql.type;

import graphql.parser.antlr.GraphqlParser;
import io.graphoenix.spi.graphql.Definition;
import io.graphoenix.spi.graphql.AbstractDefinition;
import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroupFile;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static io.graphoenix.spi.constant.Hammurabi.*;
import static io.graphoenix.spi.utils.DocumentUtil.getImplementsInterfaces;
import static io.graphoenix.spi.utils.StreamUtil.distinctByKey;

public class ObjectType extends AbstractDefinition implements Definition {

    private final STGroupFile stGroupFile = new STGroupFile("stg/type/ObjectType.stg");
    private Collection<String> interfaces;
    private Map<String, FieldDefinition> fieldDefinitionMap;

    public ObjectType() {
        super();
        this.interfaces = new LinkedHashSet<>();
        this.fieldDefinitionMap = new LinkedHashMap<>();
    }

    public ObjectType(String name) {
        super(name);
        this.interfaces = new LinkedHashSet<>();
        this.fieldDefinitionMap = new LinkedHashMap<>();
    }

    public ObjectType(GraphqlParser.ObjectTypeDefinitionContext objectTypeDefinitionContext) {
        super(objectTypeDefinitionContext.name(), objectTypeDefinitionContext.description(), objectTypeDefinitionContext.directives());
        if (objectTypeDefinitionContext.implementsInterfaces() != null) {
            this.interfaces = getImplementsInterfaces(objectTypeDefinitionContext.implementsInterfaces())
                    .collect(Collectors.toCollection(LinkedHashSet::new));
        }
        if (objectTypeDefinitionContext.fieldsDefinition() != null) {
            this.fieldDefinitionMap = objectTypeDefinitionContext.fieldsDefinition().fieldDefinition().stream()
                    .map(FieldDefinition::new)
                    .collect(
                            Collectors.toMap(
                                    FieldDefinition::getName,
                                    fieldDefinition -> fieldDefinition,
                                    (x, y) -> y,
                                    LinkedHashMap::new
                            )
                    );
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
        super.merge(objectTypes);
        interfaces.addAll(
                Stream
                        .concat(
                                Stream.ofNullable(interfaces),
                                Stream.of(objectTypes).flatMap(item -> Stream.ofNullable(item.getInterfaces()))
                        )
                        .flatMap(Collection::stream)
                        .collect(Collectors.toCollection(LinkedHashSet::new))
        );

        fieldDefinitionMap.putAll(
                Stream
                        .concat(
                                Stream.ofNullable(fieldDefinitionMap.values()),
                                Stream.of(objectTypes).flatMap(item -> Stream.ofNullable(item.getFields()))
                        )
                        .flatMap(Collection::stream)
                        .filter(distinctByKey(FieldDefinition::getName))
                        .collect(
                                Collectors.toMap(
                                        FieldDefinition::getName,
                                        fieldDefinition -> fieldDefinition
                                )
                        )
        );
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
        this.interfaces.add(interfaceType);
        return this;
    }

    public FieldDefinition getField(String name) {
        return fieldDefinitionMap.get(name);
    }

    public Collection<FieldDefinition> getFields() {
        return fieldDefinitionMap.values();
    }

    public ObjectType setFields(Collection<FieldDefinition> fieldDefinitions) {
        this.fieldDefinitionMap.clear();
        return addFields(fieldDefinitions);
    }

    public ObjectType addFields(Collection<FieldDefinition> fieldDefinitions) {
        this.fieldDefinitionMap.putAll(
                fieldDefinitions.stream()
                        .collect(
                                Collectors.toMap(
                                        FieldDefinition::getName,
                                        fieldDefinition -> fieldDefinition
                                )
                        )
        );
        return this;
    }

    public ObjectType addField(FieldDefinition fieldDefinition) {
        this.fieldDefinitionMap.put(fieldDefinition.getName(), fieldDefinition);
        return this;
    }

    public Optional<FieldDefinition> getIDField() {
        return Optional.of(fieldDefinitionMap.values())
                .flatMap(fieldDefinitions ->
                        fieldDefinitions.stream()
                                .filter(fieldDefinition -> fieldDefinition.getType().getTypeName().getName().equals(SCALA_ID_NAME))
                                .findFirst()
                );
    }

    public Optional<FieldDefinition> getCursorField() {
        return Optional.of(fieldDefinitionMap.values())
                .flatMap(fieldDefinitions ->
                        fieldDefinitions.stream()
                                .filter(fieldDefinition -> fieldDefinition.hasDirective(DIRECTIVE_CURSOR_NAME))
                                .findFirst()
                );
    }

    @Override
    public boolean isObject() {
        return true;
    }

    @Override
    public String toString() {
        ST st = stGroupFile.getInstanceOf("objectTypeDefinition");
        st.add("objectType", this);
        return st.render();
    }
}
