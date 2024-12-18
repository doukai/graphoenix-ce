package io.graphoenix.spi.graphql.type;

import graphql.parser.antlr.GraphqlParser;
import io.graphoenix.spi.graphql.AbstractDefinition;
import io.graphoenix.spi.graphql.Definition;
import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroupFile;

import java.util.stream.Stream;

public class ScalarType extends AbstractDefinition implements Definition {

    private final STGroupFile stGroupFile = new STGroupFile("stg/type/ScalarType.stg");

    public ScalarType() {
        super();
    }

    public ScalarType(String name) {
        super(name);
    }

    public ScalarType(GraphqlParser.ScalarTypeDefinitionContext scalarTypeDefinitionContext) {
        super(scalarTypeDefinitionContext.name(), scalarTypeDefinitionContext.description(), scalarTypeDefinitionContext.directives());
    }

    public ScalarType(GraphqlParser.ScalarTypeExtensionDefinitionContext scalarTypeExtensionDefinitionContext) {
        super(scalarTypeExtensionDefinitionContext.name(), null, scalarTypeExtensionDefinitionContext.directives());
        setExtension(true);
    }

    public ScalarType merge(GraphqlParser.ScalarTypeDefinitionContext... scalarTypeDefinitionContexts) {
        return merge(
                Stream.of(scalarTypeDefinitionContexts)
                        .map(ScalarType::new)
                        .toArray(ScalarType[]::new)
        );
    }

    public ScalarType merge(GraphqlParser.ScalarTypeExtensionDefinitionContext... scalarTypeExtensionDefinitionContexts) {
        return merge(
                Stream.of(scalarTypeExtensionDefinitionContexts)
                        .map(ScalarType::new)
                        .toArray(ScalarType[]::new)
        );
    }

    @Override
    public boolean isScalar() {
        return true;
    }

    @Override
    public String toString() {
        ST st = stGroupFile.getInstanceOf("scalarTypeDefinition");
        st.add("scalarType", this);
        return st.render();
    }
}
