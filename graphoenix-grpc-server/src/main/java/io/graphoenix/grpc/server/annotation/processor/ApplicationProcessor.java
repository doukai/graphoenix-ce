package io.graphoenix.grpc.server.annotation.processor;

import com.google.auto.service.AutoService;
import io.graphoenix.core.annotation.processor.BaseProcessor;
import io.graphoenix.core.handler.GraphQLConfigRegister;
import io.graphoenix.grpc.server.implementer.GrpcServerProducerBuilder;
import io.graphoenix.grpc.server.implementer.GrpcServiceImplementer;
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

    private final GraphQLConfigRegister configRegister = BeanContext.get(GraphQLConfigRegister.class);
    private final GrpcServiceImplementer grpcServiceImplementer = BeanContext.get(GrpcServiceImplementer.class);
    private final GrpcServerProducerBuilder grpcServerProducerBuilder = BeanContext.get(GrpcServerProducerBuilder.class);
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
        applicationRoundInit(roundEnv);

        try {
            configRegister.registerApplication(ApplicationProcessor.class.getClassLoader());
            grpcServiceImplementer.writeToFiler(filer);
            grpcServerProducerBuilder.writeToFiler(filer);
        } catch (IOException | URISyntaxException e) {
            Logger.error(e);
            processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, e.getMessage());
        }
        return false;
    }
}
