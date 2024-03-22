package com.proudlobster.stacks.wss.jetty.endpoint;

import com.proudlobster.stacks.Stacks;

import jakarta.websocket.MessageHandler;

@FunctionalInterface
public interface StacksMessageHandler extends MessageHandler.Whole<String> {

    public static StacksMessageHandler get(final Stacks stacks, final Long sessionId) {
        return m -> stacks.$()
                .createEntitiesFromTemplate("wss-incoming-message", m, sessionId)
                .commit();
    }

}
