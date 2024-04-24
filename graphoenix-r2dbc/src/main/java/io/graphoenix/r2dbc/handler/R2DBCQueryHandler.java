package io.graphoenix.r2dbc.handler;

import io.graphoenix.r2dbc.executor.QueryExecutor;
import io.graphoenix.spi.graphql.operation.Operation;
import io.graphoenix.spi.handler.QueryHandler;
import io.graphoenix.sql.translator.QueryTranslator;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.json.JsonValue;
import jakarta.json.spi.JsonProvider;
import reactor.core.publisher.Mono;

import java.io.StringReader;

@ApplicationScoped
@Named("r2dbc")
public class R2DBCQueryHandler implements QueryHandler {

    private final QueryTranslator queryTranslator;

    private final QueryExecutor queryExecutor;

    private final JsonProvider jsonProvider;

    @Inject
    public R2DBCQueryHandler(QueryTranslator queryTranslator, QueryExecutor queryExecutor, JsonProvider jsonProvider) {
        this.queryTranslator = queryTranslator;
        this.queryExecutor = queryExecutor;
        this.jsonProvider = jsonProvider;
    }

    @Override
    public Mono<JsonValue> query(Operation operation) {
        return Mono.justOrEmpty(queryTranslator.operationToSelectSQL(operation))
                .flatMap(queryExecutor::executeQuery)
                .map(json -> jsonProvider.createReader(new StringReader(json)).readValue())
                .defaultIfEmpty(JsonValue.EMPTY_JSON_OBJECT);
    }
}
