package io.graphoenix.jsonschema.annotation.processor;

import com.google.auto.service.AutoService;
import io.graphoenix.core.annotation.processor.BaseProcessor;
import io.graphoenix.core.config.GraphQLConfig;
import io.graphoenix.core.handler.DocumentBuilder;
import io.graphoenix.core.handler.DocumentManager;
import io.graphoenix.core.handler.GraphQLConfigRegister;
import io.graphoenix.jsonschema.handler.JsonSchemaTranslator;
import io.nozdormu.spi.context.BeanContext;
import org.tinylog.Logger;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Set;

@SupportedAnnotationTypes("io.graphoenix.spi.annotation.Application")
@AutoService(Processor.class)
public class ApplicationProcessor extends BaseProcessor {

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
        DocumentBuilder documentBuilder = BeanContext.get(DocumentBuilder.class);
        DocumentManager documentManager = BeanContext.get(DocumentManager.class);
        roundInit(roundEnv);

        try {
            if (DOCUMENT_CACHE.getDefinitions().isEmpty()) {
                GraphQLConfig graphQLConfig = BeanContext.get(GraphQLConfig.class);
                GraphQLConfigRegister configRegister = BeanContext.get(GraphQLConfigRegister.class);
                configRegister.registerApplication(ApplicationProcessor.class.getClassLoader());
                registerElements(roundEnv);
                registerOperations(roundEnv);
                if (graphQLConfig.getMapToLocalFetch()) {
                    documentBuilder.mapToLocalFetch();
                }
                DOCUMENT_CACHE.setDefinitions(documentManager.getDocument().getDefinitions());
            } else {
                documentManager.getDocument().setDefinitions(DOCUMENT_CACHE.getDefinitions());
            }
            JsonSchemaTranslator jsonSchemaTranslator = BeanContext.get(JsonSchemaTranslator.class);
            jsonSchemaTranslator.writeToFiler(filer);
        } catch (IOException | URISyntaxException e) {
            Logger.error(e);
            processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, e.getMessage());
        }
        return false;
    }
}
