package com.proudlobster.stacks.wss.jetty.endpoint;

import jakarta.websocket.Endpoint;
import jakarta.websocket.server.ServerEndpointConfig;

public class SingletonConfigurator extends ServerEndpointConfig.Configurator {

    private final Endpoint endpoint;

    public SingletonConfigurator(final Endpoint endpoint) {
        this.endpoint = endpoint;
    }

    @Override
    public <T> T getEndpointInstance(Class<T> endpointClass) throws InstantiationException {
        if (endpointClass.isInstance(endpoint)) {
            return endpointClass.cast(endpoint);
        } else {
            throw new InstantiationException("No endpoint for type " + endpointClass.getName());
        }
    }
}
