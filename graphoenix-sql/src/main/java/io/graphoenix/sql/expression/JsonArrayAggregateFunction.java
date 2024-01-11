package io.graphoenix.sql.expression;

import java.util.List;

import net.sf.jsqlparser.expression.*;
import net.sf.jsqlparser.statement.select.Limit;
import net.sf.jsqlparser.statement.select.OrderByElement;

public class JsonArrayAggregateFunction extends Function implements Expression {

    private Expression expression = null;

    private final OrderByClause orderBy = new OrderByClause();

    private Limit limit = new Limit();

    public Expression getExpression() {
        return expression;
    }

    public void setExpression(Expression expression) {
        this.expression = expression;
    }

    public JsonArrayAggregateFunction withExpression(Expression expression) {
        this.setExpression(expression);
        return this;
    }

    public List<OrderByElement> getOrderByElements() {
        return orderBy.getOrderByElements();
    }

    public void setOrderByElements(List<OrderByElement> orderByElements) {
        orderBy.setOrderByElements(orderByElements);
    }

    public JsonArrayAggregateFunction withOrderByElements(List<OrderByElement> orderByElements) {
        this.setOrderByElements(orderByElements);
        return this;
    }

    public Limit getLimit() {
        return limit;
    }

    public void setLimit(Limit limit) {
        this.limit = limit;
    }

    public JsonArrayAggregateFunction withLimit(Limit limit) {
        this.setLimit(limit);
        return this;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("JSON_ARRAYAGG( ");
        builder.append(expression).append(" ");

        orderBy.toStringOrderByElements(builder);

        if (limit != null) {
            builder.append(limit);
        }

        builder.append(") ");
        return builder.toString();
    }
}
