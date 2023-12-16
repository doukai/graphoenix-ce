package io.graphoenix.spi.graphql;

import graphql.parser.antlr.GraphqlParser;
import io.graphoenix.spi.graphql.operation.FragmentDefinition;
import io.graphoenix.spi.graphql.operation.Operation;
import io.graphoenix.spi.graphql.type.*;
import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroupFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.stream.Collectors;

import static io.graphoenix.spi.utils.DocumentUtil.graphqlToDocument;

public class Document {

    private final STGroupFile stGroupFile = new STGroupFile("stg/Document.stg");

    private Collection<Definition> definitions;

    public Collection<Definition> getDefinitions() {
        return definitions;
    }

    public Document(String graphql) {
        this(graphqlToDocument(graphql));
    }

    public Document(InputStream inputStream) throws IOException {
        this(graphqlToDocument(inputStream));
    }

    public Document(File file) throws IOException {
        this(graphqlToDocument(file));
    }

    public Document(Path path) throws IOException {
        this(graphqlToDocument(path));
    }

    public Document addDefinitions(String graphql) {
        if (this.definitions == null) {
            this.definitions = new LinkedHashSet<>();
        }
        this.definitions.addAll(
                graphqlToDocument(graphql).definition().stream()
                        .map(Definition::of)
                        .collect(Collectors.toList())
        );
        return this;
    }

    public Document addDefinitions(InputStream inputStream) {
        if (this.definitions == null) {
            this.definitions = new LinkedHashSet<>();
        }
        this.definitions.addAll(
                graphqlToDocument(graphql).definition().stream()
                        .map(Definition::of)
                        .collect(Collectors.toList())
        );
        return this;
    }

    public Document addDefinitions(String graphql) {
        if (this.definitions == null) {
            this.definitions = new LinkedHashSet<>();
        }
        this.definitions.addAll(
                graphqlToDocument(graphql).definition().stream()
                        .map(Definition::of)
                        .collect(Collectors.toList())
        );
        return this;
    }


    public Document(GraphqlParser.DocumentContext documentContext) {
        this.definitions = documentContext.definition().stream()
                .map(Definition::of)
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
