package io.graphoenix.spi.graphql.type;

import graphql.parser.antlr.GraphqlParser;
import io.graphoenix.spi.graphql.common.Directive;
import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroupFile;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.stream.Collectors;

import static io.graphoenix.spi.utils.DocumentUtil.getStringValue;

public class EnumValue {

    private final STGroupFile stGroupFile = new STGroupFile("stg/type/EnumValue.stg");
    private String name;
    private Collection<Directive> directives;
    private String description;

    public EnumValue() {
    }

    public EnumValue(String name) {
        this.name = name;
    }

    public EnumValue(GraphqlParser.EnumValueDefinitionContext enumValueDefinitionContext) {
        this.name = enumValueDefinitionContext.enumValue().enumValueName().getText();
        if (enumValueDefinitionContext.directives() != null) {
            this.directives = enumValueDefinitionContext.directives().directive().stream().map(Directive::new).collect(Collectors.toCollection(LinkedHashSet::new));
        }
        if (enumValueDefinitionContext.description() != null) {
            this.description = getStringValue(enumValueDefinitionContext.description().StringValue());
        }
    }

    public String getName() {
        return name;
    }

    public EnumValue setName(String name) {
        this.name = name;
        return this;
    }

    public Collection<Directive> getDirectives() {
        return directives;
    }

    public EnumValue setDirectives(Collection<Directive> directives) {
        if (directives != null) {
            this.directives = new LinkedHashSet<>(directives);
        }
        return this;
    }

    public EnumValue addDirectives(Collection<Directive> directives) {
        if (this.directives == null) {
            this.directives = new LinkedHashSet<>();
        }
        this.directives.addAll(directives);
        return this;
    }

    public String getDescription() {
        return description;
    }

    public EnumValue setDescription(String description) {
        this.description = description;
        return this;
    }

    @Override
    public String toString() {
        ST st = stGroupFile.getInstanceOf("enumValueDefinition");
        st.add("enumValue", this);
        return st.render();
    }
}
