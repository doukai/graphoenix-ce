package io.graphoenix.protobuf.handler;

import com.google.common.collect.Streams;
import graphql.parser.antlr.GraphqlParser;
import io.graphoenix.core.config.PackageConfig;
import io.graphoenix.core.handler.DocumentManager;
import io.graphoenix.core.handler.PackageManager;
import io.graphoenix.protobuf.v3.*;
import io.graphoenix.protobuf.v3.Enum;
import io.graphoenix.spi.graphql.Definition;
import io.graphoenix.spi.graphql.type.EnumType;
import io.graphoenix.spi.graphql.type.FieldDefinition;
import io.graphoenix.spi.graphql.type.InputObjectType;
import io.graphoenix.spi.graphql.type.Type;
import io.graphoenix.spi.utils.StreamUtil;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static io.graphoenix.spi.constant.Hammurabi.*;
import static io.graphoenix.spi.error.GraphQLErrorType.UNSUPPORTED_FIELD_TYPE;
import static io.graphoenix.spi.utils.NameUtil.getGrpcName;

@ApplicationScoped
public class ProtobufFileBuilder {

    private final DocumentManager documentManager;
    private final PackageManager packageManager;
    private final PackageConfig packageConfig;

    @Inject
    public ProtobufFileBuilder(DocumentManager documentManager, PackageManager packageManager, PackageConfig packageConfig) {
        this.documentManager = documentManager;
        this.packageManager = packageManager;
        this.packageConfig = packageConfig;
    }

    public Map<String, String> buildProto3() {
        Map<String, String> protoFileMap = new HashMap<>();
        protoFileMap.put("objects", new ProtoFile()
                .setImports(
                        new Import().setName(getPath("enums.proto"))
                )
                .addImports(
                        documentManager.getDocument().getObjectTypes()
                                .filter(objectType -> !documentManager.isOperationType(objectType))
                                .filter(packageManager::isOwnPackage)
                                .flatMap(objectType -> getImports(objectType.getFields()))
                                .filter(StreamUtil.distinctByKey(Import::getName))
                                .collect(Collectors.toList())
                )
                .addImports(
                        documentManager.getDocument().getObjectTypes()
                                .filter(objectType -> !documentManager.isOperationType(objectType))
                                .filter(packageManager::isOwnPackage)
                                .flatMap(objectType -> getScalarImports(objectType.getFields()))
                                .filter(StreamUtil.distinctByKey(Import::getName))
                                .collect(Collectors.toList())
                )
                .setOptions(
                        Arrays.asList(
                                new Option().setName("java_multiple_files").setValue(true),
                                new Option().setName("java_package").setValue(packageConfig.getGrpcObjectTypePackageName())
                        )
                )
                .setPkg(packageConfig.getGrpcPackageName())
                .setTopLevelDefs(documentManager.getObjects().filter(documentManager::isNotOperationType).filter(packageManager::isOwnPackage).map(this::buildMessage).map(Message::toString).collect(Collectors.toList()))
                .toString()
        );
        protoFileMap.put("interfaces", new ProtoFile()
                .setImports(
                        new Import().setName(getPath("enums.proto"))
                )
                .addImports(
                        documentManager.getDocument().getInterfaceTypes()
                                .filter(packageManager::isOwnPackage)
                                .flatMap(interfaceType -> getImports(interfaceType.getFields()))
                                .filter(StreamUtil.distinctByKey(Import::getName))
                                .collect(Collectors.toList())
                )
                .addImports(
                        documentManager.getDocument().getInterfaceTypes()
                                .filter(packageManager::isOwnPackage)
                                .flatMap(interfaceType -> getScalarImports(interfaceType.getFields()))
                                .filter(StreamUtil.distinctByKey(Import::getName))
                                .collect(Collectors.toList())
                )
                .setOptions(
                        Arrays.asList(
                                new Option().setName("java_multiple_files").setValue(true),
                                new Option().setName("java_package").setValue(packageConfig.getGrpcObjectTypePackageName())
                        )
                )
                .setPkg(packageConfig.getGrpcPackageName())
                .setTopLevelDefs(documentManager.getInterfaces().filter(packageManager::isOwnPackage).map(this::buildMessage).map(Message::toString).collect(Collectors.toList()))
                .toString()
        );
        protoFileMap.put("input_objects", new ProtoFile()
                .setImports(
                        new Import().setName(getPath("enums.proto"))
                )
                .addImports(
                        documentManager.getDocument().getInputObjectTypes()
                                .filter(packageManager::isOwnPackage)
                                .flatMap(this::getImports)
                                .filter(StreamUtil.distinctByKey(Import::getName))
                                .collect(Collectors.toList())
                )
                .addImports(
                        documentManager.getDocument().getInputObjectTypes()
                                .filter(packageManager::isOwnPackage)
                                .flatMap(this::getScalarImports)
                                .filter(StreamUtil.distinctByKey(Import::getName))
                                .collect(Collectors.toList())
                )
                .setOptions(
                        Arrays.asList(
                                new Option().setName("java_multiple_files").setValue(true),
                                new Option().setName("java_package").setValue(packageConfig.getGrpcInputObjectTypePackageName())
                        )
                )
                .setPkg(packageConfig.getGrpcPackageName())
                .setTopLevelDefs(documentManager.getInputObjects().filter(packageManager::isOwnPackage).map(this::buildMessage).map(Message::toString).collect(Collectors.toList()))
                .toString()
        );
        protoFileMap.put("enums", new ProtoFile()
                .setOptions(
                        new Option().setName("java_multiple_files").setValue(true),
                        new Option().setName("java_package").setValue(packageConfig.getGrpcEnumTypePackageName())
                )
                .setPkg(packageConfig.getGrpcPackageName())
                .setTopLevelDefs(documentManager.getEnums().filter(packageManager::isOwnPackage).map(this::buildEnum).map(Enum::toString).collect(Collectors.toList()))
                .toString()
        );
        protoFileMap.put("query_requests", new ProtoFile()
                .setImports(
                        new Import().setName(getPath("enums.proto")),
                        new Import().setName(getPath("objects.proto")),
                        new Import().setName(getPath("interfaces.proto")),
                        new Import().setName(getPath("input_objects.proto"))
                )
                .addImports(
                        io.vavr.collection.List
                                .ofAll(
                                        documentManager.getQueryOperationTypeName()
                                                .flatMap(documentManager::getObject).stream()
                                                .flatMap(objectTypeDefinitionContext -> getImportPath(objectTypeDefinitionContext.fieldsDefinition()))
                                )
                                .distinctBy(Import::getName)
                                .toJavaList()
                )
                .addImports(
                        io.vavr.collection.List
                                .ofAll(
                                        documentManager.getQueryOperationTypeName()
                                                .flatMap(documentManager::getObject).stream()
                                                .flatMap(objectTypeDefinitionContext ->
                                                        objectTypeDefinitionContext.fieldsDefinition().fieldDefinition().stream()
                                                                .flatMap(fieldDefinitionContext ->
                                                                        Stream.ofNullable(fieldDefinitionContext.argumentsDefinition())
                                                                )
                                                )
                                                .flatMap(this::getImportPath)
                                )
                                .distinctBy(Import::getName)
                                .toJavaList()
                )
                .addImports(
                        io.vavr.collection.List
                                .ofAll(
                                        Stream.concat(
                                                documentManager.getQueryOperationTypeName()
                                                        .flatMap(documentManager::getObject).stream()
                                                        .flatMap(objectTypeDefinitionContext -> getImportScalarTypePath(objectTypeDefinitionContext.fieldsDefinition())),
                                                documentManager.getQueryOperationTypeName()
                                                        .flatMap(documentManager::getObject).stream()
                                                        .flatMap(objectTypeDefinitionContext ->
                                                                objectTypeDefinitionContext.fieldsDefinition().fieldDefinition().stream()
                                                                        .flatMap(fieldDefinitionContext ->
                                                                                Stream.ofNullable(fieldDefinitionContext.argumentsDefinition())
                                                                        )
                                                        )
                                                        .flatMap(this::getImportScalarTypePath)
                                        )
                                )
                                .distinctBy(Import::getName)
                                .toJavaList()
                )
                .setOptions(
                        new Option().setName("java_multiple_files").setValue(true),
                        new Option().setName("java_package").setValue(packageConfig.getGrpcPackageName())
                )
                .setPkg(packageConfig.getGrpcPackageName())
                .setTopLevelDefs(buildQueryRpcRequest().map(Message::toString).collect(Collectors.toList()))
                .toString()
        );
        protoFileMap.put("query_responses", new ProtoFile()
                .setImports(
                        new Import().setName(getPath("enums.proto")),
                        new Import().setName(getPath("objects.proto")),
                        new Import().setName(getPath("interfaces.proto")),
                        new Import().setName(getPath("input_objects.proto"))
                )
                .addImports(
                        io.vavr.collection.List
                                .ofAll(
                                        documentManager.getQueryOperationTypeName()
                                                .flatMap(documentManager::getObject).stream()
                                                .flatMap(objectTypeDefinitionContext -> getImportPath(objectTypeDefinitionContext.fieldsDefinition()))
                                )
                                .distinctBy(Import::getName)
                                .toJavaList()
                )
                .addImports(
                        io.vavr.collection.List
                                .ofAll(
                                        documentManager.getQueryOperationTypeName()
                                                .flatMap(documentManager::getObject).stream()
                                                .flatMap(objectTypeDefinitionContext ->
                                                        objectTypeDefinitionContext.fieldsDefinition().fieldDefinition().stream()
                                                                .flatMap(fieldDefinitionContext ->
                                                                        Stream.ofNullable(fieldDefinitionContext.argumentsDefinition())
                                                                )
                                                )
                                                .flatMap(this::getImportPath)
                                )
                                .distinctBy(Import::getName)
                                .toJavaList()
                )
                .addImports(
                        io.vavr.collection.List
                                .ofAll(
                                        Stream.concat(
                                                documentManager.getQueryOperationTypeName()
                                                        .flatMap(documentManager::getObject).stream()
                                                        .flatMap(objectTypeDefinitionContext -> getImportScalarTypePath(objectTypeDefinitionContext.fieldsDefinition())),
                                                documentManager.getQueryOperationTypeName()
                                                        .flatMap(documentManager::getObject).stream()
                                                        .flatMap(objectTypeDefinitionContext ->
                                                                objectTypeDefinitionContext.fieldsDefinition().fieldDefinition().stream()
                                                                        .flatMap(fieldDefinitionContext ->
                                                                                Stream.ofNullable(fieldDefinitionContext.argumentsDefinition())
                                                                        )
                                                        )
                                                        .flatMap(this::getImportScalarTypePath)
                                        )
                                )
                                .distinctBy(Import::getName)
                                .toJavaList()
                )
                .setOptions(
                        new Option().setName("java_multiple_files").setValue(true),
                        new Option().setName("java_package").setValue(packageConfig.getGrpcPackageName())
                )
                .setPkg(packageConfig.getGrpcPackageName())
                .setTopLevelDefs(buildQueryRpcResponse().map(Message::toString).collect(Collectors.toList()))
                .toString()
        );
        protoFileMap.put("mutation_requests", new ProtoFile()
                .setImports(
                        new Import().setName(getPath("enums.proto")),
                        new Import().setName(getPath("objects.proto")),
                        new Import().setName(getPath("interfaces.proto")),
                        new Import().setName(getPath("input_objects.proto"))
                )
                .addImports(
                        io.vavr.collection.List
                                .ofAll(
                                        documentManager.getMutationOperationTypeName()
                                                .flatMap(documentManager::getObject).stream()
                                                .flatMap(objectTypeDefinitionContext -> getImportPath(objectTypeDefinitionContext.fieldsDefinition()))
                                )
                                .distinctBy(Import::getName)
                                .toJavaList()
                )
                .addImports(
                        io.vavr.collection.List
                                .ofAll(
                                        documentManager.getMutationOperationTypeName()
                                                .flatMap(documentManager::getObject).stream()
                                                .flatMap(objectTypeDefinitionContext ->
                                                        objectTypeDefinitionContext.fieldsDefinition().fieldDefinition().stream()
                                                                .flatMap(fieldDefinitionContext ->
                                                                        Stream.ofNullable(fieldDefinitionContext.argumentsDefinition())
                                                                )
                                                )
                                                .flatMap(this::getImportPath)
                                )
                                .distinctBy(Import::getName)
                                .toJavaList()
                )
                .addImports(
                        io.vavr.collection.List
                                .ofAll(
                                        Stream.concat(
                                                documentManager.getMutationOperationTypeName()
                                                        .flatMap(documentManager::getObject).stream()
                                                        .flatMap(objectTypeDefinitionContext -> getImportScalarTypePath(objectTypeDefinitionContext.fieldsDefinition())),
                                                documentManager.getMutationOperationTypeName()
                                                        .flatMap(documentManager::getObject).stream()
                                                        .flatMap(objectTypeDefinitionContext ->
                                                                objectTypeDefinitionContext.fieldsDefinition().fieldDefinition().stream()
                                                                        .flatMap(fieldDefinitionContext ->
                                                                                Stream.ofNullable(fieldDefinitionContext.argumentsDefinition())
                                                                        )
                                                        )
                                                        .flatMap(this::getImportScalarTypePath)
                                        )
                                )
                                .distinctBy(Import::getName)
                                .toJavaList()
                )
                .setOptions(
                        new Option().setName("java_multiple_files").setValue(true),
                        new Option().setName("java_package").setValue(packageConfig.getGrpcPackageName())
                )
                .setPkg(packageConfig.getGrpcPackageName())
                .setTopLevelDefs(buildMutationRpcRequest().map(Message::toString).collect(Collectors.toList()))
                .toString()
        );
        protoFileMap.put("mutation_responses", new ProtoFile()
                .setImports(
                        new Import().setName(getPath("enums.proto")),
                        new Import().setName(getPath("objects.proto")),
                        new Import().setName(getPath("interfaces.proto")),
                        new Import().setName(getPath("input_objects.proto"))
                )
                .addImports(
                        io.vavr.collection.List
                                .ofAll(
                                        documentManager.getMutationOperationTypeName()
                                                .flatMap(documentManager::getObject).stream()
                                                .flatMap(objectTypeDefinitionContext -> getImportPath(objectTypeDefinitionContext.fieldsDefinition()))
                                )
                                .distinctBy(Import::getName)
                                .toJavaList()
                )
                .addImports(
                        io.vavr.collection.List
                                .ofAll(
                                        documentManager.getMutationOperationTypeName()
                                                .flatMap(documentManager::getObject).stream()
                                                .flatMap(objectTypeDefinitionContext ->
                                                        objectTypeDefinitionContext.fieldsDefinition().fieldDefinition().stream()
                                                                .flatMap(fieldDefinitionContext ->
                                                                        Stream.ofNullable(fieldDefinitionContext.argumentsDefinition())
                                                                )
                                                )
                                                .flatMap(this::getImportPath)
                                )
                                .distinctBy(Import::getName)
                                .toJavaList()
                )
                .addImports(
                        io.vavr.collection.List
                                .ofAll(
                                        Stream.concat(
                                                documentManager.getMutationOperationTypeName()
                                                        .flatMap(documentManager::getObject).stream()
                                                        .flatMap(objectTypeDefinitionContext -> getImportScalarTypePath(objectTypeDefinitionContext.fieldsDefinition())),
                                                documentManager.getMutationOperationTypeName()
                                                        .flatMap(documentManager::getObject).stream()
                                                        .flatMap(objectTypeDefinitionContext ->
                                                                objectTypeDefinitionContext.fieldsDefinition().fieldDefinition().stream()
                                                                        .flatMap(fieldDefinitionContext ->
                                                                                Stream.ofNullable(fieldDefinitionContext.argumentsDefinition())
                                                                        )
                                                        )
                                                        .flatMap(this::getImportScalarTypePath)
                                        )
                                )
                                .distinctBy(Import::getName)
                                .toJavaList()
                )
                .setOptions(
                        new Option().setName("java_multiple_files").setValue(true),
                        new Option().setName("java_package").setValue(packageConfig.getGrpcPackageName())
                )
                .setPkg(packageConfig.getGrpcPackageName())
                .setTopLevelDefs(buildMutationRpcResponse().map(Message::toString).collect(Collectors.toList()))
                .toString()
        );
        protoFileMap.put("query", new ProtoFile()
                .setImports(
                        new Import().setName(getPath("enums.proto")),
                        new Import().setName(getPath("objects.proto")),
                        new Import().setName(getPath("interfaces.proto")),
                        new Import().setName(getPath("input_objects.proto")),
                        new Import().setName(getPath("query_requests.proto")),
                        new Import().setName(getPath("query_responses.proto"))
                )
                .setOptions(
                        new Option().setName("java_multiple_files").setValue(true),
                        new Option().setName("java_package").setValue(packageConfig.getGrpcPackageName())
                )
                .setPkg(packageConfig.getGrpcPackageName())
                .setTopLevelDefs(buildQueryService().stream().map(Service::toString).collect(Collectors.toList()))
                .toString()
        );
        protoFileMap.put("mutation", new ProtoFile()
                .setImports(
                        new Import().setName(getPath("enums.proto")),
                        new Import().setName(getPath("objects.proto")),
                        new Import().setName(getPath("interfaces.proto")),
                        new Import().setName(getPath("input_objects.proto")),
                        new Import().setName(getPath("mutation_requests.proto")),
                        new Import().setName(getPath("mutation_responses.proto"))
                )
                .setOptions(
                        new Option().setName("java_multiple_files").setValue(true),
                        new Option().setName("java_package").setValue(packageConfig.getGrpcPackageName())
                )
                .setPkg(packageConfig.getGrpcPackageName())
                .setTopLevelDefs(buildMutationService().stream().map(Service::toString).collect(Collectors.toList()))
                .toString()
        );
        protoFileMap.put("graphql", new ProtoFile()
                .setOptions(
                        new Option().setName("java_multiple_files").setValue(true),
                        new Option().setName("java_package").setValue(packageConfig.getGrpcPackageName())
                )
                .setPkg(packageConfig.getGrpcPackageName())
                .addTopLevelDef(buildGraphQLService().toString())
                .addTopLevelDef(buildGraphQLRpcRequest().toString())
                .addTopLevelDef(buildGraphQLRpcResponse().toString())
                .toString()
        );
        return protoFileMap;
    }

    public Optional<Service> buildQueryService() {
        return documentManager.getQueryOperationTypeName()
                .flatMap(documentManager::getObject)
                .map(objectTypeDefinitionContext ->
                        new Service().setName(objectTypeDefinitionContext.name().getText() + "Service")
                                .setRpcs(
                                        objectTypeDefinitionContext.fieldsDefinition().fieldDefinition().stream()
                                                .filter(packageManager::isOwnPackage)
                                                .map(fieldDefinitionContext ->
                                                        new Rpc()
                                                                .setName(getServiceRpcName(fieldDefinitionContext.name().getText()))
                                                                .setMessageType("Query" + getServiceRpcName(fieldDefinitionContext.name().getText()) + "Request")
                                                                .setReturnType("Query" + getServiceRpcName(fieldDefinitionContext.name().getText()) + "Response")
                                                                .setDescription(Optional.ofNullable(fieldDefinitionContext.description()).map(descriptionContext -> DOCUMENT_UTIL.getStringValue(descriptionContext.StringValue())).orElse(null))
                                                )
                                                .collect(Collectors.toList())
                                )
                                .setDescription(Optional.ofNullable(objectTypeDefinitionContext.description()).map(descriptionContext -> DOCUMENT_UTIL.getStringValue(descriptionContext.StringValue())).orElse(null))
                );
    }

    public Optional<Service> buildMutationService() {
        return documentManager.getMutationOperationTypeName()
                .flatMap(documentManager::getObject)
                .map(objectTypeDefinitionContext ->
                        new Service().setName(objectTypeDefinitionContext.name().getText() + "Service")
                                .setRpcs(
                                        objectTypeDefinitionContext.fieldsDefinition().fieldDefinition().stream()
                                                .filter(packageManager::isOwnPackage)
                                                .map(fieldDefinitionContext ->
                                                        new Rpc()
                                                                .setName(getServiceRpcName(fieldDefinitionContext.name().getText()))
                                                                .setMessageType("Mutation" + getServiceRpcName(fieldDefinitionContext.name().getText()) + "Request")
                                                                .setReturnType("Mutation" + getServiceRpcName(fieldDefinitionContext.name().getText()) + "Response")
                                                                .setDescription(Optional.ofNullable(fieldDefinitionContext.description()).map(descriptionContext -> DOCUMENT_UTIL.getStringValue(descriptionContext.StringValue())).orElse(null))
                                                )
                                                .collect(Collectors.toList())
                                )
                                .setDescription(Optional.ofNullable(objectTypeDefinitionContext.description()).map(descriptionContext -> DOCUMENT_UTIL.getStringValue(descriptionContext.StringValue())).orElse(null))
                );
    }

    public Service buildGraphQLService() {
        return new Service().setName("GraphQLService")
                .addRpc(
                        new Rpc()
                                .setName("Request")
                                .setMessageType("GraphQLRequest")
                                .setReturnType("GraphQLResponse")
                );
    }

    public Stream<Message> buildQueryRpcRequest() {
        return documentManager.getQueryOperationTypeName()
                .flatMap(documentManager::getObject).stream()
                .flatMap(objectTypeDefinitionContext -> objectTypeDefinitionContext.fieldsDefinition().fieldDefinition().stream())
                .filter(packageManager::isOwnPackage)
                .map(fieldDefinitionContext ->
                        new Message()
                                .setName("Query" + getServiceRpcName(fieldDefinitionContext.name().getText()) + "Request")
                                .setFields(
                                        Stream.concat(
                                                Stream.of(new Field().setName("selection_set").setOptional(true).setType("string").setNumber(1), new Field().setName("arguments").setOptional(true).setType("string").setNumber(2)),
                                                Stream.ofNullable(fieldDefinitionContext.argumentsDefinition())
                                                        .flatMap(argumentsDefinitionContext ->
                                                                IntStream.range(0, argumentsDefinitionContext.inputValueDefinition().size())
                                                                        .mapToObj(index ->
                                                                                new Field()
                                                                                        .setName(getMessageFiledName(argumentsDefinitionContext.inputValueDefinition().get(index).name().getText()))
                                                                                        .setType(buildType(argumentsDefinitionContext.inputValueDefinition().get(index).type()))
                                                                                        .setOptional(argumentsDefinitionContext.inputValueDefinition().get(index).type().nonNullType() == null)
                                                                                        .setRepeated(documentManager.fieldTypeIsList(argumentsDefinitionContext.inputValueDefinition().get(index).type()))
                                                                                        .setNumber(index + 3)
                                                                                        .setDescription(Optional.ofNullable(argumentsDefinitionContext.inputValueDefinition().get(index).description()).map(descriptionContext -> DOCUMENT_UTIL.getStringValue(descriptionContext.StringValue())).orElse(null))
                                                                        )
                                                        )
                                        ).collect(Collectors.toList())
                                )
                );
    }

    public Stream<Message> buildQueryRpcResponse() {
        return documentManager.getQueryOperationTypeName()
                .flatMap(documentManager::getObject).stream()
                .flatMap(objectTypeDefinitionContext -> objectTypeDefinitionContext.fieldsDefinition().fieldDefinition().stream())
                .filter(packageManager::isOwnPackage)
                .map(fieldDefinitionContext ->
                        new Message()
                                .setName("Query" + getServiceRpcName(fieldDefinitionContext.name().getText()) + "Response")
                                .addField(
                                        new Field()
                                                .setName(getMessageFiledName(fieldDefinitionContext.name().getText()))
                                                .setType(buildType(fieldDefinitionContext.type()))
                                                .setOptional(fieldDefinitionContext.type().nonNullType() == null)
                                                .setRepeated(documentManager.fieldTypeIsList(fieldDefinitionContext.type()))
                                                .setNumber(1)

                                )
                );
    }

    public Stream<Message> buildMutationRpcRequest() {
        return documentManager.getMutationOperationTypeName()
                .flatMap(documentManager::getObject).stream()
                .flatMap(objectTypeDefinitionContext -> objectTypeDefinitionContext.fieldsDefinition().fieldDefinition().stream())
                .filter(packageManager::isOwnPackage)
                .map(fieldDefinitionContext ->
                        new Message()
                                .setName("Mutation" + getServiceRpcName(fieldDefinitionContext.name().getText()) + "Request")
                                .setFields(
                                        Stream.concat(
                                                Stream.of(
                                                        new Field().setName("selection_set").setOptional(true).setType("string").setNumber(1), new Field().setName("arguments").setOptional(true).setType("string").setNumber(2)
                                                ),
                                                Stream.ofNullable(fieldDefinitionContext.argumentsDefinition())
                                                        .flatMap(argumentsDefinitionContext ->
                                                                IntStream.range(0, argumentsDefinitionContext.inputValueDefinition().size())
                                                                        .mapToObj(index ->
                                                                                new Field()
                                                                                        .setName(getMessageFiledName(argumentsDefinitionContext.inputValueDefinition().get(index).name().getText()))
                                                                                        .setType(buildType(argumentsDefinitionContext.inputValueDefinition().get(index).type()))
                                                                                        .setOptional(argumentsDefinitionContext.inputValueDefinition().get(index).type().nonNullType() == null)
                                                                                        .setRepeated(documentManager.fieldTypeIsList(argumentsDefinitionContext.inputValueDefinition().get(index).type()))
                                                                                        .setNumber(index + 3)
                                                                                        .setDescription(Optional.ofNullable(argumentsDefinitionContext.inputValueDefinition().get(index).description()).map(descriptionContext -> DOCUMENT_UTIL.getStringValue(descriptionContext.StringValue())).orElse(null))
                                                                        )
                                                        )
                                        ).collect(Collectors.toList())
                                )
                );
    }

    public Stream<Message> buildMutationRpcResponse() {
        return documentManager.getMutationOperationTypeName()
                .flatMap(documentManager::getObject).stream()
                .flatMap(objectTypeDefinitionContext -> objectTypeDefinitionContext.fieldsDefinition().fieldDefinition().stream())
                .filter(packageManager::isOwnPackage)
                .map(fieldDefinitionContext ->
                        new Message()
                                .setName("Mutation" + getServiceRpcName(fieldDefinitionContext.name().getText()) + "Response")
                                .addField(
                                        new Field()
                                                .setName(getMessageFiledName(fieldDefinitionContext.name().getText()))
                                                .setType(buildType(fieldDefinitionContext.type()))
                                                .setOptional(fieldDefinitionContext.type().nonNullType() == null)
                                                .setRepeated(documentManager.fieldTypeIsList(fieldDefinitionContext.type()))
                                                .setNumber(1)
                                )
                );
    }

    public Message buildGraphQLRpcRequest() {
        return new Message()
                .setName("GraphQLRequest")
                .addField(new Field().setName("request").setType("string").setNumber(1))
                .addField(new Field().setName("transaction_id").setType("string").setOptional(true).setNumber(2));
    }

    public Message buildGraphQLRpcResponse() {
        return new Message()
                .setName("GraphQLResponse")
                .addField(new Field().setName("response").setType("string").setNumber(1));
    }

    public Enum buildEnum(EnumType enumType) {
        return new Enum()
                .setName(getGrpcName(enumType.getName()))
                .setFields(
                        IntStream.range(0, enumType.getEnumValues().size())
                                .mapToObj(index ->
                                        new EnumField()
                                                .setName(
                                                        enumType.getEnumValue(index) + "_" + getEnumFieldName(enumTypeDefinitionContext.name().getText())
                                                )
                                                .setDescription(Optional.ofNullable(enumTypeDefinitionContext.enumValueDefinitions().enumValueDefinition().get(index).description()).map(descriptionContext -> DOCUMENT_UTIL.getStringValue(descriptionContext.StringValue())).orElse(null))
                                                .setNumber(index)
                                )
                                .collect(Collectors.toList())
                )
                .setDescription(Optional.ofNullable(enumTypeDefinitionContext.description()).map(descriptionContext -> DOCUMENT_UTIL.getStringValue(descriptionContext.StringValue())).orElse(null));
    }

    public Message buildMessage(GraphqlParser.ObjectTypeDefinitionContext objectTypeDefinitionContext) {
        return new Message()
                .setName(getName(objectTypeDefinitionContext.name().getText()))
                .setFields(
                        IntStream.range(0, objectTypeDefinitionContext.fieldsDefinition().fieldDefinition().size())
                                .mapToObj(index ->
                                        new Field()
                                                .setName(getMessageFiledName(objectTypeDefinitionContext.fieldsDefinition().fieldDefinition().get(index).name().getText()))
                                                .setType(buildType(objectTypeDefinitionContext.fieldsDefinition().fieldDefinition().get(index).type()))
                                                .setOptional(objectTypeDefinitionContext.fieldsDefinition().fieldDefinition().get(index).type().nonNullType() == null)
                                                .setRepeated(documentManager.fieldTypeIsList(objectTypeDefinitionContext.fieldsDefinition().fieldDefinition().get(index).type()))
                                                .setDescription(Optional.ofNullable(objectTypeDefinitionContext.fieldsDefinition().fieldDefinition().get(index).description()).map(descriptionContext -> DOCUMENT_UTIL.getStringValue(descriptionContext.StringValue())).orElse(null))
                                                .setNumber(index + 1)
                                )
                                .collect(Collectors.toList())
                )
                .setDescription(Optional.ofNullable(objectTypeDefinitionContext.description()).map(descriptionContext -> DOCUMENT_UTIL.getStringValue(descriptionContext.StringValue())).orElse(null));
    }

    public Message buildMessage(GraphqlParser.InterfaceTypeDefinitionContext interfaceTypeDefinitionContext) {
        return new Message()
                .setName(getName(interfaceTypeDefinitionContext.name().getText()))
                .setFields(
                        IntStream.range(0, interfaceTypeDefinitionContext.fieldsDefinition().fieldDefinition().size())
                                .mapToObj(index ->
                                        new Field()
                                                .setName(getMessageFiledName(interfaceTypeDefinitionContext.fieldsDefinition().fieldDefinition().get(index).name().getText()))
                                                .setType(buildType(interfaceTypeDefinitionContext.fieldsDefinition().fieldDefinition().get(index).type()))
                                                .setOptional(interfaceTypeDefinitionContext.fieldsDefinition().fieldDefinition().get(index).type().nonNullType() == null)
                                                .setRepeated(documentManager.fieldTypeIsList(interfaceTypeDefinitionContext.fieldsDefinition().fieldDefinition().get(index).type()))
                                                .setDescription(Optional.ofNullable(interfaceTypeDefinitionContext.fieldsDefinition().fieldDefinition().get(index).description()).map(descriptionContext -> DOCUMENT_UTIL.getStringValue(descriptionContext.StringValue())).orElse(null))
                                                .setNumber(index + 1)
                                )
                                .collect(Collectors.toList())
                )
                .setDescription(Optional.ofNullable(interfaceTypeDefinitionContext.description()).map(descriptionContext -> DOCUMENT_UTIL.getStringValue(descriptionContext.StringValue())).orElse(null));
    }

    public Message buildMessage(GraphqlParser.InputObjectTypeDefinitionContext inputObjectTypeDefinitionContext) {
        return new Message()
                .setName(getName(inputObjectTypeDefinitionContext.name().getText()))
                .setFields(
                        IntStream.range(0, inputObjectTypeDefinitionContext.inputObjectValueDefinitions().inputValueDefinition().size())
                                .mapToObj(index ->
                                        new Field()
                                                .setName(getMessageFiledName(inputObjectTypeDefinitionContext.inputObjectValueDefinitions().inputValueDefinition().get(index).name().getText()))
                                                .setType(buildType(inputObjectTypeDefinitionContext.inputObjectValueDefinitions().inputValueDefinition().get(index).type()))
                                                .setOptional(inputObjectTypeDefinitionContext.inputObjectValueDefinitions().inputValueDefinition().get(index).type().nonNullType() == null)
                                                .setRepeated(documentManager.fieldTypeIsList(inputObjectTypeDefinitionContext.inputObjectValueDefinitions().inputValueDefinition().get(index).type()))
                                                .setDescription(Optional.ofNullable(inputObjectTypeDefinitionContext.inputObjectValueDefinitions().inputValueDefinition().get(index).description()).map(descriptionContext -> DOCUMENT_UTIL.getStringValue(descriptionContext.StringValue())).orElse(null))
                                                .setNumber(index + 1)
                                )
                                .collect(Collectors.toList())
                )
                .setDescription(Optional.ofNullable(inputObjectTypeDefinitionContext.description()).map(descriptionContext -> DOCUMENT_UTIL.getStringValue(descriptionContext.StringValue())).orElse(null));
    }

    public String buildType(Type type) {
        Definition definition = documentManager.getDocument().getDefinition(type.getTypeName().getName());
        if (definition.isScalar()) {
            switch (definition.getName()) {
                case SCALA_ID_NAME:
                case SCALA_STRING_NAME:
                    return "string";
                case SCALA_DATE_NAME:
                    return "google.type.Date";
                case SCALA_TIME_NAME:
                    return "google.type.TimeOfDay";
                case SCALA_DATE_TIME_NAME:
                    return "google.type.DateTime";
                case SCALA_TIMESTAMP_NAME:
                    return "google.protobuf.Timestamp";
                case SCALA_BOOLEAN_NAME:
                    return "bool";
                case SCALA_INT_NAME:
                    return "int32";
                case SCALA_FLOAT_NAME:
                    return "float";
                case SCALA_BIG_INTEGER_NAME:
                case SCALA_BIG_DECIMAL_NAME:
                    return "google.type.Decimal";
                default:
                    throw new GraphQLErrors(UNSUPPORTED_FIELD_TYPE.bind(fieldTypeName));
            }
        } else if (definition.isEnum()) {
            return documentManager.getEnum(fieldTypeName)
                    .filter(packageManager::isNotOwnPackage)
                    .flatMap(enumTypeDefinitionContext -> documentManager.getGrpcPackageName(enumTypeDefinitionContext).map(packageName -> packageName + "." + getName(fieldTypeName)))
                    .orElseGet(() -> getName(fieldTypeName));
        } else if (definition.isObject()) {
            return documentManager.getObject(fieldTypeName)
                    .filter(packageManager::isNotOwnPackage)
                    .flatMap(objectTypeDefinitionContext -> documentManager.getGrpcPackageName(objectTypeDefinitionContext).map(packageName -> packageName + "." + getName(fieldTypeName)))
                    .orElseGet(() -> getName(fieldTypeName));
        } else if (definition.isInterface()) {
            return documentManager.getInterface(fieldTypeName)
                    .filter(packageManager::isNotOwnPackage)
                    .flatMap(interfaceTypeDefinitionContext -> documentManager.getGrpcPackageName(interfaceTypeDefinitionContext).map(packageName -> packageName + "." + getName(fieldTypeName)))
                    .orElseGet(() -> getName(fieldTypeName));
        } else if (definition.isInputObject()) {
            return documentManager.getInputObject(fieldTypeName)
                    .filter(packageManager::isNotOwnPackage)
                    .flatMap(inputObjectTypeDefinitionContext -> documentManager.getGrpcPackageName(inputObjectTypeDefinitionContext).map(packageName -> packageName + "." + getName(fieldTypeName)))
                    .orElseGet(() -> getName(fieldTypeName));
        } else {
            throw new GraphQLErrors(UNSUPPORTED_FIELD_TYPE.bind(fieldTypeName));
        }
    }

    public String getPath(String protoName) {
        return packageConfig.getPackageName().replaceAll("\\.", "/") + "/" + protoName;
    }

    public String getPath(String packageName, String protoName) {
        return packageName.replaceAll("\\.", "/") + "/" + protoName;
    }

    private Stream<Import> getImports(Collection<FieldDefinition> fieldDefinitions) {
        return Streams
                .concat(
                        fieldDefinitions.stream()
                                .filter(packageManager::isOwnPackage)
                                .map(documentManager::getFieldTypeDefinition)
                                .filter(packageManager::isNotOwnPackage)
                                .filter(Definition::isEnum)
                                .map(Definition::getPackageName)
                                .flatMap(Optional::stream)
                                .map(packageName -> getPath(packageName, "enums.proto"))
                                .map(path -> new Import().setName(path)),
                        fieldDefinitions.stream()
                                .filter(packageManager::isOwnPackage)
                                .map(documentManager::getFieldTypeDefinition)
                                .filter(packageManager::isNotOwnPackage)
                                .filter(Definition::isObject)
                                .map(Definition::getPackageName)
                                .flatMap(Optional::stream)
                                .map(packageName -> getPath(packageName, "objects.proto"))
                                .map(path -> new Import().setName(path)),
                        fieldDefinitions.stream()
                                .filter(packageManager::isOwnPackage)
                                .map(documentManager::getFieldTypeDefinition)
                                .filter(packageManager::isNotOwnPackage)
                                .filter(Definition::isInterface)
                                .map(Definition::getPackageName)
                                .flatMap(Optional::stream)
                                .map(packageName -> getPath(packageName, "interfaces.proto"))
                                .map(path -> new Import().setName(path))
                );
    }

    private Stream<Import> getImports(InputObjectType inputObjectType) {
        return Streams
                .concat(
                        inputObjectType.getInputValues().stream()
                                .filter(packageManager::isOwnPackage)
                                .map(documentManager::getInputValueTypeDefinition)
                                .filter(packageManager::isNotOwnPackage)
                                .filter(Definition::isEnum)
                                .map(Definition::getPackageName)
                                .flatMap(Optional::stream)
                                .map(packageName -> getPath(packageName, "enums.proto"))
                                .map(path -> new Import().setName(path)),
                        inputObjectType.getInputValues().stream()
                                .filter(packageManager::isOwnPackage)
                                .map(documentManager::getInputValueTypeDefinition)
                                .filter(packageManager::isNotOwnPackage)
                                .filter(Definition::isInputObject)
                                .map(Definition::getPackageName)
                                .flatMap(Optional::stream)
                                .map(packageName -> getPath(packageName, "input_objects.proto"))
                                .map(path -> new Import().setName(path))
                );
    }

    private Stream<Import> getScalarImports(Collection<FieldDefinition> fieldDefinitions) {
        return Streams
                .concat(
                        fieldDefinitions.stream()
                                .filter(packageManager::isOwnPackage)
                                .map(documentManager::getFieldTypeDefinition)
                                .filter(Definition::isScalar)
                                .filter(definition -> definition.getName().equals(SCALA_DATE_NAME))
                                .map(path -> new Import().setName("google/type/date.proto")),
                        fieldDefinitions.stream()
                                .filter(packageManager::isOwnPackage)
                                .map(documentManager::getFieldTypeDefinition)
                                .filter(Definition::isScalar)
                                .filter(definition -> definition.getName().equals(SCALA_TIME_NAME))
                                .map(path -> new Import().setName("google/type/timeofday.proto")),
                        fieldDefinitions.stream()
                                .filter(packageManager::isOwnPackage)
                                .map(documentManager::getFieldTypeDefinition)
                                .filter(Definition::isScalar)
                                .filter(definition -> definition.getName().equals(SCALA_DATE_TIME_NAME))
                                .map(path -> new Import().setName("google/type/datetime.proto")),
                        fieldDefinitions.stream()
                                .filter(packageManager::isOwnPackage)
                                .map(documentManager::getFieldTypeDefinition)
                                .filter(Definition::isScalar)
                                .filter(definition -> definition.getName().equals(SCALA_TIMESTAMP_NAME))
                                .map(path -> new Import().setName("google/protobuf/timestamp.proto")),
                        fieldDefinitions.stream()
                                .filter(packageManager::isOwnPackage)
                                .map(documentManager::getFieldTypeDefinition)
                                .filter(Definition::isScalar)
                                .filter(definition -> definition.getName().equals(SCALA_BIG_INTEGER_NAME) || definition.getName().equals(SCALA_BIG_DECIMAL_NAME))
                                .map(path -> new Import().setName("google/type/decimal.proto"))
                );
    }

    private Stream<Import> getScalarImports(InputObjectType inputObjectType) {
        return Streams
                .concat(
                        inputObjectType.getInputValues().stream()
                                .map(documentManager::getInputValueTypeDefinition)
                                .filter(Definition::isScalar)
                                .filter(definition -> definition.getName().equals(SCALA_DATE_NAME))
                                .map(path -> new Import().setName("google/type/date.proto")),
                        inputObjectType.getInputValues().stream()
                                .map(documentManager::getInputValueTypeDefinition)
                                .filter(Definition::isScalar)
                                .filter(definition -> definition.getName().equals(SCALA_TIME_NAME))
                                .map(path -> new Import().setName("google/type/timeofday.proto")),
                        inputObjectType.getInputValues().stream()
                                .map(documentManager::getInputValueTypeDefinition)
                                .filter(Definition::isScalar)
                                .filter(definition -> definition.getName().equals(SCALA_DATE_TIME_NAME))
                                .map(path -> new Import().setName("google/type/datetime.proto")),
                        inputObjectType.getInputValues().stream()
                                .map(documentManager::getInputValueTypeDefinition)
                                .filter(Definition::isScalar)
                                .filter(definition -> definition.getName().equals(SCALA_TIMESTAMP_NAME))
                                .map(path -> new Import().setName("google/protobuf/timestamp.proto")),
                        inputObjectType.getInputValues().stream()
                                .map(documentManager::getInputValueTypeDefinition)
                                .filter(Definition::isScalar)
                                .filter(definition -> definition.getName().equals(SCALA_BIG_INTEGER_NAME) || definition.getName().equals(SCALA_BIG_DECIMAL_NAME))
                                .map(path -> new Import().setName("google/type/decimal.proto"))
                );
    }
}
