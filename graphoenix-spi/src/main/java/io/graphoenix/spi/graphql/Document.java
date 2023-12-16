package io.graphoenix.spi.graphql;

import graphql.parser.antlr.GraphqlParser;
import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroupFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.*;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static io.graphoenix.spi.utils.DocumentUtil.graphqlToDocument;

public class Document {

    private final STGroupFile stGroupFile = new STGroupFile("stg/Document.stg");

    private Collection<Definition> definitions;

    public Collection<Definition> getDefinitions() {
        return definitions;
    }

    public Document() {
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
        return addDefinitions(
                graphqlToDocument(graphql).definition().stream()
                        .map(Definition::of)
                        .collect(Collectors.toList())
        );
    }

    public Document addDefinitions(InputStream inputStream) throws IOException {
        return addDefinitions(
                graphqlToDocument(inputStream).definition().stream()
                        .map(Definition::of)
                        .collect(Collectors.toList())
        );
    }

    public Document addDefinitions(File file) throws IOException {
        return addDefinitions(
                graphqlToDocument(file).definition().stream()
                        .map(Definition::of)
                        .collect(Collectors.toList())
        );
    }

    public Document addDefinitions(Path path) throws IOException {
        return addDefinitions(
                graphqlToDocument(path).definition().stream()
                        .map(Definition::of)
                        .collect(Collectors.toList())
        );
    }

    public Document addDefinitionsByFileName(String fileName) throws IOException {
        return addDefinitionsByFileName(fileName, this.getClass().getClassLoader());
    }

    public Document addDefinitionsByFileName(String fileName, ClassLoader classLoader) throws IOException {
        if (Files.exists(Path.of(fileName))) {
            return addDefinitions(new File(fileName));
        } else {
            try {
                InputStream inputStream = classLoader.getResourceAsStream(fileName);
                if (inputStream != null) {
                    return addDefinitions(inputStream);
                }
            } catch (FileSystemNotFoundException fileSystemNotFoundException) {
                Map<String, String> env = new HashMap<>();
                URL resource = classLoader.getResource(fileName);
                try (FileSystem fileSystem = FileSystems.newFileSystem(Objects.requireNonNull(resource).toURI(), env)) {
                    return addDefinitions(fileSystem.getPath(fileName));
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        return this;
    }

    public Document addDefinitionsByPathName(String pathName) throws IOException {
        return addDefinitionsByPathName(pathName, this.getClass().getClassLoader());
    }

    public Document addDefinitionsByPathName(String pathName, ClassLoader classLoader) throws IOException {
        Path path = Path.of(pathName);
        if (Files.exists(path)) {
            return addDefinitions(path);
        } else {
            URL resource = classLoader.getResource(pathName);
            if (resource != null) {
                try {
                    Path resourcePath = Paths.get(resource.toURI());
                    if (Files.exists(resourcePath)) {
                        try (Stream<Path> pathStream = Files.list(resourcePath)) {
                            List<Path> filePathList = pathStream.collect(Collectors.toList());
                            for (Path filePath : filePathList) {
                                addDefinitions(filePath);
                            }
                            return this;
                        }
                    }
                } catch (FileSystemNotFoundException fileSystemNotFoundException) {
                    Map<String, String> env = new HashMap<>();
                    try (FileSystem fileSystem = FileSystems.newFileSystem(Objects.requireNonNull(resource).toURI(), env);
                         Stream<Path> pathStream = Files.list(fileSystem.getPath(pathName))) {
                        List<Path> filePathList = pathStream.collect(Collectors.toList());
                        for (Path filePath : filePathList) {
                            addDefinitions(filePath);
                        }
                        return this;
                    } catch (URISyntaxException e) {
                        throw new RuntimeException(e);
                    }
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        return this;
    }

    public Document addDefinitionsByPathName(String pathName, String resourcePath) throws IOException {
        Path graphqlPath = Paths.get(resourcePath).resolve(pathName);
        try {
            if (Files.exists(graphqlPath)) {
                try (Stream<Path> pathStream = Files.list(graphqlPath)) {
                    List<Path> filePathList = pathStream.collect(Collectors.toList());
                    for (Path filePath : filePathList) {
                        addDefinitions(filePath);
                    }
                    return this;
                }
            }
        } catch (FileSystemNotFoundException fileSystemNotFoundException) {
            Map<String, String> env = new HashMap<>();
            try (FileSystem fileSystem = FileSystems.newFileSystem(graphqlPath.toUri(), env);
                 Stream<Path> pathStream = Files.list(fileSystem.getPath(pathName))) {
                List<Path> filePathList = pathStream.collect(Collectors.toList());
                for (Path filePath : filePathList) {
                    addDefinitions(filePath);
                }
                return this;
            }
        }
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
