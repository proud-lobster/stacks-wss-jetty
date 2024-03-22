import com.proudlobster.stacks.Stacks;
import com.proudlobster.stacks.wss.jetty.StacksJakartaServer;
import com.proudlobster.stacks.wss.jetty.WssMessageProcessor;

public interface Main {
    public static void main(String[] args) throws Exception {
        final Stacks stacks = Stacks.create();
        final StacksJakartaServer server = StacksJakartaServer.get(stacks);
        stacks.$("wss-managed-processor", WssMessageProcessor.COMPONENT, WssMessageProcessor.get());
        server.start();
        while (true) {
            stacks.runProcessors();
        }
    }
}