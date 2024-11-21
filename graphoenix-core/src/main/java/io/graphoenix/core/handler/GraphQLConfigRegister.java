package io.graphoenix.core.handler;

import io.graphoenix.core.config.GraphQLConfig;
import io.graphoenix.core.config.PackageConfig;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.tinylog.Logger;

import javax.annotation.processing.Filer;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.*;
import java.util.*;
import java.util.stream.Stream;

import static io.graphoenix.core.utils.FilerUtil.*;

@ApplicationScoped
public class GraphQLConfigRegister {

    private final GraphQLConfig graphQLConfig;
    private final PackageConfig packageConfig;
    private final DocumentManager documentManager;

    @Inject
    public GraphQLConfigRegister(GraphQLConfig graphQLConfig, PackageConfig packageConfig, DocumentManager documentManager) {
        this.graphQLConfig = graphQLConfig;
        this.packageConfig = packageConfig;
        this.documentManager = documentManager;
    }

    public void registerConfig() throws IOException {
        if (graphQLConfig.getGraphQL() != null) {
            documentManager.getDocument().addDefinitions(graphQLConfig.getGraphQL());
            Logger.info("registered graphql {}", graphQLConfig.getGraphQL());
        } else if (graphQLConfig.getGraphQLFileName() != null) {
            documentManager.getDocument().addDefinitionsByFileName(graphQLConfig.getGraphQLFileName());
            Logger.info("registered file {}", graphQLConfig.getGraphQLFileName());
        } else if (graphQLConfig.getGraphQLPath() != null) {
            documentManager.getDocument().addDefinitionsByPathName(graphQLConfig.getGraphQLPath());
            Logger.info("registered path {}", graphQLConfig.getGraphQLPath());
        }
    }

    public void registerConfig(ClassLoader classLoader) throws IOException {
        if (graphQLConfig.getGraphQL() != null) {
            documentManager.getDocument().addDefinitions(graphQLConfig.getGraphQL());
            Logger.info("registered graphql {}", graphQLConfig.getGraphQL());
        } else if (graphQLConfig.getGraphQLFileName() != null) {
            documentManager.getDocument().addDefinitionsByFileName(graphQLConfig.getGraphQLFileName(), classLoader);
            Logger.info("registered file {}", graphQLConfig.getGraphQLFileName());
        } else if (graphQLConfig.getGraphQLPath() != null) {
            documentManager.getDocument().addDefinitionsByPathName(graphQLConfig.getGraphQLPath(), classLoader);
            Logger.info("registered path {}", graphQLConfig.getGraphQLPath());
        }
    }

    public void registerConfig(String resourcePath) throws IOException {
        if (graphQLConfig.getGraphQL() != null) {
            documentManager.getDocument().addDefinitions(graphQLConfig.getGraphQL());
            Logger.info("registered graphql {}", graphQLConfig.getGraphQL());
        } else if (graphQLConfig.getGraphQLFileName() != null) {
            documentManager.getDocument().addDefinitions(new FileInputStream(new File(Paths.get(resourcePath).resolve(graphQLConfig.getGraphQLFileName()).toUri())));
            Logger.info("registered file {}", graphQLConfig.getGraphQLFileName());
        } else if (graphQLConfig.getGraphQLPath() != null) {
            documentManager.getDocument().addDefinitionsByPathName(graphQLConfig.getGraphQLPath(), resourcePath);
            Logger.info("registered path {}", graphQLConfig.getGraphQLPath());
        }
    }

    public void registerConfig(Filer filer) throws IOException {
        if (graphQLConfig.getGraphQL() != null) {
            documentManager.getDocument().addDefinitions(graphQLConfig.getGraphQL());
            Logger.info("registered graphql {}", graphQLConfig.getGraphQL());
        } else if (graphQLConfig.getGraphQLFileName() != null) {
            documentManager.getDocument().addDefinitions(getResource(filer, graphQLConfig.getGraphQLFileName()));
            Logger.info("registered file {}", graphQLConfig.getGraphQLFileName());
        } else if (graphQLConfig.getGraphQLPath() != null) {
            documentManager.getDocument().addDefinitionsByPath(graphQLConfig.getGraphQLPath(), getResourcesPath(filer));
            documentManager.getDocument().addDefinitionsByPath(graphQLConfig.getGraphQLPath(), getTestResourcesPath(filer));
            Logger.info("registered path {}", graphQLConfig.getGraphQLPath());
        }
    }

    public void registerPackage(ClassLoader classLoader) throws IOException, URISyntaxException {
        register(classLoader, false);
    }

    public void registerApplication(ClassLoader classLoader) throws IOException, URISyntaxException {
        register(classLoader, true);
    }

    public void register(ClassLoader classLoader, boolean application) throws IOException, URISyntaxException {
        Enumeration<URL> urlEnumeration = classLoader.getResources("META-INF/graphql");
        while (urlEnumeration.hasMoreElements()) {
            URL url = urlEnumeration.nextElement();
            URI uri = url.toURI();
            try (Stream<Path> pathStream = Files.list(Path.of(uri))) {
                register(pathStream, application);
            } catch (FileSystemNotFoundException fileSystemNotFoundException) {
                Map<String, String> env = new HashMap<>();
                try (FileSystem fileSystem = FileSystems.newFileSystem(uri, env);
                     Stream<Path> pathStream = Files.list(fileSystem.getPath("META-INF/graphql"))) {
                    register(pathStream, application);
                }
            }
        }
    }

    public void register(Stream<Path> pathStream, boolean application) {
        pathStream
                .filter(path -> !path.getFileName().toString().equals("main.gql"))
                .filter(path ->
                        application ||
                                !path.getFileName().toString().equals(packageConfig.getPackageName() + ".gql") &&
                                        !path.getFileName().toString().equals(packageConfig.getPackageName() + ".graphql")
                )
                .forEach(path -> {
                            try {
                                documentManager.getDocument().merge(path);
                                Logger.info("registered preset path {}", path);
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        }
                );
    }
}
