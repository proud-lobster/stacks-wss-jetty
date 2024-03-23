package com.proudlobster.stacks.wss.jetty.endpoint;

import java.util.Optional;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import com.proudlobster.stacks.Fallible;
import com.proudlobster.stacks.Stacks;
import com.proudlobster.stacks.ecp.Entity;
import com.proudlobster.stacks.structure.Triple;
import com.proudlobster.stacks.wss.jetty.WssComponent;

import jakarta.websocket.MessageHandler;

@FunctionalInterface
public interface StacksMessageHandler extends MessageHandler.Whole<String> {

    @FunctionalInterface
    public static interface Message {

        public static Message of(final Long sessionId, final String command, final String payload) {
            return () -> Triple.of(sessionId.toString(), command, payload);
        }

        public static Message of(final String m) {
            return () -> Optional.of(m)
                    .map(s -> s.split(Pattern.quote(DELIMITER), 3))
                    .filter(s -> s.length == 3)
                    .map(s -> Triple.of(s[0], s[1], s[2]))
                    .orElseThrow(Fallible.of("Message is badly formed: " + m));
        }

        public static Message of(final Entity e) {
            return Message.of(e.longValue(WssComponent.WSS_SESSION_REF).get(),
                    e.stringValue(WssComponent.WSS_COMMAND).get(), e.stringValue(WssComponent.WSS_PAYLOAD).get());
        }

        Triple<String> tuple();

        default Long sessionId() {
            return Long.parseLong(tuple().first());
        }

        default String command() {
            return tuple().second();
        }

        default String payload() {
            return tuple().third();
        }

        default String string() {
            return tuple().stream().collect(Collectors.joining(DELIMITER));
        }
    }

    String DELIMITER = "|";

    public static StacksMessageHandler get(final Stacks stacks, final Long sessionId) {
        return msg -> Optional.of(msg)
                .map(m -> Message.of(m))
                .filter(m -> m.sessionId().equals(sessionId))
                .map(m -> stacks.$().createEntitiesFromTemplate("wss-inbound-message", sessionId.toString(), msg,
                        m.command(), m.payload()))
                .orElseThrow(Fallible.of("Message is badly formed or the session ID does not match: " + msg))
                .commit();

    }

}
