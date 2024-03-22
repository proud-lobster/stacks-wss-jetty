package com.proudlobster.stacks.wss.jetty.endpoint;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.proudlobster.stacks.Fallible;

import jakarta.websocket.Session;

@FunctionalInterface
public interface SessionStorage {

    public static SessionStorage get() {
        return () -> new ConcurrentHashMap<>();
    }

    Map<Long, Session> map();

    default void register(final Long sessionId, final Session session) {
        map().put(sessionId, session);
    }

    default void sendMessage(final Long sessionId, final String message) {
        Fallible.attemptRun(() -> map().get(sessionId).getBasicRemote().sendText(message));
    }
}
