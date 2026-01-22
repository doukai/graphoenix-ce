package io.graphoenix.http.server;

import io.graphoenix.http.server.config.HttpServerConfig;
import io.graphoenix.http.server.context.RequestBeanScoped;
import io.graphoenix.http.server.http.GetHandler;
import io.graphoenix.http.server.http.PostHandler;
import io.graphoenix.spi.bootstrap.Runner;
import io.graphoenix.spi.constant.Hammurabi;
import io.netty.channel.ChannelOption;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.cors.CorsConfig;
import io.netty.handler.codec.http.cors.CorsConfigBuilder;
import io.netty.handler.codec.http.cors.CorsHandler;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Instance;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import reactor.netty.ConnectionObserver;
import reactor.netty.DisposableServer;
import reactor.netty.http.HttpOperations;
import reactor.netty.http.server.HttpServer;

import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
@Named(Hammurabi.ENUM_PROTOCOL_ENUM_VALUE_HTTP)
public class GraphQLHttpServer implements Runner {

    private final HttpServerConfig httpServerConfig;
    private final List<GetHandler> getHandlerList;
    private final List<PostHandler> postHandlerList;
    private final RequestBeanScoped requestBeanScoped;

    @Inject
    public GraphQLHttpServer(
            HttpServerConfig httpServerConfig,
            Instance<GetHandler> getHandlerInstance,
            Instance<PostHandler> postHandlerInstance,
            RequestBeanScoped requestBeanScoped
    ) {
        this.httpServerConfig = httpServerConfig;
        this.getHandlerList = getHandlerInstance.stream().collect(Collectors.toList());
        this.postHandlerList = postHandlerInstance.stream().collect(Collectors.toList());
        this.requestBeanScoped = requestBeanScoped;
    }

    @Override
    public String protocol() {
        return Hammurabi.ENUM_PROTOCOL_ENUM_VALUE_HTTP;
    }

    @Override
    public int port() {
        return httpServerConfig.getPort();
    }

    @Override
    public void run() {
        CorsConfig corsConfig = CorsConfigBuilder.forAnyOrigin()
                .allowedRequestHeaders(HttpHeaderNames.CONTENT_TYPE)
                .allowedRequestHeaders(HttpHeaderNames.AUTHORIZATION)
                .allowedRequestMethods(HttpMethod.GET)
                .allowedRequestMethods(HttpMethod.POST)
                .build();

        DisposableServer server = HttpServer.create()
                .option(ChannelOption.SO_BACKLOG, httpServerConfig.getSoBackLog())
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, httpServerConfig.getConnectTimeOutMillis())
                .childOption(ChannelOption.TCP_NODELAY, httpServerConfig.getTcpNoDelay())
                .childOption(ChannelOption.SO_KEEPALIVE, httpServerConfig.getSoKeepAlive())
                .doOnConnection(connection -> connection.addHandlerLast(new CorsHandler(corsConfig)))
                .route(httpServerRoutes -> {
                    getHandlerList.forEach(getHandler -> httpServerRoutes.get(getHandler.path(), getHandler::handle));
                    postHandlerList.forEach(postHandler -> httpServerRoutes.post(postHandler.path(), postHandler::handle));
                })
                .childObserve((connection, newState) -> {
                    if (connection instanceof HttpOperations<?, ?> && newState.equals(ConnectionObserver.State.DISCONNECTING)) {
                        String requestId = ((HttpOperations<?, ?>) connection).requestId();
                        requestBeanScoped.destroy(requestId);
                    }
                })
                .port(httpServerConfig.getPort())
                .bindNow();

        server.onDispose().block();
    }
}
