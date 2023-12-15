package io.graphoenix.spi.utils;

import com.google.common.base.CharMatcher;
import graphql.parser.antlr.GraphqlLexer;
import graphql.parser.antlr.GraphqlParser;
import io.graphoenix.spi.error.GraphQLErrors;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.atn.PredictionMode;
import org.antlr.v4.runtime.tree.TerminalNode;
import org.tinylog.Logger;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.Collection;
import java.util.stream.Stream;

import static io.graphoenix.spi.error.GraphQLErrorType.SYNTAX_ERROR;

public final class DocumentUtil {

    public static GraphqlParser.DocumentContext graphqlToDocument(String graphql) {
        CodePointCharStream charStream;
        charStream = CharStreams.fromString(graphql);
        return graphqlToDocument(charStream);
    }

    public static GraphqlParser.DocumentContext graphqlToDocument(InputStream inputStream) throws IOException {
        CharStream charStream;
        charStream = CharStreams.fromStream(inputStream);
        return graphqlToDocument(charStream);
    }

    public static GraphqlParser.DocumentContext graphqlToDocument(File graphqlFile) throws IOException {
        CharStream charStream;
        charStream = CharStreams.fromFileName(graphqlFile.getPath());
        return graphqlToDocument(charStream);
    }

    public static GraphqlParser.DocumentContext graphqlToDocument(Path graphqlPath) throws IOException {
        CharStream charStream;
        charStream = CharStreams.fromPath(graphqlPath);
        return graphqlToDocument(charStream);
    }

    public static GraphqlParser.OperationDefinitionContext graphqlToOperation(String graphql) {
        CodePointCharStream charStream;
        charStream = CharStreams.fromString(graphql);
        return graphqlToOperation(charStream);
    }

    public static GraphqlParser.OperationDefinitionContext graphqlToOperation(InputStream inputStream) throws IOException {
        CharStream charStream;
        charStream = CharStreams.fromStream(inputStream);
        return graphqlToOperation(charStream);
    }

    public static GraphqlParser.SelectionSetContext graphqlToSelectionSet(String graphql) {
        CodePointCharStream charStream;
        charStream = CharStreams.fromString(graphql);
        return graphqlToSelectionSet(charStream);
    }

    public static GraphqlParser.SelectionContext graphqlToSelection(String graphql) {
        CodePointCharStream charStream;
        charStream = CharStreams.fromString(graphql);
        return graphqlToSelection(charStream);
    }

    public static GraphqlParser.ObjectTypeDefinitionContext graphqlToObjectTypeDefinition(String graphql) {
        CodePointCharStream charStream;
        charStream = CharStreams.fromString(graphql);
        return graphqlToObjectTypeDefinition(charStream);
    }

    public static GraphqlParser.InterfaceTypeDefinitionContext graphqlToInterfaceTypeDefinition(String graphql) {
        CodePointCharStream charStream;
        charStream = CharStreams.fromString(graphql);
        return graphqlToInterfaceTypeDefinition(charStream);
    }

    public static GraphqlParser.EnumTypeDefinitionContext graphqlToEnumTypeDefinition(String graphql) {
        CodePointCharStream charStream;
        charStream = CharStreams.fromString(graphql);
        return graphqlToEnumTypeDefinition(charStream);
    }

    public static GraphqlParser.EnumValueContext graphqlToEnumValue(String graphql) {
        CodePointCharStream charStream;
        charStream = CharStreams.fromString(graphql);
        return graphqlToEnumValue(charStream);
    }

    public static GraphqlParser.InputObjectTypeDefinitionContext graphqlToInputObjectTypeDefinition(String graphql) {
        CodePointCharStream charStream;
        charStream = CharStreams.fromString(graphql);
        return graphqlToInputObjectTypeDefinition(charStream);
    }

    public static GraphqlParser.FieldDefinitionContext graphqlToFieldDefinition(String graphql) {
        CodePointCharStream charStream;
        charStream = CharStreams.fromString(graphql);
        return graphqlToFieldDefinition(charStream);
    }

    public static GraphqlParser.EnumValueDefinitionContext graphqlToEnumValueDefinition(String graphql) {
        CodePointCharStream charStream;
        charStream = CharStreams.fromString(graphql);
        return graphqlToEnumValueDefinition(charStream);
    }

    public static GraphqlParser.InputValueDefinitionContext graphqlToInputValueDefinition(String graphql) {
        CodePointCharStream charStream;
        charStream = CharStreams.fromString(graphql);
        return graphqlToInputValueDefinition(charStream);
    }

    public static GraphqlParser.TypeContext graphqlToType(String graphql) {
        CodePointCharStream charStream;
        charStream = CharStreams.fromString(graphql);
        return graphqlToType(charStream);
    }

    public static GraphqlParser.DocumentContext graphqlToDocument(CharStream charStream) {
        return getGraphqlParser(charStream).document();
    }

    public static GraphqlParser.OperationDefinitionContext graphqlToOperation(CharStream charStream) {
        return getGraphqlParser(charStream).operationDefinition();
    }

    public static GraphqlParser.SelectionSetContext graphqlToSelectionSet(CharStream charStream) {
        return getGraphqlParser(charStream).selectionSet();
    }

    public static GraphqlParser.SelectionContext graphqlToSelection(CharStream charStream) {
        return getGraphqlParser(charStream).selection();
    }

    public static GraphqlParser.ObjectTypeDefinitionContext graphqlToObjectTypeDefinition(CharStream charStream) {
        return getGraphqlParser(charStream).objectTypeDefinition();
    }

    public static GraphqlParser.InterfaceTypeDefinitionContext graphqlToInterfaceTypeDefinition(CharStream charStream) {
        return getGraphqlParser(charStream).interfaceTypeDefinition();
    }

    public static GraphqlParser.EnumTypeDefinitionContext graphqlToEnumTypeDefinition(CharStream charStream) {
        return getGraphqlParser(charStream).enumTypeDefinition();
    }

    public static GraphqlParser.EnumValueContext graphqlToEnumValue(CharStream charStream) {
        return getGraphqlParser(charStream).enumValue();
    }

    public static GraphqlParser.InputObjectTypeDefinitionContext graphqlToInputObjectTypeDefinition(CharStream charStream) {
        return getGraphqlParser(charStream).inputObjectTypeDefinition();
    }

    public static GraphqlParser.FieldDefinitionContext graphqlToFieldDefinition(CharStream charStream) {
        return getGraphqlParser(charStream).fieldDefinition();
    }

    public static GraphqlParser.EnumValueDefinitionContext graphqlToEnumValueDefinition(CharStream charStream) {
        return getGraphqlParser(charStream).enumValueDefinition();
    }

    public static GraphqlParser.InputValueDefinitionContext graphqlToInputValueDefinition(CharStream charStream) {
        return getGraphqlParser(charStream).inputValueDefinition();
    }

    public static GraphqlParser.TypeContext graphqlToType(CharStream charStream) {
        return getGraphqlParser(charStream).type();
    }

    public static GraphqlParser getGraphqlParser(String graphql) {
        CodePointCharStream charStream;
        charStream = CharStreams.fromString(graphql);
        return getGraphqlParser(charStream);
    }

    public static GraphqlLexer getGraphqlLexer(String graphql) {
        CodePointCharStream charStream;
        charStream = CharStreams.fromString(graphql);
        return getGraphqlLexer(charStream);
    }

    private static GraphqlLexer getGraphqlLexer(CharStream charStream) {
        GraphqlLexer lexer = new GraphqlLexer(charStream);
        lexer.removeErrorListeners();
        lexer.addErrorListener(new BaseErrorListener() {
            @Override
            public void syntaxError(Recognizer<?, ?> recognizer, Object offendingSymbol, int line, int charPositionInLine, String msg, RecognitionException e) {
                Logger.error(e);
                throw new GraphQLErrors(SYNTAX_ERROR.bind(msg, line, charPositionInLine), line, charPositionInLine);
            }
        });
        return lexer;
    }

    private static GraphqlParser getGraphqlParser(CharStream charStream) {
        CommonTokenStream tokens = new CommonTokenStream(getGraphqlLexer(charStream));
        GraphqlParser parser = new GraphqlParser(tokens);
        parser.removeErrorListeners();
        parser.addErrorListener(new BaseErrorListener() {
            @Override
            public void syntaxError(Recognizer<?, ?> recognizer, Object offendingSymbol, int line, int charPositionInLine, String msg, RecognitionException e) {
                Logger.error(e);
                throw new GraphQLErrors(SYNTAX_ERROR.bind(msg, line, charPositionInLine), line, charPositionInLine);
            }
        });
        parser.getInterpreter().setPredictionMode(PredictionMode.SLL);
        return parser;
    }

    public static String getStringValue(TerminalNode terminalNode) {
        return CharMatcher.is('"').trimFrom(terminalNode.getText());
    }

    public static Integer getIntValue(TerminalNode terminalNode) {
        if (terminalNode.getText().equals("null")) {
            return null;
        }
        return Integer.parseInt(terminalNode.getText());
    }

    public static Float getFloatValue(TerminalNode terminalNode) {
        if (terminalNode.getText().equals("null")) {
            return null;
        }
        return Float.parseFloat(terminalNode.getText());
    }

    public static Double getDoubleValue(TerminalNode terminalNode) {
        if (terminalNode.getText().equals("null")) {
            return null;
        }
        return Double.parseDouble(terminalNode.getText());
    }

    public static Boolean getBooleanValue(TerminalNode terminalNode) {
        if (terminalNode.getText().equals("null")) {
            return null;
        }
        return Boolean.parseBoolean(terminalNode.getText());
    }

    public static Stream<String> getImplementsInterfaces(GraphqlParser.ImplementsInterfacesContext implementsInterfacesContext) {
        return Stream.concat(
                Stream.ofNullable(implementsInterfacesContext.typeName())
                        .flatMap(Collection::stream)
                        .map(typeNameContext -> typeNameContext.name().getText()),
                Stream.ofNullable(implementsInterfacesContext.implementsInterfaces())
                        .flatMap(DocumentUtil::getImplementsInterfaces)
        );
    }
}
