package com.proudlobster.stacks.wss.jetty;

import org.eclipse.jetty.ee10.servlet.ServletContextHandler;
import org.eclipse.jetty.ee10.websocket.jakarta.server.config.JakartaWebSocketServletContainerInitializer;
import org.eclipse.jetty.server.Server;

import com.proudlobster.stacks.Fallible;
import com.proudlobster.stacks.Stacks;
import com.proudlobster.stacks.structure.Configuration;
import com.proudlobster.stacks.wss.jetty.endpoint.SessionStorage;
import com.proudlobster.stacks.wss.jetty.endpoint.StacksJakartaEndpoint;

@FunctionalInterface
public interface StacksJakartaServer {

    public static StacksJakartaServer get(final Stacks stacks) {

        final Server serv = stacks.$(Configuration.class, "stacks.wss.port")
                .map(Configuration::value)
                .map(Integer::parseInt)
                .map(Server::new)
                .orElseGet(() -> new Server(8080));

        final ServletContextHandler handler = stacks.$(Configuration.class, "stacks.wss.contextPath")
                .map(Configuration::value)
                .map(ServletContextHandler::new)
                .orElseGet(() -> new ServletContextHandler("/"));

        final SessionStorage sessionStorage = SessionStorage.get();
        serv.setAttribute(SESSION_STORAGE_ATTR, sessionStorage);
        serv.setHandler(handler);

        JakartaWebSocketServletContainerInitializer.configure(handler,
                (ctx, srv) -> srv.addEndpoint(StacksJakartaEndpoint.getEndpointConfig(stacks, sessionStorage)));

        return () -> serv;
    }

    String SESSION_STORAGE_ATTR = "sessionStorage";

    Server server();

    default void start() {
        Fallible.attemptRun(server()::start);
    }

    default void stop() {
        Fallible.attemptRun(server()::stop);
    }

    default SessionStorage sessionStorage() {
        return SessionStorage.class.cast(server().getAttribute(SESSION_STORAGE_ATTR));
    }

    default void sendMessage(final Long sessionId, final String message) {
        sessionStorage().sendMessage(sessionId, message);
    }
}
