package io.graphoenix.core.handler;

import graphql.parser.antlr.GraphqlParser;
import io.graphoenix.core.config.PackageConfig;
import io.graphoenix.spi.annotation.Application;
import io.graphoenix.spi.graphql.AbstractDefinition;
import io.graphoenix.spi.graphql.Definition;
import io.graphoenix.spi.graphql.Document;
import io.graphoenix.spi.graphql.common.Directive;
import io.graphoenix.spi.graphql.type.FieldDefinition;
import io.graphoenix.spi.graphql.type.InputValue;
import io.graphoenix.spi.graphql.type.ObjectType;
import jakarta.inject.Inject;

import java.util.LinkedHashSet;
import java.util.Optional;
import java.util.stream.Collectors;

import static io.graphoenix.core.utils.NameUtil.getGrpcTypeName;
import static io.graphoenix.core.utils.PredicateUtil.not;
import static io.graphoenix.spi.constant.Hammurabi.*;

@Application
public class DocumentBuilder {

    private final PackageConfig packageConfig;
    private final PackageManager packageManager;
    private  Document document;

    @Inject
    public DocumentBuilder(PackageConfig packageConfig, PackageManager packageManager) {
        this.packageConfig = packageConfig;
        this.packageManager = packageManager;
    }

    public Document build(Document document) {

        this.document = document;
        document.getDefinitions().stream()
                .filter(Definition::isObject)
                .map(definition -> (AbstractDefinition) definition)
                .filter(packageManager::isOwnPackage)
                .filter(abstractDefinition -> not(abstractDefinition.isContainerType()))
                .filter(abstractDefinition -> not(abstractDefinition.isOperationType()))


        return document;
    }

    public ObjectType buildObject(ObjectType objectType) {
        if (objectType.getPackageName().isEmpty()) {
            objectType.addDirective(
                    new Directive(DIRECTIVE_PACKAGE_INFO_NAME)
                            .addArgument("packageName", packageConfig.getPackageName())
                            .addArgument("grpcPackageName", packageConfig.getGrpcPackageName())
            );
        }

        if (objectType.getClassName().isEmpty()) {
            objectType.addDirective(
                    new Directive(DIRECTIVE_CLASS_INFO_NAME)
                            .addArgument("className", packageConfig.getObjectTypePackageName() + "." + objectType.getName())
                            .addArgument("grpcClassName", packageConfig.getGrpcObjectTypePackageName() + "." + getGrpcTypeName(objectType.getName()))
            );
        }

        objectType.setFields(
                fieldDefinitionContextList.stream()
                        .map(fieldDefinitionContext ->
                                manager.isNotInvokeField(fieldDefinitionContext) ?
                                        buildField(objectTypeDefinitionContext.name().getText(), fieldDefinitionContext, manager.isMutationOperationType(objectTypeDefinitionContext.name().getText())) :
                                        buildField(fieldDefinitionContext)
                        )
                        .collect(Collectors.toCollection(LinkedHashSet::new))
        );

        return objectType;
    }

    public FieldDefinition buildField(ObjectType objectType, FieldDefinition fieldDefinition) {


        Optional<GraphqlParser.ObjectTypeDefinitionContext> fieldObjectTypeDefinitionContext = manager.getObject(manager.getFieldTypeName(fieldDefinitionContext.type()));
        if (fieldObjectTypeDefinitionContext.isPresent()) {
            if (isMutationOperationType) {
                field.addArguments(buildArgumentsFromObjectType(fieldObjectTypeDefinitionContext.get(), InputType.INPUT));
            } else {
                field.addArguments(buildArgumentsFromObjectType(fieldObjectTypeDefinitionContext.get(), InputType.EXPRESSION))
                        .addArgument(new InputValue().setName(GROUP_BY_INPUT_NAME).setType(new ListType(new NonNullType(new TypeName("String")))));
                if (manager.fieldTypeIsList(fieldDefinitionContext.type())) {
                    field.addArgument(new InputValue().setName(FIRST_INPUT_NAME).setType("Int"))
                            .addArgument(new InputValue().setName(LAST_INPUT_NAME).setType("Int"))
                            .addArgument(new InputValue().setName(OFFSET_INPUT_NAME).setType("Int"));

                    manager.getFieldByDirective(typeName, CURSOR_DIRECTIVE_NAME)
                            .findFirst()
                            .or(() -> manager.getObjectTypeIDFieldDefinition(typeName))
                            .ifPresent(cursorFieldDefinitionContext ->
                                    field.addArgument(new InputValue().setName(AFTER_INPUT_NAME).setType(manager.getFieldTypeName(cursorFieldDefinitionContext.type())))
                                            .addArgument(new InputValue().setName(BEFORE_INPUT_NAME).setType(manager.getFieldTypeName(cursorFieldDefinitionContext.type())))
                            );

                    field.addArgument(new InputValue().setName(ORDER_BY_INPUT_NAME).setType(manager.getFieldTypeName(fieldDefinitionContext.type()) + InputType.ORDER_BY));
                }
            }
        } else if (manager.isScalar(manager.getFieldTypeName(fieldDefinitionContext.type())) || manager.isEnum(manager.getFieldTypeName(fieldDefinitionContext.type()))) {
            if (manager.fieldTypeIsList(fieldDefinitionContext.type())) {
                field.addArgument(new InputValue().setName("opr").setType("Operator").setDefaultValue("EQ"))
                        .addArgument(new InputValue().setName("val").setType(manager.getFieldTypeName(fieldDefinitionContext.type())))
                        .addArgument(new InputValue().setName("in").setType(new ListType(new TypeName(manager.getFieldTypeName(fieldDefinitionContext.type())))))
                        .addArgument(new InputValue().setName(FIRST_INPUT_NAME).setType("Int"))
                        .addArgument(new InputValue().setName(LAST_INPUT_NAME).setType("Int"))
                        .addArgument(new InputValue().setName(OFFSET_INPUT_NAME).setType("Int"));

                mapper.getWithObjectTypeDefinition(typeName, fieldDefinitionContext.name().getText())
                        .flatMap(objectTypeDefinitionContext -> manager.getFieldByDirective(objectTypeDefinitionContext.name().getText(), CURSOR_DIRECTIVE_NAME)
                                .findFirst()
                                .or(() -> manager.getObjectTypeIDFieldDefinition(objectTypeDefinitionContext.name().getText()))
                        )
                        .ifPresent(cursorFieldDefinitionContext ->
                                field.addArgument(new InputValue().setName(AFTER_INPUT_NAME).setType(manager.getFieldTypeName(cursorFieldDefinitionContext.type())))
                                        .addArgument(new InputValue().setName(BEFORE_INPUT_NAME).setType(manager.getFieldTypeName(cursorFieldDefinitionContext.type())))
                        );

                field.addArgument(new InputValue().setName(SORT_INPUT_NAME).setType("Sort"));
            }
        }
        return field;
    }


    public InputValue fieldToArgument(ObjectType objectType, FieldDefinition fieldDefinition) {
        String fieldTypeName = manager.getFieldTypeName(fieldDefinitionContext.type());
        if (inputType.equals(InputType.INPUT) || inputType.equals(InputType.MUTATION_ARGUMENTS)) {
            if (fieldDefinitionContext.name().getText().equals("__typename")) {
                return new InputValue().setName("__typename").setType("String").setDefaultValue(typeName);
            }
            String argumentTypeName;
            if (manager.isScalar(fieldTypeName) || manager.isEnum(fieldTypeName)) {
                argumentTypeName = fieldTypeName;
            } else {
                argumentTypeName = fieldTypeName + InputType.INPUT;
            }
            boolean isList = manager.fieldTypeIsList(fieldDefinitionContext.type());
            if (isList) {
                argumentTypeName = "[" + argumentTypeName + "]";
            }
            InputValue inputValue = new InputValue().setName(fieldDefinitionContext.name().getText()).setType(argumentTypeName);
            Optional.ofNullable(fieldDefinitionContext.directives())
                    .flatMap(directivesContext ->
                            directivesContext.directive().stream()
                                    .filter(directiveContext -> directiveContext.name().getText().equals(VALIDATION_DIRECTIVE_NAME)).findFirst()
                    )
                    .ifPresent(directiveContext -> inputValue.addDirective(new io.graphoenix.core.operation.Directive(directiveContext)));
            return inputValue;
        } else if (inputType.equals(InputType.EXPRESSION) || inputType.equals(InputType.QUERY_ARGUMENTS) || inputType.equals(InputType.SUBSCRIPTION_ARGUMENTS)) {
            if (fieldDefinitionContext.name().getText().equals(DEPRECATED_FIELD_NAME)) {
                return new InputValue().setName(DEPRECATED_INPUT_NAME).setType("Boolean").setDefaultValue("false");
            }
            String argumentTypeName;
            switch (fieldTypeName) {
                case "Boolean":
                    argumentTypeName = "Boolean" + InputType.EXPRESSION;
                    break;
                case "ID":
                case "String":
                case "Date":
                case "Time":
                case "DateTime":
                case "Timestamp":
                    argumentTypeName = "String" + InputType.EXPRESSION;
                    break;
                case "Int":
                case "BigInteger":
                    argumentTypeName = "Int" + InputType.EXPRESSION;
                    break;
                case "Float":
                case "BigDecimal":
                    argumentTypeName = "Float" + InputType.EXPRESSION;
                    break;
                default:
                    argumentTypeName = fieldTypeName + InputType.EXPRESSION;
                    break;
            }
            return new InputValue().setName(fieldDefinitionContext.name().getText()).setType(argumentTypeName);
        } else {
            return new InputValue().setName(fieldDefinitionContext.name().getText()).setType("Sort");
        }
    }
}
