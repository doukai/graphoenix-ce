package io.graphoenix.spi.graphql.type;

import graphql.parser.antlr.GraphqlParser;
import io.graphoenix.spi.graphql.AbstractDefinition;
import io.graphoenix.spi.graphql.Definition;
import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroupFile;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.stream.Collectors;

public class DirectiveDefinition extends AbstractDefinition implements Definition {

    private final STGroupFile stGroupFile = new STGroupFile("stg/type/DirectiveDefinition.stg");
    private final Map<String, InputValue> argumentMap = new LinkedHashMap<>();
    private final Collection<String> directiveLocations = new LinkedHashSet<>();

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
        return argumentMap.values();
    }

    public DirectiveDefinition setArguments(Collection<InputValue> arguments) {
        this.argumentMap.clear();
        return addArguments(arguments);
    }

    public DirectiveDefinition addArguments(Collection<InputValue> arguments) {
        this.argumentMap.putAll(
                arguments.stream()
                        .collect(
                                Collectors.toMap(
                                        InputValue::getName,
                                        inputValue -> inputValue
                                )
                        )
        );
        return this;
    }

    public DirectiveDefinition addArgument(InputValue argument) {
        this.argumentMap.put(argument.getName(), argument);
        return this;
    }

    public Collection<String> getDirectiveLocations() {
        return directiveLocations;
    }

    public DirectiveDefinition setDirectiveLocations(Collection<String> directiveLocations) {
        this.directiveLocations.clear();
        this.directiveLocations.addAll(directiveLocations);
        return this;
    }

    @Override
    public boolean isDirective() {
        return true;
    }

    @Override
    public String toString() {
        ST st = stGroupFile.getInstanceOf("directiveDefinitionDefinition");
        st.add("directiveDefinition", this);
        return st.render();
    }
}
