package io.graphoenix.core.handler;

import graphql.parser.antlr.GraphqlParser;
import io.graphoenix.core.config.PackageConfig;
import io.graphoenix.spi.annotation.Application;
import io.graphoenix.spi.graphql.AbstractDefinition;
import io.graphoenix.spi.graphql.Definition;
import io.graphoenix.spi.graphql.Document;
import io.graphoenix.spi.graphql.common.Directive;
import io.graphoenix.spi.graphql.type.*;
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
    private Document document;

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
                .filter(abstractDefinition -> not(isOperationType(abstractDefinition)))


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


    public InputValue fieldToArgument(ObjectType objectType, FieldDefinition fieldDefinition, InputType inputType) {
        Definition fieldTypeDefinition = getFieldTypeDefinition(fieldDefinition);
        if (inputType.equals(InputType.INPUT) || inputType.equals(InputType.MUTATION_ARGUMENTS)) {
            if (fieldDefinition.getName().equals(FIELD_TYPE_NAME_NAME)) {
                return new InputValue(FIELD_TYPE_NAME_NAME).setType(SCALA_STRING_NAME).setDefaultValue(objectType.getName());
            }
            Type argumentType;
            if (fieldTypeDefinition.isLeaf()) {
                argumentType = new TypeName(fieldTypeDefinition.getName());
            } else {
                argumentType = new TypeName(fieldTypeDefinition.getName() + InputType.INPUT);
            }
            if (fieldDefinition.getType().isList()) {
                argumentType = new ListType(argumentType);
            }
            InputValue inputValue = new InputValue(fieldDefinition.getName()).setType(argumentType);
            Optional.ofNullable(fieldDefinition.getDirective(DIRECTIVE_VALIDATION_NAME))
                    .ifPresent(inputValue::addDirective);
            return inputValue;
        } else if (inputType.equals(InputType.EXPRESSION) || inputType.equals(InputType.QUERY_ARGUMENTS) || inputType.equals(InputType.SUBSCRIPTION_ARGUMENTS)) {
            if (fieldDefinition.getName().equals(FIELD_DEPRECATED_NAME)) {
                return new InputValue(INPUT_DEPRECATED_NAME).setType(SCALA_BOOLEAN_NAME).setDefaultValue(false);
            }
            Type argumentType;
            switch (fieldTypeDefinition.getName()) {
                case SCALA_BOOLEAN_NAME:
                    argumentType = new TypeName(SCALA_BOOLEAN_NAME + InputType.EXPRESSION);
                    break;
                case SCALA_ID_NAME:
                case SCALA_STRING_NAME:
                case SCALA_DATE_NAME:
                case SCALA_TIME_NAME:
                case SCALA_DATE_TIME_NAME:
                case SCALA_TIMESTAMP_NAME:
                    argumentType = new TypeName(SCALA_STRING_NAME + InputType.EXPRESSION);
                    break;
                case SCALA_INT_NAME:
                case SCALA_BIG_INTEGER_NAME:
                    argumentType = new TypeName(SCALA_INT_NAME + InputType.EXPRESSION);
                    break;
                case SCALA_FLOAT_NAME:
                case SCALA_BIG_DECIMAL_NAME:
                    argumentType = new TypeName(SCALA_FLOAT_NAME + InputType.EXPRESSION);
                    break;
                default:
                    argumentType = new TypeName(fieldTypeDefinition.getName() + InputType.EXPRESSION);
                    break;
            }
            return new InputValue(fieldDefinition.getName()).setType(argumentType);
        } else if (inputType.equals(InputType.ORDER_BY)) {
            return new InputValue(fieldTypeDefinition.getName()).setType(INPUT_SORT_NAME);
        }
        throw new RuntimeException("unsupported input type:" + inputType);
    }

    private enum InputType {
        EXPRESSION(SUFFIX_EXPRESSION),
        INPUT(SUFFIX_INPUT),
        QUERY_ARGUMENTS(SUFFIX_ARGUMENTS),
        MUTATION_ARGUMENTS(SUFFIX_ARGUMENTS),
        SUBSCRIPTION_ARGUMENTS(SUFFIX_ARGUMENTS),
        ORDER_BY(SUFFIX_ORDER_BY),
        CONNECTION(SUFFIX_CONNECTION),
        EDGE(SUFFIX_EDGE);

        private final String suffix;

        InputType(String suffix) {
            this.suffix = suffix;
        }

        @Override
        public String toString() {
            return suffix;
        }
    }

    public boolean isQueryOperationType(Definition definition) {
        return definition.isObject() &&
                definition.getName().equals(document.getSchema().map(Schema::getQuery).orElse(TYPE_QUERY_NAME));
    }

    public boolean isMutationOperationType(Definition definition) {
        return definition.isObject() &&
                definition.getName().equals(document.getSchema().map(Schema::getMutation).orElse(TYPE_MUTATION_NAME));
    }

    public boolean isSubscriptionOperationType(Definition definition) {
        return definition.isObject() &&
                definition.getName().equals(document.getSchema().map(Schema::getSubscription).orElse(TYPE_SUBSCRIPTION_NAME));
    }

    public boolean isOperationType(Definition definition) {
        return isQueryOperationType(definition) || isMutationOperationType(definition) || isSubscriptionOperationType(definition);
    }

    public Definition getFieldTypeDefinition(FieldDefinition fieldDefinition) {
        return document.getDefinition(fieldDefinition.getType().getTypeName().getName());
    }
}
