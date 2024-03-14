package io.graphoenix.sql.implementer;

import com.github.vertical_blank.sqlformatter.SqlFormatter;
import com.github.vertical_blank.sqlformatter.languages.Dialect;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.stream.Collectors;
import java.util.stream.Stream;

@ApplicationScoped
public class SQLFormatHandler {

    private final SqlFormatter.Formatter formatter = SqlFormatter.of(Dialect.MariaDb).extend(cfg -> cfg.plusNamedPlaceholderTypes(":"));

    public String query(String sql) {
        return formatter.format(sql);
    }

    public String mutation(Stream<String> sqlStream) {
        return formatter.format(sqlStream.collect(Collectors.joining(";")));
    }
}
