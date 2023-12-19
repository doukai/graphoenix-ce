package io.graphoenix.spi.graphql;

import graphql.parser.antlr.GraphqlParser;
import io.graphoenix.spi.graphql.operation.FragmentDefinition;
import io.graphoenix.spi.graphql.operation.Operation;
import io.graphoenix.spi.graphql.type.*;

public interface Definition {

    String getName();

    default boolean isSchema() {
        return false;
    }

    default boolean isScalar() {
        return false;
    }

    default boolean isEnum() {
        return false;
    }

    default boolean isObject() {
        return false;
    }

    default boolean isInterface() {
        return false;
    }

    default boolean isInputObject() {
        return false;
    }

    default boolean isDirective() {
        return false;
    }

    default boolean isOperation() {
        return false;
    }

    default boolean isFragment() {
        return false;
    }

    default boolean isLeaf() {
        return isScalar() || isEnum();
    }

    boolean isContainerType();

    String toString();

    static Definition of(GraphqlParser.DefinitionContext definitionContext) {
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
}
