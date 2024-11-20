package io.graphoenix.spi.graphql;

import graphql.parser.antlr.GraphqlParser;
import io.graphoenix.spi.error.GraphQLErrors;
import io.graphoenix.spi.graphql.common.BooleanValue;
import io.graphoenix.spi.graphql.common.Directive;
import io.graphoenix.spi.graphql.common.StringValue;
import io.graphoenix.spi.graphql.common.ValueWithVariable;
import org.eclipse.microprofile.graphql.Source;

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static io.graphoenix.spi.constant.Hammurabi.*;
import static io.graphoenix.spi.error.GraphQLErrorType.*;
import static io.graphoenix.spi.utils.DocumentUtil.getStringValue;
import static io.graphoenix.spi.utils.ElementUtil.*;
import static io.graphoenix.spi.utils.StreamUtil.distinctByKey;

@SuppressWarnings("ALL")
public abstract class AbstractDefinition implements Definition {

    private String name;
    private String description;
    private Map<String, Directive> directiveMap;
    private boolean extension = false;

    public AbstractDefinition() {
    }

    public AbstractDefinition(String name) {
        this.name = name;
    }

    public AbstractDefinition(GraphqlParser.DescriptionContext description) {
        if (description != null) {
            this.description = getStringValue(description.StringValue());
        }
    }

    public AbstractDefinition(GraphqlParser.DescriptionContext description, GraphqlParser.DirectivesContext directivesContext) {
        this(description);
        if (directivesContext != null) {
            setDirectives(
                    directivesContext.directive().stream()
                            .map(Directive::new)
                            .collect(Collectors.toList())
            );
        }
    }

    public AbstractDefinition(List<GraphqlParser.DirectivesContext> directivesContextList) {
        if (directivesContextList != null) {
            setDirectives(
                    directivesContextList.stream()
                            .flatMap(directivesContext -> directivesContext.directive().stream())
                            .map(Directive::new)
                            .collect(Collectors.toList())
            );
        }
    }

    public AbstractDefinition(GraphqlParser.NameContext nameContext, GraphqlParser.DescriptionContext description, GraphqlParser.DirectivesContext directivesContext) {
        this(description, directivesContext);
        if (nameContext != null) {
            this.name = nameContext.getText();
        }
    }

    public AbstractDefinition(GraphqlParser.NameContext nameContext, GraphqlParser.DescriptionContext description) {
        this(nameContext, description, null);
    }

    public AbstractDefinition(TypeElement typeElement) {
        this.name = getNameFromElement(typeElement);
        this.description = getDescriptionFromElement(typeElement);
        this.addDirectives(getDirectivesFromElement(typeElement));
        this.addDirective(
                new Directive(DIRECTIVE_CLASS_NAME)
                        .addArgument(DIRECTIVE_CLASS_ARGUMENT_NAME_NAME, typeElement.getQualifiedName().toString())
                        .addArgument(DIRECTIVE_CLASS_ARGUMENT_EXISTS_NAME, true)
        );
    }

    public AbstractDefinition(VariableElement variableElement) {
        this.name = getNameFromElement(variableElement);
        this.description = getDescriptionFromElement(variableElement);
        this.addDirectives(getDirectivesFromElement(variableElement));
    }

    public AbstractDefinition(ExecutableElement executableElement) {
        this.name = executableElement.getParameters().stream()
                .filter(variableElement -> variableElement.getAnnotation(Source.class) != null)
                .filter(variableElement -> !variableElement.getAnnotation(Source.class).name().isBlank())
                .findFirst()
                .map(variableElement -> variableElement.getAnnotation(Source.class).name())
                .orElseGet(() -> getNameFromElement(executableElement));
        this.description = getDescriptionFromElement(executableElement);
        this.addDirectives(getDirectivesFromElement(executableElement));
    }

    public <T extends AbstractDefinition> T merge(AbstractDefinition... abstractDefinitions) {
        this.directiveMap = Stream
                .concat(
                        Stream.ofNullable(directiveMap).map(Map::values),
                        Stream.of(abstractDefinitions).flatMap(item -> Stream.ofNullable(item.getDirectives()))
                )
                .flatMap(Collection::stream)
                .filter(distinctByKey(Directive::getName))
                .collect(
                        Collectors.toMap(
                                Directive::getName,
                                directive -> directive,
                                (x, y) -> y,
                                LinkedHashMap::new
                        )
                );
        return (T) this;
    }

    public String getName() {
        return name;
    }

    public <T extends AbstractDefinition> T setName(String name) {
        this.name = name;
        return (T) this;
    }

    public String getDescription() {
        return description;
    }

    public <T extends AbstractDefinition> T setDescription(String description) {
        this.description = description;
        return (T) this;
    }

    public Map<String, Directive> getDirectiveMap() {
        return directiveMap;
    }

    public <T extends AbstractDefinition> T setDirectiveMap(Map<String, Directive> directiveMap) {
        this.directiveMap = new LinkedHashMap<>(directiveMap);
        return (T) this;
    }

    @Override
    public Directive getDirective(String name) {
        return Optional.ofNullable(directiveMap).map(stringDirectiveMap -> stringDirectiveMap.get(name)).orElse(null);
    }

    @Override
    public boolean hasDirective(String name) {
        return directiveMap != null && directiveMap.containsKey(name);
    }

    @Override
    public Collection<Directive> getDirectives() {
        return Optional.ofNullable(directiveMap).map(stringDirectiveMap -> stringDirectiveMap.values()).orElse(null);
    }

    public <T extends AbstractDefinition> T setDirectives(Collection<Directive> directives) {
        if (directives != null && directives.size() > 0) {
            this.directiveMap = directives.stream()
                    .collect(
                            Collectors.toMap(
                                    Directive::getName,
                                    directive -> directive,
                                    (x, y) -> y,
                                    LinkedHashMap::new
                            )
                    );
        }
        return (T) this;
    }

    public <T extends AbstractDefinition> T addDirective(Directive directive) {
        if (this.directiveMap == null) {
            this.directiveMap = new LinkedHashMap<>();
        }
        this.directiveMap.put(directive.getName(), directive);
        return (T) this;
    }

    public <T extends AbstractDefinition> T addDirectives(Collection<Directive> directives) {
        if (this.directiveMap == null) {
            this.directiveMap = new LinkedHashMap<>();
        }
        this.directiveMap.putAll(
                (Map<? extends String, ? extends Directive>) directives.stream()
                        .collect(
                                Collectors.toMap(
                                        Directive::getName,
                                        directive -> directive,
                                        (x, y) -> y,
                                        LinkedHashMap::new
                                )
                        )
        );
        return (T) this;
    }

    @Override
    public boolean isExtension() {
        return extension;
    }

    public void setExtension(boolean extension) {
        this.extension = extension;
    }

    @Override
    public Optional<String> getPackageName() {
        return Optional.ofNullable(directiveMap)
                .map(map -> map.get(DIRECTIVE_PACKAGE_NAME))
                .flatMap(directive -> Optional.ofNullable(directive.getArgument(DIRECTIVE_PACKAGE_ARGUMENT_NAME_NAME)))
                .filter(ValueWithVariable::isString)
                .map(valueWithVariable -> (StringValue) valueWithVariable)
                .map(StringValue::getString);
    }

    @Override
    public Optional<String> getClassName() {
        return Optional.ofNullable(directiveMap)
                .map(map -> map.get(DIRECTIVE_CLASS_NAME))
                .flatMap(directive -> Optional.ofNullable(directive.getArgument(DIRECTIVE_CLASS_ARGUMENT_NAME_NAME)))
                .filter(ValueWithVariable::isString)
                .map(valueWithVariable -> (StringValue) valueWithVariable)
                .map(StringValue::getString);
    }

    @Override
    public boolean classExists() {
        return Optional.ofNullable(directiveMap)
                .map(map -> map.get(DIRECTIVE_CLASS_NAME))
                .flatMap(directive -> Optional.ofNullable(directive.getArgument(DIRECTIVE_CLASS_ARGUMENT_EXISTS_NAME)))
                .filter(ValueWithVariable::isBoolean)
                .map(valueWithVariable -> (BooleanValue) valueWithVariable)
                .map(BooleanValue::getValue)
                .orElse(false);
    }

    @Override
    public Optional<String> getAnnotationName() {
        return Optional.ofNullable(directiveMap)
                .map(map -> map.get(DIRECTIVE_ANNOTATION_NAME))
                .flatMap(directive -> Optional.ofNullable(directive.getArgument(DIRECTIVE_ANNOTATION_ARGUMENT_NAME_NAME)))
                .filter(ValueWithVariable::isString)
                .map(valueWithVariable -> (StringValue) valueWithVariable)
                .map(StringValue::getString);
    }

    @Override
    public Optional<String> getGrpcName() {
        return Optional.ofNullable(directiveMap)
                .map(map -> map.get(DIRECTIVE_GRPC_NAME))
                .flatMap(directive -> Optional.ofNullable(directive.getArgument(DIRECTIVE_GRPC_ARGUMENT_NAME_NAME)))
                .filter(ValueWithVariable::isString)
                .map(valueWithVariable -> (StringValue) valueWithVariable)
                .map(StringValue::getString);
    }

    @Override
    public String getPackageNameOrError() {
        return getPackageName().orElseThrow(() -> new GraphQLErrors(PACKAGE_NAME_ARGUMENT_NOT_EXIST));
    }

    @Override
    public String getClassNameOrError() {
        return getClassName().orElseThrow(() -> new GraphQLErrors(CLASS_NAME_DIRECTIVE_NOT_EXIST));
    }

    @Override
    public String getAnnotationNameOrError() {
        return getAnnotationName().orElseThrow(() -> new GraphQLErrors(ANNOTATION_NAME_ARGUMENT_NOT_EXIST));
    }

    @Override
    public String getGrpcNameOrError() {
        return getGrpcName().orElseThrow(() -> new GraphQLErrors(GRPC_CLASS_NAME_ARGUMENT_NOT_EXIST));
    }

    public boolean isContainer() {
        return hasDirective(DIRECTIVE_CONTAINER_NAME);
    }
}
