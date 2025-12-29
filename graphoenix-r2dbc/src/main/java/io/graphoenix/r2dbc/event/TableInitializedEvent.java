package io.graphoenix.r2dbc.event;

import io.graphoenix.r2dbc.config.R2DBCConfig;
import io.graphoenix.r2dbc.executor.TableManager;
import io.graphoenix.sql.translator.TypeTranslator;
import io.nozdormu.spi.event.ScopeEvent;
import jakarta.annotation.Priority;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.context.Initialized;
import jakarta.inject.Inject;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import static io.graphoenix.core.event.DocumentInitializedEvent.DOCUMENT_INITIALIZED_SCOPE_EVENT_PRIORITY;

@ApplicationScoped
@Initialized(ApplicationScoped.class)
@Priority(TableInitializedEvent.TABLE_INITIALIZED_SCOPE_EVENT_PRIORITY)
public class TableInitializedEvent implements ScopeEvent {

    private static final Logger logger = Logger.getLogger(TableInitializedEvent.class.getName());

    public static final int TABLE_INITIALIZED_SCOPE_EVENT_PRIORITY = DOCUMENT_INITIALIZED_SCOPE_EVENT_PRIORITY + 100;

    private final R2DBCConfig r2DBCConfig;
    private final TypeTranslator typeTranslator;
    private final TableManager tableManager;

    @Inject
    public TableInitializedEvent(R2DBCConfig r2DBCConfig, TypeTranslator typeTranslator, TableManager tableManager) {
        this.r2DBCConfig = r2DBCConfig;
        this.typeTranslator = typeTranslator;
        this.tableManager = tableManager;
    }

    @Override
    public Mono<Void> fireAsync(Map<String, Object> context) {
        if (r2DBCConfig.getCreateTables()) {
            logger.info("table initialized starting");
            return tableManager.selectColumns(typeTranslator.selectColumnsSQL())
                    .flatMap(existsColumnNameList ->
                            tableManager.mergeTable(
                                    typeTranslator.mergeTablesSQL(existsColumnNameList)
                                            .collect(Collectors.joining(";"))
                            )
                    )
                    .doOnSuccess((v) -> logger.info("table initialized success"))
                    .then(
                            tableManager.selectIndexes(typeTranslator.selectIndexesSQL())
                                    .flatMap(existsIndexNameList ->
                                            tableManager.mergeTable(
                                                    typeTranslator.createIndexesSQL(existsIndexNameList)
                                                            .collect(Collectors.joining(";"))
                                            )
                                    )
                                    .doOnSuccess((v) -> logger.info("index initialized success"))
                    );
        }
        return Mono.empty();
    }
}
