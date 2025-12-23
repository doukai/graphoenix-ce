package graphoenix.annotation.processor;

import com.google.auto.service.AutoService;
import io.graphoenix.core.annotation.processor.BaseProcessor;
import io.graphoenix.core.config.PackageConfig;
import io.graphoenix.core.handler.DocumentBuilder;
import io.graphoenix.core.handler.DocumentManager;
import io.graphoenix.core.handler.GraphQLConfigRegister;
import io.nozdormu.spi.context.BeanContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;
import javax.tools.FileObject;
import javax.tools.StandardLocation;
import java.io.IOException;
import java.io.Writer;
import java.net.URISyntaxException;
import java.util.Set;

@SupportedAnnotationTypes("io.graphoenix.spi.annotation.Package")
@AutoService(Processor.class)
public class PackageProcessor extends BaseProcessor {

    private static final Logger logger = LoggerFactory.getLogger(PackageProcessor.class);

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
        PackageConfig packageConfig = BeanContext.get(PackageConfig.class);
        DocumentManager documentManager = BeanContext.get(DocumentManager.class);
        DocumentBuilder documentBuilder = BeanContext.get(DocumentBuilder.class);
        roundInit(roundEnv);
        try {
            GraphQLConfigRegister configRegister = BeanContext.get(GraphQLConfigRegister.class);
            configRegister.registerPackage(PackageProcessor.class.getClassLoader());
            documentBuilder.build();
            registerElements(roundEnv);
            documentBuilder.buildInvoker();
            registerOperations(roundEnv);
            FileObject packageGraphQL = filer.createResource(StandardLocation.CLASS_OUTPUT, "", "META-INF/graphql/" + packageConfig.getPackageName() + ".gql");
            Writer writer = packageGraphQL.openWriter();
            writer.write(documentManager.getPackageDocument().toString());
            writer.close();

        } catch (IOException | URISyntaxException e) {
            logger.error(e.getMessage(), e);
            processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, e.getMessage());
        }
        return false;
    }
}
