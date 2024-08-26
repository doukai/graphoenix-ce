package io.graphoenix.protobuf.handler;

import com.google.common.collect.Streams;
import io.graphoenix.core.config.PackageConfig;
import io.graphoenix.core.handler.DocumentManager;
import io.graphoenix.core.handler.PackageManager;
import io.graphoenix.protobuf.v3.Enum;
import io.graphoenix.protobuf.v3.*;
import io.graphoenix.spi.error.GraphQLErrors;
import io.graphoenix.spi.graphql.Definition;
import io.graphoenix.spi.graphql.type.*;
import io.graphoenix.spi.utils.StreamUtil;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static io.graphoenix.spi.constant.Hammurabi.*;
import static io.graphoenix.spi.error.GraphQLErrorType.UNSUPPORTED_FIELD_TYPE;
import static io.graphoenix.spi.utils.NameUtil.*;

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
                                .flatMap(objectType -> getFieldDefinitionsImports(objectType.getFields()))
                                .filter(StreamUtil.distinctByKey(Import::getName))
                                .collect(Collectors.toList())
                )
                .addImports(
                        documentManager.getDocument().getObjectTypes()
                                .filter(objectType -> !documentManager.isOperationType(objectType))
                                .filter(packageManager::isOwnPackage)
                                .flatMap(objectType -> getFieldDefinitionsScalarImports(objectType.getFields()))
                                .filter(StreamUtil.distinctByKey(Import::getName))
                                .collect(Collectors.toList())
                )
                .setOptions(
                        Arrays.asList(
                                new Option().setName("java_multiple_files").setValue(true),
                                new Option().setName("java_package").setValue(packageConfig.getGrpcObjectTypePackageName())
                        )
                )
                .setPkg(packageConfig.getPackageName())
                .setTopLevelDefs(
                        documentManager.getDocument().getObjectTypes()
                                .filter(objectType -> !documentManager.isOperationType(objectType))
                                .filter(packageManager::isOwnPackage)
                                .map(this::buildMessage)
                                .map(Message::toString)
                                .collect(Collectors.toList())
                )
                .toString()
        );
        protoFileMap.put("interfaces", new ProtoFile()
                .setImports(
                        new Import().setName(getPath("enums.proto"))
                )
                .addImports(
                        documentManager.getDocument().getInterfaceTypes()
                                .filter(packageManager::isOwnPackage)
                                .flatMap(interfaceType -> getFieldDefinitionsImports(interfaceType.getFields()))
                                .filter(StreamUtil.distinctByKey(Import::getName))
                                .collect(Collectors.toList())
                )
                .addImports(
                        documentManager.getDocument().getInterfaceTypes()
                                .filter(packageManager::isOwnPackage)
                                .flatMap(interfaceType -> getFieldDefinitionsScalarImports(interfaceType.getFields()))
                                .filter(StreamUtil.distinctByKey(Import::getName))
                                .collect(Collectors.toList())
                )
                .setOptions(
                        Arrays.asList(
                                new Option().setName("java_multiple_files").setValue(true),
                                new Option().setName("java_package").setValue(packageConfig.getGrpcObjectTypePackageName())
                        )
                )
                .setPkg(packageConfig.getPackageName())
                .setTopLevelDefs(
                        documentManager.getDocument().getInterfaceTypes()
                                .filter(packageManager::isOwnPackage)
                                .map(this::buildMessage)
                                .map(Message::toString)
                                .collect(Collectors.toList())
                )
                .toString()
        );
        protoFileMap.put("input_objects", new ProtoFile()
                .setImports(
                        new Import().setName(getPath("enums.proto"))
                )
                .addImports(
                        documentManager.getDocument().getInputObjectTypes()
                                .filter(packageManager::isOwnPackage)
                                .flatMap(inputObjectType -> getInputValuesImports(inputObjectType.getInputValues()))
                                .filter(StreamUtil.distinctByKey(Import::getName))
                                .collect(Collectors.toList())
                )
                .addImports(
                        documentManager.getDocument().getInputObjectTypes()
                                .filter(packageManager::isOwnPackage)
                                .flatMap(inputObjectType -> getInputValuesScalarImports(inputObjectType.getInputValues()))
                                .filter(StreamUtil.distinctByKey(Import::getName))
                                .collect(Collectors.toList())
                )
                .setOptions(
                        Arrays.asList(
                                new Option().setName("java_multiple_files").setValue(true),
                                new Option().setName("java_package").setValue(packageConfig.getGrpcInputObjectTypePackageName())
                        )
                )
                .setPkg(packageConfig.getPackageName())
                .setTopLevelDefs(
                        documentManager.getDocument().getInputObjectTypes()
                                .filter(packageManager::isOwnPackage)
                                .map(this::buildMessage)
                                .map(Message::toString)
                                .collect(Collectors.toList())
                )
                .toString()
        );
        protoFileMap.put("enums", new ProtoFile()
                .setOptions(
                        new Option().setName("java_multiple_files").setValue(true),
                        new Option().setName("java_package").setValue(packageConfig.getGrpcEnumTypePackageName())
                )
                .setPkg(packageConfig.getPackageName())
                .setTopLevelDefs(
                        documentManager.getDocument().getEnums()
                                .filter(packageManager::isOwnPackage)
                                .map(this::buildEnum)
                                .map(Enum::toString)
                                .collect(Collectors.toList())
                )
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
                        documentManager.getDocument().getQueryOperationType().stream()
                                .flatMap(objectType -> getFieldDefinitionsImports(objectType.getFields()))
                                .filter(StreamUtil.distinctByKey(Import::getName))
                                .collect(Collectors.toList())
                )
                .addImports(
                        documentManager.getDocument().getQueryOperationType().stream()
                                .flatMap(objectType ->
                                        objectType.getFields().stream()
                                                .flatMap(fieldDefinition ->
                                                        Stream.ofNullable(fieldDefinition.getArguments())
                                                )
                                )
                                .flatMap(this::getInputValuesImports)
                                .filter(StreamUtil.distinctByKey(Import::getName))
                                .collect(Collectors.toList())
                )
                .addImports(
                        Stream
                                .concat(
                                        documentManager.getDocument().getQueryOperationType().stream()
                                                .flatMap(objectType -> getFieldDefinitionsScalarImports(objectType.getFields())),
                                        documentManager.getDocument().getQueryOperationType().stream()
                                                .flatMap(objectType ->
                                                        objectType.getFields().stream()
                                                                .flatMap(fieldDefinition ->
                                                                        Stream.ofNullable(fieldDefinition.getArguments())
                                                                )
                                                )
                                                .flatMap(this::getInputValuesScalarImports)
                                )
                                .filter(StreamUtil.distinctByKey(Import::getName))
                                .collect(Collectors.toList())
                )
                .setOptions(
                        new Option().setName("java_multiple_files").setValue(true),
                        new Option().setName("java_package").setValue(packageConfig.getGrpcPackageName())
                )
                .setPkg(packageConfig.getPackageName())
                .setTopLevelDefs(
                        buildQueryRpcRequest()
                                .map(Message::toString)
                                .collect(Collectors.toList())
                )
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
                        documentManager.getDocument().getQueryOperationType().stream()
                                .flatMap(objectType -> getFieldDefinitionsImports(objectType.getFields()))
                                .filter(StreamUtil.distinctByKey(Import::getName))
                                .collect(Collectors.toList())
                )
                .addImports(
                        documentManager.getDocument().getQueryOperationType().stream()
                                .flatMap(objectType ->
                                        objectType.getFields().stream()
                                                .flatMap(fieldDefinition ->
                                                        Stream.ofNullable(fieldDefinition.getArguments())
                                                )
                                )
                                .flatMap(this::getInputValuesImports)
                                .filter(StreamUtil.distinctByKey(Import::getName))
                                .collect(Collectors.toList())
                )
                .addImports(
                        Stream
                                .concat(
                                        documentManager.getDocument().getQueryOperationType().stream()
                                                .flatMap(objectType -> getFieldDefinitionsScalarImports(objectType.getFields())),
                                        documentManager.getDocument().getQueryOperationType().stream()
                                                .flatMap(objectType ->
                                                        objectType.getFields().stream()
                                                                .flatMap(fieldDefinition ->
                                                                        Stream.ofNullable(fieldDefinition.getArguments())
                                                                )
                                                )
                                                .flatMap(this::getInputValuesScalarImports)
                                )
                                .filter(StreamUtil.distinctByKey(Import::getName))
                                .collect(Collectors.toList())
                )
                .setOptions(
                        new Option().setName("java_multiple_files").setValue(true),
                        new Option().setName("java_package").setValue(packageConfig.getGrpcPackageName())
                )
                .setPkg(packageConfig.getPackageName())
                .setTopLevelDefs(
                        buildQueryRpcResponse()
                                .map(Message::toString)
                                .collect(Collectors.toList())
                )
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
                        documentManager.getDocument().getMutationOperationType().stream()
                                .flatMap(objectType -> getFieldDefinitionsImports(objectType.getFields()))
                                .filter(StreamUtil.distinctByKey(Import::getName))
                                .collect(Collectors.toList())
                )
                .addImports(
                        documentManager.getDocument().getMutationOperationType().stream()
                                .flatMap(objectType ->
                                        objectType.getFields().stream()
                                                .flatMap(fieldDefinition ->
                                                        Stream.ofNullable(fieldDefinition.getArguments())
                                                )
                                )
                                .flatMap(this::getInputValuesImports)
                                .filter(StreamUtil.distinctByKey(Import::getName))
                                .collect(Collectors.toList())
                )
                .addImports(
                        Stream
                                .concat(
                                        documentManager.getDocument().getMutationOperationType().stream()
                                                .flatMap(objectType -> getFieldDefinitionsScalarImports(objectType.getFields())),
                                        documentManager.getDocument().getMutationOperationType().stream()
                                                .flatMap(objectType ->
                                                        objectType.getFields().stream()
                                                                .flatMap(fieldDefinition ->
                                                                        Stream.ofNullable(fieldDefinition.getArguments())
                                                                )
                                                )
                                                .flatMap(this::getInputValuesScalarImports)
                                )
                                .filter(StreamUtil.distinctByKey(Import::getName))
                                .collect(Collectors.toList())
                )
                .setOptions(
                        new Option().setName("java_multiple_files").setValue(true),
                        new Option().setName("java_package").setValue(packageConfig.getGrpcPackageName())
                )
                .setPkg(packageConfig.getPackageName())
                .setTopLevelDefs(
                        buildMutationRpcRequest()
                                .map(Message::toString)
                                .collect(Collectors.toList())
                )
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
                        documentManager.getDocument().getMutationOperationType().stream()
                                .flatMap(objectType -> getFieldDefinitionsImports(objectType.getFields()))
                                .filter(StreamUtil.distinctByKey(Import::getName))
                                .collect(Collectors.toList())
                )
                .addImports(
                        documentManager.getDocument().getMutationOperationType().stream()
                                .flatMap(objectType ->
                                        objectType.getFields().stream()
                                                .flatMap(fieldDefinition ->
                                                        Stream.ofNullable(fieldDefinition.getArguments())
                                                )
                                )
                                .flatMap(this::getInputValuesImports)
                                .filter(StreamUtil.distinctByKey(Import::getName))
                                .collect(Collectors.toList())
                )
                .addImports(
                        Stream
                                .concat(
                                        documentManager.getDocument().getMutationOperationType().stream()
                                                .flatMap(objectType -> getFieldDefinitionsScalarImports(objectType.getFields())),
                                        documentManager.getDocument().getMutationOperationType().stream()
                                                .flatMap(objectType ->
                                                        objectType.getFields().stream()
                                                                .flatMap(fieldDefinition ->
                                                                        Stream.ofNullable(fieldDefinition.getArguments())
                                                                )
                                                )
                                                .flatMap(this::getInputValuesScalarImports)
                                )
                                .filter(StreamUtil.distinctByKey(Import::getName))
                                .collect(Collectors.toList())
                )
                .setOptions(
                        new Option().setName("java_multiple_files").setValue(true),
                        new Option().setName("java_package").setValue(packageConfig.getGrpcPackageName())
                )
                .setPkg(packageConfig.getPackageName())
                .setTopLevelDefs(
                        buildMutationRpcResponse()
                                .map(Message::toString)
                                .collect(Collectors.toList())
                )
                .toString()
        );
        protoFileMap.put("query",
                new ProtoFile()
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
                        .setPkg(packageConfig.getPackageName())
                        .setTopLevelDefs(buildQueryService().stream().map(Service::toString).collect(Collectors.toList()))
                        .toString()
        );
        protoFileMap.put("mutation",
                new ProtoFile()
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
                        .setPkg(packageConfig.getPackageName())
                        .setTopLevelDefs(buildMutationService().stream().map(Service::toString).collect(Collectors.toList()))
                        .toString()
        );
        protoFileMap.put("graphql",
                new ProtoFile()
                        .setOptions(
                                new Option().setName("java_multiple_files").setValue(true),
                                new Option().setName("java_package").setValue(packageConfig.getGrpcPackageName())
                        )
                        .setPkg(packageConfig.getPackageName())
                        .addTopLevelDef(buildGraphQLService().toString())
                        .addTopLevelDef(buildGraphQLRpcRequest().toString())
                        .addTopLevelDef(buildGraphQLRpcResponse().toString())
                        .toString()
        );
        return protoFileMap;
    }

    public Optional<Service> buildQueryService() {
        return documentManager.getDocument().getQueryOperationType()
                .map(objectType ->
                        new Service()
                                .setName(objectType.getName() + "Service")
                                .setRpcs(
                                        objectType.getFields().stream()
                                                .filter(packageManager::isOwnPackage)
                                                .map(fieldDefinition ->
                                                        new Rpc()
                                                                .setName(getGrpcServiceRpcName(fieldDefinition.getName()))
                                                                .setMessageType(TYPE_QUERY_NAME + getGrpcServiceRpcName(fieldDefinition.getName()) + "Request")
                                                                .setReturnType(TYPE_QUERY_NAME + getGrpcServiceRpcName(fieldDefinition.getName()) + "Response")
                                                                .setDescription(fieldDefinition.getDescription())
                                                )
                                                .collect(Collectors.toList())
                                )
                                .setDescription(objectType.getDescription())
                );
    }

    public Optional<Service> buildMutationService() {
        return documentManager.getDocument().getMutationOperationType()
                .map(objectType ->
                        new Service()
                                .setName(objectType.getName() + "Service")
                                .setRpcs(
                                        objectType.getFields().stream()
                                                .filter(packageManager::isOwnPackage)
                                                .filter(fieldDefinition -> !fieldDefinition.getName().equals("singleFile"))
                                                .filter(fieldDefinition -> !fieldDefinition.getName().equals("multipleFile"))
                                                .filter(fieldDefinition -> !fieldDefinition.getName().equals("singleUpload"))
                                                .filter(fieldDefinition -> !fieldDefinition.getName().equals("multipleUpload"))
                                                .map(fieldDefinition ->
                                                        new Rpc()
                                                                .setName(getGrpcServiceRpcName(fieldDefinition.getName()))
                                                                .setMessageType(TYPE_MUTATION_NAME + getGrpcServiceRpcName(fieldDefinition.getName()) + "Request")
                                                                .setReturnType(TYPE_MUTATION_NAME + getGrpcServiceRpcName(fieldDefinition.getName()) + "Response")
                                                                .setDescription(fieldDefinition.getDescription())
                                                )
                                                .collect(Collectors.toList())
                                )
                                .setDescription(objectType.getDescription())
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
        return documentManager.getDocument().getQueryOperationType().stream()
                .flatMap(objectType -> objectType.getFields().stream())
                .filter(packageManager::isOwnPackage)
                .map(fieldDefinition ->
                        new Message()
                                .setName(TYPE_QUERY_NAME + getGrpcServiceRpcName(fieldDefinition.getName()) + "Request")
                                .setFields(
                                        Stream
                                                .concat(
                                                        Stream.of(
                                                                new Field()
                                                                        .setName("selection_set")
                                                                        .setOptional(true)
                                                                        .setType("string")
                                                                        .setNumber(1),
                                                                new Field()
                                                                        .setName("arguments")
                                                                        .setOptional(true)
                                                                        .setType("string")
                                                                        .setNumber(2)
                                                        ),
                                                        Stream.ofNullable(fieldDefinition.getArguments())
                                                                .flatMap(inputValues ->
                                                                        IntStream.range(0, inputValues.size())
                                                                                .mapToObj(index ->
                                                                                        new Field()
                                                                                                .setName(getGrpcMessageFiledName(fieldDefinition.getArgument(index).getName()))
                                                                                                .setType(buildType(fieldDefinition.getArgument(index).getType()))
                                                                                                .setOptional(fieldDefinition.getArgument(index).getType().isNonNull())
                                                                                                .setRepeated(fieldDefinition.getArgument(index).getType().hasList())
                                                                                                .setNumber(index + 3)
                                                                                                .setDescription(fieldDefinition.getArgument(index).getDescription())
                                                                                )
                                                                )
                                                )
                                                .collect(Collectors.toList())
                                )
                );
    }

    public Stream<Message> buildQueryRpcResponse() {
        return documentManager.getDocument().getQueryOperationType().stream()
                .flatMap(objectType -> objectType.getFields().stream())
                .filter(packageManager::isOwnPackage)
                .map(fieldDefinition ->
                        new Message()
                                .setName(TYPE_QUERY_NAME + getGrpcServiceRpcName(fieldDefinition.getName()) + "Response")
                                .addField(
                                        new Field()
                                                .setName(getGrpcMessageFiledName(fieldDefinition.getName()))
                                                .setType(buildType(fieldDefinition.getType()))
                                                .setOptional(fieldDefinition.getType().isNonNull())
                                                .setRepeated(fieldDefinition.getType().hasList())
                                                .setNumber(1)
                                )
                );
    }

    public Stream<Message> buildMutationRpcRequest() {
        return documentManager.getDocument().getMutationOperationType().stream()
                .flatMap(objectType -> objectType.getFields().stream())
                .filter(packageManager::isOwnPackage)
                .map(fieldDefinition ->
                        new Message()
                                .setName(TYPE_MUTATION_NAME + getGrpcServiceRpcName(fieldDefinition.getName()) + "Request")
                                .setFields(
                                        Stream
                                                .concat(
                                                        Stream.of(
                                                                new Field()
                                                                        .setName("selection_set")
                                                                        .setOptional(true)
                                                                        .setType("string")
                                                                        .setNumber(1),
                                                                new Field()
                                                                        .setName("arguments")
                                                                        .setOptional(true)
                                                                        .setType("string")
                                                                        .setNumber(2)
                                                        ),
                                                        Stream.ofNullable(fieldDefinition.getArguments())
                                                                .flatMap(inputValues ->
                                                                        IntStream.range(0, inputValues.size())
                                                                                .mapToObj(index ->
                                                                                        new Field()
                                                                                                .setName(getGrpcMessageFiledName(fieldDefinition.getArgument(index).getName()))
                                                                                                .setType(buildType(fieldDefinition.getArgument(index).getType()))
                                                                                                .setOptional(fieldDefinition.getArgument(index).getType().isNonNull())
                                                                                                .setRepeated(fieldDefinition.getArgument(index).getType().hasList())
                                                                                                .setNumber(index + 3)
                                                                                                .setDescription(fieldDefinition.getArgument(index).getDescription())
                                                                                )
                                                                )
                                                )
                                                .collect(Collectors.toList())
                                )
                );
    }

    public Stream<Message> buildMutationRpcResponse() {
        return documentManager.getDocument().getMutationOperationType().stream()
                .flatMap(objectType -> objectType.getFields().stream())
                .filter(packageManager::isOwnPackage)
                .map(fieldDefinition ->
                        new Message()
                                .setName(TYPE_MUTATION_NAME + getGrpcServiceRpcName(fieldDefinition.getName()) + "Response")
                                .addField(
                                        new Field()
                                                .setName(getGrpcMessageFiledName(fieldDefinition.getName()))
                                                .setType(buildType(fieldDefinition.getType()))
                                                .setOptional(fieldDefinition.getType().isNonNull())
                                                .setRepeated(fieldDefinition.getType().hasList())
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
                                                .setName(enumType.getEnumValue(index).getName() + "_" + getGrpcEnumFieldName(enumType.getName()))
                                                .setDescription(enumType.getEnumValue(index).getDescription())
                                                .setNumber(index)
                                )
                                .collect(Collectors.toList())
                )
                .setDescription(enumType.getDescription());
    }

    public Message buildMessage(ObjectType objectType) {
        return new Message()
                .setName(getGrpcName(objectType.getName()))
                .setFields(
                        IntStream.range(0, objectType.getFields().size())
                                .mapToObj(index ->
                                        new Field()
                                                .setName(getGrpcMessageFiledName(objectType.getField(index).getName()))
                                                .setType(buildType(objectType.getField(index).getType()))
                                                .setOptional(objectType.getField(index).getType().isNonNull())
                                                .setRepeated(objectType.getField(index).getType().hasList())
                                                .setDescription(objectType.getField(index).getDescription())
                                                .setNumber(index + 1)
                                )
                                .collect(Collectors.toList())
                )
                .setDescription(objectType.getDescription());
    }

    public Message buildMessage(InterfaceType interfaceType) {
        return new Message()
                .setName(getGrpcName(interfaceType.getName()))
                .setFields(
                        IntStream.range(0, interfaceType.getFields().size())
                                .mapToObj(index ->
                                        new Field()
                                                .setName(getGrpcMessageFiledName(interfaceType.getField(index).getName()))
                                                .setType(buildType(interfaceType.getField(index).getType()))
                                                .setOptional(interfaceType.getField(index).getType().isNonNull())
                                                .setRepeated(interfaceType.getField(index).getType().hasList())
                                                .setDescription(interfaceType.getField(index).getDescription())
                                                .setNumber(index + 1)
                                )
                                .collect(Collectors.toList())
                )
                .setDescription(interfaceType.getDescription());
    }

    public Message buildMessage(InputObjectType inputObjectType) {
        return new Message()
                .setName(getGrpcName(inputObjectType.getName()))
                .setFields(
                        IntStream.range(0, inputObjectType.getInputValues().size())
                                .mapToObj(index ->
                                        new Field()
                                                .setName(getGrpcMessageFiledName(inputObjectType.getInputValue(index).getName()))
                                                .setType(buildType(inputObjectType.getInputValue(index).getType()))
                                                .setOptional(inputObjectType.getInputValue(index).getType().isNonNull())
                                                .setRepeated(inputObjectType.getInputValue(index).getType().hasList())
                                                .setDescription(inputObjectType.getInputValue(index).getDescription())
                                                .setNumber(index + 1)
                                )
                                .collect(Collectors.toList())
                )
                .setDescription(inputObjectType.getDescription());
    }

    public String buildType(Type type) {
        Definition definition = documentManager.getDocument().getDefinition(type.getTypeName().getName());
        if (definition.isScalar()) {
            switch (definition.getName()) {
                case SCALA_ID_NAME:
                case SCALA_STRING_NAME:
                case SCALA_UPLOAD_NAME:
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
                    throw new GraphQLErrors(UNSUPPORTED_FIELD_TYPE.bind(definition.getName()));
            }
        } else {
            if (packageManager.isOwnPackage(definition)) {
                return getGrpcName(definition.getName());
            } else {
                if (definition.isEnum()) {
                    return definition.getPackageNameOrError() + "." + getGrpcName(definition.getName());
                } else if (definition.isObject()) {
                    return definition.getPackageNameOrError() + "." + getGrpcName(definition.getName());
                } else if (definition.isInterface()) {
                    return definition.getPackageNameOrError() + "." + getGrpcName(definition.getName());
                } else if (definition.isInputObject()) {
                    return definition.getPackageNameOrError() + "." + getGrpcName(definition.getName());
                }
            }
        }
        throw new GraphQLErrors(UNSUPPORTED_FIELD_TYPE.bind(definition.getName()));
    }

    public String getPath(String protoName) {
        return packageConfig.getPackageName().replaceAll("\\.", "/") + "/" + protoName;
    }

    public String getPath(String packageName, String protoName) {
        return packageName.replaceAll("\\.", "/") + "/" + protoName;
    }

    private Stream<Import> getFieldDefinitionsImports(Collection<FieldDefinition> fieldDefinitions) {
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

    private Stream<Import> getInputValuesImports(Collection<InputValue> inputValues) {
        return Streams
                .concat(
                        inputValues.stream()
                                .filter(packageManager::isOwnPackage)
                                .map(documentManager::getInputValueTypeDefinition)
                                .filter(packageManager::isNotOwnPackage)
                                .filter(Definition::isEnum)
                                .map(Definition::getPackageName)
                                .flatMap(Optional::stream)
                                .map(packageName -> getPath(packageName, "enums.proto"))
                                .map(path -> new Import().setName(path)),
                        inputValues.stream()
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

    private Stream<Import> getFieldDefinitionsScalarImports(Collection<FieldDefinition> fieldDefinitions) {
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

    private Stream<Import> getInputValuesScalarImports(Collection<InputValue> inputValues) {
        return Streams
                .concat(
                        inputValues.stream()
                                .map(documentManager::getInputValueTypeDefinition)
                                .filter(Definition::isScalar)
                                .filter(definition -> definition.getName().equals(SCALA_DATE_NAME))
                                .map(path -> new Import().setName("google/type/date.proto")),
                        inputValues.stream()
                                .map(documentManager::getInputValueTypeDefinition)
                                .filter(Definition::isScalar)
                                .filter(definition -> definition.getName().equals(SCALA_TIME_NAME))
                                .map(path -> new Import().setName("google/type/timeofday.proto")),
                        inputValues.stream()
                                .map(documentManager::getInputValueTypeDefinition)
                                .filter(Definition::isScalar)
                                .filter(definition -> definition.getName().equals(SCALA_DATE_TIME_NAME))
                                .map(path -> new Import().setName("google/type/datetime.proto")),
                        inputValues.stream()
                                .map(documentManager::getInputValueTypeDefinition)
                                .filter(Definition::isScalar)
                                .filter(definition -> definition.getName().equals(SCALA_TIMESTAMP_NAME))
                                .map(path -> new Import().setName("google/protobuf/timestamp.proto")),
                        inputValues.stream()
                                .map(documentManager::getInputValueTypeDefinition)
                                .filter(Definition::isScalar)
                                .filter(definition -> definition.getName().equals(SCALA_BIG_INTEGER_NAME) || definition.getName().equals(SCALA_BIG_DECIMAL_NAME))
                                .map(path -> new Import().setName("google/type/decimal.proto"))
                );
    }
}
