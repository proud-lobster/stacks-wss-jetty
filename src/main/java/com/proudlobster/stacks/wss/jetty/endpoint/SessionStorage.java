package com.proudlobster.stacks.wss.jetty.endpoint;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.proudlobster.stacks.Fallible;
import com.proudlobster.stacks.ecp.ManagedEntity;
import com.proudlobster.stacks.wss.jetty.WssComponent;

import jakarta.websocket.Session;

@FunctionalInterface
public interface SessionStorage {

    public static SessionStorage get() {
        final Map<Long, Session> m = new ConcurrentHashMap<>();
        return () -> m;
    }

    Map<Long, Session> map();

    default void register(final Long sessionId, final Session session) {
        map().put(sessionId, session);
    }

    default void sendMessage(final Long sessionId, final String message) {
        Fallible.attemptRun(() -> map().get(sessionId).getBasicRemote().sendText(message));
    }

    default void sendMessage(final ManagedEntity message) {
        sendMessage(message.referenceEntity(WssComponent.WSS_SESSION_REF).get().identifier(),
                message.stringValue(WssComponent.WSS_MESSAGE_OUT).get());
    }

}
