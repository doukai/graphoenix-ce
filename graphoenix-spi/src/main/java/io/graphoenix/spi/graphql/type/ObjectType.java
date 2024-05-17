package io.graphoenix.spi.graphql.type;

import com.google.common.collect.Iterators;
import graphql.parser.antlr.GraphqlParser;
import io.graphoenix.spi.error.GraphQLErrors;
import io.graphoenix.spi.graphql.AbstractDefinition;
import io.graphoenix.spi.graphql.Definition;
import io.graphoenix.spi.graphql.FieldsType;
import org.eclipse.microprofile.graphql.Ignore;
import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroupFile;

import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.util.Types;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static io.graphoenix.spi.constant.Hammurabi.*;
import static io.graphoenix.spi.constant.Hammurabi.DIRECTIVE_DENY_ALL;
import static io.graphoenix.spi.error.GraphQLErrorType.ID_FIELD_DEFINITION_NOT_EXIST;
import static io.graphoenix.spi.utils.DocumentUtil.getImplementsInterfaces;
import static io.graphoenix.spi.utils.ElementUtil.getNameFromElement;
import static io.graphoenix.spi.utils.StreamUtil.distinctByKey;

public class ObjectType extends AbstractDefinition implements Definition, FieldsType {

    private final STGroupFile stGroupFile = new STGroupFile("stg/type/ObjectType.stg");
    private final Collection<String> interfaces = new LinkedHashSet<>();
    private final Map<String, FieldDefinition> fieldDefinitionMap = new LinkedHashMap<>();

    public ObjectType() {
        super();
    }

    public ObjectType(String name) {
        super(name);
    }

    public ObjectType(GraphqlParser.ObjectTypeDefinitionContext objectTypeDefinitionContext) {
        super(objectTypeDefinitionContext.name(), objectTypeDefinitionContext.description(), objectTypeDefinitionContext.directives());
        if (objectTypeDefinitionContext.implementsInterfaces() != null) {
            setInterfaces(
                    getImplementsInterfaces(objectTypeDefinitionContext.implementsInterfaces())
                            .collect(Collectors.toCollection(LinkedHashSet::new))
            );
        }
        if (objectTypeDefinitionContext.fieldsDefinition() != null) {
            setFields(
                    objectTypeDefinitionContext.fieldsDefinition().fieldDefinition().stream()
                            .map(FieldDefinition::new)
                            .collect(Collectors.toList())
            );
        }
    }

    public ObjectType(TypeElement typeElement, Types types) {
        super(typeElement);
        setInterfaces(
                typeElement.getInterfaces().stream()
                        .map(typeMirror -> getNameFromElement(types.asElement(typeMirror)))
                        .collect(Collectors.toCollection(LinkedHashSet::new))
        );
        setFields(
                typeElement.getEnclosedElements().stream()
                        .filter(element -> element.getKind().equals(ElementKind.FIELD))
                        .filter(element -> element.getAnnotation(Ignore.class) == null)
                        .map(element -> new FieldDefinition((VariableElement) element, types))
                        .collect(Collectors.toList())
        );
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
                (Map<? extends String, ? extends FieldDefinition>) Stream
                        .concat(
                                Stream.ofNullable(fieldDefinitionMap.values()),
                                Stream.of(objectTypes).flatMap(item -> Stream.ofNullable(item.getFields()))
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

    public ObjectType setInterfaces(Collection<String> interfaces) {
        this.interfaces.clear();
        this.interfaces.addAll(interfaces);
        return this;
    }

    public ObjectType addInterface(String interfaceType) {
        this.interfaces.add(interfaceType);
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

    public ObjectType setFields(Collection<FieldDefinition> fieldDefinitions) {
        this.fieldDefinitionMap.clear();
        return addFields(fieldDefinitions);
    }

    public ObjectType addFields(Collection<FieldDefinition> fieldDefinitions) {
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

    public FieldDefinition getIDFieldOrError() {
        return getIDField().orElseThrow(() -> new GraphQLErrors(ID_FIELD_DEFINITION_NOT_EXIST.bind(this.getName())));
    }

    public Optional<FieldDefinition> getCursorField() {
        return Optional.of(fieldDefinitionMap.values())
                .flatMap(fieldDefinitions ->
                        fieldDefinitions.stream()
                                .filter(fieldDefinition -> fieldDefinition.hasDirective(DIRECTIVE_CURSOR_NAME))
                                .findFirst()
                );
    }

    public boolean isPermitAll() {
        return hasDirective(DIRECTIVE_PERMIT_ALL);
    }

    public boolean isDenyAll() {
        return hasDirective(DIRECTIVE_DENY_ALL);
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
