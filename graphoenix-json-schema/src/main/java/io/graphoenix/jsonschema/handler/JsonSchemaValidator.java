package io.graphoenix.jsonschema.handler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import jakarta.inject.Inject;
import jakarta.json.JsonValue;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

import static io.graphoenix.spi.constant.Hammurabi.*;
import static io.graphoenix.spi.error.GraphQLErrorType.UNSUPPORTED_OPERATION_TYPE;

@ApplicationScoped
@Priority(110)
public class JsonSchemaValidator implements OperationBeforeHandler {

    private final ObjectMapper mapper = new ObjectMapper();
    private final DocumentManager documentManager;
    private final JsonSchemaManager jsonSchemaManager;
    private final JsonSchemaFactory factory;

    @Inject
    public JsonSchemaValidator(JsonSchemaManager jsonSchemaManager, DocumentManager documentManager, JsonSchemaResourceURNFactory jsonSchemaResourceURNFactory) {
        this.documentManager = documentManager;
        this.jsonSchemaManager = jsonSchemaManager;
        JsonMetaSchema jsonMetaSchema = JsonMetaSchema.getV201909();
        this.factory = new JsonSchemaFactory.Builder().defaultMetaSchemaURI(jsonMetaSchema.getUri()).addMetaSchema(jsonMetaSchema).addUrnFactory(jsonSchemaResourceURNFactory).build();
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
                                .map(validationMessage -> new GraphQLError(validationMessage.getMessage()).setSchemaPath(validationMessage.getSchemaPath()))
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
}
