package com.proudlobster.stacks.wss.jetty.endpoint;

import com.proudlobster.stacks.Stacks;
import com.proudlobster.stacks.structure.Configuration;

import jakarta.websocket.Endpoint;
import jakarta.websocket.EndpointConfig;
import jakarta.websocket.Session;
import jakarta.websocket.server.ServerEndpointConfig;

public class StacksJakartaEndpoint extends Endpoint {

    public static ServerEndpointConfig getEndpointConfig(final Stacks stacks, final SessionStorage sessionStorage) {
        final Endpoint endpoint = new StacksJakartaEndpoint(stacks, sessionStorage);
        final String endpointPath = stacks.$(Configuration.class, "stacks.wss.endpointPath")
                .map(Configuration::value)
                .orElse("/wss");

        return ServerEndpointConfig.Builder
                .create(StacksJakartaEndpoint.class, endpointPath)
                .configurator(new SingletonConfigurator(endpoint))
                .build();
    }

    final Stacks stacks;
    final SessionStorage sessionStorage;

    public StacksJakartaEndpoint(final Stacks stacks, final SessionStorage sessionStorage) {
        this.stacks = stacks;
        this.sessionStorage = sessionStorage;
    }

    @Override
    public void onOpen(Session session, EndpointConfig config) {
        final Long sessionId = stacks.nextId();

        // Create entity for session.
        stacks.$().createEntitiesFromTemplate("wss-new-session", sessionId).commit();

        // Store session.
        sessionStorage.register(sessionId, session);

        // Assign message handler.
        session.addMessageHandler(StacksMessageHandler.get(stacks, sessionId));
    }

}
