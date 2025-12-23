package io.graphoenix.http.server.handler;

import io.graphoenix.http.server.error.HttpErrorStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Mono;
import reactor.netty.http.server.HttpServerResponse;
import reactor.util.context.Context;
import reactor.util.context.ContextView;

import java.util.Map;

import static io.graphoenix.http.server.context.SessionScopeInstanceFactory.SESSION_ID;
import static io.graphoenix.http.server.utils.ResponseUtil.error;
import static io.graphoenix.http.server.utils.ResponseUtil.next;

public abstract class BaseHandler {

    private static final Logger logger = LoggerFactory.getLogger(BaseHandler.class);

    public static final String REQUEST = "request";
    public static final String RESPONSE = "response";
    public static final String OPERATION = "operation";

    protected Mono<Void> sessionHandler(Map<String, Object> context, Mono<Void> mono, ContextView contextView) {
        return context.containsKey(SESSION_ID) ?
                mono.contextWrite(Context.of(SESSION_ID, context.get(SESSION_ID))) :
                mono;
    }

    protected Mono<String> errorHandler(Throwable throwable, HttpServerResponse response) {
        logger.error(throwable.getMessage(), throwable);
        response.status(HttpErrorStatus.getStatus(throwable.getClass()));
        return Mono.just(error(throwable));
    }

    protected Mono<String> errorSSEHandler(Throwable throwable, HttpServerResponse response, String id) {
        logger.error(throwable.getMessage(), throwable);
        return Mono.just(next(throwable, id));
    }
}
