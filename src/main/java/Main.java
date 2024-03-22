import com.proudlobster.stacks.Stacks;
import com.proudlobster.stacks.wss.jetty.StacksJakartaServer;
import com.proudlobster.stacks.wss.jetty.WssMessageInProcessor;
import com.proudlobster.stacks.wss.jetty.WssMessageOutProcessor;
import com.proudlobster.stacks.wss.jetty.WssSessionProcessor;

public interface Main {
    public static void main(String[] args) throws Exception {
        final Stacks stacks = Stacks.create();
        final StacksJakartaServer server = StacksJakartaServer.get(stacks);
        stacks.$("wss-message-in-processor", WssMessageInProcessor.COMPONENT, WssMessageInProcessor.get());
        stacks.$("wss-message-out-processor", WssMessageOutProcessor.COMPONENT,
                WssMessageOutProcessor.get(server.sessionStorage()));
        stacks.$("wss-session-processor", WssSessionProcessor.COMPONENT, WssSessionProcessor.get());
        server.start();
        while (true) {
            stacks.runProcessors();
        }
    }
}