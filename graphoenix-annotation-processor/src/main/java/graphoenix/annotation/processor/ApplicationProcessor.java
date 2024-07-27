package graphoenix.annotation.processor;

import com.google.auto.service.AutoService;
import io.graphoenix.core.annotation.processor.BaseProcessor;
import io.graphoenix.core.config.GraphQLConfig;
import io.graphoenix.core.handler.DocumentBuilder;
import io.graphoenix.core.handler.DocumentManager;
import io.graphoenix.core.handler.GraphQLConfigRegister;
import io.graphoenix.core.handler.PackageManager;
import io.graphoenix.java.implementer.ArgumentsInvokeHandlerBuilder;
import io.graphoenix.java.implementer.InputInvokeHandlerBuilder;
import io.graphoenix.java.implementer.InvokeHandlerBuilder;
import io.graphoenix.spi.graphql.type.FieldDefinition;
import io.graphoenix.spi.graphql.type.InputObjectType;
import io.nozdormu.spi.context.BeanContext;
import org.tinylog.Logger;

import javax.annotation.processing.*;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;
import javax.tools.FileObject;
import javax.tools.StandardLocation;
import java.io.IOException;
import java.io.Writer;
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
        PackageManager packageManager = BeanContext.get(PackageManager.class);
        DocumentManager documentManager = BeanContext.get(DocumentManager.class);
        DocumentBuilder documentBuilder = BeanContext.get(DocumentBuilder.class);
        roundInit(roundEnv);

        try {
            GraphQLConfig graphQLConfig = BeanContext.get(GraphQLConfig.class);
            GraphQLConfigRegister configRegister = BeanContext.get(GraphQLConfigRegister.class);
            configRegister.registerPackage(ApplicationProcessor.class.getClassLoader(), true);
            registerElements(roundEnv);
            registerOperations(roundEnv);
            documentBuilder.buildFetchFieldsProtocol();
            if (graphQLConfig.getMapToLocalFetch()) {
                documentBuilder.mapToLocalFetch();
            }
            FileObject mainGraphQL = filer.createResource(StandardLocation.CLASS_OUTPUT, "", "META-INF/graphql/main.gql");
            Writer writer = mainGraphQL.openWriter();
            writer.write(documentManager.getDocument().toString());
            writer.close();

            if (documentManager.getDocument().getObjectTypes()
                    .flatMap(objectType -> objectType.getFields().stream())
                    .filter(packageManager::isLocalPackage)
                    .anyMatch(FieldDefinition::isInvokeField)
            ) {
                InvokeHandlerBuilder invokeHandlerBuilder = BeanContext.get(InvokeHandlerBuilder.class);
                invokeHandlerBuilder.writeToFiler(filer);
            }

            if (documentManager.getDocument().getInputObjectTypes()
                    .filter(packageManager::isLocalPackage)
                    .anyMatch(InputObjectType::isInvokesInput)
            ) {
                InputInvokeHandlerBuilder inputInvokeHandlerBuilder = BeanContext.get(InputInvokeHandlerBuilder.class);
                ArgumentsInvokeHandlerBuilder argumentsInvokeHandlerBuilder = BeanContext.get(ArgumentsInvokeHandlerBuilder.class);
                inputInvokeHandlerBuilder.writeToFiler(filer);
                argumentsInvokeHandlerBuilder.writeToFiler(filer);
            }
        } catch (IOException | URISyntaxException e) {
            Logger.error(e);
            processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, e.getMessage());
        }
        return false;
    }
}
