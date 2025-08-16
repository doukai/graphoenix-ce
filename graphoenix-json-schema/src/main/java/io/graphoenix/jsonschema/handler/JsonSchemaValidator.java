package io.graphoenix.jsonschema.handler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.networknt.schema.Format;
import com.networknt.schema.JsonMetaSchema;
import com.networknt.schema.JsonSchemaFactory;
import com.networknt.schema.ValidationMessage;
import io.graphoenix.core.handler.DocumentManager;
import io.graphoenix.spi.error.GraphQLError;
import io.graphoenix.spi.error.GraphQLErrors;
import io.graphoenix.spi.graphql.operation.Field;
import io.graphoenix.spi.graphql.operation.Operation;
import io.graphoenix.spi.handler.OperationBeforeHandler;
import jakarta.annotation.Priority;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Instance;
import jakarta.inject.Inject;
import jakarta.json.JsonValue;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static io.graphoenix.core.handler.before.FragmentHandler.FRAGMENT_HANDLER_PRIORITY;
import static io.graphoenix.spi.constant.Hammurabi.*;
import static io.graphoenix.spi.error.GraphQLErrorType.UNSUPPORTED_OPERATION_TYPE;

@ApplicationScoped
@Priority(JsonSchemaValidator.JSON_SCHEMA_VALIDATOR_PRIORITY)
public class JsonSchemaValidator implements OperationBeforeHandler {

    public static final int JSON_SCHEMA_VALIDATOR_PRIORITY = FRAGMENT_HANDLER_PRIORITY + 50;

    private final ObjectMapper mapper = new ObjectMapper();
    private final DocumentManager documentManager;
    private final JsonSchemaManager jsonSchemaManager;
    private final JsonSchemaFactory factory;

    @Inject
    public JsonSchemaValidator(JsonSchemaManager jsonSchemaManager, DocumentManager documentManager, JsonSchemaResourceURNFactory jsonSchemaResourceURNFactory, Instance<Format> formatInstance) {
        this.documentManager = documentManager;
        this.jsonSchemaManager = jsonSchemaManager;
        JsonMetaSchema v201909 = JsonMetaSchema.getV201909();
        JsonMetaSchema.Builder builder = JsonMetaSchema.builder(v201909.getUri(), v201909);
        if (formatInstance != null) {
            formatInstance.forEach(builder::addFormat);
        }
        JsonMetaSchema jsonMetaSchema = builder.build();
        this.factory = new JsonSchemaFactory.Builder()
                .defaultMetaSchemaURI(jsonMetaSchema.getUri())
                .addMetaSchema(jsonMetaSchema)
                .addUrnFactory(jsonSchemaResourceURNFactory)
                .build();
    }

    public Set<ValidationMessage> validate(String jsonSchemaName, String json) throws JsonProcessingException {
        return factory.getSchema(jsonSchemaManager.getJsonSchema(jsonSchemaName)).validate(mapper.readTree(json));
    }

    @Override
    public Mono<Operation> handle(Operation operation, Map<String, JsonValue> variables) {
        return Flux
                .fromStream(
                        operation.getFields().stream()
                                .filter(field -> field.getArguments() != null)
                                .flatMap(field -> validateSelection(operation, field))
                                .map(validationMessage -> new GraphQLError(validationMessage.getMessage()).setPath(validationMessageToGraphQLPath(validationMessage)))
                )
                .collectList()
                .flatMap(graphQLErrors -> {
                            if (!graphQLErrors.isEmpty()) {
                                return Mono.error(new GraphQLErrors().addAll(graphQLErrors));
                            }
                            return Mono.just(operation);
                        }
                );
    }

    protected Stream<ValidationMessage> validateSelection(Operation operation, Field field) {
        try {
            String operationTypeName;
            if (operation.getOperationType() == null || operation.getOperationType().equals(OPERATION_QUERY_NAME)) {
                operationTypeName = documentManager.getDocument().getQueryOperationTypeOrError().getName();
            } else if (operation.getOperationType().equals(OPERATION_SUBSCRIPTION_NAME)) {
                operationTypeName = documentManager.getDocument().getSubscriptionOperationTypeOrError().getName();
            } else if (operation.getOperationType().equals(OPERATION_MUTATION_NAME)) {
                operationTypeName = documentManager.getDocument().getMutationOperationTypeOrError().getName();
            } else {
                throw new GraphQLErrors(UNSUPPORTED_OPERATION_TYPE.bind(operation.getOperationType()));
            }
            return validate(operationTypeName + "_" + field.getName() + "_" + SUFFIX_ARGUMENTS, field.getArguments().toJson()).stream();
        } catch (JsonProcessingException e) {
            throw new GraphQLErrors(e);
        }
    }

    private String validationMessageToGraphQLPath(ValidationMessage validationMessage) {
        Stream<String> pathStream = Stream.of(validationMessage.getPath().split("\\."))
                .flatMap(item -> {
                            if (item.contains("[")) {
                                return Stream
                                        .of(
                                                item.substring(0, item.lastIndexOf("[")),
                                                item.substring(item.lastIndexOf("[") + 1, item.length() - 1)
                                        );
                            } else {
                                return Stream.of(item);
                            }
                        }
                )
                .filter(item -> !item.equals("$"));

        if (validationMessage.getType().equals("required")) {
            return Stream
                    .concat(
                            pathStream,
                            Stream.of(validationMessage.getArguments()[0])
                    )
                    .collect(Collectors.joining("/"));

        } else {
            return pathStream
                    .collect(Collectors.joining("/"));
        }
    }
}
