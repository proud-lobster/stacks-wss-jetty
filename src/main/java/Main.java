import org.eclipse.jetty.ee10.servlet.ServletContextHandler;
import org.eclipse.jetty.ee10.websocket.jakarta.server.config.JakartaWebSocketServletContainerInitializer;
import org.eclipse.jetty.server.Server;

import com.proudlobster.stacks.Stacks;

public interface Main {
    public static void main(String[] args) throws Exception {
        Stacks.create();
        System.out.println("Stacks!!!");

        Server s = new Server(8080);
        ServletContextHandler handler = new ServletContextHandler("/ctx");
        s.setHandler(handler);

        JakartaWebSocketServletContainerInitializer.configure(handler,
                (ctx, srv) -> srv.addEndpoint(PrototypeEndpoint.class));

        s.start();
        System.out.println("Server running.");
    }
}