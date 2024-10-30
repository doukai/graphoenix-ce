package io.graphoenix.core.annotation.processor;

import io.graphoenix.core.config.PackageConfig;
import io.graphoenix.core.handler.DocumentManager;
import io.graphoenix.core.handler.GraphQLConfigRegister;
import io.graphoenix.spi.annotation.Application;
import io.graphoenix.spi.annotation.GraphQLOperation;
import io.graphoenix.spi.annotation.Package;
import io.graphoenix.spi.annotation.SelectionSet;
import io.graphoenix.spi.error.GraphQLErrors;
import io.graphoenix.spi.graphql.Definition;
import io.graphoenix.spi.graphql.common.ArrayValueWithVariable;
import io.graphoenix.spi.graphql.common.Directive;
import io.graphoenix.spi.graphql.common.ObjectValueWithVariable;
import io.graphoenix.spi.graphql.operation.Field;
import io.graphoenix.spi.graphql.operation.Operation;
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
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import javax.tools.Diagnostic;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static io.graphoenix.spi.constant.Hammurabi.*;
import static io.graphoenix.spi.error.GraphQLErrorType.FIELD_DEFINITION_NOT_EXIST;
import static io.graphoenix.spi.error.GraphQLErrorType.UNSUPPORTED_OPERATION_TYPE;
import static io.graphoenix.spi.utils.DocumentUtil.graphqlToSelectionSet;
import static io.graphoenix.spi.utils.ElementUtil.*;

public abstract class BaseProcessor extends AbstractProcessor {

    private PackageConfig packageConfig;
    private DocumentManager documentManager;
    private Types types;
    private Elements elements;

    @Override
    public void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        types = processingEnv.getTypeUtils();
        elements = processingEnv.getElementUtils();
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
                .map(packageElement -> packageElement.getQualifiedName().toString())
                .or(() ->
                        roundEnv.getElementsAnnotatedWith(Application.class).stream()
                                .filter(element -> element.getKind().equals(ElementKind.CLASS))
                                .findFirst()
                                .map(element -> (TypeElement) element)
                                .map(typeElement -> processingEnv.getElementUtils().getPackageOf(typeElement).getQualifiedName().toString())
                );
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
                                .map(element ->
                                        (EnumType) new EnumType((TypeElement) element)
                                                .addDirective(
                                                        new Directive(DIRECTIVE_PACKAGE_NAME)
                                                                .addArgument(DIRECTIVE_PACKAGE_ARGUMENT_NAME_NAME, packageConfig.getPackageName())
                                                )
                                )
                                .collect(Collectors.toList())
                )
                .addDefinitions(
                        roundEnv.getElementsAnnotatedWith(Interface.class).stream()
                                .filter(element -> element.getAnnotation(Generated.class) == null)
                                .filter(element -> element.getKind().equals(ElementKind.INTERFACE))
                                .map(element ->
                                        (InterfaceType) new InterfaceType((TypeElement) element, types)
                                                .addDirective(
                                                        new Directive(DIRECTIVE_PACKAGE_NAME)
                                                                .addArgument(DIRECTIVE_PACKAGE_ARGUMENT_NAME_NAME, packageConfig.getPackageName())
                                                )
                                )
                                .collect(Collectors.toList())
                )
                .addDefinitions(
                        roundEnv.getElementsAnnotatedWith(Type.class).stream()
                                .filter(element -> element.getAnnotation(Generated.class) == null)
                                .filter(element -> element.getKind().equals(ElementKind.CLASS))
                                .map(element ->
                                        (ObjectType) new ObjectType((TypeElement) element, types)
                                                .addDirective(
                                                        new Directive(DIRECTIVE_PACKAGE_NAME)
                                                                .addArgument(DIRECTIVE_PACKAGE_ARGUMENT_NAME_NAME, packageConfig.getPackageName())
                                                )
                                )
                                .collect(Collectors.toList())
                )
                .addDefinitions(
                        roundEnv.getElementsAnnotatedWith(Input.class).stream()
                                .filter(element -> element.getAnnotation(Generated.class) == null)
                                .filter(element -> element.getKind().equals(ElementKind.CLASS))
                                .map(element ->
                                        (InputObjectType) new InputObjectType((TypeElement) element, types)
                                                .addDirective(
                                                        new Directive(DIRECTIVE_PACKAGE_NAME)
                                                                .addArgument(DIRECTIVE_PACKAGE_ARGUMENT_NAME_NAME, packageConfig.getPackageName())
                                                )
                                )
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
                                                .map(subElement ->
                                                        (EnumType) new EnumType((TypeElement) subElement)
                                                                .addDirective(
                                                                        new Directive(DIRECTIVE_PACKAGE_NAME)
                                                                                .addArgument(DIRECTIVE_PACKAGE_ARGUMENT_NAME_NAME, packageConfig.getPackageName())
                                                                )
                                                )
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
                                                .map(subElement ->
                                                        (EnumType) new EnumType((TypeElement) subElement)
                                                                .addDirective(
                                                                        new Directive(DIRECTIVE_PACKAGE_NAME)
                                                                                .addArgument(DIRECTIVE_PACKAGE_ARGUMENT_NAME_NAME, packageConfig.getPackageName())
                                                                )
                                                )
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
                                                .map(subElement ->
                                                        (EnumType) new EnumType((TypeElement) subElement)
                                                                .addDirective(
                                                                        new Directive(DIRECTIVE_PACKAGE_NAME)
                                                                                .addArgument(DIRECTIVE_PACKAGE_ARGUMENT_NAME_NAME, packageConfig.getPackageName())
                                                                )
                                                )
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
                                FieldDefinition fieldDefinition = new FieldDefinition(executableElement, types)
                                        .addDirective(new Directive(DIRECTIVE_PACKAGE_NAME).addArgument(DIRECTIVE_PACKAGE_ARGUMENT_NAME_NAME, packageConfig.getPackageName()));
                                documentManager.getDocument().getQueryOperationType()
                                        .ifPresentOrElse(
                                                objectType -> objectType.addField(fieldDefinition),
                                                () -> documentManager.getDocument().addDefinition(new ObjectType(TYPE_QUERY_NAME).addField(fieldDefinition))
                                        );
                            } else if (executableElement.getAnnotation(Mutation.class) != null) {
                                FieldDefinition fieldDefinition = new FieldDefinition(executableElement, types)
                                        .addDirective(new Directive(DIRECTIVE_PACKAGE_NAME).addArgument(DIRECTIVE_PACKAGE_ARGUMENT_NAME_NAME, packageConfig.getPackageName()));
                                documentManager.getDocument().getMutationOperationType()
                                        .ifPresentOrElse(
                                                objectType -> objectType.addField(fieldDefinition),
                                                () -> documentManager.getDocument().addDefinition(new ObjectType(TYPE_MUTATION_NAME).addField(fieldDefinition))
                                        );
                            } else if (executableElement.getParameters().stream()
                                    .anyMatch(variableElement ->
                                            variableElement.getAnnotation(Source.class) != null &&
                                                    types.asElement(variableElement.asType()).getAnnotation(Type.class) != null
                                    )
                            ) {
                                executableElement.getParameters().stream()
                                        .filter(variableElement -> variableElement.getAnnotation(Source.class) != null)
                                        .findFirst()
                                        .ifPresent(variableElement -> {
                                                    FieldDefinition fieldDefinition = new FieldDefinition(executableElement, types);
                                                    String typeName = getNameFromElement(types.asElement(variableElement.asType()));
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
                                                    types.asElement(variableElement.asType()).getAnnotation(Interface.class) != null
                                    )
                            ) {
                                executableElement.getParameters().stream()
                                        .filter(variableElement -> variableElement.getAnnotation(Source.class) != null)
                                        .findFirst()
                                        .ifPresent(variableElement -> {
                                                    FieldDefinition fieldDefinition = new FieldDefinition(executableElement, types);
                                                    String typeName = getNameFromElement(types.asElement(variableElement.asType()));
                                                    documentManager.getDocument().getImplementsObjectType(typeName)
                                                            .forEach(objectType -> objectType.addField(fieldDefinition));
                                                }
                                        );
                            } else if (executableElement.getParameters().stream()
                                    .anyMatch(variableElement ->
                                            variableElement.getAnnotation(Source.class) != null &&
                                                    types.asElement(variableElement.asType()).getAnnotation(Input.class) != null
                                    )
                            ) {
                                boolean async = executableElement.getAnnotation(Async.class) != null;
                                ObjectValueWithVariable invoke = ObjectValueWithVariable.of(
                                        INPUT_INVOKE_INPUT_VALUE_CLASS_NAME_NAME, executableElement.getEnclosingElement().toString(),
                                        INPUT_INVOKE_INPUT_VALUE_METHOD_NAME_NAME, async ? getAsyncMethodName(executableElement, types) : executableElement.getSimpleName().toString(),
                                        INPUT_INVOKE_INPUT_VALUE_PARAMETER_NAME,
                                        new ArrayValueWithVariable(
                                                executableElement.getParameters().stream()
                                                        .map(parameter ->
                                                                ObjectValueWithVariable.of(
                                                                        INPUT_INVOKE_PARAMETER_INPUT_VALUE_NAME_NAME,
                                                                        parameter.getSimpleName().toString(),
                                                                        INPUT_INVOKE_PARAMETER_INPUT_VALUE_CLASS_NAME_NAME,
                                                                        ElementUtil.getTypeWithArgumentsName(parameter.asType(), types)
                                                                )
                                                        )
                                                        .collect(Collectors.toList())
                                        ),
                                        INPUT_INVOKE_INPUT_VALUE_RETURN_CLASS_NAME_NAME, ElementUtil.getTypeWithArgumentsName(executableElement.getReturnType(), types),
                                        INPUT_INVOKE_INPUT_VALUE_ASYNC_NAME, async
                                );

                                executableElement.getParameters().stream()
                                        .filter(variableElement ->
                                                variableElement.getAnnotation(Source.class) != null &&
                                                        types.asElement(variableElement.asType()).getAnnotation(Input.class) != null
                                        )
                                        .map(variableElement -> getNameFromElement(types.asElement(variableElement.asType())))
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
                                                                directive -> directive.getArgument(DIRECTIVE_INVOKES_METHODS_NAME).asArray().add(invoke),
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
                                                        types.asElement(variableElement.asType()).getAnnotation(Input.class) != null
                                        )
                                        .map(variableElement -> getNameFromElement(types.asElement(variableElement.asType())))
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
                                                                directive -> directive.getArgument(DIRECTIVE_INVOKES_METHODS_NAME).asArray().add(invoke),
                                                                () -> inputObjectType
                                                                        .addDirective(
                                                                                new Directive(DIRECTIVE_INVOKES_NAME)
                                                                                        .addArgument(DIRECTIVE_INVOKES_METHODS_NAME, new ArrayValueWithVariable(invoke))
                                                                        )
                                                        )
                                        );
                            } else if (executableElement.getParameters().stream()
                                    .anyMatch(variableElement ->
                                            variableElement.getAnnotationMirrors().stream()
                                                    .anyMatch(annotationMirror ->
                                                            annotationMirror.getAnnotationType().getAnnotation(io.graphoenix.spi.annotation.Directive.class) != null
                                                    )
                                    )
                            ) {
                                executableElement.getParameters().stream()
                                        .flatMap(variableElement ->
                                                variableElement.getAnnotationMirrors().stream()
                                                        .filter(annotationMirror ->
                                                                annotationMirror.getAnnotationType().getAnnotation(io.graphoenix.spi.annotation.Directive.class) != null
                                                        )
                                        )
                                        .findFirst()
                                        .map(annotationMirror -> annotationMirror.getAnnotationType().getAnnotation(io.graphoenix.spi.annotation.Directive.class).value())
                                        .ifPresent(directiveName -> {
                                                    boolean async = executableElement.getAnnotation(Async.class) != null;
                                                    ObjectValueWithVariable invoke = ObjectValueWithVariable.of(
                                                            INPUT_INVOKE_INPUT_VALUE_CLASS_NAME_NAME, executableElement.getEnclosingElement().toString(),
                                                            INPUT_INVOKE_INPUT_VALUE_METHOD_NAME_NAME, async ? getAsyncMethodName(executableElement, types) : executableElement.getSimpleName().toString(),
                                                            INPUT_INVOKE_INPUT_VALUE_PARAMETER_NAME,
                                                            new ArrayValueWithVariable(
                                                                    executableElement.getParameters().stream()
                                                                            .map(parameter ->
                                                                                    ObjectValueWithVariable.of(
                                                                                            INPUT_INVOKE_PARAMETER_INPUT_VALUE_NAME_NAME,
                                                                                            parameter.getSimpleName().toString(),
                                                                                            INPUT_INVOKE_PARAMETER_INPUT_VALUE_CLASS_NAME_NAME,
                                                                                            ElementUtil.getTypeWithArgumentsName(parameter.asType(), types)
                                                                                    )
                                                                            )
                                                                            .collect(Collectors.toList())
                                                            ),
                                                            INPUT_INVOKE_INPUT_VALUE_RETURN_CLASS_NAME_NAME, ElementUtil.getTypeWithArgumentsName(executableElement.getReturnType(), types),
                                                            INPUT_INVOKE_INPUT_VALUE_ASYNC_NAME, async,
                                                            INPUT_INVOKE_INPUT_VALUE_DIRECTIVE_NAME_NAME, directiveName
                                                    );

                                                    documentManager.getDocument().getObjectTypes()
                                                            .flatMap(objectType -> objectType.getFields().stream())
                                                            .filter(fieldDefinition -> fieldDefinition.hasDirective(directiveName))
                                                            .forEach(fieldDefinition ->
                                                                    Optional.ofNullable(fieldDefinition.getDirective(DIRECTIVE_INVOKES_NAME))
                                                                            .ifPresentOrElse(
                                                                                    directive -> directive.getArgument(DIRECTIVE_INVOKES_METHODS_NAME).asArray().add(invoke),
                                                                                    () -> fieldDefinition
                                                                                            .addDirective(
                                                                                                    new Directive(DIRECTIVE_INVOKES_NAME)
                                                                                                            .addArgument(DIRECTIVE_INVOKES_METHODS_NAME, new ArrayValueWithVariable(invoke))
                                                                                            )
                                                                            )
                                                            );
                                                }
                                        );
                            }
                        }
                );
    }

    public void registerOperations(RoundEnvironment roundEnv) {
        roundEnv.getElementsAnnotatedWith(GraphQLOperation.class).stream()
                .filter(element -> element.getKind().equals(ElementKind.INTERFACE))
                .flatMap(element -> buildOperations((TypeElement) element))
                .forEach(operation -> documentManager.getDocument().addDefinition(operation));
    }

    public Stream<Operation> buildOperations(TypeElement typeElement) {
        return typeElement.getEnclosedElements().stream()
                .filter(element -> element.getKind().equals(ElementKind.METHOD))
                .map(element -> (ExecutableElement) element)
                .flatMap(executableElement ->
                        getOperationAnnotationMirror(executableElement)
                                .map(annotationMirror -> buildOperation(executableElement, annotationMirror)).stream()
                );
    }

    public Optional<? extends AnnotationMirror> getOperationAnnotationMirror(ExecutableElement executableElement) {
        return executableElement.getAnnotationMirrors().stream()
                .filter(annotationMirror ->
                        elements.getPackageOf(annotationMirror.getAnnotationType().asElement()).getQualifiedName().toString().equals(packageConfig.getAnnotationPackageName())
                )
                .findFirst();
    }

    public Operation buildOperation(ExecutableElement executableElement, AnnotationMirror annotationMirror) {
        TypeElement typeElement = (TypeElement) executableElement.getEnclosingElement();
        int index = typeElement.getEnclosedElements().indexOf(executableElement);
        Map.Entry<? extends ExecutableElement, ? extends AnnotationValue> filedEntry = annotationMirror.getElementValues().entrySet().stream()
                .findFirst()
                .orElseThrow(() -> new GraphQLErrors(FIELD_DEFINITION_NOT_EXIST.bind(annotationMirror.toString())));
        Field field = new Field(executableElement, filedEntry.getKey(), (AnnotationMirror) filedEntry.getValue());
        Operation operation = new Operation(
                typeElement.getQualifiedName().toString().replaceAll("\\.", "_") +
                        "_" +
                        executableElement.getSimpleName().toString() +
                        "_" +
                        index
        )
                .addDirectives(getDirectivesFromElement(executableElement))
                .addDirective(
                        new Directive()
                                .setName(DIRECTIVE_INVOKE_NAME)
                                .addArgument(DIRECTIVE_INVOKE_ARGUMENT_CLASS_NAME_NAME, executableElement.getEnclosingElement().toString())
                                .addArgument(DIRECTIVE_INVOKE_ARGUMENT_METHOD_NAME_NAME, executableElement.getSimpleName().toString())
                                .addArgument(DIRECTIVE_INVOKE_ARGUMENT_METHOD_INDEX_NAME, index)
                                .addArgument(
                                        DIRECTIVE_INVOKE_ARGUMENT_PARAMETER_NAME,
                                        new ArrayValueWithVariable(
                                                executableElement.getParameters().stream()
                                                        .map(parameter ->
                                                                Map.of(
                                                                        INPUT_INVOKE_PARAMETER_INPUT_VALUE_NAME_NAME,
                                                                        parameter.getSimpleName().toString(),
                                                                        INPUT_INVOKE_PARAMETER_INPUT_VALUE_CLASS_NAME_NAME,
                                                                        getTypeWithArgumentsName(parameter.asType(), types)
                                                                )
                                                        )
                                                        .collect(Collectors.toList())
                                        )
                                )
                                .addArgument(DIRECTIVE_INVOKE_ARGUMENT_RETURN_CLASS_NAME_NAME, getTypeWithArgumentsName(executableElement.getReturnType(), types))
                                .addArgument(DIRECTIVE_INVOKE_ARGUMENT_THROWN_TYPES_NAME,
                                        Stream.ofNullable(executableElement.getThrownTypes())
                                                .flatMap(Collection::stream)
                                                .map(typeMirror -> getTypeWithArgumentsName(typeMirror, types))
                                                .collect(Collectors.toList())
                                )
                )
                .addDirective(
                        new Directive(DIRECTIVE_PACKAGE_NAME)
                                .addArgument(DIRECTIVE_PACKAGE_ARGUMENT_NAME_NAME, packageConfig.getPackageName())
                );
        FieldDefinition fieldDefinition;
        switch (annotationMirror.getAnnotationType().asElement().getSimpleName().toString()) {
            case TYPE_QUERY_NAME:
                operation.setOperationType(OPERATION_QUERY_NAME);
                fieldDefinition = documentManager.getDocument().getQueryOperationTypeOrError().getField(filedEntry.getKey().getSimpleName().toString());
                break;
            case TYPE_MUTATION_NAME:
                operation.setOperationType(OPERATION_MUTATION_NAME);
                fieldDefinition = documentManager.getDocument().getMutationOperationTypeOrError().getField(filedEntry.getKey().getSimpleName().toString());
                break;
            default:
                throw new GraphQLErrors(UNSUPPORTED_OPERATION_TYPE);
        }
        Definition fieldTypeDefinition = documentManager.getFieldTypeDefinition(fieldDefinition);
        if (fieldTypeDefinition.isObject()) {
            SelectionSet selectionSet = executableElement.getAnnotation(SelectionSet.class);
            if (selectionSet != null) {
                String value = selectionSet.value();
                if (!value.isBlank()) {
                    field.setSelections(graphqlToSelectionSet(value).selection().stream().map(Field::new).collect(Collectors.toList()));
                } else {
                    int layers = selectionSet.layers();
                    field.setSelections(buildFields(fieldTypeDefinition.asObject(), 0, layers));
                }
            } else {
                field.setSelections(buildFields(fieldTypeDefinition.asObject(), 0, 0));
            }
        }
        return operation.addSelection(field);
    }

    public List<Field> buildFields(ObjectType objectType, int level, int layers) {
        return objectType.getFields().stream()
                .filter(fieldDefinition -> !fieldDefinition.isInvokeField())
                .filter(fieldDefinition -> !fieldDefinition.isFetchField())
                .filter(fieldDefinition -> !fieldDefinition.isFunctionField())
                .filter(fieldDefinition -> !fieldDefinition.isConnectionField())
                .filter(fieldDefinition -> !fieldDefinition.isAggregateField())
                .flatMap(fieldDefinition -> {
                            Definition fieldTypeDefinition = documentManager.getFieldTypeDefinition(fieldDefinition);
                            if (fieldTypeDefinition.isObject()) {
                                if (level < layers) {
                                    return Stream.of(
                                            new Field(fieldDefinition.getName())
                                                    .setSelections(buildFields(fieldTypeDefinition.asObject(), level + 1, layers))
                                    );
                                } else {
                                    return Stream.empty();
                                }
                            } else {
                                return Stream.of(new Field(fieldDefinition.getName()));
                            }
                        }
                )
                .collect(Collectors.toList());
    }
}
