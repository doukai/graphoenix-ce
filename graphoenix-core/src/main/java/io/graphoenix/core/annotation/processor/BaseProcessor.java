package io.graphoenix.core.annotation.processor;

import graphql.parser.antlr.GraphqlParser;
import io.graphoenix.core.config.GraphQLConfig;
import io.graphoenix.core.config.PackageConfig;
import io.graphoenix.core.handler.DocumentBuilder;
import io.graphoenix.core.handler.DocumentManager;
import io.graphoenix.core.handler.GraphQLConfigRegister;
import io.graphoenix.spi.annotation.Ignore;
import io.graphoenix.spi.annotation.Package;
import io.nozdormu.config.TypesafeConfig;
import io.nozdormu.spi.context.BeanContext;
import jakarta.json.JsonValue;
import jakarta.json.spi.JsonProvider;
import org.eclipse.microprofile.config.Config;
import org.eclipse.microprofile.graphql.Enum;
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
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public abstract class BaseProcessor extends AbstractProcessor {

    private GraphQLConfig graphQLConfig;
    private PackageConfig packageConfig;
    private DocumentManager documentManager;
    private DocumentBuilder documentBuilder;
    private JsonProvider jsonProvider;
    private Types typeUtils;

    @Override
    public void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        typeUtils = processingEnv.getTypeUtils();
        Filer filer = processingEnv.getFiler();
        BeanContext.load(BaseProcessor.class.getClassLoader());
        Config config = BeanContext.get(Config.class);
        ((TypesafeConfig) config).load(filer);
        graphQLConfig = BeanContext.get(GraphQLConfig.class);
        packageConfig = BeanContext.get(PackageConfig.class);
        documentBuilder = BeanContext.get(DocumentBuilder.class);
        jsonProvider = BeanContext.get(JsonProvider.class);
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
        roundEnv.getElementsAnnotatedWith(Enum.class).stream()
                .filter(element -> element.getAnnotation(Ignore.class) == null)
                .filter(element -> element.getKind().equals(ElementKind.ENUM))
                .forEach(element -> manager.mergeDocument(javaElementToEnum.buildEnum((TypeElement) element).toString()));

        roundEnv.getElementsAnnotatedWith(Interface.class).stream()
                .filter(element -> element.getAnnotation(Ignore.class) == null)
                .filter(element -> element.getKind().equals(ElementKind.INTERFACE))
                .forEach(element -> {
                            manager.mergeDocument(javaElementToInterface.buildInterface((TypeElement) element, typeUtils).toString());
                            element.getEnclosedElements().stream()
                                    .filter(subElement -> subElement.getAnnotation(Ignore.class) == null)
                                    .filter(subElement -> subElement.getAnnotation(Enum.class) != null)
                                    .filter(subElement -> subElement.getKind().equals(ElementKind.ENUM))
                                    .forEach(subElement -> manager.mergeDocument(javaElementToEnum.buildEnum((TypeElement) subElement).toString()));
                        }
                );

        roundEnv.getElementsAnnotatedWith(Type.class).stream()
                .filter(element -> element.getAnnotation(Ignore.class) == null)
                .filter(element -> element.getKind().equals(ElementKind.CLASS))
                .forEach(element -> {
                            manager.mergeDocument(javaElementToObject.buildObject((TypeElement) element, typeUtils).toString());
                            element.getEnclosedElements().stream()
                                    .filter(subElement -> subElement.getAnnotation(Ignore.class) == null)
                                    .filter(subElement -> subElement.getAnnotation(Enum.class) != null)
                                    .filter(subElement -> subElement.getKind().equals(ElementKind.ENUM))
                                    .forEach(subElement -> manager.mergeDocument(javaElementToEnum.buildEnum((TypeElement) subElement).toString()));
                        }
                );

        roundEnv.getElementsAnnotatedWith(Input.class).stream()
                .filter(element -> element.getAnnotation(Ignore.class) == null)
                .filter(element -> element.getKind().equals(ElementKind.CLASS))
                .forEach(element -> {
                            manager.mergeDocument(javaElementToInputType.buildInputType((TypeElement) element, typeUtils).toString());
                            element.getEnclosedElements().stream()
                                    .filter(subElement -> subElement.getAnnotation(Ignore.class) == null)
                                    .filter(subElement -> subElement.getAnnotation(Enum.class) != null)
                                    .filter(subElement -> subElement.getKind().equals(ElementKind.ENUM))
                                    .forEach(subElement -> manager.mergeDocument(javaElementToEnum.buildEnum((TypeElement) subElement).toString()));
                        }
                );

        roundEnv.getElementsAnnotatedWith(GraphQLApi.class).stream()
                .filter(element -> element.getKind().equals(ElementKind.CLASS))
                .forEach(this::registerGraphQLApiElement);
    }

    private void registerGraphQLApiElement(Element element) {
        element.getEnclosedElements()
                .forEach(subElement -> {
                            if (subElement.getAnnotation(Query.class) != null && subElement.getKind().equals(ElementKind.METHOD)) {
                                ObjectType objectType = manager.getObject(manager.getQueryOperationTypeName().orElse(QUERY_TYPE_NAME))
                                        .map(objectTypeDefinitionContext -> documentBuilder.buildObject(objectTypeDefinitionContext))
                                        .orElseGet(() -> new ObjectType(QUERY_TYPE_NAME));
                                objectType.addField(graphQLApiBuilder.variableElementToField((ExecutableElement) subElement, typeUtils));
                                manager.mergeDocument(objectType.toString());
                            } else if (subElement.getAnnotation(Mutation.class) != null && subElement.getKind().equals(ElementKind.METHOD)) {
                                ObjectType objectType = manager.getObject(manager.getMutationOperationTypeName().orElse(MUTATION_TYPE_NAME))
                                        .map(objectTypeDefinitionContext -> documentBuilder.buildObject(objectTypeDefinitionContext))
                                        .orElseGet(() -> new ObjectType(MUTATION_TYPE_NAME));
                                objectType.addField(graphQLApiBuilder.variableElementToField((ExecutableElement) subElement, typeUtils));
                                manager.mergeDocument(objectType.toString());
                            } else if (subElement.getKind().equals(ElementKind.METHOD) &&
                                    ((ExecutableElement) subElement).getParameters().stream()
                                            .anyMatch(variableElement ->
                                                    variableElement.getAnnotation(Source.class) != null &&
                                                            typeUtils.asElement(variableElement.asType()).getAnnotation(Type.class) != null
                                            )
                            ) {
                                Tuple2<String, Field> objectField = graphQLApiBuilder.variableElementToObjectField((ExecutableElement) subElement, typeUtils);
                                GraphqlParser.ObjectTypeDefinitionContext objectTypeDefinitionContext = manager.getObject(objectField._1()).orElseThrow(() -> new GraphQLErrors(GraphQLErrorType.TYPE_NOT_EXIST.bind(objectField._1())));
                                ObjectType objectType = documentBuilder.buildObject(objectTypeDefinitionContext).addField(objectField._2());
                                manager.mergeDocument(objectType.toString());
                            } else if (subElement.getKind().equals(ElementKind.METHOD) &&
                                    ((ExecutableElement) subElement).getParameters().stream()
                                            .anyMatch(variableElement ->
                                                    variableElement.getAnnotation(Source.class) != null &&
                                                            typeUtils.asElement(variableElement.asType()).getAnnotation(Interface.class) != null
                                            )
                            ) {
                                Tuple2<String, Field> objectField = graphQLApiBuilder.variableElementToObjectField((ExecutableElement) subElement, typeUtils);
                                manager.getImplementsObjectType(objectField._1())
                                        .filter(objectTypeDefinitionContext -> manager.isNotOperationType(objectTypeDefinitionContext))
                                        .forEach(objectTypeDefinitionContext -> {
                                                    ObjectType objectType = documentBuilder.buildObject(objectTypeDefinitionContext).addField(objectField._2());
                                                    manager.mergeDocument(objectType.toString());
                                                }
                                        );
                            } else if (subElement.getKind().equals(ElementKind.METHOD) &&
                                    ((ExecutableElement) subElement).getParameters().stream()
                                            .anyMatch(variableElement ->
                                                    variableElement.getAnnotation(Source.class) != null &&
                                                            typeUtils.asElement(variableElement.asType()).getAnnotation(Input.class) != null
                                            )
                            ) {
                                ExecutableElement executableElement = (ExecutableElement) subElement;
                                executableElement.getParameters().stream()
                                        .filter(variableElement ->
                                                variableElement.getAnnotation(Source.class) != null &&
                                                        typeUtils.asElement(variableElement.asType()).getAnnotation(Input.class) != null
                                        )
                                        .map(variableElement -> typeUtils.asElement(variableElement.asType()).getSimpleName().toString())
                                        .filter(inputName -> manager.getInputObject(inputName).isEmpty() || !manager.isInterface(manager.getInputObject(inputName).get()))
                                        .map(inputName ->
                                                manager.getInputObject(inputName)
                                                        .map(inputObjectTypeDefinitionContext ->
                                                                documentBuilder.buildInputObjectType(inputObjectTypeDefinitionContext))
                                                        .orElseGet(() -> new InputObjectType(inputName))
                                        )
                                        .findFirst()
                                        .ifPresent(inputObjectType -> {
                                                    Map<String, Object> invoke = Map.of(
                                                            "className", executableElement.getEnclosingElement().toString(),
                                                            "methodName", executableElement.getSimpleName().toString(),
                                                            "parameters",
                                                            new ArrayValueWithVariable(
                                                                    executableElement.getParameters().stream()
                                                                            .map(parameter -> Map.of("name", parameter.getSimpleName().toString(), "className", ELEMENT_UTIL.getTypeMirrorName(parameter.asType(), typeUtils)))
                                                                            .collect(Collectors.toList())
                                                            ),
                                                            "returnClassName", ELEMENT_UTIL.getTypeMirrorName(executableElement.getReturnType(), typeUtils)
                                                    );
                                                    Optional<Directive> invokes = Stream.ofNullable(inputObjectType.getDirectives())
                                                            .flatMap(Collection::stream)
                                                            .filter(directive -> directive.getName().equals(INVOKES_DIRECTIVE_NAME))
                                                            .findFirst();
                                                    if (invokes.isPresent() && invokes.get().getArguments().get(LIST_INPUT_NAME) != null && invokes.get().getArguments().get(LIST_INPUT_NAME).getValueType().equals(JsonValue.ValueType.ARRAY)) {
                                                        invokes.get().getArguments().put(LIST_INPUT_NAME, jsonProvider.createArrayBuilder(invokes.get().getArguments().get(LIST_INPUT_NAME).asJsonArray()).add(new ObjectValueWithVariable(invoke)).build());
                                                    } else {
                                                        inputObjectType.addDirective(
                                                                new Directive()
                                                                        .setName(INVOKES_DIRECTIVE_NAME)
                                                                        .addArgument(LIST_INPUT_NAME,
                                                                                new ArrayValueWithVariable(
                                                                                        Collections.singleton(invoke)
                                                                                )
                                                                        )
                                                        );
                                                    }
                                                    manager.registerGraphQL(inputObjectType.toString());
                                                }
                                        );

                                executableElement.getParameters().stream()
                                        .filter(variableElement ->
                                                variableElement.getAnnotation(Source.class) != null &&
                                                        typeUtils.asElement(variableElement.asType()).getAnnotation(Input.class) != null
                                        )
                                        .map(variableElement -> typeUtils.asElement(variableElement.asType()).getSimpleName().toString())
                                        .flatMap(inputName -> manager.getInputObject(inputName).stream())
                                        .filter(manager::isInterface)
                                        .flatMap(inputObjectTypeDefinitionContext -> manager.getImplementsInputObjectTypeDefinition(inputObjectTypeDefinitionContext.name().getText()))
                                        .map(inputObjectTypeDefinitionContext -> documentBuilder.buildInputObjectType(inputObjectTypeDefinitionContext))
                                        .forEach(inputObjectType -> {
                                                    Map<String, Object> invoke = Map.of(
                                                            "className", executableElement.getEnclosingElement().toString(),
                                                            "methodName", executableElement.getSimpleName().toString(),
                                                            "parameters",
                                                            new ArrayValueWithVariable(
                                                                    executableElement.getParameters().stream()
                                                                            .map(parameter -> Map.of("name", parameter.getSimpleName().toString(), "className", ELEMENT_UTIL.getTypeMirrorName(parameter.asType(), typeUtils)))
                                                                            .collect(Collectors.toList())
                                                            ),
                                                            "returnClassName", ELEMENT_UTIL.getTypeMirrorName(executableElement.getReturnType(), typeUtils)
                                                    );
                                                    Optional<Directive> invokes = Stream.ofNullable(inputObjectType.getDirectives())
                                                            .flatMap(Collection::stream)
                                                            .filter(directive -> directive.getName().equals(INVOKES_DIRECTIVE_NAME))
                                                            .findFirst();
                                                    if (invokes.isPresent() && invokes.get().getArguments().get("list") != null && invokes.get().getArguments().get("list").getValueType().equals(JsonValue.ValueType.ARRAY)) {
                                                        invokes.get().getArguments().put("list", jsonProvider.createArrayBuilder(invokes.get().getArguments().get("list").asJsonArray()).add(new ObjectValueWithVariable(invoke)).build());
                                                    } else {
                                                        inputObjectType.addDirective(
                                                                new Directive()
                                                                        .setName(INVOKES_DIRECTIVE_NAME)
                                                                        .addArgument("list",
                                                                                new ArrayValueWithVariable(
                                                                                        Collections.singleton(invoke)
                                                                                )
                                                                        )
                                                        );
                                                    }
                                                    manager.registerGraphQL(inputObjectType.toString());
                                                }
                                        );
                            }
                        }
                );
    }
}
