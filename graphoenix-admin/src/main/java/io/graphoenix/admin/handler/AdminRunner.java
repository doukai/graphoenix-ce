package io.graphoenix.admin.handler;

import io.graphoenix.admin.config.AdminConfig;
import io.graphoenix.spi.bootstrap.Runner;
import io.javalin.Javalin;
import io.javalin.http.staticfiles.Location;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.jetty.proxy.AsyncProxyServlet;
import org.eclipse.jetty.servlet.ServletHolder;

@ApplicationScoped
public class AdminRunner implements Runner {

    private final AdminConfig adminConfig;

    @Inject
    public AdminRunner(AdminConfig adminConfig) {
        this.adminConfig = adminConfig;
    }

    @Override
    public void run() {
        Javalin app = Javalin.create(
                config -> {
                    config.staticFiles.add("/webroot", Location.CLASSPATH);
                    config.staticFiles.add("/webroot/static", Location.CLASSPATH);
                    config.spaRoot.addFile("/", "/webroot/index.html");
                    config.jetty.modifyServletContextHandler(handler -> {
                        ServletHolder proxyServlet = new ServletHolder(AsyncProxyServlet.Transparent.class);
                        proxyServlet.setInitParameter("proxyTo", adminConfig.getGraphQLPath());
                        proxyServlet.setInitParameter("prefix", "/graphql");
                        handler.addServlet(proxyServlet, "/graphql/*");
                    });
                }
        );

        app.start(port());
    }

    @Override
    public int port() {
        return adminConfig.getPort();
    }
}
