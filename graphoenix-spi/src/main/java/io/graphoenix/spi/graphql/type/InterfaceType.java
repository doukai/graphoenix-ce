package io.graphoenix.spi.graphql.type;

import com.google.common.collect.Iterators;
import graphql.parser.antlr.GraphqlParser;
import io.graphoenix.spi.graphql.AbstractDefinition;
import io.graphoenix.spi.graphql.Definition;
import io.graphoenix.spi.graphql.FieldsType;
import io.graphoenix.spi.graphql.common.Directive;
import org.eclipse.microprofile.graphql.Ignore;
import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroupFile;

import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.util.Types;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static io.graphoenix.spi.constant.Hammurabi.DIRECTIVE_CONTAINER_NAME;
import static io.graphoenix.spi.utils.DocumentUtil.getImplementsInterfaces;
import static io.graphoenix.spi.utils.ElementUtil.getNameFromElement;
import static io.graphoenix.spi.utils.StreamUtil.distinctByKey;

public class InterfaceType extends AbstractDefinition implements Definition, FieldsType {

    private final STGroupFile stGroupFile = new STGroupFile("stg/type/InterfaceType.stg");
    private final Collection<String> interfaces = new LinkedHashSet<>();
    private final Map<String, FieldDefinition> fieldDefinitionMap = new LinkedHashMap<>();

    public InterfaceType() {
        super();
    }

    public InterfaceType(String name) {
        super(name);
    }

    public InterfaceType(GraphqlParser.InterfaceTypeDefinitionContext interfaceTypeDefinitionContext) {
        super(interfaceTypeDefinitionContext.name(), interfaceTypeDefinitionContext.description(), interfaceTypeDefinitionContext.directives());
        if (interfaceTypeDefinitionContext.implementsInterfaces() != null) {
            setInterfaces(
                    getImplementsInterfaces(interfaceTypeDefinitionContext.implementsInterfaces())
                            .collect(Collectors.toList())
            );
        }
        if (interfaceTypeDefinitionContext.fieldsDefinition() != null) {
            setFields(
                    interfaceTypeDefinitionContext.fieldsDefinition().fieldDefinition().stream()
                            .map(FieldDefinition::new)
                            .collect(Collectors.toList())
            );
        }
    }

    public InterfaceType(TypeElement typeElement, Types types) {
        super(typeElement);
        addDirective(new Directive(DIRECTIVE_CONTAINER_NAME));
        setInterfaces(
                typeElement.getInterfaces().stream()
                        .map(typeMirror -> getNameFromElement(types.asElement(typeMirror)))
                        .collect(Collectors.toList())
        );
        setFields(
                typeElement.getEnclosedElements().stream()
                        .filter(element -> element.getKind().equals(ElementKind.FIELD))
                        .filter(element -> element.getAnnotation(Ignore.class) == null)
                        .map(element -> new FieldDefinition((VariableElement) element, types))
                        .collect(Collectors.toList())
        );
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
        interfaces.addAll(
                Stream
                        .concat(
                                Stream.ofNullable(interfaces),
                                Stream.of(interfaceTypes).flatMap(item -> Stream.ofNullable(item.getInterfaces()))
                        )
                        .flatMap(Collection::stream)
                        .collect(Collectors.toCollection(LinkedHashSet::new))
        );

        fieldDefinitionMap.putAll(
                (Map<? extends String, ? extends FieldDefinition>) Stream
                        .concat(
                                Stream.ofNullable(fieldDefinitionMap.values()),
                                Stream.of(interfaceTypes).flatMap(item -> Stream.ofNullable(item.getFields()))
                        )
                        .flatMap(Collection::stream)
                        .filter(distinctByKey(FieldDefinition::getName))
                        .collect(
                                Collectors.toMap(
                                        FieldDefinition::getName,
                                        fieldDefinition -> fieldDefinition,
                                        (x, y) -> y,
                                        LinkedHashMap::new
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
        this.interfaces.clear();
        this.interfaces.addAll(interfaces);
        return this;
    }

    @Override
    public FieldDefinition getField(String name) {
        return fieldDefinitionMap.get(name);
    }

    public FieldDefinition getField(int index) {
        return Iterators.get(fieldDefinitionMap.values().iterator(), index);
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
                (Map<? extends String, ? extends FieldDefinition>) fieldDefinitions.stream()
                        .collect(
                                Collectors.toMap(
                                        FieldDefinition::getName,
                                        fieldDefinition -> fieldDefinition,
                                        (x, y) -> y,
                                        LinkedHashMap::new
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
