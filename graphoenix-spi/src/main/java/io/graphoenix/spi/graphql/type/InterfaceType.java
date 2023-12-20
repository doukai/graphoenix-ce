package io.graphoenix.spi.graphql.type;

import graphql.parser.antlr.GraphqlParser;
import io.graphoenix.spi.graphql.AbstractDefinition;
import io.graphoenix.spi.graphql.Definition;
import io.graphoenix.spi.graphql.FieldsType;
import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroupFile;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static io.graphoenix.spi.utils.DocumentUtil.getImplementsInterfaces;
import static io.graphoenix.spi.utils.StreamUtil.distinctByKey;

public class InterfaceType extends AbstractDefinition implements Definition, FieldsType {

    private final STGroupFile stGroupFile = new STGroupFile("stg/type/InterfaceType.stg");
    private Collection<String> interfaces;
    private Map<String, FieldDefinition> fieldDefinitionMap;

    public InterfaceType() {
        super();
        this.interfaces = new LinkedHashSet<>();
        this.fieldDefinitionMap = new LinkedHashMap<>();
    }

    public InterfaceType(String name) {
        super(name);
        this.interfaces = new LinkedHashSet<>();
        this.fieldDefinitionMap = new LinkedHashMap<>();
    }

    public InterfaceType(GraphqlParser.InterfaceTypeDefinitionContext interfaceTypeDefinitionContext) {
        super(interfaceTypeDefinitionContext.name(), interfaceTypeDefinitionContext.description(), interfaceTypeDefinitionContext.directives());
        if (interfaceTypeDefinitionContext.implementsInterfaces() != null) {
            this.interfaces = getImplementsInterfaces(interfaceTypeDefinitionContext.implementsInterfaces())
                    .collect(Collectors.toCollection(LinkedHashSet::new));
        }
        if (interfaceTypeDefinitionContext.fieldsDefinition() != null) {
            this.fieldDefinitionMap = interfaceTypeDefinitionContext.fieldsDefinition().fieldDefinition().stream()
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

    public InterfaceType merge(GraphqlParser.InterfaceTypeDefinitionContext... interfaceTypeDefinitionContexts) {
        return merge(
                Stream.of(interfaceTypeDefinitionContexts)
                        .map(InterfaceType::new)
                        .toArray(InterfaceType[]::new)
        );
    }

    public InterfaceType merge(InterfaceType... interfaceTypes) {
        super.merge(interfaceTypes);
        interfaces = Stream
                .concat(
                        Stream.ofNullable(interfaces),
                        Stream.of(interfaceTypes).flatMap(item -> Stream.ofNullable(item.getInterfaces()))
                )
                .flatMap(Collection::stream)
                .collect(Collectors.toCollection(LinkedHashSet::new));

        fieldDefinitionMap.putAll(
                Stream
                        .concat(
                                Stream.ofNullable(fieldDefinitionMap.values()),
                                Stream.of(interfaceTypes).flatMap(item -> Stream.ofNullable(item.getFields()))
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

    @Override
    public Collection<String> getInterfaces() {
        return interfaces;
    }

    public InterfaceType setInterfaces(Collection<String> interfaces) {
        this.interfaces = interfaces;
        return this;
    }

    @Override
    public FieldDefinition getField(String name) {
        return fieldDefinitionMap.get(name);
    }

    @Override
    public Collection<FieldDefinition> getFields() {
        return fieldDefinitionMap.values();
    }

    public InterfaceType setFields(Collection<FieldDefinition> fieldDefinitions) {
        this.fieldDefinitionMap.clear();
        return addFields(fieldDefinitions);
    }

    public InterfaceType addFields(Collection<FieldDefinition> fieldDefinitions) {
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

    public InterfaceType addField(FieldDefinition fieldDefinition) {
        this.fieldDefinitionMap.put(fieldDefinition.getName(), fieldDefinition);
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
