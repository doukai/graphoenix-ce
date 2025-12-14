package io.graphoenix.grpc.client.annotation.processor;

import com.google.auto.service.AutoService;
import io.graphoenix.core.annotation.processor.BaseProcessor;
import io.graphoenix.core.config.GraphQLConfig;
import io.graphoenix.core.handler.DocumentBuilder;
import io.graphoenix.core.handler.DocumentManager;
import io.graphoenix.core.handler.GraphQLConfigRegister;
import io.graphoenix.grpc.client.implementer.GrpcFetchHandlerBuilder;
import io.nozdormu.spi.context.BeanContext;
import org.tinylog.Logger;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;
import javax.tools.FileObject;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.nio.file.NoSuchFileException;
import java.util.Set;

@SupportedAnnotationTypes("io.graphoenix.spi.annotation.Application")
@AutoService(Processor.class)
public class ApplicationProcessor extends BaseProcessor {

    private final DocumentBuilder documentBuilder = BeanContext.get(DocumentBuilder.class);
    private final DocumentManager documentManager = BeanContext.get(DocumentManager.class);
    private final GraphQLConfig graphQLConfig = BeanContext.get(GraphQLConfig.class);
    private final GraphQLConfigRegister configRegister = BeanContext.get(GraphQLConfigRegister.class);
    private final GrpcFetchHandlerBuilder grpcFetchHandlerBuilder = BeanContext.get(GrpcFetchHandlerBuilder.class);
    private Filer filer;

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        filer = processingEnv.getFiler();
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        if (annotations.isEmpty()) {
            return false;
        }
        roundInit(roundEnv);

        try {
            if (DOCUMENT_CACHE.containsKey(MAIN_GQL_FILE_NAME)) {
                documentManager.setDocument(DOCUMENT_CACHE.get(MAIN_GQL_FILE_NAME));
            } else {
                FileObject fileObject = getResource(MAIN_GQL_FILE_NAME);
                try (InputStream inputStream = fileObject.openInputStream()) {
                    documentManager.getDocument().addDefinitions(inputStream);
                } catch (NoSuchFileException e) {
                    configRegister.registerApplication(ApplicationProcessor.class.getClassLoader());
                    registerElements(roundEnv);
                    registerOperations(roundEnv);
                    documentBuilder.buildFetchFieldsProtocol();
                    if (graphQLConfig.getMapToLocalFetch()) {
                        documentBuilder.mapToLocalFetch();
                    }
                    createResource(MAIN_GQL_FILE_NAME, documentManager.getDocument().toString());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                DOCUMENT_CACHE.put(MAIN_GQL_FILE_NAME, documentManager.getDocument());
            }
            grpcFetchHandlerBuilder.writeToFiler(filer);
        } catch (IOException | URISyntaxException e) {
            Logger.error(e);
            processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, e.getMessage());
        }
        return false;
    }
}
