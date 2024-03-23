import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.proudlobster.stacks.Stacks;
import com.proudlobster.stacks.ecp.ManagedProcessor.ManagedProcessorFunction;
import com.proudlobster.stacks.wss.jetty.StacksJakartaServer;
import com.proudlobster.stacks.wss.jetty.WssMessageInProcessor;
import com.proudlobster.stacks.wss.jetty.WssMessageOutProcessor;
import com.proudlobster.stacks.wss.jetty.WssSessionProcessor;
import com.proudlobster.stacks.wss.jetty.endpoint.StacksMessageHandler.Message;

public interface Main {
    public static void main(String[] args) throws Exception {
        final Stacks stacks = Stacks.create();
        final StacksJakartaServer server = StacksJakartaServer.get(stacks);
        final Map<String, ManagedProcessorFunction> commandSubprocessors = new ConcurrentHashMap<>();
        commandSubprocessors.put("ECHO", e -> e.$().createEntitiesFromTemplate("wss-outbound-message",
                Message.of(e).string(), Message.of(e).sessionId()));
        stacks.$("wss-message-in-processor", WssMessageInProcessor.COMPONENT,
                WssMessageInProcessor.get(commandSubprocessors));
        stacks.$("wss-message-out-processor", WssMessageOutProcessor.COMPONENT,
                WssMessageOutProcessor.get(server.sessionStorage()));
        stacks.$("wss-session-processor", WssSessionProcessor.COMPONENT, WssSessionProcessor.get());
        server.start();
        while (true) {
            stacks.runProcessors();
        }
    }
}