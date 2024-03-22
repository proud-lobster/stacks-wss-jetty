package com.proudlobster.stacks.wss.jetty;

import com.proudlobster.stacks.ecp.Component;
import com.proudlobster.stacks.ecp.ManagedProcessor.ManagedProcessorFunction;

public interface WssMessageProcessor extends ManagedProcessorFunction {

    public static Component COMPONENT = WssComponent.WSS_MESSAGE;

    public static WssMessageProcessor get() {
        return e -> {
            e.stringValue(COMPONENT).ifPresent(System.out::println);
            return e.removeComponent(WssComponent.WSS_NEW)
                    .andThen(e.assignComponent(Component.Core.EXPIRED));
        };
    }

}
