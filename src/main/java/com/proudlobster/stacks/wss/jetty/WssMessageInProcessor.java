package com.proudlobster.stacks.wss.jetty;

import com.proudlobster.stacks.ecp.Component;
import com.proudlobster.stacks.ecp.ManagedProcessor.ManagedProcessorFunction;

@FunctionalInterface
public interface WssMessageInProcessor extends ManagedProcessorFunction {

    public static Component COMPONENT = WssComponent.WSS_MESSAGE_IN;

    public static WssMessageInProcessor get() {
        return e -> {
            e.stringValue(COMPONENT).ifPresent(System.out::println);
            return e.assignComponent(Component.Core.EXPIRED);
        };
    }

}
