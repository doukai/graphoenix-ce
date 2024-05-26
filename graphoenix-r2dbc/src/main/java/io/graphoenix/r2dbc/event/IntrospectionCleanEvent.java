package io.graphoenix.r2dbc.event;

import io.graphoenix.core.config.GraphQLConfig;
import io.graphoenix.r2dbc.executor.TableCreator;
import io.graphoenix.sql.translator.TypeTranslator;
import io.nozdormu.spi.event.ScopeEvent;
import jakarta.annotation.Priority;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.context.Initialized;
import jakarta.inject.Inject;
import org.tinylog.Logger;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.stream.Collectors;

import static io.graphoenix.core.event.DocumentInitializedEvent.DOCUMENT_INITIALIZED_SCOPE_EVENT_PRIORITY;
import static io.graphoenix.spi.constant.Hammurabi.PREFIX_INTROSPECTION;

@ApplicationScoped
@Initialized(ApplicationScoped.class)
@Priority(IntrospectionCleanEvent.INTROSPECTION_CLEAN_SCOPE_EVENT_PRIORITY)
public class IntrospectionCleanEvent implements ScopeEvent {

    public static final int INTROSPECTION_CLEAN_SCOPE_EVENT_PRIORITY = DOCUMENT_INITIALIZED_SCOPE_EVENT_PRIORITY + 199;

    private final GraphQLConfig graphQLConfig;
    private final TypeTranslator typeTranslator;
    private final TableCreator tableCreator;

    @Inject
    public IntrospectionCleanEvent(GraphQLConfig graphQLConfig, TypeTranslator typeTranslator, TableCreator tableCreator) {
        this.graphQLConfig = graphQLConfig;
        this.typeTranslator = typeTranslator;
        this.tableCreator = tableCreator;
    }

    @Override
    public Mono<Void> fireAsync(Map<String, Object> context) {
        if (graphQLConfig.getBuildIntrospection()) {
            Logger.info("introspection clean started");
            return tableCreator.selectTables(typeTranslator.selectTablesSQL())
                    .flatMap(existsTableNameList ->
                            tableCreator.mergeTable(
                                    existsTableNameList.stream()
                                            .filter(tableName -> tableName.startsWith(PREFIX_INTROSPECTION))
                                            .map(typeTranslator::truncateTableSQL)
                                            .collect(Collectors.joining(";"))
                            )
                    )
                    .doOnSuccess((jsonValue) -> Logger.info("introspection clean success"))
                    .then();
        }
        return Mono.empty();
    }
}
