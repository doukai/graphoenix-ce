package io.graphoenix.r2dbc.event;

import io.graphoenix.r2dbc.config.R2DBCConfig;
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

@ApplicationScoped
@Initialized(ApplicationScoped.class)
@Priority(100)
public class TableInitializedEvent implements ScopeEvent {

    private final R2DBCConfig r2DBCConfig;
    private final TypeTranslator typeTranslator;
    private final TableCreator tableCreator;

    @Inject
    public TableInitializedEvent(R2DBCConfig r2DBCConfig, TypeTranslator typeTranslator, TableCreator tableCreator) {
        this.r2DBCConfig = r2DBCConfig;
        this.typeTranslator = typeTranslator;
        this.tableCreator = tableCreator;
    }

    @Override
    public Mono<Void> fireAsync(Map<String, Object> context) {
        if (r2DBCConfig.getCreateTables()) {
            Logger.info("table initialized starting");
            return tableCreator.selectColumns(typeTranslator.selectColumnsSQL())
                    .flatMap(existsColumnNameList ->
                            tableCreator.mergeTable(
                                    typeTranslator.mergeTablesSQL(existsColumnNameList)
                                            .collect(Collectors.joining(";"))
                            )
                    )
                    .doOnSuccess((v) -> Logger.info("table initialized success"))
                    .doOnError(Logger::error)
                    .then();
        }
        return Mono.empty();
    }
}
