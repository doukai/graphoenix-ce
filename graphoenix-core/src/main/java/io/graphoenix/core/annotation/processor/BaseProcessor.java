package io.graphoenix.core.annotation.processor;

import io.graphoenix.core.config.PackageConfig;
import io.graphoenix.core.handler.DocumentManager;
import io.graphoenix.core.handler.GraphQLConfigRegister;
import io.graphoenix.spi.annotation.*;
import io.graphoenix.spi.annotation.Package;
import io.graphoenix.spi.error.GraphQLErrors;
import io.graphoenix.spi.graphql.AbstractDefinition;
import io.graphoenix.spi.graphql.Definition;
import io.graphoenix.spi.graphql.Document;
import io.graphoenix.spi.graphql.common.*;
import io.graphoenix.spi.graphql.common.Directive;
import io.graphoenix.spi.graphql.operation.Field;
import io.graphoenix.spi.graphql.operation.Operation;
import io.graphoenix.spi.graphql.type.*;
import io.graphoenix.spi.graphql.type.TypeName;
import io.graphoenix.spi.utils.ElementUtil;
import io.graphoenix.spi.utils.StreamUtil;
import io.nozdormu.config.TypesafeConfig;
import io.nozdormu.spi.async.Async;
import io.nozdormu.spi.context.BeanContext;
import jakarta.annotation.Generated;
import org.eclipse.microprofile.config.Config;
import org.eclipse.microprofile.graphql.Enum;
import org.eclipse.microprofile.graphql.Type;
import org.eclipse.microprofile.graphql.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.*;
import javax.lang.model.util.Types;
import javax.tools.Diagnostic;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static io.graphoenix.spi.constant.Hammurabi.*;
import static io.graphoenix.spi.error.GraphQLErrorType.FIELD_DEFINITION_NOT_EXIST;
import static io.graphoenix.spi.error.GraphQLErrorType.UNSUPPORTED_OPERATION_TYPE;
import static io.graphoenix.spi.utils.DocumentUtil.graphqlToSelectionSet;
import static io.graphoenix.spi.utils.ElementUtil.*;

public abstract class BaseProcessor extends AbstractProcessor {

    private final static Logger logger = LoggerFactory.getLogger(BaseProcessor.class);

    protected static final Document DOCUMENT_CACHE = new Document();

    private PackageConfig packageConfig;
    private DocumentManager documentManager;
    private Types types;

    @Override
    public void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        types = processingEnv.getTypeUtils();
        Filer filer = processingEnv.getFiler();
        BeanContext.setClassLoader(BaseProcessor.class.getClassLoader());
        Config config = BeanContext.get(Config.class);
        ((TypesafeConfig) config).load(filer);
        packageConfig = BeanContext.get(PackageConfig.class);
        documentManager = BeanContext.get(DocumentManager.class);
        GraphQLConfigRegister configRegister = BeanContext.get(GraphQLConfigRegister.class);

        try {
            documentManager.getDocument().clear();
            configRegister.registerConfig(filer);
        } catch (IOException e) {
            logger.error(e.getMessage());
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
                                        INPUT_INVOKE_INPUT_VALUE_PACKAGE_NAME_NAME, packageConfig.getPackageName(),
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
                            } else if (executableElement.getParameters().stream()
                                    .anyMatch(variableElement ->
                                            variableElement.getAnnotationMirrors().stream()
                                                    .anyMatch(annotationMirror ->
                                                            annotationMirror.getAnnotationType().asElement().getAnnotation(io.graphoenix.spi.annotation.Directive.class) != null
                                                    )
                                    )
                            ) {
                                executableElement.getParameters().stream()
                                        .flatMap(variableElement ->
                                                variableElement.getAnnotationMirrors().stream()
                                                        .filter(annotationMirror ->
                                                                annotationMirror.getAnnotationType().asElement().getAnnotation(io.graphoenix.spi.annotation.Directive.class) != null
                                                        )
                                        )
                                        .findFirst()
                                        .map(annotationMirror -> annotationMirror.getAnnotationType().asElement().getAnnotation(io.graphoenix.spi.annotation.Directive.class).value())
                                        .ifPresent(directiveName -> {
                                                    boolean async = executableElement.getAnnotation(Async.class) != null;
                                                    boolean onField = executableElement.getAnnotation(OnField.class) != null;
                                                    boolean onInputValue = executableElement.getAnnotation(OnInputValue.class) != null;
                                                    boolean onExpression = executableElement.getAnnotation(OnExpression.class) != null;
                                                    ObjectValueWithVariable invoke = ObjectValueWithVariable.of(
                                                            INPUT_INVOKE_INPUT_VALUE_PACKAGE_NAME_NAME, packageConfig.getPackageName(),
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
                                                            INPUT_INVOKE_INPUT_VALUE_DIRECTIVE_NAME_NAME, directiveName,
                                                            INPUT_INVOKE_INPUT_VALUE_ON_FIELD_NAME, onField,
                                                            INPUT_INVOKE_INPUT_VALUE_ON_INPUT_VALUE_NAME, onInputValue,
                                                            INPUT_INVOKE_INPUT_VALUE_ON_EXPRESSION_NAME, onExpression
                                                    );

                                                    documentManager.getDocument().getDirective(directiveName)
                                                            .ifPresent(directiveDefinition ->
                                                                    Stream.ofNullable(directiveDefinition.getArguments())
                                                                            .flatMap(Collection::stream)
                                                                            .filter(inputValue -> inputValue.getName().equals(DIRECTIVE_INVOKES_NAME))
                                                                            .findFirst()
                                                                            .ifPresentOrElse(
                                                                                    inputValue -> inputValue.getDefaultValue().asObject().getValueWithVariable(DIRECTIVE_INVOKES_METHODS_NAME).asArray().add(invoke),
                                                                                    () -> directiveDefinition
                                                                                            .addArgument(
                                                                                                    new InputValue(DIRECTIVE_INVOKES_NAME)
                                                                                                            .setType(new ListType(new TypeName(INPUT_INVOKE_NAME)))
                                                                                                            .setDefaultValue(
                                                                                                                    ObjectValueWithVariable.of(DIRECTIVE_INVOKES_METHODS_NAME, new ArrayValueWithVariable(invoke))
                                                                                                            )
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
                        Stream.of(TYPE_QUERY_NAME, TYPE_MUTATION_NAME, TYPE_SUBSCRIPTION_NAME)
                                .anyMatch(name -> annotationMirror.getAnnotationType().asElement().getSimpleName().toString().equals(name))
                )
                .findFirst();
    }

    public Operation buildOperation(ExecutableElement executableElement, AnnotationMirror annotationMirror) {
        TypeElement typeElement = (TypeElement) executableElement.getEnclosingElement();
        int index = typeElement.getEnclosedElements().indexOf(executableElement);
        Map.Entry<? extends ExecutableElement, ? extends AnnotationValue> fieldEntry = annotationMirror.getElementValues().entrySet().stream()
                .findFirst()
                .orElseThrow(() -> new GraphQLErrors(FIELD_DEFINITION_NOT_EXIST.bind(annotationMirror.toString())));
        AnnotationMirror fieldAnnotationMirror = (AnnotationMirror) fieldEntry.getValue();

        Field field = new Field(executableElement, fieldEntry.getKey(), fieldAnnotationMirror);
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

        List<VariableDefinition> variableDefinitions = executableElement.getParameters().stream()
                .map(variableElement ->
                        new VariableDefinition()
                                .setVariable(new Variable(variableElement.getSimpleName().toString()))
                                .setType(variableElementToTypeName(variableElement, types))
                )
                .collect(Collectors.toList());

        if (!variableDefinitions.isEmpty()) {
            operation.setVariableDefinitions(variableDefinitions);
        }

        switch (annotationMirror.getAnnotationType().asElement().getSimpleName().toString()) {
            case TYPE_QUERY_NAME:
                operation.setOperationType(OPERATION_QUERY_NAME);
                break;
            case TYPE_MUTATION_NAME:
                operation.setOperationType(OPERATION_MUTATION_NAME);
                break;
            default:
                throw new GraphQLErrors(UNSUPPORTED_OPERATION_TYPE);
        }

        io.graphoenix.spi.graphql.type.Type type = executableElementToTypeName(executableElement, types);
        Definition fieldTypeDefinition = documentManager.getDocument().getDefinition(type.getTypeName().getName());
        if (fieldTypeDefinition.isObject()) {
            SelectionSet selectionSet = executableElement.getAnnotation(SelectionSet.class);
            if (selectionSet != null) {
                String value = selectionSet.value();
                if (!value.isBlank()) {
                    field.setSelections(
                            Stream
                                    .concat(
                                            graphqlToSelectionSet(value).selection().stream().map(Field::new),
                                            documentManager.getDocument().getInterfaceType(INTERFACE_META_NAME).stream()
                                                    .flatMap(interfaceType -> interfaceType.getFields().stream())
                                                    .map(fieldDefinition -> new Field(fieldDefinition.getName()))
                                    )
                                    .filter(StreamUtil.distinctByKey(AbstractDefinition::getName))
                                    .collect(Collectors.toList())
                    );
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
        return Stream
                .concat(
                        objectType.getFields().stream()
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
                                ),
                        documentManager.getDocument().getInterfaceType(INTERFACE_META_NAME).stream()
                                .flatMap(interfaceType -> interfaceType.getFields().stream())
                                .map(fieldDefinition -> new Field(fieldDefinition.getName()))
                )
                .filter(StreamUtil.distinctByKey(AbstractDefinition::getName))
                .collect(Collectors.toList());
    }
}
