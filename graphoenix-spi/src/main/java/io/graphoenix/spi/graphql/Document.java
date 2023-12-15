package io.graphoenix.spi.graphql;

import graphql.parser.antlr.GraphqlParser;
import io.graphoenix.spi.graphql.operation.FragmentDefinition;
import io.graphoenix.spi.graphql.operation.Operation;
import io.graphoenix.spi.graphql.type.*;
import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroupFile;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.stream.Collectors;

public class Document {

    private final STGroupFile stGroupFile = new STGroupFile("stg/Document.stg");

    private Collection<Definition> definitions;

    public Collection<Definition> getDefinitions() {
        return definitions;
    }

    public Document(GraphqlParser.DocumentContext documentContext) {
        this.definitions = documentContext.definition().stream()
                .map(definitionContext -> {
                            if (definitionContext.operationDefinition() != null) {
                                return new Operation(definitionContext.operationDefinition());
                            } else if (definitionContext.fragmentDefinition() != null) {
                                return new FragmentDefinition(definitionContext.fragmentDefinition());
                            } else if (definitionContext.typeSystemDefinition() != null) {
                                if (definitionContext.typeSystemDefinition().schemaDefinition() != null) {
                                    return new Schema(definitionContext.typeSystemDefinition().schemaDefinition());
                                } else if (definitionContext.typeSystemDefinition().typeDefinition() != null) {
                                    if (definitionContext.typeSystemDefinition().typeDefinition().scalarTypeDefinition() != null) {
                                        return new ScalarType(definitionContext.typeSystemDefinition().typeDefinition().scalarTypeDefinition());
                                    } else if (definitionContext.typeSystemDefinition().typeDefinition().enumTypeDefinition() != null) {
                                        return new EnumType(definitionContext.typeSystemDefinition().typeDefinition().enumTypeDefinition());
                                    } else if (definitionContext.typeSystemDefinition().typeDefinition().objectTypeDefinition() != null) {
                                        return new ObjectType(definitionContext.typeSystemDefinition().typeDefinition().objectTypeDefinition());
                                    } else if (definitionContext.typeSystemDefinition().typeDefinition().interfaceTypeDefinition() != null) {
                                        return new InterfaceType(definitionContext.typeSystemDefinition().typeDefinition().interfaceTypeDefinition());
                                    } else if (definitionContext.typeSystemDefinition().typeDefinition().inputObjectTypeDefinition() != null) {
                                        return new InputObjectType(definitionContext.typeSystemDefinition().typeDefinition().inputObjectTypeDefinition());
                                    }
                                } else if (definitionContext.typeSystemDefinition().directiveDefinition() != null) {
                                    return new DirectiveDefinition(definitionContext.typeSystemDefinition().directiveDefinition());
                                }
                            }
                            throw new RuntimeException("unsupported document definition: " + definitionContext.getText());
                        }
                )
                .collect(Collectors.toList());
    }

    public Document setDefinitions(Collection<Definition> definitions) {
        this.definitions = definitions;
        return this;
    }

    public Document addDefinition(Definition definition) {
        if (this.definitions == null) {
            this.definitions = new LinkedHashSet<>();
        }
        this.definitions.add(definition);
        return this;
    }

    public Document addDefinitions(Collection<Definition> definitions) {
        if (this.definitions == null) {
            this.definitions = definitions;
        }
        this.definitions.addAll(definitions);
        return this;
    }

    @Override
    public String toString() {
        ST st = stGroupFile.getInstanceOf("documentDefinition");
        st.add("document", this);
        return st.render();
    }
}
