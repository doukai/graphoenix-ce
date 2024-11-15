package io.graphoenix.spi.graphql.type;

import graphql.parser.antlr.GraphqlParser;
import io.graphoenix.spi.graphql.AbstractDefinition;
import io.graphoenix.spi.graphql.Definition;
import io.graphoenix.spi.graphql.common.ObjectValueWithVariable;
import io.graphoenix.spi.graphql.common.ValueWithVariable;
import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroupFile;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static io.graphoenix.spi.constant.Hammurabi.*;

public class DirectiveDefinition extends AbstractDefinition implements Definition {

    private final STGroupFile stGroupFile = new STGroupFile("stg/type/DirectiveDefinition.stg");
    private Map<String, InputValue> argumentMap;
    private Collection<String> directiveLocations;

    public DirectiveDefinition() {
        super();
    }

    public DirectiveDefinition(String name) {
        super(name);
    }

    public DirectiveDefinition(GraphqlParser.DirectiveDefinitionContext directiveDefinitionContext) {
        super(directiveDefinitionContext.name(), directiveDefinitionContext.description());
        if (directiveDefinitionContext.argumentsDefinition() != null) {
            setArguments(
                    directiveDefinitionContext.argumentsDefinition().inputValueDefinition().stream()
                            .map(InputValue::new)
                            .collect(Collectors.toList())
            );
        }
        setDirectiveLocations(directiveLocationList(directiveDefinitionContext.directiveLocations()));
    }

    public Collection<String> directiveLocationList(GraphqlParser.DirectiveLocationsContext directiveLocationsContext) {
        Collection<String> directiveLocationList = new LinkedHashSet<>();
        if (directiveLocationsContext.directiveLocation() != null) {
            directiveLocationList.add(directiveLocationsContext.directiveLocation().name().getText());
        }
        if (directiveLocationsContext.directiveLocations() != null) {
            directiveLocationList.addAll(directiveLocationList(directiveLocationsContext.directiveLocations()));
        }
        return directiveLocationList;
    }

    public Collection<InputValue> getArguments() {
        return Optional.ofNullable(argumentMap).map(Map::values).orElse(null);
    }

    public DirectiveDefinition setArguments(Collection<InputValue> arguments) {
        if (arguments != null) {
            this.argumentMap = arguments.stream()
                    .collect(
                            Collectors.toMap(
                                    InputValue::getName,
                                    inputValue -> inputValue,
                                    (x, y) -> y,
                                    LinkedHashMap::new
                            )
                    );
        }
        return this;
    }

    public DirectiveDefinition addArguments(Collection<InputValue> arguments) {
        if (argumentMap == null) {
            argumentMap = new LinkedHashMap<>();
        }
        this.argumentMap.putAll(
                (Map<? extends String, ? extends InputValue>) arguments.stream()
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

    public DirectiveDefinition addArgument(InputValue argument) {
        if (argumentMap == null) {
            argumentMap = new LinkedHashMap<>();
        }
        this.argumentMap.put(argument.getName(), argument);
        return this;
    }

    public Collection<String> getDirectiveLocations() {
        return directiveLocations;
    }

    public DirectiveDefinition setDirectiveLocations(Collection<String> directiveLocations) {
        if (directiveLocations != null) {
            if (directiveLocations.isEmpty()) {
                this.directiveLocations = null;
            } else {
                this.directiveLocations = new LinkedHashSet<>(directiveLocations);
            }
        }
        return this;
    }

    @Override
    public boolean isDirective() {
        return true;
    }

    @Override
    public Optional<String> getPackageName() {
        return Stream.ofNullable(getArguments())
                .flatMap(Collection::stream)
                .filter(inputValue -> inputValue.getName().equals(DIRECTIVE_PACKAGE_NAME))
                .findFirst()
                .map(inputValue -> inputValue.getDefaultValue().asString().getString());
    }

    public List<ObjectValueWithVariable> getInvokes() {
        return Stream.ofNullable(getArguments())
                .flatMap(Collection::stream)
                .filter(inputValue -> inputValue.getName().equals(DIRECTIVE_INVOKES_NAME))
                .flatMap(inputValue ->
                        inputValue.getDefaultValue().asObject().getValueWithVariableOrEmpty(DIRECTIVE_INVOKES_METHODS_NAME).stream()
                                .filter(ValueWithVariable::isArray)
                                .map(ValueWithVariable::asArray)
                                .flatMap(arrayValueWithVariable -> arrayValueWithVariable.getValueWithVariables().stream())
                                .filter(ValueWithVariable::isObject)
                                .map(ValueWithVariable::asObject)
                )
                .collect(Collectors.toList());
    }

    @Override
    public String toString() {
        ST st = stGroupFile.getInstanceOf("directiveDefinitionDefinition");
        st.add("directiveDefinition", this);
        return st.render();
    }
}
