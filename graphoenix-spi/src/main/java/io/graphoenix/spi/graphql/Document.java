package io.graphoenix.spi.graphql;

import graphql.parser.antlr.GraphqlParser;
import io.graphoenix.spi.error.GraphQLErrors;
import io.graphoenix.spi.graphql.operation.FragmentDefinition;
import io.graphoenix.spi.graphql.operation.Operation;
import io.graphoenix.spi.graphql.type.*;
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

import static io.graphoenix.spi.constant.Hammurabi.*;
import static io.graphoenix.spi.error.GraphQLErrorType.*;
import static io.graphoenix.spi.utils.DocumentUtil.graphqlToDocument;

public class Document {

    private final STGroupFile stGroupFile = new STGroupFile("stg/Document.stg");

    private final Map<String, Definition> definitionMap = new LinkedHashMap<>();

    public Definition getDefinition(String name) {
        return definitionMap.get(name);
    }

    public boolean hasDefinition(String name) {
        return definitionMap.containsKey(name);
    }

    public Collection<Definition> getDefinitions() {
        return definitionMap.values();
    }

    public Document() {
    }

    public Document(GraphqlParser.DocumentContext documentContext) {
        setDefinitions(
                documentContext.definition().stream()
                        .map(Definition::of)
                        .collect(Collectors.toList())
        );
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
        return addDefinitionsByPath(pathName, Paths.get(resourcePath));
    }

    public Document addDefinitionsByPath(String pathName, Path resourcePath) throws IOException {
        Path graphqlPath = resourcePath.resolve(pathName);
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

    public Document setDefinitions(Collection<? extends Definition> definitions) {
        this.definitionMap.clear();
        return addDefinitions(definitions);
    }

    public Document addDefinition(Definition definition) {
        this.definitionMap.put(definition.getName(), definition);
        return this;
    }

    public Document addDefinitions(Collection<? extends Definition> definitions) {
        this.definitionMap.putAll(
                (Map<? extends String, ? extends Definition>) definitions.stream()
                        .collect(
                                Collectors.toMap(
                                        Definition::getName,
                                        definition -> definition,
                                        (x, y) -> y,
                                        LinkedHashMap::new
                                )
                        )
        );
        return this;
    }

    public Document merge(Path path) throws IOException {
        return merge(graphqlToDocument(path));
    }

    public Document merge(File file) throws IOException {
        return merge(graphqlToDocument(file));
    }

    public Document merge(GraphqlParser.DocumentContext documentContext) {
        this.definitionMap.putAll(
                (Map<? extends String, ? extends Definition>) documentContext.definition().stream()
                        .map(this::merge)
                        .collect(
                                Collectors.toMap(
                                        Definition::getName,
                                        definition -> definition,
                                        (x, y) -> y,
                                        LinkedHashMap::new
                                )
                        )
        );
        return this;
    }

    public Optional<ObjectType> getObjectType(String name) {
        return Optional.ofNullable(getDefinition(name))
                .map(Definition::asObject);
    }

    public ObjectType getObjectTypeOrError(String name) {
        return getObjectType(name).orElseThrow(() -> new GraphQLErrors(TYPE_DEFINITION_NOT_EXIST.bind(name)));
    }

    public Optional<InterfaceType> getInterfaceType(String name) {
        return Optional.ofNullable(getDefinition(name))
                .map(Definition::asInterface);
    }

    public InterfaceType getInterfaceTypeOrError(String name) {
        return getInterfaceType(name).orElseThrow(() -> new GraphQLErrors(TYPE_DEFINITION_NOT_EXIST.bind(name)));
    }

    public Stream<ObjectType> getObjectTypes() {
        return getDefinitions().stream()
                .filter(Definition::isObject)
                .map(Definition::asObject);
    }

    public Stream<InterfaceType> getInterfaceTypes() {
        return getDefinitions().stream()
                .filter(Definition::isInterface)
                .map(Definition::asInterface);
    }

    public Stream<InputObjectType> getInputObjectTypes() {
        return getDefinitions().stream()
                .filter(Definition::isInputObject)
                .map(Definition::asInputObject);
    }

    public Stream<EnumType> getEnums() {
        return getDefinitions().stream()
                .filter(Definition::isEnum)
                .map(Definition::asEnum);
    }

    public Stream<ScalarType> getScalarTypes() {
        return getDefinitions().stream()
                .filter(Definition::isScalar)
                .map(Definition::asScalar);
    }

    public Optional<ScalarType> getScalarType(String name) {
        return Optional.ofNullable(getDefinition(name))
                .map(Definition::asScalar);
    }

    public Stream<DirectiveDefinition> getDirectives() {
        return getDefinitions().stream()
                .filter(Definition::isDirective)
                .map(Definition::asDirective);
    }

    public Optional<DirectiveDefinition> getDirective(String name) {
        return Optional.ofNullable(getDefinition(name))
                .map(Definition::asDirective);
    }

    public Stream<ObjectType> getImplementsObjectType(String name) {
        return getDefinitions().stream()
                .filter(Definition::isObject)
                .map(Definition::asObject)
                .filter(objectType ->
                        objectType.getInterfaces().stream()
                                .anyMatch(interfaceTypeName -> interfaceTypeName.equals(name))
                );
    }

    public Optional<InputObjectType> getInputObjectType(String name) {
        return Optional.ofNullable(getDefinition(name))
                .map(Definition::asInputObject);
    }

    public InputObjectType getInputObjectTypeOrError(String name) {
        return getInputObjectType(name).orElseThrow(() -> new GraphQLErrors(INPUT_OBJECT_DEFINITION_NOT_EXIST.bind(name)));
    }

    public Optional<EnumType> getEnumType(String name) {
        return Optional.ofNullable(getDefinition(name))
                .map(Definition::asEnum);
    }

    public EnumType getEnumTypeOrError(String name) {
        return getEnumType(name).orElseThrow(() -> new GraphQLErrors(INPUT_OBJECT_DEFINITION_NOT_EXIST.bind(name)));
    }

    public Optional<FragmentDefinition> getFragmentDefinition(String name) {
        return Optional.ofNullable(getDefinition(name))
                .map(Definition::asFragmentDefinition);
    }

    public FragmentDefinition getFragmentDefinitionOrError(String name) {
        return getFragmentDefinition(name).orElseThrow(() -> new GraphQLErrors(FRAGMENT_NOT_EXIST.bind(name)));
    }

    public Optional<Operation> getOperation() {
        return getDefinitions().stream()
                .filter(Definition::isOperation)
                .map(Definition::asOperation)
                .findFirst();
    }

    public Stream<Operation> getOperations() {
        return getDefinitions().stream()
                .filter(Definition::isOperation)
                .map(Definition::asOperation);
    }

    public Operation getOperationOrError() {
        return getOperation().orElseThrow(() -> new GraphQLErrors(OPERATION_NOT_EXIST));
    }

    public Stream<InputObjectType> getImplementsInputObject(String name) {
        return getDefinitions().stream()
                .filter(Definition::isInputObject)
                .map(Definition::asInputObject)
                .filter(inputObjectType ->
                        inputObjectType.getInterfaces().stream()
                                .anyMatch(interfaceTypeName -> interfaceTypeName.equals(name))
                );
    }

    public Optional<ObjectType> getQueryOperationType() {
        return Optional.ofNullable(getDefinition(getSchema().map(Schema::getQuery).orElse(TYPE_QUERY_NAME)))
                .map(Definition::asObject);
    }

    public Optional<ObjectType> getMutationOperationType() {
        return Optional.ofNullable(getDefinition(getSchema().map(Schema::getMutation).orElse(TYPE_MUTATION_NAME)))
                .map(Definition::asObject);
    }

    public Optional<ObjectType> getSubscriptionOperationType() {
        return Optional.ofNullable(getDefinition(getSchema().map(Schema::getSubscription).orElse(TYPE_SUBSCRIPTION_NAME)))
                .map(Definition::asObject);
    }

    public ObjectType getQueryOperationTypeOrError() {
        return getQueryOperationType().orElseThrow(() -> new GraphQLErrors(QUERY_TYPE_DEFINITION_NOT_EXIST));
    }

    public ObjectType getMutationOperationTypeOrError() {
        return getMutationOperationType().orElseThrow(() -> new GraphQLErrors(MUTATION_TYPE_DEFINITION_NOT_EXIST));
    }

    public ObjectType getSubscriptionOperationTypeOrError() {
        return getSubscriptionOperationType().orElseThrow(() -> new GraphQLErrors(SUBSCRIBE_TYPE_DEFINITION_NOT_EXIST));
    }

    public Optional<InterfaceType> getMetaInterface() {
        return Optional.ofNullable(getDefinition(INTERFACE_META_NAME)).map(definition -> (InterfaceType) definition);
    }

    public Definition merge(Definition definition) {
        if (!definition.isExtension()) {
            if (definition.isSchema()) {
                getSchema()
                        .ifPresentOrElse(
                                schema -> schema.merge(definition.asSchema()),
                                () -> addDefinition(definition)
                        );
            } else if (definition.isObject()) {
                getObjectType(definition.getName())
                        .ifPresentOrElse(
                                objectType -> objectType.merge(definition.asObject()),
                                () -> addDefinition(definition)
                        );
            } else if (definition.isInterface()) {
                getInterfaceType(definition.getName())
                        .ifPresentOrElse(
                                interfaceType -> interfaceType.merge(definition.asObject()),
                                () -> addDefinition(definition)
                        );
            } else if (definition.isInputObject()) {
                getInputObjectType(definition.getName())
                        .ifPresentOrElse(
                                inputObjectType -> inputObjectType.merge(definition.asObject()),
                                () -> addDefinition(definition)
                        );
            } else if (definition.isEnum()) {
                getEnumType(definition.getName())
                        .ifPresentOrElse(
                                enumType -> enumType.merge(definition.asObject()),
                                () -> addDefinition(definition)
                        );
            } else if (definition.isScalar()) {
                getScalarType(definition.getName())
                        .ifPresentOrElse(
                                scalarType -> scalarType.merge(definition.asScalar()),
                                () -> addDefinition(definition)
                        );
            }
        } else {
            this.definitionMap.put(definition.getName(), definition);
        }
        return definition;
    }

    public Definition merge(GraphqlParser.DefinitionContext definitionContext) {
        Definition definition = Definition.of(definitionContext);
        return merge(definition);
    }

    public Optional<Schema> getSchema() {
        return getDefinitions().stream()
                .filter(Definition::isSchema)
                .map(definition -> (Schema) definition)
                .findFirst();
    }

    public void clear() {
        this.definitionMap.clear();
    }

    @Override
    public String toString() {
        ST st = stGroupFile.getInstanceOf("documentDefinition");
        st.add("document", this);
        return st.render();
    }
}
