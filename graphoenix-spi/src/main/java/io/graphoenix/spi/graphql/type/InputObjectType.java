package io.graphoenix.spi.graphql.type;

import com.google.common.collect.Iterators;
import graphql.parser.antlr.GraphqlParser;
import io.graphoenix.spi.graphql.AbstractDefinition;
import io.graphoenix.spi.graphql.Definition;
import io.graphoenix.spi.graphql.common.ObjectValueWithVariable;
import io.graphoenix.spi.graphql.common.ValueWithVariable;
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
import static io.graphoenix.spi.utils.StreamUtil.distinctByKey;

public class InputObjectType extends AbstractDefinition implements Definition {

    private final STGroupFile stGroupFile = new STGroupFile("stg/type/InputObjectType.stg");
    private final Map<String, InputValue> inputValueMap = new LinkedHashMap<>();

    public InputObjectType() {
        super();
    }

    public InputObjectType(String name) {
        super(name);
    }

    public InputObjectType(GraphqlParser.InputObjectTypeDefinitionContext inputObjectTypeDefinitionContext) {
        super(inputObjectTypeDefinitionContext.name(), inputObjectTypeDefinitionContext.description(), inputObjectTypeDefinitionContext.directives());
        if (inputObjectTypeDefinitionContext.inputObjectValueDefinitions() != null) {
            setInputValues(
                    inputObjectTypeDefinitionContext.inputObjectValueDefinitions().inputValueDefinition().stream()
                            .map(InputValue::new)
                            .collect(Collectors.toList())
            );
        }
    }

    public InputObjectType(GraphqlParser.InputObjectTypeExtensionDefinitionContext inputObjectTypeExtensionDefinitionContext) {
        super(inputObjectTypeExtensionDefinitionContext.name(), null, inputObjectTypeExtensionDefinitionContext.directives());
        if (inputObjectTypeExtensionDefinitionContext.extensionInputObjectValueDefinitions() != null) {
            setInputValues(
                    inputObjectTypeExtensionDefinitionContext.extensionInputObjectValueDefinitions().inputValueDefinition().stream()
                            .map(InputValue::new)
                            .collect(Collectors.toList())
            );
        }
    }

    public InputObjectType(TypeElement typeElement, Types types) {
        super(typeElement);
        setInputValues(
                typeElement.getEnclosedElements().stream()
                        .filter(element -> element.getKind().equals(ElementKind.FIELD))
                        .filter(element -> element.getAnnotation(Ignore.class) == null)
                        .map(element -> new InputValue((VariableElement) element, types))
                        .collect(Collectors.toList())
        );
    }

    public InputObjectType merge(GraphqlParser.InputObjectTypeDefinitionContext... inputObjectTypeDefinitionContexts) {
        return merge(
                Stream.of(inputObjectTypeDefinitionContexts)
                        .map(InputObjectType::new)
                        .toArray(InputObjectType[]::new)
        );
    }

    public InputObjectType merge(GraphqlParser.InputObjectTypeExtensionDefinitionContext... inputObjectTypeExtensionDefinitionContexts) {
        return merge(
                Stream.of(inputObjectTypeExtensionDefinitionContexts)
                        .map(InputObjectType::new)
                        .toArray(InputObjectType[]::new)
        );
    }

    public InputObjectType merge(InputObjectType... inputObjectTypes) {
        super.merge(inputObjectTypes);
        inputValueMap.putAll(
                (Map<? extends String, ? extends InputValue>) Stream
                        .concat(
                                Stream.ofNullable(inputValueMap.values()),
                                Stream.of(inputObjectTypes)
                                        .flatMap(item -> Stream.ofNullable(item.getInputValues()))
                        )
                        .flatMap(Collection::stream)
                        .filter(distinctByKey(InputValue::getName))
                        .collect(
                                Collectors.toMap(
                                        InputValue::getName,
                                        inputValue -> inputValue,
                                        (x, y) -> y,
                                        LinkedHashMap::new
                                )
                        )
        );
        return this;
    }

    public InputValue getInputValue(String name) {
        return inputValueMap.get(name);
    }

    public InputValue getInputValue(int index) {
        return Iterators.get(inputValueMap.values().iterator(), index);
    }

    public Optional<InputValue> getInputValueOrEmpty(String name) {
        return Optional.ofNullable(getInputValue(name));
    }

    public Collection<InputValue> getInputValues() {
        return inputValueMap.values();
    }

    public InputObjectType setInputValues(Collection<InputValue> inputValues) {
        this.inputValueMap.clear();
        return addInputValues(inputValues);
    }

    public InputObjectType addInputValues(Collection<InputValue> inputValues) {
        this.inputValueMap.putAll(
                (Map<? extends String, ? extends InputValue>) inputValues.stream()
                        .collect(
                                Collectors.toMap(
                                        InputValue::getName,
                                        inputValue -> inputValue,
                                        (x, y) -> y,
                                        LinkedHashMap::new
                                )
                        )
        );
        return this;
    }

    public InputObjectType addInputValue(InputValue inputValue) {
        this.inputValueMap.put(inputValue.getName(), inputValue);
        return this;
    }

    public boolean isInputInterface() {
        return hasDirective(DIRECTIVE_INTERFACE_NAME);
    }

    public Collection<String> getInterfaces() {
        return Stream.ofNullable(getDirective(DIRECTIVE_IMPLEMENTS_NAME))
                .flatMap(directive ->
                        directive.getArgumentOrEmpty(DIRECTIVE_IMPLEMENTS_ARGUMENT_INTERFACES_NAME).stream()
                                .filter(ValueWithVariable::isArray)
                                .flatMap(valueWithVariable -> valueWithVariable.asArray().getValueWithVariables().stream())
                                .filter(ValueWithVariable::isString)
                                .map(valueWithVariable -> valueWithVariable.asString().getString())
                )
                .collect(Collectors.toList());
    }

    public boolean isInvokesInput() {
        return hasDirective(DIRECTIVE_INVOKES_NAME);
    }

    public List<ObjectValueWithVariable> getInputInvokes() {
        return Stream.ofNullable(getDirective(DIRECTIVE_INVOKES_NAME))
                .flatMap(directive ->
                        directive.getArgumentOrEmpty(DIRECTIVE_INVOKES_METHODS_NAME).stream()
                                .filter(ValueWithVariable::isArray)
                                .map(ValueWithVariable::asArray)
                                .flatMap(arrayValueWithVariable -> arrayValueWithVariable.getValueWithVariables().stream())
                                .filter(ValueWithVariable::isObject)
                                .map(ValueWithVariable::asObject)
                )
                .collect(Collectors.toList());
    }

    @Override
    public boolean isInputObject() {
        return true;
    }

    @Override
    public String toString() {
        ST st = stGroupFile.getInstanceOf("inputObjectTypeDefinition");
        st.add("inputObjectType", this);
        return st.render();
    }
}
