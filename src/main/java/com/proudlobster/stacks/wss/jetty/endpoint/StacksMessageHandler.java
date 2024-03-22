package com.proudlobster.stacks.wss.jetty.endpoint;

import java.util.Optional;

import com.proudlobster.stacks.Fallible;
import com.proudlobster.stacks.Stacks;
import jakarta.websocket.MessageHandler;

@FunctionalInterface
public interface StacksMessageHandler extends MessageHandler.Whole<String> {

    String DELIMITER = "\\|";

    public static StacksMessageHandler get(final Stacks stacks, final Long sessionId) {
        return msg -> Optional.of(msg)
                .map(m -> m.split(DELIMITER, 3))
                .filter(m -> m.length == 3)
                .filter(m -> m[0].equals(sessionId.toString()))
                .map(m -> (Object[]) m)
                .map(m -> stacks.$().createEntitiesFromTemplate("wss-inbound-message", m))
                .orElseThrow(Fallible.of("Message is badly formed or the session ID does not match: " + msg))
                .commit();

    }

}
