package graphoenix.annotation.processor;

import io.graphoenix.core.config.PackageConfig;
import io.graphoenix.core.handler.DocumentManager;
import io.graphoenix.core.handler.GraphQLConfigRegister;
import io.graphoenix.spi.annotation.Package;
import io.graphoenix.spi.graphql.common.ArrayValueWithVariable;
import io.graphoenix.spi.graphql.common.Directive;
import io.graphoenix.spi.graphql.common.ObjectValueWithVariable;
import io.graphoenix.spi.graphql.type.*;
import io.graphoenix.spi.utils.ElementUtil;
import io.nozdormu.config.TypesafeConfig;
import io.nozdormu.spi.async.Async;
import io.nozdormu.spi.context.BeanContext;
import jakarta.annotation.Generated;
import org.eclipse.microprofile.config.Config;
import org.eclipse.microprofile.graphql.Enum;
import org.eclipse.microprofile.graphql.Type;
import org.eclipse.microprofile.graphql.*;
import org.tinylog.Logger;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.*;
import javax.lang.model.util.Types;
import javax.tools.Diagnostic;
import java.io.IOException;
import java.util.Optional;
import java.util.stream.Collectors;

import static io.graphoenix.spi.constant.Hammurabi.*;
import static io.graphoenix.spi.utils.ElementUtil.getAsyncMethodName;
import static io.graphoenix.spi.utils.ElementUtil.getNameFromElement;

public abstract class BaseProcessor extends AbstractProcessor {

    private PackageConfig packageConfig;
    private DocumentManager documentManager;
    private Types typeUtils;

    @Override
    public void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        typeUtils = processingEnv.getTypeUtils();
        Filer filer = processingEnv.getFiler();
        BeanContext.load(BaseProcessor.class.getClassLoader());
        Config config = BeanContext.get(Config.class);
        ((TypesafeConfig) config).load(filer);
        packageConfig = BeanContext.get(PackageConfig.class);
        documentManager = BeanContext.get(DocumentManager.class);
        GraphQLConfigRegister configRegister = BeanContext.get(GraphQLConfigRegister.class);

        try {
            documentManager.getDocument().clear();
            configRegister.registerConfig(filer);
        } catch (IOException e) {
            Logger.error(e);
            processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, e.getMessage());
        }
    }

    public Optional<String> getDefaultPackageName(RoundEnvironment roundEnv) {
        return roundEnv.getElementsAnnotatedWith(Package.class).stream()
                .filter(element -> element.getKind().equals(ElementKind.PACKAGE))
                .findFirst()
                .map(element -> (PackageElement) element)
                .map(packageElement -> packageElement.getQualifiedName().toString());
    }

    public void roundInit(RoundEnvironment roundEnv) {
        if (packageConfig.getPackageName() == null) {
            getDefaultPackageName(roundEnv).ifPresent(packageName -> packageConfig.setPackageName(packageName));
        }
    }

    public void registerElements(RoundEnvironment roundEnv) {
        documentManager.getDocument()
                .addDefinitions(
                        roundEnv.getElementsAnnotatedWith(Enum.class).stream()
                                .filter(element -> element.getAnnotation(Generated.class) == null)
                                .filter(element -> element.getKind().equals(ElementKind.ENUM))
                                .map(element -> new EnumType((TypeElement) element))
                                .collect(Collectors.toList())
                )
                .addDefinitions(
                        roundEnv.getElementsAnnotatedWith(Interface.class).stream()
                                .filter(element -> element.getAnnotation(Generated.class) == null)
                                .filter(element -> element.getKind().equals(ElementKind.INTERFACE))
                                .map(element -> new InterfaceType((TypeElement) element, typeUtils))
                                .collect(Collectors.toList())
                )
                .addDefinitions(
                        roundEnv.getElementsAnnotatedWith(Type.class).stream()
                                .filter(element -> element.getAnnotation(Generated.class) == null)
                                .filter(element -> element.getKind().equals(ElementKind.CLASS))
                                .map(element -> new ObjectType((TypeElement) element, typeUtils))
                                .collect(Collectors.toList())
                )
                .addDefinitions(
                        roundEnv.getElementsAnnotatedWith(Input.class).stream()
                                .filter(element -> element.getAnnotation(Generated.class) == null)
                                .filter(element -> element.getKind().equals(ElementKind.CLASS))
                                .map(element -> new InputObjectType((TypeElement) element, typeUtils))
                                .collect(Collectors.toList())
                )
                .addDefinitions(
                        roundEnv.getElementsAnnotatedWith(Interface.class).stream()
                                .filter(element -> element.getAnnotation(Generated.class) == null)
                                .filter(element -> element.getKind().equals(ElementKind.INTERFACE))
                                .flatMap(element ->
                                        element.getEnclosedElements().stream()
                                                .filter(subElement -> subElement.getAnnotation(Enum.class) != null)
                                                .filter(subElement -> subElement.getKind().equals(ElementKind.ENUM))
                                                .map(subElement -> new EnumType((TypeElement) subElement))
                                )
                                .collect(Collectors.toList())
                )
                .addDefinitions(
                        roundEnv.getElementsAnnotatedWith(Type.class).stream()
                                .filter(element -> element.getAnnotation(Generated.class) == null)
                                .filter(element -> element.getKind().equals(ElementKind.CLASS))
                                .flatMap(element ->
                                        element.getEnclosedElements().stream()
                                                .filter(subElement -> subElement.getAnnotation(Enum.class) != null)
                                                .filter(subElement -> subElement.getKind().equals(ElementKind.ENUM))
                                                .map(subElement -> new EnumType((TypeElement) subElement))
                                )
                                .collect(Collectors.toList())
                )
                .addDefinitions(
                        roundEnv.getElementsAnnotatedWith(Input.class).stream()
                                .filter(element -> element.getAnnotation(Generated.class) == null)
                                .filter(element -> element.getKind().equals(ElementKind.CLASS))
                                .flatMap(element ->
                                        element.getEnclosedElements().stream()
                                                .filter(subElement -> subElement.getAnnotation(Enum.class) != null)
                                                .filter(subElement -> subElement.getKind().equals(ElementKind.ENUM))
                                                .map(subElement -> new EnumType((TypeElement) subElement))
                                )
                                .collect(Collectors.toList())
                );

        roundEnv.getElementsAnnotatedWith(GraphQLApi.class).stream()
                .filter(element -> element.getKind().equals(ElementKind.CLASS))
                .forEach(this::registerGraphQLApiElement);
    }

    private void registerGraphQLApiElement(Element element) {
        element.getEnclosedElements().stream()
                .filter(subElement -> subElement.getKind().equals(ElementKind.METHOD))
                .map(subElement -> (ExecutableElement) subElement)
                .forEach(executableElement -> {
                            if (executableElement.getAnnotation(Query.class) != null) {
                                FieldDefinition fieldDefinition = new FieldDefinition(executableElement, typeUtils);
                                documentManager.getDocument().getQueryOperationType()
                                        .ifPresentOrElse(
                                                objectType -> objectType.addField(fieldDefinition),
                                                () -> documentManager.getDocument().addDefinition(new ObjectType(TYPE_QUERY_NAME).addField(fieldDefinition))
                                        );
                            } else if (executableElement.getAnnotation(Mutation.class) != null) {
                                FieldDefinition fieldDefinition = new FieldDefinition(executableElement, typeUtils);
                                documentManager.getDocument().getMutationOperationType()
                                        .ifPresentOrElse(
                                                objectType -> objectType.addField(fieldDefinition),
                                                () -> documentManager.getDocument().addDefinition(new ObjectType(TYPE_MUTATION_NAME).addField(fieldDefinition))
                                        );
                            } else if (executableElement.getParameters().stream()
                                    .anyMatch(variableElement ->
                                            variableElement.getAnnotation(Source.class) != null &&
                                                    typeUtils.asElement(variableElement.asType()).getAnnotation(Type.class) != null
                                    )
                            ) {
                                executableElement.getParameters().stream()
                                        .filter(variableElement -> variableElement.getAnnotation(Source.class) != null)
                                        .findFirst()
                                        .ifPresent(variableElement -> {
                                                    FieldDefinition fieldDefinition = new FieldDefinition(executableElement, typeUtils);
                                                    String typeName = getNameFromElement(typeUtils.asElement(variableElement.asType()));
                                                    documentManager.getDocument().getObjectType(typeName)
                                                            .ifPresentOrElse(
                                                                    objectType -> objectType.addField(fieldDefinition),
                                                                    () -> documentManager.getDocument().addDefinition(new ObjectType(typeName).addField(fieldDefinition))
                                                            );
                                                }
                                        );
                            } else if (executableElement.getParameters().stream()
                                    .anyMatch(variableElement ->
                                            variableElement.getAnnotation(Source.class) != null &&
                                                    typeUtils.asElement(variableElement.asType()).getAnnotation(Interface.class) != null
                                    )
                            ) {
                                executableElement.getParameters().stream()
                                        .filter(variableElement -> variableElement.getAnnotation(Source.class) != null)
                                        .findFirst()
                                        .ifPresent(variableElement -> {
                                                    FieldDefinition fieldDefinition = new FieldDefinition(executableElement, typeUtils);
                                                    String typeName = getNameFromElement(typeUtils.asElement(variableElement.asType()));
                                                    documentManager.getDocument().getImplementsObjectType(typeName)
                                                            .forEach(objectType -> objectType.addField(fieldDefinition));
                                                }
                                        );
                            } else if (executableElement.getParameters().stream()
                                    .anyMatch(variableElement ->
                                            variableElement.getAnnotation(Source.class) != null &&
                                                    typeUtils.asElement(variableElement.asType()).getAnnotation(Input.class) != null
                                    )
                            ) {
                                boolean async = executableElement.getAnnotation(Async.class) != null;
                                ObjectValueWithVariable invoke = ObjectValueWithVariable.of(
                                        INPUT_INVOKE_INPUT_VALUE_CLASS_NAME_NAME, executableElement.getEnclosingElement().toString(),
                                        INPUT_INVOKE_INPUT_VALUE_METHOD_NAME_NAME, async ? getAsyncMethodName(executableElement, typeUtils) : executableElement.getSimpleName().toString(),
                                        INPUT_INVOKE_INPUT_VALUE_PARAMETER_NAME,
                                        new ArrayValueWithVariable(
                                                executableElement.getParameters().stream()
                                                        .map(parameter ->
                                                                ObjectValueWithVariable.of(
                                                                        INPUT_INVOKE_PARAMETER_INPUT_VALUE_NAME_NAME,
                                                                        parameter.getSimpleName().toString(),
                                                                        INPUT_INVOKE_PARAMETER_INPUT_VALUE_CLASS_NAME_NAME,
                                                                        ElementUtil.getTypeNameFromTypeMirror(parameter.asType(), typeUtils)
                                                                )
                                                        )
                                                        .collect(Collectors.toList())
                                        ),
                                        INPUT_INVOKE_INPUT_VALUE_RETURN_CLASS_NAME_NAME, ElementUtil.getTypeNameFromTypeMirror(executableElement.getReturnType(), typeUtils),
                                        INPUT_INVOKE_INPUT_VALUE_ASYNC_NAME, async
                                );

                                executableElement.getParameters().stream()
                                        .filter(variableElement ->
                                                variableElement.getAnnotation(Source.class) != null &&
                                                        typeUtils.asElement(variableElement.asType()).getAnnotation(Input.class) != null
                                        )
                                        .map(variableElement -> getNameFromElement(typeUtils.asElement(variableElement.asType())))
                                        .map(inputName ->
                                                documentManager.getDocument().getInputObjectType(inputName)
                                                        .orElseGet(() ->
                                                                documentManager.getDocument()
                                                                        .addDefinition(new InputObjectType(inputName))
                                                                        .getInputObjectTypeOrError(inputName)
                                                        )
                                        )
                                        .filter(inputObjectType -> !inputObjectType.isInputInterface())
                                        .findFirst()
                                        .ifPresent(inputObjectType ->
                                                Optional.ofNullable(inputObjectType.getDirective(DIRECTIVE_INVOKES_NAME))
                                                        .ifPresentOrElse(
                                                                directive -> directive.getArgumentOrNull(DIRECTIVE_INVOKES_METHODS_NAME).asArray().add(invoke),
                                                                () -> inputObjectType
                                                                        .addDirective(
                                                                                new Directive(DIRECTIVE_INVOKES_NAME)
                                                                                        .addArgument(DIRECTIVE_INVOKES_METHODS_NAME, new ArrayValueWithVariable(invoke))
                                                                        )
                                                        )
                                        );

                                executableElement.getParameters().stream()
                                        .filter(variableElement ->
                                                variableElement.getAnnotation(Source.class) != null &&
                                                        typeUtils.asElement(variableElement.asType()).getAnnotation(Input.class) != null
                                        )
                                        .map(variableElement -> getNameFromElement(typeUtils.asElement(variableElement.asType())))
                                        .map(inputName ->
                                                documentManager.getDocument().getInputObjectType(inputName)
                                                        .orElseGet(() ->
                                                                documentManager.getDocument()
                                                                        .addDefinition(new InputObjectType(inputName).addDirective(new Directive(DIRECTIVE_INTERFACE_NAME)))
                                                                        .getInputObjectTypeOrError(inputName)
                                                        )
                                        )
                                        .filter(InputObjectType::isInputInterface)
                                        .flatMap(inputObjectType -> documentManager.getDocument().getImplementsInputObject(inputObjectType.getName()))
                                        .forEach(inputObjectType ->
                                                Optional.ofNullable(inputObjectType.getDirective(DIRECTIVE_INVOKES_NAME))
                                                        .ifPresentOrElse(
                                                                directive -> directive.getArgumentOrNull(DIRECTIVE_INVOKES_METHODS_NAME).asArray().add(invoke),
                                                                () -> inputObjectType
                                                                        .addDirective(
                                                                                new Directive(DIRECTIVE_INVOKES_NAME)
                                                                                        .addArgument(DIRECTIVE_INVOKES_METHODS_NAME, new ArrayValueWithVariable(invoke))
                                                                        )
                                                        )
                                        );
                            }
                        }
                );
    }
}
