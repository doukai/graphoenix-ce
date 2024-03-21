package io.graphoenix.jsonschema.annotation.processor;

import com.google.auto.service.AutoService;
import io.graphoenix.core.annotation.processor.BaseProcessor;
import io.graphoenix.core.config.GraphQLConfig;
import io.graphoenix.core.handler.DocumentBuilder;
import io.graphoenix.core.handler.GraphQLConfigRegister;
import io.graphoenix.jsonschema.handler.JsonSchemaTranslator;
import io.nozdormu.spi.context.BeanContext;
import org.tinylog.Logger;

import javax.annotation.processing.*;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Set;

import static javax.lang.model.SourceVersion.RELEASE_11;

@SupportedAnnotationTypes("io.graphoenix.spi.annotation.Application")
@SupportedSourceVersion(RELEASE_11)
@AutoService(Processor.class)
public class ApplicationProcessor extends BaseProcessor {

    private Filer filer;

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
        roundInit(roundEnv);

        try {
            GraphQLConfig graphQLConfig = BeanContext.get(GraphQLConfig.class);
            GraphQLConfigRegister configRegister = BeanContext.get(GraphQLConfigRegister.class);
            configRegister.registerPackage(ApplicationProcessor.class.getClassLoader(), true);
            registerElements(roundEnv);
            registerOperations(roundEnv);
            if (graphQLConfig.getMapToLocalFetch()) {
                documentBuilder.mapToLocalFetch();
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
