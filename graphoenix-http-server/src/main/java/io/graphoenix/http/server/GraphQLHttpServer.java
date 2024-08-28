package io.graphoenix.http.server;

import io.graphoenix.http.server.config.HttpServerConfig;
import io.graphoenix.http.server.context.RequestScopeInstanceFactory;
import io.graphoenix.http.server.handler.DownloadRequestHandler;
import io.graphoenix.http.server.handler.GetRequestHandler;
import io.graphoenix.http.server.handler.PostRequestHandler;
import io.graphoenix.spi.bootstrap.Runner;
import io.graphoenix.spi.constant.Hammurabi;
import io.netty.channel.ChannelOption;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.cors.CorsConfig;
import io.netty.handler.codec.http.cors.CorsConfigBuilder;
import io.netty.handler.codec.http.cors.CorsHandler;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import reactor.netty.ConnectionObserver;
import reactor.netty.DisposableServer;
import reactor.netty.http.HttpOperations;
import reactor.netty.http.server.HttpServer;

import static io.graphoenix.http.server.handler.DownloadRequestHandler.FILE_PARAM_ID;

@ApplicationScoped
@Named(Hammurabi.ENUM_PROTOCOL_ENUM_VALUE_HTTP)
public class GraphQLHttpServer implements Runner {

    private final HttpServerConfig httpServerConfig;
    private final GetRequestHandler getRequestHandler;
    private final PostRequestHandler postRequestHandler;
    private final DownloadRequestHandler downloadRequestHandler;
    private final RequestScopeInstanceFactory requestScopeInstanceFactory;

    @Inject
    public GraphQLHttpServer(HttpServerConfig httpServerConfig, GetRequestHandler getRequestHandler, PostRequestHandler postRequestHandler, DownloadRequestHandler downloadRequestHandler, RequestScopeInstanceFactory requestScopeInstanceFactory) {
        this.httpServerConfig = httpServerConfig;
        this.getRequestHandler = getRequestHandler;
        this.postRequestHandler = postRequestHandler;
        this.downloadRequestHandler = downloadRequestHandler;
        this.requestScopeInstanceFactory = requestScopeInstanceFactory;
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
                .route(httpServerRoutes ->
                        httpServerRoutes
                                .get(httpServerConfig.getGraphqlContextPath(), getRequestHandler::handle)
                                .post(httpServerConfig.getGraphqlContextPath(), postRequestHandler::handle)
                                .get(httpServerConfig.getDownloadContextPath() + "/{" + FILE_PARAM_ID + "}", downloadRequestHandler::handle)
                )
                .childObserve((connection, newState) -> {
                            if (connection instanceof HttpOperations<?, ?> && newState.equals(ConnectionObserver.State.DISCONNECTING)) {
                                String requestId = ((HttpOperations<?, ?>) connection).requestId();
                                requestScopeInstanceFactory.invalidate(requestId);
                            }
                        }
                )
                .port(httpServerConfig.getPort())
                .bindNow();

        server.onDispose().block();
    }
}
