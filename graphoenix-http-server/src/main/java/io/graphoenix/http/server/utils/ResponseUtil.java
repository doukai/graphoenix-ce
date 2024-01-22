package io.graphoenix.http.server.utils;

import io.graphoenix.spi.error.GraphQLErrors;
import jakarta.json.JsonObjectBuilder;
import jakarta.json.JsonValue;
import jakarta.json.bind.Jsonb;
import jakarta.json.bind.spi.JsonbProvider;
import jakarta.json.spi.JsonProvider;
import org.eclipse.microprofile.graphql.GraphQLException;

import java.io.StringReader;
import java.io.StringWriter;

public final class ResponseUtil {

    private static final JsonProvider jsonProvider = JsonProvider.provider();

    private static final Jsonb jsonb = JsonbProvider.provider().create().build();

    public static String success(String jsonString) {
        return success(jsonString, null);
    }

    public static String success(String jsonString, String id) {
        JsonObjectBuilder responseBuilder = jsonProvider.createObjectBuilder();
        responseBuilder.add("data", jsonProvider.createReader(new StringReader(jsonString)).readValue());
        StringWriter stringWriter = new StringWriter();
        if (id != null) {
            jsonProvider.createWriter(stringWriter).write(jsonProvider.createObjectBuilder().add("id", id).add("payload", responseBuilder).build());
            return stringWriter.toString();
        }
        jsonProvider.createWriter(stringWriter).write(responseBuilder.build());
        return stringWriter.toString();
    }

    public static String success(JsonValue jsonValue) {
        return success(jsonValue, null);
    }

    public static String success(JsonValue jsonValue, String id) {
        JsonObjectBuilder responseBuilder = jsonProvider.createObjectBuilder();
        responseBuilder.add("data", jsonValue);
        StringWriter stringWriter = new StringWriter();
        if (id != null) {
            jsonProvider.createWriter(stringWriter).write(jsonProvider.createObjectBuilder().add("id", id).add("payload", responseBuilder).build());
            return stringWriter.toString();
        }
        jsonProvider.createWriter(stringWriter).write(responseBuilder.build());
        return stringWriter.toString();
    }

    public static String success(Object object) {
        return success(object, null);
    }

    public static String success(Object object, String id) {
        JsonObjectBuilder responseBuilder = jsonProvider.createObjectBuilder();
        responseBuilder.add("data", jsonProvider.createReader(new StringReader(jsonb.toJson(object))).read());
        StringWriter stringWriter = new StringWriter();
        if (id != null) {
            jsonProvider.createWriter(stringWriter).write(jsonProvider.createObjectBuilder().add("id", id).add("payload", responseBuilder).build());
            return stringWriter.toString();
        }
        jsonProvider.createWriter(stringWriter).write(responseBuilder.build());
        return stringWriter.toString();
    }

    public static String error(GraphQLErrors graphQLErrors) {
        return error(graphQLErrors, null);
    }

    public static String error(GraphQLErrors graphQLErrors, String id) {
        JsonObjectBuilder responseBuilder = jsonProvider.createObjectBuilder();
        if (graphQLErrors.getData() != null) {
            responseBuilder.add("data", jsonProvider.createReader(new StringReader(jsonb.toJson(graphQLErrors.getData()))).read());
        }
        responseBuilder.add("errors", jsonProvider.createReader(new StringReader(jsonb.toJson(graphQLErrors.getErrors()))).read());
        StringWriter stringWriter = new StringWriter();
        if (id != null) {
            jsonProvider.createWriter(stringWriter).write(jsonProvider.createObjectBuilder().add("id", id).add("payload", responseBuilder).build());
            return stringWriter.toString();
        }
        jsonProvider.createWriter(stringWriter).write(responseBuilder.build());
        return stringWriter.toString();
    }

    public static String error(GraphQLException graphQLException) {
        return error(graphQLException, null);
    }

    public static String error(GraphQLException graphQLException, String id) {
        return error(new GraphQLErrors(graphQLException), id);
    }

    public static String error(Throwable throwable) {
        return error(throwable, null);
    }

    public static String error(Throwable throwable, String id) {
        return error(new GraphQLErrors(throwable), id);
    }

    public static String next(JsonValue jsonValue) {
        return next(jsonValue, null);
    }

    public static String next(JsonValue jsonValue, String id) {
        if (id != null) {
            return "event: next\ndata: " + success(jsonValue, id) + "\n\n";
        } else {
            return "event: next\ndata: " + success(jsonValue) + "\n\n";
        }
    }

    public static String next(Throwable throwable) {
        return next(throwable, null);
    }

    public static String next(Throwable throwable, String id) {
        if (id != null) {
            return "event: next\ndata: " + error(throwable, id) + "\n\n";
        } else {
            return "event: next\ndata: " + error(throwable) + "\n\n";
        }
    }

    public static String complete() {
        return complete(null);
    }

    public static String complete(String id) {
        if (id != null) {
            JsonObjectBuilder data = jsonProvider.createObjectBuilder().add("id", id);
            StringWriter stringWriter = new StringWriter();
            jsonProvider.createWriter(stringWriter).write(data.build());
            return "event: complete\ndata: " + stringWriter + "\n\n";
        } else {
            return "event: complete\n\n";
        }
    }
}
