package io.graphoenix.r2dbc.handler;

import io.r2dbc.spi.Statement;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.*;

@ApplicationScoped
public class ParameterBinder {

    public static List<String> parse(String query) {
        List<String> orderedParameters = new ArrayList<>();
        int length = query.length();
        boolean inSingleQuote = false;
        boolean inDoubleQuote = false;
        boolean inSingleLineComment = false;
        boolean inMultiLineComment = false;
        boolean inDoubleColon = false;

        for (int i = 0; i < length; i++) {
            char c = query.charAt(i);
            if (inSingleQuote) {
                if (c == '\'') {
                    inSingleQuote = false;
                }
            } else if (inDoubleQuote) {
                if (c == '"') {
                    inDoubleQuote = false;
                }
            } else if (inMultiLineComment) {
                if (c == '*' && query.charAt(i + 1) == '/') {
                    inMultiLineComment = false;
                }
            } else if (inDoubleColon) {
                if (!Character.isJavaIdentifierPart(c)) {
                    inDoubleColon = false;
                }
            } else if (inSingleLineComment) {
                if (c == '\n') {
                    inSingleLineComment = false;
                }
            } else {
                if (c == '\'') {
                    inSingleQuote = true;
                } else if (c == '"') {
                    inDoubleQuote = true;
                } else if (c == '/' && query.charAt(i + 1) == '*') {
                    inMultiLineComment = true;
                } else if (c == '-' && query.charAt(i + 1) == '-') {
                    inSingleLineComment = true;
                } else if (c == ':' && query.charAt(i + 1) == ':') {
                    inDoubleColon = true;
                } else if (c == ':' && i + 1 < length && Character.isJavaIdentifierStart(query.charAt(i + 1))) {
                    int j = i + 2;
                    while (j < length && Character.isJavaIdentifierPart(query.charAt(j))) {
                        j++;
                    }
                    String name = query.substring(i + 1, j);
                    orderedParameters.add(name);
                    i += name.length(); // skip past the end if the parameter
                }
            }
        }
        return orderedParameters;
    }

    public void bindParameters(String sql, Statement statement, Map<String, Object> parameters) {
        if (parameters == null) {
            return;
        }
        List<String> orderedParameters = parse(sql);
        for (Map.Entry<String, Object> entry : parameters.entrySet()) {
            Collection<Integer> indexes = new ArrayList<>();
            for (int i = 0; i < orderedParameters.size(); i++) {
                if (orderedParameters.get(i).equals(entry.getKey())) {
                    indexes.add(i);
                }
            }
            if (!indexes.isEmpty()) {
                for (int index : indexes) {
                    statement.bind(index, entry.getValue());
                }
            }
        }
    }
}
