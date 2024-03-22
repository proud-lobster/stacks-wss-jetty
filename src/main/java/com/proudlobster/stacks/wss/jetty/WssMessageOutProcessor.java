package com.proudlobster.stacks.wss.jetty;

import com.proudlobster.stacks.ecp.Component;
import com.proudlobster.stacks.ecp.ManagedProcessor.ManagedProcessorFunction;
import com.proudlobster.stacks.wss.jetty.endpoint.SessionStorage;

@FunctionalInterface
public interface WssMessageOutProcessor extends ManagedProcessorFunction {

    public static Component COMPONENT = WssComponent.WSS_MESSAGE_OUT;

    public static WssMessageInProcessor get(final SessionStorage storage) {
        return e -> e.assignComponent(Component.Core.EXPIRED)
                .andThen(w -> storage.sendMessage(e));
    }

}
