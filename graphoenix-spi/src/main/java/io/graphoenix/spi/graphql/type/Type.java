package io.graphoenix.spi.graphql.type;

import graphql.parser.antlr.GraphqlParser;
import io.graphoenix.spi.error.GraphQLErrors;;import static io.graphoenix.spi.error.GraphQLErrorType.UNSUPPORTED_FIELD_TYPE;
import static io.graphoenix.spi.utils.DocumentUtil.graphqlToType;

public interface Type {

    static Type of(String typeName) {
        return of(graphqlToType(typeName));
    }

    static Type of(GraphqlParser.TypeContext typeContext) {
        if (typeContext.typeName() != null) {
            return new TypeName(typeContext.typeName().name().getText());
        } else if (typeContext.listType() != null) {
            return new ListType(of(typeContext.listType().type()));
        } else if (typeContext.nonNullType() != null) {
            if (typeContext.nonNullType().typeName() != null) {
                return new NonNullType(new TypeName(typeContext.nonNullType().typeName().name().getText()));
            } else if (typeContext.nonNullType().listType() != null) {
                return new NonNullType(new ListType(of(typeContext.nonNullType().listType().type())));
            }
        }
        throw new GraphQLErrors(UNSUPPORTED_FIELD_TYPE.bind(typeContext.getText()));
    }

    default boolean isList() {
        return false;
    }

    default boolean isNonNull() {
        return false;
    }

    default TypeName getTypeName() {
        if (isList()) {
            return ((ListType) this).getType().getTypeName();
        } else if (isNonNull()) {
            return ((NonNullType) this).getType().getTypeName();
        }
        return (TypeName) this;
    }

    default boolean hasList() {
        if (isList()) {
            return true;
        } else if (isNonNull()) {
            return ((NonNullType) this).getType().hasList();
        }
        return false;
    }
}
