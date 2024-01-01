package graphoenix.annotation.processor;

import com.google.auto.service.AutoService;
import io.graphoenix.core.handler.DocumentManager;
import io.graphoenix.core.handler.GraphQLConfigRegister;
import io.graphoenix.java.implementer.InputInvokeHandlerBuilder;
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
        DocumentManager documentManager = BeanContext.get(DocumentManager.class);
        InputInvokeHandlerBuilder inputInvokeHandlerBuilder = BeanContext.get(InputInvokeHandlerBuilder.class);
        roundInit(roundEnv);

        try {
            GraphQLConfigRegister configRegister = BeanContext.get(GraphQLConfigRegister.class);
            configRegister.registerPackage(ApplicationProcessor.class.getClassLoader(), true);
            registerElements(roundEnv);
            FileObject mainGraphQL = filer.createResource(StandardLocation.CLASS_OUTPUT, "", "META-INF/graphql/main.gql");
            Writer writer = mainGraphQL.openWriter();
            writer.write(documentManager.getDocument().toString());
            writer.close();

            inputInvokeHandlerBuilder.writeToFiler(filer);

        } catch (IOException | URISyntaxException e) {
            Logger.error(e);
            processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, e.getMessage());
        }
        return false;
    }
}
