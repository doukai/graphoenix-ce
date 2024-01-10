package io.graphoenix.sql.expression;

import net.sf.jsqlparser.expression.Alias;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.StringValue;
import net.sf.jsqlparser.schema.Table;
import net.sf.jsqlparser.statement.create.table.ColumnDefinition;
import net.sf.jsqlparser.statement.select.PlainSelect;

import java.util.ArrayList;
import java.util.List;

public class JsonTableFunction extends Table {

    private Expression json;

    private StringValue path;

    private List<ColumnDefinition> columnDefinitions;

    private Alias alias = null;

    public Expression getJson() {
        return json;
    }

    public void setJson(Expression json) {
        this.json = json;
    }

    public JsonTableFunction withJson(Expression json) {
        this.json = json;
        return this;
    }

    public StringValue getPath() {
        return path;
    }

    public void setPath(StringValue path) {
        this.path = path;
    }

    public JsonTableFunction withPath(StringValue path) {
        this.path = path;
        return this;
    }

    public List<ColumnDefinition> getColumnDefinitions() {
        return columnDefinitions;
    }

    public void setColumnDefinitions(List<ColumnDefinition> columnDefinitions) {
        this.columnDefinitions = columnDefinitions;
    }

    public JsonTableFunction withColumnDefinitions(List<ColumnDefinition> columnDefinitions) {
        this.columnDefinitions = columnDefinitions;
        return this;
    }

    public JsonTableFunction withColumnDefinitions(ColumnDefinition columnDefinitions) {
        this.columnDefinitions = new ArrayList<>();
        this.columnDefinitions.add(columnDefinitions);
        return this;
    }

    public Alias getAlias() {
        return alias;
    }

    public void setAlias(Alias alias) {
        this.alias = alias;
    }

    public JsonTableFunction withAlias(Alias alias) {
        this.alias = alias;
        return this;
    }

    @Override
    public String toString() {
        return "JSON_TABLE(" +
                json + "," +
                path +
                " COLUMNS (" + PlainSelect.getStringList(columnDefinitions, true, false) + ")" +
                ") " + alias;
    }
}
