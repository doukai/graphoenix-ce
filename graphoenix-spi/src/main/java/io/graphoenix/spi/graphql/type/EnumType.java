package io.graphoenix.spi.graphql.type;

import com.google.common.collect.Iterators;
import graphql.parser.antlr.GraphqlParser;
import io.graphoenix.spi.graphql.AbstractDefinition;
import io.graphoenix.spi.graphql.Definition;
import org.eclipse.microprofile.graphql.Ignore;
import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroupFile;

import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static io.graphoenix.spi.utils.StreamUtil.distinctByKey;

public class EnumType extends AbstractDefinition implements Definition {

    private final STGroupFile stGroupFile = new STGroupFile("stg/type/EnumType.stg");
    private final Map<String, EnumValueDefinition> enumValueMap = new LinkedHashMap<>();

    public EnumType() {
        super();
    }

    public EnumType(String name) {
        super(name);
    }

    public EnumType(GraphqlParser.EnumTypeDefinitionContext enumTypeDefinitionContext) {
        super(enumTypeDefinitionContext.name(), enumTypeDefinitionContext.description(), enumTypeDefinitionContext.directives());
        if (enumTypeDefinitionContext.enumValueDefinitions() != null) {
            setEnumValues(
                    enumTypeDefinitionContext.enumValueDefinitions().enumValueDefinition().stream()
                            .map(EnumValueDefinition::new)
                            .collect(Collectors.toList())
            );
        }
    }

    public EnumType(GraphqlParser.EnumTypeExtensionDefinitionContext enumTypeExtensionDefinitionContext) {
        super(enumTypeExtensionDefinitionContext.name(), null, enumTypeExtensionDefinitionContext.directives());
        if (enumTypeExtensionDefinitionContext.extensionEnumValueDefinitions() != null) {
            setEnumValues(
                    enumTypeExtensionDefinitionContext.extensionEnumValueDefinitions().enumValueDefinition().stream()
                            .map(EnumValueDefinition::new)
                            .collect(Collectors.toList())
            );
        }
    }

    public EnumType(TypeElement typeElement) {
        super(typeElement);
        setEnumValues(
                typeElement.getEnclosedElements().stream()
                        .filter(element -> element.getKind().equals(ElementKind.ENUM_CONSTANT))
                        .filter(element -> element.getAnnotation(Ignore.class) == null)
                        .map(element -> new EnumValueDefinition((VariableElement) element))
                        .collect(Collectors.toList())
        );
    }

    public EnumType merge(GraphqlParser.EnumTypeDefinitionContext... enumTypeDefinitionContexts) {
        return merge(
                Stream.of(enumTypeDefinitionContexts)
                        .map(EnumType::new)
                        .toArray(EnumType[]::new)
        );
    }

    public EnumType merge(GraphqlParser.EnumTypeExtensionDefinitionContext... enumTypeExtensionDefinitionContexts) {
        return merge(
                Stream.of(enumTypeExtensionDefinitionContexts)
                        .map(EnumType::new)
                        .toArray(EnumType[]::new)
        );
    }

    public EnumType merge(EnumType... enumTypes) {
        super.merge(enumTypes);
        enumValueMap.putAll(
                (Map<? extends String, ? extends EnumValueDefinition>) Stream
                        .concat(
                                Stream.ofNullable(enumValueMap.values()),
                                Stream.of(enumTypes).flatMap(item -> Stream.ofNullable(item.getEnumValues()))
                        )
                        .flatMap(Collection::stream)
                        .filter(distinctByKey(EnumValueDefinition::getName))
                        .collect(
                                Collectors.toMap(
                                        EnumValueDefinition::getName,
                                        enumValueDefinition -> enumValueDefinition,
                                        (x, y) -> y,
                                        LinkedHashMap::new
                                )
                        )
        );
        return this;
    }

    public EnumValueDefinition getEnumValue(String name) {
        return enumValueMap.get(name);
    }

    public Collection<EnumValueDefinition> getEnumValues() {
        return enumValueMap.values();
    }

    public EnumValueDefinition getEnumValue(int index) {
        return Iterators.get(enumValueMap.values().iterator(), index);
    }

    public EnumType setEnumValues(Collection<EnumValueDefinition> enumValueDefinitions) {
        enumValueMap.clear();
        return addEnumValues(enumValueDefinitions);
    }

    public EnumType addEnumValues(Collection<EnumValueDefinition> enumValueDefinitions) {
        this.enumValueMap.putAll(
                (Map<? extends String, ? extends EnumValueDefinition>) enumValueDefinitions.stream()
                        .collect(
                                Collectors.toMap(
                                        EnumValueDefinition::getName,
                                        enumValueDefinition -> enumValueDefinition,
                                        (x, y) -> y,
                                        LinkedHashMap::new
                                )
                        )
        );
        return this;
    }

    public EnumType addEnumValue(EnumValueDefinition enumValueDefinition) {
        this.enumValueMap.put(enumValueDefinition.getName(), enumValueDefinition);
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
