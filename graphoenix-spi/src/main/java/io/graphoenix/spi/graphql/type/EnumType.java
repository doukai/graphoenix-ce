package io.graphoenix.spi.graphql.type;

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

import static io.graphoenix.spi.utils.ElementUtil.getNameFromElement;
import static io.graphoenix.spi.utils.StreamUtil.distinctByKey;

public class EnumType extends AbstractDefinition implements Definition {

    private final STGroupFile stGroupFile = new STGroupFile("stg/type/EnumType.stg");
    private Map<String, EnumValue> enumValueMap;

    public EnumType() {
        super();
        this.enumValueMap = new LinkedHashMap<>();
    }

    public EnumType(String name) {
        super(name);
        this.enumValueMap = new LinkedHashMap<>();
    }

    public EnumType(GraphqlParser.EnumTypeDefinitionContext enumTypeDefinitionContext) {
        super(enumTypeDefinitionContext.name(), enumTypeDefinitionContext.description(), enumTypeDefinitionContext.directives());
        if (enumTypeDefinitionContext.enumValueDefinitions() != null) {
            this.enumValueMap = enumTypeDefinitionContext.enumValueDefinitions().enumValueDefinition().stream()
                    .map(EnumValue::new)
                    .collect(
                            Collectors.toMap(
                                    EnumValue::getName,
                                    enumValue -> enumValue,
                                    (x, y) -> y,
                                    LinkedHashMap::new
                            )
                    );
        }
    }

    public EnumType(TypeElement typeElement) {
        super(typeElement);
        this.enumValueMap = typeElement.getEnclosedElements().stream()
                .filter(element -> element.getKind().equals(ElementKind.ENUM_CONSTANT))
                .filter(element -> element.getAnnotation(Ignore.class) == null)
                .map(element -> new EnumValue((VariableElement) element))
                .collect(
                        Collectors.toMap(
                                EnumValue::getName,
                                enumValue -> enumValue,
                                (x, y) -> y,
                                LinkedHashMap::new
                        )
                );
    }

    public EnumType merge(GraphqlParser.EnumTypeDefinitionContext... enumTypeDefinitionContexts) {
        return merge(
                Stream.of(enumTypeDefinitionContexts)
                        .map(EnumType::new)
                        .toArray(EnumType[]::new)
        );
    }

    public EnumType merge(EnumType... enumTypes) {
        super.merge(enumTypes);
        enumValueMap.putAll(
                Stream
                        .concat(
                                Stream.ofNullable(enumValueMap.values()),
                                Stream.of(enumTypes).flatMap(item -> Stream.ofNullable(item.getEnumValues()))
                        )
                        .flatMap(Collection::stream)
                        .filter(distinctByKey(EnumValue::getName))
                        .collect(
                                Collectors.toMap(
                                        EnumValue::getName,
                                        enumValue -> enumValue
                                )
                        )
        );
        return this;
    }

    public EnumValue getEnumValue(String name) {
        return enumValueMap.get(name);
    }

    public Collection<EnumValue> getEnumValues() {
        return enumValueMap.values();
    }

    public EnumType setEnumValues(Collection<EnumValue> enumValues) {
        enumValueMap.clear();
        return addEnumValues(enumValues);
    }

    public EnumType addEnumValues(Collection<EnumValue> enumValues) {
        this.enumValueMap.putAll(
                enumValues.stream()
                        .collect(
                                Collectors.toMap(
                                        EnumValue::getName,
                                        enumValue -> enumValue
                                )
                        )
        );
        return this;
    }

    public EnumType addEnumValue(EnumValue enumValue) {
        this.enumValueMap.put(enumValue.getName(), enumValue);
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
