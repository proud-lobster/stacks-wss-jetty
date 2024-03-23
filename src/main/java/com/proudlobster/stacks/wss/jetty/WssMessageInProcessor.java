package com.proudlobster.stacks.wss.jetty;

import java.util.Map;

import com.proudlobster.stacks.ecp.Component;
import com.proudlobster.stacks.ecp.ManagedProcessor.ManagedProcessorFunction;

@FunctionalInterface
public interface WssMessageInProcessor extends ManagedProcessorFunction {

    public static Component COMPONENT = WssComponent.WSS_MESSAGE_IN;

    public static WssMessageInProcessor get(final Map<String, ManagedProcessorFunction> commandSubprocessors) {
        return e -> {
            // TODO unknown commands
            return commandSubprocessors.get(e.stringValue(WssComponent.WSS_COMMAND).get()).apply(e)
                    .andThen(e.assignComponent(Component.Core.EXPIRED));
        };
    }

}
