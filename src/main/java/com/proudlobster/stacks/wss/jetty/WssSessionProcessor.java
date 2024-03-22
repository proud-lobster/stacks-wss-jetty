package com.proudlobster.stacks.wss.jetty;

import java.util.Optional;

import com.proudlobster.stacks.ecp.Component;
import com.proudlobster.stacks.ecp.ManagedProcessor.ManagedProcessorFunction;

@FunctionalInterface
public interface WssSessionProcessor extends ManagedProcessorFunction {

    public static Component COMPONENT = WssComponent.WSS_SESSION;

    public static WssSessionProcessor get() {
        return e -> Optional.of(e)
                .filter(n -> n.is(WssComponent.WSS_NEW))
                .map(n -> n.removeComponent(WssComponent.WSS_NEW))
                .map(t -> t.andThen(e.$().createEntitiesFromTemplate("wss-outbound-message",
                        e.identifier() + "|SUCCESS|", e.identifier())))
                .orElseGet(() -> e.$());
    }

}
