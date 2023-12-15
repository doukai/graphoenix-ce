package io.graphoenix.spi.graphql.type;

import graphql.parser.antlr.GraphqlParser;
import io.graphoenix.spi.graphql.Definition;
import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroupFile;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.stream.Collectors;

import static io.graphoenix.spi.utils.DocumentUtil.getStringValue;

public class DirectiveDefinition implements Definition {

    private final STGroupFile stGroupFile = new STGroupFile("stg/type/DirectiveDefinition.stg");
    private String name;
    private Collection<InputValue> arguments;
    private Collection<String> directiveLocations;
    private String description;

    public DirectiveDefinition() {
    }

    public DirectiveDefinition(String name) {
        this.name = name;
    }

    public DirectiveDefinition(GraphqlParser.DirectiveDefinitionContext directiveDefinitionContext) {
        this.name = directiveDefinitionContext.name().getText();
        if (directiveDefinitionContext.argumentsDefinition() != null) {
            this.arguments = directiveDefinitionContext.argumentsDefinition().inputValueDefinition().stream()
                    .map(InputValue::new)
                    .collect(Collectors.toCollection(LinkedHashSet::new));
        }
        this.directiveLocations = directiveLocationList(directiveDefinitionContext.directiveLocations());
        if (directiveDefinitionContext.description() != null) {
            this.description = getStringValue(directiveDefinitionContext.description().StringValue());
        }
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

    public String getName() {
        return name;
    }

    public DirectiveDefinition setName(String name) {
        this.name = name;
        return this;
    }

    public Collection<InputValue> getArguments() {
        return arguments;
    }

    public DirectiveDefinition setArguments(Collection<InputValue> arguments) {
        this.arguments = arguments;
        return this;
    }

    public Collection<String> getDirectiveLocations() {
        return directiveLocations;
    }

    public DirectiveDefinition setDirectiveLocations(Collection<String> directiveLocations) {
        this.directiveLocations = directiveLocations;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public DirectiveDefinition setDescription(String description) {
        this.description = description;
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
