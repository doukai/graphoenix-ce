package io.graphoenix.http.server.handler;

import org.tinylog.Logger;
import reactor.core.publisher.Mono;
import reactor.netty.http.server.HttpServerResponse;
import reactor.util.context.Context;
import reactor.util.context.ContextView;

import java.util.Map;

import static io.graphoenix.http.server.context.SessionScopeInstanceFactory.SESSION_ID;
import static io.graphoenix.http.server.error.HttpErrorStatusUtil.getStatus;
import static io.graphoenix.http.server.utils.ResponseUtil.error;
import static io.graphoenix.http.server.utils.ResponseUtil.next;

public abstract class BaseHandler {

    protected Mono<Void> sessionHandler(Map<String, Object> context, Mono<Void> mono, ContextView contextView) {
        return context.containsKey(SESSION_ID) ?
                mono.contextWrite(Context.of(SESSION_ID, context.get(SESSION_ID))) :
                mono;
    }

    protected Mono<String> errorHandler(Throwable throwable, HttpServerResponse response) {
        Logger.error(throwable);
        response.status(getStatus(throwable.getClass()));
        return Mono.just(error(throwable));
    }

    protected Mono<String> errorSSEHandler(Throwable throwable, HttpServerResponse response, String id) {
        Logger.error(throwable);
        return Mono.just(next(throwable, id));
    }
}
