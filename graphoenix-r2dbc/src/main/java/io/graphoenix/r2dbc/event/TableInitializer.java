package io.graphoenix.r2dbc.event;

import io.graphoenix.r2dbc.config.R2DBCConfig;
import io.graphoenix.r2dbc.executor.TableManager;
import io.graphoenix.sql.translator.TypeTranslator;
import jakarta.annotation.Priority;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.context.Initialized;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Mono;

import java.util.stream.Collectors;

import static io.graphoenix.core.event.DocumentInitializer.DOCUMENT_INITIALIZED_SCOPE_EVENT_PRIORITY;

@ApplicationScoped
public class TableInitializer {

    private static final Logger logger = LoggerFactory.getLogger(TableInitializer.class.getName());

    public static final int TABLE_INITIALIZED_SCOPE_EVENT_PRIORITY = DOCUMENT_INITIALIZED_SCOPE_EVENT_PRIORITY + 100;

    private final R2DBCConfig r2DBCConfig;
    private final TypeTranslator typeTranslator;
    private final TableManager tableManager;

    @Inject
    public TableInitializer(R2DBCConfig r2DBCConfig, TypeTranslator typeTranslator, TableManager tableManager) {
        this.r2DBCConfig = r2DBCConfig;
        this.typeTranslator = typeTranslator;
        this.tableManager = tableManager;
    }

    public Mono<Void> initializeTable(@Observes @Initialized(ApplicationScoped.class) @Priority(TableInitializer.TABLE_INITIALIZED_SCOPE_EVENT_PRIORITY) Object event) {
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
